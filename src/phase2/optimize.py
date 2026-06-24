"""
Hyperparameter optimization strategies — 可抽換設計。

支援：
  optuna       : Bayesian optimization（推薦，效率最高）
  random       : RandomizedSearchCV
  grid         : GridSearchCV
  none         : 直接用預設參數

config 範例（phase2.yaml）：
  param_optimize:
    method: optuna
    n_trials: 50        # optuna 用
    cv: 3               # cross-validation folds
    timeout: 300        # optuna 最大秒數（選填）
"""

from __future__ import annotations
import numpy as np
from typing import Any
from sklearn.model_selection import RandomizedSearchCV, GridSearchCV, StratifiedKFold
from sklearn.utils.class_weight import compute_sample_weight

# ── 各模型的超參數搜索空間 ────────────────────────────────────────────────────

PARAM_SPACES = {
    "random_forest": {
        "n_estimators":      [100, 200, 300, 500],
        "max_depth":         [None, 10, 20, 30],
        "min_samples_split": [2, 5, 10],
        "min_samples_leaf":  [1, 2, 4],
        "max_features":      ["sqrt", "log2"],
    },
    "xgboost": {
        "n_estimators":  [100, 200, 300],
        "max_depth":     [3, 5, 7, 9],
        "learning_rate": [0.01, 0.05, 0.1, 0.2],
        "subsample":     [0.6, 0.8, 1.0],
        "colsample_bytree": [0.6, 0.8, 1.0],
        "reg_alpha":     [0, 0.1, 1.0],
        "reg_lambda":    [1.0, 2.0, 5.0],
    },
    "lightgbm": {
        "n_estimators":   [100, 200, 300, 500],
        "max_depth":      [-1, 5, 10, 15],
        "learning_rate":  [0.01, 0.05, 0.1, 0.2],
        "num_leaves":     [31, 63, 127],
        "subsample":      [0.6, 0.8, 1.0],
        "colsample_bytree": [0.6, 0.8, 1.0],
        "reg_alpha":      [0, 0.1, 1.0],
        "reg_lambda":     [0, 0.1, 1.0],
    },
    "catboost": {
        "iterations":    [100, 200, 300],
        "depth":         [4, 6, 8, 10],
        "learning_rate": [0.01, 0.05, 0.1, 0.2],
        "l2_leaf_reg":   [1, 3, 5, 10],
        "bagging_temperature": [0.0, 0.5, 1.0],
    },
    "svm": {
        "C":     [0.01, 0.1, 1.0, 10.0],
        "max_iter": [1000, 2000],
    },
}

# ── Optuna 搜索空間（連續範圍，比離散更有效） ─────────────────────────────────

def _optuna_space(trial, model_name: str) -> dict:
    import optuna
    if model_name == "random_forest":
        return {
            "n_estimators":      trial.suggest_int("n_estimators", 100, 500),
            "max_depth":         trial.suggest_int("max_depth", 5, 30),
            "min_samples_split": trial.suggest_int("min_samples_split", 2, 10),
            "min_samples_leaf":  trial.suggest_int("min_samples_leaf", 1, 4),
            "max_features":      trial.suggest_categorical("max_features", ["sqrt", "log2"]),
        }
    if model_name == "xgboost":
        return {
            "n_estimators":      trial.suggest_int("n_estimators", 100, 500),
            "max_depth":         trial.suggest_int("max_depth", 3, 10),
            "learning_rate":     trial.suggest_float("learning_rate", 0.01, 0.3, log=True),
            "subsample":         trial.suggest_float("subsample", 0.5, 1.0),
            "colsample_bytree":  trial.suggest_float("colsample_bytree", 0.5, 1.0),
            "reg_alpha":         trial.suggest_float("reg_alpha", 1e-3, 10.0, log=True),
            "reg_lambda":        trial.suggest_float("reg_lambda", 1e-3, 10.0, log=True),
        }
    if model_name == "lightgbm":
        return {
            "n_estimators":      trial.suggest_int("n_estimators", 100, 500),
            "max_depth":         trial.suggest_int("max_depth", 3, 15),
            "learning_rate":     trial.suggest_float("learning_rate", 0.01, 0.3, log=True),
            "num_leaves":        trial.suggest_int("num_leaves", 20, 150),
            "subsample":         trial.suggest_float("subsample", 0.5, 1.0),
            "colsample_bytree":  trial.suggest_float("colsample_bytree", 0.5, 1.0),
            "reg_alpha":         trial.suggest_float("reg_alpha", 1e-3, 10.0, log=True),
            "reg_lambda":        trial.suggest_float("reg_lambda", 1e-3, 10.0, log=True),
        }
    if model_name == "catboost":
        return {
            "iterations":         trial.suggest_int("iterations", 100, 400),
            "depth":              trial.suggest_int("depth", 4, 10),
            "learning_rate":      trial.suggest_float("learning_rate", 0.01, 0.3, log=True),
            "l2_leaf_reg":        trial.suggest_float("l2_leaf_reg", 1.0, 10.0),
            "bagging_temperature":trial.suggest_float("bagging_temperature", 0.0, 1.0),
        }
    if model_name == "svm":
        return {
            "C": trial.suggest_float("C", 1e-2, 100.0, log=True),
        }
    raise ValueError(f"No Optuna space for model: {model_name}")


