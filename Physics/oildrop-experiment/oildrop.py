import math as m
import numpy as np
import matplotlib.pyplot as plt
from tqdm import tqdm
from matplotlib.font_manager import *

myfont = FontProperties(fname='C:/Windows/Fonts/msjh.ttc')#字型設定
#數據集
data17 = [1064,1130,1185,1298,1301,1311,1359,1487,1551,1575,1601,1632,1635,1642,1690,1766,1826,1860,1962,2073,2105,2267,2493,2567,2695,2753,2794,2917,3215,3220,3256,3311,3428,3459,3495,3802,3964,4695,4733,4785,4842,5098,5155,5767,6165,6514,6753,6841,6874,12812]
data16 = [580,665,964,1012,1069,1194,1352,1404,1482,1608,1629,1640,1671,1771,1881,1927,1950,1973,2002,2002,2018,2062,2069,2074,2160,2205,2349,2349,2517,2650,2707,2892,3069,3080,3127,3268,3381,3585,3626,3833,4021,4249,4725,4860,5130,5209,5559,5894,7257,7561]
data8 = [347,672,685,703,957,1038,1134,1208,1309,1318,1318,1320,1374,1546,1588,1628,1656,1714,1725,1734,1760,1835,1896,1898,1901,1910,1915,1918,1991,2026,2049,2087,2090,2320,2342,2423,2540,2575,2606,2733,2744,2858,3132,3689,3939,3985,4080,4183,4867,5476,5847,6017,6191,6802,7242]
data24 = [585,721,976,1198,1209,1211,1301,1404,1451,1452,1494,1566,1567,1610,1618,1647,1729,1734,1745,1784,1925,1933,1940,1999,2439,2766,2773,2834,2843,2880,3018,3080,3123,3132,3205,3214,3506,3594,3649,3974,4153,4386,4404,4775,5200,5705,7162,9867,11592,16528]
alldata = data17+data16+data8+data24
#變數設定
thedata = alldata
MAX = max(thedata)
start, end, interval, times = 1, 3000, 1, 0
unit = start
total = 2*(end-start)/interval#進度條
unit_axis, avg_res_axis, localx, localmin = [], [], [], []#座標設定

#擬合
while unit <= end:
    n = m.ceil(MAX/unit) #unit的最大倍數
    residual = 0 #殘差總和
    for data in thedata:
        residual += min( [abs(data-i*unit) for i in range(1, n+1)] )
        #計算距離最近水平線的殘差並加入殘差總和
    avg_res = residual/len(thedata)
    unit_axis.append(unit)
    avg_res_axis.append(avg_res)
    unit += interval
    times += 1
    print('\r'+'[Progress]:[%s%s]%.2f%%;'%('█'*int(times*20/total),''*(20-int(times*20/total)),float(times/total*100)), end='')

#尋找相對最小值
r = 50
for i in range(r, len(unit_axis)-r, 1):
    times += 1
    print('\r'+'[Progress]:[%s%s]%.2f%%;'%('█'*int(times*20/total),''*(20-int(times*20/total)),float(times/total*100)), end='')
    if avg_res_axis[i-1]>=avg_res_axis[i] and avg_res_axis[i]<=avg_res_axis[i+1]:
        difL, difR, dif = 0, 0, 0 
        for j in range(-r, 0):
            difL += avg_res_axis[i+j]-avg_res_axis[i]
        for j in range(1, r+1):
            difR += avg_res_axis[i+j]-avg_res_axis[i]
        # for j in range(-r, r+1):
        #     dif += avg_res_axis[i+j]-avg_res_axis[i]
        if difL/r>2 and difR/r>2:
            localx.append(unit_axis[i])
            localmin.append(avg_res_axis[i])

#繪製圖形
mua, mlx = [x/1000 for x in unit_axis], [x/1000 for x in localx]
maxX, maxY = max(mlx), max(localmin)
plt.plot(mlx,localmin, color='orangered', marker='v', markersize=4, linestyle='', label='local min')
plt.plot(np.linspace(maxX, maxX), np.linspace(maxY-(avg_res_axis[-1]-avg_res_axis[1])*0.25, maxY+(avg_res_axis[-1]-avg_res_axis[1])*0.25), linewidth=1, color='orangered')
plt.annotate(s=maxX, xy =(maxX, maxY), xytext=(maxX+0.05, maxY-90))
plt.plot(mua, avg_res_axis, color='#6699FF')
plt.xlabel('擬合單位長u(10^-19)', fontproperties=myfont)
plt.ylabel('每點平均殘差D' ,fontproperties=myfont)
plt.legend(loc='upper left')
plt.show()
