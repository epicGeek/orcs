/*
 * 指标评分查询
 * */
var dataSource;
var breakReasionDataSource= []; 
var columns =[];
var areaCode=getQueryString("areaCode");
var cityCode=getQueryString("cityCode");
var neCode=getQueryString("neCode");
var startCycle=getQueryString("startDate");
var endCycle=getQueryString("endDate");
var type=getQueryString("type");
//var tableName = getQueryString("tableName");
$(function() {
	kendo.culture("zh-CN");
	var today = new Date();
	searchParams={
			startCycle: (startCycle && startCycle != null && startCycle !="null")?startCycle:new Date(today.getFullYear(), today.getMonth(),today.getDate(),today.getHours()-5).Format("yyyy-MM-dd hh:00:00"),
			endCycle: (endCycle && endCycle!=null && endCycle !="null")?endCycle:new Date().Format("yyyy-MM-dd hh:00:00"),
			areaCode: (areaCode && areaCode!=null && areaCode !="null")?areaCode:"",
			cityCode: (cityCode && cityCode!=null && cityCode !="null")?cityCode:"",
			neCode: (neCode && neCode!=null && neCode !="null")?neCode:"",
		//	tableName:(tableName && tableName!=null && tableName != "null")?tableName:'cell_score_hour'
	}
	
	//返回按钮
	if(type==1){
		$("#returnBtn").show();
	}
	//查询条件
	var start = $("#btnTimeStart").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:00:00",
		value: searchParams.startCycle
	}).data("kendoDatePicker");
	
	var end =$("#btnTimeEnd").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:00:00",
		value: searchParams.endCycle
	}).data("kendoDatePicker");
	
	$("#btnTimeStart").attr("disabled",true);
	$("#btnTimeEnd").attr("disabled",true);

	dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "kpiScore/search"
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
            }
        },
        serverPaging : true,
		serverFiltering : true,
		serverSorting : true
    });
	//初始化列表
	$.when(initKpiMap()).done(function(){
		initKpiGrid();
	});
	
	initCityList();    //初始化市
	initAreaList();	//初始化区

	$("#neCode").val(searchParams.neCode);	
	//	条件查询按钮
	$('#searchBtn').on('click', function() {
		
	/*	if(tableName=='bts_score_week'){
			var d = $('#btnTimeStart').val();
			var ed= $('#btnTimeEnd').val();
			var s = new Date(d);
			var e=new Date(ed);
			var start = new Date(s-(s.getDay()-1)*86400000).Format("yyyy-MM-dd 00:00:00"); //当前所在周周一的日期
			var end = new  Date(e.getFullYear(),e.getMonth(),e.getDate()+6).Format("yyyy-MM-dd 23:00:00");
			searchParams.startCycle = start;
			searchParams.endCycle = end;
		}else{
		}*/
		searchParams.startCycle = $('#btnTimeStart').val();
		searchParams.endCycle = $('#btnTimeEnd').val();
		searchParams.areaCode = $('#areaCode').val(); //地区
		searchParams.cityCode = $('#cityCode').val(); //地市
		searchParams.neCode = $('#neCode').val(); //基站ID
		if(!checkContentLength(searchParams.neCode,10,'基站ID')){return;};
		//	$("#kpiScoreList").data("kendoGrid").pager.page(1);
		//条件查询重新加载 
		dataSource.read(searchParams);
	});
	
	// 重置
	$('#resetBtn').click(function(){
		$('#neCode').val(""); 
		$('#cityCode').data("kendoDropDownList").value(''); 
		$('#areaCode').data("kendoDropDownList").value('');
		$('#btnTimeStart').data("kendoDateTimePicker").value(new Date(today.getFullYear(),today.getMonth(),today.getDate()-1));
		$('#btnTimeEnd').data("kendoDateTimePicker").value(today);
		searchParams.neCode = "";
		searchParams.cityCode="";
		searchParams.areaCode="";
		dataSource.read(searchParams);
	});
	
});

