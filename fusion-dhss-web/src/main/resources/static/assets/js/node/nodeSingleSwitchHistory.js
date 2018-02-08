var contextPathValue = $("#contextPath").val();

var queryParams = {
	page : 1,
	pageSize : 10,
	pSiteName : '',
	pNeName : '',
	pTypeName : '',
	pNodeName : ''
};

var datasource;
// 定义datasource
datasource = new kendo.data.DataSource({
	transport : {
		read : {
			url : "nodeSwitch/searchSingleNodeSwitchHistoryList",
			dataType : "json",
			contentType : "application/json;charset=UTF-8",
			type : "GET"
		},
		parameterMap : function(options, operation) {
			if (operation == "read") {
				queryParams.page = options.page;
				queryParams.pageSize = options.pageSize;
				queryParams.pSiteName = $("#inputSite").data("kendoDropDownList").text();
				queryParams.pNeName = $("#inputNe").data("kendoDropDownList").text();
				queryParams.pTypeName = $("#inputType").data("kendoComboBox").text();
				queryParams.pNodeName = $("#inputNodeSearchName").val();
				return queryParams;
			}
		}
	},
	batch : true,
	pageSize : 10,
	serverPaging : true,
	serverFiltering : true,
	serverSorting : true,
	schema : {
		data : function(d) {
			return d.rows; // 响应到页面的数据
		},
		total : function(d) {
			return d.total; // 总条数
		}
	}
});

/*$("#inputSite").kendoDropDownList({
	optionLabel : "全部",
	dataTextField : "siteName",
	dataValueField : "siteCode",
	dataSource : {
		type : "json",
		serverFiltering : true,
		transport : {
			read : "backup/siteList"
		}
	}
});

$("#inputType").kendoDropDownList({
	autoBind : false,
	cascadeFrom : "inputSite",
	optionLabel : "请选择...",
	dataTextField : "neTypeName",
	dataValueField : "neTypeCode",
	dataSource : {
		type : "json",
		serverFiltering : true,
		transport : {
			read : "backup/neTypeList"
		}
	}
});*/
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
    suggest: true
}).data("kendoDropDownList");

/**操作选择*/
var inputType = $("#inputType").kendoComboBox({
	dataTextField : "text",
	dataValueField : "value",
	dataSource : [
	              {text : "隔离",value : "隔离"}, 
	              {text : "恢复",value : "恢复"}
	              ],
	filter : "contains",
	suggest : true
}).data("kendoComboBox");
/***/

// 跳转到板卡倒换
function toNodeSwitching() {
	window.location.href = "nodeSwitching";
}

/*******************************************************************************
 * 构建查询结果集的GRID
 ******************************************************************************/
$("#gridSingleHistory").kendoGrid({
	allowCopy : {
		delimeter : ","
	},
	columns : [ 
       {
		field : "id",
		title : "#id"
	}, {
		field : "sitename",
		title : "站点"
	}, {
		field : "nename",
		title : "网元"
	}, {
		field : "unitname",
		title : "板卡"
	},{
		field : "conftypename",
		title : "类型"
	}, {
		field : "operatedate",
		template : "#= operatedate ? kendo.toString(new Date(operatedate), \'yyyy-MM-dd HH:mm\') : \'\' #",
		title : "时间",
		format : "{0:yyyy-MM-dd hh:mm:ss}"
	}, {
		field : "operator",
		title : "操作人"
	}, {
		command : [ {
			name : "download",
			text : "",
			class : "btn btn-xs btn-success"
		} ]
	} ],
	groupable : false,
	sortable : true,
	reorderable : true,
	resizable : false,
	columnMenu : false,
	scrollable : false,
	pageable : {
		buttonCount : 4
	}
});

/*******************************************************************************
 * 执行查询，根据页面选择的：站点,类型,节点名称
 ******************************************************************************/
