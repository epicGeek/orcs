<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<!DOCTYPE html>
<html>

	<head lang="zh-cn">
		<title>用户数据查询</title> 
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
		
		<input type="hidden" id="number" value="${number}">
		<input type="hidden" id="isAdvanced" value="${isFlag}">
	    <div class='container-fluid'>
			<div class='row'>
				<div class='col-md-12'>
					<div class="panel">
						<div class="panel-body">
						  	<div class="row">
							  <div class="col-sm-2"  style='margin-left: 20px;'>
							  	   <label class="control-label text-right" style='margin: 0px;line-height: 34px;'>
								  	<input type="radio" class="radioText" value="2" name='radio1'/>&nbsp;IMSI &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								   </label>
								   <label class="control-label text-right" style='margin: 0px;line-height: 34px;'>
								  	<input type="radio" class="radioText" value="1" name='radio1'/>&nbsp;MSISDN
								   </label>
							  </div>
							  <div class="col-sm-2">
							  	<input type="text" id="contextvalue" class="form-control" />
							  </div>
							  <div class="col-md-2">
							  	<button id="btn-search" class="btn btn-info">
							  		<span class="glyphicon glyphicon-search"></span>
							  		查询
							  	</button>
							  	<button class="btn btn-success" type="button" id='histryId' >历史查询记录</button>
							  </div>
							   <div id="isUnit" class="col-md-5" style="display: none;" >
							      <label id='labelUnit'></label>
							      
							   </div>
							</div>
						</div>
						<div id='userDataList'></div>
					</div>
				</div>
			</div>
			<div style="display: none;"  id="logModal" >
			       <div class="form-inline search-btn-group">
			           		<input id='startTime' type="text" /> - 
							<input id='endTime' type="text" />
							<input id="searchName" class="k-textbox"  placeholder="筛选查询号段、操作人" title="筛选查询号段、操作人"/>
                    </div>
				<div id="soapList"></div>
		    </div>
				<div class="col-md-12">
					<div role="tabpanel">
					  <ul class="nav nav-tabs" role="tablist" id="liList"></ul>
					  <div class="tab-content" id='tabContent'></div>
					</div>
				</div>
		</div>
		<span id="popupNotification"></span>
	    <!--底部-->
	    <@tailer></@tailer>
		<@script></@script>
		<script src="custom/app/common.js"></script>
		<script src="custom/app/advanced/userData.js"></script>
		<script src="custom/app/advanced/userDataUtil.js"></script>
		
	</body>
</html>