kendo.culture("zh-CN");

var flag=true;
var allNe = [];
var allLocation = [];
var gridDataSource;
var staticId;
function getLocationList(){
	$("#locationInput").kendoDropDownList({
	    optionLabel: "请选择站点",
	    dataSource: allLocation,
	    filter: "contains",
	    change: function(){
	    	getNeList();
	    	gridDataSourceFilter();
	    },
	    suggest: false
	}).data("kendoDropDownList");
}
function getNeList(){
	var text = $('#locationInput').data("kendoDropDownList").text();
	var array = [];
	var titleName ="请选择网元";
	if(text == "请选择站点" || text == ""){
		array = allNe;
	}else{
		text = $("#locationInput").val();
		$.each(allNe,function(index,item){
			if(item.location == text ){
				array[array.length] = item;
			}
		})
		titleName = array[0].neName;
	}
	 $("#neNameInput").kendoDropDownList({
	    optionLabel: "请选择网元",
	    dataTextField: "neName",
	    dataValueField: "neName",
	    dataSource: array,
	    filter: "contains",
	    change: function(){
//	    	getUnitList();
	    	gridDataSourceFilter();
	    }
	}).data("kendoDropDownList");
//	$('#neNameInput').data("kendoDropDownList").text("请选择网元");
}
function getcjList(){
	
	$.ajax({
		url : "rest/node-switch-case",
		datatype : 'json',
		timeout:5000,
		cache:false,
		success : function(data) {
			var array = [];
			if(data._embedded){
				array = data._embedded["node-switch-case"];
			}
			$("#cjInput").kendoDropDownList({
			    optionLabel: "请选择场景",
			    dataTextField: "casename",
			    dataValueField: "caseId",
			    dataSource: array,
			    filter: "contains",
			    suggest: false
			}).data("kendoDropDownList");
			
			if($("#case").val() != ""){
				$("#cjInput").data("kendoDropDownList").text($("#caseName").val());
				$("#cjInput").val($("#case").val());
			}
		}
	});
}
function  gridDataSourceFilter(){
	var filters = [];

	if($('#locationInput').data("kendoDropDownList").text() != "请选择站点" && $('#locationInput').data("kendoDropDownList").text() != "")
    	filters.push({field: "location", operator: "eq", value: $('#locationInput').val() });
	if($('#neNameInput').data("kendoDropDownList").text() != "请选择网元" && $('#neNameInput').data("kendoDropDownList").text() != ""){
		filters.push({field: "neName", operator: "eq", value: $('#neNameInput').val() });
	}

	gridDataSource.filter(filters);
	gridDataSource.fetch();
}
var stepGrid;
function chk(neHref){
	var caseid = $("#cjInput").val();
	if($("#cjInput").data("kendoDropDownList").text() == "请选择场景" ){
		caseid = "";
		infoTip({content: "请选择场景",color:"red"});
	}else{
		if($("#actionInput").data("kendoDropDownList").text() != "请选择操作"){
			$.ajax({
				dataType : 'json',
				type : "GET",
				url : neHref,
				success : function(ne) {
					staticUnitId = ne;
						var action = $("#actionInput").data("kendoDropDownList").text();
						$.ajax({
							url : "nodeSwitch/stepExecute",
							data : {
								siteName:ne.location,
								neId:ne.neId,
								neName:ne.neName,
								actionVal:action,
								caseId:caseid
							},
							datatype : 'json',
							timeout:5000,
							cache:false,
							success : function(data) {	
								initStepAndResult(data);
							}
						});
				}
			});
		}else{
			infoTip({content: "请选择操作",color:"red"});
		}
	}
	
	

	
}



var tr;

var finalFlag = false;
function initStepAndResult(data){
	finalFlag = false;
	stepGrid.dataSource.data([]);
	staticId = data.id;
	stepGrid.dataSource.data(data.list);
	if(data.result == ""){
		resultGrid.dataSource.data([]);
	}else{
		var results = [];
		var array = data.result.split(";");
		$.each(array,function(index,item){
			if(item!=""){
				var rlt = item.split(",");
				var flag = "0";
				if(rlt[1] == ""){
					flag = "1";
					finalFlag = true;
				}
				results[results.length] = { desc : rlt[0] , url : rlt[1] , flag : flag };
			}
		})
		resultGrid.dataSource.data(results);
	}
}

function downloadLog(url,desc){
	location.href = "nodeSwitch/downloadLog?url="+url+"&desc="+desc;
}

function clearInfo(){
	$.ajax({
		url : "nodeSwitch/updateSwitchingReturnInfo",
		data : {
			id:staticId
		},
		datatype : 'json',
		timeout:5000,
		cache:false,
		success : function(data) {	
			resultGrid.dataSource.data([]);
		}
	});
}
var resultGrid;

