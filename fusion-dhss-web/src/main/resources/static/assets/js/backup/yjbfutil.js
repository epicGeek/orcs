function doCancel(){
	$('#myBackupModal').modal('hide');
}
function checkNoFinished(){
$.ajax({
	url : "backup/getBackUpMinitor",
	datatype : 'json',
	timeout:150000,
	cache:false,
	success : function(data) {	
		if(data.backUpingCount>0){
			$("#div_backuping").modal('show');
			$('#div_backuping').find("div").eq(0).unbind("click");
			$.ajax({
				url:"backup/getFailureMinitor",
				datatype : 'json',
				timeout:150000,
				cache:false,
				success : function(res) {
					$("#div_backuping").modal('hide');
					alert("检查完毕。");
				}	
			})
		}
		reflush();
	}
});
}
$.ajax({
	url : "backup/getAllNeTree",
	datatype : 'json',
	timeout:15000,
	cache:false,
	success : function(data) {	
		$("#div_che_tree").kendoTreeView({
		    checkboxes: {
		        checkChildren: true
		    },
		    check: onCheck,
		    dataSource:data
		});
	}
});

function reflush(){
	$.ajax({
		url : "backup/getBackUpMinitor",
		datatype : 'json',
		timeout:150000,
		cache:false,
		success : function(data) {	
			var stepHtml = "";
			$.each(data.backUpMinitorList,function(idx,monitor){
				var status_unit="";
				var execute="";
				if(monitor.result_code=='0'){
					status_unit='成功';
				}else{
					status_unit='失败';
				}
				if(monitor.executing==0){
					execute="/正在执行...";
				}
				stepHtml+="<tr>"
					+"<th scope='row'>"+monitor.site+"_"+monitor.unit_type+"_"+monitor.unit_name+"("+monitor.step_id+")"+"</th>"
					+"<td>"+kendo.toString(new Date(monitor.end_time), 'yyyy-MM-dd HH:mm')+"</td>"
					+"<td>"+monitor.error_msg+"</td>"	
					+"<td>"+status_unit+execute+"</td>"
					+"</tr>";             
			});
			$("#tb_monitorLog").html("");
			$("#tb_monitorLog").html(stepHtml);
		}
	});
}

function delMonitor(obj){
	$.ajax({
		url : "backup/delBackUpMinitor",
		datatype : 'json',
		timeout:30000,
		cache:false,
		success : function(data) {	
			reflush();
		}
	});
}
// function that gathers IDs of checked nodes
function checkedNodeIds(nodes, checkedNodes) {
    for (var i = 0; i < nodes.length; i++) {
        if (nodes[i].hasChildren) {
            checkedNodeIds(nodes[i].children.view(), checkedNodes);
        }else{
			if (nodes[i].checked) {
				checkedNodes.push(nodes[i].allVal);
			}
		}
    }
}

// show checked node IDs on datasource change
function onCheck() {
    var checkedNodes = [],
        treeView = $("#div_che_tree").data("kendoTreeView"),
        message;

    checkedNodeIds(treeView.dataSource.view(), checkedNodes);
    var temp=new Array();
    temp=checkedNodes[0].split("_");
    topOneUnit=checkedNodes;
    getStepByType(temp[1],temp[2]);
}

/***
 * 根据网元  板卡  来获取备份不走列表
 * 
 */
function getStepByType(unit_type,unit){
	$.ajax({
		url : "backup/getStepByType",
		data : {
			   site:"",
			   unit_type : unit_type,
			   unit : unit
		},
		datatype : 'json',
		timeout:5000,
		cache:false,
		success : function(data) {	
			var stepHtml = "";
			
			$.each(data,function(index,s){						
				stepHtml+="<tr>"
					+"<th scope='row'>"+s.step_seq +"</th>"
					+"<td style='display:none'>"+ s.step_command +"</td>"
					+"<td>"+ s.step_explain +"</td>"
					+"<td>"+ s.step_describe +"</td>"
					+"<td><button id='btnStartBackup' onclick='javascript:doBeifen(this);' class='btn btn-danger'><i class='glyphicon glyphicon-log-out'></i> 执行备份</button></td>"
					+"</tr>";             
			});

			$("#stepId").html(stepHtml);
		}
	});
}
