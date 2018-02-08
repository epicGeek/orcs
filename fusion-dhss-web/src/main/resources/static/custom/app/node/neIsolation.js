var sites = "";
var neTypes = "";
var units = "";
kendo.culture("zh-CN");


var neList = [];
var unitList = [];
var unitTypeList = [];

var unitGrid;

function  gridUnitGrid(){
	var filters = [];

	if($('#inputNeTypeTrigger').data("kendoDropDownList").text() != "请选择网元类型" 
		&& $('#inputNeTypeTrigger').data("kendoDropDownList").text() != ""){
		filters.push({field: "neType", operator: "eq", value: $('#inputNeTypeTrigger').val() });
	}
	if($('#inputNeTrigger').data("kendoDropDownList").text() != "请选择网元" 
		&& $('#inputNeTrigger').data("kendoDropDownList").text() != ""){
		filters.push({field: "neName", operator: "eq", value: $('#inputNeTrigger').data("kendoDropDownList").text() });
	}
	if($('#inputUnitTypeTrigger').data("kendoDropDownList").text() != "请选择单元类型" 
		&& $('#inputUnitTypeTrigger').data("kendoDropDownList").text() != ""){
		filters.push({field: "unitType", operator: "eq", value: $('#inputUnitTypeTrigger').val() });
	}
	if($('#inputKeyWord').data("kendoDropDownList").text() != "请选择单元" 
		&& $('#inputKeyWord').data("kendoDropDownList").text() != ""){
		filters.push({field: "unitName", operator: "eq", value: $('#inputKeyWord').val() });
	}
	console.log($('#inputKeyWord').val());

	unitGrid.dataSource.filter(filters);
	unitGrid.dataSource.fetch();
}


