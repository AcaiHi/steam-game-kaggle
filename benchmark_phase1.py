"""
Phase 1 分群演算法 Benchmark
用法：python benchmark_phase1.py [--iterations 500] [--population 20] [--n_colors 8]
         [--fitness plain|weighted]
"""
import argparse
import io
import sys
import time
import contextlib

import yaml
import numpy as np

from src.data import load_raw, load_phase1_features, filter_phase1_samples
from src.phase1.dimensions import build_dimensions
from src.phase1.objective import assess_partition
from src.phase1 import metaheuristic

# ── 指標計算（M 統一用 centroid fitness 算，KMeans 也算）
from src.phase1.metaheuristic.centroid_fitness import make_fitness, decode_labels
from src.phase1.metaheuristic.weighted_fitness import (
    compute_density_weights,
    compute_M_weighted,
)

BASE_CONFIG = "configs/assign_base.yaml"

METHODS = [
    "kmeans",
    "kmeans++",
    "pso",
    "pso_cold",
    "sma",
    "hho",
    "vigpso",
    "vigpso_cold",
    "avicpso",
]

# 不輸出迭代 log 的方法（有 verbose print）
SILENT_METHODS = {"hho", "vigpso", "vigpso_cold", "avicpso", "sma"}


def compute_M(labels: np.ndarray, values: np.ndarray) -> float:
    """Plain M：所有點到（unweighted）群心的歐氏距離總和。"""
    unique = np.unique(labels)
    total = 0.0
    for k in unique:
        mask = labels == k
        if mask.sum() == 0:
            continue
        centroid = values[mask].mean(axis=0)
        total += np.linalg.norm(values[mask] - centroid, axis=1).sum()
    return float(total)


