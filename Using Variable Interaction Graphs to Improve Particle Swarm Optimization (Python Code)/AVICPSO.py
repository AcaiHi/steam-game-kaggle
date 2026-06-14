# -*- coding: utf-8 -*-
"""
AVIC-PSO: Adaptive VIG-Informed Clustering PSO

Extensions over standard VIGPSO:
  A. VIG-based centroid collapse detection + Lévy restart
  B. Per-particle stagnation detection + Lévy perturbation
  C. Unified linear/exponential schedule tying VIG alpha, Lévy sigma,
     and stagnation threshold together.

Reference for base VIGPSO:
    Czworkowski & Sheppard, GECCO 2025
    DOI: https://doi.org/10.1145/3712255.3726589

Lévy sampling: Mantegna (1994) approximation, beta=1.5
"""

import numpy as np
import time
from math import gamma, pi, sin
from solution import solution


# ---------------------------------------------------------------------------
# Lévy step sampler
# ---------------------------------------------------------------------------
_LEVY_BETA = 1.5
_num = gamma(1 + _LEVY_BETA) * sin(pi * _LEVY_BETA / 2)
_den = gamma((1 + _LEVY_BETA) / 2) * _LEVY_BETA * 2 ** ((_LEVY_BETA - 1) / 2)
_LEVY_SIGMA = (_num / _den) ** (1 / _LEVY_BETA)


def _levy(dim: int) -> np.ndarray:
    u = np.random.normal(0, _LEVY_SIGMA, dim)
    v = np.random.normal(0, 1, dim)
    return u / (np.abs(v) ** (1 / _LEVY_BETA))


