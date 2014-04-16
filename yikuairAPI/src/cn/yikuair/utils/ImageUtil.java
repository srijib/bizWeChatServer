package cn.yikuair.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUtil {
	
	private static Logger logger = Log.getLogger(ImageUtil.class);
	public static boolean subByteToImg(byte[] bytes,String path,int ...ints){
		int len = ints.length;
		String imgType = path.substring(path.lastIndexOf(".")+1);
		try{
			File file=new File(path);
			FileOutputStream out = new FileOutputStream(file);
			out.write(bytes);
			out.flush();
			out.close();
			
			int w=1,h=1,x=0,y=0;
			
			if(len>=2){
				w = ints[0];
				h = ints[1];
			}
			if(len>=4){
				x = ints[2];
				y = ints[3];
			}
			if(len>=2){				
				InputStream in = new FileInputStream(file); 
				BufferedImage bi = ImageIO.read(in);
				BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				Graphics g = bufferedImage.getGraphics();
				
				 
				
				int oriW = bi.getWidth();
				int oriH = bi.getHeight();
				int left = 0,top = 0;
				
				if(w>oriW||h>oriH){
					if(oriW<w){
						left = (w-oriW)/2;
					}
					if(oriH<h){
						top = (h-oriH)/2;
					}
					g.setColor(Color.white);
					g.fillRect(0, 0, w, h);
					g.drawImage(bi, left, top, null);
				} else {
					g.drawImage(bi.getSubimage(x, y, w, h),0,0,null);
				}
				g.dispose();
				ImageIO.write(bufferedImage,imgType,file);
			} 
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		return true;
	}
	
	
	public static int[] averageSize(int oriW,int oriH){
		int newWidth = 0,newHeight = 0;  
		if(oriW<=160&&oriH<=160){
			newWidth = oriW;
			newHeight = oriH;
		}
		double rate1 = ((double) oriW) / (double) 100 + 0.1;   
        double rate2 = ((double) oriH) / (double) 100 + 0.1; 
        double rate = rate1 > rate2 ? rate1 : rate2;
        newWidth = (int) (((double) oriW) / rate);   
        newHeight = (int) (((double) oriH) / rate);
		
        int []_wh = {newWidth,newHeight};
        return _wh;
	}
	
	
	
	public static void compressImg(byte[] bytes,String path){
		//http://www.iteye.com/topic/266585
		
		String imgType = path.substring(path.lastIndexOf(".")+1);
		
		try{
			FileUtil.createPath(path);
			logger.debug("path:"+path);
			File file=new File(path);
			FileOutputStream out = new FileOutputStream(file);
			out.write(bytes);
			out.flush();
			out.close();
			InputStream in = new FileInputStream(file); 
			BufferedImage bi = ImageIO.read(in);
			int oriW = bi.getWidth();
			int oriH = bi.getHeight();
			
			int[] _wh =  averageSize(oriW, oriH);
			
			
			//生成图像变换对象
			AffineTransform transform = new AffineTransform();
			
			double sx = (double)_wh[0]/oriW;//小/大图像的宽度比例   
            double sy = (double)_wh[1]/oriH;//小/大图像的高度比例 
			transform.setToScale(sx,sy);// 设置图像转换的比例   
            //生成图像转换操作对象   //TYPE_INT_RGB
			  
			BufferedImage bufferedImage = new BufferedImage(_wh[0], _wh[1], BufferedImage.TYPE_INT_RGB);
            Graphics2D gd2 = bufferedImage.createGraphics();
            gd2.drawImage(bi, transform,null);
            gd2.dispose();
			ImageIO.write(bufferedImage, imgType, file);
	
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	//new
	
	 public static void compressPic(File file,String newUrl,int ... ints) { 
		 try{
			 BufferedImage img = ImageIO.read(file); 
			 if (img.getWidth(null) == -1) {
				 System.out.println(" can't read,retry!" + "<BR>"); 
                 return; 
			 } else {
				 int newWidth; int newHeight;
				 if (ints.length>0) {
					// 为等比缩放计算输出的图片宽度及高度 
                     double rate1 = ((double) img.getWidth(null)) / (double) ints[0] + 0.1; 
                     double rate2 = ((double) img.getHeight(null)) / (double) ints[1] + 0.1;
                     // 根据缩放比率大的进行缩放控制 
                     double rate = rate1 > rate2 ? rate1 : rate2; 
                     newWidth = (int) (((double) img.getWidth(null)) / rate); 
                     newHeight = (int) (((double) img.getHeight(null)) / rate); 
				 } else {
					 newWidth = img.getWidth(null); // 输出的图片宽度 
                     newHeight = img.getHeight(null); // 输出的图片高度 
				 }
				 BufferedImage tag = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_USHORT_555_RGB); 
				 
				 /*
                  * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的
                  * 优先级比速度高 生成的图片质量比较好 但速度慢
                  */ 
                  tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight,60), 0, 0, null);
                  FileOutputStream out = new FileOutputStream(newUrl);
                  // JPEGImageEncoder可适用于其他图片类型的转换 
                  JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out); 
                  encoder.encode(tag); 
                  out.close();
			 }
		 } catch(Exception ex) {
			 ex.printStackTrace();
		 }
	 } 
	
	
	
	
	public static String comFloderImg(String oriUrl,String newUrl) {
		try{
			File root = new File(oriUrl);
			File []files = root.listFiles();
			FileUtil.createPath(newUrl);
			
			for (int i=0,j=files.length;i<j;i++) {
				boolean isDir = files[i].isDirectory();
				if (isDir) {
					String dirName = files[i].getName();
					String childPath = newUrl+"/"+dirName+"/";
					FileUtil.createPath(childPath);
					
					File []jpgFile = files[i].listFiles();
					for (int m = 0,n=jpgFile.length;m<n;m++) {
						byte[]b = FileUtil.fileToByte(jpgFile[m].getAbsolutePath());
						//subByteToImg(b, childPath+"/"+jpgFile[m].getName(), 0,0,160,160);
						//compressImg(b, childPath+"/"+jpgFile[m].getName());
						compressPic(jpgFile[m],childPath+"/"+jpgFile[m].getName(),220,220);
					}
					
				} else {
					logger.info(files[i].getName());
				}
			}
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return "ok";
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Properties property = Conf.getProperty();
//		String path = property.getProperty("path.imgpath");
//		byte[]b = FileUtil.fileToByte(path+"/img/id.jpg");
//		subByteToImg(b,path+"/small.jpg",640,320,0,0);
//		//subByteToImg(b,path+"/sub5.jpg");
//		compressImg(b,path+"/comww.jpg");
		
		comFloderImg("/Users/yikuair/html/img","/Users/yikuair/html/newimg");
		
	}

}
