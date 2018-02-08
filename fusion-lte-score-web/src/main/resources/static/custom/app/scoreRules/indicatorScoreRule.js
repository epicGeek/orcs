/*
 * 指标评分规则
 * */
var dataSource;

var gridGrid;

var modalObj;

var flag = 0;//默认不重复

var kpiArr = [];

var kpiId = "";
var kpiName = "";

//KPI 类型 0:无 1:接入性 2:可用性 3：保持性 4：移动性 5：业务质量
var kpiTypeValue= ['无','接入性','可用性','保持性','移动性','业务质量'];

var postParams = {};
var searchParams = {kpiId:''/*,cycle:''*/};

$(function() {
	kendo.culture("zh-CN");
	dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "kpiIndex/search"
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
            	
            	//组装KPI下拉
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
	//初始化列表
	gridGrid = $("#indexList").kendoGrid({
		   dataSource:dataSource,
		   groupable: false,
	       sortable: true,
	       resizable: true,
	       columnMenu: false,
	       reorderable: true,
	   	   pageable: true,
	       ageable: {
	    	  refresh: true,
	    	 // pageSizes: true,
	    	  buttonCount: 5,
	      },
	      toolbar: kendo.template($("#template").html()),
	        columns: [
	                  { field: "kpiId", title:"指标ID",width: 50},
	                  { field: "kpiCategory", title:"指标类型",width: 50,
	                	template: "<span  title='#:kpiTypeValue[kpiCategory]#'>#:kpiTypeValue[kpiCategory]#</span>"
	                  },
	                  { field: "cnName", title:"指标名称",width: 70},
	                  { field: "threshold", title:"门限",width: 70,
	                	  template:function(dataItem){
	                		  return dataItem.threshold+dataItem.relationThreshold+"&nbsp; &nbsp;扣分:"+dataItem.scoreThreshold;
	                	  }
	                  },
	                  { field: "minValOne", title:"KPI值区间扣分1",width: 100,
				        	  template:function(dataItem){
				        		  return dataItem.minValOne+dataItem.operatorMin+"&nbsp; &nbsp;"+dataItem.operatorMax+dataItem.maxValOne
				        		  +"     扣分: "+dataItem.scoreOne;
				        	  }
				       },
				       { field: "minValTwo", title:"KPI值区间扣分2",width: 100,
				        	  template:function(dataItem){
				        		  return dataItem.minValTwo+dataItem.operatorMin+"&nbsp; &nbsp;"+dataItem.operatorMax+dataItem.maxValTwo
				        		  +"  扣分: "+dataItem.scoreTwo;
				        	  }
				       },
				       { field: "minValThree", title:"KPI值区间扣分3",width: 100,
				        	  template:function(dataItem){
				        		  return dataItem.minValThree+dataItem.operatorMin+"&nbsp; &nbsp;"+dataItem.operatorMax+dataItem.maxValThree
				        		  +"&nbsp; &nbsp;扣分: "+dataItem.scoreThree;
				        	  }
				       },
				       { field: "minValFour", title:"KPI值区间扣分4",width: 100,
				        	  template:function(dataItem){
				        		  return dataItem.minValFour+dataItem.operatorMin+"&nbsp; &nbsp;"+dataItem.operatorMax+dataItem.maxValFour
				        		  +"&nbsp; &nbsp;扣分: "+dataItem.scoreFour;
				        	  }
				       },
				       { field: "", title:"KPI值区间扣分5",width: 100,
				        	  template:function(dataItem){
				        		  if(dataItem.maxValFive==null || dataItem.maxValFive==""){
				        				return dataItem.minValFive+dataItem.operatorMin+"&nbsp; &nbsp;扣分: "+dataItem.scoreFive;
				        		  }
				        		  return dataItem.minValFive+dataItem.operatorMin+"&nbsp; &nbsp;"+dataItem.operatorMax+dataItem.maxValFive
				        		  	+"&nbsp; &nbsp;扣分: "+dataItem.scoreFive;
				        	  }
				       },
				       { field: "minValSix", title:"KPI值区间扣分6",width: 100,
				        	  template:function(dataItem){
				        		  if(dataItem.minValSix == null){
				        			  return "";
				        		  }
				        		return dataItem.minValSix+dataItem.operatorMin+"     "+dataItem.operatorMax+dataItem.maxValSix
				        		+"&nbsp; &nbsp;扣分: "+dataItem.scoreSix;
				        		  
				        	  }
				       },
	                  { field: "hasThreshold", title:"是否有阀值",hidden:true},
	                  /*{width: 70,
	  					template: "<a class='updateBtn btn btn-warning btn-xs'>修改</a><a class='deleteBtn btn btn-danger btn-xs'>删除</a>",
	  					encoded: false,
	  					title: "<span  title='操作'>操作</span>"
	  				}*/]
				}).data("kendoGrid");
		
		
		//add指标名称下拉
		//initKpiName();
		//initPeriod("period");  //周期下拉
		//initPeriod("addPeriod");
		
		//指标类型
		kpiType();
		hasThresholdValue();
		
		//查询
		$('#searchBtn').on('click', function() {
		
			searchParams.kpiId = $('#kpiIdInput').val();
			if(!checkContentLength(searchParams.kpiId,10,"指标ID")){return;};
			$("#indexList").data("kendoGrid").pager.page(1);
			//条件查询重新加载 
			dataSource.read(searchParams);
		});
		
		// 重置
		$('#resetBtn').click(function(){
			$('#kpiIdInput').val('');
			searchParams.kpiId ="";
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
			var data = gridGrid.dataItem($(this).closest("tr"));
			postParams.id= data.id;
			modalObj.setModal(data);
			
		});
		
		$('body').delegate(".deleteBtn", 'click', function() {
			var data = gridGrid.dataItem($(this).closest("tr"));
			if(confirm("确认删除吗？")){
				deleteData(data.id);
			}
		});
		
		//弹窗 添加 修改 
		modalObj = {
				
			//清空弹窗内的数据
			clearModal: function(){
				postParams.id="";
				//$('#addCnNameDown').data("kendoDropDownList").text("");
				//$('#addPeriod').data("kendoDropDownList").value("");
				$('#addCnNameDown').val("");
				$('#addPeriod').val("");
				$('#kpiType').data("kendoDropDownList").value("");
				$("#kpiIdValue").val("");  
				$("#method").val("");
				$("#relationValue").val("");
				$("#thresholdValue").val("");
				$("#ceilVal").val("");
				$("#floorVal").val("");
				$("#ceilScoreValue").val("");
				$("#floorScoreValue").val("");
			},
			
			//设置弹窗内的数据
			setModal: function(data){
				
			//	$('#addCnNameDown').data("kendoDropDownList").text(data.cnName);
			//	$('#addPeriod').data("kendoDropDownList").value(data.cycle);
				$('#kpiType').data("kendoDropDownList").value(data.kpiCategory);
				$('#addCnNameDown').val(data.cnName);
				$('#addPeriod').val(data.cycle);
				$('#hasThresholdValue').data("kendoDropDownList").value(data.hasThreshold);  //是否有阀值  （0：没有，1：有）
				$("#kpiIdValue").val(data.kpiId);
				$("#method").val(data.scoreRule);
				$("#thresholdValue").val(data.threshold);
				$("#relationValue").val(data.relation);
				$("#ceilVal").val(data.ceilVal);
				$("#floorVal").val(data.floorVal);
				$("#ceilScoreValue").val(data.ceilScore);
				$("#floorScoreValue").val(data.floorScore);
				//临时存放值
				kpiId = data.kpiId;
				kpiName =data.cnName;
			},
			
			//弹窗保存按钮
			saveBtn: function(){
				$("#saveBtn").on("click",function(){
					postParams.kpiId = $("#kpiIdValue").val();
					postParams.cnName = $("#addCnNameDown").val();
					postParams.scoreRule = $("#method").val();
					postParams.cycle = $("#addPeriod").val();
					postParams.relation = $("#relationValue").val();
					postParams.threshold = $("#thresholdValue").val();
					postParams.ceilVal = $("#ceilVal").val();
					postParams.floorVal = $("#floorVal").val();
					postParams.ceilScore = $("#ceilScoreValue").val();
					postParams.floorScore = $("#floorScoreValue").val();
					postParams.kpiCategory = $("#kpiType").val();
					postParams.hasThreshold = $("#hasThresholdValue").val();  //是否有阈值(0:没有,1:有)',  不能为空
					postParams.createTime = new Date();
					
					if(checkIsNull(postParams.kpiId,'KPIID')){return;}
					if(!checkContentLength(postParams.kpiId,10,'KPIID')){return;};
					if(checkIsNull(postParams.kpiCategory,'KPI类型')){return;};
					if(!DecimalAndNumber(postParams.threshold,'门限判断')){return;};  //or小数
					if(!checkContentLength(postParams.threshold,10,'门限判断')){return;};
					if(!validataNumber(postParams.ceilVal,'上限')){return;};
					if(!validataNumber(postParams.floorVal,'下限')){return;};
					if(!validataNumber(postParams.ceilScore,'上限扣除分数')){return;};
					if(!validataNumber(postParams.floorScore,'下限扣除分数')){return;};
					if(!validata(postParams.cycle,'判断周期')){return;};
					if(!judgingMethod(postParams.relation,'门限判断符')){return;};
					if(checkIsNull(postParams.hasThreshold,'是否有阈值')){return;};
					//验证重复
					var inputKpiId = $("#kpiIdValue").val();
					var inputKpiName = $("#addCnNameDown").val();
					if(kpiName!= inputKpiName){
						VerificationRepeat(postParams.cnName,2);
					}
					if(inputKpiId!=kpiId){
						VerificationRepeat(postParams.kpiId,1);
					}
					
					if(flag==0){
						if(postParams.id!="" && typeof(postParams.id)!="undefined"){
							postAddAndEdit('PATCH','rest/kpiIndex/'+postParams.id);
						}else{
							postAddAndEdit('POST','rest/kpiIndex/');
						}
					}
				});
			}
		};
		

	//导出
        $("#btn-export").on("click",function(){
        	var kpiId = $('#kpiIdInput').val();
        	if(kpiId =='null' || kpiId==null){
        		kpiId=='';
        	}
        	window.location.href = "kpiIndex/exportFile?kpiId=" +kpiId;
        	//+"&cnName="+$('#cnNameDown').val()+"&cycle="+$('#period').val();
		});
		
/*		//模板下载
        $("#downFile").on("click",function(){
        	downFile();
		});*/
		
	/*	//上传
		$("#upload").kendoUpload({
			localization:{
				"select": "选择上传文件……",
				"dropFilesHere": "拖拽文件到此区域",
			},
			multiple: true,
			async: {
				saveUrl: "kpiIndex/uploadFile",
				//removeUrl: "remove",
				autoUpload: true,
			},
		//	template: kendo.template($('#fileTemplate').html()),
			complete: function(){
				//console.log("complete");
				//window.location.href='paraProcessing-addUploadList.html';
			},
			success: function(){
				infoTip({content: "上传成功！",color:"#08ef46"});
				dataSource.read(searchParams);
			},
			error: function(){
				infoTip({content: "上传失败！",color:"#f60a0a"});
			}
		});*/
		//保存
		modalObj.saveBtn();
});

