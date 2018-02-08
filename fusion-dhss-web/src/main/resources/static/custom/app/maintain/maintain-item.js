var category_cn = $("#category_cn").val();
var category_code = $("#category_code").val();
console.log(";category_cn="+category_cn+";category_code="+category_code);

if(category_code == "NETWORK"){
	$("#navList a.active").removeClass("active");
	$("#navList li a[href='maintain-network']").addClass("active");
}else if(category_code == "REMOTE"){
	$("#navList a.active").removeClass("active");
	$("#navList li a[href='maintain-remote']").addClass("active");
}else if(category_code == "ENVIRONMENT"){
	$("#navList a.active").removeClass("active");
	$("#navList li a[href='maintain-environment']").addClass("active");
}
var searchparams = {
		categoryId:category_code,
		neTypeId:'',
		keyWord : '',
	    neId:'',
	    unitTypeId:'',
	    cmdtypeId:''
};
var resultparamrs ={
		page: 0,
		size: 10,
		category:category_code,
		neType:'',
		unitType:'',
		sort:"requestTime,desc"
};
var windowObj;
var itemChecked = [];
var confirmObject;
var needrefresh = false;
var reCount = 10;

function resultRefreshGrid(reCount,gridDataSource){
	  setInterval(function(){
			if(reCount>0){
				gridDataSource.read(searchparams);
				reCount--;
			}
		 },1000*10);
}

//弹窗
function openWindow(id,h,w,titleName){
	if (!$("#"+id+"").data("kendoWindow")) {
		$("#"+id+"").kendoWindow({
			  height: h+'px',
			  width:w+'px',
			  title:titleName,
			  modal: true
		});
     }
     windowObj = $("#"+id+"").data("kendoWindow");
     windowObj.center().open();
     windowObj.center().resize();
}
var LogInfoWindow;
//点击下载日志
function downloadLog(id){
	window.location.href = "maintainOperateion/downloadLog?id=" + id;
}
function showloadLog(id){
	$.ajax({
		dataType : "json",
		url : "maintainOperateion/showloadLog",
		data : {id : id},
		success : function(data) {
			LogInfoWindow.obj.setOptions({"title":"日志"});
			LogInfoWindow.initContent({dataLog:data});
		}
	});
}

