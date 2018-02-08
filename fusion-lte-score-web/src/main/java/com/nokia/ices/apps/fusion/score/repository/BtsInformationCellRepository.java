package com.nokia.ices.apps.fusion.score.repository;

import java.util.List;
import java.util.Map;

import com.nokia.ices.apps.fusion.score.domain.BtsProxy;

public interface BtsInformationCellRepository {
	
	//基站基础信息dao
	List<Map<String,Object>> findBtsAll(Map<String, Object> params);
	
	Integer findBtsCount(Map<String, Object> params);

	List<BtsProxy> findAllBtsProxyList();
	
	//告警查询
	List<Map<String,Object>> findAlarmQueryAll(Map<String, Object> params);
	
	Integer findAlarmQueryCount(Map<String, Object> params);
	
	//退服查询
	List<Map<String,Object>> findOutOfQueryAll(Map<String, Object> params);
	
	Integer findOutOfQueryCount(Map<String, Object> params);

}
