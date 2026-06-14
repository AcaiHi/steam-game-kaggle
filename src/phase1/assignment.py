"""
Discrete partition assignment search via Genetic Algorithm.

Strategy axes (cfg["sa"]):

  init:
    kmeans           — KMeans init='random'  (10 restarts, take best)
    kmeans++         — KMeans init='k-means++'  (smarter start, sklearn default)
    random           — random label assignment
    sorted_partition — sort by feature-sum, cut into K equal slices

  perturbation:
    baseline — random mutation
    improve  — BAM (Boundary-Aware Mutation) — mutation prob ∝ inconsistency
               so Cold/stable games are more likely mutated to escape local optima

  local_search:
    false — no local search
    true  — γ hot-game KNN-majority correction (greedy, strict)
"""
from __future__ import annotations

from typing import Tuple

import numpy as np
from sklearn.cluster import KMeans
from sklearn.neighbors import NearestNeighbors

from .objective import evaluate_partition, assess_partition


def run(values: np.ndarray, cfg: dict) -> Tuple[np.ndarray, float]:
    return run_ga(values, cfg)


# ── GA ────────────────────────────────────────────────────────────────────────

def run_ga(values: np.ndarray, cfg: dict) -> Tuple[np.ndarray, float]:
    n_samples, n_dims = values.shape
    n_colors = int(cfg.get("n_colors", 2 ** n_dims))

    pop_size       = int(cfg.get("population", 10))
    n_iter         = int(cfg.get("iterations", 100))
    crossover_rate = float(cfg.get("crossover_rate", 0.8))
    mutation_rate  = float(cfg.get("mutation_rate", 0.1))
    rng = np.random.default_rng(int(cfg.get("seed", 42)))

    sa_cfg       = cfg.get("sa", {})
    init_method  = (
        sa_cfg.get("init")
        or cfg.get("initial_solution", {}).get("method", "kmeans++")
    )
    perturbation = sa_cfg.get("perturbation", "baseline")
    use_ls       = bool(sa_cfg.get("local_search", False))

    knn_cfg    = cfg.get("knn", {})
    knn_k      = int(knn_cfg.get("k", 5))
    moves_pct  = float(cfg.get("moves_pct", 0.01))
    base_moves = max(1, int(n_samples * moves_pct))

    need_knn = (perturbation == "improve") or use_ls
    knn_indices: np.ndarray | None = None
    if need_knn:
        knn_indices = (
            NearestNeighbors(n_neighbors=knn_k + 1)
            .fit(values)
            .kneighbors(values, return_distance=False)[:, 1:]
        )

    _all_inits = ["random", "kmeans", "kmeans++", "sorted_partition"]
    pop = [_make_initial_labels(values, n_colors, init_method, rng)]
    for i in range(pop_size - 1):
        alt = _all_inits[i % len(_all_inits)]
        pop.append(_make_initial_labels(values, n_colors, alt, rng))
    pop    = np.stack(pop, axis=0)
    scores = np.array([evaluate_partition(ind, values, cfg) for ind in pop])

    for t in range(n_iter):
        progress = t / max(n_iter - 1, 1)
        gamma    = progress

        parents   = _tournament(pop, scores, pop_size, rng)
        offspring = _crossover(parents, crossover_rate, rng)

        if perturbation == "improve" and knn_indices is not None:
            offspring = _mutate_population_bam(
                offspring, values, rng, n_colors, mutation_rate, knn_indices
            )
        else:
            offspring = _mutate_population(
                offspring, values, rng, n_colors, mutation_rate
            )

        new_scores = np.array([evaluate_partition(ind, values, cfg) for ind in offspring])

        if use_ls and knn_indices is not None:
            n_gamma = max(1, int(base_moves * gamma))
            best_i  = int(np.argmin(new_scores))
            offspring[best_i] = _knn_local_search(
                offspring[best_i], values, cfg, n_colors, knn_indices, n_gamma,
            )
            new_scores[best_i] = evaluate_partition(offspring[best_i], values, cfg)

        pop, scores = _elitism(pop, scores, offspring, new_scores)

    best_idx = int(np.argmin(scores))
    return pop[best_idx].copy(), float(scores[best_idx])


# ── KNN helpers ───────────────────────────────────────────────────────────────

def _incons_per_game(labels: np.ndarray, knn_indices: np.ndarray) -> np.ndarray:
    """Per-game KNN inconsistency ∈ [0,1]: fraction of k neighbours in a different cluster."""
    return (labels[knn_indices] != labels[:, None]).mean(axis=1)


# ── initial solution factory ──────────────────────────────────────────────────

