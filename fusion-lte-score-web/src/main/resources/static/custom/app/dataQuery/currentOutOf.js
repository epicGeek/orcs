
//告警查询
var dataSource;
var start_time;
var cancel_time;
var listGrid;
var searchParams;

$(function() {
	
	kendo.culture("zh-CN");

	var today = new Date();
	//获得参数
	var neCode = getQueryString("neCode");
	var areaCode = getQueryString("areaCode");
	var cityCode = getQueryString("cityCode");
	var startDate = getQueryString("startDate");
	var endDate = getQueryString("endDate");
	var type = getQueryString("type");
	var manufacturer = getQueryString("manufacturer");
	var tableName = getQueryString("tableName");
	searchParams = {
		neCode: (neCode && neCode != null && neCode !="null")?neCode:"",
		alarmTitle:"",
		areaCode: (areaCode && areaCode!=null && areaCode !="null")?areaCode:"",
		cityCode: (cityCode && cityCode!=null && cityCode !="null")?cityCode:"",
		startDate: (startDate && startDate!=null && startDate !="null")?startDate:new Date(today.getFullYear(), today.getMonth(),today.getDate(),today.getHours()-5).Format("yyyy-MM-dd hh:00:00"),
		endDate: (endDate && endDate!=null && endDate !="null")?endDate:today.Format("yyyy-MM-dd hh:00:00"),
		manufacturer:(manufacturer && manufacturer!=null && manufacturer!="null")?manufacturer:"",
		type:type?type:"",
		alarmDelay:"",
		tableName: (tableName && tableName!=null && tableName !="null")?tableName:"ices_alarm_out_of_delay"
	}
	
	//初始化城市
	initCityList();
	//初始化地区
	initAreaList();
	
	//基站id回显
	$('#neCode').val(searchParams.neCode);
	
	//查询条件
	$("#startDate").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:00:00",
		value:searchParams.startDate
	});
	$("#endDate").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:00:00",
		value:searchParams.endDate
	});
	$("#startDate").attr('disabled',true);
	$("#endDate").attr('disabled',true)
	
	dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "POST",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "outOfQuery/search"
            },
            parameterMap: function (options, operation) { 
                if (operation == "read") {
	                 searchParams.page = options.page;
	                 searchParams.pageSize = options.pageSize;
	                 return  kendo.stringify(searchParams);
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
	
	initAlarmGrid();
	//厂家下拉
	manufacturerFx();
	
	tableNameBtn(searchParams.tableName);
	
	$('.blockquote .btn').on('click', function(){
		var table = $(this).children("input").val();
		tableNameBtn(table,0);
	});
	
	//页面显示与隐藏【返回】按钮
	if(type==1){
		$("#returnBtn").show();
	}else{
		$("#returnBtn").hide();
	}
	$("#returnBtn").on('click',function(){
		if(type==1){
			history.go(-1); //	javascript:history.go(-1);
		}
	});
	
	//查询
	$('#btn-search').on('click', function() {
		searchParams.neCode = $('#neCode').val();
		searchParams.alarmTitle = $('#alarmTitle').val();
		searchParams.cityCode = $('#cityCode').val();
		searchParams.areaCode = $('#areaCode').val();
		searchParams.startDate = $('#startDate').val();
		searchParams.endDate = $('#endDate').val();
		searchParams.manufacturer = $('#manufacturer').val();
		searchParams.alarmDelay = $("#alarmDelay").val();
		if(!checkContentLength(searchParams.neCode,10,'基站ID')){return;};
		//$("#outOfQueryList").data("kendoGrid").pager.page(1);
		//条件查询重新加载 
		dataSource.read(searchParams);
	});
	//重置
	$('#btn-reset').on('click', function() {
		$('#neCode').val("");
		$('#alarmDelay').val("");
		$('#alarmTitle').val("");
		$('#cityCode').data("kendoDropDownList").text('');
		$('#areaCode').data("kendoDropDownList").text('');
		$('#manufacturer').data("kendoDropDownList").text('');
		$('#startDate').data("kendoDateTimePicker").value(new Date(today.getFullYear(), today.getMonth(),today.getDate()-1));
		$('#endDate').data("kendoDateTimePicker").value(today);
		
		searchParams.neCode = "";
		searchParams.cityCode = "";
		searchParams.areaCode = "";
		searchParams.alarmTitle = "";
		searchParams.manufacturer="";
		searchParams.alarmDelay="";
		
		dataSource.read(searchParams);
	});
	
	//导出
    $("#btn-export").on("click",function(){
    	var neCode = $('#neCode').val();
    	if(neCode == 'null' || neCode == null){
    		neCode = '';
    	}
    	var areaCode = $('#areaCode').val();
    	if(areaCode == 'null' || areaCode == null){
    		areaCode = '';
    	}
    	var cityCode = $('#cityCode').val();
    	if(cityCode == 'null' || cityCode == null){
    		cityCode = '';
    	}
    	var startDate = $('#startDate').val(); //时间
    	if(startDate == 'null' || startDate == null){
    		startDate = '';
    	}
    	var endDate = $('#endDate').val(); //
    	if(endDate == 'null' || endDate == null){
    		endDate = '';
    	} 
    	var manufacturer = $('#manufacturer').val();
    	if(manufacturer == 'null' || manufacturer == null){
    		manufacturer = '';
    	}
    	var alarmDelay = $('#alarmDelay').val();
    	if(alarmDelay == 'null' || alarmDelay == null){
    		alarmDelay='';
    	}
    	window.location.href = "outOfQuery/outOfExportFile?neCode=" +neCode
    						+"&cityCode="+cityCode+"&areaCode="+areaCode+"&startDate="+startDate+"&endDate="+endDate
    						+"&alarmTitle"+alarmTitle+"&manufacturer"+manufacturer+"&alarmDelay"+alarmDelay+"&tableName="+searchParams.tableName;
	});
});
//列表
function initAlarmGrid(){
	
	listGrid =$("#outOfQueryList").kendoGrid({
		dataSource: dataSource,
		reorderable: true,
		resizable: true,
		sortable: true,
		//columnMenu: true,
		pageable: true,
		//导出
		toolbar: kendo.template($("#template").html()),
		columns: [
		          { field: "area_name", title:"地市名称",width: 75},
		          { field: "city_name", title:"区县名称",width: 70},
		          { field: "ne_code", title:"基站ID",width: 75},
		          { field: "alarm_title", title:"告警码",width: 75},
		          { field: "ne_name_cn",title:"基站名称",width:150},
		          { field: "alarm_desc", title:"告警信息"},
		          { field: "start_time", title:"告警开始时间",width: 150},
		          { field: "cancel_time", title:"告警取消时间",width: 150},
		          {field:"delay",title:"时长(小时)",width:95},
		          {field:"frequency",title:"频次(次数)",width:95,hide:true},
		          { field: "manufacturer",width: 60,
	                	 template: function(dataItem) {
	                		 manufacturer=0;
	  					var str="";
						switch(dataItem.manufacturer){
						case '0':
							str="诺基亚";
							break;
						case '1':
							str="华为";
							break;
						case '2':
							str="中兴";
							break;
						case '3':
							str="大唐";
							break;
						case '4':
							str="普天";
							break;
					    default:
					    	str="未知";
					        break;
						}
		        		return "<span class='tdOverFont' title='"+str+"'>"+str+"</span>";
		            },
		            encoded: false,
					title: "<span class='tdOverFont' title='厂家'>厂家</span>",
					lockable: false }
		          ]
	}).data("kendoGrid");
}

