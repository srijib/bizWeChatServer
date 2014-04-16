<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="cn.yikuair.utils.HttpParams,cn.yikuair.wechat.user.Password" %>
<%
	HttpParams params = new HttpParams(request);
	Password login  =new Password(); 
	out.println(login.userAndOldpwdToNewpwd(params));
%>
