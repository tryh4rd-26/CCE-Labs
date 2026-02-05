#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>

#define PORT 8080
#define BUFFER_SIZE 1024

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("Usage: %s <message>\n", argv[0]);
        return 0;
    }

    int sockfd;
    struct sockaddr_in server_addr;
    char buff[BUFFER_SIZE];

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd == -1) { perror("Socket creation failed"); exit(0); }

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = htonl(INADDR_ANY);

    if (connect(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr)) == -1) {
        perror("Connection failed"); close(sockfd); exit(0);
    }

    // Send message
    send(sockfd, argv[1], strlen(argv[1]), 0);

    // Receive response
    memset(buff, 0, sizeof(buff));
    int recdbytes = recv(sockfd, buff, sizeof(buff), 0);
    if (recdbytes > 0) {
        printf("Server Response: %s\n", buff);
    }

    close(sockfd);
    return 0;
}
