"""
目標函數與評估指標。

- evaluate()    : 供 metaheuristic 最小化，回傳負的 (inter - intra)
- assess()      : 分群完成後的正式評估，回傳 Silhouette、DBI、Cluster Distribution
"""
import numpy as np
import pandas as pd
from sklearn.metrics import silhouette_score, davies_bouldin_score


def evaluate(thresholds: list[float], dims: pd.DataFrame, cfg: dict) -> float:
    """
    Metaheuristic 目標函數，回傳負值供最小化器使用。
    加入空群懲罰：每少一個活躍群加 penalty_per_missing。
    """
    labels = assign_labels(thresholds, dims)
    dim_cols = [c for c in dims.columns if c != "appid"]
    values = dims[dim_cols].values

    intra = _intra_variance(values, labels)
    inter = _inter_distance(values, labels)

    w_intra = cfg["objective"]["intra_weight"]
    w_inter = cfg["objective"]["inter_weight"]
    base = -(w_inter * inter - w_intra * intra)

    n_missing = 8 - len(np.unique(labels))
    penalty = cfg["objective"].get("penalty_per_missing", 1.0) * n_missing
    return base + penalty


def assess(thresholds: list[float], dims: pd.DataFrame) -> dict:
    """
    分群結果的正式評估指標（依 spec 定義）：
    - silhouette_score  : 越高越好，範圍 -1 ~ 1
    - davies_bouldin    : 越低越好
    - cluster_dist      : 各群樣本數，任一群 > 50% 時發出警示
    """
    labels = assign_labels(thresholds, dims)
    dim_cols = [c for c in dims.columns if c != "appid"]
    values = dims[dim_cols].values

    unique, counts = np.unique(labels, return_counts=True)
    dist = {int(k): int(v) for k, v in zip(unique, counts)}
    dominant_pct = max(counts) / len(labels)

    n_active = len(unique)
    try:
        sil = float(silhouette_score(values, labels, sample_size=min(10000, len(labels)), random_state=42)) if n_active >= 2 else -1.0
        dbi = float(davies_bouldin_score(values, labels)) if n_active >= 2 else float("inf")
    except ValueError:
        sil, dbi = -1.0, float("inf")

    result = {
        "silhouette": sil,
        "davies_bouldin": dbi,
        "cluster_dist": dist,
        "n_active_clusters": int(n_active),
        "dominant_cluster_pct": float(dominant_pct),
        "balance_warning": dominant_pct > 0.5,
    }
    return result


def assign_labels(thresholds: list[float], dims: pd.DataFrame) -> np.ndarray:
    """
    依固定編碼公式切分：cluster_id = R×4 + P×2 + H×1
    位元順序：Rating → Playtime → Hotness
    """
    dim_cols = [c for c in dims.columns if c != "appid"]
    bits = (dims[dim_cols].values > np.array(thresholds)).astype(int)
    labels = bits @ (2 ** np.arange(len(dim_cols) - 1, -1, -1))
    return labels


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
