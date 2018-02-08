var units1 = "";
var units2 = "";
kendo.culture("zh-CN");
$(function(){
	var inputSite = $("#inputSite").kendoDropDownList({
	    optionLabel: "请选择...",
	    dataTextField: "locationName",
	    dataValueField: "locationCode",
	    dataSource: {
	        type: "json",
	        serverFiltering: true,
	        transport: {
	            read: "nodeSwitch/getLocationList"
	        }
	    },
	    filter: "contains",
	    change: onChangeSite,
	    suggest: true
	}).data("kendoDropDownList");

	var inputNe = $("#inputNe").kendoDropDownList({
		autoBind: false,
		cascadeFrom: "inputSite",
        optionLabel: "请选择...",
        dataTextField: "neName",
        dataValueField: "neId",
        dataSource: {
            type: "json",
            serverFiltering: true,
            transport: {
                read: "nodeSwitch/neList"
            }
        },
        filter: "contains",
        change: onChangeNe,
        suggest: true
    }).data("kendoDropDownList");
	
	var inputUnitType = $("#inputUnitType").kendoDropDownList({
		autoBind: false,
		cascadeFrom: "inputNe",
        optionLabel: "请选择...",
        dataTextField: "unitTypeName",
        dataValueField: "unitTypeId",
        dataSource: {
            type: "json",
            serverFiltering: true,
            transport: {
                read: "nodeSwitch/unitTypeList"
            }
        },
        filter: "contains",
        change: onChangeUnit,
        suggest: true
    }).data("kendoDropDownList");
	/**场景选择*/
	var inputCase = $("#inputCase").kendoDropDownList({
		autoBind: false,
		cascadeFrom: "inputNe",
        optionLabel: "请选择...",
        dataTextField: "caseName",
        dataValueField: "caseId",
        dataSource: {
            type: "json",
            serverFiltering: true,
            transport: {
                read: "nodeSwitch/caseList"
            }
        },
        filter: "contains",
        change: onChangeCase,
        suggest: true
    }).data("kendoDropDownList");
	/***/
	/**操作选择*/
	var inputAction = $("#inputAction").kendoComboBox({
		dataTextField : "text",
		dataValueField : "value",
		dataSource : [
		              {text : "隔离",value : "1"}, 
		              {text : "恢复",value : "2"}
		              ],
		filter : "contains",
		change: onChangeAction,
		suggest : true
	}).data("kendoComboBox");
	/***/
	function onChangeAction() {
		clearData();//清空执行步骤和返回信息
	}
	/**场景描述*/
	function onChangeCase() {
		clearData();//清空执行步骤和返回信息
		$.ajax({
			url : "nodeSwitch/getDetailed",
			data : {
				caseId : $("#inputCase").data("kendoDropDownList").value()
			},
			datatype : 'json',
			timeout:5000,
			cache:false,
			success : function(data) {	
				$("#detailed").text(data);
			}
		});
	}
	/***/
	function onChangeUnit() {
		$("#unitType").html("");
		$("#unit1").html("");
		$("#unit2").html("");
		clearInputUnit();
		getUnit(inputNe.value(),inputUnitType.value());
		$("#unitType").text($("#inputUnitType").data("kendoDropDownList").text()); 
	};
	
	function getUnit(inputNe,inputUnitType){
		$.ajax({
			url : "nodeSwitch/unitList",
			data : {
				neId : inputNe,
          	  unitType:inputUnitType
			},
			datatype : 'json',
			timeout:5000,
			cache:false,
			success : function(data) {	
				handleUnitData1(data);	
				handleUnitData2(data);
			}
		});
	}
	//返回的节点1数据
    function handleUnitData1(data) { 	
    	clearUnitData1();
		var len = data.length;
		units1 = "[";
		$.each(data,function(index,unit){		
			if (index === len - 1){
				units1+='{ text:"'+unit.unit+'", value:"'+unit.id+'"}'
		     }else{
		    	units1+='{ text:"'+unit.unit+'", value:"'+unit.id+'"},'  
		     } 
		});	  	
	  	units1+="]";
	  
	  $("#inputUnit1").kendoDropDownList({
		  dataTextField: "text",
		  dataValueField: "value",
		  dataSource:  (new Function("return " + units1))(),
		  change: onChangeUnit1,
		  filter: "contains",
		  suggest: true
		});
	  $("#unit1").text($("#inputUnit1").data("kendoDropDownList").text());    
    }
    //返回的节点2数据
    function handleUnitData2(data) { 	
    	clearUnitData2();
		var len = data.length;
		units2 = "[";
		$.each(data,function(index,unit){		
			if (index === len - 1){
				units2+='{ text:"'+unit.unit+'", value:"'+unit.id+'"}'
		     }else{
		    	units2+='{ text:"'+unit.unit+'", value:"'+unit.id+'"},'  
		     } 
		});	  	
	  	units2+="]";
	  
	  $("#inputUnit2").kendoDropDownList({
		  dataTextField: "text",
		  dataValueField: "value",
		  dataSource:  (new Function("return " + units2))(),
		  change: onChangeUnit2,
		  filter: "contains",
		  suggest: true
		});
	  $("#unit2").text($("#inputUnit2").data("kendoDropDownList").text());    
    }
    
    function onChangeUnit1() {
    	clearData();//清空执行步骤和返回信息
    	$("#unit1").text($("#inputUnit1").data("kendoDropDownList").text()); 
    };
    function onChangeUnit2() {
    	clearData();//清空执行步骤和返回信息
    	$("#unit2").text($("#inputUnit2").data("kendoDropDownList").text()); 
    };
    
    function onChangeSite() {
		clearData();//清空执行步骤和返回信息
		$("#ne").html("");
		$("#unitType").html("");
		$("#unit1").html("");
		$("#unit2").html("");
		clearInputUnit();
		$("#site").text($("#inputSite").data("kendoDropDownList").text());
	};
	
	function onChangeNe() {
		clearData();//清空执行步骤和返回信息
		$("#ne").html("");
		$("#unitType").html("");
		$("#unit1").html("");
		$("#unit2").html("");
		clearInputUnit();
		$("#ne").text($("#inputNe").data("kendoDropDownList").text());
	};

	//清空inputNode
	function clearUnitData1(){
		$("#inputNode1").kendoDropDownList({
			  dataTextField: "text",
			  dataValueField: "value",
			  dataSource:  null,
			  filter: "contains",
			  suggest: true
			});
	}  
	//清空inputNode
	function clearUnitData2(){
		$("#inputNode2").kendoDropDownList({
			  dataTextField: "text",
			  dataValueField: "value",
			  dataSource:  null,
			  filter: "contains",
			  suggest: true
			});
	}
	//清空节点名称inputUnit
	function clearInputUnit(){
		$("#inputUnit1").kendoDropDownList({
			  dataTextField: "text",
			  dataValueField: "value",
			  dataSource:  null,
			  filter: "contains",
			  suggest: true
			});
		$("#inputUnit2").kendoDropDownList({
			  dataTextField: "text",
			  dataValueField: "value",
			  dataSource:  null,
			  filter: "contains",
			  suggest: true
			});
	}
	
	//清空
	function clearData(){
		$("#stepId").html("");
		$(".pre-scrollable").text("");
	}
	/**
	 * 添加操作步骤
	 */
	$("#addButton").click(function(){
		if($("#inputSite").data("kendoDropDownList").value()==""){
		  	alert("站点不能为空，请选择！");
		  	return false;
		  }
		 if($("#inputNe").data("kendoDropDownList").value()==""){
			  	alert("网元不能为空，请选择！");
			  	return false;
			  }
		 if($("#inputCase").data("kendoDropDownList").value()==""){
			  	alert("场景不能为空，请选择！");
			  	return false;
			  }
		 if($("#inputAction").data("kendoComboBox").value()==""){
			  	alert("操作不能为空，请选择！");
			  	return false;
			  }
	  //根据网元类型获取板卡倒换执行步骤
		
	  $.ajax({
			url : "nodeSwitch/stepExecute",
			data : {
				siteName:$("#inputSite").data("kendoDropDownList").text(),
				ne_Id : inputNe.value(),
				neName:$("#inputNe").data("kendoDropDownList").text(),
				caseId:inputCase.value(),
				actionVal:inputAction.value()
//					fromUnitName : $("#inputUnit1").data("kendoDropDownList").text(),
//					toUnitName : $("#inputUnit2").data("kendoDropDownList").text(),
//					unitTypeName:$("#inputUnitType").data("kendoDropDownList").text()
			},
			datatype : 'json',
			timeout:5000,
			cache:false,
			success : function(data) {	
				
				var stepHtml = "";
				$.each(data.list,function(index,s){
					
					stepHtml+="<tr>"
						+"<th scope='row'>"+s.step_seq +"</th>"
						+"<td style='display:none'>"+ s.cmd +"</td>"
						+"<td style='display:none'>"+ s.script +"</td>"
						+"<td style='display:none'>"+ s.unit_name +"</td>"
						+"<td>"+ s.step_describes +"</td>"
						+"<td>"+ s.cmd +"</td>"
						+"<td><button id='btnStartBackup' onclick='startBackup(this);' class='btn btn-danger'><i class='glyphicon glyphicon-log-out'></i> 执行倒换</button></td>"
						+"</tr>";             
				});
				$("#stepId").html(stepHtml);
				
				$("#id").val(data.id);
			}
		});		 
	});
	
  //清空
function clearData(){
	$("#stepId").html("");
	$(".pre-scrollable").text("");
}

});
var td;
var command;
var script;
var unitName;
function startBackup(e){
	var tr = $(e).parent().parent(); 
    td = tr.children("th:eq(0)");
    command = tr.children("td:eq(0)");
    script = tr.children("td:eq(1)");
    unitName = tr.children("td:eq(2)");
	$('#myBackupModal').modal('show');
}
function doSwitch(){
//	alert("操作有风险，禁止操作！");
//	$("#myBackupModal").modal("hide");
//	return false;
	/////////
	$.ajax({
		url : "nodeSwitch/switching",
		data : {
//			stepId : td.text(),
			command:command.text(),
			script:script.text(),
			unitName:unitName.text(),
			id:$("#id").val()
		},
		datatype : 'json',
		timeout:1000*500,
		cache:false,
		success : function(data) {
//			$("#stepId").html("");
			
			if(data.error==-1){
				alert("指令执行超时，请重试！");
				$("#myBackupModal").modal("hide");
			}else if(data.error==-2){
				alert("指令执行失败，请重试！");
				$("#myBackupModal").modal("hide");
			}else{
			/*var stepHtml = "";	
			$.each(data.list,function(index,s){
				
				stepHtml+="<tr>"
					+"<th scope='row'>"+s.step_seq +"</th>"
					+"<td style='display:none'>"+ s.step_command +"</td>"
					+"<td style='display:none'>"+ s.script +"</td>"
					+"<td style='display:none'>"+ s.unit_name +"</td>"
					+"<td>"+ s.step_describes +"</td>"
					+"<td>"+ s.cmd +"</td>"
					+ "<td><button id='btnStartBackup' onclick='javascript:startBackup(this);' class='btn btn-danger'><i class='glyphicon glyphicon-log-out'></i> 执行倒换</button></td>"
					+"</tr>";             
			});
			$("#stepId").html(stepHtml);*/
			
			$(".pre-scrollable").text(data.ret);
			alert("指令执行完毕！");
			$("#myBackupModal").modal("hide");
			}
		}
		,
        error:function(errorMessage){
            alert("指令执行错误信息:"+errorMessage);
            $("#myBackupModal").modal("hide");
        }
	});		
}
/**
 * 跳转到网元倒换历史查询
 */
