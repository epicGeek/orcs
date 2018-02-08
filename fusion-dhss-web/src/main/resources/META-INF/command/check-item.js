var key = "USER_DATA";
var searchparams = {
		page:1,
		size:10,
		searchField:''
};	
$(function() {
	
//	$("#tb1").on("click",function(){
//		$("#tab1").show();
//		$("#tab2").hide();
//		alert("a");
//	})
//	
//	$("#tb2").on("click",function(){
//		$("#tab1").hide();
//		$("#tab2").show();
//		alert("b");
//	})
	
	
	/*当前导航*/
	$("#topNavList .navListWrap:eq(3) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(3) ul li:eq(6)").addClass("active");
	$("#navList a[href='check-item']").addClass("active");
	
	
	$('body').delegate("#instructionType", 'change', function() {
		var text = $("#instructionType").find("option:selected").text();
		isShowCmdType(text,'');
	});
	
	
	
	var dataItem = {};
	var dataSource=new kendo.data.DataSource({
        type: "odata",
        transport: {
            read: {
                type: "GET",
                url: "/rest/check-item/search/page",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            },
            parameterMap: function(options, operation) {
                if (operation == "read") {       
                    searchparams.page = options.page-1;
                    searchparams.size = options.pageSize;
                    searchparams.searchField = $("#inputKeyWord").val();
                    return searchparams;                   
                }     
            }
        },
        schema: {
            data: function(d) {   
            	if(d._embedded && d._embedded["check-item"]){  
                	return d._embedded["check-item"];
            	}else{
            		return new Array(); //响应到页面的数据
            	}
            	
            },
            total: function(d) {
                return d.page.totalElements; 
            },
            pageSize: function(d) {
                return d.page.size; 
            }
        },
        requestEnd: function(e) {
            var response = e.response;
            if(response){
                var type = e.type;
                if(type !='read'){
                     this.read();                  
                }
            }else{
            	console.log(e);
                alert("服务器异常，请重试！");
            }        
        },
        pageSize: 10,
        serverPaging: true,
        serverSorting: true
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
					field: "_links.self.href", 
					title:"_links.self.href", 
					hidden:true
				},{
					field: "category",
					template: "<span  title='#=category#'>#=category#</span>",
					title: "<span  title='指令类型'>指令类型</span>"
				}, {
					field: "cmdType",
					template: "<span  title='#if(typeof(cmdType)!='undefined'){##:cmdType##}#'>#if(typeof(cmdType)!='undefined'){##:cmdType##}#</span>",
					title: "<span  title='命令类别'>命令类别</span>"
				}, {
					field: "name",
					template: "#if(name!=null){#<span  title='#:name#'>#:name#</span>#}#",
					title: "<span  title='指令名称'>指令名称</span>"
				}, {
					field: "command",
					template: "#if(command!=null){#<span  title='#:command#'>#:command#</span>#}#",
					title: "<span  title='指令内容'>指令内容</span>"
				}, {
					field: "params",
					template: "#if(params!=null){#<span  title='#:params#'>#:params#</span>#}#",
					title: "<span  title='参数名称'>参数名称</span>"
				}, {
					field: "account",
					template: "#if(account!=null){#<span  title='#:account#'>#:account#</span>#}#",
					title: "<span  title='执行指令的用户'>执行指令的用户</span>"
				}, {
					field: "remarks",
					template: "#if(remarks!=null){#<span  title='#:remarks#'>#:remarks#</span>#}#",
					title: "<span  title='备注'>备注</span>"
				}, {
					width: 130,
					template: "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 编辑</button>&nbsp;&nbsp;"+
							  "<button class='deleteBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon-remove-sign'></i> 删除</button>",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function(){
					dataGridObj.addClick();
					dataGridObj.editClick();
					dataGridObj.deleteClick();
				}
			}).data("kendoGrid");
		},
		
		//添加按钮，显示弹窗
		addClick: function() {
			$("#addBtn").on("click", function() {				
				dataItem = {
					id:"",
					params:"",
					category:{id:"",name:""}, 
					command:"",
					name:"", 
					account:"", 
					remarks:"",
					script:""
				};               
                infoWindow.obj.setOptions({"title":"添加"});                
                infoWindow.initContent(dataItem);
                $("#instructionType").kendoDropDownList();
    			$.ajax({
    				url:"search/commandCategory",
    				dataType:"json",
    				success:function(data){
    					$('#cmdType').hide();
    					$("#instructionType").kendoDropDownList({
    						dataSource: data,
    						filter: "contains",
    						suggest: true,
    						change:function(e){
    							$('#cmdTypes').val("");
    							var text = $('#instructionType').data("kendoDropDownList").text();
    							if(key == text){
    								getSubtoolCmdType(dataItem.cmdType);
    							}
    						}
    					});
    				}
    			});
                //指令类型
                $("#instructionType option:eq(0)").attr("selected","selected");          
			});
		},		
		//编辑
		editClick: function() {
			$(".editBtn").on("click", function() {		
				var text;
                dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
                dataItem.link = dataGridObj.dataGrid.dataItem($(this).closest("tr"))._links.self.href;
                infoWindow.obj.setOptions({"title":"修改"});               
                infoWindow.initContent(dataItem);
                $("#instructionType").kendoDropDownList();
    			$.ajax({
    				url:"search/commandCategory",
    				dataType:"json",
    				success:function(data){
    					$("#instructionType").kendoDropDownList({
    						dataSource: data,
    						filter: "contains",
    						suggest: true,
    						change:function(e){
    							$('#cmdTypes').val("");
    							text = $('#instructionType').data("kendoDropDownList").text();
    							if(key == text){
    								getSubtoolCmdType(dataItem.cmdType);
    							}
    						}
    					});
    					$('#instructionType').data("kendoDropDownList").text(dataItem.category);
    					if(dataItem.cmdType != null && dataItem.cmdType != "null" && dataItem.cmdType != ""){
							getSubtoolCmdType(dataItem.cmdType);
						}
    				} 
    			});
    			
                
			});
		},
	
		//删除
		deleteClick: function() {
			$(".deleteBtn").on("click", function() {
				if(confirm("确定删除么？")){
					var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
					var link = dataItem._links.self.href;
					var index = link.indexOf("{") != -1 ? link.indexOf("{") : link.length;
					link = link.substring(0,index);
					dataGridObj.dataGrid.dataSource.remove(dataItem);
					$.ajax({
						url : link,
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
	
	//弹窗
	var infoWindow = {	
		obj: undefined,		
		template: undefined,	
		id: $("#infoWindow"),		
		//保存 【添加】/
		saveClick: function(){
			$("#saveBtn").on("click",function(){
				var dto = {};
				dto.account = $("#page-account").val();
				dto.command = $("#page-command").val();
				dto.name =  $("#page-name").val();
				dto.params =  $("#page-params").val();
				dto.remarks =  $("#page-remarks").val();
				dto.category = $("#instructionType").val();
				dto.cmdType = $("#cmdTypes").val();
				dto.script = $("#command-script").val();
                if(!dataItem.link){//添加
                	$.ajax({
	            		url : "rest/check-item/",
	            		type : "post",
	            		data: kendo.stringify(dto),
	            		dataType: "json",
	                    contentType: "application/json;charset=UTF-8",
	            		success : function(data) {	
	            			massage(infoWindow,dataSource,'添加成功！');
            				infoWindow.obj.close();
            				dataSource.read(searchparams);
//	            			$.ajax({
//	            				url : data._links.category.href,
//	            				type : "PUT",
//	            				data : "category/"+category_id,
//	            				contentType: "text/uri-list",
//	            				success:function(category_data){
//	            					if(cmdTypeId!="" && null!=cmdTypeId){
//	            						$.ajax({
//	            							url : data._links.cmdType.href,
//	            							type : "PUT",
//	            							data : "cmdType/"+cmdTypeId,
//	            							contentType: "text/uri-list",
//	            							success:function(data){
//	            								massage(infoWindow,dataSource,'添加成功！');
//	            								infoWindow.obj.close();
//	            								dataSource.read(searchparams);
//	            							}
//	            						});	
//	    	            			}else{
//	    	            				massage(infoWindow,dataSource,'添加成功！');
//	    	            				infoWindow.obj.close();
//	    	            				dataSource.read(searchparams);
//	    	            			}
//	            				}
//	            			});	            			
	            		},
	            		fail : function(data) {
	            			showNotify(data.message,"error");
	            		}
	            	});
				}else{
					var link = dataItem._links.self.href;
					$.ajax({
	            		url : link,
	            		type : "PATCH",
	            		data: kendo.stringify(dto),
	            		dataType: "json",
	                    contentType: "application/json;charset=UTF-8",
	            		success : function(data) {
	            			infoWindow.obj.close();
        					infoTip({content: "修改成功！",color:"#D58512"});
        					dataSource.read(searchparams);
//	            			$.ajax({
//	            				url : "rest/check-item/"+dataItem.id+"/category",
//	            				type : "PUT",
//	            				data : "category/"+category_id,
//	            				contentType: "text/uri-list",
//	            				success:function(data){
//	            					infoWindow.obj.close();
//	            					infoTip({content: "修改成功！",color:"#D58512"});
//	            					dataSource.read(searchparams);
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
					title: "指令管理"
				});
			}
			infoWindow.obj = infoWindow.id.data("kendoWindow");
		}
	};
	
	$('#clearsearch').on('click', function (event) {
        $("#inputKeyWord").val(""); 
        searchparams.searchField = $("#inputKeyWord").val();
		dataSource.read(searchparams);
	});
	$('#inputKeyWord').on('keyup',function (event){
		searchparams.searchField = $("#inputKeyWord").val();
		dataSource.read(searchparams);
	  });		
	dataGridObj.init();	
	infoWindow.init();	
});

function isShowCmdType(value,id){

		$('#cmdType').hide();
//		$('#cmdTypes').data("kendoDropDownList").text("");
}


function massage(infoWindow,dataSource,text){
	infoWindow.obj.close();
	infoTip({content: text,color:"#D58512"});
	dataSource.read(searchparams);
}

//获取subtool命令类型
function getSubtoolCmdType(cmdType){
	$.ajax({
		url:"subtool/category/search",
		dataType:"json",
		success:function(data){
			$("#cmdTypes").kendoDropDownList({
				dataSource: data,
				filter: "contains",
				suggest: true
			});
			if(cmdType != "null" && cmdType != ""){
				$('#cmdTypes').data("kendoDropDownList").text(cmdType);
			}else{
				if(data.length!=0){
					$('#cmdTypes').data("kendoDropDownList").text(data[0]);
				}
			}
			$('#cmdType').show();
		}
	});
}