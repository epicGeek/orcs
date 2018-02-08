kendo.culture("zh-CN");

var treeViewValue = "";

var treeViewText = "";


var dhssName = "";

var dhssNameText = "";

var alarmType = "";

var dhssNames = [];
var treeValues = [];
var params = {
	page : 1,
	size : 20,
	text : "",
	endAlarmTime : "",
	startAlarmTime : "",
	alarmNum : "",
	core : "",
	desc : "",
	dhssName:"",
	alarmType:""
};


var date = new Date();
var seperator1 = "-";
var year = date.getFullYear();
var month = date.getMonth() + 1;
var strDate = date.getDate();
if (month >= 1 && month <= 9) {
    month = "0" + month;
}
if (strDate >= 0 && strDate <= 9) {
    strDate = "0" + strDate;
}
var currentdate = year + seperator1 + month + seperator1 + strDate;
var startTime = currentdate+" 00:00:00" ;
var endTime = currentdate+" 23:59:59" ;
var LogInfoWindow;
var functions = {function1:undefined};

var dataResources = [];
var notAlarmdataResources;
$(function() {
	
	
	$("#addNotImportant").on("click",function(){
		var value = $("#notImportant").val();
		if(value == ""){
			if(confirm("确定删除所有非重要告警号吗？")){
				$.ajax({
					url : "delNotImportant",
					type : "GET",
					success : function(data) {
						if(data){
							infoTip({
								content : "删除成功！"
							});
						}
					}
				})
			}
		}else{
			$.ajax({
				url : "addNotImportant",
				type : "POST",
				data:{value : value},
				success : function(data) {
					if(data){
						infoTip({
							content : "保存成功！"
						});
					}
				}
			}) 
		}
		functions.function1();
	})
	
	$("#clearNotImportant").on("click",function(){
		$("#notImportant").val("");
	})


	
	
	
	
	functions.function1 = function(){
		$.ajax({
			url : "alarm-record/search/unitTreeView",
			type : "GET",
			success : function(data) {
				var treeview = $("#categoryTreeview").data("kendoTreeView");
		        treeview.setDataSource(data);
			}
		})
		var t1 = dhssName;
		var t2 = treeViewValue;
		$.each(dataResources,function(index,item){
			dhssName = dhssNames[index];
			treeViewValue = treeValues[index];
			item.dataSource.read();
		})
		dhssName = t1;
		treeViewValue = t2;
	}
	
//	setInterval("functions.function1()", 1000*60); 
	
	var url = "alarm-new";
	var htmltemp = "";
	var title = [ {
		name : "活动告警",
		url : "alarm-new"
	}, {
		name : "历史告警",
		url : "alarm-new?history=history"
	} ];
	$.each(title,
			function(index, item) {
				htmltemp += "<li><a href='" + item.url + "'>" + item.name
						+ "</a></li>";
			})
	$("#navList").html(htmltemp);
	$("#navList a[href='" + url + "']").addClass("active");

	// 左右可分割面板
	$("#split").kendoSplitter({
		panes : [ {
			collapsible : true,
			size : "300px"
		}, {
			collapsible : false
		} ]
	});

	// 左侧tab
	var leftTabstrip = $("#left_pane_Tabstrip").kendoTabStrip({
		animation : {
			open : {
				effects : "fadeIn",
				duration : 100,
			},
			close : {
				duration : 100,
				effects : "fadeOut"
			}
		},
		activate : function(e) {
			var index = $(e.item).index();
			// 如果是【我的收藏】tab，则隐藏下方查询等操作按钮
			if (index == 2) {
				$("#queryBtn,#resetBtn,#collectConditionBtn").hide();
				$("#cancelMultiCollectbtn").show();
			} else {
				$("#queryBtn,#resetBtn,#collectConditionBtn").show();
				$("#cancelMultiCollectbtn").hide();
			}
		}
	}).data("kendoTabStrip");

	// 左侧tab -- 查询条件 -- 时间
	$("#startTime,#endTime").kendoDateTimePicker({
		format : "yyyy-MM-dd HH:mm:ss"
	});

	$("#resetBtn").on("click", function() {
		treeViewValue = "";
		treeViewText = "";
		$("#startTime,#endTime,#alarmNum,#core,#desc").val("");
	});

	$("#cancelMultiCollectbtn").on("click", function() {

		var flag = $(" [name = 'chk' ]:checked ").length;
		var ind = 0;
		$.each($(" [name = 'chk' ]:checked "), function(index, item) {
			ind += 1;
			$.ajax({
				url : "rest/user-alarm-monitor/" + $(this).val(),
				type : "delete",
				dataType : "json",
				contentType : "text/uri-list",
				success : function(data) {
					if (flag == ind) {
						categoryTreeview.obj.dataSource.read();
						myDataSource.read();
						infoTip({
							content : "取消成功！"
						});
					}
				}
			});

		})

	})

	// 左侧面板--网络拓扑 -- 树形结构
	var categoryTreeview = {

		obj : undefined,

		// 点击【收藏】或【取消收藏】图标按钮
		collectionClick : function() {
			$("#categoryTreeview")
					.delegate(
							".collectionIconBtn",
							"click",
							function(e) {
								e.stopPropagation();
								var that = this;
								var currentLi = $(that).closest("li");
								var dataItem = categoryTreeview.obj
										.dataItem(currentLi);
								var options;
								if ($(that).hasClass("collectioned")) {
									options = {
										title : "取消收藏",
										content : "<h4 style='color:red;text-align:center;'>确认取消收藏么？</h4>",
										width : 400
									};
									confirmWindow(
											options,
											function() {
												$(that).prop("title", "收藏");
												$(that)
														.removeClass(
																"collectioned")
														.addClass(
																'noCollectioned');
												$(that)
														.find("i")
														.removeClass(
																"glyphicon-star")
														.addClass(
																'glyphicon-star-empty');

												$
														.ajax({
															url : "delUserAlarm",
															type : "GET",
															dataType : "json",
															data : {
																unitName : treeViewText,
																cnum : treeViewValue,
																startTime : $(
																		"#startTime")
																		.val(),
																endTime : $(
																		"#endTime")
																		.val(),
																alarmNum : $(
																		"#alarmNum")
																		.val(),
																keyword : $(
																		"#core")
																		.val(),
																desc : $(
																		"#desc")
																		.val()
															},
															contentType : "application/json;charset=UTF-8",
															success : function(
																	data) {
																if (data) {
																	myDataSource
																			.read();
																	infoTip({
																		content : "取消成功！"
																	});
																}
															}
														});

											});
								} else {
									options = {
										title : "收藏条件",
										content : "<h4 style='color:red;text-align:center;'>确认收藏么？</h4>",
										width : 400
									};
									confirmWindow(
											options,
											function() {
												$(that).prop("title", "取消收藏");
												$(that).removeClass(
														"noCollectioned")
														.addClass(
																'collectioned');
												$(that)
														.find("i")
														.removeClass(
																"glyphicon-star-empty")
														.addClass(
																'glyphicon-star');

												$
														.ajax({
															url : "addUserAlarmMonitor",
															type : "GET",
															dataType : "json",
															data : {
																unitName : treeViewText,
																cnum : treeViewValue,
																startTime : $(
																		"#startTime")
																		.val(),
																endTime : $(
																		"#endTime")
																		.val(),
																alarmNum : $(
																		"#alarmNum")
																		.val(),
																keyword : $(
																		"#core")
																		.val(),
																desc : $(
																		"#desc")
																		.val()
															},
															contentType : "application/json;charset=UTF-8",
															success : function(
																	data) {
																if (data) {
																	myDataSource
																			.read();
																	infoTip({
																		content : "收藏成功！",
																		color : "green"
																	});
																}
															}
														});
											});
								}

							});
		},
		// 生成网络拓扑树
		initTree : function() {
			$.ajax({
				url : "alarm-record/search/unitTreeView",
				type : "GET",
				success : function(data) {
					
					$("#categoryTreeview").html("");
					categoryTreeview.obj = undefined;
					categoryTreeview.obj = $("#categoryTreeview")
							.kendoTreeView(
									{
										dataSpriteCssClassField : "sprite",
										dragAndDrop : false,
										template : kendo.template($(
												"#treeview-template").html()),
										dataSource : data,
										height : $(window).height()
												- $("#categoryTreeview")
														.offset().top - 10,
										select : function(e) {
											var treeView = $(
													"#categoryTreeview").data(
													"kendoTreeView");
											var nodes = treeView.dataSource
													.view();
											treeViewText = "";
											var value = this.text(e.node);
											treeViewText = value;
											treeViewValue = "";
											dhssName = "";
											checkedNodeIds(nodes, value);
											console.log(treeViewValue);
											if(treeViewText == "全网" || ( treeViewValue != "---" ) ){
												var text = treeViewValue;
												startTime = currentdate+" 00:00:00" ;
												endTime =  currentdate+" 23:59:59" ;
												if(treeViewValue == "+++++"){
													dhssName = dhssNameText;
												}else{
													dhssName = "";
												}
												var time = startTime + " 至 " + endTime;
												var contentHtml = "<div class='form-inline'>"
														+ "<div class='form-group'> "
														+ treeViewText
														+ "  告警信息</div>"
														+ "<div class='dataGrid'></div>"
														+ "</div>";
												rightTabstrip.tabStrip
														.append([ {
															text : treeViewText
																	+ " <i class='removeTabBtn glyphicon glyphicon-remove'></i>",
															encoded : false, // Allows use of
																				// HTML for item
																				// text
															content : contentHtml
														} ]);
												$(window).scrollTop(0);
												rightTabstrip.tabStrip
														.select("li.k-item.k-last");
											}
											
											
										}
									}).data("kendoTreeView");
				}
			});
		},
		init : function() {
			this.initTree();
			this.collectionClick();
		}
	};

	function checkedNodeIds(nodes, text) {
		for (var i = 0; i < nodes.length; i++) {
			if (nodes[i].flag || nodes[i].cnum != "") {
				if (nodes[i].text == text) {
					treeViewValue = nodes[i].cnum;
					dhssNameText = nodes[i].text;
					if(treeViewValue == "+++++"){
						dhssNames[dataResources.length] = dhssNameText;
					}else{
						dhssNames[dataResources.length] = "";
					}
					treeValues[dataResources.length] = treeViewValue;
				}
			}

			if (nodes[i].hasChildren) {
				checkedNodeIds(nodes[i].children.view(), text);
			}
		}
	}

	// 【收藏查询条件】按钮
	$("#collectConditionBtn").on(
			"click",
			function() {
				var startTime = $("#startTime").val();
				var endTime = $("#endTime").val();
				var time = startTime + " 至 " + endTime;
				var contentHtml = "<table>"
						+ "<tr><th class='text-right'>板卡：</th><td>"
						+ treeViewText + "</td></tr>"
						+ "<tr><th class='text-right'>时间：</th><td>" + time
						+ "</td></tr>"
						+ "<tr><th class='text-right'>告警号：</th><td>"
						+ $("#alarmNum").val() + "</td></tr>"
						+ "<tr><th class='text-right'>关键字：</th><td>"
						+ $("#core").val() + "</td></tr>"
						+ "<tr><th class='text-right'>描述：</th><td>"
						+ $("#desc").val() + "</td></tr>" + "</table>";
				var options = {
					title : "确认收藏条件吗？",
					content : contentHtml,
					width : 500,
					height : 400
				};
				confirmWindow(options, function() {

					$.ajax({
						url : "addUserAlarmMonitor",
						type : "GET",
						dataType : "json",
						data : {
							unitName : treeViewText,
							cnum : treeViewValue,
							startTime : $("#startTime").val(),
							endTime : $("#endTime").val(),
							alarmNum : $("#alarmNum").val(),
							keyword : $("#core").val(),
							desc : $("#desc").val()
						},
						contentType : "application/json;charset=UTF-8",
						success : function(data) {
							if (data) {
								myDataSource.read();
								infoTip({
									content : "收藏成功！",
									color : "green"
								});
							}
						}
					});

				});
			});

	var myDataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
				url : "findUserAlarm",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			}
		},
		batch : true,
		schema : {
			data : function(d) {
				return d;
			}
		},
		pageSize : 20,
		serverPaging : true,
		serverSorting : true
	});

	// 我的收藏
	var myCollection = {
		obj : undefined,
		// hover 显示详情
		initToolTip : function() {
			$(".tooTipTd").kendoTooltip(
					{
						width : 300,
						position : "right",
						show : function(e) {
							var content = $(e.sender.content);
							var element = $(e.sender.element);
							var dataItem = myCollection.obj.dataItem(element
									.closest("tr"));
							var html = "<table>" + "<tr>" + "<th>板卡：</th>"
									+ "<td>"
									+ dataItem.unitName
									+ "</td>"
									+ "</tr>"
									+ "<tr>"
									+ "<th>时间：</th>"
									+ "<td>"
									+ dataItem.createTime
									+ "</td>"
									+ "</tr>"
									+ "<tr>"
									+ "<th>告警号：</th>"
									+ "<td>"
									+ dataItem.alarmNum
									+ "</td>"
									+ "</tr>"
									+ "<tr>"
									+ "<th>描述：</th>"
									+ "<td>"
									+ dataItem.alarmDesc
									+ "</td>"
									+ "</tr>"
									+ "</table>";
							content.html(html);
						}
					});
		},
		// 取消收藏
		cancelCollectionClick : function() {
			$(".removeBtn")
					.on(
							"click",
							function() {
								var that = this;
								var options = {
									title : "取消收藏",
									content : "<h4 style='color:red;text-align:center;'>确认取消收藏么？</h4>",
									width : 400
								};
								confirmWindow(
										options,
										function() {
											var dataItem = myCollection.obj
													.dataItem($(that).closest(
															"tr"));
											myCollection.obj.dataSource
													.remove(dataItem);

											$
													.ajax({
														url : "rest/user-alarm-monitor/"
																+ dataItem.id,
														type : "delete",
														dataType : "json",
														contentType : "application/json;charset=UTF-8",
														success : function(data) {
															categoryTreeview.obj.dataSource
																	.read();
															infoTip({
																content : "取消成功！"
															});
														}
													});

										});
							});
		},

		// 点击信息，生成右侧tab item 进行查看
		collectionClick : function() {
			$(".collectionA")
					.on(
							"click",
							function() {
								var dataItem = myCollection.obj
										.dataItem($(this).closest("tr"));

								var contentHtml = "<div class='form-inline'>"
										+ "<div class='form-group'><label>板卡：</label>"
										+ dataItem.unitName
										+ "</div>"
										+ "<div class='form-group'><label>时间：</label>"
										+ dataItem.createTime
										+ "</div>"
										+ "<div class='form-group'><label>告警号：</label>"
										+ dataItem.alarmNum
										+ "</div>"
										+ "<div class='form-group'><label>描述：</label>"
										+ dataItem.alarmDesc + "</div>"
										+ "<div class='dataGrid'></div>"
										+ "</div>";
								$("#startTime").val(dataItem.startTime);
								$("#endTime").val(dataItem.endTime);
								treeViewValue = dataItem.cnum;
								$("#alarmNum").val(dataItem.alarmNum);
								$("#core").val(dataItem.keyword);
								$("#desc").val(dataItem.alarmDesc);
								rightTabstrip.tabStrip
										.append([ {
											text : dataItem.unitName
													+ " <i class='removeTabBtn glyphicon glyphicon-remove'></i>",
											encoded : false, // Allows use of
																// HTML for item
																// text
											content : contentHtml
										} ]);
								$(window).scrollTop(0);
								rightTabstrip.tabStrip
										.select("li.k-item.k-last");
							});
		},
		initGrid : function() {
			myCollection.obj = $("#myCollectionGrid")
					.kendoGrid(
							{
								dataSource : myDataSource,
								height : 380,
								resizable : true,
								columns : [
										{
											template : "<input type='checkbox' name = 'chk' value='#:id#'/>",
											title : "<input type='checkbox' class='selectAll' />",
											width : 35
										},
										{
											template : "<a class='collectionA'>#:createTime#_#:unitName#_#:keyword#_#:alarmNum#</a>",
											title : "信息",
											attributes : {
												"class" : "tooTipTd"
											}
										},
										{
											width : 80,
											template : "<button class='removeBtn btn btn-xs btn-danger' title='取消收藏'>"
													+ "<i class='glyphicon glyphicon-star'></i> 取消"
													+ "</button>",
											title : "取消收藏"
										} ],
								dataBound : function() {
									myCollection.initToolTip();
									myCollection.cancelCollectionClick();
									myCollection.collectionClick();
									// 全选按钮
									$("#myCollectionGrid .selectAll")
											.on(
													"click",
													function() {
														$(
																"#myCollectionGrid .k-grid-content input[type='checkbox']")
																.prop(
																		"checked",
																		$(this)
																				.prop(
																						"checked"));
													});

									// 取消全选
									$(
											"#myCollectionGrid .k-grid-content input[type='checkbox']")
											.on(
													"click",
													function() {
														if (!$(this).prop(
																"checked")) {
															$(
																	"#myCollectionGrid .selectAll")
																	.prop(
																			"checked",
																			false);
														}
													});
								}
							}).data("kendoGrid");
		},
		init : function() {
			this.initGrid();
		}
	};

	LogInfoWindow = {

		obj : undefined,

		template : undefined,

		id : $("#windowTemplate"),
		// 取消
		cancelClick : function() {
			$("#cancelBtn").on("click", function() {
				LogInfoWindow.obj.close();
			});
		},

		initContent : function(dataItem) {

			// 填充弹窗内容
			LogInfoWindow.obj.content(LogInfoWindow.template(dataItem));
			$("#logText").html(dataItem.dataLog);
			LogInfoWindow.cancelClick();

			LogInfoWindow.obj.center().open();
		},

		init : function() {

			this.template = kendo.template($("#windowTemplate").html());

			if (!LogInfoWindow.id.data("kendoWindow")) {
				LogInfoWindow.id.kendoWindow({
					width : "700px",
					actions : [ "Close" ],
					modal : true,
					title : "号段管理"
				});
			}
			LogInfoWindow.obj = LogInfoWindow.id.data("kendoWindow");
		}
	};

	LogInfoWindow.init();

	var column = [
			{
				field : "alarmCell",
				title : "告警单元",
				width: 400 
			},
			{
				field : "alarmText",
				title : "告警标题",
				width: 400
			},
			{
				field : "alarmLevel",
				title : "告警等级",
				width: 100
			},
			{
				field : "alarmNo",
				title : "告警号",
				width: 100
			},
			{
				field : "receiveStartTime",
				title : "告警时间",
				width: 150
			},{
				field : "notifyId",
				title : "告警ID",
				width: 150
			},{
				field : "userInfo",
				title : "userInfo",
				width: 150
			},{
				field : "supplInfo",
				title : "supplInfo",
				width: 150
			},{
				field : "diagInfo",
				title : "diagInfo",
				width: 150
			},{
				field : "objInfo",
				title : "objInfo",
				width: 150
			},{
				title : "描述",//
				template : "<a class='btn btn-xs btn-warning' onClick = 'showdesc(\"#:alarmText#\",\"#:supplInfo#\",\"#:alarmDesc#\")'><i class='glyphicon'></i> 告警描述</a>",
				width: 150
			} ];

	// 右侧tab
	var rightTabstrip = {

		// 右侧面板移除按钮
		removeItemTabClick : function() {
			$("#right_pane_Tabstrip").delegate(
					".removeTabBtn",
					"click",
					function(e) {
						e.stopPropagation();

						var tab = $(this).closest("li.k-item"), otherTab = tab
								.next();
						var otherTab = otherTab.length ? otherTab : tab.prev();

						rightTabstrip.tabStrip.remove(tab);
						rightTabstrip.tabStrip.select(otherTab);
					});
		},

		// 查询按钮，右侧tab 添加item
		queryClick : function() {
			$("#queryBtn")
					.on(
							"click",
							function() {
								var text = treeViewValue == "" ? "全网"
										: treeViewValue;
								startTime = $("#startTime").val();
								endTime = $("#endTime").val();
								var time = startTime + " 至 " + endTime;
								var contentHtml = "<div class='form-inline'>"
										+ "<div class='form-group'><label>板卡：</label>"
										+ treeViewText
										+ "</div>"
										+ "<div class='form-group'><label>时间：</label>"
										+ time
										+ "</div>"
										+ "<div class='form-group'><label>告警号：</label>"
										+ $("#alarmNum").val()
										+ "</div>"
										+ "<div class='form-group'><label>关键字：</label>"
										+ $("#core").val() + "</div>" +
										// "<div
										// class='form-group'><label>描述：</label>"+
										// $("#desc").val() +"</div>" +
										"<div class='dataGrid'></div>"
										+ "</div>";
								rightTabstrip.tabStrip
										.append([ {
											text : treeViewText
													+ " <i class='removeTabBtn glyphicon glyphicon-remove'></i>",
											encoded : false, // Allows use of
																// HTML for item
																// text
											content : contentHtml
										} ]);
								
								$(window).scrollTop(0);
								rightTabstrip.tabStrip
										.select("li.k-item.k-last");
							});
		},

		// 生成右侧tab
		initTab : function() {
			rightTabstrip.tabStrip = $("#right_pane_Tabstrip")
					.kendoTabStrip(
							{
								animation : {
									open : {
										effects : "fadeIn",
										duration : 100,
									},
									close : {
										duration : 100,
										effects : "fadeOut"
									}
								},
								activate : function(e) {
									var $dataGrid = $(e.contentElement).find(
											".dataGrid");
									var tabDataSource = new kendo.data.DataSource(
											{
												transport : {
													read : {
														type : "GET",
														url : "alarm-record-new/search/searchByFilter?time="
																+ new Date(),
														dataType : "json",
														contentType : "application/json;charset=UTF-8"
													},
													parameterMap : function(
															options, operation) {
														if (operation == "read") {
															params.page = options.page - 1;
															params.size = options.pageSize;
															params.startAlarmTime = startTime;
															params.endAlarmTime = endTime;
															params.text = treeViewValue == "+++++" ? "" : treeViewValue ;
															params.alarmNum = $("#alarmNum").val();
															params.core = $("#core").val();
															params.desc = $("#desc").val();
															params.sort = "alarmLevel,receiveStartTime,desc";
															params.dhssName = dhssName;
															params.alarmType = alarmType;
															return params;
														}
													}
												},
												batch : true,
												schema : {
													data : function(d) {
														if(alarmType == "alarm"){
															notAlarmdataResources = tabDataSource;
														}
														alarmType = "";
														return d;
													},
													total : function(d) {
														return d.length;
														
													},
												},
												pageSize : 20,
												serverPaging : false,
												serverSorting : false,
												sort: [
											        {field: "receiveStartTime", dir: "desc"}
											    ]
											});
									
									
									
									
									
									if (!$dataGrid.data("kendoGrid")) {
										rightTabstrip.initGrid($dataGrid,
												tabDataSource);
									}
								}
							}).data("kendoTabStrip");
		},
		// 右侧 tab内 数据列表
		initGrid : function($dataGrid, tabDataSource) {
			var d = $dataGrid.kendoGrid({

				dataSource : tabDataSource,

				height : $(window).height() - $dataGrid.offset().top - 50,

				reorderable : true,

				resizable : true,

				sortable : false,

				columnMenu : false,

				pageable : true,

				columns : column

			}).data("kendoGrid");
			dataResources[dataResources.length] = d;
			
			$($dataGrid).on("dblclick", "tr", function (e) {
				showdesc(d.dataItem($(this)).alarmText,d.dataItem($(this)).supplInfo,d.dataItem($(this)).alarmDesc);
			}); 
		},
		init : function() {
			this.initTab();
			this.removeItemTabClick();
			this.queryClick();
		}
	};

	categoryTreeview.init();
	myCollection.init();
	rightTabstrip.init();
	
	
	$.ajax({
		url : "rest/not-important-alarm",
		type : "GET",
		success : function(data) {
			if(data._embedded){
				$("#notImportant").val(data._embedded["not-important-alarm"][0].alarmNoArray); 
			}
			alarmType = "alarm";
			var text = treeViewValue == "" ? "全网重要告警" : treeViewValue;
			var contentHtml = "<div class='form-inline'>"
					+ "<div class='form-group'>全网重要告警       <input id='dhssNe' /> <button id='reBtn' class='btn btn-default btn-sm'>" +
							"<span class='glyphicon glyphicon-repeat'></span> 刷新</button></div>"
					+ "<div class='dataGrid'></div>" + "</div>";
			rightTabstrip.tabStrip.append([ {
				text : text ,
				encoded : false, // Allows use of HTML for item text
				content : contentHtml
			} ]);
			$(window).scrollTop(0);
			rightTabstrip.tabStrip.select("li.k-item.k-last");
			getDhssName();
			$("#reBtn").on("click",function(index,item){
				alarmType = "alarm";
				treeViewValue = "";
				dhssName = "";
				notAlarmdataResources.read();
			})
		}
	})
	
	
	
	
	
	/*$("#alarms").on("click",function(){
		alarmType = "alarm";
		treeViewValue = "";
		dhssName = "";
		var text = treeViewValue == "" ? "全网重要告警" : treeViewValue;
		var contentHtml = "<div class='form-inline'>"
				+ "<div class='form-group'>全网重要告警       <input id='dhssNe' /></div>"
				+ "<div class='dataGrid'></div>" + "</div>";
		rightTabstrip.tabStrip.append([ {
			text : text + " <i class='removeTabBtn glyphicon glyphicon-remove'></i>",
			encoded : false, // Allows use of HTML for item text
			content : contentHtml
		} ]);
		$(window).scrollTop(0);
		rightTabstrip.tabStrip.select("li.k-item.k-last");
		getDhssName();
	})*/

});
function getDhssName(){
	$.ajax({
		url : "rest/equipment-ne",
		type : "GET",
		success : function(data) {
			var nes = data._embedded ? data._embedded["equipment-ne"] : []
			var dhssLocation = new Array();
			$.each(nes,function(index,item){
				dhssLocation[item.dhssName] = { name : item.dhssName , value : (dhssLocation[item.dhssName] ? dhssLocation[item.dhssName].value : "") + "@@" + item.cnum };
			})
			
			var dhssLocation1 = new Array();
			for(var i in dhssLocation){
				dhssLocation1[dhssLocation1.length] = dhssLocation[i];
			}
			
			console.log(dhssLocation);
			var dhssNe = $("#dhssNe").kendoDropDownList({
				optionLabel:"全部局址",
				dataTextField: "name",
				dataValueField: "value",
				dataSource: dhssLocation1,
				filter: "contains",
				suggest: true,
				change:function(){
					alarmType = "alarm";
					
					var filters = [];
			    	if($("#dhssNe").val() != ""){
			    		var filter = $("#dhssNe").val().split("@@");
			    		
			    		$.each(filter,function(index,item){
			    			if(item != "" && item != null && item != "null"){
			    				filters.push({field: "alarmCell", operator: "contains", value: item});
			    			}
			    		})
			    		
			    	}
			    	var arr = {filters : filters , logic : "or"};
			    	notAlarmdataResources.filter(arr);
//			    	notAlarmdataResources.fetch();
				}
			}).data("kendoDropDownList");
			
			
		}
	})
}

function showdesc(text,desc,id) {
	LogInfoWindow.obj.setOptions({
		"title" : "描述"
	});
	if(id !=null && id != ""){
		$.ajax({
			dataType : "json",
			url : "rest/system-alarm-rule/" + id,
			type : "GET",
			success : function(data) {
				console.log(data);
				LogInfoWindow.initContent({
					dataLog : text +"\n\n"+desc+ "\n\n" + data.alarmDesc
				});
			}
		});
	}else{
		LogInfoWindow.initContent({
			dataLog : text + "\n\n" + desc
		});
	}
	
}
