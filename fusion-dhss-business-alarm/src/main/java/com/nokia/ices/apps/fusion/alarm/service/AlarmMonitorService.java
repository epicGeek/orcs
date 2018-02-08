package com.nokia.ices.apps.fusion.alarm.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nokia.ices.apps.fusion.alarm.AlarmMonitor;
import com.nokia.ices.apps.fusion.alarm.AlarmReceiveRecord;

/**
 * 告警监控业务接口
 * @author xiaojun
 *
 */
public interface AlarmMonitorService {

	 Page<AlarmMonitor> findAlarmMonitorList(Map<String, Object> searchParams, Pageable pageable);
//    public List<AlarmMonitor> queryAlarmMonitor(Map<String, Object> m);
//	public Long queryAlarmMonitorCount(Map<String, Object> m);
//	public int findCurrentCount();
	 public List<AlarmReceiveRecord> findAlarmReceiveRecordCount(Map<String, Object> searchParams);
}
