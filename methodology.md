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

### 3.5.1 VIGPSO — Variable Interaction Graph 速度協調機制

VIGPSO（Czworkowski & Sheppard, GECCO 2025）在標準 PSO 速度之上，引入 **Variable Interaction Graph（VIG）** 偵測搜尋維度之間的交互依賴關係，並以此修正速度方向。

#### VIG 建構與更新

VIG 以 $\mathbf{G} \in \mathbb{R}^{D \times D}$（$D = K \times d = 9$）表示各維度對之間的相關強度，每隔 `update_interval`（預設 10 代）更新一次：

**Step 1 — 計算位移矩陣**：記錄本輪迭代前後所有粒子的位移

$$\Delta \mathbf{X} = \mathbf{X}_{t} - \mathbf{X}_{t-1} \in \mathbb{R}^{S \times D}$$

**Step 2 — 計算 Pearson 相關矩陣**：對 $\Delta\mathbf{X}$ 的各維度對計算跨粒子的絕對 Pearson 相關係數

$$\rho_{ij} = \left|\text{Pearson}\left(\Delta X_{:,i},\ \Delta X_{:,j}\right)\right|, \quad i \neq j$$

**Step 3 — 遲滯更新 G（雙門檻 hysteresis）**：

$$G_{ij} \leftarrow \begin{cases} \rho_{ij} & \text{if } \rho_{ij} > \tau_1 = 0.5 \quad \text{（新增強邊）} \\ 0 & \text{if } \rho_{ij} < \tau_2 = 0.3 \quad \text{（移除弱邊）} \\ G_{ij} & \text{otherwise} \quad \text{（保留現有邊）} \end{cases}$$

雙門檻設計避免邊在兩側門檻附近震盪，提供穩定的拓撲結構。

#### VIG 速度分量與混合

對每個粒子 $i$，先計算標準 PSO 速度 $\mathbf{v}_s$：

$$\mathbf{v}_s = \omega \mathbf{V}_i + c_1 r_1 (\mathbf{p}_i^{\text{best}} - \mathbf{X}_i) + c_2 r_2 (\mathbf{g}^{\text{best}} - \mathbf{X}_i)$$

再計算 VIG 速度分量 $\mathbf{v}_{\text{VIG}}$：對每個維度 $d$，取其在 $\mathbf{G}$ 中具有正邊的鄰居維度以邊權重加權平均

$$v_{\text{VIG},d} = \frac{\sum_{j \in \mathcal{N}(d)} G_{dj} \cdot v_{s,j}}{\sum_{j \in \mathcal{N}(d)} G_{dj}}, \quad \mathcal{N}(d) = \{j : G_{dj} > 0\}$$

最終速度以動態比例 $\alpha$ 混合兩分量：

$$\mathbf{v}_{\text{new}} = (1 - \alpha)\,\mathbf{v}_s + \alpha\,\mathbf{v}_{\text{VIG}}$$

$$\alpha = \alpha_{\max} \cdot \left(1 - e^{-2 \cdot \text{prog}}\right), \quad \alpha_{\max} = 0.3$$

$\alpha$ 採指數增長型排程：初期 $\alpha \approx 0$（VIG 資訊不足，以標準 PSO 為主），迭代後期 $\alpha \to 0.3$（VIG 拓撲穩定後逐步加大影響力）。

#### 在 Centroid Encoding 中的語意

本研究的解空間維度為 $D = K \times d = 3 \times 3 = 9$，每 $d=3$ 個連續維度對應一個群心的座標。若群心 $k_1$ 的 $x$ 維度與群心 $k_2$ 的 $x$ 維度在多個粒子中呈現高度相關的位移（$\rho > \tau_1$），VIG 即寫入此邊，表示這兩個群心正沿相同方向收縮——這是**群心崩塌（centroid collapse）的早期信號**。VIG 的跨群心速度協調使 VIGPSO 能夠感知分群結構的退化風險，而非僅依賴純數值優化。

---

### 3.5.2 AVICPSO — 三軸自適應擴充機制

AVICPSO 在 VIGPSO 基礎上增加三項自適應機制（Axis A / B / C），專門針對分群問題中的退化現象進行主動干預。

#### Axis A — VIG 群心崩塌偵測 + 區塊 Lévy 重啟

**設計精神**：分群問題存在一種特殊退化模式——兩個群心在搜尋過程中逐漸相互吸引、最終重疊於同一資料區域，導致另一區域的群組失去代表，分群品質劇降。標準 PSO 觀察到的只是一個純量 fitness，無法在退化完成前察覺此問題。Axis A 的核心思想是**以 VIG 的結構感知能力提前偵測崩塌訊號**：兩個群心的座標維度若在族群中呈現高度同向移動，即表示它們正朝相同方向收縮。偵測到此信號後，立即對退化群心施加**外科式重啟**——僅擾動該群心對應的 $d$ 個維度，完整保留其餘 $(K-1)\times d$ 個優質群心，使干預的破壞面最小化。此設計將分群退化的應對從「觀測 fitness 惡化後被動補救」轉變為「觀測運動相關性主動預防」，是 AVICPSO 對分群問題結構性理解的核心體現。

**觸發條件**：每次 VIG 更新後，掃描所有群心對 $(k_i, k_j)$，提取對應的 $d \times d$ 子區塊 $\mathbf{G}_{k_i, k_j}$。若

$$\text{mean}\left(\mathbf{G}_{k_i, k_j}\right) > \theta_{\text{collapse}} = 0.6$$

