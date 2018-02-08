package com.nokia.ices.apps.fusion.score.service;

import java.util.Map;

public interface AlarmQueryService {

	/**
	 * 告警查询
	 * @param searchParams
	 * @return
	 */
	Map<String, Object> findAlarmQueryAll(Map<String, Object> searchParams);
	
	Integer findAlarmQueryCount(Map<String, Object> searchParams);

	void afterPropertiesSet() throws Exception;
	
	/**
	 * 退服查询
	 * @param searchParams
	 * @return
	 */
	Map<String, Object> findOutOfQueryAll(Map<String, Object> searchParams);
	
	Integer findOutOfQueryCount(Map<String, Object> searchParams);

}
