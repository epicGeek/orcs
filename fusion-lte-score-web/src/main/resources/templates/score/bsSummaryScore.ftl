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
							<div class="header">
							 <strong>基站健康度汇总</strong>
                                <div class="pull-right" id="backPage" style="display:none">
								    <a href="bsMap.html" class="btn btn-default btn-rad"><i class="fa fa-fast-backward"></i>返回 </a>
							    </div>
							</div>
							<div class="content no-padding-top no-padding-bottom">
								<div class="form-horizontal">
									<div class="form-group form-group-sm form-inline">
										<input id="types" value="${types}" type="hidden"/>
										<input id="hiddenCityCode" value="${cityCode}" type="hidden"/>
										<input id="hiddenNeCode" value="${neCode}" type="hidden"/>
                                        <input id="hiddenAreaCode" value="${areaCode}" type="hidden"/>
										<label class="control-label">基站ID</label>
										<input id="neCode" class="form-control" maxlength="10"/>

									    <label  class="control-label">开始时间</label>
										<input id='startDate' style="width: 150px;" />

										<label class="control-label">结束时间</label>
										<input id='endDate' style="width: 150px;" /> 

									</div>
									<div class="form-group form-group-sm form-inline">
										<label class="control-label">地市</label>
										<select class="form-control" id="areaCode">
										</select>

										<label class="control-label">区县</label>
										<select class="form-control" id="cityCode">
										</select>

										<label class="control-label">评分</label>
										<select id="totalScoreValue" class="form-control">
										</select>

										<label class="control-label"></label>
										<button id="searchBtn" class="btn btn-primary btn-sm btn-rad">
											<i class="fa fa-search"></i> 查询
										</button>
										<button id="resetBtn" class="btn btn-default btn-sm btn-rad">
											<i class="fa fa-repeat"></i> 重置
										</button>
									</div>
								</div>
							</div>

							<div class="content  text-center no-padding-top">
								<div class="blockquote text-left">
									<div class="btn-group" data-toggle='buttons'>
										<label class="btn btn-info">
											<input type="radio" name="options" autocomplete="off" value="bts_score_day" /> 日评分
										</label>
										<label class="btn btn-info">
											<input type="radio" name="options" autocomplete="off" value="bts_score_week"/> 周评分
										</label>
										<label class="btn btn-info">
											<input type="radio" name="options" autocomplete="off" value="bts_score_month"/> 月评分
										</label>
									</div>
								</div>

								<div id='btsSummaryList' class="table-noWrap"></div>
								<script type="text/x-kendo-template" id="template">
									<div class="toolbar">
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
		<script type="text/javascript" src="custom/app/score/bsSummaryScore.js"></script>
	</body>

</html>