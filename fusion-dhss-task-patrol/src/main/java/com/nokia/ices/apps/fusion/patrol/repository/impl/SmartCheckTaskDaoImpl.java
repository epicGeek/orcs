//package com.nokia.ices.apps.fusion.patrol.repository.impl;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//
//import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
//import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckTastDao;
//
//@Repository("smartCheckTaskDao")
//public class SmartCheckTaskDaoImpl implements SmartCheckTastDao {
//
//	@Autowired
//	JdbcTemplate jdbcTemplate;
//	
//	
//	
//	//定时巡检
//
//	@Override
//	public Map<String, Object> getScheduleResult(Map<String, Object> map) {
//		StringBuffer sb = new StringBuffer("SELECT ID,JOB_NAME as jobName,JOB_DESC as jobDesc,EXEC_TIME as"+
//				" jobHour,ifNull(LOOP_HOUR,'')loopHour,DATE_FORMAT(NEXT_DAY,'%Y-%m-%d')"+
//				" as jobDay,JOB_TYPE as jobType FROM"+
//				" smart_check_job a WHERE EXEC_FLAG=1");
//		String jobDay = map.get("jobDay") == null ? "" : map.get("jobDay").toString();
//		String jobHour = map.get("jobHour") == null ? "" : map.get("jobHour").toString();
//		List<String> array = new ArrayList<String>();
//		if(StringUtils.isNotEmpty(jobDay)){
//			sb.append(" AND NEXT_DAY = ? ");
//			array.add(jobDay);
//		}
//		if(StringUtils.isNotEmpty(jobHour)){
//			sb.append(" AND (EXEC_TIME = ? or LOOP_HOUR = ? ) ");
//			array.add(jobHour);
//			array.add(jobHour);
//		}
//		List<Map<String, Object>> results = jdbcTemplate.queryForList(sb.toString(),array.toArray());
//		Map<String, Object> maps = new HashMap<String, Object>();
//		maps.put("content", results);
//		return maps;
//	}
//
//	@Override
//	public CommandCheckItem searchCheckDetailTmp(Map<String, Object> map) {
//		StringBuffer sb = new StringBuffer("SELECT d.id AS unitId,d.unit_name"+
//				" AS"+
//				" unitName,"+
//				" e.command,e.account"+
//				" username,HEX(d.root_password) rootPwd,"+
//				" b.NE_ID"+
//				" as neId,b.NE_TYPE_ID as"+
//				" neTypeId,b.NE_TYPE_NAME as"+
//				" neTypeName,b.NE_NAME as neName,"+
//				" b.UNIT_TYPE_ID as"+
//				" unitTypeId,b.UNIT_TYPE_NAME as unitTypeName,"+
//				" a.id AS"+
//				" scheduleId,"+
//				" a.START_TIME as startTime, c.CHECK_ITEM_ID as"+
//				" checkItemId,e.`name` as checkItemName"+
//				" FROM ( SELECT * FROM"+
//				" smart_check_schedule_result WHERE EXEC_FLAG=1"+
//				" AND"+
//				" START_TIME=CONCAT('2015-9-10',' ','12:00') ) a,"+
//				" smart_check_ne"+
//				" b,smart_check_schedule c,equipment_unit d,command_check_item e"+
//				" WHERE"+
//				" a.job_id=b.JOB_ID"+
//				" AND"+
//				" a.job_id=c.JOB_ID"+
//				" AND d.ne_id=b.NE_ID AND"+
//				" d.unit_type_id=b.UNIT_TYPE_ID"+
//				" AND e.id=c.CHECK_ITEM_ID"+
//				" AND EXISTS("+
//				" SELECT 'X' FROM command_group_check_item"+
//				" r,command_group t"+
//				" WHERE r.command_group_resource_id=t.resource_id AND t.ne_type_id=b.NE_TYPE_ID AND"+
//				" t.unit_type_id=b.UNIT_TYPE_ID"+
//				" AND r.check_item_id=c.CHECK_ITEM_ID )");
//		List<CommandCheckItem> list = jdbcTemplate.queryForList(sb.toString(), CommandCheckItem.class);
//		return list.size() == 0 ? null : list.get(0);
//	}
//
//	@Override
//	public Map<String, Object> getTimeOutCheckDetailTmpList(
//			Map<String, Object> map) {
//		StringBuffer sb = new StringBuffer("SELECT a.id,a.UU_ID AS UUID FROM SMART_CHECK_RESULT_TMP a where 1=1 ");
//		String ids = map.get("ids") == null ? "" : map.get("ids").toString();
//		String timeOut = map.get("timeOut") == null ? "" : map.get("timeOut").toString();
//		List<String> args = new ArrayList<String>();
//		if(StringUtils.isNotEmpty(ids)){
//			sb.append(" and UU_ID= ? ");
//			args.add(ids);
//		}
//		if(StringUtils.isNotEmpty(timeOut)){
//			sb.append(" AND TIMESTAMPDIFF(MINUTE,a.START_TIME,NOW())> ? ");
//			args.add(timeOut);
//		}
//		List<Map<String, Object>> results =jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		Map<String, Object> maps = new HashMap<String, Object>();
//		maps.put("content", results);
//		return maps;
//	}
//
//	@Override
//	public Map<String, Object> getCheckDetailTmpList(Map<String, Object> map) {
//		StringBuffer sb = new StringBuffer("SELECT a.id,a.UU_ID AS UUID,a.UNIT_ID AS unitId,a.UNIT_NAME AS"+
//				" unitName,a.command,a.username,a.ROOT_PWD AS rootPwd,a.NE_ID AS"+
//				" neId,a.NE_TYPE_ID"+
//				" AS neTypeId,a.NE_TYPE_NAME AS neTypeName,"+
//				" a.NE_NAME AS"+
//				" neName,a.UNIT_TYPE_ID"+
//				" AS unitTypeId,a.UNIT_TYPE_NAME AS"+
//				" unitTypeName,a.SCHEDULE_ID AS"+
//				" scheduleId,DATE_FORMAT(a.START_TIME,'%Y-%m-%d %H:%i') AS startTime,"+
//				" a.CHECK_ITEM_ID AS checkItemId,a.CHECK_ITEM_NAME AS"+
//				" checkItemName,a.EXEC_FLAG AS execFlag,b.script,c.JOB_NAME as jobName"+
//				" FROM SMART_CHECK_RESULT_TMP a"+
//				" LEFT JOIN command_check_item b ON"+
//				" a.CHECK_ITEM_ID=b.id"+
//				" LEFT JOIN smart_check_schedule_result c on"+
//				" a.schedule_id=c.id where 1=1 ");
//		String ids = map.get("ids") == null ? "" : map.get("ids").toString();
//		String timeOut = map.get("timeOut") == null ? "" : map.get("timeOut").toString();
//		List<String> args = new ArrayList<String>();
//		if(StringUtils.isNotEmpty(ids)){
//			sb.append(" and UU_ID not in( ? ) ");
//			args.add(ids);
//		}
//		if(StringUtils.isNotEmpty(timeOut)){
//			sb.append(" AND TIMESTAMPDIFF(MINUTE,a.START_TIME,NOW()) > ? ");
//			args.add(timeOut);
//		}
//		List<Map<String, Object>> results =jdbcTemplate.queryForList(sb.toString(),args.toArray());
//		Map<String, Object> maps = new HashMap<String, Object>();
//		maps.put("content", results);
//		return maps;
//	}
//
//	@Override
//	public Map<String, Object> findAllWithDHLR(Map<String, Object> map) {
//		String sql = "SELECT ne_name,dhss_name FROM equipment_ne";
//		List<Map<String, Object>> results =jdbcTemplate.queryForList(sql);
//		Map<String, Object> maps = new HashMap<String, Object>();
//		maps.put("content", results);
//		return maps;
//	}
//
//	@Override
//	public Map<String, Object> getAllJob(Map<String, Object> map) {
//		String sql = "SELECT ID,JOB_NAME AS jobName,JOB_DESC AS jobDesc,EXEC_TIME AS " +
//		" jobHour,LOOP_HOUR as loopHour,DATE_FORMAT(NEXT_DAY,'%Y-%m-%d') AS" +
//		" jobDay,JOB_TYPE AS jobType FROM" +
//		" smart_check_job a WHERE EXEC_FLAG=1 AND" +
//		" CONCAT(NEXT_DAY,' ',CASE WHEN a.LOOP_HOUR IS NULL THEN a.EXEC_TIME" +
//		" ELSE a.LOOP_HOUR END) <" +
//		" DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i')" ;
//		List<Map<String, Object>> results =jdbcTemplate.queryForList(sql);
//		Map<String, Object> maps = new HashMap<String, Object>();
//		maps.put("content", results);
//		return maps;
//	}
//
//	@Override
//	public Map<String, Object> findUnfinishedId() {
//		String sql = "SELECT ID FROM smart_check_schedule_result WHERE exec_flag=1 ";
//		List<Map<String, Object>> results =jdbcTemplate.queryForList(sql);
//		Map<String, Object> maps = new HashMap<String, Object>();
//		maps.put("content", results);
//		return maps;
//	}
//
//	@Override
//	public Map<String, Object> findJobIdByTime(Map<String, Object> map) {
//		String sql = "SELECT ID FROM smart_check_job WHERE EXEC_FLAG=1 ";
//		String jobDay = map.get("jobDay") == null ? "" : map.get("jobDay").toString();
//		String jobHour = map.get("jobHour") == null ? "" : map.get("jobHour").toString();
//		List<String> args = new ArrayList<String>();
//		if(StringUtils.isNotEmpty(jobDay)){
//			sql += " AND NEXT_DAY= ? ";
//			args.add(jobDay);
//		}
//		if(StringUtils.isNotEmpty(jobHour)){
//			sql += " AND ( EXEC_TIME= ? or LOOP_HOUR= ? ) ";
//			args.add(jobHour);
//			args.add(jobHour);
//		}
//		List<Map<String, Object>> results =jdbcTemplate.queryForList(sql,args.toArray());
//		Map<String, Object> maps = new HashMap<String, Object>();
//		maps.put("content", results);
//		return maps;
//	}
//
//	@Override
//	public void updateCheckItem(Map<String, Object> map) {
//		String sql = "update smart_check_result_tmp set ";
//		String filePath = map.get("filePath") == null ? "" : map.get("filePath").toString();
//		String ERROR_MESSAGE = map.get("ERROR_MESSAGE") == null ? "" : map.get("ERROR_MESSAGE").toString();
//		List<String> array = new ArrayList<String>();
//		if(StringUtils.isNotEmpty(filePath)){
//			sql += " FILE_PATH = ? ,";
//			array.add(filePath);
//		}
//		if(StringUtils.isNotEmpty(ERROR_MESSAGE)){
//			sql += " ERROR_MESSAGE = ? ";
//			array.add(ERROR_MESSAGE);
//		}
//		
//		sql += " where id = ? ";
//		array.add(map.get("id").toString());
//		jdbcTemplate.update(sql,array.toArray());
//		
//	}
//
//	@Override
//	public void updateJob(Map<String, Object> map) {
//		String NEXT_DAY = map.get("NEXT_DAY") == null ? "" : map.get("NEXT_DAY").toString();
//		String LOOP_HOUR = map.get("LOOP_HOUR") == null ? ""  : map.get("LOOP_HOUR").toString();
//		String id = map.get("id") == null ? "" : map.get("id").toString();
//		String sql = "update SMART_CHECK_JOB set ";
//		List<String> array = new ArrayList<String>();
//		if(StringUtils.isNotEmpty(NEXT_DAY)){
//			sql += " NEXT_DAY = ? ,";
//			array.add(NEXT_DAY);
//		}
//		if(StringUtils.isNotEmpty(LOOP_HOUR)){
//			sql += " LOOP_HOUR = ? ,";
//			array.add(LOOP_HOUR);
//		}
//		sql += " CREATE_TIME = ? ";
//		array.add(new Date().toString());
//		sql += " where id = ? ";
//		array.add(id);
//		jdbcTemplate.update(sql,array.toArray());
//	}
//
//	@Override
//	public void deleteByUUID(String uuId) {
//		String sql = "delete from SMART_CHECK_RESULT_TMP where UU_ID= ? ";
//		jdbcTemplate.update(sql, uuId);
//	}
//
//	@Override
//	public void saveCheckDetailTmp(String contexts) {
//		String sql = "insert into SMART_CHECK_RESULT_TMP(UU_ID,UNIT_ID,UNIT_NAME,COMMAND,USERNAME,ROOT_PWD,NE_ID,"+
//		"NE_TYPE_ID,NE_TYPE_NAME,NE_NAME,UNIT_TYPE_ID,UNIT_TYPE_NAME,SCHEDULE_ID,START_TIME,CHECK_ITEM_ID,CHECK_ITEM_NAME) values " + contexts;
//		jdbcTemplate.update(sql);
//	}
//
//	@Override
//	public void saveCheckDetailTmpList(Map<String, Object> map) {
//		String sql = "INSERT INTO"+
//		" SMART_CHECK_RESULT(NE_ID,NE_TYPE_ID,NE_TYPE_NAME,NE_NAME,UNIT_ID,UNIT_TYPE_ID,UNIT_TYPE_NAME,UNIT_NAME,CHECK_ITEM_ID,CHECK_ITEM_NAME,"+
//		" SCHEDULE_ID,RESULT_CODE,ERROR_MESSAGE,FILE_PATH,START_TIME)"+
//		" SELECT"+
//		" NE_ID,NE_TYPE_ID,NE_TYPE_NAME,NE_NAME,UNIT_ID,UNIT_TYPE_ID,UNIT_TYPE_NAME,UNIT_NAME,CHECK_ITEM_ID,CHECK_ITEM_NAME,"+
//		" SCHEDULE_ID, ? , ? ,FILE_PATH,START_TIME FROM"+
//		" SMART_CHECK_RESULT_TMP a"+
//		" WHERE"+
//		" TIMESTAMPDIFF(MINUTE,a.START_TIME,NOW())  >  ? ";
//		String resultCode = map.get("resultCode") == null ? "" : map.get("resultCode").toString();
//		String errorMessage = map.get("errorMessage") == null ? "" : map.get("errorMessage").toString();
//		String timeOut = map.get("timeOut") == null ? "" : map.get("timeOut").toString();
//		jdbcTemplate.update(sql,resultCode,errorMessage,timeOut);
//	}
//
//	@Override
//	public void deleteTimeOutCheckDetailTmpList(Map<String, Object> map) {
//		String timeOut = map.get("timeOut") == null ? "" : map.get("timeOut").toString();
//		String sql = "delete FROM SMART_CHECK_RESULT_TMP WHERE TIMESTAMPDIFF(MINUTE,START_TIME,NOW())> ? ";
//		jdbcTemplate.update(sql,timeOut);
//	}
//
//	@Override
//	public void checkState(Map<String, Object> map) {
//		String ids = map.get("ids") == null ? "" : map.get("ids").toString();
//		String sql = "UPDATE"+
//		"smart_check_schedule_result a,("+
//		"SELECT schedule_id,(SELECT"+
//		"COUNT(DISTINCT unit_id) FROM smart_check_result b WHERE result_code=1"+
//		"AND b.schedule_id in ?"+
//		"AND b.schedule_id = a.schedule_id ) AS"+
//		"resultCode,COUNT(*) AS"+
//		"AMOUNT_JOB"+
//		"FROM smart_check_result a"+
//		"WHERE"+
//		"schedule_id IN ?"+
//		"GROUP BY schedule_id"+
//		") b"+
//		"SET"+
//		"a.EXEC_FLAG=2,a.ERROR_UNIT=b.resultCode"+
//		"WHERE a.id in ?"+
//		"and"+
//		"a.id=b.schedule_id AND"+
//		"a.AMOUNT_JOB=b.AMOUNT_JOB";
//		jdbcTemplate.update(sql,ids,ids,ids);
//	}
//
//	@Override
//	public void saveScheduleResult(Map<String, Object> map) {
//		String ids = map.get("ids") == null ? "" : map.get("ids").toString();
//		String sql = "insert into"+
//				" smart_check_schedule_result(JOB_ID,JOB_NAME,JOB_DESC,START_TIME,EXEC_FLAG,AMOUNT_UNIT,ERROR_UNIT,AMOUNT_JOB)"+
//				" SELECT a.ID,a.JOB_NAME,a.JOB_DESC,DATE_FORMAT(CONCAT(a.NEXT_DAY,'"+
//				" ',CASE WHEN a.LOOP_HOUR IS NULL THEN a.EXEC_TIME ELSE a.LOOP_HOUR END)"+
//				" ,'%Y-%m-%d %H:%i:00')AS EXEC_TIME,1,"+
//				" COUNT(DISTINCT d.id) AS"+
//				" AMOUNT_UNIT,0,COUNT(d.id) AS AMOUNT_JOB FROM "+
//				" (select * from smart_check_job where id in ? )a,"+
//				" (select * from smart_check_ne where job_id in ? )b,"+
//				" (select * from smart_check_schedule where job_id in ? )c ,unit d,unit_account"+
//				" f,account"+
//				" g,check_item e"+
//				" WHERE"+
//				" a.id=b.job_id"+
//				" AND"+
//				" a.id=c.job_id"+
//				" AND d.ne=b.NE_ID"+
//				" AND"+
//				" d.unit_type=b.UNIT_TYPE_ID"+
//				" AND d.id=f.unit AND e.id =c.CHECK_ITEM_ID"+
//				" AND f.account=g.id"+
//				" AND EXISTS("+
//				" SELECT 'X' FROM check_item_command_group r,command_group t"+
//				" WHERE r.command_group=t.id AND t.ne_type=b.NE_TYPE_ID AND"+
//				" t.unit_type=b.UNIT_TYPE_ID"+
//				" AND r.check_item=c.CHECK_ITEM_ID"+
//				" )"+
//				" GROUP BY"+
//				" a.ID"+
//				" HAVING AMOUNT_UNIT>0";
//		jdbcTemplate.update(sql,ids,ids,ids);
//	}
//}