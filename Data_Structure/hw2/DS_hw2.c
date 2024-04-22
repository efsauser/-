#include <stdio.h>
#include <string.h>
#define MAX 10000
int parent[MAX];

int find(int i){
    for(; parent[i]>=0; i=parent[i])
    ;
    return i;
}

void heightUnion(int i, int j){
    //i, j are roots with parent[i] = -height[i]
    int root1 = find(i), root2 = find(j);
    if(root1 > root2) // ex: -5 > -10
        parent[root1] = root2;
    else if (root1 == root2){
        parent[root2] = root1;
        parent[root1]--;
    }
    else
        parent[root2] = root1;
}

int printSame(int e1, int e2){
    if(find(e1)==find(e2))
        printf("true");
    else 
        printf("false");
}

void printParent(){
    for(int i=0; i<10; i++)
        printf("%2d ", i);
    printf("\n");
    for(int i=0; i<10; i++)
        printf("%2d ", parent[i]);
    printf("\n");
}

int main(){
    int cases, n, ops, e1, e2;
    char operation[MAX];
    scanf("%d", &cases);
    for(int i=0; i<cases; i++){ // loop of cases
        scanf("%d %d", &n, &ops);
        for(int i=0; i<n; i++) // initialize
            parent[i] = -1;
        for(int j=0; j<ops; j++){ // loop of ops
            scanf("%s", &operation);
            if(strcmp(operation, "union")==0){
                scanf("%d %d", &e1, &e2);
                heightUnion(e1, e2);
            }
            if(strcmp(operation, "find")==0){
                scanf("%d", &e1);
                printf("%d\n", find(e1));
            }
            if(strcmp(operation, "same")==0){
                scanf("%d %d", &e1, &e2);
                if(find(e1)==find(e2))
                    printf("true\n");
                else 
                    printf("false\n");
            }
        }
    }
    return 0;
}


// #include <stdio.h>
// #include <stdlib.h>

// int *parent;

// int find(int i) {
//     for(; parent[i]>=0; i=parent[i])
//     ;
//     return i;
// }

// void heightUnion(int a, int b) {
//     int rootA = find(a);
//     int rootB = find(b);

//     if (rootA != rootB) {
//         if (parent[rootA] > parent[rootB]) { 
//             parent[rootA] = rootB;
//         } else if (parent[rootA] < parent[rootB]) {
//             parent[rootB] = rootA;
//         } else { 
//             parent[rootA] += parent[rootB];
//             parent[rootB] = rootA;
//         }
//     }
// }

// int main() {
//     int t;
//     scanf("%d", &t);

//     while (t--) {
//         int n, ops;
//         scanf("%d %d", &n, &ops);
//         parent = (int *)malloc(n * sizeof(int));

//         for (int i = 0; i < n; i++) {
//             parent[i] = -1;
//         }

//         for (int i = 0; i < ops; i++) {
//             char command[10];
//             int a, b;
//             scanf("%s", command);
            
//             if (command[0] == 'u') { // union
//                 scanf("%d %d", &a, &b);
//                 heightUnion(a, b);
//             } else if (command[0] == 'f') { // find
//                 scanf("%d", &a);
//                 printf("%d\n", find(a));
//             } else if (command[0] == 's') { // same
//                 scanf("%d %d", &a, &b);
//                 if (find(a) == find(b)) printf("true\n");
//                 else printf("false\n");
//             }
//         }

//         free(parent);
//     }

//     return 0;
// }