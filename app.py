"""
Steam Game Cluster Analysis — Streamlit Web GUI
Usage: streamlit run app.py
"""
import streamlit as st
import io, os, time, json, warnings, contextlib
warnings.filterwarnings("ignore")

import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import yaml
from glob import glob
from pathlib import Path

# ── Working directory ──────────────────────────────────────────────
ROOT = Path(__file__).parent
os.chdir(ROOT)
os.makedirs("outputs", exist_ok=True)
os.makedirs("outputs/analysis", exist_ok=True)

st.set_page_config(
    page_title="Steam Cluster GUI",
    page_icon="🎮",
    layout="wide",
    initial_sidebar_state="expanded",
)

# ══════════════════════════════════════════════════════════════════
# Import project modules
# ══════════════════════════════════════════════════════════════════
try:
    from src.data import load_raw, load_phase1_features, filter_phase1_samples
    from src.phase1.dimensions import build_dimensions
    from src.phase1.objective import assess_partition
    from src.phase1 import metaheuristic as _meta
    from src.phase1.metaheuristic.weighted_fitness import (
        compute_density_weights, compute_M_weighted,
    )
except ImportError as e:
    st.error(f"❌ 無法匯入專案模組: {e}")
    st.stop()

# ══════════════════════════════════════════════════════════════════
# Constants
# ══════════════════════════════════════════════════════════════════
METHODS_ALL = [
    "kmeans", "kmeans++", "pso", "pso_cold",
    "sma", "hho", "vigpso", "vigpso_cold", "avicpso",
]
SILENT_METHODS = {"hho", "vigpso", "vigpso_cold", "avicpso", "sma"}
ANALYSIS_DIR = Path("outputs/analysis")

# ══════════════════════════════════════════════════════════════════
# Shared helpers
# ══════════════════════════════════════════════════════════════════

@st.cache_data(show_spinner="載入原始資料…")
def _load_raw_cached():
    return load_raw()


def list_labels_csvs():
    return sorted(glob("outputs/labels_*.csv"))


def list_assign_configs():
    return sorted(f for f in glob("configs/*.yaml") if "assign" in Path(f).name)


def compute_M(labels: np.ndarray, values: np.ndarray) -> float:
    total = 0.0
    for k in np.unique(labels):
        mask = labels == k
        if mask.sum() == 0:
            continue
        centroid = values[mask].mean(axis=0)
        total += np.linalg.norm(values[mask] - centroid, axis=1).sum()
    return float(total)


def _hbar(df_sorted: pd.DataFrame, col: str, title: str, ax):
    n = len(df_sorted)
    colors = ["#1565C0"] + ["#90CAF9"] * (n - 1)
    ax.barh(range(n), df_sorted[col].values, color=colors)
    ax.set_yticks(range(n))
    ax.set_yticklabels(df_sorted["method"].values)
    ax.invert_yaxis()
    for i, v in enumerate(df_sorted[col].values):
        ax.text(v * 1.002, i, f" {v:.4f}", va="center", fontsize=9)
    ax.set_title(title, fontweight="bold", fontsize=11)


# ══════════════════════════════════════════════════════════════════
# Sidebar navigation
# ══════════════════════════════════════════════════════════════════
with st.sidebar:
    st.markdown("## 🎮 Steam Cluster GUI")
    st.divider()
    page = st.radio(
        "功能選單",
        ["📊 Phase 1 Benchmark", "🔍 Visualize Clusters", "🤖 XGB Analysis"],
        label_visibility="collapsed",
    )
    st.divider()
    st.caption("各頁說明")
    st.caption("**Benchmark** — 跑多種分群演算法並比較指標")
    st.caption("**Visualize** — 從 CSV 讀取 labels 並繪製群分布圖")
    st.caption("**XGB** — 用 XGB/LGBM/AdaBoost 驗證分群品質")


