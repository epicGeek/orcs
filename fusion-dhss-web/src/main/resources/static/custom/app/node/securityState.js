kendo.culture("zh-CN");

var neTypes = "";

var unit;
var neType;
var neState;
var startdatetime;
var enddatetime;

var searchparams = {
	page : 0,
	size : 20,
};
var dataSource;
$(function() {
	$("#inputBusinessQuota").remove();
	allbscname();

	
	$("#inputNeState").kendoDropDownList({
				optionLabel:"请选择操作方式",
				dataTextField : "text",
				dataValueField : "value",
				dataSource : [{
							text : "节点隔离",
							value : "1"
						}, {
							text : "节点恢复",
							value : "2"
						}, {
							text : "网元隔离",
							value : "4"
						}, {
							text : "网元恢复",
							value : "5"
						}],
				filter : "contains",
				suggest : true
			});
	var myDate = new Date();
  	var year = myDate.getFullYear();       //年
    var month = myDate.getMonth() + 1;     //月
    if(month<10) month="0"+month;
    var day = myDate.getDate();            //日
    if(day<10)  day="0"+day;
    startTime=year+"-"+month+"-"+day+" 00:00";
    endTime=year+"-"+month+"-"+day+" 23:59";
    
    $("#startdatetime").kendoDateTimePicker({
		format:"yyyy-MM-dd HH:mm:ss",
		value : startTime
	});
	$("#enddatetime").kendoDateTimePicker({
		format:"yyyy-MM-dd HH:mm:ss",
		value : new Date()
	});
	
	getDataM();
});
var gridDate;
function getDataM() {

	var ne = $("#inputNeNameQuota").val();
	var neState = $("#inputNeState").val();
	var startdatetime = $("#startdatetime").val();
	var enddatetime = $("#enddatetime").val();

	dataSource = new kendo.data.DataSource({
				transport : {
					read : {
						type : "GET",
						url : "emergencySecurity/querySecurityState",
						dataType : "json",
						contentType : "application/json"
					},
					parameterMap : function(options, operation) {
						if (operation == "read") {
							searchparams.page = options.page - 1;
							searchparams.pageSize = options.size;
							searchparams.ne = ne;
							searchparams.operate_state = neState;
							searchparams.startdatetime = startdatetime;
							searchparams.enddatetime = enddatetime;
							return searchparams;
						}
					}
				},
				batch : true,
				pageSize : 20, // 每页显示个数
				schema : {
					data : function(d) {
						if(d._embedded){
							var temparray = [];
							$.each(d._embedded["emergency-security-state"],function(index,item){
								if(item.unitEntity == null){
									item.unitEntity = {unitName:""};
								}
								temparray[temparray.length] = item;
							});
							return temparray;
						}
						return []; // 响应到页面的数据
					},
					total : function(d) {
						return d.page.totalElements; // 总条数
					}
				},
				serverPaging : true,
				serverFiltering : true,
				serverSorting : true

			});

	gridDate = $("#gridNodeRecovery").kendoGrid({
		dataSource : dataSource,
		height: $(window).height()-$("#gridNodeRecovery").offset().top - 50,				
		reorderable: true,		
		resizable: true,		
		sortable: true,		
		columnMenu: false,		
		pageable: true,		
		columns : [{
					field : "operateDate",
					template : "#= operateDate ? kendo.toString(new Date(operateDate), \'yyyy-MM-dd HH:mm\') : \'\' #",
					title : "时间",
					width : 180
				}, {
					field : "unitEntity.unitName",
					title : "单元名称",
					width : 180
				}, {
					field : "neEntity.neName",
					title : "网元名称",
					width : 120
				}, {
					field : "neEntity.location",
					title : "所在站点",
					width : 120
				}, {
					field : "operateState",
					template : "#if(operateState==1){#单元隔离#}if(operateState==2){#单元恢复#}if(operateState==4){#网元隔离#}if(operateState==5){#网元恢复#}#",
					title : "状态",
					width : 100
				}, {
					field : "operator",
					title : "操作人",
					width : 100
				},{
					template : "<button  onclick='showDetails(this);' class='btn btn-xs btn-default'><i class='glyphicon glyphicon-log-out'></i>查看详情</button>",
					title : "操作",
					width : "180px"
				}]
	}).data("kendoGrid");
}
// 查询
	$("#searchButton").click(function() {
		getDataM();
	});
function  handleSiteData(){
	
}	
	
	
function showDetails(e) {
	var data = gridDate.dataItem($(e).closest("tr"));
	var unitId;
	if(data.unitEntity.unitId==undefined){
		unitId = 0;
	}else{
		unitId = data.unitEntity.unitId;
	}
	if(data.operateState == 1 || data.operateState == 2){
		var url = "nodeIsolation";
		if(data.operateState == 2){
			url = "nodeRecoveryList";
		}
		location.href = url + '?state=' + data.operateState + '&webSite=' + data.neEntity.neId
		+ '&neTypeId=' + data.neEntity.neType + '&unitId=' + unitId;
	}else{
		location.href = "neSwitching?action="+data.operateState + "&caseid=" + data.nodeCaseEntity.caseId + 
		"&ne=" + data.neEntity.neId + "&caseName="+data.nodeCaseEntity.casename;
	}
	
}