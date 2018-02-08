package com.nokia.ices.apps.fusion.score.service;

import java.util.Map;

public interface AreaScoreService {
	  //评分地市统计
	Map<String, Object> queryAllAreaScore(Map<String, Object> params);
    //故障原因统计
	Map<String, Object> queryAllBreakReason(Map<String, Object> params);
}
