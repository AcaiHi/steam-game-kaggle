"""Particle Swarm Optimization — 最佳化三個門檻值。"""
import numpy as np
from typing import Callable


def run(objective: Callable, n_dims: int, cfg: dict, perturb_fn=None) -> tuple[list[float], float]:
    pop_size = cfg.get("population", 50)
    n_iter = cfg.get("iterations", 200)
    w = cfg.get("inertia", 0.7)
    c1 = cfg.get("cognitive", 1.5)
    c2 = cfg.get("social", 1.5)

    pos = np.random.rand(pop_size, n_dims)
    vel = np.zeros_like(pos)
    scores = np.array([objective(p) for p in pos])

    pbest_pos = pos.copy()
    pbest_score = scores.copy()
    gbest_idx = np.argmin(scores)
    gbest_pos = pos[gbest_idx].copy()
    gbest_score = scores[gbest_idx]

    for t in range(n_iter):
        r1, r2 = np.random.rand(pop_size, n_dims), np.random.rand(pop_size, n_dims)
        vel = w * vel + c1 * r1 * (pbest_pos - pos) + c2 * r2 * (gbest_pos - pos)
        pos = np.clip(pos + vel, 0, 1)
        if perturb_fn is not None:
            pos = np.array([perturb_fn(p, t, n_iter) for p in pos])
        scores = np.array([objective(p) for p in pos])

        improved = scores < pbest_score
        pbest_pos[improved] = pos[improved]
        pbest_score[improved] = scores[improved]

        if pbest_score.min() < gbest_score:
            gbest_idx = np.argmin(pbest_score)
            gbest_pos = pbest_pos[gbest_idx].copy()
            gbest_score = pbest_score[gbest_idx]

    return gbest_pos.tolist(), float(gbest_score)
