var scoreData = [{ text: "小时", value: "1" },{ text: "日", value: "2" },{ text: "周", value: "3" },{ text: "月", value: "4" }];
var tables_bts = {1:"bts_score_hour",2: "bts_score_day",3:"bts_score_week",4:"bts_score_month"};
/*
 * 基站评分查询js
 * */
var dataSource,btsGrid,type = 0,bsValue,kpiValue,str,kpiV,isFlag;
var levelMap ={5:1,4:2,3:3,2:4,1:5};
var kpiMap = [];//kpi名称 
var parameters;
//获得参数
var startDate = getQueryString("startDate");
var endDate = getQueryString("endDate");
var cityCode = getQueryString("cityCode");
var areaCode =getQueryString("areaCode");
var grade = getQueryString("grade");
var areaName = getQueryString("areaName");
var tableName = getQueryString("tableName");
var bsValue = getQueryString("bsValue");
var str = getQueryString("kpiId");
var type = getQueryString("type");
var neCode = getQueryString("neCode");
var tjType = (getQueryString("tjType")==null || getQueryString("tjType")=="null")?1:getQueryString("tjType");
var kpiName=getQueryString("kpiName");
kpiName = decodeURI(kpiName);

$(function() {
	
	kendo.culture("zh-CN");
	
	var today = new Date();
	var isWeek = $('#isWeek').val();
	tjType = $('#tjType').val()==""?1:$('#tjType').val();
	grade = $('#hiddenGrade').val();
	if(isWeek=='yes'){
		tableName = tables_bts[2];
	}else{
		tableName = tables_bts[tjType];
	}
	
	//隐藏 显示
	function toggleOptionColumn(types){
		var grid = $("#btsList").data("kendoGrid");
		if(types==1){ //小时----显示全部可点击操作列
			grid.showColumn("alarm_score");  
			grid.showColumn("cycle_month_first");
		} else if(types==4){//月----全部隐藏
			grid.hideColumn('alarm_score');
			grid.hideColumn("cycle_month_first");
		}else if(types==3){//显示 操作列 隐藏明细列
			grid.showColumn("alarm_score");
			grid.hideColumn("cycle_month_first");
		}else if(types==2){
			grid.showColumn("alarm_score");
			grid.hideColumn("cycle_month_first");
		}
		grid.setOptions({width:'100%'});
	}
	
	if(null!=str){
		kpiV = getQueryString("kpiId").toUpperCase();
		kpiValue = getQueryString("kpiId")+"_score";
	}
	
	parameters = {
				neCode:(neCode && neCode !=null && neCode !="")?neCode:"",
				startDate: (startDate && startDate != null && startDate !="null")?startDate:new Date(today.getFullYear(), today.getMonth(),today.getDate(),today.getHours()-20).Format("yyyy-MM-dd hh:00:00"),
				endDate: (endDate && endDate!=null && endDate !="null")?endDate:new Date(today).Format("yyyy-MM-dd hh:00:00"),
				areaCode: (areaCode && areaCode!=null && areaCode !="null")?areaCode:"",
				cityCode: (cityCode && cityCode!=null && cityCode !="null")?cityCode:"",
				grade: (grade && grade!=null && grade !="null")?grade:"",
				areaName:(areaName && areaName!=null && areaName!="null")?areaName:"",
				kpiValue:(kpiValue && kpiValue!=null && kpiValue!="null")?kpiValue:"",
				tableName:(tableName && tableName!=null && tableName !="null")?tableName:"bts_score_hour",
			//	kpiValue:(kpiValue && kpiValue!=null && kpiValue!="null")?kpiValue:"",		
				bsValue:bsValue?bsValue:"",tjType:tjType
	}
	
	initScoreLevel();   //评分
	
	initScoreType(scoreData,tjType); //初始统计类型
	
	formatDate(tjType,parameters,false);	//默认初始化小时
	
	parameters.startDate=$("#startDate").val();
	parameters.endDate=$("#endDate").val();
	
	initAreaList();  //初始化地市,根据当前用户拥有的地区权限过滤查询
	
	initCityList(); //初始化区县
	
	initKpiMap();  //初始kpi
	
	initDataSource();  //初始数据
	
	initBtsGrid();  //初始化基站grid
	
	//显示 与 隐藏
	$.when(initBtsGrid()).done(function(){
		toggleOptionColumn(tjType);
	});
	
	$("#neCode").val(parameters.neCode);
	
	//返回按钮
	if(type==2){
		$("#returnBtn").show();
	}
	
	//	条件查询按钮
	$('#searchBtn').on('click', function() {
		parameters.grade = $('#scoreLevel').val(); //分数 
		parameters.areaCode = $('#areaCode').val(); //地区
		parameters.cityCode = $('#cityCode').val(); //地市
		parameters.neCode = $('#neCode').val(); 
		var  type1  = $('#scoreType').val();
		if(isFlag && type1==3){
			parameters.tableName = tables_bts[2];
		}else{
			parameters.tjType=type1;
			parameters.tableName = tables_bts[type1];
		}
		parameters.startDate=$("#startDate").val();
		parameters.endDate=$("#endDate").val();
		parameters.kpiValue=$("#kpiValue").val()!=""?$("#kpiValue").val().toLowerCase()+"_score":"";
		
		//if(!checkContentLength(parameters.neId,10,'基站ID')){return;}
		//条件查询重新加载 
	    //$("#btsList").data("kendoGrid").pager.page(1);
		//dataSource.read(parameters);
		toggleOptionColumn(type1);
		
		exportBtsScore();
		
	});
	
	// 重置
	$('#resetBtn').click(function(){
		
		$('#neCode').val("");
		$('#kpiValue').data("kendoDropDownList").select(0); 
		$('#scoreLevel').data("kendoDropDownList").select(0);
		$('#cityCode').data("kendoDropDownList").value(''); 
		$('#areaCode').data("kendoDropDownList").value('');
		
		initScoreType(scoreData,tjType);
		formatDate(1,parameters,false);
		$("#startDate").data("kendoDateTimePicker").value(new Date(today.getFullYear(),today.getMonth(),today.getDate(),today.getHours()-23).Format("yyyy-MM-dd hh:00:00"));
		$('#endDate').data("kendoDateTimePicker").value(today);
		
		parameters.startDate=$("#startDate").val();
		parameters.endDate=$("#endDate").val();
		parameters.neCode = "";
		parameters.grade = "";
		parameters.cityCode="";
		parameters.areaCode="";
		parameters.page=1;
		parameters.tableName="bts_score_hour";
		dataSource.read(parameters);
		
	});
	
	exportBtsScore();
});

