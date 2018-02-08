<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<!DOCTYPE html>
<html>
	<head lang="zh-cn">
    <@header></@header>
</head>

	<body class="page-product">
		

<!-- 用户信息 -->
		<@user_bar/>
		<!--顶部导航栏-->
		<@navigate_bar/>
    <!--内容部分-->
    <div class="container-fluid">
				<div class="row">
					<div class="col-sm-12 ">
						<div class="form-inline">
							<div class="form-group form-group-sm">
	                    		<label>网元类型：</label><input id="inputSite" placeholder="请选择..." />
							</div>
							<div class="form-group form-group-sm">
	                    		<label>单元类型：</label><input id="inputType" placeholder="请选择..." />
							</div>
							<div class="form-group form-group-sm">
	                           <label>节点名称</label><input id="inputNodeSearchName" type="text" class="form-control" placeholder="节点名称">
							</div>  
							<button class="btn btn-primary  btn-sm" onclick="doSearchBackupHistory()"><i class="glyphicon glyphicon-search"></i>查询</button>
							<button class="btn btn-default  btn-sm" onclick="doreset()" id="reset"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
	                    </div>
                        	<div id='dataGrid'></div>
                    </div>
                </div>
            </div>
    
    
    
    
    

   <form id="downloadFileForm" action="backup/downloadBackupFile" method="post">
       <input id="downloadFileName" name="downloadFileName" type="hidden"/>
       <input id="downloadFileDisplayName" name="downloadFileDisplayName" type="hidden"/>
       <input id="downloadRowId" name="downloadRowId" type="hidden"/>
   </form>
   <!--弹出信息-->
	<div id="showLogWindows" style="display: none">
	    <div id="logData" style="height: 480px;"></div>
	</div>
	<!--//弹出信息-->
 
		
		<@tailer></@tailer>
		
	    <@script></@script>
		
        <script src="custom/app/oneKey/onekeybackuphistory.js"></script>
    
	</body>
</html>