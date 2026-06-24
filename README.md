# Steam Game Market Positioning — Two-Phase ML Framework

基於 Kaggle Steam 遊戲資料集，以兩階段框架進行市場定位分析：
**Phase 1** 無監督分群（Metaheuristic Clustering）為遊戲賦予市場位置標籤；
**Phase 2** 監督式分類驗證遊戲描述性特徵能否預測其市場定位。

## 資料來源

[Kaggle — Steam Games March 2025 (Cleaned)](https://www.kaggle.com/datasets/artermiloff/steam-games-dataset)
— 89,618 款遊戲 × 47 欄位（評分、遊玩時長、在線人數、標籤、描述等）

## 專案結構

```
├── src/
│   ├── data.py                  # 資料載入與前處理
│   ├── phase1/
│   │   ├── dimensions.py        # 分群維度建構 (Rating, Playtime, Hotness)
│   │   ├── objective.py         # 適應度函數與評估指標 (Silhouette, DBI, WDBI)
│   │   ├── clustering.py        # 標籤產生
│   │   ├── assignment.py        # GA 分群 (BAM 擾動 + KNN 局部搜尋)
│   │   └── metaheuristic/
│   │       ├── centroid_fitness.py    # Centroid encoding fitness + factory
│   │       ├── weighted_fitness.py    # 密度加權適應度函數
│   │       ├── kmeans.py              # KMeans / KMeans++
│   │       ├── pso.py                 # PSO (warm / cold start)
│   │       ├── vigpso.py              # VIGPSO (warm / cold start)
│   │       ├── avicpso.py             # AVICPSO
│   │       ├── hho.py                 # Harris Hawks Optimization
│   │       └── sma.py                 # Slime Mould Algorithm
│   └── phase2/
│       ├── pipeline.py          # 特徵工程 pipeline (數值/時間/文字/類別)
│       ├── train.py             # 訓練與評估
│       ├── optimize.py          # 超參數最佳化 (Optuna/Random/Grid)
│       └── features/
│           ├── numerical.py
│           ├── temporal.py
│           ├── text.py          # TF-IDF TextEncoder (fit/transform 分離)
│           └── categorical.py   # Target Encoder
├── configs/
│   ├── assign_base.yaml         # Phase 1 基礎配置
│   ├── assign_avicpso*.yaml     # AVICPSO 多組實驗配置
│   ├── phase2.yaml              # Phase 2 基礎配置
│   └── p2_*.yaml                # 各模型 Phase 2 配置
├── benchmark_phase1.py          # Phase 1 多演算法比較
├── benchmark_phase2.py          # Phase 2 多模型比較
├── run_phase1.py                # 單次 Phase 1 執行
├── run_phase2.py                # 單次 Phase 2 執行
├── visualize_clusters.py        # 分群視覺化 (bar/boxplot/radar/scatter3d/violin)
├── analyze_clusters_xgb.py      # XGBoost 特徵重要性分析
├── eda_package_recommend.py     # 套件推薦 EDA
├── app.py                       # 互動式應用介面
├── methodology.md               # 研究方法論詳述
└── spec.md                      # 方法規格書
```

## Phase 1 — 無監督分群

### 分群維度

| 維度 | 欄位 | 前處理 |
|------|------|--------|
| Rating | `pct_pos_total` | MinMax |
| Playtime | `average_playtime_forever` | log1p → MinMax |
| Hotness | `peak_ccu` | log1p → MinMax |

### 解表示法

採用 **Centroid Encoding**：以 K×d 實數向量表示 K 個群心座標（預設 K=8, d=3，搜尋維度=24）。

### 演算法

| 方法 | 說明 |
|------|------|
| KMeans / KMeans++ | sklearn 基線 |
| PSO / PSO (cold) | 標準粒子群最佳化，支援 warm/cold start |
| VIGPSO / VIGPSO (cold) | Variable Interaction Graph PSO |
| AVICPSO | Adaptive VIGPSO |
| HHO | Harris Hawks Optimization |
| SMA | Slime Mould Algorithm |
| GA | Assignment 表示法 + BAM 擾動 + KNN 局部搜尋 |

### Fitness 函數

透過 `make_fitness_fn` 工廠依 config 分派：

| 類型 | 說明 |
|------|------|
| `plain` | Maulik & Bandyopadhyay M（歐氏距離總和） |
| `weighted` | 密度加權 M |
| `wcss` | 加權 WCSS |
| `wdbi` | 加權 DBI |
| `wcombined` | 加權組合指標 |
| `combined` | 組合適應度 |

### 執行

```bash
# 單次執行
python run_phase1.py --config configs/assign_base.yaml

# Benchmark（所有演算法比較）
python benchmark_phase1.py --iterations 500 --population 20 --n_colors 8 --fitness plain
```

## Phase 2 — 監督式分類

以 Phase 1 產出的群集標籤為 y，訓練分類器預測遊戲市場定位。

### 特徵工程

- **數值特徵**：遊戲基本屬性
- **時間特徵**：發行日期衍生
- **文字特徵**：TF-IDF（`short_description`, `about_the_game`, `detailed_description`），fit/transform 分離避免 data leakage
- **類別特徵**：Target Encoding（`genres`, `categories`, `tags`, `supported_languages`）

### 模型

Random Forest / XGBoost / LightGBM / CatBoost / SVM

### 超參數最佳化

支援 Optuna、RandomizedSearchCV、GridSearchCV，或 `none`（使用預設參數）。

### 執行

```bash
# 單次執行
python run_phase2.py --p1-config configs/assign_avicpso.yaml --p2-config configs/phase2.yaml

# Benchmark
python benchmark_phase2.py
```

## 視覺化

```bash
# 分群結果視覺化
python visualize_clusters.py

# 群集特徵重要性分析
python analyze_clusters_xgb.py

# 互動式應用
python app.py
```

## 環境設定

```bash
pip install -r requirements.txt
```

主要依賴：scikit-learn, xgboost, lightgbm, catboost, optuna, mlflow, pandas, numpy, matplotlib
