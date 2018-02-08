/**
 * 基站汇总评分查询
 * */
//endCycleDate:'',startCycleDate:'',
//var searchParams = {areaCode:'',cityCode:'',neCode:'',startCycleDate:'',endCycleDate:'',grade:'',tableName:'bts_score_day'};

var dataSource;
var parameters;
var sumGrid;//列表grid
var scoreLevelMap ={5:1,4:2,3:3,2:4,1:5};

$(function() {
	
	kendo.culture("zh-CN");
	
	//获得参数
	var today = new Date();
	var neCode = getQueryString("neCode");
	var neId = getQueryString("neId");
	var startCycleDate = getQueryString("startCycleDate");
	var endCycleDate = getQueryString("endCycleDate");
	var cityCode = getQueryString("cityCode");
	var areaCode = getQueryString("areaCode");
	var grade = getQueryString("grade");
	var tableName = getQueryString("tableName");
	var page = getQueryString("page");
	var types=getQueryString("types");
	parameters = {
		neCode: (neCode && neCode != null && neCode !="null")?neCode:"",
		neId:(neId && neId != null && neId !="null")?neId:"",
		startCycleDate: startCycleDate?startCycleDate: new Date(today.getFullYear(),today.getMonth(),today.getDate()-1),
		endCycleDate: (endCycleDate &&endCycleDate!=null && endCycleDate !="null")?endCycleDate: today,
		cityCode: (cityCode && cityCode!=null && cityCode !="null")?cityCode:"",
		areaCode: (areaCode && areaCode!=null && areaCode !="null")?areaCode:"",
		grade: (grade && grade!=null && grade !="null")?grade:"",
		tableName: (tableName && tableName!=null && tableName !="null")?tableName:"bts_score_day",
		types:types?types:"",		
		page: page?page:1
	}
	if(parameters.types == 2){
		$('#backPage').show();
	}else{
		$('#backPage').hide();
	}

	//下面 调用 parameters;
	//点击查询事件 以及 日/周/月事件的时候,重新赋值 parameters;  parameters.neCode = "*****";
	
	//查询条件
	var startTime = $("#startDate").kendoDatePicker({
		format: "yyyy-MM-dd",
		value: parameters.startCycleDate
	});
	var endTime = $("#endDate").kendoDatePicker({
		format: "yyyy-MM-dd",
		value: parameters.endCycleDate
	});
	
	$('#startDate').attr("disabled",true);
	$('#endDate').attr("disabled",true);
	
	dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "btsSummaryScore/search"
            },
            parameterMap: function (options, operation) {
                if (operation == "read") {
                	 parameters.page = options.page;
                	 parameters.pageSize = options.pageSize;
	                 parameters.startCycleDate = $('#startDate').val(); //时间
	         		 parameters.endCycleDate = $('#endDate').val();
	         		 parameters.neCode = parameters.neId;
	                 return parameters;
                } else {
                	return kendo.stringify(options);
                }
                
            }
        },
        page: parameters.page,
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
	
	initBtsSumGrid(); //初始化汇总列表
	
	tableNameBtn(parameters.tableName); //日周月切换
	
	initCityList();   //初始化区
	
	initAreaList();   //初始化市
	
	initScoreLevel();
		
	//导出
    $("#btn-export").on("click",function(){
    	
    	var grade = $('#totalScoreValue').val()
    	if(grade == 'null' || grade == null){
    		grade = '';
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
    	var startCycleDate = $('#startDate').val();
    	if(startCycleDate == 'null' || startCycleDate == null){
    		startCycleDate = '';
    	}
    	var endCycleDate = $('#endDate').val();
    	if(endCycleDate == 'null' || endCycleDate == null){
    		endCycleDate = '';
    	}
    	
    	window.location.href = "btsSummaryScore/exportFile?grade=" +grade
					    	+"&areaCode="+areaCode+"&cityCode="+cityCode
					    	+"&startCycleDate="+startCycleDate+"&endCycleDate="+endCycleDate
					    	+"&neCode="+neCode+"&tableName="+parameters.tableName;
	});
    
    $('#neCode').val(parameters.neId);
	
//	条件查询按钮
	$('#searchBtn').on('click', function() {
		parameters.grade = $('#totalScoreValue').val(); //分数 
		parameters.areaCode = $('#areaCode').val(); //地区
		parameters.cityCode = $('#cityCode').val(); //地市
		parameters.startCycleDate = $('#startDate').val(); //时间
		parameters.endCycleDate = $('#endDate').val();
		parameters.neId = $('#neCode').val(); // 基站ID
		if(!checkContentLength(parameters.neId,10,'基站ID')){return;};
		//条件查询重新加载 
		dataSource.read(parameters);
	});
	
	// 重置
	$('#resetBtn').click(function(){
		$('#neCode').val("");
		$('#cityCode').data("kendoDropDownList").value(''); 
		$('#areaCode').data("kendoDropDownList").value('');
		//$('#totalScoreValue').data("kendoDropDownList").value('');
		$('#totalScoreValue').data("kendoDropDownList").select(0);
		//清除
		var today = new Date();
		$('#endDate').data("kendoDatePicker").value(today);
		$('#startDate').data("kendoDatePicker").value(new Date(today.getFullYear(),today.getMonth(),today.getDate()-1));
		parameters.neId = "";
		parameters.grade = "";
		parameters.cityCode="";
		parameters.areaCode="";
		parameters.grade="";
		parameters.page = 1;
		
		dataSource.read(parameters);
	});
	
	$('.blockquote .btn').on('click', function(){
		//tableName = $(".blockquote .btn input[name='options']:checked").val();
		var table = $(this).children("input").val();
		tableNameBtn(table,0);
	});
	
});

