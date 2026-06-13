"""
Phase 1 執行入口
用法：python run_phase1.py --config configs/phase1.yaml
"""
import argparse
import yaml
import mlflow
import pandas as pd
from functools import partial

from src.data import load_raw, load_phase1_features, filter_phase1_samples
from src.phase1.dimensions import build_dimensions
from src.phase1.objective import evaluate, assess, evaluate_partition, assess_partition
from src.phase1.clustering import make_labels, make_labels_from_ids
from src.phase1.adaptive import make_perturb_fn
from src.phase1.assignment import run as run_assignment
from src.phase1 import metaheuristic


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--config", default="configs/phase1.yaml")
    args = parser.parse_args()

    with open(args.config, encoding="utf-8") as f:
        cfg = yaml.safe_load(f)

    df = load_raw()
    raw_n = len(df)
    df = filter_phase1_samples(df, cfg.get("sample_filter"))
    filtered_n = len(df)
    phase1_df = load_phase1_features(df)
    dims = build_dimensions(phase1_df, cfg)

    representation = cfg.get("representation", "threshold")
    use_adaptive = cfg.get("adaptive", {}).get("enabled", False)
    algo = cfg["algorithm"]
    default_run_name = f"{algo}_aps" if use_adaptive else algo
    run_name = cfg.get("run_name", default_run_name)

    mlflow.set_tracking_uri(cfg["mlflow"]["tracking_uri"])
    mlflow.set_experiment(cfg["mlflow"]["experiment_name"])
    with mlflow.start_run(run_name=run_name):
        mlflow.log_params({
            "algorithm": cfg["algorithm"],
            "iterations": cfg["iterations"],
            "population": cfg.get("population", "-"),
            "dimensions": list(cfg["dimensions"].keys()),
            "adaptive": use_adaptive,
            "sample_filter": cfg.get("sample_filter", {}).get("mode", "none"),
            "n_samples_raw": raw_n,
            "n_samples_used": filtered_n,
        })

        if representation == "assignment":
            dim_cols = [c for c in dims.columns if c != "appid"]
            values = dims[dim_cols].values
            labels, score = run_assignment(values, cfg)
            mlflow.log_metric("objective_score", score)
            metrics = assess_partition(labels, values)
            labels_df = make_labels_from_ids(labels, dims)
            mlflow.log_metric("n_colors", int(len(set(labels.tolist()))))
        else:
            objective_fn = partial(evaluate, dims=dims, cfg=cfg)
            perturb_fn = make_perturb_fn(dims, cfg) if use_adaptive else None
            algorithm = metaheuristic.get(cfg["algorithm"])
            n_dims = len(cfg["dimensions"])
            thresholds, score = algorithm(objective_fn, n_dims, cfg, perturb_fn=perturb_fn)
            mlflow.log_metrics({f"threshold_{d}": t for d, t in zip(cfg["dimensions"], thresholds)})
            mlflow.log_metric("objective_score", score)
            metrics = assess(thresholds, dims)
            labels_df = make_labels(thresholds, dims)

        mlflow.log_metric("silhouette", metrics["silhouette"])
        mlflow.log_metric("davies_bouldin", metrics["davies_bouldin"])
        mlflow.log_metric("n_active_clusters", metrics["n_active_clusters"])
        mlflow.log_metric("dominant_cluster_pct", metrics["dominant_cluster_pct"])
        for cluster_id, count in metrics["cluster_dist"].items():
            mlflow.log_metric(f"n_cluster_{cluster_id}", count)
        labels_df = labels_df.merge(df[["appid", "name"]], on="appid")

        out_path = f"outputs/labels_{run_name}.csv"
        labels_df.to_csv(out_path, index=False)
        mlflow.log_artifact(out_path)

        print(f"[Phase 1] algorithm={cfg['algorithm']}  obj={score:.4f}")
        print(f"Samples    : {filtered_n}/{raw_n}")
        if representation == "threshold":
            print(f"Thresholds : {dict(zip(cfg['dimensions'], thresholds))}")
        else:
            print(f"Representation: {representation}")
        print(f"Silhouette : {metrics['silhouette']:.4f}")
        print(f"DBI        : {metrics['davies_bouldin']:.4f}")
        if metrics["balance_warning"]:
            print(f"[WARNING] Dominant cluster > 50% ({metrics['dominant_cluster_pct']:.1%})")
        print(f"Cluster dist: {metrics['cluster_dist']}")
        print(f"Labels saved to {out_path}")


if __name__ == "__main__":
    main()
