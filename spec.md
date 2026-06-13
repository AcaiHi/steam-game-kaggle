# 方法 Spec

## Phase 1 — 三維門檻分群

### 分群維度

將每款遊戲在三個維度上各切一個門檻，高於門檻標記為 H，低於標記為 L，組合出 2³ = **8 個群組**。

| 維度 | 使用欄位 |
|------|----------|
| 評分 (Rating) | `positive`, `negative`, `pct_pos_total`, `pct_pos_recent`, `num_reviews_total`, `num_reviews_recent`, `metacritic_score`, `user_score`, `score_rank`, `recommendations` |
| 在線時長 (Playtime) | `average_playtime_forever`, `average_playtime_2weeks`, `median_playtime_forever`, `median_playtime_2weeks` |
| 熱度 (Hotness) | `peak_ccu`, `estimated_owners` |

### 門檻尋找方法

採用 **Metaheuristic**（如 GA、PSO、SA）對每個維度的切分門檻值進行最佳化，目標函數設計使群內聚合度最大、群間差異最大。

### 群組編碼

位元順序固定為 **Rating → Playtime → Hotness**，數值編碼公式：

```
cluster_id = R × 4 + P × 2 + H × 1
```

其中 R / P / H 各為 1（高於門檻）或 0（低於門檻）。

**公式來源（Binary Encoding）**：設維度數 $K=3$，將 $K$ 個二元分類結果視為一個 $K$-bit 二進位數字。第 $i$ 個維度（從高位到低位）的權重為 $2^{K-1-i}$，即 R 佔最高位（$2^{K-1}=4$）、P 佔中間位（$2^{K-2}=2$）、H 佔最低位（$2^{K-3}=1$）。$K$ 個 0/1 值唯一對應 $0$ 到 $2^K-1$ 共 $2^K=8$ 個整數（即群組總數），且標籤不隨門檻調整而改變，確保跨實驗可比性。

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

`cluster_id` 為跨實驗比較的固定數值標籤，標籤字串（HHH 等）供人工解讀使用。

### 評估方法

Phase 1 為無監督分群，採用以下三個指標評估門檻品質：

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

以遊戲的描述性特徵預測其所屬群組（8 類多分類問題），流程包含特徵選擇（Feature Selection）與特徵提取（Feature Extraction）。

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
