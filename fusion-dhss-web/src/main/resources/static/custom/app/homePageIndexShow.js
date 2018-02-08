/**
 *  把查询、计算好的值放在首页上面：今日全网告警数量、鉴权成功率、语音呼叫查询成功率
 *  @author Neil pei

 */
$(function(){
	/*$.ajax({
		url:"alarm-monitor/search/countKpiAlarm",
		type:"GET",
		success:function(data){
<<<<<<< .mine
			data = data.split("=")[1].replace("}","");
			$("#kpiAlarmCount").html(data);
=======
			console.log(data)
			$("#kpiAlarmCount").append("<div><span>"+data.count+"</span><span>个</span></div>");
			$("#kpiAlarmCount").append("<div>周期起始时间：<span>"+data.period_start_time+"</span><span></span></div>");
>>>>>>> .r17329
		}
	});*/
	$.ajax({
		url:"/calculateKpiValue",
		data:{kpiName:"kpi001,kpi002"},
		type:"GET",
		success:function(data){
			$("#authenticationSuccessRate").html("");
			$.each(data["kpi001"],function(index,item){
				$("#kpi1").html(item.KpiName);
				$("#authenticationSuccessRate").append("<div><span>"+item.dhssName + " : " + item.value+"</span></div>");
			})
			$("#callSuccessRate").html("");
			$.each(data["kpi002"],function(index,item){
				$("#kpi2").html(item.KpiName);
				$("#callSuccessRate").append("<div><span>"+item.dhssName + " : " + item.value+"</span></div>");
			})
		}
	
	})
	
	/*$.ajax({
		url:"alarm-monitor/search/count",
		type:"GET",
		success:function(data){
			$.each(data,function(index,item){
				$("#showAlarmNum").append(item.value);
			})
		}
	
	})*/
	$.ajax({
		url:"alarm-monitor/search/count",
		type:"GET",
		success:function(data){
		    $("#alarmCount").html("");
			$.each(data,function(index,item){
				var ar = item.value.split(":");
				$("#alarmCount").append("<div>"+ar[0] + ":" + ar[1] +" 个</div> ");
			})
		}
	
	})
	//巡检结果异常单元
	$.ajax({
		url:"/smart-check-result/count",
		type:"GET",
		success:function(data){
		    $("#smartCheckResult").html("");
				$("#smartCheckResult").append("<div>"+ data[0].count +"</div>");
		}
	
	})
	
	

});
	    