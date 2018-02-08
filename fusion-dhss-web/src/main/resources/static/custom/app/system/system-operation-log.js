
var dataSource;
var searchParams={page:0,size:20,userName:'',startTime:'',endTime:'',sort : "id,desc"};
var grid;
function isNotEmpty(value){
	if(value == null || value == "null" || value == ""){
		return "";
	}else{
		return value;
	}
}

function splitUserName(text){
	if(isNotEmpty(text) == ""){
		return "";
	}else{
		return text.split("@")[0];
	}
}
//获取日志列表数据
function loadGridList(){
	
	dataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
				url : "rest/system-operation-log/search/searchByFilter",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			},
			parameterMap : function(options, operation) {
				if (operation == "read") {
						 searchParams.page = options.page-1;
						 searchParams.size = options.pageSize;
						 console.log(searchParams);
						 return searchParams;
				}
			},
		},
		batch : true,
		pageSize : 20, //每页显示个数
		schema : {
			data : function(d) {
				//console.log(d.content);
				if(d._embedded){
					return d._embedded["system-operation-log"];  //响应到页面的数据
				}else{
					return new Array();
				}
			},
			total : function(d) {
				return d.page.totalElements; //总条数
			},
		},
		serverPaging : true,
		serverFiltering : true,
		serverSorting : true
		
	});
	grid = $("#dataGrid").kendoGrid({
		
		dataSource: dataSource,
		height: $(window).height()-$("#dataGrid").offset().top - 50,
		reorderable: true,

		resizable: true,

		sortable: false,

		columnMenu: false,

		pageable: true,

		columns: [
		          
		 { field: "_links.self.href", title:"_links.self.href", hidden:true},
		 {
			field: "app",
			template: "<span  title='#:isNotEmpty(app)#'>#:isNotEmpty(app)#</span>",
			title: "<span  title='项目名称'>项目名称</span>"
		}, {
			field: "appModule",
			template: "<span  title='#:isNotEmpty(appModule)#'>#:isNotEmpty(appModule)#</span>",
			title: "<span  title='模块'>模块</span>"
		}, {
			field: "logTime",
			template: "<span  title='#:isNotEmpty(logTime)#'>#:isNotEmpty(logTime)#</span>",
			title: "<span  title='时间'>时间</span>"
		}, {
			field: "opText",
			template: "<span  title='#:isNotEmpty(opText)#'>#:isNotEmpty(opText)#</span>",
			title: "<span  title='操作'>操作</span>"
		}, {
			field: "loginUserName",
			template: "<span  title='#:splitUserName(loginUserName)#'>#:splitUserName(loginUserName)#</span>",
			title: "<span  title='用户名称'>用户名称</span>"
		}]
	}).data("kendoGrid");
	
}

$(function() {
	kendo.culture("zh-CN");
	
	//加载grid
	loadGridList();
	
	$("#startTime").kendoDateTimePicker({
		format:"yyyy-MM-dd hh:mm:ss"
	});
	$("#endTime").kendoDateTimePicker({
		format:"yyyy-MM-dd hh:mm:ss"
	});
	
	//查询按钮时间
	$('#btn-search').on('click', function() {
		selectVal();
	});
	
	//清空文本数据
	$("#btn-clear").on("click",function(){
		$('#userName,#startTime,#endTime').val("");
		selectVal();
	})
});

//查询方法
function selectVal(){
	searchParams.userName = $('#userName').val();
	searchParams.startTime = $('#startTime').val();
	searchParams.endTime = $('#endTime').val();
	dataSource.read(searchParams);
}
