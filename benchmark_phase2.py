"""
Phase 2 分類器 Benchmark
比較 5 個模型 × 3 種特徵選擇方式，指標：Accuracy / F1 Macro / F1 Weighted / AP / ROC-AUC
用法：python benchmark_phase2.py [--labels outputs/phase1_labels.csv]
"""
import argparse
import time
import warnings
import numpy as np
import pandas as pd
import yaml
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder
from sklearn.metrics import (
    accuracy_score, f1_score, average_precision_score, roc_auc_score,
)

warnings.filterwarnings("ignore")

from src.data import load_raw, load_phase2_features
from src.phase2.pipeline import (
    build_base_features,
    fit_transform_text,
    fit_transform_categorical,
    fit_transform_selection,
    fit_transform_extraction,
)
from src.phase2.optimize import _base_params
from src.phase2.train import _make_model


BASE_CFG = {
    "features":   {"numerical": True, "categorical": True, "temporal": True, "text": False},
    "categorical": {"smoothing": 1.0},
    "extraction": {"method": "none"},
    "param_optimize": {"method": "none"},
}

MODELS = ["random_forest", "xgboost", "lightgbm", "catboost", "svm"]

SELECTIONS = {
    "mutual_info": {"method": "mutual_info", "k": 50},
    "rfe":         {"method": "rfe",         "k": 50, "rfe_estimators": 50, "rfe_step": 0.1},
    "none":        {"method": "none"},
}


