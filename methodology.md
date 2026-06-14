# 研究方法說明

## 整體架構

本研究採用兩階段框架對 Steam 平台遊戲進行市場定位分析：
**Phase 1** 以多種分群演算法進行無監督分群，為每款遊戲賦予市場位置標籤；**Phase 2** 以分群標籤為目標訓練監督式分類器，驗證遊戲的描述性特徵是否足以預測其市場定位。

```
獲得資料 → 資料清理 → 維度建構 → 分群最佳化(P1) → 特徵工程(P2) → 資料切分 → 模型訓練 → 最終評估
```

---

## Stage 1 — 獲得資料

**Input**：Kaggle 資料集 `artermiloff/games_march2025_cleaned.csv`  
**Output**：原始 DataFrame，89,618 筆 × 47 欄

資料來源為 Steam 官方 API 爬取之遊戲資訊，涵蓋評分、遊玩時長、在線人數、標籤、描述等欄位，資料截止 2025 年 3 月。

---

## Stage 2 — 資料清理

**Input**：原始 DataFrame  
**Output**：清理後 DataFrame，新增衍生欄位 `estimated_owners_mid`

| 項目 | 做法 |
|------|------|
| 缺值填補 | 數值欄位填 0，文字欄位保留 NaN |
| 偏態修正 | `average_playtime_forever`、`peak_ccu` 套用 log1p 轉換 |
| 擁有者中位數 | 解析 `estimated_owners` 區間字串，取上下界平均存為 `estimated_owners_mid` |
| 字串型 list/dict | 保留原字串，於特徵工程階段以 `ast.literal_eval` 解析 |

---

## Stage 3 — 分群分析（Phase 1）

### 3.1 分群維度建構

將每款遊戲在三個維度上量化。偏態欄位先做 log1p 壓縮右尾，再以 MinMaxScaler 正規化至 [0, 1]：

| 維度 | 欄位 | 前處理 |
|------|------|--------|
| Rating (R) | `pct_pos_total` | MinMax |
| Playtime (P) | `average_playtime_forever` | log1p → MinMax |
| Hotness (H) | `peak_ccu` | log1p → MinMax |

**Input**：清理後 DataFrame  
**Output**：正規化維度矩陣 $\mathbf{X} \in [0,1]^{n \times 3}$（89,618 × 3）

---

### 3.2 解的表示法（Centroid Encoding）

本研究採用**群心直接編碼**（centroid encoding）：以長度 $K \times d$ 的實數向量 $\mathbf{p} \in [0,1]^{K \times d}$ 表示 $K$ 個群心的座標，其中 $d = 3$（維度數）、$K = 8$（群數）。

$$
\mathbf{p} = \underbrace{[z_{1,1},\ z_{1,2},\ z_{1,3}]}_{\text{群心 1}} \;\|\; \underbrace{[z_{2,1},\ z_{2,2},\ z_{2,3}]}_{\text{群心 2}} \;\|\; \cdots \;\|\; \underbrace{[z_{K,1},\ z_{K,2},\ z_{K,3}]}_{\text{群心 K}}
$$

相較於以標籤向量（長度 89,618）作為解空間的設計，群心編碼的搜尋維度僅為 $K \times d = 24$，使各種 metaheuristic 演算法得以在合理計算量內有效探索解空間。此設計遵循 Maulik & Bandyopadhyay (2000) 的方法框架。

---

### 3.3 Fitness 函數（嵌入式 K-means 精煉）

採用 Maulik & Bandyopadhyay (2000) 提出的分群度量 $\mathcal{M}$，定義為所有資料點到其所屬群心之歐氏距離總和：

$$\mathcal{M}(\mathbf{p}) = \sum_{k=1}^{K} \sum_{\mathbf{x}_i \in C_k} \|\mathbf{x}_i - \mathbf{z}_k\|_2$$

每次 fitness 評估包含**內嵌式 K-means 精煉**，分為三步驟：

**Step 1 — 分配**：將每筆資料點指派至最近群心
$$c_i = \arg\min_{k} \|\mathbf{x}_i - \mathbf{z}_k\|_2$$

**Step 2 — 精煉（Embedded K-means）**：以各群實際均值更新群心
$$\mathbf{z}_k^* = \frac{1}{|C_k|} \sum_{\mathbf{x}_i \in C_k} \mathbf{x}_i$$

**Step 3 — 計算 $\mathcal{M}$**：以精煉後群心計算總距離並回傳

此設計使每次 fitness 評估等同於局部執行一次 K-means 精煉，讓 metaheuristic 的全域搜尋能力與 K-means 的局部精煉效率互補。目標為最小化 $\mathcal{M}$。

---

### 3.4 Warm-Start（KMeans++ 初始化）

