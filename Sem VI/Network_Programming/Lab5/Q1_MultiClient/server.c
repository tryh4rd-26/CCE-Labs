#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define PORT 8080
#define BUFFER_SIZE 1024
#define MAX_CLIENTS 2

typedef struct {
    int sockfd;
    struct sockaddr_in addr;
} client_info;

char client_msgs[MAX_CLIENTS][BUFFER_SIZE];
char client_addrs[MAX_CLIENTS][BUFFER_SIZE];
int client_count = 0;
int client_fds[MAX_CLIENTS];
pthread_mutex_t lock = PTHREAD_MUTEX_INITIALIZER;

void* handle_client(void* arg) {
    client_info *info = (client_info*)arg;
    char buff[BUFFER_SIZE];
    memset(buff, 0, sizeof(buff));

    recv(info->sockfd, buff, sizeof(buff), 0);

    pthread_mutex_lock(&lock);
    if (client_count >= MAX_CLIENTS) {
        // Too many clients
        send(info->sockfd, "terminate session", strlen("terminate session"), 0);
        for (int i = 0; i < client_count; i++) {
            send(client_fds[i], "terminate session", strlen("terminate session"), 0);
            close(client_fds[i]);
        }
        close(info->sockfd);
        exit(0); // terminate server
    }

    strcpy(client_msgs[client_count], buff);
    snprintf(client_addrs[client_count], sizeof(client_addrs[client_count]),
             "%s:%d", inet_ntoa(info->addr.sin_addr), ntohs(info->addr.sin_port));
    client_fds[client_count] = info->sockfd;
    client_count++;

    if (client_count == MAX_CLIENTS) {
        // Read keyword from file
        FILE *fp = fopen("keyword.txt", "r");
        char keyword[BUFFER_SIZE];
        if (fp == NULL) {
            strcpy(keyword, "Manipal");
        } else {
            fgets(keyword, sizeof(keyword), fp);
            keyword[strcspn(keyword, "\n")] = '\0';
            fclose(fp);
        }

        char result[BUFFER_SIZE];
        snprintf(result, sizeof(result), "%s %s %s", keyword,
                 client_msgs[0], client_msgs[1]);

        printf("Final Result: %s\n", result);
        printf("Client1 Address: %s\n", client_addrs[0]);
        printf("Client2 Address: %s\n", client_addrs[1]);

        for (int i = 0; i < client_count; i++) {
            send(client_fds[i], result, strlen(result), 0);
            close(client_fds[i]);
        }
        exit(0); // terminate server after handling 2 clients
    }
    pthread_mutex_unlock(&lock);

    free(info);
    return NULL;
}

int main() {
    int sockfd;
    struct sockaddr_in server_addr, client_addr;
    socklen_t addr_len;

    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd == -1) { perror("Socket creation failed"); exit(0); }

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = htonl(INADDR_ANY);

    if (bind(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr)) == -1) {
        perror("Bind failed"); close(sockfd); exit(0);
    }

    if (listen(sockfd, 5) == -1) {
        perror("Listen failed"); close(sockfd); exit(0);
    }

    printf("Concurrent Server running on port %d...\n", PORT);

    while (1) {
        addr_len = sizeof(client_addr);
        int newsockfd = accept(sockfd, (struct sockaddr*)&client_addr, &addr_len);
        if (newsockfd == -1) { perror("Accept failed"); continue; }

        client_info *info = malloc(sizeof(client_info));
        info->sockfd = newsockfd;
        info->addr = client_addr;

        pthread_t tid;
        pthread_create(&tid, NULL, handle_client, info);
        pthread_detach(tid); // no need to join
    }

    close(sockfd);
    return 0;
}
