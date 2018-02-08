<#macro navigate_bar>  
		<div class="n-nav n-nav-bg">
			<div class="container-fluid">
				<div class="row noMarginTop noMarginBottom">
					<div class="col-md-2 col-sm-3 col-xs-10">
						<div class="clearfix">
							<div class="n-navbar-title">
									<a href="welcome" class="btn n-icon n-icon-32 n-icon-color3" role="button">
										<i class="glyphicon glyphicon-menu-left"></i>
									</a>
									&nbsp;<font id="parentMenuName"></font>
							</div>
						</div>
					</div>
					<div class="col-md-offset-1 col-md-9 col-sm-9 col-xs-14" style="float:right; _position:relative;">
						<div id="topNavListWrap" class="n-navbar-nav clearfix" >
							<div id="topNavList">
								<ul id='navList' class="nav navbar-nav">
								</ul>
							</div>
							<button id='overFlowBtn' class="btn btn-info">
								<span class="glyphicon glyphicon-align-justify"></span>
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--//顶部导航栏-->
		<script>
		$(function() {
		/*当前导航*/
			var pageURI = (window.location.href);
			var aa = pageURI.match(/http?:\/\/[^/]+\/([^/]+)/);
			var menuPath = aa[1];
			if(menuPath.indexOf("?")!=-1)
				menuPath = menuPath.substring(0,menuPath.indexOf("?"));
			else
				menuPath = menuPath;
			$.ajax({
				url:"rest/system-menu/search/findByLink/"+menuPath,
				method:"GET",
				dataType : 'json',
				success:function(data){
					var linkTemplate = '<li><a href="{menuCode}">{menuName}</a></li>';
					var menuHtml = "";
					$.each(data,function(index,menuItem){
						var menuLink = linkTemplate.replace("{menuCode}",menuItem.menuCode).replace("{menuName}",menuItem.menuName);
						menuHtml += menuLink;
						if(menuItem.menuCode==menuPath){
							$("#parentMenuName").html(menuItem.parentMenuName);
						}
					});
					$("#navList").html(menuHtml);
					$("#navList a[href='"+menuPath+"']").addClass("active");
				}
			});
		});
		</script>
</#macro>  