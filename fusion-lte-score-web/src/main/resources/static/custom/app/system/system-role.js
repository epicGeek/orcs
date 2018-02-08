
var searchparams = {page:1, size: 10,searchField:""};
var dataSource;
var userGrid;
var roleId;
var dialogObj;
var postParams = {};
var userData;
var areaData = [];
var gridData = [];
var checkedHerf = "";
var creatorPath = "common";
var url = "";
var flag = false;//默认不重复
var oldRoleName = "";

function initDataSource(){
	
	dataSource=new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url:"platform/role/search",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            },
            parameterMap: function(options, operation) {
                if (operation == "read") {
                    searchparams.page = (options.page-1);
                    searchparams.size = options.pageSize;
                    searchparams.searchField = $("#inputKeyWord").val();
                    return searchparams;
                }
                
            }
        },
        schema: {
            data: function (data) {
            	 return data._embedded["system-role"];// 返回页面数据
            },
            total: function (data) {
            	return data.page.totalElements; // 总条数
            },
            pageSize: function(data) {
                return data.page.size; 
            }
        },
        requestEnd: function(e) {
            var response = e.response;

            if(response){
                var type = e.type;
                if(type !='read'){
                     this.read();                  
                }
            }else{
            	infoTip({content: "服务器异常，请重试！",color:"#088703"});
            }        
        },
        pageSize: 10,
        serverPaging : true,
		serverFiltering : true,
		serverSorting : true
      
    });
}

/**
 * 用户列表
 */
function initUserDataSource(userHerf){
	
	userData=new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url:"platform/user/search",
                dataType: "json",
                async: false,//同步
                contentType: "application/json;charset=UTF-8"
            },
            parameterMap: function(options, operation) {
                if (operation == "read") {
                	var user_parameter={};
                	user_parameter.page = (options.page-1);
                	user_parameter.size = options.pageSize;
                	user_parameter.searchField = $("#userNameInput").val();
                    return user_parameter;
                }
                
            }
        },
        schema: {
            data: function (data) {
            	var datas = data._embedded["system-user"];// 返回页面数据
            	filterCheckedData(userHerf,'system-user',datas);
            	return datas;
            },
            total: function (data) {
            	return data.page.totalElements; // 总条数
            }
        },
        requestEnd: function(e) {
            var response = e.response;

            if(response){
                var type = e.type;
                if(type !='read'){
                     this.read();                  
                }
            }else{
            	infoTip({content: "服务器异常，请重试！",color:"#088703"});
            }        
        },
        pageSize: 10,
        serverPaging : true,
		serverFiltering : true,
		serverSorting : true
      
    });
}

//初始化全部地区
function getAreaData(searchName){
	$.ajax({
		url:"platform/area/search",
		type:"GET",
		data:{searchField:searchName},
		async: false,//同步
		success:function(data){
			areaData = [];//重置清空
		    if(null!=data){
		    	areaData = data;
		    }
		}
	});
}


/**
 * 根据url过滤已经加入的记录
 */
function  filterCheckedData(itemHref,key,dataList){
	$.ajax({
		dataType : 'json',
		type : "GET",
		url : itemHref,
		async: false,//同步
		success : function(data) {
			gridData = [];
			var value = "";
			if(key=='system-user'){
				value = "userName";
			}else{
				value = "areaCode";
			}
			if(data["_embedded"]){
				var requestUserList = data["_embedded"][key];
				if(requestUserList){
					$.each(requestUserList,function(index,userItem){
						gridData[index] = (userItem[value]);
					});
				}
				
			}
			$.each(dataList,function(index,item){
				if($.inArray(item[value], gridData)!=-1){
					item.added = "true";
					item.force = "true";
				}else{
					item.added = "false";
					item.force = "false";
				}
			});
		},
		fail : function(data) {
			showNotify(data.message, "error");
		}
	});
}

