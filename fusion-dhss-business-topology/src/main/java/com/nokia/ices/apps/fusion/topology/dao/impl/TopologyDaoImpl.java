package com.nokia.ices.apps.fusion.topology.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nokia.ices.apps.fusion.topology.dao.TopologyDao;

@Repository("TopologyDao")
public class TopologyDaoImpl implements  TopologyDao{
	@Autowired
	JdbcTemplate jdbcTemplate;

	/*@SuppressWarnings("unused")
	@Override
	public List<Map<String, Object>> queryNeList(Map<String, Object> m) {
		String SHHSS="";
		if(m.get("SHHSS")!=null && !m.get("SHHSS").equals("")){
			SHHSS=m.get("SHHSS").toString();
		}
		StringBuffer sb = new StringBuffer(""
		+ "select distinct parent as neid, parent as "
		+ "nename,'ALLHSS' as netype,'' as father, 'YES' as son, '0' as nestate, '' as  "
		+ "location, ids_version as "
		+ "idsversion,sw_version as swversion,'' as remarks from ne where ne.parent = '"+SHHSS+"' "
		+ "union "
		+ "select distinct location as "
		+ "neid, location as nename,'SITE' as netype, parent as father,'YES' as son,'0' as nestate,'' "
		+ "as location,ids_version as "
		+ "idsversion,sw_version as swversion,remarks from ne where ne.parent = '"+SHHSS+"' "
		+ "union "
		+ "select ne "
		+ "as neid,ne as "
		+ "nename,ne_type.ne_type_name as netype,location as father,'YES' as son, '0' as nestate, '' as "
		+ "location,ids_version as "
		+ "idsversion,sw_version as swversion,remarks from (select * from ne where ne.parent = '"+SHHSS+"') "
		+ "ne left join ne_type on "
		+ "ne.ne_type = ne_type.id "
		+ "union "
		+ "select unit as neid,unit as nename,unit_type.unit_type_name as "
		+ "netype,ne.ne as father,'' as "
		+ "son, '0' as nestate, '' as location,unit.ids_version as idsversion,unit.sw_version as "
		+ "swversion,remarks from unit left "
		+ "join unit_type on unit.unit_type = unit_type.id inner join ne on unit.ne = ne.id and ne.parent = '"+SHHSS+"'"); 
		
		List<String> args = new ArrayList<String>();
		System.out.println(sb);
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString())= new ArrayList<Map<String, Object>>();
		return resultList;
	}*/

	/*@SuppressWarnings("unused")
	@Override
	public List<Map<String, Object>> queryCsDomainList(Map<String, Object> m) {
		String SHHSS="";
		if(m.get("SHHSS")!=null && !m.get("SHHSS").equals("")){
			SHHSS=m.get("SHHSS").toString();
		}
		StringBuffer sb = new StringBuffer("select 'CE' as neid,'CE' as nename,'ALLCE' as "
				+ "netype,'' as father,'YES' as son "
				+ "union "
				+ "select cs_name as neid,cs_name as "
				+ "nename,'CE' as netype,'CE' as father,'' as son "
				+ "from (select * from csdomain where status ='1') csdomain inner join "
				+ "(select * from ne where parent = '"+SHHSS+"') ne on "
				+ "csdomain.ne = ne.id"); 
		
		List<String> args = new ArrayList<String>();
		System.out.println(sb);
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString())= new ArrayList<Map<String, Object>>();
		return resultList;
	}*/