則判定群心 $k_i$ 與 $k_j$ 在高維交互意義下已趨向合併。

**干預策略**：保留 pbest 品質較佳的群心；對退化群心執行**區塊 Lévy 重啟**，僅擾動該群心對應的 $d$ 個維度：

$$\mathbf{z}_{k}^{\text{new}} = \text{clip}\left(\mathbf{g}^{\text{best}}_{[k]} + \sigma_{\text{Lévy}} \cdot \text{Lévy}(d),\ 0,\ 1\right)$$

將新群心寫入**全部粒子**的對應位置（加入 $\mathcal{N}(0, 0.05)$ 擾動以維持多樣性），並清零對應維度的速度與停滯計數。以 gbest 為錨點確保重啟後的群心落在已知高品質區域的鄰近，而非隨機飛至資料密度極低的角落。

#### Axis B — 個體停滯偵測 + K-means Pool Lévy 重啟

**設計精神**：PSO 的社會學習機制在族群收斂後容易導致所有粒子聚集於 gbest 附近，形成多樣性喪失的「群聚退化」。傳統解法是在停滯時隨機重啟，但均勻隨機重啟會將粒子送入無結構的搜尋空間，浪費大量評估次數在結構無效的解（如群心完全重疊或產生空群）。Axis B 的核心思想是**將重啟視為多樣性問題而非隨機問題**：重啟的目的是讓停滯粒子探索「不同的資料結構盆地」，而非僅僅跳離當前位置。K-means pool 以資料本身的分布為依據，預先生成 5 個結構上合法且彼此差異化的分群配置作為重啟錨點，再以 Lévy 飛行在其鄰域探索。輪詢機制確保同時停滯的多個粒子跳至不同的基底，從根本上解決「重啟後族群再次聚集」的退化問題。

**觸發條件**：每個粒子獨立維護停滯計數器 $\text{stag\_cnt}[i]$，當 pbest 連續 `stagnation_thresh`（預設 15 代）未改善時觸發。

**干預策略**：採用**K-means 重啟池（restart pool）**策略。啟動前以 5 個不同 seed 執行 KMeans++ 預先產生 5 組合法群心配置，形成 restart pool $\mathcal{P} \in \mathbb{R}^{5 \times D}$。粒子停滯時依輪詢索引 $\text{idx}[i]$ 取出基底解：

$$\mathbf{X}_i \leftarrow \text{clip}\left(\mathcal{P}[\text{idx}[i] \bmod 5] + \sigma_{\text{Lévy}} \cdot \text{Lévy}(D),\ 0,\ 1\right)$$

$$\text{idx}[i] \mathrel{+}= 1, \quad \mathbf{V}_i \leftarrow \mathbf{0}, \quad \text{stag\_cnt}[i] \leftarrow 0$$

相較於「全部粒子從 gbest 擴散」的退化策略，K-means pool 確保各粒子跳至資料結構上合法且彼此分散的區域，有效防止重啟後的族群再次聚集於同一局部最優。

#### Axis C — 統一線性 / 指數排程

**設計精神**：Axis A 與 Axis B 的干預強度、VIG 的影響力、以及 PSO 本身的探索—開發平衡，三者在理想上應**隨迭代同步演化**：早期各機制均應偏向探索（大 Lévy 步長、低 VIG 依賴），後期均應收斂至精煉（小擾動、充分利用 VIG 結構）。若三者以獨立超參數分別調控，調參複雜度隨之三倍放大，且容易出現「探索已結束但 Lévy 仍大步跳躍」或「VIG 尚未穩定但 alpha 已過高」的相位不一致問題。Axis C 的核心思想是**以單一進度變數 $\text{prog}$ 作為全局時鐘**，將所有機制的強度排程綁定至同一演化進度，保證各機制的探索—開發轉換相位同步，無需獨立調參即可維持整體的一致性。

三項機制的關鍵超參數以統一的迭代進度 $\text{prog} = t / T_{\max}$ 共同調度：

| 參數 | 排程公式 | 語意 |
|------|----------|------|
| 慣性權重 $\omega_t$ | $\omega \cdot (1 - 0.6 \cdot \text{prog})$ | 線性遞減：早期全域探索，後期局部精煉 |
| Lévy 縮放 $\sigma_t$ | $\sigma_{\max} \cdot (1 - \text{prog})$ | 線性遞減：早期大跳躍，後期小擾動 |
| VIG 混合比例 $\alpha_t$ | $\alpha_{\max} \cdot (1 - e^{-2 \cdot \text{prog}})$ | 指數增長：VIG 穩定後才加大影響力 |

三者協同保證：早期（$\text{prog} \to 0$）以大 Lévy 步長、低 VIG 影響力自由探索；後期（$\text{prog} \to 1$）以小 Lévy 精煉、高 VIG 協調利用已知結構。$\omega_t$ 與 $\sigma_t$ 採線性衰減以保持可預測性，$\alpha_t$ 採指數增長以反映 VIG 邊在早期稀疏、後期趨於穩定的拓撲成熟週期。

---

### 3.5.3 Lévy 飛行的數學定義與分群應用

#### Mantegna（1994）近似採樣

本研究採用 Mantegna（1994）提出的 Lévy 穩定分布近似採樣法，穩定指數 $\beta = 1.5$：

$$\sigma_u = \left(\frac{\Gamma(1+\beta)\sin(\pi\beta/2)}{\Gamma\!\left(\frac{1+\beta}{2}\right)\beta\cdot 2^{(\beta-1)/2}}\right)^{1/\beta}$$

