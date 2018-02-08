kendo.culture("zh-CN");

var modalObj;
var alarmRuleGrid;
var initGrid;
var postParams = {};
var postParamsAlarm = {};
var searchParams = {alarmNo:'',alarmTitle:''};
var searchParamAlarm = {page:'',pageSize:''};
var alarmConfigModalObj;
var dataSource;
var alarmDataSource;
var alarmNo = "";

var flag = 0;//默认不重复
/*
 * 基站性能告警规则 js 副本
 * */
$(function() {
	
	 dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "btsAlarmRule/search"
            },
            parameterMap: function (options, operation) {
                if (operation == "read") {
	                 searchParams.page = options.page;
	                 searchParams.pageSize = options.pageSize;
	                 return searchParams;
                } else {
                	return kendo.stringify(options);
                }
            }
        },
        pageSize: 10,
        schema: {
            data: function (data) {
            	 return data.content;//返回页面数据
            },
            total: function (data) {
            	return data.totalElements; //总条数
            },
        },
        serverPaging : true,
		serverFiltering : true,
		serverSorting : true
    });
	 
	alarmRuleGrid = $("#alarmRuleList").kendoGrid({

				dataSource: dataSource,
				reorderable: true,
				resizable: true,
				sortable: true,
				pageable: true,
			//	toolbar: kendo.template($("#template").html()),
				columns: [
				          { field: "alarmNo", title:"告警号",width: 60},
				          { field: "alarmTitle",title:"告警标题",width:100},
		                  { field: "alarmType", title:"告警对象类型",width: 70,},
		                  { field: "alarmExplain", title:"告警解释",width: 130},
		                  { field: "delayOne", title:"时长",width: 90,
		                	  template:function(item){
		                		  return  "-"+item.delayScoreOne+"/"+item.delayOne;
		                	  }
		                  },
		                  { field: "delayTwo", title:"时长",width: 90,
		                	  template:function(item){
		                		  return  "-"+item.delayScoreTwo+"/"+item.delayTwo;
		                	  }
		                  },
		                  { field: "delayThree", title:"时长",width: 90,
		                	  template:function(item){
		                		  return  "-"+item.delayScoreThree+"/"+item.delayThree;
		                	  }
		                  },
		                  { field: "frequencyOne", title:"频次",width: 90,
		                	  template:function(item){
		                		  return "-"+item.freScoreOne+"/"+item.frequencyOne;
		                	  }
		                  },
		                  { field: "frequencyTwo", title:"频次",width: 90,
		                	  template:function(item){
		                		  return "-"+item.freScoreTwo+"/"+item.frequencyTwo;
		                	  }
		                  },
		                  { field: "frequencyThree", title:"频次",width: 90,
		                	  template:function(item){
		                		  return "-"+item.freScoreThree+"/"+item.frequencyThree;
		                	  }
		                  },
		                  { field: "frequencyFour", title:"频次",width: 90,
		                	  template:function(item){
		                		  return "-"+item.freScoreFour+"/"+item.frequencyFour;
		                	  }
		                  }
		                 ],
			}).data("kendoGrid");
 
 	//	条件查询
	$('#searchBtn').on('click', function() {
		searchParams.alarmNo = $('#alarmNoSearch').val();
		searchParams.alarmTitle= $('#alarmTitle').val();
		if(!checkContentLength(searchParams.alarmNo,10,"告警号")){return;}; //输入长度不超过10个字符
		if(!checkContentLength(searchParams.alarmTitle,100,"告警标题")){return;};
		$("#alarmRuleList").data("kendoGrid").pager.page(1);
		//条件查询重新加载 
		//dataSource.read(searchParams);
	});
	
	// 重置
	$('#resetBtn').click(function(){
		$('#alarmNoSearch').val("");
		$('#alarmTitle').val("");
		searchParams.alarmTitle = "";
		searchParams.alarmNo = "";
		dataSource.read(searchParams);
	});

