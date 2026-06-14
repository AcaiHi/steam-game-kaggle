"""
Phase 1 分群演算法 Benchmark
用法：python benchmark_phase1.py [--iterations 500] [--population 20] [--n_colors 8]
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

BASE_CONFIG = "configs/assign_base.yaml"

METHODS = [
    "kmeans",
    "kmeans++",
    "pso",
    "sma",
    "hho",
    "vigpso",
    "avicpso",
]

# 不輸出迭代 log 的方法（有 verbose print）
SILENT_METHODS = {"hho", "vigpso", "avicpso", "sma"}


def compute_M(labels: np.ndarray, values: np.ndarray) -> float:
    """論文 M：所有點到群心的歐氏距離總和（統一可比指標）。"""
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

    # KMeans 的 obj 是 inter-intra 分數，統一換算成 M
    if name in ("kmeans", "kmeans++"):
        obj = None   # inter-intra 與 M 不同單位，不比較

    return labels, obj


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--iterations", type=int, default=500)
    parser.add_argument("--population", type=int, default=20)
    parser.add_argument("--n_colors", type=int, default=8)
    parser.add_argument("--config", default=BASE_CONFIG)
    args = parser.parse_args()

    with open(args.config, encoding="utf-8") as f:
        base_cfg = yaml.safe_load(f)

    base_cfg["iterations"] = args.iterations
    base_cfg["population"] = args.population
    base_cfg["n_colors"]   = args.n_colors

    df = load_raw()
    df = filter_phase1_samples(df, base_cfg.get("sample_filter"))
    phase1_df = load_phase1_features(df)
    dims = build_dimensions(phase1_df, base_cfg)
    dim_cols = [c for c in dims.columns if c != "appid"]
    values = dims[dim_cols].values

    print(f"\n{'='*68}")
    print(f"  Phase 1 Benchmark  |  iterations={args.iterations}  population={args.population}  n_colors={args.n_colors}")
    print(f"  Samples: {len(values):,}  |  Dimensions: {dim_cols}")
    print(f"{'='*68}")

    results = []
    for name in METHODS:
        cfg = {**base_cfg, "algorithm": name}
        t0 = time.time()
        labels, obj = run_method(name, values, cfg)
        elapsed = time.time() - t0

        metrics = assess_partition(labels, values)
        M = compute_M(labels, values)

        results.append({
            "method":    name,
            "sil":       metrics["silhouette"],
            "dbi":       metrics["davies_bouldin"],
            "M":         M,
            "obj":       obj,
            "n_active":  metrics["n_active_clusters"],
            "min_cl":    min(metrics["cluster_dist"].values()),
            "max_cl":    max(metrics["cluster_dist"].values()),
            "time_s":    elapsed,
        })

        tag_w = "[WARN]" if metrics["balance_warning"] else ""
        obj_str = f"{obj:.1f}" if obj is not None else "  —  "
        print(f"  {name:<10}  sil={metrics['silhouette']:.4f}  dbi={metrics['davies_bouldin']:.4f}"
              f"  M={M:,.0f}  obj={obj_str}  t={elapsed:.1f}s  {tag_w}")

    # ── 排名表
    print(f"\n{'─'*68}")
    print("  Ranking (Silhouette ↑):")
    for r in sorted(results, key=lambda x: -x["sil"]):
        print(f"    {r['method']:<10}  sil={r['sil']:.4f}  dbi={r['dbi']:.4f}  M={r['M']:,.0f}")

    print(f"\n  Ranking (DBI ↓):")
    for r in sorted(results, key=lambda x: x["dbi"]):
        print(f"    {r['method']:<10}  dbi={r['dbi']:.4f}  sil={r['sil']:.4f}  M={r['M']:,.0f}")

    print(f"\n  Ranking (M ↓):")
    for r in sorted(results, key=lambda x: x["M"]):
        print(f"    {r['method']:<10}  M={r['M']:,.0f}  sil={r['sil']:.4f}  dbi={r['dbi']:.4f}")

    print(f"{'='*68}\n")


if __name__ == "__main__":
    main()
