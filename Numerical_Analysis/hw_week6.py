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

def diffPoly(coeff, x):
    m = len(coeff)-1
    # get new coefficients
    for i in range(m-1):
        coeff[i]= (i+1)*coeff[i+1]
    coeff[m] = 0
    # evaluate
    p = coeff[m-1]
    for i in range(m-2,-1,-1):
        p = p*x + coeff[i]
    return p

xData = np.array([-2.2,   -0.3,    0.8,    1.9])
yData = np.array([ 15.180, 10.962, 1.920, -2.040])

coeff = polyFit(xData,yData,3)
print("Coefficients are:\n",coeff)

# 注意，這裡可能只有python能這樣寫，如果用其他語言可能會出問題
# 用外部函式修改傳入的參數是很危險的行為
# 但是到底怎麼寫函式的內部才會出問題我還沒研究出來
print("df(x) at x=0: ",diffPoly(coeff,0))
print("ddf(x) at x=0: ",diffPoly(coeff,0))

print("事實上，這個interpolant，其實已經找到原本的多項式了吧?")
print("所以得到的答案基本上是準確的")