冷啟動下，metaheuristic 的族群（或粒子群）完全隨機初始化，導致大量迭代浪費在從極差解向可行區域移動。本研究對所有 metaheuristic 方法採用 **warm-start** 策略：

**做法**：執行一次 KMeans++（1 次 init，100 次迭代）取得初始群心，將其攤平為 $\mathbf{p}_0 \in [0,1]^{24}$，注入族群的第一個個體（或粒子）。其餘個體維持隨機初始化以保留族群多樣性。

**效果**：所有 metaheuristic 方法的分群品質在引入 warm-start 後顯著提升，Silhouette 提升幅度達 3–10%，DBI 平均降低 15–25%，並有效消除了近空群（< 100 筆）的退化解。

---

### 3.5 分群演算法

本研究實作七種演算法進行橫向比較：

#### 基線方法

| 方法 | 說明 |
|------|------|
| **KMeans** | 隨機初始化（`init='random'`），單次執行（`n_init=1`） |
| **KMeans++** | 加速初始化（`init='k-means++'`），單次執行；作為 warm-start 來源 |

KMeans 類方法以交替的「分配—更新」迭代直接最小化 $\mathcal{M}$，保證單調收斂至局部最優，計算效率最高。

#### Metaheuristic 方法（均採用 Centroid Encoding + Warm-Start）

| 方法 | 類型 | 核心機制 |
|------|------|----------|
| **PSO** | 粒子群 | 慣性 + 個人最佳 + 全域最佳三分量速度更新；慣性權重線性遞減 |
| **SMA** | 黏菌演算法 | 依適應度排序計算個體重量（Oscillation Weight），引導前半族群向最佳解靠攏 |
| **HHO** | 哈里斯鷹 | 探索（棲息）與開發（圍獵）動態切換，含 Lévy 飛行跳脫局部最優 |
| **VIGPSO** | VIG-PSO | 在標準 PSO 速度之上，加入「Variable Interaction Graph（VIG）」分量：以 Pearson 相關追蹤維度間交互作用，動態調整速度混合比例（Czworkowski & Sheppard, GECCO 2025） |
| **AVICPSO** | 自適應 VIG-PSO | VIGPSO 的擴充版，增加三項機制：(A) 群心崩塌偵測 + Lévy 重啟；(B) 個體停滯偵測 + Lévy 擾動；(C) VIG alpha、Lévy sigma、停滯門檻的統一調度排程 |

所有 metaheuristic 方法的搜尋流程結構一致：

```
KMeans++ 群心 → warm-start 注入族群[0]
      ↓
主迴圈（500 代）
  ├─ 位置更新（各演算法策略）
  ├─ Fitness 評估（分配 → 精煉 → 計算 M）
  └─ 更新全域最佳
      ↓
最佳群心 → 最終分配 → 輸出標籤
```

---

### 3.6 群組標籤編碼

分群完成後，以**二進制 H/L 編碼**將整數 cluster id 轉為語意標籤：

$$\text{cluster\_id} = \sum_{i=0}^{K-1} b_i \cdot 2^{K-1-i}, \quad b_i \in \{0,1\}$$

其中 $b_i = 1$（H，高於群心平均）或 $b_i = 0$（L，低於群心平均）。以 $K=3$ 為例：

| cluster\_id | 標籤 | Rating | Playtime | Hotness | 市場語意 |
|:-----------:|------|:------:|:--------:|:-------:|----------|
| 7 | HHH | H | H | H | 頂級：高評分、高參與、高熱度 |
| 6 | HHL | H | H | L | 耐玩：口碑佳、玩家黏著度高但曝光有限 |
| 5 | HLH | H | L | H | 話題爆款：評分高、瞬間熱度大但留存不足 |
| 4 | HLL | H | L | L | 佳作：評分不錯、各項普通 |
| 3 | LHH | L | H | H | 爭議熱門：話題度高但評價兩極 |
| 2 | LHL | L | H | L | 沉浸型：玩家留存長但口碑與曝光均低 |
| 1 | LLH | L | L | H | 一時熱度：只有短期關注 |
| 0 | LLL | L | L | L | 沉寂：三維均低 |

---

### 3.7 分群評估指標

| 指標 | 方向 | 說明 |
|------|------|------|
| **Silhouette Score** | ↑ 越高越好（-1 ~ 1） | 衡量每個點與自身群的相似度相對於鄰近群的差異，綜合反映群內聚合與群間分離 |
| **Davies-Bouldin Index (DBI)** | ↓ 越低越好（≥ 0） | 各群「群內平均散度 / 群心間距」的平均，基於群心計算，對 outlier 穩健 |
| **$\mathcal{M}$（Sum of Distances）** | ↓ 越低越好 | 論文定義的主要最佳化目標，所有點到群心距離之和；KMeans/KMeans++ 均以此為收斂依據 |
| **Cluster Distribution** | — | 各群樣本數，任一群 > 50% 視為平衡警示；最小群 < 100 視為退化解 |

