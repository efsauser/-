import numpy as np
def gaussElimin(a,b):
    n = len(b)
    # Elimination Phase
    for k in range(0,n-1):
        for i in range(k+1,n):
            if a[i,k] != 0.0:
                lam = a[i,k]/a[k,k]
                a[i,k+1:n] = a[i,k+1:n] - lam*a[k,k+1:n]
                b[i] = b[i] - lam*b[k]
    # Back substitution
    for k in range(n-1,-1,-1):
        b[k] = (b[k] - np.dot(a[k,k+1:n],b[k+1:n]))/a[k,k]
    return b

# calculate determinant by brute force
def det_2x2(a):
    return a[0,0]*a[1,1] - a[0,1]*a[1,0]

def det_3x3(a):
    # make cofactors
    cofc1 = np.array(
        [[a[1,1], a[1,2]],
         [a[2,1], a[2,2]]])
    det1 = det_2x2(cofc1)
    cofc2 = np.array(
        [[a[1,0], a[1,2]],
         [a[2,0], a[2,2]]])
    det2 = det_2x2(cofc2)
    cofc3 = np.array(
        [[a[1,0], a[1,1]],
         [a[2,0], a[2,1]]])
    det3 = det_2x2(cofc3)
    return a[0,0]*det1-a[0,1]*det2+a[0,2]*det3

def det_4x4(a):
    # sorry, I do it by a stupid way
    # make cofactors
    cofc1 = np.array(
        [[a[1,1], a[1,2], a[1,3]],
         [a[2,1], a[2,2], a[2,3]],
         [a[3,1], a[3,2], a[3,3]]])
    det1 = det_3x3(cofc1)
    cofc2 = np.array(
        [[a[1,0], a[1,2], a[1,3]],
         [a[2,0], a[2,2], a[2,3]],
         [a[3,0], a[3,2], a[3,3]]])
    det2 = det_3x3(cofc2)
    cofc3 = np.array(
        [[a[1,0], a[1,1], a[1,3]],
         [a[2,0], a[2,1], a[2,3]],
         [a[3,0], a[3,1], a[3,3]]])
    det3 = det_3x3(cofc3)
    cofc4 = np.array(
        [[a[1,0], a[1,1], a[1,2]],
         [a[2,0], a[2,1], a[2,2]],
         [a[3,0], a[3,1], a[3,2]]])
    det4 = det_3x3(cofc4)
    return a[0,0]*det1-a[0,1]*det2+a[0,2]*det3-a[0,3]*det4

# martix times vector
def mar_times_vec(a,b):
    n = len(b)
    result = np.array([.0,.0,.0,.0])
    for i in range(n):
        for j in range(n):
            result[i] += a[i,j]*b[j]
    return result
        
A = np.array([[ 3.50,  2.77, -0.76,  1.80],
              [-1.80,  2.68,  3.44, -0.09],
              [ 0.27,  5.07,  6.90,  1.61],
              [ 1.71,  5.45,  2.68,  1.71]])
b = np.array( [ 7.31,  4.23, 13.85, 11.55])
A_orig = A.copy() # Save original matrix
x = gaussElimin(A, b)

print("x =",x)
print("det(A) =", det_4x4(A_orig))
print("A*x =",  mar_times_vec(A_orig,x))
print("A的行列式值det(A)=", det_4x4(A_orig),"並沒有非常靠近0，")
print("因此這個解能夠判斷是準確的")
print("這一點也能從計算A*x的結果看出。")