"""
將原始欄位轉換為單一維度數值（0~1 正規化）。
偏態欄位（log_scale: true）先做 log1p 再正規化。
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
        col = dim_cfg["features"][0]        # 單欄位
        series = df[col].fillna(0).clip(lower=0)
        use_log = dim_cfg.get("log_scale", col in LOG_SCALE_COLS)
        if use_log:
            series = np.log1p(series)
        result[dim_name] = series

    dim_cols = list(cfg["dimensions"].keys())
    result[dim_cols] = MinMaxScaler().fit_transform(result[dim_cols])
    return result
