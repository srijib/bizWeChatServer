package cn.yikuair.db.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Vector;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;

public class ConnPool {
	static Logger logger = Log.getLogger(ConnPool.class);
	public static Vector<ConnObject> connections = null;//支持线程的同步,性能低

	
	public synchronized static void createPool(){ 
        if (connections != null) {  
            return; // 如果己经创建，则返回  
        } 
        try{
        	
        	Driver driver = (Driver) (Class.forName(ConnParam.getDriver()).newInstance());  
            DriverManager.registerDriver(driver);
            connections = new Vector<ConnObject>();
            logger.debug(" 数据库连接池创建成功！ "); 
            createConns(ConnParam.getMinConnection());
        } catch(Exception ex){
        	ex.printStackTrace();
        }
	}
	public static synchronized ConnObject getConnection() {
		if(connections==null){
			createPool();
		}
		ConnObject conn = getFreeConnection();	
		
		while(conn==null){
			wait(200);
			getFreeConnection();
		}
		return conn;
	}
	
	public static synchronized void refreshPool(){
		if (connections == null) {  
            createPool();
        } else {
        	for(int i=0,j=connections.size();i<j;i++){
        		ConnObject conn = connections.get(i);
        		if(conn.isBusy()){
        			wait(5000);
        		}
        		closeConn(conn.getConnection());
        		conn.setConnection(initConnection());
        		conn.setBusy(false);
        	}
        }
	}
	
	private static ConnObject getFreeConnection(){
		ConnObject connObj = null;
		boolean isFree = false;
		for(int i=0,j=connections.size();i<j;i++){
			connObj = connections.get(i);
			if(!connObj.isBusy()){
				connObj.setBusy(true);
				isFree = true;
				break;
			}
		}
		
		//
		if(!isFree){//连接池都在busy状态
			connObj = createConns(ConnParam.getIncrementalConnections());
			connObj.setBusy(true);
		}
		
		return connObj;
	}
	
	private static ConnObject createConns(int numConn){
		if (ConnParam.getMaxConnection() > 0  
                && connections.size() >= ConnParam.getMaxConnection()) {
			return null;
        }
		ConnObject conn = null;
		for (int x = 0; x < numConn; x++) {
			ConnObject connObject = new ConnObject(initConnection());
			if(x==0) conn = connObject;
			connections.addElement(connObject);
		}
		return conn;
	}
	
	
	
	public static void closePools(){
		if(connections==null){
			logger.debug("连接池还没有被创建，无法进行关闭！");
			return ;
		}
		for(int i=0,j=connections.size();i<j;i++){
			ConnObject connObj = connections.get(i);
			if(connObj.isBusy()){
				wait(5000);
			}
			closeConn(connObj.getConnection());
			connections.removeElement(connObj); 
		}
		
	}
	
	private static Connection initConnection() {
		Connection conn = null;
		try{
			conn = DriverManager.getConnection(ConnParam.getUrl(),ConnParam.getUser(), ConnParam.getPassword());
			if (connections.size() == 0) {
				DatabaseMetaData metaData = conn.getMetaData();  
	            int driverMaxConnections = metaData.getMaxConnections();
	            if (driverMaxConnections > 0  
	                    && ConnParam.getMaxConnection() > driverMaxConnections) {  
	            	ConnParam.setMaxConnection(driverMaxConnections);  
	            } 
	            
			}
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return conn;
	} 
	
	
	
	private static void wait(int seconds) {
		try{
			Thread.sleep(seconds);
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private static void closeConn(Connection conn){
		try{
			if(conn!=null) conn.close();
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
}