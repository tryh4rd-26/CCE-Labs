#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <strings.h>
#include <string.h>
#include <arpa/inet.h>

#define PORT 8053
#define BUFFER_SIZE 1024

typedef struct {
    char hostname[50];
    char ip[50];
} dns_entry;

dns_entry table[] = {
    {"server1.com", "127.0.0.2"},
    {"server2.com", "127.0.0.3"},
    {"server3.com", "127.0.0.4"}
};

int resolve(char *return_ip, const char *hostname) {
    for (int i = 0; i < 3; i++) {
        if (strcmp(table[i].hostname, hostname) == 0) {
            strcpy(return_ip, table[i].ip);
            return 1;
        }
    }
    return 0;
}

int main() {
    int sockfd;
    struct sockaddr_in server_addr, client_addr;
    socklen_t addr_len;
    char buff[BUFFER_SIZE];

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0) {
        perror("Socket creation failed");
        exit(1);
    }

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = inet_addr("127.0.0.1");

    if (bind(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0) {
        perror("Bind failed");
        close(sockfd);
        exit(1);
    }

    printf("UDP DNS Server running on port %d\n", PORT);

    while (1) {
        addr_len = sizeof(client_addr);
        memset(buff, 0, BUFFER_SIZE);

        int recd = recvfrom(sockfd, buff, BUFFER_SIZE, 0,
                            (struct sockaddr*)&client_addr, &addr_len);
        if (recd <= 0) continue;

        char return_ip[50];
        int found = 0;

        if (strncmp(buff, "RESOLVE ", 8) == 0) {
            char *hostname = buff + 8;  // splice after "RESOLVE "
            found = resolve(return_ip, hostname);
        }

        if (found) {
            sendto(sockfd, return_ip, strlen(return_ip), 0,
                   (struct sockaddr*)&client_addr, addr_len);
        } else {
            sendto(sockfd, "NOTFOUND", 8, 0,
                   (struct sockaddr*)&client_addr, addr_len);
        }
    }

    close(sockfd);
    return 0;
}
