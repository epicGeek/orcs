kendo.culture("zh-CN");
function search(n){
	if(n == 'N'){
		return '否';
	}else{
		return '是';
	}
}
$(function(){
	var searchparams={ alarmNo:'',size:20};
	var href;
	var ruleObj = {
		ruleGrid: undefined,
		initGrid: function(){
			this.ruleGrid = $("#ruleList").kendoGrid({
				dataSource: {
					    type: "odata",
					    transport: {
					        read: {
					            type: "GET",
					            url: "/alarmRule/search",
					            dataType: "json",
					            contentType: "application/json;charset=UTF-8"
					        },
					        parameterMap: function(options, operation) {
					            	searchparams.page = options.page-1;
					                searchparams.size = options.size;
					                searchparams.sort="id,desc",
					                searchparams.alarmNo = $("#paraAlarmNo").val();
					                return searchparams;
					        }
					    },
					    schema: {
					        data: function(d) {
					            return d.content; //响应到页面的数据
					        },
					        total: function(d) {
					            return d.totalElements; //总条数
					        },
					    },
					    pageSize: 20,
					    serverPaging: true,
					    serverSorting: true
				},
		        height: $(window).height() - $("#ruleList").offset().top-20,
		        toolbar: kendo.template($("#template").html()),
				reorderable: true,
				resizable: true,
				sortable: false,
				columnMenu: false,
				pageable: true,
				columns: [{
					locked: true,
					width: 30,
					template: "<input type='checkbox' value='#:id#' />",
					encoded: false,
					title: ""
				},{
					locked: true,
					width: 70,
					template: "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 编辑</button>",
					encoded: false,
					title: "<span class='tdOverFont'  title='操作'>操作</span>"
				},{
					field: "alarmNo",
					width: 130,                                            
					template: "<span class='tdOverFont' title='#:alarmNo##:faultId == null ? '':faultId #'>#:alarmNo##:faultId == null ? '':faultId#</span>",
					encoded: false,
					title: "<span class='tdOverFont' title='厂家告警号'>厂家告警号</span>"
				},{
					field: "alarmContent",
					width: 350,
					template: "<span class='tdOverFont' title='#:alarmText == null ? '':alarmText#'>#:alarmText == null ? '':alarmText#</span>",
					encoded: false,
					title: "<span class='tdOverFont' title='告警'>告警标题</span>"
				},/*{
					field: "alarmName",
					width: 170,
					template: "<span class='tdOverFont' title='#:alarmName == null ? '':alarmName#'>#:alarmName == null ? '':alarmName#</span>",
					encoded: false,
					title: "<span class='tdOverFont' title='告警标准名'>告警标准名</span>"
				},{
					field: "onEquipment",
					width: 170,
					template: "<span class='tdOverFont' title='#:onEquipment == null ? '':onEquipment#'>#:onEquipment == null ? '':onEquipment#</span>",
					encoded: false,
					title: "<span class='tdOverFont' title='事件对设备影响'>事件对设备影响</span>"
				},{
					field: "onBusiness",
					width: 170,
					template: "<span class='tdOverFont' title='#:onBusiness == null ? '':onBusiness#'>#:onBusiness == null ? '':onBusiness#</span>",
					encoded: false,
					title: "<span class='tdOverFont' title='事件对业务影响'>事件对业务影响</span>"
				},*/{
					field: "alarmRemark",
					width: 320,
					template: "<span class='tdOverFont' title='#:alarmMeaning == null ? '':alarmMeaning#'>#:alarmMeaning == null ? '':alarmMeaning#</span>",
					encoded: false,
					title: "<span class='tdOverFont' title='告警解释'>告警解释</span>"
				},{
					field: "notifyImmediately",
					width: 140,
					template: "<span class='tdOverFont' title='#:notifyImmediately == null ? '' : search(notifyImmediately)#'>#:notifyImmediately == null ? '':search(notifyImmediately)#</span>",
					encoded: false,
					title: "<span class='tdOverFont' title='是否立即通知'>是否立即通知</span>"
				},{
					field: "isImportant",
					width: 140,
					template: "<span class='tdOverFont' title='#:isImportant == null ? '':search(isImportant)#'>#:isImportant == null ? '':search(isImportant)#</span>",
					encoded: false,
					title: "<span class='tdOverFont' title='是否重大告警'>是否重大告警</span>"
				},{
					field: "totalSumInterval",
					width: 220,
					template: "<span class='tdOverFont' title='#:totalSumInterval == null ? '':totalSumInterval#'>#:totalSumInterval == null ? '':totalSumInterval#</span>",
					encoded: false,
					title: "<span class='tdOverFont' title='全网告警个数门限判断时长'>全网告警个数门限判断时长</span>"
				},{
					field: "totalSumLimit",
					width: 170,
					template: "<span class='tdOverFont' title='#:totalSumLimit == null ? '':totalSumLimit#'>#:totalSumLimit == null ? '':totalSumLimit#</span>",
					encoded: false,
					title: "<span class='tdOverFont' title='全网告警个数门限'>全网告警个数门限</span>"
				},{
					field: "vipSumInterval",
					width: 250,
					template: "<span class='tdOverFont' title='#:vipSumInterval == null ? '':vipSumInterval#'>#:vipSumInterval == null ? '':vipSumInterval#</span>",
					encoded: false,
					title: "<span class='tdOverFont' title='VIP基站告警个数门限判断时长'>VIP基站告警个数门限判断时长</span>"
				},{
					field: "vipSumLimit",
					width: 180,
					template: "<span class='tdOverFont' title='#:vipSumLimit == null ? '':vipSumLimit#'>#:vipSumLimit == null ? '':vipSumLimit#</span>",
					encoded: false,
					title: "<span class='tdOverFont' title='VIP基站告警个数门限'>VIP基站告警个数门限</span>"
				},{
					field: "generalSumInterval",
					width: 250,
					template: "<span class='tdOverFont' title='#:generalSumInterval == null ? '':generalSumInterval#'>#:generalSumInterval == null ? '':generalSumInterval#</span>",
					encoded: false,
					title: "<span class='tdOverFont' title='普通基站告警个数门限判断时长'>普通基站告警个数门限判断时长</span>"
				},{
					field: "generalSumLimit",
					width: 190,
					template: "<span class='tdOverFont' title='#:generalSumLimit == null ? '':generalSumLimit#'>#:generalSumLimit == null ? '':generalSumLimit#</span>",
					encoded: false,
					title: "<span class='tdOverFont' title='普通基站告警个数门限'>普通基站告警个数门限</span>"
				}],
				dataBound: function(){
					ruleObj.editRuleClick();
				}
			}).data("kendoGrid");
		},
		
		//添加按钮，显示弹窗
		addRuleClick: function() {
			$("#addRule").on("click", function() {
				var data_to_save = {};
				data_to_save.alarmNo = "";
				data_to_save.faultId = "";
				data_to_save.alarmContent = "";
				data_to_save.alarmDesc = "";
				data_to_save.alarmName = "";
				data_to_save.onEquipment = "";
				data_to_save.onBusiness = "";
				data_to_save.alarmRemark = "";
				data_to_save.notifyImmediately = "";
				data_to_save.isImportant = "";
				data_to_save.totalSumInterval = "";
				data_to_save.totalSumLimit = "";
				data_to_save.vipSumInterval = "";
				data_to_save.vipSumLimit = "";
				data_to_save.generalSumInterval = "";
				data_to_save.generalSumLimit = "";
				
				ruleInfoWindow.initContent(data_to_save);
				$("#ruleInfoWindow").scrollTop(0);
				href="rest/system-alarm-rule";
				reset();
			});
			
		},
		//保存 
		saveRuleClick: function(){
			$("#saveRule").on("click",function(){
				var alarmRule = getData();
				if ($("#ruleInfoWindow").kendoValidator().data("kendoValidator").validate()) {
					$.ajax({
						dataType : 'json',
						type : (href=="rest/system-alarm-rule")?"POST":"PATCH",
				                url: href,
						contentType : "application/json;charset=UTF-8",
						data : kendo.stringify(alarmRule),
						success : function(data) {
							ruleInfoWindow.obj.close();
						
							infoTip({content: "保存成功！",color:"#D58512"});
							ruleObj.ruleGrid.dataSource.read();
						}
					});
				} else {
					
				}
				
			});
		},
		
		//取消
		cancelRuleClick: function(){
			$("#cancelRule").on("click",function(){
				ruleInfoWindow.obj.close();
			});
		},
	
		//删除
		deleteRuleClick: function() {
			$("#deleteRule").on("click", function() {
				var checkboxs = $("#ruleList :checked");
				console.log(checkboxs);
				if(checkboxs.length == 0){
					infoTip({"content": "请选择至少一条数据！"});
				} else {
					if(confirm("确定删除吗？")){
						for(var i=0; i<checkboxs.length; i++){
							var that = checkboxs.eq(i);
							var dataItem = ruleObj.ruleGrid.dataItem(that.closest("tr"));

							$.ajax({
								url : "rest/system-alarm-rule/"+that.val(),
								type : "DELETE",
								success : function(data) {
									ruleObj.ruleGrid.dataSource.remove(dataItem);
									infoTip({content: "删除成功！"});
									ruleObj.ruleGrid.dataSource.read(searchparams);
								}
							});
							ruleObj.ruleGrid.dataSource.read();
//							ruleObj.ruleGrid.dataSource.remove(dataItem);
						}
					}
				}
			});
		},
		
		//编辑
		editRuleClick: function() {
			$(".editBtn").on("click", function() {
				var pickRow = ruleObj.ruleGrid.dataItem($(this).closest("tr"));
				href="rest/system-alarm-rule/"+pickRow.id;
				$.ajax({
					dataType : 'json',
					type : "GET",
					url : "rest/system-alarm-rule/"+pickRow.id,
					success : function(data) {
						ruleInfoWindow.initContent(data,false);
					},
					error : function(data) {
						showNotify(data.message,"error");
					}
				});
			});
		},
		
		init: function(){
			ruleObj.initGrid();
			ruleObj.addRuleClick();
			ruleObj.deleteRuleClick();
//			ruleObj.editRuleClick();
		}
	};
	
	//新增告警-弹窗
	var ruleInfoWindow = {
		obj: undefined,
		template: undefined,
		id: $("#ruleInfoWindow"),
		initCondition: function(){
			//是否立即通知
			$("#notifyImmediately").kendoDropDownList({
				dataSource:[
				            {notifyImmediately:"Y",notifyImmediatelyView:"是"},
				            {notifyImmediately:"N",notifyImmediatelyView:"否"}
				            ]
			});
			$("#isImportant").kendoDropDownList({
				dataSource:[
				            {isImportant:"Y",isImportantView:"是"},
				            {isImportant:"N",isImportantView:"否"}
				            ]
			});
			//全网告警个数门限判断时长
			$("#totalSumInterval").kendoDropDownList({
				dataSource: [{totalSumInterval:"1",totalSumIntervalView:"1分钟"},
				             {totalSumInterval:"3",totalSumIntervalView:"3分钟"},
				             {totalSumInterval:"5",totalSumIntervalView:"5分钟"},
				             {totalSumInterval:"10",totalSumIntervalView:"10分钟"},
				             {totalSumInterval:"15",totalSumIntervalView:"15分钟"},
				             {totalSumInterval:"30",totalSumIntervalView:"30分钟"},
				             {totalSumInterval:"60",totalSumIntervalView:"60分钟"}]
			});
			//VIP基站告警个数门限判断时长
			$("#vipSumInterval").kendoDropDownList({
				dataSource: [{vipSumInterval:"1",vipSumIntervalView:"1分钟"},
				             {vipSumInterval:"3",vipSumIntervalView:"3分钟"},
				             {vipSumInterval:"5",vipSumIntervalView:"5分钟"},
				             {vipSumInterval:"10",vipSumIntervalView:"10分钟"},
				             {vipSumInterval:"15",vipSumIntervalView:"15分钟"},
				             {vipSumInterval:"30",vipSumIntervalView:"30分钟"},
				             {vipSumInterval:"60",vipSumIntervalView:"60分钟"}]
			});
			//普通基站告警个数门限判断时长
			$("#generalSumInterval").kendoDropDownList({
				dataSource: [{generalSumInterval:"1",generalSumIntervalView:"1分钟"},
				             {generalSumInterval:"3",generalSumIntervalView:"3分钟"},
				             {generalSumInterval:"5",generalSumIntervalView:"5分钟"},
				             {generalSumInterval:"10",generalSumIntervalView:"10分钟"},
				             {generalSumInterval:"15",generalSumIntervalView:"15分钟"},
				             {generalSumInterval:"30",generalSumIntervalView:"30分钟"},
				             {generalSumInterval:"60",generalSumIntervalView:"60分钟"}]
			});
		},
		
		
		initContent: function(dataItem){
	
			//填充弹窗内容
			ruleInfoWindow.obj.content(ruleInfoWindow.template(dataItem));
			this.initCondition();
			
			ruleInfoWindow.obj.center().open();
			ruleObj.saveRuleClick();
			ruleObj.cancelRuleClick();
			
			
		},
		
		init: function(){
			this.template = kendo.template($("#ruleInfoWindowTemplate").html());
			this.initCondition();
			//信息验证
			$("#bsInfoValidate").kendoValidator().data("kendoValidator");
			//弹窗内，保存/取消按钮
			ruleObj.saveRuleClick();
			ruleObj.cancelRuleClick();
			if (!ruleInfoWindow.id.data("kendoWindow")) {
				ruleInfoWindow.id.kendoWindow({
					width: 900,
					actions: ["Close"],
					modal:true,
					title: "添加告警"
				});
			}
			ruleInfoWindow.obj = ruleInfoWindow.id.data("kendoWindow");
			
		}
	};
	ruleInfoWindow.init();
	ruleObj.init();
	
	$('#searchBtn').on('click', function (event) {
		searchparams.alarmNo = $("#alarmNo").val();
		$("#ruleList").data("kendoGrid").pager.page(1);
	});
	$('#clearBtn').on('click', function (event) {
		$("#paraAlarmNo").val("");
	});	
	
	
});



