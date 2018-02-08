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
				<div class="col-sm-4  col-xs-12">
					<input id="inputKeyWord" value="" type="text" class="form-control input-sm" placeholder="筛选方案名称、方案描述" style="width: 100%;" title="对MSISDN号段、IMSI号段进行模糊查询"/>
				</div>
				<div class="col-sm-5 col-xs-12">
					<button  id='clearsearch' class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
					<button  id='allstop' class="btn btn-danger btn-sm"><i class="glyphicon glyphicon-repeat"></i> 全部停止</button>
				</div>
			</div>
			<div class="row ">
				<div class="col-sm-12">
					<div id='dataGrid'></div>
				</div>
			</div>
		</div>
		<!--//内容部分-->

		<!--底部-->
		<@tailer></@tailer>
		<!--//底部-->
		
		<!-- 添加、修改  -->	
		<div id='infoWindow' style="display: none;">
			<div class="container-fluid">
				<div>
					<!-- Nav tabs -->
					<ul class="nav nav-tabs" role="tablist">
						<li role="presentation" class="active"><a href="#tab1" aria-controls="tab1" role="tab" data-toggle="tab">基本信息</a></li>
					</ul>
					<!-- Tab panes -->
					<div class="tab-content" style="padding-top: 20px;">
						<div role="tabpanel" class="tab-pane active" id="tab1">
							<input type="hidden" id="jobId" />
							<div class="form-horizontal">
								<div class="form-group">
									<label class="col-md-2 control-label" for="ip">方案类型</label>
									<div class="col-md-8">
										<select id='jobType' style='width:350px' class="form-control input-sm">
											<!-- <option value="4">15分钟</option>
											<option value="5">每小时</option> -->
											<option value="1">每天</option>
											<option value="2">每周</option>
											<option value="3">每月</option>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-2 control-label">方案名称</label>
									<div class="col-md-8">
										<input id ="jobName" placeholder="方案名称" class="form-control input-sm" type="text" value=''/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-2 control-label" for="ip">方案描述</label>
									<div class="col-md-8">
										<textarea required="" placeholder="方案描述" rows="5"  id="jobDesc" class="form-control"></textarea>
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-2 control-label">开始时间</label>
									<div class="col-md-8">
										<input id='startTime' type="text" /> 
									</div>
								</div>
							</div>
							<div class="k-edit-buttons k-state-default text-right windowFooter">
								<a id='saveBaseInfoBtn' class="k-button k-button-icontext k-primary k-grid-update">
									<span class="k-icon k-update"></span>保存基本信息
								</a>
								<a class="cancelBtns k-button k-button-icontext k-grid-cancel">
									<span class="k-icon k-cancel"></span>取消
								</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 添加、修改 end -->	
		
		
		<!-- 网元权限弹窗-->
		<div id='netElementWindow' style="display: none;">
			<div class="container-fluid">
				<div class="form-inline ">
					<div class="form-group form-group-sm ">
						<label>网元</label>
						<select id='inputNeTrigger' class="form-control input-sm"></select>
						<label>网元类型</label>
						<select id='neTypeList' class="form-control input-sm"></select>
						<label>单元类型</label>
						<select id='unitTypeList' class="form-control input-sm"></select>
					</div>
					<div class="form-group form-group-sm ">
						<label>筛选</label>
						<input id="unitNameInput" type='text' class="form-control" placeholder="单元名称" style="width: 300px;" title='网元名称'/>
					</div>
					<button id="unitBtnSelect" type="submit" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-search"></i> 查询</button>
					<button id="unitClearInput" type="submit" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
				</div>
				<div class="k-gridWrap">
					<button id='addAllNetElement' class="btn btn-xs btn-success">
						<i class="glyphicon glyphicon-plus-sign"></i> 全部加入
					</button>
					<button id='removeAllNetElement' class="btn btn-xs btn-danger">
						<i class="glyphicon glyphicon-trash"></i> 全部移除
					</button>
				</div>
				<div id='netElementGrid'></div>
			</div>
		</div>
		<!-- 网元权限弹窗 end-->
		
		
		
		<!-- 网元、指令  -->	
		<div id='unitWin' style="display: none;">
			<div class="container-fluid">
				<div>
					<!-- Tab panes -->
					<div class="tab-content" style="padding-top: 20px;">
						<div role="tabpanel" class="tab-pane active" id="tab3">
							<div class="form-horizontal">
								<div class="form-group">
									<div class="col-md-15">
										<div id="treeView"></div>
										<input id ="itemName" placeholder="对指令名称、指令内容、网元类型进行模糊查询" class="form-control input-sm" type="text" value=''/>
										<table id='tableListCheckItem' class="table table-striped table-responsive" style="padding-left: 15px">
													<thead>
														<tr>
															<th>指令名称</th>
															<th>指令内容</th>
															<th>适用网元类型</th>
															<th><button id="saveItemBtn" type='button'  class='btn btn-default btn-xs'>全部加入/移除</button></th>
														</tr>
													</thead>
													<tbody>
													</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 网元、指令 end -->			
		</div>
		<@script></@script>
		<script src="custom/app/smart/smart.js"></script>
	</body>

</html>