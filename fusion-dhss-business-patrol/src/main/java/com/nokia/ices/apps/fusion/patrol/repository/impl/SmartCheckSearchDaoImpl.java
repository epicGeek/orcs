package com.nokia.ices.apps.fusion.patrol.repository.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckSearchDao;

@Repository("smartCheckSearchDao")
public class SmartCheckSearchDaoImpl implements SmartCheckSearchDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public Map<String, Object> getSmartCheckResultPageList(
			Map<String, Object> map) {
		String type = map.get("type") !=null ? map.get("type").toString() : null;
		String neType = map.get("neType") !=null ? map.get("neType").toString() : null;
		String neName = map.get("neName") !=null ? map.get("neName").toString() : null;
		String unitType = map.get("unitType") !=null ? map.get("unitType").toString() : null;
		String checkItemName = map.get("checkItemName") !=null ? map.get("checkItemName").toString() : null;
		String checkItemId = map.get("checkItemId") !=null ? map.get("checkItemId").toString() : null;
		String scheduleId = map.get("scheduleId") !=null ? map.get("scheduleId").toString() : null;
		String isExport = map.get("isExport") !=null ? map.get("isExport").toString() : null;
		StringBuffer sb = new StringBuffer("SELECT ");
		String pageSize = map.get("limit") !=null ? map.get("limit").toString() : null;
		String page = map.get("offset") !=null ? map.get("offset").toString() : null;
		int currPage = (Integer.parseInt(page) - 1 )* Integer.parseInt(pageSize);
		
		if(type != null && !"".equals(type) && "2".equals(type)){
			sb.append(" NE_TYPE,NE_TYPE_NAME,NE_ID,NE_NAME,(SELECT ");
			sb.append(" COUNT(DISTINCT unit_id) FROM smart_check_result b WHERE");
			sb.append(" result_code = 0");
			sb.append(" AND b.schedule_id = a.schedule_id and a.NE_ID=b.NE_ID )");
			sb.append(" AS");
			sb.append(" RESULT_CODE,");
		}else{
			sb.append(" CHECK_ITEM_ID,CHECK_ITEM_NAME,(SELECT COUNT(DISTINCT");
			sb.append(" unit_id) FROM smart_check_result b WHERE");
			sb.append(" result_code = 0");
			sb.append(" AND");
			sb.append(" b.schedule_id = a.schedule_id and a.CHECK_ITEM_ID=b.CHECK_ITEM_ID )");
			sb.append(" AS");
			sb.append(" RESULT_CODE,");
		}
		sb.append(" COUNT(DISTINCT UNIT_ID) AS UNIT_AMOUNT,");
		sb.append(" SCHEDULE_ID,DATE_FORMAT(START_TIME,'%Y%m%d%H%m%s')");
		sb.append(" START_TIME");
		sb.append(" FROM");
		sb.append(" smart_check_result a");
		sb.append(" WHERE 1=1 ");
		List<String> args = new ArrayList<String>();
		if(StringUtils.isNotEmpty(neType)){
			sb.append(" AND a.NE_TYPE_ID= ? ");
			args.add(neType);
		}
		if(StringUtils.isNotEmpty(neName)){
			sb.append(" AND LOCATE( ? ,a.NE_NAME)");
			args.add(neName);
		}
		if(StringUtils.isNotEmpty(unitType)){
			sb.append(" and a.UNIT_TYPE_ID= ? ");
			args.add(unitType);
		}
		if(StringUtils.isNotEmpty(checkItemName)){
			sb.append(" and LOCATE( ? ,a.CHECK_ITEM_NAME)");
			args.add(checkItemName);
		}
		if(StringUtils.isNotEmpty(checkItemId)){
			sb.append(" and a.CHECK_ITEM_ID= ? ");
			args.add(checkItemId);
		}
		if(StringUtils.isNotEmpty(scheduleId)){
			sb.append(" and a.SCHEDULE_ID= ? ");
			args.add(scheduleId);
		}
		
		if(type != null && !"".equals(type) && "2".equals(type)){
			sb.append(" GROUP BY NE_TYPE,NE_ID");
		}else{
			sb.append(" GROUP BY CHECK_ITEM_ID");
		}
		sb.append(" order by RESULT_CODE desc ");
		Integer total = null;
		Object [] array = args.toArray();
		if(isExport == null || isExport.equals("")){
			total = jdbcTemplate.queryForList(sb.toString(),array).size();
			sb.append(" limit "+pageSize+" offset "+currPage);
		}
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sb.toString(),array);
		Map<String, Object> resultMaps = new HashMap<String, Object>();
		resultMaps.put("totalElements", total);
		resultMaps.put("content", resultList);
		return resultMaps;
	}
	
	@Override
	public List<Map<String, Object>> getSmartCheckCountResult(){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String startTime = sf.format(new Date()) + " 00:00:00";
		String sql = "SELECT COUNT(DISTINCT unit_name) as count FROM smart_check_result WHERE start_time >='"+ startTime +"' AND check_item_name like '%cpu%' AND result_code is false";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
		return result;
	}


	@Override
	public List<Map<String, Object>> getSmartCheckDetailResultPageList(
			Map<String, Object> map) {
		StringBuffer sb = new StringBuffer("SELECT ID,"+
				" NE_TYPE,NE_ID,NE_NAME,UNIT_ID,UNIT_NAME,UNIT_TYPE,CHECK_ITEM_ID,CHECK_ITEM_NAME,"+
				" RESULT_CODE,ifNull(ERROR_MESSAGE,'')"+
				" ERROR_MESSAGE,ifNull(FILE_PATH,'')"+
				" FILE_PATH,SCHEDULE_ID,DATE_FORMAT(START_TIME,'%Y%m%d%H%m%s')"+
				" START_TIME,LOG_STATE"+
				" FROM smart_check_result a WHERE 1=1");
		String id = map.get("id") == null ? "" : map.get("id").toString();
		String neId = map.get("neId") == null ? "" : map.get("neId").toString();
		String unitId = map.get("unitId") == null ? "" : map.get("unitId").toString();
		String neTypeId = map.get("neTypeId") == null ? "" : map.get("neTypeId").toString();
		String unitTypeId = map.get("unitTypeId") == null ? "" : map.get("unitTypeId").toString();
		String checkItemId = map.get("checkItemId") == null ?  "" :  map.get("checkItemId").toString();
		String checkItemName = map.get("checkItemName") == null ? "" : map.get("checkItemName").toString();
		String scheduleId = map.get("scheduleId") == null ? "" : map.get("scheduleId").toString();
		List<String> args = new ArrayList<String>();
		if(StringUtils.isNotEmpty(id)){
			sb.append(" and a.id= ? ");
			args.add(id);
		}
		if(StringUtils.isNotEmpty(neId)){
			sb.append(" and a.NE_ID= ? ");
			args.add(neId);
		}
		if(StringUtils.isNotBlank(unitId)){
			sb.append(" and a.UNIT_ID= ? ");
			args.add(unitId);
		}
		if(StringUtils.isNotEmpty(neTypeId)){
			sb.append(" and a.NE_TYPE= ? ");
			args.add(neTypeId);
		}
		if(StringUtils.isNotEmpty(unitTypeId)){
			sb.append(" and a.UNIT_TYPE= ? ");
			args.add(unitTypeId);
		}
		if(StringUtils.isNotEmpty(checkItemId)){
			sb.append(" and a.CHECK_ITEM_ID= ? ");
			args.add(checkItemId);
		}
		if(StringUtils.isNotEmpty(checkItemName)){
			sb.append("and ( LOCATE( ? ,a.CHECK_ITEM_NAME) or LOCATE( ? ,a.UNIT_NAME))");
			args.add(checkItemName);
			args.add(checkItemName);
		}
		if(StringUtils.isNotEmpty(scheduleId)){
			sb.append(" and a.SCHEDULE_ID= ? ");
			args.add(scheduleId);
		}
		sb.append(" order by UNIT_ID desc");
		return jdbcTemplate.queryForList(sb.toString(),args.toArray());
	}


	@Override
	public List<Map<String, Object>> getSmartCheckDetailResultPageList(String id) {
		StringBuffer sb = new StringBuffer("SELECT ne.dhss_name,result.ID,result.NE_TYPE,result.NE_ID,result.NE_NAME,"
				+ "result.UNIT_ID,result.UNIT_NAME,result.UNIT_TYPE,result.CHECK_ITEM_ID,result.CHECK_ITEM_NAME,"
				+ "result.RESULT_CODE,ifNull(result.ERROR_MESSAGE,'') ERROR_MESSAGE,ifNull(result.FILE_PATH,'') FILE_PATH,result.SCHEDULE_ID,"
				+ "DATE_FORMAT(result.START_TIME,'%Y%m%d%H%m%s') START_TIME,result.LOG_STATE FROM smart_check_result result,equipment_ne ne "
				+ "where result.schedule_id in ("+id.substring(1)+") AND  result.ne_id = ne.id;");
		return jdbcTemplate.queryForList(sb.toString());
	}
	
}