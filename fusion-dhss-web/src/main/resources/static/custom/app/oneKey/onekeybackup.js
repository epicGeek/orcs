var units ="";
var topOneUnit;
kendo.culture("zh-CN");
var contextPathValue=$("#contextPath").val();

$(function(){
    //设置备份完成div为不显示
    $("#backFinished").css("display","none");
    //设置进度条为不显示
    $("#progressbar").css("display","none");
    
    $("#test").height($(window).height() - $("#test").offset().top - 127);
});

var tmp;
function doBeifen(e){
    $.ajax({
        url:"backup/getExecutingMinitor",
        type:"POST",
        data : {
			backUpUnits:topOneUnit.toString(),
        },
		datatype : 'json',
		cache:false,
        success:function(data) {
        	if(data.backUpMinitorList.length==0){
        		$('#myBackupModal').modal('show');
        		tmp = e;
        		$('#myBackupModal').find("div").eq(0).unbind("click");
        	}else{
        		reflush();
        		alert(" 用户:"+data.backUpMinitorList[0].systemUser+" 正在执行。请对应监控列表取消选中");
        	}
        },
        error:function(errorMessage){
            alert("网络异常,稍后重试");
        }
    });
	
	

}
/***
 * 备份按钮触发事件
 * 1.置backup按钮为不可用
 * 2.修改进度条
 * 3.请求服务器进行backup
 * ****/
function doBackUp(){
//	alert($("#inputSite").data("kendoDropDownList").text());
//	alert($("#inputType").data("kendoDropDownList").text());
//	alert($("#inputNode").data("kendoDropDownList").text());
	$("#myBackupModal").modal("hide");
	$("#div_backuping").modal('show');
	$('#div_backuping').find("div").eq(0).unbind("click");
	
	var tr = stepGrid.dataItem($(tmp).closest("tr"));
	if(tr == null){
		tr = { step_seq : "" , step_command : ""};
	}
	/*td = tr.children("th:eq(0)");*/
	//for(var i=0;i<topOneUnit.length;i++){
		
		$.ajax({
	//		url : "backup/executeBackup",
			url : "backup/oneKeyCmdBackUp",
			data : {
	//			site :topOneUnit[0] ,
	//			unit_type:topOneUnit[1],
	//			unit_name:topOneUnit[2],
				backUpUnits:topOneUnit.toString(),
				stepId :tr.step_seq/*tr.children("th:eq(0)").text()*/,
				command:tr.step_command,
				isLocalhost:jQuery("#isLocalhost")[0].checked
			},
			anync:false,
			type:"POST",
			datatype : 'json',
			timeout:1000*60*60*24*7,
			cache:false,
			success : function(data) {
				reflush();
				$("#div_backuping").modal('hide');
			},
	        error:function(errorMessage){
	            alert("执行备份指令!,错误信息:"+errorMessage);
	            $("#div_backuping").modal('hide');
	        }
		});
	//}
}
/****
 * 查看服务器完成进度
 * ****/
function checkProcessPercent(interval,processId){
    $.ajax({
        url:"backup/checkBackupProcessPercent",
        async:false,
        data:{processId:processId} ,
        success:function(data) {
           var finishedPercent=parseFloat(9);
            if (finishedPercent < 10) {
                $("#progressBarDiv").css("width",100+"%")
            } else {
                clearInterval(interval);
                $("#btnStartBackup").css("display","block");
                $("#progressbar").css("display","none");
            }
        },
        error:function(errorMessage){
            clearInterval(interval);
            alert("执行备份出错!");
        }
    });

}
/***
 * 跳转到备份历史查询
 * ***/
function searchBackupHistory(){
  window.location.href="oneKeyBackup?show=show";
}

