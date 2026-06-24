"""
Density-aware weighted fitness for centroid encoding.

  M_w = Σ_k Σ_{i∈Ck}  w_i · ‖x_i − μ_k‖₂

  μ_k  = (Σ_{i∈Ck} w_i x_i) / (Σ_{i∈Ck} w_i)   (weighted centroid)

Weight derivation (density-aware):
  For each dimension j, bin all values into n_bins equal-width bins.
  Let n_ij = count of samples in the same bin as sample i along dim j.
    w_ij      = 1 / log(n_ij + 1)
    w̃_i       = (1/d) Σ_j w_ij          (combine=mean)
    w̃_i       = clip(w̃_i, w_min, w_max)
    w_i       = w̃_i / mean(w̃)           (normalize so mean=1)
"""
from __future__ import annotations
import numpy as np


def compute_density_weights(values: np.ndarray, cfg: dict) -> np.ndarray:
    fit_cfg  = cfg.get("fitness", {})
    n_bins   = int(fit_cfg.get("density_bins", 20))
    combine  = fit_cfg.get("combine", "mean")
    w_min    = float(fit_cfg.get("w_min", 0.05))
    w_max    = float(fit_cfg.get("w_max", 10.0))

    n, d = values.shape
    W = np.zeros((n, d))
    for j in range(d):
        col = values[:, j]
        lo, hi = col.min(), col.max()
        edges = np.linspace(lo, hi + 1e-9, n_bins + 1)
        bin_idx = np.clip(np.digitize(col, edges) - 1, 0, n_bins - 1)
        counts  = np.bincount(bin_idx, minlength=n_bins)
        n_ij    = counts[bin_idx]
        W[:, j] = 1.0 / np.log(n_ij + 1.0)

    if combine == "mean":
        w_tilde = W.mean(axis=1)
    else:
        w_tilde = W.mean(axis=1)

    w_tilde = np.clip(w_tilde, w_min, w_max)
    w_tilde = w_tilde / w_tilde.mean()
    return w_tilde


def make_weighted_fitness(values: np.ndarray, n_colors: int, weights: np.ndarray):
    """Return weighted fitness function for centroid encoding."""
    n_dims = values.shape[1]

    def fitness(pos: np.ndarray) -> float:
        centroids = pos.reshape(n_colors, n_dims).copy()
        dists = np.linalg.norm(
            values[:, None, :] - centroids[None, :, :], axis=2
        )  # (n, K)
        labels = np.argmin(dists, axis=1)
        # weighted centroid refinement
        for k in range(n_colors):
            mask = labels == k
            if mask.any():
                w_k = weights[mask]
                centroids[k] = (w_k[:, None] * values[mask]).sum(axis=0) / w_k.sum()
        M_w = 0.0
        for k in range(n_colors):
            mask = labels == k
            if mask.any():
                d_k = np.linalg.norm(values[mask] - centroids[k], axis=1)
                M_w += (weights[mask] * d_k).sum()
        return float(M_w)

    return fitness


def make_wdbi_fitness(values: np.ndarray, n_colors: int, weights: np.ndarray, eps: float = 1e-10):
    """Return a fitness function that minimizes WDBI (centroid encoding)."""
    n_dims = values.shape[1]

    def fitness(pos: np.ndarray) -> float:
        centroids = pos.reshape(n_colors, n_dims).copy()
        dists  = np.linalg.norm(values[:, None, :] - centroids[None, :, :], axis=2)
        labels = np.argmin(dists, axis=1)
        # weighted centroid refinement (one K-means step, consistent with M_w)
        for k in range(n_colors):
            mask = labels == k
            if mask.any():
                w_k = weights[mask]
                centroids[k] = (w_k[:, None] * values[mask]).sum(axis=0) / w_k.sum()

        unique = np.unique(labels)
        if len(unique) < 2:
            return float("inf")

        mu, s = {}, {}
        for k in unique:
            mask = labels == k
            w_k  = weights[mask]
            W_k  = w_k.sum()
            mu_k = (w_k[:, None] * values[mask]).sum(axis=0) / W_k
            d_k  = np.linalg.norm(values[mask] - mu_k, axis=1)
            mu[k] = mu_k
            s[k]  = float((w_k * d_k).sum() / W_k)

        total = 0.0
        for k in unique:
            worst = 0.0
            for j in unique:
                if j == k:
                    continue
                d_kj = float(np.linalg.norm(mu[k] - mu[j]))
                r    = (s[k] + s[j]) / max(d_kj, eps)
                if r > worst:
                    worst = r
            total += worst
        return float(total / len(unique))

    return fitness




