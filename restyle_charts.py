import matplotlib
matplotlib.rcParams['font.family'] = 'Microsoft JhengHei'
matplotlib.rcParams['axes.unicode_minus'] = False
import pandas as pd, numpy as np, matplotlib.pyplot as plt
import matplotlib.gridspec as gridspec
import matplotlib.patches as mpatches

BG_YELLOW = '#FFE033'
BG_PANEL  = '#FFF176'
BORDER    = '#1a1a1a'
C1, C1L   = '#1565C0', '#90CAF9'
C2, C2L   = '#1B5E20', '#A5D6A7'
C3, C3L   = '#E65100', '#FFCC80'
CYAN_BTN  = '#00BCD4'
TEXT_C    = '#1a1a1a'

def pixel_style(ax):
    ax.set_facecolor(BG_PANEL)
    for sp in ax.spines.values():
        sp.set_edgecolor(BORDER); sp.set_linewidth(2.5)
    ax.tick_params(colors=TEXT_C, labelsize=8, width=1.5, length=4)
    ax.xaxis.label.set_color(TEXT_C)
    ax.yaxis.label.set_color(TEXT_C)
    ax.title.set_color(TEXT_C)
    ax.grid(color=BORDER, linewidth=0.8, alpha=0.15, zorder=0)

df = pd.read_csv('datasets/artermiloff/games_march2025_cleaned.csv', low_memory=False)
s1 = df[df['average_playtime_forever'] > 0]['average_playtime_forever']
s2 = df[df['peak_ccu'] > 0]['peak_ccu']
s3 = df[df['pct_pos_total'] >= 0]['pct_pos_total']

edges  = [0, 30, 50, 60, 70, 80, 90, 100]
labels = ['0-30','30-50','50-60','60-70','70-80','80-90','90-100']

# ── Fig 3: raw distribution ──────────────────────────────────────
fig3 = plt.figure(figsize=(15, 5.5))
fig3.patch.set_facecolor(BG_YELLOW)
gs3  = gridspec.GridSpec(1, 3, figure=fig3, wspace=0.40)

ax = fig3.add_subplot(gs3[0])
p99_1 = np.percentile(s1, 99)
q1, q3 = np.percentile(s1, 25), np.percentile(s1, 75)
ax.hist(s1[s1 <= p99_1], bins=80, color=C1, alpha=0.9, edgecolor=BORDER, linewidth=0.8, zorder=2)
ax.set_xlim(0, p99_1)
ax.axvspan(q1, q3, color=C1L, alpha=0.55, zorder=0)
ax.axvline(s1.median(), color=BORDER, lw=2, ls='--', zorder=3, label=f'中位數 {s1.median():.0f} 分鐘')
ax.axvline(s1.mean(),   color=C1,     lw=2, ls=':',  zorder=3, label=f'平均數 {s1.mean():.0f} 分鐘')
ax.set_xlabel('分鐘（裁切至 P99）', fontsize=9, fontweight='bold')
ax.set_ylabel('遊戲數', fontsize=9, fontweight='bold')
ax.set_title('平均遊玩時長（歷史）\n（非零值，n=8,010）', fontsize=10, fontweight='bold', pad=8)
ax.legend(fontsize=7.5, facecolor=BG_PANEL, edgecolor=BORDER, labelcolor=TEXT_C)
ax.text(0.98, 0.97, f'IQR={q3-q1:.0f} 分鐘\n異常值 12.4%\nP99={p99_1:.0f} 分鐘',
        transform=ax.transAxes, ha='right', va='top', fontsize=8,
        color=C1, fontweight='bold',
        bbox=dict(boxstyle='square,pad=0.4', fc=C1L, ec=C1, lw=2))
pixel_style(ax)

