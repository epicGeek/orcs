var app = angular.module("systemApp", [ "kendo.directives" ]);
var myProfileController = app.controller("MyProfileController", [ '$scope',function($scope) {

} ]);

var linkController = app.controller("LinkController", [ '$scope',function($scope) {

} ]);

var sustemUserController = app.controller("SystemUserController", [ '$scope',function($scope) {
			$scope.dataSource = new kendo.data.DataSource({
				transport : {
					read : {
						type : "GET",
						url : "rest/system-user/search/findByFilter",
						dataType : "json",
						contentType : "application/json;charset=UTF-8"
					}
				},
				pageSize : 10,
				serverPaging : false,
				serverFiltering : false,
				serverSorting : false
			});
			$scope.userDataGridOption = {
					dataSource:this.dataSource,
					reorderable: true,
					resizable: true,
					sortable: true,
					columnMenu: true,
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
						template: "<span title='#:expireDate#'>#:expireDate#</span>",
						title: "<span title='密码有效期'>密码有效期</span>",
						format: "{0: yyyy-MM-dd}" //格式化时间
					}, {
						template: "<button class='editBtn btn btn-xs btn-info'><i class='glyphicon glyphicon-edit'></i> 编辑</button>&nbsp;&nbsp;"+
								  "<button class='deleteBtn btn btn-xs btn-warning'><i class='glyphicon glyphicon-remove-sign'></i> 删除</button>&nbsp;&nbsp;"+
								  "<button class='resetBtn btn btn-xs btn-default'>重置密码</button>",
						title: "<span  title='操作'>操作</span>"
					}]
			}
			$scope.addItem = function(){
				alert();
			}
			$scope.dataSource.read();
		} ]);