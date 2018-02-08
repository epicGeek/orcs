<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<#include "../common/ems-menu.ftl">
<!DOCTYPE html> 
<html>

	<head lang="zh-cn">
		<@header></@header>
	</head>

	<body>
		<!-- 用户信息 -->
		<!--顶部-->
		<@user_bar/>
		
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
					<div class="tab-content" style="padding-top: 20px;">
						<div role="tabpanel" class="tab-pane active" id="tab1">
							<input type="hidden" id="jobId" />
							<div class="form-horizontal">
								<div class="form-group">
									<label class="col-md-2 control-label" for="ip">方案类型</label>
									<div class="col-md-8">
										<select id='jobType' style='' style="float:left" class="form-control input-sm">
										</select><span id="hourInput" style="display:none"><input placeholder="小时" style="width:100px" id="hourText"></span>
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
										<input id='execDate' type="text" /> 
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
						<select id='neTypeList' class="form-control input-sm"></select>
						<select id='inputNeTrigger' class="form-control input-sm"></select>
						<select id='unitTypeList' class="form-control input-sm"></select>
						<input id="unitNameInput" type='text'  placeholder="单元名称" style="width: 150px;" title='网元名称'/>
					</div>
				</div>
				<div id='netElementGrid'></div>
				<div class="k-edit-buttons k-state-default text-right windowFooter">
				
								<br><a id='saveUnitBtns' class="k-button k-button-icontext k-primary k-grid-update">
									<span class="k-icon k-update"></span>保存
								</a>
				</div>
			</div>
		</div>
		<!-- 网元权限弹窗 end-->
		
		
		
		<!-- 网元、指令  -->	
		<div id='commandWindow' style="display: none;">
			<div class="container-fluid">
				<div class="form-inline ">
					<div class="form-group form-group-sm ">
						<input id="commandInput" type='text' class="form-control"  placeholder="指令名称" style="width:500px;" title='指令名称'/>
					</div>
				</div>
				<div id='commandGrid'></div>
				<div class="k-edit-buttons k-state-default text-right windowFooter">
								<br><a id='saveCommandBtns' class="k-button k-button-icontext k-primary k-grid-update">
									<span class="k-icon k-update"></span>保存
								</a>
				</div>
			</div>
		</div>
		<!-- 网元、指令 end -->
		
		
		<!-- 通知组  -->	
		<div id='groupWindow' style="display: none;">
			<div class="container-fluid">
				<div id='groupGrid'></div>
				<div class="k-edit-buttons k-state-default text-right windowFooter">
								<br><a id='saveGroupBtns' class="k-button k-button-icontext k-primary k-grid-update">
									<span class="k-icon k-update"></span>保存
								</a>
				</div>
			</div>
		</div>
		<!-- 通知组  end -->
					
		</div>
		<@script></@script>
		<script src="custom/app/ems/emsManage.js"></script>
	</body>

</html>