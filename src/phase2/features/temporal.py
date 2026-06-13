"""時間型特徵：從 release_date 衍生 year, month, days_since_release。"""
import pandas as pd

REFERENCE_DATE = pd.Timestamp("2025-03-01")


def build(df: pd.DataFrame) -> pd.DataFrame:
    dates = pd.to_datetime(df["release_date"], errors="coerce")
    return pd.DataFrame({
        "release_year": dates.dt.year.fillna(0).astype(int),
        "release_month": dates.dt.month.fillna(0).astype(int),
        "days_since_release": (REFERENCE_DATE - dates).dt.days.clip(lower=0).fillna(0).astype(int),
    }, index=df.index)