$(function(){
	
	
	
	
	
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
		}, {
			field: "serverIp",
			title: "<span  title='地址'>地址</span>"
		}, {
			field: "unitType",
			title: "<span  title='单元类型'>单元类型</span>"
		}, {
			template : '<button class="btn btn-xs btn-success" onclick=""><i class="glyphicon glyphicon-search"></i>添加操作列表</button>',
			title: "<span  title='单元类型'>单元类型</span>"
		}],
		dataBound: function(){
			//全选按钮
			$(".grid_checkbox").on("click",function(){
				$("#unitGrid .k-grid-content input[type='checkbox']").prop("checked",$(this).prop("checked"));
			});
		}
	}).data("kendoGrid");
	
	//添加执行步骤到操作列表
	$("#addButton").click(function(){
		if($('#inputKeyWord').data("kendoDropDownList").text() == "请选择单元"){//网元操作
			$.ajax({
				url : "emergencySecurity/getOperateStateByNe",
				data :{neId : $("#inputType").val()},
				datatype : 'text',
				timeout:5000,
				success:function(data) {
					if(data==2){
					alert("该网元已经被隔离，请选择其它网元！");
					}else{
						$.ajax({
				url : "emergencySecurity/getStepByType",
				data : {
					   site : $("#inputSite").val(),
					ne_type : $("#inputType").val(),
					   unit : $("#inputNode").val()
				},
				datatype : 'json',
				timeout:5000,
				cache:false,
				success : function(data) {	
					var stepHtml = "";
					
					$.each(data,function(index,s){
						state = "";
						if(s.execute_state==0){
						state="待执行";
						btn = "<td><button id='btnStartBackup' onclick='javascript:startBackup(this);' class='btn btn-danger'><i class='glyphicon glyphicon-log-out'></i> 执行隔离</button></td>";
						}else{
							state="已执行";
							btn = "<td><button disabled='disabled' id='btnStartBackup' onclick='javascript:startBackup(this);' class='btn btn-danger'><i class='glyphicon glyphicon-log-out'></i> 执行隔离</button></td>";
						}
						
						stepHtml+="<tr>"
							+"<th scope='row'>"+s.step_seq +"</th>"
							+"<td style='display:none'>"+ s.step_command +"</td>"
							+"<td>"+ s.step_explain +"</td>"
							+"<td>"+ s.step_describe +"</td>"
							+"<td>"+ state +"</td>"
							+btn
							+"</tr>";             
					});

					$("#stepId").html(stepHtml);
				}
			});
					}
				}
			});
		}else{
			
			
			$.ajax({
				url : "getOperateStateByUnit",
				data :{unit : $("#inputKeyWord").val()},
				datatype : 'text',
				timeout:5000,
				success:function(data) {
					if(data.operateState == 2){
						alert("该节点已经被隔离，请选择其它节点！");
					}else{
						$.ajax({
							url : "emergencySecurity/getStepByType",
							data : {
									neType : $("#inputNeTypeTrigger").val(),
									ne : $("#inputNeTrigger").val(),
								    unit : $("#inputKeyWord").val()
							},
							datatype : 'json',
							timeout:5000,
							cache:false,
							success : function(data) {	
								var stepHtml = "";
								
								$.each(data,function(index,s){
									state = "";
									if(s.execute_state==0){
									state="待执行";
									btn = "<td><button id='btnStartBackup' onclick='javascript:startBackup(this);' class='btn btn-danger'><i class='glyphicon glyphicon-log-out'></i> 执行隔离</button></td>";
									}else{
										state="已执行";
										btn = "<td><button disabled='disabled' id='btnStartBackup' onclick='javascript:startBackup(this);' class='btn btn-danger'><i class='glyphicon glyphicon-log-out'></i> 执行隔离</button></td>";
									}
									
									stepHtml+="<tr>"
										+"<th scope='row'>"+s.step_seq +"</th>"
										+"<td style='display:none'>"+ s.step_command +"</td>"
										+"<td>"+ s.step_explain +"</td>"
										+"<td>"+ s.step_describe +"</td>"
										+"<td>"+ state +"</td>"
										+btn
										+"</tr>";             
								});
			
								$("#stepId").html(stepHtml);
							}
						});
					}
				}
			});
		}
				
	});
	
});
var tr;
function doIsolation(){
//	alert("操作有风险，禁止操作！");
	$("#myBackupModal").modal("hide");
//	return false;
	/////////
	$.ajax({
		url : "emergencySecurity/isolation",
		data : {
			ne:$("#inputType").val(),
			unit : $("#inputNode").val(),
			unitName:$("#inputNode").data("kendoDropDownList").text(),
			stepId : tr.children("th:eq(0)").text(),
			command:tr.children("td:eq(0)").text()
		},
		datatype : 'json',
		timeout:5000,
		cache:false,
		success : function(data) {
			if(data.error==-1){
				alert("节点隔离失败，请重试！");
			}else{
				
				$("#stepId").html("");
			
			var stepHtml = "";
				
			$.each(data.list,function(index,s){
				state = "";
				btn = "";
				if(s.execute_state==0){
					state="待执行";
					btn = "<td><button id='btnStartBackup' onclick='javascript:startBackup(this);' class='btn btn-danger'><i class='glyphicon glyphicon-log-out'></i> 执行隔离</button></td>";
				}else{
					state="已执行";
					btn = "<td><button disabled='disabled' id='btnStartBackup' onclick='javascript:startBackup(this);' class='btn btn-danger'><i class='glyphicon glyphicon-log-out'></i> 执行隔离</button></td>";
				}
								
				stepHtml+="<tr>"
					+"<th scope='row'>"+s.step_seq +"</th>"
					+"<td style='display:none'>"+ s.step_command +"</td>"
					+"<td>"+ s.step_explain +"</td>"
					+"<td>"+ s.step_describe +"</td>"
					+"<td>"+ state +"</td>"
					+ btn
					+"</tr>";             
			});
//				alert(stepHtml);
			$("#stepId").html(stepHtml);
			
//			alert(data.ret);
			$(".pre-scrollable").text(data.ret);
		}
			}
			
	});
}