	/*@SuppressWarnings("unused")
	@Override
	public List<Map<String, Object>> queryLineList(Map<String, Object> m) {
		String SHHSS="";
		if(m.get("SHHSS")!=null && !m.get("SHHSS").equals("")){
			SHHSS=m.get("SHHSS").toString();
		}
		StringBuffer sb = new StringBuffer("select csdomain.id as id,ne.ne as "
				+ "fromid,ne_type.ne_type_name as fromnetype,cs_name as toid,'CE' as tonetype,cs_name as "
				+ "linename,'0' as state from "
				+ "(select * from csdomain where status='1')csdomain inner join ne on csdomain.ne = ne.id "
				+ "inner join ne_type on ne.ne_type "
				+ "= ne_type.id and ne.parent = '"+SHHSS+"' "
				+ "union "
				+ "select csdomain.id as id,unit.unit as "
				+ "fromid,unit_type.unit_type_name as "
				+ "fromnetype,cs_name as toid,'CE' as tonetype,cs_name as linename,'0' as state from "
				+ "(select * from csdomain where "
				+ "status='2')csdomain inner join unit on csdomain.ne = unit.id inner join unit_type on "
				+ "unit.unit_type = unit_type.id "
				+ "inner join ne on unit.ne = ne.id and ne.parent='"+SHHSS+"'"); 
		
		List<String> args = new ArrayList<String>();
		System.out.println(sb);
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString())= new ArrayList<Map<String, Object>>();
		return resultList;
	}*/

	@Override
	public List<Map<String, Object>> queryNeKpiList(Map<String, Object> m) {
		String neName=m.get("neName")!=null && !m.get("neName").equals("") ? m.get("neName").toString() : "";
		StringBuffer sb = new StringBuffer(/*"select "
				+ "kpiname as kpiid, "
				+ "case when kpiname = 'kpi001' "
				+ "then '鉴权请求成功率' "
				+ "when kpiname = 'kpi002' then '语音呼叫查询成功率' "
				+ "when kpiname = 'kpi003' then '短信被叫查询成功率' "
				+ "when kpiname = 'kpi004' "
				+ "then '位置更新请求成功率' "
				+ "when kpiname = 'kpi005' then 'GPRS附着成功率' "
				+ "when kpiname = 'kpi006' then 'LTE位置更新成功率' "
				+ "when kpiname = "
				+ "'kpi007' then 'LTE用户鉴权成功率' "
				+ "when kpiname = 'kpi008' then 'MAP-CancelLocation请求次数' "
				+ "when kpiname = 'kpi009' then "
				+ "'S6A-CancelLocation请求次数' "
				+ "else 'Other' "
				+ "end as kpiname, "
				+ "nthlr_name,round((sum(kpivalue)/count(kpivalue)),2) "
				+ "kpivalue,max(period) as date,'' as unit "
				+ "from "
				+ "quota_monitor where nthlr_name = '"+neName+"' group by nthlr_name,kpiname "
				+ "order by kpiname,nthlr_name"*/); 
		sb.append("SELECT id as kpiid,kpi_name as kpiname,kpi_value as kpivalue,unit_name as unit,period_start_time "
				+ "as date FROM quota_monitor WHERE " + m.get("TYPE") + " = '" + neName + "' GROUP BY kpi_name,ne_name ORDER BY kpi_name,ne_name");
		
		return jdbcTemplate.queryForList(sb.toString());
	}

	/*@SuppressWarnings("unused")
	@Override
	public List<Map<String, Object>> queryUnitKpiList(Map<String, Object> m) {
		String neName="";
		if(m.get("neName")!=null && !m.get("neName").equals("")){
			neName=m.get("neName").toString();
		}
		StringBuffer sb = new StringBuffer("select "
				+ "kpiname as kpiid, "
				+ "case when kpiname = 'kpi001' "
				+ "then '鉴权请求成功率' "
				+ "when kpiname = 'kpi002' then '语音呼叫查询成功率' "
				+ "when kpiname = 'kpi003' then '短信被叫查询成功率' "
				+ "when kpiname = 'kpi004' "
				+ "then '位置更新请求成功率' "
				+ "when kpiname = 'kpi005' then 'GPRS附着成功率' "
				+ "when kpiname = 'kpi006' then 'LTE位置更新成功率' "
				+ "when kpiname = "
				+ "'kpi007' then 'LTE用户鉴权成功率' "
				+ "when kpiname = 'kpi008' then 'MAP-CancelLocation请求次数' "
				+ "when kpiname = 'kpi009' then "
				+ "'S6A-CancelLocation请求次数' "
				+ "else 'Other' "
				+ "end as kpiname, "
				+ "nthlr_name,round((sum(kpivalue)/count(kpivalue)),2) "
				+ "kpivalue,max(period) as date,'' as unit "
				+ "from "
				+ "quota_monitor where node = '"+neName+"' group by nthlr_name,kpiname order by "
				+ "kpiname,nthlr_name"); 
		
		List<String> args = new ArrayList<String>();
		System.out.println(sb);
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		List<Map<String, Object>> resultList /*= MysqlDB.queryForList(sb.toString())= new ArrayList<Map<String, Object>>();
		return resultList;
	}*/