ax2 = fig3.add_subplot(gs3[1])
p99_2 = np.percentile(s2, 99)
q1, q3 = np.percentile(s2, 25), np.percentile(s2, 75)
ax2.hist(s2[s2 <= p99_2], bins=80, color=C2, alpha=0.9, edgecolor=BORDER, linewidth=0.8, zorder=2)
ax2.set_xlim(0, p99_2)
ax2.axvspan(q1, q3, color=C2L, alpha=0.55, zorder=0)
ax2.axvline(s2.median(), color=BORDER, lw=2, ls='--', zorder=3, label=f'中位數 {s2.median():.0f}')
ax2.axvline(s2.mean(),   color=C2,     lw=2, ls=':',  zorder=3, label=f'平均數 {s2.mean():.0f}')
ax2.set_xlabel('峰值同時在線人數（裁切至 P99）', fontsize=9, fontweight='bold')
ax2.set_ylabel('遊戲數', fontsize=9, fontweight='bold')
ax2.set_title('峰值同時在線人數\n（非零值，n=18,920）', fontsize=10, fontweight='bold', pad=8)
ax2.legend(fontsize=7.5, facecolor=BG_PANEL, edgecolor=BORDER, labelcolor=TEXT_C)
ax2.text(0.98, 0.97, f'IQR={q3-q1:.0f}\n異常值 16.8%\nP99={p99_2:.0f}',
        transform=ax2.transAxes, ha='right', va='top', fontsize=8,
        color=C2, fontweight='bold',
        bbox=dict(boxstyle='square,pad=0.4', fc=C2L, ec=C2, lw=2))
pixel_style(ax2)

ax3 = fig3.add_subplot(gs3[2])
counts = [((s3 > edges[i]) & (s3 <= edges[i+1])).sum() for i in range(len(labels))]
cb = [C3 if edges[i] >= 70 else '#9E9E9E' for i in range(len(labels))]
bars = ax3.bar(labels, counts, color=cb, alpha=0.9, edgecolor=BORDER, linewidth=1.2, width=0.7)
for bar, cnt in zip(bars, counts):
    ax3.text(bar.get_x()+bar.get_width()/2, bar.get_height()+150,
             f'{cnt/len(s3)*100:.1f}%', ha='center', va='bottom',
             fontsize=7.5, color=TEXT_C, fontweight='bold')
ax3.set_xlabel('好評率 (%)', fontsize=9, fontweight='bold')
ax3.set_ylabel('遊戲數', fontsize=9, fontweight='bold')
ax3.set_title('整體好評率\n（有效評分，n=53,199）', fontsize=10, fontweight='bold', pad=8)
ax3.tick_params(axis='x', rotation=30)
q1, q3 = np.percentile(s3, 25), np.percentile(s3, 75)
ax3.text(0.98, 0.97, f'IQR={q3-q1:.0f}%  [Q1={q1:.0f}, Q3={q3:.0f}]\n異常值 2.0%',
        transform=ax3.transAxes, ha='right', va='top', fontsize=8,
        color=C3, fontweight='bold',
        bbox=dict(boxstyle='square,pad=0.4', fc=C3L, ec=C3, lw=2))
pixel_style(ax3)

fig3.suptitle('Steam 遊戲特徵分布分析', fontsize=14,
              fontweight='bold', color=TEXT_C, y=1.02,
              bbox=dict(boxstyle='square,pad=0.5', fc=CYAN_BTN, ec=BORDER, lw=2.5))
fig3.patch.set_alpha(0)
plt.savefig('feature_distribution_light.png', dpi=150, bbox_inches='tight', transparent=True)
print('Fig3 saved')
plt.close()

# ── Fig 2: log distribution ──────────────────────────────────────
fig2 = plt.figure(figsize=(15, 5.5))
fig2.patch.set_facecolor(BG_YELLOW)
gs2  = gridspec.GridSpec(1, 3, figure=fig2, wspace=0.40)

ax = fig2.add_subplot(gs2[0])
q1, q3 = np.percentile(s1, 25), np.percentile(s1, 75)
log_bins = np.logspace(np.log10(1), np.log10(s1.max()), 45)
ax.hist(s1, bins=log_bins, color=C1, alpha=0.9, edgecolor=BORDER, linewidth=0.6, zorder=2)
ax.axvspan(q1, q3, color=C1L, alpha=0.55, zorder=0)
ax.axvline(q1, color=C1, lw=1.2, ls='--', alpha=0.7)
ax.axvline(q3, color=C1, lw=1.2, ls='--', alpha=0.7)
ax.axvline(s1.median(), color=BORDER, lw=2, ls='--', zorder=3, label=f'中位數 {s1.median():.0f} 分鐘')
ax.axvline(s1.mean(),   color=C1,     lw=2, ls=':',  zorder=3, label=f'平均數 {s1.mean():.0f} 分鐘')
ax.set_xscale('log')
ax.set_xlabel('分鐘（對數尺度）', fontsize=9, fontweight='bold')
ax.set_ylabel('遊戲數', fontsize=9, fontweight='bold')
ax.set_title('平均遊玩時長（歷史）\n（非零值，n=8,010）', fontsize=10, fontweight='bold', pad=8)
ax.legend(fontsize=7.5, facecolor=BG_PANEL, edgecolor=BORDER, labelcolor=TEXT_C)
ax.text(0.98, 0.97, f'IQR={q3-q1:.0f} 分鐘\n異常值 12.4%',
        transform=ax.transAxes, ha='right', va='top', fontsize=8,
        color=C1, fontweight='bold',
        bbox=dict(boxstyle='square,pad=0.4', fc=C1L, ec=C1, lw=2))
