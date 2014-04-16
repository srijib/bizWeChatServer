$$.ready(function () {
    var userManager = $$.define('userManager',function(vm){
    	var j_tree_Html = $("#j_tree").val();
    	var tree_json = $.parseJSON(j_tree_Html);
    	vm.data = {
    		tree_json:tree_json
    	};
    	
    	//{"id":"454","message":"success","comname":"富士康",
    	//"com_id":"3","msguuid":"834c4c72-c3a3-4b02-a3e3-865d1f6650e5","star":null,"own":"com","code":"200"}
    	
    	
    	
    	
    	
        vm.set = function(){
            var grid = $$.$ui('mygrid');
            //console.info( grid.getSelectedItems() );
        };

        vm.select_page = function(i){
        	datajson.pageIndex = i;
        	datajson = fnGetCondition(datajson);
        	callback(datajson);
        };
        
        vm.fnEdit = function ( evt , item ) {
        	evt && evt.preventDefault();
        	var ids = item.id;
        	fnEditCallback(ids);
        };    
        

        vm.fnDelete = function( evt,item ){
        	evt && evt.preventDefault();
            //alert(item.de_name);
            //var grid = $$.$ui('mygrid');
            var ids = item.id;
            //console.info( grid.getSelectedItems() );
            //console.info( item )
            fnDeleteCallback(ids);
        };
    });

    
    
  //j_message
	var j_message_Html = $("#j_message").val();
	var message_json = $.parseJSON(j_message_Html);
	var comname = message_json.comname;
	var own = message_json.own;
	var datajson = {};
	datajson.com_id = message_json.com_id;
	datajson.star = 0;
	datajson.pageIndex = 1;
	datajson.pageSize = 10;
	userManager.vm = comname;
	$$.scan();
	
	var $realname = $("#j_searchRealname");
	var $username = $("#j_searchUsername");
	//condition search
    function fnGetCondition (datajson) {
    	datajson.star = 1;
    	var realname = $realname.val();
    	datajson.realname = realname;
    	var username = $username.val();
    	datajson.username = username;
    	return datajson;
    }
    datajson = fnGetCondition(datajson);
	
	
	var grid = $$.$ui('mygrid');
	function callback (datajson) {
		$.ajax({
			url:"/yikuairAPI/admin/udata",//java api
			type:"post",
			data:datajson,
			timeout:3000,
			contentType: "application/x-www-form-urlencoded",
			dataType: "json",
			async:true,
			success:function (data) {
				grid.setData(data.data);
				grid.getPager().totalcount = data.total;
		        grid.getPager().pagesize = datajson.pageSize;
		        $$.scan();
			}
			
		});
	}
	callback(datajson);
	
	function fnEditCallback (ids) {
		if(!ids) {
			fnAlert("请勾选删除的人。",1500);
			return;
		}
		var bool = confirm("确认重置吗?");
		if(!bool) {
			return;
		}
		
		$.ajax({
    		url:"/yikuairAPI/admin/edit/user",//java api
    		type:"post",
    		data:{"ids":ids},
    		timeout:3000,
    		contentType: "application/x-www-form-urlencoded",
    		dataType: "json",
    		async:true,
    		success:function (data) {
    			//alert(data);
    			if(data.code==200) {
    				fnAlert("修改密码【123456】成功",1500);
    				grid.getPager().index = datajson.pageIndex = 1;
        			callback(datajson);
    			} else {
    				fnAlert(data.message,1500);
    			}
    		}
    	});
		
	}

	function fnDeleteCallback (ids) {
		if(!ids) {
			fnAlert("请勾选删除的人。",1500);
			return;
		}
		var bool = confirm("确认删除?");
		if(!bool) {
			return;
		}
    	$.ajax({
    		url:"/yikuairAPI/admin/delete/user",//java api
    		type:"post",
    		data:{"ids":ids},
    		timeout:3000,
    		contentType: "application/x-www-form-urlencoded",
    		dataType: "json",
    		async:true,
    		success:function (data) {
    			//alert(data);
    			if(data.code==200) {
    				fnAlert("删除成功",1500);
    				grid.getPager().index = datajson.pageIndex = 1;
        			callback(datajson);
    			} else {
    				fnAlert(data.message,1500);
    			}
    		}
    	});
    }
	
	$("#j_search").bind("click",function (){
		datajson = fnGetCondition(datajson);
		grid.getPager().index = datajson.pageIndex = 1;
		callback(datajson);
		fnAlert("查找中...",1500);
		
	});
	
	
	
	$("#j_EditAll").bind("click",function (evt) {
		evt && evt.preventDefault();
		var _grid = grid.getSelectedItems();
		var ids = [];
		for (var i = 0,j = _grid.length ; i < j ; i++ ) {
			ids.push(_grid[i].id);
		}
		fnEditCallback(ids.join(","));
	});
	
	$("#j_deleteAll").bind("click",function (evt) {
		evt && evt.preventDefault();
		var _grid = grid.getSelectedItems();
		var ids = [];
		for (var i = 0,j = _grid.length ; i < j ; i++ ) {
			ids.push(_grid[i].id);
		}
		fnDeleteCallback(ids.join(","));
	});
	
	
	var addContentHtml = null;
    $("#j_openAddUserDialog").bind("click",function (evt) {
        evt && evt.preventDefault();
        if (!addContentHtml) {
            var $j_addUserContent = $("#j_addUserContent");
            addContentHtml = $j_addUserContent.html();
            $j_addUserContent.remove();
        }
        window.fnCloneDialog({
            content:addContentHtml
        });

        $(".c_close").click(function (){
            $('#alertBoxWrap').remove();
            $('#j_dialog').remove();
        });
        
        $("#j_addUser").bind("click",function (evt) {
        	evt && evt.preventDefault();
        	var string = $("#j_addUserDialog form").serialize();
        	var realname = $("#j_realname").val();
        	var de_name = $("#j_de_name").val();
        	var username = $("#j_username").val();
        	if (realname==""){
        		alert("必须填写名字");
        		return;
        	} else if (de_name=="") {
        		alert("必须填写部门");
        		return;
        	} else if(username=="") {
        		alert("必须填写员工id");
        		return;
        	}
        	
        	
        	$.ajax({
        		url:"/yikuairAPI/admin/add/user",//java api
        		type:"post",
        		data:string,
        		timeout:3000,
        		contentType: "application/x-www-form-urlencoded",
        		dataType: "json",
        		async:true,
        		success:function (data) {
        			//alert(data);
        			$(".c_close").trigger("click");
        			if(data.code==200) {
        				fnAlert("添加成功",1500);
        				grid.getPager().index = datajson.pageIndex = 1;
        				datajson = fnGetCondition(datajson);
            			callback(datajson);
        			} else {
        				fnAlert(data.message,1500);
        			}
        		}
        	});
        	
        });
    });
    
    
    
    
   //upload xls
    
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
    
    
	
	$("#j_exit").bind("click",function (evt){
		evt && evt.preventDefault();
		$.ajax({
    		url:"/yikuairAPI/admin/exit",//java api
    		type:"post",
    		timeout:3000,
    		contentType: "application/x-www-form-urlencoded",
    		async:true
    	});
		location.href="http://www.yikuair.com";
	});
    

});
