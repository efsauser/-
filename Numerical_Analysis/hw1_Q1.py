import numpy as np
import math

def gaussSeidel_noRelax(x, tol=1.0e-4):
    for i in range(1, 501):
        xOld = x.copy()
        x = iterEqs_noRelax(x)
        dx = math.sqrt(np.dot(x-xOld, x-xOld))
        if dx < tol: 
            return x,i
    print('failed to converge')

def iterEqs_noRelax(x):
    # 注意index
    x[0] = (3*x[1]+80)/5
    x[1] = (x[2]+x[0])/2
    x[2] = (x[3]+x[1])/2
    x[3] = (x[4]+x[2]-20)/2
    x[4] = 3*x[3]/5
    return x

def gaussSeidel_withRelax(x, tol=1.0e-4):
    k = 10 # first 10 iterations with omega
    p = 1 # p is a positive number
    w = 1.0 # 我使用w而不是"omega"
    for i in range(1, 501):
        xOld = x.copy()
        x = iterEqs_withRelax(x, w)
        dx = math.sqrt(np.dot(x-xOld, x-xOld))
        if dx < tol: 
            return x, i, w
        if i==k:
            dx1 = dx
        if i==k+p:
            dx2 = dx
            w = 2.0/(1.0+math.sqrt(1.0-(dx2/dx1)**(1.0/p)))
    print('failed to converge')

def iterEqs_withRelax(x, w):
    # 注意index
    x[0] = w*(3*x[1]+80)/5+(1-w)*x[0]
    x[1] = w*(x[2]+x[0])/2+(1-w)*x[1]
    x[2] = w*(x[3]+x[1])/2+(1-w)*x[2]
    x[3] = w*(x[4]+x[2]-20)/2+(1-w)*x[3]
    x[4] = w*3*x[3]/5+(1-w)*x[4]
    return x

def conjGrad(Av, x, b, tol=1.0e-4):
    n = len(b)
    r = b - Av(x)
    s = r.copy()
    for i in range(n):
        As = Av(s)
        alpha = np.dot(s, r)/np.dot(s, As)
        x = x + alpha*s
        r = b - Av(x)
        if (math.sqrt(np.dot(r, r))) < tol:
            break
        else:
            beta = -np.dot(r, As)/np.dot(s, As)
            s = r + beta*s
    return x, i

def Av(x): # 矩陣乘以向量
    Av = np.zeros(5)
    Av[0]   =  5*x[0]-3*x[1]
    Av[1:4] = -3*x[0:3]+6*x[1:4]-3*x[2:5]
    Av[4]   = -3*x[3]+5*x[4]
    return Av

n = 5
x = np.zeros(n)
x, numIter = gaussSeidel_noRelax(x)
print("\n===Gauss-Seidel without relaxtion===")
print("number of iterations: ", numIter)
print("the solution is:\n", x)

x = np.zeros(n)
x, numIter, omega = gaussSeidel_withRelax(x)
print("\n===Gauss-Seidel with relaxtion===")
print("Relaxation factor =",omega)
print("Number of iterations: ", numIter)
print("the solution is:\n", x)

print("\nGauss-Seidel with relaxtion收斂較快")
print("因為omega>1，因此當解往正確的方向收斂時，relaxation factor能使收斂速度更快")
print("這叫做simultaneous overrelaxation")
print("引用自Numerical Methods for Engineers by Chapra")

x = np.zeros(n)
b = np.array([80, 0, 0, -60, 0])
x, numIter= conjGrad(Av, x, b)
print("\n===conjugate gradient===")
print("Number of iterations: ", numIter)
print("the solution is:\n", x)