kendo.culture("zh-CN");
//var alarmScoreGrid;
var searchParams = {neCode:'', cycleStart:'',cycleEnd:'', cityCode:'',areaCode:'',score:''};
var searchParamAlarm ={neCode:'',cycle:''};
var dataSource;
var columns =[];
var alarmNoModalObj;
/*
 * 基站性能告警得分
 */
$(function(){
	
	//scoreSearch();
	//初始化城市
	initCityList();
	
	//初始化地区
	initAreaList();
	var today = new Date();
	//查询条件
	$("#cycleStart").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:00:00",  
		value: new Date(today.getFullYear(), today.getMonth(),today.getDate(),today.getHours()-3)
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
                url: "btsAlarmScore/search"
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
        page:searchParams.page,
        pageSize: 10,
        schema: {
            data: function (data) {
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
	 
 $("#btsAlarmScoreList").kendoGrid({

				dataSource: dataSource,
				groupable: false,
		        sortable: true,
		        resizable: true,
		        columnMenu: false,
		        reorderable: true,
				pageable: true,
				toolbar: kendo.template($("#template").html()),
				columns: [
				          { field: "area_name", title:"地市",width: 60},
				          { field: "city_name", title:"区县",width: 60},
				          { field: "ne_code", title:"基站ID",width: 60},
				          { field: "ne_name_cn", title:"基站名称",width: 110},
				          { field: "cycle", title:"时间",width: 90},
				          { field: "city_code", title:"地区ID",width: 60,hidden:true},
				          { field: "area_code", title:"区县ID",width: 60,hidden:true},
		               //   { field: "cycle_date", title:"时间",width:70},
		              //    { field: "cycle_hour", title:"小时",width: 70},
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
							template: "<a onclick='openMx(#:ne_code#,\"#:city_code#\",#:area_code#)' class='btn btn-warning btn-xs btn-flat'>告警明细</a>"
									+"<button onclick='alarmNoBtn(#:ne_code#,\"#:cycle#\")' class='btn btn-warning btn-xs btn-flat'>明细</button>",
							encoded: false,
							title: "<span class='tdOverFont' title='操作'>操作</span>"
						}
						],
			}).data("kendoGrid");
 
/*	//查询条件
	$("#cycleDateInput").kendoDatePicker({
		format: "yyyy-MM-dd"
	//	value:searchParams.cycleDate
	});
	//时间下拉
	initTime("cycleHourStartInput");
	initTime("cycleHourEndInput");*/
 
 	//	条件查询
	$('#searchBtn').on('click', function() {
			searchParams.neCode = $('#neCodeInput').val();
			searchParams.cycleStart= $('#cycleStart').val();
			searchParams.cycleEnd= $('#cycleEnd').val();
			searchParams.cityCode=  $('#cityCode').val();
			searchParams.areaCode= $('#areaCode').val();
			searchParams.score = $('#score').val();
			$("#btsAlarmScoreList").data("kendoGrid").pager.page(1);
			//条件查询重新加载 
			dataSource.read(searchParams);
	});
	
	// 重置
	$('#resetBtn').click(function(){
			$('#neCodeInput').val("");
			$('#cycleStart').val('');
			$('#cycleEnd').val('');
			$('#score').val('');
			$('#cityCode').data("kendoDropDownList").text('');
			$('#areaCode').data("kendoDropDownList").text('');
			searchParams.score="";
			searchParams.neCode = "";
			searchParams.cityCode = "";
			searchParams.neCode = "";
			searchParams.cycleStart= "";
			searchParams.cycleEnd="";
			searchParams.score = "";
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
	    	var neCode = $('#neCodeInput').val();
	    	if(neCode == 'null' || neCode == null){
	    		neCode = '';
	    	}
	    	var cycleStart = $('#cycleStart').val();
	    	if(cycleStart == 'null' || cycleStart == null){
	    		cycleStart = '';
	    	}
	    	var cycleEnd = $('#cycleEnd').val();
	    	if(cycleEnd == 'null' || cycleEnd == null){
	    		cycleEnd = '';
	    	}
	    	var score = $("#score").val();
	    	if(score == 'null' || score == null){
	    		score='';
	    	}
		 
		 window.location.href="btsAlarmScore/exportFile?cycleStart="+cycleStart+"&cycleEnd="+cycleEnd+
		 		"&score="+score+"&neCode="+neCode+"&cityCode="+cityCode+ "&areaCode="+areaCode;
	 });

});


/*function initTime(value){
	
	var data = [
	            {text:"01",value:"1"},{text:"02",value:"2"},{text:"03",value:"3"},{text:"04",value:"4"},
	            {text:"05",value:"5"},{text:"06",value:"6"},{text:"07",value:"7"},{text:"08",value:"8"},
	            {text:"09",value:"9"},{text:"10",value:"10"},{text:"11",value:"11"},{text:"12",value:"12"},
	            {text:"13",value:"13"},{text:"14",value:"14"},{text:"15",value:"15"},{text:"16",value:"16"},
	            {text:"17",value:"17"},{text:"18",value:"18"},{text:"19",value:"19"},{text:"20",value:"20"},
	            {text:"21",value:"21"},{text:"22",value:"22"},{text:"23",value:"23"},{text:"24",value:"24"}
	            
	        ];
	//时间下拉
	$("#"+value).kendoDropDownList({
		 	autoBind: false,
		 	optionLabel:"--请选择时间--",
		 	dataTextField: "text",
	        dataValueField: "value",
			dataSource: data,
			filter: "contains"
		});
	
	if(value=='cycleHourStartInput'){
		$('#cycleHourStartInput').data("kendoDropDownList").value(searchParams.cycleHourStart);
	}else{
		$('#cycleHourEndInput').data("kendoDropDownList").value(searchParams.cycleHourEnd);
	}
}*/
function scoreSearch(){
	
		$("#score").kendoDropDownList({
			optionLabel:"--请选择分值范围--",
	        dataTextField: "text",
	        dataValueField: "value",
	        dataSource: [
                                     {text:"0-60",value:"0-60"},
                                     {text:"60-70",value:"60-70"},
                                     {text:"70-80",value:"70-80"},
                                     {text:"80-90",value:"80-90"},
                                     {text:"90-100",value:"90-100"}
	                     ],
	        filter: "contains"
	    });
}


function alarmNoBtn(neCode,cycle){
	
	searchParamAlarm.neCode = neCode;
	searchParamAlarm.cycle = cycle;
	//性能告警得分配置
	alarmNo();
	alarmNoModalObj = {
			initGrid: function(){
				alarmNoModalObj.dataGrid = $("#alarmNoGrid").kendoGrid({
					dataSource: alarmDataSource,
					height: 390,
					reorderable: true,
					resizable: true,
				//	sortable: true,
					//columnMenu: true,
					pageable: true,
					columns: [
					          { field: "ne_code", title:"基站ID",width: 120},
					          { field: "alarm_no", title:"告警号",width: 120},
					          { field: "freScore", title:"频次分数",width: 100},
					          { field: "delayScore", title:"时长分数",width: 100
					        	/*  template:function(dataItem){
					        		  return dataItem.delayScore+"       "+dataItem.startTime;
					        	  }*/
					          },
					          { field:"startTime", title:"时长__告警开始时间",width:120}
					   ]
				}).data("kendoGrid");
				
			},
			
	};
	alarmNoModalObj.initGrid();
	$("#alarmNoModalObj").modal("show");
}

//明细
function openMx(neCode,cityCode,areaCode){
	var reurl ="alarmQuery?neCode="+neCode+"&cityCode="+cityCode+"&areaCode="+areaCode+"&type=1"+
										"&startDate="+$('#cycleStart').val()+"&endDate="+$('#cycleEnd').val();
	window.location.href=reurl;
}

//告警明细
function alarmNo(){
	alarmDataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "btsAlarmNo/search"
            },
            parameterMap: function (options, operation) {
                if (operation == "read") {
	               	  searchParamAlarm.page = options.page;
                      searchParamAlarm.pageSize = options.pageSize;
                      return searchParamAlarm;
                }
            }
        },
        page: searchParamAlarm.page,
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
