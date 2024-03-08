/*
Read two integers and store them in variables a and b.
Swap the values in a and b and print them out .

Input
    Two integers split by a space.

Output
    Two integers split by a space.

Example 1
    Input
    20 21
        
    Output
    21 20
    
Example 2
    Input
    2147483647 -2147483648
    
    Output
    -2147483648 2147483647
*/
#include <stdio.h>
int main(void){
    int a=0, b=0, c=0;
    scanf("%d %d", &a, &b);
    c = a;
    a = b;
    b = c;
    printf("%d %d", a, b);
    return 0;
}
