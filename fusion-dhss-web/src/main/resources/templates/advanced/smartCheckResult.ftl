<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<!DOCTYPE html>
<html>
<head>
        <@header></@header>
		<myStyle>
		   <style type="text/css">
			.alert {
	    		margin-bottom: 0px;
			}
			
			.unitred {
	    		color: #ff6137;
			}
			.diva{
			   height: 30px;
			}
		    </style>
		</myStyle>
</head>
<body>
        <!-- 用户信息 -->
		<@user_bar/>


		<!--顶部导航栏-->
		<@navigate_bar/>
		
		<input type="hidden" id="scheduleId" value="${scheduleId}">
        <input type="hidden" id="type" value="${type}">
		<!--内容部分-->
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-12 ">
					<div class="clearfix">
						<div class="pull-left">
							<div class="form-inline">
								<div class="form-group form-group-sm">
									<label id="typeName">巡检任务名称</label>
									<input id="searchName" type="text" class="form-control" style="min-width: 200px;" />
								</div>
								<button id="btn-search" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-search"></i> 查询</button>
								<button id="btn-clear" type="submit" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
							</div>
						</div>
						<div class="pull-right">
							<a href="smartCheckJobResult" class="btn btn-sm btn-link">
								返回智能巡检  
								<i class="glyphicon glyphicon-forward"></i>
							</a>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<div id='dataGrid'></div>
				</div>
			</div>
		</div>
		<!--//内容部分-->
		
		<!--底部-->
		
		<@tailer></@tailer>
		<@script></@script>
	<script src="custom/app/advanced/smartCheckResult.js"></script>
</body>
</html>