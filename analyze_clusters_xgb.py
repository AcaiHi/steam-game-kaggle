"""
Cluster 分析：XGBoost + LightGBM + AdaBoost + Optuna + 特徵選取 + SHAP + 多項分析
用法：python analyze_clusters_xgb.py [--labels-csv outputs/labels_avicpso.csv]
"""
import argparse
import warnings
import os
import json
warnings.filterwarnings("ignore")
os.makedirs("outputs/analysis", exist_ok=True)

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

from sklearn.model_selection import (
    train_test_split, StratifiedKFold, cross_val_score, learning_curve
)
from sklearn.metrics import (
    classification_report, confusion_matrix, f1_score,
    accuracy_score, ConfusionMatrixDisplay,
    roc_curve, auc, precision_recall_curve, average_precision_score,
)
from sklearn.preprocessing import label_binarize
from sklearn.feature_selection import SelectKBest, mutual_info_classif
from sklearn.ensemble import AdaBoostClassifier
from sklearn.tree import DecisionTreeClassifier
from xgboost import XGBClassifier
from lightgbm import LGBMClassifier
import optuna
optuna.logging.set_verbosity(optuna.logging.WARNING)

from src.data import load_raw, load_phase2_features, filter_phase1_samples
from src.phase2.pipeline import (
    build_base_features, fit_transform_text,
    fit_transform_categorical, fit_transform_selection,
)

PHASE1_DIMS = {"rating", "playtime", "hotness"}   # 禁止使用的三維度
SAVE_DIR = "outputs/analysis"


# ══════════════════════════════════════════════════════════════════
# 資料載入
# ══════════════════════════════════════════════════════════════════

def load_data(labels_csv: str):
    labels_df = pd.read_csv(labels_csv)[["appid", "cluster_id"]]

    df_raw = load_raw()
    df_raw = df_raw.merge(labels_df, on="appid", how="inner")

    # 移除三個 Phase 1 維度欄位（leakage）
    df_feat = load_phase2_features(df_raw)
    for col in list(PHASE1_DIMS):
        if col in df_feat.columns:
            df_feat = df_feat.drop(columns=[col])

    y = df_raw["cluster_id"].astype(int)
    print(f"Samples: {len(y):,}  |  Classes: {sorted(y.unique())}")
    print(f"Class distribution:\n{y.value_counts().sort_index()}\n")
    return df_raw, df_feat, y


def build_features(df_raw, df_feat, y, cfg):
    base_cfg = {
        "features": {"numerical": True, "temporal": True, "categorical": True, "text": False},
        "categorical": {"smoothing": 1.0},
        "selection": {"method": "mutual_info", "k": 50},
    }
    base_cfg.update(cfg)

    idx_train, idx_test = train_test_split(
        np.arange(len(y)), test_size=0.2, random_state=42, stratify=y
    )

    df_tr = df_feat.iloc[idx_train].reset_index(drop=True)
    df_te = df_feat.iloc[idx_test].reset_index(drop=True)
    df_raw_tr = df_raw.iloc[idx_train].reset_index(drop=True)
    df_raw_te = df_raw.iloc[idx_test].reset_index(drop=True)
    y_tr = y.iloc[idx_train].reset_index(drop=True)
    y_te = y.iloc[idx_test].reset_index(drop=True)

    base_tr = build_base_features(df_raw_tr, base_cfg)
    base_te = build_base_features(df_raw_te, base_cfg)

    cat_tr, cat_te = fit_transform_categorical(df_raw_tr, df_raw_te, y_tr, base_cfg)

    X_tr = pd.concat([base_tr, cat_tr], axis=1).fillna(0)
    X_te = pd.concat([base_te, cat_te], axis=1).fillna(0)

    # 特徵選取：mutual_info Top-50
    k = min(50, X_tr.shape[1])
    sel = SelectKBest(mutual_info_classif, k=k)
    sel.fit(X_tr, y_tr)
    sel_cols = X_tr.columns[sel.get_support()].tolist()
    X_tr = X_tr[sel_cols]
    X_te = X_te[sel_cols]

    print(f"Features after selection: {len(sel_cols)}")
    print(f"Train: {len(y_tr):,}  |  Test: {len(y_te):,}\n")
    return X_tr, X_te, y_tr, y_te, sel_cols


