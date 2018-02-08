package com.nokia.ices.apps.fusion.score.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.score.domain.BtsAlarmRule;
import com.nokia.ices.apps.fusion.score.service.BtsAlarmRuleService;
import com.nokia.ices.core.utils.JsonMapper;

/**
 * 基站告警规则
 * 
 * */
@RestController
public class BtsAlarmRuleController {

	public static final Logger logger = LoggerFactory.getLogger(BtsAlarmRuleController.class);

	@Autowired
	BtsAlarmRuleService alarmRuleService;
	
	@RequestMapping(value = "btsAlarmRule/search")
	public Page<BtsAlarmRule> getAlarmScoreRuleList(
			@RequestParam("alarmTitle") String alarmTitle,
			@RequestParam("alarmNo") String alarmNo,
			@RequestParam("page") Integer iDisplayStart,
			@RequestParam("pageSize") Integer iDisplayLength) {

		List<String> sortSet = new ArrayList<String>();
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("alarmTitle", alarmTitle);
		searchParams.put("alarmNo", alarmNo);
		Page<BtsAlarmRule> page = alarmRuleService.findAlarmRulePageBySearch(searchParams, null,iDisplayStart, iDisplayLength, sortSet);
		logger.debug(new JsonMapper().toJson(page));
		return page;
	}

	/**
	 * 根据属性值查询记录，=0不重复，>0重复
	 * 
	 * @return
	 */
	@RequestMapping(value = "btsAlarmRule/isNotRepeat")
	public Integer VerificationRepeat(
			@RequestParam("searchFild") String searchFild,
			@RequestParam("valiDateFild") String valiDateFild,Sort sort) {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		if ("1".equals(valiDateFild)) {
			searchParams.put("alarmNo", searchFild);
			searchParams.put("alarmTitle", "");
		}
		List<BtsAlarmRule> alarmList = alarmRuleService.findBtsAlarmRuleListByCreator(searchParams, sort);
		Integer flag = alarmList.size();
		return flag;

	}

}
