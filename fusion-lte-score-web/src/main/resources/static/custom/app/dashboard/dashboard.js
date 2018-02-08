var scoreData = [{ text: "小时", value: "1" },{ text: "日", value: "2" },{ text: "周", value: "3" },{ text: "月", value: "4" }];
var breakReasionDataSource = [], kpiMap = [];
var clickChart;
var areaCode = getQueryString("areaCode");
var areaName = getQueryString("areaName");
var cityCode = getQueryString("cityCode");
var startDate = getQueryString("startDate");
var endDate = getQueryString("endDate");
var type = getQueryString("type");
var tjType = (getQueryString("tjType")==null || getQueryString("tjType")=="null")?1:getQueryString("tjType");
//var tjType = getQueryString("tjType");
var parameters = {
	startDate: (startDate && startDate!=null && startDate !="null")?startDate:"",
	endDate: (endDate && endDate!=null && endDate !="null")?endDate:"",
	cityCode: (cityCode && cityCode!=null && cityCode !="null")?cityCode:"",
	areaCode: (areaCode && areaCode!=null && areaCode !="null")?areaCode:"",
	tableName : 'area_score_hour',
	type:type==null?"":type
};
//全省雷达图参数对象
var today = new Date();
var radar = {tableName: "bts_score_day",dataType:'0',
		startDate:new Date(today.getFullYear(), today.getMonth(),today.getDate()-1).Format("yyyy-MM-dd"),
		endDate:today.Format("yyyy-MM-dd")};

$(function() {

	kendo.culture("zh-CN");
	
	initScoreType(scoreData,tjType); //初始化时间
	
	formatDate(tjType,parameters,false); //时间
	
	if(tjType==1){
		$("#startDate").val("");
		$("#endDate").val("");
	}

	//地市模块
	$("#searchBtn-area").click(function() {
		var type = $('#scoreType').val();
		parameters.tableName = tables[type];
		if(decodeURI(areaName) == null || decodeURI(areaName) =="null"){
			parameters.startDate = $('#startDate').val();
			parameters.endDate = $('#endDate').val();
		}else{
			parameters.startDate = $('#startCycle').val();
			parameters.endDate = $('#endCycle').val();
			
		}
		initScoreAreaDataSource();
	})

	//雷达图
	$("#searchBtn_radar").click(function(){
		radar.startDate = $('#startTime').val();
		radar.endDate = $('#endTime').val();
		radarDataSource();
	}) 
	$("#startTime").kendoDateTimePicker({
		format:"yyyy-MM-dd",
		value:radar.startDate
	})
	$("#endTime").kendoDateTimePicker({
		format:"yyyy-MM-dd",
		value:radar.endDate
	})
	
	$("#startCycle").kendoDateTimePicker({
		format : "yyyy-MM-dd HH:00:00",
		value : parameters.startDate
	}).data("kendoDatePicker");
	
	$("#endCycle").kendoDateTimePicker({
		format : "yyyy-MM-dd HH:00:00",
		value : parameters.endDate
	}).data("kendoDatePicker");
	
	$("#searchBtn-gz").click(function() {
		initBreakReasionDataSource();
	})
	
	initKpiMap();
	
	if(parameters.areaCode){
		$('#name').text(decodeURI(areaName)+"下 - 区县基站个数及一级占比图");
	}else{
		// 故障饼图 查询条件
		initAreaList();
		//饼状图
		initBreakReasionDataSource();
		//最差区县
		initWorstAreaDataSource();
		
		radarDataSource();
	}
	//地市图
	initScoreAreaDataSource();
	
});

//图形绘制
function tochart(options,id) {
	clickChart = echarts.init(document.getElementById(id));
	clickChart.setOption(options);
}

