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
		
		<link rel="stylesheet" href="bower_components/plugins/leaflet-0.7.5/dist/leaflet.css" />
		<link rel="stylesheet" href="bower_components/plugins/leaflet-0.7.5/Leaflet.markercluster/MarkerCluster.css" />
		<link rel="stylesheet" href="bower_components/plugins/leaflet-0.7.5/Leaflet.markercluster/MarkerCluster.Default.css" />
		
		<link rel="stylesheet" href="custom/css/theme.css" />
		
		<style>
			.block .header{
				padding: 3px 10px;
			}
			.k-grid td{
				padding-top: 0px;
				padding-bottom: 0px;
			}
			.k-grid-content img{
				height: 35px;
			}
			.bsNameBtn{
				text-decoration: underline;
				cursor: pointer;
			}
			
			/*基站地图*/
			.marginTop5{
				margin-top: 5px;
			}
			.bg-lightGreen{
				background-color: #DFF0D8 !important;
			}
			#mapBox{
				background: #EBF1FB;
				height: 520px;
			}
			#mapQueryBox{
				border: 1px solid #ddd;
				min-height: 400px;
			}
			#resultWrap{
				height: 368px;
				margin-left: -12px;
				margin-right: -12px;
			}
			#resultWrap li{
				line-height: 28px;
				font-size: 12px;
				color: #777;
				padding-left: 12px;
				cursor: pointer;
				list-style: circle inside url('custom/images/location-gray.png');
			}
			#resultWrap li:hover{
				background: #EBF1FB;
				color: #0066CC;
				list-style: circle inside url('custom/images/location-blue.png');
			}
			#resultWrap li.active{
				background: #EBF1FB;
				color:#0066CC;
				list-style: circle inside url('custom/images/location-blue.png');
			}
			.leaflet-marker-icon.leaflet-div-icon{
				border:0px;
				background: none;
				position: absolute;
			}
			.leaflet-marker-icon img{
				position: absolute;
				top:-24px;
				left:-24px;
				z-index:1000;
			}
			/*图层控制*/
			#mapControl{ position: absolute; top: 47px; z-index: 1000;right:10px; background: #fff; 
				width: 34px;  height:34px;text-align:center; border-radius: 3px;
				box-shadow: 0px 1px 2px 1px rgba(0,0,0,.3); padding: 2px 3px;
			}
			#mapControl img{
				margin-top: 2px;
			}
			#mapControl:hover ul{
				display: block;
			}
			#mapControl ul{
				display: none;
				position: absolute;
				top: -1px;
				right: -2px;
				border-radius: 5px;
				width: 150px;
				background: #fff;
				box-shadow: 0px 1px 2px 1px rgba(0,0,0,.3); 
				padding: 0px 0px 8px;
			}
			#mapControl li {
				height: 34px;
				line-height: 34px;
				font-size: 12px;
			}
			#mapControl li:nth-child(1){
				border-bottom: 1px solid #ddd;
				background: #F4F4F4;
				border-radius: 5px;
			}
			#mapControl li input{
				vertical-align: middle;
				margin: 0px;
			}
			#mapControl li img{
				height: 32px;
			}
			
			.leaflet-marker-icon.highlightIcon{
				position: absolute;
				z-index:1001 !important;
				width:8px;
				height:8px;
				background: #EF0A00;
				border:2px solid #fff;
				border-radius: 50%;
				-webkit-animation: highlightIconAni 1s ease-in infinite;
				-moz-animation: highlightIconAni 1s ease-in infinite;
				-o-animation: highlightIconAni 1s ease-in infinite;
				animation: highlightIconAni 1s ease-in infinite;
			}
			@-webkit-keyframes highlightIconAni{
				from{box-shadow: 0px 0px 1px 1px rgba(239,10,0,.1);}
				to{box-shadow: 0px 0px 4px 10px rgba(0,0,0,.1), 0px 0px 13px 15px rgba(239,10,0,.6);}
			}
			@keyframes highlightIconAni{
				from{box-shadow: 0px 0px 1px 1px rgba(0,0,0,.1);}
				to{box-shadow: 0px 0px 4px 10px rgba(0,0,0,.1), 0px 0px 13px 15px rgba(239,10,0,.6);}
			}
			#loading{
				position:absolute;
				top: 50%;
				left: 50%;
				margin-top: -20px;
				margin-left: -60px;
				z-index:10000;
			}
		</style>
	</head>

	<body>
		<input id="menu" value="${menu}" type="hidden"/>
		<input id="types" value="" type="hidden"/>
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

				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown currentTopNav">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
							<span id='currentSubNavName'></span> <i class="fa fa-caret-down"></i>
						</a>
						<ul class="dropdown-menu">
							<li>
								<div id='currentSubNavList'>
									<!--<a href='#'>嘟嘟</a>-->
								</div>
							</li>
							<li>
								<a id="mainNavName" href="welcome" class="text-center"></a>
							</li>
						</ul>
					</li>
				</ul>

			</div>
		</div>
		<div class="contentWrap">
			<div class="leftFixedMenu leftCollapsed">
				<div class="verticalNavWrap">
					<ul id='leftNavList'>
					</ul>
				</div>
				<div class='collapseButtonWrap'>
					<button class="collapseBtn btn btn-transparent btn-sm">
						<i class="fa fa-angle-left"></i>
						<i class="fa fa-angle-right"></i>
					</button>
				</div>
			</div>
			<div class=" container-fluid rightContent no-padding-bottom no-padding-top">
				<div class="row marginTop5">
					<div class="col-sm-3 col-md-3  no-padding">
						<div class="block">
							<div class="header bg-lightGreen"><strong>基站查询</strong></div>
							<div class="content" style="border-right: 1px solid #ccc;">
								<div class="row">
									<div class="col-sm-3 col-xs-6 text-right">
										<p class="marginTop5">地市</p>
									</div>
									<div class="col-sm-7 col-xs-6  no-padding-left">
										<select  id="areaCode"></select>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-3  col-xs-6 text-right">
										<p class="marginTop5">区县</p>
									</div>
									<div class="col-sm-7  col-xs-6 no-padding-left">
										<select  id="cityCode"></select>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-4 col-xs-6 text-right">
										<p class="marginTop5">开始时间</p>
									</div>
									<div class="col-sm-7 col-xs-6  no-padding-left">
											<input id='startDate' style="width: 150px;"/>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-4 col-xs-6 text-right">
										<p class="marginTop5">结束时间</p>
									</div>
									<div class="col-sm-7 col-xs-6  no-padding-left">
											<input id='endDate' style="width: 150px;" />
									</div>
								</div>
								<div class="row">
									<div class="col-sm-3  col-xs-6 text-right">
										<p class="marginTop5">关键字</p>
									</div>
									<div class="col-sm-7  col-xs-6 no-padding-left">
										<input class="form-control input-sm" id="searchIndex" maxlength="20" style="width:175px"/>
									</div>
									<div class="col-sm-2 col-xs-12 no-padding">
										<button class="btn btn-sm btn-default" id="searchBtn">
											查询
										</button>
									</div>
								</div>
								<hr style="margin: 8px -20px;border-top: 1px solid #ddd;" />
								<div id='resultWrap'>
									<ul></ul>
								</div>
							</div>
						</div>
					</div>
					<div class="col-sm-9 col-md-9 no-padding">
						<div class="block">
							<div class="header clearfix">
								<strong class="pull-left">基站分布情况</strong>
								<div id='loading'>
									<img  src='custom/images/loading.gif' />
								</div>
								<div id='mapControl' class="pull-right">
									<img src="bower_components/plugins/leaflet-0.7.5/dist/images/layers.png"/>
									<ul>
										<li>
											<label>
												图层控制
											</label>
										</li>
										<li>
											<label>
												<input type="checkbox" name="controlInput" imgClass='star1' value="1" checked="checked"/>
												<img src="custom/images/bsRed.gif"/> 4G 一星级站
											</label>
										</li>
										<li>
											<label>
												<input type="checkbox" name="controlInput" imgClass='star2' value="2" />
												<img src="custom/images/bsOrange.gif"/> 4G 二星级站
											</label>
										</li>
										<li>
											<label>
												<input type="checkbox" name="controlInput" imgClass='star3' value="3" />
												<img src="custom/images/bsYellow.gif"/> 4G 三星级站
											</label>
										</li>
										<li>
											<label>
												<input type="checkbox" name="controlInput" imgClass='star4' value="4"/>
												<img src="custom/images/bsBlue.gif"/> 4G 四星级站
											</label>
										</li>
										<li>
											<label>
												<input type="checkbox" name="controlInput" imgClass='star5' value="5" />
												<img src="custom/images/bsGreen.gif"/> 4G 五星级站
											</label>
										</li>
										<li>
											<label style="margin-left: 20px;">
												<img src="custom/images/bsGroup.gif"/> 汇聚 4G站点
											</label>
										</li>
									</ul>
								</div>
							</div>
							<div class="content text-center no-padding">
								<!--<iframe src="map_files.htm" style="width:100%;height:600px;" frameborder="0"></iframe>-->
								<!--<img src="custom/images/map.png" style="margin: auto;"/>-->
								<div id="mapBox"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- Modal -->
		<div class="modal fade" id="detailsModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="margin-top: 150px;">
			<div class="modal-dialog" role="document" style="width: 900px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title">汇聚4G站点列表</h4>
					</div>
					<div class="modal-body text-center text-danger">
						<div id="detailsGrid"></div>
					</div>
				</div>
			</div>
		</div>
		<!-- Modal End-->

		<!--jquery js-->
		<script src="bower_components/plugins/jquery/jquery-1.11.1.min.js"></script>

		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
		<script src="bower_components/plugins/jquery/html5shiv.min.js"></script>
		<script src="bower_components/plugins/jquery/respond.min.js"></script>
		<![endif]-->
		<!-- echarts -->

		<script type="text/javascript" src="bower_components/plugins/bootstrap/bootstrap.js"></script>
		<script type="text/javascript" src="bower_components/plugins/mCustomScrollbar/jquery.mCustomScrollbar.concat.min.js"></script>

		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/kendo.all.min.js"></script>
		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/cultures/kendo.culture.zh-CN.min.js"></script>
		<script type="text/javascript" src="bower_components/plugins/kendoui2015.1.318/js/messages/kendo.messages.zh-CN.min.js"></script>

		<script src="bower_components/plugins/leaflet-0.7.5/dist/leaflet-src.js"></script>
		<script src="bower_components/plugins/leaflet-0.7.5/Leaflet.markercluster/leaflet.markercluster-src.js"></script>
		<script src="bower_components/plugins/leaflet-0.7.5/Leaflet.markercluster/leaflet.featuregroup.subgroup-src.js"></script>

		<script type="text/javascript" src="bower_components/js/nav.js"></script>
		 
	    <!-- <script type="text/javascript" src="custom/app/dashboard/mapDataSource.js"></script> -->
	    <script type="text/javascript" src="custom/app/common.js"></script> 
		<script type="text/javascript" src="custom/app/dashboard/bsMap.js"></script>
	    <script type="text/javascript" src="custom/app/dashboard/yunnanJSON.js"></script>
	</body>

</html>