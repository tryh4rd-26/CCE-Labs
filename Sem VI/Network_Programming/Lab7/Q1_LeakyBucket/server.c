#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>

#define PORT 8081
#define BUFFER_SIZE 2048

typedef struct {
    int time;
    int size;
} Packet;

int cmp(const void *a, const void *b) {
    Packet *p1 = (Packet*)a;
    Packet *p2 = (Packet*)b;
    return p1->time - p2->time;
}

int main() {
    int server_fd, new_socket;
    struct sockaddr_in address;
    int addrlen = sizeof(address);
    char buffer[BUFFER_SIZE];

    // Operator inputs
    int BUCKET_SIZE, OUTPUT_RATE, DELAY;
    printf("Enter bucket size (bytes): ");
    scanf("%d", &BUCKET_SIZE);
    printf("Enter output rate (bytes/sec): ");
    scanf("%d", &OUTPUT_RATE);
    printf("Enter delay between outputs (seconds): ");
    scanf("%d", &DELAY);

    // Socket setup
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        perror("socket");
        exit(1);
    }
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = htonl(INADDR_LOOPBACK);
    address.sin_port = htons(PORT);

    if (bind(server_fd, (struct sockaddr*)&address, sizeof(address)) < 0) {
        perror("bind");
        exit(1);
    }
    if (listen(server_fd, 3) < 0) {
        perror("listen");
        exit(1);
    }

    printf("Server listening on port %d...\n", PORT);

    new_socket = accept(server_fd, (struct sockaddr*)&address, (socklen_t*)&addrlen);
    if (new_socket < 0) {
        perror("accept");
        exit(1);
    }

    // Receive packet info from client
    memset(buffer, 0, sizeof(buffer));
    ssize_t r = recv(new_socket, buffer, sizeof(buffer)-1, 0);
    if (r <= 0) {
        perror("recv");
        close(new_socket);
        close(server_fd);
        exit(1);
    }
    buffer[r] = '\0';

    int n;
    Packet packets[BUFFER_SIZE];

    // Format: n t1 t2 ... tn s1 s2 ... sn
    char *tok = strtok(buffer, " ");
    n = atoi(tok);
    for (int i = 0; i < n; i++) {
        tok = strtok(NULL, " ");
        packets[i].time = atoi(tok);
    }
    for (int i = 0; i < n; i++) {
        tok = strtok(NULL, " ");
        packets[i].size = atoi(tok);
    }

    // Sort and merge duplicates
    qsort(packets, n, sizeof(Packet), cmp);
    int m = 0;
    for (int i = 0; i < n; i++) {
        if (m > 0 && packets[i].time == packets[m-1].time) {
            packets[m-1].size += packets[i].size; // merge sizes
        } else {
            packets[m++] = packets[i];
        }
    }
    n = m;

    int bucket = 0;
    int last_time = 0;

    for (int i = 0; i < n; i++) {
        int current_time = packets[i].time;
        int leaked = (current_time - last_time) * OUTPUT_RATE;
        bucket -= leaked;
        if (bucket < 0) bucket = 0;

        char line[256];
        snprintf(line, sizeof(line),
                 "\nTime %d sec\nLeaked %d bytes\nBucket before: %d bytes\n",
                 current_time, leaked, bucket);
        send(new_socket, line, strlen(line), 0);

        int packet_size = packets[i].size;
        if (bucket + packet_size <= BUCKET_SIZE) {
            bucket += packet_size;
            snprintf(line, sizeof(line),
                     "Packet %d (%d bytes) → CONFORMING (Accepted)\n", i+1, packet_size);
        } else {
            snprintf(line, sizeof(line),
                     "Packet %d (%d bytes) → NON-CONFORMING (Dropped)\n", i+1, packet_size);
        }
        send(new_socket, line, strlen(line), 0);

        snprintf(line, sizeof(line),
                 "Bucket after: %d bytes | Max: %d | Free space: %d\n",
                 bucket, BUCKET_SIZE, BUCKET_SIZE - bucket);
        send(new_socket, line, strlen(line), 0);

        last_time = current_time;
        sleep(DELAY); // configurable delay
    }

    char endmsg[] = "\n===== SIMULATION COMPLETED =====\n";
    send(new_socket, endmsg, strlen(endmsg), 0);

    close(new_socket);
    close(server_fd);
    return 0;
}
