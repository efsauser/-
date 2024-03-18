#include <stdio.h>

int main(void){
    float x;
    scanf("%f", &x);
    float result = 7*x*x*x*x-8*x*x*x-x*x+6*x-22;
    printf("%.1f", result);
    return 0;
}
