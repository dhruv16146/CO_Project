package armsim;

import java.util.HashMap;

class Handle{
	private static Handle h=null;
	private Handle(){
		
	}
	String getOpcode(String str){
		
		String s1=str.substring(6,9);
		String bin="";
		
		for(int i=0;i<3;i++) {
			try {
			Integer.parseInt(s1.substring(i,i+1));
			bin+= Integer.toBinaryString(i);
			}
			catch(Exception e) {
				if(s1.substring(i,i+1).equals("A")) {
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
	
	
	
	
	
	
	
	String getR1(String str) {
		return str.substring(9,10);
	
	}
	
	
	String getRdest(String str) {
		return str.substring(10,11);
	}
	String getR2(String str) {
		return str.substring(str.length()-1);
	}
	String getShift(String str) {
		return str.substring(str.length()-3,str.length()-1);
	}
	String getAddress(String str) {
		return str.substring(0,3);
		
	}
	
	public static Handle getHandle(){
		if(h==null) {
			h=new Handle();
		}
		return h;
	}
}


