kendo.culture("zh-CN");
var searchParams = {};

//告警查询
var dataSource;
$(function() {
	var today =  new Date();
	var alarmDelay = getQueryString("alarmDelay");
	var areaCode = getQueryString("areaCode");
	var alarmNo = getQueryString("alarmNo");
	var cityCode = getQueryString("cityCode");
	var tableName = getQueryString("tableName");
	var startDate =getQueryString("startDate");
	var endDate = getQueryString("endDate");
	var neCode = getQueryString("neCode");
	var manufacturer = getQueryString("manufacturer");
	var type = getQueryString("type");
	searchParams={
			neCode:(neCode && neCode!=null && neCode !="null")?neCode:"",
			alarmNo:(alarmNo && alarmNo!=null && alarmNo !="null")?alarmNo:"",
			alarmDelay:(alarmDelay && alarmDelay !=  null && alarmDelay !="null")?alarmDelay:"",
			areaCode: (areaCode && areaCode!=null && areaCode !="null")?areaCode:"",
			cityCode: (cityCode && cityCode!=null && cityCode !="null")?cityCode:"",
			startDate: startDate?startDate: new Date(today.getFullYear(),today.getMonth(),today.getDate()-1),
			endDate: (endDate &&endDate!=null && endDate !="null")?endDate: today,
			manufacturer:(manufacturer && manufacturer!=null && manufacturer!="null")?manufacturer:"",
			type:type?type:"",	
			tableName: (tableName && tableName!=null && tableName !="null")?tableName:"bts_alarm_delay_day"
	}
	
	if(searchParams.type==1){
		$('#returnBtn').show();
	}else{
		$('#returnBtn').hide();
	}
	
	//查询条件
	var startTime = $("#startDate").kendoDatePicker({
		format: "yyyy-MM-dd",
		value: searchParams.startDate
	});
	var endTime = $("#endDate").kendoDatePicker({
		format: "yyyy-MM-dd",
		value: searchParams.endDate
	});
	
	$('#startDate').attr("disabled",true);
	$('#endDate').attr("disabled",true);
	
	//初始化城市
	initCityList();
	//初始化地区
	initAreaList();
	//厂家下拉
	manufacturerFx();
	
	dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "alarm/DelaySearch"
            },
            parameterMap: function (options, operation) { 
                if (operation == "read") {
	                 searchParams.page = options.page;
	                 searchParams.pageSize = options.pageSize;
	                 searchParams.startDate = $('#startDate').val(); //时间
	                 searchParams.endDate = $('#endDate').val();
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
	
	//查询
	$('#btn-search').on('click', function() {
		searchParams.alarmDelay = $('#alarmDelay').val();  //时长
		searchParams.cityCode = $('#cityCode').val();
		searchParams.areaCode = $('#areaCode').val();
		searchParams.tableName = $("#scoreType").val();
		searchParams.startDate = $('#startDate').val(); //时间
		searchParams.endDate = $('#endDate').val();
		searchParams.neCode = $('#neCode').val();
		searchParams.alarmNo = $('#alarmNo').val();
		searchParams.manufacturer = $('#manufacturer').val();
		
		setShowAndHide();
		//条件查询重新加载 
		$("#gridList").data("kendoGrid").pager.page(1);
		//dataSource.read(searchParams);
	});
	//重置
	$('#btn-reset').on('click', function() {
		$('#alarmDelay').val("");
		$('#neCode').val("");
		$('#alarmNo').val("");
		$('#cityCode').data("kendoDropDownList").text('');
		$('#areaCode').data("kendoDropDownList").text('');
		$('#manufacturer').data("kendoDropDownList").text('');
		searchParams.alarmDelay = "";
		searchParams.cityCode = "";
		searchParams.areaCode = "";
		searchParams.neCode = "";
		searchParams.alarmNo ="";
		
		var today = new Date();
		$('#endDate').data("kendoDatePicker").value(today);
		$('#startDate').data("kendoDatePicker").value(new Date(today.getFullYear(),today.getMonth(),today.getDate()-1));
		$('#scoreType').data("kendoDropDownList").select(0);
		searchParams.manufacturer="";
		
		var grid = $("#gridList").data("kendoGrid");
		grid.hideColumn(4);
		grid.hideColumn(5);
		grid.showColumn(3);
		dataSource.read(searchParams);
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
    	var alarmNo=$('#alarmNo').val();
    	if(alarmNo == 'null' || alarmNo == null){
    		alarmNo='';
    	}
    	
    	var startDate = $('#startDate').val();
    	if(startDate == 'null' || startDate == null){
    		startDate = '';
    	}
    	var endDate = $('#endDate').val();
    	if(endDate == 'null' || endDate == null){
    		endDate = '';
    	}
    	var alarmDelay = $('#alarmDelay').val();
    	if(alarmDelay == 'null' || alarmDelay == null){
    		alarmDelay='';
    	}
		window.location.href = "alarm/exportAlarmFile?tableName="+$("#scoreType").val()+"&startDate=" +startDate+
				"&endDate="+endDate+"&cityCode="+cityCode+"&areaCode="+areaCode+"&neCode="+neCode+
				"&manufacturer="+$('#manufacturer').val()+"&alarmDelay="+alarmDelay+"&alarmNo="+alarmNo;
	});
	
});

function setShowAndHide(){
	
	var grid = $("#gridList").data("kendoGrid");
	if(searchParams.tableName.indexOf('day')>=0){
		grid.hideColumn(5);
		grid.hideColumn(4);
		grid.showColumn(3);
	}else if(searchParams.tableName.indexOf('week')>=0){
		grid.hideColumn(5);
		grid.hideColumn(3);
		grid.showColumn(4);
	}else if(searchParams.tableName.indexOf('month')>=0){
		grid.hideColumn(3);
		grid.hideColumn(4);
		grid.showColumn(5);
	}
}

//列表
function initAlarmGrid(){
	
	$("#gridList").kendoGrid({
		
		dataSource: dataSource,
		reorderable: true,
		resizable: true,
		//	sortable: true,
		//columnMenu: true,
		pageable: true,
		//导出
		toolbar: kendo.template($("#template").html()),
		columns: [
		          {field: "area_name", title:"地市名称"}, 
		          {field: "city_name", title:"区县名称"}, 
		          {field: "ne_code",title: "基站ID"},
		          {field: "date_day", title:"日"}, 
		          {field: "date_week", title:"周",hidden: true},
		          {field: "date_month", title:"月",hidden: true},
		          {field: "alarm_no", title:"告警号"}, 
		          {field: "difftime",title: "告警时长(小时)",/*template:"#:difftime# (小时)"*/},
		          {field: "start_time",title: "告警开始时间"},
		          {field: "cancel_time",title: "告警取消时间"},
		          {
				    	 field: "manufacturer",
				    	 /*width: 110,*/
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
			 "sumType":"delay"
		},
		//async:false,
		success : function(data) {
			if(data){
				//console.log(data);
				$('#sumTxt').text("总时长(小时)：["+data+"]");
			}
		}
	});
	
}

function initScoreType(){
	var data = [
	            { text: "日", value: "bts_alarm_delay_day" },
	            { text: "周", value: "bts_alarm_delay_week" },
	            { text: "月", value: "bts_alarm_delay_month" }
	        ];
	//下拉
	$("#scoreType").kendoDropDownList({
		 	//autoBind: false,
		//	optionLabel:"--请选择统计方式--",
			dataSource:  data,
			dataTextField: "text",
	        dataValueField: "value",
	        select: function(e){
	        	//console.log($(e.item).text());
	        	if($(e.item).text()=='日'){
	        		$("#dateWrap").show();
	        	}else{
	        		$("#dateWrap").hide();
	        	}
	        }
		});
	
	//回显
	var va = searchParams.tableName;
	if(va!=""){
		$('#scoreType').data("kendoDropDownList").value(va);
		setShowAndHide();
	}else{
		$('#scoreType').data("kendoDropDownList").value("bts_alarm_delay_day");
	}

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
			//回显
			var va = searchParams.areaCode;
			if(va!=""){
				$('#areaCode').data("kendoDropDownList").value(va);
				getAreaData(va);
			}
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
