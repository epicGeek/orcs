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
			<div class="row ">
				<div class="col-sm-12" style="width:auto">
                    <div class="form-inline">
						<div class="form-group form-group-sm">
							<input id="soapGwName" placeholder="请选择SOAP-GW名称" />
						</div>
						<div class="form-group form-group-sm">
							<input id="businessType" placeholder="请选择业务类型..." />
						</div>
						<div class="form-group form-group-sm">
							<input id="timePeriod" placeholder="请选择时间粒度..." />
						</div>
						 <div class="form-group form-group-sm">
							<button class="btn btn-primary btn-sm" id="searchc" onclick="queryBossKpi()"><i class="glyphicon glyphicon-search"></i> 查询</button>
						</div>
					</div>
                    <div class="form-inline">
                    	<div class="form-group form-group-sm">
							<input id='startTime' type="text" /> -
							<input id='endTime' type="text" /> 
						</div>
						
					</div>
					</div>
				</div>
			</div>
			<div class="panel-body">
							<div class="defaultChart">
								<div id='charts' style="height: 430px;"></div>
							</div>
						</div>
		</div>
		<!--//内容部分-->
		<@script></@script>
		<script src="custom/app/boss-rev/boss-rev-kpi-es.js"></script>    <!-- 基于ES的BOSS三期JS。如果不使用三期BOSS，使用 boss-rev-kpi.js -->
		<script type="text/javascript" src="assets/plugins/echarts-2.2.5/dist/echarts-all.js"></script>
		<!-- 尾部 -->
		<@tailer></@tailer>
		<!-- 尾部 -->
		
	</body>
</html>