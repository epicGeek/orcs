var searchparams = {
		page:1,
		size:10,
		searchField:''
};	
//定义一个比较器
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

$(function() {
	
	/*当前导航*/
	$("#topNavList .navListWrap:eq(3) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(3) ul li:eq(0)").addClass("active");
	$("#navList a[href='number-section-manage']").addClass("active");

 /*   $.ajax({
		url : "rest/system-area",
		type : "GET",
		success : function(data) {
			var html = "";
			$.each(data._embedded["system-area"],function(index,item){
				html += '<option value="' + item._links.self.href + '">' + item.areaName + '</option>';
			});            			
			$('#instructionArea').html(html);
		},
		error : function(data) {
		    infoTip({content: data.message});
		}
	});*/
	
	var dataItem = {};
	var dataSource=new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: "rest/equipment-number-section",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            }
        },
        schema: {
            data: function(d) {        	
            	if(d._embedded){
            		return d._embedded["equipment-number-section"].sort(compare("numberId")); //响应到页面的数据
            	}else{
            		return new Array();
            	}
            },
            total: function(d) {
            	if(d._embedded){
            		return d._embedded["equipment-number-section"].length; 
            	}
            	return 0;
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
				columnMenu: true,		
				pageable: true,		
				columns: [{ 
					field: "_links.self.href", 
					title:"_links.self.href", 
					hidden:true
				},{
					field: "number",
					template: "#if(number!=null){#<span  title='#:number#'>#:number#</span>#}#",
					title: "<span  title='MSISDN号段'>MSISDN号段</span>"
				}, {
					field: "imsi",
					template: "#if(imsi!=null){#<span  title='#:imsi#'>#:imsi#</span>#}#",
					title: "<span  title='IMSI号段'>IMSI号段</span>"
				}, {
					field: "areaName",
					template: "#if(areaName!=null){#<span title='#=areaName#'>#=areaName#</span>#}#",
					title: "<span  title='所属地区'>所属地区</span>"
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
					_link:"",
					number: "",
					imsi: ""
				};
                
                infoWindow.obj.setOptions({"title":"添加"});             
                infoWindow.initContent(dataItem);
                
                $("#instructionArea option:eq(0)").attr("selected","selected"); 
			});
		},
		
		//编辑
		editClick: function() {
			$(".editBtn").on("click", function() {
				
                dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
                console.log(dataItem);
                infoWindow.obj.setOptions({"title":"修改"});
                infoWindow.initContent(dataItem);
                /*$.ajax({
            		url : "rest/system-area",
            		type : "GET",
            		success : function(data) {
            			var html = "";
            			$.each(data._embedded["system-area"],function(index,item){
            				html += '<option value="' + item._links.self.href + '">' + item.areaName + '</option>';
            			});            			
            			$('#instructionArea').html(html);
            			//回显指令类型
//            			$("#instructionArea option[value='"+dataItem.area.id+"']").attr("selected", "selected");
            		},
            		error : function(data) {
            			showNotify(data.message,"error");
            		}
            	}); */
			});
		},
		//删除
		deleteClick: function() {
			$(".deleteBtn").on("click", function() {
				if(confirm("确定删除吗？")){
					dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));					
					$.ajax({
						url : dataItem._links.self.href,
	            		type : "DELETE",
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
				dto.number = $("#page-number").val();
				dto.imsi = $("#page-imsi").val();
				if(dto.number == "" && dto.imsi == ""){
					infoTip({content: "请输入MSISDN号段或者IMSI号段！",color:"#D58512"});
					return;
				}
//				var area_id = $("#instructionArea").val();
				$.ajax({
            		url : dataItem._links?dataItem._links.self.href:"rest/equipment-number-section/",
            		type : dataItem._links?"PATCH":"POST",
            		data: kendo.stringify(dto),
            		dataType: "json",
                    contentType: "application/json;charset=UTF-8",
                    success:function(data){
                    	infoWindow.obj.close();
                    	infoTip({content: "保存成功！",color:"#D58512"});
    					dataSource.read(searchparams);
                    }
            	});
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
	
	
	$('#clearsearch').on('click', function (event) { 
        $("#inputKeyWord").val("");
        var filters = [];
        dataSource.filter(filters);
		dataSource.fetch();
	});
	$('#inputKeyWord').on('keyup',function (event){
		var filters = [];
    	if($("#inputKeyWord").val() != ""){
    		filters.push({field: "numberAndImsi", operator: "contains", value: $("#inputKeyWord").val()});
    	}
		dataSource.filter(filters);
		dataSource.fetch();
	});		
	
	dataGridObj.init();
	
	infoWindow.init();
	
});