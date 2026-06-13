"""
從 MLflow 讀取 Phase 1 實驗結果，產出比較報告。
用法：python report_phase1.py
"""
import mlflow
import pandas as pd
import sys

TRACKING_URI = "sqlite:///mlflow.db"
EXPERIMENT   = "phase1_clustering"

ALGORITHMS = ["ga", "pso", "sa", "sma", "hho", "gwo"]
METRICS    = ["silhouette", "davies_bouldin", "n_active_clusters", "dominant_cluster_pct"]


def fetch_results() -> pd.DataFrame:
    mlflow.set_tracking_uri(TRACKING_URI)
    client = mlflow.tracking.MlflowClient()
    exp = client.get_experiment_by_name(EXPERIMENT)
    if exp is None:
        print(f"Experiment '{EXPERIMENT}' not found.")
        sys.exit(1)

    runs = client.search_runs(
        experiment_ids=[exp.experiment_id],
        order_by=["attributes.start_time DESC"],
    )

    rows = []
    seen = set()
    for run in runs:
        name = run.data.tags.get("mlflow.runName", "")
        if not name or name in seen:
            continue
        seen.add(name)
        row = {"run": name}
        row["algorithm"] = run.data.params.get("algorithm", "")
        row["adaptive"]  = run.data.params.get("adaptive", "False") == "True"
        for m in METRICS:
            row[m] = run.data.metrics.get(m, float("nan"))
        end = run.info.end_time or run.info.start_time
        row["time_s"] = (end - run.info.start_time) / 1000
        rows.append(row)

    df = pd.DataFrame(rows)
    df = df[df["run"].str.replace("_aps", "").isin(ALGORITHMS)]
    return df


def render_report(df: pd.DataFrame) -> str:
    lines = []
    lines.append("# Phase 1 實驗報告\n")

    # ── (1) 各方法完整比較 ─────────────────────────────────────────────
    lines.append("## (1) 各方法比較\n")
    lines.append("> Silhouette 越高越好；DBI 越低越好\n")

    tbl = df.sort_values(["algorithm", "adaptive"])[[
        "run", "silhouette", "davies_bouldin", "n_active_clusters", "dominant_cluster_pct", "time_s"
    ]].copy()
    tbl["silhouette"]           = tbl["silhouette"].map("{:.4f}".format)
    tbl["davies_bouldin"]       = tbl["davies_bouldin"].map("{:.4f}".format)
    tbl["n_active_clusters"]    = tbl["n_active_clusters"].map("{:.0f}".format)
    tbl["dominant_cluster_pct"] = tbl["dominant_cluster_pct"].map("{:.1%}".format)
    tbl["time_s"]               = tbl["time_s"].map("{:.1f}s".format)
    tbl.columns = ["Run", "Silhouette ↑", "DBI ↓", "活躍群數", "最大群佔比", "時間"]

    lines.append(_df_to_md(tbl))
    lines.append("")

    # ── Best per metric ────────────────────────────────────────────────
    best_sil = df.loc[df["silhouette"].idxmax(), "run"]
    best_dbi = df.loc[df["davies_bouldin"].idxmin(), "run"]
    lines.append(f"- **最高 Silhouette**：`{best_sil}` ({df['silhouette'].max():.4f})")
    lines.append(f"- **最低 DBI**：`{best_dbi}` ({df['davies_bouldin'].min():.4f})\n")

    # ── (2) APS 效能成長 gap ───────────────────────────────────────────
    lines.append("## (2) APS 效能成長 Gap\n")
    lines.append("> Δ Silhouette = APS − Base（正值代表 APS 改善）")
    lines.append("> Δ DBI = Base − APS（正值代表 APS 改善，因 DBI 越低越好）\n")

    gap_rows = []
    for algo in ALGORITHMS:
        base = df[df["run"] == algo]
        aps  = df[df["run"] == f"{algo}_aps"]
        if base.empty or aps.empty:
            continue
        b = base.iloc[0]
        a = aps.iloc[0]
        d_sil  = a["silhouette"]     - b["silhouette"]
        d_dbi  = b["davies_bouldin"] - a["davies_bouldin"]   # 正 = APS 降低 DBI
        d_time = a["time_s"]         - b["time_s"]
        overhead_pct = d_time / b["time_s"] * 100 if b["time_s"] > 0 else float("nan")
        gap_rows.append({
            "Algorithm": algo.upper(),
            "Base Sil": f"{b['silhouette']:.4f}",
            "APS Sil":  f"{a['silhouette']:.4f}",
            "Δ Sil":    f"{d_sil:+.4f}",
            "Base DBI": f"{b['davies_bouldin']:.4f}",
            "APS DBI":  f"{a['davies_bouldin']:.4f}",
            "Δ DBI (↓)": f"{d_dbi:+.4f}",
            "時間開銷":  f"+{d_time:.1f}s ({overhead_pct:.0f}%)",
        })

    gap_df = pd.DataFrame(gap_rows)
    lines.append(_df_to_md(gap_df))
    lines.append("")

    # ── APS 綜合判斷 ───────────────────────────────────────────────────
    lines.append("### APS 效果判斷\n")
    for row in gap_rows:
        algo = row["Algorithm"]
        sil_up = float(row["Δ Sil"]) > 0
        dbi_up = float(row["Δ DBI (↓)"]) > 0
        if sil_up and dbi_up:
            verdict = "✅ 雙指標改善"
        elif sil_up and not dbi_up:
            verdict = "⚠️  Silhouette 改善，DBI 退步"
        elif not sil_up and dbi_up:
            verdict = "⚠️  DBI 改善，Silhouette 退步"
        else:
            verdict = "❌ 雙指標退步"
        lines.append(f"- **{algo}**：{verdict}  (Δ Sil {row['Δ Sil']}，Δ DBI {row['Δ DBI (↓)']}，開銷 {row['時間開銷']})")

    return "\n".join(lines)


def _df_to_md(df: pd.DataFrame) -> str:
    header = "| " + " | ".join(df.columns) + " |"
    sep    = "| " + " | ".join(["---"] * len(df.columns)) + " |"
    rows   = ["| " + " | ".join(str(v) for v in row) + " |" for row in df.itertuples(index=False)]
    return "\n".join([header, sep] + rows)


def main():
    df = fetch_results()
    if df.empty:
        print("No results found.")
        sys.exit(1)

    report = render_report(df)
    out = "outputs/phase1_report.md"
    with open(out, "w", encoding="utf-8") as f:
        f.write(report)

    sys.stdout.buffer.write((report + f"\n\nReport saved to {out}\n").encode("utf-8"))



if __name__ == "__main__":
    main()
