//使用基于ES存储的BOSS业务监控JS
kendo.culture("zh-CN");
var apiEndPoint = "http://127.0.0.1:8096/"
//var apiEndPoint = "http://172.16.73.51:8096/"
var kendoDropDownList;
var soapGwNameAndHlrsnMap = {};
var hlrsnArray = ["50","51","52","53"];
var businessTypes = [ {
	"text" : "VoLTE",
	"value" : "VoLTE"
},{
	"text" : "LTE",
	"value" : "LTE"
},  {
	"text" : "语音",
	"value" : "VOICE"
}, {
	"text" : "智能网",
	"value" : "NETWORK"
}, {
	"text" : "开销户",
	"value" : "OVERHEAD"
}, {
	"text" : "停复机",
	"value" : "STOPRESET"
}, {
	"text" : "GPRS",
	"value" : "GPRS"
}];
var timePeriod = [ {
	"text" : "15分钟",
	"value" : "15min"
},{
	"text" : "1小时",
	"value" : "1hour"
}, {
	"text" : "24小时",
	"value" : "1day"
}, {
	"text" : "1个月",
	"value" : "1month"
}];
$(function(){
	initKpiDefaultSelectOptionsAndData();
	queryBossKpi(); 
});
function initKpiDefaultSelectOptionsAndData() {
	loadSoapGwNameInTheSelectOption();
	loadDefaultTimePeriod();
	loadBusinessType();
	loadTimePeriod();
}
function getParams(){
	var params = {
			hlrsn:$("#soapGwName").val(),
			businessType:$("#businessType").val(),
			startTime:$("#startTime").val(),
			endTime:$("#endTime").val(),
			timeFiness:$("#timePeriod").val()
	}
	return params;
}
function queryBossKpi(){
	var params = getParams();
	if(params.businessType==''||params.timeFiness==''){
		infoTip({content: "必须选择业务类型和时间粒度"});
	}else{
		$.ajax({
		//	url:"/boss-rev/getBossKpi",
			url:apiEndPoint+"api/cmcc-sh/boss/statistic",
			type:"GET",
			data:{"hlrsn":params.hlrsn,
				"businessType":params.businessType,
				"startTime":params.startTime,
				"endTime":params.endTime,
				"timeFineness":params.timeFiness},
			dataType:"json",
			success: function(data){
				var timePeriodArray = new Array();
				var successRateArray = new Array();
				var totalCountArray = new Array();
				if(data.label !== null){
					timePeriodArray = data.label;
				}
				if(data.count !== null){
					totalCountArray = data.count;
				}
				if(data.ratio !== null){
					successRateArray = data.ratio;
				}
				console.log(data);
				drawCharts(timePeriodArray,successRateArray,totalCountArray);
			}
		});
	}

}
function tochart(options){
	var charts = echarts.init(document.getElementById('charts'));
	charts.setOption(options);
}
function drawCharts(timePeriodArray,successRateArray,totalCountArray){
	
	var viewTitle = ['成功率','总次数'];
	var ctitles = '总次数';
	var options = {
			tooltip: {
				trigger: 'axis'
			},
			grid:{
				borderColor: '#efefef',
				y2:20
			},
			toolbox: {
				show: true,
				feature: {
					
					dataView: {
						show: true,
						readOnly: false
					},
					magicType: {
						show: true,
						type: ['bar', 'line']
					},
					restore: {
						show: true
					},
					saveAsImage: {
						show: true
					}
				}
			},
			calculable: true,
			legend: {
				data: viewTitle
			},
			xAxis: [{
				type: 'category',
				axisLine:{
					show:false
				},
				splitLine:{
					show: true,
					lineStyle:{
						color: '#efefef'
					}
				},
				axisTick:{
					lineStyle:{
						color: '#ccc'
					}
				},
				data: timePeriodArray
			}],
			yAxis: [{
				type: 'value',
				name: "",
				splitLine:{
					show:false
				},
				splitArea:{
					show : true
				},
				axisLine:{
					lineStyle:{
						color: '#FF7F50'
					}
				},
				axisLabel: {
					formatter: '{value}%'
				}
			}, {
				type: 'value',
				name: '',
				splitLine:{
					show:false
				},
				axisLine:{
					lineStyle:{
						color: '#87CEFA'
					}
				},
				axisLabel: {
					formatter: '{value}次'
				}
			}],
			series: [
				{
					name: '成功率',
					type: 'line',
					//barMaxWidth: 40, //限制柱状图 宽度
					data: successRateArray
				}, {
					name: ctitles,
					type: 'bar',
					yAxisIndex: 1,
					smooth:true,
					symbolSize:4,
					symbol: "emptyCircle",
					data:totalCountArray
				}
			]
		};
	
	tochart(options);
}
function loadBusinessType(){
	kendoDropDownList = $("#businessType").kendoDropDownList({
		optionLabel : "请选择业务类型",
		dataSource : businessTypes,
		dataTextField : "text",
		dataValueField : "value",
		filter : "contains",
		suggest : true,
		change : function() {
			
		}
	}).data("kendoDropDownList");
	kendoDropDownList.select(1);
}
function loadTimePeriod(){
	kendoDropDownList = $("#timePeriod").kendoDropDownList({
		optionLabel : "请选择时间粒度",
		dataSource : timePeriod,
		dataTextField : "text",
		dataValueField : "value",
		filter : "contains",
		suggest : true,
		change : function() {
			var timeFitness = $("#timePeriod").val();
			$.ajax({
				url:"/boss-rev/getBossKpiTimePeriodSelection",
				type:"GET",
				data:{"timeFitness":timeFitness},
				success:function(data){
					var startTime = data.startTime;
					var endTime = data.endTime;
					$("#startTime").val(startTime) ;
					$("#endTime").val(endTime) ;
					
					
				}
			});
		}
	}).data("kendoDropDownList");
	kendoDropDownList.select(1);
}
function loadSoapGwNameInTheSelectOption() {

	kendoDropDownList = $("#soapGwName").kendoDropDownList({
		optionLabel : "请选择HLRSN",
		dataSource : hlrsnArray,
		filter : "contains",
		suggest : true,
		change : function() {

		}
	}).data("kendoDropDownList");
	kendoDropDownList.select(1);

}
function loadDefaultTimePeriod() {

	$.ajax({
		url : "/boss-rev/getCurrentHourTimePeriod",
		type : "GET",
		contentType : "application/json;charset=UTF-8",
		async:false,
		success : function(data) {
			var startTime = data.startTime;
			var endTime = data.endTime;
			$("#startTime").kendoDateTimePicker({
				format : "yyyy-MM-dd HH:mm:ss",
				value : startTime
			});
			$("#endTime").kendoDateTimePicker({
				format : "yyyy-MM-dd HH:mm:ss",
				value : endTime
			});
		}
	});

}