/*	//性能告警得分配置——按钮
	$("#alarmScoreBtn").on("click",function(){
		if(!alarmConfigModalObj.dataGrid){
			alarmConfigModalObj.initGrid();
		} else {
			//重新设置数据源
			//alarmModalObj.dataGrid.setDataSource(dataSource);
		}
		$("#alarmConfigModal").modal("show");
	});*/
	
	//添加按钮
	$("#addBtn").on("click",function(){
		$('#myModal').modal('show');
		modalObj.clearModal();
	});
	
	//修改按钮
	$('body').delegate(".updateBtn", 'click', function() {
		
		$('#myModal').modal('show');
		var data = alarmRuleGrid.dataItem($(this).closest("tr"));
		postParams.id= data.id;
		modalObj.setModal(data);
		
	});
	
	//删除按钮
	$('body').delegate(".deleteBtn", 'click', function() {
		var data = alarmRuleGrid.dataItem($(this).closest("tr"));
		if(confirm("确认删除吗？")){
			deleteData(data.id);
		}
	});
	
    initAlarmLevelValue(); //告警级别下拉
	initAlarmTypeValue();  //告警类别
	
	//弹窗
	modalObj = {
			
		//清空弹窗内的数据
		clearModal: function(){
			$("#alarmNoValue").val("");
			$("#alarmNoValue").attr("disabled",false);
			$("#alarmTitleValue").val("");       
			$("#alarmDescValue").val("");       
			$("#ruleOne").val("");        
			$("#ruleTwo").val("");
			$("#ruleThree").val("");
			$("#ruleFour").val("");
			$('#alarmLevelValue').data("kendoDropDownList").value("");  //告警级别
			$('#alarmTypeValue').data("kendoDropDownList").value("");  //告警类别
		},
		
		//修改按钮弹窗内的数据
		setModal: function(data){
			$("#alarmNoValue").val(data.alarmNo).attr("disabled","disabled");
			//$("#alarmTypeValue").data("kendoDropDownList").value(data.alarmType); //类别
			//$("#alarmTypeValue").data("kendoDropDownList").enable(false); //不可修改
			$("#alarmTitleValue").val(data.alarmTitle);
			$("#alarmDescValue").val(data.alarmDesc);
			$("#alarmLevelValue").data("kendoDropDownList").value(data.alarmLevel); //告警级别
			$("#alarmTypeValue").data("kendoDropDownList").value(data.alarmType);
			$("#ruleOne").val(data.rule_1);
			$("#ruleTwo").val(data.rule_2);
			$("#ruleThree").val(data.rule_3);
			$("#ruleFour").val(data.rule_4);
			
			//临时存放值
			alarmNo = data.alarmNo;
		},
		//弹窗保存按钮
		saveBtn: function(){
			$("#saveBtn").on("click",function(){
				postParams.alarmNo = $("#alarmNoValue").val();
				postParams.alarmTitle = $("#alarmTitleValue").val();
				postParams.alarmDesc = $("#alarmDescValue").val();
				postParams.alarmType = $("#alarmTypeValue").val();  //告警类别
				postParams.alarmLevel = $("#alarmLevelValue").val();//告警级别
				postParams.rule_1= $("#ruleOne").val();
				postParams.rule_2 = $("#ruleTwo").val();
				postParams.rule_3 = $("#ruleThree").val();  
				postParams.rule_4 = $("#ruleFour").val();  
				
				if(!validata(postParams.alarmNo,'告警号')){return;};  //判断数字和长度
				if(checkIsNull(postParams.alarmType,'告警类别')){return;} 
				if(checkIsNull(postParams.alarmLevel,'告警级别')){return;};
				
				//验证重复
				var inputAlarmNo = $("#alarmNoValue").val();
				if(inputAlarmNo!=alarmNo){
					VerificationRepeat(postParams.alarmNo,1);
				}
				
				if(flag==0){
					if(postParams.id!="" && typeof(postParams.id)!="undefined"){
						postAddAndEdit('PATCH','rest/btsAlarmRule/'+postParams.id);
					}else{
						postAddAndEdit('POST','rest/btsAlarmRule/');
					}
				}
			});
		}
	};
	//添加 修改保存
	modalObj.saveBtn();
