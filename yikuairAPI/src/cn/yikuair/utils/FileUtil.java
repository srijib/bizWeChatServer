package cn.yikuair.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.wechat.message.Upload;

public class FileUtil {
	private static Logger logger = Log.getLogger(FileUtil.class);
	public static byte[] fileToByte(String url){
		InputStream in = null;
		byte[] buf =  new byte[1024];
		int len = 0;
		byte []arrayByte  = null;
		try{
			in = new FileInputStream(url);
			while((len=in.read(buf))!=-1){
				arrayByte = DataUtil.byteArray(arrayByte,DataUtil.subBytes(buf, 0, len));
			}
			in.close();
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return arrayByte;
	}
	
	public static void createPath(String path) throws IOException{
		String temppath = path.substring(0,path.lastIndexOf("/"));
		File tfile = new File(temppath);
		if(tfile.mkdirs()){
			logger.debug("创建目录成功："+temppath);
		}
	}
	
	public static boolean byteToFile(byte[] bytes,String path){
		try{
			createPath(path);
			File file=new File(path);
			FileOutputStream out = new FileOutputStream(file);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	
	public static void main(String[] args) throws IOException {
		createPath("/Users/yikuair/Documents/workspace/yikuairAPI/WebContent/static/smallimg/2013-09-04/");
	}
	
	
	
//	public static void main(String[] args) throws Exception{
//		// TODO Auto-generated method stub
//		Properties property = Conf.getProperty();
//		String path = property.getProperty("path.imgpath");
//		System.out.println(path);
//		byte[]b = fileToByte(path+"/img/sex.jpg");
////		StringBuffer sb = new StringBuffer();
////		for(int i =0,j=b.length;i<j;i++){
////			System.out.println(b[i]);
////			sb.append(b[i]+"");
////		}
////		System.out.println(sb.toString());
////		long a = new Date().getTime();
////		String string16 = DataUtil.Bytes2HexString(b);	
////		byte []bb = DataUtil.HexString2Bytes(string16);
//		
//		
//		String string16_2 = DataUtil.Bytes2HexString(b);
//		System.out.println("16::"+string16_2.length());
//		
//		byte []bb_2 = DataUtil.HexString2Bytes(string16_2);
////		long ba = new Date().getTime();
////		System.out.println("time:::"+(ba-a));
////		System.out.println("".length());
//		//time:::529686
//		
//		String imgs = DataUtil.ecodeBase64(bb_2);
//		bb_2 = DataUtil.decodeBase64(imgs);
//		
//		System.out.println("base64:"+imgs.length());
//		
//		byteToFile(bb_2,path+"/ok30.jpg");
//		
//		
//	}

}
