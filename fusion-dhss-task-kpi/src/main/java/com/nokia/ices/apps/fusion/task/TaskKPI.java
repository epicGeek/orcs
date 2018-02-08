package com.nokia.ices.apps.fusion.task;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nokia.ices.apps.fusion.CustomSettings;
import com.nokia.ices.apps.fusion.alarm.AlarmMonitor;
import com.nokia.ices.apps.fusion.alarm.repository.AlarmMonitorRepository;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.kpi.domain.KpiItem;
import com.nokia.ices.apps.fusion.quota.domain.QuotaMonitor;

@Component
@EnableScheduling
public class TaskKPI {
	private static final Logger logger = LoggerFactory.getLogger(TaskKPI.class);
	private static Map<String, EquipmentNe> equipmentNeMap = new HashMap<String, EquipmentNe>();
	private static Map<String, EquipmentUnit> equipmentUnitMap = new HashMap<String, EquipmentUnit>();
	private static final SimpleDateFormat standardTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat hourFormat = new SimpleDateFormat("HH");

	@Autowired
	private AlarmMonitorRepository alarmMonitorRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;//mysql 数据源

	@Autowired
	@Qualifier("jdbcTemplateOss")
	private JdbcTemplate jdbcTemplateOss; //OSS数据源
	
	//@Scheduled(cron = "0 5/15 * * * ?") //每小时5,20,35,50跑
	//@Scheduled(initialDelay = 200, fixedDelay = 30000000) // local test
	
	
//	@Scheduled(cron = "0 0/1 * * * ?")
//		public void judgeToStart() throws IOException{
//			List<Map<String, Object>> listoss = jdbcTemplateOss.queryForList("select sysdate from dual");
//			Date ossTime = (Date)listoss.get(0).get("SYSDATE");
//			String ossTimeMinStr = minuteTimeFormat.format(ossTime);
//			logger.info("OSS time:" + ossTimeMinStr);
//			if(ossTimeMinStr.equalsIgnoreCase("05")||ossTimeMinStr.equalsIgnoreCase("20")||ossTimeMinStr.equalsIgnoreCase("35")||ossTimeMinStr.equalsIgnoreCase("50")){
//				logger.info("Required time on!:"+ossTimeMinStr);
//				executeKPITask();
//			}else{
//				logger.info("NOT REQUIRED TIME.Exit." + ossTimeMinStr);
//			}
//		}
	//@Scheduled(initialDelay = 200, fixedDelay = 30000) // local test
	@Scheduled(cron = "${spring.report.task-kpi-cron}") //每小时5,20,35,50跑
	public void executeKPITask() {
		deleteOldData();
		getEquipmentInfo();
		getOSSData();
		cancelKpiAlarm();
		judgeToAlarm();

	}
	/**
	 * 删除一个月之前的数据
	 */
	private void deleteOldData() {
		 	Calendar today = Calendar.getInstance();
		 	Integer holdMonth = CustomSettings.getHoldKpiDataMonth();
		 	logger.info("hold kpi data for "+holdMonth+" months");
		 	today.add(Calendar.MONTH, -holdMonth);//获得一个月前的时间点
	        String targetTime = standardTimeFormat.format(today.getTime());
		 	String deleteSql = "DELETE FROM quota_monitor_history WHERE period_start_time < '"+targetTime+"'";
		 	logger.info(deleteSql);
		 	//删除一个月（默认）前的数据。
		 	jdbcTemplate.update(deleteSql);
	}
	/**
	 * 查找有效的门限来判断是否触发告警
	 */
	private void judgeToAlarm() {
		// 看看配了哪些有效门限
		//有效门限约束条件：规定了比较方法、监控时间、请求次数基数（分母要足够大）、门限值和取消告警的门限值的门限配置。
		String sql = "SELECT\n" +
				"	*\n" +
				"FROM\n" +
				"	kpi_item\n" +
				"WHERE\n" +
				"	compare_method IS NOT NULL\n" +
				"AND compare_method != ''\n" +
				"AND monitor_time_string IS NOT NULL\n" +
				"AND monitor_time_string != ''\n" +
				"AND request_sample IS NOT NULL\n" +
				"AND request_sample != ''\n" +
				"AND threshold IS NOT NULL\n" +
				"AND threshold != ''\n" +
				"AND threshold_cancel IS NOT NULL\n" +
				"AND threshold_cancel != ''";
		List<KpiItem> kpiThresholdList = jdbcTemplate.query(sql, new Object[] {},
				new BeanPropertyRowMapper<KpiItem>(KpiItem.class));
		if (kpiThresholdList.size() == 0) {
			logger.info("No available kpi threshold config...");
			return;
		}
		List<QuotaMonitor> quotaMonitorList = jdbcTemplate.query("select * from quota_monitor", new Object[] {},
				new BeanPropertyRowMapper<QuotaMonitor>(QuotaMonitor.class));
		//根据配好的有效门限，在最新的一个周期里判断哪些门限是需要告警的。
		compareKpiAndThreshold(kpiThresholdList, quotaMonitorList);
	}
	/**
	 * 门限判断方法
	 * @param kpiThresholdList 门限配置
	 * @param quotaMonitorList 最新周期KPI
	 */
	private void compareKpiAndThreshold(List<KpiItem> kpiThresholdList, List<QuotaMonitor> quotaMonitorList) {
		for (KpiItem kpiThreshold : kpiThresholdList) {
			logger.info("Kpi threshold config --> kpi name:"+kpiThreshold.getKpiName()+",kpi code:"+kpiThreshold.getKpiCode()+", compared_method:"+kpiThreshold.getCompareMethod()+",threshold:"+kpiThreshold.getThreshold()+",cancel value:"+kpiThreshold.getThresholdCancel()+",monitoring time:"+kpiThreshold.getMonitorTimeString()+",base sample :"+kpiThreshold.getRequestSample());
			for (QuotaMonitor quotaMonitor : quotaMonitorList) {
				if(quotaMonitor.getKpiCode().equals(kpiThreshold.getKpiCode())){//match kpi and its threshold
					//KPICODE是这条KPI的后台标识，与KPI一对一且不重复。
					Integer startHour = Integer.valueOf(kpiThreshold.getMonitorTimeString().split("-")[0]);
					Integer stopHour = Integer.valueOf(kpiThreshold.getMonitorTimeString().split("-")[1]);
					Integer kpiTime = Integer.valueOf(hourFormat.format(quotaMonitor.getPeriodStartTime()));
					//告警条件 1：这条KPI的发生时间应该发生在监控时间段内
						if(startHour<kpiTime&&kpiTime<stopHour){
//							logger.info("monitor time string:"+kpiThreshold.getMonitorTimeString());
//							logger.info("start hour:"+startHour+",stop hour:"+stopHour);
//							logger.info("kpi time:"+quotaMonitor.getPeriodStartTime()+",kpi hour:"+kpiTime);
							//if this kpi happened in the monitoring time:
							//告警条件2：这条KPI的请求次数应该足够大，大于配置里的REQUESTSAMPLE这个字段。
							if(quotaMonitor.getKpiRequestCount()>kpiThreshold.getRequestSample()){
								//request is big enough
								logger.info("kpi request count:"+quotaMonitor.getKpiRequestCount()+",base sample:"+kpiThreshold.getRequestSample());
								
								//根据门限判断方法来判断 这条KPI是不是应该触发告警。
								if(kpiThreshold.getCompareMethod().equals("<")){
									if(quotaMonitor.getKpiValue() < Double.valueOf(kpiThreshold.getThreshold())){
										setAlarmContent(quotaMonitor, kpiThreshold);
										logger.info("A kpi alarm has happened: this kpi value is lower than its threshold");
										logger.info("this kpi:"+quotaMonitor.getKpiValue());
										logger.info("its threshold config:"+kpiThreshold.getThreshold());
									}
								}
								if(kpiThreshold.getCompareMethod().equals(">")){
									if(quotaMonitor.getKpiValue() > Double.valueOf(kpiThreshold.getThreshold())){
										setAlarmContent(quotaMonitor, kpiThreshold);
										logger.info("A kpi alarm has happened: this kpi value is bigger than its threshold");
										logger.info("this kpi:"+quotaMonitor.getKpiValue());
										logger.info("its threshold config:"+kpiThreshold.getThreshold());
									}
								}
							}
						}
				}
			}
		}
	}
	/**
	 * 设置告警内容
	 * @param quotaMonitor 告警数据
	 * @param kpiItem 门限规则
	 */
	private void setAlarmContent(QuotaMonitor quotaMonitor, KpiItem kpiItem) {
		logger.info("-------------setting kpi alarm info-------------------------------------");
		logger.info(quotaMonitor.getKpiName() + "触发了告警！KPI值：" + quotaMonitor.getKpiValue()
		+"比较符号："+ kpiItem.getCompareMethod() +",门限值："+ kpiItem.getThreshold());
		logger.info(quotaMonitor.getPeriodStartTime().toString());
		logger.info(quotaMonitor.getDhssName());
		logger.info(quotaMonitor.getNeName());
		logger.info(quotaMonitor.getUnitName());
		logger.info(quotaMonitor.getNeType());
		logger.info(quotaMonitor.getKpiCode());
		logger.info(kpiItem.getCompareMethod());
		logger.info(quotaMonitor.getUnitType());
		logger.info(kpiItem.getThresholdCancel().toString());
		AlarmMonitor alarm = new AlarmMonitor();
		alarm.setAlarmContent(quotaMonitor.getKpiName() + "触发了告警！KPI值：" + quotaMonitor.getKpiValue()
				+"比较符号："+ kpiItem.getCompareMethod() +",门限值："+ kpiItem.getThreshold());
		alarm.setAlarmTitle("KPI告警");
		alarm.setAlarmType("指标监控结果");
		alarm.setStartTime(standardTimeFormat.format(quotaMonitor.getPeriodStartTime()));
		alarm.setBelongSite(quotaMonitor.getDhssName());
		alarm.setNeName(quotaMonitor.getNeName());
		alarm.setUnitName(quotaMonitor.getUnitName());
		alarm.setNeType(quotaMonitor.getNeType());
		alarm.setKpiCode(quotaMonitor.getKpiCode());
		alarm.setKpiComparedMethod(kpiItem.getCompareMethod());
		alarm.setUnitType(quotaMonitor.getUnitType());
		alarm.setAlarmLevel("*");
		alarm.setAlarmScene(quotaMonitor.getScene());
		alarm.setAlarmLimit(kpiItem.getThresholdCancel().toString());
		alarmMonitorRepository.save(alarm);
		logger.info("------------------kpi alarm data input completed------------------");
	}
	/**
	 * 取消激活的KPI告警方法
	 */
	private void cancelKpiAlarm() {
		String sql = "SELECT * FROM alarm_monitor WHERE cancel_time IS NULL AND alarm_type = 'KPI'";
		List<AlarmMonitor> kpiAlarmList = jdbcTemplate.query(sql, new Object[] {},
				new BeanPropertyRowMapper<AlarmMonitor>(AlarmMonitor.class));
		String sqlK = "SELECT * FROM quota_monitor ";
		List<QuotaMonitor> currentPeriodKpiList = jdbcTemplate.query(sqlK, new Object[] {},
				new BeanPropertyRowMapper<QuotaMonitor>(QuotaMonitor.class));
		for (AlarmMonitor kpiAlarm : kpiAlarmList) {
			//try to cancel alarm
			String unitName = kpiAlarm.getUnitName();
			String kpiCode = kpiAlarm.getKpiCode();
			for (QuotaMonitor currentKpi : currentPeriodKpiList) {
				if(currentKpi.getUnitName().equals(unitName)&&currentKpi.getKpiCode().equals(kpiCode)){
					//match this unit's new period kpi
					if(kpiAlarm.getKpiComparedMethod().equals("<")){
						if(currentKpi.getKpiValue()>Double.valueOf(kpiAlarm.getAlarmLimit())){
							kpiAlarm.setCancelTime(currentKpi.getPeriodStartTime().toString());
							alarmMonitorRepository.save(kpiAlarm);
							logger.info("A kpi alarm has been cancelled.");
							logger.info("Unit:"+kpiAlarm.getUnitName()+",kpi code:"+kpiAlarm.getKpiCode()+",kpi value:"+currentKpi.getKpiValue()+",compared method:"+kpiAlarm.getKpiComparedMethod()+",cancel threshold:"+kpiAlarm.getAlarmLimit());
						}
					}
					if(kpiAlarm.getKpiComparedMethod().equals(">")){
						if(currentKpi.getKpiValue()<Double.valueOf(kpiAlarm.getAlarmLimit())){
							kpiAlarm.setCancelTime(currentKpi.getPeriodStartTime().toString());
							alarmMonitorRepository.save(kpiAlarm);
							logger.info("A kpi alarm has been cancelled.");
							logger.info("Unit:"+kpiAlarm.getUnitName()+",kpi code:"+kpiAlarm.getKpiCode()+",kpi value:"+currentKpi.getKpiValue()+",compared method:"+kpiAlarm.getKpiComparedMethod()+",cancel threshold:"+kpiAlarm.getAlarmLimit());
						}
					}
				}
			}
		}
	}

