"""
Coloring-based Phase 1 search.

Each game is assigned a color / cluster label directly. The search keeps the
same min-cluster constraint and the same partition metrics as the threshold
version, but the solution representation is a label vector instead of thresholds.
"""
from __future__ import annotations

import numpy as np
from typing import Tuple
from sklearn.cluster import KMeans

from .objective import evaluate_partition


def run(values: np.ndarray, cfg: dict) -> Tuple[np.ndarray, float]:
    """
    Run a simple simulated annealing search over color assignments.
    Returns (labels, score).
    """
    n_samples, n_dims = values.shape
    n_colors = int(cfg.get("n_colors", 2 ** n_dims))
    min_size = int(cfg.get("constraints", {}).get("min_cluster_size", 1))

    labels = _initial_kmeans_labels(values, n_colors, min_size)
    current_score = evaluate_partition(labels, values, cfg)
    best_labels = labels.copy()
    best_score = current_score

    n_iter = int(cfg.get("iterations", 100))
    t0 = float(cfg.get("temperature", 1.0))
    cooling = float(cfg.get("cooling_rate", 0.995))
    move_per_iter = int(cfg.get("moves_per_iter", max(1, n_samples // 100)))

    temp = t0
    rng = np.random.default_rng(int(cfg.get("seed", 42)))

    for _ in range(n_iter):
        for _ in range(move_per_iter):
            idx = int(rng.integers(0, n_samples))
            src = int(labels[idx])
            dst = int(rng.integers(0, n_colors - 1))
            if dst >= src:
                dst += 1

            if not _can_move(labels, src, dst, min_size):
                continue

            proposal = labels.copy()
            proposal[idx] = dst
            proposal_score = evaluate_partition(proposal, values, cfg)

            delta = proposal_score - current_score
            if delta < 0 or rng.random() < np.exp(-delta / max(temp, 1e-9)):
                labels = proposal
                current_score = proposal_score
                if current_score < best_score:
                    best_score = current_score
                    best_labels = labels.copy()

        temp *= cooling

    return best_labels, float(best_score)


def _initial_kmeans_labels(values: np.ndarray, n_colors: int, min_size: int) -> np.ndarray:
    if values.shape[0] < n_colors * min_size:
        raise ValueError(
            f"Cannot satisfy min_cluster_size={min_size} with {values.shape[0]} samples and {n_colors} colors."
        )

    km = KMeans(n_clusters=n_colors, n_init=10, random_state=42)
    labels = km.fit_predict(values).astype(int)
    return _repair_min_size(labels, values, n_colors, min_size)


def _repair_min_size(labels: np.ndarray, values: np.ndarray, n_colors: int, min_size: int) -> np.ndarray:
    labels = labels.copy().astype(int)

    while True:
        counts = np.bincount(labels, minlength=n_colors)
        small = np.where(counts < min_size)[0]
        if len(small) == 0:
            return labels

        large = int(np.argmax(counts))
        target = int(small[0])
        if large == target:
            return labels

        idx_large = np.where(labels == large)[0]
        if len(idx_large) <= min_size:
            return labels

        centroids = np.array([
            values[labels == k].mean(axis=0) if (labels == k).any() else values.mean(axis=0)
            for k in range(n_colors)
        ])
        score = np.linalg.norm(values[idx_large] - centroids[target], axis=1) - np.linalg.norm(
            values[idx_large] - centroids[large], axis=1
        )
        move_idx = idx_large[int(np.argmax(score))]
        labels[move_idx] = target


def _can_move(labels: np.ndarray, src: int, dst: int, min_size: int) -> bool:
    if src == dst:
        return False
    src_count = int((labels == src).sum())
    dst_count = int((labels == dst).sum())
    return (src_count - 1) >= min_size and (dst_count + 1) >= min_size
