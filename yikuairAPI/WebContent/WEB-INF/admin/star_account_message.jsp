<%@page import="cn.yikuair.business.MessageManager"%>
<%@page import="cn.yikuair.business.UserManager"%>
<%@page import="cn.yikuair.utils.*,java.util.*"%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>

<%
	
	
	try {
		String message = session.getAttribute("message")+"";
		String id = JsonUtil.StringToObject(message).getString("id");
		MessageManager manager = MessageManager.getMessageManager();
		int count = manager.getTodayCountMessage(id);
		request.setAttribute("count", count);
	} catch (Exception ex){
		response.sendRedirect("http://www.yikuair.com?code=error_star_message");
		ex.printStackTrace();
	}
	
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
        <link rel="shortcut icon" href="${ctx}/res/img/favicon.ico">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
        <link rel="stylesheet" href="${ctx}/res/css/global.css" />
        <link rel="stylesheet" href="${ctx}/res/css/onionui.css" />
        <link rel="stylesheet" href="${ctx}/res/css/yikuair.css" />
        <script src="${ctx}/res/js/jquery-1.10.2.min.js"></script>
        <script src="${ctx}/res/js/json2.js"></script>
        <script src="${ctx}/res/js/common.js"></script>
        <script src="${ctx}/res/js/ajaxupload.js"></script>
        <script src="${ctx}/res/js/com.alert.js"></script>
        <script src="${ctx}/res/js/avalon.js"></script>
        <script src="${ctx}/res/js/avalon-filter.js"></script>
        <script src="${ctx}/res/js/ui.pager.js"></script>
        <script src="${ctx}/res/js/ui.grid.js"></script>
        <script src="${ctx}/res/js/yikuair.js"></script>
        <script src="${ctx}/res/js/star_account_message.js"></script>
        <!--[if lt IE 9]>
            <script src="${ctx}/res/js/html5.js"></script>
        <![endif]-->
    </head>
    <body ms-controller="accountMessage">
        <div class="frame_left">
            <div class="company_logo">
                <img  src="${ctx}/res/img/logo_foxconn.png" />
            </div>

            <ul class="jusridiciton_list" ms-each-el="data.tree_json">
                <li ms-attr-class="$index==0?'active':''" ><a ms-attr-href="el.url"><i></i>{{el.name}}</a></li>
            </ul>
        </div>
        <div class="frame_right">
            <div class="top_nav">
                <a class="c_exit" href="#">安全退出</a>
            </div>

            <!-- grid search begin -->
            <div class="grid account_message clearfix" id="j_right1">
                <div class="grid_title">
                    <em>账户信息</em>
                </div>
                <ul class="title_tabs" ms-each="data.liArray">
                    <li ms-attr-class="el.active==true?'active':''" ><a href="#" ms-text="el.title" ms-click="fnTabs($event,$index)" ></a></li>
                </ul>

                <div class="tab1" ms-if="data.operateIndex==0">
                    <textarea placeholder="内容" ms-duplex="data.messageData.tab0.content"></textarea>
                </div>

                <div class="tab2" ms-if="data.operateIndex==1">
                    <div class="tab2_nav">
                        <a class="upload" href="#" id="j_uploadTab1">上传单张图片</a><span>大小图片不超过2m，格式pmp,jpg,png,jpeg</span>
                    </div>
                    <div class="frame_img">
                        <img width="240" height="200" ms-attr-src="data.formatTab1Path" /><a href="#" ms-if="data.formatTab1Path!=''" ms-click="fnDelete($event,'tab2')" class="deleteImg">删除</a>
                    </div>
                </div>

                <div class="tab3" ms-if="data.operateIndex==2" >
                    <input class="img_content_title" placeholder="标题" ms-duplex="data.messageData.tab2.title" />

                    <div class="tab3_nav">
                        <a class="upload" href="#" id="j_uploadTab2">上传单张图片</a><span>大小图片不超过2m，格式pmp,jpg,png,jpeg</span>
                    </div>
                    <div class="frame_img">
                        <img width="240" height="200" ms-attr-src="data.formatTab2Path" /><a href="#" ms-if="data.formatTab2Path!=''" ms-click="fnDelete($event,'tab3')" class="deleteImg">删除</a>
                    </div>

                    <div class="tab3_1">
                        <textarea ms-duplex="data.messageData.tab2.content"></textarea>
                    </div>

                    <input class="img_content_url" placeholder="链接" ms-duplex="data.messageData.tab2.url" />
                </div>

                <a class="message_submit" ms-click="fnSendMessage($event,data.operateIndex)">发送消息</a>
                <span class="messageline">你今天还能群发<em>{{10 - data.messageCount}}</em>条消息</span>
            </div>
            <!-- grid search end -->
        </div>
		<input type="hidden" id="j_tree" value='${sessionScope.treeObj}' />
		<input type="hidden" id="j_message" value='${sessionScope.message}' />
		<input type="hidden" id="j_com_id" value='${sessionScope.com_id}' />
		<input type="hidden" id="j_count" value='${count}' />
    </body>
    <!--[if lt IE 7]>
        <script src="${ctx}/res/js/dd_belatedpng.js"></script>
        <script>DD_belatedPNG.fix('.company_logo img,.grid_content .search_parent .c_search i');</script>
    <![endif]-->
</html>