# ══════════════════════════════════════════════════════════════════
# 模型訓練 + Optuna
# ══════════════════════════════════════════════════════════════════

def _cv(n_splits=5):
    return StratifiedKFold(n_splits=n_splits, shuffle=True, random_state=42)


def train_xgb(X_tr, y_tr, n_trials=50):
    def objective(trial):
        params = {
            "n_estimators":     trial.suggest_int("n_estimators", 100, 500),
            "max_depth":        trial.suggest_int("max_depth", 3, 10),
            "learning_rate":    trial.suggest_float("learning_rate", 0.01, 0.3, log=True),
            "subsample":        trial.suggest_float("subsample", 0.5, 1.0),
            "colsample_bytree": trial.suggest_float("colsample_bytree", 0.5, 1.0),
            "reg_alpha":        trial.suggest_float("reg_alpha", 1e-3, 10.0, log=True),
            "reg_lambda":       trial.suggest_float("reg_lambda", 1e-3, 10.0, log=True),
            "seed": 42, "eval_metric": "mlogloss", "verbosity": 0, "n_jobs": -1,
        }
        m = XGBClassifier(**params)
        return cross_val_score(m, X_tr, y_tr, cv=_cv(), scoring="f1_macro", n_jobs=-1).mean()

    study = optuna.create_study(direction="maximize")
    study.optimize(objective, n_trials=n_trials, show_progress_bar=True)
    print(f"\n[XGB] Best params: {study.best_params}")
    print(f"[XGB] Best CV F1-macro: {study.best_value:.4f}\n")
    best = XGBClassifier(**study.best_params, seed=42, eval_metric="mlogloss", verbosity=0, n_jobs=-1)
    best.fit(X_tr, y_tr)
    return best, study


def train_lgbm(X_tr, y_tr, n_trials=50):
    def objective(trial):
        params = {
            "n_estimators":     trial.suggest_int("n_estimators", 100, 500),
            "max_depth":        trial.suggest_int("max_depth", 3, 15),
            "learning_rate":    trial.suggest_float("learning_rate", 0.01, 0.3, log=True),
            "num_leaves":       trial.suggest_int("num_leaves", 20, 150),
            "subsample":        trial.suggest_float("subsample", 0.5, 1.0),
            "colsample_bytree": trial.suggest_float("colsample_bytree", 0.5, 1.0),
            "reg_alpha":        trial.suggest_float("reg_alpha", 1e-3, 10.0, log=True),
            "reg_lambda":       trial.suggest_float("reg_lambda", 1e-3, 10.0, log=True),
            "random_state": 42, "verbose": -1, "n_jobs": -1,
        }
        m = LGBMClassifier(**params)
        return cross_val_score(m, X_tr, y_tr, cv=_cv(), scoring="f1_macro", n_jobs=-1).mean()

    study = optuna.create_study(direction="maximize")
    study.optimize(objective, n_trials=n_trials, show_progress_bar=True)
    print(f"\n[LGBM] Best params: {study.best_params}")
    print(f"[LGBM] Best CV F1-macro: {study.best_value:.4f}\n")
    best = LGBMClassifier(**study.best_params, random_state=42, verbose=-1, n_jobs=-1)
    best.fit(X_tr, y_tr)
    return best, study


