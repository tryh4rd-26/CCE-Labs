#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <string.h>
#include <unistd.h>

#define BUFFER_SIZE 2048
#define PORT 8080

void xor_operation(char *dividend, char *divisor, int len) {
    for (int i = 0; i < len; i++)
        dividend[i] = (dividend[i] == divisor[i]) ? '0' : '1';
}

int check_crc(char *data, char *generator) {
    int data_len = strlen(data);
    int gen_len = strlen(generator);

    char temp[BUFFER_SIZE];
    strcpy(temp, data);

    for (int i = 0; i <= data_len - gen_len; i++) {
        if (temp[i] == '1')
            xor_operation(&temp[i], generator, gen_len);
    }

    for (int i = data_len - gen_len + 1; i < data_len; i++) {
        if (temp[i] != '0') return 0;  // Corrupt
    }
    return 1;  // Correct
}

int main() {
    int sockfd, newsockfd;
    struct sockaddr_in server_addr, client_addr;
    socklen_t addr_len;
    char generator[64], data[BUFFER_SIZE];

    printf("Creating socket...\n");
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd == -1) { perror("Socket creation failed"); exit(0); }
    printf("Socket created successfully.\n");

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = htonl(INADDR_ANY);

    printf("Binding socket...\n");
    if (bind(sockfd, (struct sockaddr *)&server_addr, sizeof(server_addr)) == -1) {
        perror("Bind failed"); close(sockfd); exit(0);
    }
    printf("Socket bound successfully.\n");

    printf("Listening on port %d...\n", PORT);
    if (listen(sockfd, 1) == -1) { perror("Listen failed"); close(sockfd); exit(0); }

    addr_len = sizeof(client_addr);
    printf("Waiting for client connection...\n");
    newsockfd = accept(sockfd, (struct sockaddr *)&client_addr, &addr_len);
    if (newsockfd == -1) { perror("Accept failed"); close(sockfd); exit(0); }
    printf("Client connected.\n");

    while (1) {
        memset(generator, 0, sizeof(generator));
        memset(data, 0, sizeof(data));

        int gbytes = recv(newsockfd, generator, sizeof(generator), 0);
        if (gbytes <= 0) break;
        sleep(1);
        int dbytes = recv(newsockfd, data, sizeof(data), 0);
        if (dbytes <= 0) break;

        if (strcmp(data, "CLOSE") == 0) break;

        printf("Received Codeword: %s\n", data);

        if (check_crc(data, generator))
            send(newsockfd, "Data NOT CORRUPTED", 19, 0);
        else
            send(newsockfd, "Data CORRUPTED", 14, 0);
    }

    printf("Closing sockets...\n");
    close(newsockfd);
    close(sockfd);
    return 0;
}
