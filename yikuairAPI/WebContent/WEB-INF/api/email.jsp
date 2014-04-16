<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="cn.yikuair.business.BaseManager" %>
<%
	String email = request.getParameter("email");
	String callback = request.getParameter("callback");
	if(null==email||email.equals("")){
		out.print(callback+"({\"code\":\"404\"})");
	} else {
		BaseManager manager = BaseManager.getBaseManager();
		manager.saveEmail(email);
		out.println(callback+"({\"code\":\"200\"})");
	}
%> 