def train_adaboost(X_tr, y_tr, n_trials=30):
    def objective(trial):
        params = {
            "n_estimators":  trial.suggest_int("n_estimators", 50, 300),
            "learning_rate": trial.suggest_float("learning_rate", 0.01, 1.0, log=True),
            "estimator": DecisionTreeClassifier(
                max_depth=trial.suggest_int("max_depth", 1, 5),
            ),
        }
        m = AdaBoostClassifier(**params, random_state=42, algorithm="SAMME")
        return cross_val_score(m, X_tr, y_tr, cv=_cv(), scoring="f1_macro", n_jobs=-1).mean()

    study = optuna.create_study(direction="maximize")
    study.optimize(objective, n_trials=n_trials, show_progress_bar=True)
    best_p = study.best_params
    print(f"\n[AdaBoost] Best params: {best_p}")
    print(f"[AdaBoost] Best CV F1-macro: {study.best_value:.4f}\n")
    best = AdaBoostClassifier(
        n_estimators=best_p["n_estimators"],
        learning_rate=best_p["learning_rate"],
        estimator=DecisionTreeClassifier(max_depth=best_p["max_depth"]),
        random_state=42, algorithm="SAMME",
    )
    best.fit(X_tr, y_tr)
    return best, study


# ══════════════════════════════════════════════════════════════════
# 分析圖表
# ══════════════════════════════════════════════════════════════════

def plot_confusion_matrix(model, X_te, y_te, tag="model"):
    y_pred = model.predict(X_te)
    cm = confusion_matrix(y_te, y_pred)
    classes = sorted(y_te.unique())

    fig, ax = plt.subplots(figsize=(max(6, len(classes)), max(5, len(classes) - 1)))
    disp = ConfusionMatrixDisplay(cm, display_labels=[f"C{c}" for c in classes])
    disp.plot(ax=ax, colorbar=True, cmap="Blues")
    ax.set_title(f"Confusion Matrix — {tag}", fontsize=13, fontweight="bold")
    plt.tight_layout()
    _save(f"confusion_matrix_{tag}.png", fig)


def plot_feature_importance(model, feature_names, tag="model"):
    imp = pd.Series(model.feature_importances_, index=feature_names).sort_values(ascending=True)
    top = imp.tail(30)

    fig, ax = plt.subplots(figsize=(8, max(6, len(top) * 0.3)))
    top.plot(kind="barh", ax=ax, color="steelblue")
    ax.set_title(f"{tag} Feature Importance", fontsize=13, fontweight="bold")
    ax.set_xlabel("Importance")
    plt.tight_layout()
    _save(f"feature_importance_{tag}.png", fig)


def plot_learning_curve(model, X_tr, y_tr, tag="model"):
    train_sizes, train_scores, val_scores = learning_curve(
        model, X_tr, y_tr,
        cv=StratifiedKFold(5, shuffle=True, random_state=42),
        scoring="f1_macro", n_jobs=-1,
        train_sizes=np.linspace(0.1, 1.0, 8),
    )
    fig, ax = plt.subplots(figsize=(8, 5))
    ax.plot(train_sizes, train_scores.mean(1), "o-", label="Train", color="steelblue")
    ax.fill_between(train_sizes,
                    train_scores.mean(1) - train_scores.std(1),
                    train_scores.mean(1) + train_scores.std(1), alpha=0.15, color="steelblue")
    ax.plot(train_sizes, val_scores.mean(1), "o-", label="Validation", color="coral")
    ax.fill_between(train_sizes,
                    val_scores.mean(1) - val_scores.std(1),
                    val_scores.mean(1) + val_scores.std(1), alpha=0.15, color="coral")
    ax.set_title(f"Learning Curve — {tag} (F1-macro)", fontsize=13, fontweight="bold")
    ax.set_xlabel("Training samples")
    ax.set_ylabel("F1-macro")
    ax.legend()
    plt.tight_layout()
    _save(f"learning_curve_{tag}.png", fig)


