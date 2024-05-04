import math
import numpy as np
import matplotlib.pyplot as plt

def swapRows(v,i,j):
    if len(v.shape) == 1:
        v[i],v[j] = v[j],v[i]
    else:
        v[[i,j],:] = v[[j,i],:]

def gaussPivot(a,b,tol=1.0e-12):
    n = len(b)
  # Set up scale factors
    s = np.zeros(n)
    for i in range(n):
        s[i] = max(np.abs(a[i,:]))
    for k in range(0,n-1):
      # Row interchange, if needed
        p = np.argmax(np.abs(a[k:n,k])/s[k:n]) + k
        if p != k:
            swapRows(b,k,p)
            swapRows(s,k,p)
            swapRows(a,k,p)

      # Elimination
        for i in range(k+1,n):
            if a[i,k] != 0.0:
                lam = a[i,k]/a[k,k]
                a[i,k+1:n] = a[i,k+1:n] - lam*a[k,k+1:n]
                b[i] = b[i] - lam*b[k]
  # Back substitution
    b[n-1] = b[n-1]/a[n-1,n-1]
    for k in range(n-2,-1,-1):
        b[k] = (b[k] - np.dot(a[k,k+1:n],b[k+1:n]))/a[k,k]
    return b

def newtonRaphson2(f,x,tol=1.0e-9):
    def jacobian(f,x):
        h = 1.0e-4
        n = len(x)
        jac = np.zeros((n,n))
        f0 = f(x)
        for i in range(n):
            temp = x[i]
            x[i] = temp + h
            f1 = f(x)
            x[i] = temp
            jac[:,i] = (f1 - f0)/h
        return jac,f0
    
    for i in range(30):
        jac,f0 = jacobian(f,x)
        if math.sqrt(np.dot(f0,f0)/len(x)) < tol: return x
        dx = gaussPivot(jac,-f0)
        x = x + dx
        if math.sqrt(np.dot(dx,dx)) < tol*max(max(abs(x)),1.0):
            return x
    print('Too many iterations')

def residual(y): 
    # y(0) = 1/2 = alpha, y'(1) = -2/9 = beta
    a = 1.0/2.0
    b = -2.0/9.0
    r = np.zeros(m + 1)
    r[0] = y[0]-a
    r[m] = 2*y[m-1]-2*y[m]-h*h*F(x[m], y[m], b)+2*h*b
    for i in range(1,m):
        r[i] = y[i-1] - 2.0*y[i] + y[i+1] \
        - h*h*F(x[i],y[i],(y[i+1] - y[i-1])/(2.0*h))
    return r

def F(x,y,Dy): # Differential eqn. y" = F(x,y,y’)
    # Dy = y' = yPrime
    F = -2*y*(2*x*Dy + y)
    return F

def startSoln(x): # Starting solution y(x)
    y = np.zeros(m + 1)
    for i in range(m + 1): 
        y[i] = 0.5-2.0*x[i]/9.0
        # y[i] = 1.0/(2+x[i]*x[i])
    return y

xStart = 0.0 # x at left end
xStop = 1.0 # x at right end
m = 20 # Number of mesh intervals
h = (xStop - xStart)/m
x = np.arange(xStart,xStop + h,h)
y_cal = newtonRaphson2(residual,startSoln(x),1.0e-5)
y_exact = 1/(2+x**2)

print("\n        x              y")
for i in range(m + 1):
    print("%14.5e %14.5e"%(x[i],y_cal[i]))

plt.plot(x,y_cal,'o',x,y_exact,'-')
plt.legend(('Numerical','Exact'))
plt.show()