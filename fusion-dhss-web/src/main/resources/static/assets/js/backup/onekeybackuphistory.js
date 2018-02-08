var contextPathValue = $("#contextPath").val();

var queryParams = {
	page : 1,
	pageSize : 1,
	pSiteId : '',
	pTypeId : '',
	pNodeName : ''
};

var datasource;
// 定义datasource

$("#inputSite").kendoDropDownList({
	optionLabel : "全部",
	dataTextField : "siteName",
	dataValueField : "siteName",
	dataSource : {
		type : "json",
		serverFiltering : true,
		transport : {
			read : "backup/allNeTypeList"
		}
	}
// filter: "contains",
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
			read : "backup/getUnitTypeByBackUpHist"
		}
	}
});

// 跳转到备份界面
function toOneKeyBackup() {
	window.location.href = "oneKeyBackup";
}

/*******************************************************************************
 * 构建查询结果集的GRID
 ******************************************************************************/
$("#gridNodeBackupHistory").kendoGrid({
	allowCopy : {
		delimeter : ","
	},
	columns : [ {
		field : "id",
		title : "#ID"
	}, {
		field : "bksitename",
		title : "站点"
	}, {
		field : "bksitetypename",
		title : "类型"
	}, {
		field : "bknodename",
		title : "节点名称"
	}, {
		hidden : true,
		field : "bkfilenameuuid"
	}, {
		hidden : true,
		field : "bkfiledisplayname"
	}, {
		field : "bkdate",
		title : "备份时间",
		format : "{0:yyyy-MM-dd hh:mm:ss}"
	}, {
		field : "bkaddwho",
		title : "操作人"
	}, {
		command : [ {
			name : "download",
			text : "下载",
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
 * 下载文件
 ******************************************************************************/
function downloadBackupFile(rowid, fileUid, displayFileName) {
	// 更改下载文件form中的属性值
	$("#downloadRowId").val(rowid);
	$("#downloadFileName").val(fileUid);
	$("#downloadFileDisplayName").val(displayFileName);
	// 提交form进行文件下载
	$("#downloadFileForm").submit();
}

/*******************************************************************************
 * 执行查询，根据页面选择的：站点,类型,节点名称
 * 
 * 
 ******************************************************************************/
function doSearchBackupHistory() {
	// searchBackupHistoryData();
//	getBackupHistoryData();// 加载查询结果
	// 重新构建Grid
	/***************************************************************************
	 * 重新构建查询结果集的GRID
	 **************************************************************************/
	
	datasource = new kendo.data.DataSource({
		transport : {
			// read:searchBackupHistoryData
			read : {
				url : "backup/searchOneKeyBackupHistoryList",
				dataType : "json",
				contentType : "application/json;charset=UTF-8",
				type : "GET"
			},
			parameterMap : function(options, operation) {
				if (operation == "read") {
					queryParams.page = options.page;
					queryParams.pageSize = options.pageSize;
					queryParams.pSiteId = $("#inputSite").val();
					queryParams.pTypeId = $("#inputType").val(),
							queryParams.pNodeName = $("#inputNodeSearchName").val()
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
	$("#gridNodeBackupHistory").kendoGrid({
				dataSource : datasource,
				columns : [
						{field : "id",title : "#ID"},
						{field : "bksitename",title : "站点"},
						{field : "bksitetypename",title : "类型"},
						{field : "bknodename",title : "节点名称"},
						{hidden : true,field : "bkfilenameuuid"},
						{hidden : true,field : "bkfiledisplayname"},
						{field : "bkdate",template : "#= bkdate ? kendo.toString(new Date(bkdate), \'yyyy-MM-dd HH:mm\') : \'\' #",
							title : "备份时间",
							//format : "{0:yyyy-MM-dd hh:mm:ss}"
						},
						{field : "bkaddwho",title : "操作人"},
						{
							command : [
									{
										name : "download",
										text : "下载",
										title : "下载操作",
										class : "btn btn-xs btn-success",
										click : function(e) {
											var tr = $(e.target).closest("tr"); // get
																				// the
																				// current
																				// table
																				// row
																				// (tr)
											// get the data bound to the current
											// table row
											var data = this.dataItem(tr);
											downloadBackupFile(data.id, data.bkfilenameuuid, data.bkfiledisplayname);
										}
									}, {
										name : "check",
										text : "备份日志",
										title : "",
										class : "btn btn-xs btn-success",
										click : function(e) {
											var tr = $(e.target).closest("tr");
											var data = this.dataItem(tr);
											showLog(data.id);
										}
									} ]
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
		pSiteId : $("#inputSite").val(),
		pTypeId : $("#inputType").val(),
		pNodeName : $("#inputNodeSearchName").val()
	};

	$.ajax({
		url : "backup/searchOneKeyBackupHistoryList",
		async : false,
		dataType : "json",
		data : params,
		type : "POST",
		success : function(result) {
			// notify the data source that the request succeeded \
			datasource = new kendo.data.DataSource({
				data : result.rows,
				pageSize : 1,
				// serverPaging: true,
				schema : {
					total : function() {
						return result.total;
					}// total is returned in the "total" field of the
						// response
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
	var window = $("#showLogWindows");
	if (!window.data("kendoWindow")) {
		window.kendoWindow({
			width : "1000px",
			y : "10px",
			title : "备份日志",
			actions : [ "Pin", "Maximize", "Close" ]
		});
	}
});
function showLog(id) {
	$("#showLogWindows").data("kendoWindow").pin();
	$("#showLogWindows").data("kendoWindow").open();
	$("#showLogWindows").data("kendoWindow").center();
	$(document).ready(queryLog(id));
}

function queryLog(id) {
	$.ajax({
		url : 'backup/queryLog',
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
