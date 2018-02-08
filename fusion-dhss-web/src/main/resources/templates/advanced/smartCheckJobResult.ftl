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
		<!--内容部分-->
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-12 ">
					<div class="form-inline">
						<div class="form-group form-group-sm">
							<label>巡检时间范围</label>
							<input id='startTime' type="text" /> - 
							<input id='endTime' type="text" />
						</div>
						<div class="form-group form-group-sm">
							<label>方案名称</label>
							<input id="jobName" type="text" class="form-control" />
						</div>
						<button id="btn-search" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-search"></i> 查询</button>
						<button id="btn-clear" type="submit" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>&nbsp;&nbsp;&nbsp;&nbsp;
						<button id="all-error-search" class="btn btn-danger btn-sm"><i class="glyphicon glyphicon-search"></i> 异常单元</button>
						<button  id='export' class="btn btn-default btn-sm"><i class="glyphicon glyphicon-export"></i>下载日志</button>
					</div>
					
					<div id='dataGrid'></div>
				</div>
			</div>
		</div>
		<!--//内容部分-->
		
		<!--底部-->
		<@tailer></@tailer>
	    <@script></@script>
		<script src="custom/app/advanced/intelligentInspection.js"></script>
</body>
</html>