pixel_style(ax)

ax2 = fig2.add_subplot(gs2[1])
q1, q3 = np.percentile(s2, 25), np.percentile(s2, 75)
log_bins2 = np.logspace(0, np.log10(s2.max()), 35)
ax2.hist(s2, bins=log_bins2, color=C2, alpha=0.9, edgecolor=BORDER, linewidth=0.6, zorder=2)
ax2.axvspan(q1, q3, color=C2L, alpha=0.55, zorder=0)
ax2.axvline(q1, color=C2, lw=1.2, ls='--', alpha=0.7)
ax2.axvline(q3, color=C2, lw=1.2, ls='--', alpha=0.7)
ax2.axvline(s2.median(), color=BORDER, lw=2, ls='--', zorder=3, label=f'中位數 {s2.median():.0f}')
ax2.axvline(s2.mean(),   color=C2,     lw=2, ls=':',  zorder=3, label=f'平均數 {s2.mean():.0f}')
ax2.set_xscale('log')
ax2.set_yscale('log')
ax2.set_xlabel('峰值同時在線人數（對數尺度）', fontsize=9, fontweight='bold')
ax2.set_ylabel('遊戲數（對數尺度）', fontsize=9, fontweight='bold')
ax2.set_title('峰值同時在線人數\n（非零值，n=18,920）', fontsize=10, fontweight='bold', pad=8)
ax2.legend(fontsize=7.5, facecolor=BG_PANEL, edgecolor=BORDER, labelcolor=TEXT_C)
ax2.text(0.98, 0.97, f'IQR={q3-q1:.0f}\n異常值 16.8%',
        transform=ax2.transAxes, ha='right', va='top', fontsize=8,
        color=C2, fontweight='bold',
        bbox=dict(boxstyle='square,pad=0.4', fc=C2L, ec=C2, lw=2))
pixel_style(ax2)
ax2.grid(color=BORDER, linewidth=0.8, alpha=0.15, which='both')

ax3 = fig2.add_subplot(gs2[2])
counts = [((s3 > edges[i]) & (s3 <= edges[i+1])).sum() for i in range(len(labels))]
cb = [C3 if edges[i] >= 70 else '#9E9E9E' for i in range(len(labels))]
bars = ax3.bar(labels, counts, color=cb, alpha=0.9, edgecolor=BORDER, linewidth=1.2, width=0.7)
for bar, cnt in zip(bars, counts):
    ax3.text(bar.get_x()+bar.get_width()/2, bar.get_height()+150,
             f'{cnt/len(s3)*100:.1f}%', ha='center', va='bottom',
             fontsize=7.5, color=TEXT_C, fontweight='bold')
ax3.set_xlabel('好評率 (%)', fontsize=9, fontweight='bold')
ax3.set_ylabel('遊戲數', fontsize=9, fontweight='bold')
ax3.set_title('整體好評率\n（有效評分，n=53,199）', fontsize=10, fontweight='bold', pad=8)
ax3.tick_params(axis='x', rotation=30)
q1, q3 = np.percentile(s3, 25), np.percentile(s3, 75)
ax3.text(0.98, 0.97, f'IQR={q3-q1:.0f}%  [Q1={q1:.0f}, Q3={q3:.0f}]\n異常值 2.0%',
        transform=ax3.transAxes, ha='right', va='top', fontsize=8,
        color=C3, fontweight='bold',
        bbox=dict(boxstyle='square,pad=0.4', fc=C3L, ec=C3, lw=2))
pixel_style(ax3)

fig2.suptitle('Steam 遊戲特徵分布分析（對數尺度）', fontsize=14,
              fontweight='bold', color=TEXT_C, y=1.02,
              bbox=dict(boxstyle='square,pad=0.5', fc=CYAN_BTN, ec=BORDER, lw=2.5))
fig2.patch.set_alpha(0)
plt.savefig('feature_distribution_log_light.png', dpi=150, bbox_inches='tight', transparent=True)
print('Fig2 saved')
plt.close()

# ── Fig 1: quadrant ──────────────────────────────────────────────
d = df[(df['average_playtime_forever'] > 0) &
       (df['peak_ccu'] > 0) &
       (df['pct_pos_total'] >= 0)][['average_playtime_forever','peak_ccu','pct_pos_total']].copy()
