/**
 * 基站指标图形-默认20个小时
 */
var parameters;
var tjType = (getQueryString("tjType")==null || getQueryString("tjType")=="null")?1:getQueryString("tjType");
var scoreData = [{ text: "小时", value: "1" },{ text: "日", value: "2" },{ text: "周", value: "3" }];
var tableName;
var startDate;
var endDate;
function kpiDataList(){
	$.ajax({
		dataType : 'json',
		type : "POST",
		url : "kpiScoreList/search",
		contentType : "application/json;charset=UTF-8",
		data:kendo.stringify(parameters),
		success : function(data) {
			var kpiScore_y = [];
			var kpiTime_x = [];
			var ne = {};
			
			$.each(data, function(index,item){
				if(index==0){
				  ne.neCode = item.ne_code;
				  ne.neName = item.ne_name_cn;
				}
				if(tableName=='bts_score_week'){
					kpiTime_x[index] = item.cycle_year+"-"+item.cycle_week+"周";
					kpiScore_y[index] = item.total_score;
				}else if(tableName=='bts_score_day'){
					kpiTime_x[index] = item.cycle_date;
					kpiScore_y[index] = item.total_score;
				}else{
					kpiTime_x[index] = getDate(item.cycle);
					kpiScore_y[index] = item.total_score;
				}
				
			});
				
			var end = kpiTime_x.length-2;
			var lineChart = echarts.init(document.getElementById('lineChart'));
			var lineOption = {
					title: {
						text: '基站ID:'+ne.neCode+'， 基站名称：'+ne.neName,
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
					toolbox: {show:false},
					color:["#fff"],
					grid:{borderColor:"#FFA88D",y2: 110,x:95},
					xAxis: [{
						type: 'category',
						boundaryGap: false,
						splitLine:{
							lineStyle:{
								color: "#FFA88D"
							}
						},
						axisLine: {show: false},
						axisTick: {show: false},
						axisLabel: {textStyle: {
								color: "#fff",
								fontFamily:"微软雅黑"
							},
							rotate: 15,
							interval:1
						},
						data: kpiTime_x
					}],
					yAxis: [{
						name:"分数",
						type: 'value',
						splitLine:{
							lineStyle:{
								color: "#FFA88D"
							}
						},
						axisLine: {
							show: true,
							lineStyle:{
								color:"#fff",
								width: 0
							}
						},
						axisTick: {show: false},
						axisLabel: {
							textStyle: {
								color: "#fff"
							}
						}
					}],
					dataZoom:{
						show: true,
						realtime: true,
						showDetai:true,
						start:0,
						end:100,
						height:40,
						handleColor:"#009",
						handleSize:10
					},
					series: [{
						name: '时间',
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
						data:kpiScore_y
					}]
			};
			lineChart.setOption(lineOption);
		}
	});
}	

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
	
	kendo.culture("zh-CN");
	
	//a页面传过来的参数
	//获得参数
	var neCode = getQueryString("neCode");
	var startDate=getQueryString("startCycle");
	var endDate=getQueryString("endCycle");
	 tableName=getQueryString("tableName");
	parameters = {
		neCode: (neCode && neCode != null && neCode !="null")?neCode:"",
		startDate: (startDate && startDate != null && startDate !="null")?startDate:startDate,
		endDate: (endDate &&endDate!=null && endDate !="null")?endDate:endDate,
		tableName:(tableName && tableName != null )?tableName:tableName
	}
	
	//统计方式
	//initScoreType(scoreData,tjType);
	//formatDate(tjType,parameters,false); //时间
	// var index = tjType==4?1:tjType;
	 //parameters.tableName = tables[index];
	 
	//查询条件
	$("#cycleStartTime").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:00:00",
		value:parameters.startDate
	});
	$("#cycleEndTime").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:00:00",
		value:parameters.endDate
	});
	
	//图表
	kpiDataList();
	
	//$("#cycleEndTime").attr("disabled",true);
		
	 //条件查询按钮
	$('#searchBtn').on('click', function() {
		
		endDate = $("#cycleEndTime").val();
		startDate = $("#cycleStartTime").val();
		
		if(tableName=='bts_score_week'){
			if(endDate.length>7 || startDate.length>7){
				
				var s = new Date(startDate);
				var s_week = getYearWeek(s);
				var s_year = s.getFullYear();
				var start = s_year+"-"+s_week;
				
				var e = new Date(endDate);
				var e_week = getYearWeek(e);
				var e_year = e.getFullYear();
				var end = e_year+"-"+e_week;
				parameters.startDate =start;
				parameters.endDate=end;
			}else{
				parameters.startDate = startDate;
				parameters.endDate = endDate;
			}
		}else{
			parameters.startDate = startDate;
			parameters.endDate = endDate;
		}
    	kpiDataList();
	});
	
	// 重置
	$('#resetBtn').click(function(){
		$('#cycleEndTime').data("kendoDateTimePicker").value(today);
		$('#cycleStartTime').data("kendoDateTimePicker").value(new Date(today.getFullYear(),
				today.getMonth(),today.getDate(),today.getHours()-20));
		kpiDataList();
	});
	
	//点击返回按钮事件
	//$("#returnBtn").click(function(){
	//	window.history.back(-1);
	//	javascript:history.back(-1);
		/*var chartUrl = "bsCurrentScore?&neId="+parameters.neId+"&startCycleHour="+parameters.startCycleHour
							+"&endCycleHour="+parameters.endCycleHour+"&cityCode="+parameters.cityCode
							+"&areaCode="+parameters.areaCode+"&grade="+parameters.grade
							+"&cycleDate="+parameters.cycleDate+"&neId="+parameters.neId
							+"&page="+parameters.page;
		
		if(parameters.type==2){
			chartUrl+="&type=2&bsValue=1"
		}else if(parameters.type==1){
			chartUrl+="&type=1&bsValue=1"
		}else if(parameters.type==3){
			chartUrl+="&type=3&bsValue=1"
		}
		window.location.href=chartUrl;*/
	//});			
	
});


