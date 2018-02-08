<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<#include "./search_bar_unit.ftl">
<#include "./search_bar_unit_readonly.ftl">
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
			<@search_bar_unit />
			<div class="row">
				<div class="col-sm-12">
					<div id='dataGrid'></div>
				</div>
			</div>
		</div>
		<!--//内容部分-->

		<!--底部-->
		<div class="n-footer">
			<@tailer></@tailer>
		</div>
		<div id='infoWindow' style="display: none;">
				<ul class="nav nav-tabs">
				   <li id = "tt1" class="active"> <a href="\#home" data-toggle="tab">基本信息</a> </li>
				   <li  id='webInterfaceTab'><a href="#tab2" aria-controls="tab2" role="tab" data-toggle="tab">Web接口</a></li>
				</ul>
		
				<div id="myTabContent" class="tab-content">
				   <div class="tab-pane fade in active" id="home">
				      <div class="container-fluid">
						<div class="form-horizontal">
								<div class="form-group">
									<label class="col-md-2 control-label">网元名称</label>
									<div class="col-md-5">
										<input id='wyName' class="form-control input-sm" style="width:280px">
											
										</input>
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-2 control-label" for="ip">单元类型</label>
									<div class="col-md-5">
										<input id='unitType' class="form-control input-sm">
											
										</input>
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-2 control-label" for="ip">单元名称</label>
									<div class="col-md-5">
										<input id ="page-unitName" class="form-control input-sm" type="text" value=''/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-2 control-label">MML登陆协议</label>
									<div class="col-md-5">
										<select id='protocolType' class="form-control input-sm">
											
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-2 control-label" for="ip">MML接口地址</label>
									<div class="col-md-5">
										<input id ="page-ip"  class="form-control input-sm" type="text" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-2 control-label" for="ip">MML接口端口</label>
									<div class="col-md-5">
										<input id ="page-port"  class="form-control input-sm" type="text" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-2 control-label" for="ip">Root密码</label>
									<div class="col-md-5">
										<input id ="page-rootPassword"  class="form-control input-sm" type="password" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-2 control-label" for="ip">默认登录用户名</label>
									<div class="col-md-5">
										<input  id ="page-username" class="form-control input-sm" type="text" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-2 control-label" for="ip">登录密码</label>
									<div class="col-md-5">
										<input id ="page-password" class="form-control input-sm" type="password" />
									</div>
								</div>
						</div>
						<div class="k-edit-buttons k-state-default text-right windowFooter">
								<a id='saveBaseInfoBtn' class="k-button k-button-icontext k-primary k-grid-update">
									<span class="k-icon k-update"></span>保存基本信息
								</a>
								<a class="cancelBtns k-button k-button-icontext k-grid-cancel">
									<span class="k-icon k-cancel"></span>取消
								</a>
							</div>
					  </div>
				   </div>
				   <div class="tab-pane fade" id="tab2">
				      <div class="container-fluid">
							<small style="color: #ccc;">提示：点击【URL、密码、用户名称】单元格进行修改！</small>
							<br />
							<div id='webInterfaceList'></div>
							<br />
							<div class="k-edit-buttons k-state-default text-right windowFooter">
								<a id ='saveWebInterfaceBtn' class="k-button k-button-icontext k-primary k-grid-update">
									<span class="k-icon k-update"></span>保存WEB接口信息
								</a>
								<a class="cancelBtns k-button k-button-icontext k-grid-cancel">
									<span class="k-icon k-cancel"></span>取消
								</a>
							</div>
					  </div>
				   </div>
				</div>
		</div>
		<@script></@script>
		<script type="text/javascript">
			var unitlogin = false;
		</script>
		<script type="text/javascript" src="custom/app/equipment/equipment-unit.js"></script> 
	</body>

</html>