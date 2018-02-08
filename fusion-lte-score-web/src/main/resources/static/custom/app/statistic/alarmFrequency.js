kendo.culture("zh-CN");
//告警查询
var dataSource;
var searchParams = {manufacturer:'',startDate:'',endDate:'',neCode:'',areaCode:'',cityCode:'',alarmFreq:'',tableName:'bts_alarm_frequency_day'};

$(function() {
	
	//初始化城市
	initCityList();
	
	//初始化地区
	initAreaList();
	
	var today = new Date();
	//查询条件
	var startTime = $("#startDate").kendoDatePicker({
		format: "yyyy-MM-dd",
		value: new Date(today.getFullYear(),today.getMonth(),today.getDate()-1)
	});
	var endTime = $("#endDate").kendoDatePicker({
		format: "yyyy-MM-dd",
		value: today
	});
	
	$('#startDate').attr("disabled",true);
	$('#endDate').attr("disabled",true);
	
	dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "alarm/freqSearch"
            },
            parameterMap: function (options, operation) { 
                if (operation == "read") {
	                 searchParams.page = options.page;
	                 searchParams.pageSize = options.pageSize;
	                 searchParams.startDate = $("#startDate").val();
	                 searchParams.endDate = $("#endDate").val();
	                 return searchParams;
                } else {
                	return kendo.stringify(options);
                }
                
            }
        },
        pageSize: 10,
        schema: {
            data: function (data) {
            	getTotal();
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
	
	initAlarmGrid();
	
	initScoreType();
	
	manufacturerFx();
	
	//查询
	$('#btnSearch').on('click', function() {
		searchParams.neCode = $('#neCode').val();
		searchParams.cityCode = $('#cityCode').val();
		searchParams.areaCode = $('#areaCode').val();
		searchParams.alarmFreq = $('#alarmFreq').val();
		searchParams.tableName = $("#scoreType").val();
		searchParams.startDate = $('#startDate').val(); //时间
		searchParams.endDate = $('#endDate').val();
		searchParams.manufacturer = $('#manufacturer').val();
		
		var grid = $("#gridList").data("kendoGrid");
		if(searchParams.tableName.indexOf('day')>=0){
			grid.hideColumn(4);
			grid.hideColumn(5);
			grid.showColumn(3);
		}else if(searchParams.tableName.indexOf('week')>=0){
			grid.hideColumn(3);
			grid.hideColumn(5);
			grid.showColumn(4);
		}else if(searchParams.tableName.indexOf('month')>=0){
			grid.hideColumn(4);
			grid.hideColumn(3);
			grid.showColumn(5);
		}
		//grid.dataSource.add({ name: "John Doe", age: 33 });
		if(!checkContentLength(searchParams.neCode,11,'小区id')){return;};
		$("#gridList").data("kendoGrid").pager.page(1);
		//条件查询重新加载 
		dataSource.read(searchParams);
	});
	//重置
	$('#btnReset').on('click', function() {
		$('#neCode').val("");
		$('#alarmFreq').val("");
		$('#cityCode').data("kendoDropDownList").text('');
		$('#areaCode').data("kendoDropDownList").text('');
		$('#scoreType').data("kendoDropDownList").select(0);
		$('#manufacturer').data("kendoDropDownList").text('');
		searchParams.manufacturer="";
		searchParams.neCode = "";
		searchParams.cityCode = "";
		searchParams.areaCode = "";
		searchParams.alarmFreq = "";
		//清除
		var today = new Date();
		$('#endDate').data("kendoDatePicker").value(today);
		$('#startDate').data("kendoDatePicker").value(new Date(today.getFullYear(),today.getMonth(),today.getDate()-1));

	//	searchParams.tableName = "bts_alarm_frequency_day";
		dataSource.read(searchParams);
		var grid = $("#gridList").data("kendoGrid");
		grid.hideColumn(4);
		grid.hideColumn(5);
		grid.showColumn(3);
	});
	
	//导出
	  $("#btn-export").on("click",function(){
		  
		  var areaCode = $('#areaCode').val();
	    	if(areaCode == 'null' || areaCode == null){
	    		areaCode = '';
	    	}
	    	var cityCode = $('#cityCode').val();
	    	if(cityCode == 'null' || cityCode == null){
	    		cityCode = '';
	    	}
	    	var neCode = $('#neCode').val();
	    	if(neCode == 'null' || neCode == null){
	    		neCode = '';
	    	}
	    	var alarmFreq = $('#alarmFreq').val();
	    	if(alarmFreq =='null' || alarmFreq ==null){
	    		alarmFreq='';
	    	}
	    	var manufacturer=$('#manufacturer').val();
	    	if(manufacturer =='null' || manufacturer ==null){
	    		manufacturer='';
	    	}
		  window.location.href="alarm/frequencyExportFile?startDate="+$('#startDate').val()+
		  "&endDate="+$('#endDate').val()+"&alarmFreq="+alarmFreq+"&neCode="+neCode+"&areaCode="+areaCode+
		  "&cityCode="+cityCode+"&manufacturer="+manufacturer+"&tableName="+$("#scoreType").val();
		  
	  });
	
});
//列表
function initAlarmGrid(){
	
	$("#gridList").kendoGrid({
		dataSource: dataSource,
		reorderable: true,
		resizable: true,
		sortable: true,
		pageable: true,
		toolbar: kendo.template($("#template").html()),
		columns: [
		          {field: "area_name",title:"地市名称"}, 
		          {field: "city_name", title:"区县名称"},
		          {field: "ne_code",title: "基站ID"},
		          {field: "date_day", title:"日"},
		          {field: "date_week", title:"周",hidden: true},
		          {field: "date_month", title:"月",hidden: true},
		          {field: "alarm_no", title:"告警号"}, 
		          {field: "difftime",title: "告警次数"}, 
		          {
				    	 field: "manufacturer",
				    	/* width: 90,*/
				    	 template: function(dataItem) {
				    		 var str="";					
				    		 switch(dataItem.manufacturer){
				    		 case '0':str="诺基亚";
				    		 break;
				    		 case '1':str="华为";
				    		 break;
				    		 case '2':str="中兴";
				    		 break;
				    		 case '3':str="大唐";
				    		 break;
				    		 case '4':str="普天";
				    		 break;
				    		 case '5':str="全部厂家";
				    		 break;	
				    		 default:str="未知";
				    		 break;
				    		 
				    		 }
				    		 return "<span class='tdOverFont' title='"+str+"'>"+str+"</span>";
				    	 },
				    	 title: "<span class='tdOverFont' title='厂家'>厂家</span>"}
		   ]
	}).data("kendoGrid");
	
}

