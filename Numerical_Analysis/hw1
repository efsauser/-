x1 = 1
x2 = 3**-0.5  # 1/sqrt(3)
x3 = 2-3**0.5 # 2-sqrt(3)
sum1 = 0
sum2 = 0
sum3 = 0
pi = 3.14159265358979

# series 1
print("case 1: expanding 4*arctan(1)")
n = 0
while( (sum1-pi)>10**-4 or (sum1-pi)<-10**-4):
    sum1 += 4*(-1)**n*x1**(2*n+1)/(2*n+1)
    print("n="+str(n)+":"+str(sum1))
    n+=1
print("case 1 needs "+str(n)+" term to converge")
print("----------------------------------------")
# series 2
print("case 2: expanding 6*arctan(1/sqrt(3))")
n = 0
while( (sum2-pi)>10**-4 or (sum2-pi)<-10**-4):
    sum2 += 6*(-1)**n*x2**(2*n+1)/(2*n+1)
    print("n="+str(n)+":"+str(sum2))
    n+=1
print("case 2 needs "+str(n)+" term to converge")
print("----------------------------------------")
print("So case 2 converge faster.")
print("The reason is because the arctan is expanding at x=0,")
print("the closer the argument of arctan is to zero, the faster the series converges.")


# if you want, you can try this case.
'''
n = 0
while( (sum3-pi)>10**-4 or (sum3-pi)<-10**-4):
    sum3 += 12*(-1)**n*x3**(2*n+1)/(2*n+1)
    print(str(n)+":"+str(sum3))
    n+=1
'''
