import matplotlib.pyplot as plt 
import numpy as np 

y = 100
v = 0 
g = -9.8
t = 0
dt = 0.01

tlist = []
ylist = []
vlist = []
alist = []

while y>0:
    a = g + (1.3*0.5*0.5*0.5*np.pi*v*v)/(2*5)
    v += a*dt
    y += v*dt
    t += dt
    alist.append(a)
    vlist.append(v)
    ylist.append(y)
    tlist.append(t)

plt.figure(1)
plt.xlim(-0.5, 8.5)
plt.ylim(-5, 105)
plt.xlabel('t')
plt.ylabel('y')
plt.plot(tlist, ylist)

plt.figure(2)
plt.xlim(-0.5, 8.5)
plt.ylim(-14.5, 0.5)
plt.xlabel('t')
plt.ylabel('v')
plt.plot(tlist, vlist)

plt.figure(3)
plt.xlim(-0.5, 8.5)
plt.ylim(-10.25, 0.25)
plt.xlabel('t')
plt.ylabel('a')
plt.plot(tlist, alist)
plt.show()