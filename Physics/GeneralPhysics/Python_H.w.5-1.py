import numpy as np 
import matplotlib.pyplot as plt 

m = 660*10**3
W = 2*np.pi/6.8

def find_cri(b):
    beta = b/(2*m) 
    w = (W**2-beta**2)**0.5
    t = np.linspace(0, 6.8, 100)
    jlist = 0.2*np.e**(-beta*t)*((w**3-3*w*beta**2)*np.sin(w*t)
                                -(beta**3-3*beta*w**2)*np.cos(w*t))
    max = jlist[0]
    for i in range(0, len(jlist)):
        if abs(jlist[i])>abs(max) :
            max = abs(jlist[i])
    return max

x_axis = np.linspace(900000, 1100000, 100)
y_axis = np.linspace(0.05, 0.05, 100) #y = 0.05 black line
plt.plot(x_axis, y_axis, color='black')

blist, jlist = [], []
needed_b = 0
for b in range(900000, 1100000, 100):
    blist.append(b)
    jlist.append(find_cri(b))
    if 0.05-find_cri(b) < 0.01 and needed_b == 0:
        needed_b = b
plt.ylabel('j')
plt.xlabel('damping coefficient')
plt.title('j-b plot')
plt.plot(blist, jlist)

print("The needed damping coefficient is "+str(needed_b))

plt.show()