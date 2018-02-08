package com.nokia.ices.apps.fusion.score.repository;

import java.util.List;
import java.util.Map;

//性能告警得分配置 dao
public interface BtsAlarmScoreRepository {

	List<Map<String,Object>> findAlarmScoreAll(Map<String, Object> params);
	Integer findAlarmScoreCount(Map<String, Object> params);
	//修改
	void alarmScoreRuleEdit(Map<String,Object> params);
	
	
}
