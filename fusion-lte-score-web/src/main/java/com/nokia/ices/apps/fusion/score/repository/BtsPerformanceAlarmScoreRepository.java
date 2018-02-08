package com.nokia.ices.apps.fusion.score.repository;

import java.util.List;
import java.util.Map;

//性能告警得分 dao
public interface BtsPerformanceAlarmScoreRepository {
	
	//基站性能告警的得分列表
	List<Map<String,Object>> findBtsPerformanceAlarmScoreAll(Map<String,Object> params);
	
	Integer findBtsPerformanceAlarmScoreCount(Map<String, Object> params);
	
	List<Map<String,Object>> findBtsAlarmNo(Map<String,Object> params);
	
	Integer findBtsAlarmNoCount(Map<String, Object> params);
	
}
