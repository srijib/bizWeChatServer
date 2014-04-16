package cn.yikuair.base;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cn.yikuair.test.Log4j;

public class Log implements Serializable{
	private static final long serialVersionUID = 7148580623617572783L;
	private static Logger logger  = null;
	private static boolean flag = false; 
	public static <T> Logger getLogger(Class <T> c) {
		if(!flag){
			PropertyConfigurator.configure(Log4j.class.getResource("/conf/log4j.properties"));
		}
		logger = Logger.getLogger(c);
		return logger;
	}
}
