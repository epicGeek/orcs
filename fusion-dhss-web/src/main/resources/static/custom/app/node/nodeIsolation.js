var sites = "";
var neTypes = "";
var units = "";
kendo.culture("zh-CN");


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


$(function(){
	
	
	/*var webSite = $("#webSite").val();
	var unitId = $("#unitId").val();
	var neTypeId = $("#neTypeId").val();*/
	
	
	
	
	
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
			//全选按钮
			$(".grid_checkbox").on("click",function(){
				$("#unitGrid .k-grid-content input[type='checkbox']").prop("checked",$(this).prop("checked"));
			});
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
					"<button id='btnStartBackup' onclick='javascript:startBackup(this);' class='btn-xs btn-success'><i class='glyphicon glyphicon-log-out'></i> 执行隔离</button>" +
					"#}else{#" +
					"<button disabled='disabled' id='btnStartBackup' onclick='javascript:startBackup(this);' class='btn-xs btn-success'><i class='glyphicon glyphicon-log-out'></i> 执行隔离</button></td> " +
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
	
	
	/*$.ajax({
		url : "getOperateStateByUnit",
		data :{unit : unitId },
		datatype : 'text',
		timeout:5000,
		success:function(data) {
			if(data.operateState == 2){
				infoTip({content: "该节点已经被隔离，请选择其它节点！",color:"red"});
			}else{
				$.ajax({
					url : "emergencySecurity/getStepByType",
					data : {
							neType : neTypeId,
							ne : webSite,
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
	});*/
	
	
	
});
var tr;
function startBackup(e){
	$('#myBackupModal').modal('show');
	tr = stepGrid.dataItem($(e).closest("tr"));
	$('#myBackupModal').find("div").eq(0).unbind("click");
}

function addList(btn){
	tr = unitGrid.dataItem($(btn).closest("tr"));
	$.ajax({
		url : "getOperateStateByUnit",
		data :{unit : tr.unitId },
		datatype : 'text',
		timeout:5000,
		success:function(data) {
			if(data.operateState == 2){
				infoTip({content: "该节点已经被隔离，请选择其它节点！",color:"red"});
			}else{
				$.ajax({
					url : "emergencySecurity/getStepByType",
					data : {
							neType : tr.neType,
							ne : tr.neId,
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




function doIsolation(){
//	alert("操作有风险，禁止操作！");
	$("#myBackupModal").modal("hide");
//	return false;
	/////////
	$.ajax({
		url : "isolation",
		data : {
			neType : tr.emergencySecurityState.ne.neType,
			unit : tr.emergencySecurityState.unit.id,
			unitName:tr.emergencySecurityState.unit.unitName,
			stepId : tr.stepConfTable.stepSeq,
			command:tr.stepConfTable.stepCommand,
			id:tr.id
		},
		datatype : 'json',
		timeout:5000,
		cache:false,
		success : function(data) {
			if(data.error==-1){
				infoTip({content: "节点隔离失败，请重试！",color:"red"});
			}else{
				stepGrid.dataSource.data(data.list);
				$("#resultGrid").text(data.ret);
		    }
		}
			
	});
}