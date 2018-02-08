/**
 * 公共折线图
 */
var day = 20;//默认20天折线图
var parameters;
var scoreData =  [{ text: "小时", value: "1" },{ text: "日", value: "2" },{ text: "周", value: "3" },{ text: "月", value: "4" }];
//获得参数
var areaCode = getQueryString("areaCode");
var startDate = getQueryString("startDate");
var endDate = getQueryString("endDate");
var areaName = getQueryString("areaName");
var cityName = getQueryString("cityName");
var cityCode = getQueryString("cityCode");
//var tjType = (getQueryString("tjType")==null || getQueryString("tjType")=="null")?2:getQueryString("tjType");
var tjType=getQueryString("tjType");
areaName = decodeURI(areaName); //解码
var today = new Date();
var charts;

$(function() {
	
	kendo.culture("zh-CN");
	
	var start = new Date(startDate.replace(/-/g,   "/"));
	var end = new Date(endDate.replace(/-/g,   "/"));
	var date3=end.getTime()-start.getTime();  //时间差的毫秒数
	//计算出相差天数
	var days=Math.floor(date3/(24*3600*1000));
	if(tjType == 2){
		if(days<day){
			startDate = new Date(end.getFullYear(), end.getMonth(),end.getDate()-day).Format("yyyy-MM-dd");
		}
	}
	parameters={
		cityCode: (cityCode && cityCode!=null && cityCode !="null")?cityCode:"",
		areaCode: (areaCode && areaCode!=null && areaCode !="null")?areaCode:"",
		startDate: (startDate && startDate!=null && startDate !="null")?startDate:new Date(today.getFullYear(), today.getMonth(),today.getDate(),today.getHours()-24).Format("yyyy-MM-dd hh:00:00"),
		endDate: (endDate && endDate!=null && endDate !="null")?endDate:new Date(today).Format("yyyy-MM-dd hh:00:00"),
		areaName:(areaName && areaName != null && areaName != "null")?areaName:"",
		cityName:(cityName && cityName != null && cityName != "null")?cityName:"",
		tableName:"area_score_hour",
	}
	
	$('#name').text(parameters.areaName+" -  折线图");
	
	//统计方式
    initScoreType_zxChart(scoreData,tjType);
    //var index = tjType==1?1:tjType;
    
    parameters.tableName = tables[tjType];
    
	//时间格式化
	formatDate_zxChart(tjType,parameters,false);
	$("#scoreType").data("kendoDropDownList").value(tjType);
	$("#startDate").css("width","150px");
	$("#endDate").css("width","150px");
	
	initAreaDataSource();
	
	$("#searchBtn").click(function(){
		var type = $('#scoreType').val();
		parameters.tableName = tables[type];
		parameters.startDate=$("#startDate").val();
		parameters.endDate=$("#endDate").val();
		
		initAreaDataSource();
	})
	
	// 折线图--->【雷达图】按钮
	$("#queryBtn").on("click",function(){
		var barName= parameters.areaName;
		barName = encodeURI(barName); //编码
		barName = encodeURI(barName); //编码
		window.location.href = "barChart?areaCode="+parameters.areaCode+"&areaName="+barName+"&cityCode="+parameters.cityCode
						+"&startDate="+parameters.startDate+"&endDate="+parameters.endDate+"&tjType="+$("#scoreType").val();
	});
});


function initAreaDataSource() {
	$.ajax({
		dataType : 'json',
	    type : "POST",
		url : "dashboard/searchAreaGrade",
		contentType : "application/json;charset=utf-8",
		data:kendo.stringify(parameters),
		success : function(data) {
			if(data.length==0){
				showNotify("无数据!","warning");
				tochart({});
				return;
			}
			var x_data = [];
			var level1=[],level2=[],level3=[],level4=[],level5=[];
			var y_data = {};
			var value = $('#scoreType').val();
			$.each(data, function(tjType, item) {
				//新增需求 增加小时折线图
				if(value==1){
					var s = getDate(item.cycle);
					x_data.push(s);
				}else
				if(value==2){
					x_data.push(item.cycle_date);
				}else if(value==3){
					x_data.push(today.getFullYear()+"-"+item['cycle_week']+"周");  
				}else if(value==4){
					x_data.push(today.getFullYear()+"-0"+item['cycle_month']+"月");  
				}
				
				level1.push(item.grade1);
				level2.push(item.grade2);
				level3.push(item.grade3);
				level4.push(item.grade4);
				level5.push(item.grade5);
			});
			
			y_data.level1=level1;
			y_data.level2=level2;
			y_data.level3=level3;
			y_data.level4=level4;
			y_data.level5=level5;
			viewChart(x_data,y_data);
		},	
		fail:function(error){
			showNotify("失败","warning");
		}
	});
	
}


