package com.nokia.ices.apps.fusion.score.repository;

import java.util.List;
import java.util.Map;

public interface AlarmDelayAndFrequencyRepository {
	
	//柱状图
	List<Map<String,Object>> findAlarmDelayBarChart(Map<String, Object> params);
	
	//饼图
	List<Map<String,Object>> findAlarmDelayPie(Map<String, Object> params);
		
	//时长
	List<Map<String,Object>> findAlarmDelayAll(Map<String, Object> params);
	Integer findAlarmDelayCount(Map<String, Object> params);
	
	Double findAlarmDelaySum(Map<String, Object> params);
	
	Double findAlarmFrequencySum(Map<String, Object> params);
	
	//频次
	List<Map<String,Object>> findAlarmFrequencyAll(Map<String, Object> params);
	Integer findAlarmFrequencyCount(Map<String, Object> params);
	//最新周期数据
	String MaxDate(Map<String, Object> params);
	
}
