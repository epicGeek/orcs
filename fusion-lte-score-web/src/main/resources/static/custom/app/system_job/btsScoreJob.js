//基站健康度工单
var dataSource;
var levelMap ={5:1,4:2,3:3,2:4,1:5};
var searchParams = {neCode:'',areaCode:'',cityCode:'',cycleStart:'',cycleEnd:'',grade:''};
$(function() {
	
	kendo.culture("zh-CN");
	
	//初始化城市
	initCityList();
	
	//初始化地区
	initAreaList();
	//分数
	initScoreLevel();
	
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
                url: "btsScoreJob/search"
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
		searchParams.grade = $('#scoreLevel').val();
		searchParams.cycleStart= $('#cycleStart').val();
		searchParams.cycleEnd= $('#cycleEnd').val();
		$("#btsJobList").data("kendoGrid").pager.page(1);
		if(!checkContentLength(searchParams.neCode,10,"基站ID")){return;};
		//条件查询重新加载 
		dataSource.read(searchParams);
	});
	//重置
	$('#btn-reset').on('click', function() {
		$('#neCode').val("");
		$('#cityCode').data("kendoDropDownList").text('');
		$('#areaCode').data("kendoDropDownList").text('');
		$('#scoreLevel').data("kendoDropDownList").select(0);
		$('#cycleStart').val('');
		$('#cycleEnd').val('');
		searchParams.neCode = "";
		searchParams.cityCode = "";
		searchParams.areaCode = "";
		searchParams.cycleStart= "";
		searchParams.cycleEnd="";
		
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
						{ field: "area_name", title:"地市",width: 75},
						{ field: "city_name", title:"区县",width: 75},
						{ field: "ne_code", title:"基站ID",width: 75},
						{ field: "cell_id", title:"小区ID",width: 80,hidden:true},
						{ field: "ne_name_cn", title:"基站名称",width: 130},
						{ field: "total_score", title:"分数",width: 70},
						{ field: "cycle_date", title:"时间",width: 130,template: 
							  function(dataItem) {
							     return dataItem.cycle_date+" "+dataItem.cycle_hour+":00:00";
						    }
						},
						{ width: 120,
								field: "grade",
								template: "#for(var i=0;i<grade;i++){# <img class='starIcon' src='custom/images/starActive.png' /> #} for(var j=grade;j<5;j++){# <img class='starIcon' src='custom/images/starDefault.png' /> #}#", 
								encoded: false,
								cssClass:"primary-emphasis",
								title: "<span class='tdOverFont' title='评分等级'>评分等级</span>"
						},
						{ field: "manufacturer", title:"厂家",width: 80,
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
								lockable: false 
						},
						{ width: 100,
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


function initScoreLevel(){
	
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "scorelevel/getScoreLevel",
		"success" : function(data) {
			var levelData = [];
			$.each(data, function(index, item) {
				levelData.push({id:item.level,name:item.scorefrom+"-"+item.scoreto});
			});
			$("#scoreLevel").kendoDropDownList({
				optionLabel:"--请选择分数-",
				dataTextField: "name",
				dataValueField: "id",
				dataSource:levelData,
				filter: "contains",
				suggest: true,
				index:levelMap[searchParams.grade]
			});
		}
	});
}
