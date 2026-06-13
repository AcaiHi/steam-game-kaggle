import pandas as pd, sys, io, numpy as np
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8', errors='replace')

# ---- Load fronkongames (with corrected column mapping) ----
df_f = pd.read_csv('D:/Project/SELAB/steam-game-kaggle/datasets/fronkongames/games.csv',
                   low_memory=False, index_col=0)
df_f.index.name = 'steam_appid'
df_f = df_f.rename(columns={
    'AppID': 'name', 'Name': 'release_date', 'Release date': 'estimated_owners',
    'Estimated owners': 'peak_ccu', 'Peak CCU': 'required_age',
    'Required age': 'price', 'Price': 'dlc_count',
})
df_f['positive_ratio_f'] = df_f['Positive'] / (df_f['Positive'] + df_f['Negative'])
df_f['total_reviews_f'] = df_f['Positive'] + df_f['Negative']

# ---- Load srgiomanhes ----
df_s = pd.read_csv('D:/Project/SELAB/steam-game-kaggle/datasets/srgiomanhes/steam_games.csv')
df_s = df_s.set_index('steam_appid')
df_s['positive_ratio_s'] = df_s['total_positive'] / df_s['total_reviews'].replace(0, np.nan)

print('=== Dataset Size Comparison ===')
print(f'fronkongames: {len(df_f):,} games')
print(f'srgiomanhes:  {len(df_s):,} games')

# ---- Overlap ----
common = df_f.index.intersection(df_s.index)
only_f = df_f.index.difference(df_s.index)
only_s = df_s.index.difference(df_f.index)

print()
print('=== AppID Overlap ===')
print(f'Common:          {len(common):,}')
print(f'Only in fronko:  {len(only_f):,}')
print(f'Only in srgio:   {len(only_s):,}')

# ---- Merge on common AppIDs ----
merged = df_f.loc[common][['name','positive_ratio_f','total_reviews_f','price','Metacritic score']].copy()
merged['positive_ratio_s'] = df_s.loc[common]['positive_ratio_s']
merged['total_reviews_s'] = df_s.loc[common]['total_reviews']
merged['price_s'] = df_s.loc[common]['price_initial (USD)']
merged['metacritic_s'] = df_s.loc[common]['metacritic']

# Filter to games with enough reviews in both
both_rev = merged[(merged['total_reviews_f'] >= 10) & (merged['total_reviews_s'] >= 10)].dropna(
    subset=['positive_ratio_f', 'positive_ratio_s'])
print()
print(f'=== Positive Ratio Correlation (games with >=10 reviews in BOTH) ===')
print(f'N = {len(both_rev):,}')
corr = both_rev['positive_ratio_f'].corr(both_rev['positive_ratio_s'])
print(f'Pearson r = {corr:.4f}')
# Absolute difference
diff = (both_rev['positive_ratio_f'] - both_rev['positive_ratio_s']).abs()
print(f'Mean absolute diff: {diff.mean():.4f}')
print(f'Median absolute diff: {diff.median():.4f}')
print(f'% of games with diff < 1%: {(diff < 0.01).mean()*100:.1f}%')
print(f'% of games with diff < 5%: {(diff < 0.05).mean()*100:.1f}%')

# Review count comparison
print()
print('=== Total Reviews Count Comparison ===')
rev_corr = both_rev['total_reviews_f'].corr(both_rev['total_reviews_s'])
print(f'Pearson r (total reviews) = {rev_corr:.4f}')
ratio = both_rev['total_reviews_f'] / both_rev['total_reviews_s']
print(f'fronko/srgio review ratio: mean={ratio.mean():.2f}, median={ratio.median():.2f}')
print('(>1 means fronko has more reviews, likely due to later crawl date)')

# Price comparison
print()
print('=== Price Comparison ===')
price_both = merged[['price','price_s']].dropna()
price_both['price'] = pd.to_numeric(price_both['price'], errors='coerce')
price_both = price_both.dropna()
p_corr = price_both['price'].corr(price_both['price_s'])
print(f'Pearson r (price) = {p_corr:.4f}')
p_diff = (price_both['price'] - price_both['price_s']).abs()
print(f'Mean abs price diff: ${p_diff.mean():.4f}')
print(f'Exact match: {(p_diff == 0).mean()*100:.1f}%')

# Metacritic comparison
print()
print('=== Metacritic Score Comparison ===')
mc_both = merged[(merged['Metacritic score'] > 0) & (merged['metacritic_s'] > 0)][['Metacritic score','metacritic_s']].dropna()
print(f'Games with Metacritic in both: {len(mc_both):,}')
if len(mc_both) > 0:
    mc_corr = mc_both['Metacritic score'].corr(mc_both['metacritic_s'])
    print(f'Pearson r (metacritic) = {mc_corr:.4f}')
    mc_diff = (mc_both['Metacritic score'] - mc_both['metacritic_s']).abs()
    print(f'Mean abs diff: {mc_diff.mean():.2f}')
    print(f'Exact match: {(mc_diff == 0).mean()*100:.1f}%')

# Review count discrepancy: srgio vs fronko
print()
print('=== Review Count Discrepancy (fronko has MORE reviews = game appeared after srgio crawl) ===')
has_more = (both_rev['total_reviews_f'] > both_rev['total_reviews_s']).sum()
has_less = (both_rev['total_reviews_f'] < both_rev['total_reviews_s']).sum()
same = (both_rev['total_reviews_f'] == both_rev['total_reviews_s']).sum()
print(f'fronko > srgio: {has_more:,} ({has_more/len(both_rev)*100:.1f}%)')
print(f'fronko < srgio: {has_less:,} ({has_less/len(both_rev)*100:.1f}%)')
print(f'same:           {same:,} ({same/len(both_rev)*100:.1f}%)')

# Top discrepancies
print()
print('=== Top 10 Largest Positive Ratio Discrepancies ===')
both_rev['ratio_diff'] = (both_rev['positive_ratio_f'] - both_rev['positive_ratio_s']).abs()
top_disc = both_rev.nlargest(10, 'ratio_diff')[['name','positive_ratio_f','positive_ratio_s','total_reviews_f','total_reviews_s','ratio_diff']]
print(top_disc.to_string())

# Summary
print()
print('=== SUMMARY ===')
print(f'fronkongames: 122K games, crawled 2026-01, has Tags+Playtime+EstOwners')
print(f'srgiomanhes:  71K games, crawled 2025-01, 100% non-null, cleaner structure')
print(f'Common games: {len(common):,} ({len(common)/len(df_f)*100:.0f}% of fronko, {len(common)/len(df_s)*100:.0f}% of srgio)')
print(f'Positive ratio corr: {corr:.4f} -> highly consistent between datasets')
print(f'Price corr: {p_corr:.4f}')
