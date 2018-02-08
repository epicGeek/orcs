kendo.culture("zh-CN");
var dateTime = new Date().toString();
var unitQueryResult = [];
var unitQueryResultTemp = [];
var locationName = [];
var neType = [];
var neName = [];
var unitName = [];
var dhssName = [];
var unitName = [];
var dhssNameSelected = "";
var locationNameSelected = "";
var neTypeSelected = "";
var kpiName;
var startTime = new Date();
var endTime = new Date();
var kpiCategory = [];
var kpiItem = [];
var kpiReportTemplate;
var reportTemplate;
function unique(arr) {
    var result = [], hash = {};
    for (var i = 0, elem; (elem = arr[i]) != null; i++) {
        if (!hash[elem]) {
            result.push(elem);
            hash[elem] = true;
        }
    }
    return result;
}
function findKpiItemListByCategory(kpiCategory){
	var kpiItemSelected = [];
	$.each(kpiItem,function(index,item){
		if(item.kpiCategory==kpiCategory)
			kpiItemSelected[kpiItemSelected.length] = item;
	});
	return kpiItemSelected;
}
function filterArrayWithKeyWords(list,searchField,keyWord){
	var result = [];
	if(keyWord){
		$.each(list,function(index,item){
			if(item[searchField]==keyWord)
				result[result.length] = item;
		});
	}else{
		return list;
	}
	return result;
}

