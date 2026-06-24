"""
論文 Maulik & Bandyopadhyay (2000) 的 centroid 編碼與 fitness。

解的表示：K 個群心攤平為長度 K*n_dims 的向量，值域 [0, 1]（對應正規化特徵空間）。
Fitness 計算：
  1. 將每筆資料分配至最近群心
  2. 以各群實際均值更新群心（內嵌一次 K-means 精煉）
  3. 回傳 M = 所有點到群心的歐氏距離總和（最小化）
"""
from __future__ import annotations
import numpy as np


def make_fitness(values: np.ndarray, n_colors: int):
    """回傳 fitness function，接受長度 K*n_dims 的向量，回傳 M（越小越好）。"""
    n_dims = values.shape[1]

    def fitness(pos: np.ndarray) -> float:
        centroids = pos.reshape(n_colors, n_dims).copy()
        dists = np.linalg.norm(values[:, None, :] - centroids[None, :, :], axis=2)  # (n, K)
        labels = np.argmin(dists, axis=1)
        for k in range(n_colors):
            mask = labels == k
            if mask.any():
                centroids[k] = values[mask].mean(axis=0)
        M = sum(
            np.linalg.norm(values[labels == k] - centroids[k], axis=1).sum()
            for k in range(n_colors) if (labels == k).any()
        )
        return float(M)

    return fitness


def get_warmstart_pos(values: np.ndarray, n_colors: int) -> np.ndarray:
    """用 KMeans++ 取得初始群心，攤平為長度 K*n_dims 的向量供 warm-start 使用。"""
    from sklearn.cluster import KMeans
    km = KMeans(n_clusters=n_colors, init="k-means++", n_init=1, max_iter=100, random_state=42)
    km.fit(values)
    return km.cluster_centers_.flatten()


def make_restart_pool(values: np.ndarray, n_colors: int, n_restarts: int = 5) -> np.ndarray:
    """
    用不同 seed 跑 n_restarts 次 K-means，回傳 (n_restarts, K*n_dims) 的 pool。
    供 AVICPSO Axis B 停滯重啟使用：每個粒子停滯時輪流從不同 K-means 解出發，
    而非全部錨定在 gbest，確保重啟跳到資料結構上合法且彼此分散的區域。
    """
    from sklearn.cluster import KMeans
    pool = []
    for seed in range(n_restarts):
        km = KMeans(n_clusters=n_colors, init="k-means++", n_init=1,
                    max_iter=100, random_state=seed)
        km.fit(values)
        pool.append(km.cluster_centers_.flatten())
    return np.array(pool)  # (n_restarts, dim)


def make_fitness_fn(
    values: np.ndarray,
    n_colors: int,
    cfg: dict,
    weights: "np.ndarray | None" = None,
):
    """Factory: dispatch to plain or weighted fitness based on cfg['fitness']['type'].

    Weights are resolved in order:
      1. explicit *weights* argument
      2. cfg['_weights_cache']  (pre-computed by caller, e.g. benchmark loop)
      3. computed on demand from values + cfg['fitness']
    This avoids re-running compute_density_weights for each method when
    values and fitness config are constant across a benchmark run.
    """
    fit_type = cfg.get("fitness", {}).get("type", "plain")
    if fit_type == "combined":
        from .weighted_fitness import make_combined_fitness
        return make_combined_fitness(values, n_colors, cfg)
    if fit_type in ("weighted", "wcss", "wdbi", "wcombined"):
        from .weighted_fitness import (
            compute_density_weights,
            make_weighted_fitness,
            make_wcss_fitness,
            make_wdbi_fitness,
            make_wcombined_fitness,
        )
        if weights is None:
            weights = cfg.get("_weights_cache")
        if weights is None:
            weights = compute_density_weights(values, cfg)
        if fit_type == "wcss":
            return make_wcss_fitness(values, n_colors, weights)
        if fit_type == "wdbi":
            return make_wdbi_fitness(values, n_colors, weights)
        if fit_type == "wcombined":
            return make_wcombined_fitness(values, n_colors, weights, cfg)
        return make_weighted_fitness(values, n_colors, weights)
    return make_fitness(values, n_colors)


def decode_labels(pos: np.ndarray, values: np.ndarray, n_colors: int) -> np.ndarray:
    """從最佳 centroid 向量取得每筆資料的 cluster id。"""
    n_dims = values.shape[1]
    centroids = pos.reshape(n_colors, n_dims)
    dists = np.linalg.norm(values[:, None, :] - centroids[None, :, :], axis=2)
    return np.argmin(dists, axis=1).astype(int)