def _make_initial_labels(
    values: np.ndarray,
    n_colors: int,
    method: str,
    rng: np.random.Generator,
) -> np.ndarray:
    n = values.shape[0]
    if method == "kmeans++":
        km = KMeans(n_clusters=n_colors, init="k-means++", n_init=10, random_state=42)
        return km.fit_predict(values).astype(int)
    if method == "kmeans":
        km = KMeans(n_clusters=n_colors, init="random", n_init=10, random_state=42)
        return km.fit_predict(values).astype(int)
    if method == "random":
        return rng.integers(0, n_colors, n).astype(int)
    if method == "sorted_partition":
        return _init_sorted_partition(values, n_colors)
    raise ValueError(f"Unknown init method: {method!r}")


def _init_sorted_partition(values: np.ndarray, n_colors: int) -> np.ndarray:
    """Sort by feature-sum, cut into n_colors equal slices → balanced, spatially coherent."""
    n = values.shape[0]
    order = np.argsort(values.sum(axis=1))
    labels = np.empty(n, dtype=int)
    for k in range(n_colors):
        start = k * n // n_colors
        end   = (k + 1) * n // n_colors if k < n_colors - 1 else n
        labels[order[start:end]] = k
    return labels


# ── GA helpers ────────────────────────────────────────────────────────────────

def _mutate_labels(
    labels: np.ndarray, rng: np.random.Generator,
    n_colors: int, rate: float,
) -> np.ndarray:
    mask = rng.random(labels.shape[0]) < rate
    for idx in np.where(mask)[0]:
        cur = int(labels[idx])
        new = int(rng.integers(0, n_colors - 1))
        if new >= cur:
            new += 1
        labels[idx] = new
    return labels


def _mutate_population(
    pop: np.ndarray, values: np.ndarray, rng: np.random.Generator,
    n_colors: int, rate: float,
) -> np.ndarray:
    return np.stack([
        _mutate_labels(ind.copy(), rng, n_colors, rate)
        for ind in pop
    ], axis=0)


def _tournament(
    pop: np.ndarray, scores: np.ndarray, n: int,
    rng: np.random.Generator, k: int = 3,
) -> np.ndarray:
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
            offspring[i, pt:], offspring[i + 1, pt:] = (
                parents[i + 1, pt:].copy(), parents[i, pt:].copy()
            )
    return offspring


def _elitism(
    old: np.ndarray, old_s: np.ndarray, new: np.ndarray, new_s: np.ndarray,
) -> Tuple[np.ndarray, np.ndarray]:
    combined = np.vstack([old, new])
    scores   = np.concatenate([old_s, new_s])
    idx      = np.argsort(scores)[:len(old)]
    return combined[idx], scores[idx]


# ── GA BAM helpers ────────────────────────────────────────────────────────────

def _mutate_labels_bam(
    labels: np.ndarray, rng: np.random.Generator,
    n_colors: int, rate: float, knn_indices: np.ndarray,
) -> np.ndarray:
    """Boundary-Aware Mutation: Hot (high-inconsistency) games mutated more."""
    incons    = _incons_per_game(labels, knn_indices)
    mut_probs = np.clip(incons * rate * 2, 0, 1)
    mask = rng.random(labels.shape[0]) < mut_probs
    for idx in np.where(mask)[0]:
        cur = int(labels[idx])
        new = int(rng.integers(0, n_colors - 1))
        if new >= cur:
            new += 1
        labels[idx] = new
    return labels


def _mutate_population_bam(
    pop: np.ndarray, values: np.ndarray, rng: np.random.Generator,
    n_colors: int, rate: float, knn_indices: np.ndarray,
) -> np.ndarray:
    return np.stack([
        _mutate_labels_bam(ind.copy(), rng, n_colors, rate, knn_indices)
        for ind in pop
    ], axis=0)


def _knn_local_search(
    labels: np.ndarray, values: np.ndarray, cfg: dict,
    n_colors: int, knn_indices: np.ndarray, n_steps: int,
) -> np.ndarray:
    """Greedy KNN majority correction: move the most inconsistent game to its neighbours' majority cluster."""
    current_score = evaluate_partition(labels, values, cfg)
    incons = _incons_per_game(labels, knn_indices)
    hot = np.where(incons >= np.percentile(incons, 75))[0]
    for _ in range(n_steps):
        if len(hot) == 0:
            break
        idx = int(hot[np.argmax(incons[hot])])
        src = int(labels[idx])
        votes = np.bincount(labels[knn_indices[idx]], minlength=n_colors)
        votes[src] = 0
        if votes.sum() == 0:
            continue
        dst = int(np.argmax(votes))
        if src == dst:
            continue
        proposal = labels.copy()
        proposal[idx] = dst
        proposal_score = evaluate_partition(proposal, values, cfg)
        if proposal_score < current_score:
            labels = proposal
            current_score = proposal_score
            incons = _incons_per_game(labels, knn_indices)
            hot = np.where(incons >= np.percentile(incons, 75))[0]
    return labels