// 地市柱状图    红橙黄蓝绿
function setOption(xData, series) {
	var barOption = {
		title : {
			text : '',
			x : 'left'
		},
		grid : {
			x : 50,
			y : 40,
			x2 : 55,
			y2 : 40
		},
		tooltip : {
			trigger : 'axis',
			formatter : function(arr) {
				var tip = arr[0].name.area_cn + "<br />";
				for (var i = 0; i < arr.length; i++) {
					tip += arr[i].seriesName + "：" + (arr[i].value);
					if(arr[i].seriesName.indexOf("占比") >0){
						tip+='%';
					}
					tip+="<br />";
				}
				return tip;
			}
		},
		legend : {
			data : [ '一级', '二级', '三级', '四级', '五级', '一级基站占比' ],
			show : true
		},
		toolbox : {
			show : false
		},
		axis : {
			axisLine : {
				show : false
			}
		},
		color : [ "#FF0000", '#FFA500', "#FFFF00", '#0000FF', '#008000','#000099' ],
		calculable : true,
		xAxis : [ {
			type : 'category',
			axisLine : {
				show : false
			},
			axisTick : {
				show : false
			},
			splitLine : {
				show:false,
				lineStyle : {
					color : [ '#D9D9D9' ]
				}
			},
			axisLabel : {
				formatter : function(value) {
					return value.area_cn
				}
			},
			data : xData
		} ],
		yAxis : [ {
			name : "",
			type : 'value',
			// max:100,
			axisLine : {
				show : false
			},
			axisTick : {
				show : false
			},
			splitLine : {
				show:false,
				lineStyle : {
					color : [ '#000' ]
				}
			},
			axisLabel : {
				formatter : '{value}'
			}
		},{
			name:"",
			type:"value",
			splitLine:{
				lineStyle:{
					color:["#ddd"]
				}
			},
			axisLabel:{
				formatter: "{value} %"
			}
		} ],
		series : series
	};
	tochart(barOption,"barChart");
}

/**
 * 初始化kpi
 */
function initKpiMap() {
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "kpiIndex/getKpiRule",
		"success" : function(data) {
			$.each(data, function(index, item) {
				kpiMap.push(item.KPI_NAME);
				breakReasionDataSource.push({
					id : item.KPI_ID,
					name : item.KPI_NAME
				});
				
			});
			breakReasionDataSource.push({
				id : 'alarm',
				name : '基站告警'
			});
	/*		breakReasionDataSource.push({
				id : 'out_of',
				name : '退服'
			});*/
		}
	});
}

function piechart(yData){
	
	var pieOption = {
			title : {
				text : ''
			},
			tooltip : {
				trigger : 'item',
				formatter : "{b} : {c}%"
			},
			color : [ "#417AB1", "#4982B9", "#4E82BF", "#528BC2", "#5893CA",
			          "#5F9dCF", "#629CD3", "#669aD3", "#6BA4DB", "#72ADE4",
			          "#77ADED" ],
			          legend : {
			        	  show : false,
			        	  data : kpiMap
			          },
			          toolbox : {
			        	  show : false
			          },
			          series : [ {
			        	  type : 'pie',
			        	  minAngle : 10,
			        	  radius : '65%',
			        	  // center: ['50%', '60%'],
			        	  itemStyle : {
			        		  normal : {
			        			  label : {
			        				  show : true,
			        				  textStyle : {
			        					  color : "#555"
			        				  },
			        				  formatter : function(e) {
			        					  return e.name + "：" + e.percent + "%";
			        				  }
			        			  },
			        			  labelLine : {
			        				  show : true,
			        				  lineStyle : {
			        					  color : '#ccc'
			        				  }
			        			  },
			        			  borderColor : "#dfdfdf"
			        		  },
			        		  emphasis : {
			        			  label : {
			        				  show : true,
			        				  position : 'center',
			        				  textStyle : {
			        					  fontSize : '16',
			        					  fontWeight : 'bold',
			        					  color : "#333"
			        				  }
			        			  },
			        			  borderColor : "#FD6A5E"
			        		  }
			        	  },
			        	  data : yData
			          } ]
	};
	tochart(pieOption,"pieChart");
}