def plot_class_distribution(y_tr, y_te):
    classes = sorted(set(y_tr) | set(y_te))
    tr_cnt = y_tr.value_counts().reindex(classes, fill_value=0)
    te_cnt = y_te.value_counts().reindex(classes, fill_value=0)

    x = np.arange(len(classes))
    fig, ax = plt.subplots(figsize=(max(6, len(classes)), 4))
    ax.bar(x - 0.2, tr_cnt.values, 0.4, label="Train", color="steelblue", alpha=0.8)
    ax.bar(x + 0.2, te_cnt.values, 0.4, label="Test",  color="coral",     alpha=0.8)
    ax.set_xticks(x)
    ax.set_xticklabels([f"C{c}" for c in classes])
    ax.set_title("Class Distribution (Train vs Test)", fontsize=13, fontweight="bold")
    ax.set_ylabel("Count")
    ax.legend()
    plt.tight_layout()
    _save("class_distribution.png", fig)


def plot_per_class_f1(model, X_te, y_te, tag="model"):
    y_pred = model.predict(X_te)
    report = classification_report(y_te, y_pred, output_dict=True)
    classes = sorted(y_te.unique())
    f1s = [report.get(str(c), {}).get("f1-score", 0) for c in classes]
    supports = [report.get(str(c), {}).get("support", 0) for c in classes]

    fig, ax1 = plt.subplots(figsize=(max(6, len(classes)), 4))
    ax2 = ax1.twinx()
    ax1.bar([f"C{c}" for c in classes], f1s, color="steelblue", alpha=0.8)
    ax2.plot([f"C{c}" for c in classes], supports, "o--", color="coral", label="Support")
    ax1.set_ylim(0, 1.05)
    ax1.set_ylabel("F1-score", color="steelblue")
    ax2.set_ylabel("Support", color="coral")
    ax1.set_title(f"Per-class F1-score & Support — {tag}", fontsize=13, fontweight="bold")
    ax2.legend(loc="upper right")
    plt.tight_layout()
    _save(f"per_class_f1_{tag}.png", fig)


def plot_optuna_history(study, tag="model"):
    trials = [t for t in study.trials if t.value is not None]
    values = [t.value for t in trials]
    best_so_far = np.maximum.accumulate(values)

    fig, axes = plt.subplots(1, 2, figsize=(12, 4))
    axes[0].plot(values, "o", alpha=0.4, markersize=4, color="steelblue")
    axes[0].plot(best_so_far, "-", color="coral", linewidth=2)
    axes[0].set_title(f"Optuna Trial History — {tag}", fontsize=12, fontweight="bold")
    axes[0].set_xlabel("Trial")
    axes[0].set_ylabel("F1-macro")

    importances = optuna.importance.get_param_importances(study)
    imp_df = pd.Series(importances).sort_values(ascending=True)
    imp_df.plot(kind="barh", ax=axes[1], color="steelblue")
    axes[1].set_title(f"Optuna Hyperparameter Importance — {tag}", fontsize=12, fontweight="bold")
    axes[1].set_xlabel("Importance")
    plt.tight_layout()
    _save(f"optuna_history_{tag}.png", fig)


# ══════════════════════════════════════════════════════════════════
# SHAP 分析
# ══════════════════════════════════════════════════════════════════

# ══════════════════════════════════════════════════════════════════
# 其他分析
# ══════════════════════════════════════════════════════════════════

def plot_prediction_confidence(model, X_te, y_te, tag="model"):
    """預測機率分布 — 正確 vs 錯誤預測的信心分布。"""
    probs = model.predict_proba(X_te)
    max_prob = probs.max(axis=1)
    y_pred = model.predict(X_te)
    correct = y_pred == y_te.values

    fig, ax = plt.subplots(figsize=(8, 4))
    ax.hist(max_prob[correct],  bins=30, alpha=0.6, label="Correct",   color="steelblue")
    ax.hist(max_prob[~correct], bins=30, alpha=0.6, label="Incorrect", color="coral")
    ax.set_title(f"Prediction Confidence — {tag}", fontsize=13, fontweight="bold")
    ax.set_xlabel("Max predicted probability")
    ax.set_ylabel("Count")
    ax.legend()
    plt.tight_layout()
    _save(f"prediction_confidence_{tag}.png", fig)


