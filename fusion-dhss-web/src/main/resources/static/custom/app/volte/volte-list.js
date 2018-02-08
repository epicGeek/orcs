kendo.culture("zh-CN");
var params = {page:0,size:20,startTime:"",endTime:"",type:"",imsi:"",sort:"startTime,desc"};
function transformStatus(code){
	if(code=="1"){
		return "失败";
	}
	if(code=="0"){
		return "成功";
	}
}
function transformAutoProv(code){
	if(code=="1"){
		return "是";
	}
	if(code=="0"){
		return "否";
	}
}
$(function(){
		$("#startTime,#endTime").kendoDateTimePicker({
			format: "yyyy-MM-dd HH:mm:ss"
		});
		
		
		var dataSource1 = new kendo.data.DataSource({
			transport : {
				read : {
					type : "GET",
					url : "volte-list/searchPageList",
					dataType : "json",
					contentType : "application/json;charset=UTF-8"
				},
				parameterMap : function(options, operation) {
					if (operation == "read") {
						params.page = options.page - 1;
						params.size = options.pageSize;
						params.startTime = $("#startTime").val();
						params.endTime = $("#endTime").val();
						params.type = "hss2boss";
						params.imsi = $("#imsi").val();
						return params;
					}
				}
			},
			batch : true,
			schema : {
				data : function(d) {
					return d._embedded ? d._embedded["volte-message"] : [];
				},
				total : function(d) {
					if (d.page) {
						return d.page.totalElements; // 总条数
					}
				},
			},
			pageSize : 20,
			serverPaging : true,
			serverSorting : true
		});
		
		var dataSource2 = new kendo.data.DataSource({
			transport : {
				read : {
					type : "GET",
					url : "volte-list/searchPageList",
					dataType : "json",
					contentType : "application/json;charset=UTF-8"
				},
				parameterMap : function(options, operation) {
					if (operation == "read") {
						params.page = options.page - 1;
						params.size = options.pageSize;
						params.startTime = $("#startTime").val();
						params.endTime = $("#endTime").val();
						params.type = "boss2hss";
						params.imsi = $("#imsi").val();
						return params;
					}
				}
			},
			batch : true,
			schema : {
				data : function(d) {
					return d._embedded ? d._embedded["volte-message"] : [];
				},
				total : function(d) {
					if (d.page) {
						return d.page.totalElements; // 总条数
					}
				},
			},
			pageSize : 20,
			serverPaging : true,
			serverSorting : true
		});
		
		
		
		var dataGridItem1 = $("#resultGrid1").kendoGrid({
	        dataSource: dataSource1,
			height : $(window).height() - $("#resultGrid1").offset().top - 100,
			pageable: true,
			resizable: true,
	        columns: [{
	            field: "imsi",
	            title: "IMSI"
	        }, {
	            field: "msisdn",
	            title: "MSISDN"
	        },{
	        	field : "actionStatus",
				template : "<span  title='#:transformStatus(actionStatus)#'>#:transformStatus(actionStatus)#</span>",
				title : "<span title='开通状态'>开通状态</span>"
	        },{
	            field: "startTime",
	            title: "时间"
	        },{
	            field: "costTime",
	            title: "时差"
	        },{
	            field: "neName",
	            title: "网元"
	        }/*,{
	            field: "autoProv",
				template : "<span  title='#:transformAutoProv(autoProv)#'>#:transformAutoProv(autoProv)#</span>",
				title : "<span title='自动开通'>自动开通</span>"
	        }*/]
	    }).data("kendoGrid");


		var dataGridItem2 = $("#resultGrid2").kendoGrid({
	        dataSource: dataSource2,
			height : $(window).height() - $("#resultGrid2").offset().top - 100,
			pageable: true,
			resizable: true,
	        columns: [{
	            field: "imsi",
	            title: "IMSI"
	        }, {
	            field: "msisdn",
	            title: "MSISDN"
	        },{
	        	field : "actionStatus",
				template : "<span  title='#:transformStatus(actionStatus)#'>#:transformStatus(actionStatus)#</span>",
				title : "<span title='开通状态'>开通状态</span>"
	        },{
	            field: "startTime",
	            title: "时间"
	        },{
	            field: "costTime",
	            title: "时差"
	        },{
	            field: "neName",
	            title: "网元"
	        },{
	            field: "autoProv",
				template : "<span  title='#:transformAutoProv(autoProv)#'>#:transformAutoProv(autoProv)#</span>",
				title : "<span title='自动开通'>自动开通</span>"
	        }]
	    }).data("kendoGrid");


		$("#startTime,#endTime").on("change",function(){
			shuaxin();
		})

		$("#imsi").on("input",function(){
			shuaxin();
		})

		$("#clearsearch").on("click",function(){
			$("#startTime,#endTime,#imsi").val("");
			shuaxin();
		})
		function shuaxin(){
			dataGridItem1.dataSource.read();
			dataGridItem2.dataSource.read();
		}
		
		$("#export").on("click",function(){
			window.location.href="volte-table/export?startTime="+$("#startTime").val()+"&endTime="+$("#endTime").val()+"&imsi="+$("#imsi").val();
		})
})
