var units ="";
var topOneUnit;
kendo.culture("zh-CN");
var contextPathValue=$("#contextPath").val();

$(function(){
    //设置备份完成div为不显示
    $("#backFinished").css("display","none");
    //设置进度条为不显示
    $("#progressbar").css("display","none");
//        var inputSite = $("#inputSite").kendoDropDownList({
//        	optionLabel: "请选择...",
//            dataTextField: "siteName",
//            dataValueField: "siteCode",
//            dataSource: {
//                type: "json",
//                serverFiltering: true,
//                transport: {
//                    read: "backup/siteList"
//                }
//            },
//            filter: "contains",
//            suggest: true ,
//            change: function(e) {
//                var value = this.value();
//                var te = this.text();
//                $("#siteSelected").html(te);
//                // Use the value of the widget
//            }
//        });
    /*$.getJSON(contextPathValue+"/backup/getSiteList",function(data){
    });*/
    /*$.ajax({
        url: contextPathValue+"/backup/getAllSite",
        async:false,
        success:function(data){
            var json= eval(data);
            $("#inputSite").kendoDropDownList({
                dataTextField: "siteName",
                dataValueField: "siteId",
                dataSource:json,
                filter: "contains",
                suggest: true
            });
        }
    });*/
    /*$("#inputSite").kendoDropDownList({
        dataTextField: "siteName",
        dataValueField: "siteId",
        dataSource:{
            autoSync: true,
            transport: {
                read: {
                    dataType: "json",
                    cache: true,
                    type: "POST",
                    url: contextPathValue+"/backup/getAllSite",
                    data: {
                        //q: $("#search").val() // send the value of the #search input to the remote service
                    }
                }
            }
        },
        filter: "contains",
        suggest: true
    });*/
//        var inputType =$("#inputType").kendoDropDownList({
//        autoBind: false,
//        cascadeFrom: "inputSite",
//        optionLabel: "请选择...",
//        dataTextField: "neTypeName",
//        dataValueField: "neTypeCode",
//        dataSource: {
//            type: "json",
//            serverFiltering: true,
//            transport: {
//                read: "backup/neTypeList"
//            }
//        },
//        filter: "contains",
//        suggest: true,
//        change: function(e) {
//            var value = this.value();
//            var te = this.text();
//            $("#siteTypeSelect").html(te);    
//            $("#nodeNameSelect").html("");
//            getUnit($("#inputSite").data("kendoDropDownList").text(),value);
//        }
//    });
    /*$("#inputNode").kendoDropDownList({
        autoBind: false,
        cascadeFrom: "paramess",
        optionLabel: "请选择...",
        dataTextField: "unitName",
        dataValueField: "unitCode",
        dataSource: {
            type: "json",
            serverFiltering: true,
            transport: {
                read: "backup/unitList"
            }
        },
        filter: "contains",
        suggest: true  ,
        change: function(e) {
            var value = this.value();
            var te = this.text();
            $("#nodeNameSelect").html(te);
            // Use the value of the widget
        }
    });*/
      //添加执行步骤到操作列表
//    	$("#addButton").click(function(){
//    		if($("#inputSite").data("kendoDropDownList").text()=="请选择..."){
//    		 	alert("请选择站点！ ")
//    		  return false;
//    		}
//    		if($("#inputType").data("kendoDropDownList").text()=="请选择..."){
//    		 	alert("请选择类型！ ")
//    		  return false;
//    		}
//    		if($("#inputNode").data("kendoDropDownList").text()=="请选择..."){
//    		 	alert("请选择节点名称！ ")
//    		  return false;
//    		}
//    		
//    		$.ajax({
//				url : "backup/getStepByType",
//				data : {
//					   site : $("#inputSite").data("kendoDropDownList").text(),
//					   unit_type : $("#inputType").data("kendoDropDownList").value(),
//					   unit : $("#inputNode").data("kendoDropDownList").text()
//				},
//				datatype : 'json',
//				timeout:5000,
//				cache:false,
//				success : function(data) {	
//					var stepHtml = "";
//					
//					$.each(data,function(index,s){						
//						stepHtml+="<tr>"
//							+"<th scope='row'>"+s.step_seq +"</th>"
//							+"<td style='display:none'>"+ s.step_command +"</td>"
//							+"<td>"+ s.step_explain +"</td>"
//							+"<td>"+ s.step_describe +"</td>"
//							+"<td><button id='btnStartBackup' onclick='javascript:doBeifen(this);' class='btn btn-danger'><i class='glyphicon glyphicon-log-out'></i> 执行备份</button></td>"
//							+"</tr>";             
//					});
//
//					$("#stepId").html(stepHtml);
//				}
//			});
//    	});
});

