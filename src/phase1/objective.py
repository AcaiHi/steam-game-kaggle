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


def assess_partition(
    labels: np.ndarray,
    values: np.ndarray,
    weights: "np.ndarray | None" = None,
) -> dict:
    unique, counts = np.unique(labels, return_counts=True)
    dist = {int(k): int(v) for k, v in zip(unique, counts)}
    dominant_pct = max(counts) / len(labels)

    n_active = len(unique)
    try:
        sil = float(silhouette_score(values, labels, sample_size=min(10000, len(labels)), random_state=42)) if n_active >= 2 else -1.0
        dbi = float(davies_bouldin_score(values, labels)) if n_active >= 2 else float("inf")
    except ValueError:
        sil, dbi = -1.0, float("inf")

    wdbi = weighted_davies_bouldin_score(labels, values, weights) if (weights is not None and n_active >= 2) else None

    return {
        "silhouette": sil,
        "davies_bouldin": dbi,
        "weighted_davies_bouldin": wdbi,
        "cluster_dist": dist,
        "n_active_clusters": int(n_active),
        "dominant_cluster_pct": float(dominant_pct),
        "balance_warning": dominant_pct > 0.5,
    }


def weighted_davies_bouldin_score(
    labels: np.ndarray,
    values: np.ndarray,
    weights: np.ndarray,
    eps: float = 1e-10,
) -> float:
    """Weighted Davies-Bouldin Index.

    WDBI = (1/K) Σ_k  max_{j≠k}  (s_k^w + s_j^w) / d_kj^w

    s_k^w  = Σ_{i∈Ck} w_i ‖x_i − μ_k^w‖ / Σ_{i∈Ck} w_i   (weighted mean distance)
    μ_k^w  = Σ_{i∈Ck} w_i x_i / Σ_{i∈Ck} w_i               (weighted centroid)
    d_kj^w = ‖μ_k^w − μ_j^w‖                                 (centroid distance)
    """
    unique = np.unique(labels)
    K = len(unique)
    if K < 2:
        return float("inf")

    # weighted centroids and scatter per cluster
    mu = {}
    s  = {}
    for k in unique:
        mask = labels == k
        w_k  = weights[mask]
        W_k  = w_k.sum()
        mu_k = (w_k[:, None] * values[mask]).sum(axis=0) / W_k
        d_k  = np.linalg.norm(values[mask] - mu_k, axis=1)
        mu[k] = mu_k
        s[k]  = float((w_k * d_k).sum() / W_k)

    total = 0.0
    for k in unique:
        worst = 0.0
        for j in unique:
            if j == k:
                continue
            d_kj = float(np.linalg.norm(mu[k] - mu[j]))
            r_kj = (s[k] + s[j]) / max(d_kj, eps)
            if r_kj > worst:
                worst = r_kj
        total += worst

    return float(total / K)


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
