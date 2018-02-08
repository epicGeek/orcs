//package com.nokia.ices.apps.fusion.topology.dao.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//
//import com.nokia.ices.apps.fusion.topology.dao.TopologyDao;
//import com.nokia.ices.core.utils.db.MysqlDB;
//
//@Repository("TopologyDao")
//public class TopologyDaoImpl implements  TopologyDao{
//	@Autowired
//	JdbcTemplate jdbcTemplate;
//
//	@Override
//	public List<Map<String, Object>> queryNeList(Map<String, Object> m) {
//		String SHHSS="";
//		if(m.get("SHHSS")!=null && !m.get("SHHSS").equals("")){
//			SHHSS=m.get("SHHSS").toString();
//		}
//		StringBuffer sb = new StringBuffer(""
//		+ "select distinct parent_ne as neid, parent_ne as "
//		+ "nename,'ALLHSS' as netype,'' as father, 'YES' as son, '0' as nestate, '' as  "
//		+ "location, ids_version as "
//		+ "idsversion,sw_version as swversion,'' as remarks from ne where ne.parent_ne = '"+SHHSS+"' "
//		+ "union "
//		+ "select distinct location as "
//		+ "neid, location as nename,'SITE' as netype, parent_ne as father,'YES' as son,'0' as nestate,'' "
//		+ "as location,ids_version as "
//		+ "idsversion,sw_version as swversion,remarks from ne where ne.parent_ne = '"+SHHSS+"' "
//		+ "union "
//		+ "select ne "
//		+ "as neid,ne as "
//		+ "nename,ne_type.ne_type_name as netype,location as father,'YES' as son, '0' as nestate, '' as "
//		+ "location,ids_version as "
//		+ "idsversion,sw_version as swversion,remarks from (select * from ne where ne.parent_ne = '"+SHHSS+"') "
//		+ "ne left join ne_type on "
//		+ "ne.ne_type = ne_type.id "
//		+ "union "
//		+ "select unit as neid,unit as nename,unit_type.unit_type_name as "
//		+ "netype,ne.ne as father,'' as "
//		+ "son, '0' as nestate, '' as location,unit.ids_version as idsversion,unit.sw_version as "
//		+ "swversion,remarks from unit left "
//		+ "join unit_type on unit.unit_type = unit_type.id inner join ne on unit.ne = ne.id and ne.parent_ne = '"+SHHSS+"'"); 
//		
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString());
//		return resultList;
//	}
//
//	@Override
//	public List<Map<String, Object>> queryCsDomainList(Map<String, Object> m) {
//		String SHHSS="";
//		if(m.get("SHHSS")!=null && !m.get("SHHSS").equals("")){
//			SHHSS=m.get("SHHSS").toString();
//		}
//		StringBuffer sb = new StringBuffer("select 'CE' as neid,'CE' as nename,'ALLCE' as "
//				+ "netype,'' as father,'YES' as son "
//				+ "union "
//				+ "select cs_name as neid,cs_name as "
//				+ "nename,'CE' as netype,'CE' as father,'' as son "
//				+ "from (select * from csdomain where status ='1') csdomain inner join "
//				+ "(select * from ne where parent_ne = '"+SHHSS+"') ne on "
//				+ "csdomain.ne = ne.id"); 
//		
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString());
//		return resultList;
//	}
//
//	@Override
//	public List<Map<String, Object>> queryLineList(Map<String, Object> m) {
//		String SHHSS="";
//		if(m.get("SHHSS")!=null && !m.get("SHHSS").equals("")){
//			SHHSS=m.get("SHHSS").toString();
//		}
//		StringBuffer sb = new StringBuffer("select csdomain.id as id,ne.ne as "
//				+ "fromid,ne_type.ne_type_name as fromnetype,cs_name as toid,'CE' as tonetype,cs_name as "
//				+ "linename,'0' as state from "
//				+ "(select * from csdomain where status='1')csdomain inner join ne on csdomain.ne = ne.id "
//				+ "inner join ne_type on ne.ne_type "
//				+ "= ne_type.id and ne.parent_ne = '"+SHHSS+"' "
//				+ "union "
//				+ "select csdomain.id as id,unit.unit as "
//				+ "fromid,unit_type.unit_type_name as "
//				+ "fromnetype,cs_name as toid,'CE' as tonetype,cs_name as linename,'0' as state from "
//				+ "(select * from csdomain where "
//				+ "status='2')csdomain inner join unit on csdomain.ne = unit.id inner join unit_type on "
//				+ "unit.unit_type = unit_type.id "
//				+ "inner join ne on unit.ne = ne.id and ne.parent_ne='"+SHHSS+"'"); 
//		
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString());
//		return resultList;
//	}
//
//	@Override
//	public List<Map<String, Object>> queryNeKpiList(Map<String, Object> m) {
//		String neName="";
//		if(m.get("neName")!=null && !m.get("neName").equals("")){
//			neName=m.get("neName").toString();
//		}
//		StringBuffer sb = new StringBuffer("select "
//				+ "kpiname as kpiid, "
//				+ "case when kpiname = 'kpi001' "
//				+ "then '鉴权请求成功率' "
//				+ "when kpiname = 'kpi002' then '语音呼叫查询成功率' "
//				+ "when kpiname = 'kpi003' then '短信被叫查询成功率' "
//				+ "when kpiname = 'kpi004' "
//				+ "then '位置更新请求成功率' "
//				+ "when kpiname = 'kpi005' then 'GPRS附着成功率' "
//				+ "when kpiname = 'kpi006' then 'LTE位置更新成功率' "
//				+ "when kpiname = "
//				+ "'kpi007' then 'LTE用户鉴权成功率' "
//				+ "when kpiname = 'kpi008' then 'MAP-CancelLocation请求次数' "
//				+ "when kpiname = 'kpi009' then "
//				+ "'S6A-CancelLocation请求次数' "
//				+ "else 'Other' "
//				+ "end as kpiname, "
//				+ "nthlr_name,round((sum(kpivalue)/count(kpivalue)),2) "
//				+ "kpivalue,max(period) as date,'' as unit "
//				+ "from "
//				+ "quota_monitor where nthlr_name = '"+neName+"' group by nthlr_name,kpiname "
//				+ "order by kpiname,nthlr_name"); 
//		
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString());
//		return resultList;
//	}
//
//	@Override
//	public List<Map<String, Object>> queryUnitKpiList(Map<String, Object> m) {
//		String neName="";
//		if(m.get("neName")!=null && !m.get("neName").equals("")){
//			neName=m.get("neName").toString();
//		}
//		StringBuffer sb = new StringBuffer("select "
//				+ "kpiname as kpiid, "
//				+ "case when kpiname = 'kpi001' "
//				+ "then '鉴权请求成功率' "
//				+ "when kpiname = 'kpi002' then '语音呼叫查询成功率' "
//				+ "when kpiname = 'kpi003' then '短信被叫查询成功率' "
//				+ "when kpiname = 'kpi004' "
//				+ "then '位置更新请求成功率' "
//				+ "when kpiname = 'kpi005' then 'GPRS附着成功率' "
//				+ "when kpiname = 'kpi006' then 'LTE位置更新成功率' "
//				+ "when kpiname = "
//				+ "'kpi007' then 'LTE用户鉴权成功率' "
//				+ "when kpiname = 'kpi008' then 'MAP-CancelLocation请求次数' "
//				+ "when kpiname = 'kpi009' then "
//				+ "'S6A-CancelLocation请求次数' "
//				+ "else 'Other' "
//				+ "end as kpiname, "
//				+ "nthlr_name,round((sum(kpivalue)/count(kpivalue)),2) "
//				+ "kpivalue,max(period) as date,'' as unit "
//				+ "from "
//				+ "quota_monitor where node = '"+neName+"' group by nthlr_name,kpiname order by "
//				+ "kpiname,nthlr_name"); 
//		
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString());
//		return resultList;
//	}
//
//	@Override
//	public List<Map<String, Object>> queryNeAlarmList(Map<String, Object> m) {
//		String neName="";
//		if(m.get("neName")!=null && !m.get("neName").equals("")){
//			neName=m.get("neName").toString();
//		}
//		StringBuffer sb = new StringBuffer("select id as alarmid,alarm_level as "
//				+ "alarm_level,alarm_content as alarm_content,start_time as date from alarm_monitor "
//				+ "where unit_name = '"+neName+"' order by "
//				+ "start_time desc limit 50"); 
//		
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString());
//		return resultList;
//	}
//
//	@Override
//	public List<Map<String, Object>> queryUnitAlarmList(Map<String, Object> m) {
//		String neName="";
//		if(m.get("neName")!=null && !m.get("neName").equals("")){
//			neName=m.get("neName").toString();
//		}
//		StringBuffer sb = new StringBuffer("select id as alarmid,alarm_level as "
//				+ "alarm_level,alarm_content as alarm_content,start_time as date from alarm_monitor "
//				+ "where ne_name = '"+neName+"' order by "
//				+ "start_time desc limit 10;"); 
//		
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString());
//		return resultList;
//	}
//
//	@Override
//	public List<Map<String, Object>> queryTopologyNeList(Map<String, Object> m) {
//		String SHHSS="";
//		if(m.get("SHHSS")!=null && !m.get("SHHSS").equals("")){
//			SHHSS=m.get("SHHSS").toString();
//		}
//		StringBuffer sb = new StringBuffer("SELECT "
//				+ "NE.parent_ne,NE.LOCATION,NE.NE,NE_TYPE.NE_TYPE_NAME,NE.IDS_VERSION NE_IDS_VERSION,NE.SW_VERSION "
//				+ "NE_SW_VERSION,NE.REMARKS,UNIT.UNIT,UNIT_TYPE.UNIT_TYPE_NAME,UNIT.IDS_VERSION UNIT_IDS_VERSION,UNIT.SW_VERSION "
//				+ "UNIT_SW_VERSION FROM UNIT,NE,UNIT_TYPE,NE_TYPE WHERE UNIT.NE = NE.ID AND NE.NE_TYPE = NE_TYPE.ID AND UNIT.UNIT_TYPE = "
//				+ "UNIT_TYPE.ID AND NE.parent_ne = '"+SHHSS+"'"); 
//		
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString());
//		return resultList;
//	}
//
//	@Override
//	public List<Map<String, Object>> queryTopologyCsList(Map<String, Object> m) {
//		String SHHSS="";
//		if(m.get("SHHSS")!=null && !m.get("SHHSS").equals("")){
//			SHHSS=m.get("SHHSS").toString();
//		}
//		StringBuffer sb = new StringBuffer("SELECT DISTINCT "
//				+ "CS_NE_RELA.CS_CODE,CS_INFO.CS_NAME FROM CS_NE_RELA,CS_INFO WHERE CS_INFO.CS_CODE = CS_NE_RELA.CS_CODE AND "
//				+ "CS_NE_RELA.parent = '"+SHHSS+"'"); 
//		
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString());
//		return resultList;
//	}
//
//	@Override
//	public List<Map<String, Object>> queryTopologyLineList(Map<String, Object> m) {
//		String SHHSS="";
//		if(m.get("SHHSS")!=null && !m.get("SHHSS").equals("")){
//			SHHSS=m.get("SHHSS").toString();
//		}
//		StringBuffer sb = new StringBuffer("SELECT "
//				+ "CS_CODE,parent,LOCATION,CS_NE_RELA.NE,CS_NE_RELA.UNIT,UNIT_TYPE.UNIT_TYPE_NAME FROM CS_NE_RELA,UNIT,UNIT_TYPE WHERE "
//		+ "CS_NE_RELA.UNIT = UNIT.UNIT AND UNIT.UNIT_TYPE = UNIT_TYPE.ID AND parent = '"+SHHSS+"'"); 
//		
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString());
//		return resultList;
//	}
//
//	@Override
//	public List<Map<String, Object>> queryCoDnFromSite(Map<String, Object> m) {
//		String NEID="";
//		if(m.get("NEID")!=null && !m.get("NEID").equals("")){
//			NEID=m.get("NEID").toString();
//		}
//		StringBuffer sb = new StringBuffer("SELECT NE_COMMON_OBJECTS.CO_DN FROM "
//				+ "NE_COMMON_OBJECTS,NE WHERE NE_COMMON_OBJECTS.NE_NAME = NE.NE AND NE.LOCATION = '"+NEID+"'"); 
//		
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString());
//		return resultList;
//	}
//
//	@Override
//	public Map<String, Object> queryCoDnFromNeUnit(Map<String, Object> m) {
//		String NEID="";
//		if(m.get("NEID")!=null && !m.get("NEID").equals("")){
//			NEID=m.get("NEID").toString();
//		}
//		StringBuffer sb = new StringBuffer("SELECT CO_DN FROM NE_COMMON_OBJECTS WHERE NE_NAME = '"+NEID+"'"); 
//		
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		Map<String, Object> resultMap = jdbcTemplate.queryForMap(sb.toString());
//		Map<String, Object> resultMap = MysqlDB.queryForMap(sb.toString());
//		return resultMap;
//	}
//
//	@Override
//	public List<Map<String, Object>> queryCoDnFromUnitType(Map<String, Object> m) {
//		String unitTypeName="";
//		if(m.get("unitTypeName")!=null && !m.get("unitTypeName").equals("")){
//			unitTypeName=m.get("unitTypeName").toString();
//		}
//		StringBuffer sb = new StringBuffer("SELECT NE_COMMON_OBJECTS.CO_DN FROM "
//				+ "NE_COMMON_OBJECTS,UNIT,UNIT_TYPE WHERE NE_COMMON_OBJECTS.NE_NAME = UNIT.UNIT AND UNIT.UNIT_TYPE = UNIT_TYPE.ID AND "
//		+ "UNIT.UNIT = #{neName} AND UNIT_TYPE.UNIT_TYPE_NAME = '"+unitTypeName+"'"); 
//		
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString());
//		return resultList;
//	}
//
//	@Override
//	public List<Map<String, Object>> queryAlarmRecord(Map<String, Object> m) {
//		List<String> queryAlarmCellList=new ArrayList<String>();
//		queryAlarmCellList=(List<String>) m.get("cellList");
//		StringBuffer sb = new StringBuffer("SELECT ALARM_ID,ALARM_NUM,ALARM_LEVEL,ALARM_TEXT,ALARM_TIME2 "
//				+ "FROM ICES_ALARM_RECORD where 1=2 "); 
//		
//		for(String s:queryAlarmCellList){
//			sb.append(" OR ALARM_CELL = '"+s+"'");
//		}
//		sb.append(" ORDER BY ALARM_TIME2 DESC LIMIT 100");
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString());
//		return resultList;
//	}
//
//	@Override
//	public List<Map<String, Object>> queryNeFromSite(Map<String, Object> m) {
//		String NEID="";
//		if(m.get("NEID")!=null && !m.get("NEID").equals("")){
//			NEID=m.get("NEID").toString();
//		}
//		StringBuffer sb = new StringBuffer("SELECT NE FROM NE WHERE LOCATION = '"+NEID+"'"); 
//		
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString());
//		return resultList;
//	}
//
//	@Override
//	public List<Map<String, Object>> queryUnitFromUnitType(Map<String, Object> m) {
//		String neName="";
//		String unitTypeName="";
//		if(m.get("neName")!=null && !m.get("neName").equals("")){
//			neName=m.get("neName").toString();
//		}
//		if(m.get("unitTypeName")!=null && !m.get("unitTypeName").equals("")){
//			unitTypeName=m.get("unitTypeName").toString();
//		}
//		StringBuffer sb = new StringBuffer("SELECT UNIT.UNIT FROM UNIT,UNIT_TYPE,NE WHERE "
//				+ "UNIT.NE = NE.ID AND UNIT.UNIT_TYPE = UNIT_TYPE.ID "
//				+ "AND NE.NE = '"+neName+"' AND UNIT_TYPE.UNIT_TYPE_NAME = '"+unitTypeName+"'"); 
//		
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString());
//		return resultList;
//	}
//
//	@Override
//	public List<Map<String, Object>> queryAlarmMonotorFromNe(Map<String, Object> m) {
//		List<String> neList=new ArrayList<String>();
//		neList=(List<String>) m.get("neList");
//		StringBuffer sb = new StringBuffer("SELECT ID AS ALARMID,ALARM_LEVEL AS ALARM_LEVEL,ALARM_CONTENT "
//				+ "AS ALARM_CONTENT,START_TIME AS DATE, UNIT_NAME,NE_NAME FROM ALARM_MONITOR where 1=2 "); 
//		
//		for(String s:neList){
//			sb.append(" OR UNIT_NAME = '"+s+"'");
//		}
//		sb.append(" ORDER BY START_TIME DESC LIMIT 100");
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString());
//		return resultList;
//	}
//
//	@Override
//	public List<Map<String, Object>> queryAlarmMonotorFromUnit(Map<String, Object> m) {
//		List<String> unitList=new ArrayList<String>();
//		unitList=(List<String>) m.get("unitList");
//		StringBuffer sb = new StringBuffer("SELECT ID AS ALARMID,ALARM_LEVEL AS ALARM_LEVEL,ALARM_CONTENT "
//				+ "AS ALARM_CONTENT,START_TIME AS DATE, UNIT_NAME,NE_NAME FROM ALARM_MONITOR where 1=2 "); 
//		
//		for(String s:unitList){
//			sb.append(" OR NE_NAME = '"+s+"'");
//		}
//		sb.append(" ORDER BY START_TIME DESC LIMIT 100");
//		List<String> args = new ArrayList<String>();
//		System.out.println(sb);
////		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString());
//		return resultList;
//	}
//}
