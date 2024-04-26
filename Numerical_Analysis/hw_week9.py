import numpy as np
import matplotlib.pyplot as plt

def modified_integrate(F,x,y,yStop,h):
    def run_kut4(F,x,y,h):
        K0 = h*F(x,y)
        K1 = h*F(x + h/2.0, y + K0/2.0)
        K2 = h*F(x + h/2.0, y + K1/2.0)
        K3 = h*F(x + h, y + K2)
        return (K0 + 2.0*K1 + 2.0*K2 + K3)/6.0

    X = []
    Y = []
    X.append(x)
    Y.append(y)
    while y[0] > yStop:
        # h = min(h,xStop - x)
        y = y + run_kut4(F,x,y,h) 
        x = x+h
        X.append(x)
        Y.append(y)
    
    return np.array(X),np.array(Y)

# y = [y[0] ,y[1]=dy], F(x,y)=[dy, ddy] = [y[1], g-CD*y[1]**2/m]
# 為了作圖方便，我定義向上為正
def F(x,y):
    # H = 5000
    g = 9.80665
    CD = 0.2028
    m = 80
    F = np.zeros(2)
    F[0] = y[1]
    F[1] = -g+CD*y[1]**2/m
    return F


x = 0.0 
yStop = 0 
y = np.array([5000.0, 0.0])
h = 0.2 

X,Y = modified_integrate(F, x, y, yStop, h)
print('The falling time is' , X[-1], 'second.')
plt.plot(X,Y[:,0],'o')
plt.grid(True)
plt.xlabel('t'); plt.ylabel('y')
plt.legend(('Numerical'),loc=0)
plt.show()
