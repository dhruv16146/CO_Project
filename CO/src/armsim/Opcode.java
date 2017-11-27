package armsim;

import java.util.HashMap;


class Opcode {

	public static void main(String[] args) {
		Handle h=Handle.getHandle();
		System.out.println(h.getOpcode("0x0 0xE3A0200A"));
		System.out.println(h.getR1("0x0 0xE3A0200A"));
		System.out.println(h.getRdest("0x0 0xE3A0200A"));
		System.out.println(h.getR2("0x0 0xE3A0200A"));
		System.out.println(h.getShift("0x0 0xE3A0200A"));
		System.out.println(h.getAddress("0x0 0xE3A0200A"));
		//
		
		
		

	}

}
