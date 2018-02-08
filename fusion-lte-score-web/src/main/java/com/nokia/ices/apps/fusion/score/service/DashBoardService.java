package com.nokia.ices.apps.fusion.score.service;

import java.util.List;
import java.util.Map;

import com.nokia.ices.apps.fusion.score.domain.BtsProxy;

public interface DashBoardService {
	public List<Map<String, Object>> searchCurBreakReasion(Map<String, Object> map);

	public List<Map<String, Object>> searchCurAreaScore(Map<String, Object> map);

	public List<Map<String, Object>> searchCurWorstArea();

	List<Map<String, Object>> searchAreaScore(Map<String, String> map);

	public String searchMaxCycleBreakReasion();

	public String searchMaxCycleWorstArea();

	public List<BtsProxy> searchBtsMap(Map<String, Object> map);
	
	public List<Map<String, Object>> searchAreaAndCityScore(Map<String, Object> map);
	
	/**
	 *地市  基站一级占比 折线图 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> searchAreaGrade(Map<String, Object> map);
	
	/**
	 * 地市雷达图
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> areaAvgScore(Map<String, Object> map);
	
}
