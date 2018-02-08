<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<#include "../common/navigate-bar.ftl">
<!DOCTYPE html>
<html>
	<head>
	  <@header></@header>
	</head>
   

	<head lang="zh-cn">
		<title>用户数据管理</title> 
		<@header></@header>
		<myStyle>
		  
		</myStyle>
	</head>

	<body>
	   <!-- 用户信息 -->
		<@user_bar/>
		<@navigate_bar/>
		
        <input type="hidden" id="moduleName" value="${module_name}">
		<div class='row'>
			   <div class='col-md-4' style="padding-right:0">
					<div class='panel'>
						<div class='panel-heading bg-success'>
							<div class='panel-title'>
								选择指令
							</div>
						</div>
						<div class="form-inline">
						  <div class="form-group form-group-sm">
							<input id="subtoolType" placeholder="请选择命令类型" />
						  </div>
						  <div class="form-group form-group-sm">
							<input  id="cmdName" type="text" style="height:32px; width:235px" class="form-control" placeholder="筛选指令名称" title="对指令名称进行模糊查询">
						  </div>
					    </div>
					
						<div id='commandGrid'></div>
					</div>
					<!-- <div class='panel'> -->
					<div class='col-sm-6 col-sm-offset-3'>
						<button  id="submit" class='btn btn-success' style='width: 100%;'>
						<i class="glyphicon glyphicon-ok"></i>提交执行</button>
					</div>
			   </div>
			   <div class='col-md-8'>
			        <div class='panel'>
						<div class='panel-heading bg-danger'>
							<div class='panel-title'>
								执行结果
							</div>
						</div>
						<div class="form-inline text-right"  style="padding-top:10px">
						  <div class="form-group form-group-sm">
							<input id="exeResult" placeholder="请选择结果类型" />
						  </div>
						  <div class="form-group form-group-sm">
							<input id='startTime' type="text" /> - 
							<input id='endTime' type="text" />
						  </div>
						  <div class="form-group form-group-sm">
							<input  id="inputBtn" type="text" style="height:32px; width:300px" class="form-control" placeholder="筛选用户号码、检查项" title="对用户号码、检查项进行模糊查询">
						  </div>
						  <!-- <button class="btn btn-primary" id="searchBtn"><i class="glyphicon glyphicon-search"></i> 查询</button> -->
					    </div>
					    
						<div  style="padding-top:10px">
							<div id="resultData" ></div>
						</div>
					</div>
			   </div>
		  </div>
			
		<!-- confirmCommand Start -->
		<div style="display: none;" id="confirmCommandModal">
	       <input type="hidden" id="commandName">
				<div class="modal-body">
					<div class="form-horizontal" role="form">
					   <div class="form-group">
							<label class="col-sm-2 control-label">执行命令</label>
							<div class="col-sm-6">
								<textarea style="width: 384px; height: 160px;"   id='form-confirm-command' disabled="disabled">
								</textarea>
							</div>
						</div>
							<div class="form-group">
								<button id="executeCommandButton" class="btn btn-success" style="margin-left: 159px;width: 365px;">
									<span class="glyphicon glyphicon-ok"></span> 确定
								</button>
							</div>
					</div>
				</div>
		</div>
		<!-- confirmCommand End -->
		
			<div style="display: none;" id="modal_dialg"></div>
			
			<!-- 用户日志 Modal -->
			<div class="modal fade" id="logModal" tabindex="-1" role="dialog" aria-labelledby="webModalLabel" aria-hidden="true" data-backdrop="static">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
								</button>
								<h4 class="modal-title" id="webModalLabel">
									<span class='glyphicon glyphicon-user'></span>
									查看用户日志<span id='accountLabel'></span>
								</h4>
							</div>
							<div class="modal-body">
								<p><input id="cond-log" type="text" class="form-control" placeholder="筛选查询号段、操作人"></p>
								<table id='tableListLog' class="table table-striped" cellspacing="0">
											<thead>
												<tr>
													<th>日志时间</th>
													<th>查询号段</th>
													<th>操作人</th>
													<th>下载</th>
													<th></th> 
												</tr>
											</thead>
											<tbody>
											</tbody>
								</table>
							</div>
						</div>
					</div>
		    </div>
			
			<div style="display: none;" id="selectorModal" >
		       <div class="modal-body">
		          <div role="form" class="form-horizontal">
		            <div align="center" class="form-group" style="margin-right: -5px;">
			              <label class="control-label text-right" style='margin: 0px;line-height: 34px;'>
							  	<input type="radio" class="radioText" value="0" name='radio2' checked="checked"/>&nbsp;单用户操作 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						  </label>
						  <label class="control-label text-right" style='margin: 0px;line-height: 34px;'>
						  	<input type="radio" class="radioText" value="1" name='radio2'/>&nbsp;批量用户操作
						  </label>
	                       <button id="selectorButton" type="button" class="btn btn-default btn btn-primary" style="width: 100px;margin-left: 30px;">
	                       	<span class="glyphicon glyphicon-ok"></span>确定</button>
		              <div class="col-sm-offset-2 col-sm-5">
					   </div>
					</div>
		          </div>
		         </div>
	          </div>
			  <div class="form-group" id="uploadNumber" style="display: none;">
					<div class="col-sm-9">
						<input type="file" id="fileName" name="fileName" onchange="ajaxFileUpload()">
					</div>
			  </div>
			  
		</div>
		
		<div id="textModal" style="display: none;">
			  <textarea  rows="20" id="logContext" style="width: 800px; height: 400px;resize:none"></textarea>
		</div>
		
		<span id="popupNotification"></span>
		 <!--底部-->
		<@tailer></@tailer>
	    <@script></@script>
		<script src="custom/app/common.js"></script>
		<script type="text/javascript" src="assets/plugins/jquery/ajaxfileupload.js"></script>
		<script src="custom/app/advanced/userDataManage.js"></script>
		<script src="custom/app/advanced/userDataUtil.js"></script>
		
	 </myScript> 
				
   </body>
</html>