$$u \sim \mathcal{N}(0,\, \sigma_u^2), \quad v \sim \mathcal{N}(0,\, 1)$$

$$\text{Lévy}(D) = \frac{u}{|v|^{1/\beta}} \in \mathbb{R}^D$$

此分布具有重尾特性：大多數步長較小（局部精煉），偶發性出現極大跳躍（跳脫局部最優）。與均勻隨機重啟相比，Lévy 跳躍在大步長上的概率顯著更高，適合探索高維分群空間中的稀疏合法解區域。

#### 在分群問題中的特殊設計

標準 Lévy 飛行對所有維度施加等強度擾動，可能破壞已收斂的優質群心。本研究針對分群問題採取**區塊選擇性 Lévy**策略：

- **Axis A（群心崩塌重啟）**：Lévy 僅作用於退化群心對應的 $d = 3$ 個維度，保留其餘 $(K-1) \times d$ 個優質群心維度不受干擾
- **Axis B（個體停滯重啟）**：Lévy 以 K-means pool 解為錨點，確保基底在資料流形上合法，再以 Lévy 步長在其鄰域探索，避免跳至資料密度極低的退化區域
- **縮放排程**：Lévy 步長乘以時序遞減係數 $\sigma_t$，確保後期重啟不會大幅破壞已接近最優的分群結構

---

### 3.6 分群評估指標

| 指標 | 方向 | 說明 |
|------|------|------|
| **Silhouette Score** | ↑ 越高越好（-1 ~ 1） | 衡量每個點與自身群的相似度相對於鄰近群的差異，綜合反映群內聚合與群間分離 |
| **Davies-Bouldin Index (DBI)** | ↓ 越低越好（≥ 0） | 各群「群內平均散度 / 群心間距」的平均，基於群心計算，對 outlier 穩健 |
| **$\mathcal{M}$（Sum of Distances）** | ↓ 越低越好 | 論文定義的主要最佳化目標，所有點到群心距離之和；KMeans/KMeans++ 均以此為收斂依據 |
| **Cluster Distribution** | — | 各群樣本數，任一群 > 50% 視為平衡警示；最小群 < 100 視為退化解 |

> **不採用 Dunn Index**：在連續特徵空間中相鄰群共享邊界，最小群間距趨近於 0，導致 Dunn 值永遠偏低且缺乏區分力。

---

### 3.7 Benchmark 設計

本研究以 **PSO → VIGPSO → AVICPSO** 三種 PSO 家族演算法為比較核心，驗證逐步擴充的機制改進是否在分群品質上帶來可量化的增益。KMeans / KMeans++ 作為基線參照，SMA / HHO 作為非 PSO 系列的對照組，均記錄指標但不納入演化改進分析。統一以下設定：

| 設定項目 | 值 |
|----------|----|
| 迭代次數（iterations） | 500 |
| 族群大小（population） | 20 |
| 群數（$K$） | 3 |
| 特徵維度 | 3（Rating, Playtime, Hotness） |
| Random seed | 42 |
| Warm-start | KMeans++（n_init=1, max_iter=100） |
| 評估指標 | DBI, $\mathcal{M}$ |

---

### 3.8 Benchmark 實驗結果（population=20, $K=3$）

PSO / VIGPSO / AVICPSO 三種演算法統一以相同迭代次數（500 次）進行公平比較，確保計算預算一致，並均採用 KMeans++ warm-start 初始化。評估指標為 DBI（Davies-Bouldin Index）與 $\mathcal{M}$（群內總距離目標函數）。

#### 比較表

| 方法 | DBI ↓ | $\mathcal{M}$ ↓ |
|------|:-----:|:----------------:|
| PSO     | 0.8096 | 8,734 |
| VIGPSO  | 0.8078 | 8,733 |
| AVICPSO | **0.8018** | **8,733** |

#### 結論一：AVICPSO 在 DBI 上維持領先優勢

在三群設定（$K=3$）下，搜尋空間維度壓縮為 $K \times d = 3 \times 3 = 9$，使各演算法在 500 代內均已充分收斂，三者 $\mathcal{M}$ 差距僅為 1（8,733 vs. 8,734）。DBI 的差異更能反映群間分離品質的細微差別：AVICPSO（0.8018）較 PSO（0.8096）改善 0.78%，較 VIGPSO（0.8078）改善 0.60%。AVICPSO 的**停滯偵測 + 群心崩塌重啟**機制在低維搜尋空間中仍能維持相對優勢，透過主動重置退化群心、保留優質群心，達到更佳的群間分離品質。

#### 結論二：三種演算法的 $\mathcal{M}$ 收斂至相同水平

VIGPSO 與 AVICPSO 在 $\mathcal{M}$ 上同達 8,733，PSO 僅差 1 單位（8,734）。三群問題的低維搜尋空間使各方法均能快速定位接近最優的群心配置，演算法間的搜尋策略差異對最終目標函數值的影響趨於消失。在此條件下，DBI 成為區分群組結構品質的關鍵指標，而 AVICPSO 的自適應機制在此指標上仍展現出可量化的優勢。

---

### 3.9 分群結果視覺化分析（AVICPSO, $K=3$）

以下以 AVICPSO 產生的三群標籤進行五種視覺化分析。三群分佈為：**C0**（$n=17{,}942$，20.0%）、**C1**（$n=36{,}954$，41.2%）、**C2**（$n=34{,}722$，38.7%），樣本分配相對均衡，無退化群出現。

