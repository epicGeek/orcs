kendo.culture("zh-CN");

var params = {
	page : 1,
	size : 20,
	startAlarmTime : '',
	endAlarmTime : '',
	alarmId : '',
	alarmNum : '',
	alarmCell : '',
	textValue : '',
	sort : '',
	alarmLevel : ''
};

var LogInfoWindow;

$(function() {
	
	

	var url = "alarm-new?history=history";
	var htmltemp = "";
	var title = [ {
		name : "活动告警",
		url : "alarm-new"
	}, {
		name : "历史告警",
		url : "alarm-new?history=history"
	} ];
	$.each(title,
			function(index, item) {
				htmltemp += "<li><a href='" + item.url + "'>" + item.name
						+ "</a></li>";
			})
	$("#navList").html(htmltemp);
	$("#navList a[href='" + url + "']").addClass("active");

	var unitdataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
				url : "rest/equipment-unit",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			}
		},
		schema : {
			data : function(d) {
				return d._embedded ? d._embedded["equipment-unit"]
						: new Array();
			}
		}
	});

	var alarmCell = $("#alarmCell").kendoDropDownList({
		optionLabel : "请选择单元",
		dataTextField : "unitName",
		dataValueField : "cnum",
		dataSource : unitdataSource,
		filter : "contains",
		suggest : true,
	}).data("kendoDropDownList");

	var alarmLevel = $("#alarmLevel").kendoDropDownList({
		optionLabel : "请选择等级",
		dataSource : [ "*", "**", "***" ],
		filter : "contains",
		suggest : true,
	}).data("kendoDropDownList");

	// 查询条件
	$("#startTime,#endTime").kendoDateTimePicker({
		format : "yyyy-MM-dd HH:MM:ss"
	});

	var dataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
				url : "alarm-history/search/searchByFilter",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			},
			parameterMap : function(options, operation) {
				if (operation == "read") {
					params.page = options.page - 1;
					params.size = options.pageSize;
					params.startAlarmTime = $("#startTime").val();
					params.endAlarmTime = $("#endTime").val();
					params.alarmId = $("#alarmId").val();
					params.alarmNum = $("#alarmNum").val();
					params.alarmCell = $("#alarmCell").val();
					params.alarmLevel = $("#alarmLevel").val();
					params.sort = "startTime,desc";
					params.textValue = $("#textValue").val();
					return params;
				}
			}
		},
		batch : true,
		schema : {
			data : function(d) {
				return d._embedded ? d._embedded["alarm_receive_history"] : [];
			},
			total : function(d) {
				if (d.page) {
					return d.page.totalElements; // 总条数
				}
			},
		},
		pageSize : 20,
		serverPaging : true,
		serverSorting : true
	});

	LogInfoWindow = {

		obj : undefined,

		template : undefined,

		id : $("#windowTemplate"),
		// 取消
		cancelClick : function() {
			$("#cancelBtn").on("click", function() {
				LogInfoWindow.obj.close();
			});
		},

		initContent : function(dataItem) {

			// 填充弹窗内容
			LogInfoWindow.obj.content(LogInfoWindow.template(dataItem));
			$("#logText").html(dataItem.dataLog);
			LogInfoWindow.cancelClick();

			LogInfoWindow.obj.center().open();
		},

		init : function() {

			this.template = kendo.template($("#windowTemplate").html());

			if (!LogInfoWindow.id.data("kendoWindow")) {
				LogInfoWindow.id.kendoWindow({
					width : "700px",
					actions : [ "Close" ],
					modal : true,
					title : "号段管理"
				});
			}
			LogInfoWindow.obj = LogInfoWindow.id.data("kendoWindow");
		}
	};

	LogInfoWindow.init();

	var column = [
			{
				field : "notifyId",
				title : "告警ID"
			},
			{
				field : "alarmCell",
				title : "告警单元"
			},
			{
				field : "alarmLevel",
				title : "告警等级"
			},
			{
				field : "alarmNo",
				title : "告警号"
			},
			{
				field : "startTime",
				title : "告警时间"
			},
			{
				field : "cancelTime",
				title : "告警关闭时间"
			},
			{
				field : "supplInfo",
				title : "附加细信息",
				hidden : true
			},
			{
				title : "描述",
				template : "<a class='btn btn-xs btn-warning' onClick = 'showdesc(\"#:alarmString#\",\"#:alarmDesc#\")'><i class='glyphicon'></i> 告警描述</a>"
			} ];

	var grid = $("#dataGrid").kendoGrid({

		height : $(window).height() - $("#dataGrid").offset().top - 50,

		dataSource : dataSource,

		reorderable : true,

		resizable : true,

		sortable : false,

		columnMenu : false,

		pageable : true,

		columns : column

	}).data("kendoGrid");

	$("#search").on("click", function() {
		dataSource.read();
	});

	$("#clearbtn")
			.on(
					"click",
					function() {
						alarmCell.select(0);
						alarmLevel.select(0);
						$(
								"#startTime,#endTime,#alarmId,#alarmNum,#alarmLevel,#textValue")
								.val("");
						dataSource.read();
					});

	$('#export').on('click', function() {
		$("#myExportModal").modal("show");
	});

	$('#doExport').on('click', function() {
		$("#dataGrid").data("kendoGrid").saveAsExcel();
		$("#myExportModal").modal("hide");
	});

});

function showdesc(text,id) {
	LogInfoWindow.obj.setOptions({
		"title" : "描述"
	});
	if(id !=null && id != ""){
		$.ajax({
			dataType : "json",
			url : "rest/system-alarm-rule/" + id,
			type : "GET",
			success : function(data) {
				LogInfoWindow.initContent({
					dataLog : text + "\n\n" + data.alarmDesc
				});
			}
		});
	}else{
		LogInfoWindow.initContent({
			dataLog : text + "\n\n" 
		});
	}
	
}
