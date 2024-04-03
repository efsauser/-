import numpy as np 
import matplotlib.pyplot as plt 
import matplotlib.animation as ani

# PI = np.pi
# theta, omega, alpha, L, t_rod, t_ball, dt, g, ay, vy, y = 0, 0, 0, 0.3, 0, 0, 0.001, 9.8, 0 ,0, 0
# t_rodlist, t_balllist, xlist, ylist ,vlist, alist = [], [], [], [], [], []
# y_balllist = []

def rod(L):
    PI = np.pi
    theta, omega, alpha, L, t_rod, t_ball, dt, g, ay, vy, y = 0, 0, 0, L, 0, 0, 0.001, 9.8, 0 ,0, 0
    t_rodlist, ylist ,vlist, alist = [], [], [], []
    y_balllist = []
    while theta <= PI/2:
        alpha = (3*g*np.cos(theta))/(2*L)
        omega += alpha*dt
        theta += omega*dt
        ay = L*omega**2*np.sin(theta)-L*alpha*np.cos(theta)
        vy = -L*omega*np.cos(theta)
        y = -L*np.sin(theta)
        t_rod += dt 
        t_rodlist.append(t_rod)
        alist.append(ay)
        vlist.append(vy)
        ylist.append(y)
    return t_rod, t_rodlist, ylist ,vlist, alist

t_rod, t_rodlist, ylist ,vlist, alist = rod(0.3)

H, dt, g, ay, vy, y = 0.3, 0.001, 9.8, 0, 0, 0
t_ball = 0
t_balllist, y_balllist = [], []
while H >= 0:
    ay = g
    vy += ay*dt
    H -= vy*dt
    t_ball += dt
    t_balllist.append(t_ball)
    y_balllist.append(H-0.3)

plt.figure(1)
plt.title('y_rod-t and y_ball-t')
plt.plot(t_rodlist, ylist, label='rod')
plt.plot(t_balllist, y_balllist, label='ball')
plt.legend(loc='upper right')

plt.figure(2)
plt.title('vy_rod-t')
plt.plot(t_rodlist, vlist)

plt.figure(3)
plt.title('ay_rod-t')
plt.plot(t_rodlist, alist)


t_rod2, t_rodlist, ylist ,vlist, alist = rod(0.5)

print("(a)The time of the rod is "+str(t_rod))
print("(b1)The time of the ball is "+str(t_ball)+"\n(b2)the ratio of them is "+str(t_rod/t_ball))
print("(c)The time of rod that L=0.5m is "+str(t_rod2)+"\n so the length L affecting the ratio")

plt.show()
