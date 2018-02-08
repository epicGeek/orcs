package com.nokia.ices.apps.fusion.quota.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nokia.ices.apps.fusion.quota.dao.QuotaMonitorDao;

@Repository("QuotaMonitorDao")
public class QuotaMonitorDaoImpl implements QuotaMonitorDao {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> queryQuotaMonitorData(Map<String, Object> m) {
		StringBuffer sb_select = new StringBuffer("select nthlr_id,nthlr_name,nthlrfe_id,nthlrfe_name,node,netype,kpiname,"
				+ "kpivalue,kpifailcount,kpirequestcount,period,scene from quota_monitor ");
		
		List<String> args = new ArrayList<String>();
		StringBuffer sb_condition = new StringBuffer("where 1=1 ");
		prepareConditions(m, sb_condition, args);
		
		sb_condition.append(" order by kpivalue desc");
		
		if(m.get("rows")!=null && !m.get("rows").equals("")){
			int rows=Integer.parseInt(m.get("rows").toString());
			int page=Integer.parseInt(m.get("page").toString());
			sb_condition.append(" limit "+rows+" offset "+page);
		}
		System.out.println(sb_select.append(sb_condition).toString());
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb_select.toString(),args.toArray());
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>()/*=MysqlDB.queryForList(sb_select.toString())*/;
		return resultList;
	}

	private void prepareConditions(Map<String, Object> m, StringBuffer sb,
			List<String> args) {
		if(m.get("nodeName")!=null && !m.get("nodeName").equals("")){
			sb.append(" and node = '"+m.get("nodeName").toString()+"'");
		}
		if(m.get("neType")!=null && !m.get("neType").equals("")){
			sb.append(" and  netype = '"+m.get("neType").toString()+"'");
		}
		if(m.get("scene")!=null && !m.get("scene").equals("")){
			sb.append(" and  scene = '"+m.get("scene").toString()+"'");
		}
		if(m.get("kpiName")!=null && !m.get("kpiName").equals("")){
			sb.append(" and  kpiName = '"+m.get("kpiName").toString()+"'");
		}
	}

	@Override
	public Long queryQuotaMonitorDataCount(Map<String, Object> m) {
		StringBuffer sb_select = new StringBuffer("select count(1) from quota_monitor ");
		List<String> args = new ArrayList<String>();
		StringBuffer sb_condition = new StringBuffer(" where 1=1 ");
		prepareConditions(m, sb_condition, args);

		System.out.println(sb_select.append(sb_condition).toString());
		Long returnNum=0l;
		/*returnNum=MysqlDB.queryForLong(sb_select.toString());*/
//		return	jdbcTemplate.queryForLong(sb_select.toString(),args.toArray());
		return returnNum;
	}

	@Override
	public List<Map<String, Object>> queryAllBscType(Map<String, Object> m) {
		// 
		StringBuffer sb_select = new StringBuffer("select distinct netype from quota_monitor");
		List<String> args = new ArrayList<String>();
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb_select.toString(),args.toArray());
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>()/*= MysqlDB.queryForList(sb_select.toString())*/;
		return resultList;
	}

	@Override
	public List<Map<String, Object>> queryAllQuotaData(Map<String, Object> m) {
		 
		StringBuffer sb_select = new StringBuffer("select nthlr_id,nthlr_name,nthlrfe_id,nthlrfe_name,node,netype,"
				+ "kpiname,kpivalue,kpifailcount,kpirequestcount,period,scene from quota_monitor_history ");
		System.out.println(sb_select);
		List<String> args = new ArrayList<String>();
		StringBuffer sb_condition = new StringBuffer("where 1=1 ");
		if(m.get("nodeName")!=null && !m.get("nodeName").equals("")){
			sb_condition.append("and node = '"+m.get("nodeName").toString()+"'");
		}
		if(m.get("bscName")!=null && !m.get("bscName").equals("")){
			sb_condition.append(" and  nthlr_id = "+Integer.parseInt(m.get("bscName").toString()));
		}
		if(m.get("quotaName")!=null && !m.get("quotaName").equals("")){
			sb_condition.append(" and  kpiname = '"+m.get("quotaName").toString()+"'");
		}
		if(m.get("startTime")!=null && !m.get("startTime").equals("")){
			sb_condition.append(" and  period between '"+m.get("startTime").toString()+"' and '"+m.get("endTime").toString()+"'");
		}
		System.out.println(sb_select.append(sb_condition).toString());
		System.out.println(args.toArray());
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb_select.toString(),args.toArray());
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>()/*=MysqlDB.queryForList(sb_select.toString())*/;
		return resultList;
	}
	@Override
	public List<Map<String, Object>> queryAllBscName(Map<String, Object> m) {
		StringBuffer sb_select = new StringBuffer("select distinct neId from quota_monitor where 1=1 order by neId desc ");
//		StringBuffer sb_select = new StringBuffer("select distinct nthlr_id,nthlr_name from quota_monitor where 1=1 order by nthlr_name asc ");
		List<String> args = new ArrayList<String>();
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb_select.toString(),args.toArray());
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>()/*=MysqlDB.queryForList(sb_select.toString())*/;
		return resultList;
	}
}
