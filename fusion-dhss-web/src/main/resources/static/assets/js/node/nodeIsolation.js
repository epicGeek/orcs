var sites = "";
var neTypes = "";
var units = "";

var webSite = "";
var unitId = "";
var neTypeId = "";
kendo.culture("zh-CN");
$(function(){
	webSite = $("#webSite").val();
	unitId = $("#unitId").val();
	neTypeId = $("#neTypeId").val();
	//获取站点
	$.ajax({
		url : "emergencySecurity/queryLocation",
		datatype : 'json',
		timeout:5000,
		cache:false,
		success : function(data) {
			handleSiteData(data)
		}
	});
	//返回站点数据
	function handleSiteData(data) {
		var len = data.length;
		sites = "[";
	  $.each(data, function(i){
		  if (i === len - 1){
		   sites+='{ text:"'+this+'", value:"'+this+'"}'
	      }else{
	   	   sites+='{ text:"'+this+'", value:"'+this+'"},'  
	      } 
		 }); 
	  	sites+="]";
	  
	  $("#inputSite").kendoDropDownList({
		  dataTextField: "text",
		  dataValueField: "value",
		  dataSource:  (new Function("return " + sites))(),
		  filter: "contains",
		  change: onChangeSite,
		  suggest: true
		});

		if(webSite!=""){
			var color = $("#inputSite").data("kendoDropDownList");
//			color.select(1);
			color.value(webSite);
			$("#site").text(webSite);
	  		getNe(webSite);	
		}else{
			$("#site").text($("#inputSite").val());
	  		getNe($("#inputSite").val());		
		}
	   
    }
	
	//根据site获取Ne
	function getNe(site){
		$.ajax({
			url : "emergencySecurity/queryNe",
			data : {
				location : site
			},
			datatype : 'json',
			timeout:5000,
			cache:false,
			success : function(data) {	
				handleTypeData(data);
			}
		});
	}
	//根据neType获取节点
	function getUnit(neId){
		$.ajax({
			url : "emergencySecurity/queryUnitByNe",
			data : {
				neId : neId
			},
			datatype : 'json',
			timeout:5000,
			cache:false,
			success : function(data) {					
				handleUnitData(data);				
			}
		});
	}
	
	function onChangeSite() {
//      alert("onChangeSite()=="+$("#inputSite").val());
	    clearData();//清空执行步骤和返回信息
        $("#site").text($("#inputSite").data("kendoDropDownList").text());
        
        getNe($("#inputSite").val());
    };
    
    function handleTypeData(data) {
    	clearNeTypeData();
		var len = data.length;
		neTypes = "[";
	  	
	  	$(data.rows).each(function(i, rows) {
			if (i === len - 1){
				neTypes+='{ text:"'+rows.name+'", value:"'+rows.id+'"}'
		      }else{
		    	  neTypes+='{ text:"'+rows.name+'", value:"'+rows.id+'"},'  
		      } 
		});	
	  	neTypes+="]";
	  
	  $("#inputType").kendoDropDownList({
		  dataTextField: "text",
		  dataValueField: "value",
		  dataSource:  (new Function("return " + neTypes))(),
		  change: onChangeType,
		  filter: "contains",
		  suggest: true
		});
//	  alert("默认inputType的值："+$("#inputType").val());	
		if(neTypeId!=""){
			var color = $("#inputType").data("kendoDropDownList");
			color.value(neTypeId);
	  		getUnit(neTypeId); 
		}else{
			
	  		getUnit($("#inputType").val()); 	
		}
      	$("#neType").text($("#inputType").data("kendoDropDownList").text());    
    }
    
    function onChangeType() {
//      alert("onChangeType()=="+$("#inputType").val());
    	clearData();//清空执行步骤和返回信息
        $("#neType").text($("#inputType").data("kendoDropDownList").text());
        getUnit($("#inputType").val()); 
    };
    
    //返回的节点数据
    function handleUnitData(data) {
    	
    	clearUnitData();
    	
		var len = data.length;
		units = "[";
		
		units+='{ text:"请选择", value:"0"},'
		$.each(data,function(index,unit){
			
			if (index === len - 1){
				units+='{ text:"'+unit.unit+'", value:"'+unit.id+'"}'
		     }else{
		    	units+='{ text:"'+unit.unit+'", value:"'+unit.id+'"},'  
		     } 
		});
	  	
	  	units+="]";
	  
	  $("#inputNode").kendoDropDownList({
		  dataTextField: "text",
		  dataValueField: "value",
		  dataSource:  (new Function("return " + units))(),
		  change: onChangeNode,
		  filter: "contains",
		  suggest: true
		});
		
		if(unitId!=""){
			var color = $("#inputNode").data("kendoDropDownList");
			color.value(unitId);
		}

		$("#unit").text($("#inputNode").data("kendoDropDownList").text());
	      
    }
    
	function onChangeNode() {
    	clearData();//清空执行步骤和返回信息
    	
    	$("#unit").text($("#inputNode").data("kendoDropDownList").text());
    };
	//清空inputType
	function clearNeTypeData(){
		$("#inputType").kendoDropDownList({
			  dataTextField: "text",
			  dataValueField: "value",
			  dataSource:  null,
			  filter: "contains",
			  suggest: true
			});
	}
	//清空inputNode
	function clearUnitData(){
		$("#inputNode").kendoDropDownList({
			  dataTextField: "text",
			  dataValueField: "value",
			  dataSource:  null,
			  filter: "contains",
			  suggest: true
			});
	}
	//添加执行步骤到操作列表
	$("#addButton").click(function(){
//		alert($("#inputSite").val());
//		alert($("#inputType").val());
//		alert($("#inputNode").val());
//		alert($("#inputNode").data("kendoDropDownList").text());	

		if($("#inputNode").val()==0){//网元操作
			$.ajax({
				url : "emergencySecurity/getOperateStateByNe",
				data :{neId : $("#inputType").val()},
				datatype : 'text',
				timeout:5000,
				success:function(data) {
					if(data==2){
					alert("该网元已经被隔离，请选择其它网元！");
					}else{
						$.ajax({
				url : "emergencySecurity/getStepByType",
				data : {
					   site : $("#inputSite").val(),
					ne_type : $("#inputType").val(),
					   unit : $("#inputNode").val()
				},
				datatype : 'json',
				timeout:5000,
				cache:false,
				success : function(data) {	
					var stepHtml = "";
					
					$.each(data,function(index,s){
						state = "";
						if(s.execute_state==0){
						state="待执行";
						btn = "<td><button id='btnStartBackup' onclick='javascript:startBackup(this);' class='btn btn-danger'><i class='glyphicon glyphicon-log-out'></i> 执行隔离</button></td>";
						}else{
							state="已执行";
							btn = "<td><button disabled='disabled' id='btnStartBackup' onclick='javascript:startBackup(this);' class='btn btn-danger'><i class='glyphicon glyphicon-log-out'></i> 执行隔离</button></td>";
						}
						
						stepHtml+="<tr>"
							+"<th scope='row'>"+s.step_seq +"</th>"
							+"<td style='display:none'>"+ s.step_command +"</td>"
							+"<td>"+ s.step_explain +"</td>"
							+"<td>"+ s.step_describe +"</td>"
							+"<td>"+ state +"</td>"
							+btn
							+"</tr>";             
					});

					$("#stepId").html(stepHtml);
				}
			});
					}
				}
			});
		}else{
			$.ajax({
				url : "emergencySecurity/getOperateStateByUnit",
				data :{unit : $("#inputNode").val()},
				datatype : 'text',
				timeout:5000,
				success:function(data) {
					if(data==2){
					alert("该节点已经被隔离，请选择其它节点！");
					}else{
						$.ajax({
				url : "emergencySecurity/getStepByType",
				data : {
					   site : $("#inputSite").val(),
					ne_type : $("#inputType").val(),
					   unit : $("#inputNode").val()
				},
				datatype : 'json',
				timeout:5000,
				cache:false,
				success : function(data) {	
					var stepHtml = "";
					
					$.each(data,function(index,s){
						state = "";
						if(s.execute_state==0){
						state="待执行";
						btn = "<td><button id='btnStartBackup' onclick='javascript:startBackup(this);' class='btn btn-danger'><i class='glyphicon glyphicon-log-out'></i> 执行隔离</button></td>";
						}else{
							state="已执行";
							btn = "<td><button disabled='disabled' id='btnStartBackup' onclick='javascript:startBackup(this);' class='btn btn-danger'><i class='glyphicon glyphicon-log-out'></i> 执行隔离</button></td>";
						}
						
						stepHtml+="<tr>"
							+"<th scope='row'>"+s.step_seq +"</th>"
							+"<td style='display:none'>"+ s.step_command +"</td>"
							+"<td>"+ s.step_explain +"</td>"
							+"<td>"+ s.step_describe +"</td>"
							+"<td>"+ state +"</td>"
							+btn
							+"</tr>";             
					});

					$("#stepId").html(stepHtml);
				}
			});
					}
				}
			});
		}
				
	});
//清空
function clearData(){
	$("#stepId").html("");
	$(".pre-scrollable").text("");
}	
	
});
var tr;
function startBackup(e){
	 $("#myBackupModal").modal("show");
	 tmp = e;
	 tr = $(tmp).parent().parent(); 
// 	 td = tr.children("th:eq(0)");
	
}
function doIsolation(){
//	alert("操作有风险，禁止操作！");
	$("#myBackupModal").modal("hide");
//	return false;
	/////////
	$.ajax({
		url : "emergencySecurity/isolation",
		data : {
			ne:$("#inputType").val(),
			unit : $("#inputNode").val(),
			unitName:$("#inputNode").data("kendoDropDownList").text(),
			stepId : tr.children("th:eq(0)").text(),
			command:tr.children("td:eq(0)").text()
		},
		datatype : 'json',
		timeout:5000,
		cache:false,
		success : function(data) {
			if(data.error==-1){
				alert("节点隔离失败，请重试！");
			}else{
				
				$("#stepId").html("");
			
			var stepHtml = "";
				
			$.each(data.list,function(index,s){
				state = "";
				btn = "";
				if(s.execute_state==0){
					state="待执行";
					btn = "<td><button id='btnStartBackup' onclick='javascript:startBackup(this);' class='btn btn-danger'><i class='glyphicon glyphicon-log-out'></i> 执行隔离</button></td>";
				}else{
					state="已执行";
					btn = "<td><button disabled='disabled' id='btnStartBackup' onclick='javascript:startBackup(this);' class='btn btn-danger'><i class='glyphicon glyphicon-log-out'></i> 执行隔离</button></td>";
				}
								
				stepHtml+="<tr>"
					+"<th scope='row'>"+s.step_seq +"</th>"
					+"<td style='display:none'>"+ s.step_command +"</td>"
					+"<td>"+ s.step_explain +"</td>"
					+"<td>"+ s.step_describe +"</td>"
					+"<td>"+ state +"</td>"
					+ btn
					+"</tr>";             
			});
//				alert(stepHtml);
			$("#stepId").html(stepHtml);
			
//			alert(data.ret);
			$(".pre-scrollable").text(data.ret);
		}
			}
			
	});
}