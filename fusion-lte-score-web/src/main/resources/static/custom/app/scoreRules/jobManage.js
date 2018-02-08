kendo.culture("zh-CN");
var modalObj;
var initGrid;
var postParams = {};
var searchParams = {cellId:'',ruleTitle:'',jobType:''};
var jobs = ['基站健康度','基础性能告警'];
var dataSource;
var flag = 0;//默认不重复
var ruleName = "",cellIdName= "";
/*
 * 基站性能告警规则 js
 * */
$(function() {
	
	jobType();
	
	$("#jobType_ae").kendoDropDownList({
	 	optionLabel:"--请选择工单类型--",
	 	dataTextField: "text",
	    dataValueField: "value",
		dataSource: [
		              {text:'基站健康度',value:0},
		              {text:'基础性能告警',value:1}
		             
		           ],
		autoBind: false
		//filter: "contains"
	});
	
	 dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "jobManage/search"
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
				toolbar: kendo.template($("#template").html()),
				columns: [
				          { field: "ruleTitle",title:"规则名称",width:100},
				          { field: "cellId", title:"基站ID",width: 60},
		                  { field: "jobType", title:"工单类型",width: 70,
				        	template:"#if(jobType==0){##:jobs[jobType]##}else{##:jobs[jobType]##}#"
		                  },
		                  { field: "ruleDesc", title:"规则描述",width: 130},
		                  { field: "jobUp", title:"工单上限阈值",width: 100},
		                  { field: "jobNext", title:"工单下限阈值",width: 70,},
		                  { field: "jobDecide", title:"门限判断符",width: 60},
		                  {
							width: 60,
							template: "<a class='updateBtn btn btn-warning btn-xs'>修 改</a><a class='deleteBtn btn btn-danger btn-xs'>删 除</a>",
							encoded: false,
							title: "<span  title='操作'>操作</span>"
				       }],
			}).data("kendoGrid");
 
 	//	条件查询
	$('#searchBtn').on('click', function() {
		searchParams.cellId = $('#cellId').val();
		searchParams.ruleTitle= $('#ruleTitle').val();
		searchParams.jobType = $('#jobType').val();
		$("#alarmRuleList").data("kendoGrid").pager.page(1);
		//条件查询重新加载 
		//dataSource.read(searchParams);
	});
	
	// 重置
	$('#resetBtn').click(function(){
		$('#cellId').val("");
		$('#ruleTitle').val("");
		$('#jobType').val("");
		searchParams.cellId = "";
		searchParams.ruleTitle = "";
		searchParams.jobType = "";
		dataSource.read(searchParams);
	});

	
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
	
	//弹窗
	modalObj = {
			
		//清空弹窗内的数据
		clearModal: function(){
			$("#ruleTitle_ae").val("");
			$("#cellId_ae").val("");       
			$("#ruleDesc").val("");       
			$("#jobUp").val("");        
			$("#jobNext").val("");
			$("#jobDecide").val("");
			$('#jobType_ae').data("kendoDropDownList").value("");
		},
		
		//修改按钮弹窗内的数据
		setModal: function(data){
			$("#ruleTitle_ae").val(data.ruleTitle);
			$("#cellId_ae").val(data.cellId);
			$("#ruleDesc").val(data.ruleDesc);
			$("#jobUp").val(data.jobUp);
			$("#jobNext").val(data.jobNext);
			$("#jobDecide").val(data.jobDecide);
			$("#jobType_ae").data("kendoDropDownList").value(data.jobType);
			
			ruleName = data.ruleTitle;
		},
		//弹窗保存按钮
		saveBtn: function(){
			$("#saveBtn").on("click",function(){
				
				postParams.ruleTitle = $("#ruleTitle_ae").val();
				postParams.cellId = $("#cellId_ae").val();
				postParams.ruleDesc = $("#ruleDesc").val();
				postParams.jobUp = $("#jobUp").val();  //告警类别
				postParams.jobNext = $("#jobNext").val();//告警级别
				postParams.jobDecide= $("#jobDecide").val();
				postParams.jobType = $("#jobType_ae").val();
				if(!numberAndIsNull(postParams.jobUp,'工单上限值')){return;};  //判断数字和null
				if(!numberAndIsNull(postParams.jobNext,'工单下限值')){return;};
				if(checkIsNull(postParams.ruleTitle,'规则名称')){return;};
				if(!numberAndIsNull(postParams.cellId,'小区ID')){return;};
				//验证重复
				var ruleTitle = $("#ruleTitle_ae").val();
				var cellId = $("#cellId_ae").val();
				if(ruleName!=ruleTitle){
					VerificationRepeat(postParams.ruleTitle,'ruleTitle');
				}
				if(cellIdName!=cellId){
					VerificationRepeat(postParams.cellId,'cellId'); 
				}
				if(flag==0){
					if(postParams.id!="" && typeof(postParams.id)!="undefined"){
						postAddAndEdit('PATCH','rest/jobManage/'+postParams.id);
					}else{
						postAddAndEdit('POST','rest/jobManage/');
					}
				}
			});
		}
	};
	//添加 修改保存
	modalObj.saveBtn();
//==================================================================================================
	
});

//工单类型
function jobType(){
	
	var  jobData=[
		              {text:'基站健康度',value:0},
		              {text:'基础性能告警',value:1}
		             
		    ];
	$("#jobType").kendoDropDownList({
		 	optionLabel:"--请选择工单类型--",
		 	dataTextField: "text",
		    dataValueField: "value",
			dataSource: jobData,
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
	
	
	//console.log(kendo.stringify(postParams));
	
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
		url : "jobManage/isNotRepeat",
		type : "POST",
		data:{
			"searchFild":searchFild,
			"valiDateFild":valiDateFild
		},
		async:false,
		success : function(data) {
			flag = data;
			if(flag>0){
			  infoTip({content: "规则不允许重复！",color:"#f60a0a"});
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
		url:"rest/jobManage/"+id,
		type:"delete",
		dataType:"json",
		contentType:"application/json;charset=utf-8",
		success:function(data){
			infoTip({content: "删除成功！",color:"#088703"});
			dataSource.read(searchParams);
		},
		fail:function(error){
			infoTip({content: "删除失败！",color:"#f60a0a"});
		},
		
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
