package armsim;
import java.util.Arrays;
import java.util.HashMap;
class Opcode 
{
	HashMap<Integer,String> ins_memory;
	HashMap<Integer, Integer> mem;
	Handle h=Handle.getHandle();
	static int[]  R=new int[16];
	String current;
	int N,Z,E,C;
	int first,second,dest;
	int cond;
	String offset;
	boolean immediate;
	boolean link,mem_op;
	boolean exit_status;
	Opcode()
	{
		ins_memory=h.Readmemfile();
		R[13]=ins_memory.size()*4;
		R=new int[16];
		immediate=false;
		link=false;
		mem_op=false;
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
			this.fetch();
			this.decode();
			this.execute();
			this.mem();
		}
	
		
		
	
	}
	void swi_exit() {
		System.out.print("Exit:");
		System.exit(0);
		
		
	}
	void fetch() 
	{
		current=ins_memory.get(R[15]);
		if(current.equals("EF000011")) {
			exit_status=true;
		}
		System.out.println("Fetching Instruction 0x"+ins_memory.get(R[15])+" from address "+R[15]);
		R[15]+=4;
	}
	void decode() 
	{
		//Data Processing
		if(!exit_status) {
		System.out.print("Decode: ");
		System.out.print(" Operation is ");
		}
		if(h.getF(h.getBeg(current)).equals("00"))
		{
			int op=Integer.parseInt(h.getOpcode(h.getBeg(current)),2);
			
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
					System.out.print("Read Registers  R"+first+"="+R[first]);
				}
				else {
					System.out.print("Read Registers  R" + first + "=" + R[first] + ", R"+second+"="+ R[second]);
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
				System.out.print("Read Registers  R"+first+"="+R[first]);
				System.out.println();
				//if second a register use R[second]
			}
		}
		
		//Data Store
		else if(h.getF(h.getBeg(current)).equals("01")) 
		{
			mem_op=true;
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
			String s=h.getOffsetbranch(current);
			
			offset = "";
			for(int i=0;i<6;i++)
			{
				String se = s.substring(i,i+1);
				String te = Integer.toBinaryString(Integer.parseInt(se,16));
				for(int j=0;j<4-te.length();j++)
				{
					te="0"+te;
				}
				offset = offset + te;
			}
			
			if(cond==0) 
			
				System.out.print("Branch Equals");	
			
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
					R[dest]=R[first]&R[second];
				}
				//eor
				else if(op==1)
				{
					R[dest]=R[first]^R[second];
				}
				//sub
				else if(op==2)
				{
					R[dest]=R[first]-R[second];
				}
				//rsb
				else if(op==3)
				{
					R[dest]=R[second]-R[first];
				}
				//add
				else if(op==4)
				{
					R[dest]=R[first]+R[second];
				}
				//cmp
				else if(op==10) 
				{
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
					R[dest] = R[first] | R[second];	
				}
				//mov
				else if(op==13) 
				{
					R[dest] = R[second];
				}
				//mvn
				else if(op==15)
				{
					R[dest] = 0xFFFFFFF ^ R[second];		
				}
			}
			else
			{
				//and
				if(op==0)
				{
					R[dest]=R[first]&second;
					System.out.println("Execute: ADD " + R[first] + " and "+ R[second]);
				}
				//eor
				else if(op==1)
				{
					R[dest]=R[first]^second;
				}
				//sub
				else if(op==2)
				{
					R[dest]=R[first]-second;
				}
				//rsb
				else if(op==3)
				{
					R[dest]=second-R[first];
				}
				//add
				else if(op==4)
				{
					R[dest]=R[first]+second;
				}
				//cmp
				else if(op==10) 
				{
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
					R[dest] = R[first] | second;	
				}
				//mov
				else if(op==13) 
				{
					R[dest] = second;
				}
				//mvn
				else if(op==15)
				{
					R[dest] = 0xFFFFFFF ^ second;		
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
				mem.put(R[first]+second, R[dest]);
				
			}
			//LDR
			else if(op==25) 
			{
				System.out.print(" LDR ");
				if(mem.get(R[first]+second)!=null)
				{
					R[dest]=mem.get(R[first]+second);
				}
				else 
				{
					R[dest]=0;
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
			int temp = 0;
			if(op==0)
			{
				if(Z>0)
				{
					temp = 1;
				}
			}
			else if(op==1)
			{
				if(Z<=0)
				{
					temp = 1;
				}
			}
			else if(op==11)
			{
				if(N>0)
				{
					temp = 1;
				}
			}
			else if(op==13)
			{
				if(N>0 || Z>0)
				{
					temp = 1;
				}
			}
			else if(op==12)
			{
				if(N<=0)
				{
					temp = 1;
				}
			}
			else if(op==10)
			{
				if(N<=0 && Z>0)
				{
					temp = 1;
				}
			}
			else if(op==14)
			{
				temp = 1;
			}
			
			if(temp == 1)
			{
				if(offset.substring(0, 1).equals("1"))
				{
					offset = "11111111" + offset;
				}
				else
				{
					offset = "00000000" + offset;
				}
				R[15] = R[15] + Integer.parseInt(offset, 2);
				System.out.println("Updating PC to " + R[15]);
			}
			else
			{
				System.out.println("No execute operation");
			}
		}
	}
	
	public void mem() {
		if(mem_op) {
			System.out.println(" Memory Operation ");
		}
		else {
			System.out.println("No Memory Operation ");
		}
		if(exit_status) {
			swi_exit();
		}
		
	}
	
	public static void main(String[] args) 
	{
		Opcode op=new Opcode();
		op.cycle();
		
	}

}