//基站基础信息

var dataSource;

var searchParams = {neCode:'',areaCode:'',cityCode:'',cycleStart:'',cycleEnd:'',score:''};
$(function() {
	
	kendo.culture("zh-CN");
	
	//初始化城市
	initCityList();
	
	//初始化地区
	initAreaList();
	var today = new Date();
	//查询条件
	$("#cycleStart").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:00:00",  
		value: new Date(today.getFullYear(), today.getMonth(),today.getDate())
	});
	$("#cycleEnd").kendoDateTimePicker({
    	value: today,
		format: "yyyy-MM-dd HH:00:00"
	});
	$("#cycleStart").attr('disabled',true);
	$("#cycleEnd").attr('disabled',true)
	
	dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "btsAlarmJob/search"
            },
            parameterMap: function (options, operation) {   
                if (operation == "read") {
	                 searchParams.page = options.page;
	                 searchParams.pageSize = options.pageSize;
	                 searchParams.cycleStart= $('#cycleStart').val();
		     		 searchParams.cycleEnd= $('#cycleEnd').val();
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
		searchParams.score = $('#score').val();
		searchParams.cycleStart= $('#cycleStart').val();
		searchParams.cycleEnd= $('#cycleEnd').val();
		if(!checkContentLength(searchParams.neCode,10,"基站ID")){return;};
		$("#btsJobList").data("kendoGrid").pager.page(1);
		//条件查询重新加载 
		//dataSource.read(searchParams);
	});
	//重置
	$('#btn-reset').on('click', function() {
		$('#neCode').val("");
		$('#cityCode').data("kendoDropDownList").text('');
		$('#areaCode').data("kendoDropDownList").text('');
		$('#cycleStart').val('');
		$('#cycleEnd').val('');
		$('#score').val('');
		searchParams.neCode = "";
		searchParams.cityCode = "";
		searchParams.areaCode = "";
		searchParams.cycleStart= "";
		searchParams.cycleEnd="";
		searchParams.score = "";
		
		dataSource.read(searchParams);
	});
			 
});

//列表
function initBSGrid(){
	
	$("#btsJobList").kendoGrid({
		
		dataSource: dataSource,
		reorderable: true,
		resizable: true,
		sortable: true,
		//columnMenu: true,
		pageable: true,
		columns: [
		          { field: "area_name", title:"地市",width: 60},
		          { field: "city_name", title:"区县",width: 60},
		          { field: "ne_code", title:"基站ID",width: 60},
		          { field: "ne_name_cn", title:"基站名称",width: 110},
		          { field: "cycle", title:"时间",width: 90},
		          { field: "city_code", title:"地区ID",width: 60,hidden:true},
		          { field: "area_code", title:"区县ID",width: 60,hidden:true},
                  { field: "score", title:"分数",width: 40},
                  { field: "manufacturer",width: 50,
                	 template: function(dataItem) {
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
				//locked: true,
				//hidden: true,
				lockable: false },
				//{ field:"",title:"备注",width:100,template:"频次得分[#:frequencyScore#],延时得分[#:delayScore#]"},
				{ width: 80,
					template: "<button onclick='alarmNoBtn(#:ne_code#,\"#:cycle#\")' class='btn btn-success btn-xs btn-flat'>派    单</button>",
					encoded: false,
					title: "<span class='tdOverFont' title='操作'>操作</span>"
				} ]
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
