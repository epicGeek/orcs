function isNotEmpty(value){
	if(value == null || value == "null" || value == ""){
		return "";
	}else{
		return value;
	}
}
var menus = [];
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
$(function(){
	var dataSource=new kendo.data.DataSource({
        type: "odata",
        transport: {
            read: {
                type: "GET",
                url: "system-menu/search/all",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            }
        },
        schema: {
        	data: function(d) { 
        		menus = d;
            	return menus.sort(compare("id"));
            },
            total: function(d) {
            	return d.length;
            }
        },
        pageSize: 20,
        serverPaging: false,
        serverSorting: false
    });
	
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
						field: "menuCode",
						template: "<span  title='#:isNotEmpty(menuCode)#'>#:isNotEmpty(menuCode)#</span>",
						title: "<span  title='地区编码'>菜单编码</span>"
					}, {
						field: "menuName",
						template: "<span  title='#:isNotEmpty(menuName)#'>#:isNotEmpty(menuName)#</span>",
						title: "<span  title='地区名称'>菜单名称</span>"
					}, {
						field: "parentMenuName",
						template: "<span  title='#:isNotEmpty(parentMenuName)#'>#:isNotEmpty(parentMenuName)#</span>",
						title: "<span  title='地区名称'>上级菜单名称</span>"
					}, {
						template: "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 编辑</button>&nbsp;&nbsp;"+
								  "<button class='deleteBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon-remove-sign'></i> 删除</button>&nbsp;&nbsp;",
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
						menuCode: "",
						menuName: "",
						parentMenuName : ""
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
							url : "rest/system-menu/"+dataItem.id,
		            		type : "delete",
		            		dataType: "json",
		                    contentType: "application/json;charset=UTF-8",
	        				success:function(data){
		    				    infoTip({content: "删除成功！"});
	        					dataSource.read();
	        				}
	        			});
					}
				});
			}
		};
	
	
	
	
	//编辑、修改弹窗
	var infoWindow = {
		
		obj: undefined,
		
		template: undefined,
		
		id: $("#windowTemplate"),
		
		//保存 【添加】/
		saveClick: function(){
			$("#saveBtn").on("click",function(){
				var dto = {};
				dto.menuCode = $("#page-menuCode").val();
				dto.menuName = $("#page-menuName").val();
				dto.parentMenuName = $("#page-parentMenuName").val();
				if(dto.menuCode == ""){
					infoTip({content: "请输入菜单编码！",color:"#D58512"});
					return;
				}
				if(dto.menuName == ""){
					infoTip({content: "请输入菜单名称！",color:"#D58512"});
					return;
				}
				if(dto.parentMenuName == ""){
					infoTip({content: "请输入上级菜单名称！",color:"#D58512"});
					return;
				}
				if(dataItem.id == ""){//添加
					$.each(menus,function(index,item){
						if(item.menuCode == dto.menuCode){
							infoTip({content: "菜单编码【" + dto.menuCode + "】已经存在！",color:"#D58512"});
							return false;
						}
						if(item.menuName == dto.menuName){
							infoTip({content: "菜单名称【" + dto.menuName + "】已经存在！",color:"#D58512"});
							return false;
						}
						if(index == (menus.length - 1) ){
							$.ajax({
	    	            		url : "rest/system-menu",
	    	            		type : "POST",
	    	            		data: kendo.stringify(dto),
	    	            		dataType: "json",
	    	                    contentType: "application/json;charset=UTF-8",
	    	            		success : function(data1) {
	    	            			$.ajax({
	    	    	            		url : "resource/"+data1.entityId,
	    	    	            		type : "POST",
	    	    	            		dataType: "json",
	    	    	                    contentType: "application/json;charset=UTF-8",
	    	    	            		success : function(data2) {
	    	    	            			infoWindow.obj.close();
	    	    	        				infoTip({content: "保存成功！",color:"#D58512"});
	    	    	        				dataSource.read();
	    	    	            		}
	    	    	            	});
	    	            		}
	    	            	});
						}
					})
				}else{
					$.each(menus,function(index,item){
						if(item.id != dataItem.id){
							if(item.menuCode == dto.menuCode){
								infoTip({content: "菜单编码【" + dto.menuCode + "】已经存在！",color:"#D58512"});
								return false;
							}
							if(item.menuName == dto.menuName){
								infoTip({content: "菜单名称【" + dto.menuName + "】已经存在！",color:"#D58512"});
								return false;
							}
						}
						if(index == (menus.length - 1) ){
							$.ajax({
			            		url : "rest/system-menu/"+dataItem.id,
			            		type : "PATCH",
			            		data: kendo.stringify(dto),
			            		dataType: "json",
			                    contentType: "application/json;charset=UTF-8",
			            		success : function(data) {
			            			infoWindow.obj.close();
			        				infoTip({content: "保存成功！",color:"#D58512"});
			        				dataSource.read();
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
	
	$('#inputKeyWord').on('keyup',function (event){
		var filters = [];
    	if($("#inputKeyWord").val() != "")
        	filters.push({field: "menuName", operator: "contains", value: $("#inputKeyWord").val()});
		dataSource.filter(filters);
		dataSource.fetch();
	});		
	$('#clearsearch').on('click', function (event) {
        $("#inputKeyWord").val(""); 
        var filters = [];
        dataSource.filter(filters);
		dataSource.fetch();
	});
	
	
	dataGridObj.init();
	
	infoWindow.init();
})