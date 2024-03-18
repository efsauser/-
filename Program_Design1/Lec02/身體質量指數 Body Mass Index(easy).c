#include<stdio.h>

int main () {
    int x, y;
    scanf("%d %d",&x, &y);
    float h = x/100.0, w = y;
    float BMI = w/(h*h);
    printf("%.6f", BMI);
    return 0;
}
