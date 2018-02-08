kendo.culture("zh-CN");
	
var execStateStore = ['等待执行','执行中','执行成功','执行失败'];
var isPassStore = ['不通过','通过'];
var execWeekDay = ['','每天','每周','每月','15分钟','每小时'];
var checkStartTitle = ['启用','停止','启用'];
var dataSource;
var dataGridObj;
var dataItem = {_links:"",jobType:"",jobName:"",jobDesc:"",execTime:""};
var smartCheckJob = {};
var searchParams={page:0,size:10,jobName:'',sort : "execTime,desc"};
var grid;
var allUnit = [];
$("#navList a[href='smartCheckManage']").addClass("active");
function isDisableFun(isDisable){
	return isDisable == 0 ? "" : "disabled=disabled"
}
function isSuccessFun( isSuccess,message,jobName){
	if(isSuccess == 1){
		infoTip({content: jobName + ": 修改失败！" + message });
	}
}
//获取日志列表数据
function loadGridList(){
	
	dataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
				url : "rest/smart-check-job/search/findByFilter?ran=" + Math.random(),
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			},
			parameterMap : function(options, operation) {
				if (operation == "read") {
					searchParams.jobName = $('#inputKeyWord').val();
					searchParams.page = options.page - 1;
					searchParams.size = options.pageSize;
					return searchParams;
				}
			},
		},
		batch : true,
		pageSize : 20, //每页显示个数
		schema : {
			data : function(d) {
				if(d._embedded){
					return d._embedded["smart-check-job"];  //响应到页面的数据
			     }else{
					return new Array();
				}
			},
			total : function(d) {
				return d.page.totalElements; //总条数
			},
		},
		serverPaging : true,
		serverFiltering : true,
		serverSorting : true
		
	});
	grid = $("#dataGrid").kendoGrid({
		
		dataSource: dataSource,
		height: $(window).height()-$("#dataGrid").offset().top - 50,
		width: "100%",
		reorderable: true,

		resizable: true,

		sortable: false,

		columnMenu: false,

		pageable: true,

		columns: [
		          
		 { field: "_links.self.href", title:"_links.self.href", hidden:true},
		 {
			field: "jobName",
			template: "<span #:isSuccessFun( isSuccess,message,jobName)#  title='#:isNotEmpty(jobName)#'>#:isNotEmpty(jobName)#</span>",
			title: "<span  title='方案名称'>方案名称</span>"
		}, {
			field: "jobDesc",
			template: "<span  title='#:isNotEmpty(jobDesc)#'>#:isNotEmpty(jobDesc)#</span>",
			title: "<span title='方案描述'>方案描述</span>"
		}, {
			field: "execDay",
			template: "<span  title='#:isNotEmpty(execTime)#'>#:isNotEmpty(execTime)#</span>",
			title: "<span  title='执行开始时间'>开始执行时间</span>"
		}, {
			field: "jobType",
			template: "<span  title='#:execWeekDay[jobType]#'>#:execWeekDay[jobType]#</span>",
			title: "<span  title='巡检单元个数'>执行周期</span>"
		}, {
			field: "nextDay",
			template: "<span  title='#:isNotEmpty(nextDay)#'>#:isNotEmpty(nextDay)#</span>",
			title: "<span  title='巡检单元个数'>下次执行时间</span>"
		}, {
			field: "execFlag",
			template: "<span  title='#:execStateStore[execFlag]#'>#:execStateStore[execFlag]#</span>",
			title: "<span  title='执行状态'>方案状态</span>"
		}, {
			title: "<span  title='操作'>操作</span>",
			template: "<a #:isDisableFun(isDisable)# onclick='openNeWin(this)' class='btn btn-xs btn-warning'> 编辑 </a>&nbsp;&nbsp;"+
						"<a #:isDisableFun(isDisable)# onclick='delCheck(this)' class='btn btn-danger btn-xs'> 删除 </a>&nbsp;&nbsp;"+
						"<a #:isDisableFun(isDisable)# onclick='selectWy(this)'   class='btn btn-primary btn-xs'  > 选择网元 </a>&nbsp;&nbsp;"+
						"<a #:isDisableFun(isDisable)# onclick='selectzl(this,\"#:jobName#\")' class='btn btn-primary btn-xs'> 选择指令 </a>&nbsp;&nbsp;"+
						"<a #:isDisableFun(isDisable)#  onclick='checkStart(this,#:execFlag#)' class='btn btn-xs btn-warning'>#:checkStartTitle[execFlag]# </a>&nbsp;&nbsp;"+
						"<a #:isDisableFun(isDisable)# class='execJob  btn btn-danger btn-xs'>立即执行</a>",
			width:"30%"
		}],
		dataBound: function() {
			dataGridObj.addClick();
			$(".execJob").on("click",function(){
				if(confirm("确认执行吗?")){
					var uri = grid.dataItem($(this).closest("tr"))._links.self.href;
					var id = uri.substring(uri.lastIndexOf("/")+1);
					$.ajax({
						url:"execJob",
						data:{id : id},
						type : "GET",
					    dataType: "json",
					    contentType: "application/json;charset=UTF-8",
					    success:function(data){
					    	if(data){
					    		infoTip({content: "执行成功！",color:"#D58512"});
					    	}
					    }
					})
				}
			})
		} 
	}).data("kendoGrid");
	
}
var nodes;
var unitWin;
var singleUrl;
var checkUrl;
var itemIds=[];
function selectzl(dataValue,jobName){
	itemIds=[];
	singleUrl = grid.dataItem($(dataValue).closest("tr"))._links.self.href;
	checkUrl = grid.dataItem($(dataValue).closest("tr"))._links.checkItem.href;
	unitWin.obj.setOptions({
		"title": "选择【"+jobName+"】对应的指令"
	});
	htmlContent($("#itemName").val());
	unitWin.initContent();
}
var count;
var size;
var commandlist = {};
function htmlContent(text){
	count = 0;
	size = 0;
	$('#tableListCheckItem tbody').html("");
	$.ajax({
	    url : checkUrl,
	    type : "GET",
	    dataType: "json",
	    contentType: "application/json;charset=UTF-8",
	    success : function(data) {
	    	if(data._embedded){
	    		$.each(data._embedded["check-item"],function(i,item){
		    		itemIds[i] = item.itemId;
		    	})
	    	}
	    	$.ajax({
	    		url : "rest/smart-check-job/search/getCheckItemList",
	    		type : "GET",
	    		data : { itemName : text },
	    		contentType: "application/json;charset=UTF-8",
	    		success : function(data) {
	    			$.each(data,function(i, result) {
	    				result.sortFiled = arrayContain(result.id);
	    			})
	    			data = data.sort(compare('sortFiled'));
	    			var tableContent = "";
	    			$.each(data,function(i, result) {
	    						var inGroup = result.id;
	    						var command = result.command;
	    						var itemName = result.name;
	    						var itemDisplay = ((itemName.length > 23) ? (itemName
	    								.substring(0, 17) + "...")
	    								: itemName);
	    						var commandDisplay = ((command.length > 20) ? (command
	    								.substring(0, 20) + "...")
	    								: command);

	    						tableContent += "<tr>";
	    						tableContent += "<td width=150px title='"+itemDisplay+"'>" + itemDisplay
	    								+ "</td>";
	    						tableContent += "<td width=300px onmouseover=setCommandText(this,"+inGroup+") onmouseout=unsetCommandText(this,"+inGroup+")>" + commandDisplay
	    								+ "</td>";
	    						tableContent += "<td>"
	    								+ (result.applyUnit == null ? "" : result.applyUnit) + "</td>";
	    						var buttons = "";
	    						if(arrayContain(inGroup)==-1){
	    							buttons += "<button name='ids' onclick='insertZl(this)'  type='button' class='btn btn-default btn-xs' value='"
	    									+ inGroup + "'>加入</button>";
	    							size += 1;
	    						}else{
	    							buttons += "<button name='ids' onclick='deleteZl(this)'  type='button' class='btn btn-success btn-xs' value='"
	    								+ inGroup + "'>移除</button>";
	    						}
	    						tableContent += "<td>" + buttons
	    								+ "</td>"
	    						tableContent += "</tr>";
	    						count += 1;
	    						commandlist[ inGroup ] = command;
	    				});
	    			$('#tableListCheckItem tbody').html(tableContent);
	    			
	    		}
	    	})
	    }
	});
	
}
function setCommandText(tdObj,id){
	tdObj.innerHTML=commandlist[id];
}
function unsetCommandText(tdObj,id){
	var command = commandlist[id];
	var commandDisplay = ((command.length > 20) ? (command
			.substring(0, 20) + "...")
			: command);
	tdObj.innerHTML = commandDisplay;
}
function arrayContain(id){
	var flag = -1;
	$.each(itemIds,function(i,item){
		if(item==id) {
			flag =  1;
		}
	})
	return flag;
}

