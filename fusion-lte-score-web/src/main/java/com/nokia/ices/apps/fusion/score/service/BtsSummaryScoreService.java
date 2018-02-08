package com.nokia.ices.apps.fusion.score.service;

import java.util.List;
import java.util.Map;

//基站汇总评分
public interface BtsSummaryScoreService{
	
	//基站
	 Map<String,Object> findBtsSumAll(Map<String, Object> params);

	List<Map<String, Object>> findSumchartAll(Map<String, Object> params);
	
	
}
