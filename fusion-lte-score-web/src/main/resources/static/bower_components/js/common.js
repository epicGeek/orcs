$.ajaxSetup({
    complete:function(XMLHttpRequest,textStatus){
          if(textStatus=="parsererror"){
//        	  alert("libs/js/common.js");
//        	  console.log(XMLHttpRequest);
              window.location.href = 'welcome';
		  } else if(textStatus=="error"){

          }
    }
});


/*
 * 作为统一验证非空和字符长度
 * 检测是否为空
 * checkVal  检测的值
 * hintName 提示名称
 */
function showNotifyDelay(msg,type,delay){
	showNotify(msg,type);
}
/*
 * 作为统一验证非空和字符长度
 * 检测是否为空
 * checkVal  检测的值
 * hintName 提示名称
 */
function showNotify(msg,type){
	var popupNotification = $("#popupNotification").kendoNotification({
		width:206,
		height:40,
		position: {
            pinned: true,
            top: 30,
            right: 30
        },
        autoHideAfter: 5000,
        stacking: "down"
        	
	}).data("kendoNotification");
	
	popupNotification.show(msg, type);
}

function validataNumber(value){
	
	if(!/^[0-9]*$/.test(value)){
        alert("请输入数字!");
        return false;
    }else{
    	//输入正确
    	if(postParams.level>100 ||postParams.level<0){
    		 alert("请输入0——100的数字!");
		        return false;
    	}
    }
	
	return true;
}

function checkIsNull(checkVal,hintName){
	if(null==checkVal || ""==checkVal || 0==checkVal){
		showNotify(hintName+"不能为空","warning");
		return true;
	}
	return false;
}
function isNull(checkVal,hintName){
	if(!checkVal || null==checkVal || ""==checkVal){
		if(hintName){
			alert(hintName+"不能为空","warning");
		}
		return true;
	}
	return false;
}
function isNotNull(checkVal,hintName){
	return !isNull(checkVal,hintName);
}
function checkContentLength(checkVal,length,hintName){
	
	var warningInfo = hintName+ "输入内容长度不允许超过" + length + "个字符";
	if(checkVal.length > length){
		showNotify(warningInfo,"warning");
		return true;
	}
	return false;
}
$(document).ready(function(){
	$(".dropdown-menu").each(function(index,menu){
		var firstChild = $(menu).children(0);
//		console.log(firstChild.hasClass("divider"));
	});
});