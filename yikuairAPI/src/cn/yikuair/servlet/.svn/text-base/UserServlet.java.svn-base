package cn.yikuair.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.yikuair.db.sql.ConnPool;
import cn.yikuair.message.SocketConfig;
import cn.yikuair.test.TestSql;
import cn.yikuair.utils.HttpParams;
import cn.yikuair.utils.JsonUtil;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/user.html")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		String token = request.getParameter("token");
//		if(null!=token&&token.equals("1")){
//			System.out.println("token:"+1);
//		} else {
//			System.out.println("token is not exists");
//		}
//		
		int size = 0;
		if(null!=ConnPool.connections){
			size = ConnPool.connections.size();
		}
		
//		SocketConfig.socketBeanMap.put("0001", new cn.yikuair.beans.TokenSocketBean());
//		SocketConfig.socketBeanMap.put("0002", new cn.yikuair.beans.TokenSocketBean());
//		SocketConfig.socketBeanMap.put("0003", new cn.yikuair.beans.TokenSocketBean());
//		SocketConfig.socketBeanMap.put("0004", new cn.yikuair.beans.TokenSocketBean());
//		
//		response.setCharacterEncoding("utf-8");
//		PrintWriter out = response.getWriter();
//		
//		out.println(SocketConfig.socketBeanMap.hashCode());
//		out.println(SocketConfig.socketBeanMap.size()+"----"+size);
//		
		
		
//		out.println(System.getProperty("java.version"));
//		out.println(System.getProperty("user.dir"));
//		out.println(System.getProperty("user.home"));
//		out.println(System.getProperty("java.version"));
//		out.println(System.getProperty("java.version"));
		
//		out.println(Tools.getResource());
//		TestSql testSql = new TestSql();
////		List<User> list = testSql.getListData();
//		List list = null;
//		String string = JsonUtil.ObjectToJsonString(list);
//		
//		out.println("param:"+request.getParameter("__")+"--:"+request.getQueryString());
//		
//		System.out.println(string);
//		out.println(string);
//		
//		HttpParams params = new HttpParams(request);
//		String a = params.getStr("a", "");
//		String b = params.getStr("b", "");
//		out.println("<>"+a+"<>"+b);
		
		//ServletContext application = this.getServletContext();
		//this.conf.getServletContext();
		//response.sendRedirect("");
		//out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}
	
	private ServletConfig conf = null; 
	public void init(ServletConfig conf) throws ServletException { 
	    //实例化config对象 
        this.conf=conf;
    } 

}
