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

		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
		<script src="bower_components/plugins/jquery/html5shiv.min.js"></script>
		<script src="bower_components/plugins/jquery/respond.min.js"></script>
		<![endif]-->
		
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
								<div id='currentSubNavList' ></div>
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
					    <div class="col-sm-6 col-md-6">
							<div class="block">
								<div class="header no-border"><strong>地市</strong>
								</div>
								<div class="content">
									<div class='form-inline'>
										<div class='form-group' >
											<label>时间范围</label>
											<input id='startDate'/> - 
											<input id='endDate'  />
										</div>
									   <div class='form-group'>
										<label>统计方式</label> 
									   <select id="scoreType" style='width:200px;'></select>
									   </div>
										<div class='form-group'>
											<button  type='button' id="searchBtn-area" class="btn btn-primary btn-sm btn-rad">
												<i class="fa fa-search" ></i> 查询
											</button>
											<!-- <button  id="resetBtn" class="btn btn-default btn-sm btn-rad">
											<i class="fa fa-repeat"></i> 重置
											</button -->
										</div>
									</div>
									<br>
									<div id='barChart' style="height: 225px;"></div>
									<span class="tipText">
										提示：点击图中柱子，可查看该地市统计图表。
									</span>
								</div>
							</div>
						</div>
						<div class="col-sm-6 col-md-6">
							<div class="block">
								<div class="header no-border"><strong>故障原因</strong></div>
										<div class="content">
											<div class='form-inline'>
												<div class='form-group' style='margin-bottom: 8px;'>
													<strong>时间区间</strong>
													<input id='startCycle' style="width: 200px;" />-<input id='endCycle' style="width: 200px;" />
												</div>
												<div class='form-group'>
													<strong>地市</strong>
													<select  id="areaCode"></select>
													<!-- <strong>区县</strong>
													<select class="form-control" id="cityCode"></select> -->
												</div>
												<div class='form-group'>
													<button  type='button' id="searchBtn-gz" class="btn btn-primary btn-sm btn-rad">
														<i class="fa fa-search" ></i> 查询
													</button>
												<!--	<button  id="resetBtn" class="btn btn-default btn-sm btn-rad">
														<i class="fa fa-repeat"></i> 重置
													</button> -->
												</div>
											</div>
											<br>
											<div id='pieChart' style="height: 240px;"></div>
									</div>
							</div>
						</div>
						<div class="col-sm-6 col-md-6">
							<div class="block">
								<div class="header no-border"><strong>最差区县前十排名</strong></div>
								<div class="content">
									<table class="no-border ">
										<thead class="no-border ">
											<tr>
												<th>排名</th>
												<th>地市</th>
												<th>区县</th>
												<th style="width: 35%;">最差占比</th>
												<th>日期</th>
											</tr>
										</thead>
										<tbody id='gridList'>
										</tbody>
									</table>
									<br />
									<span class="tipText">
										提示：点击区县，可查看基站当前评分。
									</span>
								</div>
							</div>
						</div>
						
						<div class="col-sm-6 col-md-6">
							<div class="block">
								<div class="header no-border"><strong>全省雷达图</strong>
								</div>
								<div class="content">
									<div class='form-inline'>
										<div class='form-group' >
											<label>时间范围</label>
											<input id='startTime' style="width: 150px;"/> - <input id='endTime' style="width: 150px;" />
										</div>
										<div class='form-group'>
											<button  type='button' id="searchBtn_radar" class="btn btn-primary btn-sm btn-rad">
												<i class="fa fa-search" ></i> 查询
											</button>
										</div>
									</div>
									<br>
									<div id='radarChart' style="height: 367px;"></div>
								</div>
							</div>
						</div>
						
				</div>
			</div>
		</div>

		<span id="popupNotification"></span>

		<script type="text/javascript" src="bower_components/plugins/bootstrap/bootstrap.js"></script>
		<script type="text/javascript" src="bower_components/plugins/mCustomScrollbar/jquery.mCustomScrollbar.concat.min.js"></script>
		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/kendo.all.min.js"></script>
		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/cultures/kendo.culture.zh-CN.min.js"></script>
		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/messages/kendo.messages.zh-CN.min.js"></script>
		<!-- echarts -->
		<script type="text/javascript" src="bower_components/plugins/echarts-2.2.5/dist/echarts-all.js"></script>
		<script type="text/javascript" src="bower_components/js/nav.js"></script>
		<script type="text/javascript" src="custom/app/common.js"></script> 
		<script type="text/javascript" src="custom/app/dashboard/dashboard.js"></script>
	</body>
</html>