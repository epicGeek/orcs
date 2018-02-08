var dataSource=[];
var columns = [], kpiData = [],cellIds=[];
var neCode = getQueryString("neCode");
var cycle = getQueryString("cycle");
var neName =decodeURI(getQueryString("neName"));
var parameters = {
	neCode : (neCode && neCode != null && neCode != "null") ? neCode : "",
	cycle : (cycle && cycle != null && cycle != "null") ? cycle : ""
};

/*
 * 基站指标 基准值 前值>当前值 箭头向上   前值和当前值>基准值变成红色
 */
$(function() {

	kendo.culture("zh-CN");
	$('#neCode').html("<span>基站ID："+parameters.neCode+"&nbsp;&nbsp;&nbsp;&nbsp;基站名称："+neName
			+"&nbsp;&nbsp;&nbsp;&nbsp;时间："+cycle+"</span>");
	
	// 获取kpi类型
	initKpi();
	// 根据小区动态创建标题
	createRow();

	$("#btsAlarmScoreList").kendoGrid({
		dataSource : dataSource,
		groupable : false,
		sortable : true,
		resizable : true,
		columnMenu : false,
		reorderable : true,
		columns : columns,
	}).data("kendoGrid");
});

/**
 * 动态获取kpi名称
 */
function initKpi() {
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "kpiIndex/getKpiRule",
		async : false,
		"success" : function(data) {
			kpiData = data;
		}
	});
}

// 动态创建列
function createRow() {

	columns.push({field : "kpiId",hidden:true});
	columns.push({field : "kpiName",title : "指标名称",template:function(item){
		var kpiName=encodeURI(encodeURI(item.kpiName));
		var name = encodeURI(encodeURI(neName));
		return "<a href='chartList?cellId="+cellIds.join(",")+"&neCode="+parameters.neCode+"&kpiId="+item.kpiId
					+"&kpiName="+kpiName+"&cycle="+parameters.cycle+"&neName="+name+"'>"+item.kpiName+"</a>";
		}
	});
	columns.push({field : "baseValue",width : '60px;',title : "基准值"});

	$.ajax({
		dataType : 'json',
		type : "POST",
		contentType : "application/json;charset=UTF-8",
		url : "kpiIndex/row",
		async : false,
		data : kendo.stringify(parameters),
		success : function(data) {
			cellIds = data.id;
			$.each(cellIds, function(index, cellId) {
				var perClass = '', currentClass = '',red="";
				columns.push({
					field : 'beforeValue' + cellId,
					width : '160px;',
					title : "前值(小区ID:" + cellId + ")",
					template : function(da) {
						if(da['beforeValue' + cellId]!=""){
							if (da['beforeValue' + cellId] > da['currentValue'+ cellId]) {
								perClass = "toTop";
							}else{
								if (da['beforeValue' + cellId] < da['currentValue'+ cellId]){
									perClass = "toBottom";
								}else{
									perClass = "";
								}
							}
							
							if (da['beforeValue' + cellId] > da.baseValue) {
								red= " redColor";
							}else{
								red = "";
							}
						}
						return "<span class='" + perClass + " "+red+"'>"
								+ da['beforeValue' + cellId] + "</span>";
					}
				});
				columns.push({
					field : 'currentValue' + cellId,
					width : '160px;',
					title : "当前值(小区ID:" + cellId + ")",
					template : function(da) {
						
						if(da['beforeValue' + cellId]!=""){
							if (da['currentValue'+ cellId]<da['beforeValue' + cellId] ) {
								currentClass = "toBottom";
							}else{
								if(da['beforeValue' + cellId] < da['currentValue'+ cellId]){
									currentClass = "toTop";
								}else{
									currentClass = "";
								}
							}
						}
						
						if (da['currentValue' + cellId] > da.baseValue) {
							red = " redColor";
						}else{
							red = "";
						}
						return "<span class='" + currentClass + " "+red+"'>"
								+ da['currentValue' + cellId] + "</span>";
					}

				});

			});
			
			// 封装dataSoure
			setKpiData(data);
		}
	});
}

/**
 * 动态获取数据
 */
function setKpiData(data) {
	if (data) {
		 cellIds = data.id;
		var rowData = data.data;
		$.each(kpiData, function(x, item_kpi) {
			var bak = {};
			bak.kpiId = item_kpi.KPI_ID;
			bak.kpiName = item_kpi.KPI_NAME;
			bak.baseValue = item_kpi.case_value;
			var key = item_kpi.KPI_ID.toLowerCase() + '_value';
			$.each(cellIds, function(index, cellId) {
				var beData = rowData[index]['be' + cellId];
				var cuData = rowData[index]['cu' + cellId];
				if(beData){
					bak['beforeValue' + cellId] = beData[key];
				}else{
					bak['beforeValue' + cellId] = "";
				}
				bak['currentValue' + cellId] = cuData[key];
			});
			dataSource.push(bak);
		});
	}
}