	@SuppressWarnings("unused")
	@Override
	public List<Map<String, Object>> queryNeAlarmList(Map<String, Object> m) {
		String neName="";
		if(m.get("neName")!=null && !m.get("neName").equals("")){
			neName=m.get("neName").toString();
		}
		StringBuffer sb = new StringBuffer("select id as alarmid,alarm_level as "
				+ "alarm_level,alarm_content as alarm_content,start_time as date from alarm_monitor "
				+ "where unit_name = '"+neName+"' order by "
				+ "start_time desc limit 50"); 
		
		List<String> args = new ArrayList<String>();
		System.out.println(sb);
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
		List<Map<String, Object>> resultList /*= MysqlDB.queryForList(sb.toString())*/= new ArrayList<Map<String, Object>>();
		return resultList;
	}

	@SuppressWarnings("unused")
	@Override
	public List<Map<String, Object>> queryUnitAlarmList(Map<String, Object> m) {
		String neName="";
		if(m.get("neName")!=null && !m.get("neName").equals("")){
			neName=m.get("neName").toString();
		}
		StringBuffer sb = new StringBuffer("select id as alarmid,alarm_level as "
				+ "alarm_level,alarm_content as alarm_content,start_time as date from alarm_monitor "
				+ "where ne_name = '"+neName+"' order by "
				+ "start_time desc limit 10;"); 
		
		List<String> args = new ArrayList<String>();
		System.out.println(sb);
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
		List<Map<String, Object>> resultList /*= MysqlDB.queryForList(sb.toString())*/= new ArrayList<Map<String, Object>>();
		return resultList;
	}

	@Override
	public List<Map<String, Object>> queryTopologyNeList(Map<String, Object> m) {
		String SHHSS=m.get("SHHSS")!=null && !m.get("SHHSS").equals("") ? m.get("SHHSS").toString() : "";
		
		StringBuffer sb = new StringBuffer(/*"SELECT "
				+ "NE.parent,NE.LOCATION,NE.NE,NE_TYPE.NE_TYPE_NAME,NE.IDS_VERSION NE_IDS_VERSION,NE.SW_VERSION "
				+ "NE_SW_VERSION,NE.REMARKS,UNIT.UNIT,UNIT_TYPE.UNIT_TYPE_NAME,UNIT.IDS_VERSION UNIT_IDS_VERSION,UNIT.SW_VERSION "
				+ "UNIT_SW_VERSION FROM UNIT,NE,UNIT_TYPE,NE_TYPE WHERE UNIT.NE = NE.ID AND NE.NE_TYPE = NE_TYPE.ID AND UNIT.UNIT_TYPE = "
				+ "UNIT_TYPE.ID AND NE.parent = '"+SHHSS+"'"*/);
		
		sb.append("SELECT dhss_name,ne.location,ne.ne_name,ne.ne_ids_version,ne.ne_sw_version,ne.remarks,unit.unit_type,"+
					"unit.unit_name,unit.unit_ids_version,unit.unit_sw_version,rela.cs_code FROM equipment_unit unit INNER JOIN equipment_ne ne ON unit.ne_id = ne.id"+
				" LEFT JOIN cs_ne_rela rela ON unit.unit_name = rela.unit WHERE ne.dhss_name = '" + SHHSS + "'");
		
		return  jdbcTemplate.queryForList(sb.toString());
		
	}

