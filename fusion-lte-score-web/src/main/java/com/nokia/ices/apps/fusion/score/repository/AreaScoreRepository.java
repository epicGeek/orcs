package com.nokia.ices.apps.fusion.score.repository;

import java.util.List;
import java.util.Map;


public interface AreaScoreRepository {
	
	List<Map<String,Object>> queryAreaScoreList(Map<String, Object> params);
	Integer findAreaScoreCount(Map<String, Object> params);
	
	Integer findBreakReasonCount(Map<String, Object> params);
	List<Map<String, Object>> queryBreakReasonList(Map<String, Object> params);

}
