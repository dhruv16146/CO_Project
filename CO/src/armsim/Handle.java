package armsim;

import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Handle{
	private static Handle h=null;
	
	private Handle()
	{
		
	}
	private final static File f=new File("./input");
	
	int getIntegerfromhex(String str){
		
		if(str.equalsIgnoreCase("A")) 
		{
		return 10;
		}
		else if(str.equalsIgnoreCase("B")) {
			return 11;
		}
		else if(str.equalsIgnoreCase("C")) {
			return 12;
		}
		else if(str.equalsIgnoreCase("D")){
			return 13;
		}
		else if(str.equalsIgnoreCase("E")){
			return 14;
		}
		else if(str.equalsIgnoreCase("F")){
		return 15;
		}		
		return Integer.parseInt(str);
	}
	
	
	String getBeg(String str)
	{	
		String s1=str.substring(0,3);
		String bin="";
		
		for(int i=0;i<3;i++) {
			try 
			{
				int t=Integer.parseInt(s1.substring(i,i+1));
				if(t==0)
				{
					bin+="0000";
				}
				else if(t==1) {
					bin+="000"+Integer.toBinaryString(t);
				}
				else if(t>1 && t<=3)
				{
					bin+="00"+Integer.toBinaryString(t);
				}
				else if(t>=4 && t<=7)
				{
					bin+="0"+Integer.toBinaryString(t);
				}
				else if(t>=8 && t<=9)
				{
					bin+=Integer.toBinaryString(t);
				}
			}
			catch(Exception e) {
				if(s1.substring(i,i+1).equalsIgnoreCase("A")) 
				{
					bin+= Integer.toBinaryString(10);
				}
				else if(s1.substring(i,i+1).equalsIgnoreCase("B")) {
					bin+= Integer.toBinaryString(11);
				}
				else if(s1.substring(i,i+1).equalsIgnoreCase("C")) {
					bin+= Integer.toBinaryString(12);
				}
				else if(s1.substring(i,i+1).equalsIgnoreCase("D")){
					bin+= Integer.toBinaryString(13);
				}
				else if(s1.substring(i,i+1).equalsIgnoreCase("E")){
					bin+= Integer.toBinaryString(14);
				}
				else if(s1.substring(i,i+1).equalsIgnoreCase("F")){
					bin+= Integer.toBinaryString(15);
				}		
			}
		}
		return bin;
	}
	//binary from e3a
	String getOffsetbranch(String str) {
		return str.substring(8);
	}
	//binary from e3a
	String getCond(String str) {
		return str.substring(0,4);
	}
	//binary from e3a
	String getOpcodeBranch(String str) {
		return str.substring(6,8);
	}
	
	String getOpcodeDS(String str) {
		//System.out.println(str);
		return str.substring(6,12);
	}
	String getOpcode(String str)
	{
		return str.substring(7, 11);
	}
	
//	1110 0010 0100 1101 1101 0000 0010 1100
	
	HashMap<Long,String> Readmemfile() {
		String hold="";
		HashMap<Long,String> tmp=new HashMap<Long,String>();
		long cnt=0;
		for(String i:f.list()) {
			if(i.equals("input.mem")) {
				File fr=new File("./input/input.mem");
				try {
					Scanner input=new Scanner(fr);
					
					while(input.hasNextLine()) 
					{
						hold = input.nextLine();
						String[] f=hold.split(" ");
						if(f.length==1 || f.length==0)
						{
							
						}
						else
						{
							hold=hold.substring(hold.length()-8);
							tmp.put(cnt,hold);
							cnt+=4;
						}
						
						
						//System.out.println(cnt+" "+tmp.get(cnt));
						
						
					}
				} 
				catch (FileNotFoundException e) 
				{			
					e.printStackTrace();
				}
			}
		}
		return tmp;
	}
	
	//E3A
	String getR1(String str) 
	{
		return str.substring(3,4);
	}
	//E3A
	String getRdest(String str) 
	{
		return str.substring(4,5);
	}
	//E3A
	String getImmediate(String str) {
		return str.substring(str.length()-2);
	}
	//E3A
	String getBranchOffset(String str) {
		return str.substring(str.length()-6);	
	}
	
	//E3A
	
	String getR2(String str) 
	{
		return str.substring(str.length()-1);
	}
	
	String getShift(String str) 
	{
		return str.substring(str.length()-3,str.length()-1);
	}
	
	String getAddress(String str) 
	{
		return str.substring(0,3);	
	}
	//binary
	String getF(String str)
	{
		return str.substring(4,6);
	}
	//binary
	String getI(String str)
	{
		return str.substring(6,7);
	}
	
	public static Handle getHandle(){
		if(h==null) {
			h=new Handle();
		}
		return h;
	}
}