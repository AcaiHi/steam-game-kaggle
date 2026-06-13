"""Simulated Annealing — 最佳化三個門檻值。"""
import numpy as np
from typing import Callable


def run(objective: Callable, n_dims: int, cfg: dict, perturb_fn=None) -> tuple[list[float], float]:
    n_iter = cfg.get("iterations", 200)
    T0 = cfg.get("temperature", 1.0)
    cooling = cfg.get("cooling_rate", 0.995)
    step = cfg.get("step_size", 0.05)

    current = np.random.rand(n_dims)
    current_score = objective(current)
    best, best_score = current.copy(), current_score
    T = T0

    for t in range(n_iter):
        candidate = np.clip(current + np.random.randn(n_dims) * step, 0, 1)
        if perturb_fn is not None:
            candidate = perturb_fn(candidate, t, n_iter)
        candidate_score = objective(candidate)
        delta = candidate_score - current_score
        if delta < 0 or np.random.rand() < np.exp(-delta / T):
            current, current_score = candidate, candidate_score
        if current_score < best_score:
            best, best_score = current.copy(), current_score
        T *= cooling

    return best.tolist(), float(best_score)