def make_combined_fitness(values: np.ndarray, n_colors: int, cfg: dict):
    """Unweighted combined fitness: -(w_inter·inter - w_intra·intra).

    intra = global MSE = (1/N) Σ_k Σ_{i∈Ck} ‖x_i−μ_k‖²
    inter = mean pairwise distance between unweighted centroids ‖μ_k−μ_j‖

    Using global MSE instead of mean-of-per-cluster-variance prevents the
    optimizer from exploiting singleton clusters (variance=0) to cheat intra.
    """
    n_dims  = values.shape[1]
    N       = float(len(values))
    w_intra = float(cfg.get("objective", {}).get("intra_weight", 1.0))
    w_inter = float(cfg.get("objective", {}).get("inter_weight", 1.0))

    def fitness(pos: np.ndarray) -> float:
        centroids = pos.reshape(n_colors, n_dims).copy()
        dists  = np.linalg.norm(values[:, None, :] - centroids[None, :, :], axis=2)
        labels = np.argmin(dists, axis=1)
        for k in range(n_colors):
            mask = labels == k
            if mask.any():
                centroids[k] = values[mask].mean(axis=0)

        unique  = np.unique(labels)
        mu_list = []
        sse     = 0.0
        for k in unique:
            mask = labels == k
            mu_k = centroids[k]
            sse += float((np.linalg.norm(values[mask] - mu_k, axis=1) ** 2).sum())
            mu_list.append(mu_k)

        intra = sse / N

        inter   = 0.0
        n_pairs = 0
        for a in range(len(mu_list)):
            for b in range(a + 1, len(mu_list)):
                inter  += float(np.linalg.norm(mu_list[a] - mu_list[b]))
                n_pairs += 1
        inter = inter / n_pairs if n_pairs > 0 else 0.0

        return float(-(w_inter * inter - w_intra * intra))

    return fitness


def make_wcss_fitness(values: np.ndarray, n_colors: int, weights: np.ndarray):
    """Weighted WCSS: Σ_k Σ_{i∈Ck} w_i · ‖x_i − μ_k^w‖²

    Same objective as KMeans (sum of squared distances) but with density-aware
    weights and weighted centroids.
    """
    n_dims = values.shape[1]

    def fitness(pos: np.ndarray) -> float:
        centroids = pos.reshape(n_colors, n_dims).copy()
        dists  = np.linalg.norm(values[:, None, :] - centroids[None, :, :], axis=2)
        labels = np.argmin(dists, axis=1)
        for k in range(n_colors):
            mask = labels == k
            if mask.any():
                w_k = weights[mask]
                centroids[k] = (w_k[:, None] * values[mask]).sum(axis=0) / w_k.sum()
        wcss_w = 0.0
        for k in range(n_colors):
            mask = labels == k
            if mask.any():
                sq_d    = np.linalg.norm(values[mask] - centroids[k], axis=1) ** 2
                wcss_w += float((weights[mask] * sq_d).sum())
        return float(wcss_w)

    return fitness


def make_wcombined_fitness(values: np.ndarray, n_colors: int, weights: np.ndarray, cfg: dict):
    """Weighted combined fitness: -(w_inter·inter_w − w_intra·intra_w).

    intra_w = global weighted MSE = (1/Σw) Σ_k Σ_{i∈Ck} w_i‖x_i−μ_k^w‖²
    inter_w = mean pairwise distance between weighted centroids ‖μ_k^w−μ_j^w‖

    Global weighted MSE prevents singleton-cluster exploitation under high intra_weight.
    """
    n_dims   = values.shape[1]
    W_total  = float(weights.sum())
    w_intra  = float(cfg.get("objective", {}).get("intra_weight", 1.0))
    w_inter  = float(cfg.get("objective", {}).get("inter_weight", 1.0))

    def fitness(pos: np.ndarray) -> float:
        centroids = pos.reshape(n_colors, n_dims).copy()
        dists  = np.linalg.norm(values[:, None, :] - centroids[None, :, :], axis=2)
        labels = np.argmin(dists, axis=1)
        for k in range(n_colors):
            mask = labels == k
            if mask.any():
                w_k = weights[mask]
                centroids[k] = (w_k[:, None] * values[mask]).sum(axis=0) / w_k.sum()

        unique  = np.unique(labels)
        mu_list = []
        wsse    = 0.0
        for k in unique:
            mask = labels == k
            w_k  = weights[mask]
            mu_k = (w_k[:, None] * values[mask]).sum(axis=0) / w_k.sum()
            sq_d = np.linalg.norm(values[mask] - mu_k, axis=1) ** 2
            wsse += float((w_k * sq_d).sum())
            mu_list.append(mu_k)

        intra = wsse / W_total

        inter   = 0.0
        n_pairs = 0
        for a in range(len(mu_list)):
            for b in range(a + 1, len(mu_list)):
                inter  += float(np.linalg.norm(mu_list[a] - mu_list[b]))
                n_pairs += 1
        inter = inter / n_pairs if n_pairs > 0 else 0.0

        return float(-(w_inter * inter - w_intra * intra))

    return fitness


def compute_M_weighted(
    labels: np.ndarray, values: np.ndarray, weights: np.ndarray
) -> float:
    """Post-hoc M_w given pre-computed cluster labels and density weights."""
    unique = np.unique(labels)
    total  = 0.0
    for k in unique:
        mask = labels == k
        if not mask.any():
            continue
        w_k     = weights[mask]
        mu_k    = (w_k[:, None] * values[mask]).sum(axis=0) / w_k.sum()
        d_k     = np.linalg.norm(values[mask] - mu_k, axis=1)
        total  += (w_k * d_k).sum()
    return float(total)
