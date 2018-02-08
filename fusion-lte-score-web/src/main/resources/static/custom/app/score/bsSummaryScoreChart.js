/**
 * 基站汇总指标图形
 * 
 * */ 
kendo.culture("zh-CN");
var searchParams = {neCode:'',sumStartDate:'',sumEndDate:'',tableName:''};
var parameters;
function bsSumDataList(){
	
	$.ajax({
		type: "GET",
		url: "sumScoreChart/search",
		data:searchParams,
		success : function(data) {
			var score_y = [];
			var time_x = [];
			
			$.each(data, function(index,item){
				if(searchParams.tableName=='bts_score_day'){
					
				    time_x[index] = item.cycle_date;  //时间
					score_y[index] = item.total_score; //分数
					/*var date = new Date();
					var startDate = new Date(date.getFullYear(),date.getMonth(),(date.getDate()-20));
					 $("#sumStartDate").kendoDatePicker({
							value:startDate,
							format: "yyyy-MM-dd"
					});
					 $("#sumEndDate").kendoDatePicker({
						 value:new Date(),
						 format:"yyyy-MM-dd"
					 });*/
					
				}else if(searchParams.tableName=='bts_score_week'){
					
					var x_date= item.cycle_week_first+" 第"+item.cycle_week+"周"; //如：2015-10-25 第44周
					time_x[index] = x_date;
					score_y[index] = item.total_score;
					
				}else if(searchParams.tableName=='bts_score_month'){
					
					var x_date = item.cycle_year+"-"+item.cycle_month
					time_x[index] = x_date;     //如：2015-09
					score_y[index] = item.total_score; 
				}
			});
				
			var lineChart = echarts.init(document.getElementById('lineChart'));
			var lineOption = {
					title: {
						text: '基站ID：'+$('#neCode').val()+'， 基站名称：'+$('#neName').val(),
						subtext: '',
						x: "center",
						backgroundColor: "rgba(0,0,0,.1)",
						borderColor:"rgba(0,0,0,.4)",
						borderWidth:0,
						textStyle:{color: "#eee",fontSize:14,fontFamily:"微软雅黑"}
					},
					tooltip: {trigger: 'axis',formatter: "时间：{b}<br />分数：{c}"},
					legend: {
						data: ['指标'],
						show:false,
						textStyle:{color: "#fff"}
					},
					toolbox: {show: false},
					color:["#fff"],
					grid:{borderColor:"#FFA88D"},
					xAxis: [{
						type: 'category',
						boundaryGap: false,
						splitLine:{lineStyle:{color: "#FFA88D"}},
						axisLine: {show: false},
						axisTick: {show: false},
						axisLabel: {textStyle: {color: "#fff",fontFamily:"微软雅黑"},rotate: 15},
						data: time_x
					}],
					yAxis: [{
						name:"分数",
						type: 'value',
						splitLine:{lineStyle:{color: "#FFA88D"}},
						axisLine: {show: true,lineStyle:{color:"#fff",width: 0}},
						axisTick: {show: false},
						axisLabel: {textStyle: {color: "#fff"}}
					}],
					series: [{
						name: '时间',
						type: 'line',
						smooth: true,
						symbolSize: 5,
						itemStyle: {normal: {areaStyle: {type: 'default'},}},
						data:score_y
					},]
			};
			lineChart.setOption(lineOption);
		}
	
   });
}

$(function() {
	
	kendo.culture("zh-CN");
	//a页面传过来的参数
	var today = new Date();
	var neId = getQueryString("neId");
	var startCycleDate = getQueryString("startCycleDate");
	var endCycleDate = getQueryString("endCycleDate");
	var cityCode = getQueryString("cityCode");
	var areaCode = getQueryString("areaCode");
	var grade = getQueryString("grade");
	var tableName = getQueryString("tableName");
	var page = getQueryString("page");
	var types=getQueryString("types");
	parameters = {
			neId: (neId && neId!=null && neId !="null")?neId:"",
			startCycleDate: (startCycleDate && startCycleDate!="null" && startCycleDate!=null)?startCycleDate: new Date(today.getFullYear(),today.getMonth(),today.getDate()-1),
			endCycleDate: endCycleDate &&endCycleDate!=null && endCycleDate !="null"?endCycleDate: today,
			cityCode: (cityCode && cityCode!=null && cityCode !="null")?cityCode:"",
			areaCode: (areaCode && areaCode!=null && areaCode !="null")?areaCode:"",
			grade: (grade && grade!=null && grade !="null")?grade:"",
			tableName: (tableName && tableName!=null && tableName !="null")?tableName:"bts_score_day",
			types:types?types:"",		
			page: page?page:1
		}
	searchParams.neCode = $('#neCode').val();
	searchParams.sumStartDate = $('#sumStartDate').val();
	searchParams.sumEndDate = $('#sumEndDate').val();
	searchParams.tableName = $('#tableName').val();
	
	bsSumDataList();
	
	//查询条件
	 $("#sumStartDate").kendoDatePicker({
		format: "yyyy-MM-dd"
	});
	$("#sumEndDate").kendoDatePicker({
		format: "yyyy-MM-dd"
	});
	//$("#sumStartDate").attr("disabled",true);
	//$("#sumEndDate").attr("disabled",true);
	
	 //条件查询按钮
	$('#searchBtn').on('click', function(){
		 searchParams.sumStartDate = $("#sumStartDate").val();
		 searchParams.sumEndDate = $("#sumEndDate").val();
		 bsSumDataList();
	});
	
	// 重置
	$('#resetBtn').click(function(){
		$('#sumStartDate').val("");
		$('#sumEndDate').val("");
		searchParams.sumStartDate="";
		searchParams.sumEndDate="";
		bsSumDataList();
	});
	
	$("#returnBtn").click(function(){
		window.location.href = "bsSummaryScore?&neId="+parameters.neId 
							+"&startCycleDate="+parameters.startCycleDate
							+"&endCycleDate="+parameters.endCycleDate
							+"&cityCode="+parameters.cityCode
							+"&areaCode="+parameters.areaCode
							+"&grade="+parameters.grade
							+"&tableName="+parameters.tableName
							+"&neCode="+parameters.neId
							+"&page="+parameters.page
							+"&types="+parameters.types;
	});			
	
});

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