function initKpiMap() {
	var dtd = $.Deferred(); // 新建一个Deferred对象
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "kpiIndex/getKpiRule",
		"success" : function(data) {
			$.each(data, function(index, item) {
				breakReasionDataSource.push({
					id : item.KPI_ID.toLowerCase()+"_score",
					name : item.KPI_NAME+"分数"
				});
			});
			columns.push({ field: "area_name", title:"地市",width: 90, locked: true, lockable: false});
			columns.push({ field: "city_name", title:"区县",width: 90, locked: true, lockable: false});
			columns.push({ field: "ne_code", title:"基站ID",width: 90, locked: true, lockable: false});
			columns.push({ field: "cycle_date", title:"时间", width: 160,locked: true, lockable: false/* template: function(dataItem) {
				if(tableName=='bts_score_week'){
					return dataItem.cycle_week+"周";
				}else if(tableName=='bts_score_day'){
					return dataItem.cycle_date;
				}else{
					return dataItem.cycle_date+" "+dataItem.cycle_hour+":00:00";
				} }*/
			});
			 var width;
			 
		    for (var i = 0; i < breakReasionDataSource.length; i++) {
				 
				 var KPI_ID_text = breakReasionDataSource[i].id.toLowerCase();	
				 if(breakReasionDataSource[i].name>14){
					 width=160;
				 }
				 else	 { width = 130;}
				 columns.push({
	    						field: KPI_ID_text,
	    						width: width,
	    						encoded: false,
	    						title: "<span class='tdOverFont' title='"+breakReasionDataSource[i].name+"'>"+breakReasionDataSource[i].name+"</span>"
	    		 });
			 }
		    columns.push({ field: "alarm_score", title:"基站告警分数",width:90,endcoded:false});
	//	    columns.push({field:'out_of_score',title:"退服分数",width:90,endcoded:false});
		    dtd.resolve(); // 改变Deferred对象的执行状态
		}
	});
    return dtd;
}

function initCityList(){
	
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "city/search",
		"success" : function(data) {
			$("#areaCode").kendoDropDownList({
				optionLabel:"--请选择地市--",
	            dataTextField: "areaCn",
	            dataValueField: "areaCode",
	            dataSource: data,
	            filter: "contains",
	            change: function(){
	            	var areaCode = $('#areaCode').val();
	            	getAreaData(areaCode);
	            }
	        });
			//回显
			var va = searchParams.areaCode;
			if(va!=""){
				$('#areaCode').data("kendoDropDownList").value(va);
				getAreaData(va);
			}
		}
	});
}
//根据地市code获取区县
function getAreaData(cityCode){
	
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "area/search",
		data:{
			"cityCode":cityCode
		},
		"success" : function(data) {
			$("#cityCode").data("kendoDropDownList").setDataSource(data);
			$('#cityCode').data("kendoDropDownList").value(searchParams.cityCode);
		}
	});
}

function initAreaList(){
	$("#cityCode").kendoDropDownList({
		optionLabel:"--请选择区县--",
        dataTextField: "cityCn",
        dataValueField: "cityCode",
        dataSource: [],
        filter: "contains"
    });
}

//列表
function initKpiGrid(){
	$("#kpiScoreList").kendoGrid({
		dataSource: dataSource,
		reorderable: true,
		resizable: true,
		sortable: true,
		pageable: true,
		toolbar: kendo.template($("#template").html()),  	//导出
		columns:columns,
		dataBound: function(){
			//导出
		    $("#btn_export").on("click",function(){
		    	window.location.href = "kpiScore/exportFile?areaCode="+$('#areaCode').val()+"&cityCode="+$('#cityCode').val()
		    	                                          +"&neCode="+$('#neCode').val()+"&startCycle="+$('#btnTimeStart').val()+"&endCycle="+$('#btnTimeEnd').val();
		    });   
		}
		          
	}).data("kendoGrid");
}