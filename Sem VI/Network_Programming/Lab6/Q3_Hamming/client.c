#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define BUFFER_SIZE 4096
#define PORT 8080

static int is_power_of_two(int x) {
    return (x && !(x & (x - 1)));
}

/* Calculate number of redundant bits r for m data bits */
static int calc_redundant_bits(int m) {
    int r = 0;
    while ((1 << r) < (m + r + 1)) r++;
    return r;
}

/* Build Hamming code (even parity). data is a null-terminated string of '0'/'1'.
   result is written into code (1-based positions stored starting at index 1),
   and code_str is a null-terminated C string (0-based) for sending. */
void encode_hamming(const char *data, char *code, char *code_str) {
    int m = strlen(data);
    int r = calc_redundant_bits(m);
    int n = m + r;

    // initialize code positions 1..n to '0'
    for (int i = 1; i <= n; i++) code[i] = '0';
    code[n + 1] = '\0';

    // place data bits into non-power-of-two positions (left to right)
    int di = 0;
    for (int pos = 1; pos <= n; pos++) {
        if (!is_power_of_two(pos)) {
            code[pos] = data[di++];
        }
    }

    // calculate parity bits (even parity)
    for (int i = 0; i < r; i++) {
        int p = 1 << i;
        int count = 0;
        for (int pos = 1; pos <= n; pos++) {
            if (pos & p) {
                if (code[pos] == '1') count++;
            }
        }
        // set parity bit so total ones including parity is even
        code[p] = (count % 2 == 0) ? '0' : '1';
    }

    // build code_str (0-based) for sending
    for (int i = 1; i <= n; i++) code_str[i - 1] = code[i];
    code_str[n] = '\0';
}

int main() {
    int sockfd;
    struct sockaddr_in server_addr;
    char data[BUFFER_SIZE];
    char code[BUFFER_SIZE];     // 1-based
    char code_str[BUFFER_SIZE]; // 0-based string to send
    char response[BUFFER_SIZE];

    printf("Creating socket...\n");
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd == -1) { perror("Socket creation failed"); exit(1); }
    printf("Socket created successfully.\n");

    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = htonl(INADDR_LOOPBACK); // connect to localhost
    memset(server_addr.sin_zero, 0, sizeof(server_addr.sin_zero));

    printf("Connecting to server...\n");
    if (connect(sockfd, (struct sockaddr *)&server_addr, sizeof(server_addr)) == -1) {
        perror("Connection failed");
        close(sockfd);
        exit(1);
    }
    printf("Connected to server.\n");

    while (1) {
        printf("\nEnter data bits (e.g., 1011001) or type CLOSE to quit: ");
        if (scanf("%s", data) != 1) break;
        if (strcmp(data, "CLOSE") == 0) {
            send(sockfd, data, strlen(data), 0);
            break;
        }

        // validate input: only '0' and '1'
        int valid = 1;
        for (size_t i = 0; i < strlen(data); i++) {
            if (data[i] != '0' && data[i] != '1') { valid = 0; break; }
        }
        if (!valid) {
            printf("Invalid input: only 0 and 1 allowed.\n");
            continue;
        }

        int m = strlen(data);
        int r = calc_redundant_bits(m);
        int n = m + r;
        printf("Data bits: %d, Redundant bits: %d, Total codeword length: %d\n", m, r, n);

        encode_hamming(data, code, code_str);
        printf("Encoded Hamming codeword: %s\n", code_str);

        // send codeword
        send(sockfd, code_str, strlen(code_str), 0);

        // receive server response
        memset(response, 0, sizeof(response));
        int recd = recv(sockfd, response, sizeof(response) - 1, 0);
        if (recd <= 0) {
            printf("Server closed connection or error (recv returned %d).\n", recd);
            break;
        }
        response[recd] = '\0';
        printf("Server Response: %s\n", response);
    }

    printf("Closing socket...\n");
    close(sockfd);
    return 0;
}
