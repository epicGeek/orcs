<!DOCTYPE html>
<html lang="en">

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<title>LTE健康度评分系统</title>
<link rel="shortcut icon" href="custom/images/favicon.ico"
	type="image/x-icon" />
<link rel="stylesheet"
	href="bower_components/plugins/bootstrap/bootstrap.css" />
<link rel="stylesheet"
	href="bower_components/plugins/font-awesome/css/font-awesome.min.css" />
<link rel="stylesheet"
	href="bower_components/plugins/mCustomScrollbar/jquery.mCustomScrollbar.css" />

<link rel="stylesheet"
	href="bower_components/plugins/kendoui2015.1.318/styles/kendo.common.min.css" />
<link rel="stylesheet"
	href="bower_components/plugins/kendoui2015.1.318/styles/kendo.silver.min.css" />
<link rel="stylesheet" href="custom/css/theme.css" />
<link rel="stylesheet"
	href="bower_components/plugins/cplugin/cStyle.css" />

<script src="bower_components/plugins/jquery/jquery-1.11.1.min.js"></script>
<script src="bower_components/plugins/jquery/html5shiv.min.js"></script>
<script src="bower_components/plugins/jquery/respond.min.js"></script>

</head>

<body>
   <input id="menu" value="${menu}" type="hidden"/>
	<div class="navbar navbar-default navbar-fixed-top">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
				aria-expanded="false">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="welcome"> <img
				src="custom/images/logo-white.png" />
			</a>
		</div>
		<div class="collapse navbar-collapse">
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false"> ${userName!string("")} 
					<i class="fa fa-caret-down"></i>
				</a>
					<ul class="dropdown-menu">
						<li><a href="changePassword">修改密码</a></li>
						<li role="separator" class="divider"></li>
						<li><a href="logout">退出</a></li>
					</ul></li>
			</ul>

			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown currentTopNav"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown" role="button"
					aria-haspopup="true" aria-expanded="false"> <span
						id='currentSubNavName'></span> <i class="fa fa-caret-down"></i>
				</a>
					<ul class="dropdown-menu">
						<li>
							<div id='currentSubNavList'></div>
						</li>
						<li><a id="mainNavName" href="welcome" class="text-center"></a></li>
					</ul></li>
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
					<i class="fa fa-angle-left"></i> <i class="fa fa-angle-right"></i>
				</button>
			</div>
		</div>
		<div class="rightContent container-fluid">
			<div class="row ">
				<div class="col-sm-12 col-md-12">
					<div class="block">
						<div class="header">
							<strong>基站健康度明细</strong>
							<div class="pull-right">
								<input  id="returnBtn"  type="button"  value="返 回"  name="back" onClick="history.go(-1)"  class="btn btn-sm btn-default no-margin-bottom" style="display: none"></input>
							</div>
						</div>
						<div class="content no-padding-top no-padding-bottom">
							<div class="form-horizontal">
								<div class="form-group form-group-sm form-inline">
									<input id="hiddenGrade" value="${grade}" type="hidden" /> 
									<input id="tjType" value="${tjType}" type="hidden" /> 
									<input id="isWeek" value="${isWeek}" type="hidden" /> 
									<label class="control-label">基站ID</label> <input id="neCode" class="form-control" maxlength="10" />
									
									<label>&nbsp;&nbsp;评分</label> 
									<select  id="scoreLevel"></select> 
									
									<label>&nbsp;&nbsp;地市</label> 
									<select  id="areaCode"></select> 
									
									<label>&nbsp;&nbsp;区县</label> 
									<select  id="cityCode"></select> 
									<br /> 
									
									<label class="control-label">&nbsp;&nbsp;故障原因</label> 
									<input id="kpiValue"/>
										
									<label class="control-label">&nbsp;&nbsp;统计方式</label> 
									<select  id="scoreType"></select>

									<label>&nbsp;&nbsp; 时间</label>
									<input id='startDate'/>-<input id='endDate' />

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
							<div id='btsList' class="table-noWrap" style='width:100%;'></div>
							<script type="text/x-kendo-template" id="template">
									<div class="toolbar">
										<div class="pull-right">
											<a id = "btn_Export" class='btn btn-sm btn-success'><i class='fa fa-upload'></i> 导出</a>&nbsp;&nbsp;
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
	<script type="text/javascript"
		src="bower_components/plugins/bootstrap/bootstrap.js"></script>
	<script type="text/javascript"
		src="bower_components/plugins/mCustomScrollbar/jquery.mCustomScrollbar.concat.min.js"></script>

	<!-- echarts -->
	<script type="text/javascript"src="bower_components/plugins/echarts-2.2.5/dist/echarts-all.js"></script>

	<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/kendo.all.min.js"></script>
	<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/cultures/kendo.culture.zh-CN.min.js"></script>
   <script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/messages/kendo.messages.zh-CN.min.js"></script>
	<script type="text/javascript" src="bower_components/js/nav.js"></script>
	<script type="text/javascript" src="custom/app/common.js"></script>
	<script type="text/javascript" src="custom/app/score/bsCurrentScore.js"></script>
</body>

</html>