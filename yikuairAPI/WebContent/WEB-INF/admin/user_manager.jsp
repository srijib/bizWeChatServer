<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/libs.jsp" %>
<%@ page import="java.util.*,cn.yikuair.utils.*,net.sf.json.JSONObject" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>一块儿－企业移动后台管理平台 </title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
        <link rel="shortcut icon" href="${ctx}/res/img/favicon.ico">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
        <link rel="stylesheet" href="${ctx}/res/css/onionui.css" />
        <link rel="stylesheet" href="${ctx}/res/css/yikuair.css" />
        <script src="${ctx}/res/js/jquery-1.10.2.min.js"></script>
        <script src="${ctx}/res/js/json2.js"></script>
        <script src="${ctx}/res/js/common.js"></script>
        <script src="${ctx}/res/js/ajaxupload.js"></script>
        <script src="${ctx}/res/js/com.alert.js"></script>
        <script src="${ctx}/res/js/avalon.js"></script>
        <script src="${ctx}/res/js/ui.pager.js"></script>
        <script src="${ctx}/res/js/ui.grid.js"></script>
        <script src="${ctx}/res/js/yikuair.js"></script>
        <script src="${ctx}/res/js/user_manager.js"></script>
        <!--[if lt IE 9]>
            <script src="${ctx}/res/js/html5.js"></script>
        <![endif]-->
    </head>
    <body ms-controller="userManager">
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
                <a class="c_exit" href="#" id="j_exit">安全退出</a>
            </div>

            <!-- grid search begin -->
            <div class="grid clearfix" id="j_right1">
                <div class="grid_title">
                    <em>人员查询</em>
                </div>
                <div class="grid_content clrfix">
                    <div class="input_parent">
                        <label>姓名</label><input id="j_searchRealname" type="text" >
                    </div>
                    <div class="input_parent">
                        <label>性别</label>
                        <select id="j_searchSex">
                        	<option value="">不限</option>
                            <option value="1">男</option>
                            <option value="0">女</option>
                        </select>
                    </div>
                    <div class="input_parent">
                        <label>员工ID</label><input id="j_searchUsername" type="text" >
                    </div>
                    <div class="input_parent">
                        <label>手机</label><input id="j_searchMobile" type="text" >
                    </div>
                    <div class="input_parent">
                        <label>接口人</label>
                        <select id="j_searchInterface">
                        	<option value="">不限</option>
                            <option value="1">是</option>
                            <option value="0">否</option>
                        </select>
                    </div>
                    <div class="input_parent">
                        <label>部门</label><input id="j_searchDe_name" type="text" >
                    </div>
                    <div class="input_parent">
                        <label>职位</label><input id="j_searchDuty" type="text" >
                    </div>
                    <div class="search_parent">
                        <span class="c_search" id="j_search"><i></i>搜索</span>
                    </div>
                </div>
            </div>
            <!-- grid search end -->


            <!-- grid table begin -->

            <div class="grid clearfix" id="j_right2">
                <div class="grid_title">
                    <em>人员列表</em>
                </div>

                <div ms-widget="grid,mygrid" class="ui-grid" data-grid-checkall="true" data-grid-onselectpage="select_page">
                    <table>
                        <thead>
                            <tr>
                                <th data-width="100px">ID</th>
                                <th data-width="100px">公司</th>
                                <th data-width="260px">部门</th>
                                <th data-width="100px">姓名</th>
                                <th data-width="100px">职位</th>
                                <th data-width="100px">手机</th>
                                <th data-width="50px">性别</th>
                                <th data-width="50px">接口人</th>
                                <th data-width="auto">操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>{{ item.username }}</td>
                                <td>${comname}</td>
                                <td>{{ item.de_name }}</td>
                                <td>{{ item.realname }}</td>
                                <td>{{ item.duty }}</td>
                                <td>{{ item.mobile }}</td>
                                <td>{{ item.sex==0?'女':'男' }}</td>
                                <td>{{ item.interface==1?'是':'否' }}</td>
                                <td><a href="#" ms-click="fnEdit($event,item)">重置密码</a>&nbsp;&nbsp;&nbsp;<a href="#" ms-click="fnDelete($event,item)">删除</a></td>
                            </tr>
                        </tbody>
                    </table>
                    <div></div>
                    <blockquote>
                        无数据
                    </blockquote>
                </div>
            </div>
            <!-- grid table end -->
            <!-- button list begin -->
            <div class="button_list clearfix">
                <a href="#" id="j_openAddUserDialog">添加</a>
                <a href="#" id="j_uploadXls">批量导入</a>
                <a href="#" id="j_deleteAll">删除</a>
                <a href="#" id="j_EditAll">重置密码</a>
            </div>
            <!-- button list begin -->
        </div>
		<input type="hidden" id="j_tree" value='${sessionScope.treeObj}' />
		<input type="hidden" id="j_message" value='${sessionScope.message}' />
		<input type="hidden" id="j_com_id" value='${sessionScope.com_id}' />
		
		<div id="j_addUserContent" class="hide">
            <div id="j_addUserDialog" class="c_addUserDialog">
                <i class="c_close"></i>
                <form>
                <h2>人员添加</h2>
                <div class="mt_30">
                    <label>姓名</label>
                    <input name="realname" id="j_realname" type="text" >
                </div>
                <div>
                    <label>性别</label>
                    <label class="radio_sex"><input type="radio" name="sex" value="1" checked="checked" >&nbsp;男</label>
                    <label class="radio_sex"><input type="radio" value="0"  name="sex" >&nbsp;女</label>
                </div>

                <div>
                    <label>部门</label>
                    <input name="de_name" id="j_de_name" type="text" >
                </div>
                <div>
                    <label>职位</label>
                    <input name="duty" type="text" >
                </div>
                <div>
                    <label>员工ID</label>
                    <input name="username" id="j_username" type="text" >
                </div>
                <input type="hidden" name="com_id" value="${sessionScope.com_id}" />
                <div>
                    <label>手机号</label>
                    <input name="mobile" type="text" >
                </div>
                <div>
                    <label>接口人</label>
                    <select name="interface">
                    	<option value="0">否</option>
                    	<option value="1">是</option>
                    </select>
                </div>
                <div class="c_addUserPar">
                    <a href="#" class="c_addUser" id="j_addUser">添加</a>
                </div>

                </form>
            </div>
        </div>
    </body>
    <!--[if lt IE 7]>
        <script src="${ctx}/res/js/dd_belatedpng.js"></script>
        <script>DD_belatedPNG.fix('.company_logo img,.grid_content .search_parent .c_search i');</script>
    <![endif]-->
</html>
