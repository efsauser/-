import numpy as np
import matplotlib.pyplot as plt
import matplotlib.gridspec as gridspec
from tqdm import tqdm

#數值與座標設定
PI, ep0, la, sigma0 = np.pi, 8.85*10**-12, 4, 4
sidecut, ringcut, radiuscut = 30, 30, 10 
#地圖邊分割數, 圓環分割數, 半徑分割數
t = np.linspace(0, 2*PI, ringcut)
x = np.linspace(-10, 10, sidecut)
y = np.linspace(-10, 10, sidecut)

#計算電場向量場
def field(Rx, Ry, R, shape):
    num = 0
    Ex = np.zeros([sidecut, sidecut])
    Ey = np.zeros([sidecut, sidecut])
    times, total = 0, sidecut #進度條
    if shape == "ring":
        dtheta = 2*PI/ringcut
        ratio = [R]
        dq = la*R*dtheta
    if shape == "surface":
        ratio = [x/radiuscut for x in range(radiuscut, -1, -1)]
    for row in range(sidecut):
        for column in range(sidecut):
            for radius in ratio:
                Rx*=radius
                Ry*=radius
                for seg in range(ringcut):
                    num += 1
                    dEx = x[column]-Rx[seg]
                    dEy = y[row]-Ry[seg]
                    r = np.sqrt( (x[column]-Rx[seg])**2+(y[row]-Ry[seg])**2)
                    if shape == "ring":
                        Ex[row][column] += dEx*dq/(4*PI*ep0*r**3)
                        Ey[row][column] += dEy*dq/(4*PI*ep0*r**3)
                    if shape == "surface":    
                        Ex[row][column] += dEx*sigma0*radius/(4*PI*ep0*r**3)
                        Ey[row][column] += dEy*sigma0*radius/(4*PI*ep0*r**3)
        times += 1
        print('\r'+'loading...%d%%[%s%s]'%(int(times/total*100),'█'*int(times*20/total),' '*(20-int(times*20/total))), end='')
    return  Ex, Ey

#繪製
Ex1, Ey1 = field(1*np.cos(t), 1*np.sin(t), R=1, shape="ring")
Ex2, Ey2 = field(0*np.cos(t), 1*np.sin(t), R=1, shape="ring")
Ex3, Ey3 = field(1*np.cos(t), 0*np.sin(t), R=1, shape="ring")
Ex4, Ey4 = field(5*np.cos(t), 5*np.sin(t), R=5, shape="surface")
Ex5, Ey5 = field(0*np.cos(t), 5*np.sin(t), R=5, shape="surface")
Ex6, Ey6 = field(5*np.cos(t), 0*np.sin(t), R=5, shape="surface")

fig1 = plt.figure(figsize =(17, 5))
ax1 = fig1.add_subplot(131)
ax1.xaxis.set_ticks(np.linspace(-10, 10, 11))
ax1.yaxis.set_ticks(np.linspace(-10, 10, 11))
ax1.plot(1*np.cos(t), 1*np.sin(t), color='r')
ax1.streamplot(x, y, Ex1, Ey1, color='k', density=1, linewidth=0.7)

ax2 = fig1.add_subplot(132)
ax2.xaxis.set_ticks(np.linspace(-10, 10, 11))
ax2.yaxis.set_ticks(np.linspace(-10, 10, 11))
ax2.plot(0*np.cos(t), 1*np.sin(t), color='r')
ax2.streamplot(x, y, Ex2, Ey2, color='k', density=1, linewidth=0.7)

ax3 = fig1.add_subplot(133)
ax3.xaxis.set_ticks(np.linspace(-10, 10, 11))
ax3.yaxis.set_ticks(np.linspace(-10, 10, 11))
ax3.plot(1*np.cos(t), 0*np.sin(t), color='r')
ax3.streamplot(x, y, Ex3, Ey3, color='k', density=1, linewidth=0.7)

fig2 = plt.figure(figsize =(17, 5))
ax4 = fig2.add_subplot(131)
ax4.xaxis.set_ticks(np.linspace(-10, 10, 11))
ax4.yaxis.set_ticks(np.linspace(-10, 10, 11))
ax4.plot(5*np.cos(t), 5*np.sin(t), color='r')
ax4.streamplot(x, y, Ex4, Ey4, color='k', density=1, linewidth=0.7)

ax5 = fig2.add_subplot(132)
ax5.xaxis.set_ticks(np.linspace(-10, 10, 11))
ax5.yaxis.set_ticks(np.linspace(-10, 10, 11))
ax5.plot(0*np.cos(t), 5*np.sin(t), color='r')
ax5.streamplot(x, y, Ex5, Ey5, color='k', density=1, linewidth=0.7)

ax6 = fig2.add_subplot(133)
ax6.xaxis.set_ticks(np.linspace(-10, 10, 11))
ax6.yaxis.set_ticks(np.linspace(-10, 10, 11))
ax6.plot(5*np.cos(t), 0*np.sin(t), color='r')
ax6.streamplot(x, y, Ex6, Ey6, color='k', density=1, linewidth=0.7)

plt.show()