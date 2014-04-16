package cn.yikuair.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cn.yikuair.message.ServiceSocket;

public class ServerInitListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent context) {
		System.out.println("come in socket");
		//ServiceSocket.createSocket();
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

	}

}
