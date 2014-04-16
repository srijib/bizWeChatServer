<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="java.util.*,cn.yikuair.utils.*" %>
<%
	String ori = request.getParameter("ori");
	if(null!=ori && ori.length()>0) {
		String s = ImageUtil.comFloderImg(ori,ori+new Date().getTime());
		out.print(s);
	}
%>
 