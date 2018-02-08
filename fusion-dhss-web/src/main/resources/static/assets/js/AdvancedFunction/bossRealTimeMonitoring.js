kendo.culture("zh-CN");
$(function() {
	/*当前导航*/
	$("#topNavList .navListWrap:eq(2) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(2) ul li:eq(0)").addClass("active");
	
	
	//查询条件
	$("#startDateTime").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:MM"
	});
	$("#endDateTime").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:MM"
	});
	//柱状图
	var charts = echarts.init(document.getElementById('charts'));
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
					type: ['line', 'bar']
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
			data: ['请求次数', '成功率']
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
			data: ['2015-07-01', '2015-07-02', '2015-07-03', '2015-07-04', '2015-07-05', '2015-07-06', '2015-07-07', '2015-07-08', '2015-07-09', '2015-07-10', '2015-07-11月', '2015-07-12']
		}],
		yAxis: [{
			type: 'value',
			name: '',
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
				formatter: '{value}个'
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
				formatter: '{value}%'
			}
		}],
		series: [
			{
				name: '请求次数',
				type: 'bar',
				barMaxWidth: 35,
				data: [20, 49, 70, 32, 56, 76, 36, 62, 36, 20, 64, 33]
			}, {
				name: '成功率',
				type: 'line',
				yAxisIndex: 1,
				smooth:true,
				symbolSize:4,
				symbol: "emptyCircle",
				data: [2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2]
			}
		]
	};
	charts.setOption(options);
});