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
<div class="body-content">
    <div class="container" style="width:100%">
        <div class="row">
        <!-- 右侧内容  -->
        <div class="col-xs-12 content-right">
            <div class="col-md-12 right-content">
                    <div class="console-body">
                    <div class="search-item clearfix">
                    		<input id="check" name="alarmCheck" value="start" checked="checked" type = "radio" />活动告警
                    		<input name="alarmCheck" value="cancel"  type = "radio" />取消告警
                        	<input  id="alarmCell" >
                        	<input  id="alarmLevel" >
                        	<input type="text" id="textValue" value="" class="k-textbox" placeholder="根据单元模糊查询">
                            <input type="text" id="alarmId" value="" class="k-textbox" placeholder="根据告警ID进行查询">
                            <input type="text" id="alarmNum" value="" class="k-textbox" placeholder="根据告警号进行查询">
                            <input type="text" id="startAlarmTime">-
                            <input type="text" id="endAlarmTime">
                  
                            <button class="btn btn-primary"  id="search"><i class="glyphicon glyphicon-search"></i> 查询</button>
                            <button class="btn btn-default"  id="reset"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
                        	<button class="btn btn-primary  btn-sm"  id="export" ><i class="glyphicon glyphicon-export"></i> 导出</button>
                        	<a class="btn btn-xs btn-success" href='alarmMonitor?filterType=filterType' onclick="javascript:void(0)"><i class="glyphicon glyphicon-plus"></i> 查看自定义告警</a>
                </div>
                    </div>
                    <div id="grid"></div>
                    
                     <div id="grid1"></div>
                    <script type="text/x-kendo-template" id="template">
                    <div class="tabstrip">
                        <ul>
                            <li class="k-state-active">
                                       基本信息
                            </li>
                        </ul>
                        <div>
                            
                            <div class='employee-details'>
                            <ul >
                                <!-- <li><label>告警ID:</label>#= notifyId #</li> -->
                                <!--<li><label>告警单元:</label>#= alarmCell #</li>-->
                                <!--<li><label>告警等级:</label>#= alarmLevel #</li>-->
                                <!--<li><label>告警号:</label>#= alarmNo #</li>-->
                                <!--<li><label>告警时间:</label>#= receiveStartTime #</li>-->
                                <!--<li><label>告警关闭时间:</label>#= cancelTime #</li>-->
                                <li><label>网元名称:</label>#= neName#</li>
                                <!--<li><label>网元CODE:</label>#= neCode#</li>-->
                                <li><label>NOTIFY ID:</label>#= notifyId#</li>
                                <li><label>附加信息:</label>#= supplInfo #</li>
                                <li><label>USER INFO:</label>#= userInfo#</li>
                                <li><label>告警内容:</label>
                                	<div style="white-space:normal; width:100%;">#= alarmText#
                                	</div>
                                </li>
                                <li><label>告警描述:</label>
                                	<div name="desc" style="white-space:normal; width:100%;">
                                	</div>
                                </li>
                                
                            </ul>
                        </div>
                        </div>
                        <div>
                            
                        </div>
                    </div>
                </script>
                
                <script type="text/x-kendo-template" id="template1">
                    <div class="tabstrip1">
                        <ul>
                            <li class="k-state-active">
                                      	 基本信息
                            </li>
                        </ul>
                        <div>
                            <div class='employee-details'>
                            <ul >
                                <li><label>告警描述:</label>
                                	<div name="desc" style="white-space:normal; width:100%;">
                                	</div>
                                </li>
                            </ul>
                        </div>
                        </div>
                        <div>
                        </div>
                    </div>
                </script>

                </div>

                </div>
            </div>
        </div>
    </div>
</div>
<!--底部-->
<@tailer></@tailer>
		
	    
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

<myScript>
<@script></@script>
<script type="text/javascript" src="../assets/kendoui2015.1.318/js/jszip.min.js"></script>
<script src="custom/app/alarm/alarmRecord.js"></script>
</myScript>
</body>
</html>