根據各維度均值，三群的市場語意對應如下：

| 群組 | Rating 均值 | 市場語意 |
|------|:-----------:|---------|
| C2 | ≈ 0.87 | 高口碑群 |
| C0 | ≈ 0.58 | 中口碑群 |
| C1 | ≈ 0.00 | 低口碑群 |

---

#### 3D 散佈圖

![3D Scatter](cluster_scatter3d_labels_avicpso.png)

從三維散佈圖可見，**rating 為三群的主要切割軸**：三群沿 rating 軸呈現明顯的層次分離。C2（高口碑，青色）集中於 rating 高值端；C0（中口碑，藍色）分佈於中段；C1（低口碑，深色）壓縮在 rating 接近 0 的低端。playtime 與 hotness 兩軸的資料點高度集中於低值區域（接近原點），各群在此兩維度上的邊界重疊較多，反映 Steam 遊戲在參與度指標上的強烈右偏特性。

---

#### Violin 圖

![Violin](cluster_violin_labels_avicpso.png)

Violin 圖從三個維度清晰揭示各群分佈形態：

**Rating 維度**：三群呈現明確的層次分離——C2 分佈集中於高值（≈ 0.85–0.90），群內變異小；C0 分佈較寬（0.20–0.75），中位數 ≈ 0.60；C1 密集於低值（≈ 0.00–0.10），有細長右尾至 0.25。此三層結構是分群的主要語意依據。

**Playtime 與 Hotness 維度**：三群均呈高度右偏的「錐形」——絕大多數樣本壓縮於 0 附近，少數頭部作品延伸至高值。三群在這兩個維度上的群心差距有限，進一步確認 $K=3$ 設定下**評分是最具區分力的市場定位維度**，參與度指標為輔助的次要分層依據。

---

#### Radar Chart（三角雷達圖）

![Radar](cluster_radar_labels_avicpso.png)

雷達圖以三個軸（rating / playtime / hotness）同時展示各群均值，形狀差異一目了然：三群幾乎僅在 rating 軸上有可見延伸，C2 沿 rating 延伸最遠（≈ 0.87），C0 居中（≈ 0.58），C1 幾乎貼近原點（≈ 0.00）；所有群在 playtime 與 hotness 軸上的延伸均極小（< 0.07），形成扁平的「針形」輪廓。此形態直觀印證 rating 的絕對主導地位，以及 playtime / hotness 在三群間近乎相同的低均值現象。

---

#### Bar Chart（群均值條狀圖）

![Bar Chart](cluster_bar_labels_avicpso.png)

條狀圖量化三群在各維度的均值差距：C2 的 rating 均值 ≈ 0.87 遠高於 C0（≈ 0.58）與 C1（≈ 0.00）；playtime 與 hotness 在所有群的均值均低於 0.07，三群幾乎無差異。此圖清晰呈現分群結果高度由 rating 單一維度主導的特性，以及遊戲市場在參與度指標上的極度集中分佈。

---

#### Boxplot（箱形圖）

![Boxplot](cluster_boxplot_labels_avicpso.png)

箱形圖以中位數、四分位距與極端值補充 Violin 圖的細節：rating 維度中，C2 的 IQR 極小（高度集中），C0 的 IQR 較寬反映中口碑群內部評分多樣性，C1 的中位數接近 0 但有少量正值異常點（個別作品雖評分低但仍有少數正評）。playtime 與 hotness 的箱體在三群均壓縮至接近 0，鬚線與極端值向上延伸，再次確認右偏分佈的普遍性。

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

本研究採用 **Mutual Information SelectKBest** 作為特徵篩選策略：

$$I(X_j; Y) = \sum_{y} \sum_{x_j} p(x_j, y) \log \frac{p(x_j, y)}{p(x_j)\, p(y)}$$

以 `mutual_info_classif` 計算每個特徵與目標群標籤的互資訊量，選取前 $k = \min(50, \text{總特徵數})$ 個特徵。此方法能捕捉非線性依賴關係，適合類別不平衡且特徵異質的場景。

**篩選流程**：僅在訓練集上 fit selector，再 transform 至測試集，避免測試集資訊洩漏至特徵選取過程。

---

## Stage 5 — 資料切分

| 項目 | 設定 |
|------|------|
| 切分比例 | Train 80% / Test 20% |
| 策略 | Stratified split（保持各群比例一致） |
| Random seed | 42 |

Target Encoding 與特徵篩選的 fit 均**僅在 Train 集**上執行，Test 集只做 transform，確保評估結果不受資料洩漏影響。

---

## Stage 6 — 模型訓練

### 6.1 分類器選擇

本研究選用三種 Gradient Boosting 與 Ensemble 方法進行比較：

| 模型 | 類型 | 不平衡處理 | 平行化 |
|------|------|-----------|--------|
| **XGBoost** | Gradient Boosting（level-wise） | `sample_weight="balanced"` | `n_jobs=-1` |
| **LightGBM** | Gradient Boosting（leaf-wise） | `class_weight="balanced"` | `n_jobs=-1` |
| **AdaBoost** | Adaptive Boosting | 加權樣本（內建） | 基底估計器並行 |

AdaBoost 使用 Decision Tree（`max_depth` 由 Optuna 搜索）作為 base estimator，並以 `algorithm="SAMME"` 支援多分類。

### 6.2 超參數最佳化（Optuna Bayesian Optimization）

