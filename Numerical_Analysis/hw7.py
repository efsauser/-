import numpy as np
import math

# 好像希望我自己寫，所以我就寫寫看吧
def trapezoid(f, a, b, Iold, k):
    if k==1:
        Inew = (f(a)+f(b))*(b-a)/2.0
    else:
        n = 2**(k-2)
        Inew = Iold/2
        h = (b-a)/(2*n)
        for i in range(1, n+1):
            x = a + (2*i-1)*h
            Inew += f(x)*h
    return Inew

def f(x):
    # 先將(x/a)^2+(y/b)^2=1兩邊微分，得到dy/dx=-(b^2*x)/(a^2*y)
    # 代入弧長公式就可以得到下列結果
    a, b = 2, 1
    return 2*math.sqrt(1+(b**2*x**2)/(a**2*(a**2-x**2)))

a, b = 2, 1
Iold = 0
# 在上下限+a和-a會導致除以0的錯誤，因此我將上下限稍微縮短了一點點
for k in range(1,31):
    Inew = trapezoid(f, -a+1.0e-4, a-1.0e-4, Iold, k)
    if (k > 1) and (abs(Inew - Iold)) < 1.0e-5: break
    Iold = Inew
print("k =",k)
print("nPanels =",2**(k-1))
print('result =',Inew)