function insertZl(but){
		$.ajax({
		    url : singleUrl+"/checkItem",
		    type : "PATCH",
		    data: "item/"+$(but).val(),
		    dataType: "json",
		    contentType: "text/uri-list",
		    success : function(data) {
			 	infoTip({content: "指令加入成功！",color:"#D58512"});
			 	$(but).attr("class","btn btn-success btn-xs");
			 	$(but).html("移除");
			 	$(but).attr("onclick","deleteZl(this)");
		    }
		});
		
}

function deleteZl(btn){
	$.ajax({
		url : singleUrl+"/checkItem/"+$(btn).val(),
		type : "DELETE",
		dataType: "json",
        contentType: "application/json;charset=UTF-8",
		success : function(data) {
			 	infoTip({content: "指令移除成功！"});
			 	$(btn).attr("class","btn btn-default btn-xs");
			 	$(btn).html("加入");
			 	$(btn).attr("onclick","insertZl(this)");
		}
	});
}



function delCheck(d){
	var singleUrl = grid.dataItem($(d).closest("tr"))._links.self.href;
	if(confirm("确定删除该方案吗？")){
		$.ajax({
    		url : singleUrl,
    		type : "DELETE",
    		dataType: "json",
            contentType: "application/json;charset=UTF-8",
    		success : function(data) {
    					infoWindow.obj.close();
        				infoTip({content: "删除成功！",color:"#D58512"});
        				dataSource.read(searchParams);
    		}
		});
	}
}


