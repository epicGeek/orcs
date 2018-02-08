/**
 * 基站评分 明细
 */
//多个小区名称
var cell_name = [];
//多个小区ID
var cell_id = [];
//X轴对应每个KPI的评分
var kpiData_x = [];
//对应每个小区所有的KPI总得分
var cellSum =[];

var datas = [];

var kpiMap = [];//kpi名称 

kendo.culture("zh-CN");

$(function() {
	
	kendo.culture("zh-CN");
	initKpiMap();
	$.ajax({
		dataType : 'json',
		type : "GET",
		url : "kpiScoreNeCode/search",
		data:{"neCode":$('#neCode').val(),"totalScore":$('#totalScore').val(),
				"cycleDate":$('#cycleDate').val(),"cycleHour":$('#cycleHour').val()},
		success : function(data) {
			$.each(data, function(index,item){
				kendo.stringify(item);
				cell_name[index] = item.cell_name_cn;
				cell_id[index] = item.cell_id;
				kpiData_x[index] = [];
				for (var i = 0; i < kpiMap.length; i++) {
					if(kpiMap[i].id !='alarm_score'){
						var KPI_ID_text = kpiMap[i].id.toLowerCase()+"_score";
						kpiData_x[index].push(item[KPI_ID_text]);
					}
					
				}
				
				kpiData_x[index].push(item.alarm_score);
				//kpiData_x[index] = [item.kpi_1,item.kpi_2,item.kpi_3,item.kpi_4,item.kpi_5,item.kpi_6,item.kpi_7,item.kpi_8,item.kpi_9,item.kpi_10,item.alarm_score];
				var sumCount = parseInt(item.total_score);
				cellSum[index] = sumCount;
			});
			detailsList(data);
		}
	});
	
	//a页面传过来的参数
	//获得参数
	var today = new Date();
	var neId = getQueryString("neId");
	var startCycleHour = getQueryString("startCycleHour");
	var endCycleHour = getQueryString("endCycleHour");
	var cityCode = getQueryString("cityCode");
	var areaCode = getQueryString("areaCode");
	var grade = parseInt(getQueryString("grade"));
	var cycleDate = getQueryString("cycleDate");
	var page = getQueryString("page");
	var type = getQueryString("type");
	var bsValue = getQueryString("bsValue");
	parameters = {
		neId: (neId && neId != null && neId !="null")?neId:"",
		startCycleHour: (startCycleHour && startCycleHour != null && startCycleHour !="null")?startCycleHour:"",
		endCycleHour: endCycleHour &&endCycleHour!=null && endCycleHour !="null"?endCycleHour:"",
		cityCode: (cityCode && cityCode!=null && cityCode !="null")?cityCode:"",
		areaCode: (areaCode && areaCode!=null && areaCode !="null")?areaCode:"",
		grade: (grade && grade!=null && grade !="null")?grade:"",
		cycleDate: (cycleDate && cycleDate!=null && cycleDate !="null")?cycleDate:today,
		bsValue:bsValue?bsValue:"",		
		type:type?type:"",		
		page: page?page:1
	}
	
	//点击返回按钮事件
	$("#returnBtn").click(function(){
		
		var reurl = "bsCurrentScore?&neId="+parameters.neId+"&startCycleHour="+parameters.startCycleHour
					+"&endCycleHour="+parameters.endCycleHour+"&cityCode="+parameters.cityCode
					+"&areaCode="+parameters.areaCode+"&grade="+parameters.grade
					+"&neId="+parameters.neId+"&page="+parameters.page;
		//"&cycleDate="+parameters.cycleDate
		
		if(parameters.type==2){
			reurl+="&type=2&bsValue=1"
		}else if(parameters.type==1){
			reurl+="&type=1&bsValue=1"
		}else if(parameters.type==3){
			reurl+="&type=3&bsValue=1"
		}
		window.location.href=reurl;

	});			
	
});

