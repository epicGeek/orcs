<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
﻿﻿<#include "./quota-bar.ftl">
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
			<div class="row">
				<div class="col-sm-12">
					<div class="form-inline">
											<@quota/>
						<div class="form-group form-group-sm">
                            <input type="text" id="startdatetimeq" />
						</div> -
						<div class="form-group form-group-sm">
                            <input type="text" id="enddatetimeq"/>
						</div>
						<button class="btn btn-primary  btn-sm" id="searchb" onclick="allquota()"><i class="glyphicon glyphicon-search"></i> 查询</button>
						<!--<button class="btn btn-primary  btn-sm" id="exportb"><i class="glyphicon glyphicon-export"></i> 导出</button>-->
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
		
		
		
		<!--底部-->
		<@tailer></@tailer>
		<!--//底部-->
		<@script></@script>
		<script src="custom/app/quota/quota.js"></script> 
		<script type="text/javascript" src="assets/plugins/echarts-2.2.5/dist/echarts-all.js"></script>
		<script src="custom/app/quota/allQuota.js"></script> 
		

</body>
</html>

