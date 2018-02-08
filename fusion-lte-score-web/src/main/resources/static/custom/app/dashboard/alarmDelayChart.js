kendo.culture("zh-CN");
var searchParams = {startDate:'',endDate:'',areaCode:'',tableName:'bts_alarm_delay_day'};
var barChart;
var pieChart;
var type = "area_name";
var code = "area_code";
function getDelayChartData(){
	
	$.ajax({
		dataType : 'json',
		type : "GET",
		url : "alarm/DelaySearchChart",
		async:false,
		data:searchParams,
		success :function(data) {
			//console.log(data.area_code);
			var bar_x_Data = [];
			var bar_y_Data=[];
			var pie_y_Data=[];
			var pie_x_Data=[];
			if(null!=data){
				var barData = data.bar;
				var pieData = data.pie;
				//时长
				$.each(barData, function(index, item) {
					bar_x_Data.push({code : item[code],name:item[type]});
					bar_y_Data.push(item.difftime);
					
				});
				//饼图
				var total = 0;
				$.each(pieData, function(index, item) {
					if(item[type]=='1'){
						total = item.difftime;
					}
				});
				//计算饼图总时长占比
				$.each(pieData, function(index, item) {
					if(item[type]!="1"){
						pie_x_Data.push(item[type]);
						var value = item.difftime / total * 100;
						//console.log(item.difftime +'            '+total);
						var aNew = parseFloat(value.toFixed(2));
						pie_y_Data.push({value:aNew,name:item[type]});
					}
				});
				//柱状图
				setOption(bar_x_Data,bar_y_Data);
				//饼图
				setPieOption(pie_x_Data,pie_y_Data);
			}
		},
		fail:function(error){
			
		}
	});
	
}

//柱状图绘制================start=======================
function tochart(options){
	
	barChart= echarts.init(document.getElementById('barChart'));
		
	barChart.setOption(options);
	
		barChart.on("click",function(e){
			var areaCode,cityCode,tableName;
			//获取地市code
			if(code == "area_code"){
				areaCode = e.name.code;
				cityCode  = "";
			}else{
				//获取、地市区县code
				areaCode = $('#areaCode').val();
				cityCode = e.name.code;
			}
			//需要的对象： console.log(e);
			
			tableName = $("#scoreType").val()!=""?$("#scoreType").val():"bts_alarm_delay_day";
			if(tableName=='bts_alarm_delay_day'){
				window.location.href = "alarmDelay?areaCode="+areaCode+"&cityCode="+cityCode+"&tableName="+tableName+"&startDate="+$('#startDate').val()+'&endDate='+$('#endDate').val()+'&type=1';
			}else{
				window.location.href = "alarmDelay?areaCode="+areaCode+"&cityCode="+cityCode+"&tableName="+tableName+'&type=1';
			}
       });
}
function setOption(xData,yData){
	
	var barOption = {
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
						tip += arr[i].seriesName + "：" + (arr[i].value) + "小时<br />";
					}
					return tip;
				}
			},
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
				data: xData
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
					formatter: '{value}'
						//formatter:'{value}%'
				}
			}],
			series: [{
				name: '时长',
				type: 'bar',
				stack: '总量',
				barWidth: 15,
				data:yData
			}]
	};
	tochart(barOption);
	
}
//==================柱状图end=========================