def plot_roc_auc(model, X_te, y_te, tag="model"):
    """One-vs-Rest ROC curve，每個 cluster 一條線，顯示 macro AUC。"""
    classes = sorted(y_te.unique())
    y_bin = label_binarize(y_te, classes=classes)
    probs = model.predict_proba(X_te)

    colors = plt.cm.tab10(np.linspace(0, 1, len(classes)))
    fig, ax = plt.subplots(figsize=(7, 6))

    aucs = []
    for i, (cls, color) in enumerate(zip(classes, colors)):
        fpr, tpr, _ = roc_curve(y_bin[:, i], probs[:, i])
        roc_auc = auc(fpr, tpr)
        aucs.append(roc_auc)
        ax.plot(fpr, tpr, color=color, lw=1.5, label=f"C{cls} (AUC={roc_auc:.3f})")

    macro_auc = np.mean(aucs)
    ax.plot([0, 1], [0, 1], "k--", lw=1)
    ax.set_xlim(0, 1); ax.set_ylim(0, 1.02)
    ax.set_xlabel("False Positive Rate"); ax.set_ylabel("True Positive Rate")
    ax.set_title(f"ROC Curve (macro AUC={macro_auc:.3f}) — {tag}", fontsize=13, fontweight="bold")
    ax.legend(loc="lower right", fontsize=9)
    plt.tight_layout()
    _save(f"roc_auc_{tag}.png", fig)
    print(f"[{tag}] Macro AUC: {macro_auc:.4f}")
    return macro_auc


def plot_pr_curve(model, X_te, y_te, tag="model"):
    """One-vs-Rest Precision-Recall curve，顯示 macro AP。"""
    classes = sorted(y_te.unique())
    y_bin = label_binarize(y_te, classes=classes)
    probs = model.predict_proba(X_te)

    colors = plt.cm.tab10(np.linspace(0, 1, len(classes)))
    fig, ax = plt.subplots(figsize=(7, 6))

    aps = []
    for i, (cls, color) in enumerate(zip(classes, colors)):
        prec, rec, _ = precision_recall_curve(y_bin[:, i], probs[:, i])
        ap = average_precision_score(y_bin[:, i], probs[:, i])
        aps.append(ap)
        ax.plot(rec, prec, color=color, lw=1.5, label=f"C{cls} (AP={ap:.3f})")

    macro_ap = np.mean(aps)
    ax.set_xlim(0, 1); ax.set_ylim(0, 1.02)
    ax.set_xlabel("Recall"); ax.set_ylabel("Precision")
    ax.set_title(f"Precision-Recall Curve (macro AP={macro_ap:.3f}) — {tag}", fontsize=13, fontweight="bold")
    ax.legend(loc="lower left", fontsize=9)
    plt.tight_layout()
    _save(f"pr_curve_{tag}.png", fig)
    print(f"[{tag}] Macro AP: {macro_ap:.4f}")
    return macro_ap


def plot_feature_correlation(X_tr, sel_cols, top_n=25):
    """前 N 個選取特徵的相關矩陣。"""
    cols = sel_cols[:top_n]
    corr = X_tr[cols].corr()
    fig, ax = plt.subplots(figsize=(12, 10))
    mask = np.triu(np.ones_like(corr, dtype=bool))
    sns.heatmap(corr, mask=mask, cmap="coolwarm", center=0,
                annot=False, ax=ax, linewidths=0.3)
    ax.set_title(f"Feature Correlation Matrix (Top {top_n})", fontsize=12, fontweight="bold")
    plt.tight_layout()
    _save("feature_correlation.png", fig)


