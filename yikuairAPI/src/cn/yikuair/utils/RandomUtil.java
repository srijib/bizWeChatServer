package cn.yikuair.utils;

import java.util.Random;

public class RandomUtil {

	public static String getRandom(int len){
		if(len<=0) return "";
		StringBuffer sb = new StringBuffer();
		Random rd1 = new Random();
		for(int i=0;i<len&&len>0;i++){
			sb.append(rd1.nextInt(10));
		}		
		return sb.toString();
	}
	
	static String BaseTable[] = { 
		"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P", 
		"Q","R","S","T","U","V","W","X","Y","Z","a","b","c","d","e","f", 
		"g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v", 
		"w","x","y","z","0","1","2","3","4","5","6","7","8","9","+","/" 
		}; 
	
	public static String getRandom64(int len){
		if(len<=0) return "";
		StringBuffer sb = new StringBuffer();
		Random rd1 = new Random();
		for(int i=0;i<len&&len>0;i++){
			int n = rd1.nextInt(64);
			sb.append(BaseTable[n]);
		}
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(getRandom(5));
		System.out.println(getRandom64(1000));
	}

}
