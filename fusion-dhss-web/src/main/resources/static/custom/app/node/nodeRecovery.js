


var neList = [];
var unitList = [];
var unitTypeList = [];

var unitGrid;
var stepGrid;
function  gridUnitGrid(){
	var neTypetext = $('#inputNeTypeTrigger').data("kendoDropDownList").text();
	var netext = $('#inputNeTrigger').data("kendoDropDownList").text();
	var unitTypetext = $('#inputUnitTypeTrigger').data("kendoDropDownList").text();
	var unittext = $('#inputKeyWord').data("kendoDropDownList").text();
	console.log(neTypetext);
	console.log(netext);
	console.log(unitTypetext);
	console.log(unittext);
	var filters = [];

	if(neTypetext != "请选择网元类型" && neTypetext != ""){
		filters.push({field: "neType", operator: "eq", value: neTypetext });
	}
	if(netext != "请选择网元" && netext != ""){
		filters.push({field: "neName", operator: "eq", value: netext });
	}
	if(unitTypetext != "请选择单元类型" && unitTypetext != ""){
		filters.push({field: "unitType", operator: "eq", value: unitTypetext });
	}
	if(unittext != "请选择单元" && unittext != ""){
		filters.push({field: "unitName", operator: "eq", value: unittext });
	}
	unitGrid.dataSource.filter(filters);
	unitGrid.dataSource.fetch();
}