function initKpiMap() {
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "kpiIndex/getKpiRule",
		"success" : function(data) {
			//console.log(data);
			$.each(data, function(index, item) {
				kpiMap.push({id:item.KPI_ID,name:item.KPI_NAME});
				
			});
			kpiMap.push({id:"alarm_score",name:"基站告警"});

		}
	});
}
//绘制图表
function chartData(){
	//多个折线图显示
	$.each(cell_id, function(index){
		var id = "lineChart"+index;
		$("#context").append("<div id='"+id+"' style='height: 250px;'></div>");
		var lineChart = echarts.init(document.getElementById(id));
		var lineOption = {
				title: {
					text:'小区ID:'+cell_id[index]+'， 小区名称：'+cell_name[index]+'， 小区得分：'+cellSum[index],
					subtext: '',
					x: "center",
					backgroundColor: "rgba(0,0,0,.1)",
					borderColor:"rgba(0,0,0,.4)",
					borderWidth:0,
					textStyle:{
						color: "#eee",
						fontSize:14,
						fontFamily:"微软雅黑"
					}
				},
				tooltip: {
					trigger: 'axis',
					formatter:  function(arr) {
						var tip = "指标："+ arr[0].name.name + "<br />";
						tip +=" 小区得分："+arr[0].value;
						return tip;
					}
				},
				legend: {
					data: ['指标'],
					show:false,
					textStyle:{
						color: "#fff"
					}
				},
				toolbox: {
					show: false
				},
				color:["#fff"],
				grid:{
					borderColor:"#FFA88D"
				},
				xAxis: [{
					type: 'category',
					boundaryGap: false,
					splitLine:{
						lineStyle:{
							color: "#FFA88D"
						}
					},
					axisLine: {
						show: false
					},
					axisTick: {
						show: false
					},
					axisLabel: {
						clickable:true,
						formatter : function(value) {
							return  value.name;
						},
						textStyle: {
							color: "#fff",
							fontFamily:"微软雅黑"
						},
						rotate: 15
						
					},
					data: kpiMap  //kpi名称
				}],
				yAxis: [{
					name:"小区得分",
					type: 'value',
					splitNumber:10,
					min: -1,
					max: 100,
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
					name: '指标',
					type: 'line',
					smooth: true,
					symbolSize: 3,
					clickable:false,
					itemStyle: {
						normal: {
							areaStyle: {
								type: 'default'
							},
							
						}
					},
					data: kpiData_x[index]  //分数
				}, ]
		};
		lineChart.setOption(lineOption);
		
		/*var cycleStr = $('#cycleDate').val()+" "+$('#cycleHour').val()+":00:00";
		var startTime=	cycleStr.split(",")[1];
		var cycle  =$('#cycleDate').val();
		var date = cycle.split(",")[0];
		//console.log(startTime);
		var str =  startTime.split(' ')[0].split('-');
		var dateTime = new Date(str[0],str[1]-1,str[2],24,00,00);
		var d = new Date(dateTime);
		var m='';
		var day='';
		if((d.getMonth() + 1)>9){
			m = d.getMonth()+1;
		}else{
			m='0'+(d.getMonth() + 1)
		}
		if(d.getDate()>9){
			day = d.getDate();
		}else{
			day = '0'+d.getDate();
		}
		var endTime = d.getFullYear() + '-' + m + '-' + day + ' ' + d.getHours() + '0:' + d.getMinutes() + '0:' + d.getSeconds()+'0'; */
			//console.log(endTime)
		//console.log(parameters.startCycleHour);
		
		lineChart.on("click",function(e){
			/*
			 *新增需求2016-04-26：明细中点击相应指标跳转到指标实际值页面（kpi指标界面 带上小区ID、小区名称、时间)
			 *如果是基站告警就跳转到基站告警界面（由于告警查询界面 (record表) 没有小区、小区id，并且时间是精确到秒的，
			 *			点击的这个界面没有精确到秒的时间故带上基站id、yyyy-MM-dd+HH:mm:ss的时间格式跳转）
			 */
			//点击基站告警则跳转到告警查询界面
			//console.log(e); 
			if(e.name=='基站告警'){
				window.location.href="alarmQuery?neCode="+$('#neCode').val()+"&startDate="+parameters.startCycleHour+"&endDate="+parameters.endCycleHour+"&type=1";
			}else{
				var name = encodeURI(encodeURI(e.name));  //传递中文处理乱码问题
				var cellName = encodeURI(encodeURI(cell_name[index]));
				window.location.href="kpidataQuery?cellId="+cell_id[index]+"&cell_name="+cellName+"&startCycle="+parameters.startCycleHour+"&endCycle="+parameters.endCycleHour+"&kpiName="+name+"&type=1";
				
			}
		});
		
		$("#context").append('<HR style="FILTER: alpha(opacity=100,finishopacity=0,style=3)"  width="100%" color=#987cb9 SIZE=3>');
	});
}

function detailsList(dataSource){
	
	
	$("#gridList").kendoGrid({
		
		dataSource: {data:dataSource},
		reorderable: true,
		resizable: true,
		//sortable: true,
		//columnMenu: true,
		//pageable: true,
		columns: [
		          
		          { field: "area_name", title:"地市名称",width: 70},
		          { field: "city_name", title:"区县名称",width: 70},
		          { field: "ne_code", title:"基站ID",width: 70},
		          { field: "ne_name_cn", title:"基站名称",width: 120},		          
		          //{ field: "total_score", title:"分数",width: 50,},
		          { field: "cell_id", title:"小区ID",width: 60},
		          { field: "cell_name_cn", title:"小区名称",width: 130},		          
		         { field: "cycle_date", title:"日期",width: 100},
		          { field: "cycle_hour", title:"小时",width: 70},
		        /*  { field: "cycle_date", title:"时间",width: 130,hidden: true,template: 
		        	  function(dataItem) {
	        		     return dataItem.cycle_date+" "+dataItem.cycle_hour+":00:00";
		              }
	              },*/
		          { field: "total_score", title:"基站评分",width: 70},
		          
		       ]
		          
	}).data("kendoGrid");
	
	chartData();
	
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
