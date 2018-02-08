
$(function() {
	
	/*当前导航*/
	$("#topNavList .navListWrap:eq(3) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(3) ul li:eq(2)").addClass("active");
	
	
	var dataGridObj = {
		
		init: function(){
			
			this.dataGrid = $("#dataGrid").kendoGrid({
				
				dataSource: {
					data: dataList,
					pageSize:10
				},
				
				height: $(window).height()-$("#dataGrid").offset().top - 50,
				
				reorderable: true,
		
				resizable: true,
		
				sortable: true,
		
				columnMenu: true,
		
				pageable: true,
		
				columns: [{ 
					field: "superiorElement",
					template: "<span  title='#:superiorElement#'>#:superiorElement#</span>",
					title: "<span  title='上级网元'>上级网元</span>"
				}, {
					field: "netElement",
					template: "<span  title='#:netElement#'>#:netElement#</span>",
					title: "<span  title='网元名称'>网元名称</span>"
				}, {
					field: "type",
					template: "<span  title='#:type#'>#:type#</span>",
					title: "<span  title='网元类型'>网元类型</span>"
				}, {
					field: "physicalAddress",
					template: "<span  title='#:physicalAddress#'>#:physicalAddress#</span>",
					title: "<span  title='物理地址'>物理地址</span>"
				}, {
					field: "remarks",
					template: "<span  title='#:remarks#'>#:remarks#</span>",
					title: "<span  title='备注'>备注</span>"
				}, {
					width: 210,
					template: "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 编辑</button>&nbsp;&nbsp;"+
							  "<button class='deleteBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon-remove-sign'></i> 删除</button>&nbsp;&nbsp;"+
							  "<button class='bindBtn btn btn-xs btn-default'>绑定号码段</button>",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function(){
					dataGridObj.addClick();
					dataGridObj.editClick();
					dataGridObj.deleteClick();
					
					//绑定号码段弹窗
					$(".bindBtn").on("click", function(){
						bindWindowObj.center().open();
						windowGridObj.init();
					});
				}
			}).data("kendoGrid");
		},
		
		//添加按钮，显示弹窗
		addClick: function() {
			
			$("#addBtn").on("click", function() {
				
				var dataItem = {
					superiorElement: "",
					netElement: "",
					type: "",
					physicalAddress: "",
					remarks: ""
				};
                infoWindow.obj.setOptions({"title":"添加"});
                
                infoWindow.initContent(dataItem);
                
                $("#typeInput option:eq(0)").attr("selected", "selected");
			});
		},
		
		//编辑
		editClick: function() {
			$(".editBtn").on("click", function() {
				
                var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
                
                infoWindow.obj.setOptions({"title":"修改"});
                
                infoWindow.initContent(dataItem);
                
                $("#typeInput option[value='"+dataItem.type+"']").attr("selected", "selected");
                
			});
		},
	
		//删除
		deleteClick: function() {
			$(".deleteBtn").on("click", function() {
				if(confirm("确定删除么？")){
					var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
					dataGridObj.dataGrid.dataSource.remove(dataItem);
				    infoTip({content: "删除成功！"});
				}
			});
		}
	};
	
	//绑定号码段弹窗内的表格
	var windowGridObj = {
		
		init: function(){
			
			this.dataGrid = $("#bindGrid").kendoGrid({
				
				dataSource: {
					data: bindGridList
				},
				
				height: 300,
				
				reorderable: true,
		
				resizable: true,
		
				sortable: true,
		
				columnMenu: true,
		
				pageable: false,
		
				columns: [{
					field: "MSISDN",
					template: "<span  title='#:MSISDN#'>#:MSISDN#</span>",
					title: "<span  title='MSISDN号段'>MSISDN号段</span>"
				}, {
					field: "IMSI",
					template: "<span  title='#:IMSI#'>#:IMSI#</span>",
					title: "<span  title='IMSI号段'>IMSI号段</span>"
				}, {
					template: "#if(added=='false'){# <button added='false' class='addOrRemoveBtn btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button> #}else{#" + 
							  "<button added='true' class='addOrRemoveBtn btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button> #}#",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function(){
					//每一行的加入/移除按钮
					$("#bindGrid td").delegate(".addOrRemoveBtn","click",function(){
						var td=$(this).closest("td");
						var html="";
						if($(this).attr("added")=="true"){
							html = "<button added='false' class='addOrRemoveBtn btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
						} else {
							html="<button added='true' class='addOrRemoveBtn btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
						}
						td.html(html);
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
				infoWindow.obj.close();
				infoTip({content: "保存成功！",color:"#D58512"});
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
		var html = "<button added='true' class='addOrRemoveBtn btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 移除</button>";
		$(".addOrRemoveBtn[added='false']").parent().html(html);
	});
	//全部移除
	$("#removeAll").on("click",function(){
		var html="<button added='false' class='addOrRemoveBtn btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 加入</button>";
		$(".addOrRemoveBtn[added='true']").parent().html(html);
	});
	
	dataGridObj.init();
	
	infoWindow.init();
	
});

var dataList = [
    {
        "id": 14,
        "superiorElement": "FZHSS05",
        "netElement": "FZHSS05BE03BNK",
        "type": "ONE-NDS",
        "physicalAddress": "",
        "remarks": ""
    },
    {
        "id": 13,
        "superiorElement": "FZHSS05",
        "netElement": "FZHSS05FE03BNK",
        "type": "HSSFE",
        "physicalAddress": "",
        "remarks": ""
    },
    {
        "id": 9,
        "superiorElement": "FZHSS05",
        "netElement": "FZHSS05FE13BNK",
        "type": "NTHLRFE",
        "physicalAddress": "",
        "remarks": ""
    },
    {
        "id": 8,
        "superiorElement": "XIMHSS05",
        "netElement": "XIMHSS5SG1",
        "type": "SGW",
        "physicalAddress": "",
        "remarks": ""
    },
    {
        "id": 7,
        "superiorElement": "XIMHSS05",
        "netElement": "XIMHSS05BE02BNK",
        "type": "ONE-NDS",
        "physicalAddress": "",
        "remarks": ""
    },
    {
        "id": 6,
        "superiorElement": "XIMHSS05",
        "netElement": "XIMHSS05FE02BNK",
         "type": "HSSFE",
        "physicalAddress": "",
        "remarks": ""
    },
    {
        "id": 5,
        "superiorElement": "XIMHSS05",
        "netElement": "XIMHSS05FE12BNK",
         "type": "NTHLRFE",
        "physicalAddress": "",
        "remarks": ""
    },
    {
        "id": 4,
        "superiorElement": "FZHSS05",
        "netElement": "FZHSS5SG1",
        "type": "SGW",
        "physicalAddress": "",
        "remarks": ""
    },
    {
        "id": 3,
        "superiorElement": "FZHSS05",
        "netElement": "FZHSS05BE01BNK",
        "type": "ONE-NDS",
        "physicalAddress": "",
        "remarks": ""
    },
    {
        "id": 2,
        "superiorElement": "FZHSS05",
        "netElement": "FZHSS05FE11BNK",
        "type": "NTHLRFE",
        "physicalAddress": "",
        "remarks": ""
    }
];

//0：未加入，1：已加入
var bindGridList = [
    {
        "id": 4,
        "MSISDN": "8611111111",
        "IMSI": "122222",
        "ne": null,
        "area": {
            "id": 9,
            "areaName": "南平市",
            "areaCode": "0599",
            "ns": null,
            "inUse": null
        },
        "added":'true'
    },
    {
        "id": 3,
        "MSISDN": "86184590108",
        "IMSI": "4600245901080",
        "ne": null,
        "area": {
            "id": 9,
            "areaName": "南平市",
            "areaCode": "0599",
            "ns": null,
            "inUse": null
        },
        "added":'false'
    },
    {
        "id": 1,
        "MSISDN": "86184590114",
        "IMSI": "4600245901000",
        "ne": null,
        "area": {
            "id": 9,
            "areaName": "南平市",
            "areaCode": "0599",
            "ns": null,
            "inUse": null
        },
        "added":'false'
    }
];
