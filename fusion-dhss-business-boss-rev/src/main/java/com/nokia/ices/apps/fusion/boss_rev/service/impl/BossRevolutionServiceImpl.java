package com.nokia.ices.apps.fusion.boss_rev.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.boss_rev.service.BossRevolutionService;

@Service(value="bossRevolutionService")
public class BossRevolutionServiceImpl implements BossRevolutionService{
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat SDF_ZERO_SECOND = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
	private static Logger LOGGER = LoggerFactory.getLogger(BossRevolutionServiceImpl.class);
	
	@Autowired
    @Qualifier("jdbcTemplateBoss-rev")
    JdbcTemplate jdbcTemplateBossRev;

	@Override
	public List<Map<String, Object>> getDhssNameAndHlrsnMap(String bossVersion) {
		String sql = "select * from dhss_hlrsn_map";
		List<Map<String, Object>> l = jdbcTemplateBossRev.queryForList(sql);
		return l;
	}
	@Override
	public Map<String, String> getCurrentHourTimePeriod() {
		Calendar nowC = Calendar.getInstance();
		nowC.add(Calendar.HOUR, -24);
		Date startTime = nowC.getTime();
		nowC = Calendar.getInstance();
		Date endTime = nowC.getTime();
		Map<String,String> timeMap = new HashMap<>();
		timeMap.put("startTime", SDF_ZERO_SECOND.format(startTime));
		timeMap.put("endTime", SDF_ZERO_SECOND.format(endTime));
		return timeMap;
	}
	@Override
	public Map<String, String> getDefaultSelectOptionsTimePeriod() {
		Calendar nowC = Calendar.getInstance();
		int nowMinute = nowC.get(Calendar.MINUTE);
		if(nowMinute<15){
			nowC.set(Calendar.MINUTE, 0);
		}else if(nowMinute>=15&&nowMinute<30){
			nowC.set(Calendar.MINUTE, 15);
		}else if(nowMinute>=30&&nowMinute<45){
			nowC.set(Calendar.MINUTE, 30);
		}else if(nowMinute>45){
			nowC.set(Calendar.MINUTE, 45);
		}
		Date endTime = nowC.getTime();
		nowC.add(Calendar.HOUR, -1);
		Date startTime = nowC.getTime();
		Map<String, String> timeMap = new HashMap<>();
		timeMap.put("startTime", SDF_ZERO_SECOND.format(startTime));
		timeMap.put("endTime", SDF_ZERO_SECOND.format(endTime));
		return timeMap;
	}
//	@Override
//	public Map<String, String> getDefaultSelectOptionsTimePeriod() {
//		Calendar nowC = Calendar.getInstance();
//		Date endTime = nowC.getTime();
//		nowC.add(Calendar.HOUR, -3);
//		Date startTime = nowC.getTime();
//		Map<String, String> timeMap = new HashMap<>();
//		timeMap.put("startTime", SDF_ZERO_SECOND.format(startTime));
//		timeMap.put("endTime", SDF_ZERO_SECOND.format(endTime));
//		return timeMap;
//	}
	@Override
	public Map<String,Object> getOperationName(String bossVersion) {
		String sql = "";
		if(bossVersion.equalsIgnoreCase("chinamobile")){
			sql = "select * from boss_cmd_type where boss_version = 'chinamobile'";
		}else if(bossVersion.equalsIgnoreCase("unicom")){
			sql = "select * from boss_cmd_type where boss_version = 'unicom'";
		}
		List<Map<String,Object>> l = jdbcTemplateBossRev.queryForList(sql);
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("data", l);
		resultMap.put("size", l.size());
		return resultMap;
	}
	@Override
	public Map<String, Object> getErrorType(String bossVersion) {
		String sql = "select id,error_code,error_code_desc from boss_error_code";
		List<Map<String,Object>> l = jdbcTemplateBossRev.queryForList(sql);
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("data", l);
		resultMap.put("size", l.size());
		return resultMap;
	}
	
