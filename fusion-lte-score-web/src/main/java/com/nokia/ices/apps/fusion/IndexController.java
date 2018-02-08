package com.nokia.ices.apps.fusion;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysql.jdbc.StringUtils;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;

@Controller
public class IndexController {
	private boolean checkPermission(String resourceName) {
		Subject subject = SecurityUtils.getSubject();
		return subject.isPermitted(resourceName);
	}

	@RequestMapping(value = "welcome")
	public String welcome(Model model) {
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
			return "main";
		} else {
			return "login";
		}

	}
	
/*	@RequestMapping(value = "dashboard")
	public String welcome(Model model) {
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
			return "dashboard/dashboard";
		} else {
			return "login";
		}

	}*/

	@RequestMapping(value = "")
	public String login(Model model) {
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
			model.addAttribute("menu", "dashboard");
			return "dashboard/dashboard";
		} else {
			return "login";
		}
	}
	@RequestMapping(value = "/logout")
	public String logout(Model model) {
		return "redirect:/logout";
	}
	// 指标评分规则
	@RequestMapping(value = "/indicatorScoreRule")
	public String gotoIndexScoreRule(Model model) {
		if (!checkPermission("menu:indicatorScoreRule")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "indicatorScoreRule");
		return "scoreRules/indicatorScoreRule";
	}

	// 告警评分规则
	@RequestMapping(value = "/alarmScoreRule")
	public String gotoAlarmScoreRule(Model model) {
		if (!checkPermission("menu:alarmScoreRule")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "alarmScoreRule");
		return "scoreRules/alarmScoreRule";
	}

	// 分数等级
	@RequestMapping(value = "/scoreLevel")
	public String gotoScoreLevel(Model model) {
		if (!checkPermission("menu:scoreLevel")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "scoreLevel");
		return "scoreRules/scoreLevel";
	}

	// 基站基础信息
	@RequestMapping(value = "/informationCell")
	public String gotoInformationCell(Model model) {
		if (!checkPermission("menu:informationCell")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "informationCell");
		return "scoreRules/informationCell";
	}
	
	@RequestMapping(value = "/bsCurrentScoreHour")
	public String gotoBsCurrentScoreHour(HttpServletRequest request, Model model) {
		if (!checkPermission("menu:bsCurrentScoreHour")) {
			return "redirect:/logout";
		}
		model.addAttribute("tjType", "1");
		model.addAttribute("grade", "1");
		model.addAttribute("isWeek", "no");
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "bsCurrentScoreHour");
		return "score/bsCurrentScore";
	}
	@RequestMapping(value = "/bsCurrentScoreDay")
	public String gotoBsCurrentScoreDay(HttpServletRequest request, Model model) {
		if (!checkPermission("menu:bsCurrentScoreDay")) {
			return "redirect:/logout";
		}
		
		model.addAttribute("tjType", "2");
		model.addAttribute("grade", "1");
		model.addAttribute("isWeek", "no");
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "bsCurrentScoreDay");
		return "score/bsCurrentScore";
	}
	@RequestMapping(value = "/bsCurrentScoreWeek")
	public String gotoBsCurrentScoreWeek(HttpServletRequest request, Model model) {
		if (!checkPermission("menu:bsCurrentScoreWeek")) {
			return "redirect:/logout";
		}
		model.addAttribute("tjType", "3");
		model.addAttribute("isWeek", "yes");
		model.addAttribute("grade", "1");
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "bsCurrentScoreWeek");
		return "score/bsCurrentScore";
	}

	// 基站当前评分查询
	@RequestMapping(value = "/bsCurrentScore")
	public String gotoBsCurrentScore(HttpServletRequest request, Model model) {
		if (!checkPermission("menu:bsCurrentScore")) {
			return "redirect:/logout";
		}
		model.addAttribute("cityCode", StringUtils.isNullOrEmpty(request.getParameter("cityCode")) ? "" : request.getParameter("cityCode"));
		model.addAttribute("areaCode", StringUtils.isNullOrEmpty(request.getParameter("areaCode")) ? "" : request.getParameter("areaCode"));
		model.addAttribute("startCycleHour", StringUtils.isNullOrEmpty(request.getParameter("startCycleHour")) ? "" : request.getParameter("startCycleHour"));
		model.addAttribute("endCycleHour", StringUtils.isNullOrEmpty(request.getParameter("endCycleHour")) ? "" : request.getParameter("endCycleHour"));
		model.addAttribute("cycleDate", StringUtils.isNullOrEmpty(request.getParameter("cycleDate")) ? "" : request.getParameter("cycleDate"));
		model.addAttribute("grade", StringUtils.isNullOrEmpty(request.getParameter("grade")) ? "" : request.getParameter("grade"));
		model.addAttribute("cycleHour", StringUtils.isNullOrEmpty(request.getParameter("cycleHour")) ? "" : request.getParameter("cycleHour"));
		model.addAttribute("type", StringUtils.isNullOrEmpty(request.getParameter("type")) ? "" : request.getParameter("type"));
		model.addAttribute("tjType", StringUtils.isNullOrEmpty(request.getParameter("tjType")) ? "" : request.getParameter("tjType"));
		model.addAttribute("isWeek", "no");
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "bsCurrentScore");
		return "score/bsCurrentScore";
	}

	// 地市评分查询
	@RequestMapping(value = "/scoreCity")
	public String gotoScoreCity(Model model) {
		if (!checkPermission("menu:scoreCity")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "scoreCity");
		return "statistic/scoreCity";
	}

	// 故障评分查询
	@RequestMapping(value = "/breakdownReason")
	public String gotoScoreReason(HttpServletRequest request, Model model) {
		if (!checkPermission("menu:breakdownReason")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("cityCode", StringUtils.isNullOrEmpty(request.getParameter("cityCode")) ? "" : request.getParameter("cityCode"));
		model.addAttribute("areaCode", StringUtils.isNullOrEmpty(request.getParameter("areaCode")) ? "" : request.getParameter("areaCode"));
		model.addAttribute("startDate", StringUtils.isNullOrEmpty(request.getParameter("startDate")) ? "" : request.getParameter("startDate"));
		model.addAttribute("endDate", StringUtils.isNullOrEmpty(request.getParameter("endDate")) ? "" : request.getParameter("endDate"));
		model.addAttribute("scoreType", StringUtils.isNullOrEmpty(request.getParameter("scoreType")) ? "" : request.getParameter("scoreType"));
		model.addAttribute("menu", "breakdownReason");
		return "statistic/breakdownReason";
	}
	
	//2016-04-20 新增总体呈现dashboard 里面的故障原因饼图点击到—显示各个地市故障原因排名【县区柱状图】
		@RequestMapping(value = "/faultChart")
		public String gotoFaultChart(Model model) {
			ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			if (shiroUser != null) {
				model.addAttribute("userName", shiroUser.getRealName());
			}
			model.addAttribute("menu", "dashboard");
			return "dashboard/dashboard_faultChart";
		}

	// 基站当前评分查询明细 
	@RequestMapping(value = "/bsCurrentScore_details")
	public String gotoDetails(@RequestParam("neCode") String neCode, @RequestParam("totalScore") String totalScore, @RequestParam("cycleDate") String cycleDate,
			@RequestParam("cycleHour") String cycleHour, Model mode) {
		if (!checkPermission("menu:bsCurrentScore")) {
			return "redirect:/logout";
		}
		mode.addAttribute("neCode", neCode);
		mode.addAttribute("totalScore", totalScore);
		mode.addAttribute("cycleDate", cycleDate);
		mode.addAttribute("cycleHour", cycleHour);
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			mode.addAttribute("userName", shiroUser.getRealName());
		}
		mode.addAttribute("menu", "bsCurrentScore");
		return "score/bsCurrentScore_details";
	}

	// 基站 指标图形 @RequestParam("neCode") String neCode, Model mode
	@RequestMapping(value = "/bsCurrentScoreGraphical")
	public String gotoKpiDetails(@RequestParam("neCode") String neCode, Model mode) {
		if (!checkPermission("menu:bsCurrentScore")) {
			return "redirect:/logout";
		}
		mode.addAttribute("neCode", neCode);
		mode.addAttribute("menu", "bsCurrentScore");
		return "score/bsCurrentScore_chart";
	}

	// 指标当前评分查询
	@RequestMapping(value = "/indicatorCurrentScore")
	public String gotIndicator(Model model) {
		if (!checkPermission("menu:indicatorCurrentScore")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "indicatorCurrentScore");
		return "score/indicatorCurrentScore";
	}

	// dashboard
	@RequestMapping(value = "/dashboard")
	public String gotoDashboard(Model model) {
		if (!checkPermission("menu:dashboard")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "dashboard");
		return "dashboard/dashboard";
	}

	@RequestMapping(value = "/bsMap")
	public String gotoBsMap(Model model) {

		if (!checkPermission("menu:bsMap")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "bsMap");
		return "dashboard/bsMap";
	}

		//折线图
		@RequestMapping(value = "/pieChart")
		public String gotoPieChart(HttpServletRequest request, Model model) {
			model.addAttribute("areaCode", request.getParameter("areaCode"));
			model.addAttribute("areaName", request.getParameter("areaName"));
	/*		model.addAttribute("firstLevel", request.getParameter("firstLevel"));
			model.addAttribute("secondLevel", request.getParameter("secondLevel"));
			model.addAttribute("thirdLevel", request.getParameter("thirdLevel"));
			model.addAttribute("fourthLevel", request.getParameter("fourthLevel"));
			model.addAttribute("fifthLevel", request.getParameter("fifthLevel"));*/
			ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			if (shiroUser != null) {
				model.addAttribute("userName", shiroUser.getRealName());
			}
			model.addAttribute("menu", "dashboard");
			return "dashboard/dashboard_zxChart";
		}
		
		//雷达图
		@RequestMapping(value = "/barChart")
		public String gotoBarChart(HttpServletRequest request, Model model) {
			model.addAttribute("areaCode", request.getParameter("areaCode"));
			model.addAttribute("areaName", request.getParameter("areaName"));
			ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			if (shiroUser != null) {
				model.addAttribute("userName", shiroUser.getRealName());
			}
			model.addAttribute("menu", "dashboard");
			return "dashboard/dashboard_radarChart";
		}
		
		//基站 雷达图
		@RequestMapping(value = "/btsRadarChart")
		public String gotoChart(HttpServletRequest request, Model model) {
			ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			if (shiroUser != null) {
				model.addAttribute("userName", shiroUser.getRealName());
			}
			model.addAttribute("menu", "dashboard");
			return "score/bts_radarChart";
		}
		
		//区县柱状图+折线
		@RequestMapping(value = "/areaChart")
		public String gotoAreaChart(HttpServletRequest request, Model model) {
			if (!checkPermission("menu:dashboard")) {
				return "redirect:/logout";
			}
			ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			if (shiroUser != null) {
				model.addAttribute("userName", shiroUser.getRealName());
			}
			model.addAttribute("menu", "dashboard");
			return "dashboard/dashboard_areaChart";
		}
	
	// 基站汇总
	@RequestMapping(value = "/bsSummaryScore")
	public String gotoBsSummaryScore(HttpServletRequest request, Model model) {
		if (!checkPermission("menu:bsSummaryScore")) {
			return "redirect:/logout";
		}
		model.addAttribute("cityCode", StringUtils.isNullOrEmpty(request.getParameter("cityCode")) ? "" : request.getParameter("cityCode"));
		model.addAttribute("areaCode", StringUtils.isNullOrEmpty(request.getParameter("areaCode")) ? "" : request.getParameter("areaCode"));
		model.addAttribute("neCode", StringUtils.isNullOrEmpty(request.getParameter("neCode")) ? "" : request.getParameter("neCode"));
		model.addAttribute("types", StringUtils.isNullOrEmpty(request.getParameter("types")) ? "1" : request.getParameter("types"));
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "bsSummaryScore");
		return "score/bsSummaryScore";
	}

	// kpi查询  新增需求:点击基站评分界面明细 横坐标告警故障原因跳转到kpi查询2016-04-27
	@RequestMapping(value = "/kpidataQuery")
	public String kpidataQuery(@RequestParam(value="cellId",required=false)String cellId,
												  @RequestParam(value="cell_name",required=false)String cell_name,
												  @RequestParam(value="kpiName",required=false)String kpiName,
												  @RequestParam(value="startCycle",required=false)String startCycle,
												  @RequestParam(value="endCycle",required=false)String endCycle,Model model) {
		if (!checkPermission("menu:kpidataQuery")) {
			return "redirect:/logout";
		}
		model.addAttribute("cellId", cellId==null?"":cellId);
		model.addAttribute("cell_name", cell_name==null?"":cell_name);
		model.addAttribute("startCycle", startCycle==null?"":startCycle);
		model.addAttribute("kpiName", kpiName==null?"":kpiName);
		model.addAttribute("endCycle", endCycle==null?"":endCycle);
		
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "kpidataQuery");
		return "dataQuery/currentKPI";
	}
 
	/* 
	 * 告警查询    新增需求:点击基站评分界面【明细】横坐标基站告警跳转到告警查询2016-04-27
	 * 基站性能告警查询界面点击明细跳转到此页面
	 * */
	@RequestMapping(value = "/alarmQuery")
	public String alarmDataQuery(@RequestParam(value="neCode",required=false)String neCode,
														@RequestParam(value="cityCode",required=false)String cityCode,
														@RequestParam(value="areaCode",required=false)String areaCode,
														@RequestParam(value="startDate",required=false)String startDate,
														@RequestParam(value="endDate",required=false)String endDate,Model model) {
		/*@RequestParam(value="alarmCycle",required=false)String alarmCycle, */
		if (!checkPermission("menu:alarmQuery")) {
			return "redirect:/logout";
		}
		model.addAttribute("neCode", neCode==null?"":neCode);
		model.addAttribute("startDate", startDate==null?"":startDate);
		model.addAttribute("endDate", endDate==null?"":endDate);
		model.addAttribute("cityCode", cityCode==null?"":cityCode);
		model.addAttribute("areaCode", areaCode==null?"":areaCode);
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "alarmQuery");
		return "dataQuery/currentAlarm";
	}
	
	/* 
	 * 退服查询   
	 */
	@RequestMapping(value = "/outOfQuery")
	public String outOfDataQuery(Model model) {
		
		if (!checkPermission("menu:outOfQuery")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "outOfQuery");
		return "dataQuery/currentOutOf";
	}
	
	// kpi图形指标
	@RequestMapping(value = "/chartList")
	public String gotoChartList(Model mode) {
		if (!checkPermission("menu:kpiChart")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			mode.addAttribute("userName", shiroUser.getRealName());
		}
		mode.addAttribute("menu", "kpiChart");
		return "dataQuery/chartList";
	}

	// kpi图形指标主页
	@RequestMapping(value = "/kpiChart")
	public String gotoKpiChart(Model model) {
		if (!checkPermission("menu:kpiChart")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "kpiChart");
		return "dataQuery/kpiChartIndicator";
	}
	

	// 用户管理
	@RequestMapping(value = "/userManage")
	public String userManage(Model model) {
		if (!checkPermission("menu:userManage")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "userManage");
		return "system/system-user";
	}

	// 角色管理
	@RequestMapping(value = "/roleManage")
	public String roleManage(Model model) {
		if (!checkPermission("menu:roleManage")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "roleManage");
		return "system/system-role";
	}

	// kpi图形指标图明细
	@RequestMapping(value = "/kpiDetailList")
	public String gotoKpiChartList(@RequestParam("cellid") String cellid, @RequestParam("kpiName") String kpiName, @RequestParam("neCode") String neCode, @RequestParam("sectorId") String sectorId, Model model) {

		if (!checkPermission("menu:kpiChart")) {
			return "redirect:/logout";
		}
		model.addAttribute("kpiName", kpiName);
		model.addAttribute("neCode", neCode);
		model.addAttribute("cellid", cellid);
		model.addAttribute("sectorId", sectorId);
		model.addAttribute("menu", "kpiChart");
		return "dataQuery/kpiGraphicDetail";
	}

	// 基站汇总图形
	@RequestMapping(value = "/sumChartList")
	public String gotoSumChartList(@RequestParam("neCode") String neCode, @RequestParam("neName") String neName, @RequestParam("tableName") String tableName, Model model) {

		if (!checkPermission("menu:bsSummaryScore")) {
			return "redirect:/logout";
		}
		model.addAttribute("neCode", neCode);
		model.addAttribute("neName", neName);
		model.addAttribute("tableName", tableName);
		model.addAttribute("menu", "bsSummaryScore");
		return "score/bsSummaryScoreChart";
	}


	@RequestMapping(value = "/changePassword")
	public String changePassword(Model model) {
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "roleManage");
		return "system/changePassword";
	}

	//基站性能告警规则
	@RequestMapping(value = "/btsAlarmRule")
	public String btsAlarmRule(Model model) {
		
		if (!checkPermission("menu:btsAlarmRule")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "btsAlarmRule");
		return "scoreRules/btsAlarmRule";
		
	}
	/**
	 * 基站性能告警得分页面
	 */
	@RequestMapping(value = "/btsAlarmScore")
	public String btsAlarmScore(Model model) {
		
		if (!checkPermission("menu:btsAlarmScore")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "btsAlarmScore");
		return "score/btsAlarmScore";
		
	}
	/**
	 * 告警次数统计页面
	 */
	@RequestMapping(value = "/alarmFrequency")
	public String alarmFrequency(Model model) {
		
		if (!checkPermission("menu:alarmFrequency")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "alarmFrequency");
		return "statistic/alarmFrequency";
		
	}
	/**
	 * 告警时长统计页面
	 */
	@RequestMapping(value = "/alarmDelay")
	public String alarmDelay(@RequestParam(value="startDate",required=false) String startDate,
											@RequestParam(value="endCode",required=false) String endCode,
											@RequestParam(value="areaCode",required=false) String areaCode,
											@RequestParam(value="cityCode",required=false) String cityCode,
											@RequestParam(value="type",required=false) String type,
											@RequestParam(value="tableName",required=false) String tableName, Model model) {
		
		if (!checkPermission("menu:alarmDelay")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "alarmDelay");
		return "statistic/alarmDelay";
		
	}
	
	/**
	 * 总体呈现--告警时长
	 */
	@RequestMapping(value = "/alarmDelayChart")
	public String alarmDelayChart(Model model) {
		
		if (!checkPermission("menu:alarmDelayChart")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "alarmDelayChart");
		return "dashboard/alarmDelayChart";
		
	}
	/**
	 * 工单系统
	 * 基站性能告警工单
	 */
		@RequestMapping(value = "/btsAlarmJob")
	public String btsAlarmJob(Model model) {
		
		if (!checkPermission("menu:btsAlarmJob")) {
			return "redirect:/logout";
		}
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if (shiroUser != null) {
			model.addAttribute("userName", shiroUser.getRealName());
		}
		model.addAttribute("menu", "btsAlarmJob");
		return "system_job/btsAlarmJob";
		
	}
		
		/**
		 * 工单系统
		 * 基站健康度工单
		 */
			@RequestMapping(value = "/btsScoreJob")
		public String btsScoreJob(Model model) {
			
			if (!checkPermission("menu:btsScoreJob")) {
				return "redirect:/logout";
			}
			ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			if (shiroUser != null) {
				model.addAttribute("userName", shiroUser.getRealName());
			}
			model.addAttribute("menu", "btsAlarmJob");
			return "system_job/btsScoreJob";
			
		}
			
		/**
		 * 工单系统
		 * 工单历史数据
		 */
			@RequestMapping(value = "/jobHistoryData")
		public String jobHistoryData(Model model) {
			
			if (!checkPermission("menu:jobHistoryData")) {
				return "redirect:/logout";
			}
			ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			if (shiroUser != null) {
				model.addAttribute("userName", shiroUser.getRealName());
			}
			model.addAttribute("menu", "btsAlarmJob");
			return "system_job/jobHistoryData";
			
		}
			
		/**
		 * 工单派发门限管理
		 */
		@RequestMapping(value = "/jobManage")
		public String jobManage(Model model) {
			
			if (!checkPermission("menu:jobManage")) {
				return "redirect:/logout";
			}
			ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			if (shiroUser != null) {
				model.addAttribute("userName", shiroUser.getRealName());
			}
			model.addAttribute("menu", "jobManage");
			return "scoreRules/jobManage";
			
		}
		
		/**
		 * 基站健康度 --> 指标明细
		 */
		@RequestMapping(value = "/indexValue")
		public String indexValue(Model model) {
			
			/*if (!checkPermission("menu:bsCurrentScore")) {
				return "redirect:/logout";
			}*/
			ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			if (shiroUser != null) {
				model.addAttribute("userName", shiroUser.getRealName());
			}
			model.addAttribute("menu", "bsCurrentScoreHour");
			return "score/indexValue";
			
		}
	
	/*
	 * @RequestMapping(value = "/maintain-userdata") public String
	 * userdata(Model model) { if (!checkPermission("menu:userDataMaintain")) {
	 * return "redirect:/welcome"; } //setModelValues(model, "userdata"); return
	 * "advanced/userDataManage"; }
	 */

//	@Override
//	public String getErrorPath() {
//		return "error";
//	}
//
//	@RequestMapping
//	public String errorHandle() {
//		return getErrorPath();
//	}
	/*
	 * // 主菜单管理
	 * 
	 * @RequestMapping(value = "/mainMenuManage") public String
	 * mainMenuManage(Model model) { if
	 * (!checkPermission("menu:mainMenuManage")) { return "login"; } ShiroUser
	 * shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
	 * if(shiroUser != null) { model.addAttribute("userName",
	 * shiroUser.getRealName()); } return "systemManage/mainMenuManage"; }
	 * 
	 * // 子菜单管理
	 * 
	 * @RequestMapping(value = "/subMenuManage") public String
	 * subMenuManage(Model model) { if (!checkPermission("menu:subMenuManage"))
	 * { return "login"; } ShiroUser shiroUser = (ShiroUser)
	 * SecurityUtils.getSubject().getPrincipal(); if(shiroUser != null) {
	 * model.addAttribute("userName", shiroUser.getRealName()); } return
	 * "systemManage/subMenuManage"; }
	 */
}
