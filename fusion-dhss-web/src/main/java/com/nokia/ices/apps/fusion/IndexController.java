package com.nokia.ices.apps.fusion;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nokia.ices.apps.fusion.command.repository.CommandCheckItemRepository;
import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentNeRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.domain.SystemMenu;
import com.nokia.ices.apps.fusion.system.domain.SystemResource;
import com.nokia.ices.apps.fusion.system.repository.SystemResourceRepository;

import app.LicenseCheck;



@Controller
public class IndexController {
    private static final boolean isDebug = false;
    private static final String redirectToWelcome = "redirect:/welcome";
    
    private static  Set<String> linkArray = new HashSet<String>();
    
    private static final String CHECK_LICENSE_SUCCESS = "";
    
	private Logger logger = LoggerFactory.getLogger(IndexController.class);
	
    @Autowired
    EquipmentUnitRepository equipmentUnitRepository;
    
    @Autowired
    CommandCheckItemRepository commandCheckItemRepository;
    
    @Autowired
    SystemResourceRepository systemResourceRepository;
    
    @PostConstruct
    private void init(){
    	String str = "number-section-manage,system-area,equipment-ne,equipment-unit,system-user,system-role,system-resource,check-item,"
    			+ "command-group,smartCheckManage,operation-log,kpi-threshold";
    	for (String  string : str.split(",")) {
    		linkArray.add(string);
		}
    }
    
    @RequestMapping(value="/system-home")
    public String systemHome(Model model){
    	String result = checkLink("system-home");
    	model.addAttribute("message", result);
    	ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Iterable<SystemResource> resources = new ArrayList<SystemResource>();
        if ("admin".equalsIgnoreCase(shiroUser.getUserName())) {
        	resources = systemResourceRepository.findAll();
        } else {
        	resources = systemResourceRepository.findSystemResourceBySystemRoleIdEquals(shiroUser.getRoleId());
        }
        
        
        List<SystemMenu> menus = new ArrayList<SystemMenu>();
        for (SystemResource systemResource : resources) {
            if (systemResource instanceof SystemMenu) {
                SystemMenu menuItem = (SystemMenu) systemResource;
                menus.add(menuItem);
            } 
        }
        model.addAttribute("menus",menus);
		return checkPermission("menu:system-home")?"system_home" : redirectToWelcome;
    		
    }
    
    @RequestMapping(value="/smart")
    public String smart(Model model){
    	String result = checkLink("smart");
    	model.addAttribute("message", result);
		return checkPermission("menu:smart")?"smart-home" : redirectToWelcome;
    }
    
    @RequestMapping(value="/volte-counter")
    public String volteCounter(Model model){
    	String result = checkLink("volte-counter");
    	model.addAttribute("message", result);
    	return checkPermission("menu:volte-counter")?"volte/volte-counter" : redirectToWelcome;
    		
    }
    
    @RequestMapping(value="/volte-table")
    public String volteTable(Model model){
    	
    	String result = checkLink("volte-table");
    	model.addAttribute("message", result);
    	return checkPermission("menu:volte-table")?"volte/volte-table" : redirectToWelcome;
    }
    
    @RequestMapping(value="/volte-list")
    public String volteList(Model model){
    	
    	String result = checkLink("volte-list");
    	model.addAttribute("message", result);
    	return checkPermission("menu:volte-list")?"volte/volte-list" : redirectToWelcome;
    }
    
