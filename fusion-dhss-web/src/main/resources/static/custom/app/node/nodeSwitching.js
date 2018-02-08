kendo.culture("zh-CN");

var flag=true;
var allNe = [];
var allLocation = [];
var allUnit = [];
var gridDataSource;
var staticUnitId;
var staticId;
function getLocationList(){
	$("#locationInput").kendoDropDownList({
	    optionLabel: "请选择站点",
	    dataSource: allLocation,
	    filter: "contains",
	    change: function(){
	    	getNeList();
	    	getUnitList();
	    	gridDataSourceFilter();
	    },
	    suggest: false
	}).data("kendoDropDownList");
}
var flag = false;
function getNeList(){
	var text = $('#locationInput').data("kendoDropDownList").text();
	var array = [];
	var titleName ="请选择网元";
	if(text == "请选择站点" || text == ""){
		array = allNe;
	}else{
		text = $("#locationInput").val();
		$.each(allNe,function(index,item){
			if(item.location == text ){
				array[array.length] = item;
			}
		})
		titleName = array[0].neName;
	}
	 $("#neNameInput").kendoDropDownList({
	    optionLabel: "请选择网元",
	    dataTextField: "neName",
	    dataValueField: "neName",
	    dataSource: array,
	    filter: "contains",
	    change: function(){
	    	getUnitList();
	    	gridDataSourceFilter();
	    }
	}).data("kendoDropDownList");
	 if(flag == false){
		 getUnitList();
		 flag = true;
	 }
}
function getUnitList(){
	var text = $('#neNameInput').data("kendoDropDownList").text();
	var array = [];
	if(text == "请选择网元" || text == "" ){
		array = allUnit;
	}else{
		text = $("#neNameInput").val();
		$.each(allUnit,function(index,item){
			if(item.neName == text ){
				array[array.length] = item;
			}
		})
	}
	$("#unitInput").kendoDropDownList({
	    optionLabel: "请选择单元",
	    dataTextField: "unitName",
	    dataValueField: "unitName",
	    dataSource: array,
	    filter: "contains",
	    change: function(){
	    	gridDataSourceFilter();
	    },
	    suggest: false
	}).data("kendoDropDownList");
//	$('#unitInput').data("kendoDropDownList").text(title);
}
function  gridDataSourceFilter(){
	var filters = [];

	if($('#neNameInput').data("kendoDropDownList").text() != "请选择网元" && $('#neNameInput').data("kendoDropDownList").text() != "")
    	filters.push({field: "neName", operator: "eq", value: $('#neNameInput').val() });
	if($('#unitInput').data("kendoDropDownList").text() != "请选择单元" && $('#unitInput').data("kendoDropDownList").text() != ""){
		filters.push({field: "unitName", operator: "eq", value: $('#unitInput').val() });
	}

	gridDataSource.filter(filters);
	gridDataSource.fetch();
}
var stepGrid;
function chk(unitHref){
	if($("#actionInput").data("kendoDropDownList").text() == "请选择操作"){
		infoTip({content: "请选择操作",color:"red"});
		return false;
	}else{
		$.ajax({
			dataType : 'json',
			type : "GET",
			url : unitHref,
			success : function(unit) {
				staticUnitId = unit;
				$.ajax({
					dataType : 'json',
					type : "GET",
					url : unit._links.ne.href,
					success : function(ne) {
							var action = $("#actionInput").data("kendoDropDownList").text();
							$.ajax({
								url : "nodeSwitch/stepNodeExecute",
								data : {
									siteName:ne.location,
									neName:ne.neName,
									unitName:unit.unitName,
									neType:unit.neType,
									actionVal:action
								},
								datatype : 'json',
								timeout:5000,
								cache:false,
								success : function(data) {	
									initStepAndResult(data);
								}
							});
					}
				});
			}
		});
	}
	

	
}

var finalFlag = false;

