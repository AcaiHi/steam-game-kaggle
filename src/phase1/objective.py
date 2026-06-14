"""
目標函數與評估指標。

- evaluate_partition() : 供 assignment 最小化，回傳負的 (inter - intra)
- assess_partition()   : 分群完成後的正式評估，回傳 Silhouette、DBI、Cluster Distribution
"""
import numpy as np
import pandas as pd
from sklearn.metrics import silhouette_score, davies_bouldin_score


def evaluate_partition(labels: np.ndarray, values: np.ndarray, cfg: dict) -> float:
    n_dims = values.shape[1]
    n_clusters = int(cfg.get("n_colors", 2 ** n_dims))

    intra = _intra_variance(values, labels)
    inter = _inter_distance(values, labels)

    w_intra = cfg["objective"]["intra_weight"]
    w_inter = cfg["objective"]["inter_weight"]
    return -(w_inter * inter - w_intra * intra)


def assess_partition(labels: np.ndarray, values: np.ndarray) -> dict:
    unique, counts = np.unique(labels, return_counts=True)
    dist = {int(k): int(v) for k, v in zip(unique, counts)}
    dominant_pct = max(counts) / len(labels)

    n_active = len(unique)
    try:
        sil = float(silhouette_score(values, labels, sample_size=min(10000, len(labels)), random_state=42)) if n_active >= 2 else -1.0
        dbi = float(davies_bouldin_score(values, labels)) if n_active >= 2 else float("inf")
    except ValueError:
        sil, dbi = -1.0, float("inf")

    return {
        "silhouette": sil,
        "davies_bouldin": dbi,
        "cluster_dist": dist,
        "n_active_clusters": int(n_active),
        "dominant_cluster_pct": float(dominant_pct),
        "balance_warning": dominant_pct > 0.5,
    }


def _intra_variance(values: np.ndarray, labels: np.ndarray) -> float:
    unique = np.unique(labels)
    variances = [values[labels == l].var(axis=0).mean() for l in unique]
    return float(np.mean(variances))


def _inter_distance(values: np.ndarray, labels: np.ndarray) -> float:
    unique = np.unique(labels)
    centroids = np.array([values[labels == l].mean(axis=0) for l in unique])
    if len(centroids) < 2:
        return 0.0
    dists = []
    for i in range(len(centroids)):
        for j in range(i + 1, len(centroids)):
            dists.append(np.linalg.norm(centroids[i] - centroids[j]))
    return float(np.mean(dists))
