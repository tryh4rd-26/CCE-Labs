#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <string.h>
#include <unistd.h>

#define BUFFER_SIZE 1024
#define PORT 8080

// Function to calculate parity bit (0 = even, 1 = odd)
int calculate_parity(char *data, int odd) {
    int count = 0;
    for (int i = 0; data[i] != '\0'; i++) {
        if (data[i] == '1') count++;
    }
    if (odd) return (count % 2 == 0);   // odd parity: add 1 if count is even
    else return (count % 2 != 0);       // even parity: add 1 if count is odd
}

int main() {
    int sockfd, retval;
    struct sockaddr_in server_addr;
    char buff[BUFFER_SIZE];

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd == -1) {
        perror("Socket creation failed");
        exit(0);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = htonl(INADDR_ANY);

    retval = connect(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr));
    if (retval == -1) {
        perror("Connection failed");
        close(sockfd);
        exit(0);
    }

    printf("Enter binary data (e.g. 101011): ");
    fgets(buff, BUFFER_SIZE, stdin);
    buff[strcspn(buff, "\n")] = '\0';

    // Choose parity type
    int odd = 0;
    printf("Use odd parity? (1=yes, 0=no): ");
    scanf("%d", &odd);

    // Calculate parity bit
    int parity_bit = calculate_parity(buff, odd);

    // Append parity bit to data
    char message[BUFFER_SIZE];
    snprintf(message, sizeof(message), "%s%d", buff, parity_bit);

    // Send to server
    send(sockfd, message, strlen(message), 0);

    // Receive response
    memset(buff, 0, sizeof(buff));
    int recdbytes = recv(sockfd, buff, sizeof(buff), 0);
    if (recdbytes > 0) {
        printf("Server Response: %s\n", buff);
    }

    close(sockfd);
    return 0;
}