var tr;
function startBackup2(e){
	if(!finalFlag){
		tr = stepGrid.dataItem($(e).closest("tr"));
		$('#myBackupModal2').modal('show');
	}else{
		infoTip({content: "请等待上一条指令执行成功以后再执行！",color:"red"});
	}
}
function doSwitch2(){
	
	$.ajax({
		dataType : 'json',
		type : "GET",
		data : {
			id:staticId,
			desc:tr.stepDesc
		},
		url : "nodeSwitch/updateSingleSwitching",
		success : function(datas) {
			finalFlag = true;
			initStepAndResult(datas);
			infoTip({content: "指令下发成功！"});
			$("#myBackupModal2").modal("hide");
			$.ajax({
				url : "nodeSwitch/singleSwitching",
				data : {
					command:tr.stepCmd,
					script:tr.script,
					unitName:staticUnitId.unitName,
					id:staticId,
					desc:tr.stepDesc
				},
				datatype : 'json',
				timeout:1000*500,
				cache:false,
				success : function(data) {
					if(data.error==-1){
						infoTip({content: "指令执行超时，请重试！",color:"red"});　 
					}else if(data.error==-2){
						infoTip({content: "指令执行失败，请重试！",color:"red"});
					}else{
						initStepAndResult(data);
					}
				},error:function(errorMessage){
					infoTip({content: "指令执行错误信息:"+errorMessage,color:"red"});
		        }
			});		
		}
	});
	
	
	
}


function initStepAndResult(data){
	finalFlag = false;
	stepGrid.dataSource.data([]);
	staticId = data.id;
	stepGrid.dataSource.data(data.list);
	if(data.result == ""){
		resultGrid.dataSource.data([]);
	}else{
		var results = [];
		var array = data.result.split(";");
		$.each(array,function(index,item){
			if(item!=""){
				var rlt = item.split(",");
				var flag = "0";
				if(rlt[1] == ""){
					flag = "1";
					finalFlag = true;
				}
				results[results.length] = { desc : rlt[0] , url : rlt[1] , flag : flag };
			}
		})
		resultGrid.dataSource.data(results);
	}
}

function downloadLog(url,desc){
	location.href = "nodeSwitch/downloadLog?url="+url+"&desc="+desc;
}


function clearInfo(){
	$.ajax({
		url : "nodeSwitch/updateReturnInfo",
		data : {
			id:staticId
		},
		datatype : 'json',
		timeout:5000,
		cache:false,
		success : function(data) {	
			resultGrid.dataSource.data([]);
		}
	});
}

var resultGrid;

