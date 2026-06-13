"""Genetic Algorithm — 最佳化三個門檻值。"""
import numpy as np
from typing import Callable


def run(objective: Callable, n_dims: int, cfg: dict, perturb_fn=None) -> tuple[list[float], float]:
    """
    回傳 (best_thresholds, best_score)。
    thresholds 各值範圍 [0, 1]（對應正規化後的維度值）。
    """
    pop_size = cfg.get("population", 50)
    n_iter = cfg.get("iterations", 200)
    crossover_rate = cfg.get("crossover_rate", 0.8)
    mutation_rate = cfg.get("mutation_rate", 0.1)

    pop = np.random.rand(pop_size, n_dims)
    scores = np.array([objective(ind) for ind in pop])

    for t in range(n_iter):
        # 選擇（錦標賽）
        parents = _tournament(pop, scores, pop_size)
        # 交叉
        offspring = _crossover(parents, crossover_rate)
        # 突變
        offspring = _mutate(offspring, mutation_rate)
        offspring = np.clip(offspring, 0, 1)
        if perturb_fn is not None:
            offspring = np.array([perturb_fn(ind, t, n_iter) for ind in offspring])
        new_scores = np.array([objective(ind) for ind in offspring])
        # 精英保留
        pop, scores = _elitism(pop, scores, offspring, new_scores)

    best_idx = np.argmin(scores)
    return pop[best_idx].tolist(), float(scores[best_idx])


def _tournament(pop, scores, n, k=3):
    selected = []
    for _ in range(n):
        idx = np.random.choice(len(pop), k, replace=False)
        selected.append(pop[idx[np.argmin(scores[idx])]])
    return np.array(selected)


def _crossover(parents, rate):
    offspring = parents.copy()
    for i in range(0, len(parents) - 1, 2):
        if np.random.rand() < rate:
            pt = np.random.randint(1, parents.shape[1])
            offspring[i, pt:], offspring[i + 1, pt:] = parents[i + 1, pt:].copy(), parents[i, pt:].copy()
    return offspring


def _mutate(pop, rate):
    mask = np.random.rand(*pop.shape) < rate
    pop[mask] += np.random.randn(mask.sum()) * 0.1
    return pop


def _elitism(old, old_s, new, new_s):
    combined = np.vstack([old, new])
    scores = np.concatenate([old_s, new_s])
    idx = np.argsort(scores)[:len(old)]
    return combined[idx], scores[idx]