function getSingleFiledList(list,resultField){
	var result = [];

	$.each(list,function(index,item){
		result[result.length] = item[resultField];
	});
	return result;
}
function getKpiNameLong(kpiItem){
	var kpiName = kpiItem.kpiName+"";
	return kpiName;
}
function initKpiSelectBoxData(){
	$.ajax({                
		url:"rest/kpi-item",          //rest 接口获取所有配置数据
		type:"GET",
		dataType : "json",
		success:function(data){
			if(data.hasOwnProperty("_embedded")){
				kpiItem = data["_embedded"]["kpi-item"];         //判断是否取出来 kpi-item实体
			}else{
				return;
			}
			var tempKpiCategory = [];
			$.each(kpiItem,function(index,item){
				tempKpiCategory[tempKpiCategory.length] = item.kpiCategory;//得到该字段下的所有数据
				item.kpiName = getKpiNameLong(item);              //拼名字
			});
			kpiCategory = unique(tempKpiCategory);     //去重
			
			var dataSourceKpi =  new kendo.data.DataSource({
				data: kpiCategory
			});
			$("#inputKpiCategory").kendoDropDownList({
				optionLabel:"请选择指标类型",
		        dataSource: dataSourceKpi,
		        change:function(event){
		        	var inputKpiCategoryValue = $("#inputKpiCategory").val();
		        	if(inputKpiCategoryValue){
		        		inputKpiItem.setDataSource(findKpiItemListByCategory(inputKpiCategoryValue));
		        	}else{
		        		inputKpiItem.setDataSource(kpiItem);
		        	}
		        }
		    }).data("kendoDropDownList");
			var dataSourceKpiItem =  new kendo.data.DataSource({
				data: kpiItem
			});

			var inputKpiItem = $("#inputKpiItem").kendoDropDownList({
				optionLabel:"请选择指标",
		        dataSource: dataSourceKpiItem,
		        dataTextField: "kpiName",
		        dataValueField: "kpiCode"
		    }).data("kendoDropDownList");
			
			
		}
	});
}
function initNeSelectBoxData(){
	$.ajax({
		url:"rest/equipment-unit",
		type:"GET",
		dataType : "json",
		success:function(data){
			if(data.hasOwnProperty("_embedded")){
				unitQueryResultTemp = data["_embedded"]["equipment-unit"];
				var i = 0;
				$.each(unitQueryResultTemp,function(index,item){
					if(unitQueryResultTemp[index].neName!="Soap-GW"){
						unitQueryResult[i]=unitQueryResultTemp[index];
						i++;
					}
				})
			}else{
				return;
			}
			/**********distinct data**********/
			var unitNameTemp = [];
			var neNameTemp = [];
			var neTypeTemp = [];
			var locationNameTemp = [];
			var dhssNameTemp = [];
			

			$.each(unitQueryResult,function(index,item){
				unitNameTemp[unitNameTemp.length] = item.unitName;
				neNameTemp[neNameTemp.length] = item.neName;
				locationNameTemp[locationNameTemp.length] = item.location;
				dhssNameTemp[dhssNameTemp.length] = item.dhssName;
				neTypeTemp[neTypeTemp.length] = item.neType;
			});
			unitName = unitNameTemp;
			neName = unique(neNameTemp);
			dhssName = unique(dhssNameTemp);
			neType = unique(neTypeTemp);
			locationName = unique(locationNameTemp);
			

			var dataSourceUnitName =  new kendo.data.DataSource({
				data: unitName
			});	
			/*******format datasource...******/
			var dataSourceNeName =  new kendo.data.DataSource({
				data: neName
			});	
			
			var dataSourceDhssName =  new kendo.data.DataSource({
				data: dhssName
			});	
			var dataSourceLocationName =  new kendo.data.DataSource({
				data: locationName
			});	
			var dataSourceNeType =  new kendo.data.DataSource({
				data: neType
			});	

			var inputUnitName = $("#inputUnitName").kendoDropDownList({
				optionLabel:"请选择板卡名称",
		        dataSource: dataSourceUnitName,
		        suggest: true,
		        filter: "contains"
		    }).data("kendoDropDownList");	
			inputUnitName.list.width(220);
			
			var inputNeName	= $("#inputNeName").kendoDropDownList({
				optionLabel:"请选择网元名称",
		        dataSource: dataSourceNeName,
		        suggest: true,
		        filter: "contains",
		        change:function(){
		        	var neNameSelected =  $("#inputNeName").val();
		        	var unitObjectList =[];
		        	unitObjectList = filterArrayWithKeyWords(unitQueryResult,"neName",neNameSelected);
		        	inputUnitName.setDataSource(unique(getSingleFiledList(unitObjectList, "unitName")));
		        }
		    }).data("kendoDropDownList");
			
			var inputLocationName = $("#inputLocationName").kendoDropDownList({
				optionLabel:"请选择局址",
		        dataSource: dataSourceLocationName,
		        change:function(){
		        	locationNameSelected =  $("#inputLocationName").val();
		        	var neObjectList = [];
		        	neObjectList = filterArrayWithKeyWords(unitQueryResult,"neType",neTypeSelected);
	        		neObjectList = filterArrayWithKeyWords(neObjectList,"dhssName",dhssNameSelected);
	        		neObjectList = filterArrayWithKeyWords(neObjectList,"location",locationNameSelected);
		        	inputNeName.setDataSource(unique(getSingleFiledList(neObjectList, "neName")));
		        	inputUnitName.setDataSource(unique(getSingleFiledList(neObjectList, "unitName")));
		        }
		    }).data("kendoDropDownList");
			
			var inputNeType = $("#inputNeType").kendoDropDownList({
				optionLabel:"请选择网元类型",
		        dataSource: dataSourceNeType,
		        close:function(){
		        	neTypeSelected = $("#inputNeType").val();
		        	var neObjectList = [];
	        		neObjectList = filterArrayWithKeyWords(unitQueryResult,"neType",neTypeSelected);
	        		neObjectList = filterArrayWithKeyWords(neObjectList,"dhssName",dhssNameSelected);
	        		neObjectList = filterArrayWithKeyWords(neObjectList,"location",locationNameSelected);
		        	inputNeName.setDataSource(unique(getSingleFiledList(neObjectList, "neName")));
		        	inputUnitName.setDataSource(unique(getSingleFiledList(neObjectList, "unitName")));
		        }
		    }).data("kendoDropDownList");
			
			$("#inputDHSSName").kendoDropDownList({
				optionLabel:"请选择DHSS组",
		        dataSource: dataSourceDhssName,
		        change: function(){
		        	dhssNameSelected = $("#inputDHSSName").val();
		        	locationNameSelected = null;
		        	var neObjectList = [];
	        		neObjectList = filterArrayWithKeyWords(unitQueryResult,"dhssName",dhssNameSelected);
		        	inputLocationName.setDataSource(unique(getSingleFiledList(neObjectList, "location")));
	        		neObjectList = filterArrayWithKeyWords(neObjectList,"neType",neTypeSelected);
		        	inputNeName.setDataSource(unique(getSingleFiledList(neObjectList, "neName")));
		        	inputUnitName.setDataSource(unique(getSingleFiledList(neObjectList, "unitName")));
		        }
		    }).data("kendoDropDownList");
		}
	});
}
function initDefaultTime() {
  	var myDate = new Date();
  	var year = myDate.getFullYear();       //年
    var month = myDate.getMonth() + 1;     //月
    if(month<10) month="0"+month;
    var day = myDate.getDate();            //日
    if(day<10)  day="0"+day;
    startTime=year+"-"+month+"-"+day+" 00:00:00";
    endTime=year+"-"+month+"-"+day+" 23:59:59";
    
  	$("#startTime").kendoDateTimePicker({
		format:"yyyy-MM-dd HH:mm:ss",
	    value:startTime
	});
	$("#endTime").kendoDateTimePicker({
		format:"yyyy-MM-dd HH:mm:ss",
		value:endTime
	});
	
}
function getDataM() {
	loadDataGrid();
}