d['log_playtime'] = np.log1p(d['average_playtime_forever'])
d['log_ccu']      = np.log1p(d['peak_ccu'])
n = len(d)

C_HH = '#1565C0'
C_HL = '#E65100'
C_LL = '#9E9E9E'

fig1 = plt.figure(figsize=(18, 6))
fig1.patch.set_facecolor(BG_YELLOW)
gs1  = gridspec.GridSpec(1, 3, figure=fig1, wspace=0.40)

def qscatter(ax, xcol, ycol, xlabel, ylabel, title, r):
    mx, my = d[xcol].median(), d[ycol].median()
    hh = (d[xcol] >= mx) & (d[ycol] >= my)
    hl = (d[xcol] >= mx) & (d[ycol] <  my)
    lh = (d[xcol] <  mx) & (d[ycol] >= my)
    ll = (d[xcol] <  mx) & (d[ycol] <  my)
    for mask, c in [(ll, C_LL), (lh, C_HL), (hl, C_HL), (hh, C_HH)]:
        ax.scatter(d[mask][xcol], d[mask][ycol], s=6, alpha=0.3, color=c, zorder=2)
    ax.axvline(mx, color=BORDER, lw=2, ls='--', zorder=3, alpha=0.5)
    ax.axhline(my, color=BORDER, lw=2, ls='--', zorder=3, alpha=0.5)
    xlim = ax.get_xlim(); ylim = ax.get_ylim()
    xr = xlim[1]-xlim[0]; yr = ylim[1]-ylim[0]
    def lbl(mask, text, color, ha, va, xo, yo):
        cnt = mask.sum(); pct = cnt/n*100
        ax.text(xlim[0]+xo*xr, ylim[0]+yo*yr,
                f'{text}\n{cnt:,} 款 ({pct:.1f}%)',
                ha=ha, va=va, fontsize=8, color=color, fontweight='bold', zorder=6,
                bbox=dict(boxstyle='square,pad=0.35', fc='white', ec=color, lw=2, alpha=0.95))
    lbl(hh, '高 & 高\n（符合預期）', C_HH, 'right', 'top',    0.97, 0.97)
    lbl(hl, 'X 高、Y 低\n（反預期）', C_HL, 'right', 'bottom', 0.97, 0.03)
    lbl(lh, 'X 低、Y 高\n（反預期）', C_HL, 'left',  'top',    0.03, 0.97)
    lbl(ll, '低 & 低',                C_LL, 'left',  'bottom', 0.03, 0.03)
    ax.set_xlabel(xlabel, fontsize=9, fontweight='bold')
    ax.set_ylabel(ylabel, fontsize=9, fontweight='bold')
    ax.set_title(f'{title}\nSpearman r = {r}', fontsize=10, fontweight='bold', pad=8)
    pixel_style(ax)

qscatter(fig1.add_subplot(gs1[0]), 'log_playtime', 'log_ccu',
         'log（遊玩時長）', 'log（峰值在線人數）', '遊玩時長 × 峰值在線人數', '0.513')
qscatter(fig1.add_subplot(gs1[1]), 'log_ccu', 'pct_pos_total',
         'log（峰值在線人數）', '整體好評率 (%)', '峰值在線人數 × 好評率', '0.202')
qscatter(fig1.add_subplot(gs1[2]), 'log_playtime', 'pct_pos_total',
         'log（遊玩時長）', '整體好評率 (%)', '遊玩時長 × 好評率', '0.063')

leg = [mpatches.Patch(color=C_HH, label='符合預期'),
       mpatches.Patch(color=C_HL, label='反預期'),
       mpatches.Patch(color=C_LL, label='低 & 低')]
fig1.legend(handles=leg, loc='lower center', ncol=3, fontsize=9,
            facecolor=BG_PANEL, edgecolor=BORDER, labelcolor=TEXT_C,
            bbox_to_anchor=(0.5, -0.07))
fig1.suptitle('四象限分析：預期模式 vs 反預期模式  (n=5,443)', fontsize=13,
              fontweight='bold', color=TEXT_C, y=1.02,
              bbox=dict(boxstyle='square,pad=0.5', fc=CYAN_BTN, ec=BORDER, lw=2.5))
fig1.patch.set_alpha(0)
plt.savefig('feature_quadrant_all.png', dpi=150, bbox_inches='tight', transparent=True)
print('Fig1 saved')
plt.close()
