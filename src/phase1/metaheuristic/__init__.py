from .ga  import run as ga
from .pso import run as pso
from .sa  import run as sa
from .sma import run as sma
from .hho import run as hho
from .gwo import run as gwo

REGISTRY = {"ga": ga, "pso": pso, "sa": sa, "sma": sma, "hho": hho, "gwo": gwo}


def get(name: str):
    if name not in REGISTRY:
        raise ValueError(f"Unknown algorithm: {name}. Choose from {list(REGISTRY)}")
    return REGISTRY[name]
