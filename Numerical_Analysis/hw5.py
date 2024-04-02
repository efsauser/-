import numpy as np
import cmath
from random import random
def polyRoots(a,tol=1.0e-12):
    def evalPoly(a,x):
        n = len(a) - 1
        p = a[n]
        dp = 0.0 + 0.0j
        ddp = 0.0 + 0.0j
        for i in range(1,n+1):
            ddp = ddp*x + 2.0*dp
            dp = dp*x + p
            p = p*x + a[n-i]
        return p,dp,ddp

    def laguerre(a,tol):
        x = random() 
        n = len(a)-1
        for i in range(30):
            p,dp,ddp = evalPoly(a,x)
            if abs(p) < tol: 
                return x
            g = dp/p
            h = g*g - ddp/p
            f = cmath.sqrt((n - 1)*(n*h - g*g))
            if abs(g + f) > abs(g - f): 
                dx = n/(g + f)
            else: 
                dx = n/(g - f)
            x = x - dx
            if abs(dx) < tol: return x
        print('Too many iterations')

    def deflPoly(a,root): 
        n = len(a)-1
        b = [(0.0 + 0.0j)]*n
        b[n-1] = a[n]
        for i in range(n-2,-1,-1):
            b[i] = a[i+1] + root*b[i+1]
        return b
    
    n = len(a) - 1
    roots = np.zeros((n),dtype=complex)
    for i in range(n):
        x = laguerre(a,tol)
        if abs(x.imag) < tol: 
            x = x.real
        roots[i] = x
        a = deflPoly(a,x)
    return roots

c = np.array([-150.0, 130.0, 57.0, -34.0, -8.0, 4.0, 1])
print('zeros are:\n',polyRoots(c))