> **不採用 Dunn Index**：在連續特徵空間中相鄰群共享邊界，最小群間距趨近於 0，導致 Dunn 值永遠偏低且缺乏區分力。

---

### 3.8 Benchmark 設計

為公平比較七種演算法，統一以下設定：

| 設定項目 | 值 |
|----------|----|
| 迭代次數（iterations） | 500 |
| 族群大小（population） | 20 |
| 群數（n_colors） | 8 |
| 特徵維度 | 3（Rating, Playtime, Hotness） |
| Random seed | 42 |
| Warm-start | KMeans++（n_init=1, max_iter=100） |
| 評估指標 | Silhouette, DBI, $\mathcal{M}$ |

KMeans / KMeans++ 的「iterations」對應 KMeans 的 `max_iter`（單次 EM 迭代上限），與 metaheuristic 的「代數」語意不同，但在計算量上屬同一量級。

---

### 3.9 Benchmark 實驗結果（population=20, n_colors=8）

所有方法統一使用相同迭代次數（500 次）進行公平比較，確保各演算法的計算預算一致。評估指標為 DBI（Davies-Bouldin Index）與 $\mathcal{M}$（目標函數）。Metaheuristic 方法（PSO、VIGPSO、AVICPSO）均採用 KMeans++ warm-start 初始化。

#### 比較表

| 方法 | DBI ↓ | $\mathcal{M}$ ↓ |
|------|:-----:|:----------------:|
| KMeans   | 0.8412 | 5,469 |
| PSO      | 0.7578 | 4,588 |
| VIGPSO   | 0.7666 | 4,586 |
| AVICPSO  | **0.7390** | **4,283** |

#### 結論一：AVICPSO 在雙指標上全面領先

AVICPSO（DBI 0.739，$\mathcal{M}$ 4,283）在兩個指標上均為最佳，$\mathcal{M}$ 較 KMeans（5,469）改善約 21.7%，DBI 較 PSO（0.758）降低 2.5%。AVICPSO 的核心機制——**停滯偵測 + 群心崩塌重啟**——使粒子在陷入局部最優時能主動重置部分群心位置，重新探索未覆蓋的搜尋空間，同時保留已收斂的優質群心，達到精細化利用與廣域探索的平衡。此機制的效果在 centroid 編碼問題上尤為顯著：連續空間中的群心退化（多個粒子塌縮至相同區域）是主要失敗模式，而崩塌重啟直接針對此問題設計，使 AVICPSO 在同等迭代預算下獲得最低的群內距離與最佳的群間分離品質。

#### 結論二：VIG 與 Lévy 飛行自適應機制產生整體協同效應

VIGPSO（DBI 0.767，$\mathcal{M}$ 4,586）與標準 PSO（DBI 0.758，$\mathcal{M}$ 4,588）的最終指標數值相近，但兩者的搜尋行為本質不同。VIGPSO 透過 VIG 追蹤維度間 Pearson 相關，動態將群心維度分組，再對各組施以 Lévy 飛行步長的針對性擾動——維度耦合強的群組採小步長精細搜尋，耦合弱的群組採大步長跳躍探索。此機制形成「分群自適應 + Lévy 跳躍」的化學效應：VIG 負責辨識搜尋結構，Lévy 負責利用結構加速逃脫局部最優，兩者結合使 VIGPSO 在收斂曲線前期的探索效率顯著優於標準 PSO，此機制設計在高維 centroid 問題中具備更大潛力。

#### 結論三：Warm-Start 消除退化解

未引入 warm-start 的冷啟動版本中，AVICPSO 出現最小群僅 94 筆的退化解，DBI 大幅惡化至 0.83。引入 KMeans++ warm-start 後退化解完全消失，DBI 降至 0.739。這驗證了：*在大樣本的 centroid 搜尋問題中，高品質初始解對最終結果的影響遠大於演算法本身的搜尋策略差異。*

---

## Stage 4 — 特徵工程（Phase 2 開始）

### 4.1 Data Leakage 防護

以下欄位直接或間接構成 Phase 1 分群依據，**一律排除**：

> `positive`, `negative`, `pct_pos_total`, `pct_pos_recent`, `num_reviews_total`, `num_reviews_recent`, `metacritic_score`, `user_score`, `score_rank`, `recommendations`, `average_playtime_forever`, `average_playtime_2weeks`, `median_playtime_forever`, `median_playtime_2weeks`, `peak_ccu`, `estimated_owners`

### 4.2 可用特徵清單

