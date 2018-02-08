<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<#include "../common/ems-menu.ftl">
<!DOCTYPE html>
<html>
	<head lang="zh-cn">
		<@header></@header>
	</head>
	<body>
		<!-- 用户信息 -->
		<@user_bar/>
		<@navigate_bar/>
		<!--内容部分-->
		<div class="container-fluid" >
			<div class="row">
				<div class="col-sm-1 col-xs-12">
					<div id='addBtn' class="addWrap clearfix text-center ">
						<label><i class="glyphicon glyphicon-plus-sign " ></i> 添加</label>
						<span></span>
					</div>
				</div>
				<div class="col-sm-4  col-xs-12">
					<input id="inputKeyWord" value="" type="text" class="form-control input-sm" placeholder="筛选设备组信息" style="width: 100%;" title="对设备组信息进行模糊查询" />
				</div>
				<div class="col-sm-5 col-xs-12">
					<button  id='clearsearch'  class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<div id = "dataGrid"></div>
				</div>
			</div>
		</div>
		<!--修改弹窗-->
		<div id='editWindow' style="display: none;">
			<div class="container-fluid">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="name">设备组名称</label>
						<div class="col-md-5">
							<input id="groupName" class="form-control input-sm" type="text"
								value='#:groupName#' />
						</div>
					</div>
				</div>
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="name">搜索设备</label>
						<div class="col-md-5">
							<input id="searchEquipment" class="form-control input-sm" type="text"
								 placeholder="筛选单元信息" style="width: 100%;" title="对单元进行模糊搜索"/>
						</div>
					</div>
				</div>
				<div id='unitInfoList'> </div>
				<div class="k-edit-buttons k-state-default text-right windowFooter">
					<a id='saveBtn'
						class="k-button k-button-icontext k-primary k-grid-update"> <span
						class="k-icon k-update"></span>保存
					</a> <a id='cancelBtn' class="k-button k-button-icontext k-grid-cancel">
						<span class="k-icon k-cancel"></span>取消
					</a>
				</div>
			</div>
		</div>
		<!--修改弹窗END-->
		
		
		
		
		
		
		<!--//内容部分-->
		<!-- 尾部 -->
		<@tailer></@tailer>
		<!-- 尾部 -->
		<@script></@script>
		<script src="custom/app/ems/equipment-node-group.js"></script>
		<!--<script src="custom/app/ems/eng.js"></script>-->
	</body>
</html>