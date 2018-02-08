var project ="project";
function usp(){
	$(function() {
		
		var  tableListObj = {
				tableObj: undefined,
				//生成表格
				initTable: function() {
					if (tableListObj.tableObj) {
						tableListObj.tableObj.destroy();
					}
					this.tableObj = $('#dataTables').DataTable({
						"responsive": true,
						"paging": true, 
						"pageLength": 10,
						"pageType": "full_numbers",
						"searching": false,
						"ordering": false,
						"scrollX": "100%",
						"scrollCollapse": true,
						"lengthChange": false,
						"destroy": true,
					//	"data": dataArr,
						"processing": true,
						"serverSide": true,
						"ajax": {
							"dataType ": 'json',
							"type" : "GET",
							"url" : "/queryAll",
							"data":{
								"type":"project"
							},
						/*   "data": function(d) {
							      "project";
			       				return d;
						   }*/
						},
						"columns": [{
							"title": "序号",
							"data": "orderby",
						}, {
							"title": "项目名称",
							"data": "projectName"
						},  {
							"title": "运营商",
							"data": "cbt"
						},{
							"title": "操作",
							"render": function() {
								return "<button class='deleteOneLogBtn btn btn-xs btn-default '>删除</button>&nbsp;&nbsp;" +
								"<button class='editOneLogBtn btn btn-xs btn-default '>编辑</button>";
							}
						}],
						"oLanguage": {
							"sProcessing": "正在加载中......",
							"sLengthMenu": "",
							"sInfo":"",
							"sInfoEmpty": "",
							"sInfoFiltered": "",
							"sInfoPostFix": "",
							"sSearch": "",
							"sUrl": "",
							"oPaginate": {
								"sFirst": "<<",
								"sPrevious": "<",
								"sNext": ">",
								"sLast": ">>"
							}
						}
					});
				},
				
				//行内编辑按钮
				editOneLog: function() {
					$('#tableWrap').on('click', '.editOneLogBtn', function() {
						$("#listTab").hide();
						$("#formTab").show();
						$("#forDescTip").hide();
						
						formObj.currentDataItem = tableListObj.tableObj.row($(this).closest("tr")).data();
						formObj.setItem();
					});
				},
				
				//行内删除
				deleteSingleLog: function() {
					$('#tableWrap').on('click', '.deleteOneLogBtn', function() {
						var that = $(this);
						//询问框
						layer.confirm('确认删除数据么？', {
							btn: ['确认', '取消'] //按钮
						}, function() {
							//确认事件
							var selectedTr = that.closest('tr');
							var dataItem = tableListObj.tableObj.row(selectedTr).data();
							//从页面列表里删除外，还要从数据库中删除数据
							tableListObj.tableObj.row(selectedTr).remove().draw();
							layer.msg('删除成功！', { icon: 1 });
						}, function() {});
					});
				},
				
				init: function() {
					this.initTable();
					this.editOneLog();
					this.deleteSingleLog();
				}
		};
		
		//添加按钮
		$("#addBtn").on("click",function(){
			$('#myModal').modal('show');
		});
		
		//弹窗
		modalObj = {
				
				//清空弹窗内的数据
				clearModal: function(){
					$("#productInput").val("");
					$("#productVer").val("");
					$("#productId").val("");
				},
				
				//设置弹窗内的数据
				setModal: function(data){
					$("#productInput").val(data.product);
					$("#productVer").val(data.scoreto);
					$("#productId").val(data.level);
				},
				
				//弹窗保存按钮(新增、修改)
				saveBtn: function(){
					$("#saveBtn").on("click",function(){
						postParams.product = $("#productInput").val();
						postParams.version = $("#productVer").val();
						postParams.orderby = $("#productId").val();
						
						if(!validataNumber(postParams.scorefrom)){return;};
						if(!validataNumber(postParams.scoreto)){return;};
						if(!validataNumber(postParams.level)){return;};
						
						if(postParams.id!="" && typeof(postParams.id)!="undefined"){
							postAddAndEdit('PATCH','rest/scorelevel/'+postParams.id);
						}else{
							postAddAndEdit('POST','rest/scorelevel/');
						}
					});
				}
		};
		
		
		var formObj = {
				currentDataItem:undefined,
				//设置字段值
				setItem: function() {
					var dataItem = formObj.currentDataItem;
					$("#selectedDateId").html(dataItem.id);
					$("#productInput").data("selectize").setValue(dataItem.product.id);
					$("#projectInput").data("selectize").setValue(dataItem.projectName.id);
					$("#productPeriodInput").data("selectize").setValue(dataItem.stage.id);
					$("#workTypeInput").data("selectize").setValue(dataItem.workPackageType.id);
					$("#workContentInput").data("selectize").setValue(dataItem.workPackage.id);
					$("#moduleInput").val(dataItem.modular);
					$("#descInput").val(dataItem.jobOperator);
					$("#workTimeInput").val(dataItem.time);
				},
				//获取字段值
				getItem:function(){
					var dataItem = formObj.currentDataItem;
					return {
						id:dataItem.id,
						logDate: dataItem.logDate,
						userName:$("#realName").val(),
						product:{product:$("#productInput").text(),id:$("#productInput").val()},
						projectName: {projectName:$("#projectInput").text(),id:$("#projectInput").val()},
						stage: {stage:$("#productPeriodInput").text(),id:$("#productPeriodInput").val()},
						workPackageType: {workType:$("#workTypeInput").text(),id:$("#workTypeInput").val()},
						workPackage: {workPackage:$("#workContentInput").text(),id:$("#workContentInput").val()},
						modular: $("#moduleInput").val(),
						time: $("#workTimeInput").val(),
						jobOperator: $("#descInput").val(),
						status: false
					};
				},
				//取消按钮
				cancelEdit: function() {
					$("#cancelEditBtn").on("click", function() {
						$("#listTab").show();
						$("#formTab").hide();
					});
				},
				//保存按钮
				saveEdit: function() {
					$("#saveEditBtn").on("click", function() {
						var newObj = formObj.getItem();
						var product=newObj.product.product;
						var proName=newObj.projectName.projectName;
						var otherBool = false, timeBool=false;
						
						if(otherBool && timeBool){
							var edits = [];
							edits.push(newObj);
							//ajax
							$.when(addOrEditLogData(edits))
							.done(function(data){
								if(data == true || data == "true"){
									$("#tableWrap .checkedAll").prop("checked", false);
									if(tableListObj.tableObj){
										tableListObj.tableObj.fnReloadAjax();
									}
									layer.msg('修改成功！', { icon: 1 });
								} else {
									layer.msg('修改失败！', { icon: 2 });
								}
							})
							.fail(function(){
								layer.msg('修改失败！', { icon: 2 });
							});
							//显示表格，隐藏编辑面板
							$("#listTab").show();
							$("#formTab").hide();
						}
						
					});
				},
		}; 
		tableListObj.init();
		
		
	});
	
}

function getParameters() {
	
	return "project";

   // return parameters;
}



