kendo.culture("zh-CN");

$(function() {
	
	var searchparams = {
			page: 1,
			size : 10,
			searchField : '',
			sort: "createdTime,desc"
	};
	var dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: "rest/system-user/search/findPageByFilter",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            },
            parameterMap: function (options, operation) {
                if (operation == "read") {
                	searchparams.page = options.page - 1;
                	searchparams.size = options.pageSize;
                	searchparams.searchField = $("#input-search-field").val();
                    return searchparams;                   
                }
            }
        },
        pageSize: 20,
        requestEnd: function(e) {
            var response = e.response;
            if(response){
                var type = e.type;
                if(type !='read'){
                     this.read();                  
                }
            }else{
                //alert("服务器异常，请重试！");
            }   
        },
        schema: {
            data: function (data) {
            	if(data["_embedded"]){
                    return data["_embedded"]["system-user"];//返回页面数据
            	}else{
            		return [];
            	}
            },
            total: function(d) {
            	if(d.page){
                    return d.page.totalElements; //总条数
            	}
            }
        },
        serverPaging : true,
		serverFiltering : true,
		serverSorting : true
    });
	var stompClient = null;
	var dataGridObj = {
		init: function(){
			this.dataGrid = $("#dataGrid").kendoGrid({
				dataSource:dataSource,
				height: $(window).height()-$("#dataGrid").offset().top - 50,
				reorderable: true,
				resizable: true,
				sortable: false,
				columnMenu: false,
				pageable: true,
				columns: [{
					field: "userName",
					template: "<span  title='#:userName#'>#:userName#</span>",
					title: "<span  title='登录名称'>登录名称</span>"
				}, {
					field: "realName",
					template: "<span  title='#:realName#'>#:realName#</span>",
					title: "<span  title='用户姓名'>用户姓名</span>"
				}, {
					field: "email",
					template: "<span  title='#:email#'>#:email#</span>",
					title: "<span  title='邮箱'>邮箱</span>"
				}, {
					field: "expireDate",
					template: "<span title='#:formattedDate#'>#:formattedDate#</span>",
					title: "<span title='密码有效期'>密码有效期</span>",
					format: "{0:yyyy-MM-dd}" //格式化时间
				}, {
					template: "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 编辑</button>&nbsp;&nbsp;"+
							  "<button class='deleteBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon-remove-sign'></i> 删除</button>&nbsp;&nbsp;"+
							  "<button class='resetBtn btn btn-xs btn-default'>重置密码</button>&nbsp;&nbsp;"+
							  "<button class='delayBtn btn btn-xs btn-success'>密码延后90天</button>",
					title: "<span  title='操作'>操作</span>"
				}],
				dataBound: function(){
					dataGridObj.addClick();
					dataGridObj.editClick();
					dataGridObj.deleteClick();
					dataGridObj.resetClick();
					dataGridObj.refreshClick();
					dataGridObj.clearFilterClick();
					dataGridObj.delayBtnClick();
				}
			}).data("kendoGrid");
		},
		// 添加按钮，显示弹窗
		addClick: function() {
			$("#addBtn").on("click", function() {
				var dataItem = {
					href:"rest/system-user/",
					userName:"",
					realName:"",
					email:"",
					mobile:"",
					expireDate:""
				};
                infoWindow.obj.setOptions({"title":"添加"});
                infoWindow.initContent(dataItem,true);
			});
		},
		//编辑
		editClick: function() {
			$(".editBtn").on("click", function() {
				var pickRow = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
				var itemHref = (pickRow._links.self.href);
				$.ajax({
					dataType : 'json',
					type : "GET",
					url : itemHref,
					success : function(data) {
						infoWindow.obj.setOptions({
		                	"title":"修改"+"【 <span style='color:blue;'>"+data.realName+"</span> 】"
		                });
						data.href = data._links.self.href;
		                infoWindow.initContent(data,false);
					},
					error : function(data) {
						showNotify(data.message,"error");
					}
				});
			});
		},
		//删除
		deleteClick: function() {
			$(".deleteBtn").on("click", function() {
				if(confirm("确定删除吗？")){
					var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
					var deleteURL = (dataItem._links.self.href);
					console.log(deleteURL);
					$.ajax({
						url:deleteURL,
						type:"DELETE",
						success:function(data){
							dataGridObj.dataGrid.dataSource.remove(dataItem);
						    infoTip({content: "删除成功！"});
						    dataGridObj.read();
						},
						error:function(data){
							infoTip({content: "删除失败！"});
						}
					});
				}
			});
		},
		refreshClick: function(){
			$("#input-search-field").on("keyup",function(){
            	searchparams.searchField = $("#input-search-field").val();
                dataSource.read(searchparams);
			});
		},
		delayBtnClick: function(){
			$(".delayBtn").on("click",function(){
				var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
				var userUrl = dataItem._links.self.href;
				var userId = userUrl.substring(userUrl.lastIndexOf("/")+1);
				var title = ' <b style="color:red;">'+dataItem.userName+'</b> 密码延后90天';
				var content='<span style="color:blue;">当前时间往后90天</span><br/>';
				confirmWindow({"content":content,"title":title},function(){
					$.ajax({
						url:"system-user/delay-password/"+userId,
						dataType : 'json',
						type:"PATCH",
		                contentType: "application/json;charset=UTF-8",
		                success:function(status){
						    infoTip({content: "成功！"});
						    dataSource.read(searchparams);
						}
					});
				});
			});
		},

		clearFilterClick: function(){
			$("#button-reset").on("click",function(){
				$("#input-search-field").val("");
            	searchparams.searchField = $("#input-search-field").val();
                dataSource.read(searchparams);
			});
		},
		//重置密码
		resetClick: function() {
			$(".resetBtn").on("click", function() {
				var dataItem = dataGridObj.dataGrid.dataItem($(this).closest("tr"));
				var userUrl = dataItem._links.self.href;
				var userId = userUrl.substring(userUrl.lastIndexOf("/")+1);
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
				var title = '重置  <b style="color:red;">'+dataItem.userName+'</b> 的密码';
				var content='<label style="display:inline-block;width: 120px;text-align:right;">重置后密码格式：</label> <span style="color:blue;">登录名+“_Cz”+当前年月日</span><br/>'+
							'<label style="display:inline-block;width: 120px;text-align:right;">重置后密码：</label> <span style="color:blue;">'+dataItem.userName+'_Cz'+year+month+day+"</span>";
				confirmWindow({"content":content,"title":title},function(){
					$.ajax({
						url:"system-user/reset-password/"+userId,
						dataType : 'json',
						type:"PATCH",
		                contentType: "application/json;charset=UTF-8",
		                success:function(data){
						    infoTip({content: "重置成功！"});
						    dataGridObj.read();
						}
					});
//					infoTip({content: "重置密码成功！"});
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
				var data_to_save = {};
				var data_to_save_url = $("#input-href").val();
				data_to_save.userName = $("#input-userName").val();
				data_to_save.realName = $("#input-realName").val();
				data_to_save.expireDate = $("#input-expireDate").val();
				data_to_save.mobile = $("#input-mobile").val();
				data_to_save.email = $("#input-email").val();
				var szReg=/^[A-Za-zd0-9]+([-_.][A-Za-zd0-9]+)*@([A-Za-zd0-9]+[-.])+[A-Za-zd0-9]{2,5}$/;
				if(data_to_save.userName == ""){
					infoTip({content: "登录名称不能为空！",color:"#FF8512"});
				}else if(data_to_save.realName == ""){
					infoTip({content: "用户姓名不能为空！",color:"#FF8512"});
				}else if(!$("#dateTimeWrap").is(":hidden") && data_to_save.expireDate == ""){
					infoTip({content: "密码有效期不能为空！",color:"#FF8512"});
				}else if(data_to_save.email != "" && !szReg.test(data_to_save.email)){
					infoTip({content: "邮箱格式不正确！",color:"#FF8512"});
				}else{
					//此为修改
					$.ajax({
						dataType : 'json',
						type : (data_to_save_url=="rest/system-user/")?"POST":"PATCH",
		                url: data_to_save_url,
		                contentType: "application/json;charset=UTF-8",
		                data:kendo.stringify(data_to_save),
		                success:function(data){
							infoWindow.obj.close();
							$("#dataGrid").data("kendoGrid").pager.page(searchparams);
//			                dataSource.read(searchparams);
							infoTip({content: "保存成功!",color:"#D58512"});
						},
						error : function(data) {
							console.log(data);
							infoTip({content: "登录名称已经存在!",color:"#FF8512"});
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
		
		initContent: function(dataItem,addItem){
			//填充弹窗内容
			infoWindow.obj.content(infoWindow.template(dataItem));
			
			//如果是添加，隐藏密码有效期字段4
			if(addItem){
				$("#dateTimeWrap").hide();
			} else {
				$("#dateTimeWrap").show();
				$("#input-userName").attr("readonly",true);
				if(!$("#input-expireDate").data("kendoDatePicker")){
					$("#input-expireDate").kendoDatePicker({
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

