//function getUnit(inputSite,inputType){
//	$.ajax({
//		url : "backup/unitList",
//		data : {
//			site : inputSite,
//			unit_type : inputType
//		},
//		filter: "contains",
//		datatype : 'json',
//		timeout:3000,
//		cache:false,
//		success : function(data) {	
//			handleUnitData1(data);	
//		}
//	});
//}
//function handleUnitData1(data) {
//	clearUnitData1();
//	var len = data.length;
//	units = "[";
//	$.each(data,function(index,unit){		
//		if (index === len - 1){
//			units+='{ text:"'+unit.unitName+'", value:"'+unit.unitCode+'"}'
//	     }else{
//	    	units+='{ text:"'+unit.unitName+'", value:"'+unit.unitCode+'"},'  
//	     } 
//	});	  	
//  	units+="]";
//  
//  $("#inputNode").kendoDropDownList({
//	  optionLabel: "请选择...",
//	  dataTextField: "text",
//	  dataValueField: "value",
//	  dataSource:  (new Function("return " + units))(),
//	  filter: "contains",
//	  suggest: true,
//	  change: function(e) {
//          var value = this.value();
//          var te = this.text();
//          $("#nodeNameSelect").html(te);
//      }
//	});
////  $("#nodeNameSelect").text($("#inputNode").data("kendoDropDownList").text());    
//}
//清空inputNode
//function clearUnitData1(){
//	$("#inputNode").kendoDropDownList({
//		optionLabel: "请选择...",
//		  dataTextField: "text",
//		  dataValueField: "value",
//		  dataSource:  null,
//		  filter: "contains",
//		  suggest: true
//		});
//}
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
        		alert(" 用户:"+data.backUpMinitorList[0].system_user+" 正在执行。请对应监控列表取消选中");
        	}
        },
        error:function(errorMessage){
            alert("网络异常,稍后重试ssssssssssssssss");
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
	
	var tr = $(tmp).parent().parent(); 
	td = tr.children("th:eq(0)");
	for(var i=0;i<topOneUnit.length;i++){
		
		$.ajax({
	//		url : "backup/executeBackup",
			url : "backup/oneKeyCmdBackUp",
			data : {
	//			site :topOneUnit[0] ,
	//			unit_type:topOneUnit[1],
	//			unit_name:topOneUnit[2],
				backUpUnits:topOneUnit.toString(),
				stepId :tr.children("th:eq(0)").text(),
				command:tr.children("td:eq(0)").text(),
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
	}
}
//}
///***
// * 备份按钮触发事件
// * 1.置backup按钮为不可用
// * 2.修改进度条
// * 3.请求服务器进行backup
// * ****/
//function doBackUp(){
////	alert($("#inputSite").data("kendoDropDownList").text());
////	alert($("#inputType").data("kendoDropDownList").text());
////	alert($("#inputNode").data("kendoDropDownList").text());
//	$("#myBackupModal").modal("hide");
//	$("#div_backuping").modal('show');
//	$('#div_backuping').find("div").eq(0).unbind("click");
//	
//	var tr = $(tmp).parent().parent(); 
////	td = tr.children("th:eq(0)");
//	$.ajax({
//		//		url : "backup/executeBackup",
//		url : "backup/oneKeyCmdBackUp",
//		data : {
//			//			site :topOneUnit[0] ,
//			//			unit_type:topOneUnit[1],
//			//			unit_name:topOneUnit[2],
//			backUpUnits:topOneUnit.toString(),
//			stepId :tr.children("th:eq(0)").text(),
//			command:tr.children("td:eq(0)").text()
//		},
//		type:"POST",
//		datatype : 'json',
//		timeout:1000*60*60*24*7,
//		cache:false,
//		success : function(data) {
//			reflush();
//			if (data.unitList.length<1) {
//				$("#div_backuping").modal('hide');
//				
//			}else{
//				jQuery.each(data.unitList,function(i,obj){
//					topOneUnit=[];
//					topOneUnit.push(obj);
//				});
//				doBackUp();
//			}
//		},
//		error:function(errorMessage){
//			alert("执行备份指令!,错误信息:"+errorMessage);
//			$("#div_backuping").modal('hide');
//		}
//	});
//}
//function doBackUpOld(){
//	
//	alert("操作有风险，禁止操作！");
//	$("#myBackupModal").modal("hide");
//	return false;
//  //设置开始备份按钮为不可用
//  var interval=null;
//  //  $("#btnStartBackup").attr("disabled","disabled");
//    $("#myBackupModal").modal("hide");
//    $("#btnStartBackup").css("display","none");
//    $("#progressbar").css("display","block");
//    var processId=gernateProcessId();
//    var dropdownlistSite = $("#inputSite").data("kendoDropDownList");
//    var dropdownlistType = $("#inputType").data("kendoDropDownList");
//    var dropdownlistNode = $("#inputNode").data("kendoDropDownList");
//    // get the dataItem corresponding to the selectedIndex.
//    var dataItemSite = dropdownlistSite.dataItem();
//    var dataItemType = dropdownlistType.dataItem();
//    var dataItemNode = dropdownlistNode.dataItem();
//
//    var siteId=dataItemSite.optValue;
//    var siteName=dataItemSite.Name;
//
//    var siteTypeId=dataItemType.value;
//    var siteTypeName=dataItemType.text;
//    var nodeId=dataItemNode.value;
//    var nodeName=dataItemNode.text;
//    $.ajax({
//        url: "backup/doBackupAction",
//        async:true,
//        data:{processId:processId,siteId:siteId,siteName:siteName,siteTypeId:siteTypeId,siteTypeName:siteTypeName,nodeId:nodeId,nodeName:nodeName},
//        type:"POST",
//        success:function(data) {
//            if(data=="-2"){
////                alert("未找执行备份的站点服务器配置信息!");
//                alert("执行完成!");
//                 $('#btnStartBackup').attr("disabled","disabled");
//            }else if(data =="-3"){
//                alert("执行备份命令失败,请联系管理员查看日志!");
//            }else if(data=="-1"){
//                alert("执行备份命令失败!");
//            }
//            if(interval!=null){
//                clearInterval(interval);
//                $("#btnStartBackup").css("display","block");
//                $("#progressbar").css("display","none");
//            }
//        },
//        error:function(errorMessage){
//            alert("执行备份出错!,错误信息:"+errorMessage);
//        }
//    });
//    //调用定时器来检查处理的进度
//   interval = setInterval(function () {
//        checkProcessPercent(interval,processId);
//    }, 500);
//}
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
  window.location.href="oneKeyBackupHistory";
}




//function gernateProcessId(){
//    // http://www.ietf.org/rfc/rfc4122.txt
//    var s = [];
//    var hexDigits = "0123456789abcdef";
//    for (var i = 0; i < 36; i++) {
//        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
//    }
//    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
//    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
//    s[8] = s[13] = s[18] = s[23] = "-";
//
//    var uuid = s.join("");
//    return uuid;
//}