var tr;
$(function(){
	
	var webSite = $("#webSite").val();
	var unitId = $("#unitId").val();
	var neTypeId = $("#neTypeId").val();
	
	
	
	
	
	
	
	
	
	$.ajax({
		url : "queryNeType",
		datatype : 'json',
		success : function(data) {	
			$("#inputNeTypeTrigger").kendoDropDownList({         
				  optionLabel:"请选择网元类型",
		  		  dataSource:data,
		  		  filter: "contains",
		  		  suggest: true,
		  		  change:function(){
		  			 var neTypetext = $('#inputNeTypeTrigger').data("kendoDropDownList").text();
		  			 var nes = [];
		  			 if(neTypetext == "请选择网元类型"){
		  				nes = neList;
		  			 }else{
		  				$.each(neList,function(index,item){
		  					if(neTypetext == item.neType){
		  						nes[nes.length] = item;
		  					}
		  				})
		  			 }
		  			 $("#inputNeTrigger").kendoDropDownList({         
		  				  optionLabel:"请选择网元",
		  				  dataTextField: "neName",
		  			      dataValueField: "neName",
		  		  		  dataSource:nes,
		  		  		  filter: "contains",
		  		  		  suggest: true,
		  		  		  change:function(){
			  		  			var netext = $('#inputNeTrigger').data("kendoDropDownList").text();
			  		  			var units = [];
			  		  			if(netext == "请选择网元"){
			  		  				units = unitList;
			  		  			}else{
			  		  				$.each(unitList,function(index,item){
			  		  					if(netext == item.neName){
			  		  						units[units.length] = item;
			  		  					}
			  		  				})
			  		  			}
				  		  		$("#inputKeyWord").kendoDropDownList({         
				      				  optionLabel:"请选择单元",
				      				  dataTextField: "unitName",
				  			          dataValueField: "unitName",
				      		  		  dataSource:units,
				      		  		  filter: "contains",
				      		  		  suggest: true,
				      		  		  change:function(){
				      		  			gridUnitGrid();
				      		  		  }
			            		})
			            		gridUnitGrid();
				  		  		
		  		  		  }
		  			 })
		  			 gridUnitGrid();
		  		  }
			})
		}
	});
	
	
	$.ajax({
		url : "rest/equipment-ne",
		datatype : 'json',
		success : function(data) {
			neList = data._embedded["equipment-ne"];
			$("#inputNeTrigger").kendoDropDownList({         
				  optionLabel:"请选择网元",
				  dataTextField: "neName",
			      dataValueField: "neName",
		  		  dataSource:neList,
		  		  filter: "contains",
		  		  suggest: true,
		  		  change:function(){
	  		  			var netext = $('#inputNeTrigger').data("kendoDropDownList").text();
	  		  			var units = [];
	  		  			if(netext == "请选择网元"){
	  		  				units = unitList;
	  		  			}else{
	  		  				$.each(unitList,function(index,item){
	  		  					if(netext == item.neName){
	  		  						units[units.length] = item;
	  		  					}
	  		  				})
	  		  			}
		  		  		$("#inputKeyWord").kendoDropDownList({         
		      				  optionLabel:"请选择单元",
		      				  dataTextField: "unitName",
		  			          dataValueField: "unitName",
		      		  		  dataSource:unitList,
		      		  		  filter: "contains",
		      		  		  suggest: true,
		      		  		  change:function(){
		      		  			gridUnitGrid();
		      		  		  }
	            		})
	            		gridUnitGrid();
		  		  }
			})
		}
	});
	
	
	$.ajax({
		url : "queryUnitType",
		datatype : 'json',
		success : function(data) {	
			$("#inputUnitTypeTrigger").kendoDropDownList({         
				  optionLabel:"请选择单元类型",
		  		  dataSource:data,
		  		  filter: "contains",
		  		  suggest: true,
			  	  change:function(){
	  		  			var unittext = $('#inputUnitTypeTrigger').data("kendoDropDownList").text();
	  		  			var units = [];
	  		  			if(unittext == "请选择单元类型"){
	  		  				units = unitList;
	  		  			}else{
	  		  				$.each(unitList,function(index,item){
	  		  					if(unittext == item.unitType){
	  		  						units[units.length] = item;
	  		  					}
	  		  				})
	  		  			}
	  		  			console.log(units);
		  		  		$("#inputKeyWord").kendoDropDownList({         
		      				  optionLabel:"请选择单元",
		      				  dataTextField: "unitName",
		  			          dataValueField: "unitName",
		      		  		  dataSource:units,
		      		  		  filter: "contains",
		      		  		  suggest: true,
		      		  		  change:function(){
		      		  			gridUnitGrid();
		      		  		  }
	            		})
	            		gridUnitGrid();
		  		  }
			})
		}
	});
	
	var gridDataSource = new kendo.data.DataSource({
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
            		unitList = data._embedded["equipment-unit"];
            		$("#inputKeyWord").kendoDropDownList({         
	      				  optionLabel:"请选择单元类型",
	      				  dataTextField: "unitName",
	  			          dataValueField: "unitName",
	      		  		  dataSource:unitList,
	      		  		  filter: "contains",
	      		  		  suggest: true
            		})
        			return unitList;
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
	
	unitGrid = $("#unitGrid").kendoGrid({
		dataSource : gridDataSource,
		height : $(window).height() - $("#unitGrid").offset().top - 120,
		reorderable : true,
		resizable : true,
		sortable : true,
		pageable : false,
		columns: [/*{
			width: 30,
			template: "<input type='checkbox' id='#:_links.self.href#' />",
			attributes:{"class": "text-center"},
			title: "<input type='checkbox' class='grid_checkbox'/>"
		}, */{
			field: "neName",
			title: "<span  title='网元'>网元</span>"
		}, {
			field: "unitName",
			title: "<span  title='单元'>单元</span>"
		}, /*{
			field: "serverIp",
			title: "<span  title='地址'>地址</span>"
		},*/ {
			field: "unitType",
			title: "<span  title='单元类型'>单元类型</span>"
		}, {
			template : '<button class="addButton btn-xs btn-success" onclick="addList(this)"><i class="glyphicon glyphicon-search"></i>添加操作列表</button>',
			title: "<span  title='单元类型'>单元类型</span>"
		}],
		dataBound: function(){
		}
	}).data("kendoGrid");
	
	stepGrid = $("#stepGrid").kendoGrid({
		dataSource : [],
		height : $(window).height() - $("#stepGrid").offset().top - 120,
		reorderable : true,
		resizable : true,
		sortable : true,
		pageable : false,
		columns: [/*{
			width: 30,
			template: "<input type='checkbox' id='#:_links.self.href#' />",
			attributes:{"class": "text-center"},
			title: "<input type='checkbox' class='grid_checkbox'/>"
		}, */{
			field: "stepConfTable.stepSeq",
			title: "<span  title='步骤'>步骤</span>"
		}, {
			field: "stepConfTable.stepExplain",
			title: "<span  title='单元'>单元</span>"
		}, {
			field: "stepConfTable.stepDescribe",
			title: "<span  title='地址'>地址</span>"
		}, {
			field: "executeState",
			template: "#if(executeState == '0'){# <b style='color:red;'>待执行</b> #}else{#已执行 #}#",
			title: "<span  title='单元类型'>单元类型</span>"
		}, {
			template: "#if(executeState == '0'){# " +
					"<button id='btnStartBackup' onclick='javascript:startBackup(this);' class='btn-xs btn-success'><i class='glyphicon glyphicon-log-out'></i>执行恢复</button>" +
					"#}else{#" +
					"<button disabled='disabled' id='btnStartBackup' onclick='javascript:startBackup(this);' class='btn-xs btn-success'><i class='glyphicon glyphicon-log-out'></i>执行恢复</button></td> " +
					"#}#",
			title: "<span  title='单元类型'>单元类型</span>"
		}],
		dataBound: function(){
			//全选按钮
			$(".grid_checkbox").on("click",function(){
				$("#unitGrid .k-grid-content input[type='checkbox']").prop("checked",$(this).prop("checked"));
			});
		}
	}).data("kendoGrid");
	
	
	
	
	
	$("#isRecovery").click(function(){
		$("#myBackupModal").modal("hide");
		console.log(tr);
		$.ajax({
			url : "emergencySecurity/executeRecovery",
			data : {
				neTypeName: tr.stepConfTable.neType,
				unit : tr.emergencySecurityState.unitEntity.id,
				unitName:tr.emergencySecurityState.unitEntity.unitName,
				stepId : tr.stepConfTable.stepSeq,
				command:tr.stepConfTable.stepCommand
			},
			datatype : 'json',
			timeout:5000,
			cache:false,
			success : function(data) {
				if(data.error==-1){
					alert("节点恢复失败，请重试！");
				}else{
					stepGrid.dataSource.data(data.list);
					$("#resultGrid").text(data.ret);
				}
			}
		});
	});
	
	
	
	$.ajax({
		url : "getOperateStateByUnit",
		data :{unit : unitId },
		datatype : 'text',
		timeout:5000,
		success:function(data) {
			if(data.operateState==1){
				infoTip({content: "该节点已经被恢复，请选择其它节点！",color:"red"});
			}else{
				$.ajax({
					url : "emergencySecurity/getStepRecovery",
					data : {
					    site : webSite,
					    neType : neTypeId,
					    unit : unitId
					},
					datatype : 'json',
					timeout:5000,
					cache:false,
					success : function(data) {	
						stepGrid.dataSource.data(data);
					}
				});	
			}	
		}
	});	
	
	
	
	
	
	
})

function addList(btn){
	tr = unitGrid.dataItem($(btn).closest("tr"));
	$.ajax({
		url : "getOperateStateByUnit",
		data :{unit : tr.unitId },
		datatype : 'text',
		timeout:5000,
		success:function(data) {
			if(data.operateState==1){
				infoTip({content: "该节点已经被恢复，请选择其它节点！",color:"red"});
			}else{
				$.ajax({
					url : "emergencySecurity/getStepRecovery",
					data : {
					    site : tr.neId,
					    neType : tr.neType,
					    unit : tr.unitId
					},
					datatype : 'json',
					timeout:5000,
					cache:false,
					success : function(data) {	
						stepGrid.dataSource.data(data);
					}
				});	
			}	
		}
	});	
	
	
	
	
	
	
}



function startBackup(e){
	$("#myBackupModal").modal("show");
	tr = stepGrid.dataItem($(e).closest("tr"));
}







































/*


var sites = "";
var neTypes = "";
var units = "";

var webSite = "";
var unitId = "";
var neTypeId = "";

var tmp;

kendo.culture("zh-CN");
$(function(){
	webSite = $("#webSite").val();
	unitId = $("#unitId").val();
	neTypeId = $("#neTypeId").val();

	//获取站点
	$.ajax({
		url : "emergencySecurity/queryLocation",
		datatype : 'json',
		timeout:5000,
		cache:false,
		success : function(data) {
			handleSiteData(data)
		}
	});
	//返回站点数据
	function handleSiteData(data) {
		var len = data.length;
		sites = "[";
	  $.each(data, function(i){
		  if (i === len - 1){
		   sites+='{ text:"'+this+'", value:"'+this+'"}'
	      }else{
	   	   sites+='{ text:"'+this+'", value:"'+this+'"},'  
	      } 
		 }); 
	  	sites+="]";
	  
	  $("#inputSite").kendoDropDownList({
		  dataTextField: "text",
		  dataValueField: "value",
		  dataSource:  (new Function("return " + sites))(),
		  filter: "contains",
		  change: onChangeSite,
		  suggest: true
		});
//	  alert("默认inputSite的值："+$("#inputSite").val());
//	  $("#site").text($("#inputSite").val());
//	  getNeType($("#inputSite").val());	
	  if(webSite!=""){
			var color = $("#inputSite").data("kendoDropDownList");
//			color.select(1);
			color.value(webSite);
			$("#site").text(webSite);
			getNe(webSite);	
		}else{
			$("#site").text($("#inputSite").val());
			getNe($("#inputSite").val());		
		}
    }
	
	//根据site获取NeType
	function getNe(site){
		$.ajax({
			url : "emergencySecurity/queryNe",
			data : {
				location : site
			},
			datatype : 'json',
			timeout:5000,
			cache:false,
			success : function(data) {	
				handleTypeData(data);
			}
		});
	}
	//根据neType获取节点
	function getUnit(neId){
		//根据ne_type获取节点
		$.ajax({
			url : "emergencySecurity/queryUnitByNe",
			data : {
				neId : neId
			},
			datatype : 'json',
			timeout:5000,
			cache:false,
			success : function(data) {	
				
				handleUnitData(data);
				
			}
		});
	}
	
	function onChangeSite() {
//      alert("onChangeSite()=="+$("#inputSite").val());
	    clearData();//清空执行步骤和返回信息

	    $("#site").text($("#inputSite").data("kendoDropDownList").text());
	    getNe($("#inputSite").val());
	    
//	    if(unitName!=""&&unitName!=null){
//	    	alert(unitName);
//	    	$("#site").text(unitName);  
//        	getNeType(unitName);
//	    }
//	    else{
//	    	alert(unitName);
//	    	$("#site").text($("#inputSite").data("kendoDropDownList").text());
//        	getNeType($("#inputSite").val());
//	    }
        
    };
    
    function handleTypeData(data) {
    	clearNeTypeData();
		var len = data.length;
		neTypes = "[";
	  	
	  	$(data.rows).each(function(i, rows) {
			if (i === len - 1){
				neTypes+='{ text:"'+rows.name+'", value:"'+rows.id+'"}'
		      }else{
		    	neTypes+='{ text:"'+rows.name+'", value:"'+rows.id+'"},'  
		      } 
		});	
	  	neTypes+="]";
	  
	  $("#inputType").kendoDropDownList({
		  dataTextField: "text",
		  dataValueField: "value",
		  dataSource:  (new Function("return " + neTypes))(),
		  change: onChangeType,
		  filter: "contains",
		  suggest: true
		});
//	  alert("默认inputType的值："+$("#inputType").val());
//	  alert("neTypeId= "+neTypeId);

	 	if(neTypeId!=""){
			var color = $("#inputType").data("kendoDropDownList");
			color.value(neTypeId);
	  		getUnit(neTypeId); 
		}else{	
	  		getUnit($("#inputType").val()); 	
		}
		
      	$("#neType").text($("#inputType").data("kendoDropDownList").text()); 
      	    
    }
    
    function onChangeType() {
//      alert("onChangeType()=="+$("#inputType").val());
    	clearData();//清空执行步骤和返回信息
        $("#neType").text($("#inputType").data("kendoDropDownList").text());
        getUnit($("#inputType").val()); 
    };
    
    //返回的节点数据
    function handleUnitData(data) {

    	clearUnitData();
    	
		var len = data.length;
		units = "[";
		units+='{ text:"请选择", value:"0"},'
		$.each(data,function(index,unit){
			
			if (index === len - 1){
				units+='{ text:"'+unit.unit+'", value:"'+unit.id+'"}'
		     }else{
		    	units+='{ text:"'+unit.unit+'", value:"'+unit.id+'"},'  
		     } 
		});
	  	
	  	units+="]";

	  $("#inputNode").kendoDropDownList({
		  dataTextField: "text",
		  dataValueField: "value",
		  dataSource:  (new Function("return " + units))(),
		  change: onChangeNode,
		  filter: "contains",
		  suggest: true
		});

//	  alert($("#inputNode").data("kendoDropDownList").text());
//	  alert("unitId===="+unitId);
		if(unitId!=""){
			var color = $("#inputNode").data("kendoDropDownList");
			color.value(unitId);
		}
	  $("#unit").text($("#inputNode").data("kendoDropDownList").text());    
    }
    
	function onChangeNode() {

    	clearData();//清空执行步骤和返回信息
    	
    	$("#unit").text($("#inputNode").data("kendoDropDownList").text());
    };
	//清空inputType
	function clearNeTypeData(){
		$("#inputType").kendoDropDownList({
			  dataTextField: "text",
			  dataValueField: "value",
			  dataSource:  null,
			  filter: "contains",
			  suggest: true
			});
	}
	//清空inputNode
	function clearUnitData(){
		$("#inputNode").kendoDropDownList({
			  dataTextField: "text",
			  dataValueField: "value",
			  dataSource:  null,
			  filter: "contains",
			  suggest: true
			});
	}
	//添加执行步骤到操作列表
	$("#addButton").click(function(){
		
	});
	
	
	$("#btnStartBackup").click(function(){
	    $("#myBackupModal").modal("show");
	});	
	
	
		
	
});
//清空
function clearData(){
	$("#stepId").html("");
	$(".pre-scrollable").text("");
}

*/