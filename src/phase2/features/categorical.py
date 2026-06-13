"""
類別型特徵：genres, categories, tags, developers, publishers,
supported_languages, full_audio_languages。

採用 Target Encoding（one-vs-rest）：
  對每個 list 型欄位中的各類別值，計算其對應各群的條件機率
  enc[v, k] = P(y=k | category=v)
  每筆樣本取其所有 category 值的平均，產生 K 個數值特徵。

fit() 在訓練集上學習編碼表，transform() 用於測試集（防 leakage）。
"""
from __future__ import annotations
import numpy as np
import pandas as pd
from collections import defaultdict
from src.data import parse_list_col, parse_dict_col

LIST_COLS = ["genres", "categories", "developers", "publishers",
             "supported_languages", "full_audio_languages"]
DICT_COLS = ["tags"]


class TargetEncoder:
    """
    對 list 型類別欄位做 one-vs-rest target encoding。
    未見過的類別值以各 class 的全域先驗機率填補（smoothing）。
    """

    def __init__(self, cols: list[str], smoothing: float = 1.0):
        self.cols = cols
        self.smoothing = smoothing
        self.encodings_: dict[str, dict[str, np.ndarray]] = {}
        self.prior_: np.ndarray | None = None
        self.classes_: np.ndarray | None = None

    def fit(self, df: pd.DataFrame, y: pd.Series) -> "TargetEncoder":
        classes = np.sort(y.unique())
        self.classes_ = classes
        K = len(classes)
        n = len(y)

        # 全域先驗（各 class 佔比）
        self.prior_ = np.array([(y == k).sum() / n for k in classes])

        for col in self.cols:
            parsed = _parse_col(df, col)
            enc: dict[str, np.ndarray] = {}

            # 統計每個 value 出現時各 class 的次數
            count_k: dict[str, np.ndarray] = defaultdict(lambda: np.zeros(K))
            count_total: dict[str, int] = defaultdict(int)

            for i, vals in enumerate(parsed):
                yi = y.iloc[i]
                k_idx = np.searchsorted(classes, yi)
                for v in vals:
                    count_k[v][k_idx] += 1
                    count_total[v] += 1

            # smoothing：與先驗加權平均
            for v in count_k:
                n_v = count_total[v]
                raw = count_k[v] / n_v
                w = n_v / (n_v + self.smoothing)
                enc[v] = w * raw + (1 - w) * self.prior_
            self.encodings_[col] = enc
        return self

    def transform(self, df: pd.DataFrame) -> pd.DataFrame:
        frames = []
        for col in self.cols:
            parsed = _parse_col(df, col)
            enc = self.encodings_.get(col, {})
            K = len(self.classes_)

            rows = []
            for vals in parsed:
                if not vals:
                    rows.append(self.prior_)
                    continue
                vecs = [enc.get(v, self.prior_) for v in vals]
                rows.append(np.mean(vecs, axis=0))

            col_names = [f"{col}__te_cls{k}" for k in self.classes_]
            frames.append(pd.DataFrame(rows, columns=col_names, index=df.index))

        return pd.concat(frames, axis=1)

    def fit_transform(self, df: pd.DataFrame, y: pd.Series) -> pd.DataFrame:
        return self.fit(df, y).transform(df)


def build(df: pd.DataFrame, y: pd.Series | None = None,
          mode: str = "target", smoothing: float = 1.0, top_k: int = 20) -> pd.DataFrame:
    """
    mode="target" : Target Encoding（需傳入 y，訓練集用）
    mode="transform": 僅 transform（測試集用，需先 fit）
    """
    all_cols = LIST_COLS + DICT_COLS
    if mode == "target" and y is not None:
        enc = TargetEncoder(all_cols, smoothing=smoothing)
        return enc.fit_transform(df, y)
    # fallback: 若無 y 則回傳零矩陣（僅供 pipeline 介面相容）
    K = 5
    cols = [f"{col}__te_cls{k}" for col in all_cols for k in range(K)]
    return pd.DataFrame(0.0, index=df.index, columns=cols)


def _parse_col(df: pd.DataFrame, col: str) -> pd.Series:
    if col in DICT_COLS:
        parsed = parse_dict_col(df[col])
        return parsed.apply(
            lambda d: list(d.keys()) if isinstance(d, dict)
            else (d if isinstance(d, list) else [])
        )
    return parse_list_col(df[col])
