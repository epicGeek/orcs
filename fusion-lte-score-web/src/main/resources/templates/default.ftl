<!doctype html> 
<html data-ng-app="welcomeApp">
	<head> 
    	<meta content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no, width=device-width" name="viewport" />
    	<link rel="stylesheet" href="bower_components/kendo-ui-core/styles/kendo.common.min.css" />
		<link rel="stylesheet" href="bower_components/kendo-ui-core/styles/kendo.default.min.css" />
		<link rel="stylesheet" href="bower_components/bootstrap-css-only/css/bootstrap.css" />
	    <link rel="stylesheet" href="bower_components/bootstrap-css-only/css/bootstrap.min.css" />
		<link rel="stylesheet" href="custom/css/common.css" />
		<link rel="stylesheet" href="custom/css/custom-grid-button.css" />
		<link rel="stylesheet" href="custom/css/hdft.css" />
		<link rel="stylesheet" href="custom/css/menu.css" />		
		<link rel="stylesheet" href="custom/css/nsn-home-style.css">
		<style>
		.box{
		  display: -webkit-flex; /* Safari */
		  display: flex;
		}
		</style>
    </head> 
 <body>
	<div class="n-main-home">
    <div class="container">
	<div class="row">
	
		<h4>网元统一登录</h4>
        <div class="row">
        	<div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color4" role="button" href="unified-login"><i class="glyphicon glyphicon-collapse-up"></i></a>
                    <p>集中登录</p>
                </div>
            </div>
        </div> 
        
        <h4>日常运维</h4>
        <div class="row">
            <!--<div class="col-xs-4 col-sm-3 col-md-2 text-center"> -->
            <!--    <div class="thumbnail opacity"> -->
            <!--        <a class="btn n-icon n-icon-color9" role="button" href="#"><span class="badge" id="alarmCountBadge">...</span><i class="glyphicon glyphicon-eye-open"></i></a> -->
            <!--        <p>告警监控</p> -->
            <!--    </div> -->
            <!-- </div> -->
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color5" role="button" href="maintain-secure"><i class="glyphicon glyphicon-eye-open"></i></a>
                    <p>安全管理</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color1" role="button" href="maintain-hw"><i class="glyphicon glyphicon-calendar"></i></a>
                    <p>软硬件维护</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color7" role="button" href="maintain-remote"><i class="glyphicon glyphicon-search"></i></a>
                    <p>局数据管理</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color11" role="button" href="maintain-network"><i class="glyphicon glyphicon-stats"></i></a>
                    <p>网络接口维护</p>
                </div>
            </div>
        </div>
        
        
	<!--    <h4>总体呈现</h4>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail opacity">
                    <a class="btn n-icon" role="button" href="#" target="_Blank">
                        <i class="glyphicon glyphicon-asterisk"></i></a>
                    <p>拓扑图</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail opacity">
                    <a class="btn n-icon n-icon-color1" role="button" href="#">
                        <i class="glyphicon glyphicon-search"></i></a>
                    <p>应急保障状态查询</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail opacity">
                    <a class="btn n-icon n-icon-color2" role="button" href="#"><i class="glyphicon glyphicon-collapse-up"></i></a>
                    <p>一键登录</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail opacity">
                    <a class="btn n-icon n-icon-color" role="button" href="#">
                        <i class="glyphicon glyphicon-oil"></i></a>
                    <p>局数据</p>
                </div>
            </div>
        </div>
        -->
     <!--   <h4>智能运维</h4>
        <div class="row">
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail opacity">
                    <a class="btn n-icon n-icon-color10" role="button" href="#"><i class="glyphicon glyphicon-stats"></i></a>
                    <p>总体指标</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail opacity">
                    <a class="btn n-icon n-icon-color3" role="button" href="#"><i class="glyphicon glyphicon-stats"></i></a>
                    <p>指标监控</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail opacity">
                    <a class="btn n-icon n-icon-color4" role="button" href="#"><i class="glyphicon glyphicon-level-up"></i></a>
                    <p>一键备份</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail opacity">
                    <a class="btn n-icon n-icon-color9" role="button" href="#">
                        <span class="badge" id="alarmCountBadge">...</span>
                        <i class="glyphicon glyphicon-alert"></i></a>
                    <p>告警监控</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail opacity">
                    <a class="btn n-icon n-icon-color6" role="button" href="#"><i class="glyphicon glyphicon-eye-open"></i></a>
                    <p>智能巡检</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail opacity">
                    <a class="btn n-icon n-icon-color7 icon-flip" role="button" href="#"><i class="glyphicon glyphicon-resize-small"></i></a>
                    <p>板卡倒换</p>
                </div>
            </div>

        </div>  -->
        
        
        <h4>高级功能</h4>
        <div class="row">
        
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color3" role="button" href="businessMonitor"><i class="glyphicon glyphicon-dashboard"></i></a>
                    <p>BOSS业务实时监控</p>
                </div>
            </div>
        
        	<div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color1" role="button" href="businessSearch"><i class="glyphicon glyphicon-search"></i></a>
                    <p>用户BOSS业务查询</p>
                </div>
            </div>
        
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color7" role="button" href="PGWLogQuery"><i class="glyphicon glyphicon-list"></i></a>
                    <p>PGW日志验证</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color1" role="button" href="userDataQuery"><i class="glyphicon glyphicon-user"></i></a>
                    <p>用户数据查询</p>
                </div>
            </div>
            
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color6" role="button" href="maintain-userdata"><i class="glyphicon glyphicon-th-large"></i></a>
                    <p>用户数据管理</p>
                </div>
            </div>
            	<div class="col-xs-4 col-sm-3 col-md-2 text-center">
                	<div class="thumbnail">
                    <a class="btn n-icon n-icon-color8" role="button" href="networkElementOperationLog"><i class="glyphicon glyphicon-pencil"></i></a>
                    <p>网元操作日志</p>
                	</div>
            	</div>
        </div>
        <h4>平台管理</h4>
        <div class="row">
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color3" role="button" href="numberSectionManage"><i class="glyphicon glyphicon-calendar"></i></a>
                    <p>号段管理</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color5" role="button" href="areaManage"><i class="glyphicon glyphicon-asterisk"></i></a>
                    <p>地区管理</p>
                </div>
            </div>
            
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color9" role="button" href="equipment-ne"><i class="glyphicon glyphicon-resize-small"></i></a>
                    <p>网元管理</p>
                </div>
            </div>
            
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color2" role="button" href="equipment-unit"><i class="glyphicon glyphicon-dashboard"></i></a>
                    <p>单元管理</p>
                </div>
            </div>
            
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color3 icon-flip" role="button" href="system-user"><i class="glyphicon glyphicon-user"></i></a>
                    <p>用户管理</p>
                </div>
            </div>
            
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color4 icon-flip" role="button" href="system-role"><i class="glyphicon glyphicon-duplicate"></i></a>
                    <p>角色管理</p>
                </div>
            </div>
            
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color1 icon-flip" role="button" href="system-resource"><i class="glyphicon glyphicon-equalizer"></i></a>
                    <p>资源管理</p>
                </div>
            </div>
            
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color11 icon-flip" role="button" href="check-item"><i class="glyphicon glyphicon-menu-hamburger"></i></a>
                    <p>指令管理</p>
                </div>
            </div>
            
            
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color10 icon-flip" role="button" href="command-group"><i class="glyphicon glyphicon glyphicon-list-alt"></i></a>
                    <p>指令组管理</p>
                </div>
            </div>
    </div>     
     <!--   <h4>投诉支撑</h4>
        <div class="row">
        
             <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color1" role="button" href="businessSearch"><i class="glyphicon glyphicon-list"></i></a>
                    <p>用户BOSS业务查询</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail opacity">
                    <a class="btn n-icon n-icon-color9" role="button" href="#"><i class="glyphicon glyphicon-user"></i></a>
                    <p>单用户数据查询</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail opacity">
                    <a class="btn n-icon n-icon-color10" role="button" href="#"><i class="glyphicon glyphicon-th-large"></i></a>
                    <p>批量用户数据查询</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail opacity">
                    <a class="btn n-icon n-icon-color2" role="button" href="#"><i class="glyphicon glyphicon-knight"></i></a>
                    <p>高级用户查询</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail opacity">
                    <a class="btn n-icon n-icon-color9" role="button" href="#">
                        <i class="glyphicon glyphicon-pencil"></i></a>
                    <p>用户功能修改</p>
                </div>
            </div>
        </div>
        <h4>BOSS业务监控</h4>
        <div class="row">
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail">
                    <a class="btn n-icon n-icon-color3" role="button" href="businessMonitor"><i class="glyphicon glyphicon-dashboard"></i></a>
                    <p>BOSS业务实时监控</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail opacity">
                    <a class="btn n-icon n-icon-color4" role="button" href="#"><i class="glyphicon glyphicon-calendar"></i></a>
                    <p>BOSS业务开销户统计</p>
                </div>
            </div>
        </div>		-->
        		
        		
  <!--      <h4>应急保障</h4>
        <div class="row">
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail opacity">
                    <a class="btn n-icon n-icon-color5 icon-flip" role="button" href="#"><i class="glyphicon glyphicon-log-out"></i></a>
                    <p>节点隔离</p>
                </div>
            </div>
            <div class="col-xs-4 col-sm-3 col-md-2 text-center">
                <div class="thumbnail opacity">
                    <a class="btn n-icon n-icon-color6 icon-flip" role="button" href="#"><i class="glyphicon glyphicon-log-in"></i></a>
                    <p>节点恢复</p>
                </div>
            </div>
        </div> -->
      </div> 
    </div>
</div>
<!-- 
	<script src="bower_components/angular/angular.js"></script> 
	<script src="bower_components/angular-resource/angular-resource.js"></script> -->
	<script src="bower_components/jquery/dist/jquery.js"></script> 
	
</body>
</html> 