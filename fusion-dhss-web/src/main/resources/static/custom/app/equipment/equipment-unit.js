
function unitRefreshGrid(reCount,gridDataSource){
  setInterval(function(){
		if(reCount>0){
			gridDataSource.read();
			reCount--;
		}
	 },1000*10);
}
var inputLocation;
var inputDhssName;
var inputNeType;
var inputUnitType;
var inputNe;

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

function compareAsc(propertyName) {
	return function (object1, object2) {
		var value1 = object1[propertyName];
		var value2 = object2[propertyName];
		if (value2 > value1) {
			return -1;
		}else if (value2 < value1) {
			return 1;
		}else {
			return 0;
		}
	}
}




var listData;


$(function() {
	
	//下拉开始
	$.ajax({
		url: "find/DropDownList",
		dataType:"json",
		success:function(data){
			listData = data;
			$("#inputDhssNameTrigger").kendoDropDownList({
				optionLabel:"全部DHSS组",
				dataSource: data.dhssNames,
				filter: "contains",
				suggest: true,
				change:function(event){
					var val = event.sender._old;
					var locations = [];
					if(val != ""){
						$.each(data.locations,function(index,item){
							if(item.indexOf(val+　"_")　!= -1 ){
								locations[locations.length] = item.split("_")[1];
							}
						})
					}
					inputLocation.setDataSource(locations);
					gridFilter();
					gridNeFilter(inputNe);
					
				}
			});
			var inputLocation = $("#inputLocationTrigger").kendoDropDownList({
				optionLabel:"全部局址",
				dataSource: [],
				filter: "contains",
				suggest: true,
				change:function(event){
					gridFilter();
					gridNeFilter(inputNe);
				}
			}).data("kendoDropDownList");
			
			var inputNeType = $("#inputNeTypeTrigger").kendoDropDownList({
				optionLabel:"全部网元类型",
				dataSource: data.neTypes,
				filter: "contains",
				suggest: true,
				change:function(event){
					gridFilter();
					gridNeFilter(inputNe);
					var types = typesFilter(data.types,event.sender._old);
					inputUnitType.setDataSource(types);
				}
			}).data("kendoDropDownList");
			
			inputNe = $("#inputNeTrigger").kendoDropDownList({
				optionLabel:"全部网元",
				dataSource: data.nes,
				dataTextField: "neName",
				dataValueField: "neName",
				filter: "contains",
				suggest: true,
				change:function(event){
					console.log(event);
					gridFilter();
				}
			}).data("kendoDropDownList");
			var types = typesFilter(data.types.sort(compareAsc("unitType")),"");
			var inputUnitType = $("#inputUnitTypeTrigger").kendoDropDownList({
				optionLabel:"全部单元类型",
				dataSource: typesFilter(data.types,""),
				filter: "contains",
				suggest: true,
				change:function(event){
					gridFilter();
				}
			}).data("kendoDropDownList");
			 
		} 
	});
	
	
	function typesFilter(arr,text){
		var types = [];
		$.each(arr,function(i,item){
			if(text == "" && $.inArray(item.unitType,types) == -1){
				types[types.length] = item.unitType;
			}else{
				if(text == item.neType){
					types[types.length] = item.unitType;
				}
			}
		})
		return types;
	}
	
	function gridNeFilter(obj){
		var filters = [];
		if($("#inputDhssNameTrigger").val() != "")
        	filters.push({field: "dhssName", operator: "eq", value: $("#inputDhssNameTrigger").val()});
    	if($("#inputLocationTrigger").val() != "")
        	filters.push({field: "location", operator: "eq", value: $("#inputLocationTrigger").val()});
    	if($("#inputNeTypeTrigger").val() != "")
        	filters.push({field: "neType", operator: "eq", value: $("#inputNeTypeTrigger").val()});
    	if($("#numKeyWord").val() != "")
        	filters.push({field: "numberSections", operator: "contains", value: $("#numKeyWord").val()});
    	obj.dataSource.filter(filters);
	}
	
	//下拉结束
	 
	$('#inputKeyWord,#numKeyWord').on("keyup",function(){
		gridFilter();
		gridNeFilter(inputNe);
	})
	
	
	
	function gridFilter(){
		var netext = $('#inputNeTrigger').val();
		var neTypetext = $('#inputNeTypeTrigger').val();
		var dhssNametext = $('#inputDhssNameTrigger').val();
		var locationtext = $('#inputLocationTrigger').val();
		var unitTypetext = $('#inputUnitTypeTrigger').val();
		
		var filters = [];

    	if(netext != "")
        	filters.push({field: "neName", operator: "eq", value: netext});
    	if(neTypetext != "")
        	filters.push({field: "neType", operator: "eq", value: neTypetext});
    	if(dhssNametext != "")
        	filters.push({field: "ne.dhssName", operator: "eq", value: dhssNametext});
    	if(locationtext != "")
        	filters.push({field: "ne.location", operator: "eq", value: locationtext});
    	if(unitTypetext != "")
        	filters.push({field: "unitType", operator: "eq", value: unitTypetext});
    	
    	
    	var searchKey = $('#inputKeyWord').val();
    	if(searchKey != "")
        	filters.push({field: "unitNameAll", operator: "contains", value: searchKey});
    	
    	var numberSections = $("#numKeyWord").val();
    	if(numberSections != "")
    		filters.push({field: "ne.numberSections", operator: "contains", value: numberSections});
    	
    	gridDataSource.filter(filters);
    	gridDataSource.fetch();
	}
	
	
	
	
	
	
	
	

	//下拉结束
	
	
	
	
	
	
	
	
	
	var gridDataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: "equipment-unit/search/all",//
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            },
        },
        pageSize:20,
        schema: {
            data: function (data) {
            	return data.sort(compare("id"));
            },
            total: function(data) {
            	return data.length;
            }
        },
        serverPaging : false,
		serverFiltering : false,
		serverSorting : false,
	});
	
	var gridFilterMap = {neName:"",unitType:"",unitNameAll:""};


	$('#inputKeyWord').on("keyup",function(event){
		gridFilter();
	});

	
	//console.log(unitlogin);
	var editButton = "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 编辑单元</button>&nbsp;&nbsp;" +
	"<button class='deleteBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon-remove-sign'></i> 删除</button>&nbsp;&nbsp;"/*+
	"<button class='crtBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon glyphicon'></i> CRT </button>"*/;
	var terminalButton = "<button class='terminalBtn btn btn-xs btn-primary'><i class='glyphicon glyphicon-remove-sign'></i> 测试终端 </button>&nbsp;&nbsp;";
	var dataGridObj = {
		init: function() {
			this.dataGrid = $("#dataGrid").kendoGrid({
				dataSource: gridDataSource,
				height: $(window).height() - $("#dataGrid").offset().top - 50,
				reorderable: true,
				resizable: true,
				sortable: true,
				pageable: true,
				columns: [{ 
					field: "id", 
					title:"id", 
					hidden:true
				},{
					field: "neType",
					template: "<span  title='#:neType#'>#:neType#</span>",
					title: "<span  title='网元类型'>网元类型</span>"
				},{
					field: "neName",
					template: "<span  title='#:neName#'>#:neName#</span>",
					title: "<span  title='网元名称'>网元名称</span>"
				}, {
					field: "unitType",
					width:110,
					template: "<span  title='#:unitType#'>#:unitType#</span>",
					title: "<span  title='单元类型'>单元类型</span>"
						
				}, {
					field: "unitName",
					template: "<span  title='#:unitName#'>#:unitName#</span>",
					title: "<span  title='单元名称'>单元名称</span>"
				}, {
					field: "serverIp",
					template: "<span  title='#:serverIp#'>#:serverIp#</span>",
					title: "<span  title='MML接口地址'>MML接口地址</span>"
				}, {
					field: "serverProtocol",
					template: "<span  title='#=serverProtocol#'>#=serverProtocol#</span>",
					title: "<span  title='MML登录协议'>MML登录协议</span>"
				}, {
					field: "isForbidden",
					width:110,
					template: "#if(isForbidden ==0){#<span  title='启用'>启用</span>#}else{#<span title='禁用'>禁用</span>#}#",
					title: "<span  title='单元状态'>单元状态</span>"
				}, {
					width: unitlogin?150:250,
					template: "#if(isForbidden ==0){#" + (unitlogin?terminalButton:editButton) + "#}#",
					title: "<span  title='操作'>操作</span>"
				},{
					field:"serverName",
					template: "#:serverIp##:unitName#",
					hidden:true
					}
				],
				dataBound: function() {
					dataGridObj.addClick();
					dataGridObj.editClick();
					dataGridObj.terminalClick();
					dataGridObj.deleteClick();
					dataGridObj.crtClick();
				}
			}).data("kendoGrid");
		},

		//添加按钮，显示弹窗
		addClick: function() {
			$("#addBtn").on("click", function() {
				//添加时，默认隐藏[Web接口]Tab，当点击【保存 基本信息】按钮之后，显示[Web接口]Tab
				$("#webInterfaceTab").addClass("hidden");
				dataItem = {
						unitId:"",
						neId: "",
						neType:"",
						unitType: "",
						unitName: "",
						serverPort:"",
						serverIp: "",
						serverProtocol: "",
						rootPassword:"",
						loginName:"",
						loginPassword:""
				};

				infoWindow.obj.setOptions({
					"title": "添加单元"
				});

				infoWindow.initContent(dataItem);
			});
		},

		terminalClick: function() {
			$(".terminalBtn").on("click", function() {
				terminalWindow.init();
				var currentRow = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
				terminalWindow.obj.setOptions({
					"title": "单元 "+currentRow.unitName+" ["+currentRow.serverIp+"]"
				});
				terminalWindow.initContent(currentRow);
			});
		},
		//编辑
		editClick: function() {
			
			$(".editBtn").on("click", function() {
				
				//编辑时，默认显示[Web接口]Tab
				$("#webInterfaceTab").removeClass("hidden");
				
				dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
				infoWindow.obj.setOptions({
					"title": "修改单元"
				});
				infoWindow.initContent(dataItem);
			});
		},

		//删除
		deleteClick: function() {
			$(".deleteBtn").on("click", function() {
				if (confirm("确定删除吗？")) {
					var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
					$.ajax({
						url : "/equipment-unit/dele",
	            		type : "post",
	            		data: kendo.stringify(dataItem),
	            		dataType: "json",
	                    contentType: "application/json;charset=UTF-8",
	            		success : function(data) {	
	    				    infoTip({content: "删除单元基础信息命令已经发送，请等待指令执行结果返回！"});
	    				    //gridDataSource.read();
	    				    unitRefreshGrid(10,gridDataSource);
        				}
        			});				
				}
			});
		},
		crtClick:function(){
			$(".crtBtn").on("click", function() {
				var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
				window.location.href = dataItem.serverUrl;
			});
		}
	};
	var connection = null;
	var terminal = null;
	var cid = "";
	
	var keys = {};

    var termFocus = true;
	//弹窗
	var terminalWindow = {
		obj : undefined,
		template : undefined,
		terminal : undefined,
		id : $("#terminalWindow"),
		saveClick : function() {
		},
		initContent : function(dataItem) {
			var handUrl = (dataItem._links.self.href);
			var lastiIndex = dataItem._links.self.href.lastIndexOf("/");
			var uid = handUrl.substring(lastiIndex+1);
			$.ajax({
				"dataType" : 'text',
				"type" : "GET",
				"url" : "console/login",
				"data" : {
					uid : uid,
					termType : 'linux',
					termXlength : '90',
					termYlength : '30',
					timestamp : (new Date()).getTime()
				},
				timeout : 15000,
				"success" : function(data) {
					if (data != null && data != "") {
						//取 token
						cid = data;
						// WebSocket 
						var loc = window.location, ws_uri;
						if (loc.protocol === "https:") {
							ws_uri = "wss:";
						} else {
							ws_uri = "ws:";
						}
						ws_uri += "//" + loc.host + loc.pathname + '/../websocket/'
								+ cid + '/terms.ws?t=' + new Date().getTime();
						// 连接 WebSocket
						connection = new WebSocket(ws_uri);
						// Log errors
						connection.onerror = function(error) {
							//console.log('WebSocket Error ' + error);
						};
						// Log messages from the server
						connection.onmessage = function(e) {
							var json = jQuery.parseJSON(e.data);
							if (json.output == "Connect to DHSS Successful\n") {
								terminalWindow.obj.center().open();
								$('#dummy').focus();
								terminal = new Terminal({
									cols : 90,
									rows : 30,
									screenKeys : false,
									useStyle : true,
									cursorBlink : true,
									convertEol : true
								});
								terminal.open($("#terminalOutput"));
								terminal.write(json.output);
							} else if (json.output && json.output.length != 0) {
								terminal.write(json.output);
							}
						};
					}
				},
				"error" : function(data) {
					infoTip({
						content : "连接超时!"
					});
				}
			});
		},
		init : function() {
			if (!terminalWindow.id.data("kendoWindow")) {
				terminalWindow.id.kendoWindow({
					width : "850px",
					height : "520px",
					actions : [ "Close" ],
					modal : true,
					title : "",
					close : function(event) {
						$.ajax({
							"dataType" : 'text',
							"type" : "GET",
							"url" : "console/logout?cid=" + cid,
							"success" : function() {
								cid = "";
								termFocus = false;
								terminal.destroy();
								$("#terminalOutput").html("");
							}
						});
					},
				});
			}
			terminalWindow.obj = terminalWindow.id.data("kendoWindow");
		}
	};

	$('#dummy').focus();

	$(document).keypress(
			function(e) {
				//console.log("keypress termFocus:" + termFocus);
				if (termFocus) {
					var keyCode = (e.keyCode) ? e.keyCode : e.charCode;
					//console.log(keyCode);
					if (String.fromCharCode(keyCode)
							&& String.fromCharCode(keyCode) != ''
							&& (!e.ctrlKey || e.altKey) && !e.metaKey && !keys[27]
							&& !keys[37] && !keys[38] && !keys[39] && !keys[40]
							&& !keys[13] && !keys[8] && !keys[9] && !keys[46]
							&& !keys[45] && !keys[33] && !keys[34] && !keys[35]
							&& !keys[36]) {
						var cmdStr = String.fromCharCode(keyCode);
						connection.send(JSON.stringify({
							cid : cid,
							command : cmdStr
						}));
					}
				}
			});
	//function for command keys (ie ESC, CTRL, etc..)
	$(document).keydown(
					function(e) {
						if (termFocus) {
							var keyCode = (e.keyCode) ? e.keyCode : e.charCode;
							keys[keyCode] = true;
							//console.log(keyCode);

							//27 - ESC
							//37 - LEFT
							//38 - UP
							//39 - RIGHT
							//40 - DOWN
							//13 - ENTER
							//8 - BS
							//9 - TAB
							//17 - CTRL
							//46 - DEL
							//45 - INSERT
							//33 - PG UP
							//34 - PG DOWN
							//35 - END
							//36 - HOME
							if ((e.ctrlKey && !e.altKey) || keyCode == 27
									|| keyCode == 37 || keyCode == 38
									|| keyCode == 39 || keyCode == 40
									|| keyCode == 13 || keyCode == 8
									|| keyCode == 9 || keyCode == 46
									|| keyCode == 45 || keyCode == 33
									|| keyCode == 34 || keyCode == 35
									|| keyCode == 36) {
								connection.send(JSON.stringify({
									cid : cid,
									keyCode : keyCode
								}));
							}

							//prevent default for unix ctrl commands
							if (e.ctrlKey
									&& (keyCode == 83 || keyCode == 81
											|| keyCode == 84 || keyCode == 220
											|| keyCode == 90 || keyCode == 72
											|| keyCode == 87 || keyCode == 85
											|| keyCode == 82 || keyCode == 68)) {
								e.preventDefault();
								e.stopImmediatePropagation();
							}

						}

					});

	$(document).keyup(function(e) {
		//console.log("keyup termFocus:" + termFocus);

		var keyCode = (e.keyCode) ? e.keyCode : e.charCode;

		delete keys[keyCode];
		if (termFocus) {
			$('#dummy').focus();
		}
	});

	//get cmd text from paste
	$(this).bind('paste', function(e) {
		$('#dummy').focus();
		$('#dummy').val('');
		setTimeout(function() {
			var cmdStr = $('#dummy').val();
			if(connection)
			connection.send(JSON.stringify({
				cid : cid,
				command : cmdStr
			}));
		}, 100);
	});
	
	
	
	//弹窗
	var infoWindow = {
		obj: undefined,
		template: undefined,
		id: $("#infoWindow"),

		saveClick: function() {
			//保存 【添加】单元基础信息
			$("#saveBaseInfoBtn").on("click", function() {
				//根据【Web接口】tab （隐藏/显示) 判断：添加单元or（添加后）编辑单元
				//赋值不需要传给MQ的值
				
					
				dataItem.neId = $("#wyName").val();
				dataItem.neType = $("#neType").val();
				dataItem.unitType = $("#unitType").val();
				dataItem.serverProtocol = $("#protocolType").val();
				dataItem.serverIp = $("#page-ip").val();
				dataItem.rootPassword = $("#page-rootPassword").val();
				dataItem.loginName = $("#page-username").val();
				dataItem.loginPassword = $("#page-password").val();
				dataItem.unitName = $("#page-unitName").val();
				dataItem.serverPort = $("#page-port").val();
				if ($("#webInterfaceTab").hasClass("hidden")) { //添加单元
					if(dataItem.neId == "" || dataItem.neId == "全部网元"){
						infoTip({content: "请选择网元！"});
						return;
					}
					if(dataItem.unitType == ""){
						infoTip({content: "请选择单元！"});
						return;
					}
					if(dataItem.unitName == ""){
						infoTip({content: "请输入单元名称！"});
						return;
					}
					if(dataItem.serverProtocol == null){
						infoTip({content: "请选择MML登陆协议！"});
						return;
					}
					if(dataItem.serverIp == ""){
						infoTip({content: "请输入MML接口地址！"});
						return;
					}
					if(dataItem.serverPort == ""){
						infoTip({content: "请输入MML接口端口！"});
						return;
					}
					$.ajax({
	            		url : "/equipment-unit/addOrEdit",
	            		type : "post",
	            		data: kendo.stringify(dataItem),
	            		dataType: "json",
	                    contentType: "application/json;charset=UTF-8",
	            		success : function(data) {	
	            			infoTip({content: "添加单元基础信息命令已经发送，请等待指令执行结果返回！"});
	            			//gridDataSource.read();
	            			$.ajax({
	    	            		url : "resource/"+data.id,
	    	            		type : "POST",
	    	            		dataType: "json",
	    	                    contentType: "application/json;charset=UTF-8",
	    	            		success : function(data1) {
	    	            		},
	    	            		fail : function(data) {
	    	            			showNotify(data.message,"error");
	    	            		}
	    	            	});
	        			    dataItem.unitId = data.unitId;
	        			    unitRefreshGrid(10,gridDataSource);
	        			    //显示Web接口
	    					$("#webInterfaceTab").removeClass("hidden");
	    					$('#webInterfaceTab a').tab('show');
	            		},
	            		error : function(data) {
	            			infoTip({content: "单元名称不能重复！"});
	            		}
					});					
				} else {															
					//获取原数据，判断是否进行过改变					
					var unitType_id = $("#unitType").attr("data-unitType");
					
					//当前数据
					var changed_unitType_id = $("#unitType").val();	
					//单元类型修改，删除原的webinterface
					if(changed_unitType_id != unitType_id){						
						$.ajax({
							url : "rest/equipment-unit/"+dataItem.unitId+"/webInterface",
							type : "GET",
							contentType: "text/uri-list",
							success:function(data){
								  if(data._embedded){
									var str_data = data._embedded["equipment-webinterface"];
									if(str_data !=undefined){																			
									  $.each(str_data,function(index,item){
										$.ajax({
											url : item._links.self.href,
						            		type : "delete",
						            		dataType: "json",
						                    contentType: "application/json;charset=UTF-8",
					        				success:function(data){	
					        					var parameter = {};
					        					parameter.ip = $("#page-ip").val();
					        					parameter.unit_id =  dataItem.unitId; 
					        				    parameter.unit_type_id =  $("#unitType option:selected").val();
					        					webInterfaceObj.gridObj.dataSource.read(parameter);
					        				}
					        			});	
									 }); 
								   }							
							   }
							}
						});
					}
					
					$.ajax({
	            		url : "/equipment-unit/addOrEdit",
	            		type : "post",
	            		data: kendo.stringify(dataItem),
	            		dataType: "json",
	                    contentType: "application/json;charset=UTF-8",
	            		success : function(data) {	
	            			infoTip({content: "修改单元基础信息命令已经发送，请等待指令执行结果返回！"});
	            			unitRefreshGrid(10,gridDataSource);
       					    dataItem.unitId = data.unitId;
	            		},
	            		fail : function(data) {
	            			showNotify(data.message,"error");
	            		}
					});
					
					$('#webInterfaceTab a').tab('show');
				}
			});
			
			//保存WEB接口信息
			$("#saveWebInterfaceBtn").on("click", function() {				
				var data = webInterfaceObj.gridObj._data;
				
				 $.each(data,function(index,item){
					 console.log(item);
					 var dto = {};
					 dto.id = item.equipmentWebInterfaceId;
					 dto.interfaceType = item.interfaceType;
					 dto.url = item.url;
					 dto.password = item.password;
					 dto.userName = item.userName;
					 $.ajax({
		            		url : "rest/equipment-webinterface",
		            		type : "post",
		            		data: kendo.stringify(dto),
		            		dataType: "json",
		                    contentType: "application/json;charset=UTF-8",
		            		success : function(data) {
		            			
		            			$.ajax({
    	            				url : data._links.unit.href,
    	            				type : "PUT",
    	            				data : "unit/"+dataItem.unitId,
    	            				contentType: "text/uri-list",
    	            				success:function(data1){
    	            					
    	            				}
    	            			});		            			
		            		},
		            		fail : function(data) {
		            			showNotify(data.message,"error");
		            		}
					 });
				 });
				 infoWindow.obj.close();
			});
		},
		initContent: function(dataItem) {
			//填充弹窗内容
			
			//记录Root密码、默认登录用户名、登录密码原数据：编辑时标示是否改变
			
			if(!dataItem.unitType){
				dataItem.unitType = "";
			}
			$("#unitType").attr("data-unitType",dataItem.unitType);
			//…………………………
			$('#tt1').addClass('active');//显示第一个面板
			$("#webInterfaceTab").removeClass("active");
			$("#home").removeClass("tab-pane fade");
			$("#home").addClass("tab-pane fade in active");
			$("#tab2").removeClass("tab-pane fade in active");
			$("#tab2").addClass("tab-pane fade");
			infoWindow.obj.center().open();
			$("#page-unitName").val(dataItem.unitName);
			$("#page-ip").val(dataItem.serverIp);
			$("#page-rootPassword").val(dataItem.rootPassword);
			$("#page-username").val(dataItem.loginName);
			$("#page-password").val(dataItem.loginPassword);
			$("#page-port").val(dataItem.serverPort);
			if(dataItem.unitId !=""){
				$("#page-username").attr("disabled","disabled");
				$("#page-unitName").attr("disabled","disabled");
			}else{
				$("#page-username").attr("disabled",false);
				$("#page-unitName").attr("disabled",false);
			}

			/*下拉框部分*/
			var inputNeTriggerForm = $('#wyName').kendoDropDownList({
				optionLabel: "请选择网元",
		        dataTextField: "neName",
		        dataValueField: "neId",
		        filter: "contains",
		        dataSource: listData.nes,
		        select:function(event){
//		        	var dataItem = (this.dataItem(event.item));
//		        	typeRelDataSource.filter({field: "neType", operator: "eq", value:dataItem.neType});
//		        	typeRelDataSource.fetch();
		        }
			}).data("kendoDropDownList");
			
			var inputUnitTypeTriggerForm =  $('#unitType').kendoDropDownList({
				optionLabel: "请选择单元类型",
//		        dataTextField: "unitType",
//		        dataValueField: "unitType",
		        dataSource:typesFilter(listData.types,""),
			}).data("kendoDropDownList");
			

			var inputProtocolTypeTriggerForm =  $('#protocolType').kendoDropDownList({
		        dataSource:["SSH","TELNET"]
			}).data("kendoDropDownList");
			
			inputNeTriggerForm.value(dataItem.neId);
			inputUnitTypeTriggerForm.value(dataItem.unitType);
			inputProtocolTypeTriggerForm.value(dataItem.serverProtocol);
			
		},

		init: function() {

			if (!infoWindow.id.data("kendoWindow")) {
				infoWindow.id.kendoWindow({
					width: "700px",
					height: "540px",
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
						unitId:"",
						neId: "",
						neType:"",
						unitType: "",
						unitName: "",
						serverPort:"",
						serverIp: "",
						serverProtocol: "",
						rootPassword:"",
						loginName:"",
						loginPassword:""
				};
				infoWindow.obj.close();
				gridDataSource.read();
			});
		}
	};
	
	

	dataGridObj.init();
	
	terminalWindow.init();

	infoWindow.init();
	
	//Web接口数据
	var webInterfaceObj = {
		
	  
		gridObj: undefined,
		
		init: function(webInterfaceDataSource){
			
			this.gridObj = $("#webInterfaceList").kendoGrid({
		
				dataSource:webInterfaceDataSource,				
				height: 340,				
				reorderable: true,		
				resizable: true,		
				sortable: true,		
				columnMenu: true,		
				pageable: false,
				editable: true,
				columns: [{
					field: "url",
					width: 240,
					template: "<span  title='#:url#'>#:url#</span>",
					title: "<span  title='URL'>URL</span>"
				},{
					field: "interfaceType",
					template: "<span  title='#:interfaceType#'>#:interfaceType#</span>",
					title: "<span  title='接口类型'>接口类型</span>"
				},  {
					field: "userName",
					template: "#if(!userName || userName.length<=0){# <small style='color: lightGray'>点击输入Web接口用户名</small> #} else{# <span title='#:userName#'>#:userName#</span> #}#",
					
					title: "<span  title='用户名称'>用户名称</span>"
				},{
					field: "password",
					template: "#if(!password || password.length<=0){# <small style='color: lightGray'>点击输入Web接口密码</small> #} else{# <span title='#:password#'>#:password#</span> #}#",
					title: "<span  title='密码'>密码</span>"
				}],
				dataBound: function(){
					
				}
			}).data("kendoGrid");
		}
	};
	
	//第一次打开Web接口tab后,再加载数据
	$('a[data-toggle="tab"][href="#tab2"]').on('shown.bs.tab', function (e) {
		var parameter = {};
		parameter.ip = $("#page-ip").val();
		parameter.unit_id = dataItem.unitId;
	    parameter.unit_type_id =  $("#unitType").val();//$("#unitType option:selected").val();
		
		
		var webInterfaceDataSource = new kendo.data.DataSource({
			 transport: {
				 read: {
						type: "GET",
						url: "equipment-WebInterface/queryWebInterfaceByUnit",
						dataType: "json",
						contentType: "application/json;charset=UTF-8"
					},
				 parameterMap: function (options, operation) {
	                if (operation == "read") {	                	
	                	
	                    return parameter;
	                }
	            }
			 },
			 schema: {
					model: {
						fields: {
							url: {
								editable: true,
								type: "string"
							},
							interfaceType: {
								editable: false,
								type: "string"
							},
							password: {
								editable: true,
								type: "string"
							},
							userName: {
								editable: true,
								type: "string"
							}
						}
					}
			  }
		
		});
		
		webInterfaceObj.init(webInterfaceDataSource);
	});
	
});
