<#assign basePath=requestContext.contextPath>
<#macro emsMenu>  
		<!--顶部导航栏-->
		<div class="n-nav n-nav-bg">
	        <div class="container-fluid">
	            <div class="row noMarginTop noMarginBottom">
	                <div class="col-md-4 col-sm-4  col-xs-12">
	                    <div class="clearfix">
	                        <div class="n-navbar-title">
	                            <a href="ems-home" class="btn n-icon n-icon-32 n-icon-color3" role="button">
	                                <i class="glyphicon glyphicon-menu-left"></i>
	                            </a>
	                            &nbsp;设备监控系统
	                        </div>
	                    </div>
	                </div>
	                <div class="col-md-8 col-sm-8 col-xs-12">
	                    <div id="topNavListWrap" class="n-navbar-nav clearfix">
	                        <div id="topNavList">
	                            <ul id='navList' class="nav navbar-nav">
	                                <li><a href="emsResult">巡检结果</a></li>
	                                <li><a href="emsManage">方案配置</a></li>
	                                <li><a href="noticeManage">通知管理</a></li>
	                                <li><a href="exceptionRule">异常规则</a></li>
	                                <li><a href="equipment-node-group">设备组管理</a></li>
									<!--<li><a href="">通知结果</a></li>-->
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
</#macro>  