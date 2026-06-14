# 專案架構 Spec

## 設計原則

- 每個階段獨立可執行，互不依賴
- 策略可抽換（不同初始化方式、擾動策略、局部搜尋）
- 所有實驗參數走 config，結果走 MLflow
- 不過度抽象：不用複雜的 base class hierarchy，用簡單的 function / dict dispatch

---

## 目錄結構

```
steam-game-kaggle/
│
├── datasets/                        # 原始資料（已存在，已 .gitignore）
│
├── configs/
│   ├── phase1.yaml                  # 舊版 threshold 表示法參數（已棄用）
│   ├── assign_ga.yaml               # assignment + GA 直接執行參數
│   ├── assign_kmeans.yaml           # assignment + KMeans（random init）
│   ├── assign_kmeans_plus.yaml      # assignment + KMeans++
│   ├── assign_base.yaml             # assignment benchmark 基底參數（benchmark() 程式呼叫）
│   ├── phase2.yaml                  # Phase 2 基準實驗參數
│   ├── p2_rf.yaml / p2_lgbm.yaml / p2_xgb.yaml / p2_cat.yaml / p2_svm.yaml
│   │                                # 各分類器實驗
│   └── p2_cat_*.yaml                # CatBoost 特徵選擇變體
│
├── src/
│   ├── data.py                      # 資料載入、基本清理、樣本篩選
│   │
│   ├── phase1/
│   │   ├── dimensions.py            # 維度特徵合成（支援多欄位、log、IQR clip）
│   │   ├── objective.py             # 目標函數與評估指標（partition level）
│   │   ├── clustering.py            # cluster id → H/L 標籤字串轉換（make_labels_from_ids）
│   │   ├── assignment.py            # Assignment GA：直接對每筆遊戲分配 cluster id
│   │   └── metaheuristic/
│   │       ├── __init__.py          # REGISTRY：{"ga", "kmeans", "kmeans++"}
│   │       ├── ga.py                # Genetic Algorithm（含 BAM 擾動、KNN 局部搜尋）
│   │       └── kmeans.py            # KMeans（random init）與 KMeans++
│   │
│   └── phase2/
│       ├── features/
│       │   ├── numerical.py         # price, dlc_count, achievements...
│       │   ├── categorical.py       # genres, tags, categories（multi-hot / count）
│       │   ├── temporal.py          # release_date → year, month, days_since
│       │   └── text.py              # short_description 等（TF-IDF / embedding）
│       ├── pipeline.py              # 組合各 feature 模組、selection、extraction
│       ├── optimize.py              # 超參數最佳化（optuna / random / grid / none）
│       └── train.py                 # 訓練與評估分類器
│
├── run_phase1.py                    # Phase 1 執行入口（assignment GA）
├── run_phase2.py                    # Phase 2 執行入口
├── run_all_phase2.py                # 批次執行所有 Phase 2 實驗
│
├── outputs/                         # 輸出標籤 CSV（已 .gitignore）
├── mlruns / mlflow.db               # MLflow 追蹤資料（已 .gitignore）
├── spec.md                          # 方法 spec
└── project_architecture.md          # 本文件
```

---

## Phase 1 流程（Assignment 表示法 + GA）

```
載入資料
  → filter_phase1_samples：依 sample_filter 篩選有效遊戲
  → dimensions.py：合成 N 個維度的代表性數值（0~1）
  → assignment.py / run_ga：GA 最佳化 cluster id 向量
      目標函數 objective.py / evaluate_partition：
        ① 計算 intra-cluster variance 與 inter-cluster distance
        ② 空群懲罰（penalty_per_missing × 空群數）
      GA 策略軸（cfg["sa"] 子欄位）：
        init：kmeans | kmeans++ | random | sorted_partition
        perturbation：baseline | improve（BAM，邊界感知突變）
        local_search：false | true（KNN 多數決局部搜尋）
  → clustering.py / make_labels_from_ids：cluster id → H/L 標籤字串
  → MLflow 記錄：目標函數值、Silhouette、DBI、群分布
```

### Population 多樣初始化

GA 的第一個個體使用 `init_method` 指定方式，其餘個體依序循環所有 4 種初始化方法（random / kmeans / kmeans++ / sorted_partition），確保族群多樣性。

### BAM（Boundary-Aware Mutation）

當 `perturbation: improve` 時，每個遊戲的突變機率正比於其 KNN 不一致度（鄰居中不同 cluster 的比例）。邊界遊戲更容易被突變，穩定遊戲則較少被改動。

