var dataGrid;

var dataSource;

var searchParams={start:1,length:10,checkItemName:'',type:'',scheduleId:'',neName:''};

var column = [];
function isNotEmpty(value){
	return value == null || value == "null" || value == "" ? "" : value;
}
function loadGrid(){
	dataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
				url : "rest/smart-check-job/search/smartCheckPageList",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			},
			parameterMap : function(options, operation) {
				if (operation == "read") {
						 searchParams.start = options.page;
						 searchParams.length = options.pageSize;
						 return searchParams;
				}
			},
		},
		batch : true,
		pageSize : 20, //每页显示个数
		schema : {
			data : function(d) {
				return d.content;
			},
			total : function(d) {
				return d.totalElements; //总条数
			},
		},
		serverPaging : true,
		serverFiltering : true,
		serverSorting : true,
		sort: {field: "RESULT_CODE", dir: "desc"}
		
	});
	
   dataGrid = $("#dataGrid").kendoGrid({
		
		dataSource:dataSource,
		height: $(window).height()-$("#dataGrid").offset().top - 50,
		reorderable: true,
		resizable: true,
		sortable: false,
		columnMenu: false,
		pageable: true,
		columns: column
	}).data("kendoGrid");
}

$(function() {
	//初始化参数
	searchParams.type = $('#type').val();
	searchParams.scheduleId = $('#scheduleId').val();
	if(searchParams.type==1){//查看任务
		$('#typeName').text("巡检任务名称");
		column = [
        { field: "CHECK_ITEM_ID", title:"CHECK_ITEM_ID", hidden:true},
        { field: "SCHEDULE_ID", title:"SCHEDULE_ID", hidden:true},
		{
			field: "CHECK_ITEM_NAME",
			template: "<span  title='#:isNotEmpty(CHECK_ITEM_NAME)#'>#:isNotEmpty(CHECK_ITEM_NAME)#</span>",
			title: "<span  title='巡检任务名称'>巡检任务名称</span>"
		}, {
			field: "UNIT_AMOUNT",
			template: "<span  title='#:isNotEmpty(UNIT_AMOUNT)#'>#:isNotEmpty(UNIT_AMOUNT)#</span>",
			title: "<span  title='巡检单元个数'>巡检单元个数</span>"
		}, {
			field: "RESULT_CODE",
			template: "#if(RESULT_CODE>0){# <b style='color:red;'>#:RESULT_CODE#</b> #}else{# #:RESULT_CODE# #}#",
			title: "<span  title='异常单元个数'>异常单元个数</span>"
		}, {
			title: "操作",
			template: "<a onclick='downLoadLog(#:CHECK_ITEM_ID#,\"#:CHECK_ITEM_NAME#_#:START_TIME#\",#:SCHEDULE_ID#)' class='btn btn-xs btn-warning'><i class='glyphicon glyphicon-download-alt'></i> 下载日志</a>&nbsp;&nbsp;"+
			          "<a onclick='openNeWin(#:CHECK_ITEM_ID#,#:SCHEDULE_ID#)' class='btn btn-xs btn-info'><i class='glyphicon glyphicon-eye-open'></i> 查看详情 </a>"
		}];
	}else{//网元
		$('#typeName').text("网元名称");
		column = [
		
        { field: "NE_ID", title:"NE_ID", hidden:true},
        { field: "START_TIME", title:"START_TIME", hidden:true},
        { field: "SCHEDULE_ID", title:"SCHEDULE_ID", hidden:true},
		{
			field: "NE_TYPE_NAME",
			template: "<span  title='#:isNotEmpty(NE_TYPE_NAME)#'>#:isNotEmpty(NE_TYPE_NAME)#</span>",
			title: "<span  title='网元类型'>网元类型</span>"
		}, {
			field: "NE_NAME",
			template: "<span  title='#:isNotEmpty(NE_NAME)#'>#:isNotEmpty(NE_NAME)#</span>",
			title: "<span  title='网元名称'>网元名称</span>"
		}, {
			field: "UNIT_AMOUNT",
			template: "<span  title='#:UNIT_AMOUNT#'>#:UNIT_AMOUNT#</span>",
			title: "<span  title='巡检单元个数'>巡检单元个数</span>"
		}, {
			field: "RESULT_CODE",
			template: "#if(RESULT_CODE>0){# <b style='color:red;'>#:RESULT_CODE#</b> #}else{# #:RESULT_CODE# #}#",
			title: "<span  title='异常单元个数'>异常单元个数</span>"
		}, {
			title: "操作",
			template: "<a onclick='downLoadLog(#:NE_ID#,\"#:NE_TYPE#-#:NE_NAME#_#:START_TIME#\",#:SCHEDULE_ID#)' class='btn btn-xs btn-warning'><i class='glyphicon glyphicon-download-alt'></i> 下载日志</a>&nbsp;&nbsp;"+
			"<a onclick='openNeWin(#:NE_ID#,#:SCHEDULE_ID#)' class='btn btn-xs btn-info'><i class='glyphicon glyphicon-eye-open'></i> 查看详情 </a>"
		}];
	}
	
	//加载列表
	loadGrid();
	
	//查询
	$('#btn-search').on('click', function() {
		inputVal();
	});
	
	$("#btn-clear").on("click",function(){
		$('#searchName').val("");
		inputVal();
	})
	
});

function inputVal(){
	if(searchParams.type==1){//查看任务
		searchParams.checkItemName = $('#searchName').val();
	}else{//网元
		searchParams.neName = $('#searchName').val();
	}
	dataSource.read(searchParams);
}

//查看详情
function openNeWin(id,schedule_id){
	  window.location.href= "smartCheckJobResult?time=Math.random()&id="+id+"&type="+searchParams.type+"&scheduleId="+schedule_id;
}


//日志下载
function downLoadLog(ne_id,logText,schedule_id){
	
	if(searchParams.type==1){//查看任务
		window.location.href = "downloadLog?scheduleId="+schedule_id+"&type="+searchParams.type+"&checkItemId="+ne_id+"&sessionText="+logText;
	}else{
		window.location.href = "downloadLog?scheduleId="+schedule_id+"&type="+searchParams.type+"&neId="+ne_id+"&sessionText="+logText;
	}
}
