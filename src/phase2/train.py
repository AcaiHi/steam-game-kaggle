"""
訓練分類器並記錄至 MLflow。
支援：random_forest / xgboost / lightgbm / catboost / svm
不平衡處理：各模型內建 class_weight / auto_class_weights（見 optimize.py）
"""
from __future__ import annotations
import mlflow
import mlflow.sklearn
import numpy as np
import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import LinearSVC
from sklearn.metrics import (
    accuracy_score, f1_score, classification_report,
    average_precision_score, roc_auc_score
)
from xgboost import XGBClassifier
from lightgbm import LGBMClassifier
from catboost import CatBoostClassifier

from .optimize import optimize, _base_params


def _make_model(model_name: str):
    base = _base_params(model_name)
    if model_name == "random_forest":
        return RandomForestClassifier(**base)
    if model_name == "xgboost":
        return XGBClassifier(**base)
    if model_name == "lightgbm":
        return LGBMClassifier(**base)
    if model_name == "catboost":
        return CatBoostClassifier(**base)
    if model_name == "svm":
        return LinearSVC(**base)
    raise ValueError(f"Unknown model: {model_name}")


def run(
    X_train: pd.DataFrame,
    X_test: pd.DataFrame,
    y_train: pd.Series,
    y_test: pd.Series,
    cfg: dict,
):
    """
    Train/test split 已在 run_phase2.py 完成，直接傳入。
    target encoding 與 feature selection 也已在外部 fit，無洩漏。
    """
    models = cfg.get("models", ["random_forest"])
    if isinstance(models, str):
        models = [models]

    results = {}
    for model_name in models:
        print(f"\n[Phase 2] Training {model_name} ...")
        result = _train_one(model_name, X_train, X_test, y_train, y_test, cfg)
        results[model_name] = result

    _print_summary(results)
    return results


def _train_one(model_name, X_train, X_test, y_train, y_test, cfg):
    model = _make_model(model_name)
    opt_method = cfg.get("param_optimize", {}).get("method", "none")

    mlflow.set_tracking_uri(cfg["mlflow"]["tracking_uri"])
    mlflow.set_experiment(cfg["mlflow"]["experiment_name"])
    with mlflow.start_run(run_name=model_name):
        mlflow.log_params({
            "model":          model_name,
            "opt_method":     opt_method,
            "features":       cfg["features"],
            "selection":      cfg.get("selection", {}),
            "extraction":     cfg.get("extraction", {}),
            "n_train":        len(X_train),
            "n_test":         len(X_test),
            "n_features":     X_train.shape[1],
        })

        best_model, best_params = optimize(model, model_name, X_train, y_train, cfg)
        if best_params:
            mlflow.log_params({f"best_{k}": v for k, v in best_params.items()})

        y_pred = best_model.predict(X_test)

        # 取得 predict_proba（AP / ROC 需要機率）
        if hasattr(best_model, "predict_proba"):
            y_prob = best_model.predict_proba(X_test)
        elif hasattr(best_model, "decision_function"):
            from sklearn.preprocessing import label_binarize
            from scipy.special import softmax
            df_scores = best_model.decision_function(X_test)
            y_prob = softmax(df_scores, axis=1)
        else:
            y_prob = None

        acc         = accuracy_score(y_test, y_pred)
        f1_macro    = f1_score(y_test, y_pred, average="macro",    zero_division=0)
        f1_weighted = f1_score(y_test, y_pred, average="weighted", zero_division=0)

        metrics_dict = {"accuracy": acc, "f1_macro": f1_macro, "f1_weighted": f1_weighted}

        if y_prob is not None:
            try:
                ap  = average_precision_score(y_test, y_prob, average="macro")
                auc = roc_auc_score(y_test, y_prob, average="macro", multi_class="ovr")
                metrics_dict["ap_macro"]  = ap
                metrics_dict["roc_auc"]   = auc
            except Exception:
                ap, auc = float("nan"), float("nan")
        else:
            ap, auc = float("nan"), float("nan")

        mlflow.log_metrics(metrics_dict)

        report = classification_report(y_test, y_pred, output_dict=True, zero_division=0)
        for label, m in report.items():
            if isinstance(m, dict):
                for metric, val in m.items():
                    mlflow.log_metric(f"cls{label}_{metric}", val)

        # per-class AP
        if y_prob is not None:
            from sklearn.preprocessing import label_binarize
            classes = np.unique(y_test)
            y_bin = label_binarize(y_test, classes=classes)
            for idx, cls in enumerate(classes):
                try:
                    cls_ap = average_precision_score(y_bin[:, idx], y_prob[:, idx])
                    mlflow.log_metric(f"cls{cls}_ap", cls_ap)
                except Exception:
                    pass

        try:
            mlflow.sklearn.log_model(best_model, model_name)
        except Exception:
            pass

        print(f"  acc={acc:.4f}  f1_macro={f1_macro:.4f}  ap={ap:.4f}  roc_auc={auc:.4f}")

    return {"model": best_model, "acc": acc, "f1_macro": f1_macro,
            "f1_weighted": f1_weighted, "ap_macro": ap, "roc_auc": auc}


def _print_summary(results: dict):
    print("\n" + "=" * 55)
    print(f"{'Model':<16} {'Accuracy':>10} {'F1 Macro':>10} {'AP Macro':>10} {'ROC-AUC':>10}")
    print("-" * 62)
    for name, r in results.items():
        print(f"{name:<16} {r['acc']:>10.4f} {r['f1_macro']:>10.4f} "
              f"{r['ap_macro']:>10.4f} {r['roc_auc']:>10.4f}")
    print("=" * 62)