function exportBtsScore(){
	//导出
	$("#btn_Export").on("click",function(){
		
		var scoreLevel = $('#scoreLevel').val();
		if(scoreLevel == 'null' || scoreLevel == null){
			scoreLevel = '';
		}
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
		var startDate = $('#startDate').val(); //小时
		if(startDate == 'null' || startDate == null){
			startDate = '';
		}
		var endDate = $('#endDate').val(); //
		if(endDate == 'null' || endDate == null){
			endDate = '';
		} 
		var  tableName = parameters.tableName;
		if(tableName=='null' || tableName == null){
			tableName='bts_score_hour';
		}
		var kpiValue = 	$("#kpiValue").val();
		if(kpiValue=='null' || kpiValue == null){
			kpiValue='';
		}
		window.location.href = "btsScore/exportFile?areaCode="+areaCode
			+"&cityCode="+cityCode+"&neCode="+neCode+"&grade="+scoreLevel+"&startDate="+startDate
			+"&endDate="+endDate+"&kpiValue="+kpiValue+"&tableName="+tableName;
	});
	
}

/**
 * 获取列表数据
 */
function initDataSource(){
	dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "POST",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "btsScore/search"
            },
            parameterMap: function (options, operation) {
                if (operation == "read") {
	                 parameters.page = options.page;
	                 parameters.pageSize = options.pageSize;
	                 return kendo.stringify(parameters);
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
				index:levelMap[parameters.grade]
			});
		}
	});
}