### KNN 局部搜尋

當 `local_search: true` 時，每輪對最佳個體進行 KNN 多數決修正：找出不一致度最高的遊戲，將其移至鄰居多數所在的 cluster，若能降低目標函數則接受。局部搜尋步數隨迭代進度從 0 線性增長至 `base_moves × gamma`。

---

## configs/assign_ga.yaml 欄位說明

```yaml
representation: assignment
algorithm: ga
run_name: assign_ga
iterations: 100
population: 10
seed: 42
n_colors: 8              # cluster 數量

sample_filter:
  enabled: true
  mode: all_positive     # any_positive | all_positive
  columns: [pct_pos_total, average_playtime_forever, peak_ccu]

dimensions:
  rating:
    features: [pct_pos_total]
  playtime:
    features: [average_playtime_forever]
    log_scale: true
  hotness:
    features: [peak_ccu]
    log_scale: true

objective:
  intra_weight: 1.0
  inter_weight: 1.0
  penalty_per_missing: 0.5

mlflow:
  experiment_name: phase1_assignment
  tracking_uri: sqlite:///mlflow.db
```

### assign_base.yaml 額外欄位（benchmark 用）

```yaml
# assign_base.yaml 供 benchmark() 程式呼叫，不直接用 CLI 執行
temperature: -1          # SA 參數（-1 = auto-calibrate）
cooling_rate: 0.99
moves_pct: 0.01
mutation_rate: 0.15
crossover_rate: 0.8
constraints:
  enabled: true
  min_cluster_size: 50
  infeasible_score: 1000000.0
  violation_weight: 1.0
knn:
  k: 5
  amplifier: 2.0
  reheat_rate: 1.05
  stagnation_eps: 0.001
# sa.init / sa.perturbation / sa.local_search 由 benchmark() 動態設定
```

---

## Phase 2 流程

```
載入資料 + Phase 1 輸出的群標籤
  → features/（各模組獨立，可任意組合）
  → pipeline.py：feature selection + extraction
  → optimize.py：超參數最佳化（optuna / random / grid / none）
  → train.py：訓練分類器、評估、記錄至 MLflow
```

### 可用特徵模組

| 模組 | 處理欄位 | 輸出方式 |
|------|----------|----------|
| `numerical.py` | `price`, `discount`, `dlc_count`, `required_age`, `achievements` | 直接使用，視需要 scaling |
| `categorical.py` | `genres`, `categories`, `tags`, `developers`, `publishers`, `supported_languages`, `full_audio_languages` | multi-hot encoding 或 count aggregation |
| `temporal.py` | `release_date` | 衍生 year、month、days_since_release |
| `text.py` | `short_description`, `about_the_game`, `detailed_description`, `reviews`, `notes` | TF-IDF 或 sentence embedding |

### 不可使用的欄位（Phase 1 分群維度）

`positive`, `negative`, `pct_pos_total`, `pct_pos_recent`,
`num_reviews_total`, `num_reviews_recent`, `metacritic_score`,
`user_score`, `score_rank`, `recommendations`,
`average_playtime_forever`, `average_playtime_2weeks`,
`median_playtime_forever`, `median_playtime_2weeks`,
`peak_ccu`, `estimated_owners`

---

## MLflow 追蹤項目

### Phase 1

| 類型 | 內容 |
|------|------|
| params | algorithm、iterations、dimension 欄位、sample_filter mode、n_samples_raw / n_samples_used |
| metrics | objective_score、silhouette、davies_bouldin、n_active_clusters、dominant_cluster_pct、n_colors、n_cluster_\<id\> |
| artifacts | 群標籤 CSV |

### Phase 2

| 類型 | 內容 |
|------|------|
| params | 啟用的特徵模組、selection 方法、extraction 方法、分類器種類 |
| metrics | accuracy、macro F1、per-class F1、confusion matrix |
| artifacts | 特徵重要性圖、confusion matrix 圖、訓練好的模型 |

---

## 執行方式

```bash
# Phase 1：單一實驗
python run_phase1.py --config configs/assign_ga.yaml

# Phase 2：訓練預測模型
python run_phase2.py --config configs/phase2.yaml --labels outputs/labels_assign_ga.csv

# Phase 2：批次執行
python run_all_phase2.py --labels outputs/labels_assign_ga.csv

# 查看實驗結果
mlflow ui --backend-store-uri sqlite:///mlflow.db
```
