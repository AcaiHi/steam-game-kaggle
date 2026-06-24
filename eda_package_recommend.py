import pandas as pd
import numpy as np
import ast
import matplotlib.pyplot as plt
import matplotlib.gridspec as gridspec
import warnings
warnings.filterwarnings("ignore")

df = pd.read_csv("datasets/artermiloff/games_march2025_cleaned.csv")

# --- Parse packages column ---
def parse_packages(val):
    if pd.isna(val) or val == "[]":
        return 0, []
    try:
        pkgs = ast.literal_eval(val)
        prices = []
        count = 0
        for pkg in pkgs:
            subs = pkg.get("subs", [])
            count += len(subs)
            for s in subs:
                p = s.get("price", None)
                if p is not None:
                    prices.append(float(p))
        return count, prices
    except Exception:
        return 0, []

parsed = df["packages"].apply(parse_packages)
df["pkg_count"] = parsed.apply(lambda x: x[0])
df["pkg_prices"] = parsed.apply(lambda x: x[1])
df["pkg_price_avg"] = df["pkg_prices"].apply(lambda x: np.mean(x) if len(x) > 0 else np.nan)

# --- recommendations column ---
rec = pd.to_numeric(df["recommendations"], errors="coerce").dropna()

print("=== packages ===")
print(f"pkg_count  — mean={df['pkg_count'].mean():.2f}, median={df['pkg_count'].median():.0f}, "
      f"max={df['pkg_count'].max()}, zero={( df['pkg_count']==0).sum()}")
print(df["pkg_count"].describe().round(2))

print("\n=== pkg_price_avg (USD) ===")
print(df["pkg_price_avg"].describe().round(2))
valid_price = df["pkg_price_avg"].dropna()
print(f"free (price=0):  {(valid_price == 0).sum()}  ({(valid_price == 0).mean()*100:.1f}%)")
print(f"paid (price>0):  {(valid_price > 0).sum()}  ({(valid_price > 0).mean()*100:.1f}%)")

print("\n=== recommendations ===")
print(rec.describe().round(2))
print(f"zero recs: {(rec==0).sum()} ({(rec==0).mean()*100:.1f}%)")

# ------------------------------------------------------------------ plots ---
fig = plt.figure(figsize=(16, 12))
fig.suptitle("Steam Dataset — Package & Recommendation Distribution", fontsize=15, fontweight="bold", y=0.98)
gs = gridspec.GridSpec(2, 3, figure=fig, hspace=0.45, wspace=0.35)

# 1. pkg_count distribution (capped at 20 for readability)
ax1 = fig.add_subplot(gs[0, 0])
cap = 20
cnt_cap = df["pkg_count"].clip(upper=cap)
cnt_cap.plot(kind="hist", bins=range(0, cap+2), ax=ax1, color="#4C72B0", edgecolor="white", rwidth=0.85)
ax1.set_title("Package Count per Game\n(capped at 20)")
ax1.set_xlabel("# packages (sub-items)")
ax1.set_ylabel("# games")
ax1.axvline(df["pkg_count"].median(), color="red", ls="--", label=f"median={df['pkg_count'].median():.0f}")
ax1.legend(fontsize=8)

# 2. pkg_count value_counts top-15
ax2 = fig.add_subplot(gs[0, 1])
vc = df["pkg_count"].value_counts().sort_index().head(15)
vc.plot(kind="bar", ax=ax2, color="#4C72B0", edgecolor="white")
ax2.set_title("Package Count — Top 15 Values")
ax2.set_xlabel("# packages")
ax2.set_ylabel("# games")
ax2.tick_params(axis="x", rotation=0)

# 3. pkg_price_avg histogram (paid only, capped at 100)
ax3 = fig.add_subplot(gs[0, 2])
paid = valid_price[valid_price > 0].clip(upper=100)
paid.plot(kind="hist", bins=30, ax=ax3, color="#DD8452", edgecolor="white")
ax3.set_title("Avg Package Price (paid games)\n(capped at $100)")
ax3.set_xlabel("avg price (USD)")
ax3.set_ylabel("# games")
ax3.axvline(valid_price[valid_price > 0].median(), color="red", ls="--",
            label=f"median=${valid_price[valid_price>0].median():.2f}")
ax3.legend(fontsize=8)

# 4. Free vs Paid pie
ax4 = fig.add_subplot(gs[1, 0])
n_free = (valid_price == 0).sum()
n_paid = (valid_price > 0).sum()
n_na   = df["pkg_price_avg"].isna().sum()
ax4.pie([n_free, n_paid, n_na],
        labels=[f"Free\n({n_free})", f"Paid\n({n_paid})", f"No pkg\n({n_na})"],
        autopct="%1.1f%%", startangle=90,
        colors=["#55A868", "#DD8452", "#CCCCCC"])
ax4.set_title("Free / Paid / No Package")

# 5. recommendations histogram (log scale)
ax5 = fig.add_subplot(gs[1, 1])
rec_nonzero = rec[rec > 0]
ax5.hist(np.log10(rec_nonzero + 1), bins=50, color="#C44E52", edgecolor="white")
ax5.set_title("Recommendations (log10 scale)\nexcluding 0")
ax5.set_xlabel("log10(recommendations + 1)")
ax5.set_ylabel("# games")
ticks = [0, 1, 2, 3, 4, 5, 6]
ax5.set_xticks(ticks)
ax5.set_xticklabels([f"10^{t}" for t in ticks], fontsize=7)

# 6. recommendations boxplot + percentile annotation
ax6 = fig.add_subplot(gs[1, 2])
ax6.boxplot(np.log10(rec_nonzero + 1), vert=True, patch_artist=True,
            boxprops=dict(facecolor="#C44E52", alpha=0.6),
            medianprops=dict(color="black", lw=2))
ax6.set_title("Recommendations Boxplot\n(log10, non-zero)")
ax6.set_ylabel("log10(recommendations + 1)")
ax6.set_xticks([])
pcts = [25, 50, 75, 90, 99]
for p in pcts:
    val = np.percentile(rec_nonzero, p)
    ax6.annotate(f"p{p}={val:,.0f}", xy=(1.08, np.log10(val+1)),
                 fontsize=7, color="#333333")

plt.savefig("eda_package_recommend.png", dpi=150, bbox_inches="tight")
print("\nSaved: eda_package_recommend.png")
plt.show()