function initCityList(){
	var dtd = $.Deferred(); // 新建一个Deferred对象
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "city/search",
		"async":false,
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
		/*	//回显
			var va = searchParams.areaCode;
			if(va!=""){
				$('#areaCode').data("kendoDropDownList").value(va);
				getAreaData(va);
			}*/
			dtd.resolve(); // 改变Deferred对象的执行状态
		}
	});
	return dtd;
}

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
		optionLabel:"--请选择地区--",
        dataTextField: "cityCn",
        dataValueField: "cityCode",
        dataSource: [],
        filter: "contains"
    });
}

function initScoreType(){
	var data = [
	            { text: "日", value: "bts_alarm_frequency_day" },
	            { text: "周", value: "bts_alarm_frequency_week" },
	            { text: "月", value: "bts_alarm_frequency_month" }
	        ];
	//下拉
	$("#scoreType").kendoDropDownList({
		 	//autoBind: false,
		//	optionLabel:"--请选择统计方式--",
			dataSource:  data,
			dataTextField: "text",
	        dataValueField: "value",
	        index: 0,
	        select: function(e){
	        	//console.log($(e.item).text());
	        	if($(e.item).text()=='日'){
	        		$("#dateWrap").show();
	        	}else{
	        		$("#dateWrap").hide();
	        	}
	        }
		});

	//$('#scoreType').data("kendoDropDownList").value("bts_alarm_frequency_day");
}

function getTotal(){
	$.ajax({
		type : "POST",
		url : "alarm/getTotalDelay",
		data:{
			"areaCode":$('#areaCode').val(),
			 "cityCode":$('#cityCode').val(),
			 "startDate":$('#startDate').val(),
			 "endDate":$("#endDate").val(),
			 "tableName":$("#scoreType").val(),
			 "sumType":"fre"
		},
		//async:false,
		success : function(data) {
			if(data){
				//console.log(data);
				$('#sumTxt').text("总次数：["+data+"]");
			}
		}
	});
	
}

function manufacturerFx(){
	
	var data = [{text:"诺基亚",value:"0"},{text:"华为",value:"1"},{text:"中兴",value:"2"},{text:"大唐",value:"3"},{text:"普天",value:"4"}];
	
	$("#manufacturer").kendoDropDownList({
		autoBind: false,
		optionLabel:"--请选择--",
		dataTextField: "text",
		dataValueField: "value",
		dataSource: data,
		filter: "contains"
	});
}
/*
* 接收url参数
*  调用方法：
*	alert(GetQueryString("参数名1"));
*/
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]); return null;
} 