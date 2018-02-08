package com.nokia.ices.apps.fusion.alarm.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.alarm.dao.AlarmRecordDao;
import com.nokia.ices.apps.fusion.alarm.service.AlarmRecordService;

/**
 * 
 * @author LAI-DELL-13
 *
 */
@Service("alarmRecordService")
public class AlarmRecordServiceImpl implements AlarmRecordService {
    @Autowired
    AlarmRecordDao alarmRecordDao;

    @Override
    public List<Map<String, Object>> queryAlarmRecordPageList(Map<String, Object> params) {
        return alarmRecordDao.queryAlarmRecordPageList(params);
    }

    @Override
    public String getAlarmStr(Map<String, Object> params) {
        return alarmRecordDao.getAlarmStr(params);
    }

    public List<Map<String, Object>> queryAlarmExtraInfo(Map<String, Object> params) {
        return alarmRecordDao.queryAlarmExtraInfo(params);
    }

}
