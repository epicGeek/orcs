var scoreData = [{ text: "日", value: "2" },{ text: "周", value: "3" },{ text: "月", value: "4" }];
var tables = {1:"area_score_hour",2: "area_score_day",3:"area_score_week",4:"area_score_month"};
//var parameters;
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

function showNotify(msg,type){
	var popupNotification = $("#popupNotification").kendoNotification({
		width:230,
		height:50,
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
//判断数字及输入数字大小
function validataNumber(value,nameValue){
	
	if(!/^[0-9]*$/.test(value)){
      //  alert("请输入数字!");
        showNotify(nameValue+"——请输入数字","warning");
        return false;
    }else{
    	//输入正确
    	if(value>100 || value<0){
    		// alert("请输入0——100的数字!");
    		 showNotify(nameValue+"——请输入0——100的数字","warning");
		        return false;
    	}
    }
	return true;
}

//判断数字
function validata(value,nameValue){
	
	if(!/^[0-9]*$/.test(value)){
      //  alert("请输入数字!");
        showNotify(nameValue+"——请输入数字","warning");
        return false;
    }else{
    	if(value.length>10){
    		showNotify(nameValue+"——输入长度不能超过10个字符","warning");
    		return false;
    	}
    }
	return true;
}

//判断非空和必须是数字
function numberAndIsNull(value,nameValue){
	
	if(!value || null==value || ""==value){
		 showNotify(nameValue+"不能为空","warning");
	        return false;
	}else{
    	//输入正确
		if(!/^[0-9]*$/.test(value)){
			//  alert("请输入数字!");
		    showNotify(nameValue+"——请输入数字","warning");
		    return false;
		 }
    	
    }
	return true;
}
		
//判断非空和必须是数字或者小数
function DecimalAndNumber(value,nameValue){
	
	if(!value || null==value || ""==value){
		 showNotify(nameValue+"不能为空","warning");
	        return false;
	}else{
    	//输入正确
		if(!/^[0-9]*$/.test(value) || !/^\d+(\.\d+)?$/.test(value)){
		    showNotify(nameValue+"——请输入数字或者小数","warning");
		    return false;
		 }
    	
    }
	return true;
}

function checkIsNull(checkVal,hintName){
	if(null==checkVal || ""==checkVal){
		showNotify(hintName+"不能为空","warning");
		return true;
	}
	return false;
}

function isNull(checkVal,hintName){
	if(!checkVal || null==checkVal || ""==checkVal){
		showNotify(hintName+"不能为空","warning");
		return true;
	}
	return false;
}

function isNotNull(checkVal,hintName){
	return !isNull(checkVal,hintName);
}

function checkContentLength(checkVal,length,hintName){
	
	var warningInfo = hintName+ "长度不允许超过" + length + "个字符";
	if(checkVal.length <= length){
		return true;
	}
	showNotify(warningInfo,"warning");
	return false;
}

//判断非空和必须是数字和数字长度
function numberAndLength(value,nameValue){
	
    	//输入正确
		if(!/^[0-9]*$/.test(value)){
			//  alert("请输入数字!");
		    showNotify(nameValue+"——请输入11位数字","warning");
		    return false;
		    
		 }else{
			 
			 if(value.length != 11){
				 showNotify(nameValue+"——请输入11位数字","warning");
				 return false;
			 }
		 }
    	
	return true;
}
//判断邮箱格式
function CheckMail(mail,nameValue) {
	 var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	 if (!filter.test(mail)){
		 
		 showNotify(nameValue+"——格式不正确","warning");
		return false; 
	 }
	return true;
		 
}

//时间格式化
function formatDate(type,parameters,init){
	//parameters = parameters;
	//console.log(JSON.stringify(parameters));
	$("#startDate").css("width","200px");
	$("#endDate").css("width","200px");
	var today = new Date();
	var format = [];
	if(type==1){
		format[0]= "yyyy-MM-dd HH:00:00";
		format[1]=(parameters.startDate!=null && parameters.startDate !="")?parameters.startDate:new Date(today.getFullYear(), today.getMonth(),today.getDate(),today.getHours()-23).Format("yyyy-MM-dd hh:mm:ss");
		format[2]=(parameters.endDate!=null && parameters.endDate !="")?parameters.endDate:today;
	}else if(type==2){
		format[0]= "yyyy-MM-dd";
		format[1]=(parameters.startDate!=null && parameters.startDate !="")?parameters.startDate:new Date(today.getFullYear(), today.getMonth(),today.getDate()-1).Format("yyyy-MM-dd");
		format[2]=(parameters.endDate!=null && parameters.endDate !="")?parameters.endDate:today;
	}else if(type==3){
		if(parameters.startDate){
			if(parameters.startDate.length>10){
				parameters.startDate = "";
				parameters.endDate  = "";
			}
		}
		var week = getYearWeek(today)-1;
		format[0]= "yyyy-"+week;
		format[1]=(parameters.startDate!=null && parameters.startDate !="")?parameters.startDate:new Date(today).Format("yyyy-"+week);
		format[2]=(parameters.endDate!=null && parameters.endDate !="")?parameters.endDate:new Date(today).Format("yyyy-"+week);
	}else if(type==4){
		format[0]= "yyyy-MM";
		format[1]=(parameters.startDate!=null && parameters.startDate !="")?parameters.startDate:new Date(today.getFullYear(), today.getMonth()-1).Format("yyyy-MM");
		format[2]=(parameters.endDate!=null && parameters.endDate !="")?parameters.endDate:today;
	}
	//查询条件
	var startDate = $("#startDate").data("dateTimePicker");
	var endDate = $("#endDate").data("dateTimePicker");
	if(!init){
		$("#startDate").kendoDateTimePicker({
			format: format[0],value:format[1]
		});
		$("#endDate").kendoDateTimePicker({
			format: format[0],value: format[2]
		});
	} else {
		startDate.setOptions({
			format: format[0],
			value: format[1]
		});
		endDate.setOptions({
			format: format[0],
			value:format[2]
		});
	}
}

//统计方式
function initScoreType(data,index){
	//下拉
	$("#scoreType").kendoDropDownList({
		 	//autoBind: false,
			dataSource:  data,
			dataTextField: "text",
	        dataValueField: "value",
	        //index:0, // 当前默认选中项，索引从0开始。
	        change: function(e){
	        	 var value = this.value();
	        	formatDate(value,false);
	        	//isHide(value);
       
            }
		});
	$("#scoreType").data("kendoDropDownList").value(index);
	
}

function isHide(type){
	if(type==1){
		$("#btsList").data("kendoGrid").showColumn("alarm_score");
	}else{
		$("#btsList").data("kendoGrid").hideColumn("alarm_score");
	}
}

/*
 * 接收url参数
 *  调用方法：
 *	alert(GetQueryString("参数名1"));
 */
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]); return null;
} 

/**
 * 时间格式化
 * @author Administrator
 *
 */
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

/**
 * 获取当前时间的上周
 * @param date
 * @returns
 */
function getYearWeek(date){
    var date2=new Date(date.getFullYear(), 0, 1); 
    var day1=date.getDay(); 
    if(day1==0) day1=7; 
    var day2=date2.getDay(); 
    if(day2==0) day2=7; 
    d = Math.round((date.getTime() - date2.getTime()+(day2-day1)*(24*60*60*1000)) / 86400000);   
    return Math.ceil(d /7);  
} 

$(document).ready(function(){
	$(".dropdown-menu").each(function(index,menu){
		var firstChild = $(menu).children(0);
	});
});