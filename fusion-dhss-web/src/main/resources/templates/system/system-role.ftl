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
					<input type="text" id="roleName" class="form-control input-sm" placeholder="筛选角色名称、描述" style="width: 100%;" title="角色名称、描述" />
				</div>
				<div class="col-sm-6">
					<!--<button id="selectRole" type="submit" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-search"></i> 查询</button>-->
					<button id="clearInput" type="submit" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<div id='dataGrid'></div>
				</div>
			</div>
		</div>
		<!--//内容部分-->

		<!--底部-->
		<@tailer></@tailer>
		<!--//底部-->

		<script type="text/x-kendo-template" id="windowTemplate">
			<div class="container-fluid">
				<div class="form-horizontal">
				
					<div class="form-group">
						<div class="col-md-5">
							<input id="input-href" class="form-control input-sm" type="hidden" value='#:href#' />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="name">角色名称</label>
						<div class="col-md-5">
							<input  id="input-roleName" class="form-control input-sm" type="text" value='#:roleName#' />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="ip">描述</label>
						<div class="col-md-5">
							<textarea id='input-roleDesc' name="roleDesc" rows="4" class="form-control">#:roleDesc#</textarea>
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
		
		<!-- 用户成员弹窗-->
		<div id='usersWindow' style="display: none;">
			<div class="container-fluid">
				<div class="form-group">
					<div class="col-md-5">
						<input id="role-href" class="form-control input-sm" type="hidden" />
					</div>
				</div>
				<div class="form-inline">
					<div class="form-group form-group-sm ">
						<label>筛选</label>
						<input id="userNameInput" type='text' class="form-control" placeholder="登录名称、用户姓名" style="width: 300px;" />
					</div>
					<button id="userClear" type="submit" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
				</div>
				<div id='usersGrid'></div>
			</div>
		</div>
		<!-- 用户成员弹窗 end-->

		
		<!-- 指令权限弹窗-->
		<div id='indicatorWindow' style="display: none;">
			<div class="container-fluid">
				<div class="form-inline ">
					<div class="form-group form-group-sm ">
						<label>名称</label>
						<input id='commandName' type='text' class="form-control" placeholder="对名称进行模糊查询" style="width: 300px;" />
					</div>
					<button id='commandClear' type="submit" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
				</div>
				<div class="k-gridWrap">
					<button id='addAllIndicator' class="btn btn-xs btn-success">
						<i class="glyphicon glyphicon-plus-sign"></i> 全部加入
					</button>
					<button id='removeAllIndicator' class="btn btn-xs btn-danger">
						<i class="glyphicon glyphicon-trash"></i> 全部移除
					</button>
				</div>
				<div id='indicatorGrid'></div>
			</div>
		</div>
		<!-- 指令权限弹窗 end-->
		
		<!-- 地区权限弹窗-->
		<div id='areaWindow' style="display: none;">
			<div class="container-fluid">
				<div id='areaGrid'></div>
			</div>
		</div>
		<!-- 地区权限弹窗 end-->

		<!-- 网元权限弹窗-->
		<div id='netElementWindow' style="display: none;">
			<div class="container-fluid">
				<div class="form-inline ">
					<div class="form-group form-group-sm ">
						<select id='inputDhssName' class="form-control input-sm"></select>
						<select id='neTypeList' class="form-control input-sm"></select>
						<select id='inputNeTrigger' class="form-control input-sm"></select>
						<select id='unitTypeList' class="form-control input-sm"></select>
						<input id="unitNameInput" type='text' class="form-control input-sm" placeholder="单元名称"  title='单元名称'/>
					</div>
					<!-- <button id="unitBtnSelect" type="submit" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-search"></i> 查询</button>
					<button id="unitClearInput" type="submit" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button> -->
				</div>
				<div class="k-gridWrap">
					<button id='addAllNetElement' class="btn btn-xs btn-success">
						<i class="glyphicon glyphicon-plus-sign"></i> 全部加入
					</button>
					<button id='removeAllNetElement' class="btn btn-xs btn-danger">
						<i class="glyphicon glyphicon-trash"></i> 全部移除
					</button>
				</div>
				<div id='netElementGrid'></div>
			</div>
		</div>
		<!-- 网元权限弹窗 end-->
		
		<!-- 系统功能权限弹窗-->
		<div id='systemWindow' style="display: none;">
			<div class="container-fluid">
				<div class="k-gridWrap">
					<button id='addAllSystem' class="btn btn-xs btn-success">
						<i class="glyphicon glyphicon-plus-sign"></i> 全部加入
					</button>
					<button id='removeAllSystem' class="btn btn-xs btn-danger">
						<i class="glyphicon glyphicon-trash"></i> 全部移除
					</button>
				</div>
				<div id='systemGrid'></div>
			</div>
		</div>
		<!-- 系统功能权限弹窗 end-->

		<@script></@script>

		<script type="text/javascript" src="custom/app/system/system-role.js"></script>
	</body>

</html>