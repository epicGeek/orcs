var searchparams = { page : 0 , size : 10 , fileName : "",startTime:"",endTime:"",sort:"createDate,desc"};
function downLoadLog(name,path){
	window.location.href="volte/downloadMonitor?name="+name+"&path="+path;
}
$(function() {
	
	var start = $("#startTime").kendoDateTimePicker({
		format : "yyyy-MM-dd HH:mm:ss",
	}).data("kendoDateTimePicker");

	var end = $("#endTime").kendoDateTimePicker({
		format : "yyyy-MM-dd HH:mm:ss",
	}).data("kendoDateTimePicker");
	
	
	$("#startTime,#endTime").on("change",function(){
		dataGridObj.dataGrid.dataSource.read();
	})
	
	$("#clearsearchalarm").on("click",function(){
		$("#startTime,#endTime,#inputKeyWord").val("");
		dataGridObj.dataGrid.dataSource.read();
	})
	
	
	var dataSource=new kendo.data.DataSource({
        type: "odata",
        transport: {
            read: {
                type: "GET",
                url: "volte/table/search/searchByFilter",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            },
            parameterMap: function(options, operation) {
                if (operation == "read") {       
                    searchparams.page = options.page-1;
                    searchparams.size = options.pageSize;
                    searchparams.fileName = $("#inputKeyWord").val();
                    searchparams.startTime = $("#startTime").val();
                    searchparams.endTime = $("#endTime").val();
                    return searchparams;                   
                }     
            }
        },
        schema: {
        	data: function(d) {  
        		if(d._embedded){
        			return d._embedded["monitor-table"];
        		}
        		return [];
            },
            total: function(d) {
            	return d.page.totalElements;
            }
        },
        pageSize: 20,
        serverPaging: true,
        serverSorting: true
    });
	var dataGridObj = {
		
		init: function(){
			this.dataGrid = $("#dataGrid").kendoGrid({			
				dataSource: dataSource,				
				height: $(window).height()-$("#dataGrid").offset().top - 50,				
				reorderable: true,		
				resizable: true,		
				sortable: true,		
				columnMenu: false,		
				pageable: true,		
				columns: [
				          
				 		 { field: "_links.self.href", title:"_links.self.href", hidden:true},
				 		 {
				 			field: "name",
				 			template: "<span  title='#:name#'>#:name#</span>",
				 			title: "<span  title='文件名称'>文件名称</span>"
				 		}, {
				 			field: "createDate",
				 			template: "<span  title='#:createDate#'>#:createDate#</span>",
				 			title: "<span  title='创建时间'>创建时间</span>"
				 		}, {
				 			title: "操作",
				 			template:"#if( filePath != null && filePath != 'null' && filePath != ''){#"
				 				    +"<a onclick='downLoadLog(\"#:name#\",\"#:filePath#\")'"
				 				    +"class='btn btn-xs btn-warning'><i class='glyphicon glyphicon-download-alt'></i> 下载日志</a>#}#"
				 		}]
			}).data("kendoGrid");
		}
	};
	
	
	dataGridObj.init();
	
	
});

