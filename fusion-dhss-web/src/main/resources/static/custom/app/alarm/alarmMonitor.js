kendo.culture("zh-CN");
var neName;
var startTime;
var endTime;
var alarmType;
var defaultEndTime;
var defaultStartTime;
var searchparams = {
	page : 1,
	pageSize : 20,
	startTime : '',
	endTime : '',
	neName : '',
	alarmType : ''
};
$(function() {
	$("#startTime").kendoDateTimePicker({
		format:"yyyy-MM-dd HH:mm:ss"
	});
	$("#endTime").kendoDateTimePicker({
		format:"yyyy-MM-dd HH:mm:ss"
	});
	var inputNeName = $("#inputNeName").kendoDropDownList({
		optionLabel:"全部单元",
		dataSource: {
            transport: {
                read: {
                    dataType: "json",
                    url: "rest/equipment-unit"
                }
            },
            schema : {
    			data : function(d) {
    				if(d._embedded){
    					return d._embedded["equipment-unit"];  //响应到页面的数据
    			     }else{
    					return new Array();
    				}
    			}
    		}
        },
		filter: "contains",
		dataTextField: "unitName",
		dataValueField: "unitName",
		suggest: true
	}).data("kendoDropDownList");

	$("#inputAlarmType").kendoDropDownList({
		dataTextField : "text",
		dataValueField : "value",
		optionLabel:"全部告警类型",
		dataSource : [{
						text : "指标监控结果",
						value : "指标监控结果"
					}, {
						text : "智能巡检结果",
						value : "智能巡检结果"
					}, {
						text : "BOSS监控结果",
						value : "BOSS监控结果"
					} ],
		filter : "contains",
		suggest : true
	});
	getDataM();
	$('#clearsearchalarm').on('click', function(event) {
		$("#startTime").val(defaultStartTime);
		$("#endTime").val(defaultEndTime);
		$("#inputAlarmType").data("kendoDropDownList").text("");
		$('#inputAlarmType').val("");
		$('#inputNeName').val("");
	});

	$('#searchalarm').on('click', function(event) {
		getDataM();
	});
	$.ajax({
		url:"alarm-monitor/search/countKpiAlarm",
		type:"GET",
		async:false,//确保数据全部取出来
 		success:function(data){
 			defaultEndTime = data.defaultEndTime;
 			defaultStartTime = data.defaultStartTime;
 			if($("#param").val()=='kpi'){
 				$('#inputAlarmType').data("kendoDropDownList").text("指标监控结果");
 				$('#inputAlarmType').val("指标监控结果");
 				$("#startTime").val(data.startTimeBefore);
 				$("#endTime").val(data.startTimeAfter);
 				getDataM();
 			}else{
 				$("#startTime").val(data.defaultStartTime);
 				$("#endTime").val(data.defaultEndTime);
 				getDataM();
 			}
 		}
	});
});
function getDataM() {
	neName = $("#inputNeName").val();
	startTime = $("#startTime").val();
	endTime = $("#endTime").val();
	alarmType = $("#inputAlarmType").val();
	var dataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
				url : "alarm-monitor/search/searchByFilter",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			},
			parameterMap : function(options, operation) {
				if (operation == "read") {
					searchparams.page = options.page - 1;
					searchparams.size = options.pageSize;
					searchparams.neName = neName;
					searchparams.startTime = startTime;
					searchparams.endTime = endTime;
					searchparams.alarmType = alarmType;
					searchparams.sort = "startTime,desc";
					//searchparams.neType = neType;
					return searchparams;
				}
			}
		},
		batch : true,
		schema : {
			data : function(d) {
				if (d._embedded) {
					return d._embedded["alarm-monitor"]; //响应到页面的数据
				} else {
					return [];
				}
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

	$("#gridNodeRecovery").kendoGrid({
		dataSource : dataSource,
		height: $(window).height()-$("#gridNodeRecovery").offset().top - 50,
		groupable : false,
		sortable : true,
		reorderable : true,
		resizable : true,
		columnMenu : true,
		pageable : {
			buttonCount : 4
		},
		columns : [
		/*  { field: "id", width: 50,locked: true},*/
		{
			field : "startTime",
			title : "时间",
			width : 130
		}, {
			field : "neName",
			title : "网元",
			width : 140
		}, {
			field : "unitName",
			title : "单元",
			width : 130
		}, {
			field : "alarmType",
			title : "告警类型",
			width : 80
		}, {
			field : "alarmLevel",
			title : "等级",
			width : 60
		}, {
			field : "alarmContent",
			title : "告警内容",
			width : 300,
			style : "font-size:12"
		}]
	});
}
