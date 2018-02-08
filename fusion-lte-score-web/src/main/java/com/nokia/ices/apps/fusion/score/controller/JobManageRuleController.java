/*package com.nokia.ices.apps.fusion.score.controller;

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

import com.nokia.ices.apps.fusion.score.domain.JobManageRule;
import com.nokia.ices.apps.fusion.score.domain.JobRuleConfug;
import com.nokia.ices.apps.fusion.score.service.JobManageService;
import com.nokia.ices.core.utils.JsonMapper;

*//**
 * 工单派发门限管理规则
 * 
 *//*
@RestController
public class JobManageRuleController {

	public static final Logger logger = LoggerFactory.getLogger(JobManageRuleController.class);

	@Autowired
	JobManageService  jobRuleService;
	
	@RequestMapping(value = "jobManage/search")
	public Page<JobManageRule> getJobRuleList(
			@RequestParam("jobType") String jobType,
			@RequestParam("ruleTitle") String ruleTitle,
			@RequestParam("cellId") String cellId,
			@RequestParam("page") Integer iDisplayStart,
			@RequestParam("pageSize") Integer iDisplayLength) {

		List<String> sortSet = new ArrayList<String>();
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("jobType", jobType);
		searchParams.put("ruleTitle", ruleTitle);
		searchParams.put("cellId", cellId);
		Page<JobManageRule> page = jobRuleService.findJobRulePageBySearch(searchParams, null,iDisplayStart, iDisplayLength, sortSet);
		logger.debug(new JsonMapper().toJson(page));
		return page;
	}

	*//**
	 * 根据属性值查询记录，=0不重复，>0重复
	 * 
	 * @return
	 *//*
	@RequestMapping(value = "jobManage/isNotRepeat")
	public Integer VerificationRepeat(
			@RequestParam("searchFild") String searchFild,
			@RequestParam("valiDateFild") String valiDateFild,Sort sort) {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("searchFild", searchFild);
		searchParams.put("valiDateFild", valiDateFild);
		List<JobRuleConfug> alarmList = jobRuleService.findJobRuleListByCreator(searchParams, sort);
		Integer flag = alarmList.size();
		return flag;

	} 

}
*/