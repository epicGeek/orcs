<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>日志统计系统</title>
    <meta name="description" content="User login page">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!--bootstrap style-->
    <link rel="stylesheet" type="text/css" href="assets/plugins/bootstrap-3.3.0/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="assets/plugins/bootstrap-datetimepicker/bootstrap-datetimepicker.css">
    <!--弹窗 style-->
    <link rel="stylesheet" type="text/css" href="assets/plugins/layer-v2.4/layer/skin/layer.css">
    <!--下拉框 style-->
    <link rel="stylesheet" href="assets/plugins/selectize/selectize.default.css">
    <!--dataTables style-->
    <link rel="stylesheet" type="text/css" href="assets/plugins/DataTables-1.10.12/media/css/jquery.dataTables.css">
    <link rel="stylesheet" type="text/css" href="assets/plugins/DataTables-1.10.12/extensions/Responsive/css/responsive.dataTables.min.css">
    <!--自定义common style-->
    <link rel="stylesheet" href="assets/css/common.css">
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="assets/jquery/html5shiv.min.js"></script>
    <script src="assets/jquery/respond.min.js"></script>
    <![endif]-->
    <script src="assets/plugins/jquery/jquery-1.11.1.min.js" type="text/javascript" charset="utf-8"></script>
    <!--bootstrap js-->
    <script src="assets/plugins/bootstrap-3.3.0/js/bootstrap.js" type="text/javascript" charset="utf-8"></script>
    <script src="assets/plugins/moment.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="assets/plugins/bootstrap-datetimepicker/bootstrap-datetimepicker.js" type="text/javascript" charset="utf-8"></script>
    <!--弹窗 js-->
    <script src="assets/plugins/layer-v2.4/layer/layer.js" type="text/javascript" charset="utf-8"></script>
    <!--dataTables js-->
    <script src="assets/plugins/DataTables-1.10.12/media/js/jquery.dataTables.js" type="text/javascript" charset="utf-8"></script>
    <script src="assets/plugins/DataTables-1.10.12/extensions/Responsive/js/dataTables.responsive.js" type="text/javascript" charset="utf-8"></script>
    <!--下拉框 js-->
    <script src="assets/plugins/selectize/selectize.js" type="text/javascript" charset="utf-8"></script>
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
                    <li><a href="createLog.html">日志记录</a></li>
                    <li><a href="historyLog.html">历史记录</a></li>
                    <li><a href="baseDataOperation.html">基础数据维护</a></li>
                    <li class="active"><a href="userManage.html">用户管理</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">${userName}<span class="caret"></span></a>
                        <ul role="menu" class="dropdown-menu">
                            <li><a href="changePW.html">修改密码</a></li>
                            <li class="divider"></li>
                            <li><a href="login.html">退出 </a></li>
                        </ul>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container-fluid -->
    </nav>
    <input type="hidden"  id="userName"  value="${userName}">
	<input type="hidden"  id="realName"  value="${realName}">
    <div class="container">
        <div class="row">
            <div class="col-sm-12">
                <div id='listTab'>
                    <div class="panel panel-default noMB">
                        <div class="panel-heading">
                            <div class="form-inline">
                                <div class="form-group form-group-sm">
                                    <select id='selectUser' style='width:150px;padding:5px 8px;'>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <button id="queryBtn"  type="button" class="btn btn-success btn-sm">查询</button>
                                    <button id="resetBtn" type="button" class="btn btn-default btn-sm">重置</button>
                                    <button id="addBtn"  type="button" class="btn btn-success btn-sm">添加</button>
                                </div>
                            </div>
                        </div>
                        <div id='tableWrapId'  class="panel-body">
                            <table id='dataTables' class='display nowrap ' cellspacing="0" style='width: 100%;'>
                            </table>
                        </div>
                    </div>
                </div>
                <!--form list-->
				<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
								<h4 class="modal-title" id="myModalLabel">用户信息编辑</h4>
							</div>
							<div class="modal-body container-fluid">
								<div class="form-horizontal ">
									<div class="form-group-sm  row">
										<label class="control-label col-sm-4" >英文名称</label>
										<div	 class='col-sm-6'><input id='userNameEn'  class="form-control" />
											<span id='forDescTip' class='errorTip' style='display: inline-block;display: none;'>* 必填</span>
										</div>
									</div>
									<div class="form-group-sm row">
										<label class="control-label col-sm-4">中文名称</label>
										<div	 class='col-sm-6'><input id='realNameCn'  class="form-control" />
											<span id='forDesc' class='errorTip' style='display: inline-block;display: none;'>* 必填</span>
										</div>
									</div>
									<div class="form-group-sm row">
										<label class="control-label col-sm-4">电子邮箱</label>
										<div	 class='col-sm-6'><input id='email'  class="form-control" /></div>
									</div>
									<div class="form-group-sm row">
										<label class="control-label col-sm-4" >联系方式</label>
										<div	 class='col-sm-6'><input id='number' class="form-control" /></div>
									</div>
								</div>
							</div>
							<div class="modal-footer">
								<button id='saveEditBtn' type="button" class="btn btn-primary">保存</button>
								<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
							</div>
						</div>
					</div>
				</div>
                <!--//form list-->
            </div>
        </div>
    </div>
    <script src='assets/js/userManager.js'  type="text/javascript" charset="utf-8"></script>
</body>

</html>
