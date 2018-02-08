
$(function() {
	/*当前导航*/
	$("#topNavList .navListWrap:eq(3) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(3) ul li:eq(7)").addClass("active");
	
	
	
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
					field: "name",
					template: "<span  title='#:name#'>#:name#</span>",
					title: "<span  title='指令组名称'>指令组名称</span>"
				}, {
					field: "desc",
					template: "<span  title='#:desc#'>#:desc#</span>",
					title: "<span  title='指令组描述'>指令组描述</span>"
				}, {
					field: "type",
					template: "<span  title='#:type#'>#:type#</span>",
					title: "<span  title='网元类型'>网元类型</span>"
				}, {
					field: "unitType",
					template: "<span  title='#:unitType#'>#:unitType#</span>",
					title: "<span  title='单元类型'>单元类型</span>"
				}, {
					field: "level",
					template: "<span  title='#:level#'>#:level#</span>",
					title: "<span  title='指令组级别'>指令组级别</span>"
				}, {
					field: "remarks",
					template: "<span  title='#:remarks#'>#:remarks#</span>",
					title: "<span  title='备注'>备注</span>"
				}, {
					width: 200,
					template: "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 编辑</button>&nbsp;&nbsp;"+
							  "<button class='deleteBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon-remove-sign'></i> 删除</button>&nbsp;&nbsp;"+
							  "<button class='bindBtn btn btn-xs btn-default'>选择指令</button>",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function(){
					dataGridObj.addClick();
					dataGridObj.editClick();
					dataGridObj.deleteClick();
					
					//绑定号码段弹窗
					$(".bindBtn").on("click", function(){
						bindWindowObj.center().open();
						windowGridObj.init();
					});
				}
			}).data("kendoGrid");
		},
		
		//添加按钮，显示弹窗
		addClick: function() {
			
			$("#addBtn").on("click", function() {
				
				var dataItem = {
					name:"",
					desc:"", 
					type:"", 
					unitType:"", 
					level:"", 
					remarks:""
				};
                infoWindow.obj.setOptions({"title":"添加"});
                
                infoWindow.initContent(dataItem);
                
                 //网元类型
                $("#typeInput option:eq(0)").attr("selected", "selected");
                //单元类型
                $("#unitTypeInput option:eq(0)").attr("selected", "selected");
                //指令组级别
                $("#levelInput option:eq(0)").attr("selected", "selected");
			});
		},
		
		//编辑
		editClick: function() {
			$(".editBtn").on("click", function() {
				
                var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
                
                infoWindow.obj.setOptions({"title":"修改"});
                
                infoWindow.initContent(dataItem);
                //网元类型
                $("#typeInput option[value='"+dataItem.type+"']").attr("selected", "selected");
                //单元类型
                $("#unitTypeInput option[value='"+dataItem.unitType+"']").attr("selected", "selected");
                //指令组级别
                $("#levelInput option[value='"+dataItem.level+"']").attr("selected", "selected");
                
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
	
	//绑定号码段弹窗内的表格
	var windowGridObj = {
		
		init: function(){
			
			this.dataGrid = $("#bindGrid").kendoGrid({
				
				dataSource: {
					data: bindGridList
				},
				
				height: 300,
				
				reorderable: true,
		
				resizable: true,
		
				sortable: true,
		
				columnMenu: true,
		
				pageable: false,
		
				columns: [{
					field: "name",
					template: "<span  title='#:name#'>#:name#</span>",
					title: "<span  title='指令名称'>指令名称</span>"
				}, {
					field: "content",
					template: "<span  title='#:content#'>#:content#</span>",
					title: "<span  title='指令内容'>指令内容</span>"
				}, {
					field: "suitType",
					template: "<span  title='#:suitType#'>#:suitType#</span>",
					title: "<span  title='适用网元类型'>适用网元类型</span>"
				}, {
					width: 80,
					template: "#if(added=='false'){# <button added='false' class='addOrRemoveBtn btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button> #}else{#" + 
							  "<button added='true' class='addOrRemoveBtn  btn btn-xs btn-danger' ><i class='glyphicon glyphicon-trash'></i> 移除</button> #}#",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function(){
					//每一行的加入/移除按钮
					$("#bindGrid td").delegate(".addOrRemoveBtn","click",function(){
						var td=$(this).closest("td");
						var html="";
						if($(this).attr("added")=="true"){
							html = "<button added='false' class='addOrRemoveBtn btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
						} else {
							html="<button added='true' class='addOrRemoveBtn btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
						}
						td.html(html);
					});
				}
			}).data("kendoGrid");
		}
	};
	
	//编辑、修改弹窗
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
	
	//绑定号码段弹窗
	if (!$("#bindWindow").data("kendoWindow")) {
		$("#bindWindow").kendoWindow({
			width: "900px",
			height:"400px",
			actions: ["Close"],
			modal: true,
			title: "选择指令"
		});
	}
	var bindWindowObj = $("#bindWindow").data("kendoWindow");
	
	//全部加入
	$("#addAll").on("click",function(){
		var html = "<button added='true' class='addOrRemoveBtn btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
		$(".addOrRemoveBtn[added='false']").parent().html(html);
	});
	//全部移除
	$("#removeAll").on("click",function(){
		var html="<button added='false' class='addOrRemoveBtn btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
		$(".addOrRemoveBtn[added='true']").parent().html(html);
	});
	
	dataGridObj.init();
	
	infoWindow.init();
	
});

var dataList = [
	{
		name: "高级命令组",
		desc: "高级命令组",
		type: "ONE-NDS",
		unitType: "ADM",
		level: "高级命令",
		remarks:""
	}, {
		name: "维护命令组",
		desc: "维护命令组",
		type: "ONE-NDS",
		unitType: "ADM",
		level: "维护命令",
		remarks:""
	}, {
		name: "查看命令组",
		desc: "查看命令组",
		type: "ONE-NDS",
		unitType: "ADM",
		level: "查看命令",
		remarks:""
	}, {
		name: "高级命令组",
		desc: "高级命令组",
		type: "ONE-NDS",
		unitType: "BE-DSA",
		level: "高级命令",
		remarks:""
	}, {
		name: "维护命令组",
		desc: "维护命令组",
		type: "ONE-NDS",
		unitType: "BE-DSA",
		level: "维护命令",
		remarks:""
	}, {
		name: "查看命令组",
		desc: "查看命令组",
		type: "ONE-NDS",
		unitType: "BE-DSA",
		level: "查看命令",
		remarks:""
	}, {
		name: "高级命令组",
		desc: "高级命令组",
		type: "ONE-NDS",
		unitType: "R-DSA",
		level: "高级命令",
		remarks:""
	}, {
		name: "维护命令组",
		desc: "维护命令组",
		type: "ONE-NDS",
		unitType: "R-DSA",
		level: "维护命令",
		remarks:""
	}, {
		name: "查看命令组",
		desc: "查看命令组",
		type: "ONE-NDS",
		unitType: "R-DSA",
		level: "查看命令",
		remarks:""
	}, {
		name: "高级命令组",
		desc: "高级命令组",
		type: "ONE-NDS",
		unitType: "PGW-DSA",
		level: "高级命令",
		remarks:""
	}
];

var bindGridList = [
	{
		name: "HSSFE LDAP链路状态检查",
		content: "netstat -an | grep 16611 | grep ESTABLISHED | wc",
		suitType:"HSSFE",
		added: 'true'
	}, {
		name: "HSSFE Notification端口状态",
		content: "netstat -an | grep 30300",
		suitType:"HSSFE",
		added: 'true'
	}, {
		name: "HSSFE/DRA Diameter链路状态检查",
		content: "netstat -an | grep 3868",
		suitType:"HSSFE",
		added: 'true'
	}, {
		name: "IP 路由检查",
		content: "netstat -rn",
		suitType:"HSSFE/ONE-NDS",
		added: 'false'
	}, {
		name: "IP路由",
		content: "NTHLR",
		suitType:"netstat -rn",
		added: 'false'
	}, {
		name: "LDAP查询统计信息",
		content: "./ndsLoadStats -i 3600",
		suitType:"ONE-NDS",
		added: 'false'
	}, {
		name: "NTP状态检查",
		content: "ntpq -p",
		suitType:"HSSFE/ONE-NDS",
		added: 'false'
	}, {
		name: "系统进程(PGW)",
		content: "/opt/provgw/bin/run_as_provgw.sh status",
		suitType:"PGW",
		added: 'true'
	}, {
		name: "系统负荷",
		content: "sar 10 1",
		suitType:"HSSFE/ONE-NDS",
		added: 'true'
	}, {
		name: "文件系统使用情况",
		content: "df -h",
		suitType:"NTHLR",
		added: 'true'
	}
];