function getData(){
	var data_to_save = {};
	data_to_save.alarmNo = $("#alarmNo").val().trim();
	data_to_save.faultId = $("#faultId").val();
	data_to_save.alarmContent = $("#alarmContent").val();
	data_to_save.alarmDesc = $("#alarmDesc").val();
	data_to_save.alarmName = $("#alarmName").val();
	data_to_save.onEquipment = $("#onEquipment").val();
	data_to_save.onBusiness = $("#onBusiness").val();
	data_to_save.alarmRemark = $("#alarmRemark").val();
	data_to_save.notifyImmediately = $("#notifyImmediately").val();
	data_to_save.isImportant = $("#isImportant").val();
	data_to_save.totalSumInterval = $("#totalSumInterval").val();
	data_to_save.totalSumLimit = $("#totalSumLimit").val();
	data_to_save.vipSumInterval = $("#vipSumInterval").val();
	data_to_save.vipSumLimit = $("#vipSumLimit").val();
	data_to_save.generalSumInterval = $("#generalSumInterval").val();
	data_to_save.generalSumLimit = $("#generalSumLimit").val();
	return data_to_save;
}
function reset(){
	
	 $("#alarmNo").val("");
	 $("#faultId").val("");
	 $("#alarmContent").val("");
	 $("#alarmDesc").val("");
	 $("#alarmName").val("");
	 $("#onEquipment").val("");
	 $("#onBusiness").val("");
	 $("#alarmRemark").val("");
	 $("#notifyImmediately").val("Y");
	 $("#isImportant").val("Y");
	 $("#totalSumInterval").val("1");
	 $("#totalSumLimit").val("");
	 $("#vipSumInterval").val("1");
	 $("#vipSumLimit").val("");
	 $("#generalSumInterval").val("1");
	 $("#generalSumLimit").val("");
	
}