    @RequestMapping(value="/volte-alarm")
    public String volteAlarm(Model model){
    	
    	String result = checkLink("volte-alarm");
    	model.addAttribute("message", result);
    	return checkPermission("menu:volte-alarm")?"volte/volte-alarm" : redirectToWelcome;
    }
    
//    @RequestMapping(value="/volte-home")
//    public String volteHome(Model model){
//    	
//    	String result = checkLink("volte-home");
//    	if(result.equals(CHECK_LICENSE_SUCCESS))
//    		return checkPermission("menu:volte-home")?"volte-home" : redirectToWelcome;
//    	else{
//    		model.addAttribute("message", result);
//    		return redirectToWelcome;
//    	}
//    }
    @RequestMapping(value="/volte-home")
    public String volteHome(Model model){
    	
    	String result = checkLink("volte-home");
    	model.addAttribute("message", result);
    	String permission = checkPermission("menu:volte-home")?"volte-home" : redirectToWelcome;
    	return permission;
    }
    @RequestMapping(value="/boss-rev-home")
    public String bossRevHome(Model model){
    	
    	String result = checkLink("boss-rev-home");
    	model.addAttribute("message", result);
    	String permission = checkPermission("menu:boss-rev-home")?"boss-rev-home" : redirectToWelcome;
    	return permission;
    		
    }
    @RequestMapping(value="/boss-rev-query")
    public String bossRevQuery(Model model){
    	
    	String result = checkLink("boss-rev-query");
    	model.addAttribute("message", result);
    	return checkPermission("menu:boss-rev-query")?"boss-rev/boss-rev-query" : redirectToWelcome;
    }
    @RequestMapping(value="/boss-rev-kpi")
    public String bossRevKpi(Model model){
    	
    	String result = checkLink("boss-rev-kpi");
    	model.addAttribute("message", result);
    	return checkPermission("menu:boss-rev-kpi")?"boss-rev/boss-rev-kpi" : redirectToWelcome;
    }
    @RequestMapping(value="/section-manage-home")
    public String sectionManageHome(Model model){
    	
    	String result = checkLink("section-manage-home");
    	model.addAttribute("message", result);
    	return checkPermission("menu:section-manage-home")?"section-manage-home" : redirectToWelcome;
    }
    
    @RequestMapping(value="/ems-home")
    public String emsHome(Model model){
    	
    	String result = checkLink("ems-home");
    	model.addAttribute("message", result);
    	return checkPermission("menu:ems-home")?"ems-home" : redirectToWelcome;
    }
    
    @RequestMapping(value="/emsResult")
    public String emsResult(Model model){
    	
    	String result = checkLink("emsResult");
    	model.addAttribute("message", result);
    	return checkPermission("menu:emsResult")?"ems/emsResult" : redirectToWelcome ;
    }
    
    @RequestMapping(value="/emsManage")
    public String emsManage(Model model){
    	
    	String result = checkLink("emsManage");
    	model.addAttribute("message", result);
    	return checkPermission("menu:emsManage")?"ems/emsManage": redirectToWelcome ;
    }
    
    @RequestMapping(value="/noticeManage")
    public String noticeManage(Model model){
    	
    	String result = checkLink("noticeManage");
    	model.addAttribute("message", result);
    	return checkPermission("menu:noticeManage")?"ems/noticeManage" : redirectToWelcome ;
    }
    
    @RequestMapping(value="/exceptionRule")
    public String exceptionRule(Model model){
    	
    	String result = checkLink("exceptionRule");
    	model.addAttribute("message", result);
    	return checkPermission("menu:exceptionRule")?"ems/exceptionRule": redirectToWelcome  ;
    }
    //设备组管理
    @RequestMapping(value="/equipment-node-group")
    public String equipmentGroup(Model model){
    	
    	String result = checkLink("equipment-node-group");
    	model.addAttribute("message", result);
    	return checkPermission("menu:equipment-node-group")?"ems/equipment-node-group" : redirectToWelcome ;
    }
    
    @RequestMapping(value="/alarm-new")
    public String alarmNew(@RequestParam(value = "history", required = false) String history,Model model){
    	String result = checkLink("alarm-new");
    	model.addAttribute("message", result);
		if(history == null || "".equals(history))
    		return checkPermission("menu:alarm-new")?"alarm-new/alarmMonitor":redirectToWelcome;
    	else
    		return checkPermission("menu:alarm-new")?"alarm-new/alarmMonitor_history":redirectToWelcome;
    	
    	
    }
    
    @RequestMapping(value="/kpi-threshold")
    public String kpiThreshold(Model model){
    	
    	String result = checkLink("kpi-threshold");
    	model.addAttribute("message", result);
    	return checkPermission("menu:kpi-threshold")?"report/kpi-threshold":redirectToWelcome;
    }

    @RequestMapping(value = "/monitor")
    public String monitorTable(Model model) {
    	
    	String result = checkLink("monitor");
    	model.addAttribute("message", result);
    	return checkPermission("menu:monitor")?"monitor/monitor-table":redirectToWelcome;
    }

