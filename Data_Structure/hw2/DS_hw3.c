#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define MAX 360000
int parent[MAX];

typedef struct{
    int source;
    int dest;
    int cost;
} Edge;

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

int isSame(int e1, int e2){
    if(find(e1)==find(e2))
        return 1;
    else 
        return 0;
}

int compareEdges(const void* a, const void* b) {
    return ((Edge*)a)->cost - ((Edge*)b)->cost;
}

void sortEdges(Edge *edges, long E){ // bubble sort
    Edge temp;
    for(int i=0; i<E; i++){
        for(int j=0; j<E-i-1; j++){
            if(edges[j].cost>edges[j+1].cost){
                temp = edges[j];
                edges[j]= edges[j+1];
                edges[j+1] = temp;
            }
        }
    }
}

int main(){
    FILE * fp;
    fp = fopen("data.txt", "r");
    int V, E;
    long long sum = 0;
    scanf("%d %d",&V, &E);
    // fscanf(fp, "%ld %ld",&V, &E);
    for(int i=0; i<E; i++) // initialize
        parent[i] = -1;
    Edge edges[E];
    for(int i=0; i<E; i++)
        scanf("%d %d %d",&edges[i].source,&edges[i].dest,&edges[i].cost);
        // fscanf(fp, "%d %d %d", &edges[i].source,&edges[i].dest,&edges[i].cost);
    qsort(edges, E, sizeof(Edge), compareEdges);
    for(int i=0; i<E; i++){
        if(!(isSame(edges[i].source, edges[i].dest))){
            sum += edges[i].cost;
            heightUnion(edges[i].source, edges[i].dest);
        }
        else
            continue;
    }
    printf("%lld", sum);
    return 0;
}
