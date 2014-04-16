function getBrowserV(){/*获取浏览器版本*/
	var Sys = {};
	var ua = navigator.userAgent.toLowerCase();
	var s;
	(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
	(s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
	(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
	(s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
	(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
	return Sys;
}
Array.prototype.removeArrayRecord = function(array,id){/*移除数组中的某条记录*/
	for(var i=0,n=0;i<array.length;i++){
		if(array[i]!=id) array[n++]=array[i];
    }
    array.length = array.length-1;
    return array;
};
Array.prototype.checkArrayRecord = function(array,id){/*检查数据库中的某条记录是否存在 true 代表存在，false代表不存在*/
	for(var i=0;i<array.length;i++){
        if(array[i]==id) return true;
    }
    return false;
};
Array.prototype.desc = function(){
	var _this = this;
	var temp;
	for (var i=0,j=_this.length;i<j;i++){
		for(var m=i,n=j-1;m<n;m++){
			if(_this[i]<_this[m+1]){
				temp = _this[m+1];
				_this[m+1] = _this[i];
				_this[i] = temp;
			}
		}
	}
	return _this;
};
function getEvent(event){
	var event = event || window.event;//兼容ff下event
	return event;
}
String.prototype.trim = function(){//脚本去2边的空格
	return this.replace(/(^\s*)|(\s*$)/g, "");
};

window.onerror = function (msg,url,line){
	return true;
};

function StringBuffer(){//stringBuffer的应用
	this._strings_ = new Array();
}
StringBuffer.prototype.append = function (s){
	this._strings_.push(s);
};
StringBuffer.prototype.toString = function (){
	return this._strings_.join("");
};

String.prototype.isPhone = function (){//验证电话号码
	var regExp = /^1[3458]\d{9}$/;
//	var regExp = /^((13[0-9])|(15[^4,\D])|(18[0,5-9]))\d{8}$/;
	return regExp.test(this);
};
String.prototype.isEmail = function (){//验证邮箱
//	var regExp = /^[a-zA-Z0-9\.\_\-]+@[a-zA-Z0-9\.]+\.[A-Za-z0-9]+$/;
	var regExp =  /^([a-z0-9A-Z]+[_\-|\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\.)+[a-zA-Z]{2,}$/;
	return regExp.test(this);
}; 
String.prototype.isPostCode = function(){//验证邮政编码
	var regExp = /^\d{6}$/;
	return regExp.test(this);
};
String.prototype.isNum = function(){//验证是否为数字
	var  regExp = /^[1-9]{1}(\d+)?$/;
	return regExp.test(this);
};
String.prototype.isFloat = function(){//验证浮点型
	var regExp=/^[0-9]*\.?[0-9]*$/;
	return regExp.test(this);
};
Number.prototype.toFixed = function(s){
    return (parseInt(this * Math.pow( 10, s ) + 0.5)/ Math.pow( 10, s )).toString();
};


var EventUtil = new Object();
EventUtil.addEventHandler = function(oTarget,sEventType,fnHandler){
	if(oTarget.addEventListener){//for dom - compliant false
		oTarget.addEventListener(sEventType,fnHandler,false);
	} else if(oTarget.attachEvent){ //for ie
		oTarget.attachEvent("on"+sEventType,fnHandler);
	} else {//other browser
		oTarget["on"+sEventType] = fnHandler;
	}
};
EventUtil.removeEventHandler = function (oTarget,sEventType,fnHandler){
	if(oTarget.removeEventListener){
		oTarget.removeEventListener(sEventType,fnHandler,false);
	} else if(oTarget.detachEvent){
		oTarget.detachEvent("on"+sEventType,fnHandler);
	} else {
		oTarget["on"+sEventType] = null;
	}
};
EventUtil.formatEvent  = function(oEvent){
	if(getBrowserV().ie){
		oEvent.charCode=(oEvent.type=="keypress")?oEvent.keyCode:0;
		oEvent.eventPhase=2;
		oEvent.isChar=(oEvent.charCode>0);
		oEvent.pageX=oEvent.clientX+document.body.scrollLeft;
		oEvent.pageY=oEvent.clientY+document.body.scrollTop;
		oEvent.preventDefault=function(){
			this.returnValue=false;
		};	
		if(oEvent.type=="mouseout"){
			oEvent.relatedTarget=oEvent.toElement;
		}else if(oEvent.type=="mouseover"){
			oEvent.relatedTarget=oEvent.fromElement;
		}	
		oEvent.stopPropagation=function  () {
			this.cancelBubble=true;
		};	
		oEvent.target=oEvent.srcElement;
		oEvent.time=(new Date).getTime();
	}
	return oEvent;
};
EventUtil.getEvent=function(){//caller获取调用改函数本身的对象 ，callee获取函数本身对象
	if(window.event){
		return this.formatEvent(window.event);
	}else{
		return EventUtil.getEvent.caller.arguments[0];
	}
};

var oldSetTimeout = window.setTimeout;//保留系统原方法句柄
window.setTimeout = function(fn,tCount){
	if(typeof fn == 'function'){
		var args = Array.prototype.slice.call(arguments,2);//截取第三个参数的所有参数
		var fnPushP = function(){
			fn.apply(null,args);//第一根参数代表（一般为this，函数对象）->(arguments,钱一个函数的参数)，如果参入是制定数组可以传null
		};
		return oldSetTimeout(fnPushP,tCount);
	}
	return oldSetTimeout(fn,tCount);
};
/**
 * 
 * @file_name commons-0.1.gaopeng.js
 * @Description: TODO  创建cookie
 * @author wenwei.wan@gaopeng.com
 * @date 2011-9-15 上午10:20:04 
 * @param name cookie的名字
 * @param value cookie值
 * @param days cookie有效时间
 */
function createCookie(name,value,days){
	if (days){
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = ";expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = name+"="+value+expires+"; path=/";
}
/**
 * 
 * @file_name commons-0.1.gaopeng.js
 * @Description: TODO cookie的读取
 * @author wenwei.wan@gaopeng.com
 * @date 2011-9-15 上午10:22:49 
 * @param name cookie的名字
 * @returns 返回的cookie的名字
 */
function readCookie(name){
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++)
	{
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
}
/**
 * 
 * @file_name commons-0.1.gaopeng.js
 * @Description: TODO 清空cookie
 * @author wenwei.wan@gaopeng.com
 * @date 2011-9-15 上午10:23:37 
 * @param name cookie的名字
 */
function eraseCookie(name){
	createCookie(name,"",-1);
}
/**
 * 
 * @file_name commons-0.1.gaopeng.js
 * @Description: TODO 获取url的参数名字
 * @author wenwei.wan@gaopeng.com
 * @date 2011-9-29 下午5:40:47 
 * @param name url的参数名字
 * @returns 参数值
 */
function getParam(name){
	var domainUrl = location.search;
	var params = unescape(domainUrl).substring(1,domainUrl.length).split("&");
	for(var i=0,j=params.length;i<j;i++){
		if(params[i].indexOf("=") > 0){
			if (params[i].split("=")[0].toLowerCase()==name.toString().toLowerCase()){
				return params[i].split("=")[1];
			}
		}
	}
	return null;
}
/**
* 
* @file_name log.js
* @Description: TODO firebug下面的调试，在发布版的时候将console.assert改为false
* @author wenwei.wan@gaopeng.com
* @date 2011-9-13 上午11:47:27 
* @param msg 输出信息
* @param level  日志级别
*/
function fnWriteLog(msg,level){
	if(false&&console&&console.assert){
		if(level==="log"){
			console.log(msg);
		} else if(level==='info'){
			console.info(msg);
		} else if(level==='warn'){
			console.warn(msg);
		} else if(level==='error'){
			console.error(msg);
		} else {
			console.log(msg);
		}
	}
}
/**
 * 所有的时间格式化
 */
var fnListTimeFormat = {
		/**
		 * 
		 * @file_name commons-0.1.gaopeng.js
		 * @Description: TODO 将时间转换为字符串类型
		 * @author wenwei.wan@gaopeng.com
		 * @date 2011-12-8 上午11:08:27 
		 * @param NZRTime
		 * @returns {String}
		 */
		fnDateToString:function(NZRTime){/*例时间类型:Fri Dec 12 1980 00:00:00 GMT+0800*/
			var date = new Date(NZRTime);
			var month = date.getMonth()+1;
			if((month+"").length==1) month = "0"+month;
			var day = date.getDate();
			if((day+"").length==1) day = "0"+day;
			return date.getFullYear()+"-"+month+"-"+day;
		},
		/**
		 * 
		 * @file_name commons-0.1.gaopeng.js 
		 * @Description: TODO 把字符串，转换成为时间格式
		 * @author wenwei.wan@gaopeng.com
		 * @date 2011-12-7 下午9:47:20 
		 * @param dateString 时间字符串
		 * @param formatString 时间类型
		 * @returns 返回date类型
		 */
		fnStringToDate:function(dateString, formatString){
			/** year : /yyyy/ */  
			var _y4 = "([0-9]{4})";   
			/** year : /yy/ */  
			var _y2 = "([0-9]{2})";   
			/** index year */  
			var _yi = -1;   
			/** month : /MM/ */  
			var _M2 = "(0[1-9]|1[0-2])";   
			/** month : /M/ */  
			var _M1 = "([1-9]|1[0-2])";   
			/** index month */  
			var _Mi = -1;   
			/** day : /dd/ */  
			var _d2 = "(0[1-9]|[1-2][0-9]|30|31)";   
			/** day : /d/ */  
			var _d1 = "([1-9]|[1-2][0-9]|30|31)";   
			/** index day */  
			var _di = -1;   
			/** hour : /HH/ */  
			var _H2 = "([0-1][0-9]|20|21|22|23)";   
			/** hour : /H/ */  
			var _H1 = "([0-9]|1[0-9]|20|21|22|23)";   
			/** index hour */  
			var _Hi = -1;   
			/** minute : /mm/ */  
			var _m2 = "([0-5][0-9])";   
			/** minute : /m/ */  
			var _m1 = "([0-9]|[1-5][0-9])";   
			/** index minute */  
			var _mi = -1;   
			/** second : /ss/ */  
			var _s2 = "([0-5][0-9])";   
			/** second : /s/ */  
			var _s1 = "([0-9]|[1-5][0-9])";   
			/** index month */  
			var _si = -1;   
			var regexp = "";   
			function getDate(dateString, formatString) {   
			    if (validateDate(dateString, formatString)) {  
			        var now = new Date();   
			        var vals = regexp.exec(dateString);   
			        var index = validateIndex(formatString);   
			        var year = index[0] >= 0 ? vals[index[0] + 1] : now.getFullYear();   
			        var month = index[1] >= 0 ? (vals[index[1] + 1] - 1) : now.getMonth();   
			        var day = index[2] >= 0 ? vals[index[2] + 1] : now.getDate();   
			        var hour = index[3] >= 0 ? vals[index[3] + 1] : "";   
			        var minute = index[4] >= 0 ? vals[index[4] + 1] : "";   
			        var second = index[5] >= 0 ? vals[index[5] + 1] : "";   
			        var validate;   
			        if (hour == "") {   
			            validate = new Date(year, month, day);   
			        } else {   
			            validate = new Date(year, month, day, hour, minute, second);   
			        }   
			        if (validate.getDate() == day) {   
			            return validate;   
			        }   
			    }   
			    alert("wrong date");   
			}   
			function validateDate(_dateString, formatString) {   
			    var dateString = _dateString.trim();   
			    if (dateString == "") return;
			    var reg = formatString;   
			    reg = reg.replace(/yyyy/, _y4);   
			    reg = reg.replace(/yy/, _y2);   
			    reg = reg.replace(/MM/, _M2);   
			    reg = reg.replace(/M/, _M1);   
			    reg = reg.replace(/dd/, _d2);   
			    reg = reg.replace(/d/, _d1);   
			    reg = reg.replace(/HH/, _H2);   
			    reg = reg.replace(/H/, _H1);   
			    reg = reg.replace(/mm/, _m2);   
			    reg = reg.replace(/m/, _m1);   
			    reg = reg.replace(/ss/, _s2);   
			    reg = reg.replace(/s/, _s1);   
			    reg = new RegExp("^" + reg + "$");   
			    regexp = reg;   
			    return reg.test(dateString);   
			}   
			function validateIndex(formatString) {   
			    var ia = new Array();   
			    var i = 0;   
			    _yi = formatString.search(/yyyy/);   
			    if (_yi < 0) _yi = formatString.search(/yy/);   
			    if (_yi >= 0) {   
			        ia[i] = _yi;i++;   
			    }   
			    _Mi = formatString.search(/MM/);   
			    if (_Mi < 0) _Mi = formatString.search(/M/);   
			    if (_Mi >= 0) {   
			        ia[i] = _Mi; i++;   
			    }   
			    _di = formatString.search(/dd/);   
			    if (_di < 0) _di = formatString.search(/d/);   
			    if (_di >= 0) {   
			        ia[i] = _di; i++;   
			    }   
			    _Hi = formatString.search(/HH/);   
			    if (_Hi < 0) _Hi = formatString.search(/H/);   
			    if (_Hi >= 0) {   
			        ia[i] = _Hi; i++;   
			    }   
			    _mi = formatString.search(/mm/);   
			    if (_mi < 0) _mi = formatString.search(/m/);   
			    if (_mi >= 0) {   
			        ia[i] = _mi;   
			        i++;   
			    }   
			    _si = formatString.search(/ss/);   
			    if (_si < 0)  _si = formatString.search(/s/);   
			    if (_si >= 0) {   
			        ia[i] = _si;   
			        i++;   
			    }   
			    var ia2 = new Array(_yi, _Mi, _di, _Hi, _mi, _si);   
			    for (i = 0; i < ia.length - 1; i++) {   
			        for (var j = 0; j < ia.length - 1 - i; j++) {   
			            if (ia[j] > ia[j + 1]) {   
			                temp = ia[j];   
			                ia[j] = ia[j + 1];   
			                ia[j + 1] = temp;   
			            }   
			        }   
			    }   
			    for (i = 0; i < ia.length; i++) {   
			        for (var j = 0; j < ia2.length; j++) {   
			            if (ia[i] == ia2[j]) {   
			                ia2[j] = i;   
			            }   
			        }   
			    }   
			    return ia2;   
			}
			return getDate(dateString, formatString);
		}
};