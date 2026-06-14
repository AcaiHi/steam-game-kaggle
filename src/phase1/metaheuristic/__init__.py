from ..assignment import run as ga
from .kmeans import run_kmeans, run_kmeans_plus
from .pso import run_pso
from .sma import run_sma
from .hho import run_hho
from .vigpso import run_vigpso
from .avicpso import run_avicpso

REGISTRY = {
    "ga":       ga,
    "kmeans":   run_kmeans,
    "kmeans++": run_kmeans_plus,
    "sma":      run_sma,
    "hho":      run_hho,
    "pso":      run_pso,
    "vigpso":   run_vigpso,
    "avicpso":  run_avicpso,
}


def get(name: str):
    if name not in REGISTRY:
        raise ValueError(f"Unknown algorithm: {name}. Choose from {list(REGISTRY)}")
    return REGISTRY[name]
