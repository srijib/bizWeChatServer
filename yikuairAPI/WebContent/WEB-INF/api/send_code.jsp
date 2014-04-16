<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="java.util.*,cn.yikuair.message.Msg,cn.yikuair.utils.*,cn.yikuair.business.UserManager" %>
<%
	HttpParams params = new HttpParams(request);
	String mobile = params.getStr("mobile", "");
	String code = params.getStr("code", "");
	String id = params.getStr("id", "");
	String login = params.getStr("login", "");
	//String randomString = RandomUtil.getRandom(4);
	//String ecbString = DataUtil.encodeECBAsBase64String(HttpParams.password,randomString);
	
	UserManager manager = UserManager.getUserManager();
	
	
	
	Map<String,Object> map  = new HashMap<String,Object>();
	
	if(login.equals("0")){
		List<Map<String,String>> list = manager.getUserByMobile(mobile);
		if(list==null||list.size()==0){
			map.put("code", 404);
		} else {
			map.put("code", 200);
			map.put("data", list);
			Msg.api(mobile, "验证码："+code+"【一块儿】");
		}
	} else {
		map.put("code", 200);
		Msg.api(mobile, "验证码："+code+"【一块儿】");
	}
	
	
	String outString = JsonUtil.ObjectToJsonString(map);
	
	
	
	out.print(outString);
	//out.print("{\"code\":\""+200+"\"}");
%>
