/**
 * 地区评分查询
 * */
var today = new Date();
var searchParams = {areaCode:'',cityCode:'',scoreType:'2',startDate:'',endDate:'',dataType:'0'};
var scoreData = [{ text: "小时", value: "1" },{ text: "日", value: "2" },{ text: "周", value: "3" },{ text: "月", value: "4" }];
var areaDataSource;
var sumGrid;  //列表grid
var scoreType = 2;

$(function() {
	
	kendo.culture("zh-CN");
	
	initScoreType(scoreData,scoreType); //初始统计类型
	
	formatDate(scoreType,searchParams,false);	//默认初始化天
	
	areaDataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                dataType : "json",
				contentType : "application/json;charset=UTF-8",
                url: "getAreaScore"
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
	
	initGridList(); //初始化汇总列表
	
	initAreaList();   //初始化区
	
	initCityList();   //初始化市
	
//	条件查询按钮
	$('#searchBtn').on('click', function() {
		searchParams.areaCode = $('#areaCode').val(); //地区
		searchParams.cityCode = $('#cityCode').val(); //地市
		if(searchParams.areaCode == 0 ){
			searchParams.areaCode ="";
			searchParams.dataType="";
		}
		else if(searchParams.areaCode == 1 ){
			searchParams.areaCode ="0";
			searchParams.dataType="1";
		}
		if(searchParams.cityCode == 0 ){
			searchParams.cityCode ="";			
		}
		else if(searchParams.cityCode == 1 ){
			searchParams.cityCode ="";
			searchParams.dataType="2";
		}else{
			searchParams.dataType="0";
		}
		searchParams.startDate = $('#startDate').val(); //开始时间
		searchParams.endDate = $('#endDate').val();  //结束时间
		searchParams.scoreType = $('#scoreType').val();  //类型
		$("#gridList").data("kendoGrid").pager.page(1);
		//条件查询重新加载 
		areaDataSource.read(searchParams);
	});
	
	// 重置
	$('#resetBtn').click(function(){
		$('#areaCode').data("kendoDropDownList").value('0');
		getCityData(0);
		$('#cityCode').data("kendoDropDownList").value('0');		
		
		$('#scoreType').data("kendoDropDownList").value('2');
		scoreType =  $('#scoreType').val();
		
		formatDate(scoreType,searchParams,false);
		
		$('#endDate').data("kendoDateTimePicker").value(today);
		$('#startDate').data("kendoDateTimePicker").value(new Date(today.getFullYear(), today.getMonth(),today.getDate()-1).Format("yyyy-MM-dd HH:00:00"));
		
		searchParams.startDate = $('#startDate').val();
		searchParams.endDate = $('#endDate').val();
		
		areaDataSource.read(searchParams);
	});
	
});

function initAreaList(){
	var defaultData = [{ areaCode: "0" , areaCn: "全部" },{ areaCode: "1" , areaCn: "全省" }];
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "city/search",
		"success" : function(data) {
			
			$.each(data,function(index,item){
				defaultData.push({areaCode: item.areaCode, areaCn: item.areaCn });
			});
			$("#areaCode").kendoDropDownList({
				//optionLabel:"--请选择地市--",
	            dataTextField: "areaCn",
	            dataValueField: "areaCode",
	            dataSource: defaultData,
	            filter: "contains",
	            index: 0, // 当前默认选中项，索引从0开始。
	            change: function(){
	            	var areaCode = $('#areaCode').val();
	            	getCityData(areaCode);
	            }
	        });
			
		}
	});
}

function getCityData(areaCode){
	var defaultData ="";
   
	if(areaCode == 1) {
		defaultData = [{ cityCode: "0" , cityCn: "全部" }];
	}
	else {
		 defaultData = [{ cityCode: "0" , cityCn: "全部" },{ cityCode: "1" , cityCn: "全市" }];
	}
	
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "area/search",
		data:{
			"cityCode":areaCode
		},
		index: 0, // 当前默认选中项，索引从0开始。
		"success" : function(data) {
			
			
			$.each(data,function(index,item){
				defaultData.push({cityCode: item.cityCode, cityCn: item.cityCn });
			});
			$("#cityCode").data("kendoDropDownList").setDataSource(defaultData);
			//$("#cityCode").data("kendoDropDownList").refresh();
		}
	});
}

