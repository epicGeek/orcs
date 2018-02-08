<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<!DOCTYPE html>
<html>
<head lang="zh-cn">
    <@header></@header>
    <style type="text/css" media="screen">
			.detailsStyle li{
				word-break: break-all;
			}
			.detailsStyle label{
				font-size:14px;
			}
		</style>
</head>
<body>
<!-- 用户信息 -->
		<@user_bar/>
		<!--顶部导航栏-->
		<div class="n-nav n-nav-bg">
			<div class="container-fluid">
				<div class="row noMarginTop noMarginBottom">
					<div class="col-md-2 col-sm-3 col-xs-10">
						<div class="clearfix">
							<div class="n-navbar-title">
									<a href="welcome" class="btn n-icon n-icon-32 n-icon-color3" role="button">
										<i class="glyphicon glyphicon-menu-left"></i>
									</a>
									&nbsp;<font id="parentMenuName"></font>
							</div>
						</div>
					</div>
					<div class="col-md-offset-1 col-md-9 col-sm-9 col-xs-14" style="float:right; _position:relative;">
						<div id="topNavListWrap" class="n-navbar-nav clearfix" >
							<div id="topNavList">
								<ul id='navList' class="nav navbar-nav">
								</ul>
							</div>
							<button id='overFlowBtn' class="btn btn-info">
								<span class="glyphicon glyphicon-align-justify"></span>
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
<!--内容部分-->
<div class="container-fluid">
			<div class="row">
				<div class="col-sm-12">
					<!-- <nav style="padding: 0px 0px 10px 0px">
	                    <ul class="nav nav-tabs">
	                        <li><a href="alarm-new">活动告警</a></li>
	                        <li class="active"><a href="alarm-new?history=history">历史告警</a></li>
	                    </ul>
	                </nav> -->
					<div class="form-inline">
						<!-- <div class="form-group form-group-sm">
							<label>选择</label>
							<input type="radio" name='group1' id='activeAlarmRadio' checked="true">
							<label for="activeAlarmRadio" style='text-align:left;'>活动告警</label>
							<input type="radio" name='group1' id='cancelAlarmRadio'>
							<label for="cancelAlarmRadio" style='text-align:left;'>取消告警</label>
						</div> -->
						<div class="form-group form-group-sm">
							<label>选择单元</label>
							<select id="alarmCell" class="form-control">
								<option value="">全部</option>
								<option value="">SHHSS50SG01-ESA40-1</option>
								<option value="">SHHSS50SG01-ESA40-0</option>
							</select>
						</div>
						<div class="form-group form-group-sm">
							<label>单元</label>
							<input type="text" id="textValue" class="form-control" placeholder='根据单元模糊查询'>
						</div>
						<div class="form-group form-group-sm">
							<label>告警ID</label>
							<input type="text" id="alarmId" class="form-control" placeholder='根据告警ID进行查询'>
						</div>
						<div class="form-group form-group-sm">
							<label>告警号</label>
							<input type="text" id="alarmNum" class="form-control" placeholder='根据告警号进行查询'>
						</div>
						<div class="form-group form-group-sm">
							<label>告警等级</label>
							<input type="text" id="alarmLevel" class="form-control" placeholder='根据告警等级进行查询'>
						</div>
						<div class="form-group form-group-sm">
							<label for="">时间范围</label>
							<input id='startTime' type="text"> - 
							<input id='endTime' type="text">
						</div>
						<div class="form-group form-group-sm">
							<button type="submit" id="search" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-search"></i> 查询</button>
							<button type="submit" id="clearbtn" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
							<button type="submit" id="export" class="btn btn-success btn-sm"><i class="glyphicon glyphicon-export"></i> 导出</button>
						</div>
					</div>
					<div id="dataGrid"></div>
				</div>
			</div>
		</div>
<!--底部-->
<@tailer></@tailer>
		
	    
<!--弹出信息-->
<div class="modal fade" id="myExportModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">设备告警导出</h4>
            </div>
            <div class="modal-body">
                表格导出成Excel文件？
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" id="doExport" class="btn btn-primary">开始</button>
            </div>
        </div>
    </div>
</div>

<div id='windowTemplate' style="display: none;">
				<textarea  style=" overflow:scroll;overflow-x:visible; height:300px;width:100%;" id = "logText">
				</textarea>
			</div>

<myScript>
<@script></@script>
<script type="text/javascript" src="../assets/kendoui2015.1.318/js/jszip.min.js"></script>
<script src="custom/app/alarm-new/alarmMonitor_history.js"></script>
</myScript>
</body>
</html>