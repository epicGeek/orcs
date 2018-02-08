package com.nokia.ices.apps.fusion.alarm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nokia.ices.apps.fusion.alarm.dao.AlarmMonitorDao;
import com.nokia.ices.core.utils.DateUtil;



/**
 * 告警监控访问数据库接口实现类
 * @author xiaojun
 *
 */
@Repository
public class AlarmMonitorDaoImpl implements AlarmMonitorDao{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> queryAlarmMonitor(Map<String, Object> m) {
		StringBuffer sb_select = new StringBuffer("select id,alarm_content,alarm_level,alarm_limit,alarm_scene,"
				+ "alarm_title,alarm_type,belong_site,cancel_time,ne_name,ne_type,start_time,unit_name,unit_type "
				+ "from alarm_monitor ");
		
		List<String> args = new ArrayList<String>();
		StringBuffer sb_condition = new StringBuffer("where 1=1 ");
		prepareConditions(m, sb_condition, args);
		
		sb_condition.append(" order by start_time desc");
		
		if(m.get("rows")!=null && !m.get("rows").equals("")){
			int rows=Integer.parseInt(m.get("rows").toString());
			int page=Integer.parseInt(m.get("page").toString());
			sb_condition.append(" limit "+rows+" offset "+page);
		}
		System.out.println(sb_select.append(sb_condition).toString());
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb_select.toString(),args.toArray());
		List<Map<String, Object>> resultList /*=MysqlDB.queryForList(sb_select.toString())*/= new ArrayList<Map<String, Object>>();
		return resultList;
	}
	private void prepareConditions(Map<String, Object> m, StringBuffer sb,
			List<String> args) {
		if(m.get("neName")!=null && !m.get("neName").equals("")){
			sb.append(" and  ne_name = '"+m.get("neName").toString()+"'");
		}
		if(m.get("alarmType")!=null && !m.get("alarmType").equals("")){
			sb.append(" and  alarm_type = '"+m.get("alarmType").toString()+"'");
		}
		if(m.get("neType")!=null && !m.get("neType").equals("")){
			sb.append(" and  ne_type = '"+m.get("neType").toString()+"'");
		}
		if(m.get("startTime")!=null && !m.get("startTime").equals("")){
			sb.append(" and  start_time between '"+m.get("startTime").toString()+"' and '"+m.get("endTime").toString()+"'");
		}
	}
	@Override
	public Long queryAlarmMonitorCount(Map<String, Object> m) {
		StringBuffer sb_select = new StringBuffer("select count(1) from alarm_monitor ");
		List<String> args = new ArrayList<String>();
		StringBuffer sb_condition = new StringBuffer(" where 1=1 ");
		prepareConditions(m, sb_condition, args);

		System.out.println(sb_select.append(sb_condition).toString());
//		return	jdbcTemplate.queryForLong(sb_select.toString(),args.toArray());
		return /*MysqlDB.queryForLong(sb_select.toString())*/0L;
	}

	/**
	 * welcome.jsp页面显示一小时内的告警数量
	 */
	@Override
	public int findCurrentCount(){
		String start=DateUtil.getNextHours(-1,60);
		String end =DateUtil.getCurrentDateTime();
		StringBuffer sb_select = new StringBuffer("select count(1) from alarm_monitor where start_time "
				+ "between '"+start+"' and '"+end+"'");
		List<String> args = new ArrayList<String>();
//		return	jdbcTemplate.queryForInt(sb_select.toString(),args.toArray());
		return /*MysqlDB.queryForInt(sb_select.toString())*/0;
	}

}
