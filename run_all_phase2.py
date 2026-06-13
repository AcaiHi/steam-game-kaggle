"""
一次跑完全部 Phase 2 實驗。
用法：python run_all_phase2.py [--labels outputs/phase1_labels.csv]

實驗設計：
  Group A — 模型比較    (固定: mutual_info, no opt)
  Group B — 特徵選擇比較 (固定: CatBoost, no opt)
  Group C — 超參數調整比較 (固定: CatBoost + mutual_info)
"""
import subprocess
import sys
import time
import argparse

EXPERIMENTS = [
    # ── Group A: Model Comparison ──────────────────────────────────────────
    ("A", "configs/p2_rf.yaml"),
    ("A", "configs/p2_xgb.yaml"),
    ("A", "configs/p2_lgbm.yaml"),
    ("A", "configs/p2_cat.yaml"),
    ("A", "configs/p2_svm.yaml"),
    # ── Group B: Feature Selection ─────────────────────────────────────────
    ("B", "configs/p2_cat_mi.yaml"),
    ("B", "configs/p2_cat_rfe.yaml"),
    ("B", "configs/p2_cat_var.yaml"),
    ("B", "configs/p2_cat_none.yaml"),
    # ── Group C: Param Optimization ────────────────────────────────────────
    ("C", "configs/p2_cat_opt_none.yaml"),
    ("C", "configs/p2_cat_opt_optuna.yaml"),
]


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--labels", default="outputs/phase1_labels.csv")
    parser.add_argument("--group",  default=None, help="只跑某組，如 A / B / C")
    args = parser.parse_args()

    exps = EXPERIMENTS
    if args.group:
        exps = [(g, c) for g, c in EXPERIMENTS if g == args.group.upper()]
        if not exps:
            print(f"Unknown group: {args.group}")
            sys.exit(1)

    results = []
    total = len(exps)

    for i, (group, config) in enumerate(exps, 1):
        name = config.replace("configs/", "").replace(".yaml", "")
        print(f"\n{'='*55}")
        print(f"[{i}/{total}] Group {group} | {name}")
        print(f"{'='*55}")

        t0 = time.time()
        ret = subprocess.run(
            [sys.executable, "run_phase2.py", "--config", config, "--labels", args.labels],
            cwd=".",
        )
        elapsed = time.time() - t0

        status = "OK" if ret.returncode == 0 else "FAILED"
        results.append((group, name, status, elapsed))
        print(f">> {status}  ({elapsed:.1f}s)")

    # ── Summary ──────────────────────────────────────────────────────────────
    print(f"\n{'='*55}")
    print(f"{'Group':<7} {'Config':<28} {'Status':<8} {'Time':>6}")
    print(f"{'-'*55}")
    for group, name, status, elapsed in results:
        print(f"  {group:<5} {name:<28} {status:<8} {elapsed:>5.1f}s")
    print(f"{'='*55}")

    failed = [r for r in results if r[2] == "FAILED"]
    if failed:
        print(f"\n{len(failed)} run(s) failed.")
        sys.exit(1)
    else:
        print(f"\nAll {total} runs completed successfully.")
        print(f"View results: mlflow ui --backend-store-uri sqlite:///mlflow.db")


if __name__ == "__main__":
    main()
