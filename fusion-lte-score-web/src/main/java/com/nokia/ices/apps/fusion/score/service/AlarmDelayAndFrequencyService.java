package com.nokia.ices.apps.fusion.score.service;

import java.util.List;
import java.util.Map;

public interface AlarmDelayAndFrequencyService {
	
	Map<String,Object> findAlarmDelayAllData(Map<String, Object> params);

	//时长
	Map<String, Object> findAlarmDelayAll(Map<String, Object> params);
	
	//频次
	Map<String, Object> 	findAlarmFrequencyAll(Map<String, Object> params);
	
	Double findAlarmDelaySum(Map<String, Object> params);
}
