import pandas as pd, sys, io, numpy as np
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8', errors='replace')

df = pd.read_csv('D:/Project/SELAB/steam-game-kaggle/datasets/srgiomanhes/steam_games.csv')

print('=== srgiomanhes EDA ===')
print(f'Shape: {df.shape}')
print()

# Missing values
null_pct = (df.isnull().sum() / len(df) * 100).sort_values(ascending=False)
print('--- Missing (>0%) ---')
missing = null_pct[null_pct > 0]
if len(missing) == 0:
    print('  None! All columns 100% complete.')
else:
    for col, pct in missing.items():
        print(f'  {col}: {pct:.1f}%')
print()

# Top5 by reviews
top5 = df.nlargest(5, 'total_reviews')[['steam_appid','name','price_initial (USD)','total_positive','total_negative','positive_percentual']]
print('Top5 by total_reviews:')
print(top5.to_string())
print()

# positive_percentual (target variable)
df_rev = df[df['total_reviews'] >= 10].copy()
print(f'Games with reviews (>=10): {len(df_rev):,} / {len(df):,}')
print(f'positive_percentual: mean={df_rev["positive_percentual"].mean():.1f}%, median={df_rev["positive_percentual"].median():.1f}%, std={df_rev["positive_percentual"].std():.1f}%')
q = df_rev['positive_percentual'].quantile([0.1, 0.25, 0.5, 0.75, 0.9])
print('Percentiles:', {int(k*100): round(v,1) for k,v in q.items()})
print()

# review_score_desc distribution
print('--- review_score_desc Distribution ---')
label_counts = df['review_score_desc'].value_counts()
for label, cnt in label_counts.items():
    print(f'  {label}: {cnt:,} ({cnt/len(df)*100:.1f}%)')
print()

# Price
print('--- Price (USD) ---')
print(f'  is_free=True: {df["is_free"].sum():,} ({df["is_free"].mean()*100:.1f}%)')
print(f'  price=0: {(df["price_initial (USD)"]==0).sum():,} ({(df["price_initial (USD)"]==0).mean()*100:.1f}%)')
paid = df[df['price_initial (USD)'] > 0]
print(f'  Paid: {len(paid):,} | mean=${paid["price_initial (USD)"].mean():.2f}, median=${paid["price_initial (USD)"].median():.2f}, max=${paid["price_initial (USD)"].max():.2f}')
tiers = [('0-5',0,5),('5-15',5,15),('15-30',15,30),('30-60',30,60),('60+',60,9999)]
for label, lo, hi in tiers:
    n = ((paid['price_initial (USD)']>lo) & (paid['price_initial (USD)']<=hi)).sum()
    print(f'  ${label}: {n:,}')
print()

# Metacritic
mc = df[df['metacritic'] > 0]
print(f'--- Metacritic ---')
print(f'  Has score: {len(mc):,} ({len(mc)/len(df)*100:.1f}%)')
print(f'  mean={mc["metacritic"].mean():.1f}, median={mc["metacritic"].median():.1f}')
print()

# Genres
print('--- Genres (top 15) ---')
from collections import Counter
genre_counter = Counter()
for g in df['genres'].dropna():
    for genre in str(g).split(','):
        genre_counter[genre.strip()] += 1
for genre, cnt in genre_counter.most_common(15):
    print(f'  {genre}: {cnt:,}')
print()

# Categories
print('--- Categories (top 10) ---')
cat_counter = Counter()
for c in df['categories'].dropna():
    for cat in str(c).split(','):
        cat_counter[cat.strip()] += 1
for cat, cnt in cat_counter.most_common(10):
    print(f'  {cat}: {cnt:,}')
print()

# is_released
print(f'--- is_released ---')
print(f'  Released: {df["is_released"].sum():,} ({df["is_released"].mean()*100:.1f}%)')
print()

# Platforms
print('--- Platforms ---')
from collections import Counter
plat_counter = Counter()
for p in df['platforms'].dropna():
    for plat in str(p).split(','):
        plat_counter[plat.strip()] += 1
for plat, cnt in plat_counter.most_common():
    print(f'  {plat}: {cnt:,}')
print()

# Achievements
print(f'--- n_achievements ---')
print(f'  With achievements (>0): {(df["n_achievements"]>0).sum():,} ({(df["n_achievements"]>0).mean()*100:.1f}%)')
ach = df[df['n_achievements']>0]
print(f'  mean={ach["n_achievements"].mean():.1f}, median={ach["n_achievements"].median():.1f}')
