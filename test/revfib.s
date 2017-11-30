mov r0,#0;
swi 0x6c;	
mov r1,r0;
mov r2,#-1;
mov r3,#1;
mov r4,#0;
mov r5,#0;




loop:	
	cmp r4,r1;@compare r4 with you number like basic while loop
	bgt exit1;@exit condition

	add r2,r2,r3;@add r2 with r3 to find the number,cleverly initialize them as above for ease
	add r4,r4,#1;@add r4
	mov r5,r2;@mov r5 to r4
	cmp r4,r1;@again compare
	beq exit1;@exit condition

	add r3,r3,r2;@similar as above this is because in one cycle i will actually have 2 numbers as fib
	add r4,r4,#1;
	mov r5,r3;
	cmp r4,r1;
	beq exit1;
	
b loop;

exit1:
	mov r1,r5;@initialize according to what reverse computation will need
	mov r2,r5;
	mov r3,#0;
	mov r4,#0;
	mov r5,#10;
b rev;


rev:
	cmp r2,#0;@cmp with 0 is same as n!=0
	ble exit;
	b dividmod;@call for finding quotient and remainder
b rev;

carryout:
	mul r4,r5,r4;@multiply by 10 similar as c
	add r4,r2,r4;@add so as to get effective reverse at each step and that to to ist correct place value
	mov r2,r3;@again initialize r2 with r3
	mov r3,#0;@r3 with r2 as in start
b rev;

dividmod:
	cmp r2,#10;
	blt carryout;@loop to carryout if  a point is reached when r2 is less then 10
	sub r2,r2,#10;@basic division by 10 at the end r2 will be remainder
	add r3,r3,#1;@at he end r3 will be quotient
b dividmod;
exit:	
	mov r0,#1;@print
	mov r1,r4;
	swi 0x6b;
	swi 0x11;
