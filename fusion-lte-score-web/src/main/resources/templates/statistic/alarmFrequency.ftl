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
		<!--jquery style-->
		<script src="bower_components/plugins/jquery/jquery-1.11.1.min.js"></script>

	</head>

	<body>
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
							<div class="header"><strong>告警次数统计</strong>
							<strong id="sumTxt"  style="float: right"></strong>
							</div>
							<div class="content no-padding-top no-padding-bottom">
								<div class="form-horizontal">
									<div class="form-group form-group-sm form-inline">

										<label class="control-label">基站ID</label>
										<input class="form-control" id="neCode" maxlength="11"/>
										
										<label class="control-label">地市</label>
										<select class="form-control" id="areaCode">
										</select>

										<label class="control-label">区县</label>
										<select class="form-control" id="cityCode">
										</select>
										<label class="control-label" >厂家</label>
										<select class="form-control" id="manufacturer"></select>
										<br/>
										<label class="control-label">告警次数</label>
										<input id='alarmFreq' style="width: 150px;" />

										<label class="control-label">统计方式</label>
										<select class="form-control" id='scoreType'>
										<!--	<option selected="selected">日</option>
											<option>周</option>
											<option>月</option> -->
										</select>
										
										<div id='dateWrap'  style='display:inline-block;'>
										<label  class="control-label">时间区间</label>
										<input id='startDate' style="width: 150px;" />-<input id='endDate' style="width: 150px;"/>
										</div>
										
										<label class="control-label"></label>
										<button class="btn btn-primary btn-sm btn-rad" id="btnSearch">
											<i class="fa fa-search"></i> 查询
										</button>
										<button class="btn btn-default btn-sm btn-rad" id="btnReset">
											<i class="fa fa-repeat"></i> 重置
										</button>
									</div>
								</div>
							</div>

							<div class="content  text-center no-padding-top">
								<div id='gridList' class="table-noWrap"></div>
								<script type="text/x-kendo-template" id="template">
									<div class="toolbar clearfix">
										<div class="pull-right">
											<a id="btn-export" class='btn btn-sm btn-success'><i class='fa fa-upload'></i> 导出</a>&nbsp;&nbsp;
										</div>
									</div>
								</script>
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>

				</div>
			</div>
		</div>

		<span id="popupNotification"></span>
		<script type="text/javascript" src="bower_components/plugins/bootstrap/bootstrap.js"></script>
		<script type="text/javascript" src="bower_components/plugins/mCustomScrollbar/jquery.mCustomScrollbar.concat.min.js"></script>

		<!-- echarts -->
		<script type="text/javascript" src="bower_components/plugins/echarts-2.2.5/dist/echarts-all.js"></script>

		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/kendo.all.min.js"></script>
		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/cultures/kendo.culture.zh-CN.min.js"></script>
		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/messages/kendo.messages.zh-CN.min.js"></script>
		<script type="text/javascript" src="bower_components/js/nav.js"></script>
		<script type="text/javascript" src="bower_components/js/dataSource.js"></script>
		<script type="text/javascript" src="custom/app/common.js"></script>
		<script type="text/javascript" src="custom/app/statistic/alarmFrequency.js"></script>
	</body>

</html>