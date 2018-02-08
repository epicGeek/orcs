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
        <link rel="stylesheet" href="custom/css/theme.css" />
        <link rel="stylesheet" href="bower_components/plugins/cplugin/cStyle.css" />
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
                        <li class="active"><a href="baseDataOperation.html">基础数据维护</a></li>
                        <li><a href="userManage.html">用户管理</a></li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <li class="dropdown">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">Admin<span class="caret"></span></a>
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
        <div class="container">
            <div class="row">
                <div class="col-md-3">
                    <!--form list-->
                    <div class="panel panel-default noMB">
                        <div class="panel-heading text-center">
                            基础数据<i id='dataManage'></i> - 维护
                        </div>
                        <div id='formPanel' class="panel-body ">
                            <ul id='itemList' class="itemList">
                                <li class='active'  target="#product"><a href="javascript:void(0);" >产品</a></li>
                                <li  target="#project"><a href="javascript:void(0);">项目</a></li>
                                <li  target="#productPeriod"><a href="javascript:void(0);">产品阶段</a></li>
                                <li target="#workType"><a href="javascript:void(0);" >工作包类型</a></li>
                                <li target="#workContent"><a href="javascript:void(0);" >工作包</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-md-9">
                    <div  class="panel panel-default noMB">
                        <div class="panel-heading">
                            	<b id='selectText'>产品</b> - 维护
                            	<button id='addBtn' type='button' class="btn btn-success btn-sm" >添加</button>
                        </div>
                         <div id='tableWrap' class="panel-body" >
                          <div id='product' >
                           		<table id='dataTables' class='display nowrap ' cellspacing="0" style='width: 100%;'></table>
                            </div>
                            <div id='projects' class='hide'>
                            <div id='productPeriod' >产品阶段</div>
                            	<!-- <table id='dataTables' class='display nowrap ' cellspacing="0" style='width: 100%;'></table> -->
                            </div>
                            <div id='productPeriod' class='hide'>产品阶段</div>
                            <div id='workType' class='hide'>工作包类型</div>
                            <div id='workContent' class='hide'>工作包</div>
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
						<h4 class="modal-title" id="myModalLabel">产品</h4>
					</div>
					<div class="modal-body">
						<div class="form-horizontal text-center">
							<div class="form-group-sm form-inline">
								<label class="control-label">产品名称</label>
								<input id='productInput' class="form-control" />
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">产品版本</label>
								<input id='productVer' class="form-control" />
							</div>
							<div class="form-group-sm form-inline">
								<label class="control-label">序号</label>
								<input id='productId' class="form-control" />
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
        
        <script src='assets/js/plugin.js' type="text/javascript" charset="utf-8"></script>
        <script src='assets/js/baseDataOperation.js' type="text/javascript" charset="utf-8"></script>
         <script src='assets/js/baseCommon.js' type="text/javascript" charset="utf-8"></script>
          <script src='assets/js/baseDataOper.js' type="text/javascript" charset="utf-8"></script>
    </body>

    </html>
