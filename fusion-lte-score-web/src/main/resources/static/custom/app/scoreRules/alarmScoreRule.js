/**
 * 告警评分规则 js
 */

//告警类别
var alarmTypes = ['天馈','光路','传输','电源','时钟','软件','硬件'];
//处理方式
var alarmHandles = ['无','派单处理'];
//告警级别
var alarmLevels = ['主要','次要','紧急','轻微'];

var modalObj;
var alarmGrid;
var postParams = {};
var searchParams = {alarmNo:'',alarmName:''};
var dataSource;
var alarmNo = "";
var flag = 0;//默认不重复

$(function() {
	
	kendo.culture("zh-CN");
	
	 dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "alarmScore/search"
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
	 alarmGrid = $("#alarmList").kendoGrid({

				dataSource: dataSource,
				reorderable: true,
				resizable: true,
				sortable: true,
				pageable: true,
				toolbar: kendo.template($("#template").html()),
				columns: [
				          { field: "alarmNo", title:"告警号",width: 100},
				          { field: "alarmNameCn", title:"告警名称",width: 120},
		                  { field: "alarmType", title:"告警类别",width: 80,
				        	  template: function(dataItem) {
				  					var str="";
									switch(dataItem.alarmType){
									case '0': str=""; break;
									case '1': str="天馈"; break;
									case '2': str="光路"; break;
									case '3': str="传输"; break;
									case '4': str="电源"; break;
									case'5': str="时钟";	break;
									case'6': str="软件" ; break;
									case'7': str="硬件";  break;
								    default: str="未知";   break;
									}
					        		return "<span class='tdOverFont' title='"+str+"'>"+str+"</span>";
				        	  }
		                  },
		                  { field: "delayHour_1", title:"时长",width: 90,
		                	  template:function(item){
		                		  return  "-"+item.delayScore_1+"/"+item.delayHour_1;
		                	  }
		                  },
		                  { field: "delayHour_2", title:"时长",width: 90,
		                	  template:function(item){
		                		  return  "-"+item.delayScore_2+"/"+item.delayHour_2;
		                	  }
		                  },
		                  { field: "delayHour_3", title:"时长",width: 90,
		                	  template:function(item){
		                		  return  "-"+item.delayScore_3+"/"+item.delayHour_3;
		                	  }
		                  },
		                  { field: "frequency_1", title:"频次",width: 90,
		                	  template:function(item){
		                		  return "-"+item.freScore_1+"/"+item.frequency_1;
		                	  }
		                  },
		                  { field: "frequency_2", title:"频次",width: 90,
		                	  template:function(item){
		                		  return "-"+item.freScore_2+"/"+item.frequency_2;
		                	  }
		                  },
		                  { field: "frequency_3", title:"频次",width: 90,
		                	  template:function(item){
		                		  return "-"+item.freScore_3+"/"+item.frequency_3;
		                	  }
		                  },
		                  { field: "frequency_4", title:"频次",width: 90,
		                	  template:function(item){
		                		  return "-"+item.freScore_4+"/"+item.frequency_4;
		                	  }
		                  }],
			}).data("kendoGrid");
	 
 	//	条件查询
	$('#searchBtn').on('click', function() {
		searchParams.alarmNo = $('#alarmNoInput').val();
		searchParams.alarmName = $('#alarmNameInput').val();
		if(!checkContentLength(searchParams.alarmNo,10,"告警号")){return;}; //输入长度不超过10个字符
		if(!checkContentLength(searchParams.alarmName,100,"告警名称")){return;};
		$("#alarmList").data("kendoGrid").pager.page(1);
		//条件查询重新加载 
		//dataSource.read(searchParams);
	});

	
	// 重置
	$('#resetBtn').click(function(){
		$('#alarmNoInput').val("");
		$('#alarmNameInput').val("");
		searchParams.alarmName = "";
		searchParams.alarmNo = "";
		dataSource.read(searchParams);
	});

	//添加按钮
	$("#addBtn").on("click",function(){
		$('#myModal').modal('show');
		modalObj.clearModal();
	});
	
	//编辑按钮
	$('body').delegate(".updateBtn", 'click', function() {
		
		$('#myModal').modal('show');
		var data = alarmGrid.dataItem($(this).closest("tr"));
		postParams.id= data.id;
		modalObj.setModal(data);
		
	});
	
	//删除按钮
	$('body').delegate(".deleteBtn", 'click', function() {
		var data = alarmGrid.dataItem($(this).closest("tr"));
		if(confirm("确认删除吗？")){
			deleteData(data.id);
		}
	});
	
	
	
	
	initAlarmTypeValue();  //告警类别
	
	//导出
	$("#alarmExport").on("click",function(){
		var alarmNo =   $('#alarmNoInput').val();
		if(alarmNo=='null' || alarmNo==null){
			alarmNo='';
		}
		var alarmName= $('#alarmNameInput').val();
		if(alarmName=='null' || alarmName==null){
			alarmName='';
		}
	
		window.location.href = "alarmScore/exportFile?alarmNo=" +alarmNo+"&alarmName="+alarmName;
	});
	
/*	//弹窗
	modalObj = {
			
		//清空弹窗内的数据
		clearModal: function(){
			//	$("#alarmIdValue").val("");     
			$("#alarmNoValue").val("");
			$("#alarmNoValue").attr("disabled",false);
			
			$("#alarmNameValue").val("");
			$("#alarmNameValue").attr("disabled",false);
			
			$("#alarmDescVaule").val("");       
			$("#alarmDescVaule").attr("disabled",false);
			
			$("#cancelledRule").val("");        
			$("#cancelledRule").attr("disabled",false);
			
			$("#alarmHandleValue").val("");     
			$('#alarmLevelValue').data("kendoDropDownList").value("");  //告警级别
			$('#alarmTypeValue').data("kendoDropDownList").value("");  //告警类别
			$('#activeValue').data("kendoDropDownList").value(""); //扣除分数
		},
		
		//告警级别 处理方式 扣除分数 是可以修改
		//修改设置弹窗内的数据
		setModal: function(data){
			$("#alarmNoValue").val(data.alarmNo).attr("disabled","disabled");
			//	$("#alarmNoValue").attr("disabled","disabled"); //不可修改
			//	$("#alarmIdValue").val(data.faultId);
			$("#alarmNameValue").val(data.alarmNameCn).attr("disabled","disabled");  //告警名称
			$("#alarmDescVaule").val(data.alarmDesc).attr("disabled","disabled");    //故障
			$("#cancelledRule").val(data.alarmMethod).attr("disabled","disabled");	//方法
			$("#alarmTypeValue").data("kendoDropDownList").value(data.alarmType); //类别
			$("#alarmTypeValue").data("kendoDropDownList").enable(false); //不可修改
			
			$("#alarmHandleValue").data("kendoDropDownList").value(data.alarmHandle);     //处理方式
			$("#alarmLevelValue").data("kendoDropDownList").value(data.alarmLevel); //级别  
			$('#activeValue').data("kendoDropDownList").value(data.deduction);   //分数
			//临时存放值
			alarmNo = data.alarmNo;
		},
		//弹窗保存按钮
		saveBtn: function(){
			$("#saveBtn").on("click",function(){
				postParams.alarmNo = $("#alarmNoValue").val();
			//	postParams.faultId = $("#alarmIdValue").val();
				postParams.alarmNameCn = $("#alarmNameValue").val();
				postParams.alarmDesc = $("#alarmDescVaule").val();
				postParams.alarmMethod = $("#cancelledRule").val();
				postParams.alarmHandle = $("#alarmHandleValue").val();
				postParams.alarmType = $("#alarmTypeValue").val();  //告警类别
				postParams.alarmLevel = $("#alarmLevelValue").val();  //告警级别
				postParams.deduction = $("#activeValue").val();   //扣除分数

				if(!checkContentLength(postParams.alarmNameCn,100,"告警名称")){return;};//判断输入长度
				if(!checkContentLength(postParams.alarmMethod,200,"处理方法")){return;};
				if(!checkContentLength(postParams.alarmDesc,200,"处理故障")){return;};
				if(!validata(postParams.alarmNo,'告警号')){return;};  //判断数字和长度
			//	if(!validata(postParams.faultId,'告警ID')){return;};
				if(checkIsNull(postParams.alarmHandle,'处理方式')){return;};
				if(checkIsNull(postParams.alarmType,'告警类别')){return;} 
				if(checkIsNull(postParams.alarmLevel,'告警级别')){return;};
				if(checkIsNull(postParams.deduction,'扣除分数')){return;};
				
				//验证重复
				var inputAlarmNo = $("#alarmNoValue").val();
				if(inputAlarmNo!=alarmNo){
					VerificationRepeat(postParams.alarmNo,1);
				}
				
				if(flag==0){
					if(postParams.id!="" && typeof(postParams.id)!="undefined"){
						postAddAndEdit('PATCH','rest/alarmScore/'+postParams.id);
					}else{
						postAddAndEdit('POST','rest/alarmScore/');
					}
				}
			});
		}
	};*/
	//添加 修改保存
	//modalObj.saveBtn();
	
	/*//导出
    $("#alarmExport").on("click",function(){
    	window.location.href = "alarmScore/exportFile?alarmNo=" +$('#alarmNoInput').val()
    	+"&alarmName="+$('#alarmNameInput').val();
	});
	
	//模板下载
    $("#alarmDownFile").on("click",function(){
    	downFile();
	});
	
	//上传
	$("#upload").kendoUpload({
		localization:{
			"select": "选择上传文件……",
			"dropFilesHere": "拖拽文件到此区域",
		},
		multiple: true,
		async: {
			saveUrl: "alarmScore/uploadFile",
			//removeUrl: "remove",
			autoUpload: true,
		},
		template: kendo.template($('#fileTemplate').html()),
		complete: function(){
			//console.log("complete");
			//window.location.href='paraProcessing-addUploadList.html';
		},
		success: function(){
			infoTip({content: "上传成功！",color:"#088703"});
			dataSource.read(searchParams);
		},
		error: function(){
			infoTip({content: "上传失败！",color:"#f60a0a"});
		}
	});*/
	
});

