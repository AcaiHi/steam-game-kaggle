from __future__ import annotations
import os, sys
import numpy as np
from numpy import zeros
from typing import Tuple
from .centroid_fitness import make_fitness, decode_labels, get_warmstart_pos

_SMA_DIR = os.path.abspath(
    os.path.join(os.path.dirname(__file__), "..", "..", "..",
                 "Slime Mould Algorithm (SMA) Python Code")
)


def run_sma(values: np.ndarray, cfg: dict) -> Tuple[np.ndarray, float]:
    n_colors = int(cfg.get("n_colors", 8))
    n_dims   = values.shape[1]
    dim      = n_colors * n_dims

    fitness  = make_fitness(values, n_colors)
    warm_pos = get_warmstart_pos(values, n_colors)

    if _SMA_DIR not in sys.path:
        sys.path.insert(0, _SMA_DIR)
    from SMA import BaseSMA

    class _WarmSMA(BaseSMA):
        def __init__(self, warm, **kwargs):
            super().__init__(**kwargs)
            self._warm = warm
            self._injected = False

        def create_solution(self, minmax=0):
            if not self._injected:
                self._injected = True
                fit = self.get_fitness_position(self._warm)
                return [self._warm.copy(), fit, zeros(self.problem_size)]
            return super().create_solution(minmax)

    sma = _WarmSMA(
        warm=warm_pos,
        obj_func=fitness,
        lb=[0],
        ub=[1],
        problem_size=dim,
        verbose=False,
        epoch=int(cfg.get("iterations", 100)),
        pop_size=int(cfg.get("population", 20)),
    )
    best_pos, best_fit, _ = sma.train()

    labels = decode_labels(best_pos, values, n_colors)
    return labels, best_fit
