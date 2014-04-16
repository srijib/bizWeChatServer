<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="cn.yikuair.utils.HttpParams,cn.yikuair.business.UserGroupManager" %>
<%
	HttpParams params = new HttpParams(request);
	UserGroupManager manager = UserGroupManager.getUserGroupManager();
	out.println(manager.getGroupsAndMembers(params));
%>