# ══════════════════════════════════════════════════════════════════
# PAGE 1: Phase 1 Benchmark
# ══════════════════════════════════════════════════════════════════
if page == "📊 Phase 1 Benchmark":
    st.title("📊 Phase 1 分群 Benchmark")
    st.caption("對各種分群演算法進行指標評估與比較，結果自動存入 `outputs/labels_<method>.csv`")

    # ── Config ─────────────────────────────────────────────────────
    with st.expander("⚙️ 參數設定", expanded=True):
        col1, col2, col3 = st.columns(3)
        with col1:
            iterations = st.number_input("Iterations", 50, 2000, 500, step=50)
            population = st.number_input("Population", 5, 100, 20, step=5)
        with col2:
            n_colors = st.number_input("n_colors (clusters)", 2, 20, 8, step=1)
            seed_val = st.number_input("Seed (−1 = 不固定)", -1, 9999, -1, step=1)
        with col3:
            fitness = st.selectbox(
                "Fitness type",
                ["plain", "combined", "weighted", "wcss", "wdbi", "wcombined"],
            )
            cfg_files = list_assign_configs()
            if not cfg_files:
                st.error("找不到 configs/assign_*.yaml")
            else:
                default_idx = next(
                    (i for i, c in enumerate(cfg_files) if "assign_base" in c), 0
                )
                config_file = st.selectbox("Base config", cfg_files, index=default_idx)

        methods_sel = st.multiselect("Methods", METHODS_ALL, default=METHODS_ALL)

    run_disabled = not cfg_files or not methods_sel
    if st.button("▶ 執行 Benchmark", type="primary", use_container_width=True,
                 disabled=run_disabled):
        with open(config_file, encoding="utf-8") as f:
            base_cfg = yaml.safe_load(f)

        base_cfg["iterations"] = int(iterations)
        base_cfg["population"] = int(population)
        base_cfg["n_colors"] = int(n_colors)
        if seed_val >= 0:
            base_cfg["seed"] = int(seed_val)
        base_cfg.setdefault("fitness", {})["type"] = fitness

        prog = st.progress(0, text="初始化…")
        log_box = st.empty()
        logs: list[str] = []

        def log(msg: str):
            logs.append(msg)
            log_box.code("\n".join(logs[-50:]), language=None)

        log("載入資料…")
        df_raw = _load_raw_cached()
        df_filt = filter_phase1_samples(df_raw, base_cfg.get("sample_filter"))
        phase1_df = load_phase1_features(df_filt)
        dims = build_dimensions(phase1_df, base_cfg)
        dim_cols = [c for c in dims.columns if c != "appid"]
        values = dims[dim_cols].values
        weights = compute_density_weights(values, base_cfg)
        base_cfg["_weights_cache"] = weights
        log(f"Samples: {len(values):,}  |  Dims: {dim_cols}  |  Fitness: {fitness}")

        results = []
        for i, name in enumerate(methods_sel):
            prog.progress((i + 0.3) / len(methods_sel), text=f"[{i+1}/{len(methods_sel)}] {name}…")
            log(f"\n[{i+1}/{len(methods_sel)}] Running {name}…")
            cfg = {**base_cfg, "algorithm": name}
            t0 = time.time()
            try:
                fn = _meta.get(name)
                if name in SILENT_METHODS:
                    with contextlib.redirect_stdout(io.StringIO()):
                        labels, obj = fn(values, cfg)
                else:
                    with contextlib.redirect_stdout(io.StringIO()):
                        labels, obj = fn(values, cfg)
            except Exception as e:
                log(f"  ❌ {name} 失敗: {e}")
                continue
            elapsed = time.time() - t0

            m = assess_partition(labels, values, weights)
            M = compute_M(labels, values)
            M_w = compute_M_weighted(labels, values, weights)
            wdbi = m["weighted_davies_bouldin"]

            fit_type = base_cfg.get("fitness", {}).get("type", "plain")
            obj_val = (
                None if name in ("kmeans", "kmeans++") and fit_type == "plain"
                else obj
            )

            results.append({
                "method":   name,
                "sil":      round(float(m["silhouette"]), 4),
                "dbi":      round(float(m["davies_bouldin"]), 4),
                "wdbi":     round(float(wdbi), 4) if wdbi is not None else None,
                "M":        int(M),
                "M_w":      round(float(M_w), 2),
                "n_active": int(m["n_active_clusters"]),
                "min_cl":   int(min(m["cluster_dist"].values())),
                "max_cl":   int(max(m["cluster_dist"].values())),
                "time_s":   round(elapsed, 1),
                "labels":   labels,
            })

            labels_df = (
                dims[["appid"] + dim_cols].copy()
                .assign(cluster_id=labels)
                .merge(df_raw[["appid", "name"]], on="appid")
            )[["appid", "cluster_id", "name"] + dim_cols]
            out_path = f"outputs/labels_{name}.csv"
            labels_df.to_csv(out_path, index=False)
            warn = " ⚠️ balance" if m["balance_warning"] else ""
            log(
                f"  ✓ sil={m['silhouette']:.4f}  dbi={m['davies_bouldin']:.4f}"
                f"  wdbi={wdbi:.4f if wdbi else '—'}"
                f"  n_active={m['n_active_clusters']}  t={elapsed:.1f}s{warn}"
            )

        prog.progress(1.0, text="完成!")
        st.session_state["bench_results"] = results
        st.success(f"✅ Benchmark 完成 — {len(results)} 個方法")

    # ── Results ────────────────────────────────────────────────────
    if "bench_results" in st.session_state:
        results = st.session_state["bench_results"]
        if not results:
            st.warning("無結果（所有方法都失敗）")
        else:
            disp_keys = ["method", "sil", "dbi", "wdbi", "M", "M_w",
                         "n_active", "min_cl", "max_cl", "time_s"]
            df_res = pd.DataFrame([{k: r[k] for k in disp_keys} for r in results])

            st.subheader("結果總表")
            styled = (
                df_res.style
                .highlight_max(subset=["sil"], color="#c6efce")
                .highlight_min(subset=["dbi", "M", "M_w"], color="#c6efce")
                .format({"sil": "{:.4f}", "dbi": "{:.4f}", "wdbi": "{:.4f}",
                         "M": "{:,.0f}", "M_w": "{:,.2f}", "time_s": "{:.1f}s"})
            )
            st.dataframe(styled, use_container_width=True)

            st.subheader("排名圖")
            t_sil, t_dbi, t_wdbi, t_m, t_mw = st.tabs(
                ["Silhouette ↑", "DBI ↓", "WDBI ↓", "M ↓", "M_w ↓"]
            )
            with t_sil:
                df_s = df_res.sort_values("sil", ascending=False)
                fig, ax = plt.subplots(figsize=(8, max(3, len(df_s) * 0.45)))
                _hbar(df_s, "sil", "Silhouette Score ↑ (higher is better)", ax)
                st.pyplot(fig); plt.close(fig)
            with t_dbi:
                df_s = df_res.sort_values("dbi", ascending=True)
                fig, ax = plt.subplots(figsize=(8, max(3, len(df_s) * 0.45)))
                _hbar(df_s, "dbi", "Davies-Bouldin Index ↓ (lower is better)", ax)
                st.pyplot(fig); plt.close(fig)
            with t_wdbi:
                df_s = df_res.dropna(subset=["wdbi"]).sort_values("wdbi", ascending=True)
                if df_s.empty:
                    st.info("WDBI 無資料")
                else:
                    fig, ax = plt.subplots(figsize=(8, max(3, len(df_s) * 0.45)))
                    _hbar(df_s, "wdbi", "Weighted DBI ↓ (lower is better)", ax)
                    st.pyplot(fig); plt.close(fig)
            with t_m:
                df_s = df_res.sort_values("M", ascending=True)
                fig, ax = plt.subplots(figsize=(8, max(3, len(df_s) * 0.45)))
                _hbar(df_s, "M", "M — Intra-cluster Distance ↓ (lower is better)", ax)
                st.pyplot(fig); plt.close(fig)
            with t_mw:
                df_s = df_res.sort_values("M_w", ascending=True)
                fig, ax = plt.subplots(figsize=(8, max(3, len(df_s) * 0.45)))
                _hbar(df_s, "M_w", "M_w — Weighted Distance ↓ (lower is better)", ax)
                st.pyplot(fig); plt.close(fig)

            st.subheader("下載")
            col_a, col_b = st.columns([1, 2])
            with col_a:
                csv_bytes = df_res.drop(columns=[], errors="ignore").to_csv(index=False).encode()
                st.download_button(
                    "⬇ 結果總表 CSV", csv_bytes, "benchmark_results.csv", "text/csv",
                    use_container_width=True,
                )
            with col_b:
                label_csvs = list_labels_csvs()
                if label_csvs:
                    sel_dl = st.selectbox("選擇 Labels CSV 下載", label_csvs, key="dl_sel")
                    with open(sel_dl, "rb") as f:
                        st.download_button(
                            f"⬇ {Path(sel_dl).name}", f.read(),
                            Path(sel_dl).name, "text/csv",
                            use_container_width=True, key="dl_labels",
                        )


