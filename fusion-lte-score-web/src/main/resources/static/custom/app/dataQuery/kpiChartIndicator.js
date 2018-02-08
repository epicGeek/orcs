/**
 * KPI图形指标
 */
var dataSource;
var kpiGrid;
//var searchParams = {areaCode:'',cityCode:'',cellId:'',neCode:''};
$(function() {
	
	kendo.culture("zh-CN");
	//获得参数
	var neId = getQueryString("neId");
	var cityCode = getQueryString("cityCode");
	var areaCode = getQueryString("areaCode");
	var cellCode = getQueryString("cellCode");
	var page = getQueryString("page");
	parameters = {
		neId: (neId && neId != null && neId !="null")?neId:"",
		cityCode: (cityCode && cityCode!=null && cityCode !="null")?cityCode:"",
		areaCode: (areaCode && areaCode!=null && areaCode !="null")?areaCode:"",
		cellCode: (cellCode && cellCode!=null && cellCode !="null")?cellCode:"",
		page: page?page:1
				
	}	
	
	dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "kpiChartQuery/search"
            },
            parameterMap: function (options, operation) {   
                if (operation == "read") {
	                 parameters.page = options.page;
	                 parameters.pageSize = options.pageSize;
	                 parameters.neCode = parameters.neId;
	                 parameters.cellId = parameters.cellCode;
	                 return parameters;
                } else {
                	return kendo.stringify(options);
                }
            }
        },
        page:parameters.page,
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
	kpiChartList();
	//初始化市
	initCityList();
	//初始化区
	initAreaList();
	
	$('#neCode').val(parameters.neId); 
	$('#cellIdSearch').val(parameters.cellCode);
	//	条件查询按钮
	$('#searchBtn').on('click', function() {
		
		parameters.areaCode = $('#areaCode').val(); //地区
		parameters.cityCode = $('#cityCode').val(); //区县
		parameters.neId = $('#neCode').val(); //基站ID
		parameters.cellCode = $('#cellIdSearch').val(); //小区ID
		
		if(!checkContentLength(parameters.neId,10,'基站ID')){return;};
		if(!checkContentLength(parameters.cellCode,10,'小区ID')){return;};
		$("#kpiChartList").data("kendoGrid").pager.page(1);
		
		//条件查询重新加载 
		dataSource.read(parameters);
	});
	
	// 重置
	$('#resetBtn').click(function(){
		$('#cellIdSearch').val(""); 
		$('#neCode').val("");
		$('#cityCode').data("kendoDropDownList").value(''); 
		$('#areaCode').data("kendoDropDownList").value('');
		
		parameters.cellCode = "";
		parameters.cityCode="";
		parameters.areaCode="";
		parameters.neId="";
		parameters.page=1;
		dataSource.read(parameters);
		
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
			
			//回显
			var va = parameters.areaCode;
			if(va!=""){
				$('#areaCode').data("kendoDropDownList").value(va);
				getAreaData(va);
			}
			
		}
	});
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
			$('#cityCode').data("kendoDropDownList").value(parameters.cityCode);
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

function kpiChartList(){
	
	//列表
	kpiGrid = $("#kpiChartList").kendoGrid({

		dataSource:dataSource,
		reorderable: true,
		resizable: true,
		sortable: true,
		pageable: true,
		pageable:{
			buttonCount:6
		},
		columns: [{ field: "AREA_CN", title:"地市",width:140},
		          { field: "CITY_CN", title:"区县",width:140},
		          { field: "ne_code", title:"基站ID",width:140},
		          { field: "cell_id", title:"小区ID",width:140},
		          { field: "sector_id", title:"小区ID",hidden:true},
		          { field: "cell_name_cn", title:"小区名称",width:280},
		        /*  { width:140,
		            template: "<a onclick='openMx(this)' class='btn btn-warning btn-xs btn-flat'>查看指标</a>",
					encoded: false,
					title: "<span class='tdOverFont' title='操作'>操作</span>"
				  }*/]
	}).data("kendoGrid");
}

function openMx(d){
	
	var grid = kpiGrid.dataItem($(d).closest("tr"));
	var date = new Date().Format("yyyy-MM-dd 00:00:00");
	console.log(date);
	window.location.href="chartList?cellId="+grid.cell_id+"&neCode="+grid.ne_code+"&cycle="+date;
								/*	+"&sector_id="+grid.sector_id
									+"&cellCode="+parameters.cellCode
								    +"&neId="+parameters.neId
								    +"&cityCode="+parameters.cityCode
								    +"&areaCode="+parameters.areaCode
								    +"&page="+parameters.page;*/

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