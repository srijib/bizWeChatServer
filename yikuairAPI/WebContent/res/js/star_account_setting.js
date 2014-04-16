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
    var accountSetting = $$.define('accountSetting',function(vm){
    	var j_tree_Html = $("#j_tree").val();
    	var tree_json = $.parseJSON(j_tree_Html);
    	vm.data = {
    		id:"",	
    		tree_json:tree_json,
    		headurl:"",
    		absheadurl:"",
    		des:""
    	};
    	vm.fnSave = function (evt,_this) {
    		evt && evt.preventDefault();
    		//alert(vm.data.headurl+"--"+vm.data.des);
    		
    		var json = {};
    		json.headurl = vm.data.headurl;
    		json.signature = vm.data.des;
    		json.from = vm.data.id;
    		
    		$.ajax({
                type: "post",
                contentType: "application/json",
                dataType: "json",
                url: "/yikuairAPI/admin/upload/header",
                data: $.toJSON(json),
                success: function (data) {
                	fnAlert("修改成功",1500);
                },
                error: function (err) {
                	fnAlert("err:" + err);
                }
            });
    		
    	};
    });
    
    $$.scan();
    
    /*
     * String username = params.getStr("username", "");
	   String password = params.getStr("password", "");
	   String from = params.getStr("from", "");
	   String msguuid = params.getStr("msguuid", "");
    */
    
    var message = $("#j_message").val();
    var messageJson = $.parseJSON(message);
    accountSetting.data.id = messageJson.id;
    var headurl = $("#j_headurl").val();
    var signature = $("#j_signature").val();
    accountSetting.data.headurl = headurl;
    accountSetting.data.absheadurl = prefix+headurl;
    accountSetting.data.des = signature;
    
	var fileType = "jpg|JPG|bmp|BMP|jpeg|JPEG|png|PNG";//ajax
	new AjaxUpload($("#j_uploadHeader"),{
		action :'/yikuairAPI/a/upload/header',
		name :'upload',
		fileType:fileType,
		single:false,
		data:{"username":"star","from":messageJson.id,"msguuid":messageJson.msguuid},
		onComplete:function (file,obj){
			//{"code":200,"data":{"longDate":"1390631511957","headurl":"/header/2014-01-25/star_1390631511959.jpg"},"message":"success","token":"12"}
			var json = eval("("+obj+")");
			if(json&&json.code=="200"){
				var headurl = json.data.headurl;
				var imgurl = prefix+headurl;
				accountSetting.data.headurl = headurl;
				accountSetting.data.absheadurl = imgurl;
				
				
				
				//$("#j_filterHeader").html("<img width='86' height='86' src='"+imgurl+"'  />");
				//alert("html::"+$("#j_filterHeader").html());
			}
		},
		onChange:function (file,extension) {
			//
		}
	});
	
});