package com.nokia.boss.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nokia.boss.service.AnalysedSerivice;
import com.nokia.boss.settings.CustomSettings;
import com.nokia.boss.util.DateUtils;
import com.nokia.boss.util.RangeException;

@Component
@EnableScheduling
public class TaskBoss {

	private static final Logger logger = LoggerFactory.getLogger(TaskBoss.class);
	private static final String[] TABLE_NAME = { "boss_soap", "boss_err_case" };
	@Autowired
	AnalysedSerivice analysedSerivice;
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Value("${dhss.boss.save-days}")
	private Integer saveDay;
	
	@Scheduled(initialDelay = 100, fixedDelay = 1000 * 60 * 2) // local test
	/**
	 * BOSS定时任务启动的入口
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DocumentException
	 */
	private void executeEntry() throws IOException, InterruptedException, DocumentException {
		String bossVersion = CustomSettings.getBossVersion();
		clearTempTableData();//清除临时表数据
		syncDataFiles(bossVersion);//根据BOSS日志版本，同步原始数据，数据解析入库。
		calculateCuurentPeriodKPI();//计算当前周期的成功率
		
		
	}

	private void calculateCuurentPeriodKPI() {
		String SQL = LoadStaticData.getCurrentPeriodKPICalculationSQL();
		List<Map<String,Object>> kpiList = jdbcTemplate.queryForList(SQL);
	}

	private void clearTempTableData() {
		jdbcTemplate.execute("TRUNCATE boss_soap_temp");
		jdbcTemplate.execute("TRUNCATE boss_err_case_temp");
		logger.info("Temp data clear completed!");
	}

	/**
	 * 创建分区,默认每天20点执行
	 */
	@Scheduled(cron = "0 22 10 * * *")
	private void createPartition() {
		logger.info("start createPartition operation; the time:" + new Date());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		long pnow = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
		String pName = dateFormat.format(new Date(pnow));
		long rnow = System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000;
		String range = dateFormat.format(new Date(rnow));

		String[] addRange = new String[TABLE_NAME.length];
		for (int i = 0; i < TABLE_NAME.length; i++) {
			addRange[i] = "ALTER TABLE boss_soap ADD PARTITION (PARTITION p_" + pName + " VALUES LESS THAN (" + range
					+ "000000))";
		}
		try {
			jdbcTemplate.batchUpdate(addRange);

			logger.info("createPartition P_" + pName + " success......");
		} catch (RangeException ex) {
			logger.debug("RangeException:" + ex.getMessage());
		}

		logger.info("end createPartition operation; the time:" + new Date());

	}

	/**
	 * 删除分区，凌晨1点执行
	 */
	@Scheduled(cron = "0 0 1 * * *")
	private void deletePartition() {

		logger.info("start deletePartition operation; the time:" + new Date());

		String[] dleRange = new String[TABLE_NAME.length];
		String pName = DateUtils.getSpecifiedDayAfter("yyyyMMdd", saveDay);
		for (int i = 0; i < TABLE_NAME.length; i++) {
			dleRange[i] = "ALTER TABLE " + TABLE_NAME[i] + " drop partition  P_" + pName;
		}
		try {
			jdbcTemplate.batchUpdate(dleRange);

			logger.info("deletePartition P_" + pName + " success......");
		} catch (RangeException ex) {
			logger.debug("RangeException:" + ex.getMessage());
		}

		logger.info("end deletePartition operation; the time:" + new Date());

	}

