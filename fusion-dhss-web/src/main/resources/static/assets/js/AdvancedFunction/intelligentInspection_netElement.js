
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
			field: "type",
			template: "<span  title='#:type#'>#:type#</span>",
			title: "<span  title='网元类型'>网元类型</span>"
		}, {
			field: "name",
			template: "<span  title='#:name#'>#:name#</span>",
			title: "<span  title='网元名称'>网元名称</span>"
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
						"<a href='intelligentInspection_netElementDetails.html' class='btn btn-xs btn-info'><i class='glyphicon glyphicon-eye-open'></i> 查看详情 </a>"
		}]
	});
});

var dataList = [
	{
		type:"NTHLRFE",
		name:"BJHSS01FE011BNK",
		unitNum:"12",
		unusualNum:"0"
	}, {
		type:"NTHLRFE",
		name:"BJHSS01FE012BNK",
		unitNum:"11",
		unusualNum:"0"
	}, {
		type:"NTHLRFE",
		name:"BJHSS01FE013BNK",
		unitNum:"12",
		unusualNum:"0"
	}, {
		type:"NTHLRFE",
		name:"BJHSS02FE021BNK",
		unitNum:"15",
		unusualNum:"0"
	}, {
		type:"NTHLRFE",
		name:"BJHSS03FE031BNK",
		unitNum:"11",
		unusualNum:"0"
	}, {
		type:"NTHLRFE",
		name:"BJHSS03FE032BNK",
		unitNum:"12",
		unusualNum:"11"
	}, {
		type:"NTHLRFE",
		name:"BJHSS03FE033BNK",
		unitNum:"11",
		unusualNum:"0"
	}, {
		type:"NTHLRFE",
		name:"BJHSS03FE033BNK",
		unitNum:"12",
		unusualNum:"0"
	}
];
