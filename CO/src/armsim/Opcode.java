package armsim;
import java.util.Arrays;
import java.util.HashMap;
class Opcode {
	HashMap<Integer,String> memory;
	Handle h=Handle.getHandle();
	int[]  R=new int[16];
	String current;
	int first,second,dest;
	boolean immediate;
	Opcode(){
		memory=h.Readmemfile();
		R=new int[16];
		immediate=false;
		
	}
	public void give_operands() {
		if(immediate) {
		
		first=h.getIntegerfromhex(h.getR1(current));
		second=h.getIntegerfromhex(h.getR2(current));
		dest=h.getIntegerfromhex(h.getRdest(current));
		}
		
		else {
			first=h.getIntegerfromhex(h.getR1(current));
			second= h.getIntegerfromhex(h.getImmediate(current).substring(0, 1))*16  +  h.getIntegerfromhex(h.getImmediate(current).substring(1));
			dest=h.getIntegerfromhex(h.getRdest(current));	
		}
		
	}

	void reset() {
		Arrays.fill(R,0);
		
		
	}
	void cycle() {
	
	}
	
	void fetch() {
		current=memory.get(R[15]);
		System.out.println("Fetching Instruction 0x"+memory.get(R[15])+" from address "+R[15]);
		R[15]+=4;
	
	}
	
	
	void decode() {
		//Data Processing
		System.out.print("Decode: ");
		if(h.getF(h.getBeg(current)).equals("00")){
		
		int op=Integer.parseInt(h.getOpcode(h.getBeg(current)),2);
		System.out.print("Operation is ");
		
		//and
		if(op==0) {
			System.out.print("AND");
		}
		//eor
		else if(op==1) {
			System.out.print("EOR");
		}
		//sub
		else if(op==2) {
			System.out.println("SUB");
		}
		//rsb
		else if(op==3) {
			System.out.println("RSB");
		}
		//add
		else if(op==4) {
			System.out.println("ADD");
		}
		//cmp
		else if(op==10) {
			System.out.println("CMP");
		}
		//cmn
		else if(op==11) {
			System.out.println("CMN");
		}
		//orr
		else if(op==12) {
			System.out.println("ORR");
			
		}
		//mov
		else if(op==13) {
			System.out.println("MOV");
		}
		//mvn
		else if(op==15)
		{
			System.out.println("MVN");
			
		}
		
		
		
		
		
		if(h.getI(h.getBeg(current)).equals("0")) {	
			immediate=false;
			give_operands();
			System.out.println("First Operand is R" + first + ", Second Operand is R"+second);
		}
		
		else {
			immediate=true;
			give_operands();
			System.out.println("First Operand is R" + first + ", Second Immediate Operand is R"+second);
			
			//if second a register use R[second]
		}
		
		}
		
		
		//Data Store
		else if(h.getF(h.getBeg(current)).equals("01")) {
		
			
			
			
			
			
			
		}
		
		
		
		//Branch
		else if(h.getF(h.getBeg(current)).equals("10")) {
			
			
			
		}
		
		
		
		
		
	}
	

	
	
	
	
	public static void main(String[] args) {
	
		
		
		
		
		
	}

}