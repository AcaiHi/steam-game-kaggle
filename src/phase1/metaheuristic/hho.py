from __future__ import annotations
import os, sys
import numpy as np
from typing import Tuple
from .centroid_fitness import make_fitness_fn, decode_labels, get_warmstart_pos

_HHO_DIR = os.path.abspath(
    os.path.join(os.path.dirname(__file__), "..", "..", "..",
                 "Python code of Harris Hawks optimization (HHO Python)")
)


def run_hho(values: np.ndarray, cfg: dict) -> Tuple[np.ndarray, float]:
    n_colors = int(cfg.get("n_colors", 8))
    n_dims   = values.shape[1]
    dim      = n_colors * n_dims

    fitness  = make_fitness_fn(values, n_colors, cfg)
    warm_pos = get_warmstart_pos(values, n_colors)

    if _HHO_DIR not in sys.path:
        sys.path.insert(0, _HHO_DIR)
    from HHO import HHO

    s = HHO(
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
