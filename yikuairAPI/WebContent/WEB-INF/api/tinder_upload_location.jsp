<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="cn.yikuair.utils.HttpParams,cn.yikuair.wechat.user.*" %>
<%
	HttpParams params = new HttpParams(request);
	Tinder tinder  =new Tinder();
	out.println(tinder.updateLocation(params));
%>
