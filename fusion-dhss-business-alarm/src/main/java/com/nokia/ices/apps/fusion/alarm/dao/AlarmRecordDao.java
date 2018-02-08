package com.nokia.ices.apps.fusion.alarm.dao;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author LAI-DELL-13
 *
 */
@Repository("alarmRecordDao")
public interface AlarmRecordDao {
    public List<Map<String, Object>> queryAlarmRecordPageList(Map<String, Object> params);

    public String getAlarmStr(Map<String, Object> params);
    
    public List<Map<String, Object>> queryAlarmExtraInfo(Map<String, Object> params);
}
