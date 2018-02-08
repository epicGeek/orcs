
var isPassStore = ['正常','异常'];
var dataSource;
//条件查询
var searchParams={page:0,size:20,unitId:'',neId:'',unitTypeId:'',neTypeId:'',checkItemId:'',checkItemName:'',scheduleId:'',type:'',resultCode:"",sort:"resultCode,asc"};
function isNotEmpty(value){
	if(value == null || value == "null" || value == ""){
		return "";
	}else{
		return value;
	}
}
var allUnit;
function filterGrid(){
	var filters = [];
	if($("#unitName").val() != "")
    	filters.push({field: "unitName", operator: "contains", value: $("#unitName").val()});
	if($("#unitType").val() != "")
    	filters.push({field: "unitType", operator: "contains", value: $("#unitType").val()});
	if($("#searchName").val() != "")
    	filters.push({field: "checkItemName", operator: "contains", value: $("#searchName").val()});
	if($("#startTime").val() != "")
    	filters.push({field: "startTime", operator: "ge", value: $("#startTime").val()});
	if($("#endTime").val() != "")
    	filters.push({field: "startTime", operator: "lt", value: $("#endTime").val()});
	dataSource.filter(filters);
	dataSource.fetch();
}
$(function() {
	
	$("#export").on("click",function(){
		window.location.href = "smart-check-job/donwload/smartCheckDetailPageList?unitId="+searchParams.unitId
		 +"&neId="+searchParams.neId
		 +"&unitTypeId="+$("#unitType").val()
		 +"&checkItemId="+searchParams.checkItemId
		 +"&checkItemName="+$("#searchName").val()
		 +"&scheduleId="+searchParams.scheduleId
		 +"&resultCode="+searchParams.resultCode
		 +"&unitName="+$("#unitName").val()
		 +"&startTime="+$("#startTime").val()
		 +"&endTime="+$("#endTime").val();
	})
	
	var operateType = $('#type').val();
	searchParams.scheduleId = $('#scheduleId').val();
	if(operateType==1){//巡检
		searchParams.checkItemId = $('#typeId').val();
	}else{//网元
		searchParams.neId = $('#typeId').val();
	}
	searchParams.resultCode = $("#resultCode").val();
	searchParams.type = operateType;
	
	$("#startTime,#endTime").on("change",function(){
		filterGrid();
	})
	
	$("#searchName").on("keyup",function(){
		filterGrid();
	})
	$.ajax({
		url:"rest/equipment-unit",
		dataType:"json",
		success:function(data){
			allUnit = $("#unitName").kendoDropDownList({
				optionLabel:"全部单元",
				dataSource: data["_embedded"] ? data["_embedded"]["equipment-unit"] : [],
				dataTextField: "unitName",
				dataValueField: "unitName",
				filter: "contains",
				suggest: true,
				change:function(){
					
					filterGrid();
				}
			}).data("kendoDropDownList");
		}
	});
    
	//加载grid数据
	initGridData();
	
	
	selectInit();
	
	$("#startTime").kendoDateTimePicker({
		format:"yyyy-MM-dd HH:mm:ss"
	});
	$("#endTime").kendoDateTimePicker({
		format:"yyyy-MM-dd HH:mm:ss"
	});
	
	
	
	
	//查询
	$('#btn-search').on('click', function() {
		inputVal();
	});
	
	$("#btn-clear").on("click",function(){
		$('#searchName').val("");
		$("#unitType").val("");
		inputVal();
	})
	
});

//加载下拉框
function selectInit(){
	$("#unitType").kendoDropDownList();
	$.ajax({
		url:"equipment-unitType/search/list",
		dataType:"json",
		success:function(data){
			$("#unitType").kendoDropDownList({
				optionLabel:"全部单元类型",
				dataSource: data,
				filter: "contains",
				suggest: true,
				change:function(){
					var filters = [];
					if($("#unitType").val() != "")
				    	filters.push({field: "unitType", operator: "contains", value: $("#unitType").val()});
					allUnit.dataSource.filter(filters);
					allUnit.dataSource.fetch();
					filterGrid();
				}
			});
		}
	});
}

