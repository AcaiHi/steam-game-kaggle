"""
Slime Mould Algorithm (SMA)
Based on: Li et al., "Slime Mould Algorithm: A New Method for Stochastic Optimization"
Future Generation Computer Systems, 2020. https://doi.org/10.1016/j.future.2020.03.055
"""
import numpy as np
from typing import Callable


def run(objective: Callable, n_dims: int, cfg: dict, perturb_fn=None) -> tuple[list[float], float]:
    pop_size = cfg.get("population", 50)
    n_iter   = cfg.get("iterations", 200)
    z        = cfg.get("z", 0.03)          # random exploration probability (Eq. 2.7)

    # Initialise population in [0, 1]
    pop = np.random.rand(pop_size, n_dims)
    fitness = np.array([objective(ind) for ind in pop])

    # Sort: best (lowest) first
    order = np.argsort(fitness)
    pop, fitness = pop[order], fitness[order]

    g_best_pos   = pop[0].copy()
    g_best_score = fitness[0]

    for epoch in range(n_iter):
        s = fitness[0] - fitness[-1] + 1e-10   # Eq.(2.5) denominator guard

        # Fitness weight for each individual (Eq. 2.5)
        weight = np.ones((pop_size, n_dims))
        half = pop_size // 2
        for i in range(pop_size):
            r = np.random.rand(n_dims)
            log_term = np.log10((fitness[0] - fitness[i]) / s + 1)
            if i <= half:
                weight[i] = 1 + r * log_term
            else:
                weight[i] = 1 - r * log_term

        a = np.arctanh(1 - (epoch + 1) / n_iter)   # Eq.(2.4)
        b = 1 - (epoch + 1) / n_iter

        for i in range(pop_size):
            if np.random.rand() < z:               # Eq.(2.7): random walk
                pop[i] = np.random.rand(n_dims)
            else:
                p  = np.tanh(abs(fitness[i] - g_best_score))   # Eq.(2.2)
                vb = np.random.uniform(-a, a, n_dims)           # Eq.(2.3)
                vc = np.random.uniform(-b, b, n_dims)

                candidates = list(set(range(pop_size)) - {i})
                id_a, id_b = np.random.choice(candidates, 2, replace=False)

                pos_1 = g_best_pos + vb * (weight[i] * pop[id_a] - pop[id_b])  # Eq.(2.1) branch A
                pos_2 = vc * pop[i]                                              # Eq.(2.1) branch B
                mask  = np.random.rand(n_dims) < p
                pop[i] = np.where(mask, pos_1, pos_2)

            pop[i] = np.clip(pop[i], 0, 1)
            if perturb_fn is not None:
                pop[i] = perturb_fn(pop[i], epoch, n_iter)
            fitness[i] = objective(pop[i])

        # Re-sort and update global best
        order = np.argsort(fitness)
        pop, fitness = pop[order], fitness[order]
        if fitness[0] < g_best_score:
            g_best_score = fitness[0]
            g_best_pos   = pop[0].copy()

    return g_best_pos.tolist(), float(g_best_score)
