kendo.culture("zh-CN");
var searchparams = {ids:""};
var colorarr = ["","gray","orange","red"];
var errorMessage=[];
var audio;


function getColor(i,text){
	if(i != 0){
		var arr = text.split("&");
		if(arr.length == 7 && command != "" && unit != ""){
        	var command = arr[3];
        	var unit = arr[4];
        	if(getIsCancel(text) == ""){
        		errorMessage[errorMessage.length] ={monitoredUnitName : unit,monitoredCommandName:command,resultValue:arr[0]} ;
        	}
		}
	}
	return colorarr[i];
}
var contentMenuValue="";
var infoWindow;
var errorMessageWindow;
//resultValue格式："result_value&result_level&result_path&monitored_command_name&monitored_unit_name&monitored_command_id&monitored_unit_id";
var dataSource;
function refMoney(){
	errorMessage = [];
	dataSource.read();
}
var emsMutedItem = [];
function getIsCancel(text){
	var value = "";
	var i = resultUtils(text,5);
	var u = resultUtils(text,6);
	$.each(emsMutedItem,function(index,item){
		if(item.mutedCommandId == i && item.mutedUnitId == u){
			value = "glyphicon glyphicon-volume-off";
			return false;
		}
	})
	return value;
}
var gridList;
var flagRun = true;
$(function() {
	
	
	
	$("[ href = 'emsResult' ]").addClass("active");
	
	
	var type;
	var units;
	var commands;
	$('#groups').kendoDropDownList({optionLabel : "请选择组"});
	$.ajax({
		url : "rest/equipment-node-group",
		type : "GET",
		dataType: "json",
        contentType: "application/json;charset=UTF-8",
		success : function(data) {
			$('#groups').kendoDropDownList({
				optionLabel : "请选择组",
		        filter: "contains",
		        dataTextField: "groupName",
		        dataValueField: "unitIdCol",
		        dataSource: data._embedded ? data._embedded["equipment-node-group"] : [],
		        change:function(){
		        	refMoney();
		        }
			}).data("kendoDropDownList");
		}
	});
	
	setInterval("refMoney()", 1000*60*5); 
	
	
	
	
	
	
	dataSource = new kendo.data.DataSource({
        type: "odata",
        transport: {
            read: {
                type: "GET",
                url: "emsResult/result",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            },
            parameterMap: function(options, operation) {
                if (operation == "read") {       
                    searchparams.ids = $('#groups').data("kendoDropDownList").text() == "请选择组" ? "ALL" : $("#groups").val();
                    return searchparams;                   
                }     
            }
        },
        schema: {
        	data: function(d) {  
        		$.ajax({
        			url : "emsResult/emsMutedItem",
        			type : "GET",
        			dataType: "json",
        	        contentType: "application/json;charset=UTF-8",
        	        async: false,
        			success : function(data) {
        				emsMutedItem = data;
        			}
        		});
        		
        		$("#dataGrid").html("");
        		var column= [{
        	        template: "<span  title='#:monitored_command_name#'>#:monitored_command_name#</span>",
        	        title: "检查项",
        	        locked: false,
        	        lockable: false,
        	        width: 180,
        	        attributes: {
        	            "class": "text-right"
        	        }
        	    }];
        		$.each(d.units,function(index,item){
					var i = (index+1);
					column[column.length] = {
			            field: "#:value"+i+"#",
			            template: "<div oncontextmenu='contextmenuClick(\"#:value"+i+"#\",\"#:getIsCancel(value"+i+")#\")'>" +
			            		"	<span class='circle' title='#:resultUtils(value"+i +",0)#' style='display:block;background-color:" +
			            				"#:getColor(resultUtils(value"+i+",1),value"+i+")#;float:left'></span>&nbsp;&nbsp;#:resultUtils(value"+i+",0)#" +
			            				"&nbsp;&nbsp; <span style = 'font-size:15px;'><i class='#:getIsCancel(value"+i+")#'></i></span></div>",
			            title: "<span  title='"+item.monitored_unit_name+"'>"+item.monitored_unit_name+"</span>",
			            width: 220,
			            attributes: {
			                "class": "contextMenu"
			            }
			        };
				})
				if(column.length == 1){  
					column[0].title="没有记录";
					column[column.length] = { field: "", template: "",  title: "", width:220 };
				}
        		gridList = $("#dataGrid").kendoGrid({
        	        dataSource: d.result,
        			height : $(window).height() - $("#dataGrid").offset().top - 70,
        	        columns: column,
        	        resizable: true
        	    }).data("kendoGrid");
        		console.log(flagRun);
        		if(flagRun){
        			myfun();
        		}else{
        			flagRun = true;
        		}
            	return d.result;
            },
            total: function(d) {
            	return d.length;
            }
        }
    });
	
	var data = dataSource.read();
	
	
	

    //右键菜单
    $("#menu").kendoContextMenu({
        target: "#dataGrid",
        filter: ".contextMenu",
        select: function(e) {
            var item = $(e.item);
            if(contentMenuValue==""){
        		infoTip({content:"请选择信息！"});
        	}else{
        		var arr = contentMenuValue.split("&");
        		if(arr.length == 7 && command != "" && unit != ""){
	            	var command = arr[3];
	            	var unit = arr[4];
	            	
	        		switch (item.attr('types')) {
		                case "kpi": //KPI历史趋势
		                		$.ajax({
			                		url : "emsResult/trend",
			                		type : "GET",
			                		dataType: "json",
			                		data:{command:command,unit:unit,sort:"executeTime,asc"},
			                        contentType: "application/json;charset=UTF-8",
			                		success : function(data) {
			                			var dates = [];
			                			var array = [];
			                			$.each(data,function(index,item){
			                				dates[dates.length] = item.executeTime;
			                				if(isNaN(item.resultValue)){
			                					array[array.length] = item.resultValue == "正常" ? 100 : 0;
			                				}else{
			                					array[array.length] = item.resultValue;
			                				}
			                				
			                			})
			                			if(dates.length == 0){
			                				dates = ['没有记录'];
				                			array = [0];
			                			}
			                			kpiWindow.obj.open().center();
			    	                    kpiWindow.initKpiChart(unit,command,dates,array);
			                		}
			                	});
		                    break;
		                case "notice": //取消通知设置
		                	infoWindow.obj.setOptions({
								"title": "取消通知设置"
							});
		                	type = item.attr('count');
		                	units = unit;
		                	commands = command;
							infoWindow.initContent();
		                    break;
		                case "cancel":
		                	$.ajax({
		                		url : "emsResult/notice",
		                		type : "GET",
		                		dataType: "json",
		                		data:{command:command,unit:unit,type:item.attr('count')},
		                        contentType: "application/json;charset=UTF-8",
		                		success : function(data) {
		                			console.log(endTime);
		                			if(data){
		                				infoWindow.obj.close();
		                				infoTip({content:"恢复成功！",color:"blue"});
		                				flagRun = false;
		                				refMoney();
		                			}else{
		                				infoTip({content:"恢复失败！"});
		                			}
		                		}
		                	});
		                	break;
		                case "download": //报文下载
		                	var path = contentMenuValue.split("&")[2];
		                    location.href = "emsResult/download?path="+path;
		                    break;
	        		}
        		}else{
            		infoTip({content:"没有数据记录！"});
            	}
        		contentMenuValue = "";
        	}
            
        }
    });
    
    infoWindow = {
    		obj: undefined,
    		template: undefined,
    		id: $("#infoWindow"),
    		saveClick: function() {
    			$("#saveBaseInfoBtn").on("click", function() {
    	            	var endTime = $("#endTime").val();
                    	$.ajax({
	                		url : "emsResult/notice",
	                		type : "GET",
	                		dataType: "json",
	                		data:{command:commands,unit:units,end:endTime,type:type},
	                        contentType: "application/json;charset=UTF-8",
	                		success : function(data) {
	                			if(data){
	                				infoWindow.obj.close();
	                				infoTip({content:"取消成功！"});
	                				flagRun = false;
	                				refMoney();
	                			}else{
	                				infoTip({content:"取消失败！"});
	                			}
	                		}
	                	});
    			});
    		},
    		initContent: function() {
    			$("#endTime").val("");
    			infoWindow.obj.center().open();
    		},

    		init: function() {

    			if (!infoWindow.id.data("kendoWindow")) {
    				infoWindow.id.kendoWindow({
    					width: "450px",
    					height: "150px",
    					actions: ["Close"],
    					modal: true
    				});
    			}
    			$("#endTime").kendoDateTimePicker({
					format: "yyyy-MM-dd HH:mm:ss"
				});
    			infoWindow.obj = infoWindow.id.data("kendoWindow");
    			infoWindow.saveClick();
    			$(".cancelBtns").on("click", function(){
    				infoWindow.obj.close();
    			});
    		}
    	};


    	infoWindow.init();
    	
    	
    	errorMessageWindow = {
        		obj: undefined,
        		template: undefined,
        		id: $("#errorMessageWindow"),
        		initContent: function() {
        			errorMessageWindow.obj.center().open();
        		},
        		init: function() {
        			if (!errorMessageWindow.id.data("kendoWindow")) {
        				errorMessageWindow.id.kendoWindow({
        					width: "550px",
        					height:"400px",
        					actions: ["Close"],
        					modal: true
        				});
        			}
        			errorMessageWindow.obj = errorMessageWindow.id.data("kendoWindow");
        		}
        };


    	errorMessageWindow.init();

    //kpi指标趋势弹窗
    var kpiWindow = {
    	
        id: $("#kpiWindow"),

        initKpiChart: function(unit,command,datas,array) {

            var myChart = echarts.init(document.getElementById('kpiChart'));
            var option = {
                title: {
                    text: '',
                    subtext: ''
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: [command+" - "+unit]
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
                        formatter: '{value} %'
                    }
                }],
                series: [{
                    name: command+" - "+unit,
                    type: 'line',
                    data: array,
                    markPoint: {
                        data: [{
                            type: 'max',
                            name: '最大值'
                        }, {
                            type: 'min',
                            name: '最小值'
                        }]
                    }
                }]
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
    
    audio = document.getElementById('audiot');
    
});
function myfun()
{
	errorDataSource = new kendo.data.DataSource({
		transport : { 
			read : {
				type : "GET",
				url : "emsResult/errorItems",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			}
		},
		schema : {
			data : function(d) {
				return $('#groups').data("kendoDropDownList").text() == "请选择组" ? d : errorMessage ;
			}
		}
	});
	errorMessageWindow.obj.close();
	if(errorMessage.length != 0){
		if(audio.paused){
			audio.play();
		}else{
			audio.pause();
		}
		errorMessageWindow.obj.setOptions({
			"title": "异常信息"
		});
		$("#errorGrid").html("");
		$("#errorGrid").kendoGrid({

	        dataSource: errorDataSource,
	        resizable: true,
			height : $(window).height() - $("#errorGrid").offset().top - 280,
	        columns: [{
	        	field: "monitoredUnitName",
	            title: "单元",
	            template: "<span  title='#:monitoredUnitName#'>#:monitoredUnitName#</span>" 
	        }, {
	            field: "monitoredCommandName",
	            title: "指标",
	            template: "<span  title='#:monitoredCommandName#'>#:monitoredCommandName#</span>"
	        }, {
	            field: "resultValue",
	            title: "value",
	            template: "<span  title='#:resultValue#'>#:resultValue#</span>"
	        }]
	    });
		errorMessageWindow.initContent();
    }
	flagRun = true;
}

function resultUtils(text,type){
	if(text == null || text == ""){
		return "";
	}
	var arr = text.split('&');
	return arr[type];
}

function contextmenuClick(val,type){
	contentMenuValue = val;
	if(type != ""){
		$("[ types = 'cancel']").show();
		$("[ types = 'notice']").hide();
	}else{
		$("[ types = 'cancel']").hide();
		$("[ types = 'notice']").show();
	}
}



























