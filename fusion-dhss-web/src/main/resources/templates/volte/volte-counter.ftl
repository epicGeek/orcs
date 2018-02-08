<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<!DOCTYPE html>
<html>
	<head lang="zh-cn">
		<@header>
		</@header>
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
		
	</head>
	
<body class="page-product">
		<!--顶部-->
		<!-- 用户信息 -->
		<@user_bar/>

		<!--顶部导航栏-->
		<@navigate_bar/>

		<!--内容部分-->
		<div class="container-fluid">
        	<div class="row">
            	<div class="col-sm-12">
					<div id="grids">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-12 col-sm-12 col-xs-12">
									<div class="form-group form-group-sm">
										<label for="name">时间范围</label>
										<input id='startTime' type="text" /> -
										<input id='endTime' type="text" /> &nbsp;&nbsp;&nbsp;&nbsp;
										<button class="btn btn-primary btn-sm" id="queryDataBtn" onclick = "loadDataGrid()" ><i class="glyphicon glyphicon-search"></i> 查询</button>
              							<button class="btn btn-info btn-sm" id="downloadFileBtn"><i class="glyphicon glyphicon-export"></i> 下载</button>
									</div>
									<div id='dataGrid'></div>
										<ul id="menu">
										<li types='volteCounter'>
											<i class='glyphicon glyphicon-signal'></i> 历史趋势
										</li>
								</ul>
									</div>
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
			</div>

		<div id='kpiWindow' style="display: none;">
						<div class="panel panel-default">
							<div class="panel-body">
								<div id='kpiChart' style="width: 100%;height:280px;"></div>
							</div>
						</div>
					</div>
		<!--//内容部分-->
		<!--底部-->
		<@tailer></@tailer>
		<!--//底部-->
		<@script></@script>
		<script type="text/javascript" src="assets/plugins/echarts-2.2.5/dist/echarts-all.js"></script>
		
		<script src="custom/app/volte/volte-counter.js"></script> 
		

</body>
</html>

