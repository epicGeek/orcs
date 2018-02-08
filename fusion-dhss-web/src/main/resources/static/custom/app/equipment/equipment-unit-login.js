var logDataSource;
var searchParams={page : 0 , size : 10 , userName:'',unitName:'',sort : "startTime,desc"};
var LoginUnitName = "";
//初始化列表
function logGridData(unitName){
	logDataSource = new kendo.data.DataSource({
        transport: {
            read: {
	            type : "GET",
				url : "equipment-console-log/search/searchByFilter",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
            },
            parameterMap: function (options, operation) {
            	if (operation == "read") {
            		LoginUnitName = unitName;
            		searchParams.page = options.page-1;
            		searchParams.size = options.pageSize;
					searchParams.unitName = unitName;
					searchParams.userName = $("#cond-command").val();
					console.log(searchParams);
					return searchParams;
				}
            }
        },
        pageSize: 10,
        schema : {
			data : function(d) {
				if(d._embedded){
					console.log(d._embedded["console-log"]);
					return d._embedded["console-log"];  //响应到页面的数据
				}else
					return [];
			},
			total : function(d) {
				return d.page.totalElements; //总条数
			}
		},
        serverPaging : true,
		serverFiltering : true,
		serverSorting : true
    });
	
	$("#logGrid").kendoGrid({
		  dataSource:logDataSource,
		  height: 400,
		  reorderable: true,
			resizable: true,
			sortable: true,

			columnMenu: true,

			pageable: false,
	      columns:[
	                 { width:160,field: "startTime", title:"日志时间"},
	                 { field: "loginUserName", title:"操作人"},
	                 { field: "loginUnitName", title:"单元名称"},
	                	/* template:'#if(unitName !=""){isUnitHide = true;#'
	                		     +'#:unitName# #}else{isUnitHide = false;}#'
	                 },*/
	                 {   title:"操作",
	                	 template:'#if(logPath!="" && null!=logPath){#'
	                		    +"<button type='button' class='btn btn-info btn-xs'"
	                	        +"onclick='downloadLogFile(\"#:logPath#\")'>下载日志</button>&nbsp;&nbsp;&nbsp;#}#"
	                 }
	                
	               ]
	    });
}


function openWindow(id,h,w,titleName){
	
	$("#"+id).kendoWindow({
		  height: h+'px',
		  width:w+'px',
		  title:titleName
	});
	dialogObj = $("#"+id).data("kendoWindow");
	dialogObj.open();dialogObj.center();
}
function downloadLogFile(filePath){
	window.open("console/download-console-log?filePath="+filePath,"_blank");
}
function webFunction(text){
	if(text == ""){
		infoTip({
			content : "路径为空!"
		});
	}else{
		window.open(text,'newwindow'+new Date());
	}
}
var ne = [];
var textValue = [];

