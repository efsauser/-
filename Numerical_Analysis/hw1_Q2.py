import numpy as np
import math

def neville(xData, yData, x):
    n = len(xData)
    y = yData.copy()
    for i in range(1, n):
        for j in range(n-i):
            y[j] = ((x-xData[j+i])*y[j]+(xData[j]-x)*y[j+1])/(xData[j]-xData[j+i])
    return y[0]

def polyFit_2(xData, yData): # quadratic version 
    n = len(xData)
    A = np.zeros((3, 3))
    b = np.zeros(3)
    x = np.zeros(3)
    A[0,0] = n
    A[0,1] = A[1,0] = np.sum(xData)
    A[0,2] = A[2,0] = A[1,1] = np.dot(xData, xData)
    A[1,2] = A[2,1] = np.sum(xData**3)
    A[2,2] = np.sum(xData**4)
    
    b[0] = np.sum(yData)
    b[1] = np.dot(xData, yData)
    b[2] = np.dot(xData**2, yData)

    # solve normal equations use Cramer's rule
    det_A = np.linalg.det(A)
    for i in range(3):
        Acopy = A.copy()
        Acopy[0,i], Acopy[1,i], Acopy[2,i] = b[0], b[1], b[2]
        x[i] = np.linalg.det(Acopy)/det_A   
    return x


xData = np.array([0.0,  1.525,  3.050,  4.575,   6.10,  7.625,  9.150])
yData = np.array([1.0, 0.8617, 0.7385, 0.6292, 0.5328, 0.4481, 0.3741])

neville_result = neville(xData, yData, 10.5)
print("\nUse Neville’s Method:\ndensity at h=10.5km is", neville_result)

x = polyFit_2(xData, yData)
polyFit_result = x[0]+x[1]*10.5+x[2]*10.5**2
print("\nUse quadratic least square fitting:\ndensity at h=10.5km is", polyFit_result)
