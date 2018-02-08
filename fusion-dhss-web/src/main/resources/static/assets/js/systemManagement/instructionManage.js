
$(function() {
	/*当前导航*/
	$("#topNavList .navListWrap:eq(3) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(3) ul li:eq(6)").addClass("active");
	
	
	
	var dataGridObj = {
		
		init: function(){
			
			this.dataGrid = $("#dataGrid").kendoGrid({
				
				dataSource: {
					data: dataList,
					pageSize:10
				},
				
				height: $(window).height()-$("#dataGrid").offset().top - 50,
				
				reorderable: true,
		
				resizable: true,
		
				sortable: true,
		
				columnMenu: true,
		
				pageable: true,
		
				columns: [{
					field: "instructionType",
					template: "<span  title='#:instructionType#'>#:instructionType#</span>",
					title: "<span  title='指令类型'>指令类型</span>"
				}, {
					field: "instructionCategory",
					template: "<span  title='#:instructionCategory#'>#:instructionCategory#</span>",
					title: "<span  title='命令类别'>命令类别</span>"
				}, {
					field: "instructionName",
					template: "<span  title='#:instructionName#'>#:instructionName#</span>",
					title: "<span  title='指令名称'>指令名称</span>"
				}, {
					field: "instructionContent",
					template: "<span  title='#:instructionContent#'>#:instructionContent#</span>",
					title: "<span  title='指令内容'>指令内容</span>"
				}, {
					field: "paraName",
					template: "<span  title='#:paraName#'>#:paraName#</span>",
					title: "<span  title='参数名称'>参数名称</span>"
				}, {
					field: "user",
					template: "<span  title='#:user#'>#:user#</span>",
					title: "<span  title='执行指令的用户'>执行指令的用户</span>"
				}, {
					field: "suitType",
					template: "<span  title='#:suitType#'>#:suitType#</span>",
					title: "<span  title='适用网元类型'>适用网元类型</span>"
				}, {
					width: 130,
					template: "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 编辑</button>&nbsp;&nbsp;"+
							  "<button class='deleteBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon-remove-sign'></i> 删除</button>",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function(){
					dataGridObj.addClick();
					dataGridObj.editClick();
					dataGridObj.deleteClick();
				}
			}).data("kendoGrid");
		},
		
		//添加按钮，显示弹窗
		addClick: function() {
			
			$("#addBtn").on("click", function() {
				
				var dataItem = {
					instructionType:"", 
					instructionCategory:"",
					instructionName:"",
					instructionContent:"",
					paraName:"", 
					user:"", 
					suitType:""
				};
                
                infoWindow.obj.setOptions({"title":"添加"});
                
                infoWindow.initContent(dataItem);
                //指令类型
                $("#instructionType option:eq(0)").attr("selected","selected");
                //命令类别
                $("#instructionCategory option:eq(0)").attr("selected","selected");
			});
		},
		
		//编辑
		editClick: function() {
			$(".editBtn").on("click", function() {
				
                var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
                
                infoWindow.obj.setOptions({"title":"修改"});
                
                infoWindow.initContent(dataItem);
                
                //指令类型
                $("#instructionType option[value='"+dataItem.instructionType+"']").attr("selected","selected");
                //命令类别
                $("#instructionCategory option[value='"+dataItem.instructionCategory+"']").attr("selected","selected");
			});
		},
	
		//删除
		deleteClick: function() {
			$(".deleteBtn").on("click", function() {
				if(confirm("确定删除么？")){
					var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
					dataGridObj.dataGrid.dataSource.remove(dataItem);
				    infoTip({content: "删除成功！"});
				}
			});
		}
	};
	
	//弹窗
	var infoWindow = {
		
		obj: undefined,
		
		template: undefined,
		
		id: $("#infoWindow"),
		
		//保存 【添加】/
		saveClick: function(){
			$("#saveBtn").on("click",function(){
				infoWindow.obj.close();
				infoTip({content: "保存成功！",color:"#D58512"});
			});
		},
		
		//取消
		cancelClick: function(){
			$("#cancelBtn").on("click",function(){
				infoWindow.obj.close();
			});
		},
		
		initContent: function(dataItem){
			
			//填充弹窗内容
			infoWindow.obj.content(infoWindow.template(dataItem));
			
			//弹窗内，保存/取消按钮
			infoWindow.saveClick();
			infoWindow.cancelClick();
			
			infoWindow.obj.center().open();
		},
		
		init: function(){
			
			this.template = kendo.template($("#windowTemplate").html());
			
			if (!infoWindow.id.data("kendoWindow")) {
				infoWindow.id.kendoWindow({
					width: "700px",
					actions: ["Close"],
					modal:true,
					title: "号段管理"
				});
			}
			infoWindow.obj = infoWindow.id.data("kendoWindow");
		}
	};
	
	dataGridObj.init();
	
	infoWindow.init();
	
});

var dataList = [
	{
		instructionType:"用户数据管理",
		instructionCategory:"GPRS",
		instructionName:"用户短信限制设置（BOIC）",
		instructionContent:"subtool ZMSSBOICSMS imsi  ba_status",
		paraName:"",
		user:"",
		suitType:"OM"
	}, {
		instructionType:"用户数据管理",
		instructionCategory:"用户管理",
		instructionName:"添加带静态IP地址的4G专用APN",
		instructionContent:"subtool imsi apn ctxId ctxType",
		paraName:"",
		user:"",
		suitType:"OM"
	}, {
		instructionType:"用户数据管理",
		instructionCategory:"用户管理",
		instructionName:"显示用户全部数据",
		instructionContent:"subtool ZMIO type number",
		paraName:"",
		user:"",
		suitType:"OM"
	}, {
		instructionType:"软硬件维护",
		instructionCategory:"",
		instructionName:"查看版本信息",
		instructionContent:"show version",
		paraName:"",
		user:"admin",
		suitType:"AHUB\CE"
	}, {
		instructionType:"局数据管理",
		instructionCategory:"",
		instructionName:"局数据日常链路",
		instructionContent:"Display-m3ua-lset $1",
		paraName:"",
		user:"rtp99",
		suitType:"HRFE"
	}, {
		instructionType:"软硬件维护",
		instructionCategory:"",
		instructionName:"系统日期",
		instructionContent:"date",
		paraName:"",
		user:"root",
		suitType:"HSSFE/NTHLR"
	}, {
		instructionType:"局数据管理",
		instructionCategory:"",
		instructionName:"创建GT2",
		instructionContent:"CREATE-REMSSN $1",
		paraName:"",
		user:"rtp99",
		suitType:"NTHLR"
	}, {
		instructionType:"局数据管理",
		instructionCategory:"",
		instructionName:"创建GT",
		instructionContent:"swmml $1",
		paraName:"",
		user:"rtp99",
		suitType:"NTHLR"
	}, {
		instructionType:"网络接口维护",
		instructionCategory:"",
		instructionName:"网络接口状态",
		instructionContent:"ifconfig eth0;sleep 10;ifconfig eth0;sleep 10;ifconfig eth1;sleep 10;ifconfig eth1",
		paraName:"",
		user:"root",
		suitType:"HSSFE/ONE-NDS"
	}, {
		instructionType:"网络接口维护",
		instructionCategory:"",
		instructionName:"网络接口IP流量统计",
		instructionContent:"netstat -in",
		paraName:"",
		user:"root",
		suitType:"NTHLR"
	}
];
