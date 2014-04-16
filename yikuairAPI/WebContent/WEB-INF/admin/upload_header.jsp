<%@page import="org.apache.catalina.User"%>
<%@page import="com.sun.j3d.utils.scenegraph.io.retained.Controller"%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="cn.yikuair.wechat.user.*,cn.yikuair.utils.*" %>
<%
	HttpParams params = new HttpParams(request);
	String s = HttpUtil.getContent(request);
	Users users  =new Users();
	users.editUsersJson(s);
	out.print(s);
%>
 