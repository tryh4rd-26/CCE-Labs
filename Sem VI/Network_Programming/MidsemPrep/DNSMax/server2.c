// server2.c
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<unistd.h>
#include<sys/socket.h>
#include<sys/types.h>
#include<netinet/in.h>
#include<arpa/inet.h>
#include<sys/wait.h>

#define PORT 3390
#define MAXSIZE 2048

/* ===== YOUR CRC FUNCTION ===== */

unsigned int crc(unsigned int data) {

    unsigned int generator = 0xB; // 1011
    int deg = 3;

    data = data << deg;  // append zeros

    for(int i = 31; i >= deg; i--) {
        if(data & (1 << i))
            data ^= (generator << (i - deg));
    }

    return data;  // remainder
}

int main()
{
    int sockfd, newsockfd;
    struct sockaddr_in serveraddr, clientaddr, local;
    socklen_t len = sizeof(clientaddr);
    socklen_t locallen = sizeof(local);

    char buff[MAXSIZE];

    sockfd = socket(AF_INET, SOCK_STREAM, 0);

    serveraddr.sin_family = AF_INET;
    serveraddr.sin_port = htons(PORT);
    serveraddr.sin_addr.s_addr = inet_addr("127.0.0.3");

    if(bind(sockfd,(struct sockaddr*)&serveraddr,sizeof(serveraddr))==-1){
        printf("Binding error\n");
        close(sockfd);
        exit(1);
    }
    listen(sockfd,5);

    printf("Server2 PID: %d\n", getpid());

    while(1)
    {
        newsockfd = accept(sockfd,(struct sockaddr*)&clientaddr,&len);

        if(fork()==0)
        {
            close(sockfd);

            getsockname(newsockfd,
                        (struct sockaddr*)&local,
                        &locallen);

            printf("\nClient IP: %s\n",
                   inet_ntoa(clientaddr.sin_addr));
            printf("Client Port: %d\n",
                   ntohs(clientaddr.sin_port));

            printf("Server IP: %s\n",
                   inet_ntoa(local.sin_addr));
            printf("Server Port: %d\n",
                   ntohs(local.sin_port));

            printf("Server PID: %d PPID: %d\n",
                   getpid(), getppid());

            while(1)
            {
                memset(buff,0,sizeof(buff));

                int r = recv(newsockfd,buff,sizeof(buff),0);
                if(r<=0)
                {
                    printf("Client disconnected\n");
                    break;
                }

                /* -------- CHILD 1 : EDIT -------- */

                if(fork()==0)
                {
                    strcat(buff," [Edited]");

                    FILE *fp = fopen("server2log.txt","a");
                    fprintf(fp,"Edited Msg from %s:%d : %s\n",
                            inet_ntoa(clientaddr.sin_addr),
                            ntohs(clientaddr.sin_port),
                            buff);
                    fclose(fp);

                    exit(0);
                }
                wait(NULL);

                /* -------- CHILD 2 : CRC -------- */

                if(fork()==0)
                {
                    unsigned int num = atoi(buff); // expects integer input
                    unsigned int remainder = crc(num);

                    char response[MAXSIZE];

                    sprintf(response,
                            "Edited Msg: %s | CRC Remainder: %u\n",
                            buff,
                            remainder);

                    send(newsockfd,response,
                         strlen(response),0);

                    exit(0);
                }
                wait(NULL);
            }

            close(newsockfd);
            exit(0);
        }

        close(newsockfd);
    }
}
