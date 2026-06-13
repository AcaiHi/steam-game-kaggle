"""
Discrete partition assignment search.

This is not graph coloring. Each game is directly assigned to one of K colors
/ clusters, and the search optimizes the same partition-level objective and
constraints used by the threshold-based Phase 1.
"""
from __future__ import annotations

from typing import Tuple

import numpy as np
from sklearn.cluster import KMeans

from .objective import evaluate_partition


def run(values: np.ndarray, cfg: dict) -> Tuple[np.ndarray, float]:
    algo = cfg.get("algorithm", "ga")
    if algo == "ga":
        return run_ga(values, cfg)
    if algo == "sa":
        return run_sa(values, cfg)
    raise ValueError(f"Unknown discrete assignment algorithm: {algo}")


def run_sa(values: np.ndarray, cfg: dict) -> Tuple[np.ndarray, float]:
    n_samples, n_dims = values.shape
    n_colors = int(cfg.get("n_colors", 2 ** n_dims))
    min_size = int(cfg.get("constraints", {}).get("min_cluster_size", 1))

    labels = _initial_kmeans_labels(values, n_colors, min_size)
    current_score = evaluate_partition(labels, values, cfg)
    best_labels = labels.copy()
    best_score = current_score

    n_iter = int(cfg.get("iterations", 100))
    t0 = float(cfg.get("temperature", 1.0))
    cooling = float(cfg.get("cooling_rate", 0.995))
    move_per_iter = int(cfg.get("moves_per_iter", max(1, n_samples // 100)))

    temp = t0
    rng = np.random.default_rng(int(cfg.get("seed", 42)))

    for _ in range(n_iter):
        for _ in range(move_per_iter):
            idx = int(rng.integers(0, n_samples))
            src = int(labels[idx])
            dst = int(rng.integers(0, n_colors - 1))
            if dst >= src:
                dst += 1

            if not _can_move(labels, src, dst, min_size):
                continue

            proposal = labels.copy()
            proposal[idx] = dst
            proposal_score = evaluate_partition(proposal, values, cfg)

            delta = proposal_score - current_score
            if delta < 0 or rng.random() < np.exp(-delta / max(temp, 1e-9)):
                labels = proposal
                current_score = proposal_score
                if current_score < best_score:
                    best_score = current_score
                    best_labels = labels.copy()

        temp *= cooling

    return best_labels, float(best_score)


def run_ga(values: np.ndarray, cfg: dict) -> Tuple[np.ndarray, float]:
    n_samples, n_dims = values.shape
    n_colors = int(cfg.get("n_colors", 2 ** n_dims))
    min_size = int(cfg.get("constraints", {}).get("min_cluster_size", 1))

    pop_size = int(cfg.get("population", 10))
    n_iter = int(cfg.get("iterations", 100))
    crossover_rate = float(cfg.get("crossover_rate", 0.8))
    mutation_rate = float(cfg.get("mutation_rate", 0.1))
    rng = np.random.default_rng(int(cfg.get("seed", 42)))

    base = _initial_kmeans_labels(values, n_colors, min_size)
    pop = [base.copy()]
    for _ in range(pop_size - 1):
        pop.append(_mutate_labels(base.copy(), values, rng, n_colors, min_size, 0.05))
    pop = np.stack(pop, axis=0)
    scores = np.array([evaluate_partition(ind, values, cfg) for ind in pop])

    for _ in range(n_iter):
        parents = _tournament(pop, scores, pop_size, rng)
        offspring = _crossover(parents, crossover_rate, rng)
        offspring = _mutate_population(offspring, values, rng, n_colors, min_size, mutation_rate)
        new_scores = np.array([evaluate_partition(ind, values, cfg) for ind in offspring])
        pop, scores = _elitism(pop, scores, offspring, new_scores)

    best_idx = int(np.argmin(scores))
    return pop[best_idx].copy(), float(scores[best_idx])


def _initial_kmeans_labels(values: np.ndarray, n_colors: int, min_size: int) -> np.ndarray:
    if values.shape[0] < n_colors * min_size:
        raise ValueError(
            f"Cannot satisfy min_cluster_size={min_size} with {values.shape[0]} samples and {n_colors} colors."
        )

    km = KMeans(n_clusters=n_colors, n_init=10, random_state=42)
    labels = km.fit_predict(values).astype(int)
    return _repair_min_size(labels, values, n_colors, min_size)


def _repair_min_size(labels: np.ndarray, values: np.ndarray, n_colors: int, min_size: int) -> np.ndarray:
    labels = labels.copy().astype(int)
    while True:
        counts = np.bincount(labels, minlength=n_colors)
        small = np.where(counts < min_size)[0]
        if len(small) == 0:
            return labels

        large = int(np.argmax(counts))
        target = int(small[0])
        if large == target:
            return labels

        idx_large = np.where(labels == large)[0]
        if len(idx_large) <= min_size:
            return labels

        centroids = np.array([
            values[labels == k].mean(axis=0) if (labels == k).any() else values.mean(axis=0)
            for k in range(n_colors)
        ])
        score = np.linalg.norm(values[idx_large] - centroids[target], axis=1) - np.linalg.norm(
            values[idx_large] - centroids[large], axis=1
        )
        move_idx = idx_large[int(np.argmax(score))]
        labels[move_idx] = target


def _mutate_labels(labels: np.ndarray, values: np.ndarray, rng: np.random.Generator, n_colors: int, min_size: int, rate: float) -> np.ndarray:
    mask = rng.random(labels.shape[0]) < rate
    idxs = np.where(mask)[0]
    for idx in idxs:
        current = int(labels[idx])
        new = int(rng.integers(0, n_colors - 1))
        if new >= current:
            new += 1
        labels[idx] = new
    return _repair_min_size(labels, values, n_colors, min_size)


def _mutate_population(pop: np.ndarray, values: np.ndarray, rng: np.random.Generator, n_colors: int, min_size: int, rate: float) -> np.ndarray:
    return np.stack([
        _mutate_labels(ind.copy(), values, rng, n_colors, min_size, rate) for ind in pop
    ], axis=0)


def _tournament(pop: np.ndarray, scores: np.ndarray, n: int, rng: np.random.Generator, k: int = 3) -> np.ndarray:
    k = min(k, len(pop))
    selected = []
    for _ in range(n):
        idx = rng.choice(len(pop), k, replace=False)
        selected.append(pop[idx[np.argmin(scores[idx])]].copy())
    return np.stack(selected, axis=0)


def _crossover(parents: np.ndarray, rate: float, rng: np.random.Generator) -> np.ndarray:
    offspring = parents.copy()
    for i in range(0, len(parents) - 1, 2):
        if rng.random() < rate:
            pt = int(rng.integers(1, parents.shape[1]))
            offspring[i, pt:], offspring[i + 1, pt:] = parents[i + 1, pt:].copy(), parents[i, pt:].copy()
    return offspring


def _elitism(old: np.ndarray, old_s: np.ndarray, new: np.ndarray, new_s: np.ndarray):
    combined = np.vstack([old, new])
    scores = np.concatenate([old_s, new_s])
    idx = np.argsort(scores)[:len(old)]
    return combined[idx], scores[idx]


def _can_move(labels: np.ndarray, src: int, dst: int, min_size: int) -> bool:
    if src == dst:
        return False
    src_count = int((labels == src).sum())
    dst_count = int((labels == dst).sum())
    return (src_count - 1) >= min_size and (dst_count + 1) >= min_size
