<!DOCTYPE html>
<html>
	<head>
		<title>DHLR</title>
		 <meta content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no, width=device-width" name="viewport" />
   	     <link rel="stylesheet" href="bower_components/kendoui/styles/kendo.common.min.css" />
	     <link rel="stylesheet" href="bower_components/kendoui/styles/kendo.default.min.css" />
		 <link rel="stylesheet" href="bower_components/bootstrap-css-only/css/bootstrap.css" />
		 <link rel="stylesheet" href="bower_components/bootstrap-css-only/css/bootstrap.min.css" />
		 <link rel="stylesheet" href="custom/css/nsn-home-style.css">
         <link rel="stylesheet" href="custom/css/custom-grid-button.css">
    	
         <script src="bower_components/jquery/dist/jquery.js"></script>
 	     <script src="bower_components/kendoui/js/kendo.all.min.js"></script>
         <script src="bower_components/kendoui/js/cultures/kendo.culture.zh-CN.min.js"></script>
         <script src="bower_components/kendoui/js/messages/kendo.messages.zh-CN.min.js"></script>
         <script src="bower_components/echarts-2.2.1/dist/echarts.js"></script>
	     <style>
	       .box{
		    display: -webkit-flex; /* Safari */
		    display: flex;
	       }
	     </style>
	</head>
	<body class="page-product">
		<div class="n-nav n-nav-bg">
	      <div class="navbar-header">
	        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#n-navbar-main-nav">
	            <span class="sr-only">导航</span>
	            <span class="glyphicon glyphicon-align-justify"></span>
	        </button>
	        <div class="n-navbar-title"><a href="welcome" class="btn n-icon n-icon-32 n-icon-color3" role="button"><i class="glyphicon glyphicon-menu-left"></i></a>
	            &nbsp;平台管理/单元管理</div>
	      </div>
	      <div class="clearfix"> </div>
	     </div>
	
		<div class='content-body'>
			<div class='content-right'>
				<div class='container-fluid'>
					<div class='row' style='margin: 10px 0;'>
						<div class='col-sm-2 col-sm-offset-4' style='padding-right:0px;'>
							<input id="inputNeTrigger" style='width:100%;'>
						</div>
						<div class='col-sm-2' style='padding-right:0px;'>
							<input id="inputUnitTypeTrigger" style='width:100%;'>
						</div>
						<div class='col-sm-4'>
							<div class="input-group" >
								<input type="text" id = "inputSearch" class='form-control input-sm' aria-label="..." placeholder="筛选单元名称、MML接口地址" />
								<span class="input-group-addon"><span id="searchBtn" class='glyphicon glyphicon-search'></span></span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12" style="font-size: 11px">
							<script type="text/x-kendo-template" id="bindTemplate">
										<div class="toolbar" >
	             							<a id="addUnit" class='btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 新增单元</a>&nbsp;&nbsp;
	       								</div>
	        						</script>
							<div id='unitList'></div><br /><br />
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<script src="custom/app/equipment/equipment-unit.js"></script> 
		
		<div id='unitAddWindow' style="display: none;">
			<div class="container-fluid" >
				 <div id="tabstrip">
		             <ul>
		                 <li id="TabTitle1" class="k-state-active">基本信息</li>
		                 <li id="TabTitle2">MML接口</li>
		                 <li id="TabTitle3">Web接口</li>
		             </ul>
		             <div>
		                 <form class="form-horizontal" role="form">
							<div class="form-group form-inline">
								<input type="hidden" class="form-control" id='add-ne-id'>
								<label for="add-ne" class="col-sm-3 control-label">网元名称</label>
								<input id="add-ne">
							</div>
							<div class="form-group form-inline">
								<label for="add-unit-type" class="col-sm-3 control-label">单元类型</label>
								<input id="add-unit-type">
							</div>
							<div class="form-group form-inline">
								<label for="add-unit" class="col-sm-3 control-label">单元名称</label>
								<input type="text" class="form-control" id='add-unit' required name="add-unit">
							</div>
						</form>
		             </div>
		             <div>
		                 <form class="form-horizontal" role="form">
							<div class="form-group form-inline">
								<label for="add-protocol" class="col-sm-3 control-label">接口类型</label>
								<input id="add-protocol">
							</div>
							<div class="form-group form-inline">
								<label for="add-ip" class="col-sm-3  control-label">接口地址</label>
								<input type="text" class="form-control" id='add-ip' required name="add-ip">
							</div>
							<div class="form-group  form-inline">
								<label for="add-root-pwd" class="col-sm-3 control-label">Root密码</label>
								<input type="password" class="form-control" id='add-root-pwd' required name="add-root-pwd">
							</div>
							<div class="form-group form-inline">
								<label for="add-user" class="col-sm-3 control-label">默认登录用户名</label>
								<input type="text" class="form-control" id='add-user' required name="add-user">
							</div>
							<div class="form-group form-inline">
								<label for="add-user-pwd" class="col-sm-3 control-label">登录密码</label>
								<input type="password" class="form-control" id='add-user-pwd' required name="add-user-pwd">
							</div>
						</form>
		             </div>
		             <div>
		                 <div id="add-webtype-lumaf" class="form-horizontal" role="form">
							<div class="form-group form-inline">
								<input type="hidden" class="form-control" id='add-webtype-lumaf-id' required name="'add-webtype-lumaf-id'">
								<label for="'add-webtype-lumaf-url'" class="col-sm-3 control-label">LUMAF 接口URL</label>
								<input type="text" class="form-control form-control-long" id='add-webtype-lumaf-url' name="'add-webtype-lumaf-url'">
							</div>
							<div class="form-group form-inline">
								<label for="add-webtype-lumaf-username" class="col-sm-3 control-label">登录用户名/密码</label>
								<input type="text" class="form-control" id='add-webtype-lumaf-username' name="add-webtype-lumaf-username">
								<label class="control-label" >/</label>
								<input type="password" class="form-control" id='add-webtype-lumaf-password' name="add-webtype-lumaf-password">
							</div>
						</div>
						
						<div id="add-webtype-lemaf" class="form-horizontal" role="form">
							<div class="form-group form-inline">
								<input type="hidden" class="form-control" id='add-webtype-lemaf-id' required name="'add-webtype-lemaf-id'">
								<label for="'add-webtype-lemaf-url'" class="col-sm-3 control-label">LEMAF 接口URL</label>
								<input type="text" class="form-control form-control-long" id='add-webtype-lemaf-url' name="add-webtype-lemaf-url">
							</div>
							<div class="form-group form-inline">
								<label for="add-webtype-lemaf-username" class="col-sm-3 control-label">登录用户名/密码</label>
								<input type="text" class="form-control" id='add-webtype-lemaf-username' name="add-webtype-lemaf-username">
								<label class="control-label">/</label>
								<input type="password" class="form-control" id='add-webtype-lemaf-password' name="add-webtype-lemaf-password">
							</div>
						</div>
						
						<div id="add-webtype-tsp" class="form-horizontal" role="form">
							<div class="form-group form-inline">
								<input type="hidden" class="form-control" id='add-webtype-tsp-id' required name="'add-webtype-tsp-id'">
								<label for="add-webtype-tsp-url" class="col-sm-3 control-label">TSP 接口URL</label>
								<input type="text" class="form-control form-control-long" id='add-webtype-tsp-url' name="add-webtype-tsp-url">
							</div>
							<div class="form-group form-inline">
								<label for="add-webtype-tsp-username" class="col-sm-3 control-label">登录用户名/密码</label>
								<input type="text" class="form-control" id='add-webtype-tsp-username' name="add-webtype-tsp-username">
								<label class="control-label">/</label>
								<input type="password" class="form-control" id='add-webtype-tsp-password' name="add-webtype-tsp-password">
							</div>
						</div>
						
						<div id="add-webtype-vs" class="form-horizontal" role="form">
							<div class="form-group form-inline">
								<input type="hidden" class="form-control" id='add-webtype-vs-id' required name="'add-webtype-vs-id'">
								<label for="add-webtype-vs-url" class="col-sm-3 control-label">VC 接口URL</label>
								<input type="text" class="form-control form-control-long" id='add-webtype-vs-url' name="add-webtype-vs-url">
							</div>
							<div class="form-group form-inline">
								<label for="add-webtype-vs-username" class="col-sm-3 control-label">登录用户名/密码</label>
								<input type="text" class="form-control" id='add-webtype-vs-username' name="add-webtype-vs-username">
								<label class="control-label" >/</label>
								<input type="password" class="form-control" id='add-webtype-vs-password' name="add-webtype-vs-password">
							</div>
						</div>
						
						<div id="add-webtype-ilo" class="form-horizontal" role="form">
							<div class="form-group form-inline">
								<input type="hidden" class="form-control" id='add-webtype-ilo-id' required name="'add-webtype-ilo-id'">
								<label for="add-webtype-ilo-url" class="col-sm-3 control-label">ILO 接口URL</label>
								<input type="text" class="form-control form-control-long" id='add-webtype-ilo-url' name="add-webtype-ilo-url">
							</div> 
							<div class="form-group form-inline">
								<label for="add-webtype-ilo-username" class="col-sm-3 control-label">登录用户名/密码</label>
								<input type="text" class="form-control" id='add-webtype-ilo-username' name="add-webtype-ilo-username">
								<label class="control-label">/</label>
								<input type="password" class="form-control" id='add-webtype-ilo-password' name="add-webtype-ilo-password">
							</div>
						</div>
						
						<div id="add-webtype-oa" class="form-horizontal" role="form">
							<div class="form-group form-inline">
								<input type="hidden" class="form-control" id='add-webtype-oa-id' required name="'add-webtype-oa-id'">
								<label for="add-webtype-oa-url" class="col-sm-3 control-label">OA 接口URL</label>
								<input type="text" class="form-control form-control-long" id='add-webtype-oa-url' name="add-webtype-oa-url">
							</div>
							<div class="form-group form-inline">
								<label for="add-webtype-oa-username" class="col-sm-3 control-label">登录用户名/密码</label>
								<input type="text" class="form-control" id='add-webtype-oa-username' name="add-webtype-oa-username">
								<label class="control-label">/</label>
								<input type="password" class="form-control" id='add-webtype-oa-password' name="add-webtype-oa-password">
							</div>
						</div>
						
						<div id="add-webtype-hlr" class="form-horizontal" role="form">
							<div class="form-group form-inline">
								<input type="hidden" class="form-control" id='add-webtype-hlr-id' required name="'add-webtype-hlr-id'">
								<label for="add-webtype-hlr-url" class="col-sm-3 control-label">PGW开通（HLR） 接口URL</label>
								<input type="text" class="form-control form-control-long" id='add-webtype-hlr-url' name="add-webtype-hlr-url">
							</div>
							<div class="form-group form-inline">
								<label for="add-webtype-hlr-username" class="col-sm-3 control-label">登录用户名/密码</label>
								<input type="text" class="form-control" id='add-webtype-hlr-username' name="add-webtype-hlr-username">
								<label class="control-label">/</label>
								<input type="password" class="form-control" id='add-webtype-hlr-password' name="add-webtype-hlr-password">
							</div>
						</div>
						
						<div id="add-webtype-hss" class="form-horizontal" role="form">
							<div class="form-group form-inline">
								<input type="hidden" class="form-control" id='add-webtype-hss-id' required name="'add-webtype-hss-id'">
								<label for="add-webtype-hss-url" class="col-sm-3 control-label">PGW开通（HSS） 接口URL</label>
								<input type="text" class="form-control form-control-long" id='add-webtype-hss-url' name="add-webtype-hss-url">
							</div>
							<div class="form-group form-inline">
								<label for="add-webtype-hss-username" class="col-sm-3 control-label">登录用户名/密码</label>
								<input type="text" class="form-control" id='add-webtype-hss-username' name="add-webtype-hss-username">
								<label class="control-label">/</label>
								<input type="password" class="form-control" id='add-webtype-hss-password' name="add-webtype-hss-password">
							</div>
						</div>
						
						<div id="add-webtype-soapgw" class="form-horizontal" role="form">
							<div class="form-group form-inline">
								<input type="hidden" class="form-control" id='add-webtype-soapgw-id' required name="'add-webtype-soapgw-id'">
								<label for="add-webtype-soapgw-url" class="col-sm-3 control-label">SOAPGW开通</label>
								<input type="text" class="form-control form-control-long" id='add-webtype-soapgw-url' name="add-webtype-soapgw-url">
							</div>
							<div class="form-group form-inline">
								<label for="add-webtype-soapgw-username" class="col-sm-3 control-label">登录用户名/密码</label>
								<input type="text" class="form-control" id='add-webtype-soapgw-username' name="add-webtype-soapgw-username">
								<label class="control-label">/</label>
								<input type="password" class="form-control" id='add-webtype-soapgw-password' name="add-webtype-soapgw-password">
							</div>
						</div>
						
						<div id="add-webtype-adm" class="form-horizontal" role="form">
							<div class="form-group form-inline">
								<input type="hidden" class="form-control" id='add-webtype-adm-id' required name="'add-webtype-adm-id'">
								<label for="add-webtype-adm-url" class="col-sm-3 control-label">ADM接口URL</label>
								<input type="text" class="form-control form-control-long" id='add-webtype-adm-url' name="add-webtype-adm-url">
							</div>
							<div class="form-group form-inline">
								<label for="add-webtype-adm-username" class="col-sm-3 control-label">登录用户名/密码</label>
								<input type="text" class="form-control" id='add-webtype-adm-username' name="add-webtype-adm-username">
								<label class="control-label">/</label>
								<input type="password" class="form-control" id='add-webtype-adm-password' name="add-webtype-adm-password">
							</div>
						</div>
						
		             </div>
		         </div>
		         <br />
		         <div id ="operation" class="k-edit-buttons k-state-default text-right windowFooter">
					<a id='addBtn'  class="btn btn-danger btn-sm">
						<span class="glyphicon glyphicon-ok-sign"></span> 保存
					</a>
					<a id='cancleBtn'  class="btn btn-default  btn-sm">
						<span class="glyphicon glyphicon-remove-sign"></span> 关闭
					</a>
				</div>
		         
			</div>
		</div>
		<span id="popupNotification"></span>
		
	</body>
</html>