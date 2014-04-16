<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<html>
	<head>
		<meta charset="utf-8"/>
		<link rel="stylesheet" href="${ctx}/res/css/global.css"/>
		<link rel="stylesheet" href="${ctx}/res/css/chat.css"/>
		<script type="text/javascript" src="${ctx}/res/js/lib.js"></script>
		
		<script>
			$(function (){
				$("#j_startSocket,#j_closeSocket").bind("click",function (){
					var json = null;
					if($(this).attr("id")=="j_startSocket"){
						json = {'type':"start"};
					} else {
						json = {'type':"stop"};
					}

					var data = $.ajax({
						method:"post",
						isSync:false,// false 同步方式
						dataType:"string",
						url:"${ctx}/a/socket",
						data:json
					});
					alert("data:"+data);
				});
			});
		</script>
	</head>
	<body>
		<div>
			<div class="dialog dialog1">
				<em>wanwenwei</em>
				<div class="contentBoard"></div>
				<input type="text" class="message" />
				<input type="button" value="发送" />
			</div>
			<div class="dialog dialog2">
				<em>liudehua</em>
				<div class="contentBoard"></div>
				<input type="text" class="message" />
				<input type="button" value="发送" />
			</div>
			
			<input type="button" id="j_startSocket" value="启动Socket" >
			<input type="button" id="j_closeSocket" value="关闭Socket" >
		</div>
	</body>
</html>