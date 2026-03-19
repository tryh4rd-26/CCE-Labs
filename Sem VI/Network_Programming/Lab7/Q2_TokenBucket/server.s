	.file	"server.c"
# GNU C17 (Debian 12.2.0-14+deb12u1) version 12.2.0 (x86_64-linux-gnu)
#	compiled by GNU C version 12.2.0, GMP version 6.2.1, MPFR version 4.2.0, MPC version 1.3.1, isl version isl-0.25-GMP

# GGC heuristics: --param ggc-min-expand=100 --param ggc-min-heapsize=131072
# options passed: -mtune=generic -march=x86-64 -fasynchronous-unwind-tables
	.text
	.section	.rodata
.LC0:
	.string	"Server: Creating socket..."
.LC1:
	.string	"Socket creation failed"
	.align 8
.LC2:
	.string	"Server: Socket created successfully."
.LC3:
	.string	"Server: Binding socket..."
.LC4:
	.string	"Bind failed"
.LC5:
	.string	"Server: Bind successful."
	.align 8
.LC6:
	.string	"Server: Listening on port %d...\n"
.LC7:
	.string	"Listen failed"
.LC8:
	.string	"Server: Waiting for client..."
.LC9:
	.string	"Accept failed"
	.align 8
.LC10:
	.string	"Server: Connected to client IP %s, Port %d\n"
.LC13:
	.string	"\nTime %.1f sec\n"
.LC14:
	.string	"Tokens before packet: %d KB\n"
	.align 8
.LC15:
	.string	"Packet %d (%d KB) \342\206\222 CONFORMING (Accepted). Tokens left: %d KB"
	.align 8
.LC16:
	.string	"Packet %d (%d KB) \342\206\222 NON-CONFORMING (Queued/Dropped). Tokens left: %d KB"
.LC17:
	.string	"Server: %s\n"
	.align 8
.LC18:
	.string	"\n===== SIMULATION COMPLETED ====="
	.text
	.globl	main
	.type	main, @function
main:
.LFB6:
	.cfi_startproc
	pushq	%rbp	#
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp	#,
	.cfi_def_cfa_register 6
	pushq	%rbx	#
	subq	$1128, %rsp	#,
	.cfi_offset 3, -24
# server.c:18:     printf("Server: Creating socket...\n");
	leaq	.LC0(%rip), %rax	#, tmp99
	movq	%rax, %rdi	# tmp99,
	call	puts@PLT	#
# server.c:19:     sockfd = socket(AF_INET, SOCK_STREAM, 0);
	movl	$0, %edx	#,
	movl	$1, %esi	#,
	movl	$2, %edi	#,
	call	socket@PLT	#
	movl	%eax, -40(%rbp)	# tmp100, sockfd
# server.c:20:     if (sockfd == -1) { perror("Socket creation failed"); exit(1); }
	cmpl	$-1, -40(%rbp)	#, sockfd
	jne	.L2	#,
# server.c:20:     if (sockfd == -1) { perror("Socket creation failed"); exit(1); }
	leaq	.LC1(%rip), %rax	#, tmp101
	movq	%rax, %rdi	# tmp101,
	call	perror@PLT	#
# server.c:20:     if (sockfd == -1) { perror("Socket creation failed"); exit(1); }
	movl	$1, %edi	#,
	call	exit@PLT	#
.L2:
# server.c:21:     printf("Server: Socket created successfully.\n");
	leaq	.LC2(%rip), %rax	#, tmp102
	movq	%rax, %rdi	# tmp102,
	call	puts@PLT	#
# server.c:23:     server_addr.sin_family = AF_INET;
	movw	$2, -80(%rbp)	#, server_addr.sin_family
# server.c:24:     server_addr.sin_port = htons(PORT);
	movl	$8080, %edi	#,
	call	htons@PLT	#
# server.c:24:     server_addr.sin_port = htons(PORT);
	movw	%ax, -78(%rbp)	# _1, server_addr.sin_port
# server.c:25:     server_addr.sin_addr.s_addr = htonl(INADDR_ANY);
	movl	$0, %edi	#,
	call	htonl@PLT	#
# server.c:25:     server_addr.sin_addr.s_addr = htonl(INADDR_ANY);
	movl	%eax, -76(%rbp)	# _2, server_addr.sin_addr.s_addr
# server.c:27:     printf("Server: Binding socket...\n");
	leaq	.LC3(%rip), %rax	#, tmp103
	movq	%rax, %rdi	# tmp103,
	call	puts@PLT	#
