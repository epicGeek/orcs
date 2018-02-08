<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<#include "../common/navigate-bar.ftl">
<!DOCTYPE html>
<html>
<head lang="zh-cn">
    
    <@header></@header>
    <link rel="stylesheet" href="../assets/plugins/bootstrap-3.3.2-dist/css/bootstrap.css" />
	<script type="text/javascript" src="../assets/js/d3.v3.min.js"></script>
   <style>

        .node circle {
            fill:yellow ;
            stroke: #ccc;
            stroke-width: 1.5px;
        }

        .node {
            font: 10px sans-serif ;
        }

        .link {
            fill: none;
            stroke: #ccc;
            stroke-width: 1.5px;
        }

    </style>
</head>


<body>

<@user_bar/>
<!--顶部导航栏-->
<div class="n-nav n-nav-bg" style="padding-top:50px;">
	<div class="container-fluid">
		<div class="row noMarginTop noMarginBottom">
			<div class="col-md-4 col-sm-4  col-xs-12">
				<div class="clearfix">
					<div class="n-navbar-title">
						<a href="welcome" class="btn n-icon n-icon-32 n-icon-color3" role="button">
							<i class="glyphicon glyphicon-menu-left"></i>
						</a>
						&nbsp;拓扑图
					</div>
				</div>
			</div>
			<div class="col-md-8 col-sm-8 col-xs-12">
				<div id="topNavListWrap" class="n-navbar-nav clearfix" >
					<div id="topNavList">
						<ul id='navList' class="nav navbar-nav">
							 
						</ul>
					</div>
					 
				</div>
			</div>
		</div>
	</div>
</div>
<!--//顶部导航栏-->
<div id="topologicalmap" style="">
 
</div>

<input id="dhssName" type="hidden" value="${HSSNAME }" />
<div id='nodePanel' class="pull-right" style="font-size: 12px; width:320px;height:400px;position:fixed ;right: 10px;top:100px;">
    <div class="panel panel-default">
        <div class="panel-heading"><span class="eNodeBName" style="width: auto"></span>基本信息
            <div class="pull-right">
                <a role="button" tabindex="0" class="btn btn-default btn-xs closePanel">关闭</a>
            </div>
        </div>
        <div class="panel-body" style="padding: 5px"  style="margin:0px;"  >
            <style>
                #topKpi{
                    margin: 0px;
                    padding: 0px;
                    clear: both;
                }
                .customScrollbar{
                    min-height: 200px;
                    margin-left: 0px;
                    margin-right: 0px;
                }
                .customScrollbar li{
                    line-height: 18px;
                    font-size: 12px;
                    color: #777;
                    padding: 5px 12px;
                    cursor: pointer;

                }
                .customScrollbar li:hover{
                    background: #EBF1FB;
                    color: #0066CC;

                }
                .customScrollbar li.active{
                    background: #EBF1FB;
                    color:#0066CC;

                }
            </style>
            <ul class="" id="topKpi">
                <li>
                    <span class="kpiname">位置：</span>
                    <span class="kpinum location"> </span>
                </li>
                <li>
                    <span>硬件版本：</span><span class="idsVersion"> </span>
                </li>
                <li>
                    <span class="kpiname">软件版本：</span><span class="kpinum swVersion"> </span>
                </li>
                <li>
                    <span class="kpiname">状态：</span><span class="kpinum neState"> </span>
                </li>
                <li>
                    <span class="kpiname">描述：</span><span class="kpinum remarks"></span>
                </li>
            </ul>
            <div style="clear: both"></div>
            <!--<hr style="border-top: 1px solid #dddddd;width:100%; margin-bottom: 10px">-->

            <ul class="nav nav-tabs nav-justified" id="mytab">
                <li role="presentation" class="active">
                    <a class="dropdown-toggle" style="padding: 10px 2px" data-toggle="tab" href="#tabvranKpi" role="button" aria-haspopup="true" aria-expanded="false">
                        KPI指标
                    </a>
                </li>
                <li role="presentation">
                    <a class="dropdown-toggle" style="padding: 10px 2px" data-toggle="tab" href="#tabvranAlarm" role="button" aria-haspopup="true" aria-expanded="false">
							告警
                    </a>
                </li>
            </ul>

            <div class="tab-content" style="padding-top: 5px;padding:0 15px;overflow:scroll;overflow-x:hidden;">
                <div class="tab-pane active" id="tabvranKpi">
                    <div id='resultWrap' class="customScrollbar">
                        <ul>
                             
                        </ul>
                    </div>
                </div>
                <div class="tab-pane" id="tabvranAlarm">
                    <div id='result2Wrap' class="customScrollbar">
                        <ul>
                            
                        </ul>
                    </div>
                </div>
            </div>


        </div>
    </div>
