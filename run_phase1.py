"""
Phase 1 執行入口
用法：python run_phase1.py --config configs/phase1.yaml
"""
import argparse
import yaml
import mlflow
import pandas as pd
from functools import partial

from src.data import load_raw, load_phase1_features
from src.phase1.dimensions import build_dimensions
from src.phase1.objective import evaluate, assess
from src.phase1.clustering import make_labels
from src.phase1.adaptive import make_perturb_fn
from src.phase1 import metaheuristic


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--config", default="configs/phase1.yaml")
    args = parser.parse_args()

    with open(args.config, encoding="utf-8") as f:
        cfg = yaml.safe_load(f)

    df = load_raw()
    phase1_df = load_phase1_features(df)
    dims = build_dimensions(phase1_df, cfg)

    objective_fn = partial(evaluate, dims=dims, cfg=cfg)
    use_adaptive = cfg.get("adaptive", {}).get("enabled", False)
    perturb_fn = make_perturb_fn(dims, cfg) if use_adaptive else None
    algorithm = metaheuristic.get(cfg["algorithm"])
    n_dims = len(cfg["dimensions"])

    algo = cfg["algorithm"]
    run_name = f"{algo}_aps" if use_adaptive else algo

    mlflow.set_tracking_uri(cfg["mlflow"]["tracking_uri"])
    mlflow.set_experiment(cfg["mlflow"]["experiment_name"])
    with mlflow.start_run(run_name=run_name):
        mlflow.log_params({
            "algorithm": cfg["algorithm"],
            "iterations": cfg["iterations"],
            "population": cfg.get("population", "-"),
            "dimensions": list(cfg["dimensions"].keys()),
            "adaptive": use_adaptive,
        })

        thresholds, score = algorithm(objective_fn, n_dims, cfg, perturb_fn=perturb_fn)

        mlflow.log_metrics({f"threshold_{d}": t for d, t in zip(cfg["dimensions"], thresholds)})
        mlflow.log_metric("objective_score", score)

        # 正式評估指標
        metrics = assess(thresholds, dims)
        mlflow.log_metric("silhouette", metrics["silhouette"])
        mlflow.log_metric("davies_bouldin", metrics["davies_bouldin"])
        mlflow.log_metric("n_active_clusters", metrics["n_active_clusters"])
        mlflow.log_metric("dominant_cluster_pct", metrics["dominant_cluster_pct"])
        for cluster_id, count in metrics["cluster_dist"].items():
            mlflow.log_metric(f"n_cluster_{cluster_id}", count)

        labels_df = make_labels(thresholds, dims)
        labels_df = labels_df.merge(df[["appid", "name"]], on="appid")

        out_path = f"outputs/labels_{run_name}.csv"
        labels_df.to_csv(out_path, index=False)
        mlflow.log_artifact(out_path)

        print(f"[Phase 1] algorithm={cfg['algorithm']}  obj={score:.4f}")
        print(f"Thresholds : {dict(zip(cfg['dimensions'], thresholds))}")
        print(f"Silhouette : {metrics['silhouette']:.4f}")
        print(f"DBI        : {metrics['davies_bouldin']:.4f}")
        if metrics["balance_warning"]:
            print(f"[WARNING] Dominant cluster > 50% ({metrics['dominant_cluster_pct']:.1%})")
        print(f"Cluster dist: {metrics['cluster_dist']}")
        print(f"Labels saved to {out_path}")


if __name__ == "__main__":
    main()
