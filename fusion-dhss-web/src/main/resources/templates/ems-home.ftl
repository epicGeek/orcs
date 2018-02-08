<#include "common/header.ftl">
<#include "common/user-bar.ftl">
<#include "common/tailer.ftl">
<#include "common/script.ftl">
<!DOCTYPE html>
<html>

	<head lang="zh-cn">
		<@header></@header>
		<link rel="stylesheet" href="assets/css/nsn-home-style.css">
	</head>
	
	<style type="text/css">
            .home-nav-item ul li {
                min-width: 180px;
                line-height: 30px;
                margin-bottom: 2px;
            }
            .home-nav-item ul li i {
                font-size: 18px;
                line-height: 32px;
                top: 4px;
            }
            .home-nav-item ul li .title {
                font-size: 14px;
                line-height: 32px;
                top: 1px;
            }
            .home-nav-item ul li a {
                display: block;
                height: 100%;
                padding: 0 10px;
                text-decoration: none;
                color: #fff;
                border-bottom: 1px dashed rgba(255,255,255,0.2);
            }
            .home-nav-item ul li a.disable {
                opacity: 0.5;
                cursor:not-allowed;
            }
            .home-nav-item ul li a:hover {
                background-color: rgba(255,255,255,0.2);
            }

            .home-nav-item .home-nav-item-title {
                color: #feb100;
                padding: 10px 5px;
                /*padding-left: 5px;*/
                /*border-left: 5px solid rgba(255,255,255,0.5) ;*/
                background-color: rgba(0,0,0,.1);
                margin: 0px 0px;
            }
        </style>

	<body class="bodyBg">

		<!--顶部-->
		<@user_bar/>

        <!--内容部分-->
        <div class="n-main-home">
			<div class="container">
				<h4>设备监控系统</h4>
				<div class="row">
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color9" role="button" href="emsResult">
								<i class="glyphicon glyphicon-search"></i>
							</a>
							<p>结果查询</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color11" role="button" href="emsManage">
								<i class="glyphicon glyphicon-oil"></i>
							</a>
							<p>方案配置</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color6 icon-flip" role="button" href="noticeManage"><i class="glyphicon glyphicon-log-in"></i></a>
							<p>通知管理</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color6" role="button" href="exceptionRule">
								<i class="glyphicon glyphicon-bookmark"></i>
							</a>
							<p>异常规则</p>
						</div>
					</div>
					<!-- <div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color5" role="button" href=""><i class="glyphicon glyphicon-level-up"></i></a>
							<p>通知结果</p>
						</div>
					</div> -->
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color6" role="button" href="equipment-node-group">
								<i class="glyphicon glyphicon-bookmark"></i>
							</a>
							<p>设备组管理</p>
						</div>
					</div>
				</div>

			</div>
		</div>

		<!--底部-->
		<@tailer></@tailer>
		<!--//底部-->
		<!--引用主页上方数值显示脚本-->
        <script src="custom/app/homePageIndexShow.js"></script>
		<@script></@script>
	</body>

</html>