def evaluate(model, X_test, y_test):
    y_pred = model.predict(X_test)
    acc        = accuracy_score(y_test, y_pred)
    f1_macro   = f1_score(y_test, y_pred, average="macro",    zero_division=0)
    f1_weighted= f1_score(y_test, y_pred, average="weighted", zero_division=0)
    if hasattr(model, "predict_proba"):
        y_prob = model.predict_proba(X_test)
    elif hasattr(model, "decision_function"):
        from scipy.special import softmax
        y_prob = softmax(model.decision_function(X_test), axis=1)
    else:
        y_prob = None
    if y_prob is not None:
        try:
            ap  = average_precision_score(y_test, y_prob, average="macro")
            auc = roc_auc_score(y_test, y_prob, average="macro", multi_class="ovr")
        except Exception:
            ap, auc = float("nan"), float("nan")
    else:
        ap, auc = float("nan"), float("nan")
    return acc, f1_macro, f1_weighted, ap, auc


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--labels", default="outputs/phase1_labels.csv")
    args = parser.parse_args()

    # ── 資料載入 ──────────────────────────────────────────────────────────────
    print("Loading data ...")
    df = load_raw()
    labels_df = pd.read_csv(args.labels)[["appid", "cluster_id"]]
    df = df.merge(labels_df, on="appid").reset_index(drop=True)
    feat_df = load_phase2_features(df)
    y = df["cluster_id"]

    # 移除樣本不足的類別
    counts = y.value_counts()
    valid_classes = counts[counts >= 5].index
    mask = y.isin(valid_classes)
    feat_df = feat_df[mask].reset_index(drop=True)
    y = y[mask].reset_index(drop=True)

    le = LabelEncoder()
    y_enc = pd.Series(le.fit_transform(y), name="cluster_id")

    print(f"Samples: {len(feat_df):,}  Classes: {sorted(y_enc.unique())}  "
          f"(mapping: {dict(zip(le.classes_, le.transform(le.classes_)))})\n")

    # ── Step 1: 基礎特徵 ──────────────────────────────────────────────────────
    cfg = {**BASE_CFG}
    print("Building base features ...")
    X_base = build_base_features(feat_df, cfg)

    # ── Step 2: Split ─────────────────────────────────────────────────────────
    idx = np.arange(len(feat_df))
    idx_tr, idx_te, y_tr, y_te = train_test_split(
        idx, y_enc, test_size=0.2, random_state=42, stratify=y_enc
    )
    X_base_tr = X_base.iloc[idx_tr].reset_index(drop=True)
    X_base_te = X_base.iloc[idx_te].reset_index(drop=True)
    df_tr = feat_df.iloc[idx_tr].reset_index(drop=True)
    df_te = feat_df.iloc[idx_te].reset_index(drop=True)
    y_tr  = y_tr.reset_index(drop=True)
    y_te  = y_te.reset_index(drop=True)

    # ── Step 3: Text + Target Encoding（一次 fit，所有 selection 共用）────────
    print("Fitting text encoder ...")
    txt_tr, txt_te = fit_transform_text(df_tr, df_te, cfg)
    print("Fitting target encoder ...")
    cat_tr, cat_te = fit_transform_categorical(df_tr, df_te, y_tr, cfg)
    X_full_tr = pd.concat([X_base_tr, txt_tr, cat_tr], axis=1)
    X_full_te = pd.concat([X_base_te, txt_te, cat_te], axis=1)
    print(f"Full feature matrix: train={X_full_tr.shape}  test={X_full_te.shape}\n")

    # ── Benchmark loop ────────────────────────────────────────────────────────
    results = []

    for sel_name, sel_cfg in SELECTIONS.items():
        print(f"{'─'*60}")
        print(f"  Feature Selection: {sel_name}")
        cfg_sel = {**cfg, "selection": sel_cfg}
        t0 = time.time()
        X_tr, X_te = fit_transform_selection(X_full_tr, X_full_te, y_tr, cfg_sel)
        sel_time = time.time() - t0
        print(f"  → {X_tr.shape[1]} features  ({sel_time:.1f}s)\n")

        for model_name in MODELS:
            t0 = time.time()
            model = _make_model(model_name)
            model.fit(X_tr, y_tr)
            elapsed = time.time() - t0
            acc, f1m, f1w, ap, auc = evaluate(model, X_te, y_te)
            results.append({
                "selection": sel_name,
                "model":     model_name,
                "acc":       acc,
                "f1_macro":  f1m,
                "f1_weighted": f1w,
                "ap_macro":  ap,
                "roc_auc":   auc,
                "time_s":    elapsed,
            })
            print(f"  [{sel_name:12s}] {model_name:<14}  "
                  f"acc={acc:.4f}  f1m={f1m:.4f}  f1w={f1w:.4f}  "
                  f"ap={ap:.4f}  auc={auc:.4f}  t={elapsed:.1f}s")

    # ── 結果總表 ──────────────────────────────────────────────────────────────
    df_res = pd.DataFrame(results)
    print(f"\n{'='*80}")
    print("  BENCHMARK RESULTS — ranked by F1 Macro")
    print(f"{'='*80}")
    header = f"  {'Selection':<14} {'Model':<16} {'Acc':>7} {'F1-Mac':>8} {'F1-Wt':>8} {'AP':>8} {'AUC':>8}"
    print(header)
    print(f"  {'-'*74}")
    for _, r in df_res.sort_values("f1_macro", ascending=False).iterrows():
        print(f"  {r['selection']:<14} {r['model']:<16} "
              f"{r['acc']:>7.4f} {r['f1_macro']:>8.4f} {r['f1_weighted']:>8.4f} "
              f"{r['ap_macro']:>8.4f} {r['roc_auc']:>8.4f}")

    print(f"\n  Best F1 Macro: {df_res['f1_macro'].max():.4f}  "
          f"({df_res.loc[df_res['f1_macro'].idxmax(), 'model']} / "
          f"{df_res.loc[df_res['f1_macro'].idxmax(), 'selection']})")
    print(f"  Best ROC-AUC:  {df_res['roc_auc'].max():.4f}  "
          f"({df_res.loc[df_res['roc_auc'].idxmax(), 'model']} / "
          f"{df_res.loc[df_res['roc_auc'].idxmax(), 'selection']})")
    print(f"{'='*80}\n")

    # 儲存
    out = "outputs/benchmark_phase2.csv"
    df_res.to_csv(out, index=False)
    print(f"Saved to {out}")


if __name__ == "__main__":
    main()