def plot_cv_scores(model, X_tr, y_tr, tag="model"):
    """5-fold CV 每折 F1-macro。"""
    cv = StratifiedKFold(5, shuffle=True, random_state=42)
    scores = cross_val_score(model, X_tr, y_tr, cv=cv, scoring="f1_macro", n_jobs=-1)
    fig, ax = plt.subplots(figsize=(6, 4))
    ax.bar(range(1, 6), scores, color="steelblue", alpha=0.8)
    ax.axhline(scores.mean(), linestyle="--", color="coral", label=f"Mean={scores.mean():.4f}")
    ax.set_title(f"5-Fold CV F1-macro — {tag}", fontsize=12, fontweight="bold")
    ax.set_xlabel("Fold")
    ax.set_ylabel("F1-macro")
    ax.legend()
    plt.tight_layout()
    _save(f"cv_scores_{tag}.png", fig)
    print(f"[{tag}] CV F1-macro: {scores.mean():.4f} ± {scores.std():.4f}")
    return scores.mean()


def print_report(model, X_te, y_te, tag="model"):
    y_pred = model.predict(X_te)
    acc = accuracy_score(y_te, y_pred)
    f1  = f1_score(y_te, y_pred, average="macro")
    print(f"\n{'='*50}  [{tag}]")
    print(f"  Test Accuracy : {acc:.4f}")
    print(f"  Test F1-macro : {f1:.4f}")
    print(f"{'='*50}")
    print(classification_report(y_te, y_pred, digits=4))
    return acc, f1


def plot_model_comparison(results: dict):
    """所有模型的 Accuracy / F1 / AUC / AP 並排比較圖。"""
    names   = list(results.keys())
    metrics = ["acc", "f1", "auc", "ap", "cv"]
    labels  = ["Test Acc", "Test F1", "Macro AUC", "Macro AP", "CV F1"]
    colors  = ["steelblue", "coral", "mediumpurple", "mediumseagreen", "goldenrod"]

    x = np.arange(len(names))
    w = 0.15
    fig, ax = plt.subplots(figsize=(max(8, len(names) * 2.5), 5))
    for i, (met, lab, col) in enumerate(zip(metrics, labels, colors)):
        vals = [results[n][met] for n in names]
        offset = (i - 2) * w
        bars = ax.bar(x + offset, vals, w, label=lab, color=col, alpha=0.85)
        for xi, v in zip(x + offset, vals):
            ax.text(xi, v + 0.01, f"{v:.3f}", ha="center", fontsize=8)

    ax.set_xticks(x)
    ax.set_xticklabels(names, fontsize=12)
    ax.set_ylim(0, 1.1)
    ax.set_ylabel("Score")
    ax.set_title("Model Comparison", fontsize=14, fontweight="bold")
    ax.legend(loc="lower right", fontsize=9)
    plt.tight_layout()
    _save("model_comparison.png", fig)


def plot_roc_combined(models: dict, X_te, y_te):
    """所有模型的 macro ROC 畫在同一張。"""
    classes = sorted(y_te.unique())
    y_bin = label_binarize(y_te, classes=classes)
    colors = ["steelblue", "coral", "mediumseagreen", "mediumpurple"]

    fig, ax = plt.subplots(figsize=(7, 6))
    for (name, model), color in zip(models.items(), colors):
        probs = model.predict_proba(X_te)
        fprs, tprs = [], []
        for i in range(len(classes)):
            fpr, tpr, _ = roc_curve(y_bin[:, i], probs[:, i])
            fprs.append(fpr); tprs.append(tpr)
        mean_fpr = np.linspace(0, 1, 200)
        mean_tpr = np.mean([np.interp(mean_fpr, f, t) for f, t in zip(fprs, tprs)], axis=0)
        macro_auc = np.mean([auc(f, t) for f, t in zip(fprs, tprs)])
        ax.plot(mean_fpr, mean_tpr, color=color, lw=2, label=f"{name} (AUC={macro_auc:.3f})")

    ax.plot([0, 1], [0, 1], "k--", lw=1)
    ax.set_xlim(0, 1); ax.set_ylim(0, 1.02)
    ax.set_xlabel("False Positive Rate"); ax.set_ylabel("True Positive Rate")
    ax.set_title("ROC Curve — All Models (macro avg)", fontsize=13, fontweight="bold")
    ax.legend(fontsize=10)
    plt.tight_layout()
    _save("roc_combined.png", fig)


