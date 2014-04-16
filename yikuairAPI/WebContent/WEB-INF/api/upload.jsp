<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="cn.yikuair.utils.HttpParams,cn.yikuair.wechat.message.Upload" %>
<%
	HttpParams params = new HttpParams(request);
	Upload upload  =Upload.getUpload();
	out.println(upload.uplaodXls(params));
%>
 