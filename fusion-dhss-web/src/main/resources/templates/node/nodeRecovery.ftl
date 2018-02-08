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
<@user_bar/>
		<!--顶部导航栏-->
		<@navigate_bar/>
		
		
		<!--内容部分-->
		<div class="container-fluid">
			<div class="row noMarginBottom">
				<div class="col-sm-5 marginTop10">
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
										<label>单元名称</label>
										<input type="text" name="" id="inputKeyWord" value="" class="form-control"/>
									</div>
									<!--<div class="form-group form-group-xs">
										<button id='addButton' class="btn btn-success btn-sm" >添加操作列表</button>
									</div>-->
									
								</div>
							</div>
							<input id="webSite" type="hidden" name="webSite" value="${webSite}"/>
							<input id="unitId" type="hidden" name="unitId" value="${unitId}"/>
							<input id="neTypeId" type="hidden" name="neTypeId" value="${neTypeId}"/>
							<div id='unitGrid'></div>
						</div>
						<div class="panel-footer text-right">
							
						</div>
					</div>
				</div>
				<div class="col-sm-4 marginTop10 noPaddingLeft">
					<div class="panel panel-default">
						<div class="panel-heading">执行步骤</div>
						<div class="panel-body">
							<div id='stepGrid'></div>
						</div>
					</div>
				</div>
				<div class="col-sm-3 marginTop10 noPaddingLeft">
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
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
<!--内容部分
<div class="body-content">
    <div class="container">
        <div class="row">
             
            <div class="col-xs-12 content-right">
                <div class="col-md-12 right-content">

                    <div class="console-title clearfix">
                        <div class="pull-left">
                            <h4>节点恢复<span class="fa fa-question-circle" popover="" popover-trigger="mouseenter" popover-placement="right"></span></h4>
                        </div>
                        <div class="pull-right">
                            <a class="btn btn-xs btn-primary" href="nodeRecoveryList"><i class="glyphicon glyphicon-calendar"></i> 待恢复节点列表</a>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-8 console-body">
                            <div class="search-item text-center">
                                <span>网元类型：</span> <input id="inputSite" placeholder="请选择..." /> 
                                <span>网元：</span> <input id="inputType" placeholder="请选择..." />
                                <span>节点：</span> <input id="inputNode" placeholder="请选择..." />

                            </div>

                            <div class="search-item text-center">
                                <button id="addButton" class="btn btn-primary"><i class="glyphicon glyphicon-plus"></i> 添加到操作列表</button>
                            </div>
                            <div class="search-item clearfix">
                                <p>恢复<mark id="site"></mark> - <mark id="neType"></mark> - <mark id="unit"></mark>节点</p>
                            </div>

                            <div class="table-responsive">
                                <table class="table table-hover table-striped table-responsive table-bordered">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>步骤说明</th>
                                        <th>步骤描述</th>
                                        <th>状态</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="stepId">
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="col-sm-4">
                            <pre class="pre-scrollable" style="max-height: 420px">
                            </pre>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>-->

<!--弹出信息-->
<div class="modal fade" id="myBackupModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">节点恢复</h4>
            </div>
            <div class="modal-body">
                确定要开始恢复吗？
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button id="isRecovery" type="button" class="btn btn-primary">开始恢复</button>
            </div>
        </div>
    </div>
</div><!--//弹出信息-->
<@tailer></@tailer>
		<!--//底部-->
		<@script></@script>
<script src="custom/app/node/nodeRecovery.js"></script>
</body>
</html>
