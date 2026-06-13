# 研究方法說明

## 整體架構

本研究採用兩階段框架對 Steam 遊戲進行市場定位分析：
**Phase 1** 以 Metaheuristic 門檻最佳化進行無監督分群，**Phase 2** 以分群標籤為目標訓練多個監督式分類器，驗證遊戲的描述性特徵是否足以預測其市場定位。

```
獲得資料 → 資料清理 → 分群分析(P1) → 特徵選擇(P2) → 資料切分 → 模型訓練 → 最終評估
```

---

## Stage 1 — 獲得資料

**Input**
- Kaggle 資料集：`artermiloff/games_march2025_cleaned.csv`

**Output**
- 原始 DataFrame：89,618 筆 × 47 欄

**說明**
資料來源為 Steam 官方 API 爬取之遊戲資訊，涵蓋評分、遊玩時長、在線人數、標籤、描述等欄位，資料截止時間為 2025 年 3 月。

---

## Stage 2 — 資料清理

**Input**
- 原始 DataFrame（89,618 × 47）

**Output**
- 清理後 DataFrame，新增衍生欄位 `estimated_owners_mid`

**處理步驟**

| 項目 | 做法 |
|------|------|
| 缺值填補 | 數值欄位填 0，文字欄位保留 NaN |
| 偏態修正 | `average_playtime_forever`、`peak_ccu` 套用 log1p 轉換 |
| 擁有者中位數 | 解析 `estimated_owners` 區間字串，取上下界平均值存為 `estimated_owners_mid` |
| 字串型 list/dict | 保留原字串，於特徵工程階段以 `ast.literal_eval` 解析 |

---

## Stage 3 — 分群分析（Phase 1）

### 3.1 維度定義

將每款遊戲在三個維度上量化，偏態維度先做 log1p 再 MinMax 正規化至 [0, 1]：

| 維度 | 使用欄位 | 前處理 |
|------|----------|--------|
| Rating (R) | `pct_pos_total` | MinMax |
| Playtime (P) | `average_playtime_forever` | log1p → MinMax |
| Hotness (H) | `peak_ccu` | log1p → MinMax |

**Input**：清理後 DataFrame  
**Output**：正規化維度矩陣（89,618 × 3），值域 [0, 1]

---

### 3.2 門檻最佳化（Metaheuristic）

對三個維度各搜尋一個最佳切分門檻值 $\theta = [\theta_R, \theta_P, \theta_H] \in [0,1]^3$，使目標函數最小化：

$$f(\theta) = -\left( w_{\text{inter}} \cdot d_{\text{inter}} - w_{\text{intra}} \cdot v_{\text{intra}} \right) + \lambda \cdot n_{\text{missing}}$$

- $d_{\text{inter}}$：群心間平均距離（越大越好）
- $v_{\text{intra}}$：群內平均變異（越小越好）
- $n_{\text{missing}}$：未激活群數，值域為 $[0,\ 2^K]$；$\lambda$ 為空群懲罰係數

**自適應擾動機制（APS）**

為避免演算法在探索初期陷入空群退化解，設計與任何演算法解耦的擾動函式 $\text{perturb}(x, t, T)$，於每次位置更新後套用：

$$\alpha(t) = 1 - \frac{t}{T} \quad \text{(探索噪聲，前期主導)}$$
$$\beta_{\text{eff}}(t) = 4 \cdot \frac{t}{T}\left(1-\frac{t}{T}\right) \cdot \frac{n_{\text{missing}}}{2^K} \quad \text{(群激活壓力，中期主導)}$$
$$\gamma(t) = \frac{t}{T} \quad \text{(鄰域細搜，後期主導)}$$

其中 $\dfrac{n_{\text{missing}}}{2^K}$ 為空群比例（$0$ 到 $1$），以群組總數 $2^K$ 正規化，使壓力強度不隨維度數 $K$ 的變動而失衡。當 $n_{\text{missing}} = 0$，$\beta_{\text{eff}}$ 自動歸零，機制不干擾後期品質收斂。

