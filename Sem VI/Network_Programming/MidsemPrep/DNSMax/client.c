// client.c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define DNS_PORT 8888

#define BUF 2048

int main() {

    int dns_sock, server_sock;
    struct sockaddr_in dns, server, local;
    socklen_t len = sizeof(local);
    char buffer[BUF];
    char resolvedIP[100], resolvedPort[10];

    printf("Client PID: %d PPID: %d\n",
           getpid(),getppid());

    /* Connect to DNS */
    dns_sock = socket(AF_INET, SOCK_STREAM, 0);

    dns.sin_family = AF_INET;
    dns.sin_port = htons(DNS_PORT);
    dns.sin_addr.s_addr = inet_addr("127.0.0.1");

    connect(dns_sock,(struct sockaddr*)&dns,sizeof(dns));

    printf("Enter domain (server1.com/server2.com): ");
    scanf("%s",buffer);

    write(dns_sock,buffer,BUF);

    memset(buffer,0,BUF);
    read(dns_sock,buffer,BUF);

    close(dns_sock);

    printf("DEBUG: DNS Response: [%s]\n",buffer);
    printf("DEBUG: Response length: %lu\n", strlen(buffer));

    if(strcmp(buffer,"NOTFOUND")==0) {
        printf("Domain Not Found\n");
        return 0;
    }

    printf("Resolved: %s\n",buffer);

    /* Parse IP:PORT from DNS response */
    sscanf(buffer, "%[^:]:%s", resolvedIP, resolvedPort);
    printf("DEBUG: Parsed IP: [%s], Port: [%s]\n", resolvedIP, resolvedPort);
    int port = atoi(resolvedPort);

    /* Connect to resolved server */
    server_sock = socket(AF_INET, SOCK_STREAM, 0);

    server.sin_family = AF_INET;
    server.sin_port = htons(port);
    server.sin_addr.s_addr = inet_addr(resolvedIP);

    connect(server_sock,(struct sockaddr*)&server,sizeof(server));

    getsockname(server_sock,
                (struct sockaddr*)&local,
                &len);

    printf("Client Local: %s:%d\n",
           inet_ntoa(local.sin_addr),
           ntohs(local.sin_port));

    getchar();

    while(1) {

        printf("Enter message (type exit to quit): ");
        fgets(buffer,BUF,stdin);

        if(strncmp(buffer,"exit",4)==0)
            break;

        write(server_sock,buffer,BUF);

        memset(buffer,0,BUF);
        read(server_sock,buffer,BUF);

        printf("Server Response: %s\n",buffer);
    }

    close(server_sock);
    return 0;
}
