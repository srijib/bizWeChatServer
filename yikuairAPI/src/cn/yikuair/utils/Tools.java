package cn.yikuair.utils;

import javax.servlet.http.HttpServletRequest;

public class Tools {

	
	/**
	 * 
	* @Title: getContextPath 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param request
	* @param @return    设定文件 
	* @return String    返回类型  /TEST/test.jsp
	* @throws
	 */
	public static String getContextPath(HttpServletRequest request){
		return request.getContextPath();
	}
	
	/**
	 * 
	* @Title: getServletPath 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param request
	* @param @return    设定文件 
	* @return String    返回类型   /TEST/jsp/test.jsp
	* @throws
	 */
	public static String getServletPath(HttpServletRequest request){
		return request.getServletPath();
	}
	
	public static String getResource(){
		return System.getProperty("user.dir");
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