	private void syncDataFiles(String bossVersion) throws IOException, InterruptedException, DocumentException {
		logger.info("Start to synchronize boss data");
		String rsyncCmdPattern = CustomSettings.getRsyncCmd().replace("#rsyncDir#", CustomSettings.getRsyncDataDir());
		List<Map<String, String>> soapLoginInfoList = LoadStaticData.getSoapAddressInfoList();//得到SOAPGW登录信息配置
		for (Map<String, String> soapLoginInfoMap : soapLoginInfoList) {
			rsyncCmdPattern = rsyncCmdPattern.replace("#soap-gw-ip#", soapLoginInfoMap.get("soap-gw-ip"));
			rsyncCmdPattern = rsyncCmdPattern.replace("#soap-gw-name#", soapLoginInfoMap.get("soap-gw-name"));
			Process rysncProcess = null;
			logger.info("Start to execute command:" + rsyncCmdPattern);
			rysncProcess = Runtime.getRuntime().exec(rsyncCmdPattern);//下发同步命令
			rysncProcess.waitFor();//等待同步命令结束
			BufferedReader br = new BufferedReader(new InputStreamReader(rysncProcess.getInputStream()));
			List<String> changedFiles = new ArrayList<>();//产生变化的文件
			if (bossVersion.equalsIgnoreCase("chinamobile")) {//根据BOSS版本，选择不同的处理流程
				chinaMobileBossHandlingStream(soapLoginInfoMap, br, changedFiles);
			} else if (bossVersion.equalsIgnoreCase("unicom")) {
				unicomBossHandlingStream(soapLoginInfoMap, br, changedFiles);
			} else if (bossVersion.equalsIgnoreCase("telecom")) {
				telecomBossHandlingStream(soapLoginInfoMap, br, changedFiles);
			}
		}
	}

	@Deprecated
	/**
	 * 电信BOSS业务处理流
	 * @param soapLoginInfoMap
	 * @param br
	 * @param changedFiles
	 */
	private void telecomBossHandlingStream(Map<String, String> soapLoginInfoMap, BufferedReader br,
			List<String> changedFiles) {
	}
	/**
	 * 联通BOSS业务处理流
	 * @param soapLoginInfoMap
	 * @param br
	 * @param changedFiles
	 */
	private void unicomBossHandlingStream(Map<String, String> soapLoginInfoMap, BufferedReader br,
			List<String> changedFiles) {
		// TODO Auto-generated method stub
		logger.info("Boss version:Unicom");
	}
	/**
	 * 移动BOSS业务处理流
	 * @param soapLoginInfoMap
	 * @param br
	 * @param changedFiles
	 */
	public void chinaMobileBossHandlingStream(Map<String, String> soapLoginInfoMap, BufferedReader br,
			List<String> changedFiles) throws IOException, DocumentException {

		logger.info("Boss version:China Moblie");
		String callbackLine = null;
		while ((callbackLine = br.readLine()) != null) {
			if (callbackLine.contains("BOSS_SOAP_Agent_BOSSA_main_") || callbackLine.contains("BOSS_ERR_CASE.log")) {
				if (!callbackLine.contains("deleting")) { //--DELETE 参数存在时，被删除的文件也会存在于命令返回信息里，需要去除
					changedFiles.add(callbackLine);
				}
			}
		}
		br.close();
		logger.info("*****RSYNC analysis target list size:" + changedFiles.size() + "*****");
		for (String changedFile : changedFiles) {
			logger.info(changedFile);//需要解析的增量文件
		}
		logger.info("*****RSYNC analysis target list end.*****");
		for (String changedFile : changedFiles) {
			//一共有四种需要解析的文件，根据文件名字特征走不同的服务
			if (changedFile.contains("BOSS_SOAP_Agent_BOSSA_main_") && !changedFile.endsWith(".gz")) {
				logger.info("This soap log file is changed:" + changedFile);
				analysedSerivice.successDataAnalysisForLog(changedFile, soapLoginInfoMap.get("soap-gw-name"));
			}
			if (changedFile.contains("BOSS_SOAP_Agent_BOSSA_main_") && changedFile.endsWith(".gz")) {
				logger.info("This soap log gz is changed:" + changedFile);
				analysedSerivice.successDataAnalysisForGz(changedFile, soapLoginInfoMap.get("soap-gw-name"));
			}
			if (changedFile.contains("BOSS_ERR_CASE.log") && !changedFile.endsWith(".gz")) {
				logger.info("This err log file is changed:" + changedFile);
				analysedSerivice.failureDataAnalysisForLog(changedFile, soapLoginInfoMap.get("soap-gw-name"));
			}
			if (changedFile.contains("BOSS_ERR_CASE.log") && changedFile.endsWith(".gz")) {
				logger.info("This err gz file is changed:" + changedFile);
				analysedSerivice.failureDataAnalysisForGz(changedFile, soapLoginInfoMap.get("soap-gw-name"));
			}
		}
		
	}
}