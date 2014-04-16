<%@page import="cn.yikuair.business.UserManager"%>
<%@page import="cn.yikuair.utils.*,java.util.*"%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>

<%
/*
	dataMap.put("headurl", map.get("headurl"));
	String signature = map.get("signature");
	if (null!=signature && signature.length()>0) {
		signature = new String(DataUtil.decodeBase64(signature));
	}
	dataMap.put("signature", signature);
*/
	try {
		String message = session.getAttribute("message")+"";
		String id = JsonUtil.StringToObject(message).getString("id");
		UserManager manager = UserManager.getUserManager();
		List <Map<String,String>> list = manager.getUserById(id);
		if (list.size()>0) {
			String headurl = list.get(0).get("headurl");
			String signature = list.get(0).get("signature");
			request.setAttribute("headurl", headurl);
			request.setAttribute("signature", signature);
		}
	} catch (Exception ex){
		response.sendRedirect("http://www.yikuair.com?code=error_star_account");
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
        <script src="${ctx}/res/js/star_account_setting.js"></script>
        <!--[if lt IE 9]>
            <script src="${ctx}/res/js/html5.js"></script>
        <![endif]-->
    </head>
    <body ms-controller="accountSetting">
        <div class="frame_left">
            <div class="company_logo">
                <img  src="${ctx}/res/img/logo_foxconn.png" />
            </div>

            <ul class="jusridiciton_list" ms-each-el="data.tree_json">
                <li ms-attr-class="$index==1?'active':''" ><a ms-attr-href="el.url"><i></i>{{el.name}}</a></li>
            </ul>
        </div>
        <div class="frame_right" >
            <div class="top_nav">
                <a class="c_exit" href="#">安全退出</a>
            </div>

            <!-- grid search begin -->
            <div class="grid" id="j_right1">
                <div class="grid_title">
                    <em>账户信息</em>
                </div>
                <div class="grid_content clrfix">
                    <table class="table_cell accountSetting">
                        <tr>
                            <td class="td1">头像:</td>
                            <td class="td2"><a class="uploadHeader" id="j_uploadHeader">上传头像</a></td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>
                            	<span class="filterHeader" id="j_filterHeader">
                            		<img width="86" height="86" ms-attr-src="data.absheadurl" >
                            	</span>
                            </td>
                        </tr>
                        <tr>
                            <td>功能介绍:</td>
                            <td>
                                <textarea ms-duplex="data.des"></textarea>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" class="t_a_c">
                                <a class="submitHeader" ms-click="fnSave($event,this)">保存</a>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <!-- grid search end -->
        </div>
        
        <input type="hidden" id="j_tree" value='${sessionScope.treeObj}' />
		<input type="hidden" id="j_message" value='${sessionScope.message}' />
		<input type="hidden" id="j_com_id" value='${sessionScope.com_id}' />
		<input type="hidden" id="j_headurl" value='${headurl}' />
		<input type="hidden" id="j_signature" value='${signature}' />

    </body>
    <!--[if lt IE 7]>
        <script src="${ctx}/res/js/dd_belatedpng.js"></script>
        <script>DD_belatedPNG.fix('.company_logo img,.grid_content .search_parent .c_search i');</script>
    <![endif]-->
</html>
