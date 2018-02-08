<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>日志统计系统</title>
    <meta name="description" content="User login page">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!--bootstrap style-->
    <link rel="stylesheet" type="text/css" href="assets/plugins/bootstrap-3.3.0/css/bootstrap.css">

    <!--自定义common style-->
    <link rel="stylesheet" href="assets/css/common.css">
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="assets/jquery/html5shiv.min.js"></script>
    <script src="assets/jquery/respond.min.js"></script>
    <![endif]-->
    <script src="assets/plugins/jquery/jquery-1.11.1.min.js"></script>
    <style>
    .loginWrap {
        display: table;
        width: 100%;
        height: 100%;
    }
    
    .loginContent {
        display: table-cell;
        vertical-align: middle;
        text-align: center;
    }
    
    .logo {
        margin: auto;
        max-width: 100%;
        height: auto;
    }
    
    .inputWrap {
        text-align: left;
        width: 350px;
        margin: 10px auto;
        background: #2E3238;
        padding: 10px;
        color: #fff;
        border-radius: 4px;
    }
    @media (max-width: 768px) {
        .inputWrap {
            max-width: 300px;
            width: 96%;
        }
    }
    /*表单列表*/
    input.form-control {
        background: #26292E;
        border-color: #26292E;
        color: #fff;
    }
    .form-control:focus {
        color: #fff;
    }

    .form-control::-moz-placeholder,.form-control:-ms-input-placeholder,.form-control::-webkit-input-placeholder {
        color: #fff;
    }
    </style>
</head>

<body class='loginBody'>
    <div class='loginWrap'>
        <div class='loginContent'>
                <img class="logo" src="assets/img/logo.png">
           <form action="login" method="post">
	             <div class="inputWrap">
	                <div class="form-group">
	                    <label>账号：</label>
	                    <input  id='accountInput' type="text" class="form-control input-sm" name='username' placeholder='请输入登录名'>
	                </div>
	                <div class="form-group">
	                    <label>密码：</label>
	                    <input id='pwInput' name='password' type="password" class="form-control input-sm" placeholder='请输入密码' >
	                </div>
	                <br>
	                <p id='error'></p>
	                <div class="form-group">
	                    <a id="loginBtn" class="btn btn-sm btn-success btn-block">登录</a>
	                </div>
	            </div>
            </form>
        </div>
    </div>
    <script src='assets/js/login.js' type="text/javascript" charset="utf-8"></script>
</body>

</html>
