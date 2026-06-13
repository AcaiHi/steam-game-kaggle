"""
Harris Hawks Optimization (HHO)
Based on: Heidari et al., "Harris hawks optimization: Algorithm and applications"
Future Generation Computer Systems, 2019.
"""
import numpy as np
import math
from typing import Callable


def run(objective: Callable, n_dims: int, cfg: dict, perturb_fn=None) -> tuple[list[float], float]:
    pop_size = cfg.get("population", 50)
    n_iter   = cfg.get("iterations", 200)

    # Initialise hawks in [0, 1]
    X = np.random.rand(pop_size, n_dims)
    X[0] = _initial_solution(n_dims, cfg)

    rabbit_pos    = np.zeros(n_dims)
    rabbit_energy = np.inf

    t = 0
    while t < n_iter:
        # Evaluate and update rabbit (best solution)
        for i in range(pop_size):
            X[i] = np.clip(X[i], 0, 1)
            fit = objective(X[i])
            if fit < rabbit_energy:
                rabbit_energy = fit
                rabbit_pos    = X[i].copy()

        E1 = 2 * (1 - t / n_iter)  # decreasing energy (Eq. 3)

        for i in range(pop_size):
            E0 = 2 * np.random.rand() - 1
            escaping_energy = E1 * E0

            if abs(escaping_energy) >= 1:
                # ── Exploration ──────────────────────────────────────────────
                if np.random.rand() < 0.5:
                    rand_idx = np.random.randint(pop_size)
                    rand_hawk = X[rand_idx]
                    X[i] = rand_hawk - np.random.rand() * abs(
                        rand_hawk - 2 * np.random.rand() * X[i]
                    )
                else:
                    X[i] = (rabbit_pos - X.mean(axis=0)) - np.random.rand() * (
                        np.random.rand(n_dims)          # lb=0, ub=1
                    )
            else:
                # ── Exploitation ─────────────────────────────────────────────
                r = np.random.rand()
                jump = 2 * (1 - np.random.rand())      # jump strength J

                if r >= 0.5 and abs(escaping_energy) >= 0.5:
                    # Soft besiege
                    X[i] = (rabbit_pos - X[i]) - escaping_energy * abs(
                        jump * rabbit_pos - X[i]
                    )
                elif r >= 0.5 and abs(escaping_energy) < 0.5:
                    # Hard besiege
                    X[i] = rabbit_pos - escaping_energy * abs(rabbit_pos - X[i])

                elif r < 0.5 and abs(escaping_energy) >= 0.5:
                    # Soft besiege with progressive rapid dives
                    X_new = rabbit_pos - escaping_energy * abs(
                        jump * rabbit_pos - X[i]
                    )
                    X_new = np.clip(X_new, 0, 1)
                    if objective(X_new) < objective(X[i]):
                        X[i] = X_new
                    else:
                        X_levy = X_new + np.random.randn(n_dims) * _levy(n_dims)
                        X_levy = np.clip(X_levy, 0, 1)
                        X[i] = X_levy if objective(X_levy) < objective(X[i]) else X_new

                else:
                    # Hard besiege with progressive rapid dives
                    X_new = rabbit_pos - escaping_energy * abs(
                        jump * rabbit_pos - X.mean(axis=0)
                    )
                    X_new = np.clip(X_new, 0, 1)
                    if objective(X_new) < objective(X[i]):
                        X[i] = X_new
                    else:
                        X_levy = X_new + np.random.randn(n_dims) * _levy(n_dims)
                        X_levy = np.clip(X_levy, 0, 1)
                        X[i] = X_levy if objective(X_levy) < objective(X[i]) else X_new

            X[i] = np.clip(X[i], 0, 1)
            if perturb_fn is not None:
                X[i] = perturb_fn(X[i], t, n_iter)

        t += 1

    return rabbit_pos.tolist(), float(rabbit_energy)


def _initial_solution(n_dims: int, cfg: dict) -> np.ndarray:
    init_cfg = cfg.get("initial_solution", {})
    if init_cfg.get("enabled", False) and init_cfg.get("method", "midpoint") == "midpoint":
        return np.full(n_dims, float(init_cfg.get("value", 0.5)))
    return np.random.rand(n_dims)


def _levy(n_dims: int, beta: float = 1.5) -> np.ndarray:
    sigma = (
        math.gamma(1 + beta) * math.sin(math.pi * beta / 2)
        / (math.gamma((1 + beta) / 2) * beta * 2 ** ((beta - 1) / 2))
    ) ** (1 / beta)
    u = 0.01 * np.random.randn(n_dims) * sigma
    v = np.random.randn(n_dims)
    return u / np.power(np.abs(v), 1 / beta)
