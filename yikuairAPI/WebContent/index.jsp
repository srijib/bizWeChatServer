<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<html>
	<head>
		<meta charset="utf-8"/>
	</head>
	<body>
		<form action="${ctx}/user.html" method="post">
			<input type="text" name="userName" />
			<input type="password" name="password" />
			<input type="submit" value="提交" />
		</form>
	</body>
</html>