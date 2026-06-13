"""
Phase 2 執行入口
用法：python run_phase2.py --config configs/phase2.yaml --labels outputs/phase1_labels.csv
"""
import argparse
import yaml
import numpy as np
import pandas as pd
import mlflow
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder

from src.data import load_raw, load_phase2_features
from src.phase2.pipeline import (
    build_base_features,
    fit_transform_categorical,
    fit_transform_selection,
    fit_transform_extraction,
)
from src.phase2.train import run as train


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--config", default="configs/phase2.yaml")
    parser.add_argument("--labels", default="outputs/phase1_labels.csv")
    args = parser.parse_args()

    with open(args.config, encoding="utf-8") as f:
        cfg = yaml.safe_load(f)

    mlflow.set_tracking_uri(cfg["mlflow"]["tracking_uri"])

    # ── 載入資料 ──────────────────────────────────────────────────────────────
    df = load_raw()
    labels_df = pd.read_csv(args.labels)[["appid", "cluster_id"]]
    df = df.merge(labels_df, on="appid").reset_index(drop=True)

    feat_df = load_phase2_features(df)
    y = df["cluster_id"]

    print(f"Class distribution:\n{y.value_counts().sort_index().to_string()}\n")

    # ── 移除樣本不足的類別 ────────────────────────────────────────────────────
    min_samples = cfg.get("min_class_samples", 5)
    counts = y.value_counts()
    valid_classes = counts[counts >= min_samples].index
    mask = y.isin(valid_classes)
    feat_df = feat_df[mask].reset_index(drop=True)
    y = y[mask].reset_index(drop=True)
    dropped = sorted(set(counts.index) - set(valid_classes))
    if dropped:
        print(f"[INFO] Dropped classes with < {min_samples} samples: {dropped}")

    # LabelEncoder → 連續整數
    le = LabelEncoder()
    y_enc = pd.Series(le.fit_transform(y), name="cluster_id")
    print(f"[INFO] Label mapping: {dict(zip(le.classes_, le.transform(le.classes_)))}\n")

    # ── Step 1: y 無關特徵（可在 split 前建立）───────────────────────────────
    print("Building base features (numerical / temporal / platform) ...")
    X_base = build_base_features(feat_df, cfg)

    # ── Step 2: Train / Test Split ────────────────────────────────────────────
    idx = np.arange(len(feat_df))
    idx_train, idx_test, y_train, y_test = train_test_split(
        idx, y_enc, test_size=0.2, random_state=42, stratify=y_enc
    )

    X_base_train = X_base.iloc[idx_train].reset_index(drop=True)
    X_base_test  = X_base.iloc[idx_test].reset_index(drop=True)
    df_train = feat_df.iloc[idx_train].reset_index(drop=True)
    df_test  = feat_df.iloc[idx_test].reset_index(drop=True)
    y_train  = y_train.reset_index(drop=True)
    y_test   = y_test.reset_index(drop=True)

    # ── Step 3: Target Encoding（僅在 train 上 fit）──────────────────────────
    print(f"Fitting target encoder on train ({len(df_train)} samples) ...")
    cat_train, cat_test = fit_transform_categorical(df_train, df_test, y_train, cfg)

    X_train = pd.concat([X_base_train, cat_train], axis=1)
    X_test  = pd.concat([X_base_test,  cat_test],  axis=1)

    # ── Step 4: Feature Selection（僅在 train 上 fit）────────────────────────
    sel_method = cfg.get("selection", {}).get("method", "none")
    print(f"Fitting feature selection on train (method={sel_method}) ...")
    X_train, X_test = fit_transform_selection(X_train, X_test, y_train, cfg)

    # ── Step 5: Feature Extraction ────────────────────────────────────────────
    ext_method = cfg.get("extraction", {}).get("method", "none")
    if ext_method != "none":
        print(f"Fitting extraction on train (method={ext_method}) ...")
    X_train, X_test = fit_transform_extraction(X_train, X_test, cfg)

    print(f"\nFeature matrix — train: {X_train.shape}  test: {X_test.shape}")
    print(f"Classes: {sorted(y_train.unique())}\n")

    # ── Step 6: 訓練與評估 ────────────────────────────────────────────────────
    train(X_train, X_test, y_train, y_test, cfg)


if __name__ == "__main__":
    main()
