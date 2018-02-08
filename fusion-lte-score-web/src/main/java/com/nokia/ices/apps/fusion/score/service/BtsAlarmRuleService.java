package com.nokia.ices.apps.fusion.score.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.nokia.ices.apps.fusion.score.domain.BtsAlarmRule;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;

//基站性能告警规则
public interface BtsAlarmRuleService {

	/*public Page<BtsAlarmRule> findAlarmRulePageBySearch(Map<String, Object> searchParams, Object object,
			Integer iDisplayStart, Integer iDisplayLength, List<String> sortSet) {
		return null;
	}*/
	
	public Page<BtsAlarmRule> findAlarmRulePageBySearch(Map<String, Object> searchParams, SystemRole systemRole,
			int page, int size, List<String> sortType);
	
	 List<BtsAlarmRule> findBtsAlarmRuleListByCreator(Map<String, Object> searchParams, Sort sortType);
	 List<BtsAlarmRule> findBtsAlarmRuleAll();
	    
	 void addAlarmScore(BtsAlarmRule alarmRule);


}
