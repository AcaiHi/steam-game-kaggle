"""
Feature pipeline — 無洩漏版本。

正確流程：
  1. build_base_features()   : 只建立與 y 無關的特徵（數值/時間/平台）
  2. train/test split（在 run_phase2.py 做）
  3. fit_transform_categorical(): target encoder 只在 train 上 fit
  4. fit_transform_selection()  : selector 只在 train 上 fit
  5. apply_extraction()         : PCA 等（可在 train fit）

Feature Selection 策略（cfg.selection.method）：
  variance    : VarianceThreshold
  mutual_info : SelectKBest + mutual_info_classif
  rfe         : Recursive Feature Elimination
  none        : 不做

Feature Extraction 策略（cfg.extraction.method）：
  pca  : PCA
  none : 不做
"""
from __future__ import annotations
import numpy as np
import pandas as pd
from sklearn.decomposition import PCA
from sklearn.feature_selection import (
    SelectKBest, mutual_info_classif, VarianceThreshold, RFE
)
from sklearn.ensemble import RandomForestClassifier

from .features import numerical, temporal, text
from .features.categorical import TargetEncoder

# LIST + DICT cols that need target encoding
CATEGORICAL_COLS = [
    "genres", "categories", "developers", "publishers",
    "supported_languages", "full_audio_languages", "tags",
]


# ── Step 1: y 無關特徵 ────────────────────────────────────────────────────────

def build_base_features(df: pd.DataFrame, cfg: dict) -> pd.DataFrame:
    """數值、時間、平台特徵，不依賴 y，可在 split 前建立。"""
    frames = []
    feat_cfg = cfg["features"]

    if feat_cfg.get("numerical"):
        frames.append(numerical.build(df))
    if feat_cfg.get("temporal"):
        frames.append(temporal.build(df))
    if feat_cfg.get("text"):
        frames.append(text.build(df, **cfg.get("text", {})))

    # 平台布林欄位
    for col in ["windows", "mac", "linux"]:
        if col in df.columns:
            frames.append(df[[col]].astype(int).reset_index(drop=True))

    if not frames:
        return pd.DataFrame(index=df.index)
    return pd.concat(frames, axis=1).reset_index(drop=True)


# ── Step 3: Target Encoding（只在 train 上 fit）────────────────────────────────

def fit_transform_categorical(
    df_train: pd.DataFrame,
    df_test: pd.DataFrame,
    y_train: pd.Series,
    cfg: dict,
) -> tuple[pd.DataFrame, pd.DataFrame]:
    """
    回傳 (cat_train, cat_test)。
    encoder 在 df_train + y_train 上 fit，df_test 只做 transform。
    """
    if not cfg["features"].get("categorical", True):
        empty = pd.DataFrame(index=df_train.index)
        return empty, pd.DataFrame(index=df_test.index)

    cat_cfg = cfg.get("categorical", {})
    enc = TargetEncoder(CATEGORICAL_COLS, smoothing=cat_cfg.get("smoothing", 1.0))
    cat_train = enc.fit(df_train, y_train).transform(df_train)
    cat_test  = enc.transform(df_test)
    cat_test.index = df_test.index
    return cat_train.reset_index(drop=True), cat_test.reset_index(drop=True)


# ── Step 4: Feature Selection（只在 train 上 fit）─────────────────────────────

def fit_transform_selection(
    X_train: pd.DataFrame,
    X_test: pd.DataFrame,
    y_train: pd.Series,
    cfg: dict,
) -> tuple[pd.DataFrame, pd.DataFrame]:
    sel_cfg = cfg.get("selection", {})
    method  = sel_cfg.get("method", "none")
    k       = sel_cfg.get("k", 50)

    if method == "variance":
        sel = VarianceThreshold(threshold=sel_cfg.get("threshold", 0.0))
        sel.fit(X_train)
        cols = X_train.columns[sel.get_support()]
        return (pd.DataFrame(sel.transform(X_train), columns=cols),
                pd.DataFrame(sel.transform(X_test),  columns=cols))

    if method == "mutual_info":
        k_eff = min(k, X_train.shape[1])
        sel = SelectKBest(mutual_info_classif, k=k_eff)
        sel.fit(X_train, y_train)
        cols = X_train.columns[sel.get_support()]
        return (pd.DataFrame(sel.transform(X_train), columns=cols),
                pd.DataFrame(sel.transform(X_test),  columns=cols))

    if method == "rfe":
        k_eff = min(k, X_train.shape[1])
        estimator = RandomForestClassifier(
            n_estimators=sel_cfg.get("rfe_estimators", 50),
            random_state=42, n_jobs=-1,
        )
        sel = RFE(estimator, n_features_to_select=k_eff,
                  step=sel_cfg.get("rfe_step", 0.1))
        sel.fit(X_train, y_train)
        cols = X_train.columns[sel.get_support()]
        return (pd.DataFrame(sel.transform(X_train), columns=cols),
                pd.DataFrame(sel.transform(X_test),  columns=cols))

    return X_train.reset_index(drop=True), X_test.reset_index(drop=True)


# ── Step 5: Extraction（在 train 上 fit）──────────────────────────────────────

def fit_transform_extraction(
    X_train: pd.DataFrame,
    X_test: pd.DataFrame,
    cfg: dict,
) -> tuple[pd.DataFrame, pd.DataFrame]:
    ext_cfg = cfg.get("extraction", {})
    method  = ext_cfg.get("method", "none")
    n       = ext_cfg.get("n_components", 50)

    if method == "pca":
        n_eff = min(n, X_train.shape[1], X_train.shape[0])
        pca = PCA(n_components=n_eff, random_state=42)
        pca.fit(X_train)
        cols = [f"pc{i+1}" for i in range(n_eff)]
        return (pd.DataFrame(pca.transform(X_train), columns=cols),
                pd.DataFrame(pca.transform(X_test),  columns=cols))

    return X_train.reset_index(drop=True), X_test.reset_index(drop=True)
