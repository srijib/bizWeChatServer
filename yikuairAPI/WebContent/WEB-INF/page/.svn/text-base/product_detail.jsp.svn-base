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
				var $j_detailImgBlock  = $("#j_detailImgBlock"),$j_detailImgList = $("#j_detailImgList"),
					$j_detailImgScroller = $("#j_detailImgScroller"),$j_indicator = $("#j_indicator");
				
				var sc_detailImgWrapper = new iScroll('j_detailImgWrapper', {
					snap: true,
					momentum: false,
					hScrollbar: false,
					onScrollEnd: function () {
						document.querySelector('#j_indicator > li.active').className = '';
						document.querySelector('#j_indicator > li:nth-child(' + (this.currPageX+1) + ')').className = 'active';
					}
				 });
				
				$j_detailImgBlock.removeClass("hide");
				$("#j_imgNav").removeClass("hide");
				var sb = $.StringBuffer();
				var sbnum = $.StringBuffer();
				var imgs = ["${ctx}/res/img/1_270.jpg","${ctx}/res/img/2_270.jpg","${ctx}/res/img/3_270.jpg","${ctx}/res/img/4_270.jpg"];
				var len = imgs.length;
				
				for(var i=0,j=len;i<j;i++){
					sb.append("<li><img src='"+imgs[i]+"' width=\"100%\" /></li>");
					if(i==0){
						sbnum.append("<li class='active'>"+(i+1)+"</li>");
					} else {
						sbnum.append("<li>"+(i+1)+"</li>");
					}
				}
				$j_detailImgBlock.css({"width":width+"px","height":width+"px"});
				$j_detailImgList.html(sb.toString());
				$j_indicator.html(sbnum.toString());
				$j_detailImgList.css({"width":(width*len)+"px","height":width+"px"});
				$j_detailImgScroller.css({"width":(width*len)+"px","height":width+"px"});
				$("#j_detailImgList li").css({"width":width+"px","height":width+"px"});
				
				try{
					sc_detailImgWrapper.currPageX = 0;
					sc_detailImgWrapper.refresh();
					if(len==1){
						sc_detailImgWrapper.disable();
					} else {
						sc_detailImgWrapper.enable();
					}
				} catch (e){
					alert("error:"+e);
				}
				
				$("#j_scroll_page").css("height",height+"px");
				var sc_scroll_page = new iScroll("j_scroll_page");
			});
		</script>
	</head>
	<body>
		<div id="j_scroll_page">
		<div>
			<div id="j_detailImgBlock" class="clearfix">
				<div id="j_detailImgWrapper" class="imgblock">
					<div id="j_detailImgScroller">
						<ul id="j_detailImgList" class="clearfix">
							
						</ul>
					</div>
				</div>
			</div>
	
			<div id="j_imgNav" class="clearfix">
				<ul id="j_indicator">
					
				</ul>
			</div>
			
			<div class="introduce">
				<h2>小家电全场八折</h2>
				<time>8.15-8.31</time>
				<div class="price"><em class="cur_price">现价:<b>&nbsp;&nbsp;¥558</b></em><em class="ori_price">原价:<b>&nbsp;&nbsp;¥618</b></em></div>
				<div class="total_buy">599人购买</div>
			</div>
			
			<div class="product_detail_title">商品详情</div>
			
			<div class="product_detail_content">
				<div class="d_div"><img alt="美女" src="${ctx}/res/img/5_270.jpg"></div>
				<div class="d_div"><img alt="美女" src="${ctx}/res/img/6_270.jpg"></div>
				
				<div class="detail">
					衣服是当前流行的服饰搭配元素,想要把衣服搭得美丽,来看美丽说百万时尚网友精心挑选出的当季最流行的衣服单品、最佳搭配、购买心得、购买链接
				</div>
				
				<b class="go_buy">购买此产品</b>
			</div>
			
			
			
			
		</div>
		</div>
		
	</body>
</html>