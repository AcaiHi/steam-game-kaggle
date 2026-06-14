# -*- coding: utf-8 -*-
"""
Variable Interaction Graph Particle Swarm Optimization (VIGPSO)

Main paper:
    Using Variable Interaction Graphs to Improve Particle Swarm Optimization
    Caz L. Czworkowski, John W. Sheppard
    GECCO 2025 (Extended version)
    DOI: https://doi.org/10.1145/3712255.3726589

Implementation style follows HHO.py by Ali Asghar Heidari, Hossam Faris.
"""

import numpy as np
import time
from solution import solution


def VIGPSO(objf, lb, ub, dim, SearchAgents_no, Max_iter, warm_start_pos=None,
           omega=0.4,
           c1=1.5,
           c2=2.0,
           tau1=0.5,
           tau2=0.3,
           update_interval=10,
           alpha_max=0.3):
    """
    VIGPSO - Variable Interaction Graph Particle Swarm Optimization

    Parameters
    ----------
    objf             : callable
        目標函數（最小化），接受 numpy 1-D 陣列，回傳純量。
    lb               : float or list
        搜尋空間下界。
    ub               : float or list
        搜尋空間上界。
    dim              : int
        問題維度數。
    SearchAgents_no  : int
        粒子數量。
    Max_iter         : int
        最大迭代次數。
    omega            : float
        慣性權重 (論文建議低維 0.4)，預設 0.4。
    c1               : float
        個人學習因子，預設 1.5。
    c2               : float
        社會學習因子，預設 2.0。
    tau1             : float
        VIG 邊新增門檻（Pearson |rho| > tau1 才寫入），預設 0.5。
    tau2             : float
        VIG 邊修剪門檻（Pearson |rho| < tau2 則清除），預設 0.3。
    update_interval  : int
        每幾次迭代更新一次 VIG，預設 10。
    alpha_max        : float
        VIG 速度分量的最大混合比例上限，論文取 0.3，預設 0.3。

    Returns
    -------
    s : solution
        包含收斂曲線、最佳解等資訊的物件。
    """

    # ------------------------------------------------------------------
    # 邊界處理
    # ------------------------------------------------------------------
    if not isinstance(lb, list):
        lb = [lb] * dim
        ub = [ub] * dim
    lb = np.asarray(lb, dtype=float)
    ub = np.asarray(ub, dtype=float)

    v_max = (ub - lb) * 0.2   # 速度裁切上界（各維度獨立）

    # ------------------------------------------------------------------
    # Step 1：初始化粒子位置、速度、pbest、gbest
    # ------------------------------------------------------------------
    X = lb + np.random.uniform(0, 1, (SearchAgents_no, dim)) * (ub - lb)
    if warm_start_pos is not None:
        X[0] = np.clip(warm_start_pos, lb, ub)
    V = np.zeros((SearchAgents_no, dim))

    pbest_pos = X.copy()
    pbest_val = np.array([objf(X[i]) for i in range(SearchAgents_no)])

    gbest_idx = np.argmin(pbest_val)
    gbest_pos = pbest_pos[gbest_idx].copy()
    gbest_val = pbest_val[gbest_idx]

    # ------------------------------------------------------------------
    # Step 2：初始化 VIG — 零權重鄰接矩陣 G (dim x dim)
    # ------------------------------------------------------------------
    G = np.zeros((dim, dim))

    convergence_curve = np.zeros(Max_iter)

    # ------------------------------------------------------------------
    # 計時與 solution 物件
    # ------------------------------------------------------------------
    s = solution()
    print('VIGPSO is now tackling  "' + objf.__name__ + '"')
    timer_start = time.time()
    s.startTime = time.strftime("%Y-%m-%d-%H-%M-%S")

    # ------------------------------------------------------------------
    # 主迴圈
    # ------------------------------------------------------------------
    for t in range(Max_iter):

        prog = t / Max_iter                              # 相對進度 [0, 1)

        # 線性遞減慣性權重（論文 Step 4）
        omega_curr = omega * (1.0 - 0.6 * prog)

        # 記錄迭代前位置，供 VIG 更新使用（Step 5）
        X_old = X.copy()

        # --------------------------------------------------------------
        # Step 6-23：粒子速度 & 位置更新
        # --------------------------------------------------------------
        for i in range(SearchAgents_no):

            r1 = np.random.uniform(0, 1, dim)
            r2 = np.random.uniform(0, 1, dim)

            # 標準 PSO 速度（Step 8）
            vs = (omega_curr * V[i]
                  + c1 * r1 * (pbest_pos[i] - X[i])
                  + c2 * r2 * (gbest_pos - X[i]))

            # VIG 速度分量（Step 9-18）
            # alpha 隨迭代增長，平滑過渡：論文公式 alpha = 0.3(1 - e^{-2*prog})
            alpha = alpha_max * (1.0 - np.exp(-2.0 * prog))

            neighbors = np.where(G[np.arange(dim), :].any(axis=1))[0]

            if neighbors.size > 0:
                # 對每個維度 d，取其在 G 中有邊的鄰居維度加權平均速度
                v_vig = np.zeros(dim)
                for d in range(dim):
                    neighbor_dims = np.where(G[d] > 0)[0]
                    if neighbor_dims.size > 0:
                        weights = G[d, neighbor_dims]                # 邊權重
                        weights = weights / weights.sum()             # 正規化
                        v_vig[d] = np.dot(weights, vs[neighbor_dims])
                    else:
                        v_vig[d] = vs[d]

                v_new = (1.0 - alpha) * vs + alpha * v_vig
            else:
                # VIG 尚無有效邊，直接使用標準 PSO 速度
                v_new = vs

            # 速度裁切（Step 19）
            v_new = np.clip(v_new, -v_max, v_max)

            # 位置更新（Step 21）
            X[i] = np.clip(X[i] + v_new, lb, ub)
            V[i] = v_new

            # 更新 pbest、gbest（Step 22）
            fitness = objf(X[i])
            if fitness < pbest_val[i]:
                pbest_val[i] = fitness
                pbest_pos[i] = X[i].copy()
                if fitness < gbest_val:
                    gbest_val = fitness
                    gbest_pos = X[i].copy()

        # --------------------------------------------------------------
        # Step 24-34：每 update_interval 次迭代更新 VIG
        # --------------------------------------------------------------
        if (t + 1) % update_interval == 0:
            delta_X = X - X_old                              # 粒子位移 (S x dim)

            # 計算各維度對之間的 Pearson 相關係數
            # delta_X 形狀為 (S, dim)，對 dim 兩兩計算
            # np.corrcoef 以行為變數，需轉置
            if delta_X.std(axis=0).min() > 1e-12:           # 避免常數列除零
                corr_matrix = np.corrcoef(delta_X.T)        # (dim x dim)
            else:
                # 逐對計算，跳過標準差為零的維度
                corr_matrix = np.zeros((dim, dim))
                for di in range(dim):
                    for dj in range(di + 1, dim):
                        std_i = delta_X[:, di].std()
                        std_j = delta_X[:, dj].std()
                        if std_i > 1e-12 and std_j > 1e-12:
                            rho = np.corrcoef(delta_X[:, di],
                                              delta_X[:, dj])[0, 1]
                            corr_matrix[di, dj] = rho
                            corr_matrix[dj, di] = rho

            abs_corr = np.abs(corr_matrix)
            np.fill_diagonal(abs_corr, 0)                   # 自身不加邊

            # 依門檻更新 VIG（Step 28-32）
            G = np.where(abs_corr > tau1, abs_corr,
                np.where(abs_corr < tau2, 0.0, G))

        convergence_curve[t] = gbest_val

        if t % 1 == 0:
            print(['At iteration ' + str(t)
                   + ' the best fitness is ' + str(gbest_val)])

    # ------------------------------------------------------------------
    # 整理回傳結果
    # ------------------------------------------------------------------
    timer_end = time.time()
    s.endTime = time.strftime("%Y-%m-%d-%H-%M-%S")
    s.executionTime = timer_end - timer_start
    s.convergence = convergence_curve
    s.optimizer = "VIGPSO"
    s.objfname = objf.__name__
    s.best = gbest_val
    s.bestIndividual = gbest_pos

    return s