# ══════════════════════════════════════════════════════════════════
# PAGE 2: Visualize Clusters
# ══════════════════════════════════════════════════════════════════
elif page == "🔍 Visualize Clusters":
    st.title("🔍 Visualize Clusters")
    st.caption("從 labels CSV 讀取分群結果，產生各種分布視覺化圖表")

    try:
        from visualize_clusters import (
            plot_feature_distribution, plot_violin,
            plot_cluster_summary, plot_radar, plot_scatter3d,
        )
        _viz_ok = True
    except ImportError as e:
        st.error(f"❌ 無法匯入 visualize_clusters: {e}")
        _viz_ok = False

    if _viz_ok:
        # ── Data source ───────────────────────────────────────────
        src_mode = st.radio("資料來源", ["選擇現有 CSV", "上傳 CSV"], horizontal=True)
        dims = labels = dim_cols = None

        if src_mode == "選擇現有 CSV":
            csvs = list_labels_csvs()
            if not csvs:
                st.warning("outputs/ 中找不到 labels_*.csv，請先執行 Phase 1 Benchmark。")
            else:
                sel_csv = st.selectbox("Labels CSV", csvs)
                if st.button("載入 CSV"):
                    ldf = pd.read_csv(sel_csv)
                    skip = {"appid", "cluster_id", "cluster", "name"}
                    dim_cols = [c for c in ldf.columns if c not in skip]
                    labels = ldf["cluster_id"].values.astype(int)
                    dims = ldf[["appid"] + dim_cols].copy()
                    st.session_state.update(
                        viz_dims=dims, viz_labels=labels, viz_dim_cols=dim_cols,
                        viz_source=sel_csv
                    )
        else:
            uploaded = st.file_uploader("上傳 labels CSV", type=["csv"])
            if uploaded is not None:
                ldf = pd.read_csv(uploaded)
                skip = {"appid", "cluster_id", "cluster", "name"}
                dim_cols = [c for c in ldf.columns if c not in skip]
                labels = ldf["cluster_id"].values.astype(int)
                dims = ldf[["appid"] + dim_cols].copy()
                st.session_state.update(
                    viz_dims=dims, viz_labels=labels, viz_dim_cols=dim_cols,
                    viz_source=uploaded.name
                )

        # load from session state
        if dims is None and "viz_dims" in st.session_state:
            dims = st.session_state["viz_dims"]
            labels = st.session_state["viz_labels"]
            dim_cols = st.session_state["viz_dim_cols"]

        if dims is not None and labels is not None and dim_cols is not None:
            cluster_ids = sorted(np.unique(labels))
            src_name = st.session_state.get("viz_source", "")
            st.success(
                f"✅ {src_name}  |  {len(labels):,} samples  |  {len(cluster_ids)} clusters"
            )

            # ── Cluster stats table ───────────────────────────────
            with st.expander("📋 Cluster 統計資訊", expanded=True):
                rows = []
                for k in cluster_ids:
                    mask = labels == k
                    sub = dims[dim_cols].values[mask]
                    row = {"Cluster": f"C{k}", "Count": int(mask.sum()),
                           "Ratio": f"{mask.mean()*100:.1f}%"}
                    for fi, feat in enumerate(dim_cols):
                        row[f"{feat} mean"] = round(float(sub[:, fi].mean()), 3)
                        row[f"{feat} std"]  = round(float(sub[:, fi].std()), 3)
                    rows.append(row)
                st.dataframe(pd.DataFrame(rows), use_container_width=True)

            # ── Plot selection ───────────────────────────────────
            st.subheader("選擇圖表")
            c1, c2, c3, c4, c5 = st.columns(5)
            do_box    = c1.checkbox("📦 Boxplot",    value=True)
            do_violin = c2.checkbox("🎻 Violin",     value=True)
            do_bar    = c3.checkbox("📊 Bar (mean)", value=True)
            do_radar  = c4.checkbox("🕸️ Radar",     value=True)
            do_3d     = c5.checkbox("🔵 Scatter 3D", value=True)

            if st.button("🎨 生成圖表", type="primary", use_container_width=True):
                if do_box:
                    st.subheader("Boxplot — Feature Distribution per Cluster")
                    fig = plot_feature_distribution(dims, labels, dim_cols)
                    st.pyplot(fig); plt.close(fig)

                if do_violin:
                    st.subheader("Violin Plot — Feature Distribution per Cluster")
                    fig = plot_violin(dims, labels, dim_cols)
                    st.pyplot(fig); plt.close(fig)

                if do_bar:
                    st.subheader("Bar Chart — Mean Feature Value per Cluster")
                    fig = plot_cluster_summary(dims, labels, dim_cols)
                    st.pyplot(fig); plt.close(fig)

                if do_radar:
                    st.subheader("Radar Chart — Cluster Profiles")
                    fig = plot_radar(dims, labels, dim_cols)
                    st.pyplot(fig); plt.close(fig)

                if do_3d:
                    st.subheader("3D Scatter Plot")
                    if len(dim_cols) < 3:
                        st.info("Scatter 3D 需要至少 3 個維度")
                    else:
                        fig = plot_scatter3d(dims, labels, dim_cols)
                        if fig is not None:
                            st.pyplot(fig); plt.close(fig)

            # ── Save all plots ───────────────────────────────────
            st.divider()
            tag = src_name.rsplit("/", 1)[-1].rsplit("\\", 1)[-1].replace(".csv", "")
            if st.button("💾 儲存所有圖表到磁碟", use_container_width=True):
                saved = []
                plot_map = [
                    ("box",      do_box,    lambda: plot_feature_distribution(dims, labels, dim_cols)),
                    ("violin",   do_violin, lambda: plot_violin(dims, labels, dim_cols)),
                    ("bar",      do_bar,    lambda: plot_cluster_summary(dims, labels, dim_cols)),
                    ("radar",    do_radar,  lambda: plot_radar(dims, labels, dim_cols)),
                    ("scatter3d",do_3d,     lambda: plot_scatter3d(dims, labels, dim_cols)),
                ]
                for name, do_it, fn in plot_map:
                    if not do_it:
                        continue
                    fig = fn()
                    if fig is None:
                        continue
                    path = f"cluster_{name}_{tag}.png"
                    fig.savefig(path, dpi=150, bbox_inches="tight")
                    plt.close(fig)
                    saved.append(path)
                if saved:
                    st.success("✅ 已儲存: " + ", ".join(saved))


