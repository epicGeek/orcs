<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<!DOCTYPE html>
<html>

	<head lang="zh-cn">
		<title>BOSS业务实时监控</title> 
		<@header></@header>
		<myStyle>
		  
		</myStyle>
	</head>

	<body>

		<!-- 用户信息 -->
		<@user_bar/>
		<!--顶部导航栏-->
		<@navigate_bar/>
		<!--内容部分-->
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-12">
					<div class="form-inline">
						<div class="form-group form-group-sm">
							<label for="netName">网元名称</label>
							<input id="inputNeName" placeholder="请选择网元" />
						</div>
						<div class="form-group form-group-sm">
							<label for="businessType">业务类型</label>
							<input id="inputBusiness" placeholder="请选择业务..." />
						</div>
						<div class="form-group form-group-sm">
							<label for="unitType">监控周期</label>
							<input id="bossPeriod" placeholder="请选择周期..." />
						</div>
					</div>
                    <div class="form-inline">
						<div class="form-group form-group-sm">
							<label for="name">时间范围</label>
							<input id='startdatetime' type="text" /> -
							<input id='enddatetime' type="text" />
						</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<button id="btn-search" style="margin-top:-7px;" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-search"></i> 查询</button>
						<!--<button id="btn-reset" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>-->
					</div>

					<div class="panel panel-default" style="width:1320px;">
						<div class="panel-heading bg-danger">
							<span id="dhssText"></span>
							<div class="pull-right">
									<button class="btn btn-xs btn-warning" id="btn-succrate">boss成功率</button>
								<!--	<button class="btn btn-xs btn-warning" id="btn-sbzb">失败占比率</button>-->
							</div>
						</div>
						<div class="panel-body">
							<div class="defaultChart">
								<div id='charts' style="height: 430px;"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<!--提示框 -->
		<span id="popupNotification"></span>
		<!--//内容部分-->

		<!--底部-->
		<@tailer></@tailer>
		
	    <@script></@script>
		<!-- echarts -->
		<script type="text/javascript" src="assets/plugins/echarts-2.2.5/dist/echarts-all.js"></script>
		<script src="custom/app/advanced/businessMonitor.js"></script> 
		<script src="custom/app/common.js"></script>
	</body>

</html>