//告警类别 下拉
function initAlarmTypeValue(){
	//下拉
	var alarmTypeVal =[
	                   {text:'无',value:0},
	                   {text:'天馈',value:1},
	                   {text:'光路',value:2},
	                   {text:'传输',value:3},
	                   {text:'电源',value:4},
	                   {text:'时钟',value:5},
	                   {text:'软件',value:6},
	                   {text:'硬件',value:7}
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

//模板下载
function downFile(){
	window.location.href="alarmScore/downFile";
		 /*  +"params="+itemChecked[0].params
		   +"&defaultValue="+itemChecked[0].defaultValue
	       +"&checkName="+itemChecked[0].name;*/
}
/*
function initActiveValue(){
	//分数下拉
	$("#activeValue").kendoDropDownList({
		 	autoBind: false,
		 	optionLabel:"--请选择扣除分数--",
			dataSource: ["-10","-20","-30","-40","-50","-60","-70","-80","-90","-100"],
			change: function(){
				if(this.value() == -100){
					postParams.deduction = 0;
				}else{
					postParams.deduction = this.value();
				}
            }
			//filter: "contains"
		});
}*/


//告警级别
function initAlarmLevelValue(){
	
	var  alarmLevelValue=[
		              {text:'主要',value:1},
		              {text:'次要',value:2},
		              {text:'紧急',value:3},
		              {text:'轻微',value:4}
		             
		    ];
	$("#alarmLevelValue").kendoDropDownList({
		 	optionLabel:"--请选择告警级别--",
		 	dataTextField: "text",
		    dataValueField: "value",
			dataSource: alarmLevelValue,
			autoBind: false
			//filter: "contains"
		});
}
//处理方式
function alarmMethodValue(){
	
	var  alarmMethodValue=[
		              {text:'无',value:0},
		              {text:'派单处理',value:1},
		             
		    ];
	$("#alarmHandleValue").kendoDropDownList({
		 	optionLabel:"--请选择处理方式--",
		 	dataTextField: "text",
		    dataValueField: "value",
			dataSource: alarmMethodValue,
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
		url : "alarmScore/VerificationRepeat",
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
		url:"rest/alarmScore/"+id,
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

