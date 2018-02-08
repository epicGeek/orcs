package com.nokia.ices.apps.fusion.score.service;

import java.util.Map;

public interface BtsAlarmJobService {
	 
	//基站性能告警工单列表
	Map<String,Object> findBtsAlarmJob(Map<String,Object> params);
	
	//基站健康度工单
	Map<String,Object> findBtsScoreJob(Map<String,Object> params);
		
}
