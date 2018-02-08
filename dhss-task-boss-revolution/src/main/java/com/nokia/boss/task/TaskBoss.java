package com.nokia.boss.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nokia.boss.service.AnalysedSerivice;
import com.nokia.boss.settings.CustomSettings;
import com.nokia.boss.util.DateUtils;

@Component
@EnableScheduling
/**
 * 
 * @author Pei Nan
 * 这个类的功能是完成数据的实时更新、解析、入库和最新周期的KPI计算。
 *
 */

@ConfigurationProperties(prefix="dhss.boss.task")
public class TaskBoss {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskBoss.class);
	@Autowired
	AnalysedSerivice analysedSerivice;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Value("${dhss.boss.boss-version}")
	private String bossVersion ;
	
	/**
	 * MAIN ENTRY
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DocumentException
	 */
	@Scheduled(cron = "${dhss.boss.main-program-cron}")
	private void executeEntry() throws IOException, InterruptedException, DocumentException, ParseException {
		LOGGER.info("*********************");
		LOGGER.info(bossVersion);
		List<Map<String, String>> soapLoginInfoList = LoadStaticData.getSoapAddressInfoList();//得到SOAPGW登录信息配置
		LOGGER.info("*********************");
		LOGGER.info("ALL SOAP LOGIN INFO:");
		LOGGER.info(soapLoginInfoList.toString());
		clearTempTableData(bossVersion);
		syncDataFiles(bossVersion);
		try {
			calculateCurrentPeriodKPI(bossVersion);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	/**
	 * 临时表 boss_soap_temp，boss_err_case_temp只保存最新一周期的数据。新的周期开始时，应该先截断这两张表。
	 * Temporary table boss_soap_temp and boss_err_case_temp only save latest period data.
	 * When the new period starts,they should be TRUNCATED first. 
	 */
	private void clearTempTableData(String bossVersion) {
		if(bossVersion.equalsIgnoreCase("chinamobile")){
			jdbcTemplate.execute("TRUNCATE boss_soap_temp");
			jdbcTemplate.execute("TRUNCATE boss_err_case_temp");
		}else if(bossVersion.equalsIgnoreCase("unicom")){
			jdbcTemplate.execute("TRUNCATE boss_soap_cuc_temp");
			jdbcTemplate.execute("TRUNCATE boss_err_case_cuc_temp");
		}
		LOGGER.info("Temp data clear completed!");
	}
	/**
	 * 利用临时表里的最新一周期的BOSS数据，计算当前周期的BOSS KPI，并入库到表 boss_monitor_minute.
	 * Calculate current period BOSS KPI using table boss_soap_temp and boss_err_case_temp.
	 * Insert the result into table boss_monitor_minute.
	 */
	private void calculateCurrentPeriodKPI(String bossVersion) {
		String SQL = "";
		if (bossVersion.equalsIgnoreCase("chinamobile")) {
			SQL = LoadStaticData.getCurrentPeriodKPICalculationSQL();

		} else if (bossVersion.equalsIgnoreCase("unicom")) {
			SQL = LoadStaticData.getCurrentPeriodKPICalculationSQL_CUC();
		} else {
			LOGGER.info("UNKNOWN BOSS VERSION:" + bossVersion);
			LOGGER.info("KPI will not be calculated.");
			return;
		}
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		List<Map<String, Object>> kpiList = jdbcTemplate.queryForList(SQL);
		for (Map<String, Object> kpiMap : kpiList) {
			String businessType = LoadStaticData.getBusinessType(String.valueOf(kpiMap.get("operation_name")));
			kpiMap.put("business_type", businessType);
		}
		String insertSQL = "insert into boss_monitor_minute"
				+ "(hlrsn,period_start_time,operation_name,fail_count,total_count,business_type)" + "values"
				+ "(:hlrsn,:period_start_time,:operation_name,:fail_count,:total_count,:business_type)";
		for (Map<String, Object> map : kpiList) {
			namedParameterJdbcTemplate.update(insertSQL, map);
		}
		LOGGER.info("This period BOSS KPI calculation has been done.");
	}
	
	/**
	 * 根据给出的BOSS版本，完成同步数据和解析入库的功能。
	 * According to BOSS version,synchronize data,analysis data and load result to DB.
	 * @param bossVersion
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws DocumentException
	 * @throws ParseException 
	 */
	private void syncDataFiles(String bossVersion) throws IOException, InterruptedException, DocumentException, ParseException {
		try {
			checkPatternFile(bossVersion);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Map<String, String>> soapLoginInfoList = LoadStaticData.getSoapAddressInfoList();//得到SOAPGW登录信息配置
		LOGGER.info("ALL SOAP-GW LOGIN INFO:");
		LOGGER.info(soapLoginInfoList.toString());
		for (Map<String, String> soapLoginInfoMap : soapLoginInfoList) {
			String rsyncCmdPattern = CustomSettings.getRsyncCmd().replace("#rsyncDir#", CustomSettings.getRsyncDataDir());
			rsyncCmdPattern = rsyncCmdPattern.replace("#pattern-rule#", CustomSettings.getRuleFileAbsPath());
			LOGGER.info("RSYNC INFO ----> ");
			LOGGER.info("SOAP-GW IP:"+soapLoginInfoMap.get("soap-gw-ip"));
			LOGGER.info("SOAP-GW NAME:"+soapLoginInfoMap.get("soap-gw-name"));
			LOGGER.info("Checking mark...");
			try {
				checkMarkRecord(soapLoginInfoMap.get("soap-gw-name"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			rsyncCmdPattern = rsyncCmdPattern.replace("#soap-gw-ip#", soapLoginInfoMap.get("soap-gw-ip"));
			rsyncCmdPattern = rsyncCmdPattern.replace("#soap-gw-name#", soapLoginInfoMap.get("soap-gw-name"));
			LOGGER.info("Start to execute command:");
			LOGGER.info(rsyncCmdPattern);
			try {
				Process rysncProcess  = Runtime.getRuntime().exec(rsyncCmdPattern);//下发同步命令
				rysncProcess.waitFor();//等待同步命令结束
				InputStreamReader in = new InputStreamReader(rysncProcess.getInputStream());
				BufferedReader br = new BufferedReader(in);
				rsyncCallBackErrorInfo(rysncProcess);
				bossHandlingStream(bossVersion, soapLoginInfoMap, br);
				LOGGER.info("BOSS data analysis complete!");
				rysncProcess.destroy();//解析后销毁，防止资源占用
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 每次同步前会检查规范文件，是否是同步今日的数据。
	 * @param bossVersion
	 * @throws IOException
	 */
	private void checkPatternFile(String bossVersion) throws IOException {
		//每天的0点暂时不改。因为数据过少，而且昨天的数据可能没解析完毕。
		SimpleDateFormat sdfHH = new SimpleDateFormat("HH"); 
		String hour = sdfHH.format(new Date());
		if(Integer.valueOf(hour)==0){
			LOGGER.info("It's 0 hour now.No need to changePattern");
			return;
		}else{
			File patternFile = new File(CustomSettings.getRuleFileAbsPath());
			FileReader fr = new FileReader(patternFile);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			StringBuilder patternText = new StringBuilder();
			while ((line = br.readLine())!=null) {
				patternText.append(line+"\n");
			}
			br.close();
			fr.close();
			String patternStr = patternText.toString().trim();
			if(patternStr.contains("#static")){
				LOGGER.info("Pattern file is set to static mode.");
				System.out.println(patternStr);
				return;
			}
			String todayStr = DateUtils.getTargetTime().get("TODAY");
			String todayPattern = "";
			if(bossVersion.equalsIgnoreCase("chinamobile")){
				todayPattern = LoadStaticData.getRulePatternCM().replace("yyyy-MM-dd", todayStr);
			}else if(bossVersion.equalsIgnoreCase("unicom")){
				todayPattern = LoadStaticData.getRULE_PATTERN_CUC().replace("yyyy-MM-dd", todayStr);
			}
			if(!patternStr.equals(todayPattern)){
				FileWriter fw = new FileWriter(patternFile);
				fw.write(todayPattern);
				fw.close();
				FileReader fr_ = new FileReader(patternFile);
				BufferedReader br_ = new BufferedReader(fr_);
				String line_ = null;
				LOGGER.info("Pattern rule file has been changed as:");
				while ((line_ = br_.readLine())!=null) {
					LOGGER.info(line_);
				}
				br_.close();
			}else{
				LOGGER.info("Pattern rule is correct.");
			}
		}
	}
	/**
	 * 输出命令错误回执信息
	 * @param rysncProcess
	 * @throws IOException
	 */
	private void rsyncCallBackErrorInfo(Process rysncProcess) throws IOException {
		InputStreamReader inE = new InputStreamReader(rysncProcess.getErrorStream());
		BufferedReader br_ = new BufferedReader(inE);
		String line_ = null;
		LOGGER.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		LOGGER.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		LOGGER.info("call back ERR info :"); 
		while((line_ = br_.readLine())!=null){  
			LOGGER.info(line_); 
		}
		br_.close();
		LOGGER.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		LOGGER.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}

	/**
	 * 筛选出没有被Ignore的文件
	 * @param changedFiles
	 * @param soapName
	 * @return
	 */
	private List<File> selectNotIgnoreTargetFile(List<String> changedFiles, String soapName) {
		List<File> analysedTarget = new ArrayList<>();
		LOGGER.info("***** REMOTE SYNC FILE LIST SIZE:"+changedFiles.size()+" *****");
		for (String changedFile : changedFiles) {
			String pureName = changedFile.replace("/backup/", "");
			LOGGER.info("CHANGED FILE:"+changedFile+",PURE NAME:"+pureName);//被改变的文件
			//ignore表里存的文件名都是纯文件名，不含路径
			String sql = "select * from ignore_file where file_name = ? and soap_name = ?";
			List<Map<String,Object>> resultList = jdbcTemplate.queryForList(sql,pureName,soapName);
			if(resultList.size()==0){
				//如果查不到，说明没添加到IGNORE列表
				File fileNeedToAnalysis = new File(CustomSettings.getRsyncDataDir().replace("#soap-gw-name#", soapName)+changedFile);
				analysedTarget.add(fileNeedToAnalysis);
			}else{
				LOGGER.info("File name:"+pureName+" in ingore_file size:"+resultList.size());
				LOGGER.info(changedFile+" IS IGNORED.");
			}
		}
		for (File changedFile : analysedTarget) {
			LOGGER.info("File should be analysed:"+changedFile.getAbsolutePath());
		}
		return analysedTarget;
	}
	/**
	 * 移动BOSS业务处理流
	 * @param soapLoginInfoMap
	 * @param br
	 * @param changedFiles
	 */
	//同步命令下发后，只能取当天的日志信息。
	/**
	 * 移动文件变化规则     左边是持续写入文件，右面是打包文件：
	 * 指令日志：BOSS_SOAP_Agent_BOSSA_main_yyyy-MM-dd.HH.mm.ss-SSS -> BOSS_SOAP_Agent_BOSSA_main_yyyy-MM-dd.HH.mm.ss-SSS.gz（写满到一定大小打包）
	 * 日期是创建文件的时间。
	 * 错误日志: BOSS_ERR_CASE.log -> BOSS_ERR_CASE.log.2016-10-01.45.gz(写满到一定大小打包)
	 * 
	 * 每天可能会产生几个写不满的文件，到23:59会改名，但不打包。
	 * @throws ParseException 
	 * 
	 */
	
	private void bossHandlingStream(String bossVersion , Map<String, String> soapLoginInfoMap, BufferedReader br) throws IOException, DocumentException, ParseException {
		List<String> changedFiles = new ArrayList<>();
		String soapName = soapLoginInfoMap.get("soap-gw-name");
		LOGGER.info("Boss version:"+bossVersion+",SOAP-GW NAME:"+soapName);
		String callbackLine = null;
		while ((callbackLine = br.readLine()) != null) {
			
			if (callbackLine.contains("BOSS_SOAP_Agent_BOSSA_main_") || callbackLine.contains("BOSS_ERR_CASE.log")||// CHINA MOBILE
					callbackLine.contains("CUC_Telnet_Agent_BOSSA_main_")||callbackLine.contains("CUC_BOSS_ERR_CASE.log")) { //UNICOM
				if (!callbackLine.contains("deleting")) { //--DELETE 参数存在时，被删除的文件也会存在于命令返回信息里，需要去除
					if(callbackLine.contains("backup")){
						changedFiles.add("/"+callbackLine);//如果包含文件夹名称，前面没有“/” 拼一下
					}else{
						changedFiles.add(callbackLine);
					}
				}
			}
			if (callbackLine.contains("sent")||callbackLine.contains("total")) {
				LOGGER.info("Transport info:->"+callbackLine);
			}
		}
		br.close();
		//现在，changedFiles里面存的是所有发生变动的文件名，可能包括backup路径。
		List<File> analysedTarget = selectNotIgnoreTargetFile(changedFiles, soapName);
		List<File> soapLogTarget = new ArrayList<>();
		List<File> soapLogGZTarget = new ArrayList<>();
		List<File> errLogTarget = new ArrayList<>();
		List<File> errLogGzTarget = new ArrayList<>();
		List<File> errLogLongNameTarget = new ArrayList<>();
		targetFileClassification(analysedTarget, soapLogTarget, soapLogGZTarget, errLogTarget, errLogGzTarget,errLogLongNameTarget);
		//应该根据文件的最后修改日期排序文件的顺序。先解析较旧的日志（GZ包），再解析持续写入的文件
		//由于文件名称的问题，错误日志文件是优先被同步下来的
		checkMarkRecord(soapName);
		Map<String,List<File>> analysisTargetMap = targetFileInfo(soapLogTarget, soapLogGZTarget, errLogTarget, errLogGzTarget,errLogLongNameTarget);
		executeAnalysis(bossVersion, soapLoginInfoMap, analysisTargetMap);
	}
	/**
	 * 首先处理已打包的ERR日志。按照名称顺序，按照GZ前面的数字（或者没有GZ，但是也有数字），从小到大处理。
	 * 再处理已打包的SOAP日志。按名称排序处理。
	 * @param soapLogTarget
	 * @param soapLogGZTarget
	 * @param errLogTarget
	 * @param errLogGzTarget
	 * @param errLogLongNameTarget
	 * @return
	 */
	private Map<String,List<File>> targetFileInfo(List<File> soapLogTarget, List<File> soapLogGZTarget, List<File> errLogTarget,
			List<File> errLogGzTarget, List<File> errLogLongNameTarget) {
		Map<String,List<File>> analysisTargetMap = new HashMap<>();
		Collections.sort(soapLogGZTarget);
		Collections.sort(errLogGzTarget);
		Collections.sort(errLogLongNameTarget);
		List<File> soapFileList = new ArrayList<>();
		for (File file : soapLogGZTarget) {
			soapFileList.add(file);
		}
		for (File file : soapLogTarget) {
			soapFileList.add(file);
		}
		analysisTargetMap.put("SOAP", soapFileList);
		List<File> errFileList = new ArrayList<>();
		Map<Integer,File> allErrLogNameMap = new HashMap<>();
		for (File errLogGz : errLogGzTarget) {
			String fileName = errLogGz.getName().replace(".gz", "");
			Integer tailNumber = Integer.valueOf(fileName.split("\\.")[fileName.split("\\.").length-1]);//tail number
			allErrLogNameMap.put(tailNumber, errLogGz);
		}
		for (File errLogLongName : errLogLongNameTarget) {
			String fileName = errLogLongName.getName();
			Integer tailNumber = Integer.valueOf(fileName.split("\\.")[fileName.split("\\.").length-1]);//tail number
			allErrLogNameMap.put(tailNumber, errLogLongName);
		}
		Set<Integer> keySet = allErrLogNameMap.keySet();
		for (Integer tailNumber : keySet) {
			errFileList.add(allErrLogNameMap.get(tailNumber));
		}
		if(errLogTarget.size()==1){
			if(errLogTarget.get(0).getName().equals("BOSS_ERR_CASE.log")||errLogTarget.get(0).getName().equals("CUC_BOSS_ERR_CASE.log")){
				errFileList.add(errLogTarget.get(0));
			}
		}
		analysisTargetMap.put("ERR", errFileList);
		return analysisTargetMap;
	}
	/**
	 * 在没有MARK时，添加一条MARK。保证每台SOAP的MARK有且只有一条。
	 * @param soapName
	 */
	private void checkMarkRecord(String soapName){
		String sqlErr = "select * from err_mark where soap_name = ?";
		String sqlSoap = "select * from soap_mark where soap_name = ?";
		List<Map<String,Object>> errMarkResult = jdbcTemplate.queryForList(sqlErr,soapName);
		List<Map<String,Object>> soapMarkResult = jdbcTemplate.queryForList(sqlSoap,soapName);
		if(errMarkResult.size()==0){
			String sqlErrUpdate = "insert into err_mark (file_name,start_line,soap_name)values('waiting first err case file',0,?)";
			jdbcTemplate.update(sqlErrUpdate,soapName);
		}
		if(soapMarkResult.size()==0){
			String sqlSoapUpdate = "insert into soap_mark (file_name,start_line,soap_name)values('waiting first soap case file',0,?)";
			jdbcTemplate.update(sqlSoapUpdate,soapName);
		}
	}
	/**
	 * 执行同步流程。
	 * @param bossVersion
	 * @param soapLoginInfoMap
	 * @param analysisTargetMap
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 */
	private void executeAnalysis(String bossVersion, Map<String, String> soapLoginInfoMap, Map<String,List<File>> analysisTargetMap)
					throws IOException, DocumentException, ParseException {
		List<File> errFileList = analysisTargetMap.get("ERR");
		List<File> soapFileList = analysisTargetMap.get("SOAP");
		LOGGER.info("ANALYSIS ORDER:");
		for (File file : soapFileList) {
			LOGGER.info(file.getAbsolutePath());
		}
		for (File file : errFileList) {
			LOGGER.info(file.getAbsolutePath());
		}
		for (File soapFile : soapFileList) {
			LOGGER.info("ANALYSIS TARGET:"+soapFile.getName());
			if(bossVersion.equalsIgnoreCase("chinamobile")){
				analysedSerivice.soapLogDataAnalysis(soapFile, soapLoginInfoMap.get("soap-gw-name"));
			}else if(bossVersion.equalsIgnoreCase("unicom")){
				analysedSerivice.soapLogDataAnalysisUnicom(soapFile, soapLoginInfoMap.get("soap-gw-name"));
			}
	}
		for (File errFile : errFileList) {
				LOGGER.info("ANALYSIS TARGET:"+errFile.getName());
				if(bossVersion.equalsIgnoreCase("chinamobile")){
					analysedSerivice.errLogDataAnalysis(errFile, soapLoginInfoMap.get("soap-gw-name"));
				}else if(bossVersion.equalsIgnoreCase("unicom")){
					analysedSerivice.errLogDataAnalysisUnicom(errFile, soapLoginInfoMap.get("soap-gw-name"));
				}
				
			}

	}
	/**
	 * 给需要已发生改变的文件分类。目的是为了按顺序处理
	 * 首先处理已打包的ERR日志。按照名称顺序，按照GZ前面的数字（或者没有GZ，但是也有数字），从小到大处理。
	 * 再处理已打包的SOAP日志。按名称排序处理。
	 * @param analysedTarget
	 * @param soapLogTarget
	 * @param soapLogGZTarget
	 * @param errLogTarget
	 * @param errLogGzTarget
	 * @param errLogLongNameTarget
	 */
	public void targetFileClassification(List<File> analysedTarget, List<File> soapLogTarget,
			List<File> soapLogGZTarget, List<File> errLogTarget, List<File> errLogGzTarget ,List<File> errLogLongNameTarget) {
		String todayStr = DateUtils.getTargetTime().get("TODAY");
		for (File targetFile : analysedTarget) {
			if(targetFile.getName().contains("BOSS_ERR_CASE.log")){
				//ERR_CASE的LOG
				if(targetFile.getName().contains(".gz")){//LOG的GZ包，一定包含时间
					errLogGzTarget.add(targetFile);
				}else{//不是GZ包
					if(targetFile.getName().contains(todayStr)){//不是GZ包，包含时间
						errLogLongNameTarget.add(targetFile);
					} 
					if(targetFile.getName().equals("BOSS_ERR_CASE.log")||targetFile.getName().equals("CUC_BOSS_ERR_CASE.log")){//正在写入的文件
						errLogTarget.add(targetFile);
					}
				}
			}else if(targetFile.getName().contains("BOSS_SOAP_Agent_BOSSA_main_")||targetFile.getName().contains("CUC_Telnet_Agent_BOSSA_main_")){
				if(targetFile.getName().contains(".gz")){
					soapLogGZTarget.add(targetFile);
				}else{
					soapLogTarget.add(targetFile);
				}
			}
		}
	}
}