    @RequestMapping(value = "welcome")
    public String welcome(@RequestParam(value="result",required=false)String result,Model model) {
        checkPermission("menu:fake");
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Iterable<SystemResource> resources = new ArrayList<SystemResource>();
        if ("admin".equalsIgnoreCase(shiroUser.getUserName())) {
        	resources = systemResourceRepository.findAll();
        } else {
        	resources = systemResourceRepository.findSystemResourceBySystemRoleIdEquals(shiroUser.getRoleId());
        }
        
        
        List<SystemMenu> menus = new ArrayList<SystemMenu>();
        for (SystemResource systemResource : resources) {
            if (systemResource instanceof SystemMenu) {
                SystemMenu menuItem = (SystemMenu) systemResource;
                menus.add(menuItem);
            } 
        }
        model.addAttribute("menus",menus);
        model.addAttribute("message","");
        return "default";
    }

    @RequestMapping(value = "")
    public String defaultPage() {
        checkPermission("menu:fake");
        if(!getCurrentLoginUser())
            return "redirect:/login";
        else 
            return redirectToWelcome;
    }
    
    @RequestMapping(value = "/operation-log")
    public String gotoSystemOperationLog(Model model){
    	
    	String result = checkLink("operation-log");
    	model.addAttribute("message", result);
    	return checkPermission("menu:operation-log")?"system/system-operation-log":redirectToWelcome;
    }

    @RequestMapping(value = "/check-item")
    public String gotoCheckItem(Model model) {
    	
    	String result = checkLink("check-item");
    	model.addAttribute("message", result);
    	return checkPermission("menu:check-item")?"command/check-item":redirectToWelcome;
    }

    @RequestMapping(value = "/command-group")
    public String gotoCommandGroup(Model model) {
    	
    	String result = checkLink("command-group");
    	model.addAttribute("message", result);
    	return checkPermission("menu:command-group")?"command/command-group":redirectToWelcome;
    }

    @RequestMapping(value = "/maintain-security")
    public String maintainSecurity(Model model) {
        
        
        String result = checkLink("maintain-security");
        model.addAttribute("message", result);
		model.addAttribute("category_cn", "安全管理");
        model.addAttribute("category_code", "SECURITY");
        return checkPermission("menu:maintain-security")?"maintain/maintain-security":redirectToWelcome;
    } 

    @RequestMapping(value = "/maintain-network")
    public String maintainNetwork(@RequestParam(value = "filterType", required = false) String filterType,Model model) {
    	
        
        
        String result = checkLink("maintain-network");
        model.addAttribute("message", result);
		model.addAttribute("category_cn", "网络接口维护");
        model.addAttribute("category_code", "NETWORK");
        model.addAttribute("category_url", "maintain-network");
        if(filterType ==null || filterType =="" ){
        	 return checkPermission("menu:maintain-network")?"maintain/maintain":redirectToWelcome;
    	}else{
    		return checkPermission("menu:maintain-network")?"maintain/maintain-item":redirectToWelcome;
    	}
    }


    @RequestMapping(value = "/maintain-remote")
    public String maintainRemoteData(@RequestParam(value = "filterType", required = false) String filterType,Model model) {

		String result = checkLink("maintain-remote");
		model.addAttribute("message", result);
		model.addAttribute("category_cn", "局数据管理");
		model.addAttribute("category_code", "REMOTE");
		model.addAttribute("category_url", "maintain-remote");
		if (filterType == null || filterType == "") {
			return checkPermission("menu:maintain-remote") ? "maintain/maintain" : redirectToWelcome;
		} else {
			return checkPermission("menu:maintain-remote") ? "maintain/maintain-item" : redirectToWelcome;
		}
    }

    @RequestMapping(value = "/maintain-environment")
    public String maintainSW(@RequestParam(value = "filterType", required = false) String filterType,Model model) {
    	
        String result = checkLink("maintain-environment");
        model.addAttribute("message", result);
		System.out.println("$$$$$$$$$$:"+filterType);
        model.addAttribute("category_cn", "软硬件维护");
        model.addAttribute("category_code", "ENVIRONMENT");
        model.addAttribute("category_url", "maintain-environment");
        if(filterType ==null || filterType =="" ){
        	return checkPermission("menu:maintain-environment")?"maintain/maintain":redirectToWelcome;
   	    }else{
   	    	return checkPermission("menu:maintain-environment")?"maintain/maintain-item":redirectToWelcome;
   	    }
    }


