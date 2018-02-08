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
    <link rel="stylesheet" type="text/css" href="assets/plugins/layer-v2.4/layer/skin/layer.css">
    <!--自定义common style-->
    <link rel="stylesheet" href="assets/css/common.css">
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="assets/jquery/html5shiv.min.js"></script>
    <script src="assets/jquery/respond.min.js"></script>
    <![endif]-->
    <script src="assets/plugins/jquery/jquery-1.11.1.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="assets/plugins/bootstrap-3.3.0/js/bootstrap.js" type="text/javascript" charset="utf-8"></script>
    <script src="assets/plugins/layer-v2.4/layer/layer.js" type="text/javascript" charset="utf-8"></script>
    <script src="assets/js/common.js" type="text/javascript" charset="utf-8"></script>
</head>

<body>
    <nav role="navigation" class="navbar navbar-default navbar-fixed-top">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button data-target="#bs-example-navbar-collapse-1" data-toggle="collapse" class="navbar-toggle collapsed" type="button">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a href="#" class="navbar-brand">
                    <img src="assets/img/navLogo.png" alt="">
                </a>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div id="bs-example-navbar-collapse-1" class="collapse navbar-collapse">
                 <ul class="nav navbar-nav">
                    <li class="active"><a href="createLog">日志记录</a></li>
                    <li><a href="historyLog">历史记录</a></li>
               <!--      <li><a href="userManage">用户管理</a></li>
                    <li><a href="baseDataOperation">基础数据维护</a></li> -->
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">${userName}<span class="caret"></span></a>
                        <ul role="menu" class="dropdown-menu">
                            <li><a href="resetPwd">修改密码</a></li>
                            <li class="divider"></li>
                            <li><a href="logout">退出 </a></li>
                        </ul>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>
    <input id="userName" value="${userName}" type="hidden"/>
     <input id="flag" value="${flag}" type="hidden"/>
    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-4 col-sm-offset-4">
                <div class="panel panel-default">
                    <div class="panel-heading text-center"> 修改密码</div>
                    <div class="panel-body">
                        <form role="form">
                            <div class="form-group" id="form-oldPwd">
                                <label for="newPW1">输入旧密码</label>
                                <span id='errorTip1' class='errorTip'> * 密码格式不正确</span>
                                <input type="password" class="form-control" id="oldPwd"  data-toggle="tooltip" data-placement="top" title="6~18位，只能包含字符、数字和下划线">
                            </div>
                            <div class="form-group">
                                <label for="newPW1">输入新密码</label>
                                <span id='errorTip1' class='errorTip'> * 密码格式不正确</span>
                                <input type="password" class="form-control" id="newPW1"  data-toggle="tooltip" data-placement="top" title="6~18位，只能包含字符、数字和下划线">
                            </div>
                            <div class="form-group">
                                <label for="newPW2">重复新密码</label>
                                <span id='errorTip2' class='errorTip'> * 密码格式不正确</span>
                                <input type="password" class="form-control" id="newPW2">
                            </div>
                            <div class="text-center" style="height:25px;">
                                <span id='errorTip3' class='errorTip' >* 两次密码输入不一致!</span>
                            </div>
                            <button id='changePwBtn' type="button" class="btn btn-success btn-block">更新密码</button>
                            <br>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script type="text/javascript" charset="utf-8">
    $(function(){
        //激活 tooltip
        $("[data-toggle='tooltip']").tooltip();
        var flag = $('#flag').val();
        if(!window.localStorage.loginTimes && flag=='true'){
            var html = '<div id="alertMessage" style="bottom:-100px;" class="alert alert-danger customizer-alert alert-dismissible" '+
                'role="alert">'+
                '<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span>'+
                '<span class="sr-only">Close</span></button>'+
                '<strong><i class="glyphicon glyphicon-warning-sign"></i></strong> '+
                '为了您账号安全 ，请您首次登录时修改密码！！！'+
                '</div>';
                $(html).appendTo($("body")).animate({"bottom":"0px"},500);
                $('#form-oldPwd').hide();
        };
        
        var url  = "user/resetPassword";

        //检查密码输入是否正确
        if(flag=='false'){
	        $("#oldPwd").on("focusout",function(){
	            var oldPwd=$("#oldPwd").val();
	            if(!checkPassword(oldPwd)){
	                $("#errorTip1").show();
	            } else{
	                $("#errorTip1").hide();
	            }
	        });
	        url = "user/changeOwnerPassword";
        }
        
        //检查密码输入是否正确
        $("#newPW1").on("focusout",function(){
            var newPW1=$("#newPW1").val();
            if(!checkPassword(newPW1)){
                $("#errorTip1").show();
            } else{
                $("#errorTip1").hide();
            }
        });
        //检查密码输入是否正确
        $("#newPW2").on("focusout",function(){
            var newPW2=$("#newPW2").val();
            if(!checkPassword(newPW2)){
                $("#errorTip2").show();
            } else{
                $("#errorTip2").hide();
            }
        });
         //更新密码按钮
        $("#changePwBtn").on("click",function(){
            var newPW1=$("#newPW1").val();
            var newPW2=$("#newPW2").val();
            if(!checkPassword(newPW1) || !checkPassword(newPW2)){
                $("#errorTip1,#errorTip2").show();
            }  else if(newPW1 != newPW2){
                $("#errorTip3").show();
            } else {
            	$.ajax({
            	    type : "POST",
            	    dataType : 'json',
            		url : url,
            		data:{
            			"userName":$('#userName').val(),
            			"passwordNew":newPW2,
            			"passwordOld":$('#oldPwd').val()
            		},
            		success : function(data) {
            			if(flag=='true'){
           				 $("#errorTip3").hide();
    		                layer.msg('密码重置成功！');
    		                window.location.href="logout";
	           			}else{
	           				 $("#errorTip3").hide();
	           				 if(e.responseText==""){
	           				   layer.msg('密码更新成功！');
	          		               window.location.href="logout";
	           				 }else{
	     		                    layer.msg(e.responseText);
	           				 }
	           			}
            		},
            		error:function(e){
            			if(flag=='true'){
            				 $("#errorTip3").hide();
     		                layer.msg('密码重置成功！');
     		               window.location.href="logout";
            			}else{
            				 $("#errorTip3").hide();
            				 if(e.responseText==""){
            				   layer.msg('密码更新成功！');
           		               window.location.href="logout";
            				 }else{
      		                    layer.msg(e.responseText);
            				 }
            			}
            		}
            	});
            }
        });
        
    });
    </script>
</body>

</html>
