package com.nokia.ices.apps.fusion.score.repository;

import java.util.List;
import java.util.Map;

//基站汇总评分
public interface BtsSummaryScoreRepository{
	
	//基站
	List<Map<String,Object>> findBtsSumAll(Map<String, Object> params);
	
	Integer findBtsSumCount(Map<String, Object> params);

	List<Map<String, Object>> findBtsSumChart(Map<String, Object> params);
	
}
