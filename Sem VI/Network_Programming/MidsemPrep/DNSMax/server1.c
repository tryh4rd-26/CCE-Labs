#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<unistd.h>
#include<sys/socket.h>
#include<sys/types.h>
#include<netinet/in.h>
#include <arpa/inet.h>

#define MAXSIZE 1024

int main()
{
int sockfd,newsockfd,retval;
socklen_t actuallen;
int recedbytes,sentbytes;
struct sockaddr_in serveraddr,clientaddr;

char buff[MAXSIZE];
int a=0;
sockfd=socket(AF_INET,SOCK_STREAM,0);

if(sockfd==-1)
{
printf("\nSocket creation error");
}

serveraddr.sin_family=AF_INET;
serveraddr.sin_port=htons(3389);
serveraddr.sin_addr.s_addr=inet_addr("127.0.0.2");
retval=bind(sockfd,(struct sockaddr*)&serveraddr,sizeof(serveraddr));
if(retval==-1)
{
printf("Binding error");
close(sockfd);
exit(1);
}

retval=listen(sockfd,1);
if(retval==-1)
{
close(sockfd);
}

actuallen=sizeof(clientaddr);
printf("Server 1. PID: %d\n", getpid());
struct sockaddr_in local;
socklen_t local_len = sizeof(local);



while (1){
    newsockfd=accept(sockfd,(struct sockaddr*)&clientaddr,&actuallen);
    if (fork()==0){
        close(sockfd);
        getsockname(newsockfd, (struct sockaddr *)&local, &local_len);
        printf("Server 1 IP: %s\n", inet_ntoa(local.sin_addr));
        printf("Server 1 Port: %d\n", ntohs(local.sin_port));
        printf("Client IP: %s\n", inet_ntoa(clientaddr.sin_addr));
        printf("Client Port: %d\n", ntohs(clientaddr.sin_port));

        while(1){
            memset(buff, 0, sizeof(buff));
            int r = recv(newsockfd, buff, sizeof(buff), 0);
            if (r <= 0) {
                printf("Client disconnected.\n");
                break;
        }
        FILE* fp = fopen("server1log.txt","a");
        fprintf(fp, "[Client IP: %s, Client Port: %d] Msg: %s\n", inet_ntoa(clientaddr.sin_addr), ntohs(clientaddr.sin_port), buff);
        fclose(fp);
        }
        close(newsockfd);
        exit(0);
    }
    close(newsockfd);
}
}



