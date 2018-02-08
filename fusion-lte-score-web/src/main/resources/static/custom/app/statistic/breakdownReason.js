kendo.culture("zh-CN");
var scoreData = [{ text: "小时", value: "1" },{ text: "日", value: "2" },{ text: "周", value: "3" },{ text: "月", value: "4" }];
//var searchParams = {startDate:'',endDate:'',scoreType:'2',area_code:'',city_code:''};
var breakDownDataSource;
var breakReasionDataSource= [];
var gridObj;
var scoreType = 2;
var kpiMap = [];
var columns =[];
var today = new Date();
var searchParams;
var text = "";
function initKpiMap() {
	
	var dtd = $.Deferred(); // 新建一个Deferred对象
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "kpiIndex/getKpiRule",
		"success" : function(data) {
			//console.log(data);
			$.each(data, function(index, item) {
				kpiMap.push(item.KPI_NAME+"占比");
				breakReasionDataSource.push({
					id : item.KPI_ID,
					name : item.KPI_NAME+"占比"
				});
			});
			kpiMap.push('基站告警占比');
	//		kpiMap.push('退服占比');
			breakReasionDataSource.push({
				id : 'alarm',
				name : '基站告警占比'
			});
	/*		breakReasionDataSource.push({
				id:'outof',
				name:'退服占比'
			});*/
			columns.push({
					width: 90,
					template: "<button class='statisticBtn btn btn-danger btn-xs'><i class='fa fa-area-chart'></i>统计</button>",
					encoded: false,
					title: "<span class='tdOverFont' title='统计图'>统计图</span>",
					locked: true,
					lockable: false
			 });
			columns.push({
				field: "area_name",width: 80,
				encoded: false,
				title: "<span class='tdOverFont' title='地市'>地市</span>",
				locked: true,
				lockable: false
			});
			columns.push({
				field: "city_name",width: 80,
				encoded: false,
				title: "<span class='tdOverFont' title='区县'>区县</span>",
				template:
					function(a){
					if(a.city_name=="0"){
						return a.city_name="";
					}else{
						return a.city_name;
					}
				},
				locked:true,
				lockable:false
			});
		     
		     columns.push({
					field: "cycle",
					width: 140,
					template:function(item){
		        		var type = $('#scoreType').val();
		        		if(type==1){
		        			return  new Date(item.cycle).Format("yyyy-MM-dd hh:mm:ss"); 
		        		}else if(type==2){
		        			return item.cycle_date;
		        		}else if(type==3){
		        			return item.cycle_year+'-'+item.cycle_week+'(周)';
		        		}else if(type==4){
		        			return item.cycle_year+'-'+item.cycle_month+'(月)';
		        		}
		        	},
					encoded: false,
					title: "<span class='tdOverFont' title='时间'>时间</span>",
					locked: true,
					lockable: false
			 });
		    
		     for (var i = 0; i < breakReasionDataSource.length; i++) {
					 
				 var KPI_ID_text = breakReasionDataSource[i].id.toLowerCase();			 
				 //console.log(KPI_ID_text +'  '+i);
				 columns.push({
	    						field: KPI_ID_text,
	    						width: 125,
	    						//cycleDate=#:cycle_date#&
	    						template: "<a onclick='btsScore(this)'  kpi='"+KPI_ID_text+"'  style='cursor:pointer;' class='tdOverFont' title='#:"+KPI_ID_text+"#%'>#:"+KPI_ID_text+"#%</a>",
	    						encoded: false,
	    						title: "<span class='tdOverFont' title='"+breakReasionDataSource[i].name+"'>"+breakReasionDataSource[i].name+"</span>"
	    		 });
			 }
		    /* columns.push({
		    	 field: "manufacturer",
		    	 width: 80,
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
		    	 //	encoded: false,
		    	 title: "<span class='tdOverFont' title='厂家'>厂家</span>",
		    	 //	locked: true,
		    	 //hidden: true,
		    	 //	lockable: false
		     });*/
		    dtd.resolve(); // 改变Deferred对象的执行状态
		}
	});
    return dtd;
}

