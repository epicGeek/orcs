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
                list-style-type:none;
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
            .iconsize {
                font-size: 64px;
            }
            .mainbox {
                margin-bottom: 20px;
                color: #FFFFFF;
                background-color: rgba(0,0,0,.1);
                border: 1px solid transparent;
                border-radius: 2px;
                -webkit-box-shadow: 0 1px 1px rgba(0,0,0,.05);
                box-shadow: 0 1px 1px rgba(0,0,0,.05);
                padding: 10px 15px;
            }
            .huge {
                font-size: 40px;
            }
            .huge>span {
                font-size: 12px;
            }
            .n-icon-color1 {
                background-color: #e88a05;
            }
            .n-icon-color2 {
                background-color: #0093a8;
            }
            .n-icon-color3 {
                background-color: #ce4b27;
            }
        </style>

	<body class="bodyBg">
		
		<#if (message??)>
			<script>
				infoTip({content: 'licence 验证失败'});
			</script>
		</#if>
	
		
		
		<!--顶部-->
		<@user_bar/>
		
		 <!--内容部分-->
        <div class="n-main-home">
            <div class="container">
                <div class="row">
                    <a href="alarm-new"><div class="col-xs-6 col-sm-6 col-md-4" >

                            <div class="mainbox  n-icon-color1">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="glyphicon glyphicon-alert iconsize"></i>
                                    </div>
                                    <div class="col-xs-9 text-right" >
                                    	<div>今日全网告警数量</div>
                                        <div id="alarmCount">正在获取... </div>
                                    </div>
                                </div>
                            </div>

                    </div></a>
                    <div class="col-xs-6 col-sm-6 col-md-4">
                        <div class="mainbox  n-icon-color2">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="glyphicon glyphicon-saved iconsize"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                    	<div id = "kpi1"></div>
                                        <div id="authenticationSuccessRate">正在获取... </div>
                                    </div>
                                </div>
                        </div>
                    </div>
                    <div class="col-xs-6 col-sm-6 col-md-4">
                        <div class="mainbox  n-icon-color3">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="glyphicon glyphicon-phone iconsize"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                    	<div id = "kpi2"></div>
                                        <div id="callSuccessRate">正在获取... </div>
                                    </div>
                                </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12 col-sm-4 col-md-3">
                        <div class="home-nav-item">
                            <h4 class="home-nav-item-title">全息呈现</h4>
                            <ul>
                                <li><a href="dhssTopology" ><i class="glyphicon glyphicon-asterisk"></i> <span class="title">拓扑图</span></a></li>
                                <li><a href="equipment-unit-login"><i class="glyphicon glyphicon-log-in"></i> <span class="title">一键登录</span></a></li>
                            </ul>
                        </div>
                        <div class="home-nav-item">
                            <h4 class="home-nav-item-title">投诉支撑</h4>
                            <ul>
                                <li><a href="userDataQuery"><i class="glyphicon glyphicon-user"></i> <span class="title">单用户数据查询</span></a></li>
                                <li><a href="userQueryMulti"><i class="glyphicon glyphicon-upload"></i> <span class="title">批量用户数据查询</span></a></li>
                                <li><a href="userDataManage"><i class="glyphicon glyphicon-baby-formula"></i> <span class="title">用户数据管理</span></a></li>								
                            	  <li><a href="boss-rev-query"><i class="glyphicon glyphicon-stats"></i> <span class="title">BOSS业务数据实时查询</span></a></li>
                            	  <li><a href="boss-rev-kpi"><i class="glyphicon glyphicon-stats"></i> <span class="title">BOSS业务数据统计</span></a></li>
                            </ul>
                        </div>
                        
                    </div>
                    <div class="col-xs-12 col-sm-4 col-md-3">
                        <div class="home-nav-item">
                            <h4 class="home-nav-item-title">日常运维</h4>
                            <ul>
                            	
                            	<li><a href="maintain-environment"><i class="glyphicon glyphicon-stats"></i> <span class="title">软硬件维护</span></a></li>
                            	<li><a href="maintain-remote"><i class="glyphicon glyphicon-stats"></i> <span class="title">局数据管理</span></a></li>
                            	<li><a href="maintain-network"><i class="glyphicon glyphicon-stats"></i> <span class="title">网络接口维护</span></a></li>
                            	<li><a href="monitor"><i class="glyphicon glyphicon-resize-small"></i> <span class="title">监控报表</span></a></li>
                    
                            	
                            </ul>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-4 col-md-3">
                        <div class="home-nav-item">
                            <h4 class="home-nav-item-title">智能运维</h4>
                            <ul>
                                <li><a href="allQuota"><i class="glyphicon glyphicon-file"></i> <span class="title">总体指标</span></a></li>
                                <li><a href="quotaMonitor"><i class="glyphicon glyphicon-file"></i> <span class="title">指标监控</span></a></li>
                                <li><a href="oneKeyBackup"><i class="glyphicon glyphicon-retweet"></i> <span class="title">一键备份</span></a></li>
                                <li><a href="alarm-new"><i class="glyphicon glyphicon-floppy-open"></i> <span class="title">告警监控</span></a></li>
                                <li><a href="custom-alarm"><i class="glyphicon glyphicon-floppy-open"></i> <span class="title">自定义告警</span></a></li>                                
                                <li><a href="smart" ><i class="glyphicon glyphicon-save-file"></i> <span class="title">智能巡检</span></a></li>
                            </ul>
                        </div>
                        
                    </div>
                    <div class="col-xs-12 col-sm-4 col-md-3">
                        <div class="home-nav-item">
                            <h4 class="home-nav-item-title">系统管理</h4>
                            <ul>
                               
                                <li><a href="system-home"><i class="glyphicon glyphicon-object-align-vertical"></i> <span class="title">权域分配管理</span></a></li>
                                <li><a href="operation-log" ><i class="glyphicon glyphicon-save-file"></i> <span class="title">平台使用日志</span></a></li>
                                <li><a href="kpi-threshold" ><i class="glyphicon glyphicon-save-file"></i> <span class="title">指标门限管理</span></a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
		
		
		
		
		<script>
			$(".home-nav-item ul li  a").hide();
		</script>
		
		
		

		<!-- 内容部分
		<div class="n-main-home">
			<div class="container">
				<h4>总体呈现</h4>
				<div class="row">
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color4" role="button" href="dhssTopology">
								<i class="glyphicon glyphicon-asterisk"></i></a>
							<p>拓扑图</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon" role="button" href="equipment-unit-login">
								<i class="glyphicon glyphicon-log-in"></i></a>
							<p>集中登录</p>
						</div>
					</div>
				</div>

				<h4>智能运维</h4>
				<div class="row">
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color3" role="button" href="allQuota"><i class="glyphicon glyphicon-stats"></i></a>
							<p>总体指标</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color4" role="button" href="quotaMonitor"><i class="glyphicon glyphicon-equalizer"></i></a>
							<p>指标监控</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color5" role="button" href="oneKeyBackup"><i class="glyphicon glyphicon-level-up"></i></a>
							<p>一键备份</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color9" role="button" href="alarmMonitor">
								 <span class="badge">28</span> 
								<i class="glyphicon glyphicon-alert"></i></a>
							<p>告警监控</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color7" role="button" href="smartCheckJobResult">
								<i class="glyphicon glyphicon-retweet"></i></a>
							<p>智能巡检</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color5 icon-flip" role="button" href="nodeSwitching"><i class="glyphicon glyphicon-resize-small"></i></a>
							<p>板卡倒换</p>
						</div>
					</div>

				</div>
				<h4>日常运维</h4>
				<div class="row">
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color10" role="button" href="maintain-security">
								<i class="glyphicon glyphicon-lock"></i>
							</a>
							<p>安全管理</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color3" role="button" href="maintain-environment">
								<i class="glyphicon glyphicon-inbox"></i>
							</a>
							<p>软硬件维护</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color4" role="button" href="maintain-remote">
								<i class="glyphicon glyphicon-list-alt"></i>
							</a>
							<p>局数据管理</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color1" role="button" href="maintain-network">
								<i class="glyphicon glyphicon-briefcase"></i></a>
							<p>网络接口维护</p>
						</div>
					</div>
				</div>
				
				<h4>应急保障</h4>
				<div class="row">
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color5 icon-flip" role="button" href="nodeIsolation" ><i class="glyphicon glyphicon-log-out"></i></a>
							<p>节点隔离</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color6 icon-flip" role="button" href="nodeRecoveryList"><i class="glyphicon glyphicon-log-in"></i></a>
							<p>节点恢复</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color1" role="button" href="securityState">
								<i class="glyphicon glyphicon-search"></i></a>
							<p>应急保障状态查询</p>
						</div>
					</div>
				</div>
				<h4>高级功能</h4>
				<div class="row">
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color5" role="button" href="businessMonitor">
								<i class="glyphicon glyphicon-eye-open"></i>
							</a>
							<p>BOSS业务实时监控</p>
						</div>
					</div>
					
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color9" role="button" href="businessSearch">
								<i class="glyphicon glyphicon-search"></i>
							</a>
							<p>用户BOSS业务查询</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color7" role="button" href="userDataQuery">
								<i class="glyphicon glyphicon-equalizer"></i>
							</a>
							<p>用户数据查询</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail ">
							<a class="btn n-icon n-icon-color3" role="button" href="userDataManage">
								<i class="glyphicon glyphicon-baby-formula"></i></a>
							<p>用户数据管理</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color4" role="button" href="networkElementOperationLog">
								<i class="glyphicon glyphicon-map-marker"></i>
							</a>
							<p>网元操作日志</p>
						</div>
					</div>
					
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color8" role="button" href="monitor">
								<i class="glyphicon glyphicon-baby-formula"></i>
							</a>
							<p>监控报表</p>
						</div>
					</div>
					
				</div>
				<h4>平台管理</h4>
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
							<a class="btn n-icon n-icon-color7" role="button" href="system-resource">
								<i class="glyphicon glyphicon-book"></i>
							</a>
							<p>资源管理</p>
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
							<a class="btn n-icon n-icon-color11" role="button" href="smartCheckManage">
								<i class="glyphicon glyphicon-oil"></i>
							</a>
							<p>巡检方案配置</p>
						</div>
					</div>
					<div class="col-xs-4 col-sm-3 col-md-2 text-center">
						<div class="thumbnail">
							<a class="btn n-icon n-icon-color9" role="button" href="operation-log">
								<i class="glyphicon glyphicon-search"></i>
							</a>
							<p>结果查询</p>
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
		</div>-->

		<!--底部-->
		<@tailer></@tailer>
		<!--//底部-->
		<!--引用主页上方数值显示脚本-->
        <script src="custom/app/homePageIndexShow.js"></script>
		<@script></@script>
		
		
		<script>
			<#list menus as menu>
				$(".home-nav-item ul li  a[href='${menu.menuCode}']").show();
			</#list>
		</script>
	</body>

</html>