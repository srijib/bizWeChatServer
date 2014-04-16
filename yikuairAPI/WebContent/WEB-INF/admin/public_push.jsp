<%@page import="cn.yikuair.wechat.user.Tinder"%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="cn.yikuair.utils.*" %>
<%
	Tinder tinder = new Tinder();
	String s = HttpUtil.getContent(request);
	String string = tinder.pushMessage(s);
	out.println(string);
%> 
