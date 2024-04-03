import numpy as np
import matplotlib.pyplot as plt

PI = np.pi
x = np.linspace(0, 2*PI, 200)

fig1, ax1 = plt.subplots(1, 2, figsize=(12, 5))
y = np.sin(x)
for k in range(2, 15, 1):
    y += np.sin(k*x)
plt.xlim(0, 2*PI)
ax1[0].plot(x, y)

y = np.sin(x)
for k in range(3, 15, 2):
    y += np.sin(k*x)
plt.xlim(0, 2*PI)
ax1[1].plot(x, y)


plt.figure(2)
P = 2*PI
y = (4/PI)*(1/1)*np.sin(2*1*PI*x/P)
for n in range(3, 15, 2):
    y += (4/PI)*(1/n)*np.sin(2*n*PI*x/P)
    plt.plot(x, y)
plt.xlim(0, 2*PI)
plt.ylim(-1.5, 1.5)

square = []
for i in range(0, 100):
    square.append(1)
for i in range(0, 100):
    square.append(-1)
plt.plot(x, square, color= "black")

plt.xlabel('time(s)')
plt.ylabel('Amplitude(V)')

plt.show()