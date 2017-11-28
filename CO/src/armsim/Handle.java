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
	String getBeg(String str)
	{	
		String s1=str.substring(6,9);
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
	
	String getOpcode(String str)
	{
		return str.substring(7, 11);
	}
	String[] Readmemfile() {
		String hold="";
		for(String i:f.list()) {
			if(i.equals("input.mem")) {
				File fr=new File("./input/input.mem");
				try {
					Scanner input=new Scanner(fr);
					
					while(input.hasNextLine()) {
						hold+=input.nextLine();
						hold+=",";
						
					}
					
				} 
				catch (FileNotFoundException e) {			
					e.printStackTrace();
				}
			}
		}
		String [] lines=hold.split(",");
		return lines;
	}	
	String getR1(String str) 
	{
		return str.substring(9,10);
	
	}	
	String getRdest(String str) 
	{
		return str.substring(10,11);
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
	public static Handle getHandle(){
		if(h==null) {
			h=new Handle();
		}
		return h;
	}
}