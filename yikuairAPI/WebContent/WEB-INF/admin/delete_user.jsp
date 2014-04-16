<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="cn.yikuair.wechat.user.*,cn.yikuair.utils.*" %>
<%
	HttpParams params = new HttpParams(request);
	Users users  =new Users();
	out.println(users.deleteUsers(params));
%>
 