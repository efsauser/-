import numpy as np
import cmath
from random import random

def polyRoots(a,tol=1.0e-12):
    def evalPoly(a, x):
        n = len(a)-1
        p = a[n]
        dp, ddp = 0.0+0.0j, 0.0+0.0j
        for i in range(1, n+1):
            ddp = 2.0*dp + x*ddp
            dp = p + x*dp
            p = p*x + a[n-i]
        return p, dp, ddp

    # a is the array of coefficients
    def laguerre(a, tol):
        x = random()
        n = len(a) - 1
        for i in range(30):
            p, dp, ddp = evalPoly(a, x)
            if abs(p) < tol:
                return x
            G = dp/p
            H = G*G - ddp/p
            F = cmath.sqrt( (n-1)*(n*H-G*G) )
            if abs(G+F) > abs(G-F): 
                deno = G+F
            else: 
                deno = G-F
            r = n/deno
            x = x - r
            if abs(r) < tol:
                return x
        print('Too many iterations')
    
    def deflPoly(a, root):
        n = len(a)-1
        b = np.zeros((n),dtype=complex)
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

km = 1500.0
cm = 12.0
c = np.array([km**2, km*cm, 3*km, 2*cm, 1])
root = polyRoots(c)
print('Roots are:\n',root)