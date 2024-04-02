import numpy as np
import math
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
        # if abs(a[p,k]) < tol: error.err('Matrix is singular')
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
    # if abs(a[n-1,n-1]) < tol: error.err('Matrix is singular')
  # Back substitution
    b[n-1] = b[n-1]/a[n-1,n-1]
    for k in range(n-2,-1,-1):
        b[k] = (b[k] - np.dot(a[k,k+1:n],b[k+1:n]))/a[k,k]
    return b

def polyFit(xData,yData,m):
    a = np.zeros((m+1,m+1))
    b = np.zeros(m+1)
    s = np.zeros(2*m+1)
    for i in range(len(xData)):
        temp = yData[i]
        for j in range(m+1):
            b[j] = b[j] + temp
            temp = temp*xData[i]
        temp = 1.0
        for j in range(2*m+1):
            s[j] = s[j] + temp
            temp = temp*xData[i]
    for i in range(m+1):
        for j in range(m+1):
                a[i,j] = s[i+j]
    return gaussPivot(a,b)

def stdDev(c,xData,yData):
    def evalPoly(c,x):
        m = len(c) - 1
        p = c[m]
        for j in range(m):
            p = p*x + c[m-j-1]
        return p
    n = len(xData) - 1
    m = len(c) - 1
    sigma = 0.0
    for i in range(n+1):
        p = evalPoly(c,xData[i])
        sigma = sigma + (yData[i] - p)**2
    sigma = math.sqrt(sigma/(n - m))
    return sigma

fig, ax = plt.subplots(1,2,figsize=(9,6))

xData = np.array([1.0,2.5,3.5,4.0,1.1,1.8,2.2,3.7])
yData = np.array([6.008,15.722,27.130,33.772,5.257,9.549,11.098,28.828])

# calculate m=1 and plot
coeff = polyFit(xData,yData,1)
print("Coefficients are:\n",coeff)
print("Std. deviation =",stdDev(coeff,xData,yData))
m = len(coeff)
x1 = min(xData)
x2 = max(xData)
dx = (x2 - x1)/20.0
x = np.arange(x1,x2 + dx/10.0,dx)
y = np.zeros((len(x)))*1.0
for i in range(m):
    y = y + coeff[i]*x**i
ax[0].plot(xData,yData,'o',x,y,'-')
ax[0].set_xlabel('x'); ax[0].set_ylabel('y')
ax[0].set_title('m=1')
ax[0].grid (True)

# calculate m=2 and plot
coeff = polyFit(xData,yData,2)
print("Coefficients are:\n",coeff)
print("Std. deviation =",stdDev(coeff,xData,yData))
m = len(coeff)
x1 = min(xData)
x2 = max(xData)
dx = (x2 - x1)/20.0
x = np.arange(x1,x2 + dx/10.0,dx)
y = np.zeros((len(x)))*1.0
for i in range(m):
    y = y + coeff[i]*x**i
ax[1].plot(xData,yData,'o',x,y,'-')
ax[1].set_xlabel('x'); ax[1].set_ylabel('y')
ax[1].set_title('m=2')
ax[1].grid (True)

print("因為quadratic的標準差比較小")
print("所以quadratic是比較好的fitting")
print("這點也能從圖上看出來")
plt.suptitle("quadratic is a better fit.")
plt.show()