var inputNe;
$(function() {
	
	
	//下拉开始
	
	$.ajax({
		url: "find/DropDownList",
		dataType:"json",
		success:function(data){
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
	
	
	
	
	
	$("#cond-command").on("keyup",function(){
		logGridData(LoginUnitName);
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
        	filters.push({field: "location", operator: "eq", value: locationtext});
    	if(unitTypetext != "")
        	filters.push({field: "unitType", operator: "eq", value: unitTypetext});


    	var searchKey = $('#inputKeyWord').val();
    	if(searchKey != "")
        	filters.push({field: "unitNameAll", operator: "contains", value: searchKey});
    	
    	
    	var numberSections = $("#numKeyWord").val();
    	if(numberSections != "")
    		filters.push({field: "ne.numberSections", operator: "contains", value: numberSections});
    	
    	dataGridObj.dataGrid.dataSource.filter(filters);
	}
	
	
	
	
	
	
	
	
	
	
	var gridDataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: "rest/equipment-unit/search/all",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            },
        },
        pageSize:20,
        schema: {
            data: function (data) {
            	return data;
            } 
        },
        serverPaging : false,
		serverFiltering : false,
		serverSorting : false,
	});
	

	
	
	var terminalButton = "<div id='#:unitName#'><button   class='terminalBtn btn btn-xs btn-primary'> 终端 </button>&nbsp;&nbsp;" 
	var logButton = "<button   class='downloadLogBtn btn btn-xs btn-success'> 日志 </button>&nbsp;&nbsp;</div>"/*+
	"<button class='crtBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon glyphicon'></i>SecureCRT</button>&nbsp;&nbsp;</div>"*/;
	
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
					field: "neType",
					template: "<span  title='#:neType#'>#:neType#</span>",
					title: "<span  title='网元类型'>网元类型</span>",
					width:150
				},{
					field: "neName",
					template: "<span  title='#:neName#'>#:neName#</span>",
					title: "<span  title='网元名称'>网元名称</span>",
					width:150
				}, {
					field: "unitType",
					width:150,
					template: "<span  title='#:unitType#'>#:unitType#</span>",
					title: "<span  title='单元类型'>单元类型</span>"
				}, {
					field: "unitName",
					template: "<span  title='#:unitName#'>#:unitName#</span>",
					title: "<span  title='单元名称'>单元名称</span>",
					width:150
				}, {
					field: "serverIp",
					template: "<span  title='#:serverIp#'>#:serverIp#</span>",
					title: "<span  title='MML接口地址'>MML接口地址</span>",
					width:150
				}, {
					field: "serverProtocol",
					template: "<span  title='#=serverProtocol#'>#=serverProtocol#</span>",
					title: "<span  title='MML登录协议'>MML登录协议</span>",
					width:150
				}, {
					field: "isForbidden",
					width:150,
					template: "#if(isForbidden ==0){#<span  title='启用'>启用</span>#}else{#<span title='禁用'>禁用</span>#}#",
					title: "<span  title='单元状态'>单元状态</span>"
				}, {
					field:"serverName",
					template: "#:serverIp##:unitName#",
					hidden:true
					},
					{
					field: "flag",
					width: 350,
					template: "#if(isForbidden ==0){#" + terminalButton + logButton+   "#}else{#<div id='#:unitName#'></div>#}#",
					title: "<span  title='操作'>操作</span>"
				}
				],
				dataBound: function() {
					
					
					var dataView = dataGridObj.dataGrid.dataSource.view();
					//全部加入
					$.each(dataView,function(index,item){
						if(textValue[item.unitName] == undefined){
							$.ajax({
								url: "rest/equipment-unit/"+item.id+"/webInterface",
								dataType:"json",
								async: false,
								success:function(data){
									var text = "";
									if(data._embedded){
										var arrs = [];
										if(data._embedded){
											arrs = data._embedded["equipment-webinterface"].sort(compare("interfaceType"));
										}
										$.each(arrs,function(index,item){
											var r = item.interfaceType.split("_");
											var t = r[r.length-1];
											text += "<button name='web' onclick = 'webFunction(\""+item.url+"\")' added="
												+item._links.self.href+" class='webBtn btn btn-xs btn-warning'> "+
												(t == "OPEN" ? "SOAPGW" : t) +" </button><span name='web'>&nbsp;&nbsp;</span>";
											
										})
										$("#"+item.unitName).html().replace(text,"");
										$("#"+item.unitName).html($("#"+item.unitName).html()+text);
									}
									textValue[item.unitName] = text;
								} 
							});
						}else{
							$("#"+item.unitName).html().replace(textValue[item.unitName],"");
							$("#"+item.unitName).html($("#"+item.unitName).html()+textValue[item.unitName]);
						}
						
					})
					dataGridObj.addClick();
					dataGridObj.editClick();
					dataGridObj.terminalClick();
					dataGridObj.downloadLogClick();
					dataGridObj.deleteClick();
					dataGridObj.crtClick();
				}
			}).data("kendoGrid");
		},
		downloadLogClick:function(){
			$(".downloadLogBtn").on("click", function() {
				var currentRow = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
				openWindow('logModal','440','740','登录日志');
				logGridData(currentRow.unitName);
			});
		},
		crtClick:function(){
			$(".crtBtn").on("click", function() {
				var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
				
				var uri = dataItem.serverUrl.split("").reverse().join("");
//				alert($.base64.atob(uri, true));
				window.location.href = $.base64.atob(uri, true);
			});
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

			});
		},

		terminalClick: function() {
			
			$(".terminalBtn").on("click", function() {
				var currentRow = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
				console.log("currentRow:"+currentRow);
				$.ajax({
					type : "POST",
					async: false,
					contentType: "application/json;charset=UTF-8",
					url : "rest/equipment-unit/login?unitName="+currentRow.unitName,
					success : function(data) {
						console.log("unitName:"+currentRow.unitName);
					}
				});
				
				terminalWindow.init();
				terminalWindow.obj.setOptions({
					"title": "单元 "+currentRow.unitName+" ["+currentRow.serverIp+"]"
				});
				terminalWindow.initContent(currentRow);
			});
		},
		//编辑
		editClick: function() {},

		//删除
		deleteClick: function() {}
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
			//var handUrl = (dataItem._links.self.href);
			//var lastiIndex = dataItem._links.self.href.lastIndexOf("/");
			//var uid = handUrl.substring(lastiIndex+1);
			$.ajax({
				"dataType" : 'text',
				"type" : "GET",
				"url" : "console/login",
				"data" : {
					uid : dataItem.id,
					termType : 'dumb',
					termXlength : '90',
					termYlength : '30',
					timestamp : (new Date()).getTime()
				},
				timeout : 25000,
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
								termFocus = true;
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
								connection.close();
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
	
	
	

	dataGridObj.init();
	
	terminalWindow.init();
});



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
