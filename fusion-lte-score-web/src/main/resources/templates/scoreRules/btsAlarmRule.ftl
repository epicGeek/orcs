<!DOCTYPE html>
<html lang="en">

	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<title>LTE健康度评分系统</title>
		<link rel="shortcut icon" href="custom/images/favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" href="bower_components/plugins/bootstrap/bootstrap.css" />
		<link rel="stylesheet" href="bower_components/plugins/font-awesome/css/font-awesome.min.css" />
		<link rel="stylesheet" href="bower_components/plugins/mCustomScrollbar/jquery.mCustomScrollbar.css" />
		<link rel="stylesheet" href="bower_components/plugins/kendoui2015.1.318/styles/kendo.common.min.css" />
		<link rel="stylesheet" href="bower_components/plugins/kendoui2015.1.318/styles/kendo.silver.min.css" />
		<link rel="stylesheet" href="custom/css/theme.css" />
		<link rel="stylesheet" href="bower_components/plugins/cplugin/cStyle.css" />
		<script src="bower_components/plugins/jquery/jquery-1.11.1.min.js"></script>
		<script src="bower_components/plugins/cplugin/jquery.easing.1.3.js"></script>
		<script src="bower_components/plugins/cplugin/cjs.js"></script>
	</head>

	<body>
	<input id="menu" value="${menu}" type="hidden"/>
		<div class="navbar navbar-default navbar-fixed-top">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="welcome">
					<img src="custom/images/logo-white.png" />
				</a>
			</div>
			<div class="collapse navbar-collapse">
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
							${userName!string("")}  <i class="fa fa-caret-down"></i>
						</a>
						<ul class="dropdown-menu">
							<li><a href="changePassword">修改密码</a></li>
							<li role="separator" class="divider"></li>
							<li><a href="logout">退出</a></li>
						</ul>
					</li>
				</ul>

				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown currentTopNav">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
							<span id='currentSubNavName'></span> <i class="fa fa-caret-down"></i>
						</a>
						<ul class="dropdown-menu">
							<li>
								<div id='currentSubNavList' >
								</div>
							</li>
							<li><a id="mainNavName" href="welcome" class="text-center"></a></li>
						</ul>
					</li>
				</ul>
				
			</div>
		</div>
		<div class="contentWrap">
			<div class="leftFixedMenu">
				<div class="verticalNavWrap">
					<ul id='leftNavList'>
					</ul>
				</div>
				<div class='collapseButtonWrap'>
					<button class="collapseBtn btn btn-transparent btn-sm">
						<i class="fa fa-angle-left"></i>
						<i class="fa fa-angle-right"></i>
					</button>
				</div>
			</div>
			<div class="rightContent container-fluid">
				<div class="row ">
					<div class="col-sm-12 col-md-12">
						<div class="block">
							<div class="header"><strong>基站退服规则</strong></div>
							<div class="content no-padding-top no-padding-bottom">
								<div class="form-horizontal">
									<div class="form-group form-group-sm form-inline">

										<label class="control-label">告警号</label>
										<input class="form-control" id="alarmNoSearch" maxlength="10"/>

										<label class="control-label">告警标题</label>
										<input class="form-control" id="alarmTitle" maxlength="100"/>

										<label class="control-label"></label>
										<button class="btn btn-primary btn-sm btn-rad" id="searchBtn">
											<i class="fa fa-search"></i> 查询
										</button>
										<button class="btn btn-default btn-sm btn-rad" id="resetBtn">
											<i class="fa fa-repeat"></i> 重置
										</button>
									</div>
								</div>
							</div>

							<div id="alarmRuleList" class="table-noWrap"> </div>
							<!-- <div class="content  text-center no-padding-top">
								<script type="text/x-kendo-template" id="template">
									<div class="toolbar clearfix">
										<div class="pull-left">
											<a id='addBtn' class='btn btn-sm btn-warning'><i class='fa fa-plus-circle'></i> 添加</a>
										</div>
										<div class="pull-right">
											<a id='alarmScoreBtn' class='btn btn-sm btn-success'><i class="fa fa-yelp"></i> 性能告警得分配置</a>
										</div>
									</div>
								</script>
							</div> -->
							
							</div>
						</div>
					</div>

				</div>
		</div>

		<!-- Modal保存修改弹框 -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="myModalLabel">基站性能告警规则</h4>
					</div>
					<div class="modal-body">
						<div class="form-horizontal text-center">
							<div class="form-group-sm form-inline">
								<label class="control-label">告警号</label>
								<input id='alarmNoValue' class="form-control" maxlength="10"/>
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">告警标题</label>
								<input id='alarmTitleValue' class="form-control" maxlength="100"/>
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">告警类别</label>
								<input id='alarmTypeValue' class="form-control" />
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">告警级别</label>
								<input id='alarmLevelValue' class="form-control" />
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">告警含义</label>
								<input id='alarmDescValue' class="form-control" maxlength="500"/>
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">时延高阀值</label>
								<input id='ruleOne' class="form-control" maxlength="6"/>
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">时延中阀值</label>
								<input id='ruleTwo' class="form-control" maxlength="6"/>
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">时延低阀值</label>
								<input id='ruleThree' class="form-control" maxlength="6"/>
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">频次阀值</label>
								<input id='ruleFour' class="form-control" maxlength="6"/>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button id='saveBtn' type="button" class="btn btn-primary">保存</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
				</div>
			</div>
		</div>
		<!-- Modal End-->
		
		<!-- 性能告警得分配置弹窗 Modal -->
		<div class="modal fade" id="alarmConfigModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog modal-lg" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="myModalLabel">性能告警得分配置</h4>
					</div>
					<div class="modal-body">
						<div id='alarmConfigGrid'></div>
					</div>
					<div class="modal-footer">
						<button id='saveAlarmWinBtn' type="button" class="btn btn-primary">保存</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
				</div>
			</div>
		</div>
		<!-- Modal End-->
		
		<!-- Modal性能告警得分配置修改弹框 -->
		<div class="modal fade" id="myAlarmModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="myModalLabel">性能告警得分配置</h4>
					</div>
					<div class="modal-body">
						<div class="form-horizontal text-center">
							<div class="form-group-sm form-inline">
								<label class="control-label">时延高阀值分数</label>
								<input id='scoreRuleHighInput' class="form-control" maxlength="6"/>
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">时延中阀值分数</label>
								<input id='scoreRuleInInput' class="form-control" maxlength="6"/>
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">时延低阀值分数</label>
								<input id='scoreRuleLowInput' class="form-control" maxlength="6"/>
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">频次阀值分数&nbsp;&nbsp;&nbsp;</label>
								<input id='scoreRuleNumInput' class="form-control" maxlength="6"/>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button id='alarmSaveBtn' type="button" class="btn btn-primary">保存</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
				</div>
			</div>
		</div>
		<!-- Modal End-->
		
		<span id="popupNotification"></span>
		<script type="text/javascript" src="bower_components/plugins/bootstrap/bootstrap.js"></script>
		<script type="text/javascript" src="bower_components/plugins/mCustomScrollbar/jquery.mCustomScrollbar.concat.min.js"></script>

		<!-- echarts -->
		<script type="text/javascript" src="bower_components/plugins/echarts-2.2.5/dist/echarts-all.js"></script>

		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/kendo.all.min.js"></script>
		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/cultures/kendo.culture.zh-CN.min.js"></script>
		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/messages/kendo.messages.zh-CN.min.js"></script>
		<script type="text/javascript" src="bower_components/js/nav.js"></script>
		<script type="text/javascript" src="custom/app/common.js"></script>
		<script type="text/javascript" src="custom/app/scoreRules/btsAlarmRule.js"></script>
	</body>

</html>