package cn.yikuair.utils;


public class BoolUtil {
	public static boolean checkStringArray(String id,String []arr){
		for(int i=0,j=arr.length;i<j;i++){
			if(arr[i].equals(id)) return true;
		}
		return false;
	}
}
