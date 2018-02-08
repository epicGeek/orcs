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
		<!--顶部-->
		<!-- 用户信息 -->
		<@user_bar/>

		<!--顶部导航栏-->
		<@navigate_bar/>
	 <!--//顶部导航栏-->
	 
	 <input type="hidden"  id="type" value="${type}" class="form-control"/>
	 <input type="hidden"  id="itemunitId" value="${unitId}" class="form-control"/>
	 <div class="container-fluid">
			<div class="row noMarginBottom">
				<div class="col-sm-5 marginTop10">
					<div class="panel panel-default panelLighter">
						<div id = "itemTitle" class="panel-heading">单元倒换</div>
						<div class="panel-body ">
							<div class="row noMarginTop">
								<div class="form-inline noPadding">
									<div class="form-group form-group-xs">
										<label>站点</label>
										<input class="form-control" id='locationInput'>
									</div>
									<div class="form-group form-group-xs">
										<label>网元</label>
										<input class="form-control" id='neNameInput'>
										</input>
									</div>
									<div class="form-group form-group-xs">
										<label>单元</label>
										<input type="text" name="" id="unitInput" value="" class="form-control"/>
									</div>
									<div class="form-group form-group-xs">
										<label>操作</label>
										<input type="text" name="" id="actionInput" value="" class="form-control"/>
									</div>
									<!--<div class="form-group form-group-xs">
										<button id='addButton' class="btn btn-success btn-sm" >添加操作列表</button>
									</div>-->
									
								</div>
							</div>
							<div id='unitGrid'></div>
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
						<div class="panel-heading" >执行结果--<button onclick='clearInfo()' class="btn btn-danger btn-xs" >清除记录</button></div>
						<div class="panel-body">
							<div id='resultGrid' style="height:355px;"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--//内容部分-->
	    <!--底部-->
		<@tailer></@tailer>
	 
	 
	 
	 
	 
	 
	<!-- 
	<div id="example"> 
   <div class="demo-section k-header"> 
    <div id="tabstrip"> 
     <ul> 
      <li class="k-state-active"> 网元倒换 </li> 
      <li> 板卡倒换 </li> 
     </ul> -->
     
     <!-- <div> 
      <span class=""> </span> 
      网元倒换内容部
	<div class="body-content">
		<div class="container">
			<div class="row">
				<!-- 右侧内容  -->
				<div class="col-xs-12 content-right">
					<div class="col-md-12 right-content">

						<div class="console-title clearfix">
						<!--
							<div class="pull-left">
								<h4>
									板卡倒换<span class="fa fa-question-circle" popover=""
										popover-trigger="mouseenter" popover-placement="right"></span>
								</h4>
							</div>
							 
							<div class="pull-right">
							<a class="btn btn-xs btn-primary" onclick="searchHistory()"><i class="glyphicon glyphicon-calendar"></i> 查看历史</a>
						</div>
						</div>
						<div class="row">
							<div class="col-sm-8 console-body">
								<div class="search-item text-left">
									<span>站点：</span> <input id="inputSite" placeholder="请选择..." />
									<span>网元：</span> <input id="inputNe" placeholder="请选择..." /> 
									<span>场景：</span><input id="inputCase" placeholder="请选择..." />
									<!-- 
									<span>节点类型：</span><input id="inputUnitType" placeholder="请选择..." />
									  
								</div>
								<div class="search-item text-left">
								
								<span>操作：</span><input id="inputAction" placeholder="请选择..." />
								</div>
								<!--
								<div class="search-item text-center">
									<span>节点1名称：</span> <input id="inputUnit1" placeholder="请选择..." />
									<span>节点2名称：</span> <input id="inputUnit2" placeholder="请选择..." />
								</div>
								 
								<div class="search-item text-center">
									<button id="addButton" class="btn btn-primary">
										<i class="glyphicon glyphicon-plus"></i> 添加到操作列表
									</button>
								</div>
								<!--
								<div class="search-item clearfix">
									<p>
										倒换
										<mark id="site"></mark>
										-
										<mark id="ne"></mark>
										-
										<mark id="unitType"></mark>
										-
										<mark id="unit1"></mark>
										节点 至 -
										<mark id="unit2"></mark>
										节点
									</p>
								</div>
								 
								<div id="detailed" class="search-item clearfix">
								
								</div>

								<div class="table-responsive">
									<table
										class="table table-hover table-striped table-responsive table-bordered">
										<input id="id" type="hidden" />
										<thead>
											<tr>
												<th>#</th>
												<th>步骤描述</th>
												<th>操作指令</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody id="stepId">

										</tbody>
									</table>
								</div>
							</div>
							<div class="">
								<pre class="pre-scrollable" style="max-height: 480px">                               

                            </pre>
							</div>
						</div>


					</div>
				</div>
			</div>
		</div>

	</div>
     </div>
        
     <div> 
      <span class=""> </span> 
      <div class="body-content">
    <div class="container">
        <div class="row">
            <!-- 右侧内容   
            <div class="col-xs-12 content-right">
                <div class="col-md-12 right-content">

                    <div class="console-title clearfix">
                    	 
                        <div class="pull-left">
                            <h4>节点隔离<span class="fa fa-question-circle" popover="" popover-trigger="mouseenter" popover-placement="right"></span></h4>
                        </div>
                        
                        <div class="pull-right">
							<a class="btn btn-xs btn-primary" onclick="searchSingleHistory()"><i class="glyphicon glyphicon-calendar"></i> 查看历史</a>
						</div>
                    </div>
                    <div class="row">
                        <div class="col-sm-8 console-body">
                            <div class="search-item text-left">
                                <span>站点：</span>
                                <input id="inputSite2" placeholder="请选择..." />
								
                                <span>网元：</span>
                                <input id="inputNe2" placeholder="请选择..." />

                                <span>板卡：</span>
                                <input id="inputNode2" placeholder="请选择..." />
                            </div>
                            <div class="search-item text-left">
								
								<span>操作：</span><input id="inputAction2" placeholder="请选择..." />
								</div>

                            <div class="search-item text-center">
                                <button id="addButton2" class="btn btn-primary"><i class="glyphicon glyphicon-plus"></i> 添加到操作列表</button>
                            </div>
                            <div class="search-item clearfix">
                                <p><mark id="action2"></mark> <mark id="site2"></mark> - <mark id="ne2"></mark> - <mark id="unit2"></mark>节点</p>
                            </div>
                            <div class="table-responsive">
                                <table class="table table-hover table-striped table-responsive table-bordered">
                                <input id="id2" type="hidden" />
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>步骤描述</th>
                                        <th>操作指令</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="stepId2">

                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="">
                            <pre class="pre-scrollable2" style="max-height: 480px">
                                
                            </pre>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div> 
     </div> 
    </div> 
   </div> --> 
   <!--
   <style>
                .sunny, .cloudy, .rainy {
                    display: block;
                    margin: 30px auto 10px;
                    width: 128px;
                    height: 128px;
                    background: url('../content/web/tabstrip/weather.png') transparent no-repeat 0 0;
                }

                .cloudy{
                    background-position: -128px 0;
                }

                .rainy{
                    background-position: -256px 0;
                }

                .weather {
                    margin: 0 auto 30px;
                    text-align: center;
                }

                #tabstrip h2 {
                    font-weight: lighter;
                    font-size: 5em;
                    line-height: 1;
                    padding: 0 0 0 30px;
                    margin: 0;
                }

                #tabstrip h2 span {
                    background: none;
                    padding-left: 5px;
                    font-size: .3em;
                    vertical-align: top;
                }

                #tabstrip p {
                    margin: 0;
                    padding: 0;
                }
            </style> 
            
  </div>  -->
	<!--网元倒换 弹出信息
	<div class="modal fade" id="myBackupModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">网元倒换</h4>
				</div>
				<div class="modal-body">确定要开始倒换吗？</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" onclick="doSwitch()">开始倒换</button>
				</div>
			</div>
		</div>
	</div>-->
	<!--//网元倒换 弹出信息-->
	<!--板卡倒换 弹出信息-->
<div class="modal fade" id="myBackupModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">板卡倒换</h4>
            </div>
            <div class="modal-body">
                确定要开始倒换吗？
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" onclick="doSwitch2()">开始倒换</button>
            </div>
        </div>
    </div>
</div>
<!--//板卡倒换 弹出信息-->
	
		<!--//底部-->
		<@script></@script>
		 
		<script src="custom/app/node/nodeSwitching.js"></script>
</body>
</html>