//添加、修改，数据验证是否重复
function VerificationRepeat(searchFild,valiDateFild){
	
	$.ajax({
		url : "kpiIndex/VerificationRepeat",
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
					infoTip({content: "指标ID不允许重复！",color:"#f60a0a"});
				}else{
					infoTip({content: "指标名称不允许重复！",color:"#f60a0a"});
				}
			}
		},
		fail : function(data) {
			infoTip({content: "验证失败！",color:"#f60a0a"});
		}
	});
}

function deleteData(id){
	
	$.ajax({
		url:"rest/kpiIndex/"+id,
		type:"delete",
		dataType:"json",
		contentType:"application/json;charset=utf-8",
		data:kendo.stringify(postParams),
		success:function(data){
			infoTip({content: "删除成功！",color:"#08ef46"});
			dataSource.read(searchParams);
		},
		fail:function(error){
			infoTip({content: "删除失败！",color:"#f60a0a"});
		},
		
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
			infoTip({content: "保存成功！",color:"#08ef46"});
			dataSource.read(searchParams);
		},
		complete: function(XMLHttpRequest, textStatus) {
			if(XMLHttpRequest.status==201){
				$('#myModal').modal('hide');
				infoTip({content: "保存成功！",color:"#08ef46"});
				dataSource.read(searchParams);
			}
		},
		fail:function(error){
			infoTip({content: "保存失败！",color:"#f60a0a"});
		},
		
	});
}


