"""
依最佳門檻值對每款遊戲貼上 HHH / HHL / ... 標籤。
"""
import numpy as np
import pandas as pd
from .objective import assign_labels


LABEL_MAP = {
    7: "HHH", 6: "HHL", 5: "HLH", 4: "HLL",
    3: "LHH", 2: "LHL", 1: "LLH", 0: "LLL",
}


def make_labels(thresholds: list[float], dims: pd.DataFrame) -> pd.DataFrame:
    numeric = assign_labels(thresholds, dims)
    labels = pd.Series(numeric, index=dims.index).map(LABEL_MAP)
    return pd.DataFrame({"appid": dims["appid"], "cluster": labels, "cluster_id": numeric})
