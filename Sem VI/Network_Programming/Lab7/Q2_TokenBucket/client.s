	.file	"client.c"
	.text
	.section	.rodata
.LC0:
	.string	"Client: Creating socket..."
.LC1:
	.string	"Socket creation failed"
	.align 8
.LC2:
	.string	"Client: Socket created successfully."
.LC3:
	.string	"127.0.0.1"
	.align 8
.LC4:
	.string	"Client: Connecting to server..."
.LC5:
	.string	"Connection failed"
	.align 8
.LC6:
	.string	"Client: Connected to server at IP %s, Port %d\n"
.LC7:
	.string	"Client: Received \342\206\222 %s\n"
.LC8:
	.string	"Client: Connection closed."
	.text
	.globl	main
	.type	main, @function
main:
.LFB6:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	pushq	%rbx
	subq	$1064, %rsp
	.cfi_offset 3, -24
	leaq	.LC0(%rip), %rax
	movq	%rax, %rdi
	call	puts@PLT
	movl	$0, %edx
	movl	$1, %esi
	movl	$2, %edi
	call	socket@PLT
	movl	%eax, -20(%rbp)
	cmpl	$-1, -20(%rbp)
	jne	.L2
	leaq	.LC1(%rip), %rax
	movq	%rax, %rdi
	call	perror@PLT
	movl	$1, %edi
	call	exit@PLT
.L2:
	leaq	.LC2(%rip), %rax
	movq	%rax, %rdi
	call	puts@PLT
	movw	$2, -48(%rbp)
	movl	$8080, %edi
	call	htons@PLT
	movw	%ax, -46(%rbp)
	leaq	.LC3(%rip), %rax
	movq	%rax, %rdi
	call	inet_addr@PLT
	movl	%eax, -44(%rbp)
	leaq	.LC4(%rip), %rax
	movq	%rax, %rdi
	call	puts@PLT
	leaq	-48(%rbp), %rcx
	movl	-20(%rbp), %eax
	movl	$16, %edx
	movq	%rcx, %rsi
	movl	%eax, %edi
	call	connect@PLT
	cmpl	$-1, %eax
	jne	.L3
	leaq	.LC5(%rip), %rax
	movq	%rax, %rdi
	call	perror@PLT
	movl	-20(%rbp), %eax
	movl	%eax, %edi
	call	close@PLT
	movl	$1, %edi
	call	exit@PLT
.L3:
	movzwl	-46(%rbp), %eax
	movzwl	%ax, %eax
	movl	%eax, %edi
	call	ntohs@PLT
	movzwl	%ax, %ebx
	movl	-44(%rbp), %eax
	movl	%eax, %edi
	call	inet_ntoa@PLT
	movl	%ebx, %edx
	movq	%rax, %rsi
	leaq	.LC6(%rip), %rax
	movq	%rax, %rdi
	movl	$0, %eax
	call	printf@PLT
.L6:
	leaq	-1072(%rbp), %rax
	movl	$1024, %edx
	movl	$0, %esi
	movq	%rax, %rdi
	call	memset@PLT
	leaq	-1072(%rbp), %rsi
	movl	-20(%rbp), %eax
	movl	$0, %ecx
	movl	$1024, %edx
	movl	%eax, %edi
	call	recv@PLT
	movl	%eax, -24(%rbp)
	cmpl	$0, -24(%rbp)
	jle	.L9
	movl	-24(%rbp), %eax
	cltq
	movb	$0, -1072(%rbp,%rax)
	leaq	-1072(%rbp), %rax
	movq	%rax, %rsi
	leaq	.LC7(%rip), %rax
	movq	%rax, %rdi
	movl	$0, %eax
	call	printf@PLT
	jmp	.L6
.L9:
	nop
	movl	-20(%rbp), %eax
	movl	%eax, %edi
	call	close@PLT
	leaq	.LC8(%rip), %rax
	movq	%rax, %rdi
	call	puts@PLT
	movl	$0, %eax
	movq	-8(%rbp), %rbx
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE6:
	.size	main, .-main
	.ident	"GCC: (Debian 12.2.0-14+deb12u1) 12.2.0"
	.section	.note.GNU-stack,"",@progbits
