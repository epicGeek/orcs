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
<body class="page-product">
		<!--顶部-->
		<!-- 用户信息 -->
		<@user_bar/>

		<!--顶部导航栏-->
		<@navigate_bar/>

		<!--内容部分-->
		<div class="container-fluid">
			<div class="row noMarginBottom">
				<div class="col-sm-2  col-xs-12">
					<input type="text" id="startTime"/>
				</div>
				<div class="col-sm-2  col-xs-12">
					<input type="text" id="endTime"/>
				</div>
				<div class="col-sm-2  col-xs-12">
					<input id="imsi" value="" type="text" class="form-control input-sm" placeholder="MSISDN号段、IMSI号段" style="width: 100%;" title="对MSISDN号段、IMSI号段进行模糊查询"/>
				</div>
				<div class="col-sm-2 col-xs-12">
					<button  id='clearsearch' class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
					<button  id='export' class="btn btn-default btn-sm"><i class="glyphicon glyphicon-export"></i>导出</button>
				</div>
			</div>
			<div class="col-sm-6 marginTop10 noPadding ">
					<div class="panel panel-default panelLighter">
						<div class="panel-heading">HSS -> BOSS</div>
						<div class="panel-body" id='instructionWrap' style="overflow-y: auto;">
							<div id='resultGrid1'></div>
						</div>
					</div>
			</div>
			<div class="col-sm-6 marginTop10">
				<div class="panel panel-default">
					<div class="panel-heading">BOSS -> HSS</div>
					<div class="panel-body">
						<div id='resultGrid2'></div>
					</div>
				</div>
			</div>

		</div>

		<!--//内容部分-->
		
		
		
		<!--底部-->
		<@tailer></@tailer>
		<!--//底部-->
		<@script></@script>
		<script src="custom/app/quota/quota.js"></script> 
		<script type="text/javascript" src="assets/plugins/echarts-2.2.5/dist/echarts-all.js"></script>
		<script src="custom/app/volte/volte-list.js"></script> 
		

</body>
</html>

