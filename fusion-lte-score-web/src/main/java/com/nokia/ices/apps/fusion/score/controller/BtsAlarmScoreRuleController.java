package com.nokia.ices.apps.fusion.score.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.score.service.BtsAlarmScoreRuleService;

/**
 * 基站性能告警规则--性能告警得分配置
 * */
@RestController
public class BtsAlarmScoreRuleController {

	public static final Logger logger = LoggerFactory.getLogger(BtsAlarmScoreRuleController.class);
	
	@Autowired
	BtsAlarmScoreRuleService btsAlarmScoreRuleService;
	
	// 性能告警得分配置
	@RequestMapping(value = "alarmScoreRule/search")
	public Map<String, Object> getAlarmScoreRuleList(@RequestParam("page") Integer page, 
													 @RequestParam("pageSize") Integer pageSize) {

		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("page", (page - 1) * pageSize);
		searchParams.put("pageSize", pageSize);
		//searchParams.put("scoreRuleFiled", scoreRuleFiled);
		return btsAlarmScoreRuleService.findBtsAlarmScoreAll(searchParams);
		
	}
	
	@RequestMapping(value = "alarmScoreRule/edit")
	public Long alarmScoreRuleUpdate(@RequestBody ModelMap paramMap){
		
		try{
			btsAlarmScoreRuleService.alarmScoreRuleEdit(paramMap);
		}catch(Exception e){
			logger.debug(e.toString());
		}
		return 1L;
	}


}
