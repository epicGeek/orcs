<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<!DOCTYPE html>
<html>
<head lang="zh-cn">
    <@header></@header>
    <style type="text/css" media="screen">
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
    </style>
</head>
<body>
<!-- 用户信息 -->
		<@user_bar/>
		<!--顶部导航栏-->
		<div class="n-nav n-nav-bg">
			<div class="container-fluid">
				<div class="row noMarginTop noMarginBottom">
					<div class="col-md-2 col-sm-3 col-xs-10">
						<div class="clearfix">
							<div class="n-navbar-title">
									<a href="welcome" class="btn n-icon n-icon-32 n-icon-color3" role="button">
										<i class="glyphicon glyphicon-menu-left"></i>
									</a>
									&nbsp;<font id="parentMenuName"></font>
							</div>
						</div>
					</div>
					<div class="col-md-offset-1 col-md-9 col-sm-9 col-xs-14" style="float:right; _position:relative;">
						<div id="topNavListWrap" class="n-navbar-nav clearfix" >
							<div id="topNavList">
								<ul id='navList' class="nav navbar-nav">
								</ul>
							</div>
							<button id='overFlowBtn' class="btn btn-info">
								<span class="glyphicon glyphicon-align-justify"></span>
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>

<!--内容部分-->
<div class="container-fluid">
        <div class="row">
            <div class="col-sm-12">
               <!-- <nav style="padding: 0px 0px 10px 0px">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="alarm-new">活动告警</a></li>
                        <li><a href="alarm-new?history=history">历史告警</a></li>
                    </ul>
                </nav> -->
                <div id='split'>
                    <div>
                        <div class="panel panel-default">
                            <div class="panel-body noPadding">
                                <div id="left_pane_Tabstrip">
                                    <ul>
                                        <li class="k-state-active">
                                            网络拓扑
                                        </li>
                                        <li>
                                            查询条件
                                        </li>
                                        <li id="love">
                                            我的收藏
                                        </li>
                                    </ul>
                                    <div>
                                        <div id='categoryTreeview'></div>
                                        <script id="treeview-template" type="text/kendo-ui-template"> 
                                            #: item.text #   
                                            <small class='small'>( #:item.value#  
                                            - <span 
                                            #if(item.dayCount>0){# 
                                            style="color:red"
                                            #}#
                                            >#:item.dayCount#</span> 
                                            
                                            )</small> 
                                            #if(item.flag){ if(item.collection){# 
                                            <code class="collectionIconBtn collectioned" title='取消收藏'><i class="glyphicon glyphicon-star"></i></code> 
                                            #} else {#
                                            <code class="collectionIconBtn noCollectioned" title='收藏'>
                                            <i class="glyphicon glyphicon-star-empty"></i> </code> 
                                            #}}#
                                        </script>
                                        
                                        
                                        <script id="toolTip-template" type="text/x-kendo-template">
                                            <p>#=target#</p>
                                        </script>
                                    </div>
                                    <div>
                                        <br>
                                        <br>
                                        <div class="form-inline">
                                            <div class="form-group">
                                                <b>起始&nbsp;&nbsp;&nbsp;</b>
                                                <input type="text" id='startTime' style="width:200px;">
                                            </div>
                                        </div>
                                        <div class="form-inline">
                                            <div class="form-group">
                                                <b>结束&nbsp;&nbsp;&nbsp;</b>
                                                <input type="text" id='endTime' style="width:200px;">
                                            </div>
                                        </div>
                                        <div class="form-inline">
                                            <div class="form-group">
                                                <b>告警号</b>
                                                <input id="alarmNum" type="text" class="form-control input-sm" style="width:180px;">
                                            </div>
                                        </div>
                                        <div class="form-inline">
                                            <div class="form-group">
                                                <b>关键字</b>
                                                <input id = "core" type="text" class="form-control input-sm" style="width:180px;">
                                            </div>
                                        </div>
                                        <div class="form-inline">
                                            <div class="form-group">
                                                <b>描述&nbsp;&nbsp;&nbsp;</b>
                                                <textarea id="desc" class="form-control input-sm" style="width:180px;height: 100px;"></textarea>
                                            </div>
                                        </div>
                                        <hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
                                        <div class="form-inline">
                                            <div class="form-group">
                                                <b>非重要告警号<span style="color:red">(多个用下划线隔开)</span></b> 
                                                <textarea id="notImportant" style="width:100%;height: 150px;"></textarea><br/>
                                                <button id='addNotImportant' style="float:right;" class="btn btn-primary btn-sm"> 保存 </button>
                                                <button id='clearNotImportant' style="float:right;" class="btn btn-default btn-sm"> 清空 </button>
                                                
                                            </div>
                                        </div>
                                    </div>
                                    <div>
                                        <div id='myCollectionGrid'></div>
                                    </div>
                                </div>
                            </div>
                            <div class="panel-footer text-right">
                                <button id='queryBtn' class="btn btn-primary btn-sm">
                                    <span class="glyphicon glyphicon-search"></span> 查询
                                </button>&nbsp;&nbsp;
                                <button id='resetBtn' class="btn btn-default btn-sm">
                                    <span class="glyphicon glyphicon-repeat"></span> 重置
                                </button>&nbsp;&nbsp;
                                <button id='collectConditionBtn' class="btn btn-danger btn-sm">
                                    <span class="glyphicon glyphicon-star-empty"></span> 收藏查询条件
                                </button>
                                <button id='cancelMultiCollectbtn' class="btn btn-danger btn-sm" style='display: none;'>
                                    <span class="glyphicon glyphicon-star"></span> 批量取消收藏
                                </button>
                            </div>
                        </div>
                    </div>
                    <div>
                        <div class="panel panel-default">
                            <div class="panel-body noPadding"  >
                                <div id="right_pane_Tabstrip" >
                                    <ul>
                                        <!--  <li class="k-state-active">
                                   HSSFE-01  <i class='removeTabBtn glyphicon glyphicon-remove'></i>
                               </li> -->
                                    </ul>
                                    <!-- <div>
                                <div class="dataGrid"></div>
                            </div> -->
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
    
    <!--底部-->
<@tailer></@tailer>
    <script type="text/x-kendo-template" id="template">
        <div class='detailsStyle'>
            <h4>基本信息：</h4>
            <ul>
                <li>
                    <label>网元名称：</label>#= neName #</li>
                <li>
                    <label>网元CODE：</label>#= neCode #</li>
                <li>
                    <label>NOTIFY ID：</label>#= id #</li>
                <li>
                    <label>附加信息：</label>#= addInfo #</li>
                <li>
                    <label>USER INFO：</label>#= userInfo #</li>
                <li>
                    <label>告警内容：</label>#= content #</li>
                <li>
                    <label>告警描述：</label>#= desc #</li>
            </ul>
        </div>
    </script>

		
	    
<!--弹出信息-->
<div class="modal fade" id="myExportModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">设备告警导出</h4>
            </div>
            <div class="modal-body">
                表格导出成Excel文件？
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" id="doExport" class="btn btn-primary">开始</button>
            </div>
        </div>
    </div>
</div>
<div id='windowTemplate' style="display: none;">
				<textarea  style=" overflow:scroll;overflow-x:visible; height:300px;width:100%;" id = "logText">
				</textarea>
			</div>
<myScript>
<@script></@script>
<script type="text/javascript" src="../assets/kendoui2015.1.318/js/jszip.min.js"></script>
<script src="custom/app/alarm-new/alarmMonitor.js"></script>
</myScript>
</body>
</html>