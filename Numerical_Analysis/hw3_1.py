import numpy as np
import math
def gaussSeidel(iterEqs,x,tol = 1.0e-9):
    omega = 1.0
    k = 10
    p=1
    for i in range(1,501):
        xOld = x.copy()
        x = iterEqs(x,omega)
        dx = math.sqrt(np.dot(x-xOld, x-xOld))
        if dx < tol: return x,i,omega
        # Compute relaxation factor after k+p iterations
        if i == k: dx1 = dx
        if i == k+p:
            dx2 = dx
            omega = 2.0/(1.0+math.sqrt(1.0-(dx2/dx1)**(1.0/p)))
    print('Gauss-Seidel failed to converge')

def iterEqs(x,omega):
    n = len(x)
    x[0] = omega*(x[1]-x[n-1])/4.0 + (1.0-omega)*x[0]
    for i in range(1,n-1):
        x[i] = omega*(x[i-1]+x[i+1])/4.0 + (1.0-omega)*x[i]
    x[n-1] = omega*(1.0-x[0]+x[n-2])/4.0 + (1.0-omega)*x[n-1]
    return x

n = 20
x = np.zeros(n)
x,numIter,omega = gaussSeidel(iterEqs,x)
print("\nNumber of iterations =",numIter)
print("\nRelaxation factor =",omega)
print("\nThe solution is:\n",x)
input("\nPress return to exit")