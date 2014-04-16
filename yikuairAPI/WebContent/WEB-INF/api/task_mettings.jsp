<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="cn.yikuair.utils.HttpParams,cn.yikuair.wechat.task.Mettings" %>
<%
	HttpParams params = new HttpParams(request);
	Mettings mettings  =new Mettings();
	out.println(mettings.getMettings(params));
%> 
