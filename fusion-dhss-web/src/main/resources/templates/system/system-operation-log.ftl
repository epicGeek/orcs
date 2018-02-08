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
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-12 ">
					<div class="form-inline">
						<div class="form-group form-group-sm">
							<label>时间范围</label>
							<input id='startTime' type="text" /> - 
							<input id='endTime' type="text" />
						</div>
						<div class="form-group form-group-sm">
							<label>用户名称</label>
							<input id="userName" type="text" class="form-control" />
						</div>
						<button id="btn-search" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-search"></i> 查询</button>
						<button id="btn-clear" type="submit" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
					</div>
					
					<div id='dataGrid'></div>
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
						<label class="col-md-2 control-label" for="name">地区编码</label>
						<div class="col-md-5">
							<input id="page-areaCode" class="form-control input-sm" type="text"
								value='#:areaCode#' />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" for="ip">地区名称</label>
						<div class="col-md-5">
							<input id="page-areaName" class="form-control input-sm" type="text"
								value='#:areaName#' />
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
		
		
		<div id='bindWindow' style="display: none;">
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
		</div>
		<@script></@script>
		<script src="custom/app/system/system-operation-log.js"></script>
	</body>
</html>