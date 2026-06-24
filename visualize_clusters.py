"""
Feature Distribution per Cluster 視覺化

模式一：重新跑分群演算法
  python visualize_clusters.py --config configs/assign_avicpso.yaml

模式二：直接讀取已有的 labels CSV（不需 --config）
  python visualize_clusters.py --labels-csv outputs/labels_avicpso.csv
"""
import argparse
import io
import contextlib

import yaml
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

from src.data import load_raw, load_phase1_features, filter_phase1_samples
from src.phase1.dimensions import build_dimensions
from src.phase1 import metaheuristic

SILENT_METHODS = {"hho", "vigpso", "avicpso", "sma"}


def run_clustering(values: np.ndarray, cfg: dict, method: str) -> np.ndarray:
    fn = metaheuristic.get(method)
    if method in SILENT_METHODS:
        with contextlib.redirect_stdout(io.StringIO()):
            labels, _ = fn(values, cfg)
    else:
        labels, _ = fn(values, cfg)
    return labels


def plot_feature_distribution(dims: pd.DataFrame, labels: np.ndarray, dim_cols: list[str]):
    """Boxplot of each feature grouped by cluster."""
    n_features = len(dim_cols)
    n_clusters = len(np.unique(labels))

    fig, axes = plt.subplots(1, n_features, figsize=(5 * n_features, 5), sharey=False)
    if n_features == 1:
        axes = [axes]

    colors = plt.cm.tab10(np.linspace(0, 1, n_clusters))

    for ax, feat in zip(axes, dim_cols):
        data_by_cluster = [
            dims[feat].values[labels == k]
            for k in sorted(np.unique(labels))
        ]
        bp = ax.boxplot(data_by_cluster, patch_artist=True, notch=False)
        for patch, color in zip(bp["boxes"], colors):
            patch.set_facecolor(color)
            patch.set_alpha(0.7)
        ax.set_title(feat, fontsize=13)
        ax.set_xlabel("Cluster")
        ax.set_ylabel("Normalized value")
        ax.set_xticks(range(1, n_clusters + 1))
        ax.set_xticklabels([f"C{k}" for k in sorted(np.unique(labels))])

    fig.suptitle("Feature Distribution per Cluster", fontsize=15, fontweight="bold")
    plt.tight_layout()
    return fig


def plot_violin(dims: pd.DataFrame, labels: np.ndarray, dim_cols: list[str]):
    """Violin plot — 比 boxplot 更能看出分布形狀。"""
    n_features = len(dim_cols)
    n_clusters = len(np.unique(labels))
    cluster_ids = sorted(np.unique(labels))

    fig, axes = plt.subplots(1, n_features, figsize=(5 * n_features, 5), sharey=False)
    if n_features == 1:
        axes = [axes]

    colors = plt.cm.tab10(np.linspace(0, 1, n_clusters))

    for ax, feat in zip(axes, dim_cols):
        data_by_cluster = [dims[feat].values[labels == k] for k in cluster_ids]
        parts = ax.violinplot(data_by_cluster, positions=range(n_clusters), showmedians=True)
        for i, pc in enumerate(parts["bodies"]):
            pc.set_facecolor(colors[i])
            pc.set_alpha(0.7)
        ax.set_title(feat, fontsize=13)
        ax.set_xlabel("Cluster")
        ax.set_ylabel("Normalized value")
        ax.set_xticks(range(n_clusters))
        ax.set_xticklabels([f"C{k}" for k in cluster_ids])

    fig.suptitle("Feature Distribution per Cluster (Violin)", fontsize=15, fontweight="bold")
    plt.tight_layout()
    return fig


def plot_cluster_summary(dims: pd.DataFrame, labels: np.ndarray, dim_cols: list[str]):
    """各群的特徵平均值條狀圖，方便快速比較群間差異。"""
    cluster_ids = sorted(np.unique(labels))
    means = pd.DataFrame(
        {k: dims[dim_cols].values[labels == k].mean(axis=0) for k in cluster_ids},
        index=dim_cols,
    ).T

    ax = means.plot(kind="bar", figsize=(max(8, len(cluster_ids) * 1.2), 5),
                    colormap="tab10", edgecolor="white")
    ax.set_title("Mean Feature Value per Cluster", fontsize=14, fontweight="bold")
    ax.set_xlabel("Cluster")
    ax.set_ylabel("Normalized mean value")
    ax.set_xticklabels([f"C{k}" for k in cluster_ids], rotation=0)
    ax.legend(title="Feature", bbox_to_anchor=(1.01, 1), loc="upper left")
    plt.tight_layout()
    return ax.get_figure()


def print_cluster_stats(dims: pd.DataFrame, labels: np.ndarray, dim_cols: list[str]):
    cluster_ids = sorted(np.unique(labels))
    print(f"\n{'='*60}")
    print(f"  Cluster Stats  |  {len(cluster_ids)} clusters  |  {len(labels):,} samples")
    print(f"{'='*60}")
    for k in cluster_ids:
        mask = labels == k
        sub = dims[dim_cols].values[mask]
        means = sub.mean(axis=0)
        stds  = sub.std(axis=0)
        print(f"\n  Cluster {k}  (n={mask.sum():,})")
        for feat, m, s in zip(dim_cols, means, stds):
            bar = "█" * int(m * 20)
            print(f"    {feat:<20}  mean={m:.3f}  std={s:.3f}  {bar}")
    print(f"{'='*60}\n")


