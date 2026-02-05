#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <time.h>

#define PORT 8080
#define BUFFER_SIZE 1024

// Function to check if two strings are anagrams
int areAnagrams(char *str1, char *str2) {
    int count[256] = {0};
    for (int i = 0; str1[i] && str2[i]; i++) {
        count[(unsigned char)str1[i]]++;
        count[(unsigned char)str2[i]]--;
    }
    if (strlen(str1) != strlen(str2)) return 0;
    for (int i = 0; i < 256; i++) {
        if (count[i] != 0) return 0;
    }
    return 1;
}

int main() {
    int sockfd, newsockfd;
    struct sockaddr_in server_addr, client_addr;
    socklen_t addr_len;
    char buff[BUFFER_SIZE];

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

    printf("Iterative Server running on port %d...\n", PORT);

    while (1) {
        addr_len = sizeof(client_addr);
        newsockfd = accept(sockfd, (struct sockaddr*)&client_addr, &addr_len);
        if (newsockfd == -1) { perror("Accept failed"); continue; }

        // Log connection with date/time
        time_t now = time(NULL);
        char *dt = ctime(&now);
        dt[strcspn(dt, "\n")] = '\0'; // remove newline
        printf("Connected to client %s:%d at %s\n",
               inet_ntoa(client_addr.sin_addr),
               ntohs(client_addr.sin_port),
               dt);

        // Receive two strings
        memset(buff, 0, sizeof(buff));
        recv(newsockfd, buff, sizeof(buff), 0);

        char str1[BUFFER_SIZE], str2[BUFFER_SIZE];
        sscanf(buff, "%s %s", str1, str2);

        // Check anagram
        char result[BUFFER_SIZE];
        if (areAnagrams(str1, str2)) {
            snprintf(result, sizeof(result), "Strings '%s' and '%s' are anagrams.", str1, str2);
        } else {
            snprintf(result, sizeof(result), "Strings '%s' and '%s' are NOT anagrams.", str1, str2);
        }

        // Send result back to client
        send(newsockfd, result, strlen(result), 0);

        close(newsockfd); // iterative: handle one client, then move to next
    }

    close(sockfd);
    return 0;
}
