$(function() {
	
	/*当前导航*/
	$("#topNavList .navListWrap:eq(2) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(2) ul li:eq(4)").addClass("active");
	
	//选择指令高度
	$("#queryTable").height($(window).height() - $("#queryTable").offset().top-110);
	
	//执行结果
	var resultGridObj = $("#resultGrid").kendoGrid({
		
		dataSource: {
			data: result
		},
		
		height:$(window).height() - $("#resultGrid").offset().top - 65,
		
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
			field: "userNum",
			template: "<span  title='#:userNum#'>#:userNum#</span>",
			title: "<span  title='用户号码'>用户号码</span>"
		}, {
			field: "checkItem",
			template: "<span  title='#:checkItem#'>#:checkItem#</span>",
			title: "<span  title='检查项'>检查项</span>"
		}, {
			field: "person",
			template: "<span  title='#:person#'>#:person#</span>",
			title: "<span  title='操作人'>操作人</span>"
		}, {
			width: 60,
			template: "#if(result==0){#"+
					  "<span style='color: red;'>失败</span> #} else {#"+
					  "<span style='color: green;'>成功</span>#}#",
			title: "<span  title='结果'>结果</span>"
		}, {
			width:80,
			template: "#if(result==0){# <button class='errorBtn btn btn-xs btn-danger'><i class='glyphicon glyphicon-eye-open'></i> 显示</button> #} else {#"+
					  "<a class='btn btn-xs btn-success' href='userDataQuery.html'><i class='glyphicon glyphicon-search'></i> 显示</a> #}#",
			title: "<span>查询</span>"
		}],
		dataBound: function(){
			$(".errorBtn").on("click", function(){
				errorWindowObj.center().open();
			});
		}
	}).data("kendoGrid");
	
	//错误详情-弹窗
	if (!$("#errorWindow").data("kendoWindow")) {
				$("#errorWindow").kendoWindow({
					width: "600px",
					actions: ["Close"],
					modal:true,
					title: "错误详情"
				});
	}
	var errorWindowObj = $("#errorWindow").data("kendoWindow");
	
	//选择指令后 - 单、批量用户弹窗
	if (!$("#selectUserWindow").data("kendoWindow")) {
				$("#selectUserWindow").kendoWindow({
					width: "600px",
					actions: ["Close"],
					modal:true,
					title: "选择用户"
				});
	}
	var selectUserWindowObj = $("#selectUserWindow").data("kendoWindow");
	
	//选择单用户后-弹窗
	if (!$("#singleUserdetailsWindow").data("kendoWindow")) {
				$("#singleUserdetailsWindow").kendoWindow({
					width: "800px",
					actions: ["Close"],
					modal:true,
					title: "选择用户"
				});
	}
	var singleUserdetailsWindowObj = $("#singleUserdetailsWindow").data("kendoWindow");
	
	
	//选择批量用户后-弹窗
	if (!$("#multiUserdetailsWindow").data("kendoWindow")) {
				$("#multiUserdetailsWindow").kendoWindow({
					width: "800px",
					actions: ["Close"],
					modal:true,
					title: "选择用户"
				});
	}
	var multiUserdetailsWindowObj = $("#multiUserdetailsWindow").data("kendoWindow");
	
	//点击选择指令-下一步按钮
	$("#nextStep1Btn").on("click",function(){
		if($("#queryTable :checked").length <= 0){
			infoTip({content:"请选择指令！"});
		} else {
			var title = $("#queryTable :checked").parent().parent().next().text();
			selectUserWindowObj.setOptions({title: "指令: <code>"+title+"</code>"});
			selectUserWindowObj.center().open();
		}
	});
	
	//选择单、批量用户后-下一步按钮
	$("#nextStep2Btn").on("click",function(){
		
		
		if($("#usersRadio :checked").prop("value")=="0"){//单用户操作
			singleUserdetailsWindowObj.setOptions({title: selectUserWindowObj.options.title});
			singleUserdetailsWindowObj.center().open();
		
		} else { //批量用户操作
			multiUserdetailsWindowObj.setOptions({title: selectUserWindowObj.options.title});
			multiUserdetailsWindowObj.center().open();
		}
		selectUserWindowObj.close();
	});
	
	
	//上传
	$("#upload").kendoUpload({
		localization:{
			"select": "参数导入",
			"dropFilesHere": "拖拽文件到此区域"
		},
		multiple: true,
		async: {
			saveUrl: "save",
			removeUrl: "remove",
			autoUpload: false
		},
		template: kendo.template($('#fileTemplate').html()),
		complete: function(){
			//console.log("complete");
			//window.location.href='paraProcessing-addUploadList.html';
		},
		success: function(){
			console.log("success");
		},
		error: function(){
			console.log("error");
		}
	});
	
	
	//单用户、批量用户弹窗-确定执行按钮
	$("#singleUserDialogOKBtn, #multiUserDialogOKBtn").on("click", function(){
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
			userNum: "45354655643424",
			checkItem: "ffgf",
			person:"admin",
			result:"1"
		};
		result.push(data);
		var dataSource = new kendo.data.DataSource({
			data: result
		});
		//关闭弹窗
		multiUserdetailsWindowObj.close();
		singleUserdetailsWindowObj.close();
		//结果列表添加一条数据
		resultGridObj.setDataSource(dataSource);
	});
});
//0-失败，1-成功
var result = [
	{
		dateTime:"2015-07-28 00:28:09",
		userNum: "45354655643424",
		checkItem: "ffgf",
		person:"admin",
		result: 0
	}, {
		dateTime:"22015-07-13 23:41:49",
		userNum: "4553453534534",
		checkItem:"dfdf",
		person:"admin",
		result:1
	}
];
