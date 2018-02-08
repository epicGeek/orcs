$(function() {
	
	/*当前导航*/
	$("#topNavList .navListWrap:eq(1) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(1) ul li:eq(1)").addClass("active");
	
	
	//选择指令-高度
	$("#instructionWrap").height($(window).height() - $("#instructionWrap").offset().top - 130);
	
	//单元列表
	$("#queryGrid").kendoGrid({
		
		dataSource: {
			data: window.safetyManagementDS
		},
		
		height:$(window).height() - $("#queryGrid").offset().top - 80,
		
		reorderable: true,

		resizable: true,

		sortable: true,

		columnMenu: true,

		pageable: false,

		columns: [{
			width: 30,
			template: "<input type='checkbox' />",
			attributes:{
				"class": "text-center"
			},
			title: "<input type='checkbox' id='selectAllCheckbox'/>"
		}, {
			field: "ne",
			template: "<span  title='#:ne#'>#:ne#</span>",
			title: "<span  title='网元'>网元</span>"
		}, {
			field: "unit",
			template: "<span  title='#:unit#'>#:unit#</span>",
			title: "<span  title='单元'>单元</span>"
		}, {
			field: "ip",
			template: "<span  title='#:ip#'>#:ip#</span>",
			title: "<span  title='地址'>地址</span>"
		}, {
			field: "type",
			template: "<span  title='#:type#'>#:type#</span>",
			title: "<span  title='类型'>类型</span>"
		}],
		dataBound: function(){
			
			//全选按钮
			$("#selectAllCheckbox").on("click",function(){
				$("#queryGrid .k-grid-content input[type='checkbox']").prop("checked",$(this).prop("checked"));
			});
			
			//取消全选
			$("#queryGrid .k-grid-content input[type='checkbox']").on("click",function(){
				if(!$(this).prop("checked")){
					$("#selectAllCheckbox").prop("checked",false);
				}
			});
		}
	});

	//执行结果
	var resultGridObj = $("#resultGrid").kendoGrid({
		
		dataSource: {
			data: result
		},
		
		height:$(window).height() - $("#resultGrid").offset().top - 75,
		
		reorderable: true,

		resizable: true,

		sortable: true,

		columnMenu: true,

		pageable: false,

		columns: [{
			field: "dateTime",
			template: "<span  title='#:dateTime#'>#:dateTime#</span>",
			title: "<span  title='时间'>时间</span>"
		}, {
			field: "type",
			template: "<span  title='#:type#'>#:type#</span>",
			title: "<span  title='检查项'>检查项</span>"
		}, {
			field: "result",
			template: "#if(result==0){#"+
					  "<div class='progress'><div class='progress-bar progress-bar-warning progress-bar-striped active' style='width: 70%;'>执行中……</div></div>"+
					  "#} else if(result==1){#"+
					  "完成，<a class='btn btn-link'><i class='glyphicon glyphicon-download-alt'></i>下载</a>"+
					  "#}#",
			title: "<span  title='结果'>结果</span>"
		}],
		dataBound: function(){
			
			$(".loginBtns").on("click", function(){
				alert("连接超时 5 秒");
			});
		}
	}).data("kendoGrid");
	
	//弹窗
	if (!$("#submitWindow").data("kendoWindow")) {
				$("#submitWindow").kendoWindow({
					width: "600px",
					actions: ["Close"],
					modal:true,
					title: "确认下发指令"
				});
	}
	var windowObj = $("#submitWindow").data("kendoWindow");
	
	//提交执行
	$("#submitBtn").on("click",function(){
		if($("#queryGrid .k-grid-content input[type='checkbox']:checked").length <=0){
			infoTip({"content":"请选择单元列表！"});
		} else if($("#operationsTable input[type='radio']:checked").length <=0){
			infoTip({"content":"请选择指令！"});
		} else {
			windowObj.center().open();
		}
	});
	
	//弹窗-确定按钮
	$("#dialogOKBtn").on("click",function(){
		var dateTime = new Date();
		var year = dateTime.getFullYear();
		var month = dateTime.getMonth()+1;
		month = (month < 10)? (0+""+month): month;
		var day = dateTime.getDate();
		day = (day < 10)? (0+""+day): day;
		var hours = dateTime.getHours();
		hours = (hours < 10)? (0+""+hours): hours;
		var minutes = dateTime.getMinutes();
		minutes = (minutes < 10)? (0+""+minutes): minutes;
		var seconds = dateTime.getSeconds();
		seconds = (seconds < 10)? (0+""+seconds): seconds;
		
		var data = {
			dateTime: year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds,
			type: $("#operationsTable input[type='radio']:checked").parent().text(),
			result:"0"
		};
		result.push(data);
		var dataSource = new kendo.data.DataSource({
			data: result
		});
		windowObj.close();
		resultGridObj.setDataSource(dataSource);
	});
});
//0-执行中，1-执行完成
var result = [
	{
		dateTime:"2015-08-20 15:34:23",
		type:"软件版本检查",
		result:"1"
	},
	{
		dateTime:"2015-08-19 18:21:01",
		type:"系统运行时间",
		result:"0"
	}
];
