# 專案架構 Spec

## 設計原則

- 每個階段獨立可執行，互不依賴
- 策略可抽換（不同 metaheuristic、不同分類器、不同特徵處理方式）
- 所有實驗參數走 config，結果走 MLflow
- 不過度抽象：不用複雜的 base class hierarchy，用簡單的 function / dict dispatch

---

## 目錄結構

```
steam-game-kaggle/
│
├── datasets/                        # 原始資料（已存在）
│
├── configs/
│   ├── phase1.yaml                  # Phase 1 實驗參數
│   └── phase2.yaml                  # Phase 2 實驗參數
│
├── src/
│   ├── data.py                      # 資料載入、基本清理
│   │
│   ├── phase1/
│   │   ├── dimensions.py            # 三個維度的特徵合成（評分/時長/熱度）
│   │   ├── metaheuristic/
│   │   │   ├── ga.py                # Genetic Algorithm
│   │   │   ├── pso.py               # Particle Swarm Optimization
│   │   │   └── sa.py                # Simulated Annealing
│   │   ├── objective.py             # 目標函數（群內聚合、群間距離）
│   │   └── clustering.py            # 門檻切分 → 8 群標籤輸出
│   │
│   └── phase2/
│       ├── features/
│       │   ├── numerical.py         # price, dlc_count, achievements...
│       │   ├── categorical.py       # genres, tags, categories（multi-hot / count）
│       │   ├── temporal.py          # release_date → year, month, days_since
│       │   └── text.py              # short_description 等（TF-IDF / embedding）
│       ├── pipeline.py              # 組合各 feature 模組、selection、extraction
│       └── train.py                 # 訓練與評估分類器
│
├── notebooks/
│   ├── 01_eda.ipynb                 # 探索性分析（已有 kernel 可參考）
│   ├── 02_phase1_cluster.ipynb      # Phase 1 結果視覺化
│   └── 03_phase2_predict.ipynb      # Phase 2 特徵分析與模型結果
│
├── run_phase1.py                    # Phase 1 執行入口
├── run_phase2.py                    # Phase 2 執行入口
│
├── spec.md                          # 方法 spec（已存在）
└── project_architecture.md          # 本文件
```

---

## Phase 1 流程

```
載入資料
  → dimensions.py：合成三個維度的代表性數值
  → metaheuristic/（可抽換）：最佳化三個門檻值
      目標函數 objective.py：最大化群間距離 + 最小化群內變異
  → clustering.py：依門檻切分 H/L，輸出 8 群標籤
  → MLflow 記錄：門檻值、目標函數值、各群分佈
```

### configs/phase1.yaml 結構

```yaml
algorithm: pso          # ga | pso | sa，切換策略只改這行
iterations: 200
population: 50

dimensions:
  rating:
    features: [pct_pos_total, metacritic_score, recommendations]
    aggregation: weighted_mean   # 多欄位合成方式
  playtime:
    features: [average_playtime_forever, median_playtime_forever]
    aggregation: mean
  hotness:
    features: [peak_ccu, estimated_owners]
    aggregation: mean

objective:
  intra_weight: 1.0
  inter_weight: 1.0
```

---

## Phase 2 流程

```
載入資料 + Phase 1 輸出的群標籤
  → features/（各模組獨立，可任意組合）
  → pipeline.py：feature selection + extraction
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

### configs/phase2.yaml 結構

```yaml
features:
  numerical: true
  categorical: true
  temporal: true
  text: false            # 文字特徵較重，可獨立開關

categorical:
  mode: multi_hot        # multi_hot | count | top_k

text:
  method: tfidf          # tfidf | embedding
  max_features: 500

selection:
  method: variance       # variance | mutual_info | rfe | none

extraction:
  method: pca            # pca | none
  n_components: 50

model: random_forest     # random_forest | xgboost | svm | mlp
```

---

## MLflow 追蹤項目

### Phase 1

| 類型 | 內容 |
|------|------|
| params | algorithm、iterations、dimension 欄位組合、aggregation 方式 |
| metrics | 目標函數值、各群樣本數、群間距離、群內變異 |
| artifacts | 群標籤 CSV、門檻視覺化圖 |

### Phase 2

| 類型 | 內容 |
|------|------|
| params | 啟用的特徵模組、selection 方法、extraction 方法、分類器種類 |
| metrics | accuracy、macro F1、per-class F1、confusion matrix |
| artifacts | 特徵重要性圖、confusion matrix 圖、訓練好的模型 |

---

## 執行方式

```bash
# Phase 1：找門檻、產生群標籤
python run_phase1.py --config configs/phase1.yaml

# Phase 2：訓練預測模型
python run_phase2.py --config configs/phase2.yaml --labels outputs/phase1_labels.csv

# 切換策略只需改 config，不動程式碼
```