def plot_pr_combined(models: dict, X_te, y_te):
    """所有模型的 macro PR 畫在同一張。"""
    classes = sorted(y_te.unique())
    y_bin = label_binarize(y_te, classes=classes)
    colors = ["steelblue", "coral", "mediumseagreen", "mediumpurple"]

    fig, ax = plt.subplots(figsize=(7, 6))
    for (name, model), color in zip(models.items(), colors):
        probs = model.predict_proba(X_te)
        macro_ap = average_precision_score(y_bin, probs, average="macro")
        all_prec, all_rec = [], []
        for i in range(len(classes)):
            prec, rec, _ = precision_recall_curve(y_bin[:, i], probs[:, i])
            all_prec.append(prec); all_rec.append(rec)
        mean_rec = np.linspace(0, 1, 200)
        mean_prec = np.mean([np.interp(mean_rec, r[::-1], p[::-1]) for r, p in zip(all_rec, all_prec)], axis=0)
        ax.plot(mean_rec, mean_prec, color=color, lw=2, label=f"{name} (AP={macro_ap:.3f})")

    ax.set_xlim(0, 1); ax.set_ylim(0, 1.02)
    ax.set_xlabel("Recall"); ax.set_ylabel("Precision")
    ax.set_title("Precision-Recall Curve — All Models (macro avg)", fontsize=13, fontweight="bold")
    ax.legend(fontsize=10)
    plt.tight_layout()
    _save("pr_combined.png", fig)


def plot_confusion_matrix_combined(models: dict, X_te, y_te):
    """所有模型的 confusion matrix 並排。"""
    classes = sorted(y_te.unique())
    n = len(models)
    fig, axes = plt.subplots(1, n, figsize=(5 * n, 4))
    if n == 1:
        axes = [axes]
    for ax, (name, model) in zip(axes, models.items()):
        cm = confusion_matrix(y_te, model.predict(X_te))
        disp = ConfusionMatrixDisplay(cm, display_labels=[f"C{c}" for c in classes])
        disp.plot(ax=ax, colorbar=False, cmap="Blues")
        ax.set_title(name, fontsize=12, fontweight="bold")
    fig.suptitle("Confusion Matrix — All Models", fontsize=13, fontweight="bold")
    plt.tight_layout()
    _save("confusion_matrix_combined.png", fig)


def plot_feature_importance_combined(models: dict, sel_cols, top_n=20):
    """所有模型的 Top-N 特徵重要性並排。"""
    n = len(models)
    fig, axes = plt.subplots(1, n, figsize=(7 * n, max(5, top_n * 0.35)))
    if n == 1:
        axes = [axes]
    colors = ["steelblue", "coral", "mediumseagreen", "mediumpurple"]
    for ax, (name, model), color in zip(axes, models.items(), colors):
        imp = pd.Series(model.feature_importances_, index=sel_cols).sort_values(ascending=True).tail(top_n)
        imp.plot(kind="barh", ax=ax, color=color, alpha=0.85)
        ax.set_title(name, fontsize=12, fontweight="bold")
        ax.set_xlabel("Importance")
    fig.suptitle(f"Feature Importance — All Models (Top {top_n})", fontsize=13, fontweight="bold")
    plt.tight_layout()
    _save("feature_importance_combined.png", fig)


def _save(filename: str, fig):
    path = f"{SAVE_DIR}/{filename}"
    fig.savefig(path, dpi=150, bbox_inches="tight")
    print(f"  Saved: {path}")
    plt.close(fig)


# ══════════════════════════════════════════════════════════════════
# Main
# ══════════════════════════════════════════════════════════════════

