var category_cn = $("#category_cn").val();
var category_code = $("#category_code").val();
console.log(";category_cn="+category_cn+";category_code="+category_code);


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
		neTypeId:'',
		unitTypeId:'',
		sort:"requestTime,desc"
};

var unitChecked = [];
var infoWindow;
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
			infoWindow.obj.setOptions({"title":"日志"});
            infoWindow.initContent({dataLog:data});
		}
	});
}


$(function() {
	
	
	//编辑、修改弹窗
	infoWindow = {
		
		obj: undefined,
		
		template: undefined,
		
		id: $("#windowTemplate"),
		//取消
		cancelClick: function(){
			$("#cancelBtn").on("click",function(){
				infoWindow.obj.close();
			});
		},
		
		initContent: function(dataItem){
			
			//填充弹窗内容
			infoWindow.obj.content(infoWindow.template(dataItem));
			$("#logText").html(dataItem.dataLog);
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
	
	infoWindow.init();
	
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
            			result.push({neType:"ALL",unitType:item.unitType})
            		});
//            		$.each(uniqueUnitType,function(key,item){
//                		result.push({neType:"ALL",unitType:item})
//            		});
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
                //url: "rest/equipment-unit?sort=ne&unitType&sort=unitName",
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
            }/*,
            total: function(data) {
            	if(data._embedded){
        			return data._embedded["equipment-unit"].length;
            	}
            	return 0;
            }*/
        },
        serverPaging : false,
		serverFiltering : false,
		serverSorting : false,
	});
	
	

	/*下拉框部分*/

	var inputNeTypeTrigger = $('#inputNeTypeTrigger').kendoDropDownList({
        dataTextField: "displayField",
        dataValueField: "neType",
        dataSource: neTypeDataSource,
        change:function(event){
        	var dataItem = (this.dataItem(event.item));
        	gridFilterMap.neType = "";
        	if(dataItem.neType!="ALL"){
            	gridFilterMap.neType = dataItem.neType;
        	}/*else{
        		gridFilterMap.neType = "";
        	}*/
        	var filters_unit = [];

        	
        	if(gridFilterMap.neType){
        		filters_unit.push({field: "neType", operator: "eq", value: gridFilterMap["neType"]});
        	}
        	gridDataSource.filter(filters_unit);
        	gridDataSource.fetch();

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

        	if(gridFilterMap.neName)
            	filters.push({field: "neName", operator: "eq", value: gridFilterMap["neName"]});
        	if(gridFilterMap.unitType){
        		filters.push({field: "unitType", operator: "eq", value: gridFilterMap["unitType"]});
        	}
        	if(gridFilterMap.neType){
        		filters.push({field: "neType", operator: "eq", value: gridFilterMap["neType"]});
        	}

        	gridDataSource.filter(filters);
        	gridDataSource.fetch();
        	

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
        	gridFilterMap.unitType = "";
        	if(dataItem.unitType){
            	gridFilterMap.unitType = dataItem.unitType;
        	}/*else{
            	gridFilterMap.unitType = "";
        	}*/
        	var filters = [];

        	if(gridFilterMap.neName)
            	filters.push({field: "neName", operator: "eq", value: gridFilterMap["neName"]});
        	if(gridFilterMap.unitType){
        		filters.push({field: "unitType", operator: "eq", value: gridFilterMap["unitType"]});
        	}
        	if(gridFilterMap.neType){
        		filters.push({field: "neType", operator: "eq", value: gridFilterMap["neType"]});
        	}
        	gridDataSource.filter(filters);
        	gridDataSource.fetch();
        	
        }
	}).data("kendoDropDownList");

	$('#inputKeyWord').on("keyup",function(event){
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
	});

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
              		rdata = data._embedded["maintain-operation"];
              		/*$.each(data._embedded["maintain-operation"],function(index,item){
                  		rdata.push(item);
                  	});*/
              	}
                  return rdata;//返回页面数据
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
	
	
	//弹窗
	if (!$("#submitWindow").data("kendoWindow")) {
				$("#submitWindow").kendoWindow({
					width: "600px",
					actions: ["Close"],
					modal:true,
					title: "修改网元密码"
				});
	}
	var windowObj = $("#submitWindow").data("kendoWindow");
	
	$("#submitBtn").on("click",function(){
		unitChecked = [];	
		
		if($("#unitGrid .k-grid-content input[type='checkbox']:checked").length == 0){
			infoTip({"content":"请选择单元列表！"});
		}  else {
			//windowObj.center().open();
			
			var checkedUnitInput = $("#unitGrid .k-grid-content tbody tr input[type='checkbox']:checked");
			var queryGrid= $("#unitGrid").data("kendoGrid");
		    var unitStr = "";
			
			$.each(checkedUnitInput,function(index,value){
				var data = queryGrid.dataItem($(value).closest("tr"));	
				var u = {};
				u.neId =data.neId;
				u.neName =data.neName;
				u.id = data.unitId;
				u.unit = data.unitName;
				unitChecked.push(u);			
				
			});
			
			$('#inputAccount').val("");
			$('#newPassword').val("");
			$('#newPasswordAgain').val("");
			
			windowObj.center().open();
		    
			
		}
	});
	// 绑定修改用户密码弹窗的提交按钮事件
	$('#dialogOKBtn').on('click', function() {
        
		var account = $('#inputAccount').val();
		var newPassword = $('#newPassword').val();
		var newPasswordAgain = $('#newPasswordAgain').val();
		if(!account){
			infoTip({"content":"没有输入用户名"});
			return;
		}
		if(!newPassword||!newPasswordAgain){
			infoTip({"content":"没有输入密码"});
			return;
		}

		if(newPassword!==newPasswordAgain){
			infoTip({"content":"两次密码不相同"});
			return;
		}
		if(newPassword.length < 8){
			infoTip({"content":"新密码长度小于 "+ 8});
			return;
		}
		var postData = {
				"unitList" : unitChecked,
				"category_cn":category_cn,
				"category" : category_code,
				"account" : account,
				"newPassword" : newPassword
			};
		
		//发送提交命令
		$.ajax({
			type : "POST",
			dataType : "json",
			contentType: "application/json;charset=UTF-8",
			url : "maintain-operation/sendPassword",
			data : JSON.stringify(postData),
			success : function(data) {
				infoTip({"content":"命令下发成功"});
				$("#queryGrid .k-grid-content tbody tr input[type='checkbox']:checked").prop("checked", false);
				//关闭窗口
				windowObj.close();
				//重新刷新grid
				resultDataSource.read(resultparamrs);
			}
		});
		
	});
	
	
});