def run_method(name: str, values: np.ndarray, cfg: dict):
    fn = metaheuristic.get(name)
    if name in SILENT_METHODS:
        with contextlib.redirect_stdout(io.StringIO()):
            labels, obj = fn(values, cfg)
    else:
        labels, obj = fn(values, cfg)

    # plain fitness 時 kmeans obj 是 inter-intra 分數，與其他方法不同單位
    fit_type = cfg.get("fitness", {}).get("type", "plain")
    if name in ("kmeans", "kmeans++") and fit_type == "plain":
        obj = None  # evaluate_partition 與其他方法 obj 不同單位

    return labels, obj


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--iterations", type=int, default=500)
    parser.add_argument("--population", type=int, default=20)
    parser.add_argument("--n_colors", type=int, default=8)
    parser.add_argument("--config", default=BASE_CONFIG)
    parser.add_argument("--methods", nargs="+", default=None,
                        help="只跑指定方法，例如 --methods kmeans pso vigpso avicpso")
    parser.add_argument("--seed", type=int, default=None)
    parser.add_argument(
        "--fitness",
        choices=["plain", "combined", "weighted", "wcss", "wdbi", "wcombined"],
        default=None,
        help="覆蓋 config 中的 fitness.type",
    )
    parser.add_argument("--intra_weight", type=float, default=None,
                        help="覆蓋 config 中的 objective.intra_weight")
    parser.add_argument("--inter_weight", type=float, default=None,
                        help="覆蓋 config 中的 objective.inter_weight")
    args = parser.parse_args()

    with open(args.config, encoding="utf-8") as f:
        base_cfg = yaml.safe_load(f)

    base_cfg["iterations"] = args.iterations
    base_cfg["population"] = args.population
    base_cfg["n_colors"]   = args.n_colors
    if args.seed is not None:
        base_cfg["seed"] = args.seed
    if args.fitness is not None:
        base_cfg.setdefault("fitness", {})["type"] = args.fitness
    if args.intra_weight is not None:
        base_cfg.setdefault("objective", {})["intra_weight"] = args.intra_weight
    if args.inter_weight is not None:
        base_cfg.setdefault("objective", {})["inter_weight"] = args.inter_weight

    df = load_raw()
    df = filter_phase1_samples(df, base_cfg.get("sample_filter"))
    phase1_df = load_phase1_features(df)
    dims = build_dimensions(phase1_df, base_cfg)
    dim_cols = [c for c in dims.columns if c != "appid"]
    values = dims[dim_cols].values

    # 預先計算 density weights 一次，供所有方法共用（避免每個 method 各算一遍）
    weights = compute_density_weights(values, base_cfg)
    base_cfg["_weights_cache"] = weights   # make_fitness_fn 會優先讀此快取

    fitness_type = base_cfg.get("fitness", {}).get("type", "plain")
    w_intra = base_cfg.get("objective", {}).get("intra_weight", 1.0)
    w_inter = base_cfg.get("objective", {}).get("inter_weight", 1.0)
    print(f"\n{'='*72}")
    print(f"  Phase 1 Benchmark  |  iterations={args.iterations}  population={args.population}  n_colors={args.n_colors}")
    print(f"  Samples: {len(values):,}  |  Dimensions: {dim_cols}")
    print(f"  Fitness: {fitness_type}  |  intra_weight={w_intra}  inter_weight={w_inter}")
    print(f"{'='*72}")

    methods = args.methods if args.methods else METHODS

    results = []
    for name in methods:
        cfg = {**base_cfg, "algorithm": name}
        t0 = time.time()
        labels, obj = run_method(name, values, cfg)
        elapsed = time.time() - t0

        metrics  = assess_partition(labels, values, weights)
        M        = compute_M(labels, values)
        M_w      = compute_M_weighted(labels, values, weights)
        wdbi     = metrics["weighted_davies_bouldin"]

        results.append({
            "method":    name,
            "sil":       metrics["silhouette"],
            "dbi":       metrics["davies_bouldin"],
            "wdbi":      wdbi,
            "M":         M,
            "M_w":       M_w,
            "obj":       obj,
            "n_active":  metrics["n_active_clusters"],
            "min_cl":    min(metrics["cluster_dist"].values()),
            "max_cl":    max(metrics["cluster_dist"].values()),
            "time_s":    elapsed,
            "labels":    labels,
        })

        tag_w    = "[WARN]" if metrics["balance_warning"] else ""
        obj_str  = f"{obj:.1f}" if obj is not None else "  —  "
        wdbi_str = f"{wdbi:.4f}" if wdbi is not None else "  —  "
        print(
            f"  {name:<10}  sil={metrics['silhouette']:.4f}  dbi={metrics['davies_bouldin']:.4f}"
            f"  wdbi={wdbi_str}  M={M:,.0f}  M_w={M_w:,.2f}"
            f"  obj={obj_str}  t={elapsed:.1f}s  {tag_w}"
        )

        # ── 儲存 labels CSV
        labels_df = (
            dims[["appid"] + dim_cols]
            .copy()
            .assign(cluster_id=labels)
            .merge(df[["appid", "name"]], on="appid")
        )[["appid", "cluster_id", "name"] + dim_cols]
        out_path = f"outputs/labels_{name}.csv"
        labels_df.to_csv(out_path, index=False)
        print(f"  → saved {out_path}")

    # ── 排名表
    def _wdbi(r):
        return r["wdbi"] if r["wdbi"] is not None else float("inf")

    print(f"\n{'─'*72}")
    print("  Ranking (Silhouette ↑):")
    for r in sorted(results, key=lambda x: -x["sil"]):
        print(f"    {r['method']:<10}  sil={r['sil']:.4f}  dbi={r['dbi']:.4f}  wdbi={_wdbi(r):.4f}  M={r['M']:,.0f}  M_w={r['M_w']:,.2f}")

    print(f"\n  Ranking (DBI ↓):")
    for r in sorted(results, key=lambda x: x["dbi"]):
        print(f"    {r['method']:<10}  dbi={r['dbi']:.4f}  wdbi={_wdbi(r):.4f}  sil={r['sil']:.4f}  M={r['M']:,.0f}  M_w={r['M_w']:,.2f}")

    print(f"\n  Ranking (WDBI ↓):")
    for r in sorted(results, key=_wdbi):
        print(f"    {r['method']:<10}  wdbi={_wdbi(r):.4f}  dbi={r['dbi']:.4f}  sil={r['sil']:.4f}  M_w={r['M_w']:,.2f}")

    print(f"\n  Ranking (M ↓):")
    for r in sorted(results, key=lambda x: x["M"]):
        print(f"    {r['method']:<10}  M={r['M']:,.0f}  sil={r['sil']:.4f}  dbi={r['dbi']:.4f}  wdbi={_wdbi(r):.4f}")

    print(f"\n  Ranking (M_w ↓):")
    for r in sorted(results, key=lambda x: x["M_w"]):
        print(f"    {r['method']:<10}  M_w={r['M_w']:,.2f}  sil={r['sil']:.4f}  wdbi={_wdbi(r):.4f}  dbi={r['dbi']:.4f}")

    print(f"{'='*72}\n")


if __name__ == "__main__":
    main()
