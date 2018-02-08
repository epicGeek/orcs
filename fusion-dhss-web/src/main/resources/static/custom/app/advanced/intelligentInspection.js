
var execStateStore = ['执行中','等待执行','执行成功','执行失败'];
var isPassStore = ['不通过','通过'];
var dataSource;
var searchParams={page:0,size:10,jobName:'',startTime:'',endTime:'',sort : "startTime,desc"};
var grid;
function isNotEmpty(value){
	if(value == null || value == "null" || value == ""){
		return "";
	}else{
		return value;
	}
}
//获取日志列表数据
function loadGridList(){
	
	$("#all-error-search").on("click",function(){
		window.location.href= "smartCheckJobResult?time=Math.random()&resultCode=0";
	})
	
	dataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
				url : "rest/smart-check-job/search/recordPageList",
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
					return d._embedded["smart-check-schedule-result"];  //响应到页面的数据
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

		columns: [{ 
			 field: "_links.self.href", title:"_links.self.href", hidden:true
		},{
			width: 30,
			template: "<input type='checkbox' id='#:_links.self.href#' class = 'grid_check_one'/>",
			attributes:{"class": "text-center"},
			title: "<input type='checkbox' class='grid_check_all'/>"
		},{
			field: "jobName",
			template: "<span  title='#:isNotEmpty(jobName)#'>#:isNotEmpty(jobName)#</span>",
			title: "<span  title='方案名称'>方案名称</span>"
		}, {
			field: "jobDesc",
			template: "<span  title='#:isNotEmpty(jobDesc)#'>#:isNotEmpty(jobDesc)#</span>",
			title: "<span  title='方案描述'>方案描述</span>"
		}, {
			field: "startTime",
			template: "<span  title='#:isNotEmpty(startTime)#'>#:isNotEmpty(startTime)#</span>",
			title: "<span  title='执行开始时间'>执行开始时间</span>"
		}, {
			field: "execFlag",
			template: "<span  title='#:execStateStore[execFlag]#'>#:execStateStore[execFlag]#</span>",
			title: "<span  title='执行状态'>执行状态</span>"
		}, {
			field: "amountJob",
			template: "<span  title='#:amountJob#'>#:amountJob#</span>",
			title: "<span  title='巡检单元个数'>巡检单元个数</span>"
		}, {
			field: "errorUnit",
			template: "#if(errorUnit>0){# <b style='color:red;'>#:errorUnit#</b> #}else{# #:errorUnit# #}#",
			title: "<span  title='异常单元个数'>异常单元个数</span>"
			
		}, {
			title: "操作",
			template: "<a onclick='openNeWin(this,1)' class='btn btn-xs btn-warning'> 按任务查看 </a>&nbsp;&nbsp;"+
						"<a onclick='openNeWin(this,2)' class='btn btn-xs btn-info'> 按网元查看 </a>&nbsp;&nbsp;"+
						"<a onclick='downLoadLog(\"#:_links.self.href#\",\"#:jobName#\",\"#:startTime#\")' class='btn btn-primary btn-xs'><i class='glyphicon glyphicon-download-alt'></i> 下载日志</a>",
			width:"300px"
		}],
		dataBound: function(){
			$(".grid_check_all").on("click",function(){
				$("#dataGrid .k-grid-content input[type='checkbox']").prop("checked",$(this).prop("checked"));
				
			});
		}
	}).data("kendoGrid");
	
}

function downLoadLog(link,names,time){
	var id = link.substring(link.lastIndexOf("/")+1);
	names = names.replace(" ","_");
	window.location.href = "downloadAllLog?scheduleId=" + ",'"+id + "'" + "&sessionText=" +( names + time);
}

function openNeWin(d,type){
	var _links_self_href = grid.dataItem($(d).closest("tr"))._links.self.href;
	var id = _links_self_href.substring(_links_self_href.lastIndexOf("/")+1);
    window.location.href="smartCheckJobResult?time=Math.random()&scheduleId="+id+"&type="+type;
}
$(function() {
	
	$("#export").on("click",function(){
		var list = $(".grid_check_one:checked");
		if(list.length != 0){
			var ids = "";
			$.each(list,function(index,item){
				var uri = $(item).attr("id");
				var id = uri.substring(uri.lastIndexOf("/")+1);
				ids += ",'" + id + "'";
			})
			window.location.href = "downloadAllLog?scheduleId=" + ids ;
		}else{
			infoTip({content: "请选择要下载的任务！"});
		}
		
	})
	
	
	kendo.culture("zh-CN");
	
	//加载grid
	loadGridList();
	
	
	$("#startTime").kendoDateTimePicker({
		format:"yyyy-MM-dd HH:mm:ss"
	});
	$("#endTime").kendoDateTimePicker({
			format:"yyyy-MM-dd HH:mm:ss"
	});
	//查询按钮时间
	$('#btn-search').on('click', function() {
		selectVal();
	});
	
	//清空文本数据
	$("#btn-clear").on("click",function(){
		$('#jobName,#startTime,#endTime').val("");
		selectVal();
	})
});

//查询方法
function selectVal(){
	searchParams.jobName = $('#jobName').val();
	searchParams.startTime = $('#startTime').val();
	searchParams.endTime = $('#endTime').val();
	dataSource.read(searchParams);
}
