
/**
 * kpi图形指标-->指标图形界面 11个折线图
 */
//获得参数
var cellId = getQueryString("cellId");
var cycleDate  = getQueryString("cycle");
var kpiId = getQueryString("kpiId");
var neCode=getQueryString("neCode");
var kpiName=decodeURI(getQueryString("kpiName"));
var neName = decodeURI(getQueryString("neName"));
$(function() {
	
	kendo.culture("zh-CN");
	
	parameters = {
		cellId: (cellId && cellId != null && cellId !="null")?cellId:"",
		cycleDate: (cycleDate && cycleDate != null && cycleDate !="null")?cycleDate:"",
		kpiId: (kpiId && kpiId!=null && kpiId !="null")?kpiId:"",
		neCode:(neCode && neCode!=null && neCode!="null")?neCode:""		
	}
	
	$('#name').text("基站ID："+neCode+"  小区ID："+cellId);
			
	//查询条件
	$("#startDate").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:00:00",
		value:parameters.cycleDate
	});
	//查询天数
	$("#daysInput").kendoNumericTextBox({
		format: "",
		min: 4,
		max: 10,
		value: 4,
		step: 1
	});

	kpiChartList();
	
	$("#daysInput").attr("disabled",true);
	
    //条件查询按钮
	$('#searchBtn').on('click', function(){
		 kpiChartList();
	});
	
	// 重置
	$('#resetBtn').click(function(){
		$('#startDate').val("");
		$('#daysInput').val("4");
		kpiChartList();
	});
	
	//各指标 折线图--->【基站 雷达图】按钮
	$("#queryBtn").on("click",function(){
		var d1=new Date(parameters.cycleDate);
		var hour=$('#daysInput').val();
		var date=d1.getTime()-hour*60*60*1000;
		var startDate= new Date(date).Format("yyyy-MM-dd hh:00:00");
		var name = encodeURI(encodeURI(neName)); //编码
		window.location.href = "btsRadarChart?neCode="+parameters.neCode+"&startDate="+startDate+"&endDate="+parameters.cycleDate+"&neName="+name;
	});
	
	//点击返回按钮事件
	$("#returnBtn").click(function(){
		javascript:history.go(-1);
	});	
	
});

function kpiChartList(){
	
	$.ajax({
		dataType : 'json',
		type : "GET",
		url : "kpiChartSearch/search",
		data:{
			"cycleDate":$('#startDate').val(),
			"cellId":parameters.cellId,
			"kpiId":parameters.kpiId.toLowerCase()+"_value",
			"daysInput":$('#daysInput').val()==""?"4":$('#daysInput').val()
		},
		success : function(data) {
			var cellIds = parameters.cellId.split(',');
			var kpiObj = {};
			if(data){
				$.each(cellIds, function(x,id){
					var xData =[];
					var yData = [];
					var  cellData = data[id];
					var cell = {};
					$.each(cellData, function(y,item){
						 xData.push(item.cycle);
						 yData.push(item[parameters.kpiId.toLowerCase()+"_value"]);
					});
					cell.xd=xData;
					cell.yd=yData;
					
					kpiObj[id]=cell;
					
				})
				//给数据
				setData(kpiObj);
			}
			
		}
	});
}

function setData(kpiObj){
	$("#context").empty();
	$.each(kpiObj, function(index,item){
		lineChart(item.xd,item.yd,index);
	});
}
//折线图
function lineChart(xData,yData,id){
	
	$("#context").append("<div class='col-sm-4 chartWrap' style='margin-bottom:15px;'>"
				+"<div class='red-chart'>"
				+"<div id='"+id+"' style='height: 250px;'></div>"
				+"</div></div>");
	var lineChart = echarts.init(document.getElementById(id));
	var lineOption = {
		title: {text: '',subtext: ''},
		tooltip: {trigger: 'axis',formatter: "时间：{b}<br />KPI值：{c}"},
		legend: {
			data: [kpiName],
			textStyle: {color: "#fff"}
		},
		toolbox: {show: false},
		color: ["#fff"],
		grid: {borderColor: "#FFA88D"},
		xAxis: [{
			type: 'category',
			boundaryGap: false,
			splitLine: {
				lineStyle: {color: "#FFA88D",width:0}
			},
			axisLine: {show: false},
			axisTick: {show: false},
			axisLabel: {
				textStyle: {
					color: "#fff",
					fontFamily: "微软雅黑"
				}
			},
			data:xData
		}],
		yAxis: [{
			type: 'value',
			splitLine: {
				lineStyle: {
					color: "#FFA88D"
				}
			},
			axisLine: {
				show: true,
				lineStyle: {
					color: "#fff",
					width: 0
				},
			rotate:15,
			interval:0
			},
			axisTick: {
				show: false
			},
			axisLabel: {
				textStyle: {
					color: "#fff"
				}
			}
		}],
		series: [{
			name: 'fdfdfd',
			type: 'line',
			smooth: true,
			symbolSize: 5,
			itemStyle: {
				normal: {
					areaStyle: {
						type: 'default'
					},
				}
			},
			data: yData
		}]
	};
	 lineChart.setOption(lineOption);
}




