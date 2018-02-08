package com.nokia.ices.apps.fusion.boss_rev.service;

import java.util.List;
import java.util.Map;

public interface BossRevolutionService {
	public List<Map<String,Object>> getDhssNameAndHlrsnMap(String bossVersion);
	public Map<String, String> getDefaultSelectOptionsTimePeriod();
	public Map<String,Object> getOperationName(String bossVersion);
	public Map<String,Object> getErrorType(String bossVersion);
	public void createErrorType(String bossVersion,Map<String,Object> paraMap);
	public void updateErrorType(String bossVersion,Map<String,Object> paraMap);
	public void destroyErrorType(String bossVersion,Integer id);
	public List<Map<String,Object>> getBossExportDataByCondition(Map<String,Object> paraMap, String bossVersion);
	public Map<String,Object> getBossDataDetail(String taskId, String bossVersion);
	public List<Map<String,Object>> getBossKpi(Map<String,Object> paraMap,String bossVersion);
	public Map<String, String> getCurrentHourTimePeriod();
	public Map<String,Object> getBossQueryDataAndCount(Map<String,Object> paraMap,String bossVersion);
	public void updateOperationName(String bossVersion, Map<String, Object> valueMap);
	public void destroyOperationName(String bossVersion, Integer id);
	public void createOperationName(String bossVersion, Map<String, Object> valueMap);
}
