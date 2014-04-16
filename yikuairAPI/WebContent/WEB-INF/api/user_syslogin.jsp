<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="cn.yikuair.utils.HttpParams,cn.yikuair.wechat.user.Login" %>
<%
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	String callback = request.getParameter("callback");
	Login login  =new Login();
	try{
		String json = login.validateLogin(username,password);
		out.println(callback+"("+json+")");
	} catch (Exception ex) {
		ex.printStackTrace();
	}
%>
