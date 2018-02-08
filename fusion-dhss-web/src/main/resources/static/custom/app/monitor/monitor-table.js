
var dataSource;
var searchParams={page:0,size:20,fileName:'',startTime:'',endTime:'',sort : "id,desc"};
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
				url : "monitor/table/search/searchByFilter",
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
				console.log(d._embedded);
				if(d._embedded){
					return d._embedded["monitor-table"];  //响应到页面的数据
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
			field: "name",
			template: "<span  title='#:isNotEmpty(name)#'>#:isNotEmpty(name)#</span>",
			title: "<span  title='文件名称'>文件名称</span>"
		}, {
			field: "createDate",
			template: "<span  title='#:isNotEmpty(createDate)#'>#:isNotEmpty(createDate)#</span>",
			title: "<span  title='创建时间'>创建时间</span>"
		}, {
			title: "操作",
			template:"#if( filePath != null && filePath != 'null' && filePath != ''){#"
				    +"<a onclick='downLoadLog(\"#:name#\",\"#:filePath#\")'"
				    +"class='btn btn-xs btn-warning'><i class='glyphicon glyphicon-download-alt'></i> 下载日志</a>#}#"
		}]
	}).data("kendoGrid");
	
}

function downLoadLog(name,path){
	window.location.href="downloadMonitor?name="+name+"&path="+path;
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
		$('#fileName,#startTime,#endTime').val("");
		selectVal();
	})
});

//查询方法
function selectVal(){
	searchParams.fileName = $('#fileName').val();
	searchParams.startTime = $('#startTime').val();
	searchParams.endTime = $('#endTime').val();
	dataSource.read(searchParams);
}
