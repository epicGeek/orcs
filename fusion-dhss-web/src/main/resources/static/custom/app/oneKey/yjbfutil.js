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
			if(data.backUpMinitorList.length == 0){
				resultGrid.dataSource.data([]);
			}else{
				resultGrid.dataSource.data(data.backUpMinitorList);
			}
			
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
				console.log(nodes[i].allVal);
			}
		}
    }
}

// show checked node IDs on datasource change
function onCheck() {
    var checkedNodes = [],
        treeView = $("#div_che_tree").data("kendoTreeView"),
        message;
    var flag = checkedNodeIds(treeView.dataSource.view(), checkedNodes);
    if(flag){
    	console.log(checkedNodes);
    }
    var temp=new Array();
    
    if(checkedNodes.length != 0 ){
    	temp=checkedNodes[0].split("&");
        topOneUnit=checkedNodes;
        getStepByType(temp[0],temp[1],temp[2]);
    }
    stepGrid.dataSource.data([]);
}

var stepGrid = $("#stepGrid").kendoGrid({
	dataSource : [],
	height : $(window).height() - $("#stepGrid").offset().top - 120,
	reorderable : true,
	resizable : true,
	sortable : true,
	pageable : false,
	columns: [ {
		field: "step_describe",
		title: "<span  title='步骤描述'>步骤描述</span>"
	},{
		field: "step_command",
		title: "<span  title='步骤说明'>步骤说明</span>"
	},  {
		template: "<button id='btnStartBackup' onclick='javascript:doBeifen(this);' class='btn btn-danger btn-xs'><i class='glyphicon glyphicon-log-out'></i> 执行备份</button>",
		title: "<span  title='操作'>操作</span>"
	}],
	dataBound: function(){
	}
}).data("kendoGrid");


var resultGrid = $("#resultGrid").kendoGrid({
	dataSource : [],
	height : $(window).height() - $("#resultGrid").offset().top - 120,
	reorderable : true,
	resizable : true,
	sortable : true,
	pageable : false,
	columns: [{
		field: "unitName",
		title: "<span  title='节点名称'>节点名称</span>"
	},{
		field: "startTime",
		title: "<span  title='启动时间'>启动时间</span>"
	}, {
		field: "errorMsg",
		title: "<span  title='结果信息'>结果信息</span>"
	},{
		field: "resultCode",
		template: "#if(resultCode == '0'){# <b style='color:red;'>成功</b> #}else{# 失败 #}if(executing=='0'){#/正在执行#}#",
		title: "<span  title='状态/操作'>状态/操作</span>"
			
		/*field: "#if(resultCode=='0'){#成功#}else{#失败#}# #if(executing=='0'){#/正在执行#}#",
		template: "<span  title='#if(resultCode=='0'){#成功#}else{#失败#}#'>#if(executing=='0'){#/正在执行#}#</span>",
		title: "<span  title='状态/操作'>状态/操作</span>"*/
	}  /*{
		template: "<button id='btnStartBackup' onclick='javascript:doBeifen(this);' class='btn btn-danger btn-xs'><i class='glyphicon glyphicon-log-out'></i> 执行倒换</button>",
		title: "<span  title='操作'>操作</span>"
	}*/],
	dataBound: function(){
	}
}).data("kendoGrid");

/***
 * 根据网元  板卡  来获取备份不走列表
 * 
 */
function getStepByType(ne_type,unit_type,unit){
	$.ajax({
		url : "backup/getStepByType",
		data : {
			   site:"",
			   ne_type:ne_type,
			   unit_type : unit_type,
			   unit : unit
		},
		datatype : 'json',
		timeout:5000,
		cache:false,
		success : function(data) {	
			if(data.length == 0){
				stepGrid.dataSource.data([]);
			}else{
				stepGrid.dataSource.data(data);
			}
		}
	});
}
