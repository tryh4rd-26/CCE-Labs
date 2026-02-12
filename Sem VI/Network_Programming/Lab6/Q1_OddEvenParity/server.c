#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <string.h>
#include <unistd.h>

#define BUFFER_SIZE 1024
#define PORT 8080

// Function to check parity
int check_parity(char *data, int odd) {
    int count = 0;
    for (int i = 0; data[i] != '\0'; i++) {
        if (data[i] == '1') count++;
    }
    if (odd) {
        return (count % 2 == 1); // odd parity: total ones must be odd
    } else {
        return (count % 2 == 0); // even parity: total ones must be even
    }
}


int main() {
    int sockfd, newsockfd, retval;
    struct sockaddr_in server_addr, client_addr;
    socklen_t addr_len;
    char buff[BUFFER_SIZE];

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd == -1) {
        perror("Socket creation failed");
        exit(0);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = htonl(INADDR_ANY);

    retval = bind(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr));
    if (retval == -1) {
        perror("Bind failed");
        close(sockfd);
        exit(0);
    }

    retval = listen(sockfd, 1);
    if (retval == -1) {
        perror("Listen failed");
        close(sockfd);
        exit(0);
    }

    addr_len = sizeof(client_addr);
    newsockfd = accept(sockfd, (struct sockaddr*)&client_addr, &addr_len);
    if (newsockfd == -1) {
        perror("Accept failed");
        close(sockfd);
        exit(0);
    }

    while (1) {
        memset(buff, 0, sizeof(buff));
        int recdbytes = recv(newsockfd, buff, sizeof(buff), 0);
        if (recdbytes <= 0) break;

        printf("\nReceived Data: %s\n", buff);

        
        char msg[BUFFER_SIZE];
        if (check_parity(buff, 1)) {
            snprintf(msg, sizeof(msg), "Data OK (Odd Parity)");
        } else if (check_parity(buff, 0)) {
            snprintf(msg, sizeof(msg), "Data OK (Even Parity)");
        } else {
            snprintf(msg, sizeof(msg), "Data Corrupted");
        }

        send(newsockfd, msg, strlen(msg), 0);
    }

    close(newsockfd);
    close(sockfd);
    return 0;
}