</div>
<div id='nodeAhubPanel' class="pull-right" style="display:none;font-size: 12px; width:320px;position:absolute;right: 10px;top:100px;">
    <div class="panel panel-default">
        <div class="panel-heading"><span class="eNodeBName" style="width: auto"></span>基本信息
            <div class="pull-right">
                <a role="button" tabindex="0" class="btn btn-default btn-xs closePanel">关闭</a>
            </div>
        </div>
        <div class="panel-body" style="padding: 5px"  style="margin:0px;"  >
            <style>
                #topKpi{
                    margin: 0px;
                    padding: 0px;
                    clear: both;
                }
                .customScrollbar{
                    min-height: 200px;
                    margin-left: 0px;
                    margin-right: 0px;
                }
                .customScrollbar li{
                    line-height: 18px;
                    font-size: 12px;
                    color: #777;
                    padding: 5px 12px;
                    cursor: pointer;

                }
                .customScrollbar li:hover{
                    background: #EBF1FB;
                    color: #0066CC;

                }
                .customScrollbar li.active{
                    background: #EBF1FB;
                    color:#0066CC;

                }
                #table-5 thead th {
                    background-color: #EBF1FB;
                    color: #000;
                    border-bottom-width: 0;
                }

                /* Column Style */
                #table-5 td {
                    color: #000;
                    text-align: center;
                }
                /* Heading and Column Style */
                #table-5 tr, #table-5 th {
                    border-width: 1px;
                    border-style: solid;
                    border-color: #EBF1FB;
                }

                /* Padding and font style */
                #table-5 td, #table-5 th {
                    line-height: 16px;
                    padding: 5px 2px;
                    font-size: 12px;
                    font-family: Verdana;

                }
            </style>
            <div id='result3Wrapa' class="customScrollbar" style="padding:0 15px;overflow:scroll;overflow-y:hidden;">
                <TABLE id="table-5" style="BORDER-COLLAPSE: collapse;" width="800px">
                    <thead>
                    <th width="100px">
                        <div align=center>Port</div></>
                    <th width="150px">
                        <div align=center>Mode</div></th>
                    <th width="150px">
                        <div align=center>LAN</div></th>
                    <th width="150px">
                        <div align=center>VLAN ID</div></th>
                    <th width="150px">
                        <div align=center>IP address</div></th>
                    <th width="150px">
                        <div align=center>Cable to Equipment</div></th>
                    <th width="150px">
                        <div align=center>Cable to Port</div></th>
                    <th width="150px">
                        <div align=center>Up Link IP Address</div></th></>

                    </thead>
                    <tbody>
                     
                    </tbody>
                </TABLE>

            </div>
        </div>
    </div>
</div>
<div style="clear: both"></div>

<@tailer></@tailer>
		<!--//底部-->
   <@script></@script>

<!--自定义滚动条-->
<script type="text/javascript" src="../assets/plugins/mCustomScrollbar/jquery.mCustomScrollbar.concat.min.js"></script>
<script type="text/javascript" src="custom/app/topology/d3topological.js"></script>

</body>
</html>