//模板下载
function downFile(){
	window.location.href="kpiIndex/downFile";
		 /*  +"params="+itemChecked[0].params
		   +"&defaultValue="+itemChecked[0].defaultValue
	       +"&checkName="+itemChecked[0].name;*/
}


/*function initPeriod(selectId){
	//周期下拉
	$("#"+selectId).kendoDropDownList({
	 	autoBind: false,
	 	optionLabel:"--请选择周期--",
		dataSource: ["小时","天","周","月"]
		//filter: "contains"
	});
}*/

/*function initKpiName(){
	//指标名称下拉
	$("#addCnNameDown").kendoDropDownList({
		autoBind: false,
		optionLabel:"--请选择指标名称--",
	 	dataTextField: "cnName",
	    dataValueField: "cnName",
		dataSource: dataSource
		//filter: "contains"
	});
}*/

function kpiType(){
	//下拉
	var kpiType =[
	              {text:'无',value:0},
	              {text:'接入性',value:1},
	              {text:'保持性',value:2},
	              {text:'移动性',value:3}
	    ];
	
	$("#kpiType").kendoDropDownList({
	 	optionLabel:"--请选择KPI类型--",
	 	dataTextField: "text",
	    dataValueField: "value",
		dataSource: kpiType,
		autoBind: false
		//filter: "contains"
	});
}
//是否有阀值
function hasThresholdValue(){
	//下拉
	var hasThreshold =[
	              {text:'没有',value:"0"},
	              {text:'有',value:"1"},
	    ];
	
	$("#hasThresholdValue").kendoDropDownList({
	 	//optionLabel:"--请选择--",
	 	dataTextField: "text",
	    dataValueField: "value",
		dataSource: hasThreshold,
		//autoBind: false,
		//filter: "contains",
		index:0
	});
}
//判断是否输入的是 > < =.....
function judgingMethod(value,nameValue){
	if(value==">" || value==">=" || value=="<" || value=="<=" || value=="!=" || value=="="){
        return true;
    }
	showNotify(nameValue+"为>.<.>=.<=.!=.=","warning");
	return false;
}
