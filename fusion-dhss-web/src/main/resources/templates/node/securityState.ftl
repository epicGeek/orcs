<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../quota/quota-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<!DOCTYPE html>
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
		<div class="container-fluid" >
			<div class="row">
				<div class="col-sm-12">
					<div class="form-inline">
						<@quota/>
						<div class="form-group form-group-sm">
                            <input id="inputNeState" placeholder="请选择操作方式..." />
						</div>
						<!-- <div class="form-group form-group-sm">
                            <input id="inputUnit" value=""  placeholder="请选择节点名称..." > 
						</div> -->
						<div class="form-group form-group-sm">
                           <input type="text" id="startdatetime">
						</div> - 
						<div class="form-group form-group-sm">
                             <input type="text" id="enddatetime">
						</div>
						<button id="searchButton" class="btn btn-primary  btn-sm"> <i class="glyphicon glyphicon-search"></i> 查询 </button>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<div id = "gridNodeRecovery"></div>
				</div>
			</div>
		</div>
		<!--//内容部分-->
		
		
		
		<@tailer></@tailer>
		<!--//底部-->
		<@script></@script>
<!--//弹出信息-->
<script src="custom/app/quota/quota.js"></script>
<script src="custom/app/node/securityState.js"></script>
</body>
</html>
