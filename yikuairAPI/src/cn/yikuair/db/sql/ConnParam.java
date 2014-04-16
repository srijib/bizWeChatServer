package cn.yikuair.db.sql;

import java.io.Serializable;
 
public class ConnParam implements Serializable {
    private static final long serialVersionUID = 1L;  
  
    private static String driver="com.mysql.jdbc.Driver"; // 数据库连接驱动  121.199.9.221
    private static String url="jdbc:mysql://127.0.0.1:3306/wechatDb?useUnicode=true&autoReconnect=true&characterEncoding=UTF-8"; // 数据库连接URL  
    private static String user="root"; // 数据库连接user  
    private static String password="www123"; // 数据库连接password  
    private static int minConnection=5; // 数据库连接池最小连接数  
    private static int maxConnection=50; // 数据库连接池最大连接数  
    private static long timeoutValue; // 连接的最大空闲时间  
    private static long waitTime; // 取得连接的最大等待时间  
    private static int incrementalConnections=5; //连接池自动增加连接的数量  
	public static String getDriver() { 
		return driver;
	}
	public static void setDriver(String driver) {
		ConnParam.driver = driver;
	}
	public static String getUrl() {
		return url;
	}
	public static void setUrl(String url) {
		ConnParam.url = url;
	}
	public static String getUser() {
		return user;
	}
	public static void setUser(String user) {
		ConnParam.user = user;
	}
	public static String getPassword() {
		return password;
	}
	public static void setPassword(String password) {
		ConnParam.password = password;
	}
	public static int getMinConnection() {
		return minConnection;
	}
	public static void setMinConnection(int minConnection) {
		ConnParam.minConnection = minConnection;
	}
	public static int getMaxConnection() {
		return maxConnection;
	}
	public static void setMaxConnection(int maxConnection) {
		ConnParam.maxConnection = maxConnection;
	}
	public static long getTimeoutValue() {
		return timeoutValue;
	}
	public static void setTimeoutValue(long timeoutValue) {
		ConnParam.timeoutValue = timeoutValue;
	}
	public static long getWaitTime() {
		return waitTime;
	}
	public static void setWaitTime(long waitTime) {
		ConnParam.waitTime = waitTime;
	}
	public static int getIncrementalConnections() {
		return incrementalConnections;
	}
	public static void setIncrementalConnections(int incrementalConnections) {
		ConnParam.incrementalConnections = incrementalConnections;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
} 