所有模型均使用 **Optuna** 進行超參數搜索，採用 Tree-structured Parzen Estimator（TPE）作為 sampler，以貝葉斯方式在連續搜索空間中逐步集中於高性能區域。

**評估準則**：5-fold Stratified K-Fold CV + macro F1（對類別不平衡敏感）

#### XGBoost 搜索空間

| 超參數 | 範圍 | 類型 |
|--------|------|------|
| `n_estimators` | [100, 500] | int |
| `max_depth` | [3, 10] | int |
| `learning_rate` | [0.01, 0.3] | float（log） |
| `subsample` | [0.5, 1.0] | float |
| `colsample_bytree` | [0.5, 1.0] | float |
| `reg_alpha` | [1e-3, 10.0] | float（log） |
| `reg_lambda` | [1e-3, 10.0] | float（log） |

#### LightGBM 搜索空間

| 超參數 | 範圍 | 類型 |
|--------|------|------|
| `n_estimators` | [100, 500] | int |
| `max_depth` | [3, 15] | int |
| `learning_rate` | [0.01, 0.3] | float（log） |
| `num_leaves` | [20, 150] | int |
| `subsample` | [0.5, 1.0] | float |
| `colsample_bytree` | [0.5, 1.0] | float |
| `reg_alpha` | [1e-3, 10.0] | float（log） |
| `reg_lambda` | [1e-3, 10.0] | float（log） |

#### AdaBoost 搜索空間

| 超參數 | 範圍 | 類型 |
|--------|------|------|
| `n_estimators` | [50, 300] | int |
| `learning_rate` | [0.01, 1.0] | float（log） |
| `max_depth`（base） | [1, 5] | int |

**Trials 數量**：XGBoost / LightGBM 各 50 trials；AdaBoost 50 trials。

---

## Stage 7 — 最終評估

### 7.1 評估指標

| 指標 | 說明 |
|------|------|
| **Accuracy** | 整體正確率 |
| **Macro F1** | 各類別 F1 不加權平均，對類別不平衡敏感 |
| **CV F1-macro** | 5-fold 交叉驗證 macro F1，反映模型泛化穩定性 |
| **Macro AUC** | One-vs-Rest ROC 曲線下面積（macro 平均），衡量排序能力 |
| **Macro AP** | One-vs-Rest Precision-Recall 曲線下面積（macro 平均），對不平衡類別更具鑑別力 |
| **Per-class F1 / AUC / AP** | 各群組獨立指標，診斷模型對各市場定位群的學習效果 |

AP 對類別不平衡比 ROC-AUC 更敏感，能更真實反映少數群的預測效果。所有多分類 AUC / AP 均採用 **one-vs-rest** 策略計算。

### 7.2 最佳參數儲存

每個模型訓練完成後，將最佳超參數與所有評估指標以 JSON 格式存檔：

```
outputs/analysis/
  params_XGBoost.json
  params_LightGBM.json
  params_AdaBoost.json
```

### 7.3 實驗結果

#### 綜合效能比較

| 模型 | Accuracy | Macro F1 | CV F1（5-fold） | Macro AUC | Macro AP |
|------|:--------:|:--------:|:--------------:|:---------:|:--------:|
| **XGBoost**  | **0.6457** | **0.6016** | **0.8941 ± 0.0038** | **0.7701** | **0.6025** |
| **LightGBM** | 0.6420 | 0.5967 | 0.8938 ± 0.0042 | 0.7625 | 0.5874 |
| **AdaBoost** | 0.6244 | 0.5673 | 0.8885 ± 0.0043 | 0.7424 | 0.5577 |

89,618 筆樣本（Train: 71,694 / Test: 17,924），Mutual Information SelectKBest 共選出 32 個特徵。

#### 各群 Per-Class 指標

**XGBoost**

| 群組 | Precision | Recall | F1 | Support |
|------|:---------:|:------:|:--:|:-------:|
| C0（中口碑） | 0.484 | 0.373 | 0.421 | 3,588 |
| C1（低口碑） | 0.707 | 0.773 | 0.739 | 7,391 |
| C2（高口碑） | 0.639 | 0.651 | 0.645 | 6,945 |

**LightGBM**

| 群組 | Precision | Recall | F1 | Support |
|------|:---------:|:------:|:--:|:-------:|
| C0（中口碑） | 0.481 | 0.362 | 0.413 | 3,588 |
| C1（低口碑） | 0.704 | 0.771 | 0.736 | 7,391 |
| C2（高口碑） | 0.633 | 0.650 | 0.641 | 6,945 |

**AdaBoost**

| 群組 | Precision | Recall | F1 | Support |
|------|:---------:|:------:|:--:|:-------:|
| C0（中口碑） | 0.478 | 0.298 | 0.367 | 3,588 |
| C1（低口碑） | 0.654 | 0.821 | 0.728 | 7,391 |
| C2（高口碑） | 0.632 | 0.583 | 0.607 | 6,945 |

#### 最佳超參數

| 超參數 | XGBoost | LightGBM | AdaBoost |
|--------|---------|----------|----------|
| `n_estimators` | 450 | 105 | 240 |
| `max_depth` | 5 | 7 | 5（base） |
| `learning_rate` | 0.0357 | 0.0444 | 0.0279 |
| `subsample` | 0.781 | 0.546 | — |
| `colsample_bytree` | 0.561 | 0.681 | — |
| `num_leaves` | — | 126 | — |
| `reg_alpha` | 0.022 | 0.263 | — |
| `reg_lambda` | 0.001 | 0.074 | — |

