kendo.culture("zh-CN");

var execTypes = ["每5分钟","每10分钟","每15分钟","每20分钟","每30分钟","每小时","每n小时","每天"];
var execState = ["等待执行","执行中"];
var startState = ["启动","停止"];
var dataItem;
var netElementGrid;
var commandWindowGrid;

var allArray = [];

var groups = [];
$(function(){
	$("[ href = 'emsManage' ]").addClass("active");
	
	
	var inputNeTrigger = $("#inputNeTrigger").kendoDropDownList({
		optionLabel:"全部网元",
		dataTextField: "neName",
		dataValueField: "neName",
		filter: "contains",
		dataSource:{
            transport: {
                read: {
                    dataType: "json",
                    url: "rest/equipment-ne"
                }
            },
            schema : {
    			data : function(d) {
    				if(d._embedded){
    					return d._embedded["equipment-ne"];  //响应到页面的数据
    			     }else{
    					return new Array();
    				}
    			}
    		}
        },
		filter: "contains",
		suggest: true,
		change:function(){
			unitGridFilter();
		}
	}).data("kendoDropDownList");
	
	$("#neTypeList").kendoDropDownList({
		optionLabel:"全部网元类型",
		dataSource: {
            transport: {
                read: {
                    dataType: "json",
                    url: "equipment-neType/search/list"
                }
            }
        },
		filter: "contains",
		suggest: true,
		change:function(){
			var filters = [];
	    	if($("#neTypeList").val() != ""){
	    		filters.push({field: "neType", operator: "contains", value: $("#neTypeList").val()});
	    	}
	    	inputNeTrigger.dataSource.filter(filters);
	    	inputNeTrigger.dataSource.fetch();
	    	unitGridFilter();
		}
	});
	
	var unitTypeList = $("#unitTypeList").kendoDropDownList({
		optionLabel:"全部单元类型",
		dataSource: {
            transport: {
                read: {
                    dataType: "json",
                    url: "equipment-unitType/search/list"
                }
            }
        },
		filter: "contains",
		suggest: true,
		change:function(){
			unitGridFilter();
		}
	}).data("kendoDropDownList");
	
	
	
	var dataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
				url : "rest/ems-check-job",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			}
		},
		batch : true,
		pageSize : 20, //每页显示个数
		schema : {
			data : function(d) {
				return d._embedded ? d._embedded["ems-check-job"] : new Array();
			},
			total : function(d) {
				return d._embedded ? d._embedded["ems-check-job"].length : 0;
			},
		},
		serverPaging : false,
		serverFiltering : false,
		serverSorting : false,
		filter:{
            logic: "or",
            filters: [
                {
                    field: "jobName",
                    operator: "contains",
                    value: $("#inputKeyWord").val()
                },{
                    field: "jobDesc",
                    operator: "contains",
                    value: $("#inputKeyWord").val()
                }
            ]
        }
		
	});
	
	var grid = $("#dataGrid").kendoGrid({
		
		dataSource: dataSource,
		height: $(window).height()-$("#dataGrid").offset().top - 50,
		width: "100%",
		reorderable: true,

		resizable: true,

		sortable: false,

		columnMenu: false,

		pageable: true,
		columns: [{
			field: "jobName",
			template: "<span  title='#:jobName#'>#:jobName#</span>",
			title: "<span  title='方案名称'>方案名称</span>"
		}, {
			field: "jobDesc",
			template: "<span  title='#:jobDesc#'>#:jobDesc#</span>",
			title: "<span title='方案描述'>方案描述</span>"
		}, {
			field: "execDate",
			template: "<span  title='#:execDate#'>#:execDate#</span>",
			title: "<span  title='执行开始时间'>开始执行时间</span>"
		}, {
			field: "jobType",
			template: "<span  title='#:execTypes[jobType]#'>#:execTypes[jobType]#</span>",
			title: "<span  title='巡检单元个数'>执行周期</span>"
		}, {
			field: "nextDate",
			template: "<span  title='#:nextDate#'>#:nextDate#</span>",
			title: "<span  title='巡检单元个数'>下次执行时间</span>"
		}, {
			field: "execFlag",
			template: "<span  title='#:execState[execFlag]#'>#:execState[execFlag]#</span>",
			title: "<span  title='执行状态'>方案状态</span>"
		}, {
			title: "<span  title='操作'>操作</span>",
			template: "<a class='update btn btn-xs btn-warning'> 编辑 </a>&nbsp;&nbsp;"+
						"<a class='del btn btn-danger btn-xs'> 删除 </a>&nbsp;&nbsp;"+
						"<a class='unit btn btn-primary btn-xs'  > 选择网元 </a>&nbsp;&nbsp;"+
						"<a class='command btn btn-primary btn-xs'> 选择指令 </a>&nbsp;&nbsp;"+
						"<a class='group btn btn-primary btn-xs'> 选择通知组 </a>&nbsp;&nbsp;"+
						"<a type='#:execFlag#'  class='start btn btn-xs btn-warning'>#:startState[execFlag]# </a>",
			width:"30%"
		}],
		dataBound: function() {
			$(".update").on("click",function(){
				infoWindow.obj.setOptions({ "title": "修改方案" });
				dataItem = grid.dataItem($(this).closest("tr"));
				infoWindow.initContent(dataItem);
			});
			$(".del").on("click",function(){
				if(confirm("确定删除吗")){
					dataItem = grid.dataItem($(this).closest("tr"));
					$.ajax({
						url : dataItem._links.self.href,
	            		type : "delete",
	            		dataType: "json",
	                    contentType: "application/json;charset=UTF-8",
        				success:function(data){
	    				    infoTip({content: "删除成功！"});
	    				    dataSource.read();
        				}
        			});
				}
				
			});
			$(".unit").on("click",function(){
				dataItem = grid.dataItem($(this).closest("tr"));
				bindingItem(dataItem,"选择【" + dataItem.jobName+"】的单元",netElementWindow,0);
			});
			$(".command").on("click",function(){
				dataItem = grid.dataItem($(this).closest("tr"));
				bindingItem(dataItem,"选择【" + dataItem.jobName+"】的指令",commandWindow,1);
			});
			$(".group").on("click",function(){
				dataItem = grid.dataItem($(this).closest("tr"));
				bindingItem(dataItem,"选择【" + dataItem.jobName+"】的通知组",groupWindow,2);
			})
			
			
			$(".start").on("click",function(){
				var state = $(this).attr("type") == "0" ? "1" : "0";
				dataItem = grid.dataItem($(this).closest("tr"));
				if(dataItem.units == null || dataItem.units == "" || 
						dataItem.commands == null || dataItem.commands == ""){
					infoTip({content: "请先配置单元和指令！",color:"#D58512"});
					return;
				}
				dataItem.execFlag = state;
				$.ajax({
            		url : dataItem._links.self.href,
            		type : "PATCH",
            		data: kendo.stringify(dataItem),
            		dataType: "json",
                    contentType: "application/json;charset=UTF-8",
            		success : function(data) {
            			 infoWindow.obj.close();
    	        		 infoTip({content: startState[state== "0" ? "1" : "0"]+"成功！",color:"#D58512"});
    	        		 dataSource.read(); 
            		}
				});
			});
			
		}
	}).data("kendoGrid");
	
	function bindingItem(dataItem,title,itemWindow,type){
		$.ajax({
    		url : dataItem._links.self.href,
    		type : "get",
    		dataType: "json",
            contentType: "application/json;charset=UTF-8",
    		success : function(data) {
    			itemWindow.obj.setOptions({ "title": title });
    			var text = type == 1 ? data.commands : type == 0 ? data.units : data.roles;
    			
    			itemWindow.initContent(text == null || text == "" ? [] : text.split(","),data._links.self.href);
    		}
		});
	}
	
	
	$('#inputKeyWord').on('keyup',function (event){
		var filters = [];
    	if($("#inputKeyWord").val() != ""){
    		filters.push({field: "jobName", operator: "contains", value: $("#inputKeyWord").val()});
    		filters.push({field: "jobDesc", operator: "contains", value: $("#inputKeyWord").val()});
    	}
    	var arr = {filters : filters , logic : "or"};
    	grid.dataSource.filter(arr);
    	grid.dataSource.fetch();
    });
	
	//清空文本数据
	$("#clearsearch").on("click",function(){
		$('#inputKeyWord').val("");
		grid.dataSource.filter([]);
    	grid.dataSource.fetch();
	})
	
	var links = "";
	
	
	
	var groupWindow = {
			obj: undefined,
			template: undefined,
			id: $("#groupWindow"),
			saveClick: function() {
				
			},
			initContent: function(array,_links) {
				var groupGrid = $("#groupGrid").kendoGrid({
					dataSource: {
			            transport: {
			                read: {
			                    dataType: "json",
			                    url: "rest/system-role"
			                }
			            },
			            schema : {
			    			data : function(d) {
			    				links = _links;
			    				if(d._embedded){
			    					var groupArray = [];
			    					$.each(d._embedded["system-role"],function(index,item){
			    						item.flag = $.inArray(item.roleId+"",array);
			    						groupArray[groupArray.length] = item;
			    					})
			    					return groupArray;  //响应到页面的数据
			    			     }else{
			    					return new Array();
			    				}
			    			}
			    		},
			    		filter:{
			                logic: "or",
			                filters: [
			                    {
			                        field: "notifi",
			                        operator: "contains",
			                        value: "ems"
			                    }
			                ]
			            }
			        },
					height: $(window).height()-$("#groupGrid").offset().top - 330,
					width: "100%",
					reorderable: true,

					resizable: true,

					sortable: false,

					columnMenu: false,

					pageable: false,

					columns: [{
							width: 30,
							template: "<input type='checkbox' value='#:roleId#' name='groupChk' #if(flag != -1){# checked='checked' #}# />",
							attributes:{"class": "text-center"},
							title: "<input type='checkbox' class='grid_checkbox2'/>"
						},{
							field: "roleName",
							template: "<span  title='#:roleName#'>#:roleName#</span>",
							title: "<span  title='通知组名称'>通知组名称</span>"
						}, {
							field: "roleDesc",
							template: "<span  title='#:roleDesc#'>#:roleDesc#</span>",
							title: "<span  title='通知组描述'>通知组描述</span>"
						}],
					dataBound: function() {
						$(".grid_checkbox2").on("click",function(){
							$("[name='groupChk']").prop("checked",$(this).prop("checked"))
						})
					}
				}).data("kendoGrid");
				groupWindow.obj.center().open();
			},

			init: function() {

				if (!groupWindow.id.data("kendoWindow")) {
					groupWindow.id.kendoWindow({
						width: "700px",
						actions: ["Close"],
						modal: true,
						title: "任务管理"
					});
				}
				groupWindow.obj = groupWindow.id.data("kendoWindow");
				groupWindow.saveClick();
				$(".cancelBtns").on("click", function(){
					groupWindow.obj.close();
				});
			}
		};

	groupWindow.init();
	
	$("#saveGroupBtns").on("click",function(){
		var value = "";
		 $.each($("[name='groupChk']:checked"),function(index,item){
			 value += ","+$(this).val();
		 });
		updateItem({roles :value},groupWindow);
    });
	
	
	function updateItem(items,itemWindow){
		 
		 $.ajax({
	    		url : links,
	    		type : "PATCH",
	    		data: kendo.stringify(items),
	    		dataType: "json",
	            contentType: "application/json;charset=UTF-8",
	    		success : function(data) {
	    			itemWindow.obj.close();
	        		 infoTip({content: "保存成功！",color:"#D58512"});
	    		}
		 });
	}
	
	
	var commandWindow = {
		obj: undefined,
		template: undefined,
		id: $("#commandWindow"),
		saveClick: function() {
			
		},
		initContent: function(array,_links) {
			allArray = [];
			allArray = array;
			commandWindowGrid = $("#commandGrid").kendoGrid({
				dataSource: {
		            transport: {
		                read: {
		                    dataType: "json",
		                    url: "rest/check-item"
		                }
		            },
		            schema : {
		    			data : function(d) {
		    				links = _links;
		    				if(d._embedded){
		    					var commandArray = [];
		    					$.each(d._embedded["check-item"],function(index,item){
		    						item.flag = $.inArray(item.itemId+"",allArray);
		    						commandArray[commandArray.length] = item;
		    					})
		    					return commandArray;  //响应到页面的数据
		    			     }else{
		    					return new Array();
		    				}
		    			}
		    		},
		            filter:{
		                logic: "or",
		                filters: [
		                    {
		                        field: "emsType",
		                        operator: "eq",
		                        value: "EMS"
		                    }
		                ]
		            }
		        },
				height: $(window).height()-$("#commandGrid").offset().top - 330,
				width: "100%",
				reorderable: true,

				resizable: true,

				sortable: false,

				columnMenu: false,

				pageable: false,

				columns: [{
						width: 30,
						template: "<input type='checkbox' value='#:itemId#' name='commandChk' #if(flag != -1){# checked='checked' #}# />",
						attributes:{"class": "text-center"},
						title: "<input type='checkbox' class='grid_checkbox1'/>"
					},{
						field: "name",
						template: "<span  title='#:name#'>#:name#</span>",
						title: "<span  title='指令名称'>指令名称</span>"
					}, {
						field: "command",
						template: "<span  title='#:command#'>#:command#</span>",
						title: "<span  title='指令内容'>指令内容</span>"
					},{
						field: "remarks",
						template: "<span  title='#:remarks#'>#:remarks#</span>",
						title: "<span  title='适用网元类型'>适用网元类型</span>"
					}],
				dataBound: function() {
					$(".grid_checkbox1").on("click",function(){
						$("[name='commandChk']").prop("checked",$(this).prop("checked"))
					})
					$("[name = 'commandChk']").on("click",function(){
						if($(this).prop("checked") == true){
							allArray[allArray.length] = $(this).val()+"";
						}else{ 
							var t = $(this).val()+"";
							$.each(allArray,function(index,item){
								if(item == t){
									allArray[index] = "";
								}
							})
						}
					})
				}
			}).data("kendoGrid");
			commandWindow.obj.center().open();
			commandWindowGrid.dataSource.filter([]);
//	    	commandWindowGrid.dataSource.fetch();
		},

		init: function() {

			if (!commandWindow.id.data("kendoWindow")) {
				commandWindow.id.kendoWindow({
					width: "700px",
					actions: ["Close"],
					modal: true,
					title: "任务管理"
				});
			}
			commandWindow.obj = commandWindow.id.data("kendoWindow");
			commandWindow.saveClick();
			$(".cancelBtns").on("click", function(){
				commandWindow.obj.close();
			});
		}
	};

	commandWindow.init();
	
	$('#commandInput').on('keyup',function (event){
		commandWindowGrid.dataSource.read();
		var filters = [];
    	if($("#commandInput").val() != ""){
    		filters.push({field: "name", operator: "contains", value: $("#commandInput").val()});
    	}
    	commandWindowGrid.dataSource.filter(filters);
//    	commandWindowGrid.dataSource.fetch();
    });
	
	
	$("#saveCommandBtns").on("click",function(){
		 var value = "";
		 $.each(allArray,function(index,item){
			 if(item){
				 value += ","+item;
			 }
		 });
		 updateItem({commands:value},commandWindow);
   });
	
	
	
	//单元
	var netElementWindow = {
		obj: undefined,
		template: undefined,
		id: $("#netElementWindow"),
		saveClick: function() {
			
		},
		initContent: function(array,_links) {
			allArray = [];
			allArray = array;
			netElementGrid = $("#netElementGrid").kendoGrid({
				dataSource: {
		            transport: {
		                read: {
		                    dataType: "json",
		                    url: "rest/equipment-unit"
		                }
		            },
		            schema : {
		    			data : function(d) {
		    				links = _links;
		    				if(d._embedded){
		    					var unitArray = [];
		    					$.each(d._embedded["equipment-unit"],function(index,item){
		    						item.flag = $.inArray(item.unitId+"",allArray);
		    						unitArray[unitArray.length] = item;
		    					})
		    					return unitArray;  //响应到页面的数据
		    			     }else{
		    					return new Array();
		    				}
		    			}
		    		}
		        },
				height: $(window).height()-$("#netElementGrid").offset().top - 330,
				width: "100%",
				reorderable: false,

				resizable: true,

				sortable: false,

				columnMenu: false,

				pageable: false,

				columns: [{
						width: 30,
						template: "<input type='checkbox' value='#:unitId#' name='unitChk' #if(flag != -1){# checked='checked' #}# />",
						attributes:{"class": "text-center"},
						title: "<input type='checkbox' class='grid_checkbox'/>"
					},{
						field: "neName",
						template: "<span  title='#:neName#'>#:neName#</span>",
						title: "<span  title='网元名称'>网元名称</span>"
					}, {
						field: "neType",
						template: "<span  title='#:neType#'>#:neType#</span>",
						title: "<span  title='单元名称'>网元类型</span>"
					},{
						field: "unitName",
						template: "<span  title='#:unitName#'>#:unitName#</span>",
						title: "<span  title='单元类型'>单元名称</span>"
					},  {
						field: "unitType",
						template: "<span  title='#:unitType#'>#:unitType#</span>",
						title: "<span  title='单元类型'>单元类型</span>"
					}],
				dataBound: function() {
					$(".grid_checkbox").on("click",function(){
						$("[name='unitChk']").prop("checked",$(this).prop("checked"))
					})
					$("[name = 'unitChk']").on("click",function(){
						if($(this).prop("checked") == true){
							allArray[allArray.length] = $(this).val()+"";
						}else{ 
							console.log(allArray);
							var t = $(this).val()+"";
							$.each(allArray,function(index,item){
								if(item == t){
									allArray[index] = "";
									alert($(this).val() + "22");
								}
							})
							console.log(allArray + "aaa");
						}
					})
				}
			}).data("kendoGrid");
			netElementWindow.obj.center().open();
		},

		init: function() {

			if (!netElementWindow.id.data("kendoWindow")) {
				netElementWindow.id.kendoWindow({
					width: "700px",
					actions: ["Close"],
					modal: true,
					title: "任务管理"
				});
			}
			netElementWindow.obj = netElementWindow.id.data("kendoWindow");
			netElementWindow.saveClick();
			$(".cancelBtns").on("click", function(){
				netElementWindow.obj.close();
			});
		}
	};

	netElementWindow.init();
	
	$("#saveUnitBtns").on("click",function(){
		 var value = "";
		 $.each(allArray,function(index,item){
			 if(item != ""){
				 value += ","+item;
			 }
		 });
		 updateItem({units:value},netElementWindow);
    });
	
	function unitGridFilter(){
		netElementGrid.dataSource.read();
		var filters = [];
    	if($("#neTypeList").val() != ""){
    		filters.push({field: "neType", operator: "eq", value: $("#neTypeList").val()});
    	}
    	if($("#inputNeTrigger").val() != ""){
    		filters.push({field: "neName", operator: "eq", value: $("#inputNeTrigger").val()});
    	}
    	if($("#unitTypeList").val() != ""){
    		filters.push({field: "unitType", operator: "eq", value: $("#unitTypeList").val()});
    	}
    	if($("#unitNameInput").val() != ""){
    		filters.push({field: "unitName", operator: "contains", value: $("#unitNameInput").val()});
    	}
    	netElementGrid.dataSource.filter(filters);
    	netElementGrid.dataSource.fetch();
	}
	
	//修改、增加弹窗
	var infoWindow = {
		obj: undefined,
		template: undefined,
		id: $("#infoWindow"),
		saveClick: function() {
			//保存 【添加】单元基础信息
			$("#saveBaseInfoBtn").on("click", function() {
				var jobType = $("#jobType").val();
				var jobName = $("#jobName").val();
				var jobDesc = $("#jobDesc").val();
				if(jobName == ""){
					infoTip({content: "请输入方案名称！",color:"#D58512"});
					return;
				}
				if(execTime == ""){
					infoTip({content: "请输入开始时间！",color:"#D58512"});
					return;
				}
				var execTime = $("#execDate").val();
				var emsCheckJob = { jobType : jobType,jobName : jobName, jobDesc : jobDesc, 
										execDate : execTime , nextDate : execTime , hour : $("#hourText").val() };
				$.ajax({
	          		url : dataItem._links ? dataItem._links.self.href : "/rest/ems-check-job",
	          		type : dataItem._links ? "PATCH" : "POST",
	          		data: kendo.stringify(emsCheckJob),
	          		dataType: "json",
	                contentType: "application/json;charset=UTF-8",
	          		success : function(data) {
	          					infoWindow.obj.close();
	  	        				infoTip({content: "保存成功！",color:"#D58512"});
	  	        				dataSource.read();
	          		},
	          		error:function(data){
	          			infoTip({content: "保存失败！",color:"#D58512"});
	          		}
				});
			});
		},
		initContent: function(dataItem) {
			$("#jobType").val(dataItem.jobType);
			$('#jobType').data("kendoDropDownList").text(execTypes[dataItem.jobType]);
			$("#hourInput").hide();
            if(dataItem.jobType == 6){
            	$("#hourInput").show();
            }
            $("#hourInput").val(dataItem.hour);
			$("#jobName").val(dataItem.jobName);
			$("#jobDesc").val(dataItem.jobDesc);
			$("#execDate").val(dataItem.execDate);
			infoWindow.obj.center().open();
		},

		init: function() {

			if (!infoWindow.id.data("kendoWindow")) {
				infoWindow.id.kendoWindow({
					width: "700px",
					actions: ["Close"],
					modal: true,
					title: "任务管理"
				});
			}
			infoWindow.obj = infoWindow.id.data("kendoWindow");
			infoWindow.saveClick();
			$(".cancelBtns").on("click", function(){
				infoWindow.obj.close();
				dataSource.read();
			});
		}
	};
	infoWindow.init();
	
	$("#addBtn").on("click", function() {
		infoWindow.obj.setOptions({ "title": "添加方案" });
		dataItem = {jobType : "0",jobName : "", jobDesc : "", execDate : "" ,hour : ""};
		infoWindow.initContent(dataItem);
	});
	
	$("#unitNameInput").on("keyup", function() {
		unitGridFilter();
	});
	
	
	$('#jobType').kendoDropDownList({
        filter: "contains",
        dataTextField: "name",
        dataValueField: "value",
        dataSource: [{name:"每5分钟",value:0},
                     {name:"每10分钟",value:1},
                     {name:"每15分钟",value:2},
                     {name:"每20分钟",value:3},
                     {name:"每30分钟",value:4},
                     {name:"每小时",value:5},
                     {name:"每n小时",value:6},
                     {name:"每天",value:7}],
        change:function(){
        	 $("#hourText").val("");
        	 $("#hourInput").hide();
             if($(jobType).val() == 6){
            	 $("#hourInput").show();
             }
        }
	}).data("kendoDropDownList");
	$("#execDate").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:mm:ss"
	});
	
})


