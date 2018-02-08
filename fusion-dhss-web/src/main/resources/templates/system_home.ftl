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

	<body class="bodyBg"> 
		
		<!--顶部-->
		<@user_bar/>
 
		<div class="n-main-home">
			<div class="container">
				<h4>权域分配管理</h4>
				<div class="row">
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color3" role="button" href="number-section-manage">
								<i class="glyphicon glyphicon-object-align-vertical"></i>
							</a>
							<p>号段管理</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color4" role="button" href="system-area">
								<i class="glyphicon glyphicon-map-marker"></i>
							</a>
							<p>地区管理</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color5" role="button" href="equipment-ne"><i class="glyphicon glyphicon-calendar"></i></a>
							<p>网元管理</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color1" role="button" href="equipment-unit">
								<i class="glyphicon glyphicon-sound-dolby"></i>
							</a>
							<p>单元管理</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color7" role="button" href="system-user">
								<i class="glyphicon glyphicon-user"></i>
							</a>
							<p>用户管理</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color3" role="button" href="system-role">
								<i class="glyphicon glyphicon-cog"></i>
							</a>
							<p>角色管理</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color6" role="button" href="check-item">
								<i class="glyphicon glyphicon-bookmark"></i>
							</a>
							<p>指令管理</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color7" role="button" href="command-group">
								<i class="glyphicon glyphicon-oil"></i>
							</a>
							<p>指令组管理</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color4" role="button" href="system-resource">
								<i class="glyphicon glyphicon-list-alt"></i>
							</a>
							<p>菜单管理</p>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script>
			$(".col-xs-4").hide();
			<#list menus as menu>
				$(".thumbnail a[href='${menu.menuCode}']").parent().parent().show();
			</#list>
		</script>
		<!--底部-->
		<@tailer></@tailer>
		<!--//底部-->
		<!--引用主页上方数值显示脚本-->
		<@script></@script>
		
		
	</body>

</html>