    // 指标监控
    @RequestMapping(value = "/quotaMonitor")
    public String quotaMonitor(Model model) {
        
        String result = checkLink("quotaMonitor");
        model.addAttribute("message", result);
		return checkPermission("menu:quotaMonitor")?"quota/quotaMonitor":redirectToWelcome;
    }
    //历史指标监控
    @RequestMapping(value = "/quotaMonitorHistory")
    public String quotaMonitorHistory(Model model) {
        
        String result = checkLink("quotaMonitorHistory");
        model.addAttribute("message", result);
		return checkPermission("menu:quotaMonitorHistory")?"quota/quotaMonitorHistory":redirectToWelcome;
    }

    // 告警监控
    @RequestMapping(value = "/alarmMonitor")
	public String alarmMonitorAndAlarmRecord(@RequestParam(value = "filterType", required = false) String filterType,
	        Model model) {
		
		String result = checkLink("alarmMonitor");
		model.addAttribute("message", result);
		if (filterType == null || filterType.equals("")) {
			return checkPermission("menu:alarmMonitor") ? "alarm/alarmRecord" : redirectToWelcome;
		} else {
			return checkPermission("menu:alarmMonitor") ? "alarm/alarmMonitor" : redirectToWelcome;
		}
	}
    
 // 自定义告警
    
    @RequestMapping(value = "/custom-alarm")
	public String alarmMonitorAndAlarmRecord(Model model,@RequestParam(required=false,value="inputAlarmType") String inputAlarmType) {
    	model.addAttribute("inputAlarmType",inputAlarmType==null?"":inputAlarmType);
		String result = checkLink("custom-alarm");
		model.addAttribute("message", result);
		return checkPermission("menu:custom-alarm") ? "alarm/alarmMonitor" : redirectToWelcome;
	}

 // 告警管理
    @RequestMapping(value = "/alarmManage")
    public String alarmManage(Model model) {
        
        String result = checkLink("alarmManage");
        model.addAttribute("message", result);
		return checkPermission("menu:alarmManage") ? "alarm/alarmManage" : redirectToWelcome;
    }

    // 指标监控跳转告警监控页
    @RequestMapping(value = "/toAlarmMonitor", method = {RequestMethod.POST, RequestMethod.GET})
    public String toAlarmMonitor(HttpServletRequest request, Model model) {
        System.out.println("neName==" + request.getParameter("neName"));
        model.addAttribute("neName", request.getParameter("neName"));
        model.addAttribute("neType", request.getParameter("neType"));
        model.addAttribute("alarmType", request.getParameter("alarmType"));
        model.addAttribute("startTime", request.getParameter("startTime"));
        model.addAttribute("endTime", request.getParameter("endTime"));
        model.addAttribute("page", request.getParameter("neName"));
        model.addAttribute("pageSize", request.getParameter("neName"));
        String result = checkLink("toAlarmMonitor");
        model.addAttribute("message", result);
		return "alarm/alarmMonitor";
        
    }
    
    @Autowired
	EquipmentNeRepository equipmentNeRepository;


    @RequestMapping(value = "/dhssTopology")
    public String dhssTopology(@RequestParam(required = false, value = "HSSNAME") String HSSNAME,Model model) {
    	String result = checkLink("dhssTopology");
    	model.addAttribute("message", result);
		if (HSSNAME == null || HSSNAME.equals("")) {
			Iterable<EquipmentNe> ne = equipmentNeRepository.findAll();
			for (EquipmentNe equipmentNe : ne) {
				HSSNAME = equipmentNe.getDhssName();
				break;
			}
		}
		model.addAttribute("HSSNAME",HSSNAME);
		return "topology/dhssTopology";
    }
    // ==========================高级功能start===================================

    /**
     * 
     * @return monitor view jsp
     * 
     */
    @RequestMapping(value = "/businessMonitor")
    public String gotoBossBusinessMonitor(Model model) {
		String result = checkLink("businessMonitor");
		model.addAttribute("message", result);
		return checkPermission("menu:businessMonitor") ? "advanced/businessMonitor" : redirectToWelcome;
    }

