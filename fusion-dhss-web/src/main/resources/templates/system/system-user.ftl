<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<!DOCTYPE html>
<html>

	<head lang="zh-cn">
		<@header></@header>
	</head>
	<body>
		<!-- 用户信息 -->
		<@user_bar/>
		<!--顶部导航栏-->
		<@navigate_bar/>

		<!--内容部分-->
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-1 col-xs-12">
					<div id='addBtn' class="addWrap clearfix text-center" >
						<label><i class="glyphicon glyphicon-plus-sign"></i> 添加</label>
						<span></span>
					</div>
				</div>
				<div class="col-sm-5">
					<input id="input-search-field" type="text" class="form-control input-sm" placeholder="筛选登录名称、用户名、电话、邮箱" style="width: 100%;" title="登录名称、用户名、电话、邮箱" />
				</div>
				<div class="col-sm-6">
					<!--<button id="button-search" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-search"></i> 查询</button>-->
					<button id="button-reset" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<div id='dataGrid'></div>
				</div>
			</div>
		</div>
		<!--//内容部分-->
		<!-- 尾部 -->
		<@tailer></@tailer>
		<!-- 尾部 -->
		<script type="text/x-kendo-template" id="windowTemplate">
			<div class="container-fluid">
				<div class="form-horizontal">
					<div class="form-group">
						<div class="col-md-5">
							<input id="input-href" class="form-control input-sm" type="hidden" value='#:href#' />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label">登录名称</label>
						<div class="col-md-5">
							<input id="input-userName" class="form-control input-sm" type="text" value='#:userName#' />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label">用户姓名</label>
						<div class="col-md-5">
							<input id="input-realName" class="form-control input-sm" type="text" value='#:realName#' />
						</div>
					</div>
					<!-- div class="form-group" id='dateTimeWrap'>
						<label class="col-md-2 control-label">密码有效期</label>
						<div class="col-md-5">
							<!-- <input id='input-expireDate' value='#:expireDate#' type="text" style="width:100%;font-size: 12px;margin-top: 3px;margin-bottom: 3px;" />
						</div>
					</div -->
					<div class="form-group">
						<label class="col-md-2 control-label">手机号</label>
						<div class="col-md-5">
							<input id='input-mobile' class="form-control input-sm" type="text" value='#:mobile#' />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label">邮箱</label>
						<div class="col-md-5">
							<input id='input-email' class="form-control input-sm" type="text" value='#:email#' />
						</div>
					</div>
				</div>
			</div>
			<div class="k-edit-buttons k-state-default text-right windowFooter">
				<a id='saveBtn' class="k-button k-button-icontext k-primary k-grid-update">
					<span class="k-icon k-update"></span>保存
				</a>
				<a id='cancelBtn' class="k-button k-button-icontext k-grid-cancel">
					<span class="k-icon k-cancel"></span>取消
				</a>
			</div>
		</script>
		<div id='infoWindow' style="display: none;"></div>

		<@script></@script>
		<!-- custom script -->
		<script type="text/javascript" src="custom/app/system/system-user.js"></script>
	</body>
</html>