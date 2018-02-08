//使用基于ES存储的BOSS业务监控JS
//var apiEndPoint = "http://172.16.73.51/"
var apiEndPoint = "http://172.16.73.51:8096/"
kendo.culture("zh-CN");
var kendoDropDownList;
var resetPageNumber = true;
$(function() {
	initDefaultSelectOptionsAndData();
	queryGridData();
	
});	
$('#exportHistoryData').on('click', function() {
//	window.location.href="/boss-rev/exportDataToExcel?" +
	window.location.href= apiEndPoint+"api/cmcc-sh/boss/export/download?" +
	 "resultType="+$("#resultType").val()+
	 "&hlrsn="+$("#hlrsn").val()+
	 "&operationName="+$("#operationName").val()+ 
	 "&errorType="+$("#errorType").val()+ 
	 "&startTime="+ $('#startTime').val()+
	 "&endTime="+$('#endTime').val()+
	 "&numberSerie="+$('#numberSerie').val();
	 
});
function initDefaultSelectOptionsAndData() {
	loadDeviceSelectOption();
	loadDefaultTimePeriod();
	loadOperationName();
	loadErrorType();
	loadResultType();
}
function showChineseResult(engResult){
	if(engResult=="success"){
		return "成功";
	}else if(engResult=="failure"){
		return "失败";
	}else{
		return "未知";
	}
}
function loadResultType(){
	kendoDropDownList = $("#resultType").kendoDropDownList({
		optionLabel : "请选择结果类型",
		dataSource : [{
			"text":"全部",
			"value":"all"
		},{
			"text":"成功",
			"value":"success"
		},{
			"text":"失败",
			"value":"failure"
		}],
		dataTextField : "text",
		dataValueField : "value",
		change : function() {
			if($("#resultType").val()!="failure"){
				var errorCodePicker = $("#errorType").data("kendoDropDownList");
				errorCodePicker.select(0);
			}
		}
	}).data("kendoDropDownList");
}

function searchData(){
	resetPageNumber = true;
	if(resetPageNumber==true){
		$("#bossDataGrid").data("kendoGrid").pager.page(1);
	}
	grid.dataSource.read();
}

