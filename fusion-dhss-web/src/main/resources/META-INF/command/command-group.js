var level = ["高级命令","维护命令","查看命令"];
$(function() {
	/*当前导航*/
//	$("#navList a[href='command-group']").addClass("active");
	
	var dataItem = {};
	var dataIds = [];
	var allGroup = [];
	var allCheckItemIds = [];
	var dataSource=new kendo.data.DataSource({
        type: "odata",
        transport: {
        	read: {
                type: "GET",
                url: "rest/command-group",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            },
        },
        schema: {
            data: function(d) { 
            	 if(d._embedded){
            		 $.each(d._embedded["command-group"],function(index,item){                		
            			 allGroup.push(item);
                 	});
            	 }
            	 return allGroup; //响应到页面的数据
            },
            
            total: function(d) {
                return d._embedded["command-group"].length; 
            }
        },
        pageSize: 15,
        serverPaging: false,
        serverSorting: false
    });
	
	var dataGridObj = {
		init: function(data){
			this.dataGrid = $("#dataGrid").kendoGrid({				
				dataSource: data,				
				height: $(window).height()-$("#dataGrid").offset().top - 50,				
				reorderable: true,		
				resizable: true,		
				sortable: true,		
				columnMenu: true,		
				pageable: true,		
				columns: [{ 
					field: "commandGroupName",
					template: "<span  title='#:commandGroupName#'>#:commandGroupName#</span>",
					title: "<span  title='指令组名称'>指令组名称</span>"
				}, {
					field: "commandGroupDesc",
					template: "<span  title='#:commandGroupDesc#'>#:commandGroupDesc#</span>",
					title: "<span  title='指令组描述'>指令组描述</span>"
				}, {
					field: "neType",
					template: "<span  title='#=neType#'>#=neType#</span>",
					title: "<span  title='网元类型'>网元类型</span>"
				}, {
					field: "unitType",
					template: "<span  title='#=unitType#'>#=unitType#</span>",
					title: "<span  title='单元类型'>单元类型</span>"
				}, {
					field: "level",
					template: "<span  title='#:level#'>#:level#</span>",
					title: "<span  title='指令组级别'>指令组级别</span>"
				}, {
					field: "remark",
					template: "#if(remark!=null&&remark!='null'){#<span  title='#:remark#'>#:remark#</span>#}#",
					title: "<span  title='备注'>备注</span>"
				}, {
					width: 200,
					template: "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 编辑</button>&nbsp;&nbsp;"+
							  "<button class='deleteBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon-remove-sign'></i> 删除</button>&nbsp;&nbsp;"+
							  "<button class='bindBtn btn btn-xs btn-default'>选择指令</button>",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function(){
					dataGridObj.addClick();
					dataGridObj.editClick();
					dataGridObj.deleteClick();
					
					//绑定号码段弹窗
					$(".bindBtn").on("click", function(){
						dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
						dataIds = [];
		            	$.ajax({
		    				url: dataItem._links.checkItem.href,
		    				dataType:"json",
		    				success:function(data){
		    					if(data._embedded){
		    						$.each(data._embedded["check-item"],function(index,item){
		    							dataIds[dataIds.length] = item.itemId;
		    						})
		    					}
		    				}
		    			});
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
					commandGroupName:"",
					commandGroupDesc:"", 
					neType:"", 
					unitType:"", 
					level:"", 
					remark:""
				};
                infoWindow.obj.setOptions({"title":"添加"});
                
                infoWindow.initContent(dataItem);
                
                $("#unitTypeInput").kendoDropDownList();
    			$.ajax({
    				url:"equipment-unitType/search/list",
    				dataType:"json",
    				success:function(data){
    					$("#unitTypeInput").kendoDropDownList({
    						dataSource: data,
    						filter: "contains",
    						suggest: true
    					});
    				}
    			});
    			
    			$("#typeInput").kendoDropDownList();
    			$.ajax({
    				url:"equipment-neType/search/list",
    				dataType:"json",
    				success:function(data){
    					$("#typeInput").kendoDropDownList({
    						dataSource: data,
    						filter: "contains",
    						suggest: true
    					});
    				}
    			});
    			
    			$("#levelInput").kendoDropDownList();
    			$.ajax({
    				url:"equipment-neType/search/list",
    				dataType:"json",
    				success:function(data){
    					$("#levelInput").kendoDropDownList({
    						dataSource: level,
    						filter: "contains",
    						suggest: true
    					});
    				}
    			});
                
//                var neTypeMap = {};
//                $.ajax({
//                	dataType : 'json',
//            		url: "rest/equipment-neType",
//            		type : "GET",
//            		success : function(data) {
//            			var html = "";
//            			if(data._embedded){
//            				$.each(data._embedded["equipment-neType"],function(index,item){ 
//            				   html += '<option value="' + item.id + '">' + item.neTypeName + '</option>';
//            				   neTypeMap[item.id] = item._embedded["unitType"];
//            			    }); 
//            			}
//            			$('#typeInput').html(html);
//            			var i = $('#typeInput').val();
//                        var html1 = "";
//                        $.each(neTypeMap[i],function(index,item){
//            				html1 += '<option value="' + item.id + '">' + item.unitTypeName + '</option>';
//            			});  
//                        $('#unitTypeInput').html(html1);
//            		},
//            		fail : function(data) {
//            			showNotify(data.message,"error");
//            		}
//            	}); 
                
//                $('#typeInput').on("change",function(){
//                    var unitType= neTypeMap[$(this).children('option:selected').prop("value")];
//                    var html = "";
//                    $.each(unitType,function(index,item){
//        				html += '<option value="' + item.id + '">' + item.unitTypeName + '</option>';
//        			});  
//                    $('#unitTypeInput').html(html);
//                });
               
                 //网元类型
                $("#typeInput option:eq(0)").attr("selected", "selected");
                //单元类型
                $("#unitTypeInput option:eq(0)").attr("selected", "selected");
                //指令组级别
                $("#levelInput option:eq(0)").attr("selected", "selected");
			});
		},
		
		//编辑
		editClick: function() {
			$(".editBtn").on("click", function() {				
                dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
                dataItem.link = dataGridObj.dataGrid.dataItem($(this).closest("tr"))._links.self.href;
                infoWindow.obj.setOptions({"title":"修改"});
                infoWindow.initContent(dataItem);
                
                $("#unitTypeInput").kendoDropDownList();
    			$.ajax({
    				url:"equipment-unitType/search/list",
    				dataType:"json",
    				success:function(data){
    					$("#unitTypeInput").kendoDropDownList({
    						dataSource: data,
    						filter: "contains",
    						suggest: true
    					});
    					$('#unitTypeInput').data("kendoDropDownList").text(dataItem.unitType);
    				}
    			});
    			
    			$("#typeInput").kendoDropDownList();
    			$.ajax({
    				url:"equipment-neType/search/list",
    				dataType:"json",
    				success:function(data){
    					$("#typeInput").kendoDropDownList({
    						dataSource: data,
    						filter: "contains",
    						suggest: true
    					});
    					$('#typeInput').data("kendoDropDownList").text(dataItem.neType);
    				}
    			});
    			
    			$("#levelInput").kendoDropDownList();
    			$.ajax({
    				url:"equipment-neType/search/list",
    				dataType:"json",
    				success:function(data){
    					$("#levelInput").kendoDropDownList({
    						dataSource: level,
    						filter: "contains",
    						suggest: true
    					});
    					$('#levelInput').data("kendoDropDownList").text(dataItem.level);
    				}
    			});
                
                
//                var neTypeMap = {};
//                $.ajax({
//                	dataType : 'json',
//            		url: "rest/equipment-neType",
//            		type : "GET",
//            		success : function(data) {
//            			var html = "";
//            			if(data._embedded){
//            				$.each(data._embedded["equipment-neType"],function(index,item){            				
//                				html += '<option value="' + item.id + '">' + item.neTypeName + '</option>';
//                				neTypeMap[item.id] = item._embedded["unitType"];
//                			}); 
//            			}         			           			
//            			$('#typeInput').html(html);
//            			$("#typeInput option[value='"+dataItem.neType.id+"']").attr("selected", "selected");
//            			var html1 = "";
//            			var i = dataItem.neType.id;
//                        $.each(neTypeMap[i],function(index,item){
//            				html1 += '<option value="' + item.id + '">' + item.unitTypeName + '</option>';
//            			});  
//                        $('#unitTypeInput').html(html1);
//            			$("#unitTypeInput option[value='"+dataItem.unitType.id +"']").attr("selected", "selected");
//            		},
//            		fail : function(data) {
//            			showNotify(data.message,"error");
//            		}
//            	}); 
//                
//                $('#typeInput').on("change",function(){
//                    var unitType= neTypeMap[$(this).children('option:selected').prop("value")];
//                    var html = "";
//                    $.each(unitType,function(index,item){
//        				html += '<option value="' + item.id + '">' + item.unitTypeName + '</option>';
//        			});  
//                    $('#unitTypeInput').html(html);
//                });
                
                //网元类型
                $("#typeInput option[value='"+dataItem.neType.id+"']").attr("selected", "selected");
                //单元类型
                $("#unitTypeInput option[value='"+dataItem.unitType.id+"']").attr("selected", "selected");
                //指令组级别
                $("#levelInput option[value='"+dataItem.level+"']").attr("selected", "selected");
                
			});
		},
	
		//删除
		deleteClick: function() {
			$(".deleteBtn").on("click", function() {
				if(confirm("确定删除么？")){
					dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));					
					$.ajax({
						url : dataItem._links.self.href,
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
	
	var checkItemParameter = {};
	//绑定号码段弹窗内的表格
	var windowGridObj = {	
		url: undefined,	
		init: function(){			
			this.dataGrid = $("#bindGrid").kendoGrid({				
				dataSource: {
					transport: {
						 read: {
								type: "GET",
								url : "rest/check-item",
								dataType: "json",
								contentType: "application/json;charset=UTF-8"
							},
						parameterMap: function (options, operation) {
		                    if (operation == "read") {
		                    	var keyWord = $("#searchCheckItem").val();
		                    	checkItemParameter.group_id = dataItem.id;
		                    	checkItemParameter.itemName = keyWord;
		                    	checkItemParameter.moduleName = "";
		                    	checkItemParameter.cmdType = "";
		                        return checkItemParameter;
		                    }
		                }
					 },
					 schema: {
				            data: function(d) { 
				            	if(d._embedded){
				            		var sectionList = new Array();
					            	$.each(d._embedded["check-item"],function(i,dItem){
					            		if($("#searchCheckItem").val() != "" && dItem.name.indexOf($("#searchCheckItem").val()) == -1 ){
					            			return true;
					            		}
					            		var sectionItem = dItem;
					            		var id = dItem.itemId;
					            		if($.inArray(id,dataIds) == -1){
					            			sectionItem.check = false;
					            		}else{
					            			sectionItem.check = true;
					            		}
					            		allCheckItemIds[allCheckItemIds.length] = id;
					            		sectionList.push(sectionItem);
					            	});
					            	return sectionList; //响应到页面的数据
				            	}
				            	
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
					field: "name",
					template: "#if(name!=null&&name!='null'){#<span  title='#:name#'>#:name#</span>#}#",
					title: "<span  title='指令名称'>指令名称</span>"
				}, {
					field: "command",
					template: "#if(command!=null&&command!='null'){#<span  title='#:command#'>#:command#</span>#}#",
					title: "<span  title='指令内容'>指令内容</span>"
				}, {
					field: "remarks",
					template: "#if(remarks!=null&&remarks!='null'){#<span  title='#:remarks#'>#:remarks#</span>#}#",
					title: "<span  title='备注'>备注</span>"
				}, {
					width: 80,
					template: "#if(!check){# <button added='false' class='addOrRemoveBtn btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button> #}else{#" + 
							  "<button added='true' class='addOrRemoveBtn  btn btn-xs btn-danger' ><i class='glyphicon glyphicon-trash'></i> 移除</button> #}#",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function(){
					//每一行的加入/移除按钮
					$("#bindGrid td").delegate(".addOrRemoveBtn","click",function(){
						var dataItem1 = windowGridObj.dataGrid.dataItem($(this).closest("tr"));
						var td=$(this).closest("td");
						var html="";
						if($(this).attr("added")=="true"){
							$.ajax({
								url : "rest/command-group/" + dataItem.groupId +"/checkItem/"+ dataItem1.itemId,
								type : "DELETE",
								success : function(data) {
									html = "<button added='false' class='addOrRemoveBtn btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
									infoTip({content : "移除成功！" });
									td.html(html);
								},
								fail : function(data) {
									showNotify(data.message,"error");
								}
							});
							
						} else {
							$.ajax({
								url : "rest/command-group/" + dataItem.groupId+"/checkItem",
								contentType: "text/uri-list",
								data:"checkItem/"+dataItem1.itemId,
								type : "PATCH",
								success : function(data) {
									html="<button added='true' class='addOrRemoveBtn btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
									infoTip({content : "加入成功！",
							    		color : "#D58512"});
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
	
	$("#inputKeyWord").on("keyup",function(){
		
		var filters = [];
    	if($("#inputKeyWord").val() != "")
        	filters.push({field: "commandGroupName", operator: "contains", value: $("#inputKeyWord").val()});
    	dataSource.filter(filters);
    	dataSource.fetch();
		/*var tmpArray = [];
		var inputKeyWord = $("#inputKeyWord").val();
		$.each(allGroup,function(index,item){
			if(item.commandGroupName.indexOf(inputKeyWord) != -1 ){
				tmpArray[tmpArray.length] = item;
			}
		})
		var dataSources=new kendo.data.DataSource({
	        schema: {
	            data: function(d) { 
	            	 return tmpArray; //响应到页面的数据
	            },
	            total: function(d) {
	                return tmpArray.length; 
	            }
	        },
	        pageSize: 15,
	        serverPaging: false,
	        serverSorting: false
		});
		dataGridObj.init(dataSources);*/
	})
	
	$("#clearsearch").on("click",function(){
		$("#inputKeyWord").val("");
		/*var dataSources=new kendo.data.DataSource({
	        schema: {
	            data: function(d) { 
	            	 return allGroup; //响应到页面的数据
	            },
	            total: function(d) {
	                return allGroup.length; 
	            }
	        },
	        pageSize: 15,
	        serverPaging: false,
	        serverSorting: false
		});
		dataGridObj.init(dataSources);*/
		var filters = [];
    	dataSource.filter(filters);
    	dataSource.fetch();
	})
	
	$("#clearBtn").on("click",function(event){
		$("#searchCheckItem").val("");
		dataIds = [];
    	$.ajax({
			url: dataItem._links.checkItem.href,
			dataType:"json",
			success:function(data){
				if(data._embedded){
					$.each(data._embedded["check-item"],function(index,item){
						dataIds[dataIds.length] = item.itemId;
					})
				}
			}
		});
		windowGridObj.init();
	});
	$('#searchCheckItem').on('keyup',function (event){
//		var filters = [];
//    	if($("#searchCheckItem").val() != "")
//        	filters.push({field: "name", operator: "contains", value: $("#searchCheckItem").val()});
//    	dataSource.filter(filters);
//    	dataSource.fetch();
		dataIds = [];
    	$.ajax({
			url: dataItem._links.checkItem.href,
			dataType:"json",
			success:function(data){
				if(data._embedded){
					$.each(data._embedded["check-item"],function(index,item){
						dataIds[dataIds.length] = item.itemId;
					})
				}
			}
		});
		windowGridObj.init();
   });		
	
	//编辑、修改弹窗
	var infoWindow = {		
		obj: undefined,		
		template: undefined,		
		id: $("#infoWindow"),		
		//保存 【添加】/
		saveClick: function(){
			$("#saveBtn").on("click",function(){				
				var dto = {};
				dto.commandGroupName = $("#page-commandGroupName").val();
				dto.commandGroupDesc = $("#page-commandGroupDesc").val();
				dto.remark = $("#page-remark").val();
				dto.level = $("#levelInput").val();				
				dto.neType = $("#typeInput").val();
				dto.unitType = $("#unitTypeInput").val();				
				if(!dataItem.link){//添加
					$.ajax({
	            		url : "rest/command-group/",
	            		type : "post",
	            		data: kendo.stringify(dto),
	            		dataType: "json",
	                    contentType: "application/json;charset=UTF-8",
	            		success : function(data) {
//	            			$.ajax({
//	            				url : data._links.unitType.href,
//	            				type : "PUT",
//	            				data : "unitType/"+unittype_id,
//	            				contentType: "text/uri-list",
//	            				success:function(data1){
//	            					$.ajax({
//	    	            				url : data._links.NeType.href,
//	    	            				type : "PUT",
//	    	            				data : "neType/"+netype_id,
//	    	            				contentType: "text/uri-list",
//	    	            				success:function(data){
	    	    	            			infoWindow.obj.close();
	    	            					infoTip({content: "添加成功！",color:"#D58512"});
	    	            					dataSource.read(searchparams);
//	    	            				}
//	    	            			});
//	            				}
//	            			});	
	            		},
	            		fail : function(data) {
	            			showNotify(data.message,"error");
	            		}
	            	});
				}else{//修改
					$.ajax({
	            		url : dataItem.link,
	            		type : "PATCH",
	            		data: kendo.stringify(dto),
	            		dataType: "json",
	                    contentType: "application/json;charset=UTF-8",
	            		success : function(data) {
//	            			$.ajax({
//	            				url : "rest/command-group/"+dataItem.id+"/unitType",
//	            				type : "PUT",
//	            				data : "unitType/"+unittype_id,
//	            				contentType: "text/uri-list",
//	            				success:function(data){
//	            					$.ajax({
//	    	            				url : "rest/command-group/"+dataItem.id+"/neType",
//	    	            				type : "PUT",
//	    	            				data : "neType/"+netype_id,
//	    	            				contentType: "text/uri-list",
//	    	            				success:function(data){
	    	    	            			infoWindow.obj.close();
	    	            					infoTip({content: "添加成功！",color:"#D58512"});
	    	            					dataSource.read(searchparams);
//	    	            				}
//	    	            			});
//	            				}
//	            			});	
	            		},
	            		fail : function(data) {
	            			showNotify(data.message,"error");
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
					title: "命令组管理"
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
			title: "选择指令"
		});
	}
	var bindWindowObj = $("#bindWindow").data("kendoWindow");
	
	//全部加入
	$("#addAll").on("click",function(){
		var dataView = windowGridObj.dataGrid.dataSource.view();
		var checkedId = "";
		for(var i = 0; i < dataView.length; i++){
			checkedId += "checkItem/"+dataView[i].itemId+"\n";
		}
		$.ajax({
			url : "rest/command-group/"+dataItem.groupId+"/checkItem",
			type : "PATCH",
			data : checkedId,
			contentType: "text/uri-list",
			success:function(data){
				var html = "<button added='true' class='addOrRemoveBtn btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
				$(".addOrRemoveBtn[added='false']").parent().html(html);
				infoTip({content : "全部加入成功！",
		    		color : "#D58512"});
			}
		});
	});
	//全部移除
	$("#removeAll").on("click",function(){
		var dataView = windowGridObj.dataGrid.dataSource.view();
		$.each(dataView,function(index,item){
			$.ajax({
				url : "rest/command-group/"+dataItem.groupId+"/checkItem/"+item.itemId,
				type : "DELETE",
				dataType: "json",
				success:function(data){
					
				}
			});
		})
		var html="<button added='false' class='addOrRemoveBtn btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
		$(".addOrRemoveBtn[added='true']").parent().html(html);
		infoTip({content : "全部移除成功！"});
	});
	
	dataGridObj.init(dataSource);
	
	infoWindow.init();
	
});