function initCityList(){
	var defaultData = [{ cityCode: "0" , cityCn: "全部" },{ cityCode: "1" , cityCn: "全市" }];
	 
	$("#cityCode").kendoDropDownList({
		//optionLabel:"--请选择区县--",
        dataTextField: "cityCn",
        dataValueField: "cityCode",
        dataSource: defaultData,
        filter: "contains",
        index: 0, // 当前默认选中项，索引从0开始。
        
    });
}

//列表
function initGridList(){
	
	sumGrid = $("#gridList").kendoGrid({
	
		dataSource: areaDataSource,
		reorderable: true,
		resizable: true,
		sortable: true,
		pageable: true,
		toolbar: kendo.template($("#template").html()),
		columns: [
		          { field: "cycle", title:"时间",width: 140,
		        	template:function(item){
		        		var type = $('#scoreType').val();
		        		if(type==1){
		        			return item.cycle;
		        		}else if(type==2){
		        			return item.cycle_date;
		        		}else if(type==3){
		        			return item.cycle_year_of_week+'-'+item.cycle_week+'(周)';
		        		}else if(type==4){
		        			return item.cycle_year+'-'+item.cycle_month+'(月)';
		        		}
		        	}
		          },
		          { field: "area_name", title:"地市",width: 100,
		        	  template: function(dataItem) {
		        		  if(dataItem.data_type==1 || dataItem.area_code==0){
			        	 		return "全省";
			        	  }
		        		  else{
			        	 	    return dataItem.area_name;
			        	  }
		              }
		          },
		          { field: "city_name", title:"区县",width: 100,
		        	  template: function(dataItem) {
		        	 	 if(dataItem.data_type==1 || dataItem.area_code==0){
		        	 		    return "全省";
		        	 	 }
		        	 	 else if(dataItem.data_type==2 || dataItem.city_code==1){
			        	 		return "全市";
			        	 }
		        	 	 else{
		        	 		    return dataItem.city_name;
		        	 	 }
		        	 	 
		        	 }
		          },
		          {
						field: "city",
						template: '<table class="table-progress" >'
										+'<tbody>'
											+'<tr style="height: 16px;line-height:16px;">'
												+'<td class="progress-bar-danger-lighter" style="width: #:grade1#%;">#:grade1#%</td>'
												+'<td class="progress-bar-warning-lighter" style="width: #:grade2#%;">#:grade2#%</td>'
												+'<td class="progress-bar-yellow-lighter" style="width: #:grade3#%;">#:grade3#%</td>'
												+'<td class="progress-bar-info-lighter" style="width: #:grade4#%;">#:grade4#%</td>'
												+'<td class="progress-bar-success-lighter" style="width: #:grade5#%;">#:grade5#%</td>'
											+'</tr>'
										+'</tbody>'
								 +'</table>',
						encoded: false,
						title: "<div style='text-align:center;'><span class='tipBlock progress-bar-danger-lighter'></span>一级"
									+"<span class='tipBlock progress-bar-warning-lighter'></span>二级"
									+"<span class='tipBlock progress-bar-yellow-lighter'></span>三级"
									+"<span class='tipBlock progress-bar-info-lighter'></span>四级"
									+"<span class='tipBlock progress-bar-success-lighter'></span>五级</div>"
					}
		      ]
		          
	}).data("kendoGrid");
	
	//导出
    $("#btn_Export").on("click",function(){    	
    	var areaCode = $('#areaCode').val(); //地区
    	var cityCode = $('#cityCode').val(); //地市
    	var dataType = "";
		if(areaCode == 0 ){
			 areaCode = "";
			 dataType = "";
		}
		else if(areaCode == 1 ){
			 areaCode = "";
			 dataType = "1";
		}
		if( cityCode == 0 ){
			 cityCode = "";			
		}
		else if(cityCode == 1 ){
			 cityCode ="";
			 dataType="2";
		}else{
			 dataType="0";
		}
		var startDate = $('#startDate').val(); //开始时间
		var endDate = $('#endDate').val();  //结束时间
		var scoreType = $('#scoreType').val();  //类型
		window.location.href = "exportAreaScore?startDate="+startDate+"&endDate="+endDate+"&scoreType="+scoreType+"&dataType="+dataType+"&areaCode="+areaCode+"&cityCode="+cityCode;
	});
}	
