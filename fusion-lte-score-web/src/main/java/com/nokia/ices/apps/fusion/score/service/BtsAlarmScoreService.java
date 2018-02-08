package com.nokia.ices.apps.fusion.score.service;

import java.util.Map;

public interface BtsAlarmScoreService {
	 
	//基站性能告警的得分列表
	Map<String,Object> findBtsPerformanceAlarmScore(Map<String,Object> params);
		
	//	Integer findBtsPerformanceAlarmScoreCount(Map<String, Object> params);
	
	Map<String,Object> findAlarmNo(Map<String,Object> params);
}
