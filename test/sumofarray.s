mov r0,#0;
swi 0x6c;	
mov r1,r0;
mov r2,#0;
mov r3,#1;



loop:	
	cmp r3,r1;@ loop and compare r3 with r1 like basic while loop
	bgt exit;@ exit condition
	mov r0,#0;@take input
	swi 0x6c;
	add r2,r2,r0;@add the input you took
	add r3,r3,#1;@counter
b loop

exit:	
	mov r0,#1;@print to console
	mov r1,r2;
	swi 0x6b;
	swi 0x11;
