<#assign basePath=requestContext.contextPath>
<#macro user_bar>  
		<input id="message" type="hidden" value="${message}">
		<#if (message??)>
			<script>
				if($("#message").val())
					infoTip({content: $("#message").val()});
			</script>
		</#if>
		<div class="nav navbar navbar-fixed-top navBox">
			<div class="container-fluid">
				<div class="col-md-6 col-sm-6 col-xs-12">
					<a href="welcome"><img src="${basePath}/assets/n-images/nLogo2.png" class="img-responsive" /></a>
				</div>
				<div class="com-md-6 col-sm-6 col-xs-12">
					<div id='account' class="pull-right">
						<a title="修改密码" id="userInfoLink"></a>&nbsp;&nbsp;
						<a title="退出" href="logout"><i class="glyphicon glyphicon-log-out"></i></a>
					</div>
				</div>
			</div>
		</div>
		
		
		<div id='passwordWindow' style="display: none;">
			<div class="container-fluid">
				<div>
					<!-- Nav tabs -->
					<!-- Tab panes -->
					<div class="tab-content" style="padding-top: 20px;">
						<div role="tabpanel" class="tab-pane active" id="tab1">
						<form>
							<div class="form-horizontal">
								
								<div class="form-group">
									<label class="col-md-2 control-label">原密码</label>
									<div class="col-md-8">
										<input id ="my_password_old" placeholder="原密码" class="form-control input-sm" type="password" value=''/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-2 control-label" for="ip">新密码</label>
									<div class="col-md-8">
										<input id ="passwordNew" placeholder="新密码" class="form-control input-sm" type="password" value=''/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-md-2 control-label" for="ip">确认新密码</label>
									<div class="col-md-8">
										<input id ="passwordNewFlag" placeholder="确认新密码" class="form-control input-sm" type="password" value=''/>
									</div>
								</div>
							</div>
							<div class="k-edit-buttons k-state-default text-right windowFooter">
								<a id='savePasswordInfoBtn' class="k-button k-button-icontext k-primary k-grid-update">
									<span class="k-icon k-update"></span>保存
								</a>
								<a class="cancelPasswordBtns k-button k-button-icontext k-grid-cancel">
									<span class="k-icon k-cancel"></span>取消
								</a>
							</div>
						 </form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 添加、修改 end -->	
		
		
		
		<script>
			$(function(){
				$.ajax({
					url:"my/profile?time="+new Date().getTime(),
					method:"GET",
					success:function(data){
						if(data.delayPass){
							passwordWindow.obj.setOptions({
								 actions: [],
								"title": "密码过期，请修改密码"
							});
							passwordWindow.initContent();
							$(".cancelPasswordBtns").hide();
						}
						$("#userInfoLink").html(data.realName);	
					}
				});
				
				$("#userInfoLink").on("click",function(){
					
					passwordWindow.obj.setOptions({
						actions: ["Close"],
						"title": "修改密码"
					});

					passwordWindow.initContent();
					$(".cancelPasswordBtns").show();
				})
				//修改、增加弹窗
				var passwordWindow = {
					obj: undefined,
					template: undefined,
					id: $("#passwordWindow"),
					saveClick: function() {
						//保存 【添加】单元基础信息
						$("#savePasswordInfoBtn").on("click", function() {
							var my_password_old = $("#my_password_old").val();
							var passwordNew = $("#passwordNew").val();
							var passwordNewFlag = $("#passwordNewFlag").val();
							if(my_password_old == "" ){
								infoTip({content: "请输入原密码",color:"red"});
								return;
							}
							if(passwordNew == "" ){
								infoTip({content: "请输入新密码",color:"red"});
								return;
							}
							if(passwordNewFlag == "" ){
								infoTip({content: "请输入确认新密码",color:"red"});
								return;
							}
							if(passwordNew != passwordNewFlag){
								infoTip({content: "俩次新密码不一致！",color:"red"});
								return;
							}
							
							if(passwordNew.length < 8){
								infoTip({content: "新密码长度小于8",color:"red"});
								return;
							}
							if(passwordNew.indexOf(";") >= 0){
								infoTip({content: "密码中不能含有 ' ; '",color:"red"});
								return;
							}
							
							var re = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[`~!@#$^&*\(\)=\|\{\}':,\\\[\].<>\?]).{6,}$/;
							var result=  re.test(passwordNew);
							if(!result){
								infoTip({content: "必须包含特殊字符,大小写字母及数字",color:"red"});
								return;
							}
							
							var map = { my_password_old : my_password_old , passwordNew : passwordNew };
							$.ajax({
					            url : "my/password",
					            type : "PATCH",
					            data: kendo.stringify(map),
					            dataType: "json",
					            contentType: "application/json;charset=UTF-8",
					            success : function(data) {
					            	if(data){
					            		passwordWindow.obj.close();
					    	        	infoTip({content: "保存成功！",color:"#D58512"});
					            	}
					            },error:function(data){
					            	infoTip({content: data.responseJSON.message,color:"red"});
					            }
							});
							
						});
					},
					initContent: function() {
						
						//记录Root密码、默认登录用户名、登录密码原数据：编辑时标示是否改变
						//…………………………
						$("#my_password_old").val("");
						$("#passwordNew").val("");
						$("#passwordNewFlag").val("");
						$('a[data-toggle="tab"][href="#tab1"]').tab('show');//显示第一个面板
						passwordWindow.obj.center().open();
						
						
					},
		
					init: function() {
		
						if (!passwordWindow.id.data("kendoWindow")) {
							passwordWindow.id.kendoWindow({
								width: "700px",
								actions: ["Close"],
								modal: true,
								title: "修改密码"
							});
						}
						passwordWindow.obj = passwordWindow.id.data("kendoWindow");
						//点击【保存基本信息】按钮
						passwordWindow.saveClick();
						//取消按钮
						$(".cancelPasswordBtns").on("click", function(){
							passwordWindow.obj.close();
						});
					}
				};
			
		
				passwordWindow.init();
			});
			
		</script>
</#macro>  