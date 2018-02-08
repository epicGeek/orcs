<!DOCTYPE html>
<html>
<head lang="zh-cn">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" href="../assets/images/favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" href="../assets/plugins/bootstrap-3.3.2-dist/css/bootstrap.css" />

		<link rel="stylesheet" href="../assets/plugins/kendoui2015.1.318/styles/kendo.common.min.css" />
		<link rel="stylesheet" href="../assets/plugins/kendoui2015.1.318/styles/kendo.silver.min.css" />
		<!--easing-->
		<link rel="stylesheet" href="../assets/plugins/cplugin/cStyle.css" />
		<link rel="stylesheet" href="../assets/css/nsn-home-style.css">
		<script src="../assets/plugins/jquery/jquery-1.11.1.min.js"></script>
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
		<script src="../assets/plugins/jquery/html5shiv.min.js"></script>
		<script src="../assets/plugins/jquery/respond.min.js"></script>
		<![endif]-->
		
		<!--easing-->
		<script src="../assets/plugins/cplugin/jquery.easing.1.3.js"></script>
		<script type="text/javascript" src="../assets/plugins/cplugin/cjs.js"></script>
			
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="assets/js/html5shiv.min.js"></script>
    <script src="assets/js/respond.min.js"></script>
    <![endif]-->

    <title>倒换历史查询</title>
</head>
<body>
	<!--顶部-->
		<div class="nav navbar navbar-fixed-top navBox">
			<div class="container-fluid">
				<div class="col-md-6 col-sm-6 col-xs-12">
					<a href="welcome"><img src="../assets/n-images/nLogo2.png" class="img-responsive" /></a>
				</div>
				<div class="com-md-6 col-sm-6 col-xs-12">

					<div id='account' class="pull-right">
						<a title="修改密码">Admin</a>&nbsp;&nbsp;
						<a title="退出" href="../login.html"><i class="glyphicon glyphicon-log-out"></i></a>
					</div>
				</div>
			</div>
		</div>
		
<div class="n-nav n-nav-bg">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#n-navbar-main-nav">
            <span class="sr-only">导航</span>
            <span class="glyphicon glyphicon-align-justify"></span>
        </button>
        <div class="n-navbar-title"><a href="welcome" class="btn n-icon n-icon-32 n-icon-color3" role="button"><i class="glyphicon glyphicon-menu-left"></i></a>
            &nbsp;智能运维</div>
    </div>
    <div id="n-navbar-main-nav" class="navbar-collapse navbar-right n-navbar-nav collapse" role="navigation">
        <ul class="nav navbar-nav">
            <li><a href="allQuota">总体指标</a></li>
            <li><a href="quotaMonitor" >指标监控</a></li>
            <li><a href="oneKeyBackup">一键备份</a></li>
            <li><a href="alarmMonitor">告警监控</a></li>
            <li><a href="intelligentInspection">智能巡检</a></li>
            <li><a href="nodeSwitching" class="active">板卡倒换</a></li>
        </ul>
    </div>
    <div class="clearfix"> </div>
</div> <!--//顶部导航栏-->
	<!--内容部分-->
	<div class="body-content">
		<div class="container">
			<div class="row">
				<!-- 右侧内容  -->
				<div class="col-xs-12 content-right">
					<div class="col-md-12 right-content">

						<div class="console-title clearfix">
							<div class="pull-left">
								<h4>
									网元倒换历史查询<span class="fa fa-question-circle" popover=""
										popover-trigger="mouseenter" popover-placement="right"></span>
								</h4>
							</div>
							<div class="pull-right">
								<a class="btn btn-xs btn-primary" href="javascript:void(0)"
									onclick="toNodeSwitching()"><i
									class="glyphicon glyphicon-level-up"></i> 网元倒换</a>
							</div>
						</div>
						<div class="console-body">

							<div class="search-item">
								<span>站点：</span> <input id="inputSite" placeholder="请选择..." />
								<span>网元：</span> <input id="inputNe" placeholder="请选择..." /> 
								<span>类型：</span><input id="inputType" placeholder="请选择..." /> 
								<!--
								<span>节点名称</span><input id="inputNodeSearchName" type="text" class="form-control" style="width: 120px; display: inline" placeholder="节点名称">
								-->
								<button class="btn btn-info" onclick="doSearchBackupHistory()">查询</button>
							</div>
						</div>
						<div id="gridNodeBackupHistory"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<form id="downloadFileForm" action="nodeSwitch/downloadNodeSwitchFile">
       <input id="rowId" name="rowId" type="hidden"/>
       <input id="rowSiteName" name="rowSiteName" type="hidden"/>
       <input id="rowNeName" name="rowNeName" type="hidden"/>
       <input id="rowConfTypeName" name="rowConfTypeName" type="hidden"/>
       <input id="rowCaseName" name="rowCaseName" type="hidden"/>
   </form>
	<!--弹出信息-->
	<div id="showLogWindows" style="display: none">
		<div id="logData" style="height: 480px;"></div>
	</div>
	<!--//弹出信息-->
<script src="../assets/js/jquery-1.10.2.min.js"></script>
<script src="../assets/kendoui2015.1.318/js/kendo.all.min.js"></script>
<script src="../assets/kendoui2015.1.318/js/cultures/kendo.culture.zh-CN.min.js"></script>
<script src="../assets/kendoui2015.1.318/js/messages/kendo.messages.zh-CN.min.js"></script>
<script src="../assets/kendoui2015.1.318/js/jszip.min.js"></script>

<script src="../assets/bootstrap-3.3.2-dist/js/bootstrap.min.js"></script>
<script src="../assets/echarts-2.2.1/dist/echarts.js"></script>
<script src="../assets/js/node/nodeSwitchHistory.js"></script>
</body>
</html>
