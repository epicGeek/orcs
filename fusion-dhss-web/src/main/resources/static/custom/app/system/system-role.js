kendo.culture("zh-CN");
var roleItemIds;
var itemHref;
$(function() {
	var searchparams = { 
		page : 1,
		size : 10,
		searchField : ''
	};
	var allUser = [];
	var allUnit = [];
	$("#roleName").on("keyup",function(){
		searchparams.searchField = $("#roleName").val();
		dataGridObj.dataGrid.dataSource.read(searchparams);
	})
	$("#clearInput").on("click",function(){
		$("#roleName").val("");
		searchparams.searchField = $("#roleName").val();
		dataGridObj.dataGrid.dataSource.read(searchparams);
	})
	
	
	//获取所有用户列表
	
	$.ajax({
		url:"rest/system-user",
		type:"GET",
		dataType : "json",
		success:function(data){
			if(data.hasOwnProperty("_embedded")){
				$.ajax({
					url:"my/profile?time="+new Date().getTime(),
					method:"GET",
					success:function(user){
//						if(user.userName == "admin"){
							allUser = data["_embedded"]["system-user"];
//						}else{
//							$.each(data["_embedded"]["system-user"],function(index,item){
//								if(item.creator.indexOf(user.userName+"@") != -1){
//									allUser[allUser.length] = item;
//								}
//							})
//						}
					}
				});
			}
		}
	});
	var dataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
				url : "rest/system-role/search/findPageByFilter",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			},
			parameterMap : function(options, operation) {
				if (operation == "read") {
					searchparams.page = options.page - 1;
					searchparams.size = options.pageSize;
					searchparams.searchField = $("#roleName").val();
					return searchparams;
				}
			}
		},
		batch : true,
		pageSize : 20,
		requestEnd : function(e) {
			var response = e.response;
			if (response) {
				var type = e.type;
				if (type != 'read') {
					this.read();
				}
			} else {
				alert("服务器异常，请重试！");
			}
		},
		schema : {
			data : function(data) {
				if (data["_embedded"]) {
					return data["_embedded"]["system-role"];// 返回页面数据
				} else {
					return [];
				}
			},
			total : function(d) {
				if (d.page) {
					return d.page.totalElements; // 总条数
				}
			}
		},
		serverPaging : true,
		serverFiltering : true,
		serverSorting : true
	});
	var dataGridObj = {
		init : function() {
			this.dataGrid = $("#dataGrid")
					.kendoGrid(
							{
								dataSource : dataSource,
								height : $(window).height() - $("#dataGrid").offset().top - 50,
								reorderable : true,
								resizable : true,
								sortable : false,
								columnMenu : false,
								pageable : true,
								columns: [{
									width: 150,
									field: "roleName",
									template: "<span  title='#:isNotEmpty(roleName)#'>#:isNotEmpty(roleName)#</span>",
									title: "<span  title='角色名称'>角色名称</span>"
								},{
									field: "roleDesc",
									template: "<span  title='#:isNotEmpty(roleDesc)#'>#:isNotEmpty(roleDesc)#</span>",
									title: "<span  title='描述'>描述</span>"
								}, {
									width: 500,
									template: "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 编辑</button>&nbsp;&nbsp;" +
										"<button class='deleteBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon-remove-sign'></i> 删除</button>&nbsp;&nbsp;" +
										"<button class='userItemBtn btn btn-xs btn-default' data-role-name='#:roleName#'>用户成员</button></button>&nbsp;&nbsp;"+
										"<button class='commandItemBtn btn btn-xs btn-default' data-role-name='#:roleName#'>指令权限</button>&nbsp;&nbsp;"+
										"<button class='areaItemBtn btn btn-xs btn-default' data-role-name='#:roleName#'>地区权限</button>&nbsp;&nbsp;"+
										"<button class='neItemBtn btn btn-xs btn-default' data-role-name='#:roleName#'>网元权限</button>&nbsp;&nbsp;"+
										"<button class='menuItemBtn btn btn-xs btn-default' data-role-name='#:roleName#'>系统功能权限</button>",
									title: "<span  title='操作'>操作</span>"
								}],
								dataBound : function() {
									dataGridObj.addClick();
									dataGridObj.editClick();
									dataGridObj.deleteClick();
									dataGridObj.refreshClick();
									dataGridObj.clearFilterClick();
									dataGridObj.userItemClick();
									dataGridObj.commandItemClick();
									dataGridObj.areaItemClick();
									dataGridObj.neItemClick();
									dataGridObj.menuItemClick();
								}
							}).data("kendoGrid");
		},
		// 添加按钮，显示弹窗
		addClick : function() {
			$("#addBtn").on("click", function() {
				var dataItem = {
					href : "rest/system-role/",
					roleName : "",
					roleDesc : ""
				};
				infoWindow.obj.setOptions({
					"title" : "添加"
				});
				infoWindow.initContent(dataItem, true);
			});
		},
		// 编辑
		editClick : function() {
			$(".editBtn").on("click",function() {
						var pickRow = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
						var itemHref = (pickRow._links.self.href);
						$.ajax({
							dataType : 'json',
							type : "GET",
							url : itemHref,
							success : function(data) {
								infoWindow.obj.setOptions({
									"title" : "修改" + "【 <span style='color:blue;'>" + data.roleName + "</span> 】"
								});
								data.href = data._links.self.href;
								infoWindow.initContent(data, false);
								dataGridObj.dataGrid.dataSource.read(searchparams);	
							},
							fail : function(data) {
								showNotify(data.message, "error");
							}
						});
					});
		},
		// 编辑用户列表
		userItemClick : function() {
			$(".userItemBtn").on("click",function() {
				
						
				
						var pickRow = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
						itemHref = (pickRow._links.systemUser.href);
						$.ajax({
							dataType : 'json',
							type : "GET",
							url : itemHref,
							success : function(data) {
								var gridData = [];
								if(data["_embedded"]){
									var requestUserList = data["_embedded"]["system-user"];
									$.each(requestUserList,function(index,userItem){
										gridData[gridData.length] = (userItem.userName);
									});

								}
								$.each(allUser,function(index,item){
									if($.inArray(item.userName, gridData)!=-1){
										item.added = "true";
										item.force = "true";
									}else{
										item.added = "false";
										item.force = "false";
									}
								});
								userItemWindowObj.obj.setOptions({
									"title" : "修改" + "【 <span style='color:blue;'>" + pickRow.roleName + "</span> 】的 用户成员"
								});
								userItemWindowObj.restfulURL = itemHref;
								userItemWindowObj.obj.center().open();
								userItemWindowObj.initGrid(allUser);
							},
							fail : function(data) {
								showNotify(data.message, "error");
							}
						});
					});
		},
		commandItemClick : function () {
			$(".commandItemBtn").on("click",function(){
				var pickRow = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
				var itemHref = (pickRow._links.systemResource.href);
				$.ajax({
					dataType : 'json',
					type : "GET",
					url : itemHref,
					success : function(checkDatas) {
						$.ajax({
							dataType : 'json',
							type : "GET",
							url : "rest/command-group/search/list",
							success : function(data) {
								var dataIds = [];
								if(checkDatas["_embedded"]){
									if(checkDatas["_embedded"]["command-group"]){
										$.each(checkDatas["_embedded"]["command-group"],function(index,item){
											var link = item._links.self.href;
											dataIds[dataIds.length] = link.substring(link.lastIndexOf("/")+1);
										})
									}
								}
								var tempIds = [];
								$.each(data,function(index,item){
									tempIds[tempIds.length] = item.id;
								})
								commandItemWindowObj.obj.setOptions({
									"title" : "修改" + "【 <span style='color:blue;'>" + pickRow.roleName + "</span> 】的 指令权限"
								});
								var results = [];
								results[0] = data;
								results[1] = dataIds;
								results[2] = tempIds;
								commandItemWindowObj.restfulURL = itemHref;
								commandItemWindowObj.obj.center().open();
								commandItemWindowObj.initGrid(results);
							},
							fail : function(data) {
								showNotify(data.message, "error");
							}
						});
					},
					fail : function(data) {
						showNotify(data.message, "error");
					}
				});
				
			})
		},
		areaItemClick : function () {
			$(".areaItemBtn").on("click",function(){
				var pickRow = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
				var itemHref = (pickRow._links.systemResource.href);
				$.ajax({
					dataType : 'json',
					type : "GET",
					url : itemHref,
					success : function(checkDatas) {
						$.ajax({
							dataType : 'json',
							type : "GET",
							url : "rest/system-area",
							success : function(data) {
								var dataIds = [];
								if(checkDatas["_embedded"]){
									if(checkDatas["_embedded"]["system-area"]){
										$.each(checkDatas["_embedded"]["system-area"],function(index,item){
											var link = item._links.self.href;
											dataIds[dataIds.length] = link.substring(link.lastIndexOf("/")+1);
										})
									}
								}
								var tempIds = [];
								if(data["_embedded"]){
									if(data["_embedded"]["system-area"]){
										$.each(data["_embedded"]["system-area"],function(index,item){
											var link = item._links.self.href;
											tempIds[tempIds.length] = link.substring(link.lastIndexOf("/")+1);
										});
										data = data["_embedded"]["system-area"];
									}
								}else{
									data = [];
								}
								areaItemWindowObj.obj.setOptions({
									"title" : "修改" + "【 <span style='color:blue;'>" + pickRow.roleName + "</span> 】的 地区权限"
								});
								var results = [];
								results[0] = data;
								results[1] = dataIds;
								results[2] = tempIds;
								areaItemWindowObj.restfulURL = itemHref;
								areaItemWindowObj.obj.center().open();
								areaItemWindowObj.initGrid(results);
							},
							fail : function(data) {
								showNotify(data.message, "error");
							}
						});
					},
					fail : function(data) {
						showNotify(data.message, "error");
					}
				});
			});
		},
		neItemClick : function () {
			$(".neItemBtn").on("click",function(){
				var pickRow = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
				//获取所有单元列表
				$.ajax({
					dataType : 'json',
					type : "GET",
					data : { searchField : $("#unitNameInput").val()},
					url : /*"rest/equipment-unit"*/"equipment-unit/search/all",
					success : function(data) {
						allUnit = data;
						if(allUnit.length==0){
							infoTip({content : "可用网元列表为空,无法授权"});
							return;
						}
						itemHref = (pickRow._links.systemResource.href);
						$.ajax({
							dataType : 'json',
							type : "GET",
							url : itemHref,
							success : function(checkDatas) {
										var dataIds = [];
										if(checkDatas["_embedded"]){
											if(checkDatas["_embedded"]["equipment-unit"]){
												$.each(checkDatas["_embedded"]["equipment-unit"],function(index,item){
													link = item._links.self.href;
													dataIds[dataIds.length] = link.substring(link.lastIndexOf("/")+1);
												})
											}
										}
										var tempIds = [];
										$.each(allUnit,function(index,item){
											link = "rest/equipment-unit/"+item.id;
											tempIds[tempIds.length] = item.id;
										});
										neItemWindowObj.obj.setOptions({
											"title" : "修改" + "【 <span style='color:blue;'>" + pickRow.roleName + "</span> 】的网元权限"
										});
										var results = [];
										results[0] = allUnit;
										results[1] = dataIds;
										results[2] = tempIds;
										neItemWindowObj.restfulURL = itemHref;
										neItemWindowObj.obj.center().open();
										neItemWindowObj.initGrid(results);
							},
							fail : function(data) {
								showNotify(data.message, "error");
							}
						});
					}
				});
			})
		},
		menuItemClick : function () {
			$(".menuItemBtn").on("click",function(){
				var pickRow = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
				var itemHref = (pickRow._links.systemResource.href);
				$.ajax({
					dataType : 'json',
					type : "GET",
					url : itemHref,
					success : function(checkDatas) {
						$.ajax({
							dataType : 'json',
							type : "GET",
							url : "rest/system-menu",
							success : function(data) {
								var dataIds = [];
								if(checkDatas["_embedded"]){
									if(checkDatas["_embedded"]["system-menu"]){
										$.each(checkDatas["_embedded"]["system-menu"],function(index,item){
											var link = item._links.self.href;
											dataIds[dataIds.length] = link.substring(link.lastIndexOf("/")+1);
										})
									}
								}
								var tempIds = [];
								if(data["_embedded"]){
									if(data["_embedded"]["system-menu"]){
										$.each(data["_embedded"]["system-menu"],function(index,item){
											var link = item._links.self.href;
											var id = link.substring(link.lastIndexOf("/")+1);
											tempIds[tempIds.length] = id;
										});
										data = data["_embedded"]["system-menu"];
									}
								}else{
									data = [];
								}
								menuItemWindowObj.obj.setOptions({
									"title" : "修改" + "【 <span style='color:blue;'>" + pickRow.roleName + "</span> 】的 系统功能权限"
								});
								var results = [];
								results[0] = data;
								results[1] = dataIds;
								results[2] = tempIds;
								menuItemWindowObj.restfulURL = itemHref;
								menuItemWindowObj.obj.center().open();
								menuItemWindowObj.initGrid(results);
							},
							fail : function(data) {
								showNotify(data.message, "error");
							}
						});
					},
					fail : function(data) {
						showNotify(data.message, "error");
					}
				});
			})
		},
		// 删除
		deleteClick : function() {
			$(".deleteBtn").on("click",function() {
				if (confirm("确定删除吗？")) {
					var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
					var deleteURL = (dataItem._links.self.href);
					$.ajax({
						url : deleteURL,
						type : "DELETE",
						success : function(data) {
							dataGridObj.dataGrid.dataSource.remove(dataItem);
							infoTip({content : "删除成功！"});
							dataGridObj.dataGrid.dataSource.read(searchparams);
						}
					});
				}
			});
		},
		refreshClick : function() {
			$("#button-search").on("click", function() {
				
			});
		},

		clearFilterClick : function() {
			$("#button-reset").on("click", function() {
				
			});
		}
	};

	// 弹窗role
	var infoWindow = {
		obj : undefined,
		template : undefined,
		id : $("#infoWindow"),
		// 保存 【添加】/
		saveClick : function() {
			$("#saveBtn").on("click",function() {
					var data_to_save = {};
					var data_to_save_url = $("#input-href").val();
					data_to_save.roleDesc = $("#input-roleDesc").val();
					data_to_save.roleName = $("#input-roleName").val();
					if(data_to_save.roleName == ""){
						infoTip({
							content : "角色名称不能为空！",
							color : "#D58512"
						});
					}else{
						// 此为修改
						$.ajax({
							dataType : 'json',
							type : (data_to_save_url == "rest/system-role/") ? "POST": "PATCH",
							url : data_to_save_url,
							contentType : "application/json;charset=UTF-8",
							data : kendo.stringify(data_to_save),
							success : function(data) {
								infoWindow.obj.close();
								infoTip({
									content : "保存成功!",
									color : "#D58512"
								});
								dataGridObj.dataGrid.dataSource.read(searchparams);
							},error:function(data){
								infoTip({
									content : "角色名称不能重复!",
									color : "#D58512"
								});
							}
						});
					}
					
			 });
		},

		// 取消
		cancelClick : function() {
			$("#cancelBtn").on("click", function() {
				infoWindow.obj.close();
			});
		},

		initContent : function(dataItem, addItem) {
			// 填充弹窗内容
			infoWindow.obj.content(infoWindow.template(dataItem));

			// 如果是添加，隐藏密码有效期字段4
			if (addItem) {
				$("#dateTimeWrap").hide();
			} else {
				$("#dateTimeWrap").show();
				if (!$("#input-expireDate").data("kendoDatePicker")) {
					$("#input-expireDate").kendoDatePicker({
						format : "yyyy-MM-dd"
					});
				}
			}
			//弹窗内，保存/取消按钮
			infoWindow.saveClick();
			infoWindow.cancelClick();
			infoWindow.obj.center().open();

		},

		init : function() {
			this.template = kendo.template($("#windowTemplate").html());
			if (!infoWindow.id.data("kendoWindow")) {
				infoWindow.id.kendoWindow({
					width : "700px",
					actions : [ "Close" ],
					modal : true,
					title : ""
				});
			}
			infoWindow.obj = infoWindow.id.data("kendoWindow");

		}
	};
	//用户成员弹窗
	var userItemWindowObj = {
			obj: undefined,
			dataGrid: undefined,
			init: function() {
				initWindow("#usersWindow","用户成员","900px","400px");
				this.obj = $("#usersWindow").data("kendoWindow");
				this.selectClick();
				this.clearInput();
			},
			selectClick: function() {
				$("#userNameInput").on("keyup",function(){
					var data = [];
					var text = $("#userNameInput").val();
					$.ajax({
						dataType : 'json',
						type : "GET",
						url : itemHref,
						success : function(datas) {
							var gridData = [];
							if(datas["_embedded"]){
								var requestUserList = datas["_embedded"]["system-user"];
								$.each(requestUserList,function(index,userItem){
									gridData[gridData.length] = (userItem.userName);
								});
							}
							if(text == ""){
								data = allUser;
							}else{
								$.each(allUser,function(index,item){
									if((item.userName !=null && item.userName.indexOf(text) != -1) || (item.realName != null &&  item.realName.indexOf(text) != -1)){
										data[data.length] = item;
									}
								})
							}
							$.each(data,function(index,item){
								if($.inArray(item.userName, gridData)!=-1){
									item.added = "true";
									item.force = "true";
								}else{
									item.added = "false";
									item.force = "false";
								}
								data[index] = item;
							})
							userItemWindowObj.initGrid(data);
						},
						fail : function(data) {
							showNotify(data.message, "error");
						}
					});
					
				})
			},
			clearInput: function() {
				$("#userClear").on("click",function(){
					$("#userNameInput").val("");
					var data = [];
					$.ajax({
						dataType : 'json',
						type : "GET",
						url : itemHref,
						success : function(datas) {
							var gridData = [];
							if(datas["_embedded"]){
								var requestUserList = datas["_embedded"]["system-user"];
								$.each(requestUserList,function(index,userItem){
									gridData[gridData.length] = (userItem.userName);
								});
							}
							$.each(allUser,function(index,item){
									//tempIds[tempIds.length] = item.id;
									if($.inArray(item.userName, gridData)!=-1){
										item.added = "true";
										item.force = "true";
									}else{
										item.added = "false";
										item.force = "false";
									}
									data[index] = item;
							})
							userItemWindowObj.initGrid(data);
						}
					});
				})
			},
			initGrid: function(data) {
				var roleUrl = (this.restfulURL);
				this.dataGrid = $("#usersGrid").kendoGrid({
					dataSource: {
						data: data
					},
					height: 300,
					reorderable: true,
					resizable: true,
					sortable: true,
					columnMenu: true,
					pageable: false,
					columns: [{
						field: "loginName",
						template: "<span  title='#:isNotEmpty(userName)#'>#:isNotEmpty(userName)#</span>",
						title: "<span  title='登录名称'>登录名称</span>"
					}, {
						field: "userName",
						template: "<span  title='#:isNotEmpty(realName)#'>#:isNotEmpty(realName)#</span>",
						title: "<span  title='用户姓名'>用户姓名</span>"
					},
					{
						template: "#if(added=='false' && force=='false'){# <button added='false' force=#:force# class='addUserBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>" +
								  "#}else{#" +
								  "<button added='true' force=#:force# class='addUserBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button> #}#",
						title: "<span  title='操作'>操作</span>"
					}],
					dataBound: function() {
						//每一行的加入/移除按钮
						$("#usersGrid td").delegate(".addUserBtns", "click", function() {
							
							var userUrl = userItemWindowObj.dataGrid.dataItem($(this).closest("tr"))._links.self.href;
							var userId = userUrl.substring(userUrl.lastIndexOf("/")+1);
							var td = $(this).closest("td");
							var force = $(this).attr("force");
							var html = "";
							if ($(this).attr("added") == "true") {
								$.ajax({
									url : roleUrl+"/"+userId,
									type : "DELETE",
									dataType : "json",
									success:function(data){
										html = "<button added='false' force="+force+" class='addUserBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
										td.html(html);
										infoTip({content : "移除成功！" });
									}
								});
							} else {
								$.ajax({
								    url : roleUrl,
								    type : "PATCH",
								    data: "system-user/" + userId,
								    dataType: "json",
								    contentType: "text/uri-list",
								    success : function(data) {
								    	html = "<button added='true' force="+force+" class='addUserBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
										td.html(html);
										infoTip({content : "加入成功！",
											color : "#D58512"});
								    }
								});
							}
							
						});
					}
				}).data("kendoGrid");
			}
	};
	
	//指令权限弹窗
	var commandItemWindowObj = {
		uri : undefined,
		tempIds : undefined,
		obj: undefined,
		dataGrid: undefined,
		init: function() {
			initWindow("#indicatorWindow","指令权限","900px","400px");
			this.obj = $("#indicatorWindow").data("kendoWindow");
			this.addAllClick();
			this.removeAllClick();
			this.selectClick();
			this.clearInput();
		},
		selectClick: function() {
			$("#commandName").on("keyup",function(){
				$.ajax({
					dataType : 'json',
					type : "GET",
					data : { searchField : $("#commandName").val() },
					url : "rest/command-group/search/list",
					success : function(data) {
						var tempIds = [];
						$.each(data,function(index,item){
							tempIds[tempIds.length] = item.id;
						})
						var results = [];
						results[0] = data;
						results[1] = roleItemIds;
						results[2] = tempIds;
						commandItemWindowObj.initGrid(results);
					},
					fail : function(data) {
						showNotify(data.message, "error");
					}
				});
			})
		},
		clearInput: function() {
			$("#commandClear").on("click",function(){
				$("#commandName").val("");
				$.ajax({
					dataType : 'json',
					type : "GET",
					data : { searchField : "" },
					url : "rest/command-group/search/list",
					success : function(data) {
						var tempIds = [];
						$.each(data,function(index,item){
							tempIds[tempIds.length] = item.id;
						})
						var results = [];
						results[0] = data;
						results[1] = roleItemIds;
						results[2] = tempIds;
						commandItemWindowObj.initGrid(results);
					},
					fail : function(data) {
						showNotify(data.message, "error");
					}
				});
			})
		},
		addAllClick: function() {
			//全部加入
			$("#addAllIndicator").on("click", function() {
				var html = "<button added='true' class='addIndicatorBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
				var dataView = commandItemWindowObj.dataGrid.dataSource.view();
				var ids = [];
				$.each(dataView,function(index,item){
					ids[ids.length] = item.id; 
				})
				addAll(ids,uri,"#indicatorGrid .addIndicatorBtns[added='false']",html);
			}); 
		},
		removeAllClick: function() {
			//全部移除
			$("#removeAllIndicator").on("click", function() {
		    	var html = "<button added='false' class='addIndicatorBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
		    	var dataView = neItemWindowObj.dataGrid.dataSource.view();
				var ids = [];
				$.each(dataView,function(index,item){
					ids[ids.length] = item.id; 
				})
		    	delAll(uri,"#indicatorGrid .addIndicatorBtns[added='true']",html,ids);
			});
		},

		initGrid: function(data) {
			var roleUrl = (this.restfulURL);
			uri = roleUrl;
			roleItemIds = data[1];
			tempIds = data[2];
			this.dataGrid = $("#indicatorGrid").kendoGrid({
				dataSource: {
					data: data[0]
				},
				height: 300,
				reorderable: true,
				resizable: true,
				sortable: true,

				columnMenu: true,

				pageable: false,

				columns: [{
					field: "neType",
					template: "<span  title='#:isNotEmpty(neType)#'>#:isNotEmpty(neType)#</span>",
					title: "<span  title='网元类型'>网元类型</span>"
				}, {
					field: "unitType",
					template: "<span  title='#:isNotEmpty(unitType)#'>#:isNotEmpty(unitType)#</span>",
					title: "<span  title='单元类型'>单元类型</span>"
				}, {
					field: "commandGroupName",
					template: "<span  title='#:isNotEmpty(commandGroupName)#'>#:isNotEmpty(commandGroupName)#</span>",
					title: "<span  title='名称'>名称</span>"
				}, {
					field: "commandGroupDesc",
					template: "<span  title='#:isNotEmpty(commandGroupDesc)#'>#:isNotEmpty(commandGroupDesc)#</span>",
					title: "<span  title='描述'>描述</span>"
				}, {
					template: "#if(selectLink('id/'+id) == -1){# <button added='false' class='addIndicatorBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button> #}else{#" +
					"<button added='true'  class='addIndicatorBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button> #}#",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function() {
					//每一行的加入/移除按钮
					$("#indicatorGrid td").delegate(".addIndicatorBtns", "click", function() {
						var groupId = commandItemWindowObj.dataGrid.dataItem($(this).closest("tr")).id;
						var td = $(this).closest("td");
						var html = "";
						if ($(this).attr("added") == "true") {
							$.ajax({
								url : roleUrl+"/"+groupId,
								type : "DELETE",
								dataType : "json",
								success:function(data){
									clearCache();
									html = "<button added='false' class='addIndicatorBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
									td.html(html);
									infoTip({content : "移除成功！" });
								}
							});
						} else {
							$.ajax({
							    url : roleUrl,
							    type : "PATCH",
							    data: "commandGroupId/" + groupId,
							    dataType: "json",
							    contentType: "text/uri-list",
							    success : function(data) {
							    	clearCache();
									html = "<button added='true' class='addIndicatorBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
									td.html(html);
									infoTip({content : "加入成功！",
										color : "#D58512"});
							    }
							});
						}
					});
				}
			}).data("kendoGrid");
		}
	};
	
	//地区权限弹窗
	var areaItemWindowObj = {
		uri : undefined,
		tempIds : undefined,
		obj: undefined,
		dataGrid: undefined,
		init: function() {
			initWindow("#areaWindow","地区权限","900px","400px");
			this.obj = $("#areaWindow").data("kendoWindow");
		},

		initGrid: function(data) {
			var roleUrl = (this.restfulURL);
			uri = roleUrl;
			roleItemIds = data[1];
			tempIds = data[2];
			this.dataGrid = $("#areaGrid").kendoGrid({

				dataSource: {
					data: data[0]
				},

				height: 370,

				reorderable: true,

				resizable: true,

				sortable: true,

				columnMenu: true,

				pageable: false,

				columns: [{
					field: "areaName",
					template: "<span  title='#:isNotEmpty(areaName)#'>#:isNotEmpty(areaName)#</span>",
					title: "<span  title='地区'>地区</span>"
				}, {
					template: "#if(selectLink(_links.self.href) == -1){# <button added='false' class='addAreaBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>" +
							  "#}else{#<button added='true' class='addAreaBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button> #}#",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function() {
					//每一行的加入/移除按钮
					$("#areaGrid td").delegate(".addAreaBtns", "click", function() {
						var groupId = areaItemWindowObj.dataGrid.dataItem($(this).closest("tr"))._links.self.href;
						var td = $(this).closest("td");
						var html = "";
						if ($(this).attr("added") == "true") {
							$.ajax({
								url : roleUrl +"/"+ groupId.substring(groupId.lastIndexOf("/")+1),
								type : "DELETE",
								dataType : "json",
								success:function(data){
									clearCache();
									html = "<button added='false' class='addAreaBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
									td.html(html);
									infoTip({content : "移除成功！" });
								}
							});
						} else {
							$.ajax({
							    url : roleUrl,
							    type : "PATCH",
							    data: groupId,
							    dataType: "json",
							    contentType: "text/uri-list",
							    success : function(data) {
							    	clearCache();
									html = "<button added='true' class='addAreaBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
									td.html(html);
									infoTip({content : "加入成功！",
										color : "#D58512"});
							    }
							});
						}
					});
				}
			}).data("kendoGrid");
		}
	};
	
	//网元权限弹窗
	var neItemWindowObj = {
		uri : undefined,
		tempIds : undefined,
		obj: undefined,
		dataGrid: undefined,
		init: function() {
			$.ajax({
				url:"rest/equipment-ne?projection=equipmentNeWithAssociation",
				dataType:"json",
				success:function(data){
					var list = data["_embedded"] ? data["_embedded"]["equipment-ne"] : [];
					$("#inputNeTrigger").kendoDropDownList({
						optionLabel:"全部网元",
						dataTextField: "neName",
						dataValueField: "neName",
						dataSource: list,
						filter: "contains",
						suggest: true,
						change:function(){
							 
						}
					});
					var dhssNames = [];
					$.each(list,function(index,item){
						if($.inArray(item.dhssName,dhssNames) == -1 ){
							dhssNames[dhssNames.length] = item.dhssName;
						}
					});
					$("#inputDhssName").kendoDropDownList({
						optionLabel:"全部DHSS组",
						dataSource: dhssNames,
						filter: "contains",
						suggest: true,
						change:function(){
							 
						}
					});
				}
			});
			$.ajax({
				url:"equipment-unitType/search/list",
				dataType:"json",
				success:function(data){
					$("#unitTypeList").kendoDropDownList({
						optionLabel:"全部单元类型",
						dataSource: data,
						filter: "contains",
						suggest: true,
						change:function(){
							 
						}
					});
				}
			});
			
			$.ajax({
				url:"equipment-neType/search/list",
				dataType:"json",
				success:function(data){
					$("#neTypeList").kendoDropDownList({
						optionLabel:"全部网元类型",
						dataSource: data,
						filter: "contains",
						suggest: true,
						change:function(){
						}
					});
				}
			});
			
			
			initWindow("#netElementWindow","网元权限","900px","450px");
			this.obj = $("#netElementWindow").data("kendoWindow");

			this.addAllClick();
			this.removeAllClick();
			this.selectBtn();
			this.clearUnitInput();
		},
		selectBtn:function() {
			$("#unitTypeList,#neTypeList,#inputNeTrigger,#inputDhssName,#unitNameInput").on("change keyup",function(){
				console.log("a");
				$.ajax({
					dataType : 'json',
					type : "GET",
					url : itemHref,
					success : function(checkDatas) {
								var dataIds = [];
								if(checkDatas["_embedded"]){
									if(checkDatas["_embedded"]["equipment-unit"]){
										$.each(checkDatas["_embedded"]["equipment-unit"],function(index,item){
											dataIds[dataIds.length] = item._links.self.href.substring(item._links.self.href.lastIndexOf("/")+1);
										})
									}
								}
								var tempIds = [];
								var selectData = [];
								var neid = $("#inputNeTrigger").val();
								var unitName = $("#unitNameInput").val();
								var neType = $("#neTypeList").val();
								var unitType = $("#unitTypeList").val();
								var dhssName = $("#inputDhssName").val();
								$.each(allUnit,function(index,item){
									if(dhssName != ""&&item.ne.dhssName != dhssName){
										return true;
									}
									if(neid != ""&&item.neName != neid){
										return true;
									}
									if(unitName != ""&&item.unitName.indexOf(unitName) == -1){
										return true;
									}
									if(neType != ""&&item.neType != neType){
										return true;
									}
									if(unitType != ""&&item.unitType != unitType){
										return true;
									}
									tempIds[tempIds.length] = item.unitId;
									selectData[selectData.length] = item;
								});
								
								var results = [];
								results[0] = selectData;
								results[1] = dataIds;
								results[2] = tempIds;
								neItemWindowObj.initGrid(results);
					},
					fail : function(data) {
						showNotify(data.message, "error");
					}
				});
			})
		},
		clearUnitInput:function(){
			$("#unitClearInput").on("click",function(){
				//$("#inputNeTrigger").val("");
				$("#unitNameInput").val("");
				$.ajax({
					dataType : 'json',
					type : "GET",
					url : itemHref+"?projection=equipmentJobUnitWithAssociation",
					success : function(checkDatas) {
								var dataIds = [];
								if(checkDatas["_embedded"]){
									if(checkDatas["_embedded"]["equipment-unit"]){
										$.each(checkDatas["_embedded"]["equipment-unit"],function(index,item){
											dataIds[dataIds.length] = item._links.self.href.substring(item._links.self.href.lastIndexOf("/")+1);
										})
									}
								}
								var tempIds = [];
								$.each(allUnit,function(index,item){
									tempIds[tempIds.length] = item.id;
								});
								var results = [];
								results[0] = allUnit;
								results[1] = dataIds;
								results[2] = tempIds;
								neItemWindowObj.initGrid(results);
					},
					fail : function(data) {
						showNotify(data.message, "error");
					}
				});
			})
		},
		addAllClick: function() {
			//全部加入
			$("#addAllNetElement").on("click", function() {
				var html = "<button added='true' class='addNetElementBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
				var dataView = neItemWindowObj.dataGrid.dataSource.view();
				var ids = [];
				$.each(dataView,function(index,item){
					ids[ids.length] = item.unitId; 
				})
				addAll(ids,uri,"#netElementGrid .addNetElementBtns[added='false']",html);
			});
		},
		removeAllClick: function() {
			//全部移除
			$("#removeAllNetElement").on("click", function() {
		    	var html = "<button added='false' class='addNetElementBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
		    	var dataView = neItemWindowObj.dataGrid.dataSource.view();
				var ids = [];
				$.each(dataView,function(index,item){
					ids[ids.length] = item.unitId; 
				})
		    	delAll(uri,"#netElementGrid .addNetElementBtns[added='true']",html,ids);
			});
		},

		initGrid: function(data) {
			var roleUrl = (this.restfulURL);
			uri = roleUrl;
			roleItemIds = data[1];
			tempIds = data[2];
			this.dataGrid = $("#netElementGrid").kendoGrid({

				dataSource: {
					data: data[0]
				},

				height: 330,

				reorderable: true,

				resizable: true,

				sortable: true,

				columnMenu: true,

				pageable: false,

				columns: [{
					field: "neName",
					template: "<span  title='#:isNotEmpty(neName)#'>#:isNotEmpty(neName)#</span>",
					title: "<span  title='网元名称'>网元名称</span>"
				}, {
					field: "neType",
					template: "<span  title='#:isNotEmpty(neType)#'>#:isNotEmpty(neType)#</span>",
					title: "<span  title='网元类型'>网元类型</span>"
				},{
					field: "unitName",
					template: "<span  title='#:isNotEmpty(unitName)#'>#:isNotEmpty(unitName)#</span>",
					title: "<span  title='单元名称'>单元名称</span>"
				},  {
					field: "unitType",
					template: "<span  title='#:isNotEmpty(unitType)#'>#:isNotEmpty(unitType)#</span>",
					title: "<span  title='单元类型'>单元类型</span>"
				}, {
					template: "#if(select(id+'') == -1){# <button added='false' class='addNetElementBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button> #}else{#" +
						"<button added='true' class='addNetElementBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button> #}#",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function() {
					//每一行的加入/移除按钮
					$("#netElementGrid td").delegate(".addNetElementBtns", "click", function() {
						var groupId = neItemWindowObj.dataGrid.dataItem($(this).closest("tr")).unitId;
						var td = $(this).closest("td");
						var html = "";
						if ($(this).attr("added") == "true") {
							$.ajax({
								url : roleUrl+"/"+groupId,
								type : "DELETE",
								dataType : "json",
								success:function(data){
									clearCache();
									html = "<button added='false' class='addNetElementBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
									td.html(html);
									infoTip({content : "移除成功！" });
								}
							});
						} else {
							$.ajax({
							    url : roleUrl,
							    type : "PATCH",
							    data: "commandGroupId/" + groupId,
							    dataType: "json",
							    contentType: "text/uri-list",
							    success : function(data) {
							    	clearCache();
									html = "<button added='true' class='addNetElementBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
									td.html(html);
									infoTip({content : "加入成功！",
										color : "#D58512"});
							    }
							});
						}
					});
				}
			}).data("kendoGrid");
		}
	};
	
	//系统功能权限弹窗
	var menuItemWindowObj = {
		uri : undefined,
		tempIds : undefined,
		obj: undefined,
		dataGrid: undefined,
		init: function() {
			initWindow("#systemWindow","系统功能权限","900px","400px");
			this.obj = $("#systemWindow").data("kendoWindow");
			this.addAllClick();
			this.removeAllClick();
		},
		addAllClick: function() {
			$("#addAllSystem").on("click", function() {
				//全部加入
				var html = "<button added='true' class='addSystemBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
				var dataView = menuItemWindowObj.dataGrid.dataSource.view();
				var ids = [];
				$.each(dataView,function(index,item){
					ids[ids.length] = item.entityId; 
				})
				addAll(tempIds,uri,"#systemGrid .addSystemBtns[added='false']",html);
			});
		},
		removeAllClick: function() {
			//全部移除
			$("#removeAllSystem").on("click", function() {
				var html = "<button added='false' class='addSystemBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
				var dataView = menuItemWindowObj.dataGrid.dataSource.view();
				var hrefs = [];
				$.each(dataView,function(index,item){
					hrefs[hrefs.length] = item.entityId;
				})
				delAll(uri,"#systemGrid .addSystemBtns[added='true']",html,hrefs);
			});
		},

		initGrid: function(data) {
			var roleUrl = (this.restfulURL);
			uri = roleUrl;
			roleItemIds = data[1];
			tempIds = data[2];
			this.dataGrid = $("#systemGrid").kendoGrid({

				dataSource: {
					data: data[0]
				},

				height: 350,

				reorderable: false,

				resizable: false,

				sortable: false,

				columnMenu: true,

				pageable: false,

				columns: [{
					field: "menuName",
					template: "<span  title='#:isNotEmpty(menuName)#'>#:isNotEmpty(menuName)#</span>",
					title: "<span  title='系统功能'>系统功能</span>"
				}, {
					template: "#if(selectLink(_links.self.href) == -1){# <button added='false' class='addSystemBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button> #}else{#" +
						"<button added='true' class='addSystemBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button> #}#",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function() {
					//每一行的加入/移除按钮
					$("#systemGrid td").delegate(".addSystemBtns", "click", function() {
						var groupLinks = menuItemWindowObj.dataGrid.dataItem($(this).closest("tr"))._links.self.href;
						var groupId = groupLinks.substring(groupLinks.lastIndexOf("/")+1);
						var td = $(this).closest("td");
						var html = "";
						if ($(this).attr("added") == "true") {
							$.ajax({
								url : roleUrl+"/"+groupId,
								type : "DELETE",
								dataType : "json",
								success:function(data){
									clearCache();
									html = "<button added='false' class='addSystemBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
									td.html(html);
									infoTip({content : "移除成功！" });
								}
							});
						} else {
							$.ajax({
							    url : roleUrl,
							    type : "PATCH",
							    data: "commandGroupId/" + groupId,
							    dataType: "json",
							    contentType: "text/uri-list",
							    success : function(data) {
							    	clearCache();
									html = "<button added='true' class='addSystemBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
									td.html(html);
									infoTip({content : "加入成功！",
										color : "#D58512"});
							    }
							});
						}
					});
				}
			}).data("kendoGrid");
		}
	};
	
	//列表初始化
	dataGridObj.init();
	
	//添加、修改角色窗体-初始化
	infoWindow.init();

	//指令权限弹窗-初始化
	commandItemWindowObj.init();
	
	//用户成员弹窗-初始化
	userItemWindowObj.init();
	
	//地区弹窗-初始化
	areaItemWindowObj.init();
	
	//网元权限弹窗-初始化
	neItemWindowObj.init();
	
	//系统功能权限-初始化
	menuItemWindowObj.init();

});

function initWindow(win,text,width,height){
	if (!$(win).data("kendoWindow")) {
		$(win).kendoWindow({
			width: width,
			height: height,
			actions: ["Close"],
			modal: true,
			title: text
		});
	}
}

function initSelect(uri,select,type){
	$.ajax({
		url:uri,
		dataType:"json",
		success:function(data){
			if(type == "1"){
				if(!data["_embedded"]){
					return;
				}
				var list = data["_embedded"]["equipment-ne"];
				$(select).kendoDropDownList({
					optionLabel:"--全部--",
					dataTextField: "neName",
					dataValueField: "neName",
					dataSource: list,
					filter: "contains",
					suggest: true
				});
			}else{
				$(select).kendoDropDownList({
					optionLabel:"--全部--",
					dataSource: data,
					filter: "contains",
					suggest: true
				});
			}
			
		}
	});
}

function delAll(uri,buttons,html,hrefs){
	$.each(hrefs,function(index,item){
		$.ajax({
			url : uri+"/"+item,
			type : "DELETE",
			dataType : "json",
			success:function(data){
				
			}
		});
	})
	clearCache();
	$(buttons).parent().html(html);
	infoTip({content : "全部移除成功！" });
}

function addAll(tempIds,uri,buttons,html){
	var itemArray = "";
	$.each(tempIds,function(index,item){
		itemArray+=("systemResourceId/"+item+"\n");
	});
	$.ajax({
	    url : uri,
	    type : "PATCH",
	    data: itemArray,
	    dataType: "json",
	    contentType: "text/uri-list",
	    success : function(data) {
	    	clearCache();
	    	$(buttons).parent().html(html);
	    	infoTip({content : "全部加入成功！",
	    		color : "#D58512"});
	    }
	});
	
}

function clearCache(){
	$.ajax({
		url : "resource/"+0,
		type : "POST",
		dataType: "json",
        contentType: "application/json;charset=UTF-8",
		success : function(data1) {
		}
	});
}

function isNotEmpty(value){
	if(value == null || value == "null" || value == ""){
		return "";
	}else{
		return value;
	}
}

function select(id){
	if($.inArray(id,roleItemIds) == -1){
		return -1;
	}else{
		return 1;
	}
}
function selectLink(links){
	var id = links.substring(links.lastIndexOf("/")+1);
	return select(id);
}