    /**
     * 
     * @return Boss Business query view jsp
     * 
     */
    @RequestMapping(value = "/businessSearch")
    public String gotoBossBusinessSearch(Model model) {
    	String result = checkLink("businessSearch");
    	model.addAttribute("message", result);
		return checkPermission("menu:businessSearch")?"advanced/businessSearch":redirectToWelcome;
    	
    }


    /**
     * <p>
     * 
     * 维护计划
     * 
     * @return
     */
    /*@RequestMapping(value = "/smartCheckJobResult", method = {RequestMethod.POST, RequestMethod.GET})
    public String smartCheckJobResult() {
    	 return checkPermission("menu:smartCheckJobResult")?"advanced/smartCheckJobResult":redirectToWelcome;

    }*/

    @RequestMapping(value = "/smartCheckManage", method = {RequestMethod.POST, RequestMethod.GET})
    public String smartCheckManage(Model model) {
    	 
    	 String result = checkLink("smartCheckManage");
    	 model.addAttribute("message", result);
 		return checkPermission("menu:smartCheckManage")?"smart/smartCheckManage":redirectToWelcome;
    }

    /**
     * 网元维护计划详细
     * 
     * @return
     */
   /* @RequestMapping(value = "/smartCheckJobResult/{scheduleId}/{type}", method = {RequestMethod.POST, RequestMethod.GET})
    public String smartCheckResult(@PathVariable("") String scheduleId,@PathVariable String type,Model model) {
        // if (!checkPermission("intelligentInspectionManual")) {
        // return "redirect:/welcome";
        // }
    	
        model.addAttribute("scheduleId", scheduleId);
        model.addAttribute("type", type);
        
         * if (StringUtils.isNotEmpty(type) && Integer.valueOf(type) == 1) { return
         * "advanced/smartCheckResult"; }
         
        return "advanced/smartCheckResult";
    }*/

    /**
     * 网元维护计划详细
     * 
     * @return
     */
    @RequestMapping(value = "/smartCheckJobResult", method = {RequestMethod.POST, RequestMethod.GET})
    public String smartCheckDetailResult(@RequestParam(value="scheduleId",required =false) String scheduleId,
    		@RequestParam(value="type",required =false) String type,
    		@RequestParam(value="id",required =false) String id,
    		@RequestParam(value="resultCode",required =false) String resultCode, Model model) {
		String result = checkLink("smartCheckJobResult");
		model.addAttribute("message", result);
			model.addAttribute("scheduleId", scheduleId);
			model.addAttribute("id", id);// 代表neId和checkItemId
			model.addAttribute("type", type);
			model.addAttribute("resultCode", resultCode);
			String viewName = "advanced/smartCheckJobResult";
			if (StringUtils.isNotEmpty(scheduleId)) {
				if (StringUtils.isNotEmpty(id)) {
					model.addAttribute("resultCode", "");
					viewName = "advanced/smartCheckDetailResult";
				} else {
					viewName = "advanced/smartCheckResult";
				}
			} else if (StringUtils.isNotEmpty(resultCode)) {
				model.addAttribute("type", "");
				model.addAttribute("id", "");
				model.addAttribute("scheduleId", "");
				viewName = "advanced/smartCheckDetailResult";
			}
			return checkPermission("menu:smartCheckJobResult") ? viewName : redirectToWelcome;
    }


    @RequestMapping(value = "/networkElementOperationLog")
    public String gotoneController(Model model) {
    	
    	String result = checkLink("networkElementOperationLog");
    	model.addAttribute("message", result);
		return checkPermission("menu:networkElementOperationLog")?"advanced/neOperationLog":redirectToWelcome;
    }

    @RequestMapping(value = {"/userDataQuery","/userDataQuery_advanced"})
    public String userDataQuery(Model model,@RequestParam(value="number",required=false) String number,
    		HttpServletRequest req) {
    	String msisdnAndImsi = number==null?"":number;
    	String url = req.getRequestURI();
    	url = url.substring(url.lastIndexOf("/")+1);
    	String result = checkLink(url);
    	model.addAttribute("message", result);
		model.addAttribute("number", msisdnAndImsi);
		model.addAttribute("isFlag", url.equalsIgnoreCase("userDataQuery_advanced")+"");
    	return checkPermission("menu:"+url)?"advanced/userData":redirectToWelcome;
    }
    
    
    @RequestMapping(value = "/userQueryMulti")
    public String userQueryMulti(Model model) {
    	
    	
    	String result = checkLink("userQueryMulti");
    	model.addAttribute("message", result);
		return checkPermission("menu:userQueryMulti")?"advanced/userQueryMulti":redirectToWelcome;
    }
    