# ══════════════════════════════════════════════════════════════════
# PAGE 3: XGB Analysis
# ══════════════════════════════════════════════════════════════════
elif page == "🤖 XGB Analysis":
    st.title("🤖 XGB / LGBM / AdaBoost Cluster Analysis")
    st.caption("以機器學習分類器驗證分群品質，產生混淆矩陣、ROC、特徵重要性等圖表")

    # ── Config ───────────────────────────────────────────────────
    with st.expander("⚙️ 參數設定", expanded=True):
        c1, c2 = st.columns(2)
        with c1:
            csvs = list_labels_csvs()
            if not csvs:
                st.warning("找不到 labels CSV，請先執行 Phase 1 Benchmark。")
                sel_csv = None
            else:
                sel_csv = st.selectbox("Labels CSV", csvs)
            n_trials_xgb = st.slider("Optuna Trials (XGB / LightGBM)", 5, 100, 50, step=5)
        with c2:
            n_trials_ada = st.slider("Optuna Trials (AdaBoost)", 5, 60, 30, step=5)
            models_sel = st.multiselect(
                "模型",
                ["XGBoost", "LightGBM", "AdaBoost"],
                default=["XGBoost", "LightGBM", "AdaBoost"],
            )

    st.info(
        "⏱️ 提示：XGB/LGBM 各 50 trials × 5-fold CV 約需 10–30 分鐘，"
        "可先用較少 trials 測試。"
    )

    run_disabled = not sel_csv or not models_sel
    if st.button("▶ 執行分析", type="primary", use_container_width=True,
                 disabled=run_disabled):

        try:
            from analyze_clusters_xgb import (
                load_data, build_features,
                train_xgb, train_lgbm, train_adaboost,
                plot_confusion_matrix, plot_feature_importance,
                plot_learning_curve, plot_per_class_f1,
                plot_prediction_confidence, plot_roc_auc, plot_pr_curve,
                plot_cv_scores, plot_optuna_history,
                plot_model_comparison, plot_roc_combined, plot_pr_combined,
                plot_confusion_matrix_combined, plot_feature_importance_combined,
                print_report, save_model_params,
                plot_feature_correlation, plot_class_distribution,
            )
        except ImportError as e:
            st.error(f"❌ 無法匯入 analyze_clusters_xgb: {e}")
            st.stop()

        log_box = st.empty()
        logs: list[str] = []

        def log(msg: str):
            logs.append(msg)
            log_box.code("\n".join(logs[-60:]), language=None)

        # ── Load data ─────────────────────────────────────────────
        log(f"載入資料: {sel_csv}")
        with st.spinner("載入特徵資料…"):
            buf = io.StringIO()
            with contextlib.redirect_stdout(buf):
                df_raw, df_feat, y = load_data(sel_csv)
                X_tr, X_te, y_tr, y_te, sel_cols = build_features(df_raw, df_feat, y, {})
            for line in buf.getvalue().splitlines():
                log(line)

        log(f"Features: {len(sel_cols)}  Train: {len(y_tr):,}  Test: {len(y_te):,}")

        # class distribution & feature correlation (saved to disk by _save())
        buf = io.StringIO()
        with contextlib.redirect_stdout(buf):
            plot_class_distribution(y_tr, y_te)
            plot_feature_correlation(X_tr, sel_cols)
        log("  ✓ class_distribution.png / feature_correlation.png 已儲存")

        TRAIN_MAP = {
            "XGBoost":  (train_xgb,      n_trials_xgb),
            "LightGBM": (train_lgbm,     n_trials_xgb),
            "AdaBoost": (train_adaboost, n_trials_ada),
        }

        all_results: dict = {}
        all_models:  dict = {}

        for model_name in models_sel:
            log(f"\n{'='*50}")
            log(f"  Training: {model_name}  ({TRAIN_MAP[model_name][1]} trials)…")
            train_fn, n_trials = TRAIN_MAP[model_name]

            with st.spinner(f"訓練 {model_name}（Optuna {n_trials} trials）…"):
                buf = io.StringIO()
                with contextlib.redirect_stdout(buf):
                    model, study = train_fn(X_tr, y_tr, n_trials=n_trials)
                for line in buf.getvalue().splitlines():
                    log(line)

            buf = io.StringIO()
            with contextlib.redirect_stdout(buf):
                acc, f1  = print_report(model, X_te, y_te, tag=model_name)
                cv       = plot_cv_scores(model, X_tr, y_tr, tag=model_name)
                roc_auc  = plot_roc_auc(model, X_te, y_te, tag=model_name)
                ap       = plot_pr_curve(model, X_te, y_te, tag=model_name)
                plot_confusion_matrix(model, X_te, y_te, tag=model_name)
                plot_feature_importance(model, sel_cols, tag=model_name)
                plot_learning_curve(model, X_tr, y_tr, tag=model_name)
                plot_per_class_f1(model, X_te, y_te, tag=model_name)
                plot_prediction_confidence(model, X_te, y_te, tag=model_name)
                plot_optuna_history(study, tag=model_name)
            for line in buf.getvalue().splitlines():
                log(line)

            metrics = {"acc": acc, "f1": f1, "auc": roc_auc, "ap": ap, "cv": cv}
            save_model_params(model_name, study, metrics)
            all_results[model_name] = metrics
            all_models[model_name]  = model
            log(f"  ✓ {model_name}: Acc={acc:.4f}  F1={f1:.4f}  AUC={roc_auc:.4f}  AP={ap:.4f}  CV={cv:.4f}")

        if len(all_models) > 1:
            buf = io.StringIO()
            with contextlib.redirect_stdout(buf):
                plot_roc_combined(all_models, X_te, y_te)
                plot_pr_combined(all_models, X_te, y_te)
                plot_confusion_matrix_combined(all_models, X_te, y_te)
                plot_feature_importance_combined(all_models, sel_cols)
            log("  ✓ Combined 比較圖已儲存")
        buf = io.StringIO()
        with contextlib.redirect_stdout(buf):
            plot_model_comparison(all_results)
        log("  ✓ model_comparison.png 已儲存")

        log(f"\n✅ 分析完成 — 圖表存於 outputs/analysis/")
        st.session_state["xgb_results"] = all_results
        st.session_state["xgb_models_trained"] = list(all_models.keys())
        st.success("✅ 分析完成")

    # ── Show results ──────────────────────────────────────────────
    if "xgb_results" in st.session_state:
        results = st.session_state["xgb_results"]
        trained_models = st.session_state.get("xgb_models_trained", list(results.keys()))

        st.subheader("📈 模型指標比較")
        df_res = (
            pd.DataFrame(results)
            .T.reset_index()
            .rename(columns={"index": "Model", "acc": "Accuracy",
                              "f1": "F1-macro", "auc": "Macro AUC",
                              "ap": "Macro AP", "cv": "CV F1"})
        )
        st.dataframe(
            df_res.style
            .highlight_max(
                subset=["Accuracy", "F1-macro", "Macro AUC", "Macro AP", "CV F1"],
                color="#c6efce",
            )
            .format({"Accuracy": "{:.4f}", "F1-macro": "{:.4f}",
                     "Macro AUC": "{:.4f}", "Macro AP": "{:.4f}", "CV F1": "{:.4f}"}),
            use_container_width=True,
        )

        # ── Display saved plots ───────────────────────────────────
        st.subheader("📊 分析圖表")
        all_pngs = {p.stem: p for p in sorted(ANALYSIS_DIR.glob("*.png"))}

        if not all_pngs:
            st.info("尚無圖表（請先執行分析）")
        else:
            # Tab: overview
            overview_keys = [
                "class_distribution", "feature_correlation",
                "model_comparison", "roc_combined", "pr_combined",
                "confusion_matrix_combined", "feature_importance_combined",
            ]
            # Per-model plot keys
            per_model_templates = [
                "optuna_history_{}", "confusion_matrix_{}", "feature_importance_{}",
                "learning_curve_{}", "per_class_f1_{}", "prediction_confidence_{}",
                "roc_auc_{}", "pr_curve_{}", "cv_scores_{}",
            ]

            tab_names = ["綜合比較"] + trained_models
            tabs = st.tabs(tab_names)

            with tabs[0]:
                shown = 0
                for key in overview_keys:
                    if key in all_pngs:
                        st.image(str(all_pngs[key]), caption=key, use_column_width=True)
                        shown += 1
                if shown == 0:
                    st.info("無綜合比較圖（多模型才產生）")

            for tab, mname in zip(tabs[1:], trained_models):
                with tab:
                    shown = 0
                    for tmpl in per_model_templates:
                        key = tmpl.format(mname)
                        if key in all_pngs:
                            st.image(str(all_pngs[key]), caption=key, use_column_width=True)
                            shown += 1
                    if shown == 0:
                        st.info(f"{mname} 尚無圖表")

            # ── Download params JSON ──────────────────────────────
            st.divider()
            st.subheader("下載")
            param_jsons = sorted(ANALYSIS_DIR.glob("params_*.json"))
            if param_jsons:
                sel_json = st.selectbox("選擇 params JSON", [p.name for p in param_jsons])
                json_path = ANALYSIS_DIR / sel_json
                with open(json_path, "rb") as f:
                    st.download_button(
                        f"⬇ {sel_json}", f.read(), sel_json, "application/json",
                        use_container_width=True,
                    )
