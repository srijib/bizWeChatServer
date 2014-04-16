package cn.yikuair.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	/**
	 * 
	* @Title: getTime 
	* @Description:  
	* @param @param yyyy-MM-dd HH:mm:ss
	* @param @return  
	* @return String    yyyy-MM-dd HH:mm:ss 获得当天时间的字符串格式
	* @throws  DateUtil.getTimeString("yyyy-MM-dd HH:mm:ss");
	 */
	public static String getTimeString(String formatString) {
		Date date = new Date();
		long strDate = date.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		String str = sdf.format(strDate);
		return str;
	}
	
	public static String longToString(long l,String formatString){
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		Date dt = new Date(l);
		String str = sdf.format(dt);
		return str;
	}
	
	public static long stringToLong(String formatString,String patternString){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(formatString);
			return sdf.parse(patternString).getTime();
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return new Date().getTime();
	}
	
	public static long getlong(String patternString){
		long oldlong = Long.parseLong(patternString);
		long newlong = new Date().getTime()-oldlong;
		return newlong;
	}
	
	public static String getDateBefore(String formatString,int beforeDay){
		Calendar c = Calendar.getInstance();
		Date date  =new Date();
		c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - beforeDay);
        Date d = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		String str = sdf.format(d);
        return str;
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//long l = stringToLong("1383925448000", "yyyy-MM-dd HH:mm:ss"); 
		//long l = stringToLong("yyyy-MM-dd HH:mm:ss", "1383925448000"); 
		//System.out.println(l);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		System.out.println(sdf.parse("2013-09-11 12:12:12").getTime());
//		
//		long l = DateUtil.stringToLong("yyyy-MM-dd HH:mm:ss", "2013-09-11 12:12:12");
//		System.out.println(l);
		long x = getlong("1390763288835");
		Math.abs(x);
		System.out.println(x+"--"+1000*60*10);
		System.out.println(new Date().getTime());
		
		System.out.println(getTimeString("yyyy-MM-dd"));
		
		String s  = getDateBefore("yyyy-MM-dd",1);
		System.out.println("ddd:"+s);
		
	}

}
