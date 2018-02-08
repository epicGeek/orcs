var searchparams = { page : 0 , size : 10 , dhssName : "",startTime:"",endTime:"",sort:"startTime,desc"};
var kendoDropDownList;
$(function() {
	
	var start = $("#startTime").kendoDateTimePicker({
		format : "yyyy-MM-dd HH:mm:ss",
	}).data("kendoDateTimePicker");

	var end = $("#endTime").kendoDateTimePicker({
		format : "yyyy-MM-dd HH:mm:ss",
	}).data("kendoDateTimePicker");
	
	
	$.ajax({
		url : "volte-list/getDhssName",
		type : "GET",
		dataType: "json",
        contentType: "application/json;charset=UTF-8",
		success : function(data) {
			kendoDropDownList = $("#inputKeyWord").kendoDropDownList({
				optionLabel:"全部网元类型",
				dataSource : data,
				filter : "contains",
				suggest : true,
				change:function(){
					dataGridObj.dataGrid.dataSource.read();
				}
			}).data("kendoDropDownList");
		}
	});
	
	$("#startTime,#endTime").on("change",function(){
		dataGridObj.dataGrid.dataSource.read();
	})
	
	$("#clearsearchalarm").on("click",function(){
		$("#startTime,#endTime").val("");
		kendoDropDownList.select(0);
		dataGridObj.dataGrid.dataSource.read();
	})
	
	
	var dataSource=new kendo.data.DataSource({
        type: "odata",
        transport: {
            read: {
                type: "GET",
                url: "volte-alarm/search-page",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            },
            parameterMap: function(options, operation) {
                if (operation == "read") {       
                    searchparams.page = options.page-1;
                    searchparams.size = options.pageSize;
                    searchparams.dhssName = $("#inputKeyWord").val();
                    searchparams.startTime = $("#startTime").val();
                    searchparams.endTime = $("#endTime").val();
                    return searchparams;                   
                }     
            }
        },
        schema: {
        	data: function(d) {  
        		if(d._embedded){
        			return d._embedded["alarm-monitor"];
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
				columns: [{
					field: "alarmContent",
					template: "<span  title='#:alarmContent#'>#:alarmContent#</span>",
					title: "<span  title='告警内容'>告警内容</span>"
				},{
					field: "alarmTitle",
					template: "<span  title='#:alarmTitle#'>#:alarmTitle#</span>",
					title: "<span  title='告警标题'>告警标题</span>"
				},{
					field: "belongSite",
					template: "<span  title='#:belongSite#'>#:belongSite#</span>",
					title: "<span  title='上级网元'>上级网元</span>"
				},{
					field: "startTime",
					template: "<span  title='#:startTime#'>#:startTime#</span>",
					title: "<span  title='告警时间'>告警时间</span>"
				}]
			}).data("kendoGrid");
		}
	};
	
	
	dataGridObj.init();
	
	
});

