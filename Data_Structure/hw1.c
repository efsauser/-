#include <stdio.h>
#define MAX 100

int precedence(char op){
    switch(op){
        case '*':
        case '/':
            return 2;
        case '+':
        case '-':
            return 1;
        case '(':
        case ')':
            return -1;
        default:
            return 0;
    }
}
//      (A+B)/C*D --> AB+C/D*
//  A-B+C*D*E/F-G --> AB-CD*E*F/+G-
int main(){
    char infix[MAX], stack[MAX];
    int top=0;
    scanf("%s",infix);
    for(int i=0;infix[i]!='\0';i++){
        if(precedence(infix[i])==0) // if infix[i] is operand
            printf("%c",infix[i]);
        if(infix[i]=='(')
            stack[++top]=infix[i];
        if(infix[i]==')'){
            while(stack[top]!='(')
                printf("%c",stack[top--]);
            top--;
        }
        if(precedence(infix[i])>0){ //if infix[i] is not parenthesis '(' or ')'
            while(precedence(stack[top])>=precedence(infix[i]))
                printf("%c",stack[top--]); // pop
            stack[++top]=infix[i]; // push
        }
    }
    while(top>0)
        printf("%c",stack[top--]); // when end of expression, pop all element in stack
    return 0;
}