function searchHistory(){
  window.location.href="nodeSwitchHistory";
}
/**
 * 跳转到板卡倒换历史查询
 */
function searchSingleHistory(){
  window.location.href="nodeSingleSwitchHistory";
}
/**tabs**/
$(document).ready(function() {
    $("#tabstrip").kendoTabStrip({
        animation:  {
            open: {
                effects: "fadeIn"
            }
        }
    });
});
/**单板卡倒换****************************************************************/
$(function(){
	/**获取站点*/
	var inputSite2 = $("#inputSite2").kendoDropDownList({
	    optionLabel: "请选择...",
	    dataTextField: "locationName",
	    dataValueField: "locationCode",
	    dataSource: {
	        type: "json",
	        serverFiltering: true,
	        transport: {
	            read: "nodeSwitch/getLocationList"
	        }
	    },
	    filter: "contains",
	    change: onChangeSite2,
	    suggest: true
	}).data("kendoDropDownList");
	
	/**获取网元*/
	var inputNe2 = $("#inputNe2").kendoDropDownList({
		autoBind: false,
		cascadeFrom: "inputSite2",
        optionLabel: "请选择...",
        dataTextField: "neName",
        dataValueField: "neId",
        dataSource: {
            type: "json",
            serverFiltering: true,
            transport: {
                read: "nodeSwitch/neList"
            }
        },
        filter: "contains",
        change: onChangeNe2,
        suggest: true
    }).data("kendoDropDownList");

	/**获取板卡*/
	var inputNode2 = $("#inputNode2").kendoDropDownList({
		autoBind: false,
		cascadeFrom: "inputNe2",
        optionLabel: "请选择...",
        dataTextField: "unitName",
        dataValueField: "unitId",
        dataSource: {
            type: "json",
            serverFiltering: true,
            transport: {
                read: "nodeSwitch/getNodeListByNe"
            }
        },
        filter: "contains",
        change: onChangeUnit2,
        suggest: true
    }).data("kendoDropDownList");
	
	/**操作选择*/
	var inputAction2 = $("#inputAction2").kendoComboBox({
		optionLabel: "请选择...",
		dataTextField : "text",
		dataValueField : "value",
		dataSource : [
		              {text : "隔离",value : "1"}, 
		              {text : "恢复",value : "2"}
		              ],
		filter : "contains",
		change: onChangeAction2,
		suggest : true
	}).data("kendoComboBox");
	
	/***/
	function onChangeSite2() {
		clearData2();//清空执行步骤和返回信息
		$("#ne2").html("");
		$("#unit2").html("");
//		clearInputUnit();
//		$("#action2").html("");
		$("#site2").text($("#inputSite2").data("kendoDropDownList").text());
	};
	/***/
	function onChangeNe2() {
		clearData2();//清空执行步骤和返回信息
		$("#unit2").html("");
		$("#ne2").text($("#inputNe2").data("kendoDropDownList").text());
	};
	/***/
	function onChangeUnit2() {
		clearData2();//清空执行步骤和返回信息
		$("#unit2").text($("#inputNode2").data("kendoDropDownList").text());
	};
	/***/
	function onChangeAction2() {
		clearData2();//清空执行步骤和返回信息
		$("#action2").text($("#inputAction2").data("kendoComboBox").text());
		
	}
	//清空
	function clearData2(){
		$("#stepId2").html("");
		$(".pre-scrollable2").text("");
	}
	/**添加到操作列表*/
	$("#addButton2").click(function(){
		
		 if($("#inputSite2").data("kendoDropDownList").value()==""){
		  	alert("站点不能为空，请选择！");
		  	return false;
		  }
		 if($("#inputNe2").data("kendoDropDownList").value()==""){
			  	alert("网元不能为空，请选择！");
			  	return false;
			  }
		 if($("#inputNode2").data("kendoDropDownList").value()==""){
			  	alert("板卡不能为空，请选择！");
			  	return false;
			  }
		 if($("#inputAction2").data("kendoComboBox").value()==""){
			  	alert("操作不能为空，请选择！");
			  	return false;
			  }

		  $.ajax({
				url : "nodeSwitch/stepNodeExecute",
				data : {
					siteName:$("#inputSite2").data("kendoDropDownList").text(),
//					ne_Id : inputNe2.value(),
					neName:$("#inputNe2").data("kendoDropDownList").text(),
					unitName:$("#inputNode2").data("kendoDropDownList").text(),
					actionVal:inputAction2.value()
				},
				datatype : 'json',
				timeout:5000,
				cache:false,
				success : function(data) {	
					
					var stepHtml = "";
					$.each(data.list,function(index,s){
						
						stepHtml+="<tr>"
							+"<th scope='row'>"+s.step_seq +"</th>"
							+"<td style='display:none'>"+ s.cmd +"</td>"
							+"<td style='display:none'>"+ s.script +"</td>"
							+"<td>"+ s.step_describes +"</td>"
							+"<td>"+ s.cmd +"</td>"
							+"<td><button id='btnStartBackup' onclick='startBackup2(this);' class='btn btn-danger'><i class='glyphicon glyphicon-log-out'></i> 执行倒换</button></td>"
							+"</tr>";             
					});
					$("#stepId2").html(stepHtml);
					
					$("#id2").val(data.id);
				}
			});		 
		});
	
});
/***/
var td2;
var command2;
var script2;
function startBackup2(e){
	var tr = $(e).parent().parent(); 
//    td2 = tr.children("th:eq(0)");
    command2 = tr.children("td:eq(0)");
    script2 = tr.children("td:eq(1)");
	$('#myBackupModal2').modal('show');
}
function doSwitch2(){
//	alert("操作有风险，禁止操作！");
//	$("#myBackupModal").modal("hide");
//	return false;
	$.ajax({
		url : "nodeSwitch/singleSwitching",
		data : {
			command:command2.text(),
			script:script2.text(),
			unitName:$("#inputNode2").data("kendoDropDownList").text(),
			id:$("#id2").val()
		},
		datatype : 'json',
		timeout:1000*500,
		cache:false,
		success : function(data) {
			
			if(data.error==-1){
				alert("指令执行超时，请重试！");
				$("#myBackupModal2").modal("hide");
			}else if(data.error==-2){
				alert("指令执行失败，请重试！");
				$("#myBackupModal2").modal("hide");
			}else{
				$(".pre-scrollable2").text(data.ret);
				alert("指令执行完毕！");
				$("#myBackupModal2").modal("hide");
			}
		}
		,
        error:function(errorMessage){
            alert("指令执行错误信息:"+errorMessage);
            $("#myBackupModa2").modal("hide");
        }
	});		
}