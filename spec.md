# 方法 Spec

## Phase 1 — N 維分群（Assignment GA）

### 分群維度

將每款遊戲在 **N 個維度**上進行分群，群組數量為 `n_colors`。目前實驗使用的維度如下：

| 維度 | 欄位 |
|------|------|
| 評分 (Rating) | `pct_pos_total` |
| 在線時長 (Playtime) | `average_playtime_forever` |
| 熱度 (Hotness) | `peak_ccu` |

每個維度對應一個欄位，支援 `log_scale`、`iqr_clip` 等前處理。

### 樣本篩選

執行前先以 `sample_filter` 移除指定欄位全為 0 的遊戲（`all_positive` 模式），避免冷資料污染分群結果。

### 表示法與搜尋方法

Phase 1 使用 `assignment` 表示法：

| `representation` | 解的形式 | 搜尋演算法 |
|-----------------|----------|------------|
| `assignment` | 每筆資料的 cluster id 向量（長度 = 樣本數） | GA（含 BAM 擾動、KNN 局部搜尋）、KMeans、KMeans++ |

GA 的族群多樣初始化：第一個個體使用 `initial_solution.method` 指定，其餘依序循環 random / kmeans / kmeans++ / sorted_partition。

**GA 策略軸**（cfg["sa"] 子欄位）：

| 軸 | 選項 | 說明 |
|----|------|------|
| `init` | `kmeans` / `kmeans++` / `random` / `sorted_partition` | 初始解生成方式 |
| `perturbation` | `baseline` / `improve` | `improve` = BAM：突變機率正比於 KNN 不一致度 |
| `local_search` | `false` / `true` | `true` = KNN 多數決局部搜尋，步數隨迭代進度增長 |

### 群組編碼（threshold 表示法）

設維度數為 $K$，將 $K$ 個 H/L 二元值視為 $K$-bit 二進位數字，從高位到低位依維度順序排列：

$$
\text{cluster\_id} = \sum_{i=0}^{K-1} b_i \cdot 2^{K-1-i}
$$

其中 $b_i = 1$ 表示第 $i$ 個維度高於門檻（H），$b_i = 0$ 表示低於門檻（L）。

**3D 示例（K=3，Rating → Playtime → Hotness）**：

| cluster_id | 標籤 | Rating | Playtime | Hotness |
|:----------:|------|:------:|:--------:|:-------:|
| 7 | HHH | H | H | H |
| 6 | HHL | H | H | L |
| 5 | HLH | H | L | H |
| 4 | HLL | H | L | L |
| 3 | LHH | L | H | H |
| 2 | LHL | L | H | L |
| 1 | LLH | L | L | H |
| 0 | LLL | L | L | L |

**2D 示例（K=2，Rating → Engagement）**：

| cluster_id | 標籤 |
|:----------:|------|
| 3 | HH |
| 2 | HL |
| 1 | LH |
| 0 | LL |

`cluster_id` 為跨實驗比較的固定數值標籤，標籤字串（HHH 等）供人工解讀使用。`assignment` / `coloring` 表示法的 cluster id 由 `n_colors` 決定，同樣以相同公式轉換為 H/L 字串。

### 可行性約束

`constraints.min_cluster_size` 可在 config 中啟用：

- 若任一 cluster 的樣本數少於 `min_cluster_size`，視為不可行解
- 不可行解直接回傳 `infeasible_score + violation_weight × 總缺口`，不進入正式目標函數計算

### 評估方法

Phase 1 為無監督分群，採用以下三個指標評估分群品質（適用於所有表示法）：

#### 主要指標

| 指標 | 方向 | 說明 |
|------|------|------|
| **Silhouette Score** | 越高越好（-1 ~ 1） | 衡量每個點與自身群的相似度相對於其他群的差異，綜合反映群內聚合與群間分離 |
| **Davies-Bouldin Index (DBI)** | 越低越好 | 各群「群內平均散度 / 群心間距」的平均值，基於群心計算，對 outlier 穩健 |

#### 輔助指標

| 指標 | 說明 |
|------|------|
| **Cluster Distribution** | 各群樣本數，檢查是否有空群或極度不平衡（某群 > 50% 視為警示） |

#### 不採用 Dunn Index 的原因

Dunn Index 取最小群間距離與最大群內直徑之比。在門檻切割的場景下，相鄰群共享邊界，最小群間距離趨近於 0，導致 Dunn 值永遠偏小且缺乏區分力；同時其 O(n²) 計算成本在 89,618 筆資料下也較高，故不採用。

---

## Phase 2 — 分群預測

以遊戲的描述性特徵預測其所屬群組（多分類問題，群數由 Phase 1 設定決定），流程包含特徵選擇（Feature Selection）與特徵提取（Feature Extraction）。

### 不可使用的特徵（Phase 1 分群維度，禁止放入）

以下欄位直接或間接構成分群依據，放入 Phase 2 會造成 data leakage，**一律排除**：

| 欄位 | 原因 |
|------|------|
| `positive` | 評分維度 |
| `negative` | 評分維度 |
| `pct_pos_total` | 評分維度 |
| `pct_pos_recent` | 評分維度 |
| `num_reviews_total` | 評分維度 |
| `num_reviews_recent` | 評分維度 |
| `metacritic_score` | 評分維度 |
| `user_score` | 評分維度 |
| `score_rank` | 評分維度 |
| `recommendations` | 評分維度 |
| `average_playtime_forever` | 在線時長維度 |
| `average_playtime_2weeks` | 在線時長維度 |
| `median_playtime_forever` | 在線時長維度 |
| `median_playtime_2weeks` | 在線時長維度 |
| `peak_ccu` | 熱度維度 |
| `estimated_owners` | 熱度維度 |

### 可用特徵清單

#### 數值型

| 欄位 | 說明 |
|------|------|
| `price` | 售價（美元） |
| `discount` | 折扣百分比 |
| `dlc_count` | DLC 數量 |
| `required_age` | 年齡限制 |
| `achievements` | 成就數量 |

#### 平台型（布林）

| 欄位 | 說明 |
|------|------|
| `windows` | 是否支援 Windows |
| `mac` | 是否支援 macOS |
| `linux` | 是否支援 Linux |

#### 類別型（需解析為結構化特徵）

| 欄位 | 說明 |
|------|------|
| `genres` | Steam 官方類型（list） |
| `categories` | Steam 功能分類（list） |
| `tags` | 玩家標籤與投票數（dict） |
| `developers` | 開發商（list） |
| `publishers` | 發行商（list） |
| `supported_languages` | 支援文字語言（list） |
| `full_audio_languages` | 支援語音語言（list） |

#### 時間型（需特徵工程）

| 欄位 | 說明 |
|------|------|
| `release_date` | 發行日期，可衍生發行年份、月份、上市天數等 |

#### 文字型（需 NLP 處理）

| 欄位 | 說明 |
|------|------|
| `short_description` | 遊戲簡介 |
| `about_the_game` | 關於此遊戲 |
| `detailed_description` | 完整介紹 |
| `reviews` | 媒體評論（含大量 NaN） |
| `notes` | 內容警示備註（含大量 NaN） |