**支援演算法**：GA、PSO、SA、SMA、HHO、GWO（config 一行切換）

**Input**：正規化維度矩陣  
**Output**：最佳門檻值 $[\theta_R^*, \theta_P^*, \theta_H^*]$

---

### 3.3 群組編碼與標籤輸出

依固定編碼公式將三個維度的 H/L 結果轉為整數標籤：

$$\text{cluster\_id} = R \times 4 + P \times 2 + H \times 1, \quad R,P,H \in \{0,1\}$$

**公式來源（Binary Encoding）**

設維度數 $K = 3$，本質上是將 $K$ 個二元分類結果視為一個 $K$-bit 二進位數字。第 $i$ 個維度（$i = 0, 1, \ldots, K-1$，從高位到低位）的權重為 $2^{K-1-i}$，因此：

| 維度 | 位元位置 $i$ | 權重 $2^{K-1-i}$ |
|------|:------------:|:----------------:|
| Rating (R)   | $i=0$（最高位） | $2^{K-1} = 2^2 = 4$ |
| Playtime (P) | $i=1$（中間位） | $2^{K-2} = 2^1 = 2$ |
| Hotness (H)  | $i=2$（最低位） | $2^{K-3} = 2^0 = 1$ |

$K$ 個 0/1 值串成一個整數，值域恰好覆蓋 $0$ 到 $2^K - 1$ 共 $2^K = 8$ 個唯一值（對應 8 個群組），且順序固定不會因閾值調整而改變，確保跨實驗的標籤可比性。若日後擴充為 $K'$ 個維度，只需依相同規則更新權重，即可支援 $2^{K'}$ 個群組。

| cluster\_id | 標籤 | 語意 |
|:-----------:|------|------|
| 7 | HHH | 高評分、高時長、高熱度（頂級） |
| 6 | HHL | 高評分、高時長、低熱度（耐玩） |
| 5 | HLH | 高評分、低時長、高熱度（話題爆款） |
| 4 | HLL | 高評分、低時長、低熱度（多數普通遊戲） |
| 3 | LHH | 低評分、高時長、高熱度 |
| 2 | LHL | 低評分、高時長、低熱度 |
| 1 | LLH | 低評分、低時長、高熱度 |
| 0 | LLL | 三低（表現最差） |

**Input**：最佳門檻值、正規化維度矩陣  
**Output**：群標籤 CSV（`outputs/phase1_labels.csv`），欄位：`appid`, `cluster_id`, `cluster`

**Phase 1 評估指標**

| 指標 | 方向 | 說明 |
|------|------|------|
| Silhouette Score | ↑ 越高越好 | 群內聚合 vs 群間分離的綜合指標 |
| Davies-Bouldin Index | ↓ 越低越好 | 基於群心計算，對 outlier 穩健 |
| Cluster Distribution | — | 各群樣本數，任一群 > 50% 觸發平衡警示 |

> 不採用 Dunn Index：門檻分群下相鄰群共享邊界，最小群間距趨近 0，Dunn 失去區分力。

---

## Stage 4 — 特徵選擇（Phase 2 開始）

### 4.1 可用特徵

Phase 1 所用欄位（`pct_pos_total`、`average_playtime_forever`、`peak_ccu` 及所有分群維度相關欄位）**一律排除**，以防止 data leakage。

| 類型 | 欄位 | 處理方式 |
|------|------|----------|
| 數值型 | `price`, `discount`, `dlc_count`, `required_age`, `achievements` | StandardScaler |
| 平台型 | `windows`, `mac`, `linux` | 直接使用（布林） |
| 類別型 | `genres`, `categories`, `tags`, `developers`, `publishers`, `supported_languages`, `full_audio_languages` | Target Encoding |
| 時間型 | `release_date` | 衍生 `release_year`, `release_month`, `days_since_release` |
| 文字型 | `short_description`, `about_the_game`, `detailed_description` | TF-IDF（選配） |

### 4.2 Target Encoding（類別型特徵）

對每個 list 型欄位（如 `genres`），將各類別值編碼為其對應群的條件期望值。對多類別目標採用 **one-vs-rest** 策略，每個類別值產生 $K$ 個機率特徵（$K$ = 有效群數）：