	/**
	 * 获取网管数据的方法
	 */
	private void getOSSData() {
		logger.info("OSS task Begin");
		List<KpiItem> kpiItemList = new ArrayList<KpiItem>();
		String queryKpiItemList = "select * from kpi_item  ";
		kpiItemList = jdbcTemplate.query(queryKpiItemList, new Object[] {},
				new BeanPropertyRowMapper<KpiItem>(KpiItem.class));
		jdbcTemplate.update("delete from quota_monitor");
		for (KpiItem kpiItem : kpiItemList) {
			try {
				String kpiItemQueryScriptSQL = kpiItem.getKpiQueryScript().replaceAll("#period_duration_long#", "30/24/60")
						.replaceAll("#period_duration_short#", "15/24/60");
//				String kpiItemQueryScriptSQL = kpiItem.getKpiQueryScript().replaceAll("#period_duration_long#", "2/24")
//						.replaceAll("#period_duration_short#", "1/24");
				logger.info("real original sql:"+kpiItemQueryScriptSQL);
				List<Map<String, Object>> kpiValueList = jdbcTemplateOss.queryForList(kpiItemQueryScriptSQL);
				logger.info(kpiItem.getKpiName() + " : " + kpiValueList.size());
				saveKpiList(kpiItem, kpiValueList);
				logger.info(kpiItem.getKpiName() + " : end");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.info("OSS task End");
	}
	/**
	 * 数据入库方法
	 * @param kpiItem
	 * @param kpiValueList
	 */
	private void saveKpiList(KpiItem kpiItem, List<Map<String, Object>> kpiValueList) {
		String insertIntoQuotaMonitorHistory = CustomSettings.getInsertQuotaHistoryTable();
		String insertIntoQuotaMonitor = CustomSettings.getInsertQuotaCurrentTable();
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (Map<String, Object> map : kpiValueList) {
			logger.info(map.toString());
			String neIdString = map.getOrDefault("NE_ID", -1).toString();
			String unitIdString = map.getOrDefault("UNIT_ID", -1).toString();
			EquipmentNe equipmentNe = equipmentNeMap.getOrDefault(neIdString, new EquipmentNe());
			EquipmentUnit equipmentUnit = equipmentUnitMap.getOrDefault(unitIdString, new EquipmentUnit());
			Map<String, BigDecimal> kpiSuccessAndRequest = new HashMap<String, BigDecimal>();
			kpiSuccessAndRequest.put("KPI_SUCCESS", new BigDecimal(map.getOrDefault("KPI_SUCCESS", 0d).toString()));
			kpiSuccessAndRequest.put("KPI_REQUEST", new BigDecimal(map.getOrDefault("KPI_REQUEST", 0d).toString()));
			kpiSuccessAndRequest.put("KPI_FAIL", new BigDecimal(map.getOrDefault("KPI_FAIL", 0d).toString()));
			kpiSuccessAndRequest.put("KPI_VALUE", new BigDecimal(map.getOrDefault("KPI_VALUE", 0d).toString()));
			Double kpi_value = calculateKpiValue(kpiSuccessAndRequest, kpiItem.getOutPutField());// kpi_value
			String kpi_code = kpiItem.getKpiCode();
			String kpi_name = kpiItem.getKpiName();
			Object[] insertParam = new Object[] { 
					false, // flag
					kpi_code, // kpi_code
					kpi_name, // kpi_name
					map.getOrDefault("KPI_SUCCESS", 0), // kpi_success_count
					map.getOrDefault("KPI_FAIL", 0), // kpi_fail_count
					map.getOrDefault("KPI_REQUEST", 0), // kpi_request_count
					map.getOrDefault("PERIOD_START_TIME", null), 
					neIdString, // ne_id
					equipmentNe.getNeName(), // ne_name
					unitIdString, // unit_id
					equipmentUnit.getUnitName(), // unit_name
					equipmentUnit.getUnitType()!=null?equipmentUnit.getUnitType().toString():"emptyUnitType", // unit_type
					equipmentNe.getLocation(), // node_name
					equipmentNe.getDhssName(), // dhss_name
					getKpiUnit(kpiItem.getOutPutField()), // kpi_unit
					kpi_value, // kpi_value
					equipmentUnit.getNeType() != null ? equipmentUnit.getNeType().toString() : "emptyNeType", // ne_type
					kpiItem.getKpiCategory(), // scene
					map.getOrDefault("UNIT_NEXT", 0), // unit_next
					map.getOrDefault("UNIT_NEXT_ID", 0)// unit_next_id
			};
				batchArgs.add(insertParam);
		}
		try {
			//logger.info("try to insert kpi info data");
			jdbcTemplate.batchUpdate(insertIntoQuotaMonitor, batchArgs);
			jdbcTemplate.batchUpdate(insertIntoQuotaMonitorHistory, batchArgs);
			//logger.info("insert kpi info end");
		} catch (Exception e) {
			logger.error("insert params error!");
			logger.error(batchArgs.toString());
		}
		}

	/**
	 * KPI计算方法
	 * @param kpiSuccessAndRequest KPI中成功（分子）和请求次数（分母）数据集合
	 * @param outPutField KPI计算方法
	 * @return
	 */
	private Double calculateKpiValue(Map<String, BigDecimal> kpiSuccessAndRequest, String outPutField) {
		Double kpiValue = 0d;
		BigDecimal kpiRequest = kpiSuccessAndRequest.get("KPI_REQUEST");
		BigDecimal kpiSuccess = kpiSuccessAndRequest.get("KPI_SUCCESS");
		DecimalFormat df = new DecimalFormat("######0.0000");
		Double kpiFail = kpiRequest.doubleValue() - kpiSuccess.doubleValue();
		if (outPutField.equalsIgnoreCase("success_rate") && kpiRequest.intValue() != 0) {
			kpiValue = (kpiSuccess.doubleValue() / kpiRequest.doubleValue()) * 100.0;
			kpiValue = Double.parseDouble(df.format(kpiValue));
			return kpiValue;
		}
		if (outPutField.equalsIgnoreCase("success_count")) {
			kpiValue = kpiSuccess.doubleValue();
			return kpiValue;

		}
		if (outPutField.equals("fail_rate") && kpiRequest.intValue() != 0) {
			kpiValue = (1 - kpiSuccess.doubleValue() / kpiRequest.doubleValue()) * 100.0;
			kpiValue = Double.parseDouble(df.format(kpiValue));
			return kpiValue;
		}
		if (outPutField.equalsIgnoreCase("fail_count")) {
			kpiValue = kpiFail;
			return kpiValue;

		}
		if (outPutField.equalsIgnoreCase("total_count")) {
			kpiValue = kpiRequest.doubleValue();
			return kpiValue;

		}
		if (outPutField.equalsIgnoreCase("load_rate")) {
			kpiValue = kpiSuccessAndRequest.get("KPI_VALUE").doubleValue();
			return kpiValue;
		}
		return kpiValue;
	}

	private String getKpiUnit(String outPutField) {
		if (outPutField.endsWith("_rate")) {
			return "%";
		}
		if (outPutField.endsWith("_count")) {
			return "次数";
		} else
			return null;
	}
	/**
	 * 获取单元、网元信息
	 */
	private void getEquipmentInfo() {
		logger.info("=============== ne info Start ===============");
		List<EquipmentNe> neInfoList = jdbcTemplate.query("select * from equipment_ne", new Object[] {},
				new BeanPropertyRowMapper<EquipmentNe>(EquipmentNe.class));
		for (EquipmentNe equipmentNe : neInfoList) {
			equipmentNeMap.put(equipmentNe.getNeCode(), equipmentNe);
		}
		logger.info("=============== ne info end ===============");

		logger.info("=============== unit info Start ===============");

		List<EquipmentUnit> unitInfoList = jdbcTemplate.query("select * from equipment_unit", new Object[] {},
				new BeanPropertyRowMapper<EquipmentUnit>(EquipmentUnit.class));

		for (EquipmentUnit equipmentUnit : unitInfoList) {
			equipmentUnitMap.put(equipmentUnit.getNeCode(), equipmentUnit);
		}
		logger.info("=============== unit info end ===============");
		/*
		 * logger.info("===============开始从配置数据库取 unit info ===============");
		 * List<EquipmentUnit> list_unitInfo = jdbcTemplate.query(
		 * "select * from equipment_unit", new Object[]{},new
		 * BeanPropertyRowMapper<EquipmentUnit>(EquipmentUnit.class) ); for
		 * (EquipmentUnit equipmentUnit : list_unitInfo) {
		 * equipmentUnitMap.put(equipmentUnit.getNeCode(), equipmentUnit); }
		 * logger.info("===============结束从配置数据库取 unit info ===============");
		 * 
		 * 
		 * logger.info("===============开始从网管数据库取 dn ===============");
		 * List<Map<String, Object>> list_dn = jdbcTemplateOss.queryForList(
		 * "select distinct co_gid,co_oc_id,co_dn from nasda_objects ");
		 * dnInfoMap = list_dn.get(0); for (Map<String, Object> map : list_dn) {
		 * logger.info(map.toString()); } logger.info(
		 * "===============结束从网管数据库取 dn ===============");
		 */
	}
}
