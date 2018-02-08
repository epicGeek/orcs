<!DOCTYPE html>
<html>
	<head lang="zh-cn">
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="shortcut icon" href="custom/images/favicon.ico" type="image/x-icon" />
		<!--bootstrap style-->
		<link rel="stylesheet" href="bower_components/plugins/bootstrap/bootstrap.css"/>
		<link rel="stylesheet" href="custom/css/main.css"/>
		
		<!--自定义 style-->
		<link rel="stylesheet" href="custom/css/login.css" />
		<!--jquery style-->
		<script src="bower_components/plugins/jquery/jquery-1.11.1.min.js"></script>
		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
	    <script src="bower_components/plugins/jquery/html5shiv.min.js"></script>
	    <script src="bower_components/plugins/jquery/respond.min.js"></script>
    	<![endif]-->
		<!--bootstrap js-->
		<script src="bower_components/plugins/bootstrap/bootstrap.js"></script>

		<title>欢迎登录</title>
	</head>
	
	<body class="loginBody">
		<div class="container">
			<img class="logo" src="custom/images/logo-white.png" />
		</div>
		<div class="loginWrap">
			<div class="container">
			  <form  role="form" action="login" method="post">
				<div class="row">
					<div class="col-md-6 col-sm-6 tipImg text-center">
						<img src="custom/images/yunnan.jpg" class="img-circle"/>
					</div> 
					
					<div class="col-md-6 col-sm-6  loginContentWrap">
						<div class="loginContent">
							<div class="loginTitle">
								<h4>欢迎登陆</h4>
								<div class="dividerLine"></div>
							</div>
							<div>
								<div style="width: 80px">
									<label class="loginLabel">账&nbsp;&nbsp;&nbsp;号：</label>
								</div>
								<div>
									<input type="text" class="form-control" name="username">
								</div>
							</div>
							<div>
								<div style="width: 80px">
									<label class="loginLabel">密&nbsp;&nbsp;&nbsp;码：</label>
								</div>
								<div>
									<input type="password" class="form-control" name="password">
									<div class="checkbox">
										<label><input type="checkbox"  name="rememberMe"/>记住密码</label>
									</div>

								</div>
							</div>
							<div>
								<button type="submit" style="width: 100%;font-weight: bold;" class="btn button button-border button-glow">
										登&nbsp;&nbsp;&nbsp;录
								</button>
							</div>
						</div>
					</div>
					<div class="col-sm-12">
						<div style="color:#6F6F6F;padding: 20px 0px;font-size: 12px; text-align: center;">
							本系统推荐使用Chrome、FireFox及IE8.0以上的浏览器，IE8.0及其以下浏览器部分内容可能无法完整显示。
						</div>
					</div>
				</div>
			  </form>
			</div>
		</div>
		<!--<div style="color:#888;text-align: center;padding: 20px 0px">
			本系统推荐使用Chrome、FireFox及IE8.0以上的浏览器，IE8.0及其以下浏览器部分内容可能无法完整显示。
		</div>
		<footer class="text-center mainFooter">
			The system developed by Nokia
		</footer>-->

	</body>
</html>