$$\text{enc}_{v,k} = P(y = k \mid \text{category} = v) = \frac{\sum_{i: v \in x_i} \mathbf{1}[y_i = k]}{|\{i : v \in x_i\}|}$$

對 list 型欄位，每筆樣本的特徵值取其所有 category 值之平均：

$$\text{feature}_{i,k} = \frac{1}{|x_i|} \sum_{v \in x_i} \text{enc}_{v,k}$$

> 相較於 multi-hot encoding，target encoding 大幅降低維度（從數百 → $K$ 維），並直接嵌入群組資訊，適合後續模型學習。

### 4.3 特徵篩選

| 方法 | 說明 | config |
|------|------|--------|
| `mutual_info` | SelectKBest + mutual_info_classif（預設） | `selection.method: mutual_info` |
| `variance` | VarianceThreshold，去除零/低變異特徵 | `selection.method: variance` |
| `rfe` | Recursive Feature Elimination（以 RF 為 estimator） | `selection.method: rfe` |
| `none` | 不做篩選 | `selection.method: none` |

**Input**：清理後 DataFrame + Phase 1 標籤  
**Output**：特徵矩陣 $X$（樣本數 × 選定特徵數）、目標向量 $y$

---

## Stage 5 — 資料切分

**Input**：特徵矩陣 $X$、目標向量 $y$

**Output**：$X_{\text{train}}, X_{\text{test}}, y_{\text{train}}, y_{\text{test}}$

**做法**
- 移除樣本數 < 5 的類別（無法有效學習）
- 以 LabelEncoder 重新編碼為連續整數（XGBoost 等需要）
- Stratified split：test_size = 0.2，random_state = 42

---

## Stage 6 — 模型訓練

**Input**：$X_{\text{train}}, y_{\text{train}}$

**Output**：訓練完成的分類器（每個模型各一）

### 分類器

| 模型 | 不平衡處理 |
|------|-----------|
| Random Forest | `class_weight="balanced"` |
| XGBoost | label 重採樣 |
| LightGBM | `class_weight="balanced"` |
| CatBoost | `auto_class_weights="Balanced"` |
| SVM (Linear) | `class_weight="balanced"` |

### 超參數最佳化（可抽換）

| 方法 | 說明 | config |
|------|------|--------|
| `optuna` | Bayesian optimization，Parzen estimator（推薦） | `param_optimize.method: optuna` |
| `random` | RandomizedSearchCV | `param_optimize.method: random` |
| `grid` | GridSearchCV | `param_optimize.method: grid` |
| `none` | 使用預設參數 | `param_optimize.method: none` |

所有超參數搜索均以 Stratified K-Fold CV（預設 k=3）+ macro F1 為準則。

---

## Stage 7 — 最終評估

**Input**：訓練完成的分類器、$X_{\text{test}}, y_{\text{test}}$

**Output**：評估指標（記錄至 MLflow）

### 評估指標

| 指標 | 說明 | 計算方式 |
|------|------|----------|
| **Accuracy** | 整體正確率 | — |
| **Macro F1** | 各類別 F1 不加權平均，對類別不平衡敏感 | `average="macro"` |
| **Weighted F1** | 各類別 F1 依樣本數加權平均 | `average="weighted"` |
| **AP（Average Precision）** | Precision-Recall 曲線下面積，對不平衡類別更具鑑別力 | one-vs-rest macro |
| **ROC-AUC** | ROC 曲線下面積，衡量排序能力 | one-vs-rest macro |
| **Per-class F1 / AP / AUC** | 各群組獨立指標，診斷模型對少數群的學習效果 | — |

> AP 與 ROC-AUC 均採用 **one-vs-rest** 策略計算多類別版本。AP 對類別不平衡比 ROC-AUC 更敏感，能更真實反映少數群（如 HHH、HHL）的預測效果。

所有實驗參數與指標統一記錄至 **MLflow**（SQLite backend），支援跨模型、跨 config 比較。