function saveParamrValue(str,i,itemName){
	var value = "";
	var rests = str.split(",");
	$.each(rests,function(index,item){
	   var valueStr = rests[index];
	   value =  value + $("#input_value_"+valueStr).val()+",";
    });	
	value = value.substring(0, value.length-1);	
	
	var gridSource = $("#confirm-command-page").data("kendoGrid").dataSource;
	
	if(confirm("确定是否需要将相同指令的参数值设置为该值？")){
		$.each(gridSource._data,function(index,item){
			if(item.itemName == itemName){
				var data = gridSource.at(index);
				data.set("defaultParamValues", value);
			}
		});
	}else{
		var data = gridSource.at(i);
		data.set("defaultParamValues", value);
	}
	$("#modal_dialg").data("kendoWindow").close();
	//windowObj.close();
	
}
function confirmCommand(k){
	var flag = true;
	
	if(flag){
		if(k!=1){
			//关闭上个窗口
			windowObj.close();
		}
		
		confirmObject = {
			init: function(){
				this.dataGrid = $("#confirm-command-page").kendoGrid({	
						dataSource: {
							data: itemChecked
						},
						height: 430,						
						reorderable: true,						
						resizable: true,				
						sortable: true,				
						columnMenu: true,				
						pageable: false,
						columns: [
						          {
							field: "unit",
							width: 150,
							template: "<span  title='#:unit#'>#:unit#</span>",
							title: "<span  title='执行单元'>执行单元</span>"
						},{
							field: "itemName",
							width: 130,
							template: "<span  title='#:itemName#'>#:itemName#</span>",
							title: "<span  title='指令名称'>指令</span>"
						},{
							field: "command",
							width: 200,
							template: "<span  title='#:command#'>#:command#</span>",
							title: "<span  title='指令内容'>指令内容</span>"
						}, {
							field: "params",
							template: "#if(params!=null && params !='' ){#<span  title='#:params#'>#:params#</span> #}#",
							title: "<span  title='参数'>参数</span>"
						}, {
							field: "defaultParamValues",
							template: "#if(defaultParamValues!=null ){#<span  title='#:defaultParamValues#'>#:defaultParamValues#</span> #}#",						
							title: "<span  title='参数值'>参数值</span>"
						}, {
							field: "params",
							template: "#if(params!=null && params !='' ){#<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i>输入参数</button> #}#",						
							title: "<span  title='操作'>操作</span>"
						}],
						dataBound: function(){
							//绑定号码段弹窗
							$(".editBtn").on("click", function(){
								
								dataItem = confirmObject.dataGrid.dataItem($(this).closest("tr"));
								var str = dataItem.params.split(",");
								var html = '<div class="modal-body"><div class="form-horizontal">';
								$.each(str,function(index,item){
									valueStr = str[index];
									 html+='<div class="form-group">'
										   +'<label class="col-sm-2 control-label">'+valueStr+'</label>'
										   + '<div class="col-sm-7">'
									       +'<input type="text" class="form-control"  id="input_value_'+valueStr+'" placeholder="'+valueStr+'"></div></div>';									 
								});
								
								html+='<div class="form-group">'
									+'<label class="col-sm-2 control-label"></label>'
						            +'<div class="col-sm-7">'
						            +'<div><button onclick= "saveParamrValue(\''+dataItem.params+'\','+dataItem.index+',\''+dataItem.itemName+'\')" type="button" class="btn btn-success"'
						            +'style="width: 100%;padding: 10px 0;"><i class="glyphicon glyphicon-ok"></i>设置</button></div>'
						            +'</div></div>'
						            +'</div></div>';
								
								$('#modal_dialg').html(html); 
								openWindow('modal_dialg','250','500',dataItem.itemName);
							});
						}
					}).data("kendoGrid");
		    }
		};
		confirmObject.init();	
		
		
		//弹出窗口
		openWindow('submitWindow','480','900','确认下发指令');
	}
 }