var infoWindow;
function openNeWin(d){
	var singleUrl = grid.dataItem($(d).closest("tr"))._links.self.href;
	//window.location.href="smartCheckResult?time=Math.random()&id="+scheduleId+"&type="+type;
	$("#webInterfaceTab").removeClass("hidden");
	$("#jobType").kendoDropDownList({ width : "200px"});
	$.ajax({
		dataType : 'json',
		type : "GET",
		url : singleUrl,
		success : function(data) {
			
			dataItem = {_links:data._links.self.href,jobType:data.jobType,jobName:data.jobName,jobDesc:data.jobDesc,execTime:data.nextDay};
			infoWindow.obj.setOptions({
				"title": "修改方案"
			});
			infoWindow.initContent(dataItem);
		},
		fail : function(data) {
			showNotify(data.message,"error");
		}
	});
	
}

//启用方案
function checkStart(d,index){
	var singleUrl = grid.dataItem($(d).closest("tr"))._links.self.href;
	var unitUrl = grid.dataItem($(d).closest("tr"))._links.unit.href;
	var checkItemUrl = grid.dataItem($(d).closest("tr"))._links.checkItem.href;
	var id = singleUrl.substring(singleUrl.lastIndexOf("/")+1);
	$.ajaxSetup ({ cache: false }); 
	if(confirm("确定"+checkStartTitle[index]+"该方案吗？")){
		$.ajax({
			url : singleUrl,
			type : "GET",
			dataType : "json",
			success : function(data) {
				$.ajax({
            		url : unitUrl,
            		type : "GET",
            		dataType: "json",
            		success : function(unitSize) {
            			$.ajax({
                    		url : checkItemUrl,
                    		type : "GET",
                    		dataType: "json",
                    		success : function(checkItemSize) { 
                    			
                    			if(unitSize._embedded && checkItemSize._embedded  ){
        							var type = 1;
        							if( data.execFlag == "1")
        								type = 2;
        							$.ajax({
        			            		url : singleUrl,
        			            		type : "PATCH",
        			            		data: kendo.stringify({"execFlag":type}),
        			            		dataType: "json",
        			                    contentType: "application/json;charset=UTF-8",
        			            		success : function(data) {
			            					infoWindow.obj.close();
			            					dataSource.read();
			    	        				infoTip({content: checkStartTitle[index]+"成功！",color:"#D58512"});
        			            		}
        							});
        						}else{
        							infoWindow.obj.close();
        	        				infoTip({content: "请先配置该任务对应的网元或命令后再启用",color:"#D58512"});
        						}
                    		}
        				});
            		}
				});
			},
			fail : function(data) {
				showNotify(data.message, "error");
			}
		});
	}
}
function compare(propertyName) {
	return function (object1, object2) {
		var value1 = object1[propertyName];
		var value2 = object2[propertyName];
		if (value2 < value1) {
			return -1;
		}else if (value2 > value1) {
			return 1;
		}else {
			return 0;
		}
	}
}
var neItemWindowObj;
function selectWy(d){
	var pickRow = grid.dataItem($(d).closest("tr"));
	itemHref = (pickRow._links.unit.href);
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
					var unitList = new Array();
					var temp1 = [];
					var temp2 = [];
					if(allUnit["_embedded"]){
						if(allUnit["_embedded"]["equipment-unit"]){
							$.each(allUnit["_embedded"]["equipment-unit"],function(index,item){
								var id = item._links.self.href.substring(item._links.self.href.lastIndexOf("/")+1);
								tempIds[tempIds.length] = id;
								item.sortFiled =  $.inArray(id,dataIds);
								unitList[unitList.length] = item;
							});
							unitList = unitList.sort(compare("sortFiled"));
						}
					}
					neItemWindowObj.obj.setOptions({
						"title" : "选择" + "【 <span style='color:blue;'>" + pickRow.jobName + "</span> 】对应的网元"
					});
					var results = [];
					results[0] = unitList;
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

var itemHref;
var jobItemIds;
var type = ["","每天","每周","每月"];
function select(id){
	if($.inArray(id,jobItemIds) == -1){
		return -1;
	}else{
		return 1;
	}
}
function selectIdByLink(link){
	var id = link.substring(link.lastIndexOf("/")+1);
	return select(id);
}
$(function() {
	$.ajaxSetup ({ cache: false }); 
	
	$("#allstop").on("click",function(){
		$.ajax({
			dataType : 'json',
			type : "GET",
			url : "allStop",
			success : function(data) {
				if(data){
					searchParams.page = 0;
					grid.dataSource.read();
					infoTip({content: "全部停止成功！",color:"#D58512"});
				}
			}
		});
	})
	
	
	$.ajax({
		dataType : 'json',
		type : "GET",
		url : "rest/equipment-unit?projection=equipmentUnitWithAssociation",
		success : function(data) {
			allUnit = data;
		}
	});
	
	
	
	
	
	//网元权限弹窗
	 neItemWindowObj = {
			uri : undefined,
			tempIds : undefined,
		obj: undefined,

		dataGrid: undefined,

		init: function() {
			
			$.ajax({
				url:"equipment-unitType/search/list",
				dataType:"json",
				success:function(data){
					$("#unitTypeList").kendoDropDownList({
						optionLabel:"--全部--",
						dataSource: data,
						filter: "contains",
						suggest: true
					});
					$("#unitTypeList").kendoDropDownList();
				}
			});
			
			
			$.ajax({
				url:"equipment-neType/search/list",
				dataType:"json",
				success:function(data){
					$("#neTypeList").kendoDropDownList({
						optionLabel:"--全部--",
						dataSource: data,
						filter: "contains",
						suggest: true
					});
					$("#neTypeList").kendoDropDownList();
				}
			});
			
			$.ajax({
				dataType : 'json',
				type : "GET",
				url : "rest/equipment-ne?projection=equipmentNeWithAssociation",
				success : function(data) {
					var list = new Array();
					if(data["_embedded"]){
						if(data["_embedded"]["equipment-ne"]){
							list = data["_embedded"]["equipment-ne"];
						}
					}
					$("#inputNeTrigger").kendoDropDownList({
						optionLabel:"--全部--",
						dataTextField: "neName",
						dataValueField: "neName",
						dataSource: list,
						filter: "contains",
						suggest: true
					});
					$("#inputNeTrigger").kendoDropDownList();
				}
			}); 

			if (!$("#netElementWindow").data("kendoWindow")) {
				$("#netElementWindow").kendoWindow({
					width: "900px",
					height: "450px",
					actions: ["Close"],
					modal: true,
					title: "网元权限"
				});
			}
			this.obj = $("#netElementWindow").data("kendoWindow");

			this.addAllClick();
			this.removeAllClick();
			this.selectBtn();
			this.clearUnitInput();
		},
		selectBtn:function() {
			$("#unitBtnSelect").on("click",function(){
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
						$.each(allUnit["_embedded"]["equipment-unit"],function(index,item){
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
							tempIds[tempIds.length] = item._links.self.href.substring(item._links.self.href.lastIndexOf("/")+1);
							selectData[selectData.length] = item;
						});
						
						var results = [];
						results[0] = selectData;
						results[1] = dataIds;
						results[2] = tempIds;
						neItemWindowObj.initGrid(results);
					},
					error : function(data) {
						showNotify(data.message, "error");
					}
				});
			})
		},
		clearUnitInput:function(){
			$("#unitClearInput").on("click",function(){
				//$("#unitTypeList,#neTypeList,#inputNeTrigger[value='']").attr("selected","selected");
				//$("#unitTypeList").kendoDropDownList().select(0);
				$("#unitNameInput").val("");
				$("#unitTypeList").kendoDropDownList();
				$("#neTypeList").kendoDropDownList();
				$("#inputNeTrigger").kendoDropDownList();
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
								$.each(allUnit["_embedded"]["equipment-unit"],function(index,item){
									tempIds[tempIds.length] = item._links.self.href.substring(item._links.self.href.lastIndexOf("/")+1);
								});
								var results = [];
								results[0] = allUnit["_embedded"]["equipment-unit"];
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
				var dataView = neItemWindowObj.dataGrid.dataSource.view();
				//全部加入
				var tempArray = "";
				$.each(dataView,function(index,item){
					tempArray += "unitId/"+item.unitId+"\n";
				})
				$.ajax({
					    url : uri,
					    type : "PATCH",
					    data: tempArray,
					    dataType: "json",
					    contentType: "text/uri-list",
					    success : function(data) {
					    	var html = "<button added='true' class='addNetElementBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
							$("#netElementGrid .addNetElementBtns[added='false']").parent().html(html);
							infoTip({content : "全部加入成功！",
								color : "#D58512"});
					    }
				});
				
			});
		},
		removeAllClick: function() {
			$("#removeAllNetElement").on("click", function() {
				var dataView = neItemWindowObj.dataGrid.dataSource.view();
				//全部加入
				$.each(dataView,function(index,item){
					$.ajax({
						url : uri+"/"+item.unitId,
						type : "DELETE",
						dataType : "json",
						success:function(data){
							
						}
					});
				})
				var html = "<button added='false' class='addNetElementBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
				$("#netElementGrid .addNetElementBtns[added='true']").parent().html(html);
				infoTip({content : "全部移除成功！" });
			})
		},

		initGrid: function(data) {
			var roleUrl = (this.restfulURL);
			uri = roleUrl;
			jobItemIds = data[1];
			tempIds = data[2];
			this.dataGrid = $("#netElementGrid").kendoGrid({

				dataSource: {
					data: data[0]
				},

				height: 300,

				reorderable: true,

				resizable: true,

				sortable: false,

				columnMenu: false,

				pageable: false,

				columns: [{
					field: "neName",
					template: "<span  title='#:isNotEmpty(neName)#'>#:isNotEmpty(neName)#</span>",
					title: "<span  title='网元名称'>网元名称</span>"
				}, {
					field: "neType",
					template: "<span  title='#:isNotEmpty(neType)#'>#:isNotEmpty(neType)#</span>",
					title: "<span  title='单元名称'>网元类型</span>"
				},{
					field: "unitName",
					template: "<span  title='#:isNotEmpty(unitName)#'>#:isNotEmpty(unitName)#</span>",
					title: "<span  title='单元类型'>单元名称</span>"
				},  {
					field: "unitType",
					template: "<span  title='#:isNotEmpty(unitType)#'>#:isNotEmpty(unitType)#</span>",
					title: "<span  title='单元类型'>单元类型</span>"
				}, {
					template: "#if(selectIdByLink(_links.self.href) == -1){# <button added='false' class='addNetElementBtns btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button> #}else{#" +
						"<button added='true' class='addNetElementBtns btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button> #}#",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function() {
					//每一行的加入/移除按钮
					$("#netElementGrid td").delegate(".addNetElementBtns", "click", function() {
						var link = neItemWindowObj.dataGrid.dataItem($(this).closest("tr"))._links.self.href;
						var groupId = link.substring(link.lastIndexOf("/")+1);
						var td = $(this).closest("td");
						var html = "";
						if ($(this).attr("added") == "true") {
							$.ajax({
								url : roleUrl+"/"+groupId,
								type : "DELETE",
								dataType : "json",
								success:function(data){
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
	
	
	 neItemWindowObj.init();
	
	$("#itemName").on("keyup",function(){
		htmlContent($("#itemName").val());
	})
	$("#saveItemBtn").on("click",function(){
		if(size!=0 && size==count ){
			var tempArray = "";
			$.each($("[name='ids']"),function(i,item){
				tempArray += "item/"+$(this).val() + "\n";
			})
			$.ajax({
	    		    url : singleUrl+"/checkItem",
	    		    type : "PATCH",
	    		    data: tempArray,
	    		    dataType: "json",
	    		    contentType: "text/uri-list",
	    		    success : function(data) {
	    		    	unitWin.obj.close();
	    		     	infoTip({content: "指令全部加入成功！",color:"#D58512"});
	    		     	selectVal();
	    		    }
	    	});
			
		}else{
			var tempArray = "";
			$.each($("[name='ids']"),function(i,item){
				$.ajax({
		    		url : singleUrl+"/checkItem/"+$(this).val(),
		    		type : "DELETE",
		    		dataType: "json",
		            contentType: "text/uri-list",
		    		success : function(data) {
		    			
		    		}
				});
			})
			unitWin.obj.close();
	     	infoTip({content: "指令全部移除成功！"});
	     	selectVal();
		}
	})
	
	dataGridObj = {
			//添加按钮，显示弹窗
			addClick: function() {
				$("#addBtn").on("click", function() {
					//添加时，默认隐藏[Web接口]Tab，当点击【保存 基本信息】按钮之后，显示[Web接口]Tab
					$("#webInterfaceTab").addClass("hidden");
					dataItem = {_links:"",jobType:"",jobName:"",jobDesc:"",execTime:""};
					$("#jobType").kendoDropDownList();
					infoWindow.obj.setOptions({
						"title": "添加方案"
					});

					infoWindow.initContent(dataItem);
				});
			}
		};
	 
	//选择网元、指令 
	 unitWin = {
		obj: undefined,
		template: undefined,
		id: $("#unitWin"),
		initContent: function() {
			//填充弹窗内容
			//记录Root密码、默认登录用户名、登录密码原数据：编辑时标示是否改变
			$('a[data-toggle="tab"][href="#tab1"]').tab('show');//显示第一个面板
			unitWin.obj.center().open();
		},init: function() {
			if (!unitWin.id.data("kendoWindow")) {
				unitWin.id.kendoWindow({
					width: "800px",
					height: "500px",
					actions: ["Close"],
					modal: true,
					title: "号段管理"
				});
			}
			unitWin.obj = unitWin.id.data("kendoWindow");
			//点击【保存基本信息】按钮
			//取消按钮
			$("#exit").on("click", function(){
				unitWin.obj.close();
			});
		}
	};


	 unitWin.init();
	//选择网元、指令结束
	 
	 
	//修改、增加弹窗
		  infoWindow = {
			obj: undefined,
			template: undefined,
			id: $("#infoWindow"),
			saveClick: function() {
				//保存 【添加】单元基础信息
				$("#saveBaseInfoBtn").on("click", function() {
					var jobType = $("#jobType").val();
					var jobName = $("#jobName").val();
					var jobDesc = $("#jobDesc").val();
					var execTime = $("#startTime").val();
					var link = $("#jobId").val();
					var jobId = link.substring(link.lastIndexOf("/")+1);
					smartCheckJob = { jobType : jobType,jobName : jobName, jobDesc : jobDesc, execTime : execTime ,id :jobId};
					if(jobName == ""){
						infoTip({content: "请输入方案名称！",color:"#D58512"});
						return;
					}
					if(execTime == ""){
						infoTip({content: "请输入开始时间！",color:"#D58512"});
						return;
					}
					$.ajax({
	            		url : link?link:"/rest/smart-check-job",
	            		type : (jobId != "")?"PATCH":"POST",
	            		data: kendo.stringify(smartCheckJob),
	            		dataType: "json",
	                    contentType: "application/json;charset=UTF-8",
	            		success : function(data) {
	            					infoWindow.obj.close();
	    	        				infoTip({content: "保存成功！",color:"#D58512"});
	    	        				selectVal();
	            		},
	            		error:function(data){
	            			infoTip({content: "保存失败！",color:"#D58512"});
	            		}
					});
				});
			},
			initContent: function(dataItem) {
				//填充弹窗内容
				$("#jobId").val(dataItem._links);
				if(dataItem.jobType != ""){
					$('#jobType').data("kendoDropDownList").text(type[dataItem.jobType]);
					$('#jobType').val(dataItem.jobType);
				}else{
					$('#jobType').data("kendoDropDownList").text(type[4]);
					$('#jobType').val('4');
				}
				$("#jobName").val(dataItem.jobName);
				$("#jobDesc").val(dataItem.jobDesc);
				$("#startTime").val(dataItem.execTime);
				//记录Root密码、默认登录用户名、登录密码原数据：编辑时标示是否改变
				//…………………………
				$('a[data-toggle="tab"][href="#tab1"]').tab('show');//显示第一个面板
				infoWindow.obj.center().open();
				
				
			},

			init: function() {

				if (!infoWindow.id.data("kendoWindow")) {
					infoWindow.id.kendoWindow({
						width: "700px",
						height: "500px",
						actions: ["Close"],
						modal: true,
						title: "号段管理"
					});
				}
				infoWindow.obj = infoWindow.id.data("kendoWindow");
				//点击【保存基本信息】按钮
				infoWindow.saveClick();
				//取消按钮
				$(".cancelBtns").on("click", function(){
					dataItem = {
							id:"",
							jobType:"",
							jobName:"",
							jobDesc:"",
							execTime:""
					};
					infoWindow.obj.close();
					dataSource.read(searchParams);
				});
			}
		};
	

		infoWindow.init();
	 
	 
	
	//加载grid
	loadGridList();
	
	$("#startTime").kendoDateTimePicker({
		format:"yyyy-MM-dd HH:mm",
		min:new Date()
	});
	
	
	//查询按钮时间
	$('#inputKeyWord').on('keyup', function() {
		searchParams.page = 0;
		selectVal();
	});
	
	//清空文本数据
	$("#clearsearch").on("click",function(){
		$('#inputKeyWord').val("");
		selectVal();
	})
	
	
	
	
});
function isNotEmpty(value){
	if(value == null || value == "null" || value == ""){
		return "";
	}else{
		return value;
	}
}

//查询方法
function selectVal(){
	searchParams.jobName = $('#inputKeyWord').val();
	dataSource.read(searchParams);
}