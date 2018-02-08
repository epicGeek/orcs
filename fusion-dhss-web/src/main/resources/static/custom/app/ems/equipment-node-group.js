kendo.culture("zh-CN");
var equipmentGroupInfo = {}; // 从equipment_node_group里取出所有设备管理组信息
var dataGridObj = {};
var searchParams = {
		page : 0,
		size : 20,
		sort : "createDate,desc"
	};
var editWindow;
var unitInfoListGrid;
var links ;


function isNotEmpty(value){
	if(value == null || value == "null" || value == ""){
		return "待添加";
	}else{
		return value;
	}
}
$(function() {
	var equipmentUnitList = new kendo.data.DataSource({
		type : "odata",
		transport : {
			read : {
				type : "GET",
				url : "/rest/equipment-unit",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			},
		},
		// batch : true,
		pageSize : 20, // 每页显示个数
		schema : {
			data : function(data) {
			},
			total : function(data) {},
		},
		serverPaging : false,
		serverFiltering : false,
		serverSorting : false,
	});
		
	$("[ href = 'equipment-node-group' ]").addClass("active");
	
	$("#addBtn").on("click", function() {
		var addItem = {
				groupName:""};
		editWindow.obj.setOptions({
			"title" : "添加"
		});

		editWindow.initContent(addItem);
		
	});
	
	// 添加管理组弹窗
	var searchAllEquipmentGroupInfoKendoDataSource = new kendo.data.DataSource({
		type : "odata",
		transport : {
			read : {
				type : "GET",
				url : "/rest/equipment-node-group",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			},
		},
		// batch : true,
		pageSize : 20, // 每页显示个数
		schema : {
			data : function(data) {
				equipmentGroupInfo = data;
				if (data["_embedded"])
					return data["_embedded"]["equipment-node-group"];
				else
					return [];
			},
			total : function(data) {
				if (data["_embedded"]) {
					return data._embedded["equipment-node-group"].length;
				} else {
					return 0;
				}

			},
		},
		serverPaging : false,
		serverFiltering : false,
		serverSorting : false,
		sort:{ field: "createDate", dir: "desc" }
	});
	//
	dataGridObj = {
		init : function() {
			this.dataGrid = $("#dataGrid")
					.kendoGrid(
							{
								dataSource : searchAllEquipmentGroupInfoKendoDataSource,
								height : $(window).height()
										- $("#dataGrid").offset().top - 50,
								width : "100%",
								reorderable : true,
								resizable : true,
								sortable : false,
								columnMenu : false,
								pageable : true,
								columns : [{
											field : "groupName",
											template : "<span  title='#:isNotEmpty(groupName)#'>#:isNotEmpty(groupName)#</span>",
											title : "<span  title='设备组名称'>设备组名称</span>"
										},
										{
											template : "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 修改</button>&nbsp;&nbsp;"
													+ "<button class='deleteBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon-remove-sign'></i> 删除</button>&nbsp;&nbsp;",
											title : "<span  title='操作'>操作</span>"
										} ],
								dataBound : function() {
									dataGridObj.editClick();
									dataGridObj.deleteClick();
								}
							}).data("kendoGrid");
		},
		// 删除
		deleteClick : function() {
			$(".deleteBtn").on(
					"click",
					function() {
						if (confirm("确定删除吗？")) {
							var dataItem = dataGridObj.dataGrid
									.dataItem($(this).closest("tr"));
							//							console.log(dataItem);
							$.ajax({
								url : dataItem._links.self.href,
								type : "delete",
								success : function(data) {
									infoTip({
										content : "删除成功！"
									});
									searchAllEquipmentGroupInfoKendoDataSource
											.read(searchParams);
								}
							});
						}
					});
		},
		editClick : function(){
			$(".editBtn").on("click", function() {
				var dataItem = dataGridObj.dataGrid
				.dataItem($(this).closest("tr"));
				editWindow.obj.setOptions({
					"title" : "修改"
				});

				editWindow.initContent(dataItem);
			});
		}
		
	};
	dataGridObj.init();

	$('#inputKeyWord').on('keyup', function(event) {
		var filters = [];
		if ($("#inputKeyWord").val() != "") {
			filters.push({
				field : "groupName",
				operator : "contains",
				value : $("#inputKeyWord").val()
			});
		}
		searchAllEquipmentGroupInfoKendoDataSource.filter(filters);
		searchAllEquipmentGroupInfoKendoDataSource.fetch();
	});
	
	
	$('#clearsearch').on('click', function(event) {
		$("#inputKeyWord").val("");
		var filters = [];
		searchAllEquipmentGroupInfoKendoDataSource.filter(filters);
		searchAllEquipmentGroupInfoKendoDataSource.fetch();
	});
	
	
	editWindow = {
			obj : undefined,

			template : undefined,

			id : $("#editWindow"),
			initContent: function(dataItem){
				//填充弹窗内容
				//选中的这条数据放进编辑或添加弹出框里。
				links = dataItem._links?dataItem._links.self.href:"";
				editWindow.obj.content(editWindow.template(dataItem));
				unitInfoListGrid = $("#unitInfoList").kendoGrid({
					dataSource: {
			            transport: {
			                read: {
			                    dataType: "json",
			                    url: "rest/equipment-unit"
			                }
			            },
			            schema : {
			    			data : function(d) {
			    				if(d._embedded){
			    					var array = dataItem.unitIdCol?dataItem.unitIdCol.split(","):[];
			    					var unitArray = [];
			    					$.each(d._embedded["equipment-unit"],function(index,item){
			    						item.flag = $.inArray(item.unitId+"",array);
			    						unitArray[unitArray.length] = item;
			    					})
			    					return unitArray;  //响应到页面的数据
			    			     }else{
			    					return new Array();
			    				}
			    			}
			    		}
			        },
					height: $(window).height()-$("#unitInfoList").offset().top - 300,
					width: "100%",
					reorderable: true,

					resizable: true,

					sortable: false,

					columnMenu: false,

					pageable: false,

					columns: [{
							width: 30,
							template: "<input type='checkbox' value='#:unitId#' name='unitChk' #if(flag != -1){# checked='checked' #}#  />",
							attributes:{"class": "text-center"},
							title: "<input type='checkbox' class='grid_checkbox'/>"
						},
						{
							field: "neName",
							template: "<span  title='#:neName#'>#:neName#</span>",
							title: "<span  title='网元名称'>网元名称</span>"
						}, {
							field: "neType",
							template: "<span  title='#:neType#'>#:neType#</span>",
							title: "<span  title='单元名称'>网元类型</span>"
						},{
							field: "unitName",
							template: "<span  title='#:unitName#'>#:unitName#</span>",
							title: "<span  title='单元类型'>单元名称</span>"
						},  {
							field: "unitType",
							template: "<span  title='#:unitType#'>#:unitType#</span>",
							title: "<span  title='单元类型'>单元类型</span>"
						}],
					dataBound: function() {
						$(".grid_checkbox").on("click",function(){
							$("[name='unitChk']").prop("checked",$(this).prop("checked"))
						})
					}
				}).data("kendoGrid");
				
				
				$('#searchEquipment').on('keyup', function(event) {
					var filters = [];
					if ($("#searchEquipment").val() != "") {
						filters.push({
							field : "unitName",
							operator : "contains",
							value : $("#searchEquipment").val()
						});
						filters.push({
							field : "neType",
							operator : "contains",
							value : $("#searchEquipment").val()
						});
						filters.push({
							field : "neName",
							operator : "contains",
							value : $("#searchEquipment").val()
						});
						filters.push({
							field : "unitType",
							operator : "contains",
							value : $("#searchEquipment").val()
						});
					}
					var arr = {filters : filters , logic : "or"};
					unitInfoListGrid.dataSource.filter(arr);
					unitInfoListGrid.dataSource.fetch();
				});
				
				//弹窗内，保存/取消按钮
				editWindow.saveClick();
				editWindow.cancelClick();
				editWindow.obj.center().open();
			},
			init: function(){
				this.template = kendo.template($("#editWindow").html());
				if (!editWindow.id.data("kendoWindow")) {
					editWindow.id.kendoWindow({
						width: "700px",
						actions: ["Close"],
						modal:true,
						title: "",
						height:"500px"
					});
				}
				editWindow.obj = editWindow.id.data("kendoWindow");
			},
			cancelClick: function(){
				$("#cancelBtn").on("click",function(){
					editWindow.obj.close();
				});
			},
			saveClick : function(){
				$("#saveBtn").on("click",function(){
					 var value = "";
					 $.each($("[name='unitChk']:checked"),function(index,item){
						 value += ","+$(this).val();
					 });
					 $.ajax({
				    		url :  links?links:"/rest/equipment-node-group",
				    		type : links?"PATCH":"POST",
				    		data: kendo.stringify({unitIdCol:value,groupName:$("#groupName").val()}),
				    		dataType: "json",
				            contentType: "application/json;charset=UTF-8",
				    		success : function(data) {
				    			 editWindow.obj.close();
				        		 infoTip({content: "保存成功！",color:"#D58512"});
				        		 searchAllEquipmentGroupInfoKendoDataSource
									.read(searchParams);
				    		}
					 });
				})
			}
		};
	
	editWindow.init();
	
});
