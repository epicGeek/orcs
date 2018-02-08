kendo.culture("zh-CN");
$(function() {
	/*当前导航*/
	$("#topNavList .navListWrap:eq(3) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(3) ul li:eq(4)").addClass("active");
	
	
	var dataGridObj = {
		
		init: function(){
			
			this.dataGrid = $("#dataGrid").kendoGrid({
				
				dataSource: {
					data: dataGridList,
					pageSize:10
				},
				
				height: $(window).height()-$("#dataGrid").offset().top - 50,
				
				reorderable: true,
		
				resizable: true,
		
				sortable: true,
		
				columnMenu: true,
		
				pageable: true,
		
				columns: [{
					field: "loginName",
					template: "<span  title='#:loginName#'>#:loginName#</span>",
					title: "<span  title='登录名称'>登录名称</span>"
				}, {
					field: "userName",
					template: "<span  title='#:userName#'>#:userName#</span>",
					title: "<span  title='用户姓名'>用户姓名</span>"
				}, {
					field: "role",
					template: "<span  title='#:role#'>#:role#</span>",
					title: "<span  title='角色'>角色</span>"
				}, {
					field: "dateTime",
					template: "<span  title='#:dateTime#'>#:dateTime#</span>",
					title: "<span  title='密码有效期'>密码有效期</span>"
				}, {
					template: "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 编辑</button>&nbsp;&nbsp;"+
							  "<button class='deleteBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon-remove-sign'></i> 删除</button>&nbsp;&nbsp;"+
							  "<button class='resetBtn btn btn-xs btn-default'>重置密码</button>",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function(){
					dataGridObj.addClick();
					dataGridObj.editClick();
					dataGridObj.deleteClick();
					dataGridObj.resetClick();
				}
			}).data("kendoGrid");
		},
		
		//添加按钮，显示弹窗
		addClick: function() {
			
			$("#addBtn").on("click", function() {
				
				var dataItem = {
					loginName:"",
					userName:"",
					dateTime:"",
					phone:"",
					email:"",
					remarks:""
				};
                
                infoWindow.obj.setOptions({"title":"添加"});
                
                infoWindow.initContent(dataItem,true);
			});
		},
		
		//编辑
		editClick: function() {
			$(".editBtn").on("click", function() {
				
                var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
                
                infoWindow.obj.setOptions({
                	"title":"修改"+"【 <span style='color:blue;'>"+dataItem.loginName+"</span> 】"
                	});
                
                infoWindow.initContent(dataItem,false);
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
		},
		
		//重置密码
		resetClick: function() {
			$(".resetBtn").on("click", function() {
				var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
				var dateTime = new Date();
				var year = dateTime.getFullYear();
				var month = dateTime.getMonth()+1;
				month = (month<=9)?(0+""+month):month;
				var day = dateTime.getDate();
				day = (day<=9)?(0+""+day):day;
				/*if(confirm('重置【'+dataItem.loginName+'】的密码\n\n重置后密码格式：登录名+“_Cz”+当前年月日\n\n重置后密码：'+dataItem.loginName+'_Cz'+year+month+day)){
					var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
					dataGridObj.dataGrid.dataSource.remove(dataItem);
				    
				}*/
				var title = '重置  <b style="color:red;">'+dataItem.loginName+'</b> 的密码';
				var content='<label style="display:inline-block;width: 120px;text-align:right;">重置后密码格式：</label> <span style="color:blue;">登录名+“_Cz”+当前年月日</span><br/>'+
							'<label style="display:inline-block;width: 120px;text-align:right;">重置后密码：</label> <span style="color:blue;">'+dataItem.loginName+'_Cz'+year+month+day+"</span>";
				confirmWindow({"content":content,"title":title},function(){
					infoTip({content: "重置密码成功！"});
				});
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
		
		initContent: function(dataItem,addItem){
			
			//填充弹窗内容
			infoWindow.obj.content(infoWindow.template(dataItem));
			
			//如果是添加，隐藏密码有效期字段4
			if(addItem){
				$("#dateTimeWrap").hide();
			} else {
				$("#dateTimeWrap").show();
				if(!$("#dataTime").data("kendoDateTimePicker")){
					$("#dataTime").kendoDateTimePicker({
						format: "yyyy-MM-dd"
					});
				}
			}
			
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
	
	dataGridObj.init();
	
	infoWindow.init();
	
});

var dataGridList=[
	{
		loginName:"yuxiaoping",
		userName:"余晓平",
		role:"客服中心",
		dateTime:"2015-08-12",
		phone:"135981376768",
		email:"sehu@136.com",
		remarks:""
	}, {
		loginName:"panli",
		userName:"潘力",
		role:"初级维护人员",
		dateTime:"2015-08-12",
		phone:"135981376768",
		email:"sehu@136.com",
		remarks:""
	}, {
		loginName:"wujiahui",
		userName:"吴佳慧",
		role:"高级维护人员",
		dateTime:"2015-09-20",
		phone:"15867567876",
		email:"sanxing@136.com",
		remarks:""
	}, {
		loginName:"lihong3",
		userName:"李虹",
		role:"中级维护人员",
		dateTime:"2015-09-17",
		phone:"18767657865",
		email:"selin@136.com",
		remarks:""
	}, {
		loginName:"yangchua",
		userName:"杨川",
		role:"中级维护人员",
		dateTime:"2015-09-17",
		phone:"135981376768",
		email:"asm@136.com",
		remarks:""
	}, {
		loginName:"chenhuopao",
		userName:"陈火炮",
		role:"初级维护人员",
		dateTime:"2015-09-15",
		phone:"135981376768",
		email:"wuliang@136.com",
		remarks:""
	}, {
		loginName:"linlili",
		userName:"林立立",
		role:"prOpe",
		dateTime:"2015-09-15",
		phone:"135981376768",
		email:"wuliang@136.com",
		remarks:""
	}, {
		loginName:"lijing10",
		userName:"李静",
		role:"初级操作人员",
		dateTime:"2015-09-15",
		phone:"135981376768",
		email:"wuliang@136.com",
		remarks:""
	}, {
		loginName:"feiqiang",
		userName:"费强",
		role:"初级操作人员",
		dateTime:"2015-09-15",
		phone:"135981376768",
		email:"wuliang@136.com",
		remarks:""
	}, {
		loginName:"youjun1",
		userName:"尤骏",
		role:"初级操作人员",
		dateTime:"2015-09-15",
		phone:"135981376768",
		email:"wuliang@136.com",
		remarks:""
	}, {
		loginName:"chenwei",
		userName:"陈伟",
		role:"中级操作人员",
		dateTime:"2015-09-15",
		phone:"135981376768",
		email:"wuliang@136.com",
		remarks:""
	}, {
		loginName:"common",
		userName:"普通用户",
		role:"未分配用户组",
		dateTime:"2015-09-15",
		phone:"135981376768",
		email:"wuliang@136.com",
		remarks:""
	}
];
