kendo.culture("zh-CN");
$(function() {
	
	/*当前导航*/
	$("#topNavList .navListWrap:eq(2) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(2) ul li:eq(3)").addClass("active");
	
	//tab - 为了去掉bootstrap.js
	$("[role='tablist']").delegate("li[role='presentation']","click",function(e){
		e.stopPropagation();
		$(this).siblings(".active").removeClass("active");
		$(this).addClass("active");
		var href=$(this).children("a").attr("href");
		$(href).siblings(".active").removeClass("active");
		$(href).addClass("active");
		return false;
	});
	
	//查询条件
	$("#startDateTime").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:MM"
	});
	$("#endDateTime").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:MM"
	});
	
	//历史查询记录按钮
	$("#historyBtn").on("click", function() {
		$('#myModal').modal('show');
	});

	$("#dataGrid").kendoGrid({

		dataSource: {
			data: [{
				dateTime: "2015-06-22 12:45:12",
				num: 460029111909306,
				user: "admin",
				unit: ""
			}],
			pageSize: 10
		},

		reorderable: true,

		resizable: true,

		sortable: true,

		columnMenu: true,

		pageable: true,

		columns: [{
			field: "dateTime",
			template: "<span  title='#:dateTime#'>#:dateTime#</span>",
			title: "<span  title='日志时间'>日志时间</span>"
		}, {
			field: "num",
			template: "<span  title='#:num#'>#:num#</span>",
			title: "<span  title='查询号段'>查询号段</span>"
		}, {
			field: "user",
			template: "<span  title='#:user#'>#:user#</span>",
			title: "<span  title='操作人'>操作人</span>"
		}, {
			field: "unit",
			template: "<span  title='#:unit#'>#:unit#</span>",
			title: "<span  title='单元名称'>单元名称</span>"
		}, {
			template: "<button class='btn btn-xs btn-success'><i class='glyphicon glyphicon-download-alt'></i> 下载XML</button>&nbsp;&nbsp;"
					  +"<button class='btn btn-xs btn-warning'><i class='glyphicon glyphicon-open-file'></i> 打开</button>",
			title: "<span  title='操作'>操作</span>"
		}]
	});

});