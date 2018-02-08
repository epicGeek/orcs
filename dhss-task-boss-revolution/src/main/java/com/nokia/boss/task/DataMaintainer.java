package com.nokia.boss.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nokia.boss.settings.CustomSettings;
import com.nokia.boss.util.DateUtils;

@Component
@EnableScheduling
public class DataMaintainer {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataMaintainer.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	private static Integer SAVE_DAYS = Integer.valueOf(CustomSettings.getSaveDays());
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat SDF_LONG = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	private static SimpleDateFormat SDF_SHORT = new SimpleDateFormat("yyyyMMdd");
	private static String BOSS_VERSION = CustomSettings.getBossVersion();
	@Scheduled(cron = "${dhss.boss.data-maintainer.reset-all-mark}")
	private void resetAllMark(){
		LOGGER.info("Start to reset all marks.");
		String sqlResetSoapMark = "delete from soap_mark"; 
		String sqlResetErrMark = "delete from err_mark"; 
		LOGGER.info("Start to delete soap marks:");
		LOGGER.info(sqlResetSoapMark);
		jdbcTemplate.execute(sqlResetSoapMark);
		LOGGER.info("Done.");
		LOGGER.info("Start to delete err marks:");
		LOGGER.info(sqlResetErrMark);
		jdbcTemplate.execute(sqlResetErrMark);
		LOGGER.info("Done.");
	}
	/**
	 * 每天23点50执行，更改BOSS名称匹配规范文件，实现第二天的精确同步。
	 * Execute at 23:50 everyday.
	 * Changes the BOSS name matches pattern,and makes yesterday's BOSS file name matching synchronize accurately.
	 * @param bossVersion
	 * @throws IOException 
	 */
	@Scheduled(cron = "${dhss.boss.data-maintainer.accurate-sync-cron}") 
	private void getMatchRule() throws IOException{
		String ruleFileDir = CustomSettings.getRuleFileAbsPath();
		File ruleFile = new File(ruleFileDir);
		if(BOSS_VERSION.equalsIgnoreCase("chinamobile")){
			/*****************完成精确同步文件的规则******************/
			/*****************COMPLETE ACCURATE SYNCHRONIZE PATTERN FOR TOMORROW******************/
			String rulePattern = LoadStaticData.getRulePatternCM();
			String tomorrowFormat = DateUtils.getTargetTime().get("TOMORROW");
			String todayFormat = DateUtils.getTargetTime().get("TODAY");
			String newRule = rulePattern.replace("yyyy-MM-dd", tomorrowFormat);
			FileWriter fw = new FileWriter(ruleFile);
			fw.write(newRule);
			fw.close();
			LOGGER.info("File Pattern rule update complete!");
			LOGGER.info(newRule);
			/****************顺便删除今天同步的全部文件***************/
			/****************DELETE TODAY'S SYNCHRONIZE FILE***************/
			List<File> rsyncFileDirList = LoadStaticData.getBossDir().get("rsync");// /home/soap-gw/boss_revolution/rsync-data/soap50,/home/soap-gw/boss_revolution/rsync-data/soap51...
			for (File file : rsyncFileDirList) {
				String[] rsyncFiles =  file.list();
				for (String fileName : rsyncFiles) {
					String fileAbsPath = file.getAbsolutePath()+"/"+fileName;
					File targetFile = new File(fileAbsPath);
					if(targetFile.getName().contains(todayFormat)){
						targetFile.delete();
						LOGGER.info("TODAY'S FILE :"+targetFile.getAbsolutePath()+" HAS BEEN DELETED.");
					}
				}
			}
		}else if(BOSS_VERSION.equalsIgnoreCase("unicom")){
			//TODO UNICOM RULE?
		}
	}
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
				LOGGER.info(sqlAddSoapPartition);
				jdbcTemplate.execute(sqlAddSoapPartition);
				LOGGER.info(sqlAddErrPartition);
				jdbcTemplate.execute(sqlAddErrPartition);
				
				LOGGER.info("DROP PARTITION:");
				LOGGER.info(sqlDropSoapPartitionBeforeSaveDays);
				jdbcTemplate.execute(sqlDropSoapPartitionBeforeSaveDays);
				LOGGER.info(sqlDropErrPartitionBeforeSaveDays);
				jdbcTemplate.execute(sqlDropErrPartitionBeforeSaveDays);
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
			LOGGER.info(sqlDropSoapPartitionBeforeSaveDays);
			jdbcTemplate.execute(sqlDropSoapPartitionBeforeSaveDays);
			LOGGER.info(sqlDropErrPartitionBeforeSaveDays);
			jdbcTemplate.execute(sqlDropErrPartitionBeforeSaveDays);
	
		}
		
	}
	/**
	 * 删掉SAVE-DAYS前的IGNORE文件记录和旧的LOADER文件。晚上9:05点执行
	 */
	@Scheduled(cron = "${dhss.boss.data-maintainer.delete-old-ignore-record-and-loader-file}")
	private void deleteOldIgnoreRecordAndLoaderFile(){
	    /************************DELETE OLD IGNORE RECORD***************************/
		String sql = "delete from ignore_file where analysed_time like ";
		GregorianCalendar gc = new GregorianCalendar();
	    gc.set(Calendar.DATE, gc.get(Calendar.DATE)-SAVE_DAYS);
	    Date d = gc.getTime();
	    String timeCondition = "%"+SDF.format(d)+"%";
	    LOGGER.info("DELETE OLD IGNORE FILE RECORD...");
	    jdbcTemplate.update(sql+timeCondition);
	    LOGGER.info("DELETE COMPLETE");
	    /************************DELETE OLD LOADER FILE*********************/
	    String loaderDir = CustomSettings.getLoadFileDir().replace("#soap-gw-name#/", "");//  /home/soap-gw/boss_revolution/loader/#soap-gw-name#/->/home/soap-gw/boss_revolution/loader/
	    File loaderDirFile = new File(loaderDir);
	    String[] s = loaderDirFile.list();//soapName1,soapName2...
	    for (String soapDir : s) {
			String loaderSingleSoapDir = loaderDir+soapDir+"/";//  /home/soap-gw/boss_revolution/loader/soap1/,/home/soap-gw/boss_revolution/loader/soap2/
			File loaderFile = new File(loaderSingleSoapDir);
			String[] s_ =  loaderFile.list();
			for (String loaderName : s_) {
				String oneLoaderFileDir = loaderSingleSoapDir+loaderName;// /home/soap-gw/boss_revolution/loader/soap1/xxxxxx.loader
				File aLoader = new File(oneLoaderFileDir);
				Date lastModified = new Date(aLoader.lastModified());
				if(lastModified.before(new Date())){
					LOGGER.info("DELETE OLD LOADER:"+aLoader.getAbsolutePath());
					aLoader.delete();
				}
			}
		}
	}

	
}
