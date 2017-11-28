package armsim;
import java.util.HashMap;
class Opcode {
	public HashMap<String,String> bin = new HashMap<String,String>();
	
	public Opcode()
	{
		
	}
	
	public static void main(String[] args) {
		System.out.println("Working");
		Handle h=Handle.getHandle();
		/*.out.println(h.getOpcode(h.getBeg("0x0 0xE3A0200A")));
		System.out.println(h.getR1("0x0 0xE3A0200A"));
		System.out.println(h.getRdest("0x0 0xE3A0200A"));
		System.out.println(h.getR2("0x0 0xE3A0200A"));
		System.out.println(h.getShift("0x0 0xE3A0200A"));
		System.out.println(h.getAddress("0x4 0xE3A0200A"));*/
		String[] lines=h.Readmemfile();
		for(String i : lines)
		{
			System.out.println(h.getBeg(i));
			System.out.println(h.getF(h.getBeg(i)));
			System.out.println(h.getI(h.getBeg(i)));
		}
		
	}

}