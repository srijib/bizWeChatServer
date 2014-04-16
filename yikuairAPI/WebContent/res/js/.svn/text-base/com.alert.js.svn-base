$(function(){
	var fnListCreateDialog = function (args){
		//this.width = args.width?args.width:600;
		this.content = args.content?args.content:"";
		this.createDom();
	};
	fnListCreateDialog.prototype.createDom = function(){
		var $div = $("<div id=\"j_dialog\" class=\"c_dialog\" style=\"position:fixed;*position:absolute;z-index:1000;\"></div>");
		$div.html(this.content);
		$('body').append($div);
		fnListCreateDialog.prototype.positionBlock($div);
		$(window).bind('resize',function(){fnListCreateDialog.prototype.positionBlock($div);});
		var $alertBoxWrap = $("#alertBoxWrap");
		if(getBrowserV().ie=='6.0') {
			if($alertBoxWrap.length==0){
				$('body').append('<div id="alertBoxWrap" style="z-index:998;left:0;right:0;top:0;bottom:0;background:#000;filter:alpha(opacity=50);position:absolute;width:100%;"></div>');
				$('#alertBoxWrap').height($('body').height());
				var $ifr = $('<iframe class=\"ifr_cover\" style=\"width:100%;height:100%;z-index:997;position:absolute;top:0;left:0;filter:alpha(opacity=0)\" frameborder=0 scrolling=no></iframe>');
				$ifr.css({height:$('body').height(),width:$('body').width()});
				$('#alertBoxWrap').append($ifr);
			}

			fnListCreateDialog.prototype.positionBlockIe($div);
			$(window).bind('scroll',function(){fnListCreateDialog.prototype.positionBlockIe($div);});
		}else {
			if($alertBoxWrap.length==0){
				var $filter = $('<div id="alertBoxWrap" style="position:fixed;z-index:999;left:0;right:0;top:0;bottom:0;background:#000;opacity:0.8;filter:alpha(opacity=.8);"></div>');
				$('body').append($filter);
			}
		}

	};
	fnListCreateDialog.prototype.positionBlock = function($element){
		var _left = ($(window).width()- $element.width())/2;
		var _top =  ($(window).height()-$element.height())/2;
		$element.css({'left':_left,'top':_top});
		$element.topNum = _top;
	};
	//ie 定位
	fnListCreateDialog.prototype.positionBlockIe = function($element){
		$element.css('top',($element.topNum + $(document).scrollTop()));
	};
	var fnCloneDialog = function(args){
		new fnListCreateDialog(args);
	};
	window.fnCloneDialog = fnCloneDialog;
});
