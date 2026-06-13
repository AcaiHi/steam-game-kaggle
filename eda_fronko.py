import pandas as pd, sys, io, numpy as np
from collections import Counter
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8', errors='replace')

# NOTE: This CSV was saved with AppID as the pandas index.
# Actual column mapping (with index_col=0):
#   df.index         = AppID
#   df['AppID']      = Name (game title)
#   df['Name']       = Release date
#   df['Release date']      = Estimated owners
#   df['Estimated owners']  = Peak CCU
#   df['Peak CCU']          = Required age
#   df['Required age']      = Price (USD)
#   df['Price']             = DLC count
#   df['About the game'] onwards = correct

df = pd.read_csv('D:/Project/SELAB/steam-game-kaggle/datasets/fronkongames/games.csv',
                 low_memory=False, index_col=0)

# Aliases to correct column names
df.index.name = 'steam_appid'
df = df.rename(columns={
    'AppID':            'name',
    'Name':             'release_date',
    'Release date':     'estimated_owners',
    'Estimated owners': 'peak_ccu',
    'Peak CCU':         'required_age',
    'Required age':     'price',
    'Price':            'dlc_count',
})

print('=== fronkongames EDA ===')
print(f'Shape: {df.shape}')
print()

# Validate
cs2 = df.loc[730]
print('Validate CS2:')
print(f'  name={cs2["name"]}, release={cs2["release_date"]}, price={cs2["price"]}, pos={cs2["Positive"]}, neg={cs2["Negative"]}')
print()

# Missing values (relevant columns only)
relevant = ['name','release_date','estimated_owners','price','dlc_count',
            'Positive','Negative','Metacritic score','Average playtime forever',
            'Tags','Genres','Categories','Developers','Publishers']
null_pct = (df[relevant].isnull().sum() / len(df) * 100).sort_values(ascending=False)
print('--- Missing (relevant cols) ---')
for col, pct in null_pct[null_pct > 0].items():
    print(f'  {col}: {pct:.1f}%')
print(f'  (other cols: Achievements 100%, User score 96.5%, Score rank 100%)')
print()

# Positive ratio
df['positive_ratio'] = df['Positive'] / (df['Positive'] + df['Negative'])
df_rev = df[(df['Positive'] + df['Negative']) >= 10].copy()
print(f'Games with reviews (>=10): {len(df_rev):,} / {len(df):,}')
print(f'positive_ratio: mean={df_rev["positive_ratio"].mean():.3f}, median={df_rev["positive_ratio"].median():.3f}, std={df_rev["positive_ratio"].std():.3f}')
q = df_rev['positive_ratio'].quantile([0.1, 0.25, 0.5, 0.75, 0.9])
print('Percentiles:', {int(k*100): round(v,3) for k,v in q.items()})
print()

# Rating label distribution
def steam_label(row):
    total = row['Positive'] + row['Negative']
    if total < 10:
        return 'No reviews'
    ratio = row['Positive'] / total
    if total >= 500 and ratio >= 0.95:
        return 'Overwhelmingly Positive'
    elif ratio >= 0.80:
        return 'Very Positive'
    elif ratio >= 0.70:
        return 'Mostly Positive'
    elif ratio >= 0.40:
        return 'Mixed'
    elif ratio >= 0.20:
        return 'Mostly Negative'
    else:
        return 'Overwhelmingly Negative'

df['steam_label'] = df.apply(steam_label, axis=1)
print('--- Steam Rating Label Distribution ---')
for label, cnt in df['steam_label'].value_counts().items():
    print(f'  {label}: {cnt:,} ({cnt/len(df)*100:.1f}%)')
print()

# Price
print('--- Price (USD) ---')
df['price'] = pd.to_numeric(df['price'], errors='coerce')
print(f'  Free (0): {(df["price"]==0).sum():,} ({(df["price"]==0).mean()*100:.1f}%)')
paid = df[df['price'] > 0]
print(f'  Paid: {len(paid):,} | mean=${paid["price"].mean():.2f}, median=${paid["price"].median():.2f}, max=${paid["price"].max():.2f}')
tiers = [('0-5',0,5),('5-15',5,15),('15-30',15,30),('30-60',30,60),('60+',60,9999)]
for label, lo, hi in tiers:
    n = ((paid['price']>lo) & (paid['price']<=hi)).sum()
    print(f'  ${label}: {n:,}')
print()

# Metacritic
mc = df[df['Metacritic score'] > 0]
print(f'--- Metacritic score ---')
print(f'  Has score: {len(mc):,} ({len(mc)/len(df)*100:.1f}%)')
print(f'  mean={mc["Metacritic score"].mean():.1f}, median={mc["Metacritic score"].median():.1f}')
print()

# Playtime
pt = df[df['Average playtime forever'] > 0]
print(f'--- Average playtime forever (>0 mins) ---')
print(f'  Count: {len(pt):,} ({len(pt)/len(df)*100:.1f}%)')
q_pt = pt['Average playtime forever'].quantile([0.25,0.5,0.75,0.9,0.99]) / 60
print(f'  mean={pt["Average playtime forever"].mean()/60:.1f}hr, median={pt["Average playtime forever"].median()/60:.1f}hr')
print('  Percentiles (hr):', {int(k*100): round(v,1) for k,v in q_pt.items()})
print()

# Estimated owners range
print('--- Estimated owners (top values) ---')
for v, c in df['estimated_owners'].value_counts().head(10).items():
    print(f'  {v}: {c:,}')
print()

# Tags
tags_notnull = df['Tags'].dropna()
print(f'--- Tags ---')
print(f'  Has tags: {len(tags_notnull):,} ({len(tags_notnull)/len(df)*100:.1f}%)')
tag_counter = Counter()
for tags_str in tags_notnull:
    for tag in str(tags_str).split(','):
        tag_counter[tag.strip()] += 1
print('  Top 15 tags:')
for tag, cnt in tag_counter.most_common(15):
    print(f'    {tag}: {cnt:,}')
print()

# Genres
genres_notnull = df['Genres'].dropna()
print(f'--- Genres (top 15) ---')
genre_counter = Counter()
for g in genres_notnull:
    for genre in str(g).split(','):
        genre_counter[genre.strip()] += 1
for genre, cnt in genre_counter.most_common(15):
    print(f'  {genre}: {cnt:,}')
print()

# Peak CCU
ccu = df[df['peak_ccu'] > 0]
print(f'--- Peak CCU (>0) ---')
print(f'  Count: {len(ccu):,} ({len(ccu)/len(df)*100:.1f}%)')
print(f'  mean={ccu["peak_ccu"].mean():.0f}, median={ccu["peak_ccu"].median():.0f}')