# ── 公開介面 ─────────────────────────────────────────────────────────────────

def optimize(model, model_name: str, X, y, cfg: dict):
    """
    依 cfg['param_optimize']['method'] 選擇策略，
    回傳 fit 好的最佳模型與最佳參數 dict。
    """
    opt_cfg  = cfg.get("param_optimize", {})
    method   = opt_cfg.get("method", "none")
    cv_folds = opt_cfg.get("cv", 3)
    cv       = StratifiedKFold(n_splits=cv_folds, shuffle=True, random_state=42)

    if method == "none":
        fit_kwargs = _fit_kwargs(model_name, y)
        model.fit(X, y, **fit_kwargs)
        return model, {}

    if method == "optuna":
        return _run_optuna(model, model_name, X, y, cv, opt_cfg)

    if method == "random":
        return _run_random(model, model_name, X, y, cv, opt_cfg)

    if method == "grid":
        return _run_grid(model, model_name, X, y, cv)

    raise ValueError(f"Unknown param_optimize method: {method}")


def _run_optuna(model, model_name, X, y, cv, opt_cfg):
    import optuna
    optuna.logging.set_verbosity(optuna.logging.WARNING)

    n_trials = opt_cfg.get("n_trials", 30)
    timeout  = opt_cfg.get("timeout", None)

    from sklearn.model_selection import cross_val_score

    def objective(trial):
        params = _optuna_space(trial, model_name)
        m = model.__class__(**{**_base_params(model_name), **params})
        scores = cross_val_score(m, X, y, cv=cv, scoring="f1_macro", n_jobs=-1)
        return scores.mean()

    study = optuna.create_study(direction="maximize")
    study.optimize(objective, n_trials=n_trials, timeout=timeout, show_progress_bar=True)

    best_params = {**_base_params(model_name), **study.best_params}
    best_model = model.__class__(**best_params)
    best_model.fit(X, y, **_fit_kwargs(model_name, y))
    return best_model, study.best_params


def _run_random(model, model_name, X, y, cv, opt_cfg):
    n_iter = opt_cfg.get("n_trials", 30)
    fit_params = {f"estimator__{k}": v for k, v in _fit_kwargs(model_name, y).items()}
    search = RandomizedSearchCV(
        model, PARAM_SPACES[model_name],
        n_iter=n_iter, cv=cv, scoring="f1_macro",
        random_state=42, n_jobs=-1, verbose=0,
    )
    search.fit(X, y, **fit_params)
    return search.best_estimator_, search.best_params_


def _run_grid(model, model_name, X, y, cv):
    fit_params = {f"estimator__{k}": v for k, v in _fit_kwargs(model_name, y).items()}
    search = GridSearchCV(
        model, PARAM_SPACES[model_name],
        cv=cv, scoring="f1_macro",
        n_jobs=-1, verbose=0,
    )
    search.fit(X, y, **fit_params)
    return search.best_estimator_, search.best_params_


def _fit_kwargs(model_name: str, y) -> dict:
    """XGBoost 需要透過 sample_weight 傳入 class weight。"""
    if model_name == "xgboost":
        return {"sample_weight": compute_sample_weight("balanced", y)}
    return {}


def _base_params(model_name: str) -> dict:
    """各模型固定不調的基礎參數（random_state、class_weight 等）。"""
    base = {"random_state": 42}
    if model_name in ("random_forest",):
        base["class_weight"] = "balanced"
    if model_name == "xgboost":
        base.pop("random_state")
        base["seed"] = 42
        base["eval_metric"] = "mlogloss"
    if model_name == "lightgbm":
        base["class_weight"] = "balanced"
        base["verbose"] = -1
    if model_name == "catboost":
        base.pop("random_state")
        base["random_seed"] = 42
        base["verbose"] = 0
        base["auto_class_weights"] = "Balanced"
    if model_name == "svm":
        base["class_weight"] = "balanced"
        base["max_iter"] = 2000
    return base
