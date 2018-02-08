$(function() {
	/*当前导航*/
	$("#topNavList .navListWrap:eq(3) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(3) ul li:eq(3)").addClass("active");
	
	
	var dataGridObj = {

		init: function() {

			this.dataGrid = $("#dataGrid").kendoGrid({

				dataSource: {
					data: dataGridList,
					pageSize: 10
				},

				height: $(window).height() - $("#dataGrid").offset().top - 50,

				reorderable: true,

				resizable: true,

				sortable: true,

				columnMenu: true,

				pageable: true,

				columns: [{
					field: "netElementName",
					template: "<span  title='#:netElementName#'>#:netElementName#</span>",
					title: "<span  title='网元名称'>网元名称</span>"
				}, {
					field: "unitType",
					template: "<span  title='#:unitType#'>#:unitType#</span>",
					title: "<span  title='单元类型'>单元类型</span>"
				}, {
					field: "unitName",
					template: "<span  title='#:unitName#'>#:unitName#</span>",
					title: "<span  title='单元名称'>单元名称</span>"
				}, {
					field: "MMLInterface",
					template: "<span  title='#:MMLInterface#'>#:MMLInterface#</span>",
					title: "<span  title='MML接口地址'>MML接口地址</span>"
				}, {
					field: "MMLILoginProtocol",
					template: "<span  title='#:MMLILoginProtocol#'>#:MMLILoginProtocol#</span>",
					title: "<span  title='MML登录协议'>MML登录协议</span>"
				}, {
					width: 160,
					template: "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 编辑单元</button>&nbsp;&nbsp;" +
						"<button class='deleteBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon-remove-sign'></i> 删除</button>",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function() {
					dataGridObj.addClick();
					dataGridObj.editClick();
					dataGridObj.deleteClick();
				}
			}).data("kendoGrid");
		},

		//添加按钮，显示弹窗
		addClick: function() {

			$("#addBtn").on("click", function() {

				var dataItem = {
					netElementName: "",
					unitType: "",
					unitName: "",
					MMLInterface: "",
					MMLILoginProtocol: ""
				};

				infoWindow.obj.setOptions({
					"title": "添加单元"
				});

				infoWindow.initContent(dataItem);
			});
		},

		//编辑
		editClick: function() {
			$(".editBtn").on("click", function() {

				var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));

				infoWindow.obj.setOptions({
					"title": "修改单元"
				});

				infoWindow.initContent(dataItem);
			});
		},

		//删除
		deleteClick: function() {
			$(".deleteBtn").on("click", function() {
				if (confirm("确定删除么？")) {
					var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
					dataGridObj.dataGrid.dataSource.remove(dataItem);
					infoTip({
						content: "删除成功！"
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
		saveClick: function() {
			$("#saveBtn").on("click", function() {
				infoWindow.obj.close();
				infoTip({
					content: "保存成功！",
					color: "#D58512"
				});
			});
		},
		initContent: function(dataItem) {
			
			//填充弹窗内容
			//…………………………
			$('a[data-toggle="tab"][href="#tab1"]').tab('show');//显示第一个面板
			infoWindow.obj.center().open();
		},

		init: function() {

			if (!infoWindow.id.data("kendoWindow")) {
				infoWindow.id.kendoWindow({
					width: "700px",
					height: "400px",
					actions: ["Close"],
					modal: true,
					title: "号段管理"
				});
			}
			infoWindow.obj = infoWindow.id.data("kendoWindow");
			//取消按钮
			$(".cancelBtns").on("click", function(){
				infoWindow.obj.close();
			});
		}
	};

	dataGridObj.init();

	infoWindow.init();

	//Web接口数据
	var webInterfaceObj = {
		
		gridObj: undefined,
		
		init: function(){
			
			this.gridObj = $("#webInterfaceList").kendoGrid({
		
				dataSource: {
					data: webInterfaceGridList
				},
				
				height: 260,
				
				reorderable: true,
		
				resizable: true,
		
				sortable: true,
		
				columnMenu: true,
		
				pageable: false,
		
				columns: [{
					width: 28,
					template: "<span></span><input type='checkbox' #if(checked){# checked #}# value=#:checked# />",
					attributes: {
						"class": "text-center"
					},
					title: ""
				}, {
					field: "URL",
					template: "<span  title='#:URL#'>#:URL#</span>",
					title: "<span  title='URL'>URL</span>"
				}, {
					field: "passWord",
					template: "<span  title='#:passWord#'>#:passWord#</span>",
					title: "<span  title='密码'>密码</span>"
				}, {
					field: "name",
					template: "<span  title='#:name#'>#:name#</span>",
					title: "<span  title='用户名称'>用户名称</span>"
				}],
				dataBound: function(){
					//提示表格复选框是否进行过修改
					$("#webInterfaceList input[type='checkbox']").on("click",function(){
						var nowChecked = this.checked.toString();
						var valueChecked = $(this).attr("value");
						
						var span = $(this).prev();
						//判断是否已改变
						if(nowChecked == valueChecked){//没变化
							span.removeClass("valueChanged");
						} else {//已改变-使用红色进行标记
							span.addClass("valueChanged");
						}
					});
				}
			}).data("kendoGrid");
		}
	};
	
	//第一次打开Web接口tab后,再加载数据
	$('a[data-toggle="tab"][href="#tab3"]').one('shown.bs.tab', function (e) {
		//e.target // newly activated tab
		//e.relatedTarget // previous active tab
		webInterfaceObj.init();
	});
});

var dataGridList = [{
	netElementName: "BJSGW1_F4",
	unitType: "OMU",
	unitName: "BJSGW1_F41",
	MMLInterface: "10.223.75.196",
	MMLILoginProtocol: "SSH2"
}, {
	netElementName: "BJSGW1_F4",
	unitType: "OMU",
	unitName: "BJSGW1_F42",
	MMLInterface: "10.223.75.196",
	MMLILoginProtocol: "SSH2"
}, {
	netElementName: "BJSGW1_F4",
	unitType: "OMU",
	unitName: "BJSGW1_F43",
	MMLInterface: "10.223.75.196",
	MMLILoginProtocol: "SSH2"
}, {
	netElementName: "BJSGW1_F3",
	unitType: "OMU",
	unitName: "BJSGW1_F31",
	MMLInterface: "10.223.77.132",
	MMLILoginProtocol: "SSH2"
}, {
	netElementName: "BJSGW1_F3",
	unitType: "OMU",
	unitName: "BJSGW1_F32",
	MMLInterface: "10.223.77.132",
	MMLILoginProtocol: "SSH2"
}, {
	netElementName: "BJSGW1_F3",
	unitType: "OMU",
	unitName: "BJSGW1_F33",
	MMLInterface: "10.223.77.212",
	MMLILoginProtocol: "SSH2"
}, {
	netElementName: "BJSGW1_F2",
	unitType: "OMU",
	unitName: "BJSGW1_F22",
	MMLInterface: "10.223.78.4",
	MMLILoginProtocol: "SSH2"
}, {
	netElementName: "BJSGW1_F2",
	unitType: "OMU",
	unitName: "BJSGW1_F21",
	MMLInterface: "10.223.72.132",
	MMLILoginProtocol: "SSH2"
}, {
	netElementName: "BJSGW1_F2",
	unitType: "OMU",
	unitName: "BJSGW1_F23",
	MMLInterface: "10.223.81.68",
	MMLILoginProtocol: "SSH2"
}, {
	netElementName: "BJSOAPGW",
	unitType: "SOAP GW",
	unitName: "BJDZM-LTE-SOAPGW-102",
	MMLInterface: "10.223.139.54",
	MMLILoginProtocol: "SSH2"
}];

var webInterfaceGridList = [
	{
		checked: true,
		URL:"http://172.4.70.8:8020/",
		passWord:"123gfgj456",
		name:"common"
	}, {
		checked: true,
		URL:"http://173.14.7.28:8020/",
		passWord:"016g2345",
		name:"zhangsan"
	}, {
		checked: true,
		URL:"http://172.4.70.8:8020/",
		passWord:"453456",
		name:"liyu"
	}, {
		checked: true,
		URL:"http://173.14.7.28:8020/",
		passWord:"566734",
		name:"showcase"
	}, {
		checked: false,
		URL:"http://172.4.70.8:8020/",
		passWord:"455667",
		name:"wuyu"
	}, {
		checked: false,
		URL:"http://173.14.7.28:8020/",
		passWord:"4545546",
		name:"zhangqi"
	}, {
		checked: false,
		URL:"http://173.14.7.28:8020/",
		passWord:"34354645",
		name:"caoyi"
	}, {
		checked: false,
		URL:"",URL:"http://172.4.70.8:8020/",
		passWord:"23re57",
		name:"wuhan"
	}, {
		checked: false,
		URL:"http://173.14.7.28:8020/",
		passWord:"78987h",
		name:"caozheng"
	}
];