$(function() {
	
	
	var areaCode = getQueryString("areaCode");
	var cityCode = getQueryString("cityCode");
	var startDate = getQueryString("startDate");
	var endDate = getQueryString("endDate");
	var scoreType = getQueryString("scoreType");
	searchParams={
			areaCode: (areaCode && areaCode!=null && areaCode !="null")?areaCode:"",
			cityCode: (cityCode && cityCode!=null && cityCode !="null")?cityCode:"",
			startDate: (startDate && startDate !=null && startDate !="null")?startDate:new Date(today.getFullYear(), today.getMonth(),today.getDate()-1).Format("yyyy-MM-dd hh:00:00"),  //开始是时间为昨天,
			endDate: (endDate && endDate!=null && endDate !="null")?endDate:today,
			scoreType: (scoreType && scoreType!=null && scoreType !="null")?scoreType:"2"
	}
	
	var type = $("#hiddenScoreType").val();
	if(type !='' && type != null && type !='null'){
		$("#returnBtn").show();
	}
	else{
		$("#returnBtn").hide();
	}
	
	
	initScoreType(scoreData,searchParams.scoreType);

	formatDate(searchParams.scoreType,searchParams,false);

	setHiddenValue();
	//地市 区县下拉
	initCityList();
	initAreaList();
	
	//chart图
	var lineOption = {
		title: {
			text: '',
			subtext: ''
		},
		tooltip: {
			trigger: 'axis',
			formatter: "{b}：{c}%",
			axisPointer:{
				lineStyle: {
					color: '#ddd',
					width: 2
				}
			}
		},
		legend: {
			data: ['指标'],
			show:false,
			textStyle:{
				color: "#fff"
			}
		},
		toolbox: {
			show: false
		},
		color:["#fff"],
		grid:{
			borderColor:"#4DA7F8",
			y: 15
		},
		xAxis: [{
			type: 'category',
			boundaryGap: false,
			splitLine:{
				lineStyle:{
					color: "#4DA7F8"
				}
			},
			axisLine: {
				show: false
			},
			axisTick: {
				show: false
			},
			axisLabel: {
				textStyle: {
					color: "#fff",
					fontFamily:"微软雅黑"
				},
				rotate: 15
			},
			data:kpiMap
		}],
		yAxis: [{
			name:"",
			type: 'value',
			splitLine:{
				lineStyle:{
					color: "#4DA7F8"
				}
			},
			axisLine: {
				show: true,
				lineStyle:{
					color:"#fff",
					width: 0
				}
			},
			axisTick: {
				show: false
			},
			axisLabel: {
				textStyle: {
					color: "#fff"
				},
				formatter: "{value}%"
			}
		}],
		series: [{
			name: '指标',
			type: 'line',
			smooth: true,
			symbolSize: 5,
			itemStyle: {
				normal: {
					areaStyle: {
						type: 'default'
					},
					
				}
			},
			data: [0.4, 10, 43, 79, 39, 30, 10,12,10,34,22,34,78,23,56,67,45,78]
		}, ]
	};
	//评分故障
	breakDownDataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "getBreakDownReason"
            },
            parameterMap: function (options, operation) {   
                if (operation == "read") {
                	 searchParams.startDate =  $('#startDate').val();
                	 searchParams.endDate =  $('#endDate').val();
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
            	 return data.rows;//返回页面数据
            },
            total: function (data) {
            	return data.total; //总条数
            }
        },
        serverPaging : true,
		serverFiltering : true,
		serverSorting : true
    });	 
	
