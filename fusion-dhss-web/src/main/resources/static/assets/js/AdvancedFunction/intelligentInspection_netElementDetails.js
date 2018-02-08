
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
			field: "netType",
			template: "<span  title='#:netType#'>#:netType#</span>",
			title: "<span  title='网元类型'>网元类型</span>"
		}, {
			field: "unitType",
			template: "<span  title='#:unitType#'>#:unitType#</span>",
			title: "<span  title='单元类型'>单元类型</span>"
		}, {
			field: "netName",
			template: "<span  title='#:netName#'>#:netName#</span>",
			title: "<span  title='网元名称'>网元名称</span>"
		}, {
			field: "unitName",
			template: "<span  title='#:unitName#'>#:unitName#</span>",
			title: "<span  title='单元名称'>单元名称</span>"
		}, {
			field: "taskName",
			template: "<span  title='#:taskName#'>#:taskName#</span>",
			title: "<span  title='巡检任务名称'>巡检任务名称</span>"
		}, {
			field: "result",
			template: "<span  title='#:result#'>#:result#</span>",
			title: "<span  title='巡检结果'>巡检结果</span>"
		}, {
			field: "reason",
			template: "<span  title='#:reason#'>#:reason#</span>",
			title: "<span  title='异常原因'>异常原因</span>"
		}, {
			title: "操作",
			template: "<a class='btn btn-xs btn-warning'><i class='glyphicon glyphicon-download-alt'></i> 下载日志</a>"
		}]
	});
});

var dataList = [
	{
		netType:"NTHLRFE",	
		unitType:"PCC",
		netName:"BJHSS01FE012BNK",
		unitName:"h1nt2pcc201",
		taskName:"信令路由组状态检查(PCC)",
		result:"异常",
		reason:"巡检任务执行超过3分钟"
	}, {
		netType:"NTHLRFE",	
		unitType:"PCC",
		netName:"BJHSS01FE012BNK",
		unitName:"h1nt2pcc202",
		taskName:"信令路由组状态检查(PCC)",
		result:"异常",
		reason:"巡检任务执行超过3分钟"
	}, {
		netType:"NTHLRFE",	
		unitType:"PCC",
		netName:"BJHSS01FE012BNK",
		unitName:"h1nt2pcc203",
		taskName:"信令路由组状态检查(PCC)",
		result:"异常",
		reason:"巡检任务执行超过3分钟"
	}, {
		netType:"NTHLRFE",	
		unitType:"PCC",
		netName:"BJHSS01FE012BNK",
		unitName:"h1nt2pcc204",
		taskName:"信令路由组状态检查(PCC)",
		result:"异常",
		reason:"巡检任务执行超过3分钟"
	}, {
		netType:"NTHLRFE",	
		unitType:"NTHLRFE",
		netName:"BJHSS01FE012BNK",
		unitName:"h1nt2pcc205",
		taskName:"网络接口运行状态",
		result:"异常",
		reason:"巡检任务执行超过3分钟"
	}, {
		netType:"NTHLRFE",	
		unitType:"NTHLRFE",
		netName:"BJHSS01FE012BNK",
		unitName:"h1nt2pcc206",
		taskName:"网络接口运行状态",
		result:"异常",
		reason:"巡检任务执行超过3分钟"
	}
];