//==================================================================================================
	//性能告警得分配置
	//alarmScoreRule();
	/*alarmConfigModalObj = {
			initGrid: function(){
				alarmConfigModalObj.dataGrid = $("#alarmConfigGrid").kendoGrid({
					dataSource: alarmDataSource,
					height: 400,
					reorderable: true,
					resizable: true,
					sortable: true,
					//columnMenu: true,
					pageable: true,
					columns: [
						 { field: "score_rule_1", title:"时延高阀值分数",width: 120},
						 { field: "score_rule_2", title:"时延中阀值分数",width: 120},
						 { field: "score_rule_3", title:"时延低阀值分数",width: 120},
						 { field: "score_rule_4", title:"频次阀值分数",width: 120},
						 { width: 60,template:  "<a class='alarmRuleUpdateBtn btn btn-warning btn-xs'>修&nbsp;&nbsp;改</a> ",
						   encoded: false,
						   title: "<span  title='操作'>操作</span>"
						 }]
						
				}).data("kendoGrid");
					
			},
			
	};*/
	
	// 重置
	$('#alarmResetBtn').click(function(){
		
		 $('#scoreRuleFiled').val("");
		 searchParamAlarm.scoreRuleFiled =  "";
		 alarmDataSource.read(searchParamAlarm);
	});
	
	//性能告警得分配置--修改按钮
	$('body').delegate(".alarmRuleUpdateBtn", 'click', function() {
		
		$('#myAlarmModal').modal('show');
		var data = alarmConfigModalObj.dataGrid.dataItem($(this).closest("tr"));
		postParamsAlarm.id= data.id;
		alarmModalObj.setModal(data);
		
	});
	
	//性能告警得分配置修改弹框  
	alarmModalObj = {
					
			//修改按钮弹窗内的数据
			setModal: function(data){
				$("#scoreRuleHighInput").val(data.score_rule_1);
				$("#scoreRuleInInput").val(data.score_rule_2);
				$("#scoreRuleLowInput").val(data.score_rule_3);
				$("#scoreRuleNumInput").val(data.score_rule_4);
			},
			//弹窗保存按钮
			alarmSaveBtn: function(){
				$("#alarmSaveBtn").on("click",function(){
					postParamsAlarm.score_rule_1= $("#scoreRuleHighInput").val();
					postParamsAlarm.score_rule_2 = $("#scoreRuleInInput").val();
					postParamsAlarm.score_rule_3 = $("#scoreRuleLowInput").val();  
					postParamsAlarm.score_rule_4 = $("#scoreRuleNumInput").val(); 
					if(postParamsAlarm.id){
						alarmEdit(postParamsAlarm);
					}
				});
			}
		};
		//添加 修改保存
	alarmModalObj.alarmSaveBtn();
	
	
	//导出
	$("#alarmExport").on("click",function(){
		console.log(1111);
		var alarmNo =   $('#alarmNoSearch').val();
		if(alarmNo=='null' || alarmNo==null){
			alarmNo='';
		}
		var alarmTitle= $('#alarmTitle').val();
		if(alarmTitle=='null' || alarmTitle==null){
			alarmTitle='';
		}
	
		window.location.href = "alarmScore/exportFile?alarmNo=" +alarmNo+"&alarmTitle="+alarmTitle;
	});
		
	
	
});

