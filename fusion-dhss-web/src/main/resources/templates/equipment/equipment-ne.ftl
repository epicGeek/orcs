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
				<div class="col-sm-5  col-xs-12">
					<input id="inputKeyWord" value=""  type="text" class="form-control input-sm" placeholder="筛选网元名称、网元类型、物理地址" style="width: 100%;" title="筛选网元名称、网元类型 、物理地址"/>
				</div>
				<div class="col-sm-5 col-xs-12">
					<button id='neClear'  class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
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
		<div class="n-footer">
			<@tailer></@tailer>
		</div>
		<!--//底部-->
			
		<script type="text/x-kendo-template" id="windowTemplate">
	    	<div class="container-fluid">
				<div class="form-horizontal">
						<div class="form-group">
							<label class="col-md-2 control-label">上级网元</label>
							<div class="col-md-5">
								<input id="page-dhssName" class="form-control input-sm" type="text" value='#:dhssName#' />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">网元名称</label>
							<div class="col-md-5">
								<input  id="page-neName"  class="form-control input-sm" type="text" value='#:neName#' />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">网元类型</label>
							<div class="col-md-5">
								<select id='netypeInput' class="form-control input-sm">
									
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">物理地址 </label>
							<div class="col-md-5">
								<input <input  id="page-location" class="form-control input-sm" type="text" value='#:location#' />
							</div>
						</div>
				</div>
			</div>
	    	<div class="k-edit-buttons k-state-default text-right windowFooter">
				<a id='saveBtn'  class="k-button k-button-icontext k-primary k-grid-update">
					<span class="k-icon k-update"></span>保存
				</a>
				<a id='cancelBtn'  class="k-button k-button-icontext k-grid-cancel">
					<span class="k-icon k-cancel"></span>取消
				</a>
			</div>
		</script>
		<div id='infoWindow' style="display: none;"></div>
		
		<div id='bindWindow' style="display: none;">
			<div class="container-fluid">
				<div class="form-inline">
					<div class="form-group form-group-sm  noMargin">
						<select id ="inputTypeTrigger" class="form-control">
							<option value ="0" >全部号码段</option>
							<option value ="1" >未绑定该网元的号码段</option>
							<option value ="2" >绑定该网元的号码段</option>
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
		<script src="custom/app/equipment/equipment-ne.js"></script> 
	</body>

</html>