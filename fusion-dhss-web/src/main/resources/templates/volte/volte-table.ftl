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
				<div class="col-sm-2  col-xs-4">
					<input id="inputKeyWord" value="" type="text" style="width:100%" placeholder="文件名称" class="form-control input-sm" />
				</div>
				<div class="col-sm-2  col-xs-4">
					<input type="text" id="startTime">
				</div>
				<div class="col-sm-2  col-xs-4">
                    <input type="text" id="endTime">
				</div>
				<div class="col-sm-2  col-xs-4">
					<button id ='clearsearchalarm' class="btn btn-default"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
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
		<@script></@script>
		<script src="custom/app/volte/volte-table.js"></script>
	</body>
</html>