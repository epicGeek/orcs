//基站基础信息

var dataSource;

var searchParams = {neCode:'',areaCode:'',cityCode:''};
$(function() {
	
	kendo.culture("zh-CN");
	
	//初始化城市
	initCityList();
	
	//初始化地区
	initAreaList();
	
	dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "cell/search"
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
	
	//初始化基站grid
	initBSGrid();
	//查询
	$('#btn-search').on('click', function() {
		searchParams.neCode = $('#neCode').val();
		searchParams.cityCode = $('#cityCode').val();
		searchParams.areaCode = $('#areaCode').val();
		$("#cellList").data("kendoGrid").pager.page(1);
		if(!checkContentLength(searchParams.neCode,10,"基站ID")){return;};
		//条件查询重新加载 
		dataSource.read(searchParams);
	});
	//重置
	$('#btn-reset').on('click', function() {
		$('#neCode').val("");
		$('#cityCode').data("kendoDropDownList").text('');
		$('#areaCode').data("kendoDropDownList").text('');
		searchParams.neCode = "";
		searchParams.cityCode = "";
		searchParams.areaCode = "";
		
		dataSource.read(searchParams);
	});
	//导出
	$("#btn-export").on("click",function(){
		var neCode =  $('#neCode').val();
		if(neCode=='null' || neCode==null){
			neCode='';
		}
		var cityCode=$('#cityCode').val();
		if(cityCode=='null' || cityCode==null){
			cityCode='';
		}
		var areaCode = $('#areaCode').val();
		if(areaCode='null' || areaCode==null){
			areaCode='';
		}
		window.location.href = "cell/exportFile?neCode=" +neCode+"&cityCode="+cityCode+"&areaCode="+areaCode;
	});
			 
});

//列表
function initBSGrid(){
	
	$("#cellList").kendoGrid({
		
		dataSource: dataSource,
		reorderable: true,
		resizable: true,
		sortable: true,
		//columnMenu: true,
		pageable: true,
		toolbar: kendo.template($("#template").html()),
		columns: [
		          { field: "area_name", title:"地市名称",width: 95},
		          { field: "city_name", title:"区县名称",width: 95},
		          //{ field: "area_code", title:"地市ID"},
		         // { field: "city_code", title:"区县ID"},
		          { field: "cell_name_cn", title:"小区名称",width: 180},
		          { field: "cell_id", title:"小区ID",width: 100},
		          { field: "ne_name_cn", title:"基站名称",width: 180},
		          { field: "ne_code", title:"基站ID",width: 100},
		          { field: "sector_id", title:"扇区",width: 90},
		          { field: "proxy_x", title:"经度",width: 100},
		          { field: "proxy_y", title:"纬度",width: 100}
		          ]
	}).data("kendoGrid");
	
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
	            	getCityData(areaCode);
	            }
	        });
			
		}
	});
}

function getCityData(areaCode){
	
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "area/search",
		data:{
			"cityCode":areaCode
		},
		"success" : function(data) {
			$("#cityCode").data("kendoDropDownList").setDataSource(data);
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

