package com.nokia.ices.apps.fusion.alarm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nokia.ices.apps.fusion.alarm.dao.AlarmRecordDao;

/**
 * 
 * @author LAI-DELL-13
 *
 */
@Repository
public class AlarmRecordDaoImpl implements AlarmRecordDao{
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> queryAlarmRecordPageList(Map<String, Object> m) {
		StringBuffer sb = new StringBuffer("select id,alarm_id,alarm_cell,alarm_level,alarm_num,alarm_time,cancel_time "
				+ "from ices_alarm_record where 1=1 ");
		
		List<String> args = new ArrayList<String>();
		
		if(m.get("startAlarmTime2")!=null && !m.get("startAlarmTime2").equals("")){
			sb.append(" and  alarm_time2 >= 'to_date("+m.get("startAlarmTime2")+",yyyy-MM-dd HH24)'");
		}
		if(m.get("endAlarmTime2")!=null && !m.get("endAlarmTime2").equals("")){
			sb.append(" and  alarm_time2 <= 'to_date("+m.get("endAlarmTime2")+",yyyy-MM-dd HH24)'");
		}
		if(m.get("alarmId")!=null && !m.get("alarmId").equals("")){
			sb.append(" and  alarm_id like '%"+m.get("alarmId")+"%'");
		}
		if(m.get("alarmNum")!=null && !m.get("alarmNum").equals("")){
			sb.append(" and  alarm_num like '%"+m.get("alarmNum")+"%'");
		}
		if(m.get("alarmCell")!=null && !m.get("alarmCell").equals("")){
			sb.append(" and  alarm_cell like '%"+m.get("alarmCell")+"%'");
		}
		sb.append("order by id desc limit 5000");
		System.out.println(sb);
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
		List<Map<String, Object>> resultList /*=MysqlDB.queryForList(sb.toString())*/= new ArrayList<Map<String, Object>>();
		return resultList;
	}

	@Override
	public String getAlarmStr(Map<String, Object> m) {
		StringBuffer sb = new StringBuffer("select alarm_str from ices_alarm_record where 1=1 ");
		
		List<String> args = new ArrayList<String>();
		
		if(m.get("alarmRecordId")!=null && !m.get("alarmRecordId").equals("")){
			sb.append(" and  id="+m.get("alarmRecordId"));
		}
				System.out.println(sb);
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
		List<Map<String, Object>> resultList /*=MysqlDB.queryForList(sb.toString())*/= new ArrayList<Map<String, Object>>();
		if(resultList!=null && resultList.size()>0)
			return resultList.get(0).get("alarm_str").toString();
		else
			return "";
	}

	@Override
	public List<Map<String, Object>> queryAlarmExtraInfo(Map<String, Object> m) {
		StringBuffer sb = new StringBuffer("select alarm_type,ne_name,ne_code,alarm_text,notify_id,alarm_annex,user_info from ices_alarm_record where 1=1");
		
		List<String> args = new ArrayList<String>();
		
		if(m.get("alarmRecordId")!=null && !m.get("alarmRecordId").equals("")){
			sb.append(" and  id="+m.get("alarmRecordId"));
		}
		sb.append(" limit 10");
		System.out.println(sb);
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
		List<Map<String, Object>> resultList/* =MysqlDB.queryForList(sb.toString())*/= new ArrayList<Map<String, Object>>();
		return resultList;
	}
	
}
