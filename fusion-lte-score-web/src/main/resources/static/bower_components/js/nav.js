$(function() {
	//动态生成左侧导航
	initMenu();
	
	//左侧导航手风琴效果
	$("#leftNavList").delegate(".parentMenu > a","click", function(){
		var open = $(this).parent().hasClass("open");
		if(open){
			$(this).next(".sub-menu").slideUp(200, function(){
				$(this).parent().removeClass("open");
			});
		} else {
			$(".parentMenu.open .sub-menu").slideUp(200, function(){
				$(".parentMenu.open").removeClass("open");
			});
			
			$(this).next(".sub-menu").slideDown(200, function(){
				$(this).parent().addClass("open");
			});
		}
		
	});
	
	//左侧导航 scrollbar
	$(".verticalNavWrap").mCustomScrollbar({
		theme:"dark",
		scrollbarPosition:"inside",
		autoHideScrollbar: true
	});
	
	//左侧导航展开与关闭
	$(".collapseBtn").on("click",function(){
		$(".leftFixedMenu").toggleClass("leftCollapsed");
	});
	
});

/**
 * 拼写昨天动态菜单
 * @param navList
 */
function initHtml(navList){
	
	//console.log(navList);
	
	$.each(navList, function(index, value) {
		if(value){
			var li = $("<li id='navLi"+index+"' class='parentMenu'></li>").appendTo($("#leftNavList"));
			$("<a href='#'><i class='"+value.icon+"'></i><span>"+value.name+"</span></a>").appendTo(li);
			var ul = $("<ul class='sub-menu'></ul>").appendTo(li);
			$("<li class='sub-menu-header'><i class='"+value.icon+"'></i>"+value.name+"</li>").appendTo(ul);
			var children = value.children;
			$.each(children,function(num,obj){
				$("<li><a href='"+obj.href+"' title='"+obj.name+"'>"+obj.name+"</a></li>").appendTo(ul);
			});
		}
	});
}

/**
 * 动态获取数据
 * @returns {Array}
 */
function initMenu(){
	var code = $('#menu').val();
	console.log(code);
	var navList =[];
	$.ajax({
		url : "platform/authority/search",
		type : "get",
		async:false,
		success : function(data) {
			var keyMenu = [],activeMenu, i=0,order=0, topMenuIndex;
			$.each(data,function(key,menu){
				key=key.trim();
				var nav = {name:key};
				if(key=='总体呈现'){
					nav.icon='fa fa-home';
					order = 0;
				}else if(key=='配置管理'){
					nav.icon='fa fa-cogs';
					order = 5;
				}else if(key=='统计分析'){
					nav.icon='fa fa-pie-chart';
					order = 3;
				}else if(key=='健康度评分'){
					nav.icon='fa fa-star-half-o';
					order = 1;
				}else if(key=='系统管理'){
					nav.icon='fa fa-cog';
					order = 6;
				}else if(key=='数据查询'){
					nav.icon='fa fa-search';
					order = 2;
				}else if(key=='工单派发'){
					nav.icon='fa fa-desktop';
					order = 4;
				}
				
				var nextMenu = [];
				$.each(menu,function(index,item){
					if($.inArray(item.menuCode,keyMenu)!=-1){
						return true;
					}else{
						keyMenu.push(item.menuCode);
						var name = item.menuName.split('-')[1].trim();
						nextMenu.push({name:name,href:'/'+item.menuCode,icon:''});
						if(code==item.menuCode){
							activeMenu = name;
							topMenuIndex=order;
						}
					}
				});
				
				nav.children = nextMenu;
				navList[order]=nav;
					
			});
			
			initHtml(navList);
			setActiveMenu(activeMenu);
			initTopMenu(navList[topMenuIndex],activeMenu);
		}
	});
	
}

//设置左侧菜单选中项
function setActiveMenu(menuName){
	var subMenu = $(".sub-menu li a[title ='"+menuName+"']");
	var liParent=subMenu.parent(), parentMenu=subMenu.parentsUntil(".parentMenu").parent();
	liParent.addClass("active");
	parentMenu.addClass("open");
}
//设置顶部导航
function initTopMenu(topMenu,activeMenu){
	$("#mainNavName").html(topMenu.name);
	$("#currentSubNavName").text(activeMenu);

	$.each(topMenu.children, function(index, value) {
		$(
				"<a href='" + value.href + "'><i class='" + value.icon + "'></i> "
						+ value.name + "</a>").appendTo(
				$("#currentSubNavList"));
	});
}


/*window.navList = [
	{
		name: "总体呈现",
		icon: "fa fa-home",
		children: [{
			name: "DASHBOARD",
			icon: "fa fa-dashboard info",
			href: "/dashboard"
		}, {
			name: "基站分布图",
			icon: "fa fa-map-marker success",
			href: "/bsMap"
		}]
	},{
		name: "健康度评分",
		icon: "fa fa-star-half-o",
		children: [{
			name: "基站健康度明细",
			icon: "",
			href: "/bsCurrentScore"
		}, {
			name: "指标健康度明细",
			icon: "",
			href: "/indicatorCurrentScore"
		},{
			name: "基站健康度汇总",
			icon: "",
			href: "/bsSummaryScore"
		}]
	},{
		name: "数据查询",
		icon: "fa fa-search",
		children: [{
				name: "KPI查询",
				icon: "",
				href: "/kpidataQuery"
			}, {
				name: "告警查询",
				icon: "",
				href: "/alarmQuery"
			}, {
				name: "KPI图形指标",
				icon: "",
				href: "/kpiChart"
		},{
			name:"退服查询",
			icon:"",
			href:"/outOfQuery"
		}]
	},{
		name: "统计分析",
		icon: "fa fa-pie-chart",
		children: [{
			name: "评分地市统计",
			icon: "",
			href: "/scoreCity"
			//href: "#"
		}, {
			name: "评分故障原因统计",
			icon: "",
			href: "/breakdownReason"
			//href: "#"
		}]
	},{
		name: "工单系统",
		icon: "fa fa-desktop",
		children: [{
			name: "基站性能告警工单",
			icon: "",
			href: "/btsAlarmJob"
		}, {
			name: "基站健康度工单",
			icon: "",
			href: "/btsScoreJob"
		},{
			name: "历史工单数据",
			icon: "",
			href: "/jobHistoryData"
		}]
	},{
		name: "配置管理",
		icon: "fa fa-cogs",
		children: [
		    {
			name: "基站评分规则",
			icon: "",
			href: "../scoreRules/bsScoreRule.html"}, 
		{
			name: "指标评分规则",
			icon: "",
			href: "/indicatorScoreRule"
		}, {
			name: "告警评分规则",
			icon: "",
			href: "/alarmScoreRule"
		}, {
			name: "分数等级",
			icon: "",
			href: "/scoreLevel"
		},{
			name: "基站基础信息",
			icon: "",
			href: "/informationCell"
		},{
			name: "基站退服规则",
			icon: "",
			href: "/btsAlarmRule"
		}{
			name: "工单派发门限管理",
			icon: "",
			href: "/jobManage"
		}]
	},{
		name: "系统管理",
		icon: "fa fa-cog",
		children: [{
			name: "用户管理",
			icon: "",
			href: "/userManage"
		}, {
			name: "角色管理",
			icon: "",
			href: "/roleManage"
		}]
	}
];*/