$(function(){
	
	
	getcjList();
	
	stepGrid = $("#stepGrid").kendoGrid({
		dataSource : [],
		height : $(window).height() - $("#stepGrid").offset().top - 120,
		reorderable : true,
		resizable : true,
		sortable : true,
		pageable : false,
		columns: [/*{
			width: 30,
			template: "<input type='radio' name = 'chk' onClick = 'chk(\"#:_links.self.href#\")' id='#:_links.self.href#' />",
			attributes:{"class": "text-center"},
//			title: "<input  class='grid_radio'/>"
		},*/ {
			field: "stepSeq",
			title: "<span  title='执行顺序'>执行顺序</span>"
		}, {
			field: "stepDesc",
			title: "<span  title='步骤描述'>步骤描述</span>"
		},{
			field: "unitName",
			title: "<span  title='单元名称'>单元名称</span>"
		}, {
			field: "stepCmd",
			title: "<span  title='操作指令'>操作指令</span>"
		}, {
			template: "<button  onclick='startBackup(this);' class='btn btn-danger btn-xs'><i class='glyphicon glyphicon-log-out'></i> 执行倒换</button>",
			title: "<span  title='操作'>操作</span>"
		}]
	}).data("kendoGrid");
	
	resultGrid = $("#resultGrid").kendoGrid({
		dataSource : [],
		height : $(window).height() - $("#resultGrid").offset().top - 120,
		reorderable : true,
		resizable : true,
		sortable : true,
		pageable : false,
		columns: [{
			field: "desc",
			title: "<span  title='描述'>描述</span>"
		},{
			template: "#if(flag == '1'){#"+
			  "<div class='progress'><div class='progress-bar progress-bar-warning progress-bar-striped active' style='width: 70%;'>执行中……</div></div>"+
			  "#} else {#"+
			  "完成，<a class='btn btn-link'  onclick=\"downloadLog(\'#:url#\',\'#:desc#\')\"><i class='glyphicon glyphicon-download-alt'></i>下载</a>"+
			  "#}#",
			field: "flag",
			title: "<span  title='查看日志'>查看日志</span>"
		}]
	}).data("kendoGrid");
	
	
	var inputSite = $("#actionInput").kendoDropDownList({
		optionLabel: "请选择操作",
		dataTextField: "text",
	    dataValueField: "value",
	    dataSource: [{text : "隔离" , value : "1" },{text : "恢复" , value : "2" }],
	    filter: "contains",
	    suggest: false
	}).data("kendoDropDownList");
	
	
	
	gridDataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: "rest/equipment-ne",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            },
        },
        schema: {
            data: function (data) {
            	if(data._embedded){
            		allNe = data._embedded["equipment-ne"];
            		$.each(allNe,function(index,item){
        				if($.inArray(item.location,allLocation) == -1){
        					allLocation[allLocation.length] = item.location;
        				}
        			})
        			getLocationList();
            		getNeList();
        			return allNe;
            	}
            	return [];
            }
        },
        serverPaging : false,
		serverFiltering : false,
		serverSorting : false,
	});
	
	
	var neGrid = $("#neGrid").kendoGrid({
		dataSource : gridDataSource,
		height : $(window).height() - $("#neGrid").offset().top - 120,
		reorderable : true,
		resizable : true,
		sortable : true,
		pageable : false,
		columns: [ {
			field: "neName",
			title: "<span  title='网元'>网元</span>"
		}, {
			field: "neType",
			title: "<span  title='网元类型'>网元类型</span>"
		}, {
			field: "location",
			title: "<span  title='局址'>局址</span>"
		},{
			template: "<button onClick = 'chk(\"#:_links.self.href#\")' class='editBtn btn btn-xs btn-default'><i class='glyphicon glyphicon-edit'></i>添加到列表</button>",
			title: "<span  title='操作'>操作</span>"
		}]
	}).data("kendoGrid");
	
	
	
	
	
	
	if($("#case").val() != ""){
		
		$.ajax({
			dataType : 'json',
			type : "GET",
			url : "rest/equipment-ne/"+$("#ne").val(),
			success : function(ne) {
				staticUnitId = ne;
					var action = $("#actionInput").data("kendoDropDownList").text();
					$.ajax({
						url : "nodeSwitch/stepExecute",
						data : {
							siteName:ne.location,
							neId:ne.neId,
							neName:ne.neName,
							actionVal:$("#action").val() == "4" ? "隔离":"恢复",
							caseId:$("#case").val()
						},
						datatype : 'json',
						timeout:5000,
						cache:false,
						success : function(data) {	
							initStepAndResult(data);
						}
					});
			}
		});
		$("#actionInput").data("kendoDropDownList").text($("#action").val() == "4" ? "隔离":"恢复");
		$("#actionInput").val($("#action").val() == "4" ? "1":"2");
		
		
	
	
	}
	
	
	
	
	
	
	
	
	
	

});
function startBackup(e){
	if(!finalFlag){
		tr = stepGrid.dataItem($(e).closest("tr"));
		$('#myBackupModal').modal('show');
	}else{
		infoTip({content: "请等待上一条指令执行成功以后再执行！",color:"red"});
	}
}
function doSwitch(){
	
	
	$.ajax({
		dataType : 'json',
		type : "GET",
		data : {
			id:staticId,
			desc:tr.stepDesc
		},
		url : "nodeSwitch/updateSwitching",
		success : function(datas) {
			finalFlag = true;
			initStepAndResult(datas);
			infoTip({content: "指令下发成功！"});
			$("#myBackupModal").modal("hide");
			$.ajax({
				url : "nodeSwitch/switching",
				data : {
					command:tr.stepCmd,
					script:tr.script,
					unitName:tr.unitName,
					id:staticId
				},
				datatype : 'json',
				timeout:1000*500,
				cache:false,
				success : function(data) {
					if(data.error==-1){
						infoTip({content: "指令执行超时，请重试！",color:"red"});　
					}else if(data.error==-2){
						infoTip({content: "指令执行失败，请重试！",color:"red"});　　
					}else{
						initStepAndResult(data);
						infoTip({content: "指令执行完毕！"});
					}
				}
				,
		        error:function(errorMessage){
		            alert("指令执行错误信息:"+errorMessage);
		            $("#myBackupModal").modal("hide");
		        }
			});				
		}
	});
}
/**
 * 跳转到网元倒换历史查询
 */
function searchHistory(){
  window.location.href="nodeSwitchHistory";
}
/**
 * 跳转到板卡倒换历史查询
 */
function searchSingleHistory(){
  window.location.href="nodeSingleSwitchHistory";
}
	
/***/
