package com.nokia.ices.apps.fusion.score.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.nokia.ices.apps.fusion.score.domain.AlarmScoreRule;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;

public interface AlarmScoreRuleService {

    //public Page<AlarmScoreRule> findAlarmScoreRulePageBySearch(Map<String, Object> searchParams, SystemRole systemRole, Pageable pageable);
	
    List<AlarmScoreRule> findAlarmScoreRuleListByCreator(Map<String, Object> searchParams, Sort sort);
    List<AlarmScoreRule> findAlarmScoreRuleAll();
    
    void addAlarmScore(AlarmScoreRule alarmScoreRule);

	public Page<AlarmScoreRule> findAlarmScoreRulePageBySearch(Map<String, Object> searchParams, SystemRole systemRole,
			int page, int size, List<String> sortType);

}
