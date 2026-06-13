"""
Grey Wolf Optimizer (GWO)
Based on: Mirjalili et al., "Grey Wolf Optimizer"
Advances in Engineering Software, 2014. https://doi.org/10.1016/j.advengsoft.2013.12.007
"""
import numpy as np
from typing import Callable


def run(objective: Callable, n_dims: int, cfg: dict, perturb_fn=None) -> tuple[list[float], float]:
    pop_size = cfg.get("population", 50)
    n_iter   = cfg.get("iterations", 200)

    # Initialise wolves in [0, 1]
    X = np.random.rand(pop_size, n_dims)
    X[0] = _initial_solution(n_dims, cfg)
    fitness = np.array([objective(ind) for ind in X])

    # Alpha, Beta, Delta: three best wolves
    order = np.argsort(fitness)
    alpha_pos, alpha_score = X[order[0]].copy(), fitness[order[0]]
    beta_pos,  beta_score  = X[order[1]].copy(), fitness[order[1]]
    delta_pos, delta_score = X[order[2]].copy(), fitness[order[2]]

    for t in range(n_iter):
        # a decreases linearly from 2 to 0
        a = 2 - 2 * t / n_iter

        for i in range(pop_size):
            # Update position toward Alpha, Beta, Delta (Eq. 3.6)
            X[i] = _step(X[i], alpha_pos, beta_pos, delta_pos, a, n_dims)
            X[i] = np.clip(X[i], 0, 1)
            if perturb_fn is not None:
                X[i] = perturb_fn(X[i], t, n_iter)
            fitness[i] = objective(X[i])

        # Update hierarchy
        for i in range(pop_size):
            if fitness[i] < alpha_score:
                delta_pos, delta_score = beta_pos.copy(),  beta_score
                beta_pos,  beta_score  = alpha_pos.copy(), alpha_score
                alpha_pos, alpha_score = X[i].copy(), fitness[i]
            elif fitness[i] < beta_score:
                delta_pos, delta_score = beta_pos.copy(), beta_score
                beta_pos,  beta_score  = X[i].copy(), fitness[i]
            elif fitness[i] < delta_score:
                delta_pos, delta_score = X[i].copy(), fitness[i]

    return alpha_pos.tolist(), float(alpha_score)


def _initial_solution(n_dims: int, cfg: dict) -> np.ndarray:
    init_cfg = cfg.get("initial_solution", {})
    if init_cfg.get("enabled", False) and init_cfg.get("method", "midpoint") == "midpoint":
        return np.full(n_dims, float(init_cfg.get("value", 0.5)))
    return np.random.rand(n_dims)


def _step(
    pos: np.ndarray,
    alpha: np.ndarray,
    beta: np.ndarray,
    delta: np.ndarray,
    a: float,
    n_dims: int,
) -> np.ndarray:
    def _hunt(leader):
        r1, r2 = np.random.rand(n_dims), np.random.rand(n_dims)
        A = 2 * a * r1 - a   # Eq.(3.3)
        C = 2 * r2            # Eq.(3.4)
        D = abs(C * leader - pos)
        return leader - A * D

    X1 = _hunt(alpha)
    X2 = _hunt(beta)
    X3 = _hunt(delta)
    return (X1 + X2 + X3) / 3  # Eq.(3.6)