	public String getNumberSerieCondition(String sql, String numberSerie) {
		if(!numberSerie.isEmpty()){
			if(!numberSerie.contains(",")){
				if(numberSerie.startsWith("460")){//用户输入的是IMSI
					String imsi = numberSerie;
					sql = sql + " and imsi = "+imsi;
				}else{
					String msisdn = numberSerie;
					sql = sql + " and msisdn = "+msisdn;
				}
			}else{//多条查询
				List<String> imsiList = new ArrayList<>();
				List<String> msisdnList = new ArrayList<>();
				int conditionCount = 0;
				String[] numbers = numberSerie.split(",");
				for (String number : numbers) {
					if(number.startsWith("460")){//用户输入的是IMSI
						imsiList.add(number);
					}else{
						msisdnList.add(number);
					}
				}
				String[] orConditions = new String[imsiList.size()+msisdnList.size()];
				for (String imsi : imsiList) {
					orConditions[conditionCount] = " imsi = "+imsi;
					conditionCount++;
				}
				for (String msisdn : msisdnList) {
					orConditions[conditionCount] = " msisdn = "+msisdn;
					conditionCount++;
				}
				sql = sql+"and (";
				for (String orCondition : orConditions) {
					sql = sql +orCondition+" or ";
				}
				sql = sql.substring(0, sql.length()-3);
				sql = sql+")";
			}
		}
		return sql;
	}
	@Override
	public Map<String, Object> getBossDataDetail(String taskId,String bossVersion) {
		String sql = "select task_id from boss_err_case where task_id = ?";
		if(bossVersion.equalsIgnoreCase("unicom")){
			sql = sql.replace("boss_err_case", "boss_err_case_cuc");
		}
		List<Map<String, Object>> l = jdbcTemplateBossRev.queryForList(sql, taskId);
		Map<String, Object> resultInfoMap = new HashMap<>();
		if (l.size() >= 1) {
			resultInfoMap.put("callback_result", "failure");
		} else {
			resultInfoMap.put("callback_result", "success");
		}
		String sqlJoin = "select soap_log,err_log from boss_join where task_id = ?";
		List<Map<String, Object>> l_ = jdbcTemplateBossRev.queryForList(sqlJoin, taskId);
		StringBuilder soapLog = new StringBuilder();
		StringBuilder errLog = new StringBuilder();
		for (Map<String, Object> map : l_) {
			String aSoapLog = String.valueOf(map.get("soap_log"));
			if(aSoapLog.length()>4){
				soapLog.append(aSoapLog+"\n");
			}
			String aErrLog = String.valueOf(map.get("err_log"));
			if(aErrLog.length()>4){
				errLog.append(aErrLog+"\n");
			}
		}
		resultInfoMap.put("soap_log", soapLog);
		resultInfoMap.put("err_log", errLog);
		return resultInfoMap;
	}

	@Override
	public List<Map<String, Object>> getBossKpi(Map<String, Object> paraMap,String bossVersion) {
		String tableName = "";
		String timeField = "";
		String timeFineness = paraMap.get("timeFineness").toString();
		if(timeFineness.equals("15min")){
			tableName = "boss_monitor_minute";
			timeField = "period_start_time";
		}else if(timeFineness.equals("1hour")){
			tableName = "boss_monitor_hour";
			timeField = "period_start_hour";
		}else if(timeFineness.equals("1day")){
			tableName = "boss_monitor_day";
			timeField = "period_start_day";
		}else if(timeFineness.equals("1month")){
			tableName = "boss_monitor_month";
			timeField = "period_start_month";
		}
		String hlrsn = paraMap.get("hlrsn").toString();
		String businessType = paraMap.get("businessType").toString();
		String startTime = paraMap.get("startTime").toString();
		String endTime = paraMap.get("endTime").toString();
		String sql = "select business_type,"+timeField+",hlrsn,sum(fail_count) as fail_count,sum(total_count) as total_count from "+tableName+" where 1=1 and "+timeField+">'"+startTime+"' and "+timeField+"< '"+endTime+"'";
		if(StringUtils.isNotEmpty(hlrsn)){
			sql += "and hlrsn = '"+hlrsn+"'";
		}
		if(StringUtils.isNotEmpty(businessType)){
			sql += "and business_type = '"+businessType+"'";
		}
		sql = sql+" group by "+timeField+", hlrsn,business_type order by "+timeField+" ,hlrsn,business_type";
		if(!StringUtils.isNotEmpty(hlrsn)){
			sql = sql.replace("hlrsn,", "");
		}
		LOGGER.info("sql:");
		LOGGER.info(sql);
		LOGGER.info("params");
		DecimalFormat df = new DecimalFormat("##.##");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String,Object>> resultList = jdbcTemplateBossRev.queryForList(sql);
		for (Map<String, Object> resultMap : resultList) {
			Double failCount = Double.valueOf(resultMap.get("fail_count").toString());
			Double totalCount = Double.valueOf(resultMap.get("total_count").toString());
			Double successRate = (1-failCount/totalCount)*100.0;
			if(successRate<0.0){
				successRate = 0.0;
			}
			String successRateStr = df.format(successRate);
			String periodStartTime = sdf.format((Date)resultMap.get(timeField));
			resultMap.put("successRate", successRateStr);
			resultMap.put("periodStartTimeStr",periodStartTime);
		}
		return resultList;
	}

