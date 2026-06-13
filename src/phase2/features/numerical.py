"""數值型特徵：price, discount, dlc_count, required_age, achievements。"""
import pandas as pd
from sklearn.preprocessing import StandardScaler

COLS = ["price", "discount", "dlc_count", "required_age", "achievements"]


def build(df: pd.DataFrame) -> pd.DataFrame:
    sub = df[COLS].fillna(0).copy()
    scaled = StandardScaler().fit_transform(sub)
    return pd.DataFrame(scaled, columns=COLS, index=df.index)
