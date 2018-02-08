kendo.culture("zh-CN");

/**
 * KPI查询
 * */
var kpiDataSource = [];
var dataSource;
var kpiName = "";
var columns = [ { field: "cycle", title:"时间",width: 200},
                { field: "area_name", title:"地市",width: 140},
	            { field: "city_name", title:"区县",width: 140},
	            { field: "ne_code", title:"基站ID",width: 140},
	            { field: "cell_id", title:"小区ID",width: 140},
	            { field: "cell_name_cn", title:"小区名称",width: 280}
		       ];

var KPI_ID_text;

//var searchParams = {areaCode:'',cityCode:'',cellId:'',startCycle:'',endCycle:'',tableName:'kpi_001_value'};

var kpiGrid;//列表grid

$(function() {
	
	var today = new Date();
	//获得参数
	var areaCode = getQueryString("areaCode");
	var cityCode = getQueryString("cityCode");
	var cellId = getQueryString("cellId");
	var startCycle = getQueryString("startCycle");
	//var endCycle = parseInt(getQueryString("endCycle"));
	var endCycle = getQueryString("endCycle");
	var tableName = getQueryString("tableName");
	var cell_name = decodeURI(getQueryString("cell_name"));
     kpiName= decodeURI(getQueryString("kpiName"));
	var type = getQueryString("type");
	searchParams = {
		cellId: (cellId && cellId != null && cellId !="null")?cellId:"",
		cell_name: (cell_name && cell_name != null && cell_name !="null")?cell_name:"",
		areaCode: (areaCode && areaCode != null && areaCode !="null")?areaCode:"",
		cityCode: (cityCode && cityCode!=null && cityCode !="null")?cityCode:"",
		startCycle: (startCycle && startCycle!=null && startCycle !="null")?startCycle:new Date(today.getFullYear(), today.getMonth(),today.getDate()),
		endCycle: (endCycle && endCycle!=null && endCycle !="null")?endCycle:today,
		kpiName:(kpiName&&kpiName != null && kpiName != "null")?kpiName:"",
		tableName:(tableName && tableName!=null && tableName !="null")?tableName:"kpi_001_value",
		type:type?type:""
				
	}
	
	$('#cellIdSearch').val(searchParams.cellId);
	searchParams.cell_name = $('#hiddenCellName').val();
//	searchParams.startCycle = $('#hiddenCycle').val();    ******
	searchParams.kpiName = $('#hiddenKpiName').val();
	
	//返回按钮
	if(searchParams.type==1){
		$('#returnBtn').show();
	}else{
		$('#returnBtn').hide();
	}
	
	var today = new Date();
	
	/*	var date_s = "";
	if(searchParams.startCycle != ""){
		date_s = searchParams.startCycle;
	} */
	//查询条件
	var start = $("#startDate").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:00:00",
		value:searchParams.startCycle
		//value:date_s
	}).data("kendoDatePicker");
	var end =$("#endDate").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:00:00",
		value: searchParams.endCycle
	}).data("kendoDatePicker");
	$("#startDate").attr("disabled",true);
	$("#endDate").attr("disabled",true);
	
	/*var str =  date_s.split(' ')[0].split('-');
	$('#endDate').data("kendoDateTimePicker").value(new Date(str[0],str[1]-1,str[2],24,00,00));*/
	
	dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "kpiQuery/search"
            },
            parameterMap: function (options, operation) {   
                if (operation == "read") {
                	 searchParams.startCycle =  $('#startDate').val();
                	 searchParams.endCycle =  $('#endDate').val();
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
	
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "kpiIndex/getKpiRule",
		"success" : function(data) {
			$.each(data, function(index, item) {
				var kpistr = item.KPI_ID+"_value";
				
				
				if(kpistr.toLowerCase() == searchParams.tableName){
					columns.push({field:kpistr.toLowerCase(), title:"KPI值",width: 150});
				}else{
					
					columns.push({field:kpistr.toLowerCase(), title:"KPI值",width: 150,hidden:true});
				}
				kpiDataSource.push({
					id : item.KPI_ID,
					name : item.KPI_NAME
				});
				$("#htmlKpi").append("<label value='"+item.KPI_NAME+"' class= 'btn btn-info'><input type='radio' name='options'"
						+"autocomplete='off' value='"+item.KPI_ID+"_value'/>"+item.KPI_NAME+"</label>");
			});
			if(kpiName == 'null'){
					kpiName = data[0].KPI_NAME;
				}
			$("#htmlKpi label[value='"+kpiName+"']").addClass("active");
			
			//初始化列表
			kpiList();
		}
	});
	
	//按照KPI名称切换
	$('#htmlKpi').delegate(".btn-info",'click', function() {
		var tableName = $(this).children("input").val();
		$.each(kpiDataSource, function(index,column){
			var key = column.id+'_value';
			if(tableName==key){
				kpiName = column.name;
				kpiGrid.showColumn(key.toLowerCase());
			}else{
				kpiGrid.hideColumn(key.toLowerCase());
			}
		});
		searchParams.tableName = tableName.toLowerCase();
		dataSource.read(searchParams);
		$("#gridList").data("kendoGrid").pager.page(1);
		
	});
	
	//初始化区
	initCityList();
	//初始化市
	initAreaList();

	//	条件查询按钮
	$('#searchBtn').on('click', function() {
		
		searchParams.areaCode = $('#areaCode').val(); //地区
		searchParams.cityCode = $('#cityCode').val(); //地市
		searchParams.cellId = $('#cellIdSearch').val(); //小区ID
		searchParams.startCycle = $('#startDate').val();
		searchParams.endCycle = $('#endDate').val();
		
		if(!checkContentLength(searchParams.cellId,10,'小区ID')){return;};//输入长度不能超过10个字符
		//条件查询重新加载 
		dataSource.read(searchParams);
		$("#gridList").data("kendoGrid").pager.page(1);
	});
	
	// 重置
	$('#resetBtn').click(function(){
		$('#cellIdSearch').val(""); 
		$('#cityCode').data("kendoDropDownList").value(''); 
		$('#areaCode').data("kendoDropDownList").value('');
//		$('#startDate').val("");
//		$('#endDate').val("");
		var today = new Date();
		$('#endDate').data("kendoDateTimePicker").value(today);
		$('#startDate').data("kendoDateTimePicker").value(new Date(today.getFullYear(), today.getMonth(),today.getDate()-1));
		searchParams.cellId = "";
		searchParams.cityCode="";
		searchParams.areaCode="";
		searchParams.startCycle="";
		searchParams.endCycle ="";
		dataSource.read(searchParams);
		
	});
	
});

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
		}
	});
}

function initAreaList(){
	$("#cityCode").kendoDropDownList({
		optionLabel:"--请选择地市--",
        dataTextField: "cityCn",
        dataValueField: "cityCode",
        dataSource: [],
        filter: "contains"
    });
}

function kpiList(){
	//列表
	kpiGrid = $("#gridList").kendoGrid({

		dataSource:dataSource,
		reorderable: true,
		resizable: true,
		sortable: true,
		pageable: true,
		//toolbar: kendo.template($("#template").html()),
		columns: columns,
		dataBound: function(){
			//导出
		    $("#btn_export").on("click",function(){
		    	window.location.href = "kpiQuery/exportCSVFile?areaCode="+$('#areaCode').val()+"&cityCode="+$('#cityCode').val()
		    	                                          +"&cellId="+$('#cellIdSearch').val()+"&startCycle="+$('#startDate').val()
		    	                                          +"&endCycle="+$('#endDate').val()+"&tableName="+searchParams.tableName
		    	                                          +"&kpiName="+kpiName;
		    });  
		}
	}).data("kendoGrid");
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
