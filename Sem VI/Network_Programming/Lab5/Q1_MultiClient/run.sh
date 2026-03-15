#!/bin/bash

echo "Compiling server..."
gcc -g -pthread server.c -o server

echo "Compiling client..."
gcc -g client.c -o client

if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

echo "Starting server..."
./server &

SERVER_PID=$!

sleep 2   # Give server time to start

echo "Starting client..."
./client &

CLIENT_PID=$!

echo "Server PID: $SERVER_PID"
echo "Client PID: $CLIENT_PID"

wait