//饼图图形绘制 ===============start===================
function pieTochart(options){
	
	pieChart= echarts.init(document.getElementById('pieChart'));
	pieChart.setOption(options);
}
//故障原因饼状图
function setPieOption(pie_x_Data,pie_y_Data){
	
	//console.log(pie_x_Data);
	//console.log(pie_y_Data);
	var pieChart = echarts.init(document.getElementById('pieChart'));
	var pieOption = {
		title: {
			text: ''
		},
		tooltip: {
			trigger: 'item',
			formatter: "{b} : {c}%"
		},
		color: ["#417AB1", "#4982B9", "#4E82BF", "#528BC2", "#5893CA", "#5F9dCF", "#629CD3", "#669aD3", "#6BA4DB", "#72ADE4", "#77ADED", "#7CB5EC", "#7eB9Ed", "#84BDF4", "#85C6FD", "#86CEee", "#8eD7ee", "#91deef", "#94dfeF"],
		legend: {
			show: false,
			data: pie_x_Data
		},
		toolbox: {
			show: false
		},
		series: [{
			type: 'pie',
			itemStyle: {
				normal: {
					label: {
						show: true,
						textStyle: {
							color: "#555"
						},
					formatter:function(e){
						return e.name +"："+e.value+"%";
						}
					},
					labelLine: {
						show: true,
						lineStyle: {
							color: '#ccc'
						}
					},
					borderColor: "#dfdfdf"
				},
				emphasis: {
					label: {
						show: true,
						position: 'center',
						textStyle: {
							fontSize: '16',
							fontWeight: 'bold',
							color: "#333"
						}
					},
					borderColor: "#FD6A5E"
				}
			},
			data:pie_y_Data
			
		}]
	};
	/*console.log("pie_x_Data");
	console.log(pie_x_Data);
	console.log("pie_y_Data");
	console.log(JSON.stringify(pie_y_Data));*/
	pieChart.setOption(pieOption);
}
//=========================饼图end=====================================
$(function() {
	
	var today = new Date();
	var startTime = $("#startDate").kendoDatePicker({
		format: "yyyy-MM-dd",
		value: new Date(today.getFullYear(),today.getMonth(),today.getDate()-1)
	});
	var endTime = $("#endDate").kendoDatePicker({
		format: "yyyy-MM-dd",
		value: today
	});
	
	initCityList();  //初始化城市
	
	initAreaList()  ;//初始化地区
	
	initDateType();	//统计方式
	//查询条件
	$('#startDate').attr("disabled",true);
	$('#endDate').attr("disabled",true);
	searchParams.startDate = $('#startDate').val();
	searchParams.endDate = $('#endDate').val();
	
	getDelayChartData();
	//查询
	$('#searchBtn').on('click', function() {
		searchParams.areaCode = $('#areaCode').val();
		searchParams.startDate = $('#startDate').val(); //时间
		searchParams.endDate = $('#endDate').val();
		if(searchParams.areaCode!=""){
			type = "city_name";
			code = "city_code";
		}else{
			type = "area_name";
			code = "area_code";
		}
		searchParams.tableName = $("#scoreType").val()==""?"bts_alarm_delay_day":$("#scoreType").val();
		//searchParams.tableName = $("#scoreType").val();
		getDelayChartData();//条件查询重新加载 
	});
	
	//重置
	$('#resetBtn').on('click', function() {
		$('#areaCode').data("kendoDropDownList").text('');
		searchParams.areaCode = "";
		var today = new Date();
		$('#endDate').data("kendoDatePicker").value(today);
		$('#startDate').data("kendoDatePicker").value(new Date(today.getFullYear(),today.getMonth(),today.getDate()-1));
		//searchParams.tableName = "bts_alarm_delay_day";
		$('#scoreType').data("kendoDropDownList").select(0);
		code = "area_code";
		type = "area_name";
		getDelayChartData();
	});
	
});

function initCityList(){
	var dtd = $.Deferred(); // 新建一个Deferred对象
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "city/search",
		"async":false,
		"success" : function(data) {
			$("#areaCode").kendoDropDownList({
				optionLabel:"--请选择地市--",
	            dataTextField: "areaCn",
	            dataValueField: "areaCode",
	            dataSource: data,
	            filter: "contains"/*,
	            change: function(){
	            	var areaCode = $('#areaCode').val();
	            	getAreaData(areaCode);
	            }*/
	        });
			/*	//回显
			var va = searchParams.areaCode;
			if(va!=""){
				$('#areaCode').data("kendoDropDownList").value(va);
				getAreaData(va);
			}*/
			dtd.resolve(); // 改变Deferred对象的执行状态
		}
	});
	return dtd;
}

function getAreaData(cityCode){
	
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "area/search",
		data:{
			"cityCode":cityCode
		},
		"success" : function(data) {
			//$("#cityCode").data("kendoDropDownList").setDataSource(data);
		//	$('#cityCode').data("kendoDropDownList").value(searchParams.cityCode);
		}
	});
}

function initAreaList(){
	
	$("#cityCode").kendoDropDownList({
		optionLabel:"--请选择地区--",
        dataTextField: "cityCn",
        dataValueField: "cityCode",
        dataSource: [],
        filter: "contains"
    });
}

function initDateType(){
	var data = [
	            { text: "日", value: "bts_alarm_delay_day" },
	            { text: "周", value: "bts_alarm_delay_week" },
	            { text: "月", value: "bts_alarm_delay_month" }
	        ];
	//下拉
	$("#scoreType").kendoDropDownList({
		 	//autoBind: false,
			//optionLabel:"--请选择统计方式--",
			dataSource:  data,
			dataTextField: "text",
	        dataValueField: "value",
	        select: function(e){
	        	//console.log($(e.item).text());
	        	if($(e.item).text()=='日'){
	        		$("#dateWrap").show();
	        	}else{
	        		$("#dateWrap").hide();
	        	}
	        }
		});
}