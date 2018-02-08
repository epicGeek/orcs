package com.nokia.ices.apps.fusion.score.service;

import java.util.Map;

//性能告警得分配置
public interface BtsAlarmScoreRuleService {

	 Map<String,Object> findBtsAlarmScoreAll(Map<String, Object> params);
	 
	// Integer findAlarmScoreCount(Map<String, Object> searchParams);
	 void alarmScoreRuleEdit(Map<String,Object> params);
	 
}
