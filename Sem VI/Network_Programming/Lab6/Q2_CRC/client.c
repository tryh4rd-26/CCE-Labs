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

void crc(char *data, char *generator, char *remainder) {
    int data_len = strlen(data);
    int gen_len = strlen(generator);

    char temp[BUFFER_SIZE];
    strcpy(temp, data);

    for (int i = 0; i <= data_len - gen_len; i++) {
        if (temp[i] == '1')
            xor_operation(&temp[i], generator, gen_len);
    }

    strncpy(remainder, &temp[data_len - gen_len + 1], gen_len - 1);
    remainder[gen_len - 1] = '\0';
}

int main() {
    int sockfd, retval;
    struct sockaddr_in server_addr;
    char data[BUFFER_SIZE], message[BUFFER_SIZE];
    char generator[64], remainder[64];

    printf("Creating socket...\n");
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd == -1) { perror("Socket creation failed"); exit(0); }
    printf("Socket created successfully.\n");

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = htonl(INADDR_ANY);

    printf("Connecting to server...\n");
    retval = connect(sockfd, (struct sockaddr *)&server_addr, sizeof(server_addr));
    if (retval == -1) { perror("Connection failed"); close(sockfd); exit(0); }
    printf("Connected to server.\n");

    while (1) {
        printf("\nEnter binary data (or type CLOSE to quit): ");
        scanf("%s", data);
        if (strcmp(data, "CLOSE") == 0) {
            send(sockfd, data, strlen(data), 0);
            break;
        }

        int choice;
        printf("1. CRC-12\n2. CRC-16\n3. CRC-CCITT\nChoose: ");
        scanf("%d", &choice);

        if (choice == 1) strcpy(generator, "1100000001111");
        else if (choice == 2) strcpy(generator, "11000000000000101");
        else strcpy(generator, "10001000000100001");

        int gen_len = strlen(generator);

        char appended[BUFFER_SIZE];
        strcpy(appended, data);
        for (int i = 0; i < gen_len - 1; i++) strcat(appended, "0");

        crc(appended, generator, remainder);

        strcpy(message, data);
        strcat(message, remainder);

        printf("Codeword Sent: %s\n", message);

        // Send generator first, then codeword
        send(sockfd, generator, strlen(generator), 0);
        sleep(1);
        send(sockfd, message, strlen(message), 0);

        // Receive response
        char response[BUFFER_SIZE];
        memset(response, 0, sizeof(response));
        int recdbytes = recv(sockfd, response, sizeof(response), 0);
        if (recdbytes > 0) {
            printf("Server Response: %s\n", response);
        }
    }

    printf("Closing socket...\n");
    close(sockfd);
    return 0;
}
