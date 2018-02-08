<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<!DOCTYPE html>
<html>

	<head lang="zh-cn">
		<@header></@header>
		<link rel="stylesheet" href="assets/css/prettify.css">
		<style type="text/css">
	        pre {
	            white-space: pre-wrap; /* css-3 */
	            white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
	            white-space: -pre-wrap; /* Opera 4-6 */
	            white-space: -o-pre-wrap; /* Opera 7 */
	            word-wrap: break-word; /* Internet Explorer 5.5+ */
	        }
		</style>
	</head>

	<body>

		<!-- 用户信息 -->
		<@user_bar/>
		<!--顶部导航栏-->
		<@navigate_bar/>
		<!--内容部分-->
		<div class="container-fluid">
			<div class="row">
				<div class="col-sm-1 col-xs-12">
					<div id='addBtn' class="addWrap clearfix text-center" >
						<label><i class="glyphicon glyphicon-plus-sign"></i> 添加</label>
						<span></span>
					</div>
				</div>
				<div class="col-sm-5">
					<input type="text" id="inputKeyWord" value=""  class="form-control input-sm" placeholder="筛选指令名称、指令内容" style="width: 100%;" title="对MSISDN号段、IMSI号段进行模糊查询" />
				</div>
				<div class="col-sm-6">
					<button id='clearsearch' class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
				</div>
			</div>
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


		<script type="text/x-kendo-template" id="windowTemplate">
		
			<ul class="nav nav-tabs">
			   <li class="active">
			      <a href="\#home" data-toggle="tab">
			         	基本信息
			      </a>
			   </li>
			   <li><a href="\#ios" data-toggle="tab">LUA解析</a></li>
			</ul>
			<div id="myTabContent" class="tab-content">
			   <div class="tab-pane fade in active" id="home">
			      <div class="container-fluid">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" >指令类型</label>
						<div class="col-md-5">
							<select id='instructionType' class="form-control input-sm"></select>
						</div>
					</div>
					<div class="form-group"  id="cmdType">
						<label class="col-md-2 control-label" >命令类型</label>
						<div class="col-md-5">
							<select id='cmdTypes' class="form-control input-sm"> </select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" >指令名称</label>
						<div class="col-md-5">
							<input id="page-name" class="form-control input-sm" type="text" value='#:name#' />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" >指令内容</label>
						<div class="col-md-5">
							<textarea id="page-command" class="form-control" style="height: 90px;">#:command#</textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" >默认参数</label>
						<div class="col-md-5">
							<input id="page-default" class="form-control input-sm" type="text" value='#if(defaultParamValues!=null&&defaultParamValues!='null'){##:defaultParamValues##}#' />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" >参数名称</label>
						<div class="col-md-5">
							<input id="page-params" class="form-control input-sm" type="text" value='#if(params!=null&&params!='null'){##:params##}#' />
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-2 control-label" >执行指令的用户</label>
						<div class="col-md-5">
							<input id="page-account"  class="form-control input-sm" type="text" value='#:account#' />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" >备注</label>
						<div class="col-md-5">
							<input id="page-remarks"  class="form-control input-sm" type="text" value='#if(remarks!=null&&remarks!='null'){##:remarks##}#' />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" >适用网元</label>
						<div class="col-md-5">
							<select id="applyUnit" multiple="multiple"></select>
						</div>
					</div>	
				</div>
			</div>
			<div class="k-edit-buttons k-state-default text-right windowFooter">
				<a id='saveBtn' class="k-button k-button-icontext k-primary k-grid-update">
					<span class="k-icon k-update"></span>保存
				</a>
				<a id='cancelBtn' class="k-button k-button-icontext k-grid-cancel">
					<span class="k-icon k-cancel"></span>取消
				</a>
			</div>
			   </div>
			    <div class="tab-pane fade" id="ios">
			      <pre class="prettyprint lang-lua" style="width:100%;height:300px" id="LuaScript">#:script#</pre>
			      <div class="k-edit-buttons k-state-default text-right windowFooter">
			      	<a id='editLuaBtn' class="k-button k-button-icontext k-primary k-grid-update">
						<span class="k-icon k-update"></span>编辑
					</a>
					</div>
				</div>
					
			</div>
			
			
		
		
					<!-- Nav tabs -->
					<!--ul class="nav nav-tabs" role="tablist">
						<li role="presentation" class="active"><a href="\#tab1" aria-controls="tab1" role="tab" data-toggle="tab">基本信息</a></li>
						<li role="presentation"  ><a href="\#tab2" aria-controls="tab2" role="tab" data-toggle="tab">LUA解析</a></li>
					</ul>
		
		<div class="tab-content" style="padding-top: 20px;">
						<div role="tabpanel" class="tab-pane active" id="tab1">
		
			<div class="container-fluid">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" >指令类型</label>
						<div class="col-md-5">
							<select id='instructionType' class="form-control input-sm"></select>
						</div>
					</div>
					<div class="form-group"  id="cmdType">
						<label class="col-md-2 control-label" >命令类型</label>
						<div class="col-md-5">
							<select id='cmdTypes' class="form-control input-sm"> </select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" >指令名称</label>
						<div class="col-md-5">
							<input id="page-name" class="form-control input-sm" type="text" value='#:name#' />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" >指令内容</label>
						<div class="col-md-5">
							<textarea id="page-command" class="form-control" style="height: 90px;">#:command#</textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" >参数名称</label>
						<div class="col-md-5">
							<input id="page-params" class="form-control input-sm" type="text" value='#if(params!=null&&params!='null'){# #:params# #}#' />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" >执行指令的用户</label>
						<div class="col-md-5">
							<input id="page-account"  class="form-control input-sm" type="text" value='#:account#' />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2 control-label" >备注</label>
						<div class="col-md-5">
							<input id="page-remarks"  class="form-control input-sm" type="text" value='#if(remarks!=null&&remarks!='null'){# #:remarks# #}#' />
						</div>
					</div>
				</div>
			</div>
			<div class="k-edit-buttons k-state-default text-right windowFooter">
				<a id='saveBtn' class="k-button k-button-icontext k-primary k-grid-update">
					<span class="k-icon k-update"></span>保存
				</a>
				<a id='cancelBtn' class="k-button k-button-icontext k-grid-cancel">
					<span class="k-icon k-cancel"></span>取消
				</a>
			</div>
			
			</div>
			
				<div role="tabpanel"  class="tab-pane" id="tab2">
					<div class="container-fluid">
						<div class="form-horizontal">
							<div class="form-group">
								<label class="col-md-2 control-label" >编辑Lua脚本</label>
								<div class="col-md-5">
									<textarea id="command-script" class="form-control" style="height: 350px;width:460px">#:script#</textarea>
								</div>
							</div>
							
						</div>
					</div>
				
				</div>
			
			</div-->
			
		</script>
		<div id='infoWindow' style="display: none;"></div>
		
		<div id="editLuaWindow" style="display: none;>
				<label class="col-md-2 control-label" >编辑Lua脚本</label>
				<textarea id="editLuaTextArea" class="form-control" style="height: 500px;"></textarea>
				<div class="k-edit-buttons k-state-default text-right windowFooter">
					<a id='saveLua' class="k-button k-button-icontext k-primary k-grid-update">
						<span class="k-icon k-update"></span>保存
					</a>
				</div>		
		</div>
		<@script></@script>
		<script src="custom/app/command/check-item.js"></script> 
		<script src="assets/prettify/prettify.js"></script> 
		<script src="assets/prettify/lang-lua.js"></script> 
	</body>

</html>