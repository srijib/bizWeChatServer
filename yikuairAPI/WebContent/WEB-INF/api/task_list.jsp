<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="cn.yikuair.utils.HttpParams,cn.yikuair.wechat.task.Lists" %>
<%
	HttpParams params = new HttpParams(request);
	Lists users  =new Lists();
	out.println(users.getTasks(params));
%>
