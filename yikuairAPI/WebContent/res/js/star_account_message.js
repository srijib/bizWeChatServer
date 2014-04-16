//var prefix="http://localhost/static";
var prefix = "http://img.yikuair.com/static";

function fnAlert(String,time) {
	window.fnCloneDialog({
        content:"<div class='dialogTip'>"+String+"</div>"
    });
	if (time) {
		setTimeout(fnCloseAlert,time);
	}
}
function fnCloseAlert () {
	$('#alertBoxWrap').remove();
    $('#j_dialog').remove();
}


$$.ready(function () {
    var accountMessage = $$.define('accountMessage',function(vm){
    	var j_tree_Html = $("#j_tree").val();
    	var tree_json = $.parseJSON(j_tree_Html);
    	vm.data = {
    		id:"",	
    		tree_json:tree_json,
    		messageCount:$("#j_count").val(),//发送的消息条数
    		liArray:[
    		    {
    		    	title:"文字",
    		    	active:true,
    		    },{
    		    	title:"图片",
    		    	active:false,	
    		    },{
    		    	title:"图文",
    		    	active:false
    		    }
    		],
    		operateIndex:0,
    		messageData:{
    			tab0:{
	    			token:"1",
	        		content:""
	        	},
	        	tab1:{
	        		token:"2",
	        		smallImgPath:"",
	        		filePath:""
	        	},
	        	tab2:{
	        		token:"4",
	        		title:"",
	        		imgpath:"",
	        		content:"",
	        		url:""
	        	}
    		},
    		formatTab1Path:"",
    		formatTab2Path:""
    	};
    	vm.fnSave = function (evt,_this) {
    		evt && evt.preventDefault();
    	};
    	vm.fnTabs = function (evt,index) {
    		evt && evt.preventDefault();
    		var array = vm.data.liArray;
    		for (var i = 0, j = array.length ;i < j ; i++) {
    			var _obj = array[i];
    			if (i==index) {
    				_obj.active = true;
    				vm.data.operateIndex = index;
    			} else {
    				_obj.active = false;
    			}
    		}
    	};
    	
    	
    	//保存发布消息
    	vm.fnSendMessage = function (evt,index) {
    		evt && evt.preventDefault();
    		//alert(index);
    		
    		var json = {};
    		//{"token":"1","content":""} 
    		//{"token":"2","smallImgPath":"缩略图url",filePath:"原始图url"}
    		//{"token":"4","title":"","imgpath":"缩略图url","content":"","url":"跳转url"}
    		var messsageData = vm.data.messageData;
    		var tab = {};
    		var errorArray = [];
    		if (index==0) {
    			tab = messsageData.tab0;
    			var content = tab.content;
    			if (!content) {
    				errorArray.push("填写消息内容");
    			} 
    		} else if (index==1) {
    			tab = messsageData.tab1;
    			var filePath = tab.filePath;
    			if (!filePath) {
    				errorArray.push("请先上传图片");
    			}  
    		} else if (index == 2 ) {
    			tab = messsageData.tab2;
    			if (!tab.title) {
    				errorArray.push("请填写标题");
    			} else if (!tab.imgpath) {
    				errorArray.push("请先上传图片");
    			} else if (!tab.content) {
    				errorArray.push("填写消息内容");
    			} else if (!tab.url) {
    				errorArray.push("填写跳转url");
    			}
    		}
    		
    		if(errorArray.length>0) {
    			fnAlert(errorArray.join("<br/>"),1500);
    			return;
    		}
    		
    		//submit 
    		
    		var _tab = tab.$model;
    		_tab.id = messageJson.id;
    		
    		
    		$.ajax({
                type: "post",
                contentType: "application/json",
                dataType: "json",
                url: "/yikuairAPI/admin/public/push",
                data: $.toJSON(_tab),
                success: function (data) {
                	if (data.code==200) {
                		vm.data.messageCount = data.count;
                		
                		vm.data.messageData.tab0.content = "";
                		vm.data.messageData.tab1.smallImgPath = "";
                		vm.data.messageData.tab1.filePath = "";
                		vm.data.messageData.tab2.title = "";
                		vm.data.messageData.tab2.imgpath = "";
                		vm.data.messageData.tab2.content = "";
                		vm.data.messageData.tab2.url = "";
                		vm.data.formatTab1Path = "";
                		vm.data.formatTab2Path = "";
                		
                		fnAlert("发送消息成功",1500);
                	}
                	
                },
                error: function (err) {
                	fnAlert("err:" + err.message,2000);
                }
            });
    		
    	};
    	
    	vm.fnDelete = function (evt,tab) {
    		evt && evt.preventDefault();
    		if ( tab =="tab2" ) {
    			vm.data.formatTab1Path = "";
    			vm.data.messageData.tab1.filePath = "";
				vm.data.messageData.tab1.smallImgPath = "";
    		} else if (tab == "tab3" ) {
    			vm.data.formatTab2Path = "";
    			vm.data.messageData.tab2.imgpath = "";
    		}
    	}
    	
    	
    	/**
    	 * 
    	 * String longDate = params.getStr("longDate", "");//当前时间戳 ，仅当前时间为5分钟有效
		   String username = params.getStr("username", "");//用户名
		   String password = params.getStr("password", "");//明星密码
		   String data = params.getStr("data", "");//消息内容
		   String msguuid = params.getStr("msguuid", "");//唯一编码
		*/
    	
    	
    	
    });
    
    
    
    
    /**
     * String username = params.getStr("username", "");
			String password = params.getStr("password", "");
			String token = params.getStr("token", "");
			String type = params.getStr("type", "");
			String from = params.getStr("from", "");
			String to = params.getStr("to", "");
			String msguuid = params.getStr("msguuid", "");
     * 
     * */
    var message = $("#j_message").val();
    var messageJson = $.parseJSON(message);
    var username = messageJson.username;
    var fileType = "jpg|JPG|bmp|BMP|jpeg|JPEG|png|PNG";//ajax
	new AjaxUpload($("#j_uploadTab1"),{
		action :'/yikuairAPI/a/upload/file',
		name :'upload',
		fileType:fileType,
		single:false,
		data:{"username":username,"from":messageJson.id,"to":"-1","msguuid":username+(new Date - 0),"type":"-1","token":"2"},
		onComplete:function (file,obj){
			/*
			{
				"code":200,
				"data":
				{
					"content":"",
					"filePath":"/img/2014-01-27/yy001_1390754012770.jpg",
					"from":"1",
					"fromName":"",
					"longDate":"1390754014108",
					"msguuid":"yy0011390754001124",
					"offline":"0",
					"smallImgPath":"/smallimg/2014-01-27/yy001_1390754012770.jpg",
					"to":"-1",
					"token":"2",
					"type":"-1"
				},
				"message":"success",
				"token":"6"
			}
			*/
			var json = eval("("+obj+")");
			if(json&&json.code=="200"){
				accountMessage.data.messageData.tab1.filePath = json.data.filePath;
				accountMessage.data.messageData.tab1.smallImgPath = json.data.filePath.smallImgPath;
				accountMessage.data.formatTab1Path = prefix+json.data.filePath;
			}
		},
		onChange:function (file,extension) {
			//
		}
	});
	
	new AjaxUpload($("#j_uploadTab2"),{
		action :'/yikuairAPI/a/upload/file',
		name :'upload',
		fileType:fileType,
		single:false,
		data:{"username":username,"from":messageJson.id,"to":"-1","msguuid":username+(new Date - 0),"type":"-1","token":"2"},
		onComplete:function (file,obj){
			/*
			{
				"code":200,
				"data":
				{
					"content":"",
					"filePath":"/img/2014-01-27/yy001_1390754012770.jpg",
					"from":"1",
					"fromName":"",
					"longDate":"1390754014108",
					"msguuid":"yy0011390754001124",
					"offline":"0",
					"smallImgPath":"/smallimg/2014-01-27/yy001_1390754012770.jpg",
					"to":"-1",
					"token":"2",
					"type":"-1"
				},
				"message":"success",
				"token":"6"
			}
			*/
			var json = eval("("+obj+")");
			if(json&&json.code=="200"){
				accountMessage.data.messageData.tab2.imgpath = json.data.filePath;
				accountMessage.data.formatTab2Path = prefix+json.data.filePath;
			}
		},
		onChange:function (file,extension) {
			//
		}
	});
	
	
	
	$$.scan();
	
});