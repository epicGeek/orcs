var searchparams = {
			page:1,
			size:10,
			searchField:''
};	

var numbers = [];

function compare(propertyName) {
	return function (object1, object2) {
		var value1 = object1[propertyName];
		var value2 = object2[propertyName];
		if (value2 < value1) {
			return -1;
		}else if (value2 > value1) {
			return 1;
		}else {
			return 0;
		}
	}
}

function ConditionFilter(){
	
	var datas = searchField(dataItem.id,$('#inputTypeTrigger').val());
	var dataSource = new kendo.data.DataSource({
		data: datas
	});
	windowGridObj.dataGrid.setDataSource(dataSource);
}

function searchField(neid,type){
	
	var Items = [];
	$.ajax({
		url : "rest/equipment-ne/"+neid+"/numberSection",
		type : "GET",
		async:false,
		success : function(data) {
			var checkItemIds = [];
			if(data._embedded && data._embedded["equipment-number-section"] ){
				$.each(data._embedded["equipment-number-section"],function(index,item){
					checkItemIds[index] = item.numberId;
				})
			}
			$.each(numbers,function(index,item){
				if(type==0){
					if($.inArray(item.numberId,checkItemIds) != -1){
						item.inUse = true;
					}
					Items[index] = item;//所有的
				}else if(type==1){
					if($.inArray(item.numberId,checkItemIds) != -1){
					}else{
							Items.push(item);//未绑定的
					}
				}else if(type==2){
					if($.inArray(item.numberId,checkItemIds) != -1){
							item.inUse = true;
							Items.push(item);//绑定的
					}
				}
			})
        	
        	
		}
	});
	return Items;
}