$(function($){
	
	
	$("#authority").kendoDropDownList({
		//optionLabel:"--全部权限等级--",
		dataTextField: "text",
		dataValueField: "value",
		dataSource:[{text:'管理账号',value:'admin'},{text:'普通账号',value:'common'}],
		//filter: "contains",
		index: 1,
		suggest: true,
		change: function(){
			creatorPath=$('#authority').val();
         
        }
	});
	
	// 初始化数据
	initDataSource();
	
    // 初始化 Grid
	userGrid =  $("#roleGrid").kendoGrid({
	   dataSource: dataSource,
		reorderable: true,
		resizable: true,
		sortable: true,
		pageable: true,
		toolbar: kendo.template($("#template").html()),
		columns: [
		          {
						width: 150,
						field: "roleName",
						template: "<span  title='#:roleName#'>#:roleName#</span>",
						title: "<span  title='角色名称'>角色名称</span>"
				  },
				  {
						field: "roleDesc",
						template: "<span  title='#:roleDesc#'>#:roleDesc#</span>",
						title: "<span  title='角色描述'>角色描述</span>"
			     },
			     {
						field: "notifiType",
						template: "<span  title='#:notifiType#'>#if(notifiType=='admin'){#管理账号#}else{#普通账号#}#</span>",
						title: "<span  title='权限等级'>权限等级</span>"
			     },
	             {
						template: "<a class='updateBtn btn btn-warning btn-xs'>修&nbsp;&nbsp;改</a> " +
								"<a class='deleteBtn btn btn-danger btn-xs'>删&nbsp;&nbsp;除</a> " +
								"<a class='menuBtn btn btn-warning btn-xs'>菜单权限</a>"+
								"<a class='areaBtn btn btn-warning btn-xs'>地区权限</a>"+
								"<a class='userBtn btn btn-warning btn-xs'>用户成员</a>",
						encoded: false,
						title: "<span  title='操作'>操作</span>"
			    }
		      ]
		          
	}).data("kendoGrid");
  
	// 添加按钮
	$("#addBtn").on("click",function(){
		$('#myModal').modal('show');
		modalObj.clearModal();
	});
	
	// 编辑按钮
	$('body').delegate(".updateBtn", 'click', function() {
		
		$('#myModal').modal('show');
		var data = userGrid.dataItem($(this).closest("tr"));
		url = data._links.self.href;
		modalObj.setModal(data);
	});
	
	
	
    $('body').delegate(".userBtn", 'click', function() {
		
    	$("#userNameInput").val("");
    	var pickRow = userGrid.dataItem($(this).closest("tr"));
		var checkedHerf = (pickRow._links.systemUser.href);
		initUserDataSource(checkedHerf);
		userItemWindowObj.restfulURL = checkedHerf;
		userItemWindowObj.obj.center().open();
		userItemWindowObj.initGrid(userData);
	});
    
   $('body').delegate(".areaBtn", 'click', function() {
		
	    $("#areaSearchName").val("");
	    getAreaData("");
	    var pickRow = userGrid.dataItem($(this).closest("tr"));
	    console.log(pickRow);
		checkedHerf = (pickRow._links.systemResource.href);
		filterCheckedData(checkedHerf,'system-area',areaData);
		areaWindowObj.restfulURL = checkedHerf;
		areaWindowObj.obj.center().open();
		areaWindowObj.initGrid(areaData);
	});
	
	// 菜单权限
	$('body').delegate(".menuBtn", 'click', function() {
		var data = userGrid.dataItem($(this).closest("tr"));
		var roleUrl = data._links.self.href;
		roleId = roleUrl.substring(roleUrl.lastIndexOf("/")+1);
		checkedHerf = data._links.systemResource.href;
		initMenuTree(roleId);
	});
	
	//保存菜单权限
	$("#saveMenu").on("click",function(){
		
		var checkedNodes = [],
    	treeView = $("#checkItemsTree").data("kendoTreeView");
	    checkedNodeIds(treeView.dataSource.view(), checkedNodes);
	    $.ajax({
		    url : checkedHerf,
		    type : "PUT",
		    dataType: "json",
		    contentType: "text/uri-list",
		    success : function(data) {
		    	$.each(checkedNodes,function(index,item){
					$.ajax({
					    url : checkedHerf,
					    type : "PATCH",
					    data: "systemResourceId/"+item,
					    dataType: "json",
					    contentType: "text/uri-list",
					    success : function(data) {}
					});
				});
				infoTip({content: "保存成功！",color:"#088703"});
		    	dialogObj.close();
		    }
		});
	    
	});
	
	//取消
	$("#cancelMenu").on("click",function(){
		dialogObj.close();
	});
	
	// 删除按钮
	$('body').delegate(".deleteBtn", 'click', function() {
		var data = userGrid.dataItem($(this).closest("tr"));
		if(confirm("确认删除吗？")){
			deleteData(data._links.self.href);
		}
	});
	
	$('#searchBtn').on('click', function() {
		
		searchparams.searchField = $('#inputKeyWord').val();
		$("#roleGrid").data("kendoGrid").pager.page(1);
		//条件查询重新加载 
		dataSource.read(searchparams);
	});
	
	$('#resetBtn').on('click',function(){
		
		$('#inputKeyWord').val("");
		searchparams.searchField = "";
		dataSource.read(searchparams);
		
	});
	
	//地区弹窗
	areaWindowObj = {
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
				this.selectClick();
				this.clearInput();
			},
			//查询操作
			selectClick: function() {
				$("#areaBtn").on("click",function(){
					getAreaData($("#areaSearchName").val());
					filterCheckedData(checkedHerf,'system-area',areaData);
					areaWindowObj.initGrid(areaData);
					
				});
			},
			//重置
			clearInput: function(){
				$("#areaClear").on("click",function(){
					$("#areaSearchName").val("");
					getAreaData("");
					filterCheckedData(checkedHerf,'system-area',areaData);
					areaWindowObj.initGrid(areaData);
					
				});
			},
			initGrid: function(data) {
				var resourceUrl = (this.restfulURL);
				this.dataGrid = $("#areaGrid").kendoGrid({
					dataSource: {
						data: data
					},
					height: 300,
					reorderable: true,
					resizable: true,
					sortable: true,
					columnMenu: true,
					pageable: false,
					columns: [
				        {field: "areaCode",template: "<span  title='#:areaCode#'>#:areaCode#</span>",title: "<span  title='地区编码'>地区编码</span>"},
				        {field: "areaName",template: "<span  title='#:areaName#'>#:areaName#</span>",title: "<span  title='地区名称'>地区名称</span>"},
						{
							template: "#if(added=='false' && force=='false'){# <button added='false' force=#:force# class='addAreaBtns btn btn-xs btn-success'>加入</button>" +
									  "#}else{#" +
									  "<button added='true' force=#:force# class='addAreaBtns btn btn-xs btn-danger'>移除</button> #}#",
							title: "<span  title='操作'>操作</span>"
						}
					 ],
					dataBound: function() {
						//每一行的加入/移除按钮
						$("#areaGrid td").delegate(".addAreaBtns", "click", function() {
							var tr = areaWindowObj.dataGrid.dataItem($(this).closest("tr"));
							var areaId = tr.id;
							var td = $(this).closest("td");
							var force = $(this).attr("force");
							var html = "";
							if ($(this).attr("added") == "true") {
								$.ajax({
									url : resourceUrl+"/"+areaId,
									type : "DELETE",
									dataType : "json",
									success:function(data){
										html = "<button added='false' force="+force+" class='addAreaBtns btn btn-xs btn-success'>加入</button>";
										td.html(html);
										infoTip({content : "移除成功！" });
									}
								});
							} else {
								$.ajax({
								    url : resourceUrl,
								    type : "PATCH",
								    data: "system-role/" + areaId,
								    dataType: "json",
								    contentType: "text/uri-list",
								    success : function(data) {
								    	html = "<button added='true' force="+force+" class='addAreaBtns btn btn-xs btn-danger'>移除</button>";
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
	
	//用户成员弹窗
	userItemWindowObj = {
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
				this.selectClick();
				this.clearInput();
			},
			//查询操作
			selectClick: function() {
				$("#userBtn").on("click",function(){
					getUserAll($("#userNameInput").val());
					filterCheckedData(checkedHerf,'system-user',userData);
					userItemWindowObj.initGrid(userData);
					
				});
			},
			//重置
			clearInput: function(){
				$("#userClear").on("click",function(){
					$("#userNameInput").val("");
					getUserAll("");
					filterCheckedData(checkedHerf,'system-user',userData);
					userItemWindowObj.initGrid(userData);
					
				});
			},
			
			initGrid: function(data) {
				var roleUrl = (this.restfulURL);
				this.dataGrid = $("#usersGrid").kendoGrid({
					dataSource:data,
					height: 300,
					reorderable: true,
					resizable: true,
					sortable: true,
				//	columnMenu: true,
					pageable: true,
					columns: [{
						field: "loginName",
						template: "<span  title='#:userName#'>#:userName#</span>",
						title: "<span  title='登录名称'>登录名称</span>"
					}, {
						field: "userName",
						template: "<span  title='#:realName#'>#:realName#</span>",
						title: "<span  title='用户姓名'>用户姓名</span>"
					},
					{
						template: "#if(added=='false' && force=='false'){# <button added='false' force=#:force# class='addUserBtns btn btn-xs btn-success'>加入</button>" +
								  "#}else{#" +
								  "<button added='true' force=#:force# class='addUserBtns btn btn-xs btn-danger'>移除</button> #}#",
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
	// 弹窗
	modalObj = {
			
		// 添加清空弹窗内的数据
		clearModal: function(){
			$("#roleName").val("");         
			$("#roleDesc").val("");   
			$("#authority").val(0);
		},
		
		// 修改设置弹窗内的数据
		setModal: function(data){
			oldRoleName = data.roleName;
			$("#roleName").val(data.roleName);         
			$("#roleDesc").val(data.roleDesc);  
			$("#authority").data("kendoDropDownList").value(data.notifiType);
		},
		// 弹窗保存按钮
		saveBtn: function(){
			$("#saveBtn").on("click",function(){
				postParams.roleName = $("#roleName").val();         
				postParams.roleDesc = $("#roleDesc").val();
				postParams.notifiType = creatorPath;
                // add update 数据验证
				if(checkIsNull(postParams.roleName,'角色名称')){return;};
				if(!checkContentLength(postParams.roleName,10,'角色名称')){return;};
				if(checkIsNull(postParams.roleDesc,'角色描述')){return;};
				if(!checkContentLength(postParams.roleDesc,50,'角色描述')){return;};
				if(checkIsNull(postParams.notifiType,'权限等级')){return;};
				
				//验证重复
				if(oldRoleName != postParams.roleName){
					VerificationRepeat(postParams.roleName);
				}
				
				if(flag){return;};
				console.log(kendo.stringify(postParams)+'     '+url);
				if(url!=""){
					postAddAndEdit('PATCH',url);
				}else{
					postAddAndEdit('POST','rest/system-role');
				}
			});
		}
	};
	// 添加 修改保存
	modalObj.saveBtn();
	
	userItemWindowObj.init();
	
	areaWindowObj.init();
});

//获取选中的id
function checkedNodeIds(nodes, checkedNodes) {
	
    for (var i = 0; i < nodes.length; i++) {
    	if (nodes[i].hasChildren) {
    		checkedNodeIds(nodes[i].children.view(), checkedNodes);
    	}else{
    		if (nodes[i].checked) {
    			checkedNodes.push(nodes[i].id);
    		}
    	}
    	

    }
}


function initMenuTree(roleId){
	$("#bindCheckItemsWindowtree").html("<div id='checkItemsTree' style='height: 350px;'></div>");
	
	var ds = new kendo.data.HierarchicalDataSource({
        transport: {
            read: {
                url: "platform/menuTree/" + roleId,
                dataType: "json"
            }
        },
        schema: {
            model: {
              id: "id",
              hasChildren: "hasChildren"
            }
          }
    });
	
	$("#checkItemsTree").kendoTreeView({
     	checkboxes: {
						checkChildren: true
					},
		dataSource: ds,
		dataTextField: "text"
	});
	
	$("#bindCheckItemsWindow").kendoWindow({
		  height: '420px',
		  width: '415px',
		  draggable: false,
		  title:"权限分配"
	});
	dialogObj = $("#bindCheckItemsWindow").data("kendoWindow");
	dialogObj.open();dialogObj.center();
}

/**
 * 新增、修改请求函数
 * 
 * @param type
 * @param url
 */
function postAddAndEdit(type,url){
	//console.log(kendo.stringify(postParams));
	$.ajax({
		url:url,
		type:type,
		dataType:"json",
		contentType:"application/json;charset=utf-8",
		data:kendo.stringify(postParams),
		success:function(data){
			if(data==0){
				infoTip({content:"请检查是否重复",color:"#f60a0a"});
			}else{
				
				$('#myModal').modal('hide');
				infoTip({content: "保存成功！",color:"#088703"});
				dataSource.read(searchparams);
			}
		},
		complete: function(XMLHttpRequest, textStatus) {
			if(XMLHttpRequest.status==201){
				$('#myModal').modal('hide');
				infoTip({content: "保存成功！",color:"#088703"});
				dataSource.read(searchparams);
			}
		},
		fail:function(error){
			infoTip({content: "保存失败！",color:"#f60a0a"});
		},
	});
}


// 删除
function deleteData(url){
	
	$.ajax({
		url:url,
		type:"delete",
		dataType:"json",
		contentType:"application/json;charset=utf-8",
		data:kendo.stringify(postParams),
		success:function(data){
			infoTip({content: "删除成功！",color:"#088703"});
			dataSource.read(searchparams);
		},
		fail:function(error){
			infoTip({content: "删除失败！",color:"#f60a0a"});
		},
		
	});
}

//添加、修改，验证是否重复
function VerificationRepeat(valiDateField){
	
	$.ajax({
		url : "platform/VerificationRepeat",
		type : "POST",
		data:{
			"valiDateField":valiDateField,
			"type":"roleName"
		},
		async:false,
		success : function(data) {
			if(data>0 && data != null){
					infoTip({content: "角色名称不允许重复！",color:"#f60a0a"});
					flag=true;
			}
		},
		fail : function(data) {
			infoTip({content: "验证失败！",color:"#f60a0a"});
		}
	});
}
