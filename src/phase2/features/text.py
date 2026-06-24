"""
文字型特徵：short_description, about_the_game, detailed_description。
只用上線前開發商撰寫的欄位，排除 reviews / notes（上線後資訊）。
"""
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer

TEXT_COLS = ["short_description", "about_the_game", "detailed_description"]


class TextEncoder:
    def __init__(self, max_features: int = 500):
        self.max_features = max_features
        self.vec_ = None

    def fit(self, df: pd.DataFrame) -> "TextEncoder":
        corpus = self._corpus(df)
        self.vec_ = TfidfVectorizer(
            max_features=self.max_features,
            sublinear_tf=True,
            strip_accents="unicode",
        )
        self.vec_.fit(corpus)
        return self

    def transform(self, df: pd.DataFrame) -> pd.DataFrame:
        corpus = self._corpus(df)
        mat = self.vec_.transform(corpus)
        cols = [f"tfidf__{t}" for t in self.vec_.get_feature_names_out()]
        return pd.DataFrame(mat.toarray(), columns=cols, index=df.index)

    def fit_transform(self, df: pd.DataFrame) -> pd.DataFrame:
        return self.fit(df).transform(df)

    def _corpus(self, df: pd.DataFrame) -> pd.Series:
        return df[TEXT_COLS].fillna("").apply(lambda row: " ".join(row), axis=1)


def build(df: pd.DataFrame, method: str = "tfidf", max_features: int = 500) -> pd.DataFrame:
    """只在 train 上用，測試集請透過 TextEncoder.fit/transform 分開處理。"""
    if method == "tfidf":
        return TextEncoder(max_features).fit_transform(df)
    raise ValueError(f"Unknown text method: {method}")
