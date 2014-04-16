package cn.yikuair.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Conf {
	private static Properties property = null;
	public static Properties setProperty(){
		try {
			File file = new File(Conf.class.getResource("/conf/conf.properties").getFile());
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			property = new Properties(); 
			property.load(bis);
			return property;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	} 
	public static Properties getProperty() {
		if(property==null) setProperty();
		return property;
	}
	public static void setProperty(Properties property) {
		Conf.property = property;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		setProperty();
		System.out.println("path:"+property.getProperty("path.imgpath"));
	}

}