$(function(){
	
	stepGrid = $("#stepGrid").kendoGrid({
		dataSource : [],
		height : $(window).height() - $("#stepGrid").offset().top - 120,
		reorderable : true,
		resizable : true,
		sortable : true,
		pageable : false,
		columns: [{
			field: "stepSep",
			title: "<span  title='执行顺序'>执行顺序</span>"
		}, {
			field: "stepDesc",
			title: "<span  title='步骤描述'>步骤描述</span>"
		}, {
			field: "stepCmd",
			title: "<span  title='操作指令'>操作指令</span>"
		}, {
			template: "<button name='btnStartBackup' onclick='startBackup2(this);' class='btn btn-danger btn-xs'><i class='glyphicon glyphicon-log-out'></i> 执行倒换</button>",
			title: "<span  title='操作'>操作</span>"
		}]
	}).data("kendoGrid");
	
	resultGrid = $("#resultGrid").kendoGrid({
		dataSource : [],
		height : $(window).height() - $("#resultGrid").offset().top - 120,
		reorderable : true,
		resizable : true,
		sortable : true,
		pageable : false,
		columns: [{
			field: "desc",
			title: "<span  title='描述'>描述</span>"
		},{
			template: "#if(flag == '1'){#"+
			  "<div class='progress'><div class='progress-bar progress-bar-warning progress-bar-striped active' style='width: 70%;'>执行中……</div></div>"+
			  "#} else {#"+
			  "完成，<a class='btn btn-link'  onclick=\"downloadLog(\'#:url#\',\'#:desc#\')\"><i class='glyphicon glyphicon-download-alt'></i>下载</a>"+
			  "#}#",
			field: "flag",
			title: "<span  title='查看日志'>查看日志</span>"
		}]
	}).data("kendoGrid");
	
	$.ajax({
		dataType : 'json',
		type : "GET",
		url : "rest/equipment-ne",
		success : function(data) {
			allNe = data._embedded["equipment-ne"];
			$.each(allNe,function(index,item){
				if($.inArray(item.location,allLocation) == -1){
					allLocation[allLocation.length] = item.location;
				}
			})
			getLocationList();
			getNeList();
		}
	});
	
	
	var inputSite = $("#actionInput").kendoDropDownList({
		optionLabel: "请选择操作",
		dataTextField: "text",
	    dataValueField: "value",
	    dataSource: [{text : "隔离" , value : "1" },{text : "恢复" , value : "2" }],
	    filter: "contains",
	    suggest: false
	}).data("kendoDropDownList");
	
	
	
	gridDataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: "rest/equipment-unit",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            },
        },
        schema: {
            data: function (data) {
            	if(data._embedded){
            		allUnit = data._embedded["equipment-unit"];
        			return allUnit;
            	}
            	return [];
            },
            total: function(data) {
            	if(data._embedded){
        			return data._embedded["equipment-unit"].length;
            	}
            	return 0;
            }
        },
        serverPaging : false,
		serverFiltering : false,
		serverSorting : false,
	});
	
	var unitGrid = $("#unitGrid").kendoGrid({
		dataSource : gridDataSource,
		height : $(window).height() - $("#unitGrid").offset().top - 120,
		reorderable : true,
		resizable : true,
		sortable : true,
		pageable : false,
		columns: [/*{
			width: 30,
			template: "<input type='radio' name = 'chk' onClick = 'chk(\"#:_links.self.href#\")' id='#:_links.self.href#' />",
			attributes:{"class": "text-center"},
//			title: "<input  class='grid_radio'/>"
		},*/ {
			field: "neName",
			title: "<span  title='网元'>网元</span>"
		}, {
			field: "unitName",
			title: "<span  title='单元'>单元</span>"
		}, {
			field: "serverIp",
			title: "<span  title='地址'>地址</span>"
		}, {
			field: "unitType",
			title: "<span  title='单元类型'>单元类型</span>"
		}, {
			template: "<button onClick = 'chk(\"#:_links.self.href#\")' class='editBtn btn btn-xs btn-default'><i class='glyphicon glyphicon-edit'></i>添加到列表</button>",
			title: "<span  title='操作'>操作</span>"
		}],
		dataBound: function(){
			//全选按钮
			/*$("[ 'name' = 'chk' ]").on("click",function(){
				alert("a");
//				$("#unitGrid .k-grid-content input[type='checkbox']").prop("checked",$(this).prop("checked"));
			});*/
		}
	}).data("kendoGrid");
	
	var itemType = $("#type").val();
	var itemTitle = "单元倒换";
	if(itemType!=""){
		if(itemType == "1"){
			itemType = "隔离";
			itemTitle = "单元隔离";
		}
		if(itemType == "2"){
			itemType = "恢复";
			itemTitle = "单元恢复";
		}
		$("#itemTitle").html(itemTitle);
		$("#actionInput").data("kendoDropDownList").text(itemType);
	}
	
	
	
	var itemunitId = $("#itemunitId").val();
	if(itemunitId != ""){
		$.ajax({
			dataType : 'json',
			type : "GET",
			url : "rest/equipment-unit/"+itemunitId,
			success : function(unit) {
				staticUnitId = unit;
				$.ajax({
					dataType : 'json',
					type : "GET",
					url : unit._links.ne.href,
					success : function(ne) {
							var action = $("#actionInput").data("kendoDropDownList").text();
							$.ajax({
								url : "nodeSwitch/stepNodeExecute",
								data : {
									siteName:ne.location,
									neName:ne.neName,
									unitName:unit.unitName,
									neType:unit.neType,
									actionVal:action
								},
								datatype : 'json',
								timeout:5000,
								cache:false,
								success : function(data) {	
									initStepAndResult(data);
								}
							});
					}
				});
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
	$.ajax({
		url : "nodeSwitch/switching",
		data : {
			command:command.text(),
			script:script.text(),
			unitName:unitName.text(),
			id:$("#id").val()
		},
		datatype : 'json',
		timeout:1000*500,
		cache:false,
		success : function(data) {
			
			if(data.error==-1){
				alert("指令执行超时，请重试！");
				$("#myBackupModal").modal("hide");
			}else if(data.error==-2){
				alert("指令执行失败，请重试！");
				$("#myBackupModal").modal("hide");
			}else{
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
	/*var inputSite2 = $("#inputSite2").kendoDropDownList({
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
	}).data("kendoDropDownList");*/
	
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
		
		 		 
		});
	
});
/***/
