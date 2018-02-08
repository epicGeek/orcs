
//查询条件
var searchObj = {page:1,size:10,searchField:'',sort : "createTime,desc"};

var dialogObj;

var isUnitHide = true;

var userDataSource;
//初始化列表
function soapGridData(){
	userDataSource = new kendo.data.DataSource({
        transport: {
            read: {
            	type : "GET",
				url : "userData/searchLog",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
            },
            parameterMap: function (options, operation) {
                if (operation == "read") {
                	//动态组装条件参数
                	searchObj.page = (options.page-1);
                	searchObj.size = options.pageSize;
                	searchObj.startTime = $("#startTime").val();
                	searchObj.endTime = $("#endTime").val();
                	console.log(searchObj);
                	//searchObj.searchField = $('#searchName').val();
                    return searchObj;
                }
            }
        },
        batch: true,
        pageSize: 10,
        schema : {
			data : function(d) {
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
	
	$("#soapList").kendoGrid({
		
		  dataSource:userDataSource,
		  height: 380,
		  groupable: false,
	      sortable: true,
	      resizable: true,
	      columnMenu: true,
	      reorderable: true,
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
	        columns:[
                     {field:"path",hidden:true},
	                 { width:160,field: "createTime", title:"日志时间"},
	                 { field: "numberSection", title:"查询号段"},
	                 { field: "createName", title:"操作人"},
	                 { field: "unitName", title:"单元名称"},
                     { title:"操作",
                	   template:'#if(path!="" && null!=path){#'
	                		    +"<button type='button' class='btn btn-info btn-xs'"
	                	        +"onclick='downloadLog(\"#:path#\")'>下载XML</button>&nbsp;&nbsp;&nbsp;"
	                	        +"<button type='button' class='btn btn-info btn-xs'"
		                	    +"onclick='openUserData(\"#:path#\",\"#:numberSection#\",\"#:unitName#\",0)'>打&nbsp;&nbsp;开</button>#}#"
	                 }
	                
	               ]
	    });
}


function openWindow(id,h,w,titleName){
	
	$("#"+id).kendoWindow({
		  height: h+'px',
		  width:w+'px',
		  title:titleName
	});
	dialogObj = $("#"+id).data("kendoWindow");
	dialogObj.open();dialogObj.center();
}

$(document).ready(function() {
	
	$("#startTime").kendoDateTimePicker({
		format:"yyyy-MM-dd HH:mm:ss"
	});
	$("#endTime").kendoDateTimePicker({
		format:"yyyy-MM-dd HH:mm:ss"
	});
	
	//窗口初始化列
	$('#histryId').on('click', function() {
		//弹出窗口
		openWindow('logModal','440','740','历史查询记录');
		soapGridData();
	 });
	
	//条件查询
	$('#searchName').on('keyup', function() {
		searchObj.searchField = $('#searchName').val();
		//console.log(JSON.stringify(searchObj));
		userDataSource.read(searchObj);
	});
	
	$("#startTime,#endTime").on('change', function(){
		searchObj.searchField = $('#searchName').val();
		userDataSource.read(searchObj);
	});
	
	$("#startTime,#endTime").on('keyup', function(){
		searchObj.searchField = $('#searchName').val();
		userDataSource.read(searchObj);
	});
	
	 $('#btn-search').on('click', function() {
		 queryUserData();
	 });
	 
	//针对用户数据管理跳转参数查询
	var number = $('#number').val();
	//console.log(number);
	if(number!=null && number!=""){
		
		var r = number.match("^86.*");
		var type = "";
		if(null!=r){
			$('input[name="radio1"][value="1"]').attr('checked', true);
			type = "1";
		}else{
			type = "2";//IMSI
			$('input[name="radio1"][value="2"]').attr('checked', true);
		}
		$('#contextvalue').val(number);
		queryData(type,number);
	}
		 
});


function queryUserData(){
	
		var contextvalue = $('#contextvalue').val();
		var type = $('input:radio:checked').val();
		var ratext = "";
		if(type==2){
			ratext = 'IMSI';
			if(""==contextvalue ||null==contextvalue){
				showNotify(ratext+"值不能为空","warning");
				return;
			}else if(contextvalue.length<14){
				
				showNotify(ratext+"值长度不能小于14","warning");
				return;
			}
		}else if(type==1){
			ratext = 'MSISDN';
			if(""==contextvalue ||null==contextvalue){
				showNotify(ratext+"值不能为空","warning");
				return;
			}else if(contextvalue.length<11){
				
				showNotify(ratext+"值长度不能小于11","warning");
				return;
			}
		}else{
			showNotify("请选择查询号段类型","warning");
			return;
		}
		
		htmlClear();
		
		//查询用户数据
		queryData(type,contextvalue);
}