    @RequestMapping(value = "/userDataManage")
    public String userdata(Model model) {
        
        String result = checkLink("userDataManage");
        model.addAttribute("message", result);
		model.addAttribute("module_name", "userdata");
        return checkPermission("menu:userDataManage")?"advanced/userDataManage":redirectToWelcome;
    }
  //总体指标
    @RequestMapping(value = "/allQuota")
    public String allQuota(Model model) {
        
        String result = checkLink("allQuota");
        model.addAttribute("message", result);
		return checkPermission("menu:allQuota")?"quota/allQuota":redirectToWelcome;
    }

    @RequestMapping(value = "/equipment-ne")
    public String gotoEquipmentNe(Model model) {
        
        String result = checkLink("equipment-ne");
        model.addAttribute("message", result);
		return checkPermission("menu:equipment-ne")?"equipment/equipment-ne":redirectToWelcome;
    }


    @RequestMapping(value = "/equipment-unit")
    public String gotoEquipmentUnit(Model model) {
        
        String result = checkLink("equipment-unit");
        model.addAttribute("message", result);
		return checkPermission("menu:equipment-unit")?"equipment/equipment-unit":redirectToWelcome;
    }

    @RequestMapping(value = "/equipment-unit-login")
    public String gotoEquipmentUnitLogin(Model model) {
        
        String result = checkLink("equipment-unit-login");
        model.addAttribute("message", result);
		return checkPermission("menu:equipment-unit-login")?"equipment/equipment-unit-login":redirectToWelcome;
    }

    @RequestMapping(value = "/number-section-manage")
    public String numberSection(Model model) {
    	 
    	 String result = checkLink("number-section-manage");
    	 model.addAttribute("message", result);
 		return checkPermission("menu:number-section-manage")?"system/number-section-manage":redirectToWelcome;
    }

    @RequestMapping(value = "/system-area")
    public String areaManage(Model model) {
    	
    	String result = checkLink("system-area");
    	model.addAttribute("message", result);
 		return checkPermission("menu:system-area")?"system/system-area":redirectToWelcome;
    }

    @RequestMapping(value = "/system-user")
    public String gotoSystemUser(Model model) {
    	
    	String result = checkLink("system-user");
    	model.addAttribute("message", result);
 		return checkPermission("menu:system-user")?"system/system-user":redirectToWelcome;
    }

    @RequestMapping(value = "/system-role")
    public String gotoSystemRole(Model model) {
    	String result = checkLink("system-role");
    	model.addAttribute("message", result);
 		return checkPermission("menu:system-role")?"system/system-role":redirectToWelcome;
    	
    }
    @RequestMapping(value = "/system-resource")
    public String gotoSystemResource(Model model) {
    	
    	String result = checkLink("system-resource");
    	model.addAttribute("message", result);
 		return checkPermission("menu:system-resource")?"system/system-resource":redirectToWelcome;
    }
    
   /***
   * 板卡倒换		wlei
   * @param permission
   * @return
   */
  @RequestMapping(value = "/nodeSwitching")
  public String nodeSwitching(Model model) {
	  
	  
		String result = checkLink("nodeSwitching");
		model.addAttribute("message", result);
		model.addAttribute("type", "0");
		model.addAttribute("unitId", "");
		return checkPermission("menu:nodeSwitching") ? "node/nodeSwitching" : redirectToWelcome;
  }
  
  /***
   * 网元倒换		
   * @param permission
   * @return
   */
  @RequestMapping(value = "/neSwitching")
  public String neSwitching(@RequestParam(value = "ne", required = false) String ne,
		  @RequestParam(value = "action", required = false) String action,
          @RequestParam(value = "caseid", required = false) String caseid,
          @RequestParam(value = "caseName", required = false) String caseName, Model model) {
	  
	  
		String result = checkLink("neSwitching");
		model.addAttribute("message", result);
		model.addAttribute("ne", ne == null ? "" : ne);
		model.addAttribute("action", action == null ? "" : action);
		model.addAttribute("caseid", caseid == null ? "" : caseid);
		model.addAttribute("caseName", caseName == null ? "" : caseName);
		return checkPermission("menu:neSwitching") ? "node/neSwitching" : redirectToWelcome;
  }
  
