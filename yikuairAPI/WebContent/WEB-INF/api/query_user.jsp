<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="java.util.*,cn.yikuair.utils.*,cn.yikuair.wechat.user.Users" %>
<%
	HttpParams params = new HttpParams(request);
	Users users  =new Users(); 
	out.println(users.queryRealName(params));
%>
 