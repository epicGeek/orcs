kendo.culture("zh-CN");
var deltaTime = "1440";//default 5 min
var dhssNames = [];
var gridColumn = [
					{
						field : "counterName",
						template : "<div  oncontextmenu=\"contextmenuClick('','#:counterType#','#:counterUnit#')\"><span  title='#:counterName#'>#:counterName#</span></div>",
						title : "<span  title='指标名称'>指标名称</span>",
			            attributes: {
			                "class": "contextMenu"
			            }
					}
					];

function getDhssName(){
	$.ajax({
		type:"GET",
		url:"volte-list/getDhssName",
		async:false,
		success:function(data){
			dhssNames = data;
			console.log(data)
		}
	});
	
}
function changeGridColumn(){
	$.each(dhssNames,function(index,item){
		gridColumn[gridColumn.length] = {
				field : item,
				template : "<div  oncontextmenu=\"contextmenuClick('"+item+"','#:counterType#','#:counterUnit#')\"><span title='#:"+item+"#'>#:"+item+"#</span></div>",
				title : "<span  title='"+item+"'>"+item+"</span>",
	            attributes: {
	                "class": "contextMenu"
	            }
			}
	});
	return gridColumn;
}
var dhssName = "";
var colName = "";
var volteUnit = "";
var kpiWindow;
function contextmenuClick(text,col,unit){
	 dhssName = text;
	 colName = col;
	 volteUnit = unit;
}
function initRightClick(){
    //右键菜单
    $("#menu").kendoContextMenu({
        target: "#dataGrid",
        filter: ".contextMenu",
        select: function(e) {
            var item = $(e.item);
            switch (item.attr('types')) {
	            case "volteCounter":
	            	$.ajax({
	            		url : "volte-list/chart-line",
	            		type : "GET",
	            		dataType: "json",
	            		data:{dhssName:dhssName,colName:colName,startTime:$("#startTime").val(),endTime:$("#endTime").val()},
	                    contentType: "application/json;charset=UTF-8",
	            		success : function(data) {
	            			var chartLines = [];
	            			var dhssLineNames = [];
	            			$.each(dhssNames,function(index,items){
	            				
	            				if(data["result"][dhssNames[index]].length > 0){
	            					dhssLineNames[dhssLineNames.length] = dhssNames[index] ;
	            					chartLines[chartLines.length] = {
		    	                            name: dhssNames[index],
		    	                            type: 'line',
		    	                            data: data["result"][dhssNames[index]],
		    	                            markPoint: {
		    	                                data: [{
		    	                                    type: 'max',
		    	                                    name: '最大值'
		    	                                }, {
		    	                                    type: 'min',
		    	                                    name: '最小值'
		    	                                }]
		    	                            }
		    	                        };
	            				}
	            				
	            			})
	            			console.log(colName);
	            			kpiWindow.obj.open().center();
    	                    kpiWindow.initKpiChart(dhssName,data.dates,chartLines,volteUnit,dhssLineNames,colName);
	            		}
	            	});
	            	break;
    		}
        }
    });
}


function volteWindow(){
	//kpi指标趋势弹窗
    kpiWindow = {
    	
        id: $("#kpiWindow"),
       
        initKpiChart: function(dn,datas,column,unit,dhssLineNames,chartTitile) {
            var myChart = echarts.init(document.getElementById('kpiChart'));
            var option = {
                title: {
                    text: '',
                    subtext: kpiNameTransform(chartTitile)
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: dhssLineNames
                },
                toolbox: {
                    show: true,
                    feature: {
                        mark: {
                            show: false
                        },
                        dataView: {
                            show: true,
                            readOnly: false
                        },
                        magicType: {
                            show: true,
                            type: ['line']
                        },
                        restore: {
                            show: true
                        },
                        saveAsImage: {
                            show: true
                        }
                    }
                },
                calculable: true,
                xAxis: [{
                    type: 'category',
                    data: datas
                }],
                yAxis: [{
                    type: 'value',
                    name: '',
                    axisLabel: {
                        formatter: '{value} '+unit
                    }
                }],
                series: column
            };
            // 为echarts对象加载数据
            myChart.setOption(option);
        },

        init: function() {
            if (!kpiWindow.id.data("kendoWindow")) {
                kpiWindow.id.kendoWindow({
                    width: "800px",
                    actions: ["Close"],
                    modal: true,
                    title: "历史趋势"
                });
            }
            kpiWindow.obj = kpiWindow.id.data("kendoWindow");
        }
    };

    kpiWindow.init();
 
}
$(function() {
	getDhssName();
	loadDefaultTime();
	volteWindow();
	initRightClick();
	changeGridColumn();
	loadDataGrid();
	$("#downloadFileBtn").click(function(){
		console.log($('#startTime').val());
		console.log($('#endTime').val());
		window.location.href="/volte-counter/batchDownload?" +
		 "startTime="+ $('#startTime').val()+
		 "&endTime="+$('#endTime').val()
	});
});
function kpiNameTransform(fieldName){
	if(fieldName=="subs_hss_2_boss"){
		return "上行(HSS2BOSS)VoLTE业务自动开通请求数量";
	}
	if(fieldName=="rates_hss_2_boss"){
		return "上行(HSS2BOSS)VoLTE业务自动开通成功率";
	}
	if(fieldName=="subs_boss_2_hss_t"){
		return "下行(BOSS2HSS)VoLTE业务自动开通请求数量";
	}
	if(fieldName=="rates_boss_2_hss_t"){
		return "下行(BOSS2HSS)VoLTE业务自动开通成功率";
	}
	if(fieldName=="subs_boss_2_hss_o"){
		return "VoLTE业务主动开通请求数量";
	}
	if(fieldName=="rates_boss_2_hss_o"){
		return "VoLTE业务主动开通成功率";
	}
	if(fieldName=="ads_boss_hss"){
		return "BOSS开通VoLTE与HSS提醒时间平均差";
	}
}
function loadDefaultTime(){
	$.ajax({
		url:"volte-counter/counter-time-scope",
		type:"GET",
		async:false,
		success:function(data){
			var startTime = data.startTime;
			var endTime = data.endTime;
			$("#startTime").kendoDateTimePicker({
				format : "yyyy-MM-dd HH:mm:ss",
				value : startTime
			});
			$("#endTime").kendoDateTimePicker({
				format : "yyyy-MM-dd HH:mm:ss",
				value : endTime
			});
		}
	});
}
function loadDataGrid(){
	var searchParams = {
			page : 0,
			size : 20,
			sort : "",
		
		};
	dataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
				url : "/volte-list/volteCounter",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			},
			parameterMap : function(options, operation) {
				if(operation == "read"){
					searchParams.startTime = $("#startTime").val();
					searchParams.endTime = $("#endTime").val();
					return searchParams;
				}

			},
},
				batch : true,
				pageSize : 20, // 每页显示个数
				schema : {
					data : function(data) {
						if (data) {
							return data; // 响应到页面的数据
						} else {
							return new Array();
						}
					},
					total : function(data) {
						return data.length; // 总条数
					},
				},
				serverPaging : false,
				serverFiltering : false,
				serverSorting : false
	});
				grid = $("#dataGrid").kendoGrid({
					dataSource : dataSource,
					height : $(window).height() - $("#dataGrid").offset().top - 50,
					width : "100%",
					reorderable : true,
					resizable : true,
					sortable : true,
					columnMenu : true,
					pageable : true,
					columns : gridColumn
				
				}).data("kendoGrid");
}