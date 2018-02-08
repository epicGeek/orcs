/**
 * 地市 雷达图
 */
var tables_bts = {1:"bts_score_hour",2: "bts_score_day",3:"bts_score_week",4:"bts_score_month"};
var scoreData =  [{ text: "小时", value: "1" },{ text: "日", value: "2" },{ text: "周", value: "3" },{ text: "月", value: "4" }];
var areaCode;
var kpiMap=[];
var parameters;
var radarChart;

//获得参数
var areaCode = getQueryString("areaCode");
var cityCode = getQueryString("cityCode");
var startDate = getQueryString("startDate");
var endDate = getQueryString("endDate");
var areaName = getQueryString("areaName");
//var tjType = (getQueryString("tjType")==null || getQueryString("tjType")=="null")?2:getQueryString("tjType");
var tjType = getQueryString("tjType");
areaName = decodeURI(areaName); //解码

$(function() {
	
	kendo.culture("zh-CN");
	var today = new Date();
	parameters={
			cityCode: (cityCode && cityCode!=null && cityCode !="null")?cityCode:"",
			areaCode: (areaCode && areaCode!=null && areaCode !="null")?areaCode:"",
			startDate: (startDate && startDate!=null && startDate !="null")?startDate:new Date(today.getFullYear(), today.getMonth(),today.getDate(),today.getHours-20).Format("yyyy-MM-dd hh:00:00"),
			endDate: (endDate && endDate!=null && endDate !="null")?endDate:new Date(today).Format("yyyy-MM-dd hh:00:00"),
			areaName:(areaName && areaName != null && areaName != "null")?areaName:"",
			tableName:"bts_score_hour",dataType:'1',
	}
	
	$('#name').text(areaName+" - 雷达图");
	
	//统计方式
	initScoreType_radar(scoreData,tjType);
   // var index = tjType==1?1:tjType;
    parameters.tableName = tables_bts[tjType];
    
	//时间格式化
	//formatDate(tjType,parameters,false);
	formatDate_radar(tjType,parameters,false);
	
	$("#scoreType").data("kendoDropDownList").value(tjType);
	
	indexName();
	
	initScoreAreaDataSource();
	
	
	$("#searchBtn").click(function(){
		var type = $('#scoreType').val();
		parameters.tableName =tables_bts[type];
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
			$.each(data, function(tjType, item) {
				kpiMap.push({id:item.KPI_ID+'_score',name:item.KPI_NAME});
			});
			kpiMap.push({id:'alarm_score',name:'基站告警'});
		//	kpiMap.push({id:'out_of_score',name:'退服'});
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
			
			if(data.length>0 && data[0]!=null){
				var xData = [];
				var yData = [];
				
				$.each(kpiMap, function(tjType, item) {
					var score = data[0][item.id];
					var name = item.name;
					xData.push({text:name});
					yData.push(score);
				});
				 
			  var a = Math.max.apply(Math, yData);
			  //var m = Math.floor(a)+1; //取整
			  var b = Math.min.apply(Math, yData);
			  var s = Math.floor(b)-1;
			  
			  $.each(xData,function(tjType,item){
				  item.max=a;
				  item.min=s;
			  });
				
				setOptions(xData,yData);
			}else{
				toCharts({});
			}
			
			//柱状图加折线图
			radarChart.on("click", function(e) {
				var kpiName=e.name;
				kpiName =encodeURI(encodeURI(kpiName)); //编码
				var areaName = parameters.areaName;
				areaName = encodeURI(encodeURI(areaName));
				var value = $('#scoreType').val();
				if(parameters.cityCode){
						window.location.href = "bsCurrentScore?cityCode="+parameters.cityCode+"&areaCode="+parameters.areaCode
								+"&startDate="+parameters.startDate+"&endDate="+parameters.endDate+"&type=2"+"&grade=1&tjType="+value+"&kpiName="+kpiName;
				}else{
					window.location.href = "areaChart?&areaCode="+parameters.areaCode+"&areaName="+areaName
					          +"&startDate="+parameters.startDate+"&endDate="+parameters.endDate+"&type=1&tjType="+value+"&kpiName="+kpiName;
				}
			});
		}
	});
}

//时间格式化
function formatDate_radar(type,parameters,init){
	$("#startDate").css("width","200px");
	$("#endDate").css("width","200px");
	var today = new Date();
	var format = [];
	if(type==1){
		format[0]= "yyyy-MM-dd HH:00:00";
		format[1]=(parameters.startDate!=null && parameters.startDate !="")?parameters.startDate:new Date(today.getFullYear(), today.getMonth(),today.getDate(),today.getHours()-20).Format("yyyy-MM-dd hh:mm:ss");
		format[2]=(parameters.endDate!=null && parameters.endDate !="")?parameters.endDate:today;
	}else if(type==2){
		format[0]= "yyyy-MM-dd";
		format[1]=(parameters.startDate!=null && parameters.startDate !="")?parameters.startDate:new Date(today.getFullYear(), today.getMonth(),today.getDate()-30).Format("yyyy-MM-dd");
		format[2]=(parameters.endDate!=null && parameters.endDate !="")?parameters.endDate:today;
	}else if(type==3){
		if(parameters.startDate){
			if(parameters.startDate.length>10){
				parameters.startDate = "";
				parameters.endDate  = "";
			}
		}
		var week = getYearWeek(today)-1;
		format[0]= "yyyy-"+week;
		format[1]=(parameters.startDate!=null && parameters.startDate !="")?parameters.startDate:new Date(today).Format("yyyy-"+week);
		format[2]=(parameters.endDate!=null && parameters.endDate !="")?parameters.endDate:new Date(today).Format("yyyy-"+week);
	}else if(type==4){
		format[0]= "yyyy-MM";
		format[1]=(parameters.startDate!=null && parameters.startDate !="")?parameters.startDate:new Date(today.getFullYear(), today.getMonth()-1).Format("yyyy-MM");
		format[2]=(parameters.endDate!=null && parameters.endDate !="")?parameters.endDate:today;
	}
	//查询条件
	var startDate = $("#startDate").data("dateTimePicker");
	var endDate = $("#endDate").data("dateTimePicker");
	if(!init){
		$("#startDate").kendoDateTimePicker({
			format: format[0],value:format[1]
		});
		$("#endDate").kendoDateTimePicker({
			format: format[0],value: format[2]
		});
	} else {
		startDate.setOptions({
			format: format[0],
			value: format[1]
		});
		endDate.setOptions({
			format: format[0],
			value:format[2]
		});
	}
}

//统计方式
function initScoreType_radar(data,index){
	//下拉
	$("#scoreType").kendoDropDownList({
		 	//autoBind: false,
			dataSource:  data,
			dataTextField: "text",
	        dataValueField: "value",
	        //index:0, // 当前默认选中项，索引从0开始。
	        change: function(e){
	        	 var value = this.value();
	        	 formatDate_radar(value,false);
	        	//isHide(value);
       
            }
		});
	$("#scoreType").data("kendoDropDownList").value(index);
	
}