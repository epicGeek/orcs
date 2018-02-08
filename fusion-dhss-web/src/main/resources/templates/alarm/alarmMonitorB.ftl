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

<input id = "param" type='hidden' value = "${inputAlarmType}">
<div class="body-content">
    <div class="container" style="width:100%">
        <div class="row">
            <!-- 右侧内容  -->
            <div class="col-xs-12 content-right">
                <div class="col-md-12 right-content">

                    <div>
                        <div class="pull-right">
                        <!--<a class="btn btn-xs btn-success" href='alarmMonitor' onclick="javascript:void(0)"><i class="glyphicon glyphicon-plus"></i> 查看设备告警</a>-->
                    </div>
                    </div>
                    <div class="console-body">
                        <div class="search-item clearfix">
                            <div style="min-width:500px;float: left">
                                <span style="display:none">网元类型：<input id="inputNeType" placeholder="请选择网元类型."/></span>
                                <input id="inputNeName" value=""/>
                                <input id="inputAlarmType" placeholder="请选择告警类型." />
                                <span>时间选择：</span>
                                <input type="text" id="startTime">-
                                <input type="text" id="endTime">
                            <button id ='searchalarm' class="btn btn-primary"><i class="glyphicon glyphicon-search"></i> 查询</button>
                            <button id ='clearsearchalarm' class="btn btn-default"><i class="glyphicon glyphicon-repeat"></i> 重置</button>
                            </div>
                        </div>

                       <div id="gridNodeRecovery" style="font-size: 12px">
                       </div>
                    </div>

                </div>
            </div>
        </div>
    </div>

</div>


<!--底部-->
		<div class="n-footer">
			<div class="container" >
				<div class="footer-bottom">
					The system developed by Nokia
				</div>
			</div>
		</div>
<myScript>

<script type="text/javascript">
    $(function(){
    
        $("ul.expmenu li > div.header").click(function()
        {
            var arrow = $(this).find("i.arrow");

            if(arrow.hasClass("glyphicon-menu-up"))
            {
                arrow.removeClass("glyphicon-menu-up");
                arrow.addClass("glyphicon-menu-down");
            }
            else if(arrow.hasClass("glyphicon-menu-down"))
            {
                arrow.removeClass("glyphicon-menu-down");
                arrow.addClass("glyphicon-menu-up");
            }

            $(this).parent().find("ul.menu").slideToggle();
        });
        $("ul.expmenu li > ul.menu li").click(function(e){
            $("ul.expmenu").find("li").removeClass("active");
            $(this).addClass("active");
        });
        currentPage();
    });

    function currentPage(){
        if (!document.getElementById("n-navbar-main-nav")) return false;/*进行必要的测试，避免没有id为nav时候出错*/
        var currenturl=window.location.href;/*获取当前页面的地址*/

        var nav=document.getElementById("n-navbar-main-nav");
        var links=nav.getElementsByTagName("a");
        for(i=0;i<links.length;i++){
            var url=links[i].getAttribute("href");/*获取链接的href值*/

            if(currenturl.indexOf(url)!=-1){/*如果链接的href值在当前页面地址中有匹配*/
                links[i].className="active";
			}
        }
    }
    
    function showDetails(e) {
        location.href='NodeRecovery.html';
    }
    
    
    $("#hiddenInputNeName").val("${neName!''}");
    $("#hiddenNeType").val("${neType!''}");
    $("#hiddenAlarmType").val("${alarmType!''}");
    $("#hiddenStartTime").val("${startTime!''}");
    $("#hiddenEndTime").val("${endTime!''}");
    </script>
</myScript>
<script type="text/javascript" src="../assets/plugins/bootstrap-3.3.2-dist/js/bootstrap.js"></script>

<!-- echarts -->
<script type="text/javascript" src="../assets/plugins/kendoui2015.1.318/js/kendo.all.min.js"></script>
<script type="text/javascript" src="../assets/plugins/kendoui2015.1.318/js/cultures/kendo.culture.zh-CN.min.js"></script>
<script type="text/javascript" src="../assets/plugins/kendoui2015.1.318/js/messages/kendo.messages.zh-CN.min.js"></script>

<script type="text/javascript" src="../assets/js/nav.js"></script>
<script type="text/javascript" src="../assets/js/dataSource.js"></script>
<script type="text/javascript" src="../assets/plugins/echarts-2.2.1/dist/echarts.js"></script>
<script src="custom/app/alarm/alarmMonitor.js"></script>
</body>
</html>