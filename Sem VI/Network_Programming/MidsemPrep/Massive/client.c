// client.c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define PORT 8080
#define BUF 4096

int main(){
    int sock;
    struct sockaddr_in server;
    char buffer[BUF],response[BUF];

    sock=socket(AF_INET,SOCK_STREAM,0);

    server.sin_family=AF_INET;
    server.sin_port=htons(PORT);
    server.sin_addr.s_addr=inet_addr("127.0.0.1");

    connect(sock,(struct sockaddr*)&server,sizeof(server));

    printf("Connected to Server\n");
    printf("Client PID: %d | PPID: %d\n",
            getpid(),getppid());

    while(1){
        printf("\n===== MENU =====\n");
        printf("1 Add (Struct)\n");
        printf("2 Delete (Struct)\n");
        printf("3 Modify (Struct)\n");
        printf("4 Search (Struct)\n");
        printf("5 Count Age 12 (Struct)\n");
        printf("6 Sort by Age (Struct)\n");
        printf("7 Add (File)\n");
        printf("8 Delete (File)\n");
        printf("9 Modify (File)\n");
        printf("10 Search (File)\n");
        printf("11 Count Age 12 (File)\n");
        printf("12 Sort by Age (File)\n");
        printf("0 Exit\n");

        int choice;
        printf("Enter choice: ");
        scanf("%d",&choice);

        sprintf(buffer,"%d",choice);
        write(sock,buffer,strlen(buffer)+1);

        if(choice==0) break;

        int id=0,age=0;
        char name[50]="";

        if(choice==1||choice==7){
            printf("Enter ID Name Age: ");
            scanf("%d %s %d",&id,name,&age);
        }
        else if(choice==3||choice==9){
            printf("Enter ID Name Age: ");
            scanf("%d %s %d",&id,name,&age);
        }
        else if(choice==2||choice==4||choice==8||choice==10){
            printf("Enter ID: ");
            scanf("%d",&id);
        }

        sprintf(buffer,"%d %s %d",id,name,age);
        write(sock,buffer,strlen(buffer)+1);

        read(sock,response,BUF);
        printf("Server: %s\n",response);
    }

    close(sock);
}
