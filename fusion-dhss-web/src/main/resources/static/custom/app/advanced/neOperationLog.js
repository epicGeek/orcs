//网元类型与单元类型映射关系
var neTypeMap = [];
var typeRelDataSource;
var searchParams={searchField:'',neType:'',unitType:'',neStratTime:'',neEndTime:''};
//获取日志列表数据
function loadNeLogList(){
	
	 dataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
				url : "ne_log/search",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			},
			parameterMap : function(options, operation) {
				if (operation == "read") {
					    searchParams.page = options.page-1;
					    searchParams.size = options.pageSize;
						searchParams.searchField = $("#neSearch").val();
						console.log(searchParams);
						return searchParams;
				}
			},
		},
		batch : true,
		pageSize : 10, //每页显示个数
		schema : {
			data : function(d) {
				//console.log(d.content);
				return d.content;  //响应到页面的数据
			},
			total : function(d) {
				return d.totalElements; //总条数
			},
		},
		serverPaging : true,
		serverFiltering : true,
		serverSorting : true
		
	});
	
	$("#neLogList").kendoGrid({
		
		dataSource:dataSource,
		height: 401,
		groupable: false,
		sortable: false,
		reorderable: false,
		columnMenu: false,
		resizable: true,
		pageable: {
			  refresh: true,
	    	  pageSizes: true,
	    	  buttonCount: 5,
	    	  pageSizes: [10, 20, 30],
	    	  messages: {
	    	  display: "显示 {0}-{1} 共 {2} 项",
	    	  empty: "没有数据",
	    	  itemsPerPage: "每面显示数量",
	    	  first: "第一页",
	    	  last: "最后一页",
	    	  next: "下一页",
	    	  previous: "上一页"
	    	 }
		},
		columns: [
		          { field: "neType", title:"网元类型"},
		          { field: "neName", title:"网元名称" },
		          { field: "unitType", title:"单元类型"},
		          { field: "unitName", title:"单元名称"},
		          { field: "giveTime", title:"获取时间"},
		          { field: "path",hidden:true},
		          { title:"下&nbsp;&nbsp;&nbsp;载",
                	template: '#if(path!=null && path!=""){#'
                		    + "<button style=''  type='button' class='btn btn-success btn-xs'"
                		    + "onclick='downLoadLog(\"#:path#\")'>下&nbsp;&nbsp;载</button>"
                		    + "#}#"
                    }
		          
		         ]
	});
	
}

$(function($) {
	
	 kendo.culture("zh-CN");
	 //加载列表数据
	 loadNeLogList();
	 
	// 时间选择查询 
	 $("#neStratTime").kendoDateTimePicker({
	       value:new Date(),
           format: "yyyy-MM-dd HH:mm:ss",
           parseFormats: ["yyyy-MM-dd", "HH:mm:ss"]
	   });
	 $("#neEndTime").kendoDateTimePicker({
	       value:new Date(),
           format: "yyyy-MM-dd HH:mm:ss",
           parseFormats: ["yyyy-MM-dd", "HH:mm:ss"]
	 });
	
	//绑定查询按钮事件
	$('#btn-search').on('click', function() {
		searchParams.neStratTime = $('#neStratTime').val();
		searchParams.neEndTime = $('#neEndTime').val();
		dataSource.read(searchParams);
		
	});
	
	$("#neSearch").on('keyup', function(){
		searchParams.searchField = $('#neSearch').val();
		dataSource.read(searchParams);
	});
	
	initNeTypeAndUnitTypeMap();
	
	//加载网元类型
	neTypeList();
	
	//单元类型
	getUnitType();
});

//点击下载日志
function downLoadLog(path){
	//var tr = $(d.target).closest("tr");
	//var data = this.dataItem(tr);
	window.location.href = "ne_log/downLog?unitNamelog=" +path;
}

function neTypeList(){
	
	var neTypeDataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: "equipment-neType/search/list",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            }
        }
	});
	
	$("#neType").kendoDropDownList({
		optionLabel:"--全部网元类型--",
		dataSource:neTypeDataSource,
		filter: "contains",
		suggest: true,
		//index:1,
		change: function(e) {
			searchParams.neType=this.value();
			//刷新grid
			dataSource.read(searchParams);
			getUnitType( this.value());
		}
	});
	
}

//初始化对应关系
function initNeTypeAndUnitTypeMap(){
	
	$.ajax({
		type: "GET",
        url: "rest/equipment-type-rel-ne-unit",
        dataType: "json",
        contentType: "application/json;charset=UTF-8",
		success : function(data) {
			neTypeMap = data._embedded["equipment-type-rel-ne-unit"];
		},
		fail : function(data) {
			showNotify(data.message,"error");
		}
	});
	
	
	
}

//获取单元类型下拉
function getUnitType(netype){
	
	var unitType = [];
	
	if(netype){
		$.each(neTypeMap,function(index,item){
			if(item.neType==netype){
				unitType.push(item.unitType);
			}
		});
	}
	
	//单元类型 下拉
	$("#unitType").kendoDropDownList({
		optionLabel:"--请选择单元类型--",
		dataSource: unitType,
		filter: "contains",
		change: function(e) {
			searchParams.unitType=this.value();
			//刷新grid
			dataSource.read(searchParams);
		}
	});
	
}
