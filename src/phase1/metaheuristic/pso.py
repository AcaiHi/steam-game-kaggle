from __future__ import annotations
import numpy as np
from typing import Tuple
from .centroid_fitness import make_fitness, decode_labels, get_warmstart_pos


def run_pso(values: np.ndarray, cfg: dict) -> Tuple[np.ndarray, float]:
    n_colors = int(cfg.get("n_colors", 8))
    n_dims   = values.shape[1]
    dim      = n_colors * n_dims

    fitness  = make_fitness(values, n_colors)
    warm_pos = get_warmstart_pos(values, n_colors)

    n_particles = int(cfg.get("population", 20))
    max_iter    = int(cfg.get("iterations", 500))
    omega       = float(cfg.get("omega", 0.4))
    c1          = float(cfg.get("c1", 1.5))
    c2          = float(cfg.get("c2", 2.0))
    v_max       = 0.2

    np.random.seed(int(cfg.get("seed", 42)))

    X = np.random.uniform(0, 1, (n_particles, dim))
    X[0] = np.clip(warm_pos, 0, 1)
    V = np.zeros((n_particles, dim))

    pbest_pos = X.copy()
    pbest_val = np.array([fitness(X[i]) for i in range(n_particles)])

    gbest_idx = int(np.argmin(pbest_val))
    gbest_pos = pbest_pos[gbest_idx].copy()
    gbest_val = float(pbest_val[gbest_idx])

    for t in range(max_iter):
        w = omega * (1.0 - 0.6 * t / max_iter)
        r1 = np.random.uniform(0, 1, (n_particles, dim))
        r2 = np.random.uniform(0, 1, (n_particles, dim))

        V = w * V + c1 * r1 * (pbest_pos - X) + c2 * r2 * (gbest_pos - X)
        V = np.clip(V, -v_max, v_max)
        X = np.clip(X + V, 0, 1)

        vals = np.array([fitness(X[i]) for i in range(n_particles)])
        improved = vals < pbest_val
        pbest_pos[improved] = X[improved].copy()
        pbest_val[improved] = vals[improved]

        best_idx = int(np.argmin(pbest_val))
        if pbest_val[best_idx] < gbest_val:
            gbest_val = float(pbest_val[best_idx])
            gbest_pos = pbest_pos[best_idx].copy()

    labels = decode_labels(gbest_pos, values, n_colors)
    return labels, gbest_val
