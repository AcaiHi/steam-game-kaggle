from __future__ import annotations
import os, sys
import numpy as np
from typing import Tuple
from .centroid_fitness import make_fitness, decode_labels, get_warmstart_pos

_VIGPSO_DIR = os.path.abspath(
    os.path.join(os.path.dirname(__file__), "..", "..", "..",
                 "Using Variable Interaction Graphs to Improve Particle Swarm Optimization (Python Code)")
)


def run_vigpso(values: np.ndarray, cfg: dict) -> Tuple[np.ndarray, float]:
    n_colors = int(cfg.get("n_colors", 8))
    n_dims   = values.shape[1]
    dim      = n_colors * n_dims

    fitness  = make_fitness(values, n_colors)
    warm_pos = get_warmstart_pos(values, n_colors)

    if _VIGPSO_DIR not in sys.path:
        sys.path.insert(0, _VIGPSO_DIR)
    from VIGPSO import VIGPSO

    np.random.seed(int(cfg.get("seed", 42)))
    s = VIGPSO(
        objf=fitness,
        lb=0,
        ub=1,
        dim=dim,
        SearchAgents_no=int(cfg.get("population", 20)),
        Max_iter=int(cfg.get("iterations", 100)),
        warm_start_pos=warm_pos,
    )

    labels = decode_labels(s.bestIndividual, values, n_colors)
    return labels, float(s.best)
