package cn.yikuair.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import cn.yikuair.message.ServiceSocket;

public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public InitServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    public void init(ServletConfig cfg) throws ServletException {  
        super.init(cfg); 
        
        
        //SocketConfig.socketBeanMap.put("wan_test", new cn.yikuair.beans.TokenSocketBean());
        
        System.out.println("start..");
//        ServiceSocket serviceSocket = new ServiceSocket();
        ServiceSocket s = new ServiceSocket();
        s.createSocket();
    } 
}
