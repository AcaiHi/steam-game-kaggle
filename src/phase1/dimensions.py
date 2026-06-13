"""
將原始欄位轉換為單一維度數值（0~1 正規化）。
偏態欄位（log_scale: true）先做 log1p 再正規化。
可在 config 中啟用 iqr_clip，以降低長尾極端值對 MinMaxScaler 的影響。
"""
import numpy as np
import pandas as pd
from sklearn.preprocessing import MinMaxScaler

# 已知右偏、需要 log 轉換的欄位
LOG_SCALE_COLS = {"average_playtime_forever", "average_playtime_2weeks",
                  "median_playtime_forever", "median_playtime_2weeks",
                  "peak_ccu", "estimated_owners_mid", "recommendations"}


def build_dimensions(df: pd.DataFrame, cfg: dict) -> pd.DataFrame:
    """
    回傳 DataFrame，包含 appid + 各維度欄（0~1）。
    config 中可對每個維度指定 log_scale: true 強制 log 轉換；
    預設對 LOG_SCALE_COLS 中的欄位自動套用。
    """
    result = pd.DataFrame({"appid": df["appid"]})
    for dim_name, dim_cfg in cfg["dimensions"].items():
        frames = []
        for col in dim_cfg["features"]:
            series = df[col].fillna(0).clip(lower=0)
            use_log = dim_cfg.get("log_scale", col in LOG_SCALE_COLS)
            if use_log:
                series = np.log1p(series)

            if dim_cfg.get("iqr_clip", False):
                series = _iqr_clip(
                    series,
                    factor=dim_cfg.get("iqr_factor", 1.5),
                    positive_only=dim_cfg.get("iqr_positive_only", True),
                )

            series = _apply_transform(series, dim_cfg.get("transform", "none"))
            frames.append(series.rename(col))

        feature_df = pd.concat(frames, axis=1)
        if len(frames) > 1:
            feature_df[feature_df.columns] = MinMaxScaler().fit_transform(feature_df)
        result[dim_name] = feature_df.mean(axis=1)

    dim_cols = list(cfg["dimensions"].keys())
    result[dim_cols] = MinMaxScaler().fit_transform(result[dim_cols])
    return result


def _iqr_clip(
    series: pd.Series,
    factor: float = 1.5,
    positive_only: bool = True,
) -> pd.Series:
    """Use IQR upper clipping to reduce right-tail dominance without dropping rows."""
    ref = series[series > 0] if positive_only else series
    if ref.empty:
        return series

    q1 = ref.quantile(0.25)
    q3 = ref.quantile(0.75)
    iqr = q3 - q1
    if not np.isfinite(iqr) or iqr <= 0:
        return series

    upper = q3 + factor * iqr
    return series.clip(upper=upper)


def _apply_transform(series: pd.Series, transform: str) -> pd.Series:
    if transform in (None, "none"):
        return series
    if transform == "square":
        return series ** 2
    if transform == "exp":
        return np.exp(series)
    if transform == "exp_square":
        return np.exp(series) ** 2
    raise ValueError(f"Unknown dimension transform: {transform}")
