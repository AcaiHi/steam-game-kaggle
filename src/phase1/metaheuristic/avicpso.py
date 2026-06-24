from __future__ import annotations
import os, sys
import numpy as np
from typing import Tuple
from .centroid_fitness import make_fitness_fn, decode_labels, get_warmstart_pos, make_restart_pool

_VIGPSO_DIR = os.path.abspath(
    os.path.join(os.path.dirname(__file__), "..", "..", "..",
                 "Using Variable Interaction Graphs to Improve Particle Swarm Optimization (Python Code)")
)


def run_avicpso(values: np.ndarray, cfg: dict) -> Tuple[np.ndarray, float]:
    n_colors = int(cfg.get("n_colors", 8))
    n_dims   = values.shape[1]
    dim      = n_colors * n_dims

    fitness      = make_fitness_fn(values, n_colors, cfg)
    warm_pos     = get_warmstart_pos(values, n_colors)
    n_restarts   = int(cfg.get("n_kmeans_restarts", 5))
    restart_pool = make_restart_pool(values, n_colors, n_restarts)

    if _VIGPSO_DIR not in sys.path:
        sys.path.insert(0, _VIGPSO_DIR)
    from AVICPSO import AVICPSO

    np.random.seed(int(cfg.get("seed", 42)))
    s = AVICPSO(
        objf=fitness,
        lb=0,
        ub=1,
        dim=dim,
        SearchAgents_no=int(cfg.get("population", 20)),
        Max_iter=int(cfg.get("iterations", 100)),
        stagnation_thresh=int(cfg.get("stagnation_thresh", 15)),
        levy_sigma_max=float(cfg.get("levy_sigma_max", 0.3)),
        collapse_theta=float(cfg.get("collapse_theta", 0.6)),
        n_colors=n_colors,
        warm_start_pos=warm_pos,
        restart_pool=restart_pool,
    )

    labels = decode_labels(s.bestIndividual, values, n_colors)
    return labels, float(s.best)
