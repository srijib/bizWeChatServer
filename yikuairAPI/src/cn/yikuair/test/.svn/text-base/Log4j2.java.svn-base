package cn.yikuair.test;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class Log4j2 {

	/** 
	 * @Title: main 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param args    设定文件 
	 * @return void    返回类型 
	 * @throws 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			Logger logger = Logger.getRootLogger();
			Logger jlog = Logger.getLogger("log4j");
			jlog.setLevel(Level.ALL);
			PatternLayout pl = new PatternLayout("%r [%t] %-5p %c %x -%m\n");
			FileAppender fa = new FileAppender(pl,"/Users/yikuair/html/log/log3.log",false);
			logger.addAppender(fa);
			
			
			jlog.debug("this is debug info from cc "+jlog.getLevel());
			
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
