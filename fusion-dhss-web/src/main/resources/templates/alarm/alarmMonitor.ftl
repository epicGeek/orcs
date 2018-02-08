<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<!DOCTYPE html>
<html>
<head>
<@header/>
</head>
<body class="page-product">
<@user_bar/>
<!--顶部导航-->
<@navigate_bar/>
<input id = "param" type='hidden' value = "${inputAlarmType}">
<!--内容部分-->
	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-12 ">
				<div class="form-inline">
					<div class="form-group form-group-sm">
                   		<input id="inputNeName" value=""/>
					</div>
					<div class="form-group form-group-sm">
                   		<input id="inputAlarmType" type="text"  placeholder="请选择告警类型"/>
					</div> &nbsp;&nbsp;&nbsp;
					<div class="form-group form-group-sm">
					时间选择：
					</div>
					<div class="form-group form-group-sm">
                   		<input id="startTime" type="text"/>
					</div>
					-
					<div class="form-group form-group-sm">
                   		<input id="endTime" type="text"/>
					</div>
					<div class="form-group form-group-sm">
				   		<button id ='searchalarm' class="btn btn-primary"><i class="glyphicon glyphicon-search"></i> 查询</button>
                   		<button id ='clearsearchalarm' class="btn btn-default"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
               	    </div>
               	 <div>
               	 <div id='gridNodeRecovery'></div>
			</div>
		</div>
	</div>
<!--底部-->
<@tailer/>
<@script/>
<script src="custom/app/alarm/alarmMonitor.js"></script>
</body>
</html>