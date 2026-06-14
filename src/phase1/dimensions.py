"""
將原始欄位轉換為單一維度數值（0~1 正規化）。
每個維度對應一個欄位；偏態欄位（log_scale: true）先做 log1p 再正規化。
可在 config 中啟用 iqr_clip 以降低長尾極端值對 MinMaxScaler 的影響。
"""
import numpy as np
import pandas as pd
from sklearn.preprocessing import MinMaxScaler

LOG_SCALE_COLS = {"average_playtime_forever", "average_playtime_2weeks",
                  "median_playtime_forever", "median_playtime_2weeks",
                  "peak_ccu", "estimated_owners_mid", "recommendations"}


def build_dimensions(df: pd.DataFrame, cfg: dict) -> pd.DataFrame:
    result = pd.DataFrame({"appid": df["appid"]})
    for dim_name, dim_cfg in cfg["dimensions"].items():
        col = dim_cfg["features"][0]
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

        result[dim_name] = series.values

    dim_cols = list(cfg["dimensions"].keys())
    result[dim_cols] = MinMaxScaler().fit_transform(result[dim_cols])
    return result


def _iqr_clip(
    series: pd.Series,
    factor: float = 1.5,
    positive_only: bool = True,
) -> pd.Series:
    ref = series[series > 0] if positive_only else series
    if ref.empty:
        return series
    q1 = ref.quantile(0.25)
    q3 = ref.quantile(0.75)
    iqr = q3 - q1
    if not np.isfinite(iqr) or iqr <= 0:
        return series
    return series.clip(upper=q3 + factor * iqr)