function doSearchSingleHistory() {
//	searchBackupHistoryData();
	getBackupHistoryData();// 加载查询结果
	// 重新构建Grid
	/***************************************************************************
	 * 重新构建查询结果集的GRID
	 **************************************************************************/
	$("#gridSingleHistory").kendoGrid({
				dataSource : datasource,
				columns : [
						{
							field : "id",
							title : "#id"
						}, {
							field : "sitename",
							title : "站点"
						}, {
							field : "nename",
							title : "网元"
						}, {
							field : "unitname",
							title : "板卡"
						}, {
							field : "conftypename",
							title : "类型"
						}, {
							field : "operatedate",
							template : "#= operatedate ? kendo.toString(new Date(operatedate), \'yyyy-MM-dd HH:mm\') : \'\' #",
							title : "时间",
							format : "{0:yyyy-MM-dd hh:mm:ss}"
						}, {
							field : "operator",
							title : "操作人"
						}, {
							command : [
									{
										name : "check",
										text : "查看日志",
										title : "查看日志",
										class : "btn btn-xs btn-success",
										click : function(e) {
											var tr = $(e.target).closest("tr");
											var data = this.dataItem(tr);
											showSingleLog(data.id);
										}
									},{
										name : "download",
										text : "下载",
										title : "下载操作",
										class : "btn btn-xs btn-success",
										click : function(e) {
											var tr = $(e.target).closest("tr"); 
											var data = this.dataItem(tr);
											downloadFile(data);
										}
									},
									{name : "delete",text : "删除",title : "删除操作",class : "btn btn-xs btn-success",
										click : function(e) {
											var tr = $(e.target).closest("tr");
											var data = this.dataItem(tr);
											deleteData(data.id);
										}
									}
									]
						} ],
				scrollable : false,
				groupable : false,
				sortable : true,
				reorderable : true,
				resizable : false,
				columnMenu : false,
				// pageable:true
				pageable : {
					buttonCount : 4,
					// info: false,
					previousNext : true
				}
			});
}
/*******************************************************************************
 * 下载文件
 ******************************************************************************/
function downloadFile(data) {
	// 更改下载文件form中的属性值
	$("#rowId").val(data.id);
	$("#rowSiteName").val(data.siteName);
	$("#rowNeName").val(data.neName);
	$("#rowUnitName").val(data.unitName);
	$("#rowConfTypeName").val(data.confTypeName);
	
	// 提交form进行文件下载
	$("#downloadSingleNodeSwitchFile").submit();
}

/*******************************************************************************
/*******************************************************************************
 * 构建查询结果的datasource 应用于Grid的显示
 ******************************************************************************/
function getBackupHistoryData() {
	queryParams.page = 1;
	datasource.read(queryParams);
}
/*******************************************************************************
 * 查询备份的历史数据 返回json字符串格式
 ******************************************************************************/
function searchBackupHistoryData() {
	var params = {
		pSiteName : $("#inputSite").data("kendoDropDownList").text(),
		pNeName : $("#inputNe").data("kendoDropDownList").text(),
		pTypeName : $("#inputType").data("kendoComboBox").text(),
		pNodeName : $("#inputNodeSearchName").val()
	};
	
	$.ajax({
		url : "nodeSwitch/searchSingleNodeSwitchHistoryList",
		async : false,
		dataType : "json",
		data : params,
		type : "POST",
		success : function(result) {
			datasource = new kendo.data.DataSource({
				data : result.rows,
				pageSize : 10,
				// serverPaging: true,
				schema : {
					total : function() {
						return result.total;
					}
				}
			});
		},
		error : function(data) {
			alert("获取数据失败!");
		}
	});
}
kendo.culture("zh-CN");
$(function() {
	var window = $("#showSingleLogWindows");
	if (!window.data("kendoWindow")) {
		window.kendoWindow({
			width : "1000px",
			y : "10px",
			title : "查看日志",
			actions : [ "Pin", "Maximize", "Close" ]
		});
	}
});
/***/
function showSingleLog(id) {
	$("#showSingleLogWindows").data("kendoWindow").pin();
	$("#showSingleLogWindows").data("kendoWindow").open();
	$("#showSingleLogWindows").data("kendoWindow").center();
	$(document).ready(querySingleLog(id));
}
/***/
function querySingleLog(id) {
	$.ajax({
		url : 'nodeSwitch/querySingleLog',
		type : 'GET',
		data : 'id=' + id,
		async : false, // 默认为true 异步
		error : function() {
			divshow.text("");// 清空数据
			divshow.append("查询失败，请联系管理员");
		},
		success : function(data) {
			var divshow = $("#logData");
			divshow.text("");// 清空数据
			divshow.append('<pre>' + data + '</pre>');
		}
	});
}
function deleteData(id) {
	if(!confirm("确认删除该记录吗?")){
        return;
    }
	
    jQuery.ajax({
        type:'get',
        url:'nodeSwitch/deleteSingleNodeSwitch?id='+id,
        dataType: "json",
        success:function(data){
            alert("删除成功");  
            queryParams.page = 1;
        	datasource.read(queryParams);
        },
        error : function(data) {
            alert("删除失败");
            queryParams.page = 1;
        	datasource.read(queryParams);  
        }
    });
}