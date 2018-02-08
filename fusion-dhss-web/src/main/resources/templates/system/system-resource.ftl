<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
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
		<div class="container-fluid" >
			<div class="row">
				<div class="col-sm-1 col-xs-12">
					<div id='addBtn' class="addWrap clearfix text-center ">
						<label><i class="glyphicon glyphicon-plus-sign " ></i> 添加</label>
						<span></span>
					</div>
				</div>
				<div class="col-sm-4  col-xs-12">
					<input id="inputKeyWord" value="" type="text" class="form-control input-sm" placeholder="筛选菜单名称" style="width: 100%;" title="对MSISDN号段、IMSI号段进行模糊查询" />
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
		<!--//内容部分-->
		<!-- 尾部 -->
		<@tailer></@tailer>
		<!-- 尾部 -->
		
			<div id='windowTemplate' style="display: none;">
			<div class="container-fluid">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="name">菜单编码</label>
						<div class="col-md-5">
							<input id="page-menuCode" class="form-control input-sm" type="text"
								value='#:menuCode#' />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="ip">菜单名称</label>
						<div class="col-md-5">
							<input id="page-menuName" class="form-control input-sm" type="text"
								value='#:menuName#' />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="ip">上级菜单名称</label>
						<div class="col-md-5">
							<input id="page-parentMenuName" class="form-control input-sm" type="text"
								value='#:parentMenuName#' />
						</div>
					</div>
				</div>
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
		
		
		<!--<div id='bindWindow' style="display: none;">
			<div class="container-fluid">
				<div class="form-inline">
					<div class="form-group form-group-sm  noMargin">
						<select id ="inputTypeTrigger" style="width:200px" class="form-control">
							<option value ="0" >全部号码段</option>
							<option value ="1" >未绑定该地区的号码段</option>
							<option value ="2" >绑定该地区的号码段</option>
						</select>
						<label>MSISDN / IMSI号段</label>
						<input id = "search-field-numberSection" type='text' class="form-control" placeholder="MSISDN、IMSI号段" />
					</div>
					<button id='windowclearsearch'  class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
				</div>
				<div class="k-gridWrap">
					<button id='addAll' class="btn btn-xs btn-success">
						<i class="glyphicon glyphicon-plus-sign"></i> 全部加入
					</button>
					<button id='removeAll' class="btn btn-xs btn-danger">
						<i class="glyphicon glyphicon-trash"></i> 全部移除
					</button>
				</div>
				<div id='bindGrid'></div>
			</div>
		</div>-->
		<@script></@script>
		<script src="custom/app/system/system-resource.js"></script>
	</body>
</html>