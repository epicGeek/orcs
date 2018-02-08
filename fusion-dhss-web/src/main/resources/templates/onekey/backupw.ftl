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

	<body class="page-product">
		
		<!-- 用户信息 -->
		<@user_bar/>
		<!--顶部导航栏-->
		<@navigate_bar/>
	<!--内容部分-->
	<div class="body-content">
		
		
		
		<div class="container-fluid">
			<div class="row noMarginBottom">
				<div class="col-sm-4 marginTop10">
					<div class="panel panel-default panelLighter">
						<div class="panel-heading">一键备份&nbsp;---&nbsp;<a  href="javascript:void(0)"
								onclick="searchBackupHistory()"><i
								class="glyphicon glyphicon-calendar"></i> 查看历史</a></div>
						<div id="test" class="panel-body " style="overflow-y: scroll;">
							<div id="div_che_tree" ></div>
						</div>
					</div>
				</div>
				<div class="col-sm-4 marginTop10 noPaddingLeft">
					<div class="panel panel-default">
						<div class="panel-heading">执行步骤&nbsp;&nbsp;&nbsp;&nbsp;
							<button id='btn_checknofinished' class='btn btn-xs btn-default' onclick='javascript:reflush();'  ><i class='glyphicon glyphicon-log-out'></i>未完成任务检测</button>&nbsp;&nbsp;
                            <button id='btn_onekeybackup_id' class='btn btn-xs btn-default' onclick='javascript:doBeifen(this);'  ><i class='glyphicon glyphicon-log-out'></i> 一键备份</button>
						</div>
						<div class="panel-body">
							<div align="right"><input type="checkbox" id="isLocalhost" > 是否备份到本地服务器</div>
							<div id='stepGrid'></div>
						</div>
					</div>
				</div>
				<div class="col-sm-4 marginTop10 noPaddingLeft">
					<div class="panel panel-default">
						<div class="panel-heading">执行结果&nbsp;&nbsp;&nbsp;&nbsp;
							<button id='btn_del_monitor' onclick='javascript:delMonitor(this);' class='btn btn-xs btn-default'><i class='glyphicon glyphicon-log-out'></i> 忽略监控列表</button>
						</div>
						<div class="panel-body">
							<div id='resultGrid'></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	
	
	

</div>
	<!--弹出信息-->
	<div class="modal fade" id="myBackupModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">确认进行备份</h4>
				</div>
				<div class="modal-body">确定要开始备份吗？</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"
						onlick="doCancel()">取消</button>
					<button type="button" class="btn btn-primary" onclick="doBackUp()">开始备份</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="div_backuping" tabindex="-2" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">任务正在执行    请稍后......</div>
				<div class="modal-footer" align="center">
					<img  align="middle"  alt="执行中" src="../assets/n-images/110.gif"    width="560px" />
				</div>
			</div>
		</div>
	</div>
		<!--//弹出信息-->
	
 
		
		<@tailer></@tailer>
		
	    <@script></@script>
	    <script src="custom/app/oneKey/console.js"></script>
		<script src="custom/app/oneKey/onekeybackup.js"></script>
	 	<script src="custom/app/oneKey/yjbfutil.js"></script> 
	</body>
</html>