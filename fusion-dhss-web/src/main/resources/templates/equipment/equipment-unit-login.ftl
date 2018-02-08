<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<#include "./search_bar_unit_readonly.ftl">
<!DOCTYPE html>
<html>
	<head lang="zh-cn">
		<@header></@header>
	</head>
	<body>

		<!-- 用户信息 -->
		<@user_bar/>
		<!--顶部导航栏-->
		<@navigate_bar/>
		<!--内容部分-->
		<div class="container-fluid">
			<@search_bar_unit_readonly />
			<div class="row">
				<div class="col-sm-12">
					<div id='dataGrid'></div>
				</div>
			</div>
		</div>
		<!--//内容部分-->

		<!--底部-->
		<div class="n-footer">
			<@tailer></@tailer>
		</div>
		<!--//底部-->
		<div id='terminalWindow' style="display: none;">
			<div class="container-fluid">
					<div id="terminalOutput">
					</div>
					<div style="float:right;width:1px;height:1px;overflow:hidden">
	                    <textarea name="dummy" id="dummy" size="1"
	                              style="border:none;color:#FFFFFF;width:1px;height:1px"></textarea>
	                    <input type="text" name="dummy2" id="dummy2" size="1"
	                           style="border:none;color:#FFFFFF;width:1px;height:1px"/>
	                </div>	
			</div>
		</div>
		
		<div style="display: none;"  id="logModal" >
			<div class="search-btn-group">
				<input id="cond-command" class="k-textbox"  placeholder="筛选登录网元、操作人" title="筛选登录网元、操作人" style="width:724px;"/>
			</div>
			<div id="logGrid"></div>
		</div>
		<@script></@script>
		
		<script type="text/javascript" src="assets/plugins/tty/term.js"></script>
		<script src="assets/js/jquery.base64.js"></script>
		<script src="custom/app/equipment/equipment-unit-login.js"></script>
	</body>

</html>