# server.c:28:     if (bind(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr)) == -1) {
	leaq	-80(%rbp), %rcx	#, tmp104
	movl	-40(%rbp), %eax	# sockfd, tmp105
	movl	$16, %edx	#,
	movq	%rcx, %rsi	# tmp104,
	movl	%eax, %edi	# tmp105,
	call	bind@PLT	#
# server.c:28:     if (bind(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr)) == -1) {
	cmpl	$-1, %eax	#, _3
	jne	.L3	#,
# server.c:29:         perror("Bind failed"); close(sockfd); exit(1);
	leaq	.LC4(%rip), %rax	#, tmp106
	movq	%rax, %rdi	# tmp106,
	call	perror@PLT	#
# server.c:29:         perror("Bind failed"); close(sockfd); exit(1);
	movl	-40(%rbp), %eax	# sockfd, tmp107
	movl	%eax, %edi	# tmp107,
	call	close@PLT	#
# server.c:29:         perror("Bind failed"); close(sockfd); exit(1);
	movl	$1, %edi	#,
	call	exit@PLT	#
.L3:
# server.c:31:     printf("Server: Bind successful.\n");
	leaq	.LC5(%rip), %rax	#, tmp108
	movq	%rax, %rdi	# tmp108,
	call	puts@PLT	#
# server.c:33:     printf("Server: Listening on port %d...\n", PORT);
	movl	$8080, %esi	#,
	leaq	.LC6(%rip), %rax	#, tmp109
	movq	%rax, %rdi	# tmp109,
	movl	$0, %eax	#,
	call	printf@PLT	#
# server.c:34:     if (listen(sockfd, 5) == -1) { perror("Listen failed"); close(sockfd); exit(1); }
	movl	-40(%rbp), %eax	# sockfd, tmp110
	movl	$5, %esi	#,
	movl	%eax, %edi	# tmp110,
	call	listen@PLT	#
# server.c:34:     if (listen(sockfd, 5) == -1) { perror("Listen failed"); close(sockfd); exit(1); }
	cmpl	$-1, %eax	#, _4
	jne	.L4	#,
# server.c:34:     if (listen(sockfd, 5) == -1) { perror("Listen failed"); close(sockfd); exit(1); }
	leaq	.LC7(%rip), %rax	#, tmp111
	movq	%rax, %rdi	# tmp111,
	call	perror@PLT	#
# server.c:34:     if (listen(sockfd, 5) == -1) { perror("Listen failed"); close(sockfd); exit(1); }
	movl	-40(%rbp), %eax	# sockfd, tmp112
	movl	%eax, %edi	# tmp112,
	call	close@PLT	#
# server.c:34:     if (listen(sockfd, 5) == -1) { perror("Listen failed"); close(sockfd); exit(1); }
	movl	$1, %edi	#,
	call	exit@PLT	#
.L4:
# server.c:36:     addr_len = sizeof(client_addr);
	movl	$16, -100(%rbp)	#, addr_len
# server.c:37:     printf("Server: Waiting for client...\n");
	leaq	.LC8(%rip), %rax	#, tmp113
	movq	%rax, %rdi	# tmp113,
	call	puts@PLT	#
# server.c:38:     newsockfd = accept(sockfd, (struct sockaddr*)&client_addr, &addr_len);
	leaq	-100(%rbp), %rdx	#, tmp114
	leaq	-96(%rbp), %rcx	#, tmp115
	movl	-40(%rbp), %eax	# sockfd, tmp116
	movq	%rcx, %rsi	# tmp115,
	movl	%eax, %edi	# tmp116,
	call	accept@PLT	#
	movl	%eax, -44(%rbp)	# tmp117, newsockfd
# server.c:39:     if (newsockfd == -1) { perror("Accept failed"); close(sockfd); exit(1); }
	cmpl	$-1, -44(%rbp)	#, newsockfd
	jne	.L5	#,
# server.c:39:     if (newsockfd == -1) { perror("Accept failed"); close(sockfd); exit(1); }
	leaq	.LC9(%rip), %rax	#, tmp118
	movq	%rax, %rdi	# tmp118,
	call	perror@PLT	#
# server.c:39:     if (newsockfd == -1) { perror("Accept failed"); close(sockfd); exit(1); }
	movl	-40(%rbp), %eax	# sockfd, tmp119
	movl	%eax, %edi	# tmp119,
	call	close@PLT	#
