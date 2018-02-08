package com.nokia.boss.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nokia.boss.settings.CustomSettings;

@Component
@EnableScheduling
public class DataMaintainer {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataMaintainer.class);
	
	private static Integer SAVE_DAYS = Integer.valueOf(CustomSettings.getSaveDays());
	private static SimpleDateFormat SDF_LONG = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	private static SimpleDateFormat SDF_SHORT = new SimpleDateFormat("yyyyMMdd");
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	/**
	 * 处理分区，晚上8点执行
	 */
	@Scheduled(cron = "${dhss.boss.data-maintainer.handle-partitions-cron}")
	public void handlePartition() {
		//1.创建明天的分区：
		//例如：此方法执行时间是2017-02-15 08:00:00（response_time时间格式）
		//那么，今日数据应该保存于分区p_20170215,值 LESS THAN (to_days('2017-02-16 00:00:00'))
		//则明天的分区应该叫做p_20170216,值 LESS THAN (to_days('2017-02-17 00:00:00'))
		String bossVersion = CustomSettings.getBossVersion();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
		Date tomorrow = calendar.getTime();
		String tomorrowPartitionName = "p_"+SDF_SHORT.format(tomorrow);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
		Date dayAfterTomorrow = calendar.getTime();
		String dayAfterTomorrowStartStr = SDF_LONG.format(dayAfterTomorrow);
		calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - SAVE_DAYS);
		Date beforeSaveDays = calendar.getTime();
		String beforeSaveDaysPartitionName = "p_"+SDF_SHORT.format(beforeSaveDays);
		
		if (bossVersion.equalsIgnoreCase("chinamobile")) {
				String sqlAddSoapPartition = "ALTER TABLE boss_soap add PARTITION (PARTITION " + tomorrowPartitionName
						+ " VALUES LESS THAN" + " (TO_DAYS('" + dayAfterTomorrowStartStr + "')))";
				String sqlDropSoapPartitionBeforeSaveDays = "ALTER TABLE boss_soap drop PARTITION "
						+ beforeSaveDaysPartitionName;
				String sqlAddErrPartition = "ALTER TABLE boss_err_case add PARTITION (PARTITION " + tomorrowPartitionName
						+ " VALUES LESS THAN" + " (TO_DAYS('" + dayAfterTomorrowStartStr + "')))";
				String sqlDropErrPartitionBeforeSaveDays = "ALTER TABLE boss_err_case drop PARTITION "
						+ beforeSaveDaysPartitionName;
				LOGGER.info("ADD PARTITION:");
				try {
					LOGGER.info(sqlAddSoapPartition);
					jdbcTemplate.execute(sqlAddSoapPartition);
					
				} catch (Exception e) {
					LOGGER.info(e.getMessage());
				}
				
				try {
					LOGGER.info(sqlAddErrPartition);
					jdbcTemplate.execute(sqlAddErrPartition);
				} catch (Exception e) {
					LOGGER.info(e.getMessage());
				}
				
				
				LOGGER.info("DROP PARTITION:");
				try {
					LOGGER.info(sqlDropSoapPartitionBeforeSaveDays);
					jdbcTemplate.execute(sqlDropSoapPartitionBeforeSaveDays);
					
				} catch (UncategorizedSQLException e) {
					LOGGER.info("Target soap partition may not exist");
				}
				try {
					LOGGER.info(sqlDropErrPartitionBeforeSaveDays);
					jdbcTemplate.execute(sqlDropErrPartitionBeforeSaveDays);	
				} catch (Exception e) {
					LOGGER.info("Target err partition may not exist");
				}
		}else if(bossVersion.equalsIgnoreCase("unicom")){
			String sqlAddSoapPartition = "ALTER TABLE boss_soap_cuc add PARTITION (PARTITION " + tomorrowPartitionName
					+ " VALUES LESS THAN" + " (TO_DAYS('" + dayAfterTomorrowStartStr + "')))";
			String sqlDropSoapPartitionBeforeSaveDays = "ALTER TABLE boss_soap_cuc drop PARTITION "
					+ beforeSaveDaysPartitionName;
			String sqlAddErrPartition = "ALTER TABLE boss_err_case_cuc add PARTITION (PARTITION " + tomorrowPartitionName
					+ " VALUES LESS THAN" + " (TO_DAYS('" + dayAfterTomorrowStartStr + "')))";
			String sqlDropErrPartitionBeforeSaveDays = "ALTER TABLE boss_err_case_cuc drop PARTITION "
					+ beforeSaveDaysPartitionName;
			LOGGER.info("ADD PARTITION:");
			LOGGER.info(sqlAddSoapPartition);
			jdbcTemplate.execute(sqlAddSoapPartition);
			LOGGER.info(sqlAddErrPartition);
			jdbcTemplate.execute(sqlAddErrPartition);
			LOGGER.info("DROP PARTITION:");
			try {
				LOGGER.info(sqlDropSoapPartitionBeforeSaveDays);
				jdbcTemplate.execute(sqlDropSoapPartitionBeforeSaveDays);
				
			} catch (UncategorizedSQLException e) {
				LOGGER.info("Target soap partition may not exist");
			}
			try {
				LOGGER.info(sqlDropErrPartitionBeforeSaveDays);
				jdbcTemplate.execute(sqlDropErrPartitionBeforeSaveDays);	
			} catch (Exception e) {
				LOGGER.info("Target err partition may not exist");
			}
	
		}
		//handle boss_join partition
		String sqlAddJoinTablePartition = "ALTER TABLE boss_join add PARTITION (PARTITION " + tomorrowPartitionName
				+ " VALUES LESS THAN" + " (TO_DAYS('" + dayAfterTomorrowStartStr + "')))";
		String sqlDropJoinPartitionBeforeSaveDays = "ALTER TABLE boss_join drop PARTITION "
				+ beforeSaveDaysPartitionName;
		try {
			jdbcTemplate.execute(sqlAddJoinTablePartition);
			jdbcTemplate.execute(sqlDropJoinPartitionBeforeSaveDays);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
