package cn.yikuair.message;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import cn.yikuair.beans.TokenSocketBean;


public class ServerSocketThread extends Thread{
	
	private ServerSocket server;
	//private Map<String,TokenSocketBean> socketBeanMap;
	
	public ServerSocketThread(ServerSocket server){
		this.server = server;
		//this.socketBeanMap = socketBeanMap;
	}
	
	//public static Map<String,TokenSocketBean> socketBeanMap  = new HashMap<String,TokenSocketBean>();//在线用户
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try{
			while(true){
				Socket socket = server.accept();
				SocketThread st = new SocketThread(socket);
				st.start();
			}
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
