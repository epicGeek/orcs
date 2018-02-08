package com.nokia.ices.apps.fusion.score.repository;

import java.util.List;
import java.util.Map;

/**
 * 工单系统
 * @author Administrator
 *
 */
public interface BtsAlarmJobRepository {

	//基站性能告警工单
	List<Map<String,Object>> findBtsJobAll(Map<String, Object> params);
	
	Integer findBtsJobCount(Map<String, Object> params);
	
	//基站健康度工单
	List<Map<String,Object>> findBtsScoreJobAll(Map<String, Object> params);
	
	Integer findBtsScoreJobCount(Map<String, Object> params);
	
	
}
