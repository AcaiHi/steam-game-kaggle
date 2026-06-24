import numpy as np
import pandas as pd


def make_labels_from_ids(labels: np.ndarray, dims: pd.DataFrame) -> pd.DataFrame:
    series = pd.Series(labels, index=dims.index).astype(int)
    return pd.DataFrame({"appid": dims["appid"], "cluster": series, "cluster_id": series})