# ---------------------------------------------------------------------------
# Main algorithm
# ---------------------------------------------------------------------------
def AVICPSO(
    objf,
    lb,
    ub,
    dim,
    SearchAgents_no,
    Max_iter,
    # --- VIGPSO base params ---
    omega=0.4,
    c1=1.5,
    c2=2.0,
    tau1=0.5,
    tau2=0.3,
    update_interval=10,
    alpha_max=0.3,
    # --- Lévy / stagnation params ---
    stagnation_thresh=15,
    levy_sigma_max=0.3,
    # --- Centroid collapse params ---
    collapse_theta=0.6,
    n_colors=None,          # must be provided for collapse detection
    warm_start_pos=None,
    restart_pool=None,      # (n_restarts, dim) K-means solutions for Axis B
):
    """
    AVIC-PSO

    Parameters
    ----------
    stagnation_thresh : int
        連續幾次迭代 pbest 未改善即觸發 Lévy 擾動。
    levy_sigma_max : float
        Lévy 步長的最大縮放係數（早期大、後期小）。
    collapse_theta : float
        VIG 中兩群心區塊的平均邊權重 > collapse_theta 時判定為 collapsed。
    n_colors : int
        群數（= dim / n_feature_dims），用於切分 VIG 偵測群心崩塌。
        若為 None 則停用 collapse detection。
    restart_pool : np.ndarray or None
        shape (n_restarts, dim)。由外部預先跑 K-means 產生的多元解池。
        停滯觸發時，粒子輪流從不同 K-means 解出發 + Lévy 擾動，
        取代原本的「全部從 gbest 擴散」策略。None 時退化為原始行為。
    """
    # ------------------------------------------------------------------
    # 邊界
    # ------------------------------------------------------------------
    if not isinstance(lb, list):
        lb = [lb] * dim
        ub = [ub] * dim
    lb = np.asarray(lb, dtype=float)
    ub = np.asarray(ub, dtype=float)
    v_max = (ub - lb) * 0.2

    # centroid 區塊大小（供 collapse detection 使用）
    n_feat = dim // n_colors if n_colors else None

    # ------------------------------------------------------------------
    # 初始化
    # ------------------------------------------------------------------
    X = lb + np.random.uniform(0, 1, (SearchAgents_no, dim)) * (ub - lb)
    if warm_start_pos is not None:
        X[0] = np.clip(warm_start_pos, lb, ub)
    V = np.zeros((SearchAgents_no, dim))

    pbest_pos = X.copy()
    pbest_val = np.array([objf(X[i]) for i in range(SearchAgents_no)])
    stag_cnt = np.zeros(SearchAgents_no, dtype=int)        # 停滯計數
    stag_pool_idx = np.zeros(SearchAgents_no, dtype=int)   # 每個粒子輪用 pool 的指標

    gbest_idx = np.argmin(pbest_val)
    gbest_pos = pbest_pos[gbest_idx].copy()
    gbest_val = pbest_val[gbest_idx]

    G = np.zeros((dim, dim))   # VIG 鄰接矩陣
    convergence_curve = np.zeros(Max_iter)

    # ------------------------------------------------------------------
    s = solution()
    print('AVICPSO is now tackling  "' + objf.__name__ + '"')
    timer_start = time.time()
    s.startTime = time.strftime("%Y-%m-%d-%H-%M-%S")

    # ------------------------------------------------------------------
    # 主迴圈
    # ------------------------------------------------------------------
    for t in range(Max_iter):
        prog = t / Max_iter
        omega_curr = omega * (1.0 - 0.6 * prog)

        # Lévy 縮放：早期大、後期收縮
        levy_sigma = levy_sigma_max * (1.0 - prog)

        X_old = X.copy()

        # --------------------------------------------------------------
        # 速度 & 位置更新（VIGPSO base + stagnation Lévy）
        # --------------------------------------------------------------
        for i in range(SearchAgents_no):
            r1 = np.random.uniform(0, 1, dim)
            r2 = np.random.uniform(0, 1, dim)

            vs = (omega_curr * V[i]
                  + c1 * r1 * (pbest_pos[i] - X[i])
                  + c2 * r2 * (gbest_pos - X[i]))

            alpha = alpha_max * (1.0 - np.exp(-2.0 * prog))

            if G.any():
                v_vig = np.zeros(dim)
                for d in range(dim):
                    neighbor_dims = np.where(G[d] > 0)[0]
                    if neighbor_dims.size > 0:
                        weights = G[d, neighbor_dims]
                        weights = weights / weights.sum()
                        v_vig[d] = np.dot(weights, vs[neighbor_dims])
                    else:
                        v_vig[d] = vs[d]
                v_new = (1.0 - alpha) * vs + alpha * v_vig
            else:
                v_new = vs

            v_new = np.clip(v_new, -v_max, v_max)

            # ----- Axis B: stagnation → K-means pool + Lévy restart -----
            if stag_cnt[i] >= stagnation_thresh:
                if restart_pool is not None:
                    # 輪流從不同 K-means 解出發，確保各粒子跳到不同區域
                    base = restart_pool[stag_pool_idx[i] % len(restart_pool)]
                    stag_pool_idx[i] += 1
                else:
                    base = gbest_pos
                step = levy_sigma * _levy(dim)
                X[i] = np.clip(base + step, lb, ub)
                V[i] = np.zeros(dim)
                stag_cnt[i] = 0
            else:
                X[i] = np.clip(X[i] + v_new, lb, ub)
                V[i] = v_new

            fitness = objf(X[i])
            if fitness < pbest_val[i]:
                pbest_val[i] = fitness
                pbest_pos[i] = X[i].copy()
                stag_cnt[i] = 0
                if fitness < gbest_val:
                    gbest_val = fitness
                    gbest_pos = X[i].copy()
            else:
                stag_cnt[i] += 1

        # --------------------------------------------------------------
        # VIG 更新
        # --------------------------------------------------------------
        if (t + 1) % update_interval == 0:
            delta_X = X - X_old
            if delta_X.std(axis=0).min() > 1e-12:
                corr_matrix = np.corrcoef(delta_X.T)
            else:
                corr_matrix = np.zeros((dim, dim))
                for di in range(dim):
                    for dj in range(di + 1, dim):
                        std_i = delta_X[:, di].std()
                        std_j = delta_X[:, dj].std()
                        if std_i > 1e-12 and std_j > 1e-12:
                            rho = np.corrcoef(delta_X[:, di], delta_X[:, dj])[0, 1]
                            corr_matrix[di, dj] = rho
                            corr_matrix[dj, di] = rho

            abs_corr = np.abs(corr_matrix)
            np.fill_diagonal(abs_corr, 0)
            G = np.where(abs_corr > tau1, abs_corr,
                         np.where(abs_corr < tau2, 0.0, G))

            # ----------------------------------------------------------
            # Axis A: centroid collapse detection + Lévy restart
            # ----------------------------------------------------------
            if n_colors is not None and n_feat is not None:
                _handle_collapse(
                    G, X, V, pbest_pos, pbest_val, stag_cnt,
                    gbest_pos, lb, ub, dim,
                    n_colors, n_feat, collapse_theta, levy_sigma, objf
                )
                # 更新 gbest（collapse restart 可能找到更好解）
                best_i = np.argmin(pbest_val)
                if pbest_val[best_i] < gbest_val:
                    gbest_val = pbest_val[best_i]
                    gbest_pos = pbest_pos[best_i].copy()

        convergence_curve[t] = gbest_val

        if t % 1 == 0:
            print(['At iteration ' + str(t)
                   + ' the best fitness is ' + str(gbest_val)])

    # ------------------------------------------------------------------
    timer_end = time.time()
    s.endTime = time.strftime("%Y-%m-%d-%H-%M-%S")
    s.executionTime = timer_end - timer_start
    s.convergence = convergence_curve
    s.optimizer = "AVICPSO"
    s.objfname = objf.__name__
    s.best = gbest_val
    s.bestIndividual = gbest_pos

    return s