def load_from_csv(labels_csv: str) -> tuple[pd.DataFrame, np.ndarray, list[str]]:
    """讀取 benchmark 產出的 labels CSV，直接回傳 dims、labels、dim_cols。
    CSV 格式：appid, cluster_id, name, <dim1>, <dim2>, ...
    """
    ldf = pd.read_csv(labels_csv)
    if "cluster_id" not in ldf.columns:
        raise ValueError(f"{labels_csv} 需要有 'cluster_id' 欄位")
    skip = {"appid", "cluster_id", "cluster", "name"}
    dim_cols = [c for c in ldf.columns if c not in skip]
    if not dim_cols:
        raise ValueError(f"{labels_csv} 找不到維度欄位（除 appid/cluster_id/name 之外的欄位）")
    labels = ldf["cluster_id"].values.astype(int)
    dims = ldf[["appid"] + dim_cols].copy()
    return dims, labels, dim_cols


def plot_scatter3d(dims: pd.DataFrame, labels: np.ndarray, dim_cols: list[str]):
    """三維散佈圖，每個點依 cluster 著色。"""
    if len(dim_cols) < 3:
        print("[WARN] 散佈圖需要至少 3 個維度，跳過")
        return None

    x_col, y_col, z_col = dim_cols[0], dim_cols[1], dim_cols[2]
    cluster_ids = sorted(np.unique(labels))
    colors = plt.cm.tab10(np.linspace(0, 1, len(cluster_ids)))

    fig = plt.figure(figsize=(9, 7))
    ax = fig.add_subplot(111, projection="3d")

    for color, k in zip(colors, cluster_ids):
        mask = labels == k
        ax.scatter(
            dims[x_col].values[mask],
            dims[y_col].values[mask],
            dims[z_col].values[mask],
            c=[color], s=4, alpha=0.5, label=f"C{k} (n={mask.sum():,})"
        )

    ax.set_xlabel(x_col, fontsize=11)
    ax.set_ylabel(y_col, fontsize=11)
    ax.set_zlabel(z_col, fontsize=11)
    ax.set_title("3D Scatter by Cluster", fontsize=13, fontweight="bold")
    ax.legend(loc="upper left", bbox_to_anchor=(1.05, 1), fontsize=9)
    plt.tight_layout()
    return fig


def plot_radar(dims: pd.DataFrame, labels: np.ndarray, dim_cols: list[str]):
    """三角雷達圖（三維）— 各群的特徵平均值。"""
    cluster_ids = sorted(np.unique(labels))
    N = len(dim_cols)
    angles = np.linspace(0, 2 * np.pi, N, endpoint=False).tolist()
    angles += angles[:1]

    colors = plt.cm.tab10(np.linspace(0, 1, len(cluster_ids)))
    fig, ax = plt.subplots(figsize=(6, 6), subplot_kw=dict(polar=True))

    for color, k in zip(colors, cluster_ids):
        mask = labels == k
        values = dims[dim_cols].values[mask].mean(axis=0).tolist()
        values += values[:1]
        ax.plot(angles, values, color=color, linewidth=2, label=f"C{k} (n={mask.sum():,})")
        ax.fill(angles, values, color=color, alpha=0.15)

    ax.set_xticks(angles[:-1])
    ax.set_xticklabels(dim_cols, fontsize=12)
    ax.set_ylim(0, 1)
    ax.set_title("Cluster Radar Chart (mean per feature)", fontsize=13, fontweight="bold", pad=20)
    ax.legend(loc="upper right", bbox_to_anchor=(1.3, 1.15), fontsize=9)
    plt.tight_layout()
    return fig


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--config", default="configs/assign_avicpso.yaml")
    parser.add_argument("--method", default=None, help="覆蓋 config 中的 algorithm（僅重新跑模式有效）")
    parser.add_argument("--labels-csv", default=None, help="直接指定 labels CSV，跳過分群演算法")
    parser.add_argument("--plot", choices=["box", "violin", "bar", "radar", "scatter3d", "all"], default="all")
    parser.add_argument("--save", action="store_true", help="儲存圖片而非顯示")
    args = parser.parse_args()

    if args.labels_csv:
        print(f"讀取 labels CSV: {args.labels_csv}")
        dims, labels, dim_cols = load_from_csv(args.labels_csv)
        tag = args.labels_csv.rsplit("/", 1)[-1].rsplit("\\", 1)[-1].replace(".csv", "")
    else:
        with open(args.config, encoding="utf-8") as f:
            cfg = yaml.safe_load(f)
        df = load_raw()
        df = filter_phase1_samples(df, cfg.get("sample_filter"))
        phase1_df = load_phase1_features(df)
        dims = build_dimensions(phase1_df, cfg)
        dim_cols = [c for c in dims.columns if c != "appid"]
        method = args.method or cfg.get("algorithm", "avicpso")
        cfg["algorithm"] = method
        values = dims[dim_cols].values
        print(f"Running {method}  |  n_colors={cfg['n_colors']}  |  samples={len(values):,}")
        labels = run_clustering(values, cfg, method)
        tag = method

    print_cluster_stats(dims, labels, dim_cols)

    figs = []
    if args.plot in ("box", "all"):
        figs.append(("boxplot", plot_feature_distribution(dims, labels, dim_cols)))
    if args.plot in ("violin", "all"):
        figs.append(("violin", plot_violin(dims, labels, dim_cols)))
    if args.plot in ("bar", "all"):
        figs.append(("bar", plot_cluster_summary(dims, labels, dim_cols)))
    if args.plot in ("radar", "all"):
        figs.append(("radar", plot_radar(dims, labels, dim_cols)))
    if args.plot in ("scatter3d", "all"):
        fig3d = plot_scatter3d(dims, labels, dim_cols)
        if fig3d is not None:
            figs.append(("scatter3d", fig3d))

    if args.save:
        for name, fig in figs:
            path = f"cluster_{name}_{tag}.png"
            fig.savefig(path, dpi=150)
            print(f"Saved: {path}")
    else:
        plt.show()


if __name__ == "__main__":
    main()
