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

	<body>

		<!--顶部-->
		<!-- 用户信息 -->
		<@user_bar/>

		<!--顶部导航栏-->
		<@navigate_bar/>
		<!--//顶部导航栏-->
        <input type="hidden" id="category_cn" value="${category_cn}">
        <input type="hidden" id="category_code" value="${category_code}">
		<!--内容部分-->
		<div class="container-fluid">
			<div class="row noMarginBottom">
			 
				<div class="col-sm-3 marginTop10">
					<div class="panel panel-default panelLighter">				   
						
						<div class="panel-heading"><span class="badge">1</span>&nbsp;选择指令&nbsp;---&nbsp;<a href="${category_url}">按网元过滤</a></div>
						<div class="panel-body" id='instructionWrap' style="overflow-y: auto;">
							<div id='commandGrid' class="table table-striped table-nobordered">
							</div>
						</div>
						
						
					</div>
				</div>
				<div class="col-sm-5 marginTop10 noPadding ">
					<div class="panel panel-default panelLighter">
						<div class="panel-heading"><span class="badge">2</span> 选择网元</div>
						<div class="panel-body">
							
							<div id='unitGrid'></div>
						</div>
						<div class="panel-footer text-right">
							<button id='submitBtn' class="btn btn-success btn-sm" >提交执行</button>
						</div>
					</div>
				</div>
				<div class="col-sm-4 marginTop10">
					<div class="panel panel-default">
						<div class="panel-heading">执行结果</div>
						<div class="panel-body">
							<div id='resultGrid'></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--//内容部分-->
		
		<!--底部-->
		<@tailer></@tailer>
		<!--//底部-->
			
       <div id='paramsWindow' style="display: none;">
         <div align="center" id="modal_dialg" style="display: none; top: 20px;"></div>
       </div>
		<!--提交执行弹窗-->
		<div id='submitWindow' style="display: none;">
		    <div id='confirm-command-page'></div>
		    <div class="k-edit-buttons k-state-default text-right windowFooter">
								<a id ='dialogOKBtn' class="k-button k-button-icontext k-primary k-grid-update">
									<span class="k-icon k-update"></span>确定执行
								</a>
								<a  id ='dialogCancelBtn'  class="cancelBtns k-button k-button-icontext k-grid-cancel">
									<span class="k-icon k-cancel"></span>取消
								</a>
			</div>
			
		</div>
		
			<div id='windowTemplate' style="display: none;">
				<div  style=" overflow:scroll;overflow-x:visible; height:400px;" id = "logText">
				</div>
			</div>
		<!-- echarts -->
		<@script></@script>
		<script type="text/javascript" src="custom/app/maintain/maintain-item.js" ></script>

	</body>

</html>