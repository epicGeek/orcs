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
					<input id="inputKeyWord" value="" type="text" class="form-control input-sm" placeholder="筛选指令组名称" style="width: 100%;" title="对指令组名称进行模糊查询"/>
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
	    	<div class="container-fluid">
				<div class="form-horizontal">
						<div class="form-group">
							<label class="col-md-2 control-label">指令组名称</label>
							<div class="col-md-5">
								<input id="page-commandGroupName" class="form-control input-sm" type="text" value='#:commandGroupName#' />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">指令组描述</label>
							<div class="col-md-5">
								<input id="page-commandGroupDesc" class="form-control input-sm" type="text" value='#:commandGroupDesc#' />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">网元类型</label>
							<div class="col-md-5">
								<select id='typeInput' class="form-control input-sm">
									
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">单元类型</label>
							<div class="col-md-5">
								<select id='unitTypeInput' class="form-control input-sm">
									
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">指令组级别</label>
							<div class="col-md-5">
								<select id='levelInput' class="form-control input-sm">
									<option value="高级命令">高级命令</option>
									<option value="维护命令">维护命令</option>
									<option value="查看命令">查看命令</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">备注</label>
							<div class="col-md-5">
								<input id="page-remark" class="form-control input-sm" type="text" value='#if(remark!=null&&remark!='null'){# #:remark# #}#' />
							</div>
						</div>
				</div>
			</div>
	    	<div class="k-edit-buttons k-state-default text-right windowFooter">
				<a id='saveBtn'  class="k-button k-button-icontext k-primary k-grid-update">
					<span class="k-icon k-update"></span>保存
				</a>
				<a id='cancelBtn'  class="k-button k-button-icontext k-grid-cancel">
					<span class="k-icon k-cancel"></span>取消
				</a>
			</div>
		</script>
		<div id='infoWindow' style="display: none;"></div>
		
		<div id='bindWindow' style="display: none;">
			<div class="container-fluid">
				<div class="form-inline">
					<div class="form-group form-group-sm  noMargin">
						<input id="searchCheckItem" type='text' class="form-control" placeholder="对指令组名称进行模糊查询" title=对指令组名称进行模糊查询" style="width: 270px;"/>
						<!--<button type="submit" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-search"></i> 查询</button>-->
						<button id="clearBtn" type="submit" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
					</div>
				</div>
				<div class="k-gridWrap">
					<button id='addAll' class="btn btn-xs btn-success">
						<i class="glyphicon glyphicon-plus-sign"></i> 全部加入
					</button>
					<button id='removeAll'  class="btn btn-xs btn-danger">
						<i class="glyphicon glyphicon-trash"></i> 全部移除
					</button>
				</div>
				<div id='bindGrid'></div>
			</div>
		</div>
		
		<@script></@script>
		<script src="custom/app/command/command-group.js"></script> 
	</body>

</html>