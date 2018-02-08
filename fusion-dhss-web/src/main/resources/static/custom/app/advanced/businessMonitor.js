var inputNeName = "";
var inputBusiness ="";
var startdatetime = "";
var enddatetime = "";
var period = "MI";//默认分钟

//时间排序
var dateArr = [];
//成功次数
var succData = [];
//成功率
var succRate = [];

//请求总数
var countData = [];

var series = [];

var legend = [];

$(document).ready(function() {
	
	//每15分钟刷新一次
	setInterval(function(){
	   butSearchData();
	},1000*60*15);
	
	kendo.culture("zh-CN");
	
	$("#dhssText").text("全部网元   - 全部业务类型");
	
	//时间初始化
	$("#startdatetime").kendoDateTimePicker({
		value:new Date(),
        format: "yyyy-MM-dd HH:mm:ss",
        parseFormats: ["yyyy-MM-dd", "HH:mm:ss"]
	});
	$("#enddatetime").kendoDateTimePicker({
		value:new Date(),
        format: "yyyy-MM-dd HH:mm:ss",
        parseFormats: ["yyyy-MM-dd", "HH:mm:ss"]
	});
	
	//获取站点
	sitesData();
	//业务类型
	businessType();
	
	//默认查询当前
	butSearchData();
	
	
	//查询按钮触发
	$('#btn-search').on('click', function() {
		inputNeName = $("#inputNeName").val();
		inputBusiness = $("#inputBusiness").val();
		startdatetime = $("#startdatetime").val();
		enddatetime = $("#enddatetime").val();
		period = $("#bossPeriod").val();
		var neName = inputNeName==""?"全部网元类型":neName= $("#inputNeName").data("kendoDropDownList").text();
		var neBusiness =inputBusiness==""?"全部业务类型":inputBusiness;
		$("#dhssText").text(neName+" - "+neBusiness);
		clear();
		//console.log(period+'  '+inputNeName+'    '+inputBusiness);
		butSearchData();
	});
	
	//重置
	$('#btn-reset').on('click',function(){
		reset();
	});
	 
	//失败占比获取
	$('#btn-sbzb').on('click', function() {
		//绘制失败占比图
		xlsType = "fail";
		series = [];
		legend = [];
		butSearchData();
	});
	
	//绘制成功率图
	$('#btn-succrate').on('click', function() {
		bossBusineChart();
	});

	
	$("#bossPeriod").kendoDropDownList({
		optionLabel:"--全部周期类型--",
        dataTextField: "text",
        dataValueField: "value",
        index:1,
        dataSource: [
            { text: "15分钟", value: "MI" },
            { text: "小时", value: "H" },
            { text: "天", value: "D" },
            { text: "月", value: "MO" }
        ],
        filter: "contains",
        suggest: true
    });
	
    
 });

function butSearchData() {
	   
	$.ajax({
		url : "boss/succMonitor-item",
		type : "POST",
		datatype : 'json',
		data:{
    		"hlrid":inputNeName,
    		"businessType":inputBusiness,
    		"startTime":startdatetime,
    		"endTime":enddatetime,
    		"period":period
    	},
		success : function(data) {
			if(null!=data){
				if(typeof(data.message)!='undefined'){
					showNotify(data.message,"warning");
				}else{
					if(data.succRate.length>0){
						succAndFailFun(data.succRate);
					}else{
						showNotify("查询暂无数据......","warning");
						tochart({});
					}
				}
			}
		}
	});
}

//获取业务类型
function businessType(){
	
	$("#inputBusiness").kendoDropDownList({
		optionLabel:"--全部业务类型--",
		dataTextField: "business_name_en",
		dataValueField: "business_name",
		dataSource: new kendo.data.DataSource({
		    transport: {
		        read: {
		            url: "queryMonitorType/all",
		            datatype: "json"
		        }
		    }
		}),
		filter: "contains",
		suggest: true
	});
}


function succAndFailFun(data){
	var puta = [];//x轴对应的成功次数及成率
	var datetime =  [];
	$.each(data, function(i,item){
		//存放对应时间的值
		var objdata = {};
		var objTime ={};
		//x 轴时间存储
		var date_x = item.datestr; 
		datetime.push(date_x);//装X轴时间字符串
		objdata.total = item.total;//请求总数
		objdata.fail_count = item.fail_count;//请求失败数
		//key值就是当前周期返回的日期
		objTime[date_x]= objdata;//根据key时间获取值
		puta.push(objTime);
	});
	
	//x轴时间格式化
	countRateSucc(puta,datetime);
	
}

/**
 * 计算成功率
 * @param datePuta x轴数据 item
 * @param datetime x轴时间
 */
function countRateSucc(datePuta,datetime){
	
	dateArr = unique(datetime);
	dateArr.sort();//时间排序
	$.each(dateArr, function(i,time){
		
		var req_succ = 0;//成功请求次数
		var req_rate = 0;//成功率
		var req_sum = 0;//请求总数
		$.each(datePuta, function(x,data){
			if(typeof(data[time])!='undefined'){
				var arr = data[time];
				req_sum = arr.total;
				req_succ = req_sum-arr.fail_count;
				//console.log(req_sum+'  '+req_succ);
				if(req_succ>0){
					req_rate = parseInt(req_succ / req_sum * 100);
				}
				return true;
				
			}
		});
		succData.push(req_succ);
		succRate.push(req_rate);
		countData.push(req_sum);
	});
	
	
	//渲染图形
	bossBusineChart();
}

function unique(arr) {
    var result = [], hash = {};
    for (var i = 0, elem; (elem = arr[i]) != null; i++) {
        if (!hash[elem]) {
            result.push(elem);
            hash[elem] = true;
        }
    }
    return result;
}


//获取站点
function sitesData(){
	
	var neData = [];
	$.ajax({
		type : "GET",
		url : "getNePatentName",
		async:false,
		success : function(data) {
			$.each(data, function(i,item){
        		if(i==0){neName=item.replace(/[^a-zA-Z]/g,"");};
        		neData.push({text:item,value:item.replace(/[^0-9]/ig,"")});
        	});
		}
	});
	$("#inputNeName").kendoDropDownList({
		optionLabel:"--全部网元类型--",
		dataTextField: "text",
		dataValueField: "value",
		dataSource: neData,
		filter: "contains",
		//change: onChangeSite,
		suggest: true
	});
}


//图形绘制
function tochart(options){
	
	var charts = echarts.init(document.getElementById('charts'));
		
	charts.setOption(options);
}

//组装Boss监控数据
function bossBusineChart(){

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
				data: ['请求总数', '成功率']
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
				data: dateArr
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
					name: '请求总数',
					type: 'bar',
					//barMaxWidth: 40, //限制柱状图 宽度
					data: countData
				}, {
					name: '成功率',
					type: 'line',
					yAxisIndex: 1,
					smooth:true,
					symbolSize:4,
					symbol: "emptyCircle",
					data: succRate
				}
			]
		};
	
	//绘制图表
	tochart(options);
}

//echars渲染清空
function clear(){
	
	dateArr = [];
	//成功次数
	succData = [];
	//成功率
	succRate = [];
	countData = [];
	series = [];
	legend = [];
}

//重置查询条件
function reset(){
	$("#startdatetime").val("");
	$("#enddatetime").val("");
	$("#inputNeName").data("kendoDropDownList").text("");
	$("#bossPeriod").data("kendoDropDownList").text("");
	$("#inputBusiness").data("kendoDropDownList").text("--全部业务类型--");
}


