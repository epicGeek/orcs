//饼图——地市柱状图故障占比排名
var code = "";
var name = "";
var searchParams = {};
$(function() {
	
	//获得参数
	var areaCode = getQueryString("area_code");
	var cityCode = getQueryString("city_code");
	code = areaCode==""?"area_code":"city_code";
	name = areaCode==""?"area_name":"city_name";
	var startDate = getQueryString("startDate");
	var endDate = getQueryString("endDate");
	kpiName = getQueryString("kpiName");
	var faultName =getQueryString("faultName");
	faultName = decodeURI(faultName);
	searchParams={
			areaCode: (areaCode && areaCode!=null && areaCode !="null")?areaCode:"",
			cityCode: (cityCode && cityCode!=null && cityCode !="null")?cityCode:"",
			startDate: (startDate && startDate!=null && startDate !="null")?startDate:"",
			endDate: (endDate && endDate!=null && endDate !="null")?endDate:"",
			kpiName: (kpiName && kpiName!=null && kpiName !="null")?kpiName:"",
			faultName:(faultName && faultName!=null && faultName != "null")?faultName:"",		
	}
	
	initScoreAreaDataSource();
	
	$('#sumTxt').text(faultName+"-故障排名");
	
});

function setOptons(x_data,y_data){
	var faultChart = echarts.init(document.getElementById('faultChart'));
	var pieOption = {
			title: {
				text: '',
				x: 'left'
			},
			grid: {
				x: 50,
				y: 40,
				x2: 15,
				y2: 40
			},
			tooltip: {
				trigger: 'axis',
				formatter: function(arr) {
					var tip = arr[0].name.name + "<br />";
					for (var i = 0; i < arr.length; i++) {
						tip += arr[i].seriesName + "：" + (arr[i].value) + "%<br />";
					}
					return tip;
				}
			},
			/*	legend: {
			data: ['一级', '二级', '三级', '四级', '五级'],
			show: true
		},*/
			toolbox: {
				show: false
			},
			axis: {
				axisLine: {
					show: false
				}
			},
			color: ["#FD6A5E", 'orange', "#FDE123", '#4D90FD', '#82BF64'],
			calculable: true,
			xAxis: [{
				type: 'category',
				axisLine: {
					show: false
				},
				axisTick: {
					show: false
				},
				splitLine: {
					lineStyle: {
						color: ['#D9D9D9']
					}
				},
				axisLabel:{
					formatter:function (value){return value.name} 
				},
				data: x_data
			}],
			yAxis: [{
				name: "",
				type: 'value',
				axisLine: {
					show: false
				},
				axisTick: {
					show: false
				},
				splitLine: {
					lineStyle: {
						color: ['#D9D9D9']
					}
				},
				axisLabel: {
					//formatter: '{value}'
					formatter:'{value}%'
				}
			}],
			series: [{
				name: '故障占比',
				type: 'bar',
				stack: '总量',
				barWidth: 15,
				data:y_data
			}]
	};
	faultChart.setOption(pieOption);
	faultChart.on("click",function(e){
		
		var areaCode,cityCode;
		
		//获取地市code
		if(searchParams.areaCode == ""){
			areaCode = e.name.code;
			cityCode  = "";
		}else{
			//获取、地市区县code
			areaCode = searchParams.areaCode;
			cityCode = e.name.code;
			
		}
		//需要的对象：console.log(e);
		//	window.location.href = "./dashboard_pieChart.html?name="+e.name;
		window.location.href = "/breakdownReason?scoreType=1"+"&startDate="+searchParams.startDate+"&endDate="+
			searchParams.endDate+"&areaCode="+areaCode+"&cityCode="+cityCode;
	});
	
}

function initScoreAreaDataSource() {
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "dashboard/searchAreaAndCityScore",
		"data":{
			"startDate":searchParams.startDate,
			"endDate":searchParams.endDate,
			"areaCode":searchParams.areaCode,
			"kpiName":kpiName
		},
		"success" : function(data) {
			if(data.length==0){
				showNotify("无数据!","warning");
				return;
			}
			var x_data = [];
			var y_data = [];
			$.each(data, function(index, item) {
				x_data.push({name:item[name],code:item[code]});  
				//var value = item[kpiName] / kpi_total * 100;
				//var aNew = parseFloat(value.toFixed(2));
				y_data.push(item[kpiName]);
			});
			
		//	y_data.sort(function(a,b){return b-a});
			setOptons(x_data,y_data);

		},
		fail:function(error){
			showNotify("失败","warning");
		}
	});
}

/*
 * 接收url参数
 *  调用方法：
 *	alert(GetQueryString("参数名1"));
 */
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]); return null;
}