$(function() {
	
	
	//编辑、修改弹窗
	LogInfoWindow = {
		
		obj: undefined,
		
		template: undefined,
		
		id: $("#windowTemplate"),
		//取消
		cancelClick: function(){
			$("#cancelBtn").on("click",function(){
				LogInfoWindow.obj.close();
			});
		},
		
		initContent: function(dataItem){
			
			//填充弹窗内容
			LogInfoWindow.obj.content(LogInfoWindow.template(dataItem));
			$("#logText").html(dataItem.dataLog);
			LogInfoWindow.cancelClick();
			
			LogInfoWindow.obj.center().open();
		},
		
		init: function(){
			
			this.template = kendo.template($("#windowTemplate").html());
			
			if (!LogInfoWindow.id.data("kendoWindow")) {
				LogInfoWindow.id.kendoWindow({
					width: "700px",
					actions: ["Close"],
					modal:true,
					title: "号段管理"
				});
			}
			LogInfoWindow.obj = LogInfoWindow.id.data("kendoWindow");
		}
	};
	
	LogInfoWindow.init();
	//每10秒刷新一次
	/*setInterval(function(){
		if(needrefresh && reCount>0){
			resultDataSource.read(resultparamrs);
			reCount--;
		}
	},1000*10);*/
	
	//公共查询变量
	var gridFilterMap = {neType:"",neName:"",unitType:"",unitNameAll:""};

	/*关联数据*/

	var neTypeDataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: "equipment-neType/search/list",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            }
        },
        schema: {
            data: function (data) {
        		var uniqueNeType = [{"neType":"ALL","displayField":"全部"}];
        		//过滤掉重复的
        		var indexPlus = 1;
        		$.each(data,function(index,item){
        			uniqueNeType[indexPlus] = {"neType":item,"displayField":item};
        			indexPlus ++;
        		});
    			return uniqueNeType;
        	}
        },
        change:function(event){

        }
	});
	
	
	var typeRelDataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: "rest/equipment-type-rel-ne-unit",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            }
        },
        schema: {
            data: function (data) {
            	if(data._embedded){
            		var result = [];
            		var uniqueUnitType = {};
            		//过滤掉重复的
            		$.each(data._embedded["equipment-type-rel-ne-unit"],function(index,item){
            			uniqueUnitType[item.unitType] = item.unitType;
            		});
            		$.each(uniqueUnitType,function(key,item){
                		result.push({neType:"ALL",unitType:item})
            		});
        			return result.concat(data._embedded["equipment-type-rel-ne-unit"]);
            	}
            	return [];
            }
        },
        change:function(event){
        	gridFilterMap.unitType = "";
        }
	});
	
	var neDataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: "rest/equipment-ne",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            }
        },
        schema: {
            data: function (data) {
        		var result = [{neName:"全部",neType:"ALL",fakeItem:true},
        		              {neName:"全部HSSFE",neType:"HSSFE",fakeItem:true},
        		              {neName:"全部NTHLRFE",neType:"NTHLRFE",fakeItem:true},
        		              {neName:"全部ONE_NDS",neType:"ONE_NDS",fakeItem:true},
        		              {neName:"全部SGW",neType:"SGW",fakeItem:true}];
            	if(data._embedded){
        			return result.concat(data._embedded["equipment-ne"]);
            	}
            	return [];
            }
        },
        change:function(event){
        	gridFilterMap.neName = "";
        }
	});
	var gridDataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: "rest/equipment-unit/search/all",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            },
        },
        schema: {
            data: function (data) {
            	/*if(data._embedded){
        			 return data._embedded["equipment-unit"];
            	}*/
            	return data;
            },
            total: function(data) {
            	/*if(data._embedded){
        			return data._embedded["equipment-unit"].length;
            	}*/
            	return data.length;
            }
        },
        serverPaging : false,
		serverFiltering : false,
		serverSorting : false,
	});
	var commandGridDataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: "rest/check-item/search/findListByCategory?q="+category_code,
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            },
        },
        schema: {
            data: function (data) {
            	if(data._embedded){
        			 return data._embedded["check-item"];
            	}
            	return [];
            },
            total: function(data) {
            	if(data._embedded){
        			return data._embedded["check-item"].length;
            	}
            	return 0;
            }
        },
        serverPaging : false,
		serverFiltering : false,
		serverSorting : false,
	});
	

	/*下拉框部分*/