//	条件查询按钮
	$('#searchBtn').on('click', function() {
	 
		searchParams.startDate = $('#startDate').val(); //开始时间
		searchParams.endDate = $('#endDate').val();  //结束时间
		searchParams.scoreType = $('#scoreType').val();  //统计方式
		searchParams.areaCode = $('#areaCode').val(); //地区
		searchParams.cityCode = $('#cityCode').val(); //地市
		scoreType =  $('#scoreType').val();
		//$("#gridList").data("kendoGrid").pager.page(1);
		//条件查询重新加载 
		breakDownDataSource.read(searchParams);
	});
	// 重置
	$('#resetBtn').click(function(){
		
		$('#cityCode').data("kendoDropDownList").value(''); 
		$('#areaCode').data("kendoDropDownList").value('');
		$('#scoreType').data("kendoDropDownList").value('2');
		scoreType =  $('#scoreType').val();
		
		formatDate(scoreType,searchParams,false);
		
		$('#startDate').data("kendoDateTimePicker").value(new Date(today.getFullYear(), today.getMonth(),today.getDate()-1).Format("yyyy-MM-dd HH:00:00"));
		$('#endDate').data("kendoDateTimePicker").value(today);
		
		searchParams.startDate=$('#startDate').val();
		searchParams.endDate=$('#endDate').val();
		
		searchParams.cityCode="";
		searchParams.areaCode="";
		breakDownDataSource.read(searchParams);
	});
	
	//列表----评分故障原因统计
	gridObj = {
 
		gridGrid:undefined,
		init: function() {
			
			this.gridGrid = $("#gridList").kendoGrid({

				dataSource: breakDownDataSource,
				toolbar: kendo.template($("#template").html()),
				reorderable: true,
				resizable: true,
				sortable: true,
			//	columnMenu: true,
				pageable: true,
				columns: columns,
				dataBound: function(){
					
					//统计按钮
					$(".statisticBtn").on("click",function(){
						$('#myModal').modal('show');
						var data = gridObj.gridGrid.dataItem($(this).closest("tr"));
						var chartData=[];
						for (var i = 0; i < breakReasionDataSource.length; i++) {
								var KPI_ID_text = breakReasionDataSource[i].id.toLowerCase();
								chartData.push(data[KPI_ID_text]);
						}
						//窗口打开之后，加载图表
						$("#myModal").on("shown.bs.modal",function(){
							var typeVal = 	 $('#scoreType').val();
							if(typeVal == 1){
								text = "时间："+data.cycle;
							}else if(typeVal == 2){
								text = "时间："+data.cycle_date
							}else if(typeVal == 3){
								text = "时间："+data.cycle_year+"年第"+data.cycle_week+" 周";
							}else if(typeVal == 4){
								text = "时间："+data.cycle_year+"年 "+data.cycle_month+" 月";
							}
							$("#myModalLabel").text(text);
							var lineChart = echarts.init(document.getElementById('lineChart'));
							lineOption.series[0].data = chartData;
							lineChart.setOption(lineOption);
						});
					});
					//导出
				    $("#btn_Export").on("click",function(){
				    	var startDate = $('#startDate').val(); //开始时间
						var endDate = $('#endDate').val();  //结束时间
						var scoreType = $('#scoreType').val();  //类型
						var areaCode = $('#areaCode').val(); //地区
						var city = $('#cityCode').val(); //区县
						if(city == null || city =="null"){
							city = '';
						}
						window.location.href = "exportBreakReason?startDate="+startDate+"&endDate="+endDate+"&scoreType="+scoreType
							+"&cityCode="+city+"&areaCode="+areaCode;
					});
				    
				}
			}).data("kendoGrid");
		}
	};
	
	$.when(initKpiMap()).done(function(){
		gridObj.init();
	});
	
    function setHiddenValue(){
    	if($("#hiddenScoreType").val()!=""  && $("#hiddenScoreType").val()!=null && $("#hiddenScoreType").val()!="null"){
    		searchParams.scoreType = $("#hiddenScoreType").val();
    		$('#scoreType').data("kendoDropDownList").value($("#hiddenScoreType").val());
    		scoreType = searchParams.scoreType ;
    	}
    }
	 
});

function btsScore(d){
	 
	var kpi = $(d).attr("kpi");
	var data = gridObj.gridGrid.dataItem($(d).closest("tr"));
	var type = $("#scoreType").val();
	var sdate;
	var edate;
	if(type==1){
		 var date= data.cycle;
		 var d1=new Date(date);
		 var d2=d1.getTime()-1*60*60*1000;
		 sdate= new Date(d2).Format("yyyy-MM-dd hh:00:00");
		 edate= data.cycle;
	}else if(type==2){
		 sdate= data.cycle_date;
		 edate= data.cycle_date;
	}else if(type==3){
		sdate= data.cycle_year+"-"+data.cycle_week;
		edate= data.cycle_year+"-"+data.cycle_week;
	}else if(type==4){
		 sdate= data.cycle_year+" "+cycle_month;
		 edate= data.cycle_year+" "+cycle_month;
	}
/*	if(kpi=='outof'){
		kpi="out_of";
	}*/
	//console.log(type+"==========="+sdate);
	// href='bsCurrentScore?cycleDate=#:cycle_date#&cityCode=#:city_code#&areaCode=#:area_code#&kpiId="+KPI_ID_text+"&bsValue=2"+"&type=3'"
	window.location.href="bsCurrentScore?areaCode="+ data.area_code + "&cityCode="+ data.city_code+ "&startDate=" +sdate
										+ "&endDate=" + edate+"&kpiId="+kpi+"&bsValue=2"+"&type=2"+"&grade=1"+"&tjType="+type;
			
}


function initCityList(){
	
	var dtd = $.Deferred(); // 新建一个Deferred对象
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