//周 月 日 按钮切换
function tableNameBtn(tableName,type){

	/*	var tableName ="";
		if(parameters.tableName!='bts_score_day'){
		   tableName = parameters.tableName;
		}else{
		   tableName = table;
		}
		console.log(table+'           '+type);*/	
		if(tableName=='bts_score_day'){
			sumGrid.showColumn("cycle_date");
			sumGrid.showColumn("cycle_week");
			sumGrid.showColumn("cycle_month");
		}else if(tableName=='bts_score_week'){
			
			sumGrid.hideColumn("cycle_date");
			sumGrid.showColumn("cycle_month");
			sumGrid.showColumn("cycle_week");
			//时间处理
			if(type==0){
				var today = new Date();
				var startDate = new Date(today.getFullYear(), today.getMonth(),today.getDate()-7);
				$("#startDate").data("kendoDatePicker").value(startDate); 
			}
		}else if(tableName=='bts_score_month'){
			sumGrid.hideColumn("cycle_date");
			sumGrid.hideColumn("cycle_week");
			sumGrid.showColumn("cycle_month");
			//时间处理
			if(type==0){
				var today = new Date();
				var startDate = new Date(today.getFullYear(), today.getMonth()-1,today.getDate()-1);
				$("#startDate").data("kendoDatePicker").value(startDate);
			}
			
		}
		//返回日、周、月状态显示 
		$(".blockquote .btn input[type='radio'][value="+tableName+"]").parent().addClass('active');
		parameters.tableName = tableName;
		parameters.startCycleDate =$("#startDate").val();
		parameters.endCycleDate =$("#endDate").val();
		dataSource.read(parameters);

}

function initScoreLevel(){
	
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "scorelevel/getScoreLevel",
		"success" : function(data) {
			var scoreLevel = [];
			$.each(data, function(index, item) {
				scoreLevel.push({id:item.level,name:item.scorefrom+"-"+item.scoreto});
			});
			
			$("#totalScoreValue").kendoDropDownList({
				optionLabel:"--请选择分数--",
				dataTextField: "name",
				dataValueField: "id",
				dataSource: scoreLevel,
				filter:"contains",
				suggest:true,
				index:scoreLevelMap[parameters.grade]
			});
		}
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
	            },
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
		optionLabel:"--请选择地区--",
        dataTextField: "cityCn",
        dataValueField: "cityCode",
        dataSource: [],
        filter: "contains"
    });
	
}

//列表
function initBtsSumGrid(){
	
	sumGrid = $("#btsSummaryList").kendoGrid({
	
		dataSource: dataSource,
		reorderable: true,
		resizable: true,
		sortable: true,
		pageable: true,
		toolbar: kendo.template($("#template").html()),
		columns: [
		          { field: "area_name", title:"地市名称",width: 110},
		          { field: "city_name", title:"区县名称",width: 110},
		          { field: "ne_code", title:"基站ID",width: 115},
		          { field: "ne_name_cn", title:"基站名称"},
  		          { field: "cycle_month", title:"月",width: 100},
  		          { field: "cycle_week", title:"周",width: 100},
  		          { field: "cycle_date", title:"日",width: 120},
  		          { field: "cycle_year", title:"年份",width: 120},
  		          { field: "total_score", title:"分数",width: 120},
		          {  width: 120,
					field: "grade",
					template: "#for(var i=0;i<grade;i++){# <img class='starIcon' src='custom/images/starActive.png' /> #} for(var j=grade;j<5;j++){# <img class='starIcon' src='custom/images/starDefault.png' /> #}#", 
					encoded: false,
					cssClass:"primary-emphasis",
					title: "<span class='tdOverFont' title='评分等级'>评分等级</span>" },
				 { width: 100,
					template: "<a onclick='openSum(this)' class='btn btn-warning btn-xs btn-flat'>基站评分图形</a>",
					encoded: false,
					title: "<span class='tdOverFont' title='指标'>操作</span>"
				 }]
		          
	}).data("kendoGrid");
}

function openSum(d){
	
	var grid = sumGrid.dataItem($(d).closest("tr"));
	//window.location.href="sumChartList?&tableName="+parameters.tableName+"&neCode="+grid.ne_code+"&neName="+grid.ne_name_cn;
	window.location.href= "sumChartList?&neCode="+grid.ne_code
										+"&neName=" + grid.ne_name_cn
										+"&startCycleDate="+parameters.startCycleDate
										+"&endCycleDate="+parameters.endCycleDate
										+"&cityCode="+parameters.cityCode
										+"&areaCode="+parameters.areaCode
										+"&grade="+parameters.grade
										+"&neId="+parameters.neId
										+"&tableName="+parameters.tableName
										+"&page="+parameters.page
										+"&types="+parameters.types;

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

/*window.onbeforeunload = onbeforeunload_handler;  
function onbeforeunload_handler(){
	alert("刷新页面")
	window.location.href = "bsSummaryScore";//跳转到指定页面。
	window.attachEvent("onbeforeunload",onbeforeunload_handler());
	
} */

