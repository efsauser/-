import numpy as np 
import matplotlib.pyplot as plt 
import matplotlib.animation as ani

g, L, t, dt, la = 9.8, 1, 0, 0.001, (90-24)*np.pi/180 
r, v, we, a = np.array([1, 0, 0]), np.array([0, 0, 0]), np.array([0, 0, np.sin(la)*15*np.pi/180]), np.array([0, 0, 0])
PIx2 = 2*np.pi
T = PIx2*(L/g)**0.5

def draw(time, g, L, t, dt, la, r, v, we, a):
    rlx, rly = [], []
    while t <= time :
        a = -r*g/L-2*np.cross(we, v)
        v = v + a*dt
        r = r + v*dt
        t += dt
        rlx.append(r[0])
        rly.append(r[1])
    A = np.array([1, 0, 0])
    return rlx, rly, np.pi-np.arccos((A@r)/(np.linalg.norm(A)*np.linalg.norm(r)))

rlx, rly, dth = draw(T/2, g, L, t, dt, la, r, v, we, a)

N = int(PIx2/dth)
q =  int(PIx2/dth)

rlx, rly, dth = draw(T/2*q*N, g, L, t, dt, la, r, v, we, a)

plt.xlim(-1.3, 1.3)
plt.ylim(-1, 1)
plt.plot(rlx, rly, linewidth=0.5)

ct = np.linspace(0, PIx2, 1000)
cx = np.cos(ct)
cy = np.sin(ct)
plt.plot(cx, cy)

print("(b)time is "+str(T/2*q*N)+"sec")
print("(b)time is "+str(T/2*q*N/10**0.5)+"sec")
plt.show()
