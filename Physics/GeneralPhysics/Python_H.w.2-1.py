import matplotlib.pyplot as plt 
import numpy as np 
from math import factorial
def sine(x):
    result = 0
    for n in range(20):
        result = result + (x**(2*n+1) * (-1)**n) /factorial(2*n+1)
        #使用+=會報錯，原因有點複雜
    return result
x = np.linspace(-10, 10, 200)
y = sine(x)
plt.xlim(-10.25, 10.25)
plt.ylim(-1.25, 1.25)
plt.xlabel('time')
plt.xlabel('Amplitude')
plt.title('example')
plt.plot(x, y)
plt.show()