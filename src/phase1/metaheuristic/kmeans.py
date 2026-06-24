from __future__ import annotations

from typing import Tuple

import numpy as np
from sklearn.cluster import KMeans

from ..objective import evaluate_partition
from .centroid_fitness import make_fitness_fn


def run_kmeans(values: np.ndarray, cfg: dict) -> Tuple[np.ndarray, float]:
    return _run(values, cfg, init="random")


def run_kmeans_plus(values: np.ndarray, cfg: dict) -> Tuple[np.ndarray, float]:
    return _run(values, cfg, init="k-means++")


def _run(values: np.ndarray, cfg: dict, init: str) -> Tuple[np.ndarray, float]:
    n_colors   = int(cfg.get("n_colors", 8))
    seed       = int(cfg.get("seed", 42))
    max_iter   = int(cfg.get("iterations", 100))
    fit_type   = cfg.get("fitness", {}).get("type", "plain")

    # weighted fitness → pass sample_weight so sklearn minimizes weighted WCSS
    sample_weight = None
    if fit_type in ("weighted", "wcombined", "wdbi"):
        from .weighted_fitness import compute_density_weights
        w = cfg.get("_weights_cache")
        if w is None:
            w = compute_density_weights(values, cfg)
        if fit_type == "weighted":
            sample_weight = w

    km = KMeans(
        n_clusters=n_colors, init=init, n_init=1,
        max_iter=max_iter, random_state=seed,
    )
    labels = km.fit_predict(values, sample_weight=sample_weight).astype(int)

    # score with the same fitness function as other methods so obj is comparable
    if fit_type not in ("plain",):
        fn    = make_fitness_fn(values, n_colors, cfg)
        pos   = km.cluster_centers_.flatten()
        score = fn(pos)
    else:
        score = evaluate_partition(labels, values, cfg)

    return labels, score
