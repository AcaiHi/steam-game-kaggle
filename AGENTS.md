# Repository Guidelines

## Project Structure & Module Organization
This is a Python ML project for Steam game clustering and downstream classification.

- `src/data.py` loads the raw CSV and removes Phase 2 leakage columns. The expected dataset lives under ignored `datasets/`.
- `src/phase1/` contains clustering dimensions, objectives, adaptive perturbation logic, and metaheuristics in `src/phase1/metaheuristic/`.
- `src/phase2/` contains feature engineering, training, and optimization pipelines.
- `configs/` stores YAML experiments. Phase 1 configs cover `ga`, `pso`, `sa`, `sma`, `hho`, and `gwo`; Phase 2 configs follow `p2_*.yaml`.
- Root scripts (`run_phase1.py`, `run_all_phase1.py`, `run_phase2.py`, `run_all_phase2.py`) are the main entry points. Generated outputs, MLflow runs, local databases, and datasets are ignored.

## Build, Test, and Development Commands
Use Python from the repository root so relative paths resolve correctly.

- `python run_phase1.py --config configs/phase1.yaml`: run one Phase 1 clustering experiment.
- `python run_all_phase1.py`: run all Phase 1 metaheuristic experiments and write labels to `outputs/`.
- `python run_phase2.py --config configs/phase2.yaml --labels outputs/phase1_labels.csv`: train/evaluate one Phase 2 model using Phase 1 labels.
- `python run_all_phase2.py --labels outputs/phase1_labels.csv --group A`: run a Phase 2 experiment group (`A`, `B`, or `C`).
- `mlflow ui --backend-store-uri sqlite:///mlflow.db`: inspect experiment metrics after runs.

## Coding Style & Naming Conventions
Follow existing Python style: 4-space indentation, snake_case functions and variables, and small phase-oriented modules. Keep experiment parameters in YAML instead of hard-coding them. Name new Phase 2 configs with `p2_<model>_<variant>.yaml` when practical. Preserve UTF-8 because several scripts include Chinese comments and messages.

## Testing Guidelines
There is no formal test suite. For code changes, run the narrowest relevant experiment and confirm it completes. For shared pipeline changes, validate both phases: one Phase 1 config to generate labels, then one Phase 2 config using those labels. Do not commit generated `outputs/`, `mlruns/`, `mlflow.db`, `catboost_info/`, or `datasets/`.

## Commit & Pull Request Guidelines
The history only contains an initial commit, so use concise, descriptive messages. Prefer imperative subjects such as `Add CatBoost feature selection config` or `Fix Phase 2 label loading`. Pull requests should summarize the change, list commands run, note required local dataset paths, and include metric changes or chart screenshots when relevant.

## Security & Configuration Tips
Do not commit raw Kaggle data, credentials, `.env` files, or generated MLflow artifacts. If paths or tracking URIs differ locally, keep the YAML change ignored or document it clearly in the PR.
