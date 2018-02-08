/**
 * 地市 雷达图
 */
var kpiMap=[];
var parameters;
var radarChart;

//获得参数
var startDate = getQueryString("startDate");
var endDate = getQueryString("endDate");
var neCode = getQueryString("neCode");
var neName = decodeURI(getQueryString("neName"));
$(function() {
	var today = new Date();
	parameters={
			startDate: (startDate && startDate!=null && startDate !="null")?startDate:"",
			endDate: (endDate && endDate!=null && endDate !="null")?endDate:"",
			neCode:(neCode && neCode != null && neCode != "null")?neCode:"",
			tableName:"bts_score_hour",
	}
	
	kendo.culture("zh-CN");
	//时间格式化
	$("#startDate").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:00:00",
		value:parameters.startDate
	});
	$("#endDate").kendoDateTimePicker({
		format:"yyyy-MM-dd HH:00:00",
		value:parameters.endDate
	});
	
	$('#name').text("雷达图     基站ID："+parameters.neCode+"  基站名称："+neName);
	
	indexName();
	
	initScoreAreaDataSource();
	
	$("#searchBtn").click(function(){
		parameters.startDate=$("#startDate").val();
		parameters.endDate=$("#endDate").val();
		initScoreAreaDataSource();
	})
	
});

function indexName(){
	$.ajax({
		"dataType":'json',
		"type":"GET",
		"url":"kpiIndex/getKpiRule",
		async:false,
		"success" : function(data) {
			//console.log(data);
			$.each(data, function(index, item) {
				kpiMap.push({id:item.KPI_ID+'_score',name:item.KPI_NAME});
			});
			kpiMap.push({id:'alarm_score',name:'基站告警'});
			kpiMap.push({id:'out_of_score',name:'退服'});
		}
	})
}

function toCharts(options){
	 radarChart = echarts.init(document.getElementById('radarChart'));
	radarChart.setOption(options);
}

function setOptions(xData,yData){
	//雷达图
	var radarOption = {
			title: { text: '',   subtext: ''  },
			tooltip: {trigger: 'axis' },
			toolbox: {show: false },
			polar: [{
				indicator:	xData
			}],
			
			calculable: true,
			series: [{
				type: 'radar',
				data: [{
					value: yData,
					name: ''
				}]
			}]
	};
	toCharts(radarOption);
}

function initScoreAreaDataSource(){
	$.ajax({
		dataType : 'json',
		type : "POST",
		contentType : "application/json;charset=utf-8",
		url : "dashboard/areaRadar",
		data : kendo.stringify(parameters),
		"success" : function(data) {
			
			if(data.length>0){
				var xData = [];
				var yData = [];
				
				$.each(kpiMap, function(index, item) {
					var score = data[0][item.id];
					var name = item.name;
					xData.push({text:name,max:100});
					yData.push(score);
				});
				setOptions(xData,yData);
			}else{
				toCharts({});
			}
		}
	});
}
