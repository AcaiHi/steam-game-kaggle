"""
文字型特徵：short_description, about_the_game, detailed_description, reviews, notes。
支援 tfidf 和 embedding 兩種模式。
"""
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer

TEXT_COLS = ["short_description", "about_the_game", "detailed_description", "reviews", "notes"]


def build(df: pd.DataFrame, method: str = "tfidf", max_features: int = 500) -> pd.DataFrame:
    corpus = df[TEXT_COLS].fillna("").apply(lambda row: " ".join(row), axis=1)
    if method == "tfidf":
        return _tfidf(corpus, max_features)
    if method == "embedding":
        return _embedding(corpus)
    raise ValueError(f"Unknown text method: {method}")


def _tfidf(corpus: pd.Series, max_features: int) -> pd.DataFrame:
    vec = TfidfVectorizer(max_features=max_features, sublinear_tf=True, strip_accents="unicode")
    mat = vec.fit_transform(corpus)
    cols = [f"tfidf__{t}" for t in vec.get_feature_names_out()]
    return pd.DataFrame(mat.toarray(), columns=cols, index=corpus.index)


def _embedding(corpus: pd.Series) -> pd.DataFrame:
    # placeholder：實作時替換為 sentence-transformers 或其他 embedding 模型
    raise NotImplementedError("Embedding method not yet implemented")