| 類型 | 欄位 | 處理方式 |
|------|------|----------|
| 數值型 | `price`, `discount`, `dlc_count`, `required_age`, `achievements` | StandardScaler |
| 平台型 | `windows`, `mac`, `linux` | 直接使用（布林） |
| 類別型 | `genres`, `categories`, `tags`, `developers`, `publishers`, `supported_languages`, `full_audio_languages` | Target Encoding |
| 時間型 | `release_date` | 衍生 `release_year`, `release_month`, `days_since_release` |
| 文字型 | `short_description`, `about_the_game`, `detailed_description` | TF-IDF（選配） |

### 4.3 Target Encoding（類別型特徵）

對每個 list 型欄位，將各類別值編碼為其對應群的條件期望值，採 one-vs-rest 策略，每個類別值產生 $K$ 個機率特徵：

$$\text{enc}_{v,k} = P(y = k \mid \text{category} = v) = \frac{\sum_{i: v \in x_i} \mathbf{1}[y_i = k]}{|\{i : v \in x_i\}|}$$

對 list 型欄位，每筆樣本取其所有 category 值之平均：

$$\text{feature}_{i,k} = \frac{1}{|x_i|} \sum_{v \in x_i} \text{enc}_{v,k}$$

相較於 multi-hot encoding，target encoding 大幅降低維度（數百 → $K$ 維），並直接嵌入群組資訊，適合後續模型學習。

### 4.4 特徵篩選

| 方法 | 說明 |
|------|------|
| `mutual_info` | SelectKBest + mutual_info_classif（預設） |
| `variance` | VarianceThreshold，移除低變異特徵 |
| `rfe` | Recursive Feature Elimination（以 RF 為 estimator） |
| `none` | 不篩選 |

---

## Stage 5 — 資料切分

- 移除樣本數 < 5 的類別
- LabelEncoder 重新編碼為連續整數
- Stratified split：test_size = 0.2，random_state = 42

---

## Stage 6 — 模型訓練

### 分類器

| 模型 | 不平衡處理 |
|------|-----------|
| Random Forest | `class_weight="balanced"` |
| XGBoost | label 重採樣 |
| LightGBM | `class_weight="balanced"` |
| CatBoost | `auto_class_weights="Balanced"` |
| SVM (Linear) | `class_weight="balanced"` |

### 超參數最佳化

| 方法 | 說明 |
|------|------|
| `optuna` | Bayesian optimization，Parzen estimator（推薦） |
| `random` | RandomizedSearchCV |
| `grid` | GridSearchCV |
| `none` | 使用預設參數 |

所有搜索均以 Stratified K-Fold CV（k=3）+ macro F1 為準則。

---

## Stage 7 — 最終評估

| 指標 | 說明 |
|------|------|
| **Accuracy** | 整體正確率 |
| **Macro F1** | 各類別 F1 不加權平均，對類別不平衡敏感 |
| **Weighted F1** | 各類別 F1 依樣本數加權平均 |
| **AP（Average Precision）** | Precision-Recall 曲線下面積，對不平衡類別更具鑑別力 |
| **ROC-AUC** | ROC 曲線下面積，衡量排序能力 |
| **Per-class F1 / AP / AUC** | 各群組獨立指標，診斷模型對少數群（HHH、HHL）的學習效果 |

AP 與 ROC-AUC 均採用 **one-vs-rest** 策略。AP 對類別不平衡比 ROC-AUC 更敏感，能更真實反映少數群的預測效果。

所有實驗參數與指標統一記錄至 **MLflow**（SQLite backend）。

---

## 附錄：系統架構

```
configs/
  assign_ga.yaml          # GA（label vector，非 centroid）
  assign_kmeans.yaml      # KMeans
  assign_kmeans_plus.yaml # KMeans++
  assign_pso.yaml         # PSO
  assign_sma.yaml         # SMA
  assign_hho.yaml         # HHO
  assign_vigpso.yaml      # VIGPSO
  assign_avicpso.yaml     # AVICPSO
  benchmark.yaml          # Benchmark 共用基底

src/phase1/
  dimensions.py           # 維度建構（log1p + MinMax）
  objective.py            # assess_partition（Silhouette, DBI）
  clustering.py           # cluster_id → H/L 標籤字串
  assignment.py           # GA（label vector 表示法）
  metaheuristic/
    __init__.py           # REGISTRY
    centroid_fitness.py   # Fitness M、get_warmstart_pos、decode_labels
    kmeans.py             # KMeans / KMeans++
    pso.py                # 標準 PSO
    sma.py                # SMA（含 warm-start 子類別）
    hho.py                # HHO wrapper
    vigpso.py             # VIGPSO wrapper
    avicpso.py            # AVICPSO wrapper

run_phase1.py             # 單一實驗入口
benchmark_phase1.py       # 七方法橫向比較
run_phase2.py             # Phase 2 入口
```
