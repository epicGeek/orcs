package com.nokia.ices.apps.fusion.alarm.service;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author LAI-DELL-13
 *
 */
public interface AlarmRecordService {
    public List<Map<String, Object>> queryAlarmRecordPageList(Map<String, Object> params);

    public String getAlarmStr(Map<String, Object> params);
    
    public List<Map<String, Object>> queryAlarmExtraInfo(Map<String, Object> params);
}