function inputVal(){
	searchParams.unitTypeId = $("#unitType").val();
	searchParams.checkItemName = $('#searchName').val();
	dataSource.read(searchParams);
}


function initGridData(){
	
	
	dataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
				url : "rest/smart-check-job/search/smartCheckDetailPageList",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			},
			parameterMap : function(options, operation) {
				if (operation == "read") {
						 searchParams.page = options.page-1;
						 searchParams.size = options.pageSize;
						 searchParams.sort = "resultCode,asc";
						 return searchParams;
				}
			},
		},
		batch : true,
		pageSize : 20, //每页显示个数
		schema : {
			data : function(d) {
					return d;  //响应到页面的数据
			},
			total : function(d) {
				return d.length; //总条数
			},
		},
		serverPaging : false,
		serverFiltering : false,
		serverSorting : false,
		sort: {field: "resultCode", dir: "asc"}
		
	});
	
	
$("#dataGrid").kendoGrid({
		
		dataSource:dataSource,
		height:$(window).height()-$("#dataGrid").offset().top - 50,
		reorderable: false,

		resizable: false,

		sortable: false,

		columnMenu: false,

		pageable: true,

		columns: [

		 { field: "logState", title:"logState", hidden:true},
		 { field: "startTime", title:"startTime", hidden:true},
		 { field: "filePath", title:"filePath", hidden:true},
		 {
			field: "neType",
			template: "<span  title='#:isNotEmpty(neType)#'>#:isNotEmpty(neType)#</span>",
			title: "<span  title='网元类型'>网元类型</span>"
		}, {
			field: "unitType",
			template: "<span  title='#:isNotEmpty(unitType)#'>#:isNotEmpty(unitType)#</span>",
			title: "<span  title='单元类型'>单元类型</span>"
		}, {
			field: "neName",
			template: "<span  title='#:isNotEmpty(neName)#'>#:isNotEmpty(neName)#</span>",
			title: "<span  title='网元名称'>网元名称</span>"
		}, {
			field: "unitName",
			template: "<span  title='#:isNotEmpty(unitName)#'>#:isNotEmpty(unitName)#</span>",
			title: "<span  title='单元名称'>单元名称</span>"
		}, {
			field: "checkItemName",
			template: "<span  title='#:isNotEmpty(checkItemName)#'>#:isNotEmpty(checkItemName)#</span>",
			title: "<span  title='巡检任务名称'>巡检任务名称</span>"
		}, {
			field: "resultCode",
			template: "<span  title='#if(resultCode==false){#异常#}else{#正常#}#'>#if(resultCode==true){#正常#}else{#异常#}#</span>",
			title: "<span  title='巡检结果'>巡检结果</span>"
		},{
			field: "startTime",
			template: "<span  title='#= startTime ? startTime: \'\'#'>#= startTime ? startTime : \'\'#</span>",
			title: "<span  title='执行时间'>执行时间</span>"
		}, {
			field: "errorMessage",
			template: "<span  title='#:isNotEmpty(errorMessage)#'>#:isNotEmpty(errorMessage)#</span>",
			title: "<span  title='异常原因'>异常原因</span>"
		}, {
			title: "操作",
			template:"#if( filePath != null && filePath != 'null' && filePath != ''){if(startTime ==  null){startTime=''}#"
				    +"<a onclick='downLoadLog(\"#:neName#-#:unitName#-#:checkItemName#\",\"#:filePath#\")'"
				    +"class='btn btn-xs btn-warning'><i class='glyphicon glyphicon-download-alt'></i> 下载日志</a>#}#"
			/*template:"#if(LOG_STATE==false){console.log('测试');# <a onclick='downLoadLog(\"#:NE_NAME#-#:UNIT_NAME#-#:CHECK_ITEM_NAME#_#:START_TIME#\",\"#:FILE_PATH#\")'"
				   +" class='btn btn-xs btn-warning'><i class='glyphicon glyphicon-download-alt'></i> 下载日志</a> #}#"*/
		}]
	});
	
}


//详情日志下载
function downLoadLog(logText,filePath){ 
	//console.log(logText+'         '+filePath);
	window.location.href="downloadDetailLog?logText="+logText+"&filePath="+filePath;
}