# ---------------------------------------------------------------------------
# Collapse detection helper
# ---------------------------------------------------------------------------
def _handle_collapse(
    G, X, V, pbest_pos, pbest_val, stag_cnt,
    gbest_pos, lb, ub, dim,
    n_colors, n_feat, collapse_theta, levy_sigma, objf,
):
    """
    掃描所有群心對 (i, j)：
      - 取兩個群心對應的 VIG 區塊（n_feat × n_feat 子矩陣）
      - 若平均邊權重 > collapse_theta，判定為 collapsed
      - 保留 fitness 較低的群心；另一個以 Lévy 步從 gbest 重生
      - 更新所有粒子中含有該群心的 pbest
    """
    for ci in range(n_colors):
        for cj in range(ci + 1, n_colors):
            di = slice(ci * n_feat, (ci + 1) * n_feat)
            dj = slice(cj * n_feat, (cj + 1) * n_feat)
            block = G[di, dj]
            if block.mean() < collapse_theta:
                continue

            # 以 gbest 的兩個群心 fitness 代理比較
            # 找到 pbest 中這兩個群心最能代表的粒子
            best_ci_val = np.inf
            best_cj_val = np.inf
            for p in range(len(X)):
                vi = pbest_val[p]
                if vi < best_ci_val:
                    best_ci_val = vi
                if vi < best_cj_val:
                    best_cj_val = vi

            # 保留品質較好的，對較差的群心做 Lévy restart
            restart_slice = dj if best_ci_val <= best_cj_val else di
            step = levy_sigma * _levy(n_feat)
            new_centroid = np.clip(
                gbest_pos[restart_slice] + step,
                lb[restart_slice],
                ub[restart_slice],
            )

            # 套用到所有粒子與 pbest（讓整個群重新探索）
            for p in range(len(X)):
                X[p][restart_slice] = new_centroid + np.random.normal(
                    0, 0.05, n_feat
                ).clip(-0.1, 0.1)
                X[p] = np.clip(X[p], lb, ub)
                V[p][restart_slice] = 0.0
                fit = objf(X[p])
                if fit < pbest_val[p]:
                    pbest_val[p] = fit
                    pbest_pos[p] = X[p].copy()
                stag_cnt[p] = 0
