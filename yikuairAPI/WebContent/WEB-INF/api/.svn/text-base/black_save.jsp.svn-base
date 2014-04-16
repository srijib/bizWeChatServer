<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="cn.yikuair.utils.HttpParams,cn.yikuair.wechat.user.Users" %>
<%
	HttpParams params = new HttpParams(request);
	Users users  =new Users();
	out.println(users.getUser(params));
%>