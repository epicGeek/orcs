
$(function() {
	/*当前导航*/
	$("#topNavList .navListWrap:eq(2) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(2) ul li:eq(6)").addClass("active");
	
	$("#dataGrid").kendoGrid({
		
		dataSource: {
			data: dataList,
			pageSize: 10
		},
		
		reorderable: true,

		resizable: true,

		sortable: true,

		columnMenu: true,

		pageable: true,

		columns: [{
			field: "name",
			template: "<span  title='#:name#'>#:name#</span>",
			title: "<span  title='巡检任务名称'>巡检任务名称</span>"
		}, {
			field: "unitNum",
			template: "<span  title='#:unitNum#'>#:unitNum#</span>",
			title: "<span  title='巡检单元个数'>巡检单元个数</span>"
		}, {
			field: "unusualNum",
			template: "#if(unusualNum>0){# <b style='color:red;'>#:unusualNum#</b> #}else{# #:unusualNum# #}#",
			title: "<span  title='异常单元个数'>异常单元个数</span>"
		}, {
			title: "操作",
			template: "<a class='btn btn-xs btn-warning'><i class='glyphicon glyphicon-download-alt'></i> 下载日志</a>&nbsp;&nbsp;"+
						"<a href='intelligentInspection_taskDetails.html' class='btn btn-xs btn-info'><i class='glyphicon glyphicon-eye-open'></i> 查看详情 </a>"
		}]
	});
});

var dataList = [
	{
		name:"检查Notification端口状态",
		unitNum:"96",
		unusualNum:"0"
	}, {
		name:"查看进程状态",
		unitNum:"120",
		unusualNum:"0"
	}, {
		name:"信令链路组状态检查",
		unitNum:"96",
		unusualNum:"0"
	}, {
		name:"信令路由key状态检查",
		unitNum:"96",
		unusualNum:"0"
	}, {
		name:"系统负荷",
		unitNum:"120",
		unusualNum:"0"
	}, {
		name:"告警检查",
		unitNum:"120",
		unusualNum:"55"
	}, {
		name:"系统时间",
		unitNum:"120",
		unusualNum:"0"
	}, {
		name:"信令链路检查",
		unitNum:"96",
		unusualNum:"0"
	}
];
