import numpy as np
import pandas as pd


def make_labels_from_ids(labels: np.ndarray, dims: pd.DataFrame) -> pd.DataFrame:
    n_dims = len([c for c in dims.columns if c != "appid"])
    series = pd.Series(labels, index=dims.index)
    cluster = series.map(lambda value: _label_from_id(int(value), n_dims))
    return pd.DataFrame({"appid": dims["appid"], "cluster": cluster, "cluster_id": series.astype(int)})


def _label_from_id(value: int, n_dims: int) -> str:
    bits = format(value, f"0{n_dims}b")
    return "".join("H" if bit == "1" else "L" for bit in bits)