var conditionDataSource;
var grid;
//根据查询条件，得到数据源
function queryGridData() {
	var searchParams = {}; 	
	conditionDataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
//				url : "/boss-rev/getBossQueryDataAndCount",
				url : apiEndPoint+"api/cmcc-sh/boss/data",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			},
			parameterMap : function(options, operation) {
				if (operation == "read") {
					searchParams.pageNumber = resetPageNumber==true ? 1 : options.page  ;
					resetPageNumber =  false;
					searchParams.pageSize = options.pageSize;
					searchParams.hlrsn = $("#hlrsn").val();
					searchParams.operationName = $("#operationName").val();
					searchParams.errorType = $("#errorType").val();
					searchParams.startTime = $("#startTime").val();
					searchParams.numberSerie = $("#numberSerie").val();
					searchParams.endTime = $("#endTime").val();
					searchParams.resultType = $("#resultType").val();
					return searchParams;
				}
			}
		},
		batch : true,
		pageSize : 20, // 每页显示个数
		schema : {
			data : function(d) {
				if (d.hits) {
					return d.hits; // 响应到页面的数据
				} else {
					return new Array();
				}
			},
			total : function(d) {
				return d.total;
			},
		},
		serverPaging : true,
		serverFiltering : true,
		serverSorting : true
	});
	grid = {
		init : function() {
			this.bossDataGrid = $("#bossDataGrid").kendoGrid(
						{
							dataSource : conditionDataSource,
							height : $(window).height()
									- $("#bossDataGrid").offset().top - 50,
							width : "100%",
							groupable : false,
							sortable : true,
							resizable : true,
							columnMenu : true,
							reorderable : true,
							pageable : true,
							columns : [
									{
										field : "task_id",
										template : "<span  title='#:task_id#'>#:task_id#</span>",
										title : "<span  title='请求ID'>请求ID</span>"
									},
									{
										field : "response_time",
										template : "<span  title='#:response_time#'>#:response_time#</span>",
										title : "<span  title='响应时间'>响应时间</span>"
									},
									{
										field : "response_status",
										template : "<span  title='#:response_status#'>#:showChineseResult(response_status)#</span>",
										title : "<span  title='执行结果'>执行结果</span>"
									},
									{
										field : "error_code",
										template : "<span  title='#:error_code#'>#:error_code#</span>",
										title : "<span  title='错误码'>错误码</span>"
									},
									{
										field : "error_message",
										template : "<span  title='#:error_message#'>#:error_message#</span>",
										title : "<span  title='错误信息'>错误信息</span>"
									},
									{
										field : "hlrsn",
										template : "<span  title='#:hlrsn#'>#:hlrsn#</span>",
										title : "<span  title='HLRSN'>HLRSN</span>"
									},
									{
										field : "msisdn",
										template : "<span  title='#:msisdn#'>#:msisdn#</span>",
										title : "<span  title='MSISDN'>MSISDN</span>"
									},
									{
										field : "imsi",
										template : "<span  title='#:imsi#'>#:imsi#</span>",
										title : "<span  title='IMSI'>IMSI</span>"
									},
									{
										field : "operation_name",
										template : "<span  title='#:operation_name#'>#:operation_name#</span>",
										title : "<span  title='指令名称'>指令名称</span>"
									},
									{
										template : "<button class='queryBtn btn btn-xs btn-info' id='queryDetailButton' ><i class='glyphicon glyphicon-edit'></i> 查询详情</button>&nbsp;&nbsp;",
										title : "<span  title='操作'>操作</span>"
									} ],
							dataBound : function() {
								grid.showDetail();
							}
						}).data("kendoGrid");
			},
			showDetail: function(){
				$(".queryBtn").on("click", function() {
					var dataItem = grid.bossDataGrid.dataItem($(this).closest("tr"));
					console.log(dataItem);
		        	var bossDetailDataWindow = $("#bossDetailDataWindow");
		        	var queryDetailButton = $("#queryDetailButton");
		        	var callbackResultDiv = $("#callbackResult");
		        	var soapLogDiv = $("#soapLog");
		        	var errLogDiv = $("#errLog");
		        	var callbackResult = dataItem.response_status;
		        	if(callbackResult=="success"){
		        		callbackResult = "成功";
		        	}else if(callbackResult=="failure"){
		        		callbackResult = "失败";
		        	}
		        	var soapLog = dataItem.soap_log;
		        	if(soapLog==undefined){
		        		soapLog = "";
		        	}
		        	var errLog = dataItem.error_log;
		        	if(errLog==undefined){
		        		errLog = "";
		        	}
		        	callbackResultDiv.html("<b>指令下发状态:"+callbackResult+"</b>");
		        	soapLogDiv.html("<b>SOAP日志内容:</b><br><textarea readonly=\"readonly\" rows=\"5\" cols=\"92\">"+soapLog+"</textarea>");
		        	errLogDiv.html("<b>ERR_CASE日志内容:</b><br><textarea readonly=\"readonly\" rows=\"5\" cols=\"92\">"+errLog+"</textarea>");
		        	queryDetailButton.click(function() {
		        		bossDetailDataWindow.data("kendoWindow").open();
		        		
		            });	

		            function onClose() {
		            	queryDetailButton.fadeIn();
		            }

		            bossDetailDataWindow.kendoWindow({
		                width: "700px",
		                title: "BOSS数据详情",
		                visible: false,
		                modal : true,
		                actions: [
		                    "Pin",
		                    "Minimize",
		                    "Maximize",
		                    "Close"
		                ],
		                close: onClose
		            }).data("kendoWindow").center().open();
		        
				});
			}

	} 
	
	grid.init();
}

