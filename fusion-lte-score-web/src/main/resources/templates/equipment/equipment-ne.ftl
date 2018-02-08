<!DOCTYPE html>
<html>
<head>
<title>DHLR</title>
<head lang="zh-cn">
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
</head>
<body class="page-product">
	<div class="n-nav n-nav-bg">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#n-navbar-main-nav">
            <span class="sr-only">导航</span>
            <span class="glyphicon glyphicon-align-justify"></span>
        </button>
        <div class="n-navbar-title"><a href="welcome" class="btn n-icon n-icon-32 n-icon-color3" role="button"><i class="glyphicon glyphicon-menu-left"></i></a>
            &nbsp;平台管理/网元管理</div>
      </div>
      <div class="clearfix"> </div>
     </div>

	<div class='content-body'>
		<div class='content-right'>
			<div class='container-fluid'>
				<div class='row'>
					<div class='col-md-4 col-lg-offset-8'>
							<div class="input-group" style='margin: 15px 0px;'>
								<input type="text" id="inputSearch" class="form-control input-sm" placeholder="筛选网元名称、网元类型、物理地址" title="对网元名称进行模糊查询"> 
								<span class="input-group-addon"><span id="searchBtn" class='glyphicon glyphicon-search'></span></span>
							</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12" style="font-size: 11px">
						<div id='neList'></div><br />
					</div>
				</div>
			</div>
					
		</div>
	</div>
	<script src="custom/app/equipment/equipment-ne.js"></script> 
	<!--【绑定号码段】弹窗-->
	<div id='bindNoWindow' style="display: none;">
		<div class="container-fluid" >
			<div class='clearfix'>
				<div class='pull-left' style='width:20%;'>
					<input id="inputTypeTrigger" style='width:100%;'>
				</div>
				<div class='pull-right' style='width:79%;'>
					 <div class="input-group">
					      <input type="text" id="search-field-numberSection"  class="form-control input-sm" placeholder="对MSISDN号段、IMSI号段进行模糊查询">
					      <div id="searchNoBtn"  class="input-group-addon"><span  class='glyphicon glyphicon-search'></span></div>
					 </div>
				</div>
			</div>
			
			<br/>
			<div id='bindNoGrid' style="height: 350px;"></div>
			<script type="text/x-kendo-template" id="bindTemplate">
	             <div class="toolbar" >
	             	<a id="addAll" class='btn btn-xs btn-success'><i class='glyphicon glyphicon-plus-sign'></i> 全部加入</a>&nbsp;&nbsp;
	             	<a id="removeAll" class='btn btn-xs btn-danger'><i class='glyphicon glyphicon-trash'></i> 全部移除</a>
	       		</div>
	        </script>
			<br />
		</div>
	</div>
	<span id="popupNotification"></span>
	
	
</body>
</html>