//查询条件
var searchObj = {page:1,size:10,searchField:'',sort : "createTime,desc"};
var userDataSource;
$(document).ready(function() {
	getHistoryData();
	
	$('#cond-command').on('keyup', function() {
		searchObj.searchField = $('#cond-command').val();
		userDataSource.read(searchObj);
	});
	
	 $('#btn-user-search').on('click', function() {
		 queryUserData();
	 });
	 
	 //下载用户数据模板
	 $('#btn-demo-down').on('click', function() {
		 window.location.href="userData/downUserDataTemplate";
	 });
	 
	 $('#btn-readExcel').on('click', function() {
		 $('#fileName').click();
	 });
		 
});

//导入excel数据
function uploadExcelFile(obj){
	var extend = obj.value.substring(obj.value.lastIndexOf(".")+1); 
	 if(extend!=""){
		 if(!(extend=="xls")){ 
				showNotify("请上传后缀名为xls的文件!","warning");
			   return false; 
		 }else{
			 ajaxFileUpload();
			 $('#waing').show();
		 }
		 
	 }
}

//文件上传
function ajaxFileUpload() {
    $.ajaxFileUpload({
        url: 'userData/uploadFile',
        type: 'post',
        dataType : "json",
        secureuri: false, //一般设置为false
        fileElementId: 'fileName', // 上传文件的id、name属性名
        success: function(data,status){
        	if(data==0){
        		$('#waing').hide();
        		showNotify("查询数据成功！","success");
        		userDataSource.read(searchObj);
        	}else{
        		$('#waing').hide();
        		showNotify("获取数据失败！","waing");
        	}
        },
        error: function(data){ 
        	showNotify("查询数据失败！","warning");
        }
    });
}

//查询单用户历史数据记录
function getHistoryData(){
	userDataSource = new kendo.data.DataSource({
        transport: {
            read: {
            	type : "GET",
				url : "userData/bacthLog",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
            },
            parameterMap: function (options, operation) {
                if (operation == "read") {
                	//动态组装条件参数
                	searchObj.page = (options.page-1);
                	searchObj.size = options.pageSize;
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
	
	$("#batchUserData").kendoGrid({
		
		  dataSource:userDataSource,
		  height: 400,
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
	                 { field: "numbers", title:"查询号段"},
	                 { field: "createName", title:"操作人"},
                     { title:"操作",
                	   template:'#if(path!="" && null!=path){#'
	                		    +"<button type='button' class='btn btn-info btn-xs'"
	                	        +"onclick='downloadLog(\"#:path#\")'>下载Excel</button>#}#"
	                 }
	                
	               ]
	    });
}



//批量查询保存数据
function queryUserData(){
	
	var contextvalue = $('#contextvalue').val();
	if(contextvalue.length<11){
		showNotify("查询号码长度不能小于11","warning");
		return;
	}
	$('#waing').show();
	$.ajax({
		url : "userData/getBatchUserData/"+contextvalue+"/all",
		type : "get",
		dataType : "json",
		success : function(data) {
			//隐藏提示
			$('#waing').hide();
			if(data==0){
				showNotify("查询数据成功！","success");
				userDataSource.read(searchObj);
			}else{
				showNotify("获取数据失败！","waing");
			}
			
		},
		fail : function(data) {
			showNotify("查询数据失败！","error");
			$('#waing').hide();
		}
	});
		
}
