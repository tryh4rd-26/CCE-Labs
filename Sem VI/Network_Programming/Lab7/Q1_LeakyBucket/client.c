#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <time.h>

#define PORT 8081
#define BUFFER_SIZE 2048

int main() {
    int sockfd;
    struct sockaddr_in server_addr;
    char buffer[BUFFER_SIZE];

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) {
        perror("socket");
        exit(1);
    }

    memset(&server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = htonl(INADDR_LOOPBACK);

    if (connect(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0) {
        perror("connect");
        close(sockfd);
        exit(1);
    }

    printf("Enter number of packets: ");
    int n;
    scanf("%d", &n);

    int times[n], sizes[n];
    int random_times, random_sizes;
    printf("Randomize arrival times? (1=yes,0=no): ");
    scanf("%d", &random_times);
    printf("Randomize packet sizes? (1=yes,0=no): ");
    scanf("%d", &random_sizes);

    srand(time(NULL));

    if (random_times) {
        times[0] = rand() % 3 + 1;
        for (int i = 1; i < n; i++) {
            times[i] = times[i-1] + (rand() % 5 + 1); // strictly ascending
        }
    } else {
        printf("Enter %d arrival times (space separated): ", n);
        for (int i = 0; i < n; i++) scanf("%d", &times[i]);
    }

    if (random_sizes) {
        for (int i = 0; i < n; i++) {
            sizes[i] = (rand() % 5) + 1; // 1–5 bytes
        }
    } else {
        printf("Enter %d packet sizes (space separated): ", n);
        for (int i = 0; i < n; i++) scanf("%d", &sizes[i]);
    }

    // Build message: n t1 ... tn s1 ... sn
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