//周 月 日 按钮切换
function tableNameBtn(tableName,type){

		if(tableName=='ices_alarm_out_of_delay'){
			listGrid.showColumn("delay");
			listGrid.hideColumn("frequency");
		}else if(tableName=='ices_alarm_out_of_frequency'){
			listGrid.hideColumn("delay");
			listGrid.showColumn("frequency");
		}
		//返回 状态显示 
		$(".blockquote .btn input[type='radio'][value="+tableName+"]").parent().addClass('active');
		searchParams.tableName = tableName;
		dataSource.read(searchParams);
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

function cancelTimes(){
	var data = [{text:"全部",value:"1"},{text:"活动",value:"2"},
	            		{text:"非活动",value:"3"},];
	$("#cancelType").kendoDropDownList({
		autoBind: false,
		optionLabel:"--请选择--",
		dataTextField: "text",
		dataValueField: "value",
		dataSource: data,
		filter: "contains"
	});
}

function manufacturerFx(){
	var data = [{text:"诺基亚",value:"0"},{text:"华为",value:"1"},{text:"中兴",value:"2"},
	            		{text:"大唐",value:"3"},{text:"普天",value:"4"}];
	$("#manufacturer").kendoDropDownList({
		autoBind: false,
		optionLabel:"--请选择--",
		dataTextField: "text",
		dataValueField: "value",
		dataSource: data,
		filter: "contains"
	});
}
	