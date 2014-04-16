package cn.yikuair.message;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import cn.yikuair.base.Log;
import cn.yikuair.beans.TokenSocketBean;


public class ServiceSocket {
	private static Logger logger = Log.getLogger(ServiceSocket.class);
	public static ServerSocket server = null;
	//public  Map<String,TokenSocketBean> socketBeanMap = new HashMap<String,TokenSocketBean>();
	
	public void createSocket(){
		
		try {
			Timer timer = new Timer();
			timer.schedule(new ListenTimer(), 1000*10,1000*300);
			logger.info("ListenTimer 10秒后启动...");
			server = new ServerSocket(SocketConfig.PORT);
			ServerSocketThread sst = new ServerSocketThread(server);
			logger.info("ServiceSocket sst 启动...");
			sst.start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void stopSocket(){
		try{
			server.close();
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void lisenter(){
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//ServiceSocket.createSocket();
	}

}
