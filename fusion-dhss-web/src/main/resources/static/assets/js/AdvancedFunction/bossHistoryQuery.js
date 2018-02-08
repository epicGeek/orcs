kendo.culture("zh-CN");
$(function() {
	/*当前导航*/
	$("#topNavList .navListWrap:eq(2) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(2) ul li:eq(1)").addClass("active");
	
	
	//查询条件
	$("#startDateTime").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:MM"
	});
	$("#endDateTime").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:MM"
	});
	
	$("#dataGrid").kendoGrid({
		
		dataSource: {
			data:[],
			pageSize:10
		},
		
		height: $(window).height()-$("#dataGrid").offset().top - 50,
		
		reorderable: true,

		resizable: true,

		sortable: true,

		columnMenu: true,

		pageable: true,

		columns: [{
			field: "ID",
			template: "<span  title='#:ID#'>#:ID#</span>",
			title: "<span  title='ID'>ID</span>"
		}, {
			field: "ip",
			template: "<span  title='#:IMSI#'>#:IMSI#</span>",
			title: "<span  title='IMSI'>IMSI</span>"
		}, {
			field: "MSISDN",
			template: "<span  title='#:MSISDN#'>#:MSISDN#</span>",
			title: "<span  title='MSISDN'>MSISDN</span>"
		}, {
			field: "type",
			template: "<span  title='#:type#'>#:type#</span>",
			title: "<span  title='业务类型'>业务类型</span>"
		}, {
			field: "result",
			template: "<span  title='#:result#'>#:result#</span>",
			title: "<span  title='执行结果'>执行结果</span>"
		}, {
			field: "errorCode",
			template: "<span  title='#:errorCode#'>#:errorCode#</span>",
			title: "<span  title='错误代码'>错误代码</span>"
		}, {
			field: "dateTime",
			template: "<span  title='#:dateTime#'>#:dateTime#</span>",
			title: "<span  title='时间'>时间</span>"
		}, {
			template: "",
			title: "<span  title='操作'>操作</span>"
		}]
	});

});