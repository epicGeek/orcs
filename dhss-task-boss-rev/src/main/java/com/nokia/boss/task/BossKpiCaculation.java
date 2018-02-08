package com.nokia.boss.task;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nokia.boss.util.DateUtils;

@Component
@EnableScheduling
/**
 * @See 建议BOSS业务监控功能里所有的时间格式均为"yyyy-MM-dd HH:mm:ss"
 * 所有跟时间段相关的时间数据字段，都为 "period_start_time"
 * 例如 boss_monitor_minute表,某条记录period_start_time的值为"2016-01-01 00:00:00"表示这条数据属于"2016-01-01 00:00:00"->"2016-01-01 00:15:00"内
 * @author Pei Nan
 *
 */
public class BossKpiCaculation {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	private static Logger LOGGER = LoggerFactory.getLogger(BossKpiCaculation.class);
	
	/**
	 *  每个月1号上午7点20执行，得出上个月的KPI
	 */
	@Scheduled(cron="${dhss.boss.kpi-calculator.month-kpi-cron}")
	private void calculateMonthKPI() {
		// TODO COMPLETE METHOD
		String lastMonth = dateLogger("LAST MONTH");
		String sql = LoadStaticData.getBossMonthKpiSQL().replace("#month-start-time#", lastMonth);
		LOGGER.info(sql);
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		List<Map<String,Object>> kpiList = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> kpiMap : kpiList) {
			String businessType = LoadStaticData.getBusinessType(String.valueOf(kpiMap.get("operation_name")));
			kpiMap.put("business_type", businessType);
		}
		String insertSQL = "insert into boss_monitor_month"
				+ "(hlrsn,period_start_month,operation_name,fail_count,total_count,business_type)"
				+ "values"
				+ "(:hlrsn,:period_start_month,:operation_name,:fail_count,:total_count,:business_type)";
		for (Map<String, Object> map : kpiList) {
			namedParameterJdbcTemplate.update(insertSQL, map);
		}
		LOGGER.info("Last month's KPI calculated.");
	}
	
	/**
	 * 每天0点10分执行，计算昨天的KPI
	 */
	@Scheduled(cron="${dhss.boss.kpi-calculator.yesterday-kpi-cron}") 
	private void calculateDayKPI() {
		// TODO COMPLETE METHOD
		String yesterday = dateLogger("YESTERDAY");
		String sql = LoadStaticData.getBossDayKpiSQL().replace("#yesterday-oclock#", yesterday);
		LOGGER.info(sql);
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		List<Map<String,Object>> kpiList = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> kpiMap : kpiList) {
			String businessType = LoadStaticData.getBusinessType(String.valueOf(kpiMap.get("operation_name")));
			kpiMap.put("business_type", businessType);
		}
		String insertSQL = "insert into boss_monitor_day"
				+ "(hlrsn,period_start_day,operation_name,fail_count,total_count,business_type)"
				+ "values"
				+ "(:hlrsn,:period_start_day,:operation_name,:fail_count,:total_count,:business_type)";
		for (Map<String, Object> map : kpiList) {
			namedParameterJdbcTemplate.update(insertSQL, map);
		}
		LOGGER.info("Yesterday's KPI calculated.");
	}
	
	/**
	 * 每小时整点零五分执行，计算上一个小时的KPI
	 */
	@Scheduled(cron="${dhss.boss.kpi-calculator.last-hour-kpi-cron}") 
	private void calculateHourKPI() {
		String lastHour = dateLogger("LAST HOUR");//o'clock
		String sql = LoadStaticData.getBossHourKpiSQL().replace("#last_hour_oclock#", lastHour);
		LOGGER.info(sql);
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		List<Map<String,Object>> kpiList = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> kpiMap : kpiList) {
			String businessType = LoadStaticData.getBusinessType(String.valueOf(kpiMap.get("operation_name")));
			kpiMap.put("business_type", businessType);
		}
		String insertSQL = "insert into boss_monitor_hour"
				+ "(hlrsn,period_start_hour,operation_name,fail_count,total_count,business_type)"
				+ "values"
				+ "(:hlrsn,:period_start_hour,:operation_name,:fail_count,:total_count,:business_type)";
		for (Map<String, Object> map : kpiList) {
			namedParameterJdbcTemplate.update(insertSQL, map);
		}
		LOGGER.info("Last hour's KPI calculated.");
	}

	private String dateLogger(String dateStr){
		Map<String,String> dateMap = DateUtils.getTargetTime();
		LOGGER.info("Start to calculate BOSS KPI for "+dateStr);
		String formattedDateStr = dateMap.get(dateStr);
		LOGGER.info(dateStr+":"+formattedDateStr);
		return formattedDateStr;
	}
}
