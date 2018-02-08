$(function() {
	/*当前导航*/
	$("#topNavList .navListWrap:eq(3) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(3) ul li:eq(5)").addClass("active");
	
	
	//角色列表
	var dataGridObj = {

		init: function() {

			this.dataGrid = $("#dataGrid").kendoGrid({

				dataSource: {
					data: dataList,
					pageSize: 10
				},

				height: $(window).height() - $("#dataGrid").offset().top - 50,

				reorderable: true,

				resizable: true,

				sortable: true,

				columnMenu: true,

				pageable: true,

				columns: [{
					width: 150,
					field: "name",
					template: "<span  title='#:name#'>#:name#</span>",
					title: "<span  title='角色名称'>角色名称</span>"
				}, {
					width: 130,
					field: "level",
					template: "<span  title='#:level#'>#:level#</span>",
					title: "<span  title='操作等级'>操作等级</span>"
				}, {
					field: "desc",
					template: "<span  title='#:desc#'>#:desc#</span>",
					title: "<span  title='描述'>描述</span>"
				}, {
					width: 500,
					template: "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 编辑</button>&nbsp;&nbsp;" +
						"<button class='deleteBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon-remove-sign'></i> 删除</button>&nbsp;&nbsp;" +
						"<button class='usersBtn btn btn-xs btn-default'>用户成员</button></button>&nbsp;&nbsp;"+
						"<button class='indicatorBtn btn btn-xs btn-default'>指令权限</button>&nbsp;&nbsp;"+
						"<button class='areaBtn btn btn-xs btn-default'>地区权限</button>&nbsp;&nbsp;"+
						"<button class='netElementBtn btn btn-xs btn-default'>网元权限</button>&nbsp;&nbsp;"+
						"<button class='systemBtn btn btn-xs btn-default'>系统功能权限</button>",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function() {
					dataGridObj.addClick();
					dataGridObj.editClick();
					dataGridObj.deleteClick();
					
					//用户成员弹窗
					$(".usersBtn").on("click", function() {
						var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
						usersWindowObj.obj.setOptions({
							"title": "【 <span style='color:blue;'>"+dataItem.name+"</span> 】的用户成员"
						});
						usersWindowObj.obj.center().open();
						usersWindowObj.initGrid();
					});
					
					//指令权限弹窗
					$(".indicatorBtn").on("click", function() {
						var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
						indicatorWindowObj.obj.setOptions({
							"title": "【 <span style='color:blue;'>"+dataItem.name+"</span> 】的指令权限"
						});
						indicatorWindowObj.obj.center().open();
						indicatorWindowObj.initGrid();
					});
					
					//地区权限弹窗
					$(".areaBtn").on("click", function() {
						var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
						areaWindowObj.obj.setOptions({
							"title": "【 <span style='color:blue;'>"+dataItem.name+"</span> 】的地区权限"
						});
						areaWindowObj.obj.center().open();
						areaWindowObj.initGrid();
					});
					
					//网元权限弹窗
					$(".netElementBtn").on("click", function() {
						var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
						netElementWindowObj.obj.setOptions({
							"title": "【 <span style='color:blue;'>"+dataItem.name+"</span> 】的网元权限"
						});
						netElementWindowObj.obj.center().open();
						netElementWindowObj.initGrid();
					});
					
					//系统功能权限弹窗
					$(".systemBtn").on("click", function() {
						var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
						systemWindowObj.obj.setOptions({
							"title": "【 <span style='color:blue;'>"+dataItem.name+"</span> 】的系统功能权限"
						});
						systemWindowObj.obj.center().open();
						systemWindowObj.initGrid();
					});
				}
			}).data("kendoGrid");
		},

		//添加按钮，显示弹窗
		addClick: function() {

			$("#addBtn").on("click", function() {

				var dataItem = {
					name: "",
					level: "",
					desc: ""
				};
				$("#levelSelect option:eq(0)").attr("selected", "selected");
				infoWindow.obj.setOptions({
					"title": "添加"
				});

				infoWindow.initContent(dataItem);
			});
		},

		//编辑
		editClick: function() {
			$(".editBtn").on("click", function() {
				var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
				
				$("#levelSelect option[value='" + dataItem.level + "']").attr("selected", "selected");
				infoWindow.obj.setOptions({
					"title": "修改"+"【 <span style='color:blue;'>"+dataItem.name+"</span> 】"
				});
				
				infoWindow.initContent(dataItem);
			});
		},

		//删除
		deleteClick: function() {
			$(".deleteBtn").on("click", function() {
				if (confirm("确定删除么？")) {
					var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
					dataGridObj.dataGrid.dataSource.remove(dataItem);
					infoTip({
						content: "删除成功！"
					});
				}
			});
		}
	};

	//编辑、修改弹窗
	var infoWindow = {

		obj: undefined,

		template: undefined,

		id: $("#infoWindow"),

		//保存 【添加】/
		saveClick: function() {
			$("#saveBtn").on("click", function() {
				infoWindow.obj.close();
				infoTip({
					content: "保存成功！",
					color: "#D58512"
				});
			});
		},

		//取消
		cancelClick: function() {
			$("#cancelBtn").on("click", function() {
				infoWindow.obj.close();
			});
		},

		initContent: function(dataItem) {

			//填充弹窗内容
			infoWindow.obj.content(infoWindow.template(dataItem));

			//弹窗内，保存/取消按钮
			infoWindow.saveClick();
			infoWindow.cancelClick();

			infoWindow.obj.center().open();
		},

		init: function() {

			this.template = kendo.template($("#windowTemplate").html());

			if (!infoWindow.id.data("kendoWindow")) {
				infoWindow.id.kendoWindow({
					width: "700px",
					actions: ["Close"],
					modal: true,
					title: "号段管理"
				});
			}
			infoWindow.obj = infoWindow.id.data("kendoWindow");
		}
	};

	//用户成员弹窗
	var usersWindowObj = {

		obj: undefined,

		dataGrid: undefined,

		init: function() {

			if (!$("#usersWindow").data("kendoWindow")) {
				$("#usersWindow").kendoWindow({
					width: "900px",
					height: "400px",
					actions: ["Close"],
					modal: true,
					title: "用户成员"
				});
			}
			this.obj = $("#usersWindow").data("kendoWindow");
		},

		initGrid: function() {

			this.dataGrid = $("#usersGrid").kendoGrid({

				dataSource: {
					data: usersList
				},

				height: 300,

				reorderable: true,

				resizable: true,

				sortable: true,

				columnMenu: true,

				pageable: false,

				columns: [{
					field: "loginName",
					template: "<span  title='#:loginName#'>#:loginName#</span>",
					title: "<span  title='登录名称'>登录名称</span>"
				}, {
					field: "userName",
					template: "<span  title='#:userName#'>#:userName#</span>",
					title: "<span  title='用户姓名'>用户姓名</span>"
				}, {
					field: "roleName",
					template: "<span  title='#:roleName#'>#:roleName#</span>",
					title: "<span  title='角色名称'>角色名称</span>"
				}, {
					template: "#if(added=='false' && force=='false'){# <button added='false' force=#:force# class='addUserBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>" +
							  "#}else if(added=='false' && force=='true'){# <button added='false' force=#:force# class='addUserBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 强制加入</button> #}else{#" +
							  "<button added='true' force=#:force# class='addUserBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button> #}#",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function() {
					//每一行的加入/移除按钮
					$("#usersGrid td").delegate(".addUserBtns", "click", function() {
						var td = $(this).closest("td");
						var force = $(this).attr("force");
						var html = "";
						if ($(this).attr("added") == "true") {
							
							if(force == "true"){
								html = "<button added='false' force="+force+" class='addUserBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 强制加入</button>";
							} else {
								html = "<button added='false' force="+force+" class='addUserBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
							}
							
						} else {
							html = "<button added='true' force="+force+" class='addUserBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
						}
						td.html(html);
					});
				}
			}).data("kendoGrid");
		}
	};
	
	//指令权限弹窗
	var indicatorWindowObj = {

		obj: undefined,

		dataGrid: undefined,

		init: function() {

			if (!$("#indicatorWindow").data("kendoWindow")) {
				$("#indicatorWindow").kendoWindow({
					width: "900px",
					height: "400px",
					actions: ["Close"],
					modal: true,
					title: "指令权限"
				});
			}
			this.obj = $("#indicatorWindow").data("kendoWindow");

			this.addAllClick();
			this.removeAllClick();
		},
		addAllClick: function() {
			//全部加入
			$("#addAllIndicator").on("click", function() {
				var html = "<button added='true' class='addIndicatorBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
				$("#indicatorGrid .addIndicatorBtns[added='false']").parent().html(html);
			});
		},
		removeAllClick: function() {
			//全部移除
			$("#removeAllIndicator").on("click", function() {
				var html = "<button added='false' class='addIndicatorBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
				$("#indicatorGrid .addIndicatorBtns[added='true']").parent().html(html);
			});
		},

		initGrid: function() {

			this.dataGrid = $("#indicatorGrid").kendoGrid({

				dataSource: {
					data: indicatorList
				},

				height: 300,

				reorderable: true,

				resizable: true,

				sortable: true,

				columnMenu: true,

				pageable: false,

				columns: [{
					field: "netType",
					template: "<span  title='#:netType#'>#:netType#</span>",
					title: "<span  title='网元类型'>网元类型</span>"
				}, {
					field: "unitType",
					template: "<span  title='#:unitType#'>#:unitType#</span>",
					title: "<span  title='单元类型'>单元类型</span>"
				}, {
					field: "name",
					template: "<span  title='#:name#'>#:name#</span>",
					title: "<span  title='名称'>名称</span>"
				}, {
					field: "desc",
					template: "<span  title='#:desc#'>#:desc#</span>",
					title: "<span  title='描述'>描述</span>"
				}, {
					template: "#if(added=='false'){# <button added='false' class='addIndicatorBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button> #}else{#" +
						"<button added='true' class='addIndicatorBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button> #}#",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function() {
					//每一行的加入/移除按钮
					$("#indicatorGrid td").delegate(".addIndicatorBtns", "click", function() {
						var td = $(this).closest("td");
						var html = "";
						if ($(this).attr("added") == "true") {
							html = "<button added='false' class='addIndicatorBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
						} else {
							html = "<button added='true' class='addIndicatorBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
						}
						td.html(html);
					});
				}
			}).data("kendoGrid");
		}
	};
	
	//地区权限弹窗
	var areaWindowObj = {

		obj: undefined,

		dataGrid: undefined,

		init: function() {

			if (!$("#areaWindow").data("kendoWindow")) {
				$("#areaWindow").kendoWindow({
					width: "900px",
					height: "400px",
					actions: ["Close"],
					modal: true,
					title: "地区权限"
				});
			}
			this.obj = $("#areaWindow").data("kendoWindow");
		},

		initGrid: function() {

			this.dataGrid = $("#areaGrid").kendoGrid({

				dataSource: {
					data: areaList
				},

				height: 300,

				reorderable: true,

				resizable: true,

				sortable: true,

				columnMenu: true,

				pageable: false,

				columns: [{
					field: "loginName",
					template: "<span  title='#:name#'>#:name#</span>",
					title: "<span  title='地区'>地区</span>"
				}, {
					template: "#if(added=='false'){# <button added='false' class='addAreaBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>" +
							  "#}else{#<button added='true' class='addAreaBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button> #}#",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function() {
					//每一行的加入/移除按钮
					$("#areaGrid td").delegate(".addAreaBtns", "click", function() {
						var td = $(this).closest("td");
						var force = $(this).attr("force");
						var html = "";
						if ($(this).attr("added") == "true") {
							html = "<button added='false' class='addAreaBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
							
						} else {
							html = "<button added='true' class='addAreaBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
						}
						td.html(html);
					});
				}
			}).data("kendoGrid");
		}
	};
	
	//网元权限弹窗
	var netElementWindowObj = {

		obj: undefined,

		dataGrid: undefined,

		init: function() {

			if (!$("#netElementWindow").data("kendoWindow")) {
				$("#netElementWindow").kendoWindow({
					width: "900px",
					height: "400px",
					actions: ["Close"],
					modal: true,
					title: "网元权限"
				});
			}
			this.obj = $("#netElementWindow").data("kendoWindow");

			this.addAllClick();
			this.removeAllClick();
		},
		addAllClick: function() {
			//全部加入
			$("#addAllNetElement").on("click", function() {
				var html = "<button added='true' class='addNetElementBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
				$("#netElementGrid .addNetElementBtns[added='false']").parent().html(html);
			});
		},
		removeAllClick: function() {
			//全部移除
			$("#removeAllNetElement").on("click", function() {
				var html = "<button added='false' class='addNetElementBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
				$("#netElementGrid .addNetElementBtns[added='true']").parent().html(html);
			});
		},

		initGrid: function() {

			this.dataGrid = $("#netElementGrid").kendoGrid({

				dataSource: {
					data: netElementList
				},

				height: 300,

				reorderable: true,

				resizable: true,

				sortable: true,

				columnMenu: true,

				pageable: false,

				columns: [{
					field: "netName",
					template: "<span  title='#:netName#'>#:netName#</span>",
					title: "<span  title='网元名称'>网元名称</span>"
				}, {
					field: "netType",
					template: "<span  title='#:netType#'>#:netType#</span>",
					title: "<span  title='网元类型'>网元类型</span>"
				}, {
					field: "unitType",
					template: "<span  title='#:unitType#'>#:unitType#</span>",
					title: "<span  title='单元类型'>单元类型</span>"
				}, {
					template: "#if(added=='false'){# <button added='false' class='addNetElementBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button> #}else{#" +
						"<button added='true' class='addNetElementBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button> #}#",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function() {
					//每一行的加入/移除按钮
					$("#netElementGrid td").delegate(".addNetElementBtns", "click", function() {
						var td = $(this).closest("td");
						var html = "";
						if ($(this).attr("added") == "true") {
							html = "<button added='false' class='addNetElementBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
						} else {
							html = "<button added='true' class='addNetElementBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
						}
						td.html(html);
					});
				}
			}).data("kendoGrid");
		}
	};
	
	//系统功能权限弹窗
	var systemWindowObj = {

		obj: undefined,

		dataGrid: undefined,

		init: function() {

			if (!$("#systemWindow").data("kendoWindow")) {
				$("#systemWindow").kendoWindow({
					width: "900px",
					height: "400px",
					actions: ["Close"],
					modal: true,
					title: "系统功能权限"
				});
			}
			this.obj = $("#systemWindow").data("kendoWindow");

			this.addAllClick();
			this.removeAllClick();
		},
		addAllClick: function() {
			//全部加入
			$("#addAllSystem").on("click", function() {
				var html = "<button added='true' class='addSystemBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
				$("#systemGrid .addSystemBtns[added='false']").parent().html(html);
			});
		},
		removeAllClick: function() {
			//全部移除
			$("#removeAllSystem").on("click", function() {
				var html = "<button added='false' class='addSystemBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
				$("#systemGrid .addSystemBtns[added='true']").parent().html(html);
			});
		},

		initGrid: function() {

			this.dataGrid = $("#systemGrid").kendoGrid({

				dataSource: {
					data: funList
				},

				height: 300,

				reorderable: true,

				resizable: true,

				sortable: true,

				columnMenu: true,

				pageable: false,

				columns: [{
					field: "resourceName",
					template: "<span  title='#:resourceName#'>#:resourceName#</span>",
					title: "<span  title='系统功能'>系统功能</span>"
				}, {
					template: "#if(added=='false'){# <button added='false' class='addSystemBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button> #}else{#" +
						"<button added='true' class='addSystemBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button> #}#",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function() {
					//每一行的加入/移除按钮
					$("#systemGrid td").delegate(".addSystemBtns", "click", function() {
						var td = $(this).closest("td");
						var html = "";
						if ($(this).attr("added") == "true") {
							html = "<button added='false' class='addSystemBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
						} else {
							html = "<button added='true' class='addSystemBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
						}
						td.html(html);
					});
				}
			}).data("kendoGrid");
		}
	};
	
	//角色列表-初始化
	dataGridObj.init();
	
	//添加-修改 弹窗
	infoWindow.init();
	
	//指令权限弹窗-初始化
	indicatorWindowObj.init();
	
	//用户成员弹窗-初始化
	usersWindowObj.init();
	
	//地区弹窗-初始化
	areaWindowObj.init();
	
	//网元权限弹窗-初始化
	netElementWindowObj.init();
	
	//系统功能权限-初始化
	systemWindowObj.init();

});

var dataList = [{
	name: "客服中心",
	level: "一般操作人员",
	desc: "客服中心"
}, {
	name: "省级管理员助理",
	level: "管理员",
	desc: "省级管理员助理"
}, {
	name: "用户数据查询组",
	level: "一般操作人员",
	desc: "用户数据查询"
}, {
	name: "prOpe",
	level: "一般操作人员",
	desc: "维护员"
}, {
	name: "中级维护操作人员",
	level: "一般操作人员",
	desc: "集中登录，软硬件，接口，PGW日志、网元操作日志"
}, {
	name: "XMLLocalPro4",
	level: "一般操作人员",
	desc: "集中登录，安全，软硬件，接口，PGW日志、网元操作日志"
}, {
	name: "高级维护人员",
	level: "一般操作人员",
	desc: "集中登录，日常运维，PGW日志、网元操作日志"
}, {
	name: "中级维护人员",
	level: "一般操作人员",
	desc: "集中登录，软硬件，接口，局数据，PGW日志、网元操作日志"
}];
var usersList = [
	{
		loginName:"common",
		userName:"普通用户",
		roleName:"未分配用户组",
		dateTime:"2015-09-15",
		phone:"135981376768",
		email:"wuliang@136.com",
		remarks:"",
		force: "false",
		added:"false"
	}, {
		loginName:"yuxiaoping",
		userName:"余晓平",
		roleName:"客服中心",
		dateTime:"2015-08-12",
		phone:"135981376768",
		email:"sehu@136.com",
		remarks:"",
		force: "true",
		added:"false"
	}, {
		loginName:"panli",
		userName:"潘力",
		roleName:"初级维护人员",
		dateTime:"2015-08-12",
		phone:"135981376768",
		email:"sehu@136.com",
		remarks:"",
		force: "true",
		added:"false"
	}, {
		loginName:"wujiahui",
		userName:"吴佳慧",
		roleName:"高级维护人员",
		dateTime:"2015-09-20",
		phone:"15867567876",
		email:"sanxing@136.com",
		remarks:"",
		force: "true",
		added:"false"
	}, {
		loginName:"lihong3",
		userName:"李虹",
		roleName:"中级维护人员",
		dateTime:"2015-09-17",
		phone:"18767657865",
		email:"selin@136.com",
		remarks:"",
		force: "true",
		added:"false"
	}, {
		loginName:"yangchua",
		userName:"杨川",
		roleName:"中级维护人员",
		dateTime:"2015-09-17",
		phone:"135981376768",
		email:"asm@136.com",
		remarks:"",
		force: "true",
		added:"true"
	}, {
		loginName:"chenhuopao",
		userName:"陈火炮",
		roleName:"初级维护人员",
		dateTime:"2015-09-15",
		phone:"135981376768",
		email:"wuliang@136.com",
		remarks:"",
		force: "true",
		added:"false"
	}, {
		loginName:"linlili",
		userName:"林立立",
		roleName:"prOpe",
		dateTime:"2015-09-15",
		phone:"135981376768",
		email:"wuliang@136.com",
		remarks:"",
		force: "true",
		added:"true"
	}, {
		loginName:"lijing10",
		userName:"李静",
		roleName:"初级操作人员",
		dateTime:"2015-09-15",
		phone:"135981376768",
		email:"wuliang@136.com",
		remarks:"",
		force: "true",
		added:"false"
	}, {
		loginName:"feiqiang",
		userName:"费强",
		roleName:"初级操作人员",
		dateTime:"2015-09-15",
		phone:"135981376768",
		email:"wuliang@136.com",
		remarks:"",
		force: "true",
		added:"true"
	}, {
		loginName:"youjun1",
		userName:"尤骏",
		roleName:"初级操作人员",
		dateTime:"2015-09-15",
		phone:"135981376768",
		email:"wuliang@136.com",
		remarks:"",
		force: "true",
		added:"true"
	}, {
		loginName:"chenwei",
		userName:"陈伟",
		roleName:"中级操作人员",
		dateTime:"2015-09-15",
		phone:"135981376768",
		email:"wuliang@136.com",
		remarks:"",
		force: "true",
		added:"false"
	}
];
var indicatorList = [
	{
		netType:"NTHLRFE",
		unitType:"PCC",
		name:"查看命令组",
		desc:"查看命令组",
		added: 'false'
	}, {
		netType:"NTHLRFE",
		unitType:"PCC",
		name:"维护命令组",
		desc:"维护命令组",
		added: 'false'
	}, {
		netType:"NTHLRFE",
		unitType:"PCC",
		name:"高级命令组",
		desc:"高级命令组",
		added: 'false'
	}, {
		netType:"NTHLRFE",
		unitType:"AHUB",
		name:"查看命令组",
		desc:"查看命令组",
		added: 'false'
	}, {
		netType:"NTHLRFE",
		unitType:"AHUB",
		name:"维护命令组",
		desc:"维护命令组",
		added: 'false'
	}, {
		netType:"NTHLRFE",
		unitType:"AHUB",
		name:"高级命令组",
		desc:"高级命令组",
		added: 'false'
	}, {
		netType:"NTHLRFE",
		unitType:"TIAMS",
		name:"查看命令组",
		desc:"查看命令组",
		added: 'false'
	}, {
		netType:"NTHLRFE",
		unitType:"TIAMS",
		name:"维护命令组",
		desc:"维护命令组",
		added: 'false'
	}, {
		netType:"NTHLRFE",
		unitType:"HLRFE",
		name:"高级命令组",
		desc:"高级命令组",
		added: 'false'
	}, {
		netType:"NTHLRFE",
		unitType:"HLRFE",
		name:"维护命令组",
		desc:"维护命令组",
		added: 'false'
	}, {
		netType:"NTHLRFE",
		unitType:"HLRFE",
		name:"高级命令组",
		desc:"高级命令组",
		added: 'false'
	}
];

var netElementList = [
	{
		netName:"FZHSS05FE01BNK",
		netType:"HSSFE",
		unitType:"AHUB",
		added:"false"
	}, {
		netName:"FZHSS05FE01BNK",
		netType:"HSSFE",
		unitType:"DRA",
		added:"false"
	}, {
		netName:"FZHSS05FE01BNK",
		netType:"HSSFE",
		unitType:"HSSFE",
		added:"false"
	}, {
		netName:"FZHSS05FE01BNK",
		netType:"HSSFE",
		unitType:"TIAMS",
		added:"false"
	}, {
		netName:"FZHSS05FE11BNK",
		netType:"NTHLRFE",
		unitType:"AHUB",
		added:"false"
	}, {
		netName:"FZHSS05FE11BNK",
		netType:"NTHLRFE",
		unitType:"HLRFE",
		added:"false"
	}, {
		netName:"FZHSS05FE11BNK",
		netType:"NTHLRFE",
		unitType:"PCC",
		added:"false"
	}, {
		netName:"FZHSS05FE11BNK",
		netType:"NTHLRFE",
		unitType:"TIAMS",
		added:"false"
	}, {
		netName:"FZHSS05BE01BNK",
		netType:"ONE-NDS",
		unitType:"ADM",
		added:"false"
	}, {
		netName:"FZHSS05BE01BNK",
		netType:"ONE-NDS",
		unitType:"BE-DSA",
		added:"false"
	}
];

var areaList=[
	{
		name:"福州市",
		added:"false"
	}, {
		name:"厦门市",
		added:"false"
	}, {
		name:"宁德市",
		added:"false"
	}, {
		name:"莆田市",
		added:"false"
	}, {
		name:"泉州市",
		added:"false"
	}, {
		name:"漳州市",
		added:"false"
	}, {
		name:"龙岩市",
		added:"false"
	}, {
		name:"三明市",
		added:"false"
	}, {
		name:"南平市",
		added:"false"
	}
];

var funList=[
	{
		resourceName:"集中登录 - 集中登录",
		added:"true"
	}, {
		resourceName:"日常运维 - 安全管理",
		added:"true"
	}, {
		resourceName:"日常运维 - 软硬件维护",
		added:"true"
	}, {
		resourceName:"日常运维 - 网络接口维护",
		added:"true"
	}, {
		resourceName:"日常运维 - 局数据查询",
		added:"true"
	}, {
		resourceName:"高级功能 - BOSS业务实时监控",
		added:"false"
	}, {
		resourceName:"高级功能 - 用户BOSS业务查询",
		added:"false"
	}, {
		resourceName:"高级功能 - PGW日志验证",
		added:"false"
	}, {
		resourceName:"高级功能 - 用户数据查询",
		added:"false"
	}, {
		resourceName:"高级功能 - 用户数据管理",
		added:"false"
	}, {
		resourceName:"高级功能 - 网元操作日志",
		added:"false"
	}, {
		resourceName:"高级功能 - 智能巡检",
		added:"false"
	}, {
		resourceName:"系统管理 - 号段管理",
		added:"false"
	}, {
		resourceName:"系统管理 - 地区管理",
		added:"false"
	}, {
		resourceName:"系统管理 - 网元管理",
		added:"false"
	}, {
		resourceName:"系统管理 - 单元管理",
		added:"false"
	}, {
		resourceName:"系统管理 - 用户管理",
		added:"false"
	}, {
		resourceName:"系统管理 - 角色管理",
		added:"false"
	}, {
		resourceName:"系统管理 - 指令管理",
		added:"false"
	}, {
		resourceName:"系统管理 - 指令组管理",
		added:"false"
	}
];
