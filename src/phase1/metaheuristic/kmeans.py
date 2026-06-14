from __future__ import annotations

from typing import Tuple

import numpy as np
from sklearn.cluster import KMeans

from ..objective import evaluate_partition


def run_kmeans(values: np.ndarray, cfg: dict) -> Tuple[np.ndarray, float]:
    return _run(values, cfg, init="random")


def run_kmeans_plus(values: np.ndarray, cfg: dict) -> Tuple[np.ndarray, float]:
    return _run(values, cfg, init="k-means++")


def _run(values: np.ndarray, cfg: dict, init: str) -> Tuple[np.ndarray, float]:
    n_colors = int(cfg.get("n_colors", 8))
    seed = int(cfg.get("seed", 42))
    max_iter = int(cfg.get("iterations", 100))
    km = KMeans(n_clusters=n_colors, init=init, n_init=1, max_iter=max_iter, random_state=seed)
    labels = km.fit_predict(values).astype(int)
    score = evaluate_partition(labels, values, cfg)
    return labels, score
