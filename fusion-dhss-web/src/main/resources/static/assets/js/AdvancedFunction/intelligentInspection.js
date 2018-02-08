kendo.culture("zh-CN");
$(function() {
	/*当前导航*/
	$("#topNavList .navListWrap:eq(2) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(2) ul li:eq(6)").addClass("active");
	
	$("#startTime").kendoDateTimePicker({
		format:"yyyy-MM-dd hh:mm:ss"
	});
	$("#endTime").kendoDateTimePicker({
		format:"yyyy-MM-dd hh:mm:ss"
	});

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
			title: "<span  title='方案名称'>方案名称</span>"
		}, {
			field: "desc",
			template: "<span  title='#:desc#'>#:desc#</span>",
			title: "<span  title='方案描述'>方案描述</span>"
		}, {
			field: "startTime",
			template: "<span  title='#:startTime#'>#:startTime#</span>",
			title: "<span  title='执行开始时间'>执行开始时间</span>"
		}, {
			field: "status",
			template: "<span  title='#:status#'>#:status#</span>",
			title: "<span  title='执行状态'>执行状态</span>"
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
			template: "<a href='intelligentInspection_task.html' class='btn btn-xs btn-warning'> 按任务查看 </a>&nbsp;&nbsp;"+
						"<a href='intelligentInspection_netElement.html' class='btn btn-xs btn-info'> 按网元查看 </a>"
		}]
	});
});

var dataList = [
	{
		name:"时例行维护计划（NTHLRFE-TIMAS）",
		desc:"时例行维护计划（NTHLRFE-TIMAS）",
		startTime:"2015-09-07 18:00:00",
		status:"执行成功",
		unitNum:"1",
		unusualNum:"0"
	}, {
		name:"时例行维护计划（NTHLRFE-TIMAS）",
		desc:"时例行维护计划（NTHLRFE-TIMAS）",
		startTime:"2015-09-07 17:00:00",
		status:"执行成功",
		unitNum:"1",
		unusualNum:"0"
	}, {
		name:"时例行维护计划（NTHLRFE-TIMAS）",
		desc:"时例行维护计划（NTHLRFE-TIMAS）",
		startTime:"2015-09-07 16:00:00",
		status:"执行成功",
		unitNum:"1",
		unusualNum:"0"
	}, {
		name:"日例行维护计划（ONE-NDS）",
		desc:"日例行维护计划（ONE-NDS）",
		startTime:"2015-09-07 06:30:00",
		status:"执行成功",
		unitNum:"120",
		unusualNum:"0"
	}, {
		name:"日例行维护计划（HSSFE）",
		desc:"日例行维护计划（HSSFE）",
		startTime:"2015-09-07 05:30:00",
		status:"执行成功",
		unitNum:"48",
		unusualNum:"26"
	}, {
		name:"日例行维护计划（NTHLRFE）",
		desc:"日例行维护计划（NTHLRFE）",
		startTime:"2015-09-07 04:30:00",
		status:"执行成功",
		unitNum:"120",
		unusualNum:"57"
	}, {
		name:"时例行维护计划（NTHLRFE-TIMAS）",
		desc:"时例行维护计划（NTHLRFE-TIMAS）",
		startTime:"2015-09-07 00:00:00",
		status:"执行成功",
		unitNum:"1",
		unusualNum:"0"
	}, {
		name:"时例行维护计划（NTHLRFE-TIMAS）",
		desc:"时例行维护计划（NTHLRFE-TIMAS）",
		startTime:"2015-09-06 23:00:00",
		status:"执行成功",
		unitNum:"1",
		unusualNum:"0"
	}
];