function initAreaList(){
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

function initCityList(){
	$("#cityCode").kendoDropDownList({
		optionLabel:"--请选择地区--",
        dataTextField: "cityCn",
        dataValueField: "cityCode",
        dataSource: [],
        filter: "contains"
    });
}

//列表
function initBtsGrid(){
	
	var dtd = $.Deferred();
	
	btsGrid = $("#btsList").kendoGrid({
	
		dataSource: dataSource,
		reorderable: true,
		resizable: true,
		sortable: true,
		pageable: true,
	    pageable: {
	    	  buttonCount: 6
	      },
		//导出
		toolbar: kendo.template($("#template").html()),
		columns: [
		          { field: "area_code", title:"地市",hidden:true},
		          { field: "city_code", title:"地市",hidden:true},
		          { field: "area_name", title:"地市",width: 60},
		          { field: "city_name", title:"区县",width: 60},
		          { field: "ne_code", title:"基站ID",width: 60},
		          { field: "cell_id", title:"小区ID",hidden:true},
		          { field: "ne_name_cn", title:"基站名称",width: 120},
		          { field: "total_score", title:"分数",width: 50},
		          { field: "", title:"扣分故障原因",width:160,
		        	 template:function(dataItem){
		        		 var parasData = [];
		        		 $.each(kpiMap, function(index, item) {
		        			 var score_index = item.id.toLowerCase()+"_score";
		        			 if(dataItem[score_index]>0 && dataItem[score_index]!=101){
		        				 parasData.push(item.name);
		        			 }
		     			});
		        		 return parasData.join(',');
		        	 }
		          },
		          { field: "cycle", title:"时间",width: 130,
		        	template:function(item){
		        		var type = $('#scoreType').val();
		        		if(type==1){
		        			return item.cycle;
		        		}else if(type==2){
		        			return item.cycleDate;
		        		}else if(type==3){
		        			return item.cycle_year+'-'+item.cycle_week+'(周)';
		        		}else if(type==4){
		        			return item.cycle_year+'-'+item.cycle_month+'(月)';
		        		}
		        	}
		          },
		          { width: 90,
						field: "grade",
						template: "#for(var i=0;i<grade;i++){# <img class='starIcon' src='custom/images/starActive.png' /> #} for(var j=grade;j<5;j++){# <img class='starIcon' src='custom/images/starDefault.png' /> #}#", 
						encoded: false,
						cssClass:"primary-emphasis",
						title: "<span class='tdOverFont' title='评分等级'>评分等级</span>"
		          },
		          { field: "manufacturer", title:"厂家",width: 60,
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
		          {field:"cycle_month_first",width:70,
		        	  template:"<a onclick='openMx(#:ne_code#,\"#:cycle#\",\"#:ne_name_cn#\")' class='btn btn-warning btn-xs btn-flat'>明细值</a>",
		        	  encoded:false,
		        	  title:"<span class='tdOverFont' title='指标明细'>指标明细</span>"
		          },
		          {field: "alarm_score", width: 150,
						template: 
								  //"<a onclick='openMx(#:ne_code#,\"#:cycle#\",\"#:ne_name_cn#\")' class='btn btn-warning btn-xs btn-flat'>明细值</a>"+
								  "<a onclick='openGraphical(this)' class='btn btn-warning btn-xs btn-flat'>分数图形</a>" +
								  "<a onclick='openKpiScore(this)' class='btn btn-warning btn-xs btn-flat'>指标明细</a>"+
								  "<a onclick='openAlarmScore(this)' class='btn btn-warning btn-xs btn-flat'>告       警</a>",
								//  "<a onclick='openOutOfScore(this)' class='btn btn-warning btn-xs btn-flat'>退服</a>",
						encoded: false,
						title: "<span class='tdOverFont' title='操作'>操作</span>"
				}],
				dataBound:function(){
					dtd.resolve();
					return dtd;
				}
	}).data("kendoGrid");
	
}

//明细
function openMx(neCode,cycle,neName){
	 var na_name = encodeURI(encodeURI(neName));
	window.location.href="indexValue?neCode="+neCode+"&cycle="+cycle+"&neName="+na_name;
}
//kpi指标明细
function openKpiScore(d){
	
	var type=$("#scoreType").val();
	var grid = btsGrid.dataItem($(d).closest("tr"));
	var kpiUrl ="indicatorCurrentScore?neCode="+grid.ne_code+"&areaCode="+grid.area_code+
		"&cityCode="+grid.city_code+"&type=1";
	
	if(type==1){
		kpiUrl += "&startDate="+grid.cycle+"&endDate="+grid.cycle+"&tableName=cell_score_hour";
	}
	if(type==2){
		var d = grid.cycleDate;
		//var s = new Date(d);
		//var start = new  Date(s.getFullYear(),s.getMonth(),s.getDate()-1).Format("yyyy-MM-dd 00:00:00");
		kpiUrl += "&startDate="+d+"&endDate="+d+"&tableName=bts_score_day";
		
	}
	if(type==3){
		var start = grid.cycle_week_first+" 00:00:00";
		var end = grid.cycle_week_end+" 23:00:00";
		kpiUrl += "&startDate="+start+"&endDate="+end+"&tableName=bts_score_week";
	}
	window.location.href=kpiUrl;
}

//图形指标
function openGraphical(d){
	//显示该基站的分数图 
	var type = $('#scoreType').val();
	var grid = btsGrid.dataItem($(d).closest("tr"));
	var chartUrl="bsCurrentScoreGraphical?neCode="+grid.ne_code;
	if(type==1){
		var end = grid.cycle;
		var date = new Date(end);
		var start = new Date(date.getTime()-20*60*60*1000).Format("yyyy-MM-dd hh:00:00");
		chartUrl+="&startCycle="+start+"&endCycle="+end+"&tableName=bts_score_hour";
	}
	if(type==2){
		var end = grid.cycleDate;
		var today = new Date(end); 
		var start = new  Date(today.getFullYear(),today.getMonth(),today.getDate()-20).Format("yyyy-MM-dd");
		chartUrl+="&startCycle="+start+"&endCycle="+end+"&tableName=bts_score_day";
	}
	if(type==3){
	    var end = grid.cycle_year+'-'+grid.cycle_week;
	    var week = grid.cycle_week;
	    var a  = week-20;
	    var s = grid.cycle_year+'-'+a;
	    chartUrl +="&startCycle="+s+"&endCycle="+end+"&tableName=bts_score_week";
	}
	window.location.href=chartUrl;
}

//告警 按钮
function openAlarmScore(d){
	
	var type = $('#scoreType').val();
	
	var grid = btsGrid.dataItem($(d).closest("tr"));
	var alarmUrl = "alarmQuery?neCode="+grid.ne_code+"&areaCode="+grid.area_code+
	"&cityCode="+grid.city_code+"&type=1";
	if(type==1){
		var d1=new Date(grid.cycle);
		var d2=d1.getTime()-2*60*60*1000;
		var d = new Date(d2).Format("yyyy-MM-dd hh:00:00");
		alarmUrl += "&startDate="+d+"&endDate="+grid.cycle;
	}
	if(type==2){
		var d = grid.cycleDate;
		var end = d+" 23:00:00"; 
		var s = new Date(d);
		var start = new  Date(s.getFullYear(),s.getMonth(),s.getDate()-1).Format("yyyy-MM-dd 00:00:00");
		alarmUrl += "&startDate="+start+"&endDate="+end;
		
	}
	if(type==3){
		var start = grid.cycle_week_first+" 00:00:00";
		var end = grid.cycle_week_end+" 23:00:00";
		alarmUrl += "&startDate="+start+"&endDate="+end;
	}
	window.location.href=alarmUrl;
}

//退服 按钮
function openOutOfScore(d){
	
	var type=$("#scoreType").val();
	var grid = btsGrid.dataItem($(d).closest("tr"));
	
	var outOfUrl ="outOfQuery?neCode="+grid.ne_code+"&areaCode="+grid.area_code+
	"&cityCode="+grid.city_code+"&type=1";
	
	if(type==1){
		var d1=new Date(grid.cycle);
		var d2=d1.getTime()-2*60*60*1000;
		var d = new Date(d2).Format("yyyy-MM-dd hh:00:00");
		outOfUrl += "&startDate="+d+"endDate="+grid.cycle;
	}
	if(type==2){
		var d = grid.cycleDate;
		var end = d+" 23:00:00"; 
		var s = new Date(d);
		var start = new  Date(s.getFullYear(),s.getMonth(),s.getDate()-1).Format("yyyy-MM-dd 00:00:00");
		outOfUrl += "&startDate="+start+"&endDate="+end;
		
	}
	if(type==3){
		var start = grid.cycle_week_first+" 00:00:00";
		var end = grid.cycle_week_end+" 23:00:00";
		outOfUrl += "&startDate="+start+"&endDate="+end;
	}
	window.location.href=outOfUrl;
}

function valueSearch(){
	$("#kpiValue").kendoDropDownList({
		optionLabel:"--请选择故障原因--",
        dataTextField: "name",
        dataValueField: "id",
        dataSource: kpiMap,
        filter: "contains",
        suggest:true,
    });
	if(kpiV){
		var _v = kpiV=="ALARM"?"alarm_score":kpiV;
		$('#kpiValue').data("kendoDropDownList").value(_v);
	}
}

//指标名称
function initKpiMap() {
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "kpiIndex/getKpiRule",
		async:false,
		"success" : function(data) {
			//console.log(data);
			$.each(data, function(index, item) {
				kpiMap.push({id:item.KPI_ID,name:item.KPI_NAME});
			});
			if(str == "alarm"){
				kpiMap.push({id:kpiValue,name:"基站告警"})
			}
			kpiMap.push({id:"alarm",name :"基站告警"});
			//kpiMap.push({id:"out_of",name :"退服"});
			valueSearch();
		}
	});
}
