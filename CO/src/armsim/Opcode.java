package armsim;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
class Opcode 
{
	HashMap<Long,String> ins_memory;
	HashMap<Long, Long> mem=new HashMap<Long,Long>();
	Handle h=Handle.getHandle();
	static long[]  R=new long[16];
	String current;
	long temp=-1;
	int N,Z,E,C;
	int first,second,dest;
	int cond;
	String offset;
	boolean immediate;
	boolean exit_status;
	boolean read_status;
	boolean print_status;
	boolean flag=false;
	boolean link,mem_op;
	
	Opcode()
	{
		ins_memory=h.Readmemfile();
		R[13]=(long)ins_memory.size()*4;
		R=new long[16];
		immediate=false;
		link=false;
		mem_op=false;
		reset();
	}
	public void give_operands() 
	{
		if(immediate)
		{
			first=h.getIntegerfromhex(h.getR1(current));
			second=h.getIntegerfromhex(h.getR2(current));
			dest=h.getIntegerfromhex(h.getRdest(current));
		}
		
		else
		{
			first=h.getIntegerfromhex(h.getR1(current));
			second= h.getIntegerfromhex(h.getImmediate(current).substring(0, 1))*16  +  h.getIntegerfromhex(h.getImmediate(current).substring(1));
			dest=h.getIntegerfromhex(h.getRdest(current));	
		}
		
	}
	void reset() 
	{
		Arrays.fill(R,0);	
	}
	void cycle() 
	{
		
		while(true) {
			flag=false;
			this.fetch();
			this.decode();
			this.execute();
			this.mem();
			this.writeback();
			System.out.println();
		}
	
		
		
	
	}
	void swi_exit() {
		System.out.print("Exit:");
		System.exit(0);
		
		
	}

	void swi_read() {
		
		if(R[0]==0) {
		System.out.println("Give the input:");
		Scanner sc=new Scanner(System.in);
		R[0]=sc.nextInt();
		}
		else {
			
		}
		
	}
	void swi_print() {
		if(R[0]==1) {
			System.out.println("Output is: "+R[1]);
		}
		else {
			
			
		}
		
		
	}
	
	void fetch() 
	{
		
		exit_status=false;
		read_status=false;
		print_status=false;
		
		current=ins_memory.get(R[15]);
		if(current.equals("EF000011")) {
			exit_status=true;
		}
		else if(current.equals("EF00006C")) {
			read_status=true;
			
		}
		else if(current.equals("EF00006B")) {
			print_status=true;
		}
		
		System.out.println("FETCH:Fetching Instruction 0x"+ins_memory.get(R[15])+" from address "+R[15]);
		R[15]+=4;
	}
	
	
	void decode() 
	{
		//Data Processing
		if((!exit_status&&!read_status&&!print_status)) {
		System.out.print("DECODE: ");
		System.out.print(" Operation is ");
		}
		if(h.getF(h.getBeg(current)).equals("00"))
		{
			//System.out.println("this is "+h.getBeg(current));
			int op=Integer.parseInt(h.getOpcode(h.getBeg(current)),2);
			//System.out.println("this is "+op);
			
			//and
			if(op==0)
			{
				System.out.print("AND");
			}
			//eor
			else if(op==1)
			{
				System.out.print("EOR");
			}
			//sub
			else if(op==2)
			{
				System.out.print("SUB");
			}
			//rsb
			else if(op==3)
			{
				System.out.print("RSB");
			}
			//add
			else if(op==4)
			{
				
				System.out.print("ADD");
			}
			//cmp
			else if(op==10) 
			{
				System.out.print("CMP");
			}
			//cmn
			else if(op==11) 
			{
				System.out.print("CMN");
			}
			//orr
			else if(op==12) 
			{
				System.out.print("ORR");	
			}
			//mov
			else if(op==13) 
			{
				System.out.print("MOV");
			}
			//mvn
			else if(op==15)
			{
				System.out.print("MVN");		
			}
			
			if(h.getI(h.getBeg(current)).equals("0")) 
			{	
				immediate=false;
				give_operands();
				System.out.print(" First Operand is R" + first + ", Second Operand is R"+second);
				System.out.print(" ,Destination Register is R"+dest);
				System.out.println();
				if(first==second) {
					System.out.print("Read Registers: R"+first+"="+R[first]);
				}
				else {
					System.out.print("Read Registers: R" + first + "=" + R[first] + ", R"+second+"="+ R[second]);
				}
				System.out.println();
				
			}
		
			else 
			{
				immediate=true;
				give_operands();
				System.out.print(" First Operand is R" + first + ", Second Immediate Operand is "+second);
				System.out.print(" ,Destination Register is R"+dest);
				System.out.println();
				System.out.print("Read Registers: R"+first+"="+R[first]);
				System.out.println();
				//if second a register use R[second]
			}
		}
		
		//Data Store
		else if(h.getF(h.getBeg(current)).equals("01")) 
		{
			mem_op=true;
			//System.out.println(".."+current);
			//System.out.println("case "+h.getBeg(current));
			int op=Integer.parseInt(h.getOpcodeDS(h.getBeg(current)),2);	
			
			//STR
			if(op==24) 
			{
				System.out.print("STR");				
			}
			
			
			//LDR
			else if(op==25) 
			{
				System.out.print("LDR");
			}
			
			
			give_operands();
			System.out.print("Source Operand is R" + first + ", Destination Operand is R"+dest);
			System.out.print(" offset "+second);
			System.out.println();
			//now second acts as offset
				
		}
		
		//Branch
		else if(h.getF(h.getBeg(current)).equals("10")) 
		{	
			
			
			int op=Integer.parseInt(h.getOpcodeBranch(h.getBeg(current)),2);
			
			cond=Integer.parseInt(h.getCond(h.getBeg(current)),2);
			
			String s=h.getBranchOffset(current);
			
			
			offset = "";
			
			for(int i=0;i<6;i++)
			{
				
//				System.out.println(s+"  yeh");
				String se = s.substring(i,i+1);
				
				String te = Integer.toBinaryString(Integer.parseInt(se,16));
				for(int j=0;j<4-te.length();j++)
				{
					te="0"+te;
				}
				offset = offset + te;
			}
			
			if(cond==0) 
			{
				System.out.print("Branch Equals");	
			}
			else if(cond==1) 
			{
				System.out.print("Branch Not Equals");
			}
			
			else if(cond==10) 
			{
				System.out.print("Branch Greater then or equal");	
				
			}
			else if(cond==11) 
			{
				System.out.print("Branch less then");	
			}
			else if(cond==12) 
			{
				System.out.print("Branch Greater then");
			}
			else if(cond==13) 
			{
				System.out.print("Branch Less then or equal");		
			}
			
			if(op==2) {
				link=false;
			}
			
			else {
				link=true;
				System.out.print(" with link");	
			}
			System.out.print(" Offset is "+ offset);
			System.out.println();
		}
		
	}
	
