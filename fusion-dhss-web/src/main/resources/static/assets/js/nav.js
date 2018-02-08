$(function(){
	
	$("body").on("click",function(){
		if($("#topNavListWrap").hasClass("active")){
			$("#topNavListWrap").removeClass("active");
		}
	});
	//点击按钮，顶部显示隐藏掉的导航
	$("#overFlowBtn").on("click",function(e){
		e.stopPropagation();
		if($("#topNavListWrap").hasClass("active")){
			$("#topNavListWrap").removeClass("active");
		} else {
			$("#topNavListWrap").addClass("active");
		}
	});
	
	//没有被隐藏的顶部导航，则隐藏按钮
	if($("#navList").height() <= $("#topNavListWrap").height()){
		$("#overFlowBtn").hide();
	} else {
		$("#overFlowBtn").show();
	}
	$(window).on("resize",function(){
		//没有被隐藏的顶部导航，则隐藏按钮
		if($("#navList").height() <= $("#topNavListWrap").height()){
			$("#overFlowBtn").hide();
		} else {
			$("#overFlowBtn").show();
		}
	});
});

var navList = [
	{
		parent:"网元统一登录",
		children:[
			{
				name:"集中登录",
				icon:"glyphicon glyphicon-log-in",
				href:"../UnifiedManagement/centralizedLogin.html"
			}
		]
	}, {
		parent:"日常运维",
		children:[
			{
				name:"安全管理",
				icon:"glyphicon glyphicon-lock",
				href:"../DailyOperation/safetyManagement.html"
			}, {
				name:"软硬件维护",
				icon:"glyphicon glyphicon-inbox",
				href:"../DailyOperation/softwareHardwareMaintain.html"
			}, {
				name:"局数据管理",
				icon:"glyphicon glyphicon-list-alt",
				href:"../DailyOperation/bureauDataManagement.html"
			}, {
				name:"网络接口维护",
				icon:"glyphicon glyphicon-briefcase",
				href:"../DailyOperation/networkInterfaceMaintain.html"
			}
		]
	}, {
		parent:"高级功能",
		children:[
			{
				name:"BOSS业务实时监控",
				icon:"glyphicon glyphicon-eye-open",
				href:"../AdvancedFunction/bossRealTimeMonitoring.html"
			}, {
				name:"用户BOSS业务查询",
				icon:"glyphicon glyphicon-search",
				href:"../AdvancedFunction/bossHistoryQuery.html"
			}, {
				name:"PGW日志验证",
				icon:"glyphicon glyphicon-list",
				href:"../AdvancedFunction/pgw.html"
			}, {
				name:"用户数据查询",
				icon:"glyphicon glyphicon-equalizer",
				href:"../AdvancedFunction/userDataQuery.html"
			}, {
				name:"用户数据管理",
				icon:"glyphicon glyphicon-baby-formula",
				href:"../AdvancedFunction/userDataManagement.html"
			}, {
				name:"网元操作日志",
				icon:"glyphicon glyphicon-modal-window",
				href:"../AdvancedFunction/logs.html"
			}, {
				name:"智能巡检",
				icon:"glyphicon glyphicon-retweet",
				href:"../AdvancedFunction/intelligentInspection.html"
			}
		]
	}, {
		parent:"平台管理",
		children:[
			{
				name:"号段管理",
				icon:"glyphicon glyphicon-object-align-vertical",
				href:"../systemManagement/numberManage.html"
			}, {
				name:"地区管理",
				icon:"glyphicon glyphicon-map-marker",
				href:"../systemManagement/areaManage.html"
			}, {
				name:"网元管理",
				icon:"glyphicon glyphicon-calendar",
				href:"../systemManagement/netElement.html"
			}, {
				name:"单元管理",
				icon:"glyphicon glyphicon-sound-dolby",
				href:"../systemManagement/unitManage.html"
			}, {
				name:"用户管理",
				icon:"glyphicon glyphicon-user",
				href:"../systemManagement/userManage.html"
			}, {
				name:"角色管理",
				icon:"glyphicon glyphicon-cog",
				href:"../systemManagement/roleManage.html"
			}, {
				name:"指令管理",
				icon:"glyphicon glyphicon-bookmark",
				href:"../systemManagement/instrucitionManage.html"
			}, {
				name:"指令组管理",
				icon:"glyphicon glyphicon-oil",
				href:"../systemManagement/instructionGroupManage.html"
			}
		]
	}
];
