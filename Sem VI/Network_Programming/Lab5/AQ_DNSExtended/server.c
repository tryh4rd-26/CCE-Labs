#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>

#define PORT 8080
#define BUFFER_SIZE 1024

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("Usage: %s <bind_ip>\n", argv[0]);
        exit(1);
    }

    int sockfd;
    struct sockaddr_in server_addr, client_addr;
    socklen_t addr_len;
    char buff[BUFFER_SIZE];

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = inet_addr(argv[1]);

    bind(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr));

    printf("UDP Server running on %s:%d\n", argv[1], PORT);

    while (1) {
        addr_len = sizeof(client_addr);
        memset(buff, 0, BUFFER_SIZE);

        recvfrom(sockfd, buff, BUFFER_SIZE, 0,
                 (struct sockaddr*)&client_addr, &addr_len);

        printf("Client: %s\n", buff);

        if (strcmp(buff, "CLOSE") == 0)
            break;

        printf("Server: ");
        fgets(buff, BUFFER_SIZE, stdin);
        buff[strcspn(buff, "\n")] = '\0';

        sendto(sockfd, buff, strlen(buff), 0,
               (struct sockaddr*)&client_addr, addr_len);
    }

    close(sockfd);
    return 0;
}
