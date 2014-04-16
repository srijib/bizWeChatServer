<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="cn.yikuair.message.SocketConfig,cn.yikuair.db.sql.ConnPool,java.util.*" %>
<%
	int sqlSize = 0;
	if(null!=ConnPool.connections){
		sqlSize = ConnPool.connections.size();
	}
	int onlineSize = SocketConfig.socketBeanMap.size();
	
	out.println("{\"sqlSize\":"+sqlSize+",\"onlineSize\":"+onlineSize+"}");
	
%>