//图形绘制
function tochart(options){
	charts = echarts.init(document.getElementById('lineChart'));
	charts.setOption(options);
}

function viewChart(xData,yData){
	
	var end = xData.length;
	var zoom;
	if(end<=15){ 
		end=100;
	}else if(15<end<=30){
		end=50;
	}else{ end=10 }
	if(xData.length>=16){
		zoom={
			show: true,
			realtime: true,
			showDetai:true,
			start:0,
			end:end,
			height:40,
			handleColor:"#009",
			handleSize:10
		}
	}else{
		zoom ={show: false,
			   realtime: false,
			   showDetai:false
		};
	}
	var lineOption = {
			title: {
				text: '基站占比',
				textStyle:{
					color: "#FF7F50",
					fontSize:15
				}
			},
			tooltip: {
				trigger: 'axis',
			//	formatter: "时间：{b}<br />一级：{c}%"
			},
			legend: {
				 data:['一级','二级','三级','四级','五级']
			},
			grid: {
				left: '3%',
				right: '4%',
				bottom: '3%',
				containLabel: true,
				y2:110,
				x:95
			},
			toolbox: {
				 feature: {
			            saveAsImage: {}
			        }
			},
			color : [ "#FF0000", '#FFA500', "#FFFF00", '#0000FF', '#008000'],
			xAxis: [{
				type: 'category',
				boundaryGap: false,
				axisLabel: {
					textStyle: {
						color: "#000000",
						fontFamily:"微软雅黑"
					},
					rotate: 15
				},
				data: xData
			}],
			yAxis: [{
				type: 'value',
				axisLabel: {
					formatter:'{value}%'
				},
			}],
			dataZoom: zoom,
			series: [
			         {
			             name:'一级',
			             type:'line',
			             data:yData.level1
			         },
			         {
			             name:'二级',
			             type:'line',
			             data:yData.level2
			         },
			         {
			             name:'三级',
			             type:'line',
			             data:yData.level3
			         },
			         {
			             name:'四级',
			             type:'line',
			             data:yData.level4
			         },
			         {
			             name:'五级',
			             type:'line',
			             data:yData.level5
			         }
			     ]
			
		};
	
	//绘制图形
	tochart(lineOption);
	
	charts.on("click",function(e){
		//需要的对象：  console.log(e);
		var btsUrl= "bsCurrentScore?&areaCode="+parameters.areaCode+"&startDate="
			+parameters.startDate+"&endDate="+parameters.endDate+"&type=2"+"&tjType="+$("#scoreType").val();
		
		if(parameters.cityCode !="" && parameters.cityCode !=null){
			btsUrl +="&cityCode="+parameters.cityCode;
		}
		if(e.seriesName=="一级"){
			window.location.href =btsUrl+"&grade=1";
		}
		if(e.seriesName=="二级"){
			window.location.href =btsUrl+"&grade=2";
		}
		if(e.seriesName=="三级"){
			window.location.href =btsUrl+"&grade=3";
		}
		if(e.seriesName=="四级"){
			window.location.href =btsUrl+"&grade=4";
		}
		if(e.seriesName=="五级"){
			window.location.href =btsUrl+"&grade=5";
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

//时间格式化
function formatDate_zxChart(type,parameters,init){
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
		format[1]=(parameters.startDate!=null && parameters.startDate !="")?parameters.startDate:new Date(today.getFullYear(), today.getMonth(),today.getDate()-20).Format("yyyy-MM-dd");
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
function initScoreType_zxChart(data,index){
	//下拉
	$("#scoreType").kendoDropDownList({
		 	//autoBind: false,
			dataSource:  data,
			dataTextField: "text",
	        dataValueField: "value",
	        //index:0, // 当前默认选中项，索引从0开始。
	        change: function(e){
	        	 var value = this.value();
	        	formatDate_zxChart(value,false);
	        	//isHide(value);
       
            }
		});
	$("#scoreType").data("kendoDropDownList").value(index);
	
}