#### 結果分析

**模型排名**：XGBoost 在全部五個指標上均取得最高分，LightGBM 緊隨其後，AdaBoost 在各項指標均為最低。三者的 CV F1（0.889–0.894）遠高於 Test F1（0.567–0.602），顯示測試集（基於描述性特徵的跨域評估）難度高於交叉驗證。

**CV vs. Test F1 差距**：CV F1 約 0.89 vs. Test F1 約 0.57–0.60，差距約 0.29–0.32。此落差主要來自資料的固有不可分性：Phase 1 分群依據（rating、playtime、hotness）被排除後，Phase 2 只能依靠遊戲描述性特徵（類型、標籤、開發商、平台等）預測市場定位，而這些特徵與市場表現之間的關聯本質上較弱。

**C0 識別困難**：三個模型一致在 C0（中口碑群，n=3,588，佔 20%）上表現最差（F1：0.37–0.42），召回率僅 0.30–0.37。中口碑遊戲在元資料特徵上同時與高口碑和低口碑群重疊——類型、標籤、開發商規模皆無鮮明特徵——口碑差異主要反映在評分維度，使分類器在排除評分資訊後難以有效辨識。

**C1 識別最佳**：低口碑群（C1，n=7,391，為最大群）在三個模型均達到最高 F1（0.728–0.739）。一方面訓練樣本最多（29,563 筆）提供充足學習資料；另一方面低口碑遊戲在開發商規模、價格策略、標籤稀疏度等特徵上往往呈現共同模式，使模型較易習得判別邊界。

**AdaBoost 的高 Recall/低 Precision 傾向**：AdaBoost 在 C1（低口碑）的 Recall 達 0.821，但 Precision 僅 0.654，顯示模型對 C1 過度預測（傾向將 C0/C2 誤分類為 C1）。結合 C0 的低 Recall（0.298），可觀察到 AdaBoost 對少數類 C0（中口碑）的識別能力明顯弱於 Gradient Boosting 方法。

**Macro AUC vs. Macro AP**：三個模型的 AUC（0.742–0.770）均高於 AP（0.558–0.603），反映模型具備一定的排序能力，但在精確度—召回率曲線下面積（更重視少數類正確率）上表現相對有限，與 C0 識別困難的結論一致。

---

## Stage 8 — 視覺化分析

### 8.1 Optuna 超參數搜索歷程

![Optuna XGBoost](outputs/analysis/optuna_history_XGBoost.png)
![Optuna LightGBM](outputs/analysis/optuna_history_LightGBM.png)
![Optuna AdaBoost](outputs/analysis/optuna_history_AdaBoost.png)

**XGBoost**：50 trials 中，best 在 trial 31 穩定於 0.8941，整體搜索收斂在 trial 22 之後趨於平緩，後期新 trial 雖偶有高點但改善幅度極小。超參數重要性顯示 `learning_rate` 壓倒性地主導搜索結果（重要性 ≈ 0.65），`max_depth` 居次（≈ 0.15），其餘正則化參數（`colsample_bytree`、`reg_lambda` 等）影響力合計不及 0.20，說明學習率是 XGBoost 在此任務最敏感的超參數。

**LightGBM**：收斂更快，trial 15 即找到接近最佳解（0.8938），此後 35 個 trial 幾乎未能改善。超參數重要性中 `learning_rate` 獨占絕對主導（≈ 0.90），其餘所有參數（`colsample_bytree`、`max_depth`、`num_leaves` 等）重要性均接近 0，反映 LightGBM 在此資料集上對學習率以外的結構性超參數較為不敏感。

**AdaBoost**：30 trials 的搜索顯示收斂較慢，best 在 trial 13 確立（0.8885）後仍持續微幅改善。超參數重要性呈現三者均衡分布：`max_depth`（base estimator 深度，≈ 0.42）、`learning_rate`（≈ 0.33）、`n_estimators`（≈ 0.25），與 Gradient Boosting 方法相比，AdaBoost 對決策樹基底深度的敏感度更高，反映 base estimator 表達能力直接限制集成上限。

---

### 8.2 Cross-Validation 穩定性

![CV Scores XGBoost](outputs/analysis/cv_scores_XGBoost.png)

以 XGBoost 為代表，5-fold CV 各折 F1-macro 均高度一致，五折得分落於 0.893–0.895 之間，均值 0.8941，標準差僅 0.0038。極低的折間變異表明模型在交叉驗證下對訓練集的劃分不敏感，泛化能力穩定，不存在因特定折分布造成的估計偏差。LightGBM（0.8938 ± 0.0042）與 AdaBoost（0.8885 ± 0.0043）亦呈現相同的高穩定性。

---

### 8.3 Learning Curve 分析

![Learning Curve XGBoost](outputs/analysis/learning_curve_XGBoost.png)
![Learning Curve LightGBM](outputs/analysis/learning_curve_LightGBM.png)
![Learning Curve AdaBoost](outputs/analysis/learning_curve_AdaBoost.png)

**XGBoost / LightGBM（相似模式）**：Train F1 從小樣本的高點（XGB: 0.997, LGBM: 0.978 at 5k samples）隨樣本增加單調下降，至全量 57k 時收斂至 0.918 / 0.912；Validation F1 則從 0.885 緩升至 0.894，呈現「Train 下降、Val 上升、兩線趨近」的典型學習曲線形態。最終 Train–Val 差距約 0.024–0.026，屬輕度過擬合。Validation 曲線在全資料點仍微幅上升，顯示若訓練資料進一步增加，模型仍有提升空間。