	public void execute()
	{
		if(h.getF(h.getBeg(current)).equals("00"))
		{
			int op=Integer.parseInt(h.getOpcode(h.getBeg(current)),2);
			if(immediate == false) 
			{	
				if(op==0)
				{
					System.out.println("EXECUTE: AND "+R[first]+" and "+R[second]);
					R[dest]=R[first]&R[second];
					temp=R[dest];
					flag=true;
				}
				//eor
				else if(op==1)
				{
					System.out.println("EXECUTE: XOR "+R[first]+" and "+R[second]);
					R[dest]=R[first]^R[second];
					temp=R[dest];
					flag=true;
				}
				//sub
				else if(op==2)
				{
					System.out.println("EXECUTE: SUBTRACT "+R[first]+" and "+R[second]);
					R[dest]=R[first]-R[second];
					temp=R[dest];
					flag=true;
				}
				//rsb
				else if(op==3)
				{
					System.out.println("EXECUTE: SUBTRACT "+R[second]+" and "+R[first]);
					R[dest]=R[second]-R[first];
					temp=R[dest];
					flag=true;
				}
				//add
				else if(op==4)
				{
					System.out.println("EXECUTE: ADD "+R[first]+" and "+R[second]);
					R[dest]=R[first]+R[second];
					temp=R[dest];
					flag=true;
				}
				//cmp
				else if(op==10) 
				{
					System.out.println("EXECUTE: COMPARE "+R[first]+" and "+R[second]);
					if(R[first]-R[second]<0)
					{
						N=1;
						Z=0;
					}
					else if(R[first]-R[second]==0)
					{
						N=0;
						Z=1;
					}
				}
				//orr
				else if(op==12) 
				{
					System.out.println("EXECUTE: OR "+R[first]+" and "+R[second]);
					R[dest] = R[first] | R[second];	
					temp=R[dest];
					flag=true;
				}
				//mov
				else if(op==13) 
				{
					System.out.println("EXECUTE: MOV "+R[second]+" to R"+dest);
					R[dest] = R[second];
					temp=R[dest];
					flag=true;
				}
				//mvn
				else if(op==15)
				{
					System.out.println("EXECUTE: MVN to R"+dest+" with "+R[second]);
					R[dest] = 0xFFFFFFF ^ R[second];	
					temp=R[dest];
					flag=true;
				}
			}
			else
			{
				//and
				if(op==0)
				{
					System.out.println("EXECUTE: AND "+R[first]+" and "+R[second]);
					R[dest]=R[first]&second;
					temp=R[dest];
					flag=true;
				}
				//eor
				else if(op==1)
				{
					System.out.println("EXECUTE: XOR "+R[first]+" and "+second);
					R[dest]=R[first]^second;
					temp=R[dest];
					flag=true;
				}
				//sub
				else if(op==2)
				{
					System.out.println("EXECUTE: SUBTRACT "+R[first]+" and "+second);
					R[dest]=R[first]-second;
					temp=R[dest];
					flag=true;
				}
				//rsb
				else if(op==3)
				{
					System.out.println("EXECUTE: SUBTRACT "+second+" and "+R[first]);
					R[dest]=second-R[first];
					temp=R[dest];
					flag=true;
				}
				//add
				else if(op==4)
				{
					System.out.println("EXECUTE: ADD "+R[first]+" and "+second);
					R[dest]=R[first]+second;
					temp=R[dest];
					flag=true;
				}
				//cmp
				else if(op==10) 
				{
					System.out.println("EXECUTE: COMPARE "+R[first]+" and "+second);
					if(R[first]-second<0)
					{
						N=1;
						Z=0;
					}
					else if(R[first]-second==0)
					{
						N=0;
						Z=1;
					}
				}
				//orr
				else if(op==12) 
				{
					System.out.println("EXECUTE: OR "+R[first]+" and "+second);
					R[dest] = R[first] | second;
					temp=R[dest];
					flag=true;
				}
				//mov
				else if(op==13) 
				{
					System.out.println("EXECUTE: MOV "+second+" to R"+dest);
					R[dest] = second;
					temp=R[dest];
					flag=true;
				}
				//mvn
				else if(op==15)
				{
					System.out.println("EXECUTE: MVN to R"+dest+" with "+second);
					R[dest] = 0xFFFFFFF ^ second;
					temp=R[dest];
					flag=true;
				}
				//if second a register use R[second]
			}
		}
		//Data Store
		else if(h.getF(h.getBeg(current)).equals("01")) 
		{
			int op=Integer.parseInt(h.getOpcodeDS(h.getBeg(current)),2);	
		
			//STR
			if(op==24) 
			{
				System.out.print("STR");
				//System.out.println(R[first]+second+"adad"+R[dest]);
				mem.put(R[first]+second, R[dest]);
				
			}
			//LDR
			else if(op==25) 
			{
				System.out.print(" LDR ");
				if(mem.get(R[first]+second)!=null)
				{
					R[dest]=mem.get(R[first]+second);
					temp=R[dest];
					flag=true;
				}
				else 
				{
					R[dest]=0;
					temp=R[dest];
					flag=true;
				}
			}
			
			
			give_operands();
			System.out.print(" Source Operand is R" + first + ", Destination Operand is R"+dest);
			System.out.print(" offset "+second);
			System.out.println();
			//now second acts as offset
				
		}
		//Branch
				else if(h.getF(h.getBeg(current)).equals("10")) 
				{	
					int op=Integer.parseInt(h.getOpcodeDS(h.getBeg(current)),2);
					cond=Integer.parseInt(h.getCond(h.getBeg(current)),2);
					long temp_b = 0;
					if(cond==0)
					{
						if(Z>0)
						{
							temp_b = 1;
						}
					}
					else if(cond==1)
					{
						if(Z<=0)
						{
							temp_b = 1;
						}
					}
					else if(cond==11)
					{
						if(N>0)
						{
							temp_b = 1;
						}
					}
					else if(cond==13)
					{
						if(N>0 || Z>0)
						{
							temp_b = 1;
						}
					}
					else if(cond==12)
					{
						if(N<=0)
						{
							temp_b = 1;
						}
					}
					else if(cond==10)
					{
						if(N<=0 && Z>0)
						{
							temp_b = 1;
						}
					}
					else if(cond==14)
					{
						temp_b = 1;
					}
					
					if(temp_b == 1)
					{	
						if(offset.substring(0, 1).equals("1"))
						{
							offset = "111111" + offset + "00";
						}
						else
						{
							offset = "000000" + offset + "00";
						}
						R[15] = R[15] + Long.parseLong(offset, 2) + 4;
						R[15] = (int)R[15];
						System.out.println("Updating PC to " + R[15]);
					}
					else
					{
						System.out.println("No execute operation");
					}
				}
			}
	
	public void mem() 
	{
		if(mem_op) {
			System.out.println("MEMORY: Memory Operation ");
		}
		else {
			System.out.println("MEMORY: No Memory Operation ");
		}
		if(exit_status) {
			swi_exit();
		}
		
		else if(read_status) {
			swi_read();
		}
		
		else if(print_status) {
			swi_print();
			
		}
		
	}
	
	
	public void writeback()
	{
		if(flag==true)
		{
			System.out.println("WRITEBACK:Write "+temp+" to R"+dest);
		}
		else
		{
			System.out.println("WRITEBACK:No Writeback");
		}
	}
	public static void main(String[] args) 
	{
		Opcode op=new Opcode();
		op.cycle();
		
	}

}