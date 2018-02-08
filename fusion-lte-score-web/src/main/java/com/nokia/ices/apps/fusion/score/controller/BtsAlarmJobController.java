package com.nokia.ices.apps.fusion.score.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nokia.ices.apps.fusion.score.service.AreaService;
import com.nokia.ices.apps.fusion.score.service.BtsAlarmJobService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;

/**
 *工单系统
 * @author Administrator
 *
 */
@RestController
public class BtsAlarmJobController {

	public static final Logger logger = LoggerFactory.getLogger(BtsAlarmJobController.class);
	//private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	@Autowired
	AreaService areaService;

	@Autowired
	BtsAlarmJobService btsAlarmJobService;
    
    /**
     * 基站告警性能工单
     */
    @RequestMapping(value = "btsAlarmJob/search")
    public Map<String,Object> getAlarmJobList(@RequestParam("page") Integer page, 
																			@RequestParam("pageSize") Integer pageSize,
																		    @RequestParam("areaCode")String areaCode,
																		    @RequestParam("cityCode")String cityCode,
																		    @RequestParam("neCode")String neCode,
																		    @RequestParam("cycleStart")String cycleStart,
																		    @RequestParam("cycleEnd")String cycleEnd,
																		    @RequestParam("score")String score){
    	
	  //获取地区权限
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
        Map<String,Object> searchParams = new HashMap<String,Object>();
        searchParams.put("page", (page-1)*pageSize);
        searchParams.put("pageSize", pageSize);
        searchParams.put("areaCode", areaCode);
        searchParams.put("cityCode", cityCode);
        searchParams.put("neCode", neCode);
        searchParams.put("cycleStart", cycleStart);
        searchParams.put("cycleEnd", cycleEnd);
        searchParams.put("score", score);
    	
        return btsAlarmJobService.findBtsAlarmJob(searchParams);
    }
      /**
       * 基站健康度工单
       * @param page
       * @param pageSize
       * @param areaCode
       * @param cityCode
       * @param neCode
       * @param cycleStart
       * @param cycleEnd
       * @param grade
       * @return
       */
    @RequestMapping(value = "btsScoreJob/search")
    public Map<String,Object> getScoreJobList(@RequestParam("page") Integer page, 
																			@RequestParam("pageSize") Integer pageSize,
																		    @RequestParam("areaCode")String areaCode,
																		    @RequestParam("cityCode")String cityCode,
																		    @RequestParam("neCode")String neCode,
																		    @RequestParam("cycleStart")String cycleStart,
																		    @RequestParam("cycleEnd")String cycleEnd,
																		    @RequestParam("grade")String grade){
    	
	   //获取地区权限
  		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
  		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
        Map<String,Object> searchParams = new HashMap<String,Object>();
        searchParams.put("page", (page-1)*pageSize);
        searchParams.put("pageSize", pageSize);
        searchParams.put("areaCode", areaCode);
        searchParams.put("cityCode", cityCode);
        searchParams.put("neCode", neCode);
        searchParams.put("cycleStart", cycleStart);
        searchParams.put("cycleEnd", cycleEnd);
        searchParams.put("grade", grade);
    	
        return btsAlarmJobService.findBtsScoreJob(searchParams);
    }
    
    //历史工单数据
    @RequestMapping(value = "jobHistory/search")
    public Map<String,Object> getJobHistoryList(@RequestParam("page") Integer page, 
																			@RequestParam("pageSize") Integer pageSize,
																		    @RequestParam("areaCode")String areaCode,
																		    @RequestParam("cityCode")String cityCode,
																		    @RequestParam("neCode")String neCode,
																		    @RequestParam("cycleStart")String cycleStart,
																		    @RequestParam("cycleEnd")String cycleEnd,
																		    @RequestParam("grade")String grade){
    	
	   //获取地区权限
  		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
  		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
        Map<String,Object> searchParams = new HashMap<String,Object>();
        searchParams.put("page", (page-1)*pageSize);
        searchParams.put("pageSize", pageSize);
        searchParams.put("areaCode", areaCode);
        searchParams.put("cityCode", cityCode);
        searchParams.put("neCode", neCode);
        searchParams.put("cycleStart", cycleStart);
        searchParams.put("cycleEnd", cycleEnd);
        searchParams.put("grade", grade);
    	
        return btsAlarmJobService.findBtsScoreJob(searchParams);
    }

}