/*	var inputNeTypeTrigger = $('#inputNeTypeTrigger').kendoDropDownList({
        dataTextField: "displayField",
        dataValueField: "neType",
        dataSource: neTypeDataSource,
        change:function(event){
        	var dataItem = (this.dataItem(event.item));
        	if(dataItem.neType!="ALL"){
            	gridFilterMap.neType = dataItem.neType;
        	}else{
            	gridFilterMap.neType = "";
        	}
        	var filters_unit = [];

        	var filters_checkItem= [];
        	
        	if(gridFilterMap.neType){
        		filters_unit.push({field: "neType", operator: "eq", value: gridFilterMap["neType"]});
        		filters_checkItem.push({field: "neType", operator: "contains", value: gridFilterMap["neType"]});
        	}
        	gridDataSource.filter(filters_unit);
        	gridDataSource.fetch();

        	commandGridDataSource.filter(filters_checkItem);
        	commandGridDataSource.fetch();
        }
	}).data("kendoDropDownList");
	var inputNeTrigger = $('#inputNeTrigger').kendoDropDownList({
        dataTextField: "neName",
        dataValueField: "neType",
        cascadeFrom: "inputNeTypeTrigger",
        cascadeFromField: "neType",
        filter: "contains",
        dataSource: neDataSource,
        change:function(event){
        	var dataItem = (this.dataItem(event.item));
        	if(dataItem.fakeItem){
            	gridFilterMap.neName = "";
        	}else{
            	gridFilterMap.neName = dataItem.neName;
        	}
        	var filters = [];
        	var filters_checkItem= [];

        	if(gridFilterMap.neName)
            	filters.push({field: "neName", operator: "eq", value: gridFilterMap["neName"]});
        	if(gridFilterMap.unitType){
        		filters_checkItem.push({field: "unitType", operator: "contains", value: gridFilterMap["unitType"]});
            	filters.push({field: "unitType", operator: "eq", value: gridFilterMap["unitType"]});
        	}
        	if(gridFilterMap.neType){
        		filters_checkItem.push({field: "neType", operator: "contains", value: gridFilterMap["neType"]});
            	filters.push({field: "neType", operator: "eq", value: gridFilterMap["neType"]});
        	}

        	gridDataSource.filter(filters);
        	gridDataSource.fetch();
        	

        	commandGridDataSource.filter(filters_checkItem);
        	commandGridDataSource.fetch();
        }
	}).data("kendoDropDownList");
	
	var inputUnitTypeTrigger =  $('#inputUnitTypeTrigger').kendoDropDownList({
		optionLabel: "全部",
        dataTextField: "unitType",
        dataValueField: "unitType",
        cascadeFrom: "inputNeTrigger",
        cascadeFromField: "neType",
        dataSource:typeRelDataSource,
        change:function(event){
        	var dataItem = (this.dataItem(event.item));
        	if(dataItem.unitType){
            	gridFilterMap.unitType = dataItem.unitType;
        	}else{
            	gridFilterMap.unitType = "";
        	}
        	var filters_checkItem = [];
        	var filters = [];

        	if(gridFilterMap.neName)
            	filters.push({field: "neName", operator: "eq", value: gridFilterMap["neName"]});
        	if(gridFilterMap.unitType){
        		filters_checkItem.push({field: "unitType", operator: "contains", value: gridFilterMap["unitType"]});
            	filters.push({field: "unitType", operator: "eq", value: gridFilterMap["unitType"]});
        	}
        	if(gridFilterMap.neType){
        		filters_checkItem.push({field: "neType", operator: "contains", value: gridFilterMap["neType"]});
            	filters.push({field: "neType", operator: "eq", value: gridFilterMap["neType"]});
        	}
        	gridDataSource.filter(filters);
        	gridDataSource.fetch();
        	

        	commandGridDataSource.filter(filters_checkItem);
        	commandGridDataSource.fetch();
        }
	}).data("kendoDropDownList");*/

	/*$('#inputKeyWord').on("keyup",function(event){
		var searchKey = $('#inputKeyWord').val();
		gridFilterMap.unitNameAll = searchKey;
    	var filters = [];
    	if(gridFilterMap.neName)
        	filters.push({field: "neName", operator: "eq", value: gridFilterMap["neName"]});
    	if(gridFilterMap.unitType)
        	filters.push({field: "unitType", operator: "eq", value: gridFilterMap["unitType"]});
    	if(gridFilterMap.neType)
        	filters.push({field: "neType", operator: "eq", value: gridFilterMap["neType"]});
    	if(gridFilterMap.unitNameAll)
        	filters.push({field: "unitNameAll", operator: "contains", value: gridFilterMap["unitNameAll"]});
    	gridDataSource.filter(filters);
    	gridDataSource.fetch();
	});*/

	var unitGrid = $("#unitGrid").kendoGrid({
						dataSource : gridDataSource,
						height : $(window).height() - $("#unitGrid").offset().top - 120,
						reorderable : true,
						resizable : true,
						sortable : true,
						pageable : false,
						columns: [{
							width: 30,
							template: "<input type='checkbox' id='#:id#' />",
							attributes:{"class": "text-center"},
							title: "<input type='checkbox' class='grid_checkbox'/>"
						}, {
							field: "neName",
							title: "<span  title='网元'>网元</span>"
						}, {
							field: "unitName",
							title: "<span  title='单元'>单元</span>"
						}, {
							field: "serverIp",
							title: "<span  title='地址'>地址</span>"
						}, {
							field: "unitType",
							title: "<span  title='单元类型'>单元类型</span>"
						}],
						dataBound: function(){
							//全选按钮
							$(".grid_checkbox").on("click",function(){
								$("#unitGrid .k-grid-content input[type='checkbox']").prop("checked",$(this).prop("checked"));
							});
						}
					}).data("kendoGrid");


	var commandGrid = $("#commandGrid").kendoGrid({
						dataSource : commandGridDataSource,
						height : $(window).height() - $("#commandGrid").offset().top - 95,
						reorderable : true,
						resizable : true,
						sortable : true,
						pageable : false,
						columns: [{
							width: 30,
							template: "<input type='radio' name='radio1' class = 'grid_radio'/>",
							attributes:{"class": "text-center"}
						},{
							field: "name",
							title: "<span  title='指令名称'>指令名称</span>"
						}],
						dataBound: function(){
							//全选按钮
							$(".grid_radio").on("click",function(){
								var dataItem = $("#commandGrid .k-grid-content input[type='radio']:checked");
								var data = commandGrid.dataItem($(dataItem).closest("tr"));							
								var filters = [];
						    	if(data.unitType){
						    		var str = data.unitType.split(" or ");		
						    		$.each(str,function(index,item){
								    		   filters.push({field: "unitType", operator: "eq", value: item});
								    });							    		
						    								    	
						    	}else{
						    			filters.push({field: "unitType", operator: "eq", value: new Date().getTime()});
						    		
						    	}					    	
						    	gridDataSource.filter({logic:"or",filters:filters});
						    	gridDataSource.fetch();
							});
						}
					}).data("kendoGrid");
      var resultDataSource = new kendo.data.DataSource({
          transport: {
              read: {
                  type: "GET",
                  url: "rest/maintain-result/pageinfo",
                  dataType: "json",
                  contentType: "application/json;charset=UTF-8"
              },
              parameterMap: function (options, operation) {
                  if (operation == "read") {
                	  resultparamrs.page = options.page -1 ;
                      return resultparamrs;                   
                  }
              }
          },
          batch: true,
          pageSize: 10,
          requestEnd: function(e) {
              var response = e.response;
              if(response){
                  var type = e.type;
                  if(type !='read'){
                       this.read();                  
                  }
              }else{
                  alert("服务器异常，请重试！");
              }
              
          },
          schema: {
              data: function (data) {
              	var rdata = [];
              	if(data._embedded){
              		$.each(data._embedded["maintain-operation"],function(index,item){
              			if(item.isDone==0){
              				needrefresh = true;
              				return false;
              			}
              			needrefresh = false;
              			
                  		/*item.ne = item._embedded["ne"];
      					item.unit = item._embedded["unit"];
      					item.checkName = item._embedded["operation"].commandCheckItem.name;*/
                  		//rdata.push(item);
                  	});
              		 return data._embedded["maintain-operation"];
              	}else{
              		 return rdata;
              	}
              	 
                 // return rdata;//返回页面数据
              },
              total: function(d) {
                  return d.page.totalElements; 
              },
              pageSize: function(d) {
                  return d.page.size; 
              }
          },
         serverPaging : true,
  		serverFiltering : true,
  		serverSorting : true
      });
	//执行结果
	var resultGridObj = $("#resultGrid").kendoGrid({		
		dataSource: resultDataSource,		
		height:$(window).height() - $("#resultGrid").offset().top - 75,		
		reorderable: true,
		resizable: true,
		sortable: true,
		pageable: true,
		columns: [{
			field: "requestTime",
			template: "#if(requestTime != null ){#<span  title='#:requestTime#'>#:requestTime#</span> #}#",
			title: "<span  title='时间'>时间</span>"
		}, {
			field: "checkName",
			template: "<span  title='#:checkName#'>#:checkName#</span>",
			title: "<span  title='检查项'>检查项</span>"
		}, {
			field: "isDone",
			width: "160px",
			template: "#if(isDone == 0){#"+
					  "<div class='progress'><div class='progress-bar progress-bar-warning progress-bar-striped active' style='width: 70%;'>执行中……</div></div>"+
					  "#} else {#"+
					  "完成，<a class='btn btn-link'  onclick='showloadLog(\"#:operationId#\")'>显示</a>"+
					  "<a class='btn btn-link'  onclick='downloadLog(\"#:operationId#\")'><i class='glyphicon glyphicon-download-alt'></i>下载</a>"+
					  "#}#",
			title: "<span  title='结果'>结果</span>"
		}],
		dataBound: function(){
			$(".loginBtns").on("click", function(){
				alert("连接超时 5 秒");
			});
		}
	}).data("kendoGrid");
	
	//提交执行
	$("#submitBtn").on("click",function(){
		unitChecked = [];
		itemChecked = [];
		paramsChecked =[];
		
		var unitNumber = $("#unitGrid .k-grid-content input[type='checkbox']:checked").length;
		var commandNumber = $("#commandGrid .k-grid-content  input[type='radio']:checked").length;
		
		if(unitNumber == 0){
			infoTip({"content":"请选择单元列表！"});
		} else if(commandNumber == 0){
			infoTip({"content":"请选择指令！"});
		} else if(unitNumber * commandNumber > 100){
			infoTip({"content":"同时选择指令和网元个数乘积不能超过100！"});
		} else {
			
			var checkedUnitInput = $("#unitGrid .k-grid-content tbody tr input[type='checkbox']:checked");
			var checkedItemInput = $("#commandGrid .k-grid-content tbody tr input[type='radio']:checked");
		    var unitStr = "";			
		    var needParams = false;
		    var indexs = 0;
			$.each(checkedUnitInput,function(index,value){
				var data = unitGrid.dataItem($(value).closest("tr"));	
				
				$.each(checkedItemInput,function(index1,value1){
					
					var data1 = commandGrid.dataItem($(value1).closest("tr"));
					var u = {};
					u.neId = data.neId;
					u.unitId = data.unitId;
					u.unit = data.unitName;
					//u.unitType = data.unitType;
					//u.neType = data.neType;
					u.index = indexs++;
					u.account = data1.account;
					u.itemName =  data1.name;
					u.itemId = data1.itemId;
					//u.category = data1.category;
					u.command = data1.command;
					
					u.params = data1.params;
					u.defaultParamValues = data1.defaultParamValues;
					if(data1.params!=null || data1.params != ""){
						needParams = true;
					}
					itemChecked.push(u);
				});
			});			
			confirmCommand(1);
		}
	});
	
	//弹窗-确定按钮
	$("#dialogOKBtn").on("click",function(){
		var send = {};
		var gridSource = $("#confirm-command-page").data("kendoGrid").dataSource;
		var flag = false;
		$.each(gridSource._data,function(index,item){
			if(item.params!=null && item.params!=''){
				if(item.defaultParamValues == null || item.defaultParamValues==''){
					flag = true;
				}		
			}
		});
		if(flag){
			infoTip({"content":"请输入参数！"});
		}else{
		  send = {data:gridSource._data,category_cn:category_cn,category_code:category_code};
		  $.ajax({
			type : "POST",
			dataType : "json",
			contentType: "application/json;charset=UTF-8",
			url : "maintain-operation/sendCommand",
			data : JSON.stringify(send),
			success : function(data) {
				infoTip({"content":"命令下发成功！"});
				$("#unitGrid .k-grid-content tbody tr input[type='checkbox']:checked").prop("checked", false);
				$("#commandGrid input[type='radio']").prop('checked',false);
				//resultDataSource.read(resultparamrs);
				
				resultRefreshGrid(10,resultDataSource);
				$("#submitWindow").data("kendoWindow").close();
			}
		  });
		}
	});
	
	$("#dialogCancelBtn").on("click",function(){
		$("#submitWindow").data("kendoWindow").close();
	});
});

