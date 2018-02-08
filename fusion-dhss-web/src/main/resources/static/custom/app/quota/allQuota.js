
var bscName;
var quotaName;
var startTime;
var endTime;

var ctitles="";//图表上面显示的成功率和失败次数要动态显示
var alldates=[];//x轴的日期显示
var viewTitle=[];//图表中鼠标移动上去显示的失败次数成功率文字
var successRate=[];//成功率
var successCount=[];//失败次数

var flag = true;

function allquota(){
	viewTitle=[];//图表中鼠标移动上去显示的失败次数成功率文字
	alldates=[];//x轴的日期显示
	successRate=[];//成功率
	successCount=[];//失败次数
	ctitles="";
	
	bscName=$("#inputNeNameQuota").val();
	bscName = bscName == '全部网元' ? "" : bscName;
	var dhss = $("#inputDhssQuota").val();
	var dhssLocation = $("#inputLocationQuota").val();
	dhssLocation = dhssLocation == '全部局址' ? "" : dhssLocation;
	quotaName=$("#inputBusinessQuota").val();
	startTime=$("#startdatetimeq").val();
	endTime=$("#enddatetimeq").val();
	if(flag){
		$.ajax({
	    	url:"allQuotaControll/queryQuotaData",
	    	data:{
	    		"dhss":dhss,
	    		"dhssLocation":dhssLocation,
	    		"bscName":bscName,
	    		"quotaName":quotaName,
	    		"startTime":startTime,
	    		"endTime":endTime,
	    		"sort":"periodStartTime,desc"
	    		},
	    	datatype:'json',
	    	success:function(msg){
	    		$(msg.rows).each(function(i,rows){
	    			if($.inArray(rows.periodStartTime,alldates) == -1){
	    				alldates[alldates.length] = rows.periodStartTime;
	    				
	    				if(msg.item.outPutField == "fail_count"){
	    					successCount[successCount.length] = rows.kpiRequestCount - rows.kpiSuccessCount;
	    				}else if(msg.item.outPutField == "success_rate"){
	    					successCount[successCount.length] = rows.kpiSuccessCount;
	    				}else{
	    					successCount[successCount.length] = rows.kpiRequestCount;
	    				}
	    				successRate[successRate.length] = (rows.kpiSuccessCount / rows.kpiRequestCount * 100).toFixed(2);
	    			}
	    		});	
	    		if(msg.item.outPutField == "fail_count") {
	    			ctitles = '失败次数';
	    			viewTitle = ['成功率','失败次数'];
	    			tocharts();
	    		}else if(msg.item.outPutField == "success_rate") {
	    			ctitles = '成功次数';
	    			viewTitle = ['成功率','成功次数'];
	    			tocharts();
	    		}else{
	    			ctitles = '请求次数';
	    			viewTitle = [ '成功率','请求次数'];
	    			tocharts();
	    		}
	    			
	    	}
	    });
	}else{
		tocharts();
	}
	flag = true;
}

function handleSiteData() {
  	
  	
  	var myDate = new Date();
  	var year = myDate.getFullYear();       //年
    var month = myDate.getMonth() + 1;     //月
    if(month<10) month="0"+month;
    var day = myDate.getDate();            //日
    if(day<10)  day="0"+day;
    startTime=year+"-"+month+"-"+day+" 00:00:00";
    endTime=year+"-"+month+"-"+day+" 23:59:59";
    
  	if(quotaName!=undefined && quotaName!="")
  		$("#inputBusinessQuota").val(quotaName);
  	else
  		quotaName=$("#inputBusinessQuota").val();
  	
    $("#startdatetimeq").kendoDateTimePicker({
			format:"yyyy-MM-dd HH:mm:ss",
	        value:startTime
	});
    $("#enddatetimeq").kendoDateTimePicker({
			format:"yyyy-MM-dd HH:mm:ss",
			value:endTime
    });
}


$(function(){ 
	allbscname();
	handleSiteData();
	
	//导出 按钮
	 $('#exportb').on('click', function() {
		 	bscName=$("#inputNeNameQuota").val();
			quotaName=$("#inputBusinessQuota").val();
			startTime=$("#startdatetimeq").val();
			endTime=$("#enddatetimeq").val();
		 	var url1="allQuotaControll/exportFileQuota?";
			var url2="bscName="+bscName;
			var url3="&quotaName="+quotaName;
			var url4="&startTime="+startTime;
			var url5="&endTime="+endTime;
			var url=url1+url2+url3+url4+url5;
			console.log(url);
			window.location.href=url;
	 });
	 
	 allquota();
});



//图形绘制
function tochart(options){
	
	var charts = echarts.init(document.getElementById('charts'));
		
	charts.setOption(options);
}

//组装Boss监控数据
function tocharts(){

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
						type: ['bar', 'line']
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
				data: viewTitle
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
				data: alldates
			}],
			yAxis: [{
				type: 'value',
				name: "",
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
					formatter: '{value}%'
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
					formatter: '{value}次'
				}
			}],
			series: [
				{
					name: '成功率',
					type: 'line',
					//barMaxWidth: 40, //限制柱状图 宽度
					data: successRate
				}, {
					name: ctitles,
					type: 'bar',
					yAxisIndex: 1,
					smooth:true,
					symbolSize:4,
					symbol: "emptyCircle",
					data: successCount
				}
			]
		};
	
	//绘制图表
	tochart(options);
	
}

