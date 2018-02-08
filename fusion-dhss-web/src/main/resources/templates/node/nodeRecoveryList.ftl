<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
﻿<!DOCTYPE html>
<html>
<head lang="zh-cn">
    <@header></@header>
</head>
<body class="page-product">
<!-- 用户信息 -->
		<@user_bar/>
		<!--顶部导航栏-->
		<@navigate_bar/>
		<!--内容部分-->
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-12">
					<div class="form-inline noPadding">
						<div class="form-group form-group-sm">
							<input id="inputNeType" placeholder="请选择网元" />
						</div>
						<div class="form-group form-group-sm">
							<input id="inputUnit"  >
						</div>
						<div class="form-group form-group-sm">
							<input type="text"  id="startdatetime">
						</div>
						<div  class="form-group form-group-sm">
							<input type="text" id="enddatetime">
						</div>
						<div class="form-group form-group-sm">
							<button id="searchButton" class="btn btn-primary btn-sm">
								<i class="glyphicon glyphicon-search"></i> 查询
							</button>
							<button  id='clearsearch' class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
						</div>
					</div>
				</div>
			</div>
			<div class="row ">
				<div class="col-sm-12">
					<div id='gridNodeRecovery'></div>
				</div>
			</div>
		</div>
		<!--//内容部分-->
		
		
		
		
		
		



	<script type="text/x-kendo-template" id="template">
    <div>
        <button class="btn btn-xs btn-primary" onclick="showDetails()">#= text #</button>
    </div>

</script>
		<@tailer></@tailer>
		<!--//底部-->
		<@script></@script>
		<script src="custom/app/node/nodeRecoveryList.js"></script>
</body>
</html>
