;(function($) {
   
    // 插件
    $.fn.XX = function(param) {   
       
    }; 

})(jQuery);

//左侧划拉提示框
// param={content,color,width,height}
function infoTip(param){

	if(!param ||(param && !param.content)){
		param.content = "";
	}

	if($(".infoTip").length <= 0){
		$("<div class='infoTip'></div>").appendTo($("body"));
	}
	
	var that = $("<div class='infoTip-wrap'>"
				+"<div class='infoTip-bg'></div>"
				+"<div class='infoTip-content'>"+param.content+"</div>"
				+"<div class='infoTip-log'></div>"
				+"</div>");
	$(".infoTip").append(that);

	if(param.color){
		that.children(".infoTip-bg").css({"background": param.color});
		that.children(".infoTip-log").css({"border-left-color": param.color});
	}

	var contentWidth = that.children(".infoTip-content").width();
	var contentHeight = that.children(".infoTip-content").height();

	var w = contentWidth +30; // 30为padding
	var h = contentHeight +30;

	var infoTip_left = $(window).width() - w-10; //left
	var infoTip_top = $(window).height() - h-10; //top 10为每一个tip的间距

	that.css({"width": w, "height": h, "left": w +10});

	if(that.siblings().length<=0){
		$(".infoTip").css({"top": infoTip_top,"left": infoTip_left});
	}

	that.children(".infoTip-log").css({"left": w, "top":  (h-8)/2 - 4});

	if(that.siblings().length > 0){
		$(".infoTip").animate(
			{ 
				"top": $(window).height()-$(".infoTip").height(),
			},
			{ 
			    easing: 'easeInOutBack', 
			    duration: 400//, 
			    //complete: callback 
			}
		);
	}
		
	that.animate(
		{"left": 0 },
		{ 
		    easing: 'easeInOutBack', 
		    duration: 400, 
		    complete: function(){
		    	that.delay(4000).animate(
		    		{"left": w + 10 },
		    		{ 
					    easing: 'easeInOutBack', 
					    duration: 400, 
					    complete: function(){
					    	that.remove(); //清除
		    				$(".infoTip").css({"top": $(window).height()-$(".infoTip").height()});
					    }
					}
		    	);
		    }
		}
	);
				
}
