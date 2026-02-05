#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>

#define DNS_PORT 8053
#define SERVER_PORT 8080
#define BUFFER_SIZE 1024

int main() {
    int sockfd;
    struct sockaddr_in dns_addr, server_addr;
    socklen_t addr_len;
    char buff[BUFFER_SIZE];

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);

    dns_addr.sin_family = AF_INET;
    dns_addr.sin_port = htons(DNS_PORT);
    dns_addr.sin_addr.s_addr = inet_addr("127.0.0.1");

    printf("Client: ");
    fgets(buff, BUFFER_SIZE, stdin);
    buff[strcspn(buff, "\n")] = '\0';

    sendto(sockfd, buff, strlen(buff), 0,
           (struct sockaddr*)&dns_addr, sizeof(dns_addr));

    memset(buff, 0, BUFFER_SIZE);
    addr_len = sizeof(dns_addr);
    recvfrom(sockfd, buff, BUFFER_SIZE, 0,
             (struct sockaddr*)&dns_addr, &addr_len);

    if (strcmp(buff, "NOTFOUND") == 0) {
        printf("Hostname not found\n");
        close(sockfd);
        return 0;
    }

    printf("DNS resolved IP: %s\n", buff);


    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(SERVER_PORT);
    server_addr.sin_addr.s_addr = inet_addr(buff);

    while (1) {
        printf("Client: ");
        fgets(buff, BUFFER_SIZE, stdin);
        buff[strcspn(buff, "\n")] = '\0';

        sendto(sockfd, buff, strlen(buff), 0,
               (struct sockaddr*)&server_addr, sizeof(server_addr));

        if (strcmp(buff, "CLOSE") == 0)
            break;

        memset(buff, 0, BUFFER_SIZE);
        addr_len = sizeof(server_addr);
        recvfrom(sockfd, buff, BUFFER_SIZE, 0,
                 (struct sockaddr*)&server_addr, &addr_len);

        printf("Server: %s\n", buff);
    }

    close(sockfd);
    return 0;
}
