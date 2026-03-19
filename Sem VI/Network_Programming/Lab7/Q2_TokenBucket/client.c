#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define PORT 8080
#define BUFFER_SIZE 1024

int main() {
    int sockfd;
    struct sockaddr_in server_addr;
    char buffer[BUFFER_SIZE];

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) {
        perror("Socket creation failed");
        exit(1);
    }

    memset(&server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = htonl(INADDR_LOOPBACK);

    if (connect(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0) {
        perror("Connection failed");
        close(sockfd);
        exit(1);
    }

    printf("Enter number of packets: ");
    int n;
    scanf("%d", &n);

    int times[n], sizes[n];
    printf("Enter %d arrival times (space separated): ", n);
    for (int i = 0; i < n; i++) scanf("%d", &times[i]);

    printf("Enter %d packet sizes (space separated): ", n);
    for (int i = 0; i < n; i++) scanf("%d", &sizes[i]);

    // Build message: n t1 t2 ... tn s1 s2 ... sn
    snprintf(buffer, sizeof(buffer), "%d", n);
    for (int i = 0; i < n; i++) {
        char tmp[32];
        snprintf(tmp, sizeof(tmp), " %d", times[i]);
        strcat(buffer, tmp);
    }
    for (int i = 0; i < n; i++) {
        char tmp[32];
        snprintf(tmp, sizeof(tmp), " %d", sizes[i]);
        strcat(buffer, tmp);
    }

    send(sockfd, buffer, strlen(buffer), 0);

    // Receive simulation output streamed line by line
    while (1) {
        memset(buffer, 0, sizeof(buffer));
        ssize_t r = recv(sockfd, buffer, sizeof(buffer)-1, 0);
        if (r <= 0) break;
        buffer[r] = '\0';
        printf("%s", buffer);
    }

    close(sockfd);
    return 0;
}
