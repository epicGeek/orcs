kendo.culture("zh-CN");

$(function() {
	
	
	var dataSource = new kendo.data.DataSource({
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
				if(d._embedded){
					return d._embedded["equipment-unit"];  //响应到页面的数据
			     }else{
					return new Array();
				}
			}
		}
	});
	
	$("#alarmCell").kendoDropDownList({
		optionLabel:"请选择单元",
        dataTextField: "unitName",
        dataValueField: "cnum",
        dataSource: dataSource,
        filter: "contains",
        suggest: true,
       // index:1;
    });
	var levels = ['*','**','***'];
	$("#alarmLevel").kendoDropDownList({
		optionLabel:"请选择告警等级",
        dataSource: levels,
        suggest: false,
    });
	
	
	
	
	
	
	
	
	
	
	
	
	
	var params = {
		page : 1,
		size : 20,
		startAlarmTime : '',
		endAlarmTime : '',
		alarmId : '',
		alarmNum : '',
		alarmCell : '',
		textValue : '',
		sort:'',
		alarmLevel:''
	};
	$("input[name='alarmCheck']").eq(0).attr("checked","checked");

	$("#startAlarmTime").kendoDateTimePicker({
		// value: new Date(),
		format : "yyyy-MM-dd HH:mm:ss",
		interval : 60
	}).data("kendoDateTimePicker");
	$("#endAlarmTime").kendoDateTimePicker({
		format : "yyyy-MM-dd HH:mm:ss",
		interval : 60
	}).data("kendoDateTimePicker");

	$('#search').on('click', function(event) {
		var name = $("input[name='alarmCheck']:checked").val();
		if(name == "start"){
			$("#grid").data("kendoGrid").pager.page(params);
		}else{ 
			$("#grid1").data("kendoGrid").pager.page(params);
		}
//		$("#grid").data("kendoGrid").pager.page(params);
	});
	$('#reset').on('click', function(event) {
		$("#startAlarmTime").val("");
		$("#endAlarmTime").val("");
		$("#alarmNum").val("");
		$("#alarmId").val("");
		$("#alarmCell").val("");
		$("#textValue").val("");
		$("#alarmLevel").val("");
		var name = $("input[name='alarmCheck']:checked").val();
		if(name == "start"){
			$("#grid").data("kendoGrid").pager.page(params);
		}else{ 
			$("#grid1").data("kendoGrid").pager.page(params);
		}
	});
	var column = [ { field : "notifyId", title : "告警ID" }, { field : "alarmCell", title : "告警单元" }, 
	               { field : "alarmLevel", title : "告警等级" }, { field : "alarmNo", title : "告警号" },
	               { field : "receiveStartTime", title : "告警时间" }, { field : "cancelTime", title : "告警关闭时间"}, 
	               { field : "supplInfo", title : "附加细信息",hidden:true  }];
	
	

	var paramsExtra = {
		alarmRecordId : ''
	};
	
	var dataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
				url : "alarm-record/search/searchByFilter",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			},
			parameterMap : function(options, operation) {
				if (operation == "read") {
					params.page = options.page - 1;
					params.size = options.pageSize;
					params.startAlarmTime = $("#startAlarmTime").val();
					params.endAlarmTime = $("#endAlarmTime").val();
					params.alarmId = $("#alarmId").val();
					params.alarmNum = $("#alarmNum").val();
					params.alarmCell = $("#alarmCell").val();
					params.alarmLevel = $("#alarmLevel").val();
					params.sort="receiveStartTime,desc";
					params.textValue = $("#textValue").val();
					return params;
				}
			}
		},
		batch : true,
		schema : {
			data : function(d) {
				if (d._embedded) {
					return d._embedded["alarm_receive_record"]; // 响应到页面的数据
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
	function activeFunction(){
		

		$("#grid").kendoGrid({
			height: $(window).height()-$("#grid").offset().top - 50,
			dataSource : dataSource,
			reorderable: true,

			resizable: true,

			sortable: false,

			columnMenu: false,

			pageable: true,
			detailTemplate : kendo.template($("#template").html()),
			detailInit : detailInit,
			columns : column
		}).data("kendoGrid");
	}
	
//	activeFunction();
	
	var cancelDataSource = new kendo.data.DataSource({
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
					params.startAlarmTime = $("#startAlarmTime").val();
					params.endAlarmTime = $("#endAlarmTime").val();
					params.alarmId = $("#alarmId").val();
					params.alarmNum = $("#alarmNum").val();
					params.alarmCell = $("#alarmCell").val();
					params.alarmLevel = $("#alarmLevel").val();
					params.sort="receiveStartTime,desc";
					params.textValue = $("#textValue").val();
					return params;
				}
			}
		},
		batch : true,
		schema : {
			data : function(d) {
				if (d._embedded) {
					return d._embedded["alarm_receive_history"]; // 响应到页面的数据
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
	function cancelFunction(){
		
		$("#grid1").kendoGrid({
			height: $(window).height()-$("#grid1").offset().top - 50,
			dataSource : cancelDataSource,
			reorderable: true,

			resizable: true,

			sortable: false,

			columnMenu: false,

			pageable: true,
			detailTemplate : kendo.template($("#template1").html()),
			detailInit : detailInit1, 
			columns : column
		}).data("kendoGrid");
		
	}
	
	
	
	$("[ name = 'alarmCheck' ]").on("click",function(){
		var name = $(this).val();
		if(name == "start"){
			$("#grid1").html("");
			$("#grid1").hide();
			$("#grid").show();
			activeFunction();
		}else{ 
			$("#grid").html("");
			$("#grid").hide();
			$("#grid1").show();
			cancelFunction();
		}
	});



	function detailInit(e) {
		if(e.data.alarmDesc != null && e.data.alarmDesc != ""){
			$.ajax({
				url : "rest/system-alarm-rule/"+e.data.alarmDesc+"?date="+new Date(),
				type : "GET",
				contentType : "application/json;charset=UTF-8",
				success:function(data){
					var desc = data.alarmDesc.replace(new RegExp("\n","gm"),"<br>"/*'\n',"<br>"*/);
					
					e.data.alarmDesc = desc;
					var detailRow = e.detailRow;
					detailRow.find(".tabstrip").kendoTabStrip({
						animation : {
							open : {
								effects : "fadeIn"
							}
						}
					});
					detailRow.find(".tabstrip [name='desc']").html(desc);
				}
			})
		}
	}
	
	function detailInit1(e) {
		if(e.data.alarmDesc != null && e.data.alarmDesc != ""){
			$.ajax({
				url : "rest/system-alarm-rule/"+e.data.alarmDesc+"?date="+new Date(),
				type : "GET",
				contentType : "application/json;charset=UTF-8",
				success:function(data){
					var desc = data.alarmDesc.replace(new RegExp("\n","gm"),"<br>"/*'\n',"<br>"*/);
					
					e.data.alarmDesc = desc;
					var detailRow = e.detailRow;
					detailRow.find(".tabstrip1").kendoTabStrip({
						animation : {
							open : {
								effects : "fadeIn"
							}
						}
					});
					detailRow.find(".tabstrip1 [name='desc']").html(desc);
				}
			})
		}
	}
	


	$('#export').on('click', function() {
		$("#myExportModal").modal("show");
	});

	$('#doExport').on('click', function() {
		var name = $("input[name='alarmCheck']:checked").val();
		if(name == "start"){
			$("#grid").data("kendoGrid").saveAsExcel();
		}else{ 
			$("#grid1").data("kendoGrid").saveAsExcel();
		}
		$("#myExportModal").modal("hide");
	});
	
	var type = $("input[name='alarmCheck']:checked").val();
	if(type == "start"){
		activeFunction();
	}else{ 
		cancelFunction();
	}

});