// 饼状图
function initBreakReasionDataSource() {
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "dashboard/searchCurBreakReasion",
		"data" : {
			"startDate" : $('#startCycle').val(),
			"endDate" : $('#endCycle').val(),
			"areaCode" : $('#areaCode').val()
		},
		"success" : function(data) {
			if (data.length == 0 || data[0] == null) {
				showNotify("无故障原因数据!", "warning");
				return;
			}
			var kpiData = data[0];
			for (var i = 0; i < breakReasionDataSource.length; i++) {
				var KPI_ID_text = breakReasionDataSource[i].id;
				breakReasionDataSource[i].value = kpiData[KPI_ID_text];
			}
			piechart(breakReasionDataSource)
			clickChart.on("click", function(e) {
				var faultName = e.name;
				faultName = encodeURI(faultName);
				faultName = encodeURI(faultName);
				if(e.data.id == 'out_of'){
					e.data.id ="outof";
				}
				window.location.href = "/faultChart?area_code="+ $('#areaCode').val() 
					+ '&startDate='+ $('#startCycle').val()+ '&endDate='+ $('#endCycle').val()
					+ "&kpiName=" + e.data.id+ "&faultName=" + faultName;
			});
		}
	});
}
// 地市模块
function initScoreAreaDataSource() {
	
	$.ajax({
		dataType : 'json',
		type : "POST",
		contentType : "application/json;charset=utf-8",
		url : "dashboard/searchCurAreaScore",
		data : kendo.stringify(parameters),
		"success" : function(data) {
			if (data.length == 0) {
				tochart({},"barChart");
				showNotify("无数据!","warning");
				return;
			}
			var areaCodeData = [], firstLevel = [], secondLevel = [], thirdLevel = [], fourthLevel = [], fifthLevel = [], level = [];
			var code="",name="";
			if(parameters.areaCode){
				code="city_code";
				name="city_name";
			}else{
				code="area_code";
				name="area_name";
			}
			/*var defaultTotal =0;
			if(parameters.startDate==""){
				$.each(data, function(index, item) {
					if (item[code]) {
						defaultTotal+=item.grade1_total;
					}
				});
			}*/
			$.each(data, function(index, item) {
				if (item[code]) {
					areaCodeData.push({'area_code' : item[code],'area_cn' : item[name]});
					firstLevel.push(item.grade1_total);
					secondLevel.push(item.grade2_total);
					thirdLevel.push(item.grade3_total);
					fourthLevel.push(item.grade4_total);
					fifthLevel.push(item.grade5_total);
					level.push(item.grade1);
					/*if(parameters.startDate==""){
						level.push((item.grade1_total/defaultTotal*100).toFixed(2));
					}else{
					}*/
				}
			});
			var ser = [ {
				name : '一级',
				type : 'bar',
				stack : '总量',
				yAxisIndex: 0,
				data : firstLevel
			}, {
				name : '二级',
				type : 'bar',
				stack : '总量',
				yAxisIndex: 0,
				data : secondLevel
			}, {
				name : '三级',
				type : 'bar',
				stack : '总量',
				yAxisIndex: 0,
				data : thirdLevel
			}, {
				name : '四级',
				type : 'bar',
				stack : '总量',
				yAxisIndex: 0,
				data : fourthLevel
			}, {
				name : '五级',
				type : 'bar',
				stack : '总量',
				barWidth : 15,
				yAxisIndex: 0,
				data : fifthLevel
			}, {
				name : '一级基站占比',
				type : 'line',
				yAxisIndex: 1, //指明使用的是哪个坐标轴：0或者1
				data : level
			} ];
			setOption(areaCodeData, ser);
			clickChart.on("click",function(e) {
					var index = e.dataIndex;
					var name = e.name.area_cn;
					name = encodeURI(name);
					name = encodeURI(name);
					var prams= "";
					var sTime =$('#startCycle').val();
					var eTime =$('#endCycle').val();
					if(parameters.areaCode){
						prams ="pieChart?cityCode="+ e.name.area_code+"&areaCode="+parameters.areaCode+"&tjType="+$("#scoreType").val()
						+ "&areaName="+ name+ "&startDate="+ sTime+ "&endDate=" +eTime;
					}else{
						prams ="pieChart?areaCode="+ e.name.area_code+"&tjType="+$("#scoreType").val()
						+ "&areaName="+ name+ "&startDate="+  parameters.startDate + "&endDate=" + parameters.endDate ;
					}
						window.location.href = prams ;
					
				});
		}
	});
}

