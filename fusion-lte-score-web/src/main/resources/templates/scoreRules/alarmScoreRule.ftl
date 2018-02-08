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
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
		<script src="bower_components/plugins/jquery/html5shiv.min.js"></script>
		<script src="bower_components/plugins/jquery/respond.min.js"></script>
		<![endif]-->
		<script src="bower_components/plugins/cplugin/jquery.easing.1.3.js"></script>
		<script src="bower_components/plugins/cplugin/cjs.js"></script>
		<style type="text/css">
			.modal-content .control-label{
				min-width: 140px;
			}
		</style>
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
			<div class="leftFixedMenu leftCollapsed">
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
							<div class="header"><strong>告警评分规则</strong></div>
							<div class="content  no-padding-bottom">
								<div class="form-horizontal">
									<div class="form-group form-group-sm form-inline">

										<label class="control-label">告警号</label>
										<input class="form-control" id="alarmNoInput" maxlength="20"/>

										<label class="control-label">告警名称</label>
										<input class="form-control" id="alarmNameInput" maxlength="100"/>

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

					<!--		<div class="content  text-center no-padding-top">
								<div class="alert alert-white text-center ">
									<div class="row no-margin">
										<div class="col-sm-3">
											<button class="btn btn-default" style="margin-top: 5px;" id="alarmDownFile">下载导入模板</button>
										</div>
										<div class="col-sm-9">
											<input type="file" name="file" id="upload" class="wrap1" />
											<script id="fileTemplate" type="text/x-kendo-template">
												<span class='k-progress'></span>
												<div>
													<p>文件名： #=name#</p>
													<p>大小： #=size# bytes</p>
													<p>类型： #=files[0].extension#</p>
													<button type='button' class='k-upload-action' style='position: absolute; top: 0; right: 0;'></button>
												</div>
											</script>
										</div>
									</div>
								</div> -->
								
							<div class="content  text-center no-padding-top">
								<div id="alarmList" class="table-noWrap"> </div>
							</div> 
								
							<! -- <div id='gridList' class="table-noWrap"></div -->
							
							<script type="text/x-kendo-template" id="template">
									<div class="toolbar clearfix">
										<div class="pull-right">
											<a class='btn btn-sm btn-success' id="alarmExport"><i class='fa fa-upload'></i> 导出</a>&nbsp;&nbsp;
										</div>
									</div>
							</script>
						</div>
					</div>
				</div>

				</div>
			</div>
		</div>

		<!-- Modal -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="myModalLabel">告警评分规则</h4>
					</div>
					<div class="modal-body">
						<div class="form-horizontal text-center">
							<div class="form-group-sm form-inline">
								<label class="control-label">告警号</label>
								<input id='alarmNoValue' class="form-control" maxlength="20"/>
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">告警名称</label>
								<input id='alarmNameValue' class="form-control" maxlength="100"/>
							</div>
						<!--	<div class="form-group-sm form-inline">
								<label class="control-label">告警ID</label>
								<input id='alarmIdValue' class="form-control" />
							</div>  -->
							<div class="form-group-sm form-inline">
								<label class="control-label">扣除分数</label>
								<select id="activeValue" class="form-control"></select>
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">处理方法</label>
								<!-- <input id='cancelledRule' class="form-control" /> -->
								<textarea id='cancelledRule' class="form-control" style="height: 60px;"></textarea>
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">告警类别</label>
								<select id="alarmTypeValue" class="form-control"></select>
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">告警故障</label>
								<textarea id='alarmDescVaule' class="form-control" style="height: 60px;"></textarea>
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">处理方式</label>
								<select id="alarmHandleValue" class="form-control"></select>
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">告警级别</label>
								<select id="alarmLevelValue" class="form-control"></select>
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
		<span id="popupNotification"></span>
		<!-- Modal End-->

		<script type="text/javascript" src="bower_components/plugins/bootstrap/bootstrap.js"></script>
		<script type="text/javascript" src="bower_components/plugins/mCustomScrollbar/jquery.mCustomScrollbar.concat.min.js"></script>

		<!-- echarts -->
		<script type="text/javascript" src="bower_components/plugins/echarts-2.2.5/dist/echarts-all.js"></script>

		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/kendo.all.min.js"></script>
		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/cultures/kendo.culture.zh-CN.min.js"></script>
		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/messages/kendo.messages.zh-CN.min.js"></script>
		<script type="text/javascript" src="bower_components/js/nav.js"></script>
		 <script type="text/javascript" src="custom/app/common.js"></script> 
		<script type="text/javascript" src="custom/app/scoreRules/alarmScoreRule.js"></script>
	</body>

</html>