	@Override
	public List<Map<String, Object>> queryTopologyCsList(Map<String, Object> m) {
		String SHHSS=m.get("SHHSS")!=null && !m.get("SHHSS").equals("") ? m.get("SHHSS").toString() : "";
		
		StringBuffer sb = new StringBuffer("SELECT DISTINCT rela.cs_code,info.cs_name FROM " + 
				" cs_ne_rela as rela,cs_info as info WHERE info.cs_code = rela.cs_code AND rela.parent = '"+SHHSS+"'"); 
//		sb.append("SELECT * FROM equipment_ne WHERE dhss_name = '" + SHHSS + "'");
		return jdbcTemplate.queryForList(sb.toString());
	}

	/*@SuppressWarnings("unused")
	@Override
	public List<Map<String, Object>> queryTopologyLineList(Map<String, Object> m) {
		String SHHSS="";
		if(m.get("SHHSS")!=null && !m.get("SHHSS").equals("")){
			SHHSS=m.get("SHHSS").toString();
		}
		StringBuffer sb = new StringBuffer("SELECT "
				+ "CS_CODE,parent,LOCATION,CS_NE_RELA.NE,CS_NE_RELA.UNIT,UNIT_TYPE.UNIT_TYPE_NAME FROM CS_NE_RELA,UNIT,UNIT_TYPE WHERE "
		+ "CS_NE_RELA.UNIT = UNIT.UNIT AND UNIT.UNIT_TYPE = UNIT_TYPE.ID AND parent = '"+SHHSS+"'"); 
		
		List<String> args = new ArrayList<String>();
		System.out.println(sb);
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),args.toArray());
		List<Map<String, Object>> resultList = MysqlDB.queryForList(sb.toString())= new ArrayList<Map<String, Object>>();
		return resultList;
	}*/

	@Override
	public List<Map<String, Object>> queryCoDnFromSite(Map<String, Object> m) {
		String NEID= m.get("NEID")!=null && !m.get("NEID").equals("") ? m.get("NEID").toString() : "";
		
		StringBuffer sb = new StringBuffer(/*"SELECT NE_COMMON_OBJECTS.CO_DN FROM "
				+ "NE_COMMON_OBJECTS,NE WHERE NE_COMMON_OBJECTS.NE_NAME = NE.NE AND NE.LOCATION = '"+NEID+"'"*/); 
		sb.append("SELECT * FROM equipment_ne WHERE location = '" + NEID + "'");
		
		return jdbcTemplate.queryForList(sb.toString());
	}

	@Override
	public Map<String, Object> queryCoDnFromNeUnit(Map<String, Object> m) {
		
		String NEID=m.get("NEID")!=null && !m.get("NEID").equals("") ? m.get("NEID").toString() : "";
		
		StringBuffer sb = new StringBuffer(/*"SELECT CO_DN FROM NE_COMMON_OBJECTS WHERE NE_NAME = '"+NEID+"'"*/); 
		
		sb.append("SELECT * FROM equipment_ne WHERE ne_name = '" + NEID + "'");
		
		return jdbcTemplate.queryForMap(sb.toString());
		
	}