// 最差区县
function initWorstAreaDataSource() {
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "dashboard/searchCurWorstArea",
		"success" : function(data) {
			var html = "";
			$.each(data, function(index, item) {
				var hour=item.cycle_hour;
				if(hour<10){
					hour="0"+hour;
				}
				var date = item.cycle_date +" "+hour+ ":00:00";
				html += '<tr><td><span class="badge badge-danger">'
					+ item.rank + '</span></td>' + '<td>'
					+ item.area_name + '</td>'
					+ '<td><a href="bsCurrentScore?areaCode='
					+ item.area_code + '&cityCode=' + item.city_code
					+ '&startDate=' + date + '&endDate='
					+ date +'&type=2' + '&grade=1">' + item.city_name
					+ '</a></td>' + '<td>' +item.grade1 + '%'
					+ '</td>' + '<td>' + item.cycle_date + ' '
					+ item.cycle_hour + ':00:00</td>' + '</tr>';
					// +'<div class="progress">'
					// +'<div style="width: '+item.grade1+'%;"
					// class="progress-bar progress-bar-danger">'+
					// +</div>'
					// +'</div>'
			});
			$("#gridList").html(html);
		}
	});
}


// =================雷达图 start========================
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

function radarDataSource(){
	$.ajax({
		dataType : 'json',
		type : "POST",
		contentType : "application/json;charset=utf-8",
		url : "dashboard/areaRadar",
		data : kendo.stringify(radar),
		"success" : function(data) {
		   if(data.length>0 && data[0]!=null){
			   var xData = [];
			   var yData = [];

			  $.each(breakReasionDataSource, function(index, item) {
					var score = data[0][item.id+'_score'];
					var name = item.name;
					
					yData.push(score);
					xData.push({text:name+"("+score+")"});
				});

			  var a = Math.max.apply(Math, yData);
			  //var m = Math.floor(a)+1; //取整
			  var b = Math.min.apply(Math, yData);
			  var s = Math.floor(b)-1;
			  $.each(xData,function(index,item){
				  item.max=a;
				  item.min=s;
			  });
			 setOptions(xData,yData);
			 
			}else{
				toCharts({});
			}
		}
	});
}
//================radar end=======================

// ==地市 区县的查询 start=====
function initAreaList() {

	var dtd = $.Deferred(); // 新建一个Deferred对象
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "city/search",
		async:false,
		"success" : function(data) {
			$("#areaCode").kendoDropDownList({
				optionLabel : "--请选择地市--",
				dataTextField : "areaCn",
				dataValueField : "areaCode",
				dataSource : data,
				filter : "contains",
				change : function() {
					var areaCode = $('#areaCode').val();
					getAreaData(areaCode);
				}

			});
			dtd.resolve(); // 改变Deferred对象的执行状态
		}
	});
	return dtd;
}

function getAreaData(cityCode) {
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "area/search",
		data : {
			"cityCode" : cityCode
		},
		"success" : function(data) {
			/*
			 * $("#cityCode").data("kendoDropDownList").setDataSource(data);
			 * $('#cityCode').data("kendoDropDownList").value(parameters.cityCode);
			 */
		}
	});
}