	@Override
	public List<Map<String, Object>> getBossExportDataByCondition(Map<String, Object> paraMap, String bossVersion) {
		String sql = "select task_id,response_time,hlrsn,msisdn,imsi,operation_name from boss_soap where 1=1 ";
		String numberSerie = paraMap.get("numberSerie").toString();
		LOGGER.info("BOSS VERSION:"+bossVersion);
		LOGGER.info("REAL SQL:");
		String resultType = paraMap.get("resultType").toString();
		if(bossVersion.equals("unicom")){
			sql = sql.replace("boss_soap", "boss_soap_cuc");
		}
		if(resultType.equals("failure")){
			sql = sql.replace("boss_soap", "boss_err_case");
		}
		sql = getNumberSerieCondition(sql, numberSerie);

		String hlrsn = paraMap.get("hlrsn").toString();
		if (!hlrsn.isEmpty()) {
			sql = sql + " and hlrsn = '" + hlrsn + "'";
		}
		String operationName = paraMap.get("operationName").toString();
		if (!operationName.isEmpty()) {
			sql = sql + " and operation_name = '" + operationName + "'";
		}
		String errorCode = paraMap.get("errorType").toString();
		if (!errorCode.isEmpty()) {
			sql = sql.replace("boss_soap", "boss_err_case");
			sql = sql + " and error_code = " + errorCode ;
		}
		String startTime = paraMap.get("startTime").toString();
		String endTime = paraMap.get("endTime").toString();
		sql = sql + " and response_time > '" + startTime + "' and response_time < '" + endTime
				+ "' order by response_time desc";
		sql = sql + " limit 10000";
		LOGGER.info(sql);
		List<Map<String, Object>> l = jdbcTemplateBossRev.queryForList(sql);
		for (Map<String, Object> map : l) {
			String responseTimeStr = SDF.format((Date) map.get("response_time"));
			map.put("response_time_str", responseTimeStr);
			if(resultType.equals("failure")){
				map.put("callback_result", "failure");
			}else{
				map.put("callback_result", "success");
			}
		}
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("resultList", l);
		return l;
	}
	@Override
	public Map<String, Object> getBossQueryDataAndCount(Map<String, Object> paraMap,String bossVersion) {
		String resultType = paraMap.get("resultType").toString();
		Map<String,Object> resultMap = new HashMap<>();
		if(resultType.equals("all")){
			allBossData(paraMap, bossVersion, resultType, resultMap);
		}else if(resultType.equals("success")){
			successBossData(paraMap, bossVersion, resultType, resultMap);
		}else if(resultType.equals("failure")){
			failureBossData(paraMap, bossVersion, resultType, resultMap);
		}
		return resultMap;
	}
	private void successBossData(Map<String, Object> paraMap, String bossVersion, String resultType,
			Map<String, Object> resultMap) {
		String sql = "select a.* from boss_soap a LEFT JOIN boss_err_case b ON a.task_id = b.task_id WHERE b.task_id IS NULL";
		if(bossVersion.equals("unicom")){
			sql = sql.replace("boss_soap", "boss_soap_cuc");
			sql = sql.replace("boss_err_case", "boss_err_case_cuc");
		}
		String numberSerie = paraMap.get("numberSerie").toString();
		sql = getNumberSerieCondition(sql, numberSerie);
		sql = sql.replaceAll("imsi", "a.imsi");
		sql = sql.replaceAll("msisdn", "a.msisdn");
		String hlrsn = paraMap.get("hlrsn").toString();
		if (!hlrsn.isEmpty()) {
			sql = sql + " and a.hlrsn = '" + hlrsn + "'";
		}
		String operationName = paraMap.get("operationName").toString();
		if (!operationName.isEmpty()) {
			sql = sql + " and a.operation_name = '" + operationName + "'";
		}
		String startTime = paraMap.get("startTime").toString();
		String endTime = paraMap.get("endTime").toString();
		sql = sql + " and a.response_time > '" + startTime + "' and a.response_time < '" + endTime
				+ "' order by a.response_time desc ";
		Integer pageNumber = Integer.valueOf(paraMap.get("pageNumber").toString());
		Integer pageSize = Integer.valueOf(paraMap.get("pageSize").toString());
		LOGGER.info("LIMIT INFO:");
		String limitIndex1 = String.valueOf((pageNumber-1)*pageSize);
		String limitIndex2 = String.valueOf(pageSize);
		String limitSql =  " limit "+ limitIndex1 + "," + limitIndex2;
		sql = sql + limitSql;
		LOGGER.info("SUCCESS DATA SQL:");
		LOGGER.info(sql);
		List<Map<String, Object>> dataList = jdbcTemplateBossRev.queryForList(sql);
		for (Map<String, Object> map : dataList) {
			String responseTimeStr = SDF.format((Date) map.get("response_time"));
			map.put("response_time_str", responseTimeStr);
			map.put("error_code", "");
			map.put("error_message", "");
			map.put("callback_result", "success");
		}
		Integer soapCount = getRealPageTotal(paraMap, bossVersion, "all");
		Integer errCount = getRealPageTotal(paraMap, bossVersion, "failure");
		resultMap.put("data", dataList);
		resultMap.put("total", soapCount-errCount);
	}
	public void failureBossData(Map<String, Object> paraMap, String bossVersion, String resultType,
			Map<String, Object> resultMap) {
		String sql = getRealPageData(paraMap, bossVersion,resultType);
		if(bossVersion.equals("unicom")){
			sql=sql.replace("boss_soap_cuc", "boss_err_case_cuc");
		}else if(bossVersion.equals("chinamobile")){
			sql=sql.replace("boss_soap", "boss_err_case");
		}
		List<Map<String, Object>> dataList = jdbcTemplateBossRev.queryForList(sql);
		for (Map<String, Object> map : dataList) {
			String responseTimeStr = SDF.format((Date) map.get("response_time"));
			map.put("response_time_str", responseTimeStr);
			map.put("callback_result", "failure");
		}
		Integer totalCount = getRealPageTotal(paraMap, bossVersion,resultType);
		resultMap.put("data", dataList);
		resultMap.put("total", totalCount);
	}
	public void allBossData(Map<String, Object> paraMap, String bossVersion, String resultType,
			Map<String, Object> resultMap) {
		String sql = getRealPageData(paraMap, bossVersion,resultType);
		List<Map<String, Object>> dataList = jdbcTemplateBossRev.queryForList(sql);
		for (Map<String, Object> map : dataList) {
			String responseTimeStr = SDF.format((Date) map.get("response_time"));
			map.put("response_time_str", responseTimeStr);
			String taskId = map.get("task_id").toString();
			String resultSql = "select task_id,response_time  as fail_time,error_message,error_code from boss_err_case where task_id = '"+taskId+"'";
			if(bossVersion.equals("unicom")){
				resultSql = resultSql.replace("boss_err_case", "boss_err_case_cuc");
			}
			List<Map<String, Object>> resultErrList = jdbcTemplateBossRev.queryForList(resultSql);
			if(resultErrList.size()>=1){
				map.put("callback_result", "failure");
				String errorCode = resultErrList.get(0).get("error_code").toString()==null?"":resultErrList.get(0).get("error_code").toString();
				String errorMessage = resultErrList.get(0).get("error_message").toString()==null?"":resultErrList.get(0).get("error_message").toString();
				map.put("error_message",errorMessage);
				map.put("error_code",errorCode);
			}else{
				map.put("callback_result", "success");
				map.put("error_message","");
				map.put("error_code","");
			}
		}
		
		Integer totalCount = getRealPageTotal(paraMap, bossVersion,resultType);
		resultMap.put("data", dataList);
		resultMap.put("total", totalCount);
	}
	public Integer getRealPageTotal(Map<String, Object> paraMap, String bossVersion,String resultType) {
		String sql = "select count(*) as total_count from table_name where 1=1 ";
		String numberSerie = paraMap.get("numberSerie").toString();
		LOGGER.info("BOSS VERSION:" + bossVersion);
		sql = getNumberSerieCondition(sql, numberSerie);
		String hlrsn = paraMap.get("hlrsn").toString();
		if (!hlrsn.isEmpty()) {
			sql = sql + " and hlrsn = '" + hlrsn + "'";
		}
		String operationName = paraMap.get("operationName").toString();
		if (!operationName.isEmpty()) {
			sql = sql + " and operation_name = '" + operationName + "'";
		}
		String errorCode = paraMap.get("errorType").toString();
		if (!errorCode.isEmpty()) {
			sql = sql.replace("boss_soap", "boss_err_case");
			sql = sql + " and error_code = " + errorCode;
		}
		String startTime = paraMap.get("startTime").toString();
		String endTime = paraMap.get("endTime").toString();
		sql = sql + " and response_time > '" + startTime + "' and response_time < '" + endTime
				+ "' order by response_time";
		if(bossVersion.equals("unicom")){
			sql = sql.replace("table_name", "table_name_cuc");
		}
		if(resultType.equals("all")){
			sql = sql.replace("table_name", "boss_soap");
		}else if(resultType.equals("failure")){
			sql = sql.replace("table_name", "boss_err_case");
		}
		LOGGER.info("REAL COUNT SQL:");
		LOGGER.info(sql);
		List<Map<String,Object>> totalList = jdbcTemplateBossRev.queryForList(sql);
		Integer totalCount = Integer.valueOf(totalList.get(0).get("total_count").toString());
		return totalCount;
	}
	public String getRealPageData(Map<String, Object> paraMap, String bossVersion,String resultType) {
		String sql = "select task_id,response_time,hlrsn,msisdn,imsi,operation_name from boss_soap where 1=1 ";
		String numberSerie = paraMap.get("numberSerie").toString();
		LOGGER.info("BOSS VERSION:"+bossVersion);
		LOGGER.info("REAL SQL:");
		if(resultType.equals("failure")){
			sql = sql.replace(",operation_name", ",operation_name,error_code,error_message");
			
		}
		if(bossVersion.equals("unicom")){
			sql = sql.replace("boss_soap", "boss_soap_cuc");
		}
		sql = getNumberSerieCondition(sql, numberSerie);

		String hlrsn = paraMap.get("hlrsn").toString();
		if (!hlrsn.isEmpty()) {
			sql = sql + " and hlrsn = '" + hlrsn + "'";
		}
		String operationName = paraMap.get("operationName").toString();
		String operationCondition = " and operation_name = '" + operationName + "'";
		if (!operationName.isEmpty()) {
			sql = sql + operationCondition;
		}
		String errorCode = paraMap.get("errorType").toString();
		if(!errorCode.isEmpty()){
			sql = sql + " and error_code = '"+errorCode+"'";
		}

		String startTime = paraMap.get("startTime").toString();
		String endTime = paraMap.get("endTime").toString();
		sql = sql + " and response_time > '" + startTime + "' and response_time < '" + endTime
				+ "' order by response_time desc";
		Integer pageNumber = Integer.valueOf(paraMap.get("pageNumber").toString());
		Integer pageSize = Integer.valueOf(paraMap.get("pageSize").toString());
		LOGGER.info("LIMIT INFO:");
		String limitIndex1 = String.valueOf((pageNumber-1)*pageSize);
		String limitIndex2 = String.valueOf(pageSize);
		String limitSql =  " limit "+ limitIndex1 + "," + limitIndex2;
		sql = sql + limitSql;
		LOGGER.info(sql);
		return sql;
	}
	@Override
	public void createErrorType(String bossVersion, Map<String, Object> paraMap) {
		String createSql = "insert into boss_error_code(error_code,error_code_desc)values(:errorCode,:errorCodeDesc)";
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplateBossRev);
		namedParameterJdbcTemplate.update(createSql, paraMap);
	}
	@Override
	public void updateErrorType(String bossVersion, Map<String, Object> paraMap) {
		String updateSql = "update boss_error_code set error_code = :errorCode , error_code_desc = :errorCodeDesc where id = :id";
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplateBossRev);
		namedParameterJdbcTemplate.update(updateSql, paraMap);
	}
	@Override
	public void destroyErrorType(String bossVersion,Integer id) {
		String destroySql = "delete from boss_error_code where id = ?";
		jdbcTemplateBossRev.update(destroySql,id);
	}
	@Override
	public void updateOperationName(String bossVersion, Map<String, Object> valueMap) {
		valueMap.put("bossVersion", bossVersion);
		String updateSql = "update boss_cmd_type set operation_name = :operationName , business_type = :businessType,boss_version=:bossVersion where id = :id";
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplateBossRev);
		namedParameterJdbcTemplate.update(updateSql, valueMap);
		
	}
	@Override
	public void destroyOperationName(String bossVersion, Integer id) {
		String destroySql = "delete from boss_cmd_type where id = ?";
		jdbcTemplateBossRev.update(destroySql,id);
	}
	@Override
	public void createOperationName(String bossVersion, Map<String, Object> valueMap) {
		valueMap.put("bossVersion", bossVersion);
		String createSql = "insert into boss_cmd_type(operation_name,business_type,boss_version)values(:operationName,:businessType,:bossVersion)";
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplateBossRev);
		namedParameterJdbcTemplate.update(createSql, valueMap);
	}
}