function loadDataGrid(){
	var searchParams = {
			page : 0,
			size : 20,
			sort : "",
		
		};
	dataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
				url : "/findKpiHistoryDataByCondition",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			},
			parameterMap : function(options, operation) {
				if (operation == "read") {
					//console.log(searchParams);
					searchParams.dhssName = $("#inputDHSSName").val();
					searchParams.nodeName = $("#inputLocationName").val();
					searchParams.neType = $("#inputNeType").val();
					searchParams.neName = $("#inputNeName").val();
					searchParams.unitName = $("#inputUnitName").val();
					searchParams.scene = $("#inputKpiCategory").val();
					searchParams.kpiName = $("#inputKpiItem").val();
					searchParams.startTime = $("#startTime").val();
					searchParams.endTime = $("#endTime").val();
					searchParams.sort = "periodStartTime,desc";
					searchParams.page = options.page - 1;
					searchParams.size = options.pageSize;
					console.log(searchParams.kpiName)
					return searchParams;
				}
			},
},
				batch : true,
				pageSize : 20, // 每页显示个数
				schema : {
					data : function(d) {
						if (d._embedded) {
							return d._embedded["quota-monitor-history"]; // 响应到页面的数据
						} else {
							return new Array();
						}
					},
					total : function(d) {
						return d.page.totalElements; // 总条数
					},
				},
				serverPaging : true,
				serverFiltering : true,
				serverSorting : true
	});
				grid = $("#historyDataGrid").kendoGrid({
					dataSource : dataSource,
					height : $(window).height() - $("#historyDataGrid").offset().top - 50,
					width : "100%",
					reorderable : true,
					resizable : true,
					sortable : true,
					columnMenu : true,
					pageable : true,
					columns : [
					{
						field : "_links.self.href",
						title : "_links.self.href",
						hidden : true
					},{
						field : "dhssName",
						template : "<span  title='#:dhssName#'>#:dhssName#</span>",
						title : "<span  title='所属DHSS组'>所属DHSS组</span>"
					}, 
					{
						field : "neName",
						template : "<span  title='#:neName#'>#:neName#</span>",
						title : "<span  title='所在网元'>所在网元</span>"
					},
					{
						field : "unitName",
						template : "<span  title='#:unitName#'>#:unitName#</span>",
						title : "<span title='单元名称'>单元名称</span>"
					},{
						field : "unitNext",
						template : "<span  title='#:unitNext#'>#:unitNext == null ? '':unitNext#</span>",
						title : "<span title='链路或模块名称'>链路或模块名称</span>",
						hidden:true
					},{
						field : "unitNextId",
						template : "<span  title='#:unitNextId#'>#:unitNextId == null ? '':unitNextId#</span>",
						title : "<span title='链路或模块编号'>链路或模块编号</span>",
						hidden:true
					},{
						field : "neType",
						template : "<span  title='#:neType#'>#:neType#</span>",
						title : "<span title='网元类型'>网元类型</span>"
					}, {
						field : "nodeName",
						template : "<span  title='#:nodeName#'>#:nodeName#</span>",
						title : "<span  title='节点名称'>节点名称</span>"
					}, {
						field : "kpiName",
						template : "<span  title='#:kpiName#'>#:kpiName#</span>",
						title : "<span  title='指标名称'>指标名称</span>"
					}, {
						field : "kpiValue",
						template : "<span  title='#:kpiValue#'>#:kpiValue#</span>",
						title : "<span  title='指标值'>指标值</span>"
					}, {
						field : "kpiUnit",
						template : "<span  title='#:kpiUnit#'>#:kpiUnit#</span>",
						title : "<span  title='单位'>单位</span>"
					}, 
					{
						field : "periodStartTime",
						template : "<span  title='#:periodStartTime#'>#:periodStartTime#</span>",
						title : "<span  title='指标周期'>指标周期</span>"
					},],
					dataBound : function() {
					}
				}).data("kendoGrid");

}

$(function() {
	initKpiSelectBoxData();
	initNeSelectBoxData();
	initDefaultTime();
	getDataM();
});