  @RequestMapping(value = "/nodeSwitchHistory")
  public String nodeSwitchHistory(Model model) {
      
		String result = checkLink("nodeSwitchHistory");
		model.addAttribute("message", result);
		return "node/nodeSwitchHistory";
  }
  @RequestMapping(value = "/nodeSingleSwitchHistory")
  public String nodeSingleSwitchHistory(Model model) {
      
		String result = checkLink("nodeSingleSwitchHistory");
		model.addAttribute("message", result);
		return "node/nodeSingleSwitchHistory";
  }
  
  

  
  /***
   * 一键备份		wlei
   * @param permission
   * @return
   */
  @RequestMapping(value = "/oneKeyBackup")
  public String oneKeyBackup(@RequestParam(value="show",required =false) String show,Model model) {
	  
	  	  
//		String result = checkLink("oneKeyBackup");
		
		String result = checkLink("oneKeyBackup");
		model.addAttribute("message", result);
		
//		if (result.equals(CHECK_LICENSE_SUCCESS)) {
			String url = "onekey/backupw";
			if (StringUtils.isNotEmpty(show)) {
				url = "onekey/backupHistory";
			}
			return checkPermission("menu:oneKeyBackup") ? url : redirectToWelcome;
//		} else {
//			model.addAttribute("message", result);
//			return redirectToWelcome;
//		}
  }
  
  /***
   * 一键备份		wlei
   * @param permission
   * @return
   */
//  @RequestMapping(value = "/oneKeyBackup")
//  public String oneKeyBackup(Model model) {
//	    Subject sub = SecurityUtils.getSubject();
//	    ShiroUser user = (ShiroUser)sub.getPrincipal();
//		String result = checkLink("oneKeyBackup");
//		model.addAttribute("message", result);
//		String url = "redirect:"+ProjectProperties.getOnekeypath()+"?token="+user.getToken();
//		logger.info("URL:>>>>>>>>>{}",url);
//		return checkPermission("menu:external:oneKeyBackup") ? url : redirectToWelcome;
//  }
  
  /*@RequestMapping(value = "/oneKeyBackupHistory")
  public String oneKeyBackupHistory() {
  	return checkPermission("menu:oneKeyBackupHistory")?"onekey/backupHistory":redirectToWelcome;
  }*/
  
  /**
   * 应急保障_节点恢复列表		wlei
   * 
   * @return
   */
  @RequestMapping(value = "/nodeRecoveryList", method = { RequestMethod.POST, RequestMethod.GET })
  public String nodeRecoveryList(
		  @RequestParam(value = "state", required = false) String state,
		  @RequestParam(value = "webSite", required = false) String webSite,
          @RequestParam(value = "unitId", required = false) String unitId, 
          @RequestParam(value = "neTypeId", required = false) String neTypeId, Model model) {
	  
		String result = checkLink("nodeRecoveryList");
		model.addAttribute("message", result);
		if (StringUtils.isNotEmpty(state)) {
			model.addAttribute("webSite", webSite);
			model.addAttribute("unitId", unitId == null ? "" : unitId);
			model.addAttribute("neTypeId", neTypeId);

			if (state.equals("1") || state.equals("4")) {
				model.addAttribute("type", "1");
				return checkPermission("menu:nodeRecoveryList") ? "nodeSwitching" : redirectToWelcome;
			} else if (state.equals("2") || state.equals("5")) {
				model.addAttribute("type", "2");
				return checkPermission("menu:nodeRecoveryList") ? "node/nodeSwitching" : redirectToWelcome;
			}
		}
		model.addAttribute("unitId", "");
		model.addAttribute("type", "0");
		return checkPermission("menu:nodeRecoveryList") ? "node/nodeRecoveryList" : redirectToWelcome;
	  
	  
  }
  