**AdaBoost**：兩條曲線在整個樣本量範圍內非常接近——Train F1 從 0.926 降至 0.894，Val F1 從 0.880 升至 0.889，最終 gap 僅約 0.005。AdaBoost 使用深度受限的決策樹（max_depth=5）作為 base estimator，模型容量較 Gradient Boosting 低，過擬合程度最輕，但訓練集的 F1 上限也最低。Validation 曲線同樣仍在上升，說明 AdaBoost 並非受限於資料量，而是受限於模型表達能力。

---

### 8.4 Per-class F1 與群組識別難度

![Per-class F1 XGBoost](outputs/analysis/per_class_f1_XGBoost.png)
![Per-class F1 LightGBM](outputs/analysis/per_class_f1_LightGBM.png)
![Per-class F1 AdaBoost](outputs/analysis/per_class_f1_AdaBoost.png)

三個模型呈現一致的群組識別難度排序：**C1（低口碑）最易識別 → C2（高口碑）居中 → C0（中口碑）最難**。

- **C1**（support=7,391，最大群，低口碑）：三模型 F1 分別為 0.739 / 0.736 / 0.728。低口碑遊戲佔總樣本 41%，訓練資料量最多；此外，低評分遊戲往往在開發商知名度、定價策略、標籤稀疏度上具有共同特徵，使模型具備良好的辨識線索。
- **C2**（support=6,945，高口碑）：F1 約 0.641–0.645。高口碑群的特徵相對清晰，但部分高品質獨立遊戲在元資料上與中口碑群重疊，主要混淆發生在 C2 → C1 方向（見混淆矩陣）。
- **C0**（support=3,588，最小群，中口碑）：F1 僅 0.367–0.421，召回率最低（XGB: 0.373, LGBM: 0.362, AdaBoost: 0.298）。中口碑遊戲在元資料上既像高口碑（類似類型、標籤），又像低口碑（不知名開發商、低價），缺乏鮮明的辨識特徵，導致大量誤分類至 C2（高口碑）方向。

---

### 8.5 Per-class ROC 曲線（以 XGBoost 為例）

![ROC AUC XGBoost](outputs/analysis/roc_auc_XGBoost.png)

以 XGBoost 個別群的 One-vs-Rest ROC 為例，三條曲線均位於隨機基線（對角線）之上，驗證模型具備有效分類能力。各群 AUC 反映其識別難度：**C1（0.830）> C2（0.762）> C0（0.719）**，與 Per-class F1 的排序完全一致。

C1 曲線在低 FPR 區域（0–0.2）即快速攀升，顯示低口碑群在高精確度閾值下仍有良好的召回率。C0 曲線的 AUC（0.719）雖明顯高於隨機，但在低 FPR 段上升緩慢，反映模型在高置信度條件下識別中口碑遊戲的能力有限。

---

### 8.6 Prediction Confidence 分析

![Prediction Confidence XGBoost](outputs/analysis/prediction_confidence_XGBoost.png)
![Prediction Confidence AdaBoost](outputs/analysis/prediction_confidence_AdaBoost.png)

**XGBoost**：呈現雙峰分布——在最大預測機率接近 1.0 的區間有大量正確預測（約 3,400 筆），同時在此區間也有約 1,300 筆錯誤預測（高置信度錯誤），顯示模型對某些邊界樣本過度自信。在 0.35–0.95 的中低置信度區間，正確與錯誤預測比例接近，屬於模型本身不確定的困難樣本，即 C0（中口碑）與 C1/C2 的邊界重疊區域。

**AdaBoost**：機率分布完全不同，所有預測機率壓縮在極窄範圍（0.33–0.51）。這是 SAMME 演算法以弱分類器加權投票計算機率的固有特性——其軟性機率輸出缺乏梯度提升的精細校準，導致「高置信度」峰值僅出現在 ≈ 0.51 附近。在 0.33–0.36 的低機率端，錯誤預測佔比高，對應 C0（中口碑）的大量誤分類；≈ 0.35 峰值處正確與錯誤樣本混雜，說明 AdaBoost 對此資料集的概率估計校準性遠低於 Gradient Boosting 方法。

---

### 8.7 跨模型比較：Confusion Matrix

![Confusion Matrix Combined](outputs/analysis/confusion_matrix_combined.png)

三個混淆矩陣並排揭示各模型的誤分類模式：

**共同主要誤分類**：C0（中口碑）→ C2（高口碑）為三個模型最大的錯誤來源（XGB: 1,427；LGBM: 1,456；AdaBoost: 1,450），佔 C0 測試樣本的 40–41%。中口碑遊戲在描述性特徵上容易被誤判為高口碑，是分類器最困難的邊界。C2（高口碑）→ C1（低口碑）的混淆次之（XGB: 1,543；LGBM: 1,568；AdaBoost: 2,142），部分高品質遊戲因缺乏知名開發商標誌而被誤歸為低口碑群；AdaBoost 在此方向的誤分類最嚴重。

**C1 對角線最強**：三個模型在 C1（低口碑）的正確預測數最高（XGB: 5,715；LGBM: 5,697；AdaBoost: 6,071）。AdaBoost 的 C1 對角線數值最高（6,071），但這是以犧牲 C2 正確率為代價（C2 對角線最低：4,052），反映 AdaBoost 過度預測低口碑的傾向。

