#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<unistd.h>
#include<sys/socket.h>
#include<sys/types.h>
#include<netinet/in.h>
#include<arpa/inet.h>
#include<sys/wait.h>

#define MAXSIZE 1024
#define MAX_CLIENTS 5

int main()
{
    int sockfd, newsockfd;
    socklen_t actuallen;
    struct sockaddr_in serveraddr, clientaddr;

    char buff[MAXSIZE];

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if(sockfd == -1){
        printf("Socket creation error\n");
        exit(1);
    }

    serveraddr.sin_family = AF_INET;
    serveraddr.sin_port = htons(8888);
    serveraddr.sin_addr.s_addr = inet_addr("127.0.0.1");

    if(bind(sockfd,(struct sockaddr*)&serveraddr,sizeof(serveraddr))==-1){
        printf("Binding error\n");
        close(sockfd);
        exit(1);
    }

    listen(sockfd, MAX_CLIENTS);

    printf("DNS Server PID: %d\n", getpid());

    actuallen = sizeof(clientaddr);

    while(1)
    {
        newsockfd = accept(sockfd,
                          (struct sockaddr*)&clientaddr,
                          &actuallen);

        if(fork()==0)
        {
            close(sockfd);

            memset(buff,0,MAXSIZE);
            read(newsockfd,buff,sizeof(buff));

            printf("Client Requested Domain: %s\n",buff);

            FILE *fp = fopen("dnsdb.txt", "r");

            if(fp == NULL){
                char *msg="DNS File Error";
                send(newsockfd,msg,strlen(msg)+1,0);
                close(newsockfd);
                exit(1);
            }

            char domain[100], ip[100], port[10];
            char foundIP[100] = "NOTFOUND";

            while(fscanf(fp,"%s %s %s",domain,ip,port)==3)
            {
                if(strcmp(domain,buff)==0)
                {
                    sprintf(foundIP,"%s:%s",ip,port);
                    break;
                }
            }

            fclose(fp);

            send(newsockfd,foundIP,strlen(foundIP)+1,0);

            close(newsockfd);
            exit(0);
        }

        close(newsockfd);  // avoid zombies
    }

    close(sockfd);
    return 0;
}
