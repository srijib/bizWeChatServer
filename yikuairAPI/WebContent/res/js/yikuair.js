$(function () {
    var wh,ww;//window 高宽
    var $frame = $(".frame_left,.frame_right");

    (function () {
        wh = $(window).height(),ww = $(window).width();
        
        var right1 = $("#j_right1").height();
        var $right2 = $("#j_right2");
        
        var right2 = 0;
        if($right2.length>0) {
        	right2 = $("#j_right2").height();
        }
        var newHeight = right1+right2+160;
        
        if(wh>newHeight){
        	newHeight = wh;
        }
        $frame.css("height",newHeight);
        setTimeout(arguments.callee,500);
    })();
});

