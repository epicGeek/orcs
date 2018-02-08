<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
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
							<label for="netName">网元名称</label>
							<input id="inputNeName" placeholder="请选择网元类型" />
						</div>
						<div class="form-group form-group-sm" id="cmdtype">
							<label for="cmdTypeId">指令类型</label>
							<input id="cmdTypeId" placeholder="请选择指令类型..." />
						</div>
						<div class="form-group form-group-sm" id="bustype">
							<label for="businessType">业务类型</label>
							<input id="businessType" placeholder="请选择业务类型..." />
						</div>

						<div class="form-group form-group-sm">
							<label for="resultType">结果类型</label>
							<input id="resultType" placeholder="请选结果类型..." />
						</div>
                        <div class="form-group form-group-sm">
							<label for="errorType">错误类型</label>
							<input id="errorType" placeholder="请选择错误类型..." />
						</div>
					</div>
                    <div class="form-inline">
						<div class="form-group form-group-sm">
							<label for="name">时间范围</label>
							<input id='startDateTime' type="text" /> -
							<input id='endDateTime' type="text" /> &nbsp;&nbsp;&nbsp;&nbsp;
						</div>
						<div class="form-group form-group-sm">
							<label class='form-label'>IMSI / MSISDN：</label>
							<input type="text" style="height:34px;width:232px; border-radius: 4px;border: 1px solid #cccccc;" placeholder="输入imsi或msisdn" id="inputValue" >
						</div>
						 <div class="form-group form-group-sm">
							<button id="btn-search" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-search"></i> 查询</button>
						    <button id="btn-export" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-search"></i> 导出</button>
						</div>
					</div>

					<div id='bossList'></div>
				</div>
			</div>
		</div>
		<span id="popupNotification"></span>
        <div id="errorModal" style="display: none;">
			  <textarea  rows="20" id="context" style="width: 800px; height: 400px;resize:none"></textarea>
		</div>
		
		 <div id="textModal" style="display: none;">
			  <textarea  rows="20" id="bossContext" style="width: 800px; height: 400px;resize:none"></textarea>
		</div>
		
		<!--//内容部分-->

		<!--底部-->
		<@tailer></@tailer>
		<!--//底部-->
		
		<@script></@script>
        <script src="custom/app/advanced/businessSearch.js"></script>
        <script src="custom/app/common.js"></script> 
		
	</body>

</html>