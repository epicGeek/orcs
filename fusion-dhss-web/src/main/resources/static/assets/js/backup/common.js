$.ajaxSetup({
    complete:function(XMLHttpRequest,textStatus){
          if(textStatus=="parsererror"){
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
	Messenger.options = {
		    extraClasses: 'messenger-fixed messenger-on-top messenger-on-right',
		    theme: 'block'
	};
	Messenger().post({
		  message: msg,
		  type: type,
		  hideAfter: delay,
		  hideOnNavigate: true
	});
}
/*
 * 作为统一验证非空和字符长度
 * 检测是否为空
 * checkVal  检测的值
 * hintName 提示名称
 */
function showNotify(msg,type){
	Messenger.options = {
		    extraClasses: 'messenger-fixed messenger-on-top messenger-on-right',
		    theme: 'block'
	};
	Messenger().post({
		  message: msg,
		  type: type,
		  hideAfter: 2,
		  hideOnNavigate: true
	});
}




function checkIsNull(checkVal,hintName){
	if(null==checkVal || ""==checkVal || 0==checkVal){
		showNotify(hintName+"不能为空","warning");
		return true;
	}
	return false;
}
function checkContentLength(checkVal,length,hintName){
	
	var warningInfo = hintName+ "输入内容长度不允许超过" + length + "个字符";
	if(checkVal.length > length){
		showNotify(warningInfo,"warning");
		return true;
	}
	return false;
}