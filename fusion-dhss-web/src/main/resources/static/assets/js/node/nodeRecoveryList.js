kendo.culture("zh-CN");

var neTypes = "";

var unit;
var neType;
var neState;
var startdatetime;
var enddatetime;

var searchparams = {
	page : 1,
	pageSize : 10,
	rows : 10,
	keyWord : ''
};
var dataSource;
$(function() {
	
	getDataM();
	
	// 获取网元列表
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "emergencySecurity/getAllNe",
		"success" : function(data) {
	
			handleNeTypeData(data)
		}
	});
	// 返回网元类型数据
	function handleNeTypeData(data) {
		var len = data.length;
		neTypes = "[";
		$(data.rows).each(function(i, rows) {
			if (i === len - 1) {
				neTypes += '{ text:"' + rows.ne + '", value:"' + rows.id + '"}'
			} else {
				neTypes += '{ text:"' + rows.ne + '", value:"' + rows.id + '"},'
			}
		});
		neTypes += "]";

		$("#inputNeType").kendoComboBox({
					dataTextField : "text",
					dataValueField : "value",
					dataSource : (new Function("return " + neTypes))(),
					filter : "contains",
					suggest : true
				});
	}

	$("#startdatetime").kendoDateTimePicker({
				value : new Date()
			});
	$("#enddatetime").kendoDateTimePicker({
				value : new Date()
			});
});
function getDataM() {

	unit = $("#inputUnit").val();
	neType = $("#inputNeType").val();
	neState = $("#inputNeState").val();
	startdatetime = $("#startdatetime").val();
	enddatetime = $("#enddatetime").val();

	dataSource = new kendo.data.DataSource({
				transport : {
					read : {
						type : "GET",
						url : "emergencySecurity/querySecurityState",
						dataType : "json",
						contentType : "application/json"
					},
					parameterMap : function(options, operation) {
						if (operation == "read") {
							searchparams.page = options.page;
							searchparams.pageSize = options.pageSize;
							searchparams.unit = unit;
							searchparams.neType = neType;
							searchparams.neState = neState;
							searchparams.startdatetime = startdatetime;
							searchparams.enddatetime = enddatetime;
							searchparams.rows = options.rows;
							searchparams.operate_state = 2;
							return searchparams;
						}
					}
				},
				batch : true,
				pageSize : 10, // 每页显示个数
				schema : {
					data : function(d) {

						return d.Data; // 响应到页面的数据
					},
					total : function(d) {
						return d.totalCount; // 总条数
					},
					currPage : function(d) {
						return d.startIndex; // 总条数
					},
					pageSize : function(d) {
						return d.pageSize; // 总条数
					}
				},
				serverPaging : true,
				serverFiltering : true,
				serverSorting : true

			});

	$("#gridNodeRecovery").kendoGrid({
		dataSource : dataSource,
		groupable : false,
		sortable : false,
		reorderable : false,
		resizable : true,
		columnMenu : false,
		scrollable : false,
		pageable : {
			buttonCount : 4
		},
		columns : [{
					field : "id",
					width : 50,
					locked : true
				}, {
					field : "operate_date",
					template : "#= operate_date ? kendo.toString(new Date(operate_date), \'yyyy-MM-dd HH:mm\') : \'\' #",
					title : "时间",
					width : 180
				}, {
					field : "unit_name",
					title : "节点名称",
					width : 180
				}, {
					field : "ne_name",
					title : "网元名称",
					width : 120
				}, {
					field : "location",
					title : "所在站点",
					width : 120
				}, {
					field : "operate_state",
					template : "#if(operate_state==1){#节点隔离#}if(operate_state==2){#节点恢复#}if(operate_state==4){#网元隔离#}if(operate_state==5){#网元恢复#}#",
					title : "状态",
					width : 100
				}, {
					field : "operator",
					title : "操作人",
					width : 100
				}, {
					command : {
						text : "查看详情",
						click : showDetails
					},
					title : "操作",
					width : "180px"
				}]
	});
}
// 查询
	$("#searchButton").click(function() {
		getDataM();
	});
	
function showDetails(e) {
	// location.href = 'emergencySecurity/nodeRecovery';
	var tr = $(e.target).closest("tr");
	var data = this.dataItem(tr);
//	alert(data.ne);
	var unitId;
	if(data.unit_id==undefined){
		unitId = 0;
	}else{
		unitId = data.unit_id;
	}
	location.href = 'nodeRecovery?state=' + data.operate_state + '&webSite=' + data.location
			+ '&neTypeId=' + data.ne_id + '&unitId=' + unitId;
}