	@Override
	public List<Map<String, Object>> queryCoDnFromUnitType(Map<String, Object> m) {
		String unitTypeName=m.get("unitTypeName")!=null && !m.get("unitTypeName").equals("") ? m.get("unitTypeName").toString() : "";
		
		String neName=m.get("neName")!=null && !m.get("neName").equals("") ? m.get("neName").toString() : "";
		
		StringBuffer sb = new StringBuffer(/*"SELECT NE_COMMON_OBJECTS.CO_DN FROM "
				+ "NE_COMMON_OBJECTS,UNIT,UNIT_TYPE WHERE NE_COMMON_OBJECTS.NE_NAME = UNIT.UNIT AND UNIT.UNIT_TYPE = UNIT_TYPE.ID AND "
		+ "UNIT.UNIT = #{neName} AND UNIT_TYPE.UNIT_TYPE_NAME = '"+unitTypeName+"'"*/); 
		
		sb.append("SELECT * FROM equipment_unit WHERE unit_name = '" + neName +"' and unit_type = '" + unitTypeName + "'");
		
		return jdbcTemplate.queryForList(sb.toString());
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Map<String, Object>> queryAlarmRecord(Map<String, Object> m) {
		
		List<String> queryAlarmCellList=new ArrayList<String>();
		
		queryAlarmCellList=(List<String>) m.get("cellList");
		
		StringBuffer sb = new StringBuffer(/*"SELECT ALARM_ID,ALARM_NUM,ALARM_LEVEL,ALARM_TEXT,ALARM_TIME2 FROM ICES_ALARM_RECORD where 1=2 "*/); 
		
		sb.append("SELECT * FROM alarm_receive_record where 1=2");
		
		for(String s:queryAlarmCellList){ sb.append(" OR alarm_cell = '"+s+"'"); }
		
		sb.append(" ORDER BY start_time DESC LIMIT 100");
		
		return  jdbcTemplate.queryForList(sb.toString());
	}

	@Override
	public List<Map<String, Object>> queryNeFromSite(Map<String, Object> m) {
		String NEID=m.get("NEID")!=null && !m.get("NEID").equals("") ? m.get("NEID").toString() : "";
		
		StringBuffer sb = new StringBuffer("SELECT * FROM equipment_ne WHERE location = '"+NEID+"'"); 
		
		return jdbcTemplate.queryForList(sb.toString());
	}

	@Override
	public List<Map<String, Object>> queryUnitFromUnitType(Map<String, Object> m) {

		
		String unitTypeName=m.get("unitTypeName")!=null && !m.get("unitTypeName").equals("") ? m.get("unitTypeName").toString() : "";
		
		String neName=m.get("neName")!=null && !m.get("neName").equals("") ? m.get("neName").toString() : "";
		
		StringBuffer sb = new StringBuffer(/*"SELECT UNIT.UNIT FROM UNIT,UNIT_TYPE,NE WHERE "
				+ "UNIT.NE = NE.ID AND UNIT.UNIT_TYPE = UNIT_TYPE.ID "
				+ "AND NE.NE = '"+neName+"' AND UNIT_TYPE.UNIT_TYPE_NAME = '"+unitTypeName+"'"*/); 
		
		sb.append("SELECT * FROM equipment_unit,equipment_ne WHERE equipment_unit.ne_id = "
				+ "equipment_ne.id AND equipment_ne.ne_name = '" + neName + "' and equipment_unit.unit_type = '" + unitTypeName + "'");
		
		return jdbcTemplate.queryForList(sb.toString());
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Map<String, Object>> queryAlarmMonotorFromNe(Map<String, Object> m) {
		
		List<String> neList=new ArrayList<String>();
		
		neList=(List<String>) m.get("neList");
		
		StringBuffer sb = new StringBuffer(/*"SELECT ID AS ALARMID,ALARM_LEVEL AS ALARM_LEVEL,ALARM_CONTENT "
				+ "AS ALARM_CONTENT,START_TIME AS DATE, UNIT_NAME,NE_NAME FROM ALARM_MONITOR where 1=2 "*/); 
		
		sb.append("SELECT * FROM alarm_monitor where 1=2 ");
		
		for(String s:neList){ sb.append(" OR unit_name = '"+s+"'"); }
		
		sb.append(" ORDER BY start_time DESC LIMIT 100");
		
		return  jdbcTemplate.queryForList(sb.toString());
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Map<String, Object>> queryAlarmMonotorFromUnit(Map<String, Object> m) {
		
		List<String> unitList=new ArrayList<String>();
		
		unitList=(List<String>) m.get("unitList");
		
		StringBuffer sb = new StringBuffer(/*"SELECT ID AS ALARMID,ALARM_LEVEL AS ALARM_LEVEL,ALARM_CONTENT "
				+ "AS ALARM_CONTENT,START_TIME AS DATE, UNIT_NAME,NE_NAME FROM ALARM_MONITOR where 1=2 "*/); 
		
		sb.append("SELECT * FROM alarm_monitor where 1=2 ");
		
		for(String s:unitList){ sb.append(" OR ne_name = '"+s+"'"); }
		
		sb.append(" ORDER BY start_time DESC LIMIT 100");
		
		return jdbcTemplate.queryForList(sb.toString());
	}
}
