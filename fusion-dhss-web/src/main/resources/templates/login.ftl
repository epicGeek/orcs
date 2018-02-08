<!DOCTYPE html>
<html>

	<head lang="zh-cn">
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">
		<title>HSS智能运维平台</title>
		<link rel="shortcut icon" href="assets/n-images/favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" href="assets/plugins/bootstrap-3.3.2-dist/css/bootstrap.min.css">
		<link rel="stylesheet" href="assets/css/nsn-home-style.css">
	</head>

	<body class="bodyBg">
		<!--顶部-->

		<!--内容部分-->
		<div class="n-main-login container">
			<div style="margin: 80px 0px 0px;">
				<div class="nLogoBig text-center"><img src="assets/n-images/nLogoBig2.png" align="absmiddle" /></div>

				<div class="row" >
				  <form action="login" method="POST">
					<div class="form-horizontal col-xs-6 col-xs-offset-3  col-sm-4 col-sm-offset-4" >
						<div class="form-group" style="height: 46px;line-height: 46px;margin-bottom:0px;">
							<div class="input-group text-center" style='width:100%;'>
								<b id='error' style="color: orange;font-size: 14px;"></b>
							</div>
						</div>
						<div class="form-group">
							<div class="input-group">
								<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
								<input type="text" class="form-control noMargin" name="username" placeholder="请输入用户名">
							</div>

						</div>
						<div class="form-group">
							<div class="input-group">
								<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
								<input type="password" class="form-control noMargin" name="password" placeholder="请输入密码">
							</div>

						</div>

						<div class="form-group">
							<div class="col-sm-offset-1 col-sm-10">
								<div class="checkbox" style="color: #f7f7f7">
									<label>
										<input type="checkbox" name="rememberMe"> 记住密码
									</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-offset-3 col-sm-8">
								<button type="submit" class="btn btn-warning" style="width: 120px;">登录</button>
							</div>
						</div>
					</div>
                   </form>
				</div>
			</div>
		</div>

		<!--底部-->
		<div class="n-footer">
			<div class="container">
				<div class="footer-bottom">
					<div class="copy-right">
						<p>The system developed by Nokia&nbsp;</p>
					</div>
					<div class="clearfix"> </div>
				</div>
			</div>
		</div>
		<!--//底部-->
		<script src="assets/plugins/jquery/jquery-1.11.1.min.js"></script>
		<script src="assets/plugins/bootstrap-3.3.2-dist/js/bootstrap.min.js"></script>
	</body>
</html>