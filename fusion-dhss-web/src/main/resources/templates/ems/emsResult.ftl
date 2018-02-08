<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<#include "../common/ems-menu.ftl">
<#include "../common/navigate-bar.ftl">
<!DOCTYPE html>
<html>

	<head lang="zh-cn">
		<@header></@header>
		<link rel="stylesheet" href="assets/css/nsn-home-style.css">
	</head>
	

     <style type="text/css" media="screen">
     
     		.circle {
				border-radius: 50%;
				width: 10px;
				height: 10px;
				/* 宽度和高度需要相等 */
			}
			.detailsStyle li {
		        word-break: break-all;
		    }
		    
		    .detailsStyle label {
		        font-size: 14px;
		    }
		    #split,#split >div{
		        min-height:500px;
		        border:0px;
		        overflow:visible;
		    }
		    /*网络拓扑*/
		    .collectioned,
		    .noCollectioned {
		        margin-left: 5px;
		    }
		    .collectioned {
		        color: #FF4D00;
		    }
		    .collectioned:hover {
		        color: #888;
		        background: #fff;
		    }
		    
		    .noCollectioned {
		        color: #888;
		    }
		    
		    .noCollectioned:hover {
		        color: #FF4D00;
		        background: #fff;
		    }
		    
		    .small {
		        color: #888;
		    }
		    
		    .k-state-selected .small {
		        color: #fff;
		    }
		    /*左侧tab*/
		    
		    #left_pane_Tabstrip {
		        border: 0px;
		        background: #fff;
		    }
		    
		    #left_pane_Tabstrip .k-content {
		        height: 400px;
		    }
		    /*kendo tab*/
		    
		    .k-tabstrip>.k-content {
		        border: 0px;
		        padding-top: 10px;
		        overflow: auto;
		        margin: 0px;
		    }
		    
		    .k-tabstrip .k-tabstrip-items {
		        border-bottom: 1px solid #ddd;
		        background: #efefef;
		    }
		    
		    .k-tabstrip-items .k-loading.k-complete {
		        border: 0px;
		    }
		    
		    .k-tabstrip-items .k-link {
		        padding: 4px 6px;
		    }
		    /*右侧tab的删除按钮*/
		    .removeTabBtn {
		        position: relative;
		        top: -3px;
		        right: -3px;
		    }
		    .removeTabBtn:hover {
		        color: red;
		    }
		    td.level1{
		        background:yellow;
		    }
		    td.level2{
		        background:orange;
		    }
		    td.level3{
		        background:#FF5151;
		    }
		    /*悬浮框*/
		    
		    .k-widget.k-tooltip {
		        padding: 2px;
		    }
		    
		    .k-tooltip table {
		        width: 100%;
		        line-height: 28px;
		        background: #fff;
		        color: #333;
		    }
		    
		    .k-tooltip th {
		        width: 60px;
		        text-align: right;
		        padding-right: 10px;
		        vertical-align: top;
		    }
		    
		    .k-tooltip td {
		        text-align: left;
		    }
		    
		    .collectionA {
		        cursor: pointer;
		    }
		    .contextMenu:hover {
		        background: #DBE6FD;
		    }
		    .k-popup .k-item.k-first, .k-virtual-item.k-first{
		        border-bottom:0px;
		    }
		    .k-widget.k-popup.k-context-menu{
		        background-color: #4298E0;
		        color: #fff;
		    }
		    .k-widget.k-popup.k-context-menu li:hover{
		        background: #0066CC;
		    }
		    .k-widget.k-popup.k-context-menu li:hover span{
		        color: #fff;
		    }
        </style>

	<body>

		<!--顶部-->
		<@user_bar/>
		
		<!--顶部导航栏-->
	    <@navigate_bar/>
	    <!--//顶部导航栏-->
