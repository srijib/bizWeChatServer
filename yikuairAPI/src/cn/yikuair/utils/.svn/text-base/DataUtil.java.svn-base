package cn.yikuair.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DataUtil {
	
	private final static String xform = "DES/ECB/NoPadding"; 
	private final static byte[] hex = "0123456789ABCDEF".getBytes();
	
	public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i=begin; i<begin+count; i++) bs[i-begin] = src[i];
        return bs;
    }
	
	public static byte[] byteArray(byte[] b,byte [] nb){
		ByteArrayOutputStream os = null;
		DataOutputStream ds;
		try{
			os = new ByteArrayOutputStream();
			ds = new DataOutputStream(os);
			if(b!=null) ds.write(b);
			if(nb!=null) ds.write(nb);
			byte[] joinByte = os.toByteArray();
			ds.flush();
			ds.close();
			os.close();
			return joinByte;
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	
	
	
	public static boolean isBytesEquals(byte []a,byte []b){
		int len = a.length;
		if(len!=b.length) return false;
		for(int i=0;i<len;i++){
			if(a[i]!=b[i]) return false;
		}
		return true;
	}
	
	private static int parse(char c) {  
	    if (c >= 'a')  
	        return (c - 'a' + 10) & 0x0f;  
	    if (c >= 'A')  
	        return (c - 'A' + 10) & 0x0f;  
	    return (c - '0') & 0x0f;  
	}  
	// 从字节数组到十六进制字符串转换  
	public static String Bytes2HexString(byte[] b) {  
	    byte[] buff = new byte[2 * b.length];  
	    for (int i = 0; i < b.length; i++) {  
	        buff[2 * i] = hex[(b[i] >> 4) & 0x0f];  
	        buff[2 * i + 1] = hex[b[i] & 0x0f];  
	    }  
	    return new String(buff);  
	}  
	  
	// 从十六进制字符串到字节数组转换  
	public static byte[] HexString2Bytes(String hexstr) {  
	    byte[] b = new byte[hexstr.length() / 2];  
	    int j = 0;  
	    for (int i = 0; i < b.length; i++) {  
	        char c0 = hexstr.charAt(j++);  
	        char c1 = hexstr.charAt(j++);  
	        b[i] = (byte) ((parse(c0) << 4) | parse(c1));  
	    }  
	    return b;  
	} 
	
	
	static byte[] padbyte(byte[] b) {
        int pad = b.length % 8;
        if (pad>0)
        	pad = 8 - pad;
        byte[] newbytes = new byte[b.length + pad];
        for (int i=0; i<b.length; i++)
        	newbytes[i] = b[i];
        for (int i=0; i<pad; i++)
        	newbytes[b.length + i] = 0; 
        return newbytes;
    }
    
    static byte[] unpadbyte(byte[] b) {
	    int pad = 0;
	    for (int i=b.length-1; i>=0 && b[i]==0; i--)
	    	pad++;
	    byte[] newbytes = new byte[b.length - pad];
	    for (int i=0; i<newbytes.length; i++)
	    	newbytes[i] = b[i];
	    return newbytes;
    }
	    
    static byte[] encrypt(byte[] inpBytes, SecretKey key) throws Exception 
    {
        Cipher cipher = Cipher.getInstance(xform);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // 2010/6/22 added by peter, must multiple of 8 bytes
        return cipher.doFinal(padbyte(inpBytes));
    }        

    static byte[] decrypt(byte[] inpBytes, SecretKey key) throws Exception 
    {
        Cipher cipher = Cipher.getInstance(xform);    
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] tmp = cipher.doFinal(padbyte(inpBytes));
        return unpadbyte(tmp);
    }

    
    public static byte[] encodeECBBytes(String key, String plainText)
        throws Exception
    {
        SecretKey secretkey = new SecretKeySpec(key.getBytes(), "DES");
        return encrypt(plainText.getBytes(), secretkey);
    }    
    
    public static String encodeECBAsHexString(String key, String plainText)
        throws Exception
    {
        return Bytes2HexString(encodeECBBytes(key, plainText));
        //return ecodeBase64(encodeECBBytes(key, plainText));
    }
    
    public static String encodeECBAsBase64String(String key, String plainText) throws Exception{
    	return ecodeBase64(encodeECBBytes(key, plainText));
    }
    
    public static String decodeECBString(String key, byte[] encryptBytes)
        throws Exception
    {
        SecretKey secretkey = new SecretKeySpec(key.getBytes(), "DES");
        byte[] b = decrypt(encryptBytes, secretkey);        
        return new String(b);
    }
     
    public static String padding8(String m)
    {
        int mod = m.length() % 8;
        if (mod > 0)
        {
            StringBuffer sb = new StringBuffer();
            for (int i=0; i<(8-mod); i++)
                sb.append(' ');
            m = sb.toString() + m;   
        }        
        return m;
    }
	
	
    public static String ecodeBase64(byte[] buf) {  
        return (new BASE64Encoder()).encode(buf);  
    }  
  
    public static byte[] decodeBase64(String buf) {  
        try {  
            return (new BASE64Decoder()).decodeBuffer(buf);  
        } catch (IOException e) {
        	e.printStackTrace();
        }  
        return null;  
    } 
    
    
	
	/** 
	 * @Title: main 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param args    设定文件 
	 * @return void    返回类型 
	 * @throws 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String str = "我们真的很好共性阿訇爱爱放大法 定";
//		byte b[] = str.getBytes();
//		String str2="";
//		for(int i=0,j=b.length;i<j;i++){
//			str2+=b[i]+",";
//		}
//		System.out.println(str2);
//		byte []bb = DataUtil.subBytes(b, b.length-10,10);
//		String str3="";
//		for(int i=0,j=bb.length;i<j;i++){
//			str3+=bb[i]+",";
//		}
//		System.out.println(str3);
		
		try
        {
			long a = new Date().getTime();
//        	String []arr = {"_yikuair","4567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvf1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvfa=万文伟&b=1234567ffvf"};
//			String []arr = {"_yikuair","username=1&password=61F0B1C8257282E95C3786C8851AA137&com_id=1&de_name=财务部"};
//			String []arr = {"_yikuair","longDate="+new Date().getTime()+"&username=YY001&password=C4CA4238A0B923820DCC509A6F75849B&pageIndex=1&pageSize=50"};
//			String []arr = {"_yikuair","longDate="+new Date().getTime()+"&username=YY001&password=C4CA4238A0B923820DCC509A6F75849B&data="+DataUtil.ecodeBase64("你吃饭了吗".getBytes())+"&msguuid="+UuidUtil.getUUID()+"&token=1&to=453"};
//			String []arr = {"_yikuair","longDate="+new Date().getTime()+"&username=YY001&password=C4CA4238A0B923820DCC509A6F75849B&buttonName=打飞机&msguuid="+UuidUtil.getUUID()+"&status=0&callbackurl=http://www.baidu.com"};
//			String []arr = {"_yikuair","username=F2812375&password=C4CA4238A0B923820DCC509A6F75849B&to=1"};
//			String []arr = {"_yikuair","id=2&entrytime=2013-08-31&address=北京市-朝阳区-大望路&email=ady@specialdeals.com"};
//			String []arr = {"_yikuair","id=1&username=andy&password=61F0B1C8257282E95C3786C8851AA137"};
//			String []arr = {"_yikuair","id=1&username=andy&password=61F0B1C8257282E95C3786C8851AA137&task_id=2&from_id=2"};
//			String []arr = {"_yikuair","username=ady@yikuair.com&password=61F0B1C8257282E95C3786C8851AA137&id=2"};
			String []arr = {"_yikuair","username=YY001&password=C4CA4238A0B923820DCC509A6F75849B"};
			
			
			
			//task add
//			String []arr = {"_yikuair","user_id=1&username=andy&password=61F0B1C8257282E95C3786C8851AA137&title=周五在大会议室开技术讨论会&address=华茂大厦21501室&type=1&btime=2013-09-12 16:00&etime=2013-09-12 18:00&ids=2,3"};
			
			
			
			//ABDFC6EC2473D4A70080AE252040AC26D7F901D27B7A663131008A0BA3BAEA96   wrong
			//ABDFC6EC2473D4A70080AE252040AC265630D1355459CEBE35DBABF7ED42CAE3   right
			
//			String []arr = {"_yikuair","username=ady&password=kendeji&newpassword=yikuair"};
			//ABDFC6EC2473D4A70080AE252040AC265630D1355459CEBE80337128BCD33BB7A29E92C7B97E1C18BF3FAECCEC28BAB0BA650BECEBBD76A9
            System.out.println("orig text=" + arr[1] + " key=" + arr[0]);
            //System.out.println("encr hex=" + encodeECBAsHexString(arr[0], arr[1]));
            String base64 = encodeECBAsBase64String(arr[0], arr[1]);
            System.out.println("encr hex=" + base64);
            base64 = URLEncoder.encode(base64,"utf-8");
            System.out.println("64:"+base64);
            System.out.println("decr text=" + decodeECBString(arr[0],encodeECBBytes(arr[0], arr[1])));
            //String str = DataUtil.decodeECBString(arr[0], DataUtil.HexString2Bytes(arr[1]));
            //System.out.println("str:"+str);
            //long b = new Date().getTime();
            //System.out.println(b-a);
            
            //String b3 = URLDecoder.decode("q9%2FG7CRz1KcAgK4lIECsJmoOIUlDdpJIuaVUwtUOolSmI3bKsy46UFnSj90YhcBqc%2BZaNRuNEkO4%0AbErMi0Xw%2FoEvdFcSEMjUuVM0M49cI9o%3D","utf-8");
            //System.out.println("b3:"+b3);
           // System.out.println("decr text=" + decodeECBString(arr[0],b3));
            //String str = DataUtil.decodeECBString("_yikuair", DataUtil.decodeBase64(b3));

            //username=ady&password=61F0B1C8257282E95C3786C8851AA137&token=00-21-cc-b5-63-2c
            //System.out.println("str:"+str);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
		
		
//		byte [] xx = "username=ady&password=yikuair".getBytes();
//		String sss = DataUtil.ecodeBase64(xx);
//		System.out.println("xxx:"+sss);
//		
//		//System.out.println(decodeECBString("_yikuair", "dXNlcm5hbWU9YWR5JnBhc3N3b3JkPTYxRjBCMUM4MjU3MjgyRTk1QzM3ODZDODg1MUFBMTM3"));
//		try{
////			String str = DataUtil.decodeECBString("_yikuair", DataUtil.decodeBase64("dXNlcm5hbWU9YWR5JnBhc3N3b3JkPXlpa3VhaXI="));
////			System.out.println("dddd:"+str);
//			System.out.println(DataUtil.decodeBase64("q9/G7CRz1KcAgK4lIECsJmoOIUlDdpJIuaVUwtUOolSmI3bKsy46UFnSj90YhcBq"));
//			String str22 = DataUtil.decodeECBString("_yikuair", DataUtil.decodeBase64("q9/G7CRz1Kd6A7qyecQ1ARoBWRojI+W5APgZJH0iEcYMRAVhyXXznqkQM7yFxNL9NmroCpYM4j5oXRwGipKEQXBibEFSdDX2l8Mzf4JnkXe6+qEMATLvX6odLv1vEOf6sqpxMp9gMeZ/SHe9snjb6Q=="));	
//			System.out.println("DDDDDL:"+str22);
//		} catch(Exception ex){
//			ex.printStackTrace();
//		}
		
//		String str = DataUtil.decodeECBString(password, DataUtil.HexString2Bytes(s));
		System.out.println(new Date().getTime());	
	}

}
