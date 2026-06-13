"""
Adaptive Perturbation Strategy (APS) — v2

三個連續因子控制三種擾動效果：

  α(t) = 1 - t/T                          → 探索噪聲（前期主導，線性遞減）
  β_eff = 4·(t/T)·(1-t/T) × (n_missing/8) → 群激活修正（中期拋物線 × 激活壓力）
  γ(t) = t/T                              → 鄰域細搜（後期主導，線性遞增）

v2 核心改動：
  1. β 乘上 activation_pressure = n_missing / 8
     → 8 群全部激活後 β_eff 自動歸零，不再干擾品質收斂
  2. 方向選擇改用複合分數：
     score = empty_reduction + λ × objective_improvement
     → λ = direction_quality_weight（config 可調）
     → λ=0 等同 v1 純激活導向；λ 越大越在意品質
"""
from __future__ import annotations
import math
import numpy as np
import pandas as pd
from typing import Callable

from src.phase1.objective import assign_labels, evaluate


def make_perturb_fn(dims: pd.DataFrame, cfg: dict) -> Callable:
    """
    回傳 perturb_fn(x, t, T) -> x_new。
    dims : 正規化後的維度 DataFrame（含 appid）
    cfg  : 完整 phase1 config，讀取 adaptive 區段
    """
    acfg      = cfg.get("adaptive", {})
    sigma_exp = float(acfg.get("sigma_explore",          0.05))
    sigma_loc = float(acfg.get("sigma_local",            0.01))
    probe_eps = float(acfg.get("probe_eps",              0.02))
    levy_beta = float(acfg.get("levy_beta",              1.5))
    lam       = float(acfg.get("direction_quality_weight", 0.3))  # λ

    n_dims = len([c for c in dims.columns if c != "appid"])

    def perturb_fn(x: np.ndarray, t: int, T: int) -> np.ndarray:
        progress = t / max(T - 1, 1)

        alpha = 1.0 - progress
        beta  = 4.0 * progress * (1.0 - progress)
        gamma = progress

        x = x.copy()

        # ── α：探索噪聲 ───────────────────────────────────────────────
        x += alpha * sigma_exp * np.random.randn(n_dims)
        x  = np.clip(x, 0.0, 1.0)

        # ── β：群激活修正（乘上 activation_pressure）─────────────────
        labels   = assign_labels(x.tolist(), dims)
        n_missing = 8 - len(np.unique(labels))
        activation_pressure = n_missing / 8.0
        beta_eff = beta * activation_pressure

        if beta_eff > 1e-6 and n_missing > 0:
            current_score = evaluate(x.tolist(), dims, cfg)
            best_d, best_sign = _find_best_direction(
                x, dims, probe_eps, current_score, lam, cfg
            )
            step = best_sign * _levy(levy_beta)
            x[best_d] = np.clip(x[best_d] + beta_eff * step, 0.0, 1.0)

        # ── γ：鄰域細搜 ───────────────────────────────────────────────
        x += gamma * sigma_loc * np.random.randn(n_dims)

        return np.clip(x, 0.0, 1.0)

    return perturb_fn


# ─── 內部工具 ─────────────────────────────────────────────────────────────────

def _find_best_direction(
    x: np.ndarray,
    dims: pd.DataFrame,
    eps: float,
    current_score: float,
    lam: float,
    cfg: dict,
) -> tuple[int, float]:
    """
    對每個維度試探 ±eps，用複合分數選最佳方向：
      composite = empty_reduction + λ × objective_improvement
    平手時（composite 相同）選距離 0.5 最遠的維度。
    """
    n = len(x)
    results = []

    current_labels  = assign_labels(x.tolist(), dims)
    current_empty   = 8 - len(np.unique(current_labels))

    for d in range(n):
        for sign in (+1.0, -1.0):
            x_probe = x.copy()
            x_probe[d] = np.clip(x_probe[d] + sign * eps, 0.0, 1.0)

            probe_labels = assign_labels(x_probe.tolist(), dims)
            empty_after  = 8 - len(np.unique(probe_labels))
            empty_reduction = current_empty - empty_after          # 正 = 消滅更多空群

            probe_score = evaluate(x_probe.tolist(), dims, cfg)
            obj_improvement = current_score - probe_score          # 正 = 目標函數下降（改善）

            composite   = empty_reduction + lam * obj_improvement
            extremeness = abs(x[d] - 0.5)

            # 排序鍵：composite 越大越好（取負升序），extremeness 越大越好（平手用，取負）
            results.append((-composite, -extremeness, d, sign))

    results.sort(key=lambda r: (r[0], r[1]))
    _, _, best_d, best_sign = results[0]
    return best_d, best_sign


def _levy(beta: float = 1.5) -> float:
    """回傳一個 Levy flight 純量步長（絕對值）。"""
    sigma = (
        math.gamma(1 + beta) * math.sin(math.pi * beta / 2)
        / (math.gamma((1 + beta) / 2) * beta * 2 ** ((beta - 1) / 2))
    ) ** (1 / beta)
    u = np.random.randn() * sigma
    v = np.random.randn()
    return abs(u / (abs(v) ** (1 / beta)))