<audio id="audiot" style="display:none" controls="controls">
<source src="audios/DearDeer.ogg" type="audio/ogg">
你的浏览器不支持html5的audio标签
</audio>
        <!--内容部分-->
        <div class="container-fluid">
        <div class="row">
            <div class="col-sm-12">
               <!-- <nav style="padding: 0px 0px 10px 0px">
                    <ul class="nav nav-tabs">
						<li style="cursor:pointer;" class="active" name='clickType'><a >MSS</a></li>
						<li style="cursor:pointer;" name='clickType'><a >MGW</a></li>
                    </ul>
					
                </nav>-->


				<div id="grids">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<!--<div class="form-inline">
									<div class="form-group form-group-sm">
										<label>时间范围</label>
									</div>
									<div class="form-group form-group-sm">
										<button type="submit" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-search"></i> 查询</button>
										<button type="submit" class="btn btn-default btn-sm"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
									</div>
								</div>-->
								<div class="form-group form-group-sm">
									<input id="groups" />
								</div>
								<div id='dataGrid'></div>
								<ul id="menu">
										<li types='kpi'>
											<i class='glyphicon glyphicon-signal'></i> KPI历史趋势
										</li>
										<li types='notice' count='1'>
											<i class='glyphicon glyphicon-remove'></i> 暂停通知		
										</li>
										<li types='notice' count='2'>
											<i class='glyphicon glyphicon-remove'></i> 暂停该指标（行）通知
										</li>
										<li types='notice' count='3'>
											<i class='glyphicon glyphicon-remove'></i> 暂停该单元（列）通知
										</li>
										<li types='cancel' count='4'>
											<i class='glyphicon glyphicon-ok'></i> 恢复通知		
										</li>
										<li types='cancel' count='5'>
											<i class='glyphicon glyphicon-ok'></i> 恢复该指标（行）通知
										</li>
										<li types='cancel' count='6'>
											<i class='glyphicon glyphicon-ok'></i> 恢复该单元（列）通知
										</li>
										<li types='download'>
											<i class='glyphicon glyphicon-download-alt'></i> 报文下载
										</li>
								</ul>
							</div>
						</div>
					</div>
					<div id='kpiWindow' style="display: none;">
						<div class="panel panel-default">
							<div class="panel-body">
								<div id='kpiChart' style="width: 100%;height:280px;"></div>
							</div>
						</div>
					</div>
				</div>
            </div>
            <div class="col-sm-3">
            </div>
            <div class="col-sm-9 noPaddingLeft">
            </div>
        </div>
    </div>
   
    </div>
    
    <!-- errorMessage  -->	
		<div id='errorMessageWindow' style="display: none;">
			<div class="container-fluid">
				<div>
					<div class="tab-content" style="padding-top: 20px;">
						<div role="tabpanel" class="tab-pane active" id="tab1">
							<input type="hidden" id="jobId" />
							<div class="form-horizontal">
								<div class="form-group"  >
									<div id='errorGrid'></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 添加、修改 end -->	



		<!-- 添加、修改  -->	
		<div id='infoWindow' style="display: none;">
			<div class="container-fluid">
				<div>
					<div class="tab-content" style="padding-top: 20px;">
						<div role="tabpanel" class="tab-pane active" id="tab1">
							<input type="hidden" id="jobId" />
							<div class="form-horizontal">
								<div class="form-group">
									<label class="col-md-2 control-label">停止通知至</label>
									<div class="col-md-8">
										<input id='endTime' type="text" /> 
									</div>
								</div>
							</div>
							<div class="k-edit-buttons k-state-default text-right windowFooter">
								<a id='saveBaseInfoBtn' class="k-button k-button-icontext k-primary k-grid-update">
									<span class="k-icon k-update"></span>确认
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

		<!--底部-->
		<@tailer></@tailer>
		<!--//底部-->
		<!--引用主页上方数值显示脚本-->
		<@script></@script>
		<script type="text/javascript" src="assets/plugins/echarts-2.2.5/dist/echarts-all.js"></script>
        <script src="custom/app/ems/emsResult.js"></script>
		
	</body>

</html>