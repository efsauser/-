import numpy as np
import matplotlib.pyplot as plt
from matplotlib.collections import PolyCollection
from tqdm import tqdm
side, sidecut, alpha, plane, N = 10, 41, np.pi/12, 21, 50 #plane=sidecut/2, 將空間切一半的平面
x = np.linspace(-side, side, sidecut)
y = np.linspace(-side, side, sidecut)
z = np.linspace(-side, side, sidecut)
x, y, z = np.meshgrid(x, y, z)
V = np.zeros([sidecut, sidecut, sidecut])

times, total = 0, sidecut**3#進度條
for i in range(sidecut):
    for j in range(sidecut):
        for k in range(sidecut):
            if z[i][j][k] >= ((x[i][j][k])**2+(y[i][j][k])**2)**0.5/np.tan(alpha):
                V[i][j][k] = 10
            times += 1
            print('\r'+'loading...%.2f%%[%s%s]...1/2...'%(float(times/total*100),'█'*int(times*20/total),' '*(20-int(times*20/total))), end='')

times, total = 0, N*(sidecut-2)**3#進度條
for loop in range(N):
    for i in range(1, sidecut-1):
        for j in range(1, sidecut-1):
            for k in range(1, sidecut-1):
                if z[i][j][k] >= (((x[i][j][k])**2+(y[i][j][k])**2)**0.5)/np.tan(alpha) :
                    continue
                V[i][j][k] = (V[i+1][j][k]+V[i-1][j][k]+V[i][j+1][k]+V[i][j-1][k]+V[i][j][k+1]+V[i][j][k-1])/6
                times += 1
                print('\r'+'loading...%.2f%%[%s%s]...2/2...'%(float(times/total*100),'█'*int(times*20/total),' '*(20-int(times*20/total))), end='')

Vxy = V.transpose(2, 0, 1)
Vxz = V.transpose(1, 2, 0)
Exy = np.gradient(-1*Vxy[plane])
Exz = np.gradient(-1*Vxz[plane])

xp = np.linspace(-side, side, sidecut)
yp = np.linspace(-side, side, sidecut)
zp = np.linspace(-side, side, sidecut)
XY_x, XY_y = np.meshgrid(xp, yp)
XZ_x, XZ_z = np.meshgrid(xp, zp)

fig1, ax1 = plt.subplots(1, 2, figsize=(12, 5))
cp1 = ax1[0].contourf(XY_x, XY_y, Vxy[plane], 100)
cp2 = ax1[1].contourf(XZ_x, XZ_z, Vxz[plane], 100)
fig1.colorbar(cp1, ax=ax1[0])
fig1.colorbar(cp1, ax=ax1[1])
ax1[0].set_title('x-y potential')
ax1[1].set_title('x-z potential')

fig2, ax2 = plt.subplots(1, 2, figsize=(12, 5))
ax2[0].streamplot(XY_x, XY_y, Exy[1], Exy[0], linewidth = 0.7, density = 1, arrowsize = 1)
ax2[1].streamplot(XZ_x, XZ_z, Exz[1], Exz[0], linewidth = 0.7, density = 1, arrowsize = 1)
ax2[1].add_collection(PolyCollection([[[0, 0], [-side*np.tan(alpha), side], [side*np.tan(alpha), side]]], linewidth=0.1, facecolor="gray"))
ax2[0].set_title('x-y electric field')
ax2[1].set_title('x-z electric field')

plt.show()