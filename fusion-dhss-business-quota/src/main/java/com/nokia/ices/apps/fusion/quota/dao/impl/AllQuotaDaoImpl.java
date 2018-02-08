package com.nokia.ices.apps.fusion.quota.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nokia.ices.apps.fusion.quota.dao.AllQuotaDao;

@Repository("AllQuotaDao")
public class AllQuotaDaoImpl implements AllQuotaDao {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> queryAllQuotaData(Map<String, Object> m) {
		StringBuffer sb_select = new StringBuffer("select nthlr_id,nthlr_name,kpiname,sum(kpivalue)/count(kpivalue) "
				+ "kpivalue,sum(kpifailcount) kpifailcount,sum(kpirequestcount) kpirequestcount,period "
				+ "from quota_monitor_history ");
		
		List<String> args = new ArrayList<String>();
		StringBuffer sb_condition = new StringBuffer("where 1=1 ");
		prepareConditions(m, sb_condition, args);
		sb_condition.append(" group by nthlr_id,nthlr_name,kpiname,period");
		sb_condition.append(" order by kpiname,period desc");
		System.out.println(sb_select.append(sb_condition).toString());
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb_select.toString(),args.toArray());
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(); /*=MysqlDB.queryForList(sb_select.toString())*/;
		return resultList;
	}
	private void prepareConditions(Map<String, Object> m, StringBuffer sb,
			List<String> args) {
		if(m.get("bscName")!=null && !m.get("bscName").equals("")){
			sb.append(" and nthlr_id =  "+Integer.parseInt(m.get("bscName").toString()));
		}
		if(m.get("quotaName")!=null && !m.get("quotaName").equals("")){
			sb.append(" and  kpiname = '"+m.get("quotaName").toString()+"'");
		}
		if(m.get("startTime")!=null && !m.get("startTime").equals("")){
			sb.append(" and  period between '"+m.get("startTime").toString()+"' and '"+m.get("endTime").toString()+"'");
		}
	}
	@Override
	public List<Map<String, Object>> queryAllBscName(Map<String, Object> m) {
		StringBuffer sb_select = new StringBuffer("select distinct nthlr_id,nthlr_name from nthlr_node where 1=1 order by nthlr_name desc ");
//		StringBuffer sb_select = new StringBuffer("select distinct nthlr_id,nthlr_name from quota_monitor where 1=1 order by nthlr_name asc ");
		List<String> args = new ArrayList<String>();
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb_select.toString(),args.toArray());
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>() /*=MysqlDB.queryForList(sb_select.toString())*/;
		return resultList;
	}

	@Override
	public List<Map<String, Object>> queryQuotaKpiData(Map<String, Object> m) {
		StringBuffer sb_select = new StringBuffer("select kpiname,nthlr_name,(sum(kpivalue)/count(kpivalue))kpivalue "
				+ "from quota_monitor where 1=1 ");
		List<String> args = new ArrayList<String>();
		if(m.get("neName")!=null && !m.get("neName").equals("")){
			sb_select.append(" and nthlr_name = '"+m.get("neName").toString()+"' ");
			sb_select.append(" group by nthlr_name,kpiname ");
			sb_select.append(" order by kpiname,nthlr_name ");
		}
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb_select.toString(),args.toArray());
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>()/*=MysqlDB.queryForList(sb_select.toString())*/;
		return resultList;
	}

}
