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
							${userName!string("")} <i class="fa fa-caret-down"></i>
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
									<!--<a href='#'></a>-->
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
							<div class="header"><strong>用户管理</strong></div>
							<div class="content no-padding-top no-padding-bottom">
								<div class="form-horizontal">
									<div class="form-group form-group-sm form-inline">
  
										<label class="control-label" style="margin-left: 14px;">名称</label>
										<input class="form-control" id="inputKeyWord" placeholder="登陆名称、用户姓名" maxlength="20"/>
										<label class="control-label"></label>
										<button id="searchBtn" class="btn btn-primary btn-sm btn-rad">
											<i class="fa fa-search" ></i> 查询
										</button>
										<button id="resetBtn" class="btn btn-default btn-sm btn-rad">
											<i class="fa fa-repeat"></i> 重置
										</button>
									</div>
								</div>
							</div>

							<div class="content  text-center no-padding-top">

								<div id='userGrid'></div>
								
								<script type="text/x-kendo-template" id="template">
									<div class="toolbar text-left">
										<a id='addBtn' class='btn btn-sm btn-warning'><i class='fa fa-plus-circle'></i> 添加</a>
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
						<h4 class="modal-title" id="myModalLabel">用户管理</h4>
					</div>
					<div class="modal-body">
						<div class="form-horizontal text-center">
							<div class="form-group-sm form-inline">
								<label class="control-label">登录名称</label>
								<input id='userName' type="text" class="form-control" value="" maxlength="20"/>
							</div>

							<div class="form-group-sm form-inline">
								<label class="control-label">用户姓名</label>
								<input id='realName' class="form-control" type="text" value="" maxlength="20"/>
							</div>
                            <!--
                            <div class="form-group-sm form-inline" id="pwd1">
								<label class="control-label">设置密码</label>
								<input id='onePwd' class="form-control" type="password" value=""/>
							</div>
							<div class="form-group-sm form-inline" id="pwd2">
								<label class="control-label">密码确认</label>
								<input id='twoPwd' class="form-control" type="password" value="" />
							</div>-->
                            <div class="form-group-sm form-inline">
								<label class="control-label">联系方式</label>
								<input id='mobile' class="form-control" type="text" value="" />
							</div>

                            <div class="form-group-sm form-inline">
								<label class="control-label">电子邮箱</label>
								<input id='email' class="form-control" type="text" value="" />
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
		
		<!-- Modal -->
		<div class="modal fade" id="resetPasswordModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="margin-top: 150px;">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-body text-center text-danger">
						<h3><i class="fa fa-warning"></i> 确定重置密码？</h3>
					</div>
					<div class="modal-footer">
						<button id='resetPWBtn' type="button" class="btn btn-primary">确定</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
				</div>
			</div>
		</div>
		<span id="popupNotification"></span>
		<!-- Modal End-->
		
		
		<!-- 地区弹窗-->
		<div id='usersWindow' style="display: none;">
			<div class="container-fluid">
				<div class="form-group">
					<div class="col-md-5">
						<input id="role-href" class="form-control input-sm" type="hidden" />
					</div>
				</div>
				<div class="form-inline">
					<div class="form-group form-group-sm ">
						<label>筛选</label>
						<input id="userNameInput" type='text' class="form-control" placeholder="登录名称、用户姓名" style="width: 300px;" maxlength="20"/>
					</div>
					<button id="userBtn" type="submit" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-search"></i> 查询</button>
					<button id="userClear" type="submit" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
				</div>
				<div id='areaGrid'></div>
			</div>
		</div>
		<!-- 地区弹窗 end-->

		<script type="text/javascript" src="bower_components/plugins/bootstrap/bootstrap.js"></script>
		<script type="text/javascript" src="bower_components/plugins/mCustomScrollbar/jquery.mCustomScrollbar.concat.min.js"></script>

		<!-- echarts -->
		<script type="text/javascript" src="bower_components/plugins/echarts-2.2.5/dist/echarts-all.js"></script>

		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/kendo.all.min.js"></script>
		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/cultures/kendo.culture.zh-CN.min.js"></script>
		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/messages/kendo.messages.zh-CN.min.js"></script>
		<script type="text/javascript" src="bower_components/js/nav.js"></script>
	    <script type="text/javascript" src="custom/app/common.js"></script> 
		<script type="text/javascript" src="custom/app/system/system-user.js"></script>
	</body>

</html>