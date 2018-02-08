package com.nokia.ices.apps.fusion.alarm.dao;

import java.util.List;
import java.util.Map;


/**
 * 告警监控访问数据库接口类
 * @author songzanhua
 *
 */
public interface AlarmMonitorDao{
	
	public List<Map<String, Object>> queryAlarmMonitor(Map<String, Object> m);
	public Long queryAlarmMonitorCount(Map<String, Object> m);
	public int findCurrentCount();
      
}
