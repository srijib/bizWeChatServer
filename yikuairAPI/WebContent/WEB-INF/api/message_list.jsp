<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="java.util.*,cn.yikuair.message.Msg,cn.yikuair.utils.*,cn.yikuair.business.*" %>
<%
	HttpParams params = new HttpParams(request);
	String from = params.getStr("from", "");
	String to = params.getStr("to", "");
	Map <String , Object> map = new HashMap<String , Object> ();
	if(to.equals("")) {
		map.put("code", 200);
		map.put("message", "字段错误");
		String string = JsonUtil.ObjectToJsonString(map);
		out.print(string);
		return;
	} 
	
	MessageManager message = new MessageManager();
	List <Map<String,String>> list = message.getUnreadMessage(from,to);
	
	map.put("code", 200);
	map.put("data", list);
	String string = JsonUtil.ObjectToJsonString(map);
	out.print(string);
%>
