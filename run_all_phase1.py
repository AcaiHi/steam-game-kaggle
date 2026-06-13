"""
一次跑完全部 12 種 Phase 1 實驗。
用法：python run_all_phase1.py
"""
import subprocess
import sys
import time

CONFIGS = [
    "configs/ga.yaml",
    "configs/ga_aps.yaml",
    "configs/pso.yaml",
    "configs/pso_aps.yaml",
    "configs/sa.yaml",
    "configs/sa_aps.yaml",
    "configs/sma.yaml",
    "configs/sma_aps.yaml",
    "configs/hho.yaml",
    "configs/hho_aps.yaml",
    "configs/gwo.yaml",
    "configs/gwo_aps.yaml",
]


def main():
    results = []
    total = len(CONFIGS)

    for i, config in enumerate(CONFIGS, 1):
        name = config.replace("configs/", "").replace(".yaml", "")
        print(f"\n{'='*50}")
        print(f"[{i}/{total}] {name}")
        print(f"{'='*50}")

        t0 = time.time()
        ret = subprocess.run(
            [sys.executable, "run_phase1.py", "--config", config],
            cwd=".",
        )
        elapsed = time.time() - t0

        status = "OK" if ret.returncode == 0 else "FAILED"
        results.append((name, status, elapsed))
        print(f">> {status}  ({elapsed:.1f}s)")

    print(f"\n{'='*50}")
    print("Summary")
    print(f"{'='*50}")
    for name, status, elapsed in results:
        print(f"  {status:6}  {elapsed:6.1f}s  {name}")

    failed = [r for r in results if r[1] == "FAILED"]
    if failed:
        print(f"\n{len(failed)} run(s) failed.")
        sys.exit(1)
    else:
        print(f"\nAll {total} runs completed.")


if __name__ == "__main__":
    main()
