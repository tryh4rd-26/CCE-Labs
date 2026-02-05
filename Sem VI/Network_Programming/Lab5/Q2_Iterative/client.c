#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>

#define PORT 8080
#define BUFFER_SIZE 1024

int main() {
    int sockfd;
    struct sockaddr_in server_addr;
    char buff[BUFFER_SIZE];
    char str1[BUFFER_SIZE], str2[BUFFER_SIZE];

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd == -1) { perror("Socket creation failed"); exit(0); }

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = htonl(INADDR_ANY);

    if (connect(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr)) == -1) {
        perror("Connection failed"); close(sockfd); exit(0);
    }

    printf("Enter first string: ");
    fgets(str1, BUFFER_SIZE, stdin);
    str1[strcspn(str1, "\n")] = '\0';

    printf("Enter second string: ");
    fgets(str2, BUFFER_SIZE, stdin);
    str2[strcspn(str2, "\n")] = '\0';

    snprintf(buff, sizeof(buff), "%s %s", str1, str2);
    send(sockfd, buff, strlen(buff), 0);


    memset(buff, 0, sizeof(buff));
    int recdbytes = recv(sockfd, buff, sizeof(buff), 0);
    if (recdbytes > 0) {
        printf("Server Response: %s\n", buff);
    }

    close(sockfd);
    return 0;
}
