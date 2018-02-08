<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<#include "../common/header.ftl">
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

	
	<div class="body-content">
		<div class="container-fluid">
			<div class="row">
                <!-- 左侧内容  -->
                <!-- 左侧内容结束  -->

                <!-- 右侧内容  -->
                <div>

					<div class="form-group form-inline">
						<label>厂家告警号</label> 
						<input type="text" id="paraAlarmNo" class="form-control" style="width: 400px;"/>
						<button class="btn btn-primary"  id="searchBtn"> <i class="glyphicon glyphicon-search"></i> 查询</button>
						<button class="btn btn-default" id="clearBtn" > <i class="glyphicon glyphicon-repeat"></i> 重置 </button>
					</div>
					
					<div id='ruleList'></div>
					<script type="text/x-kendo-template" id="template">
		                <div class="toolbar" >
		                	<button id='addRule' class='btn btn-xs btn-success'> <i class='glyphicon glyphicon-plus-sign'></i> 新增告警规则 </button>
		                	<button id='deleteRule' class='btn btn-xs btn-danger'> <i class='glyphicon glyphicon-remove-sign'></i> 删除告警规则 </button>
		                	<!--<button id='exportRule' class='btn btn-xs btn-warning'> <i class='glyphicon glyphicon-cloud-download'></i> 下载数据 </button>
		                	<button id='addRule' class='btn btn-xs btn-primary'> <i class='glyphicon glyphicon-import'></i> 批量修改数据 </button>-->
		                </div>
		            </script>
		            
				</div>
				
			</div>
			
		</div>
	</div>
	
	<div id='ruleInfoWindow' style="display: none;height:500px;" >
	<script type="text/x-kendo-template" id="ruleInfoWindowTemplate">
		<div class="container-fluid" >
		
			<div id='bsInfoValidate' class="forge-heading">
			
				<div class="form-horizontal" >
				
					<div class="form-group">
						<label class="col-md-4 control-label" for="alarmNo">告警号</label>
						<div class="col-md-3">
							<input class="form-control input-sm" type="text" id="alarmNo" name="alarmNo" value = "#:alarmNo#" pattern="^[0-9]*$" validationMessage='只能是数字' required data-required-msg="必填" />
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="alarmNo"></span>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="faultId">FaultID</label>
						<div class="col-md-3">
							<input class="form-control input-sm" type="text" id="faultId" name="faultId" value="#:faultId#" pattern="^[0-9]*$" validationMessage='只能是数字' required data-required-msg="必填" />
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="faultId"></span>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="alarmContent">告警标题</label>
						<div class="col-md-3">
							<input class="form-control input-sm" type="text" id="alarmContent" name="alarmContent" value='#:alarmContent#' required data-required-msg="必填" />
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="alarmContent"></span>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="alarmDesc">告警描述</label>
						<div class="col-md-3">
							<input class="form-control input-sm" type="text" id="alarmDesc" name="alarmDesc" value='#:alarmDesc#' required data-required-msg="必填" />
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="alarmDesc"></span>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="alarmName">告警标准名</label>
						<div class="col-md-3">
							<input class="form-control input-sm" type="text" id="alarmName" name="alarmName" value='#:alarmName#' required data-required-msg="必填" />
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="alarmName"></span>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="onEquipment">事件对设备的影响</label>
						<div class="col-md-3">
							<input class="form-control input-sm" type="text" id="onEquipment" name="onEquipment" value='#:onEquipment#' required data-required-msg="必填" />
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="onEquipment"></span>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="onBusiness">事件对业务的影响</label>
						<div class="col-md-3">
							<input class="form-control input-sm" type="text" id="onBusiness" name="onBusiness" value='#:onBusiness#' required data-required-msg="必填" />
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="onBusiness"></span>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="alarmRemark">告警解释</label>
						<div class="col-md-3">
							<input class="form-control input-sm" type="text" id="alarmRemark" name="alarmRemark" value='#:alarmRemark#' required data-required-msg="必填" />
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="alarmRemark"></span>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="notifyImmediately">是否立即通知</label>
						<div class="col-md-3">
							<input class="form-control input-sm" type="text" id="notifyImmediately" name="notifyImmediately" value='#:notifyImmediately#' data-text-field="notifyImmediatelyView" data-value-field="notifyImmediately" required data-required-msg="必填" />
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="notifyImmediately"></span>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="isImportant">是否重大告警</label>
						<div class="col-md-3">
							<input class="form-control input-sm" type="text" id="isImportant" name="isImportant" value='#:isImportant#' data-text-field="isImportantView" data-value-field="isImportant" required data-required-msg="必填" />
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="isImportant"></span>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="totalSumInterval">全网告警个数门限判断时长</label>
						<div class="col-md-3">
							<input class="form-control input-sm" type="text" id="totalSumInterval" name="totalSumInterval" value='#:totalSumInterval#'  data-text-field="totalSumIntervalView" data-value-field="totalSumInterval" required data-required-msg="必填" />
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="totalSumInterval"></span>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="totalSumLimit">全网告警个数门限</label>
						<div class="col-md-3">
							<input class="form-control input-sm" type="text" id="totalSumLimit" name="totalSumLimit" value='#:totalSumLimit#' pattern="^[0-9]*$" validationMessage='只能是数字' required data-required-msg="必填" />
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="totalSumLimit"></span>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="vipSumInterval">VIP基站告警个数门限判断时长</label>
						<div class="col-md-3">
							<input class="form-control input-sm" type="text" id="vipSumInterval" name="vipSumInterval" value='#:vipSumInterval#' data-text-field="vipSumIntervalView" data-value-field="vipSumInterval" required data-required-msg="必填" />
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="vipSumInterval"></span>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="vipSumLimit">VIP基站告警个数门限</label>
						<div class="col-md-3">
							<input class="form-control input-sm" type="text" id="vipSumLimit" name="vipSumLimit" value='#:vipSumLimit#' pattern="^[0-9]*$" validationMessage='只能是数字' required data-required-msg="必填" />
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="vipSumLimit"></span>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="generalSumInterval">普通基站告警个数门限判断时长</label>
						<div class="col-md-3">
							<input class="form-control input-sm" type="text" id="generalSumInterval" name="generalSumInterval" value='#:generalSumInterval#'  data-text-field="generalSumIntervalView" data-value-field="generalSumInterval"  required data-required-msg="必填" />
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="generalSumInterval"></span>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-4 control-label" for="generalSumLimit">普通基站告警个数门限</label>
						<div class="col-md-3">
							<input class="form-control input-sm" type="text" id="generalSumLimit" name="generalSumLimit" value='#:generalSumLimit#' pattern="^[0-9]*$" validationMessage='只能是数字' required data-required-msg="必填" />
						</div>
						<div class="col-md-3">
						 	<span class="k-invalid-msg" data-for="generalSumLimit"></span>
						</div>
					</div>
					
				</div>
				
			</div>
			    	<div class="k-edit-buttons k-state-default text-right windowFooter">
			<a id='saveRule'  class="k-button k-button-icontext k-primary k-grid-update">
				<span class="k-icon k-update"></span>保存
			</a>
			<a id='cancelRule'  class="k-button k-button-icontext k-grid-cancel">
				<span class="k-icon k-cancel"></span>取消
			</a>
		</div>
			</script>
		</div>
		
		

	</div>


		<div class="n-footer">
			<div class="container">
				<div class="footer-bottom">
					The system developed by Nokia
				</div>
			</div>
		</div>
    
	
<script type="text/javascript" src="../assets/plugins/bootstrap-3.3.2-dist/js/bootstrap.js"></script>
<!-- echarts -->
<script type="text/javascript" src="../assets/plugins/kendoui2015.1.318/js/kendo.all.min.js"></script>
<script type="text/javascript" src="../assets/plugins/kendoui2015.1.318/js/cultures/kendo.culture.zh-CN.min.js"></script>
<script type="text/javascript" src="../assets/plugins/kendoui2015.1.318/js/messages/kendo.messages.zh-CN.min.js"></script>
<script type="text/javascript" src="../assets/js/nav.js"></script>
<script type="text/javascript" src="../assets/js/dataSource.js"></script>
<script type="text/javascript" src="../assets/plugins/echarts-2.2.1/dist/echarts.js"></script>

<script type="text/javascript" src="custom/app/alarm/alarmManage.js" ></script>
</body>
</html>