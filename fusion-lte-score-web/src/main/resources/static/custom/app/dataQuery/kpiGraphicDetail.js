/**
 * kpi图形-图
 * 
 * 默认显示30天
 * 
 * */

kendo.culture("zh-CN");

var searchParams = {neCode:'',cellid:'',sectorId:'',cycleStart:'',cycleEnd:''};

var lineChart;

var kpi_id = "";
//动态组装kpi名称 值
function initKpiMap() {
		$.ajax({
			"dataType" : 'json',
			"type" : "GET",
			"url" : "kpiIndex/getKpiRule",
			"success" : function(data) {
				$.each(data, function(index, item) {
					if(item.KPI_NAME==$('#kpiName').val()){
						kpi_id = item.KPI_ID;
						return false;
					}
				});
			}
	});
}

function kpiChartList(){
	
	//initKpiMap();
	$.ajax({
		dataType : 'json',
		type : "GET",
		url : "kpiChartList/search",
		data:{
			"cycleStart":$('#cycleStart').val(),
			"neCode":$('#neCode').val(),
			"cellid":$('#cellid').val(),
			"sectorId":$('#sectorId').val(),  //扇区ID
			"cycleEnd":$('#cycleEnd').val()
		},
		success : function(data) {
			//x轴时间
			var cycle_date_x=[];
			//y轴对应每个KPI的value
			var kpiData_y = [];
			$.each(data, function(index,item){
				var key = kpi_id.toLowerCase()+"_value";  //kpi_value值
				for(var kpiname in item){
					
					if(key==kpiname){  //如：kpi_001_value=kpiname
						kpiData_y[index] = item[key];
						cycle_date_x[index] = getDate(item.cycle);
						return true;
					}
				}
				
			});
			setData(cycle_date_x,kpiData_y);
		}
	});
}

function setData(date_x,dataKpi_y){
	
	$("#context").empty();
	var end = date_x.length-2;
	/*var end;
    //var val=100;
	if(val==100){
		 end = date_x.length-2;
	}else{
		//var num = date_x.length/5;
		//if(num>1){
			//end = 1/num*100;
		//}
		end = 10;
	}*/
	var lineChart = echarts.init(document.getElementById('lineChart'));
	var lineOption = {
			title: {
				text: '小区ID:'+$('#cellid').val()+'， 指标名称：'+$('#kpiName').val(),
				subtext: '',
				x: "center",
				backgroundColor: "rgba(0,0,0,.1)",
				borderColor:"rgba(0,0,0,.4)",
				borderWidth:0,
				textStyle:{color: "#eee",fontSize:14,fontFamily:"微软雅黑"}
			},
			tooltip: {trigger: 'axis',formatter: "时间：{b}<br />KPI值：{c}"},
			legend: {
				data: ['指标'],
				show:false,
				textStyle:{color: "#fff"}
			},
			toolbox: {show: false},
			color:["#fff"],
			grid:{borderColor:"#FFA88D",y2: 110,x:95},
			dataZoom:{
				show: true,
				//realtime: true,
				showDetai:true,
				start:0,
				end:100,
				height:40,
				handleColor:"#009",
				handleSize:10,
			},
			xAxis: [{
				type: 'category',
				boundaryGap: false,
				splitLine:{lineStyle:{color: "#009"}},
				axisLine: {show: true,lineStyle:{color:"#009"}},
				axisTick: {show: true,interval:0},
				axisLabel: {textStyle: {color: "#fff",fontFamily:"微软雅黑"},rotate: 0 },
				data: date_x   // x轴
			}],
			yAxis: [{
				name:"KPI值",
				type: 'value',
				splitLine:{lineStyle:{color: "#FFA88D"}},
				axisLine: {
					show: true,
					lineStyle:{color:"#fff",width: 0}
				},
				axisTick: {show: false},
				axisLabel: {textStyle: {color: "#fff"}}
			}],
			series: [{
				name: '时间',
				type: 'line',
				smooth: true,
				symbolSize: 5,
				itemStyle: {normal: {areaStyle: {type: 'default'},}},
				data:dataKpi_y   // y轴
			},]
	};
	lineChart.setOption(lineOption);
	
	/*lineChart.on(echarts.config.EVENT.DATA_ZOOM, function(param){
		console.log(arguments);
	});*/
	//DATA_CHANGED
	/*lineChart.on(echarts.config.EVENT.DATA_VIEW_CHANGED,function(param){
		//console.log(arguments);
	});*/
}
	
/*
 * 时间样式
 * function getDate(tm){
	var dateFormat=new Date(parseInt(tm)).toLocaleString();
	return dateFormat;
} */
function getDate(tm){

	var date= new Date(parseInt(tm));
	var year = date.getFullYear();
	var month = addZero(date.getMonth()+1);
	var day = addZero(date.getDate());
	var hour = addZero(date.getHours());
	var minutes = addZero(date.getMinutes());
	return (year+"/"+month+"/"+day+" "+hour+":"+minutes);
}
function addZero(s) {
    return s < 10 ? '0' + s: s;
}

$(function() {
	
	initKpiMap();
	
	kpiChartList();
	
	// 时间选择查询 
	var today=new Date();
	 $("#cycleStart").kendoDateTimePicker({
          format: "yyyy-MM-dd HH:mm:ss",
          value:new Date(today.getFullYear(),today.getMonth(),today.getDate()-30)
	   });
	 $("#cycleEnd").kendoDateTimePicker({
          format: "yyyy-MM-dd HH:mm:ss",
          value:today
	 });
	//$("#cycleStart").attr("disabled",true);
	
    //条件查询按钮
	$('#searchBtn').on('click', function() {
		 
		 kpiChartList();
	});
	
	// 重置
	$('#resetBtn').click(function(){
		$('#cycleEnd').data("kendoDateTimePicker").value(today);
		$('#cycleStart').data("kendoDateTimePicker").value(new Date(today.getFullYear(),
				today.getMonth(),today.getDate()-30));
		
		kpiChartList();
	});
	
});

