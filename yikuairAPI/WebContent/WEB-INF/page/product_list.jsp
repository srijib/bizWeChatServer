<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<html>
	<head>
		<meta charset="utf-8"/>
		<link rel="stylesheet" href="${ctx}/res/css/global.css"/>
		<link rel="stylesheet" href="${ctx}/res/css/product.css?v6"/>
		<script type="text/javascript" src="${ctx}/res/js/lib.js"></script>
		<script type="text/javascript" src="${ctx}/res/js/iscroll.js"></script>
		<script type="text/javascript">
			$(function (){
				var width = $(window).width(),height=$(window).height();
				$("#j_scroll_page").css("height",height+"px");
				var sc_scroll_page = new iScroll("j_scroll_page");
			});
		</script>
	</head>
	<body>
		<div id="j_scroll_page">
		<div>
			<ul class="ul_product_list">
				<li>
					<div>
						<img src="${ctx}/res/img/6_270.jpg" width="100%" />
						<div class="introduce">
							<h2>小家电全场八折</h2>
							<time>8.15-8.31</time>
							<div class="price"><em class="cur_price">现价:<b>&nbsp;&nbsp;¥558</b></em><em class="ori_price">原价:<b>&nbsp;&nbsp;¥618</b></em></div>
							<div class="total_buy">599人购买</div>
						</div>
					</div>
				</li>
				
				<li>
					<div>
						<img src="${ctx}/res/img/7_270.jpg" width="100%" />
						<div class="introduce">
							<h2>小家电全场八折</h2>
							<time>8.15-8.31</time>
							<div class="price"><em class="cur_price">现价:<b>&nbsp;&nbsp;¥558</b></em><em class="ori_price">原价:<b>&nbsp;&nbsp;¥618</b></em></div>
							<div class="total_buy">599人购买</div>
						</div>
					</div>
				</li>
				
			</ul>
		</div>
		</div>
		
	</body>
</html>