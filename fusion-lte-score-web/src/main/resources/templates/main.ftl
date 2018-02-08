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

		<!--自定义 style-->
		<link rel="stylesheet" href="custom/css/main.css" />

		<!--jquery style -->
		<script type="text/javascript" src="bower_components/plugins/jquery/jquery-1.11.1.min.js"></script> 

		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
		<script  src="bower_components/plugins/jquery/html5shiv.min.js"></script>
		<script src="bower_components/plugins/jquery/respond.min.js"></script>
		<![endif]-->
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
							${userName!string("")} <i class="fa fa-caret-down"></i>
						</a>
						<ul class="dropdown-menu">
							<li><a href="changePassword">修改密码</a></li>
							<li role="separator" class="divider"></li>
							<li><a href="logout">退出</a></li>
						</ul>
					</li>
				</ul>

			</div>
		</div>
		
		<div class="container">
			<div class="row">
		
				<div class="col-md-12">
					<h4 class="heading1">总体呈现</h4>
					<div class="block1">
						<div class="row">
						
							<div class="col-md-2 col-sm-4  col-xs-4 text-center">
								<a href="#" id="dashboard" class="btn n-icon n-icon-color1" role="button">
									<span class="fa-stack fa-lg">
										<i class="fa fa-dashboard fa-stack-2x"></i>
									</span>
								</a>
								<div>DASHBOARD</div>
							</div>
							 
							<div class="col-md-2 col-sm-4  col-xs-4 text-center">
								<a href="#" id="bsMap" class="btn n-icon n-icon-color2" role="button">
									<span class="fa-stack fa-lg">
										<i class="fa fa-map-marker fa-stack-2x"></i>
									</span>
								</a>
								<div>基站地图</div>
							</div>
						</div>
					</div>
			  </div>
				
			  <div class="col-md-12">
				 <h4 class="heading1">健康度评分</h4>
				 <div class="block1">
					<div class="row">
						<div class="col-md-2 col-sm-4  col-xs-4 text-center">
								<a href="#" id='bsCurrentScoreHour'   class="btn n-icon n-icon-color4" role="button">
									<span class="fa-stack fa-lg">
										<i class="fa fa-stack-1x">小时</i>
									</span>
								</a>
								<div>小时</div>
							</div>
							
							<div class="col-md-2 col-sm-4  col-xs-4 text-center">
								<a  href="#"  id="bsCurrentScoreDay" class="btn n-icon n-icon-color5" role="button">
									<span class="fa-stack fa-lg">
										<i class="fa fa-stack-1x">天</i>
									</span>
								</a>
								<div>天</div>
							</div>
							<div class="col-md-2 col-sm-4  col-xs-4 text-center">
								<a href="#"  id='bsCurrentScoreWeek'   class="btn n-icon n-icon-color3" role="button">
									<span class="fa-stack fa-lg">
										<i class="fa fa-stack-1x">周</i>
									</span>
								</a>
								<div>周</div>
							</div>
							
						</div>
					</div>
				</div>
				
				<div class="col-md-12">
				 <h4 class="heading1">工单派发</h4>
				 <div class="block1">
					<div class="row">
					
						<div class="col-md-2 col-sm-4  col-xs-4 text-center">
							<a href="btsScoreJob" class="btn n-icon n-icon-color4" role="button">
								<span class="fa-stack fa-lg">
									<i class="fa fa-stack-1x">基站健康度工单</i>
								</span>
							</a>
							<div>小时</div>
						</div>
						
						
					</div>
				</div>
			</div>
			
				
		   </div>
	  </div>
	  <script type="text/javascript" src="bower_components/plugins/bootstrap/bootstrap.js"></script>
	  <script type="text/javascript" src="bower_components/plugins/mCustomScrollbar/jquery.mCustomScrollbar.concat.min.js"></script>
	  <script type="text/javascript" src="custom/app/main.js"></script>

	</body>

</html>