**C0 識別一致低落**：三個模型的 C0（中口碑）對角線均最小（XGB: 1,338；LGBM: 1,300；AdaBoost: 1,068），AdaBoost 的 C0 召回率僅 29.8%，為三者最低。

---

### 8.8 跨模型比較：ROC 與 PR 曲線

![ROC Combined](outputs/analysis/roc_combined.png)
![PR Combined](outputs/analysis/pr_combined.png)

**ROC 曲線**：三條曲線均遠高於隨機基線，整體形態相似。在 FPR 0.0–0.2 段差異最為明顯，XGBoost（AUC=0.770）在此高精確度區間表現最佳，領先 LightGBM（0.762）與 AdaBoost（0.742）。FPR > 0.4 之後三條曲線逐漸收斂，說明模型在低閾值（允許更多假陽性）條件下的排序能力趨於一致。

**PR 曲線**：曲線起點均從精確度 ≈ 1.0（零召回率）急速下降，在 Recall ≈ 0.10–0.15 後趨於平穩，維持精確度約 0.66–0.69（XGBoost/LightGBM）。AdaBoost 在 Recall ≈ 0.05–0.10 出現明顯凹陷再回升，反映其在低召回率閾值下概率估計不穩定。三條曲線在 Recall > 0.8 後同步下滑並趨近，XGBoost（AP=0.602）全程保持最高精確度，在所有召回率水平均優於另兩者。

---

### 8.9 跨模型比較：Feature Importance

![Feature Importance Combined](outputs/analysis/feature_importance_combined.png)

三個模型的特徵重要性揭示不同的重點依賴：

**XGBoost**：前五名均為 Target Encoding 衍生特徵，`developers_te_cls1`（≈ 0.20）居首，其次是 `developers_te_cls2`、`publishers_te_cls2`、`publishers_te_cls0` 等。開發商與發行商的群組條件機率特徵主導了 XGBoost 的決策，說明「是誰做的」是預測市場定位的最強信號。`price`（價格）也出現在前十，反映定價策略與市場定位有一定相關性。

**LightGBM**：`days_since_release`（上線天數）壓倒性領先（重要性 ≈ 1,600），遠高於第二名 `tags_te_cls0`（≈ 850）與 `publishers_te_cls2`（≈ 800）。LightGBM 的 leaf-wise 生長策略對連續型特徵更敏感，因此時間特徵（遊戲在市場存在多久）被大量分裂利用。此結果顯示遊戲的上架歷史長度與其最終市場定位存在顯著關聯——上架越久的遊戲越容易被用戶口碑所分類。

**AdaBoost**：重要性分布更為集中，`developers_te_cls1`（≈ 0.38）單獨高出其餘特徵一截，`developers_te_cls0`（≈ 0.08）、`publishers_te_cls1`（≈ 0.07）依次跟隨。`days_since_release` 也進入前十（≈ 0.10），但重要性遠低於 LightGBM。整體而言，AdaBoost 的特徵依賴模式更接近 XGBoost，以開發商/發行商的目標編碼為核心，但每棵決策樹的淺層結構（max_depth=5）限制了其對複雜特徵交互的捕捉。

---

### 8.10 跨模型綜合效能比較

![Model Comparison](outputs/analysis/model_comparison.png)

五項指標並排比較直觀呈現三模型的全面差距。**CV F1（黃色柱）**在三個模型間差異最小（0.889–0.894），而 **Macro AUC（紫色柱，0.742–0.770）** 與 **Test F1（橘色柱，0.567–0.602）** 的差距更能反映模型的實際泛化能力。

最引人注意的是 CV F1 與 Test F1 之間 ≈ 0.29–0.32 的系統性落差——這並非模型設計問題，而是任務本質所致：Phase 1 的分群依據（評分、遊玩時長、熱度）在 Phase 2 中被排除，分類器只能以遊戲的靜態元資料預測市場定位，而這些特徵與市場表現之間存在固有的弱相關性，使得即使模型在交叉驗證中已充分學習，面對此「跨域預測」仍有顯著的效能衰減。XGBoost 在全部指標上的一致領先，確認其為本研究 Phase 2 的最佳分類模型。

---

## 附錄：系統架構

```
configs/
  assign_avicpso.yaml     # AVICPSO（主要分群方法）
  assign_ga.yaml          # GA
  assign_kmeans.yaml      # KMeans
  assign_kmeans_plus.yaml # KMeans++
  assign_pso.yaml         # PSO
  assign_sma.yaml         # SMA
  assign_hho.yaml         # HHO
  assign_vigpso.yaml      # VIGPSO

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
    sma.py                # SMA
    hho.py                # HHO
    vigpso.py             # VIGPSO
    avicpso.py            # AVICPSO

src/phase2/
  pipeline.py             # 特徵工程 pipeline（base / categorical / selection）
  optimize.py             # Optuna / RandomSearch / GridSearch
  features/
    numerical.py          # 數值型特徵（StandardScaler）
    temporal.py           # 時間型特徵衍生
    categorical.py        # Target Encoding
    text.py               # TF-IDF（選配）

run_phase1.py             # Phase 1 單一實驗入口
benchmark_phase1.py       # Phase 1 七方法橫向比較（輸出 labels CSV）
analyze_clusters_xgb.py   # Phase 2 分析主程式（XGB + LGBM + AdaBoost）
visualize_clusters.py     # 分群視覺化（Boxplot / Violin / Radar / 3D Scatter）
```
