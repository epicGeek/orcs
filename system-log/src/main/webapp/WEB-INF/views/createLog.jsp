<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"   isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="description" content="User login page">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>日志统计系统</title>
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
    <script src="assets/plugins/DataTables-1.10.12/media/js/fnReloadAjax.js" type="text/javascript" charset="utf-8"></script>
    <!--下拉框 js-->
    <script src="assets/plugins/selectize/selectize.js" type="text/javascript" charset="utf-8"></script>
    <script src="assets/js/common.js" type="text/javascript" charset="utf-8"></script>
    	<style>
    		.glyphicon.glyphicon-ban-circle{
    			color:#DBAA0E !important;
    		}
    	</style>
</head>

<body>
	<nav role="navigation" class="navbar navbar-default navbar-fixed-top">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button data-target="#bs-example-navbar-collapse-1"
					data-toggle="collapse" class="navbar-toggle collapsed" type="button">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a href="#" class="navbar-brand"> <img
					src="assets/img/navLogo.png" alt="">
				</a>
			</div>
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div id="bs-example-navbar-collapse-1"
				class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li class="active"><a href="createLog">日志记录</a></li>
					<li><a href="historyLog">历史记录</a></li>
					<li><a href="baseDataOperation">基础数据维护</a></li>
					<li><a href="userManage">用户管理</a></li>
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
	</nav>
	<!-- /.container-fluid --> </nav>
	<input type="hidden"  id="userName"  value="${userName}">
	<input type="hidden"  id="realName"  value="${realName}">
	<div class="container">
		<div class="row">
			<div class="col-sm-12">
				<div id='dataTimePanel'>
					<div class="panel panel-default noMB">
						<div class="panel-heading text-center">
							请选择日志时间 <small class='pull-left littleDesc'> <i
								class='glyphicon glyphicon-ok-sign' style='color: #02B300'></i>
								表示已添加日志
							</small>
						</div>
						<div id='datePanel' class="panel-body">
							<div id="datepicker"></div>
						</div>
					</div>
				</div>
				<div id='splitePanel' class="splitePanel" style="display: none;">
					<div>
						<!--form list-->
						<div class="panel panel-default noMB">
							<div class="panel-heading text-center">
								<button id='returnBtn' class='btn btn-xs btn-danger pull-left'>
									<i class='glyphicon glyphicon-chevron-left'></i> 返回
								</button>
								${realName} - <i id='selectedDateId'></i> - 日志记录
							</div>
							<div id='formPanel' class="panel-body ">
								<div class="formGroup">
									<div class="labelControl">
										<label>产品</label>
									</div>
									<div class="tipsControl">
										<i class='glyphicon glyphicon-question-sign'
											data-toggle="tooltip" data-placement="top" title="产品名称"></i>
									</div>
									<div class="inputControl">
										<select id='productInput'></select>
									</div>
								</div>
								<div class="formGroup">
									<div class="labelControl">
										<label>模块</label>
									</div>
									<div class="tipsControl">
										<i class='glyphicon glyphicon-question-sign'
											data-toggle="tooltip" data-placement="top"
											title="产品对应的模块，选填项"></i>
									</div>
									<div class="inputControl">
										<input id='moduleInput' type="text" class="form-control"
											placeholder='产品对应的模块'>
									</div>
								</div>
								<div class="formGroup">
									<div class="labelControl">
										<label>项目</label>
									</div>
									<div class="tipsControl">
										<i class='glyphicon glyphicon-question-sign'
											data-toggle="tooltip" data-placement="top" title="项目名称"></i>
									</div>
									<div class="inputControl">
										<select id='projectInput'></select>
									</div>
								</div>
								<div class="formGroup">
									<div class="labelControl">
										<label>产品阶段</label>
									</div>
									<div class="tipsControl">
										<i class='glyphicon glyphicon-question-sign'
											data-toggle="tooltip" data-placement="top" title="产品阶段"></i>
									</div>
									<div class="inputControl">
										<select id='productPeriodInput'></select>
									</div>
								</div>
								<div class="formGroup">
									<div class="labelControl">
										<label>工作包类型</label>
									</div>
									<div class="tipsControl">
										<i class='glyphicon glyphicon-question-sign'
											data-toggle="tooltip" data-placement="top" title="工作包类型"></i>
									</div>
									<div class="inputControl">
										<select id='workTypeInput'></select>
									</div>
								</div>
								<div class="formGroup">
									<div class="labelControl">
										<label>工作包</label>
									</div>
									<div class="tipsControl">
										<i class='glyphicon glyphicon-question-sign'
											data-toggle="tooltip" data-placement="top" title="工作包"></i>
									</div>
									<div class="inputControl">
										<select id='workContentInput'></select>
									</div>
								</div>
								<div class="formGroup">
									<div class="labelControl">
										<label>工时数</label>
									</div>
									<div class="tipsControl">
										<i class='glyphicon glyphicon-question-sign'
											data-toggle="tooltip" data-placement="top"
											title="该工作记录所花费的工时,0-24小时，频率0.5小时"></i>
									</div>
									<div class="inputControl">
										<div class="numControl clearfix">
											<input id='workTimeInput' type="text" value="0.5"  maxlength="4"/>
											<div class="btns">
												<span class="addNumBtn numBtn">+</span> <span
													class='spliteLine'></span> <span
													class="reduceNumBtn numBtn">-</span>
											</div>
											<span class="unit">小时</span>
										</div>
									</div>
								</div>
								<div class="formGroup">
									<div class="labelControl">
										<label>备注</label>
									</div>
									<div class="tipsControl">
										<i class='glyphicon glyphicon-question-sign'
											data-toggle="tooltip" data-placement="top"
											title="当 [ 产品 ] 或 [ 项目 ] 至少有一个选择 [ 其他 ] 项时，字段则为必填项：填写所做工作内容"></i>
										<span id='forDescTip' class='errorTip'
											style='display: inline-block; display: none;'>* 必填</span>
									</div>
									<div class="inputControl">
										<textarea id='descInput' class="form-control"></textarea>
									</div>
								</div>
								<div class="formGroup">
									<div class="labelControl">&nbsp;</div>
									<div class="inputControl">
										<p class="text-right">
											<button id='addOneLogBtn' type='button'
												class="btn btn-success btn-sm">添加日志</button>
										</p>
									</div>
								</div>
							</div>
						</div>
						<!--//form list-->
					</div>
					<div>
						<div class="panel panel-default noMB">
							<div class="panel-heading">
								已添加日志
								<small class='littleDesc pull-right'> 
									<i class='glyphicon glyphicon-ok-circle'></i> 已保存 
									<i class='glyphicon glyphicon-ban-circle'></i> 未保存
								</small>
							</div>
							<div id='tableWrap' class="panel-body">
								<div class='text-center loading'><img src='assets/img/loading.gif' /></div>
								<table id='dataListTable' class='display nowrap'
									cellspacing="0" width="100%">
								</table>
								<div class="clearfix">
									<div class='pull-left' >
										共 <label id='totalTimes' style='color:red;'>0</label> 小时
									</div>
									<div class='pull-right'>
										<button id='removeLogsBtn' type='button'
											class="btn btn-default btn-sm">删除已选择数据</button>
										<button id='saveLogBtn' type='button'
											class="btn btn-success btn-sm">保存新数据</button>
										<button id='cancelSaveLogBtn' type='button'
											class="btn btn-default btn-sm">取消</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src='assets/js/createLog.js' type="text/javascript" charset="utf-8"></script>
</body>

</html>
