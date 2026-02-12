#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define BUFFER_SIZE 4096
#define PORT 8080

static int is_power_of_two(int x) {
    return (x && !(x & (x - 1)));
}

static void print_time() {
    time_t now = time(NULL);
    char buf[64];
    strftime(buf, sizeof(buf), "%Y-%m-%d %H:%M:%S", localtime(&now));
    printf("%s", buf);
}

/* Check CRC-like parity for Hamming: compute syndrome and correct single-bit error.
   codeword is a string of '0'/'1' starting at index 1..n (code[0] unused).
   n is total length (m + r). Returns error position (0 if none). */
int detect_and_correct(char *code, int n) {
    int r = 0;
    while ((1 << r) <= n) r++;

    int syndrome = 0;
    for (int i = 0; i < r; i++) {
        int p = 1 << i;
        int count = 0;
        for (int pos = 1; pos <= n; pos++) {
            if (pos & p) {
                if (code[pos] == '1') count++;
            }
        }
        if (count % 2 != 0) { // parity check fails (we use even parity)
            syndrome |= p;
        }
    }

    if (syndrome != 0 && syndrome <= n) {
        // flip the bit at syndrome position
        code[syndrome] = (code[syndrome] == '1') ? '0' : '1';
    }
    return syndrome;
}

/* Extract original data bits from corrected codeword into out (null-terminated) */
void extract_data_from_code(char *code, int n, char *out) {
    int idx = 0;
    for (int pos = 1; pos <= n; pos++) {
        if (!is_power_of_two(pos)) {
            out[idx++] = code[pos];
        }
    }
    out[idx] = '\0';
}

int main() {
    int sockfd = -1, newsockfd = -1;
    struct sockaddr_in server_addr, client_addr;
    socklen_t addr_len;
    char recvbuf[BUFFER_SIZE];

    printf("Creating socket...\n");
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd == -1) { perror("Socket creation failed"); exit(1); }
    printf("Socket created successfully.\n");

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    memset(server_addr.sin_zero, 0, sizeof(server_addr.sin_zero));

    printf("Binding socket...\n");
    if (bind(sockfd, (struct sockaddr *)&server_addr, sizeof(server_addr)) == -1) {
        perror("Bind failed"); close(sockfd); exit(1);
    }
    printf("Socket bound to port %d.\n", PORT);

    printf("Listening on port %d...\n", PORT);
    if (listen(sockfd, 5) == -1) { perror("Listen failed"); close(sockfd); exit(1); }

    while (1) {
        addr_len = sizeof(client_addr);
        printf("Waiting for client connection...\n");
        newsockfd = accept(sockfd, (struct sockaddr *)&client_addr, &addr_len);
        if (newsockfd == -1) {
            perror("Accept failed");
            continue;
        }

        printf("Client connected from %s:%d at ", inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port));
        print_time();
        printf("\n");

        // Keepalive loop for this client
        while (1) {
            memset(recvbuf, 0, sizeof(recvbuf));
            int recd = recv(newsockfd, recvbuf, sizeof(recvbuf) - 1, 0);
            if (recd <= 0) {
                printf("Client %s:%d disconnected (recv returned %d).\n",
                       inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port), recd);
                break;
            }

            // Trim newline if any
            recvbuf[recd] = '\0';
            if (strcmp(recvbuf, "CLOSE") == 0) {
                printf("Received CLOSE from client %s:%d. Closing connection.\n",
                       inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port));
                send(newsockfd, "Connection closing", 18, 0);
                break;
            }

            // Expecting a codeword string of '0'/'1'. We will treat it as starting at index 1.
            int len = strlen(recvbuf);
            if (len < 1) {
                send(newsockfd, "Invalid input", 13, 0);
                continue;
            }

            // Build 1-based code array
            char code[BUFFER_SIZE];
            memset(code, '0', sizeof(code));
            for (int i = 0; i <= len; i++) code[i] = 0; // clear
            // allocate positions 1..len
            for (int i = 0; i < len; i++) code[i + 1] = recvbuf[i];
            code[len + 1] = '\0';

            printf("Received codeword (%d bits): %s\n", len, recvbuf);

            int error_pos = detect_and_correct(code, len);

            char original_data[BUFFER_SIZE];
            extract_data_from_code(code, len, original_data);

            char response[BUFFER_SIZE];
            if (error_pos == 0) {
                snprintf(response, sizeof(response),
                         "No error detected. Extracted data: %s", original_data);
                printf("No error detected. Extracted data: %s\n", original_data);
            } else {
                // corrected codeword string
                char corrected[BUFFER_SIZE];
                for (int i = 1; i <= len; i++) corrected[i - 1] = code[i];
                corrected[len] = '\0';

                snprintf(response, sizeof(response),
                         "Error detected at bit position %d. Corrected codeword: %s. Extracted data: %s",
                         error_pos, corrected, original_data);

                printf("Error detected at position %d. Corrected codeword: %s. Extracted data: %s\n",
                       error_pos, corrected, original_data);
            }

            // send response back to client
            send(newsockfd, response, strlen(response), 0);
        }

        close(newsockfd);
        printf("Closed connection with %s:%d at ", inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port));
        print_time();
        printf("\n\n");
    }

    close(sockfd);
    return 0;
}
