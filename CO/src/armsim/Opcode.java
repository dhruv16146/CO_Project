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
	String print;
	String current;
	long temp=-1;
	int N,Z,V,C;
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
		print="";
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
		if(!immediate)
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
	void onecycle(){
		print="";
		flag=false;
		this.fetch();
		this.decode();
		this.execute();
		this.mem();
		this.writeback();
		System.out.println();
		
	}
	void cycle() 
	{
		while(true) 
		{
			flag=false;
			this.fetch();
			this.decode();
			this.execute();
			this.mem();
			this.writeback();
			System.out.println();
		}
	}
	
	void swi_exit() 
	{
		System.out.print("Exit:");
		System.exit(0);
	}

	void swi_read() 
	{
		if(R[0]==0) 
		{
			System.out.println("Give the input:");
			Scanner sc=new Scanner(System.in);
			R[0]=sc.nextInt();
		}
		else 
		{
			
		}
	}
	
	void swi_print()
	{
		if(R[0]==1) 
		{
			System.out.println("Output is: "+R[1]);
		}
		else 
		{
			
		}
	}
	
	void fetch() 
	{
		exit_status=false;
		read_status=false;
		print_status=false;
		immediate=false;
		current=ins_memory.get(R[15]);
		if(current.equals("EF000011")) 
		{
			exit_status=true;
		}
		else if(current.equals("EF00006C")) 
		{
			read_status=true;
		}
		else if(current.equals("EF00006B")) 
		{
			print_status=true;
		}
		System.out.println("FETCH:Fetching Instruction 0x"+ins_memory.get(R[15])+" from address "+R[15]);
		print=print+""+"FETCH:Fetching Instruction 0x"+ins_memory.get(R[15])+" from address "+R[15]+"\n";
		R[15]+=4;
	}
	
	void decode() 
	{
		//Data Processing
		if((!exit_status&&!read_status&&!print_status)) {
		System.out.print("DECODE: ");
		System.out.print(" Operation is ");
		print=print+""+"DECODE: Operation is ";
		}
		if(h.getF(h.getBeg(current)).equals("00"))
		{
			int op=Integer.parseInt(h.getOpcode(h.getBeg(current)),2);
			//and
			if(op==0)
			{
				System.out.print("AND");
				print=print+"AND";
			}
			//eor
			else if(op==1)
			{
				System.out.print("EOR");
				print=print+"EOR";
			}
			//sub
			else if(op==2)
			{
				System.out.print("SUB");
				print=print+"SUB";
			}
			//rsb
			else if(op==3)
			{
				System.out.print("RSB");
				print=print+"RSB";
			}
			//add
			else if(op==4)
			{
				System.out.print("ADD");
				print=print+"ADD";
			}
			//cmp
			else if(op==10) 
			{
				System.out.print("CMP");
				print=print+"CMP";
			}
			//cmn
			else if(op==11) 
			{
				System.out.print("CMN");
				print=print+"CMN";
			}
			//orr
			else if(op==12) 
			{
				System.out.print("ORR");
				print=print+"ORR";
			}
			//mov
			else if(op==13) 
			{
				System.out.print("MOV");
				print=print+"MOV";
			}
			//mvn
			else if(op==15)
			{
				System.out.print("MVN");
				print=print+"MVN";
			}
			
			if(h.getI(h.getBeg(current)).equals("0")) 
			{	
				immediate=false;
				give_operands();
				System.out.print(" First Operand is R" + first + ", Second Operand is R"+second);
				System.out.print(" ,Destination Register is R"+dest);
				print=print+" First Operand is R" + first + ", Second Operand is R"+second+" ,Destination Register is R"+dest+"\n";
				System.out.println();
				if(first==second) {
					print=print+"Read Registers: R"+first+"="+R[first]+"\n";
					System.out.print("Read Registers: R"+first+"="+R[first]);
				}
				else {
					print=print+"Read Registers: R" + first + "=" + R[first] + ", R"+second+"="+ R[second]+"\n";
					System.out.print("Read Registers: R" + first + "=" + R[first] + ", R"+second+"="+ R[second]);
				}
				System.out.println();
			}
			else 
			{
				immediate=true;
				give_operands();
				System.out.print(" First Operand is R" + first + ", Second Immediate Operand is "+second);
				print+=" First Operand is R" + first + ", Second Immediate Operand is "+second;
				System.out.print(" ,Destination Register is R"+dest);
				print =print+" ,Destination Register is R"+dest+"\n";
				System.out.println();
				System.out.print("Read Registers: R"+first+"="+R[first]);
				print=print+"Read Registers: R"+first+"="+R[first]+"\n";
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
				System.out.println(" Source Operand is R"+dest);
				print+="STR"+" Source Operand is R"+dest+"\n";
			}
			//LDR
			else if(op==25) 
			{
				System.out.print("LDR");
				System.out.println(" Destination Register is R" +first+ " Source is memory "+(R[first]+second));
				print+="LDR"+" Destination Register is R" +first+ " Source is memory "+(R[first]+second)+"\n";
			}
			immediate=true;
			give_operands();
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
				String se = s.substring(i,i+1);
				String te = Integer.toBinaryString(Integer.parseInt(se,16));
				for(int j=0;j<4-te.length();j++)
				{
					te="0"+te;
				}
				offset = offset + te;
			}
			//beq
			if(cond==0) 
			{
				System.out.print("Branch Equals");	
				print+="Branch Equals";
			}
			//bne
			else if(cond==1) 
			{
				print+="Branch Not Equals";
				System.out.print("Branch Not Equals");
			}
			//bgt
			else if(cond==10) 
			{
				print+="Branch Greater then or equal";
				System.out.print("Branch Greater then or equal");		
			}
			//blt
			else if(cond==11) 
			{
				print+="Branch less then";
				System.out.print("Branch less then");	
			}
			//bgt
			else if(cond==12) 
			{
				print+="Branch Greater then";
				System.out.print("Branch Greater then");
			}
			//ble
			else if(cond==13) 
			{
				print+="Branch Lesser then or equal";
				System.out.print("Branch Less then or equal");		
			}
			//b
			else if(cond == 14)
			{
				print+="Unconditional Branch";
				System.out.println("Unconditional Branch");
			}
			//lr
			if(op==2) 
			{
				link=false;
			}
			else 
			{
				link=true;
				System.out.print(" with link");	
			}
			print+=" Offset is "+ offset+"\n";
			System.out.print(" Offset is "+ offset);
			System.out.println();
		}
	}
	
	public void execute()
	{
		//Data Processing
		if(h.getF(h.getBeg(current)).equals("00"))
		{
			int op=Integer.parseInt(h.getOpcode(h.getBeg(current)),2);
			if(immediate == false) 
			{	
				if(op==0)
				{
					System.out.println("EXECUTE: AND "+R[first]+" and "+R[second]);
					print+="EXECUTE: AND "+R[first]+" and "+R[second]+"\n";
					R[dest]=R[first]&R[second];
					temp=R[dest];
					flag=true;
				}
				//eor
				else if(op==1)
				{
					System.out.println("EXECUTE: XOR "+R[first]+" and "+R[second]);
					print+="EXECUTE: XOR "+R[first]+" and "+R[second]+"\n";
					R[dest]=R[first]^R[second];
					temp=R[dest];
					flag=true;
				}
				//sub
				else if(op==2)
				{
					System.out.println("EXECUTE: SUBTRACT "+R[first]+" and "+R[second]);
					print+="EXECUTE: SUBTRACT "+R[first]+" and "+R[second]+"\n";
					R[dest]=R[first]-R[second];
					temp=R[dest];
					flag=true;
				}
				//rsb
				else if(op==3)
				{
					System.out.println("EXECUTE: SUBTRACT "+R[second]+" and "+R[first]);
					print+="EXECUTE: SUBTRACT "+R[second]+" and "+R[first]+"\n";
					R[dest]=R[second]-R[first];
					temp=R[dest];
					flag=true;
				}
				//add
				else if(op==4)
				{
					System.out.println("EXECUTE: ADD "+R[first]+" and "+R[second]);
					print+="EXECUTE: ADD "+R[first]+" and "+R[second]+"\n";
					R[dest]=R[first]+R[second];
					temp=R[dest];
					flag=true;
				}
				//cmp
				else if(op==10) 
				{
					System.out.println("EXECUTE: COMPARE "+R[first]+" and "+R[second]);
					print+="EXECUTE: COMPARE "+R[first]+" and "+R[second]+"\n";
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
					print+="EXECUTE: OR "+R[first]+" and "+R[second]+"\n";
					R[dest] = R[first] | R[second];	
					temp=R[dest];
					flag=true;
				}
				//mov
				else if(op==13) 
				{
					System.out.println("EXECUTE: MOV "+R[second]+" to R"+dest);
					print+="EXECUTE: MOV "+R[second]+" to R"+dest+"\n";
					R[dest] = R[second];
					temp=R[dest];
					flag=true;
				}
				//mvn
				else if(op==15)
				{
					System.out.println("EXECUTE: MVN to R"+dest+" with "+R[second]);
					print+="EXECUTE: MVN to R"+dest+" with "+R[second]+"\n";
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
					print+="EXECUTE: AND "+R[first]+" and "+R[second]+"\n";
					R[dest]=R[first]&second;
					temp=R[dest];
					flag=true;
				}
				//eor
				else if(op==1)
				{
					System.out.println("EXECUTE: XOR "+R[first]+" and "+second);
					print+="EXECUTE: XOR "+R[first]+" and "+second+"\n";
					R[dest]=R[first]^second;
					temp=R[dest];
					flag=true;
				}
				//sub
				else if(op==2)
				{
					System.out.println("EXECUTE: SUBTRACT "+R[first]+" and "+second);
					print+="EXECUTE: SUBTRACT "+R[first]+" and "+second+"\n";
					R[dest]=R[first]-second;
					temp=R[dest];
					flag=true;
				}
				//rsb
				else if(op==3)
				{
					System.out.println("EXECUTE: SUBTRACT "+second+" and "+R[first]);
					print+="EXECUTE: SUBTRACT "+second+" and "+R[first]+"\n";
					R[dest]=second-R[first];
					temp=R[dest];
					flag=true;
				}
				//add
				else if(op==4)
				{
					System.out.println("EXECUTE: ADD "+R[first]+" and "+second);
					print+="EXECUTE: ADD "+R[first]+" and "+second+"\n";
					R[dest]=R[first]+second;
					temp=R[dest];
					flag=true;
				}
				//cmp
				else if(op==10) 
				{
					System.out.println("EXECUTE: COMPARE "+R[first]+" and "+second);
					print+="EXECUTE: COMPARE "+R[first]+" and "+second+"\n";
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
					print+="EXECUTE: OR "+R[first]+" and "+second+"\n";
					R[dest] = R[first] | second;
					temp=R[dest];
					flag=true;
				}
				//mov
				else if(op==13) 
				{
					System.out.println("EXECUTE: MOV "+second+" to R"+dest);
					print+="EXECUTE: MOV "+second+" to R"+dest+"\n";
					R[dest] = second;
					temp=R[dest];
					flag=true;
				}
				//mvn
				else if(op==15)
				{
					System.out.println("EXECUTE: MVN to R"+dest+" with "+second);
					print+="EXECUTE: MVN to R"+dest+" with "+second+"\n";
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
				System.out.println("Storing "+R[dest]+" to memory address "+(R[first]+second));
				print+="Storing "+R[dest]+" to memory address "+(R[first]+second)+"\n";
				mem.put(R[first]+second, R[dest]);
				
			}
			//LDR
			else if(op==25) 
			{
				System.out.println("Loading "+mem.get(R[first]+second)+" from memory address "+(R[first]+second)+" ");
				print+="Loading "+mem.get(R[first]+second)+" from memory address "+(R[first]+second)+" "+"\n";
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
					if(link)
					{
						R[14] = R[15];
					}
						
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
					print+="Updating PC to " + R[15]+"\n";
				}
				else
				{
					System.out.println("No execute operation");
					print+="No Excecute Operation "+"\n";
				}
			}
		}
	
	public void mem() 
	{
		if(mem_op) {
			System.out.println("MEMORY: Memory Operation ");
			print+="MEMORY: Memory Operation "+"\n";
		}
		else {
			System.out.println("MEMORY: No Memory Operation ");
			print+="MEMORY: No Memory Operation "+"\n";
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
			print+="WRITEBACK:Write "+temp+" to R"+dest+"\n";
		}
		else
		{
			System.out.println("WRITEBACK:No Writeback");
			print+="WRITEBACK:No Writeback"+"\n";
			
		}
	}
	public static void main(String[] args) 
	{
		Opcode op=new Opcode();
		op.cycle();
		
	}

}