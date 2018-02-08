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
				<div class="col-sm-1 col-xs-12">
					<div id='addBtn' class="addWrap clearfix text-center" >
						<label><i class="glyphicon glyphicon-plus-sign"></i> 添加</label>
						<span></span>
					</div>
				</div>
				<div class="col-sm-4  col-xs-12">
					<input id="inputKeyWord" value="" type="text" class="form-control input-sm" placeholder="MSISDN号段、IMSI号段" style="width: 100%;" title="对MSISDN号段、IMSI号段进行模糊查询"/>
				</div>
				<div class="col-sm-5 col-xs-12">
					<button  id='clearsearch' class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
				</div>
			</div>
			<div class="row ">
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
							<label class="col-md-2 control-label" for="name">MSISDN号段</label>
							<div class="col-md-5">
								<input id="page-number" class="form-control input-sm" type="text" id="name" name="name" required data-required-msg="必填" value='#if(number!=null&&number!='null'){##:number##}#' />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label" for="ip">IMSI号段</label>
							<div class="col-md-5">
								<input id="page-imsi" class="form-control input-sm" type="text" id="ip" name="ip" required data-required-msg="必填" value='#if(imsi!=null&&imsi!='null'){##:imsi##}#' />
							</div>
						</div>
						
						<!--<div class="form-group">
						    <label class="col-md-2 control-label" >所属地区</label>
						    <div class="col-md-5">
							    <select id='instructionArea'>
								
							    </select>
						     </div>
					    </div>-->
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
		<@script></@script>
		<script src="custom/app/system/number-section-manage.js"></script>
	</body>

</html>