def save_model_params(name, study, metrics):
    record = {
        "model": name,
        "best_params": study.best_params,
        "best_cv_f1": study.best_value,
        "test_accuracy": metrics.get("acc"),
        "test_f1_macro": metrics.get("f1"),
        "macro_auc":     metrics.get("auc"),
        "macro_ap":      metrics.get("ap"),
        "cv_f1_macro":   metrics.get("cv"),
    }
    path = f"{SAVE_DIR}/params_{name}.json"
    with open(path, "w", encoding="utf-8") as f:
        json.dump(record, f, indent=2, ensure_ascii=False)
    print(f"  Saved params: {path}")


def run_one_model(name, train_fn, X_tr, X_te, y_tr, y_te, sel_cols, n_trials):
    print(f"\n{'='*60}")
    print(f"  Training: {name}")
    print(f"{'='*60}")
    model, study = train_fn(X_tr, y_tr, n_trials=n_trials)

    plot_optuna_history(study, tag=name)
    acc, f1      = print_report(model, X_te, y_te, tag=name)
    cv           = plot_cv_scores(model, X_tr, y_tr, tag=name)
    roc_auc      = plot_roc_auc(model, X_te, y_te, tag=name)
    ap           = plot_pr_curve(model, X_te, y_te, tag=name)

    plot_confusion_matrix(model, X_te, y_te, tag=name)
    plot_feature_importance(model, sel_cols, tag=name)
    plot_learning_curve(model, X_tr, y_tr, tag=name)
    plot_per_class_f1(model, X_te, y_te, tag=name)
    plot_prediction_confidence(model, X_te, y_te, tag=name)
    metrics = {"acc": acc, "f1": f1, "auc": roc_auc, "ap": ap, "cv": cv}
    save_model_params(name, study, metrics)
    return model, metrics


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--labels-csv", default="outputs/labels_avicpso.csv")
    parser.add_argument("--n-trials", type=int, default=50, help="Optuna trials（XGB/LGBM）")
    parser.add_argument("--n-trials-ada", type=int, default=30, help="Optuna trials（AdaBoost）")
    args = parser.parse_args()

    print(f"\n{'='*60}")
    print(f"  Cluster Analysis: XGB + LGBM + AdaBoost")
    print(f"  Labels : {args.labels_csv}")
    print(f"  Trials : XGB/LGBM={args.n_trials}  AdaBoost={args.n_trials_ada}")
    print(f"{'='*60}\n")

    df_raw, df_feat, y = load_data(args.labels_csv)
    X_tr, X_te, y_tr, y_te, sel_cols = build_features(df_raw, df_feat, y, {})

    plot_class_distribution(y_tr, y_te)
    plot_feature_correlation(X_tr, sel_cols)

    MODEL_CONFIGS = [
        ("XGBoost",  train_xgb,      args.n_trials),
        ("LightGBM", train_lgbm,     args.n_trials),
        ("AdaBoost", train_adaboost, args.n_trials_ada),
    ]

    all_results = {}
    all_models  = {}
    for name, train_fn, n_trials in MODEL_CONFIGS:
        model, metrics = run_one_model(name, train_fn, X_tr, X_te, y_tr, y_te, sel_cols, n_trials)
        all_results[name] = metrics
        all_models[name]  = model

    print(f"\n{'='*60}")
    print("  Final Comparison")
    print(f"{'='*60}")
    for name, m in all_results.items():
        print(f"  {name:<12} Acc={m['acc']:.4f}  F1={m['f1']:.4f}  AUC={m['auc']:.4f}  AP={m['ap']:.4f}  CV={m['cv']:.4f}")

    # ── 個別比較圖
    plot_model_comparison(all_results)

    # ── Combined 跨模型比較圖
    plot_roc_combined(all_models, X_te, y_te)
    plot_pr_combined(all_models, X_te, y_te)
    plot_confusion_matrix_combined(all_models, X_te, y_te)
    plot_feature_importance_combined(all_models, sel_cols)

    print(f"\n全部圖表已存至 {SAVE_DIR}/")


if __name__ == "__main__":
    main()