  /**
   * 应急保障_节点隔离		wlei
   * 
   * @return
   */
  @RequestMapping(value = "/nodeIsolation", method = { RequestMethod.POST, RequestMethod.GET })
  public String nodeIsolation(@RequestParam(value = "state", required = false) String state,
		  @RequestParam(value = "webSite", required = false) String webSite,
          @RequestParam(value = "unitId", required = false) String unitId, 
          @RequestParam(value = "neTypeId", required = false) String neTypeId, Model model) {
		String result = checkLink("nodeIsolation");
		model.addAttribute("message", result);
		model.addAttribute("webSite", webSite);
		model.addAttribute("unitId", unitId == null ? "" : unitId);
		model.addAttribute("neTypeId", neTypeId);
		model.addAttribute("type", "1");

		return checkPermission("menu:nodeIsolation") ? "node/nodeSwitching" : redirectToWelcome;
  
	  
		  
  }


  /**
   * 应急保障状态查询		wlei
   * 
   * @return
   */
  @RequestMapping(value = "/securityState", method = { RequestMethod.POST, RequestMethod.GET })
  public String securityState(Model model) {
	
	String result = checkLink("securityState");
	model.addAttribute("message", result);
	return checkPermission("menu:securityState")?"node/securityState":redirectToWelcome;
  }




  @RequestMapping(value = "/nodeRecovery", method = { RequestMethod.POST, RequestMethod.GET })
  public String nodeRecovery(
		  @RequestParam(value = "state", required = true) int state,
		  @RequestParam(value = "webSite", required = true) String webSite,
          @RequestParam(value = "unitId", required = true) int unitId, 
          @RequestParam(value = "neTypeId", required = true) int neTypeId, Model model) {
      
		String result = checkLink("nodeRecovery");
		model.addAttribute("message", result);
		model.addAttribute("webSite", webSite);
		model.addAttribute("unitId", unitId);
		model.addAttribute("neTypeId", neTypeId);

		if (state == 1 || state == 4) {
			return "nodeIsolation";
		} else if (state == 2 || state == 5) {
			return "node/nodeRecovery";
		}
		return "";
  }
    

    private boolean checkPermission(String... permission) {
    	
        Subject sub = SecurityUtils.getSubject();
        return sub.isPermittedAll(permission) || isDebug;
    }
    
 	@SuppressWarnings("deprecation")
	public Map<String, String> isExpired(@RequestParam(value="menuName",required=false) String menuName) {
 		Map<String,String> res = new HashMap<String, String>();
 		String private_key_path = "";
        String public_key_path = "";
        try {
            menuName = menuName.trim();
            LicenseCheck licenseCheck = new LicenseCheck();
            private_key_path = ProjectProperties.getPrivateLicensePath()+File.separator+menuName+".key";
            public_key_path = ProjectProperties.getPublicLicensePath();
            private_key_path = URLDecoder.decode(private_key_path);
            public_key_path = URLDecoder.decode(public_key_path);
            System.out.println(private_key_path);
            res = licenseCheck.checkPrivateKey(private_key_path, public_key_path);
            logger.info("private_key_path: {}",private_key_path);
            logger.info("public_key_path: {}",public_key_path);
            logger.info("res: {}",res);
            return res;
        } catch (Exception e) {
            res.put("status", "1");
            res.put("message",e.getMessage());
            logger.info("error menuName: {}",menuName);
            logger.info("error private_key_path: {}",private_key_path);
            logger.info("error public_key_path: {}",public_key_path);
            logger.info("error message: {}",e.getMessage());
            res.put("days","1");
            res.put("message", e.getMessage());
            return res;
        }
    }
    
	private String checkLink(String link){
    	if(linkArray.contains(link)){
        	return CHECK_LICENSE_SUCCESS;
        }
        Map<String, String> map = isExpired(link);
        if(!map.get("status").equals("0") || (map.get("days") != null && Integer.valueOf(map.get("days")) < 10)){
        	return map.get("message") == null ? "error" : map.get("message");
        }
        return CHECK_LICENSE_SUCCESS;
    }

    private boolean getCurrentLoginUser() {
        Subject sub = SecurityUtils.getSubject();
        ShiroUser shiroUser = (ShiroUser)sub.getPrincipal();
        return shiroUser!=null;
    }
    
   
}
