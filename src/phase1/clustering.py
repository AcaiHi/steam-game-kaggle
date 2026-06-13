"""
依最佳門檻值或直接的 color id 對每款遊戲貼上標籤。
"""
import numpy as np
import pandas as pd
from .objective import assign_labels


def make_labels(thresholds: list[float], dims: pd.DataFrame) -> pd.DataFrame:
    numeric = assign_labels(thresholds, dims)
    n_dims = len([c for c in dims.columns if c != "appid"])
    labels = pd.Series(numeric, index=dims.index).map(
        lambda value: _label_from_id(int(value), n_dims)
    )
    return pd.DataFrame({"appid": dims["appid"], "cluster": labels, "cluster_id": numeric})


def make_labels_from_ids(labels: np.ndarray, dims: pd.DataFrame) -> pd.DataFrame:
    n_dims = len([c for c in dims.columns if c != "appid"])
    series = pd.Series(labels, index=dims.index)
    cluster = series.map(lambda value: _label_from_id(int(value), n_dims))
    return pd.DataFrame({"appid": dims["appid"], "cluster": cluster, "cluster_id": series.astype(int)})


def _label_from_id(value: int, n_dims: int) -> str:
    bits = format(value, f"0{n_dims}b")
    return "".join("H" if bit == "1" else "L" for bit in bits)