# server.c:39:     if (newsockfd == -1) { perror("Accept failed"); close(sockfd); exit(1); }
	movl	$1, %edi	#,
	call	exit@PLT	#
.L5:
# server.c:42:            inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port));
	movzwl	-94(%rbp), %eax	# client_addr.sin_port, _5
# server.c:42:            inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port));
	movzwl	%ax, %eax	# _5, _6
	movl	%eax, %edi	# _6,
	call	ntohs@PLT	#
# server.c:41:     printf("Server: Connected to client IP %s, Port %d\n",
	movzwl	%ax, %ebx	# _7, _8
	movl	-92(%rbp), %eax	# client_addr.sin_addr, tmp120
	movl	%eax, %edi	# tmp120,
	call	inet_ntoa@PLT	#
	movl	%ebx, %edx	# _8,
	movq	%rax, %rsi	# _9,
	leaq	.LC10(%rip), %rax	#, tmp121
	movq	%rax, %rdi	# tmp121,
	movl	$0, %eax	#,
	call	printf@PLT	#
# server.c:45:     int bucket_size = 50;   // KB
	movl	$50, -48(%rbp)	#, bucket_size
# server.c:46:     int tokens = bucket_size;
	movl	-48(%rbp), %eax	# bucket_size, tmp122
	movl	%eax, -20(%rbp)	# tmp122, tokens
# server.c:47:     int rate = 10;          // KBps
	movl	$10, -52(%rbp)	#, rate
# server.c:48:     int packet_size = 15;   // KB
	movl	$15, -56(%rbp)	#, packet_size
# server.c:49:     double interval = 0.5;  // seconds
	movsd	.LC11(%rip), %xmm0	#, tmp123
	movsd	%xmm0, -64(%rbp)	# tmp123, interval
# server.c:51:     double time = 0.0;
	pxor	%xmm0, %xmm0	# tmp124
	movsd	%xmm0, -32(%rbp)	# tmp124, time
# server.c:52:     int packet_num = 0;
	movl	$0, -36(%rbp)	#, packet_num
.L11:
# server.c:55:         time += interval;
	movsd	-32(%rbp), %xmm0	# time, tmp126
	addsd	-64(%rbp), %xmm0	# interval, tmp125
	movsd	%xmm0, -32(%rbp)	# tmp125, time
# server.c:56:         packet_num++;
	addl	$1, -36(%rbp)	#, packet_num
# server.c:59:         tokens += rate * interval;
	pxor	%xmm1, %xmm1	# _10
	cvtsi2sdl	-20(%rbp), %xmm1	# tokens, _10
# server.c:59:         tokens += rate * interval;
	pxor	%xmm0, %xmm0	# _11
	cvtsi2sdl	-52(%rbp), %xmm0	# rate, _11
	mulsd	-64(%rbp), %xmm0	# interval, _12
# server.c:59:         tokens += rate * interval;
	addsd	%xmm1, %xmm0	# _10, _13
	cvttsd2sil	%xmm0, %eax	# _13, tmp127
	movl	%eax, -20(%rbp)	# tmp127, tokens
# server.c:60:         if (tokens > bucket_size) tokens = bucket_size;
	movl	-20(%rbp), %eax	# tokens, tmp128
	cmpl	-48(%rbp), %eax	# bucket_size, tmp128
	jle	.L6	#,
# server.c:60:         if (tokens > bucket_size) tokens = bucket_size;
	movl	-48(%rbp), %eax	# bucket_size, tmp129
	movl	%eax, -20(%rbp)	# tmp129, tokens
.L6:
# server.c:62:         printf("\nTime %.1f sec\n", time);
	movq	-32(%rbp), %rax	# time, tmp130
	movq	%rax, %xmm0	# tmp130,
	leaq	.LC13(%rip), %rax	#, tmp131
	movq	%rax, %rdi	# tmp131,
	movl	$1, %eax	#,
	call	printf@PLT	#
# server.c:63:         printf("Tokens before packet: %d KB\n", tokens);
	movl	-20(%rbp), %eax	# tokens, tmp132
	movl	%eax, %esi	# tmp132,
	leaq	.LC14(%rip), %rax	#, tmp133
	movq	%rax, %rdi	# tmp133,
	movl	$0, %eax	#,
	call	printf@PLT	#
