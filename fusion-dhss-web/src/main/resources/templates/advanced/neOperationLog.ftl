<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<#include "../common/navigate-bar.ftl">
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
				<div class="col-sm-12">
					<div class="form-inline">
						<div class="form-group form-group-sm">
							<!--<label for="neType">网元类型</label>-->
							<input id="neType" placeholder="请选择网元类型" />
						</div>
						<div class="form-group form-group-sm">
							<!--<label for="unitType">单元类型</label>-->
							<input id="unitType" placeholder="请选择单元类型" />
						</div>
						<div class="form-group form-group-sm">
							<label for="name">时间范围</label>
							<input id='neStratTime' type="text" /> -
							<input id='neEndTime' type="text" />
						</div>
						<div class="form-group form-group-sm">
						   <label for="unitName">查询条件</label>
                           	<input id="neSearch" type="text" style="height:29px; width:260px" placeholder="筛选网元名称、单元名称" title="对网元名称、单元名称模糊查询">
                         </div>
                         <div class="form-group form-group-sm">	
                               <button id="btn-search" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-search"></i> 查询</button>
                         </div>
					</div>
					
					<div id='neLogList'></div>
				</div>
			</div>
		</div>
        <div id="errorModal" style="display: none;">
			  <textarea  rows="20" id="context" style="width: 800px; height: 400px;resize:none"></textarea>
		</div>
		
		<!--//内容部分-->

		<!--底部-->
		<@tailer></@tailer>
	    
		
		<@script></@script>
		<script src="custom/app/advanced/neOperationLog.js"></script> 
		
	</body>

</html>