function loadErrorType() {
	$.ajax({
		url : "/boss-rev/getErrorType",
		type : "GET",
		async:false,
		contentType : "application/json;charset=UTF-8",
		success : function(data) {
			var errorTypes = new Array();
			$.each(data.data, function(index, item) {
				errorTypes[index] = item.error_code;
			});
		    errorTypes.push("其他...");
			kendoDropDownList = $("#errorType").kendoDropDownList({
				optionLabel : "请选择错误类型",
				dataSource : errorTypes,
				filter : "contains",
				suggest : true,
				change : function() {
					if($("#errorType").val()!=""&&$("#errorType").val()!="其他..."){
						var resultTypePicker = $("#resultType").data("kendoDropDownList");
						resultTypePicker.select(3);
					}
					if($("#errorType").val()=="其他..."){
						var errorTypeGridWindow = $("#errorTypeGridWindow");
			            errorTypeGridWindow.kendoWindow({
			                width: "700px",
			                title: "错误类型详情",
			                visible: false,
			                actions: [
			                    "Pin",
			                    "Minimize",
			                    "Maximize",
			                    "Close"
			                ],
			                close: onClose
			            }).data("kendoWindow").center().open();
						errorTypeGridWindow.data("kendoWindow").open();
						function onClose() {
				            	errorTypeGridWindow.fadeIn();
				            	loadErrorType();
				        }
						var errorCodeDataSource = new kendo.data.DataSource({
							transport : {
								read : {
									type : "GET",
									url : "/boss-rev/getErrorTypeOnly",
									dataType : "json",
									contentType : "application/json;charset=UTF-8"
								},
								update: {
                                    url: "/boss-rev/getErrorType" + "/update",
                                    type:"POST"
                                },
                                destroy: {
                                    url: "/boss-rev/getErrorType" + "/destroy",
                                    type:"POST"
                                },
                                create: {
                                    url: "/boss-rev/getErrorType" + "/create",
                                    type:"POST"
                                },
                                parameterMap: function(options, operation) {
                                    if (operation == "create" && options.models) {
                                    	var paramObject = new Object();
                                    	paramObject.errorCode = options.models[0]["error_code"];
                                    	paramObject.errorCodeDesc = options.models[0]["error_code_desc"];
                                        return paramObject;
                                    }
                                    if (operation == "update" && options.models) {
                                    	var paramObject = new Object();
                                    	paramObject.errorCode = options.models[0]["error_code"];
                                    	paramObject.errorCodeDesc = options.models[0]["error_code_desc"];
                                    	paramObject.id = options.models[0]["id"];
                                        return paramObject;
                                    }
                                    if (operation == "destroy" && options.models) {
                                    	var paramObject = new Object();
                                    	paramObject.id = options.models[0]["id"];
                                        return paramObject;
                                    }
                                }
							},
							
							batch : true,
							pageSize : 20, // 每页显示个数
                            schema: {
                                model: {
                                    id: "id",
                                    fields: {
                                        error_code: { validation: { required: true } },
                                        error_code_desc: {  validation: { required: true } }
                                    }
                                }
                            }
						});
						grid = $("#errorTypeGrid")
								.kendoGrid(
										{
											dataSource : errorCodeDataSource,
											height : $(window).height()
													- $("#bossDataGrid").offset().top - 50,
											width : "100%",
											toolbar: ["create"],
											groupable : false,
											sortable : true,
											resizable : true,
											columnMenu : true,
											reorderable : true,
											pageable : true,
											columns : [
													{
														field : "error_code",
														template : "<span  title='#:error_code#'>#:error_code#</span>",
														title : "<span  title='错误代码'>错误代码</span>"
													},
													{
														field : "error_code_desc",
														template : "<span  title='#:error_code_desc#'>#:error_code_desc#</span>",
														title : "<span  title='错误描述'>错误描述</span>"
													},
													 {command: ["edit", "destroy"]}/*,
													{
														template : "<button class='queryBtn btn btn-xs btn-info' id='editErrorType' ><i class='glyphicon glyphicon-edit'></i> 编辑</button>&nbsp;&nbsp;",
														title : "<span  title='编辑'>编辑</span>"
													}*/ ],
											dataBound : function() {
												$('#editErrorType').on('click', function() {
													alert("edit errorType");
												});
											},
										editable: "inline"
										}).data("kendoGrid");
					}
				}
			}).data("kendoDropDownList");
		}
	});
}
function loadOperationName() {
	$.ajax({
		url : "/boss-rev/getOperationName",
		type : "GET",
		async:false,
		contentType : "application/json;charset=UTF-8",
		success : function(data) {
			var operationNames = new Array();
			$.each(data.data, function(index, item) {
				operationNames[index] = item.operation_name;
			});
			operationNames.push("其他...");
			kendoDropDownList = $("#operationName").kendoDropDownList({
				optionLabel : "请选择指令名称",
				dataSource : operationNames,
				filter : "contains",
				suggest : true,
				change : function() {

					if($("#operationName").val()=="其他..."){
						var operationNameGridWindow = $("#operationNameGridWindow");
						operationNameGridWindow.kendoWindow({
			                width: "700px",
			                title: "指令类型-业务类型详情",
			                visible: false,
			                actions: [
			                    "Pin",
			                    "Minimize",
			                    "Maximize",
			                    "Close"
			                ],
			                close: onClose
			            }).data("kendoWindow").center().open();
						operationNameGridWindow.data("kendoWindow").open();
						function onClose() {
							operationNameGridWindow.fadeIn();
							loadOperationName();
				        }
						var operationNameDataSource = new kendo.data.DataSource({
							transport : {
								read : {
									type : "GET",
									url : "/boss-rev/getOperationNameOnly",
									dataType : "json",
									contentType : "application/json;charset=UTF-8"
								},
								update: {
                                    url: "/boss-rev/getOperationName" + "/update",
                                    type:"POST"
                                },
                                destroy: {
                                    url: "/boss-rev/getOperationName" + "/destroy",
                                    type:"POST"
                                },
                                create: {
                                    url: "/boss-rev/getOperationName" + "/create",
                                    type:"POST"
                                },
                                parameterMap: function(options, operation) {
                                    if (operation == "create" && options.models) {
                                    	var paramObject = new Object();
                                    	paramObject.operationName = options.models[0]["operation_name"];
                                    	paramObject.businessType = options.models[0]["business_type"];
                                        return paramObject;
                                    }
                                    if (operation == "update" && options.models) {
                                    	var paramObject = new Object();
                                    	paramObject.operationName = options.models[0]["operation_name"];
                                    	paramObject.businessType = options.models[0]["business_type"];
                                    	paramObject.id = options.models[0]["id"];
                                        return paramObject;
                                    }
                                    if (operation == "destroy" && options.models) {
                                    	var paramObject = new Object();
                                    	paramObject.id = options.models[0]["id"];
                                        return paramObject;
                                    }
                                }
							},
							
							batch : true,
							pageSize : 20, // 每页显示个数
                            schema: {
                                model: {
                                    id: "id",
                                    fields: {
                                    	operation_name: { validation: { required: true } },
                                    	business_type: {  validation: { required: true } }
                                    }
                                }
                            }
						});
						grid = $("#operationNameGrid")
								.kendoGrid(
										{
											dataSource : operationNameDataSource,
											height : $(window).height()
													- $("#bossDataGrid").offset().top - 50,
											width : "100%",
											toolbar: ["create"],
											groupable : false,
											sortable : true,
											resizable : true,
											columnMenu : true,
											reorderable : true,
											pageable : true,
											columns : [
													{
														field : "operation_name",
														template : "<span  title='#:operation_name#'>#:operation_name#</span>",
														title : "<span  title='指令类型'>指令类型</span>"
													},
													{
														field : "business_type",
														template : "<span  title='#:business_type#'>#:business_type#</span>",
														title : "<span  title='业务类型'>业务类型</span>"
													},
													 {command: ["edit", "destroy"]}/*,
													{
														template : "<button class='queryBtn btn btn-xs btn-info' id='editErrorType' ><i class='glyphicon glyphicon-edit'></i> 编辑</button>&nbsp;&nbsp;",
														title : "<span  title='编辑'>编辑</span>"
													}*/ ],
											dataBound : function() {

											},
										editable: "inline"
										}).data("kendoGrid");
					}
				
				}
			}).data("kendoDropDownList");
		}
	});
}
function loadDefaultTimePeriod() {

	$.ajax({
		url : "/boss-rev/getDefaultTimePeriod",
		type : "GET",
		contentType : "application/json;charset=UTF-8",
		async:false,
		success : function(data) {
			var startTime = data.startTime;
			var endTime = data.endTime;
			$("#startTime").kendoDateTimePicker({
				format : "yyyy-MM-dd HH:mm:ss",
				value : startTime
			});
			$("#endTime").kendoDateTimePicker({
				format : "yyyy-MM-dd HH:mm:ss",
				value : endTime
			});
		}
	});

}
function loadDeviceSelectOption() {
	kendoDropDownList = $("#hlrsn").kendoDropDownList({
		optionLabel : "请选择HLRSN",
		dataSource : ["50","51","52","53"],
		filter : "contains",
		suggest : true,
		change : function() {
			
		}
	}).data("kendoDropDownList");
}