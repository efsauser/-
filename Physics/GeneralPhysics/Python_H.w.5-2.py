import numpy as np 
import matplotlib.pyplot as plt 

m = 660*10**3
W = 2*np.pi/6.8

b = 980000
beta = b/(2*m)
w = (W**2-beta**2)**0.5
t = np.linspace(0, 6.8, 10000)
x = 0.2*np.e**(-beta*t)*np.cos(w*t)
v = -0.2*np.e**(-beta*t)*(beta*np.cos(w*t)+w*np.sin(w*t))

plt.xlabel('v')
plt.ylabel('x')
plt.title('x-v plot')
plt.plot(v, x)

print("The plot will guadually go to the origin.")

plt.show()