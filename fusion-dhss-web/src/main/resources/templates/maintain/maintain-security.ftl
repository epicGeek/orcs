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
				<div class="col-sm-6 marginTop10">
					<div class="panel panel-default panelLighter">
						<div class="panel-heading">选择单元列表</div>
						<div class="panel-body ">
							<div class="row noMarginTop">
								<div class="form-inline noPadding">
									<div class="form-group form-group-xs">
										<label>网元类型</label>
										<input class="form-control" id='inputNeTypeTrigger'>
									</div>
									<div class="form-group form-group-xs">
										<label>网元名称</label>
										<input class="form-control" id='inputNeTrigger'>
										</input>
									</div>
									<div class="form-group form-group-xs">
										<label>单元类型</label>
										<input class="form-control" id='inputUnitTypeTrigger'>
										</input>
									</div>
									<div class="form-group form-group-xs">
										<label>单元名称/IP</label>
										<input type="text" name="" id="inputKeyWord" value="" class="form-control"/>
									</div>
									
								</div>
							</div>
							
							<div id='unitGrid'></div>
						</div>
						<div class="panel-footer text-right">
							<button id='submitBtn' class="btn btn-success btn-sm" >修改网元密码</button>
						</div>
					</div>
				</div>
				
				<div class="col-sm-6 marginTop10 noPaddingLeft">
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
	
		<!--提交执行弹窗-->
		<div id='submitWindow' style="display: none;">
			<div class="container-fluid" >
				<div class="alert alert-warning">
					<label >密码规则</label>
					<ul >
						<li><code>密码长度不小于 8 个字符</code></li>
						<li><code>必须包含特殊字符、大小写字母、数字</code></li>
						<li><code>不能与近五次的密码相同</code></li>
					</ul>
				</div>
				<div class="form-horizontal">
						<div class="form-group">
							<label class="col-md-3 control-label" >用户名</label>
							<div class="col-md-5">
								<input type="text" id= "inputAccount" class="form-control" placeholder="用户名"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-3 control-label" >新密码</label>
							<div class="col-md-5">
								<input type="password" id= "newPassword" class="form-control" placeholder="新密码"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-3 control-label" for="ip">确认新密码</label>
							<div class="col-md-5">
								<input type="password"  id= "newPasswordAgain"  class="form-control"  placeholder="确认新密码"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-3 control-label" ></label>
							<div class="col-md-5  text-right">
								<button id='dialogOKBtn' class="btn btn-sm btn-success" style="width: 30%;">确定</button>
							</div>
						</div>
				</div>
			</div>
		</div>
		
		
			<div id='windowTemplate' style="display: none;">
				<div  style=" overflow:scroll;overflow-x:visible; height:400px;" id = "logText">
				</div>
			</div>

		<@script></@script>
        <script type="text/javascript" src="custom/app/maintain/maintain-security.js" ></script>
	</body>

</html>