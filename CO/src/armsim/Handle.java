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
		
		if(str.equals("A")) 
		{
		return 10;
		}
		else if(str.equals("B")) {
			return 11;
		}
		else if(str.equals("C")) {
			return 12;
		}
		else if(str.equals("D")){
			return 13;
		}
		else if(str.equals("E")){
			return 14;
		}
		else if(str.equals("F")){
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
				else if(t>=1 && t<=3)
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
				if(s1.substring(i,i+1).equals("A")) 
				{
					bin+= Integer.toBinaryString(10);
				}
				else if(s1.substring(i,i+1).equals("B")) {
					bin+= Integer.toBinaryString(11);
				}
				else if(s1.substring(i,i+1).equals("C")) {
					bin+= Integer.toBinaryString(12);
				}
				else if(s1.substring(i,i+1).equals("D")){
					bin+= Integer.toBinaryString(13);
				}
				else if(s1.substring(i,i+1).equals("E")){
					bin+= Integer.toBinaryString(14);
				}
				else if(s1.substring(i,i+1).equals("F")){
					bin+= Integer.toBinaryString(15);
				}		
			}
		}
		return bin;
	}
	String getOffsetbranch(String str) {
		return str.substring(8);
	}
	String getCond(String str) {
		return str.substring(0,4);
	}
	
	String getOpcodeBranch(String str) {
		return str.substring(6,8);
	}
	String getOpcodeDS(String str) {
		return str.substring(6,12);
	}
	String getOpcode(String str)
	{
		return str.substring(7, 11);
	}
	
	
	
	HashMap<Integer,String> Readmemfile() {
		String hold="";
		HashMap<Integer,String> tmp=new HashMap<Integer,String>();
		int cnt=0;
		for(String i:f.list()) {
			if(i.equals("input.mem")) {
				File fr=new File("./input/input.mem");
				try {
					Scanner input=new Scanner(fr);
					
					while(input.hasNextLine()) {
						hold=input.nextLine().substring(6);
						tmp.put(cnt,hold);
						cnt+=4;
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
	String getR1(String str) 
	{
		return str.substring(3,4);
	}
	
	String getRdest(String str) 
	{
		return str.substring(4,5);
	}
	String getImmediate(String str) {
		return str.substring(str.length()-2);
	}
	
	String getBranchOffset(String str) {
		return str.substring(str.length()-6);	
	}
	
	
	
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
	
	String getF(String str)
	{
		return str.substring(4,6);
	}
	
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