$(function() {
	var dataItem = {};
	/*当前导航*/
	$("#topNavList .navListWrap:eq(3) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(3) ul li:eq(2)").addClass("active");
	$("#navList a[href='equipment-ne']").addClass("active");
	var dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: "rest/equipment-ne/search/list",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            }/*,
            parameterMap: function (options, operation) {
                if (operation == "read") {
                	searchparams.page = options.page-1;
                    searchparams.size = options.pageSize;
                    searchparams.searchField = $("#inputKeyWord").val();
                    return searchparams;                   
                }
            }*/
        },
        schema: {
            data: function(d) {        	
            	return d.sort(compare("id")); //响应到页面的数据
            }
        },
        batch: true,
        pageSize: 20,
        serverPaging : false,
		serverFiltering : false,
		serverSorting : false
    });
	var dataGridObj = {		
		init: function(){			
			this.dataGrid = $("#dataGrid").kendoGrid({				
				dataSource: dataSource,				
				height: $(window).height()-$("#dataGrid").offset().top - 50,				
				reorderable: true,		
				resizable: true,		
				sortable: true,		
				columnMenu: true,		
				pageable: true,		
				columns: [{ 
					field: "id", 
					title:"_links.self.href", 
					hidden:true
				},{ 
					field: "dhssName",
					template: "<span  title='#:dhssName#'>#:dhssName#</span>",
					title: "<span  title='上级网元'>上级网元</span>"
				}, {
					field: "neName",
					template: "<span  title='#:neName#'>#:neName#</span>",
					title: "<span  title='网元名称'>网元名称</span>"
				}, {
					field: "neTypeId",
					template: "<span  title='#:neType#'>#:neType#</span>",
					title: "<span  title='网元类型'>网元类型</span>"
				}, {
					field: "location",
					template: "<span  title='#:location#'>#:location#</span>",
					title: "<span  title='物理地址'>物理地址</span>"
				}, {
					width: 210,
					template: "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 编辑</button>&nbsp;&nbsp;"+
							  "<button class='deleteBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon-remove-sign'></i> 删除</button>&nbsp;&nbsp;"+
							  "#if(neType=='ONE_NDS'){# <button class='bindBtn btn btn-xs btn-default'>绑定号码段</button> #}#",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function(){
					dataGridObj.addClick();
					dataGridObj.editClick();
					dataGridObj.deleteClick();					
					//绑定号码段弹窗
					$(".bindBtn").on("click", function(){
						dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
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
					dhssName: "",
					neName: "",
					neType: {id:"",neTypeName:""},
					location: ""
				};
                infoWindow.obj.setOptions({"title":"添加"});
                infoWindow.initContent(dataItem);
                
                $.ajax({
            		url : "equipment-neType/search/list",
            		type : "GET",
            		success : function(data) {
            			var html = "";
            			$.each(data,function(index,item){
            				html += '<option value="' + item + '">' + item + '</option>';
            			});            			
            			$('#netypeInput').html(html);
            			$('#netypeInput').kendoDropDownList();
            		},
            		error : function(data) {
            			showNotify(data.message,"error");
            		}
            	}); 
                
                $("#typeInput option:eq(0)").attr("selected", "selected");
			});
		},
		
		//编辑
		editClick: function() {
			$(".editBtn").on("click", function() {				
                dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));                
                infoWindow.obj.setOptions({"title":"修改"});                
                infoWindow.initContent(dataItem);
                $.ajax({
            		url : "equipment-neType/search/list",
            		type : "GET",
            		success : function(data) {
            			var html = "";
            			$.each(data,function(index,item){
            				if(item===dataItem.neType){
                				html += '<option value="' + item + '" selected>' + item + '</option>';
            				}else{
                				html += '<option value="' + item + '">' + item + '</option>';
            				}
            			});            			
            			$('#netypeInput').html(html);
            			$('#netypeInput').kendoDropDownList();
            		},
            		error : function(data) {
            		}
            	});              
			});
		},
	
		//删除
		deleteClick: function() {
			$(".deleteBtn").on("click", function() {
				if(confirm("确定删除吗？")){
					var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
					dataGridObj.dataGrid.dataSource.remove(dataItem);
				    $.ajax({
						url : "rest/equipment-ne/"+dataItem.id,
	            		type : "delete",
	            		dataType: "json",
	                    contentType: "application/json;charset=UTF-8",
        				success:function(data){
	    				    infoTip({content: "删除成功！"});
        					dataSource.read(searchparams);
        				},
				        fail : function(data) {
	            			showNotify(data.message,"error");
	            		}
        			});
				}
			});
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
								url: "rest/equipment-number-section",
								//url: "rest/equipment-number-section/search/findEquipmentNumberSectionListByNeIdEquals",
								dataType: "json",
								contentType: "application/json;charset=UTF-8"
							}/*,
						     parameterMap: function (options, operation) {
							 if (operation == "read") {
				                    searchparams.page = options.page-1;
				                    searchparams.size = options.pageSize;
				                    searchparams.searchField = $("#search-field-numberSection").val();
				                    return searchparams;                   
				                }  
							 
		                }*/
					 },
					 schema: {
				            data: function(d) { 
				            	if(d._embedded["equipment-number-section"]){
				            		numbers = d._embedded["equipment-number-section"];
				            		return searchField(dataItem.id,$('#inputTypeTrigger').val());
				            	}/*else{
				            		return new Array();
				            	}*/
				            }
					 }
		        },				
				height: 300,				
				reorderable: true,		
				resizable: true,		
				sortable: true,
				columnMenu: true,		
				pageable: false,
				
				columns: [{
					field: "number#",
					template: "<span  title='#:number#'>#:number#</span>",
					title: "<span  title='MSISDN号段'>MSISDN号段</span>"
				}, {
					field: "imsi",
					template: "<span  title='#:imsi#'>#:imsi#</span>",
					title: "<span  title='IMSI号段'>IMSI号段</span>"
				}, {
					template: "#if(inUse){# <button added='true' class='addOrRemoveBtn btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button> #}else{#" + 
							  " <button added='false' class='addOrRemoveBtn btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button> #}#",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function(){
					//每一行的加入/移除按钮
					$("#bindGrid td").delegate(".addOrRemoveBtn","click",function(){
						var dataItem1 = windowGridObj.dataGrid.dataItem($(this).closest("tr"));
						var td=$(this).closest("td");
						console.log(dataItem1);
						var html="";
						
						if($(this).attr("added")=="true"){
							$.ajax({
								url : "rest/equipment-ne/" + dataItem.id+"/numberSection/"+dataItem1.numberId,
								type : "DELETE",
								success : function(data) {
									html = "<button added='false' class='addOrRemoveBtn btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";									
									td.html(html);								
								},
								fail : function(data) {
									showNotify(data.message,"error");
								}
							});
						
						} else {
							$.ajax({
								url : "rest/equipment-ne/" + dataItem.id+"/numberSection",
								contentType: "text/uri-list",
								data:"numberSection/"+dataItem1.numberId,
								type : "PATCH",
								success : function(data) {
									html="<button added='true' class='addOrRemoveBtn btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
									td.html(html);
								},
								fail : function(data) {
									showNotify(data.message,"error");
								}
							});
							
							
						}
						
						
					});
				}
			}).data("kendoGrid");
		}
	};
	
	//编辑、修改弹窗
	var infoWindow = {		
		obj: undefined,
		template: undefined,		
		id: $("#infoWindow"),		
		//保存 【添加】/
		saveClick: function(){
			$("#saveBtn").on("click",function(){
				var dto = {};
				dto.dhssName = $("#page-dhssName").val();
				dto.neName = $("#page-neName").val();
				dto.location =  $("#page-location").val();
				dto.neType = $("#netypeInput").val();
				if(dto.dhssName == ""){
					infoTip({content: '请输入上级网元名称！',color:"#D58512"});
					return;
				}
				if(dto.neName == ""){
					infoTip({content: '请输入网元名称！',color:"#D58512"});
					return;
				}
				if(dto.location == ""){
					infoTip({content: '请输入物理地址！',color:"#D58512"});
					return;
				}
                if(dataItem.id == ""){//添加
                	$.ajax({
	            		url : "rest/equipment-ne/",
	            		type : "POST",
	            		data: kendo.stringify(dto),
	            		dataType: "json",
	                    contentType: "application/json;charset=UTF-8",
	            		success : function(data) {

							infoWindow.obj.close();
			                dataSource.read(searchparams);
						    infoTip({content: '添加成功！',color:"#D58512"});
	            		},
	            		error : function(data) {
						    infoTip({content: '网元名称不能重复！',color:"#D58512"});
	            		}
	            	});
				}else{
					$.ajax({
	            		url : "rest/equipment-ne/"+dataItem.id,
	            		type : "PATCH",
	            		data: kendo.stringify(dto),
	            		dataType: "json",
	                    contentType: "application/json;charset=UTF-8",
	            		success : function(data) {

							infoWindow.obj.close();
			                dataSource.read(searchparams);
						    infoTip({content: '保存成功！',color:"#D58512"});
	            		},
	            		error : function(data) {
						    infoTip({content: '保存失败！',color:"#FF8512"});
	            		}
	            	});
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
					title: "网元管理"
				});
			}
			infoWindow.obj = infoWindow.id.data("kendoWindow");
		}
	};
	
	//绑定号码段弹窗
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
		var checkedId = "";
		var data = windowGridObj.dataGrid.dataSource._data;
		for(var i = 0; i < data.length; i++){
			checkedId += "checkItem/"+data[i].numberId+"\n";
		}
		$.ajax({
			url : "rest/equipment-ne/"+dataItem.id+"/numberSection",
			type : "PATCH",
			data : checkedId,
			contentType: "text/uri-list",
			success:function(data){
				var html = "<button added='true' class='addOrRemoveBtn btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
				$(".addOrRemoveBtn[added='false']").parent().html(html);
			}
		});
	});
	//全部移除
	$("#removeAll").on("click",function(){
		var data = windowGridObj.dataGrid.dataSource._data;
		for(var i = 0; i < data.length; i++){
			$.ajax({
				url : "rest/equipment-ne/" + dataItem.id+"/numberSection/"+data[i].numberId,
				type : "DELETE",
				success : function(data) {
					var html="<button added='false' class='addOrRemoveBtn btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
					$(".addOrRemoveBtn[added='true']").parent().html(html);								
				},
				fail : function(data) {
					showNotify(data.message,"error");
				}
			});
		}
		
		
	});
	//网元条件过滤
	$('#inputKeyWord').on('keyup',function (event){
		var filters = [];
    	if($("#inputKeyWord").val() != "")
        	filters.push({field: "searchField", operator: "contains", value: $("#inputKeyWord").val()});
    	dataGridObj.dataGrid.dataSource.filter(filters);
    	dataGridObj.dataGrid.dataSource.fetch();
	});	
	
	$('#neClear').on('click',function(e){
		$("#inputKeyWord").val("");
		dataGridObj.dataGrid.dataSource.filter([]);
    	dataGridObj.dataGrid.dataSource.fetch();
	});
	
	//号段过滤条件
	
	$('#search-field-numberSection').on('keyup',function (event){
		var filters = [];
    	if($("#search-field-numberSection").val() != ""){
    		filters.push({field: "numberAndImsi", operator: "contains", value: $("#search-field-numberSection").val()});
    	}
    	windowGridObj.dataGrid.dataSource.filter(filters);
    	windowGridObj.dataGrid.dataSource.fetch();
    });
	
	$('#inputTypeTrigger').on('change',function (event){
		
		var datas = searchField(dataItem.id,$('#inputTypeTrigger').val());
		var dataSource = new kendo.data.DataSource({
			data: datas
		});
    	windowGridObj.dataGrid.setDataSource(dataSource);
    });
	
	//重置
	$('#windowclearsearch').on('click', function (event) {
		var filters = [];
    	$("#search-field-numberSection").val("");
    	   $("#inputTypeTrigger").val(0);
    	windowGridObj.dataGrid.dataSource.filter(filters);
    	var datas = searchField(dataItem.id,$('#inputTypeTrigger').val());
		var dataSource = new kendo.data.DataSource({
			data: datas
		});
    	windowGridObj.dataGrid.setDataSource(dataSource);
    	
    	
	});
	
	dataGridObj.init();
	
	infoWindow.init();
	
});
