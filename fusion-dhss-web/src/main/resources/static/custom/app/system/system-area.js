var searchparams = { page : 0 , size : 10 , searchparams : ""};
var count = 0;
function isNotEmpty(value){
	if(value == null || value == "null" || value == ""){
		return "";
	}else{
		return value;
	}
}
var allArray = [];
$(function() {
	/*当前导航*/
	$("#topNavList .navListWrap:eq(3) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(3) ul li:eq(1)").addClass("active");
	$("#navList a[href='system-area']").addClass("active");
	var dataItem = {};
	var dataSource=new kendo.data.DataSource({
        type: "odata",
        transport: {
            read: {
                type: "GET",
                url: "system-area/search/all",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            },
            parameterMap: function(options, operation) {
                if (operation == "read") {       
                    searchparams.page = options.page-1;
                    searchparams.size = options.pageSize;
                    $("#inputKeyWord").val("");
//                    searchparams.searchField = $("#inputKeyWord").val();
                    return searchparams;                   
                }     
            }
        },
        schema: {
        	data: function(d) {  
        		/*var rdata = [];
        		if(d._embedded){
        			return  d._embedded["system-area"]; //响应到页面的数据
        		}*/
        		allArray = d;
            	return d;
            },
            total: function(d) {
            	/*if(d._embedded){
            		return d._embedded["system-area"].length; 
            	}*/
            	return d.length;
            }
        },
        pageSize: 20,
        serverPaging: false,
        serverSorting: false
    });
	var dataIds = [];
	var allIds = [];
	var dataGridObj = {
		
		init: function(){
			this.dataGrid = $("#dataGrid").kendoGrid({			
				dataSource: dataSource,				
				height: $(window).height()-$("#dataGrid").offset().top - 50,				
				reorderable: true,		
				resizable: true,		
				sortable: true,		
				columnMenu: false,		
				pageable: true,		
				columns: [{ 
					field: "id", 
					title:"id", 
					hidden:true
				},{
					field: "areaCode",
					template: "<span  title='#:isNotEmpty(areaCode)#'>#:isNotEmpty(areaCode)#</span>",
					title: "<span  title='地区编码'>地区编码</span>"
				}, {
					field: "areaName",
					template: "<span  title='#:isNotEmpty(areaName)#'>#:isNotEmpty(areaName)#</span>",
					title: "<span  title='地区名称'>地区名称</span>"
				}, {
					template: "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 编辑</button>&nbsp;&nbsp;"+
							  "<button class='deleteBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon-remove-sign'></i> 删除</button>&nbsp;&nbsp;"+
							  "<button class='bindBtn btn btn-xs btn-default'>绑定号码段</button>",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function(){
					dataGridObj.addClick();
					dataGridObj.editClick();
					dataGridObj.deleteClick();
					$(".bindBtn").on("click", function(){
						dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
						$.ajax({
		            		url : "findEquipmentNumberSectionListByAreaAreaCodeEquals?areaCode="+dataItem.areaCode,
		            		type : "GET",
		            		success : function(data) {
		            			dataIds = [];
		            			$.each(data,function(index,item){
		            				dataIds[dataIds.length] = item.id;
		            			});  
		            		}
		            	});      
						$("#inputTypeTrigger").kendoDropDownList({ width : "200px"});
						bindWindowObj.center().open();
						windowGridObj.init();
					});
				}
			}).data("kendoGrid");
		},
		
		//添加按钮，显示弹窗
		addClick: function() {
			
			$("#addBtn").on("click", function() {
				
				dataItem = {
					id:"",
					areaCode: "",
					areaName: ""
				};
                
                infoWindow.obj.setOptions({"title":"添加"});
                
                infoWindow.initContent(dataItem);
			});
		},
		
		//编辑
		editClick: function() {
			$(".editBtn").on("click", function() {
				
                dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
                
                infoWindow.obj.setOptions({"title":"修改"});
                
                infoWindow.initContent(dataItem);
			});
		},
	
		//删除
		deleteClick: function() {
			$(".deleteBtn").on("click", function() {
				if(confirm("确定删除吗？")){
					dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));					
					$.ajax({
						url : "rest/system-area/"+dataItem.id,
	            		type : "delete",
	            		dataType: "json",
	                    contentType: "application/json;charset=UTF-8",
        				success:function(data){
	    				    infoTip({content: "删除成功！"});
        					dataSource.read(searchparams);
        				}
        			});
				}
			});
		}
	};
	
	
	$('#search-field-numberSection').on('keyup',function (event){
		var filters = [];
    	if($("#search-field-numberSection").val() != ""){
    		filters.push({field: "numberAndImsi", operator: "contains", value: $("#search-field-numberSection").val()});
    	}
    	if($("#inputTypeTrigger").val() == "1"){
    		filters.push({field: "areaName", operator: "neq", value: dataItem.areaName });
    	}else if($("#inputTypeTrigger").val() == "2"){
    		filters.push({field: "areaName", operator: "eq", value: dataItem.areaName });
    	}
    	windowGridObj.dataGrid.dataSource.filter(filters);
    	windowGridObj.dataGrid.dataSource.fetch();
    });
	
	$('#inputTypeTrigger').on('change',function (event){
		var filters = [];
    	if($("#search-field-numberSection").val() != ""){
    		filters.push({field: "numberAndImsi", operator: "contains", value: $("#search-field-numberSection").val()});
    	}
    	if($("#inputTypeTrigger").val() == "1"){
    		filters.push({field: "areaName", operator: "neq", value: dataItem.areaName });
    	}else if($("#inputTypeTrigger").val() == "2"){
    		filters.push({field: "areaName", operator: "eq", value: dataItem.areaName });
    	}
    	windowGridObj.dataGrid.dataSource.filter(filters);
    	windowGridObj.dataGrid.dataSource.fetch();
    });
	
	
	//编辑、修改弹窗
	var infoWindow = {
		
		obj: undefined,
		
		template: undefined,
		
		id: $("#windowTemplate"),
		
		//保存 【添加】/
		saveClick: function(){
			$("#saveBtn").on("click",function(){
				var dto = {};
				dto.areaCode = $("#page-areaCode").val();
				dto.areaName = $("#page-areaName").val();
				if(dto.areaCode == "" ){
					infoTip({content: "请输入地区编码！",color:"#D58512"});
					return;
				}
				if(dto.areaName == "" ){
					infoTip({content: "请输入地区名称！",color:"#D58512"});
					return;
				}
				if(dataItem.id == ""){//添加
					$.each(allArray,function(index,item){
						if(item.areaCode == dto.areaCode){
							infoTip({content: "地区编码【" + dto.areaCode + "】已经存在！",color:"#D58512"});
							return false;
						}
						if(item.areaName == dto.areaName){
							infoTip({content: "地区名称【" + dto.areaName + "】已经存在！",color:"#D58512"});
							return false;
						}
						if(index == (allArray.length -1)){
							$.ajax({
			            		url : "rest/system-area/",
			            		type : "post",
			            		data: kendo.stringify(dto),
			            		dataType: "json",
			                    contentType: "application/json;charset=UTF-8",
			            		success : function(data) {
			            			$.ajax({
			    	            		url : "resource/"+data.entityId,
			    	            		type : "POST",
			    	            		dataType: "json",
			    	                    contentType: "application/json;charset=UTF-8",
			    	            		success : function(data1) {
			    	            			infoWindow.obj.close();
			    	        				infoTip({content: "保存成功！",color:"#D58512"});
			    	        				dataSource.read(searchparams);
			    	            		}
			    	            	});
			            			
			            		},error : function(data) {
			            			infoTip({content: "保存失败！地区编码或者地区名称不能重复",color:"#D58512"});
			            		}
							});
						}
					})
				}else{
					$.each(allArray,function(index,item){
						if(item.entityId !=  dataItem.entityId ){
							if(item.areaCode == dto.areaCode){
								infoTip({content: "地区编码【" + dto.areaCode + "】已经存在！",color:"#D58512"});
								return false;
							}
							if(item.areaName == dto.areaName){
								infoTip({content: "地区名称【" + dto.areaName + "】已经存在！",color:"#D58512"});
								return false;
							}
						}
						if(index == (allArray.length -1)){
							$.ajax({
			            		url : "rest/system-area/"+dataItem.entityId,
			            		type : "PATCH",
			            		data: kendo.stringify(dto),
			            		dataType: "json",
			                    contentType: "application/json;charset=UTF-8",
			            		success : function(data) {
			            			$.ajax({
					            		url : "resource/"+dataItem.entityId,
					            		type : "GET",
					            		dataType: "json",
					                    contentType: "application/json;charset=UTF-8"
									});
			            			infoWindow.obj.close();
			        				infoTip({content: "保存成功！",color:"#D58512"});
			        				dataSource.read(searchparams);
			            		},error : function(data) {
			            			infoTip({content: "保存失败！地区编码或者地区名称不能重复",color:"#D58512"});
			            		}
							});
						}
					})
					
				}
			});
		},
		
		//取消
		cancelClick: function(){
			$("#cancelBtn").on("click",function(){
				infoWindow.obj.close();
			});
		},
		
		initContent: function(dataItem){
			
			//填充弹窗内容
			infoWindow.obj.content(infoWindow.template(dataItem));
			
			//弹窗内，保存/取消按钮
			infoWindow.saveClick();
			infoWindow.cancelClick();
			
			infoWindow.obj.center().open();
		},
		
		init: function(){
			
			this.template = kendo.template($("#windowTemplate").html());
			
			if (!infoWindow.id.data("kendoWindow")) {
				infoWindow.id.kendoWindow({
					width: "700px",
					actions: ["Close"],
					modal:true,
					title: "号段管理"
				});
			}
			infoWindow.obj = infoWindow.id.data("kendoWindow");
		}
	};
	
	
	//绑定号码段弹窗内的表格
	var windowGridObj = {		
		init: function(){			
			this.dataGrid = $("#bindGrid").kendoGrid({			
				dataSource: {
					 
					 transport: {
						 read: {
								type: "GET",
								url: "rest/equipment-number-section?date="+new Date().getTime(),
								dataType: "json",
								contentType: "application/json;charset=UTF-8"
							},
						parameterMap: function (options, operation) {
		                    if (operation == "read") {
		                    	var type =$("#inputTypeTrigger").val();
		                    	var keyWord = $("#search-field-numberSection").val();
		                    	var parameter = {};
		                    	parameter.ne_id = dataItem.id;
		                        return parameter;
		                    }
		                }
					 },
					 schema: {
				            data: function(d) { 
				            	allIds = [];
				            	var sectionList = new Array();
				            	if(d._embedded){
					            	$.each(d._embedded["equipment-number-section"],function(i,dItem){
					            		var inarray = $.inArray(dItem.numberId,dataIds);
					            		var sectionItem = dItem;
					            		if(inarray == -1){
					            			sectionItem.inUse = false;
					            		}else{
					            			sectionItem.inUse = true;
					            		}
					            		sectionItem.rowid = i; 
					            		allIds[allIds.length] = dItem._links.self.href;
					            		sectionList.push(sectionItem);
					            	});
				            	}
				            	return sectionList; //响应到页面的数据
				            }
					 },
					 serverPaging: false,
				     serverSorting: false
		        },				
				height: 300,				
				reorderable: true,		
				resizable: true,		
				sortable: true,
				columnMenu: true,		
				pageable: false,
				
				columns: [{
					field: "number#",
					template: "<span  title='#:isNotEmpty(number)#'>#:isNotEmpty(number)#</span>",
					title: "<span  title='MSISDN号段'>MSISDN号段</span>"
				}, {
					field: "imsi",
					template: "<span  title='#:isNotEmpty(imsi)#'>#:isNotEmpty(imsi)#</span>",
					title: "<span  title='IMSI号段'>IMSI号段</span>"
				} ,{
					template: "#if(inUse){# <button added='true' class='addOrRemoveBtn btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button> #}else{#" + 
							  " <button added='false' class='addOrRemoveBtn btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button> #}#",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function(){
					//每一行的加入/移除按钮
					$("#bindGrid td").delegate(".addOrRemoveBtn","click",function(){
						var dataItem1 = windowGridObj.dataGrid.dataItem($(this).closest("tr"));
						
						var td=$(this).closest("td");
						var html="";
							var flag = $(this).attr("added");
								$.ajax({
		    	            		url : "rest/system-area/"+dataItem.entityId,
		    	            		type : "GET",
		    	            		dataType: "json",
		    	                    contentType: "application/json;charset=UTF-8",
		    	            		success : function(data1) {
		    	            			$.ajax({
		    			            		url : dataItem1._links.self.href,
		    			            		type : "PATCH",
		    			            		data: kendo.stringify({"area" : flag == "true" ? null : data1._links.self.href }),
		    			            		dataType: "json",
		    			                    contentType: "application/json;charset=UTF-8",
		    			            		success : function(data) {
		    			            					infoWindow.obj.close();
		    			            					if(flag=="true"){
		    												html = "<button added='false' class='addOrRemoveBtn btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";									
		    												infoTip({content: "移除成功！"});
		    												$.each(dataIds,function(index,dataItemId){
		    													if(dataItemId == dataItem1.numberId){
		    														dataIds[index] = "";
		    													}
		    												})
		    			    	        				}else{
		    				    	        				html="<button added='true' class='addOrRemoveBtn btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
		    				    	        				infoTip({content: "加入成功！",color:"#D58512"});
		    				    	        				dataIds[dataIds.length] = dataItem1.numberId;
		    			    	        				}
		    											windowGridObj.dataGrid.dataSource.read();
		    			            		}
		    							});
		    	            		},
		    	            		fail : function(data) {
		    	            			showNotify(data.message,"error");
		    	            		}
		    	            	});
								dataItem1.area = dataItem.id;
					});
				}
			}).data("kendoGrid");
		}
	};
	
	
//	//绑定号码段弹窗
	if (!$("#bindWindow").data("kendoWindow")) {
		
		$("#bindWindow").kendoWindow({
			width: "900px",
			height:"400px",
			actions: ["Close"],
			modal: true,
			title: "绑定号码段"
		});
	}
	var bindWindowObj = $("#bindWindow").data("kendoWindow");
	
	
	//全部加入
	$("#addAll").on("click",function(){
		var dataView = windowGridObj.dataGrid.dataSource.view();
		$.ajax({
    		url : "rest/system-area/"+dataItem.entityId,
    		type : "GET",
    		dataType: "json",
            contentType: "application/json;charset=UTF-8",
    		success : function(data1) {
    			var href = data1._links.self.href;
    			href = href.substring(0,href.lastIndexOf("/")+1);
    			$.each(dataView,function(index,item){
    				$.ajax({
    					url : item._links.self.href,
    					type : "PATCH",
    					data : kendo.stringify({"area":  href+dataItem.entityId }),
    					contentType: "application/json;charset=UTF-8",
    					success:function(data){
    						var html = "<button added='true' class='addOrRemoveBtn btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
    						$(".addOrRemoveBtn[added='false']").parent().html(html);
    						dataIds[dataIds.length] = item.numberId;
    						if(dataView.length -1 == index){
    							windowGridObj.dataGrid.dataSource.read();
    							infoTip({content: "全部加入成功！",color:"#D58512"});
    						}
    					}
    				});
    			})
    		},
    		fail : function(data) {
    			showNotify(data.message,"error");
    		}
    	});
		
	});
	//全部移除
	$("#removeAll").on("click",function(){
		var dataView = windowGridObj.dataGrid.dataSource.view();
		$.each(dataView,function(index,item){
			$.ajax({
				url : item._links.self.href,
				type : "PATCH",
				data : kendo.stringify({"area":  null}),
				contentType: "application/json;charset=UTF-8",
				success:function(data){
					var html="<button added='false' class='addOrRemoveBtn btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
					$(".addOrRemoveBtn[added='true']").parent().html(html);
					
					dataIds.splice($.inArray(item.numberId,dataIds),1);

					if(dataView.length -1 == index){
						windowGridObj.dataGrid.dataSource.read();
						infoTip({content: "全部移除成功！"});
					}

				}
			});
		})
		
		
	});
	
	
	$('#clearsearch').on('click', function (event) {
        $("#inputKeyWord").val(""); 
        var filters = [];
        dataSource.filter(filters);
		dataSource.fetch();
	});
	$('#inputKeyWord').on('keyup',function (event){
		var filters = [];
    	if($("#inputKeyWord").val() != "")
        	filters.push({field: "areaName", operator: "contains", value: $("#inputKeyWord").val()});
		dataSource.filter(filters);
		dataSource.fetch();
	});		
//	
//	$('#windowsearch').on('click', function (event) {
//        
//    	
//        var type =$("#inputselectNsType").val();
//    	var keyWord = $("#area-search-field-numberSection").val();
//    	var parameter = {};
//    	parameter.area_id = dataItem.id;
//    	parameter.type = type;
//    	parameter.keyWord= keyWord;
//    	 windowGridObj.dataGrid.dataSource.read(parameter);
//	});
	
	$('#windowclearsearch').on('click', function (event) {
		var filters = [];
    	$("#search-field-numberSection").val("");
    		
    	$('#inputTypeTrigger').data("kendoDropDownList").text("全部号码段");
    	windowGridObj.dataGrid.dataSource.filter(filters);
    	windowGridObj.dataGrid.dataSource.fetch();
	});
	
	dataGridObj.init();
	
	infoWindow.init();
	
});