//告警类别 下拉
function initAlarmTypeValue(){
	//下拉
	var alarmTypeVal =[
	              {text:'未定义',value:0},
	              {text:'操作维护',value:1},
	              {text:'处理器模块',value:2},
	              {text:'动力环境告警',value:3},
	              {text:'话务处理',value:4},
	              {text:'设备告警',value:5},
	              {text:'输入输出外部设备',value:6},
	              {text:'数据配置',value:7},
	              {text:"信令与ip",value:8},
	              {text:"中继与传输",value:9}
	    ];
	
	$("#alarmTypeValue").kendoDropDownList({
	 	optionLabel:"--请选择告警类别--",
	 	dataTextField: "text",
	    dataValueField: "value",
		dataSource: alarmTypeVal,
		autoBind: false
		//filter: "contains"
	});
}
//告警级别
function initAlarmLevelValue(){
	
	var  alarmLevelVal=[
		              {text:'无',value:0},
		              {text:'一级',value:1},
		              {text:'二级',value:2},
		              {text:'三级',value:3},
		              {text:'四级',value:4}
		             
		    ];
	$("#alarmLevelValue").kendoDropDownList({
		 	optionLabel:"--请选择告警级别--",
		 	dataTextField: "text",
		    dataValueField: "value",
			dataSource: alarmLevelVal,
			autoBind: false
			//filter: "contains"
		});
}

/**
 * 新增、修改请求函数
 * @param type
 * @param url
 */
function postAddAndEdit(type,url){
	$.ajax({
		url:url,
		type:type,
		dataType:"json",
		contentType:"application/json;charset=utf-8",
		data:kendo.stringify(postParams),
		success:function(data){
			$('#myModal').modal('hide');
			infoTip({content: "保存成功！",color:"#088703"});
			dataSource.read(searchParams);
		},
		complete: function(XMLHttpRequest, textStatus) {
			if(XMLHttpRequest.status==201){
				$('#myModal').modal('hide');
				infoTip({content: "保存成功！",color:"#088703"});
				dataSource.read(searchParams);
			}
		},
		fail:function(error){  
			infoTip({content: "保存失败！",color:"#f60a0a"});
		},
	});
}


//添加、修改，数据验证是否重复
function VerificationRepeat(searchFild,valiDateFild){
	
	$.ajax({
		url : "btsAlarmRule/isNotRepeat",
		type : "POST",
		data:{
			"searchFild":searchFild,
			"valiDateFild":valiDateFild
		},
		async:false,
		success : function(data) {
			flag = data;
			if(flag>0){
				if(valiDateFild==1){
					infoTip({content: "告警号不允许重复！",color:"#f60a0a"});
				}
			}
		},
		fail : function(data) {
			infoTip({content: "验证失败！",color:"#f60a0a"});
		}
	});
}


//删除
function deleteData(id){
	
	$.ajax({
		url:"rest/btsAlarmRule/"+id,
		type:"delete",
		dataType:"json",
		contentType:"application/json;charset=utf-8",
		data:kendo.stringify(postParams),
		success:function(data){
			infoTip({content: "删除成功！",color:"#088703"});
			dataSource.read(searchParams);
		},
		fail:function(error){
			infoTip({content: "删除失败！",color:"#f60a0a"});
		},
		
	});
}

//性能告警得分配置
function alarmScoreRule(){
	alarmDataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "alarmScoreRule/search"
            },
            parameterMap: function (options, operation) {
                if (operation == "read") {
	               	  searchParamAlarm.page = options.page;
                      searchParamAlarm.pageSize = options.pageSize;
                      return searchParamAlarm;
                } else {
                	return kendo.stringify(options);
                }
            }
        },
        page: searchParamAlarm.page,
        pageSize: 10,
        schema: {
            data: function (data) {
            	 return data.content;//返回页面数据
            },
            total: function (data) {
            	return data.totalElements; //总条数
            }
        },
        serverPaging : true,
		serverFiltering : true,
		serverSorting : true
    });
}

function alarmEdit(alarmEdit){
    	
    		$.ajax({
    			url : "alarmScoreRule/edit",
    			type : "POST",
    			dataType:"json",
    			contentType:"application/json;charset=utf-8",
    			data:JSON.stringify(alarmEdit),
    			async:false,
    			success : function(data) {
    				infoTip({content: "修改成功！",color:"#088703"});
    				$('#myAlarmModal').modal('hide');
    				alarmDataSource.read(searchParamAlarm);
    			},
    			fail : function(data) {
    				infoTip({content: "修改失败！",color:"#f60a0a"});
    			}
    		});
 }
