kendo.culture("zh-CN");
var dataGridObj = {};
var windowTemplate = {};
var kpiItemKendoDS = {};
var outPutField = [];
var links;
var searchParams = {
		page : 0,
		size : 20,
		sort : "kpiCode,Desc",

	};
function addClick() {
	
	$("#addBtn").on("click", function() {
		var addItem = {
				kpiName:"",
				kpiCode:"",
				dataSourceName:"",
				outPutField:"",
				kpiCategory:"",
				compareMethod:"",
				monitorTimeString:"",
				requestSample:"",
				threshold:"",
				thresholdCancel:"",
				kpiQueryScript:"",
		};
		windowTemplate.obj.setOptions({
			"title" : "添加"
		});

		windowTemplate.initContent(addItem);
		
	})
}
$(function() {
	
	loadDataGrid();
	searchTextAndClear();
	windowTemplate = {
			obj : undefined,
			template : undefined,
			id : $("#windowTemplate"),
			initContent : function(dataItem) {
				links = dataItem._links?dataItem._links.self.href:""
				windowTemplate.obj.content(windowTemplate.template(dataItem));
				windowTemplate.saveClick();
				windowTemplate.cancelClick();
				windowTemplate.obj.center().open();
				
			},
			init : function() {
				this.template = kendo.template($("#windowTemplate").html());
				if (!windowTemplate.id.data("kendoWindow")) {
					windowTemplate.id.kendoWindow({
						width : "700px",
						actions : [ "Close" ],
						modal : true,
						title : "",
						height : "500px"
					});
				}
				windowTemplate.obj = windowTemplate.id.data("kendoWindow");
			},
			cancelClick : function() {
				$("#cancelBtn").on("click", function() {
					windowTemplate.obj.close();
				});
			},
			saveClick : function() {
				$("#saveBtn").on("click",function(){
					 $.ajax({
				    		url :  links?links:"/rest/kpi-item",
				    		type : links?"PATCH":"POST",
				    		data: kendo.stringify({
				    			kpiName:$("#kpiName").val(),
				    			kpiCode:$("#kpiCode").val(),
				    			dataSourceName:$("#dataSourceName").val(),
				    			outPutField:$("#outPutField").val(),
				    			kpiCategory:$("#kpiCategory").val(),
				    			compareMethod:$("#compareMethod").val(),
				    			monitorTimeString:$("#monitorTimeString").val(),
				    			requestSample:$("#requestSample").val(),
				    			threshold:$("#threshold").val(),
				    			thresholdCancel:$("#thresholdCancel").val(),
				    			kpiQueryScript:$("#kpiQueryScript").val(),
				    			}),
				    		dataType: "json",
				            contentType: "application/json;charset=UTF-8",
				    		success : function(data) {
				    			windowTemplate.obj.close();
				        		 infoTip({content: "保存成功！",color:"#D58512"});
				        		 kpiItemKendoDS.read(searchParams);
				    		}
					 });
				})
			
			}
		};
		windowTemplate.init();
});




