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
		<div class="container-fluid" >
			<div class="row">
				<div class="col-sm-1 col-xs-12">
					<div id='addBtn' class="addWrap clearfix text-center ">
						<label><i class="glyphicon glyphicon-plus-sign " ></i> 添加</label>
						<span></span>
					</div>
				</div>
				<div class="col-sm-4  col-xs-12">
					<input id="inputKeyWord" value="" type="text" class="form-control input-sm" placeholder="对阈值名称进行模糊查询" style="width: 100%;" title="对阈值名称进行模糊查询" />
				</div>
				<div class="col-sm-5 col-xs-12">
					<button  id='clearsearch'  class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12">
					<div id = "dataGrid"></div>
				</div>
			</div>
		</div>
		<!--//内容部分-->
		<!--修改弹窗-->
		<div id='windowTemplate' style="display: none;">
			<div class="container-fluid">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="name">KPI名称</label>
						<div class="col-md-5">
							<input id="kpiName" class="form-control input-sm" type="text"
								value='#:kpiName#' placeholder="请输入KPI名称" style="width: 100%;" title="KPI名称不能为空！"/>
						</div>
					</div>
				</div>
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="name">KPI代号</label>
						<div class="col-md-5">
							<input id="kpiCode" class="form-control input-sm" type="text"
								value='#:kpiCode#' placeholder="请输入此KPI代号" style="width: 100%;" title="KPI代号开放对象为运维和开发人员，是作为其他功能接口的存在。可以为空，但我们并不建议您这样做。"/>
						</div>
					</div>
				</div>
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="name">数据源</label>
						<div class="col-md-5">
							<input id="dataSourceName" class="form-control input-sm" type="text"
								value='#:dataSourceName#' placeholder="请输入此KPI数据源" style="width: 100%;" />
						</div>
					</div>
				</div>
				
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="name">KPI数据类型</label>
						<div class="col-md-5">
							<input id="outPutField" class="form-control input-sm" type="text"
								value='#:outPutField#' placeholder="请输入此KPI数据类型" style="width: 100%;" title="KPI数据类型指，该KPI计算后的结果是什么样子的。比如“鉴权成功率”，这个KPI是一种比值，且描述的是成功率。"/>
						</div>
					</div>
				</div>
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="name">KPI分类</label>
						<div class="col-md-5">
							<input id="kpiCategory" class="form-control input-sm" type="text"
								value='#:kpiCategory#' placeholder="请输入此KPI归属分类" style="width: 100%;" title="根据业务对KPI进行分类"/>
						</div>
					</div>
				</div>
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="name">比较符号</label>
						<div class="col-md-5">
							<input id="compareMethod" class="form-control input-sm" type="text"
								value='#:compareMethod#' placeholder="请输入KPI与其门限的比较关系" style="width: 100%;" title="比如某种KPI，如果它的值低于门限产生告警，那么这里应该填“<”"/>
						</div>
					</div>
				</div>
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="name">监控时间段</label>
						<div class="col-md-5">
							<input id="monitorTimeString" class="form-control input-sm" type="text"
								value='#:monitorTimeString#' placeholder="请输入监控时间段" style="width: 100%;" title="举个例子，如果希望在早上8点到晚上10点监控这个KPI，应该输入“8-22”。"/>
						</div>
					</div>
				</div>
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="name">样本基数</label>
						<div class="col-md-5">
							<input id="requestSample" class="form-control input-sm" type="text"
								value='#:requestSample#' placeholder="请输入样本基数" style="width: 100%;" title="例如“鉴权成功率”这条KPI的样本基数是鉴权请求次数。如果鉴权请求次数低于了样本基数，说明此时这条KPI样本量不足，不具有参考性，即使低于门限也不触发告警。"/>
						</div>
					</div>
				</div>
				
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="name">门限值</label>
						<div class="col-md-5">
							<input id="threshold" class="form-control input-sm" type="text"
								value='#:threshold#' placeholder="请输入门限值" style="width: 100%;" title="当KPI和门限值满足比较关系时触发告警"/>
						</div>
					</div>
				</div>
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="name">取消门限值</label>
						<div class="col-md-5">
							<input id="thresholdCancel" class="form-control input-sm" type="text"
								value='#:thresholdCancel#' placeholder="请输入取消门限值" style="width: 100%;" title="KPI触发告警后，恢复正常的门限值。例如某台设备的“鉴权成功率”的值为80%，样本基数足够，且发生于监控时段内，这条KPI的门限为90%，比较关系为“<”。显然80%<90%已经成立，触发了告警。假如设置取消门限为92%,那么在下一个周期，假设这台设备的“鉴权成功率”恢复到了95%，95%>92%，超过了取消门限值，那么认为该告警已取消。"/>
						</div>
					</div>
				</div>
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-md-2 control-label" for="name">KPI提取公式</label>
						<div class="col-md-5">
							<textarea id="kpiQueryScript"  class="form-control" rows="5" cols="5"   placeholder="请输入此KPI提取公式"  title="提取KPI的公式。语言为ORACLE SQL。为了方便后台处理数据，我们对SQL的某些字段进行了处理，在您配置新的KPI时，请咨询相关维护人员如何改写您的SQL">
							#:kpiQueryScript#
							</textarea>
						</div>
					</div>
				</div>
				<div id='unitInfoList'> </div>
				<div class="k-edit-buttons k-state-default text-right windowFooter">
					<a id='saveBtn'
						class="k-button k-button-icontext k-primary k-grid-update"> <span
						class="k-icon k-update"></span>保存
					</a> <a id='cancelBtn' class="k-button k-button-icontext k-grid-cancel">
						<span class="k-icon k-cancel"></span>取消
					</a>
				</div>
			</div>
		</div>
		<!--修改弹窗END-->

			</div>
			</div>
		<!-- 尾部 -->
		<@tailer></@tailer>
		<!-- 尾部 -->
		<@script></@script>
		<script src="custom/app/report/kpiThresholdConf.js"></script>
	</body>
</html>