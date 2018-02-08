<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<!DOCTYPE html>
<html>
<head>
<@header/>
</head>
<body class="page-product">
<@user_bar/>
<!--顶部导航-->
<@navigate_bar/>
        <!--内容部分-->
			<div class="container-fluid">
				<div class="row">
					<div class="col-sm-12 ">
						<div class="form-inline">
						<div class="form-group form-group-sm">
                    		<input id="inputDHSSName" type="text"  placeholder="请选择DHSS组" title="请选择DHSS组" />
						</div>
						<div class="form-group form-group-sm">
                    		<input id="inputLocationName" type="text"  placeholder="请选择局址" title="请选择局址" />
						</div>
						<div class="form-group form-group-sm">
                    		<input id="inputNeType" type="text"  placeholder="请选择网元类型" title="请选择网元类型" />
						</div>
						<div class="form-group form-group-sm">
                    		<input id="inputNeName" type="text"  placeholder="请选择网元" title="请选择网元" />
						</div>
						<div class="form-group form-group-sm">
                    		<input id="inputUnitName" type="text"  placeholder="单元名称" title="单元名称" />
						</div>
						</div>
						<div class="form-inline">
                        <div class="form-group form-group-sm">
                            <input id="inputKpiCategory" type="text"  placeholder="请选择指标类型" title="请选择指标类型" />
                        </div>
						<div class="form-group form-group-sm">
                            <input id="inputKpiItem"  type="text"  placeholder="请选择指标" title="请选择指标" />
						</div>  
						<div class="form-group form-group-sm">
                            <input type="text" id="startTime" placeholder="请选择开始时间" />
						</div> -
						<div class="form-group form-group-sm">
                            <input type="text" id="endTime" placeholder="请选择结束时间" />
						</div>
						<div class="form-group form-group-sm">
							<button class="btn btn-primary btn-sm" id="searchc" onclick="getDataM()"><i class="glyphicon glyphicon-search"></i> 查询</button>
                            <!--<button class="btn btn-info btn-sm" id="exportHistoryData"><i class="glyphicon glyphicon-export"></i> 报表导出</button>-->
                        </div>
                        </div>
                        <div id='historyDataGrid'></div>
                    </div>
                </div>
            </div>

<!--底部-->
		
		
       <@tailer/>
       <@script/>
<!--<script src="custom/app/quota/quotaMonitor_revolution.js"></script>-->
<script src="custom/app/quota/quotaMonitor_revolution.js"></script>


</body>
</html>