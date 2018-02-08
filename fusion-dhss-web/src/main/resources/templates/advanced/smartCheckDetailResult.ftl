<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<!DOCTYPE html> 
<html>
<head>
        <@header></@header>
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
</head>
<body>
       <!-- 用户信息 -->
		<@user_bar/>

		<!--顶部导航栏-->
		<@navigate_bar/>
		
		<input type="hidden" id="scheduleId" value="${scheduleId}">
		<input type="hidden" id="resultCode" value="${resultCode}">
        <input type="hidden" id="type" value="${type}">
        <input type="hidden" id="typeId" value="${id}">
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-12 ">
					<div class="clearfix">
						<div class="pull-left">
							<div class="form-inline">
							
								 <input id='unitType' /> 
								 <input id='unitName' />
								 <input id="startTime" type="text"  /> - 
								 <input id="endTime" type="text" />
								 <input id="searchName" type="text" class="form-control"   placeholder="任务名称" title="任务名称"/>
								 <button  id='export' class="btn btn-default btn-sm"><i class="glyphicon glyphicon-export"></i>导出</button>
								<!--<button id="btn-search" type="submit" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-search"></i> 查询</button>
								<button id="btn-clear" type="submit" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>-->
							</div>
						</div>
						<div class="pull-right">
							<a href="smartCheckJobResult" class="btn btn-sm btn-link">
								返回智能巡检 
								<i class="glyphicon glyphicon-forward"></i>
							</a> / 
							<a href="smartCheckJobResult?time=Math.random()&scheduleId=${scheduleId}&type=${type}" class="btn btn-sm btn-link">
								返回巡检任务 
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
		<script src="custom/app/advanced/intelligentInspectionDetails.js"></script>
</body>
</html>