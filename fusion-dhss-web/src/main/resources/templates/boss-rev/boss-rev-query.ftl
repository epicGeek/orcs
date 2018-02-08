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
		<!--内容部分-->
		<div class="container-fluid">
			<div class="row ">
				<div class="col-sm-12" style="width:auto">
                    <div class="form-inline">
						<div class="form-group form-group-sm">
							<input id="hlrsn" placeholder="请选择HLRSN" />
						</div>
						<div class="form-group form-group-sm" id="operationName">
							<input id="operationName" placeholder="请选择指令类型..." />
						</div>

						<div class="form-group form-group-sm">
							<label for="name">时间范围</label>
							<input id='startTime' type="text" /> -
							<input id='endTime' type="text" /> &nbsp;&nbsp;&nbsp;&nbsp;
						</div>
					</div>
                    <div class="form-inline">
                        <div class="form-group form-group-sm">
							<input id="errorType" placeholder="请选择错误类型..." />
						</div>
					  <div class="form-group form-group-sm" id="resultType">
							<input id="resultType" placeholder="请选结果类型..." />
						</div>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<div class="form-group form-group-sm">
							<label class='form-label'>IMSI / MSISDN：</label>
							<input type="text" style="height:34px;width:373px; border-radius: 4px;border: 1px solid #cccccc;" placeholder="输入imsi或msisdn,多条数据请以半角逗号隔开" id="numberSerie" >
						</div>
						 <div class="form-group form-group-sm">
							<button class="btn btn-primary btn-sm" id="searchc" onclick="searchData()"><i class="glyphicon glyphicon-search"></i> 查询</button>
              				<button class="btn btn-info btn-sm" id="exportHistoryData"><i class="glyphicon glyphicon-export"></i> 导出</button>
						</div>
					</div>
					<div id= 'bossDataGrid'></div>
					<div id= 'bossDetailDataWindow' >
						<div id = 'callbackResult'></div>
						<div id = 'soapLog'></div>
						<div id = 'errLog'></div>
					</div>
					<div id= 'errorTypeGridWindow'>
					<div id= 'errorTypeGrid'></div>
					</div>
					<div id= 'operationNameGridWindow'>
					<div id= 'operationNameGrid'></div>
					</div>
				</div>
			</div>
		</div>	
		<!--//内容部分-->
		<!--//内容部分-->
		<!-- 尾部 -->
		<@tailer></@tailer>
		<!-- 尾部 -->
		<@script></@script>
		<script src="custom/app/boss-rev/boss-rev-query-es.js"></script>   <!-- 使用3.0版本BOSS业务监控后台。如果使用2.0BOSS后台，用 boss-rev-query.js-->
	</body>
</html>