# server.c:65:         if (tokens >= packet_size) {
	movl	-20(%rbp), %eax	# tokens, tmp134
	cmpl	-56(%rbp), %eax	# packet_size, tmp134
	jl	.L7	#,
# server.c:66:             tokens -= packet_size;
	movl	-56(%rbp), %eax	# packet_size, tmp135
	subl	%eax, -20(%rbp)	# tmp135, tokens
# server.c:67:             snprintf(buff, sizeof(buff),
	movl	-20(%rbp), %esi	# tokens, tmp136
	movl	-56(%rbp), %ecx	# packet_size, tmp137
	movl	-36(%rbp), %edx	# packet_num, tmp138
	leaq	-1136(%rbp), %rax	#, tmp139
	movl	%esi, %r9d	# tmp136,
	movl	%ecx, %r8d	# tmp137,
	movl	%edx, %ecx	# tmp138,
	leaq	.LC15(%rip), %rdx	#, tmp140
	movl	$1024, %esi	#,
	movq	%rax, %rdi	# tmp139,
	movl	$0, %eax	#,
	call	snprintf@PLT	#
	jmp	.L8	#
.L7:
# server.c:71:             snprintf(buff, sizeof(buff),
	movl	-20(%rbp), %esi	# tokens, tmp141
	movl	-56(%rbp), %ecx	# packet_size, tmp142
	movl	-36(%rbp), %edx	# packet_num, tmp143
	leaq	-1136(%rbp), %rax	#, tmp144
	movl	%esi, %r9d	# tmp141,
	movl	%ecx, %r8d	# tmp142,
	movl	%edx, %ecx	# tmp143,
	leaq	.LC16(%rip), %rdx	#, tmp145
	movl	$1024, %esi	#,
	movq	%rax, %rdi	# tmp144,
	movl	$0, %eax	#,
	call	snprintf@PLT	#
.L8:
# server.c:76:         printf("Server: %s\n", buff);
	leaq	-1136(%rbp), %rax	#, tmp146
	movq	%rax, %rsi	# tmp146,
	leaq	.LC17(%rip), %rax	#, tmp147
	movq	%rax, %rdi	# tmp147,
	movl	$0, %eax	#,
	call	printf@PLT	#
# server.c:77:         send(newsockfd, buff, strlen(buff), 0);
	leaq	-1136(%rbp), %rax	#, tmp148
	movq	%rax, %rdi	# tmp148,
	call	strlen@PLT	#
	movq	%rax, %rdx	#, _14
	leaq	-1136(%rbp), %rsi	#, tmp149
	movl	-44(%rbp), %eax	# newsockfd, tmp150
	movl	$0, %ecx	#,
	movl	%eax, %edi	# tmp150,
	call	send@PLT	#
# server.c:79:         if (packet_num == 10) break; // stop after 10 packets for demo
	cmpl	$10, -36(%rbp)	#, packet_num
	je	.L14	#,
# server.c:80:         sleep(interval); 
	movsd	-64(%rbp), %xmm0	# interval, tmp152
	cvttsd2siq	%xmm0, %rax	# tmp152, tmp151
	movl	%eax, %edi	# _15,
	call	sleep@PLT	#
# server.c:55:         time += interval;
	jmp	.L11	#
.L14:
# server.c:79:         if (packet_num == 10) break; // stop after 10 packets for demo
	nop	
# server.c:83:     close(newsockfd);
	movl	-44(%rbp), %eax	# newsockfd, tmp153
	movl	%eax, %edi	# tmp153,
	call	close@PLT	#
# server.c:84:     close(sockfd);
	movl	-40(%rbp), %eax	# sockfd, tmp154
	movl	%eax, %edi	# tmp154,
	call	close@PLT	#
# server.c:85:     printf("\n===== SIMULATION COMPLETED =====\n");
	leaq	.LC18(%rip), %rax	#, tmp155
	movq	%rax, %rdi	# tmp155,
	call	puts@PLT	#
# server.c:86:     return 0;
	movl	$0, %eax	#, _64
# server.c:87: }
	movq	-8(%rbp), %rbx	#,
	leave	
	.cfi_def_cfa 7, 8
	ret	
	.cfi_endproc
.LFE6:
	.size	main, .-main
	.section	.rodata
	.align 8
.LC11:
	.long	0
	.long	1071644672
	.ident	"GCC: (Debian 12.2.0-14+deb12u1) 12.2.0"
	.section	.note.GNU-stack,"",@progbits
