import pandas as pd
import ast


DATA_PATH = "datasets/artermiloff/games_march2025_cleaned.csv"

# Phase 1 分群維度欄位，Phase 2 禁止使用
LEAKAGE_COLS = [
    "positive", "negative", "pct_pos_total", "pct_pos_recent",
    "num_reviews_total", "num_reviews_recent", "metacritic_score",
    "user_score", "score_rank", "recommendations",
    "average_playtime_forever", "average_playtime_2weeks",
    "median_playtime_forever", "median_playtime_2weeks",
    "peak_ccu", "estimated_owners",
]


def load_raw(path: str = DATA_PATH) -> pd.DataFrame:
    df = pd.read_csv(path)
    df["estimated_owners_mid"] = df["estimated_owners"].apply(_parse_owners_mid)
    return df


def load_phase1_features(df: pd.DataFrame) -> pd.DataFrame:
    cols = ["appid", "pct_pos_total", "average_playtime_forever", "peak_ccu"]
    return df[cols].copy()


def load_phase2_features(df: pd.DataFrame) -> pd.DataFrame:
    drop = LEAKAGE_COLS + ["estimated_owners_mid"]
    return df.drop(columns=[c for c in drop if c in df.columns]).copy()


def parse_list_col(series: pd.Series) -> pd.Series:
    return series.apply(lambda x: ast.literal_eval(x) if isinstance(x, str) else [])


def parse_dict_col(series: pd.Series) -> pd.Series:
    return series.apply(lambda x: ast.literal_eval(x) if isinstance(x, str) else {})


def _parse_owners_mid(val: str) -> float:
    try:
        lo, hi = val.split(" - ")
        return (int(lo.replace(",", "")) + int(hi.replace(",", ""))) / 2
    except Exception:
        return float("nan")
