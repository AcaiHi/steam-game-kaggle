import numpy as np
import random
import math
import time
from typing import Optional
from .base_optimizer import BaseOptimizer, Solution
from .optimizer_config import HHOConfig
from tqdm import tqdm

class HHOOptimizer(BaseOptimizer):
    def __init__(self, config: Optional[HHOConfig] = None):
        self.config = config or HHOConfig()
        
    @staticmethod
    def levy_flight(dim, beta=1.5):
        """Generate a Levy flight step."""
        # beta = 1.5
        sigma = (
            math.gamma(1 + beta)
            * math.sin(math.pi * beta / 2)
            / (math.gamma((1 + beta) / 2) * beta * 2 ** ((beta - 1) / 2))
        ) ** (1 / beta)
        u = 0.01 * np.random.randn(dim) * sigma
        v = np.random.randn(dim)
        zz = np.power(np.absolute(v), (1 / beta))
        step = np.divide(u, zz)
        return step
        
    def optimize(self, objective_func, n_dim, n_agents, max_iter, config=None):
        """Harris Hawks Optimization implementation"""
        if config:
            self.config = config
            
        # Use configuration in the algorithm
        # beta = self.config.beta
        early_stop_iter = self.config.early_stop_iter
        # levy_step_size = self.config.levy_step_size
        lb = np.zeros(n_dim)
        ub = np.ones(n_dim)
        
        # Initialize
        rabbit_location = np.zeros(n_dim)
        rabbit_energy = (float("inf"))
        X = np.random.uniform(0, 1, (n_agents, n_dim))
        convergence_curve = np.zeros(max_iter)
        
        timer_start = time.time()
        t = 0
        
        # Add tqdm progress bar
        with tqdm(total=max_iter, desc="HHO Optimization") as pbar:
            while t < max_iter:
                for i in range(n_agents):
                    X = self.clip_to_bounds(X)
                    fitness = objective_func(X[i, :])
                    
                    if fitness < rabbit_energy:
                        rabbit_energy = fitness
                        rabbit_location = X[i, :]
                
                E1 = 2 * (1 - (t / max_iter))
                
                for i in range(n_agents):
                    E0 = 2 * random.random() - 1
                    escaping_energy = E1 * E0
                    
                    if abs(escaping_energy) >= 1:
                        # Exploration phase
                        if random.random() < 0.5:
                            rand_hawk = X[math.floor(n_agents * random.random()), :]
                            X[i, :] = rand_hawk - random.random() * abs(rand_hawk - 2 * random.random() * X[i, :])
                        else:
                            X[i, :] = (rabbit_location - X.mean(0)) - random.random() * ((ub - lb) * random.random() + lb)
                        X[i, :] = self.clip_to_bounds(X[i, :])
                    else:
                        # Exploitation phase
                        r = random.random()
                        
                        if r >= 0.5 and abs(escaping_energy) < 0.5:
                            X[i, :] = rabbit_location - escaping_energy * abs(rabbit_location - X[i, :])
                            X[i, :] = self.clip_to_bounds(X[i, :])
                        elif r >= 0.5 and abs(escaping_energy) >= 0.5:
                            jump_strength = 2 * (1 - random.random())
                            X[i, :] = (rabbit_location - X[i, :]) - escaping_energy * abs(jump_strength * rabbit_location - X[i, :])
                            X[i, :] = self.clip_to_bounds(X[i, :])
                        else:
                            # Progressive rapid dives
                            jump_strength = 2 * (1 - random.random())
                            if abs(escaping_energy) >= 0.5:  # Soft besiege
                                X_new = rabbit_location - escaping_energy * abs(jump_strength * rabbit_location - X[i, :])
                                X_new = self.clip_to_bounds(X_new)
                            else:  # Hard besiege
                                X_new = rabbit_location - escaping_energy * abs(jump_strength * rabbit_location - X.mean(0))
                                X_new = self.clip_to_bounds(X_new)
                            
                            if objective_func(X_new) < objective_func(X[i, :]):
                                X[i, :] = X_new
                            else:
                                X_levy = X_new + np.multiply(np.random.randn(n_dim), self.levy_flight(n_dim))
                                X_levy = self.clip_to_bounds(X_levy)
                                X[i, :] = X_levy if objective_func(X_levy) < objective_func(X[i, :]) else X_new
                
                
                convergence_curve[t] = rabbit_energy
                
                # Early stopping
                if t >= early_stop_iter and convergence_curve[t] == convergence_curve[t - 15]:
                    convergence_curve[t+1:] = convergence_curve[t]
                    # Update progress bar to completion if early stopping
                    pbar.update(max_iter - t)
                    break
                
                t += 1
                # Update progress bar
                pbar.update(1)
                # Optionally update description with current best fitness
                pbar.set_description(f"HHO Optimization (Best: {rabbit_energy:.6f})")
            
        
        return Solution(
            best_location=rabbit_location,
            best_fitness=rabbit_energy,
            convergence=convergence_curve,
            execution_time=time.time() - timer_start,
            best_params=dict(zip(range(n_dim), rabbit_location)),
            optimizer_name="HHO"
        )