function killNull(string) {
	// 不显示NULL
	if (null == string || string == "" || string == "null") {
		return "";
	} else {
		return string;
	}
}
function searchTextAndClear(){
	$('#inputKeyWord').on('keyup', function(event) {
		var filters = [];
		if ($("#inputKeyWord").val() != "") {
			filters.push({
				field : "kpiName",
				operator : "contains",
				value : $("#inputKeyWord").val()
			});
		}
		kpiItemKendoDS.filter(filters);
		kpiItemKendoDS.fetch();
	});
	$('#clearsearch').on('click', function(event) {
		$("#inputKeyWord").val("");
		var filters = [];
		kpiItemKendoDS.filter(filters);
		kpiItemKendoDS.fetch();
	});
}
function loadDataGrid() {
	kpiItemKendoDS = new kendo.data.DataSource({
		type:"odata",
		transport : {
			read : {
				type : "GET",
				url : "rest/kpi-item",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			},
			parameterMap : function(options, operation) {

			},
		},
		batch : true,
		pageSize : 20, // 每页显示个数
		schema : {
			data : function(d) {
				if (d._embedded) {
					var kpiItem = d._embedded["kpi-item"];
					$.each(kpiItem,function(index,item){
						outPutField+=kpiItem[index]["outPutField"];
					});
					return d._embedded["kpi-item"]; // 响应到页面的数据
				} else {
					return new Array();
				}
			},
			total : function(d) {
				return d._embedded["kpi-item"].length; // 总条数
			},
		},
		serverPaging : false,
		serverFiltering : false,
		serverSorting : false
	});

	var searchParams = {
		page : 0,
		size : 20,
		sort : "kpiCode,Desc",

	};
	dataGridObj = {
		init : function() {
			this.dataGrid = $("#dataGrid")
					.kendoGrid(
							{
								dataSource : kpiItemKendoDS,
								height : $(window).height()
										- $("#dataGrid").offset().top - 50,
								width : "100%",
								reorderable : true,
								resizable : true,
								sortable : true,
								columnMenu : false,
								pageable : true,
								columns : [
										{
											field : "_links.self.href",
											title : "_links.self.href",
											hidden : true
										},
										{
											field : "kpiCode",
											template : "<span  title='#:killNull(kpiCode)#'>#:killNull(kpiCode)#</span>",
											title : "<span  title='KPI代号'>KPI代号</span>"
										},
										{
											field : "kpiName",
											template : "<span  title='#:killNull(kpiName)#'>#:killNull(kpiName)#</span>",
											title : "<span  title='KPI名称'>KPI名称</span>",
											locked : true,
											width : 200

										},
										{
											title : "<span  title='操作'>操作</span>",
											template : "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 修改</button>&nbsp;&nbsp;"
													+ "<button class='deleteBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon-remove-sign'></i> 删除</button>&nbsp;&nbsp;",
											locked : true,
											width : 160
										},
										{
											field : "kpiQueryScript",
											template : "<span  title='#:killNull(kpiQueryScript)#'>#:killNull(kpiQueryScript)#</span>",
											title : "<span  title='KPI提取公式'>KPI提取公式</span>",
											width : 200
										},
										{
											field : "outPutField",
											template : "<span  title='#:killNull(outPutField)#'>#:killNull(outPutField)#</span>",
											title : "<span  title='KPI类型'>KPI类型</span>",
											width : 200
										},
										{
											field : "compareMethod",
											template : "<span  title='#:killNull(compareMethod)#'>#:killNull(compareMethod)#</span>",
											title : "<span  title='关系符号'>关系符号</span>",
											width : 200
										},
										{
											field : "monitorTimeString",
											template : "<span  title='#:killNull(monitorTimeString)#'>#:killNull(monitorTimeString)#</span>",
											title : "<span  title='监控时间'>监控时间</span>",
											width : 200
										},
										{
											field : "requestSample",
											template : "<span  title='#:killNull(requestSample)#'>#:killNull(requestSample)#</span>",
											title : "<span  title='样本基数'>样本基数</span>",
											width : 200
										},
										{
											field : "threshold",
											template : "<span  title='#:killNull(threshold)#'>#:killNull(threshold)#</span>",
											title : "<span  title='门限值'>门限值</span>",
											width : 200
										},
										{
											field : "thresholdCancel",
											template : "<span  title='#:killNull(thresholdCancel)#'>#:killNull(thresholdCancel)#</span>",
											title : "<span  title='取消告警门限值'>取消告警门限值</span>",
											width : 200
										} ],
								dataBound : function() {
									dataGridObj.deleteClick();
									addClick();
									dataGridObj.editClick();

								}
							}).data("kendoGrid");
		},
		deleteClick : function() {
			$(".deleteBtn").on(
					"click",
					function() {
						var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
						if (confirm("是否确定删除‘"+dataItem.kpiName+"’?")) {
							
							$.ajax({
								url : dataItem._links.self.href,
								type : "delete",
								success : function(data) {
									infoTip({
										content : "删除成功！"
									});
									kpiItemKendoDS.read(searchParams);
								}
							});
						}
					})
		},

		editClick : function() {
			$(".editBtn").on("click", function() {
				var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
				console.log(dataItem);
				windowTemplate.obj.setOptions({
					"title" : "修改"
				});

				windowTemplate.initContent(dataItem);
			})
		}

	};
	dataGridObj.init();

}