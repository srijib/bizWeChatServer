<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="cn.yikuair.wechat.star.Msg,cn.yikuair.utils.*" %>
<%
	//http://api.yikuair.com/yikuairAPI/a/test/callback?code=1&id=YY009
	//&secr=cjFT/+LuvjPb%2FpoWtKYpeduHtYZSgkhuxgcFEvN%2BYWJ6p5DpDhBDivL2g%2FR%2BODZoFtfYTONb5HSJ5OUM9gM%3D
	String code = request.getParameter("code");
	String username = request.getParameter("id");
	
	Msg msg = new Msg(); 
	String data = "";
	if(code!=null&&code.equals("1")){//电话
		data = "{\"token\" : 1 ,\"content\": \"5a6i5pyN55S16K+d77yaNDAwMDYyODI2Ng==\"}";
		//msg.testCallback( data, username);
	} else if(code!=null&&code.equals("2")){
		data = "{\"token\":4,\"title\":\"54mI5pys5Y+3\",\"imgpath\":\"aHR0cDovL3d3dy55aWt1YWlyLmNvbS8xL2xvZ28ucG5n\",\"content\":\"MS4zLjA=\",\"url\":\"aHR0cDovL3d3dy55aWt1YWlyLmNvbS8xLw==\"}";
		//msg.testCallback( data, username);
	} else {
		data = "{\"token\" : 1 ,\"content\":\"5bey5pS25Yiw5oKo55qE5Y+N6aaI77yM6LCi6LCi\"}";
		//msg.testCallback( data, username);
	}
	out.print(data);
%>
