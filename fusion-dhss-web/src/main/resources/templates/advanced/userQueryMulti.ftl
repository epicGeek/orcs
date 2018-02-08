<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<!DOCTYPE html>
<html>

	<head lang="zh-cn">
		<title>批量用户数据查询</title> 
		<@header></@header>
		<link rel="stylesheet" href="assets/css/soap.css">
		<myStyle>
		  
		</myStyle>
	</head>

	<body>

		 <!-- 用户信息 -->
		<@user_bar/>
		<!--顶部导航栏-->
		<@navigate_bar/>
		
		<!--内容部分-->
				<!--内容部分-->
		<div class="body-content">
		    <div class="container">
		        <div class="row">
		            <!-- 右侧内容  -->
		            <div class="col-xs-12 content-right">
		                <div class="col-md-12 right-content">
		
		                    <div class="console-title clearfix">
		                        <div class="pull-left">
		                            <h4>批量用户查询<span class="fa fa-question-circle" popover="" popover-trigger="mouseenter" popover-placement="right"></span></h4>
		                        </div>
		                        <div class="pull-right"></div>
		                    </div>
		                    <div class="console-body">
		                        <div class="row">
									 <!--  <div class="col-sm-2"  style='margin-left: 20px;'>
									  	   <label class="control-label text-right" style='margin: 0px;line-height: 34px;'>
										  	<input type="radio" class="radioText" value="2" name='radio1'/>&nbsp;IMSI &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										   </label>
										   <label class="control-label text-right" style='margin: 0px;line-height: 34px;'>
										  	<input type="radio" class="radioText" value="1" name='radio1'/>&nbsp;MSISDN
										   </label>
									  </div> -->
									  <div class="col-sm-8" >
									  	<input type="text" id="contextvalue" class="form-control" placeholder="请输入用户的MSISDN/IMSI号码,如460029111909300,8615911199300。多个号码以逗号隔开。"  />
									  </div>
									  <div class="col-md-4">
				                        <div class="search-item clearfix">
				                            <button class="btn btn-primary" id='btn-user-search'><i class="glyphicon glyphicon-search"></i> 查询</button>
				                            <button class="btn btn-primary" id='btn-demo-down'><i class="glyphicon glyphicon-search"></i> 模板下载</button>
					                        <button class="btn btn-primary" id='btn-readExcel'><i class="glyphicon glyphicon-search"></i> 号码导入</button>
				                        </div>
		                    		 </div>
								</div>
							</div>
							  <div class="row">
								 <div class="col-md-12">
									<div role="tabpanel">
									  <!-- Nav tabs -->
									  <ul class="nav nav-tabs" role="tablist" id="liList"></ul>
									  <!-- Tab panes -->
									  <div class="tab-content" id='tabContent'>
									           <div align="center" id="waing" style="display: none;">
									                  <h2>正在获取数据，请耐心等待...</h2>
									           </div>
									   </div>
									</div>
								 </div>
							  </div>
							<div class="modal-body">
							    <p><input id="cond-command" type="text" class="form-control"  placeholder="筛选查询号段、操作人" ></p>
								<div id='batchUserData'></div>
							</div>
							<div  style="display: none;">
							     <input type="file" id="fileName" name="fileName" onchange="uploadExcelFile(this)">
							</div>
		                </div>
		            </div>
		        </div>
		    </div>
		</div>
		<span id="popupNotification"></span>
	    <!--底部-->
	    <@tailer></@tailer>
		<@script></@script>
		<script src="custom/app/common.js"></script>
		<script type="text/javascript" src="assets/plugins/jquery/ajaxfileupload.js"></script>
		<script src="custom/app/advanced/userQueryMulti.js"></script>
		<script src="custom/app/advanced/userDataUtil.js"></script>
		
	</body>
</html>