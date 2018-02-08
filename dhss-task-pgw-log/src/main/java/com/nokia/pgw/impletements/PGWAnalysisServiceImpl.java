package com.nokia.pgw.impletements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.nokia.pgw.service.PgwAnalysisService;
import com.nokia.pgw.settings.CustomSetting;
import com.nokia.pgw.task.PrepareAction;
import com.nokia.pgw.util.PGWAnalyseUtil;

@Service
public class PGWAnalysisServiceImpl implements PgwAnalysisService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PGWAnalysisServiceImpl.class);
	private static final String PGW_DETAIL_DATA_TABLE_NAME = "pgw_detail_data";
	private static final String PGW_XML_LOG_TABLE_NAME = "pgw_xml_log";
	private static final SimpleDateFormat PARTITION_NAME_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat DAY_START_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	private static final SimpleDateFormat DEBUG_LOG_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private CustomSetting customSetting;

	/**
	 * 
	 * ALTER TABLE pgw_xml_log PARTITION BY RANGE (TO_DAYS(response_time))(
	 * PARTITION p_20150502 VALUES less than (TO_DAYS('2017-05-03 00:00:00')) );
	 */
	@Override
	public void handlePartition() {
		createPartition();
		deletePartition();
	}

	public void debugLogger(String logInfo) {
		Date d = new Date();

		File debugLog = new File(customSetting.getPgwLogDeployDir() + "debug.log");
		try {
			if (!debugLog.exists()) {
				debugLog.createNewFile();
			}
			FileWriter fw = new FileWriter(debugLog, true);
			fw.write(DEBUG_LOG_TIME_FORMAT.format(d) + " --------> " + logInfo + "\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.info("IO exception happened:cannot write debug log.");
		}

	}

	private void deletePartition() {
		String sql = "alter table #tableName# drop partition #partitionName#";
		Integer saveDays = customSetting.getSaveDays();
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, -saveDays);
		Date oldestDataTime = now.getTime();
		String partitionName = "p_" + PARTITION_NAME_FORMAT.format(oldestDataTime);
		String firstSql = sql.replace("#tableName#", PGW_DETAIL_DATA_TABLE_NAME);
		firstSql = firstSql.replace("#partitionName#", partitionName);
		String secondSql = sql.replace("#tableName#", PGW_XML_LOG_TABLE_NAME);
		secondSql = secondSql.replace("#partitionName#", partitionName);
		try {
			jdbcTemplate.execute(firstSql);
			jdbcTemplate.execute(secondSql);
		} catch (Exception e) {
			e.getMessage();
			LOGGER.info("Partition delete failed.");
			LOGGER.info("SQL:");
			LOGGER.info(firstSql);
			LOGGER.info(secondSql);
		}
	}

	private void createPartition() {
		String sql = "alter table #tableName# add partition (partition #partitionName# values less than(TO_DAYS('#tomorrowStartTime#')))";
		Calendar now = Calendar.getInstance();
		Date todayDate = now.getTime();
		now.add(Calendar.DATE, +1);
		Date tomorrowDate = now.getTime();
		String partitionName = "p_" + PARTITION_NAME_FORMAT.format(todayDate);
		String lessThanDate = DAY_START_TIME_FORMAT.format(tomorrowDate);
		String firstSql = sql.replace("#tableName#", PGW_DETAIL_DATA_TABLE_NAME);
		firstSql = firstSql.replace("#partitionName#", partitionName);
		firstSql = firstSql.replace("#tomorrowStartTime#", lessThanDate);
		String secondSql = sql.replace("#tableName#", PGW_XML_LOG_TABLE_NAME);
		secondSql = secondSql.replace("#partitionName#", partitionName);
		secondSql = secondSql.replace("#tomorrowStartTime#", lessThanDate);
		try {
			jdbcTemplate.execute(firstSql);
			jdbcTemplate.execute(secondSql);
		} catch (Exception e) {
			e.getMessage();
			LOGGER.info("Partition creation failed.");
			LOGGER.info("SQL:");
			LOGGER.info(firstSql);
			LOGGER.info(secondSql);
		}
	}

	@Override
	public List<String> getRsyncInfo(String rsyncCmd) {
		List<String> rsyncInfo = new ArrayList<>();
		LOGGER.info("Try to execute rsync command:");
		LOGGER.info(rsyncCmd);
		try {
			Process rsyncProcess = Runtime.getRuntime().exec(rsyncCmd);
			rsyncProcess.waitFor();
			InputStreamReader in = new InputStreamReader(rsyncProcess.getErrorStream());
			BufferedReader br = new BufferedReader(in);
			String callbackLine = null;
			if ((callbackLine = br.readLine()) != null) {
				LOGGER.info("Error info :");
				LOGGER.info("=======================================");
				while ((callbackLine = br.readLine()) != null) {
					rsyncInfo.add(callbackLine);
					LOGGER.info(callbackLine);
				}
				LOGGER.info("=======================================");
				in.close();
				br.close();
			}

			in = new InputStreamReader(rsyncProcess.getInputStream());
			br = new BufferedReader(in);
			callbackLine = null;
			LOGGER.info("Callback info :");
			LOGGER.info("=======================================");
			while ((callbackLine = br.readLine()) != null) {
				if (callbackLine.contains(".gz")) {
					rsyncInfo.add(callbackLine);
					LOGGER.info(callbackLine);
				}
			}
			LOGGER.info("=======================================");
		} catch (IOException e) {
			LOGGER.info("Rsync execution failed:IO FAILED");
			e.printStackTrace();
		} catch (InterruptedException e) {
			LOGGER.info("Rsync execution failed:RSYNC CMD INTERRUPTED.");
			e.printStackTrace();
		}
		return rsyncInfo;
	}

	@Override
	public void loadDataToDB() {
		File loadDetailFileDir = new File(PrepareAction.getLoaderDetailFileDir());
		File loadXmlFileDir = new File(PrepareAction.getLoaderXMLFileDir());
		File[] loadDetailFiles = loadDetailFileDir.listFiles();
		if (loadDetailFiles.length == 0) {
			LOGGER.info("No detail data need to be loaded into DB");
		} else {
			for (File loadDetailFile : loadDetailFiles) {
				String loadDetailDataSQL = PrepareAction.getDetailDataLoadSQL().replace("#fileAbsPath#",
						loadDetailFile.getAbsolutePath());
				LOGGER.info("Load detail data file:" + loadDetailFile.getAbsolutePath());
				try {
					LOGGER.info("Execute detail data load SQL:");
					LOGGER.info(loadDetailDataSQL);
					jdbcTemplate.execute(loadDetailDataSQL);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		/////////////////////////
		File[] loadXmlDataFiles = loadXmlFileDir.listFiles();
		if (loadXmlDataFiles.length == 0) {
			LOGGER.info("No xml loader data need to be loaded into DB");
		} else {
			for (File loadXmlDataFile : loadXmlDataFiles) {
				String loadXMLDataSQL = PrepareAction.getXMLDataLoadSQL().replace("#fileAbsPath#",
						loadXmlDataFile.getAbsolutePath());
				LOGGER.info("Load xml data file:" + loadXmlDataFile.getAbsolutePath());
				try {
					LOGGER.info("Execute xml log data load SQL:");
					LOGGER.info(loadXMLDataSQL);
					jdbcTemplate.execute(loadXMLDataSQL);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public List<String> getAllRsyncCommand() {

		String rsyncCmdPattern = customSetting.getRsyncCmdPattern();
		String remotePgwLogBaseDir = customSetting.getRemotePgwLogBaseDir();
		String accurateMatchRuleFileDir = customSetting.getAccurateMatchRuleFileDir();
		Boolean isDryRunMode = customSetting.isDryRunMode();
		Boolean isAccurateSyncMode = customSetting.isAccurateSyncMode();
		List<Map<String, Object>> deviceBasicInfoList = new ArrayList<>();
		List<String> rsyncCmdList = new ArrayList<>();
		deviceBasicInfoList = deviceBasicInfo();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		Date yesterdayDate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
		String yesterdayStr = sdf.format(yesterdayDate);
		for (Map<String, Object> deviceBasicInfoMap : deviceBasicInfoList) {
			try {
				String password = deviceBasicInfoMap.get("password").toString();
				rsyncCmdPattern = rsyncCmdPattern.replace("#password#", password);
				String userName = deviceBasicInfoMap.get("userName").toString();
				rsyncCmdPattern = rsyncCmdPattern.replace("#userName#", userName);
				String pgwIp = deviceBasicInfoMap.get("pgwIp").toString();
				rsyncCmdPattern = rsyncCmdPattern.replace("#pgwIp#", pgwIp);
				rsyncCmdPattern = rsyncCmdPattern.replace("#pgwLogRemoteDir#", remotePgwLogBaseDir);
				rsyncCmdPattern = rsyncCmdPattern.replace("#data-time#", yesterdayStr);
				String pgwName = deviceBasicInfoMap.get("pgwName").toString();
				String rsyncDataLocalDir = PrepareAction.getRsyncDataFileDir() + pgwName;
				File rsyncDataLocalDirFile = new File(rsyncDataLocalDir);
				if (!rsyncDataLocalDirFile.exists()) {
					LOGGER.info(rsyncDataLocalDir);
					LOGGER.info("rsync-data dir not exist.mkdirs.");
					rsyncDataLocalDirFile.mkdirs();
				} else {
					LOGGER.info(rsyncDataLocalDir);
					LOGGER.info("rsync-data dir exist.");
				}
				rsyncCmdPattern = rsyncCmdPattern.replace("#rsyncLocalDataDir#", rsyncDataLocalDir);
				if (isDryRunMode == true) {
					rsyncCmdPattern = rsyncCmdPattern.replace("#--dry-run#", "--dry-run");
				} else {
					rsyncCmdPattern = rsyncCmdPattern.replace(" #--dry-run# ", " ");
				}
				if (isAccurateSyncMode == true) {
					rsyncCmdPattern = rsyncCmdPattern.replace("#pattern-rule#", accurateMatchRuleFileDir);
				} else {
					rsyncCmdPattern = rsyncCmdPattern
							.replace(" --include-from=#pattern-rule# --exclude-from=#pattern-rule# ", " ");
				}
				LOGGER.info("Generated a new rsync command:");
				LOGGER.info(rsyncCmdPattern);
				rsyncCmdList.add(rsyncCmdPattern);
			} catch (Exception e) {
				LOGGER.info("Error occurs while generating rsync commands.");
				LOGGER.info("RSYNC COMMAND:");
				LOGGER.info(rsyncCmdPattern);
				e.printStackTrace();
			}
		}
		return rsyncCmdList;
	}

	@Override
	public List<Map<String, Object>> deviceBasicInfo() {
		String configuredInfo = customSetting.getPgwBasicInfo();
		String[] basicInfoPieces = configuredInfo.split(",");
		List<Map<String, Object>> pgwBasicInfoList = new ArrayList<>();
		for (String aBasicInfoPiece : basicInfoPieces) {
			Map<String, Object> aBasicInfoMap = new HashMap<>();
			String[] basicInfoArray = aBasicInfoPiece.split("-");
			String pgwName = basicInfoArray[0];
			aBasicInfoMap.put("pgwName", pgwName);
			String pgwIp = basicInfoArray[1];
			aBasicInfoMap.put("pgwIp", pgwIp);
			String userName = basicInfoArray[2];
			aBasicInfoMap.put("userName", userName);
			String password = basicInfoArray[3];
			aBasicInfoMap.put("password", password);
			pgwBasicInfoList.add(aBasicInfoMap);
		}
		return pgwBasicInfoList;
	}

	public Map<String, String> analysisPgwLogLineFront(String pgwLogLine) {
		Map<String, String> frontHalfTextAnalayseMap = new HashMap<>();
		try {
			String frontHalfLine = pgwLogLine.substring(0, pgwLogLine.indexOf("<"));
			String responseTime = frontHalfLine.substring(0, frontHalfLine.indexOf(","));
			frontHalfTextAnalayseMap.put("responseTime", responseTime);
			String[] frontHalfLineElements = frontHalfLine.split(" ");
			String pgwName = frontHalfLineElements[2];
			frontHalfTextAnalayseMap.put("pgwName", pgwName);
			String instanceName = frontHalfLineElements[3];
			frontHalfTextAnalayseMap.put("instanceName", instanceName);
			String userName = frontHalfLineElements[4];
			frontHalfTextAnalayseMap.put("userName", userName);
			String executionContent = frontHalfLineElements[5];
			frontHalfTextAnalayseMap.put("executionContent", executionContent);
			String pgwLogXmlPart = pgwLogLine.substring(pgwLogLine.indexOf("<"));
			frontHalfTextAnalayseMap.put("pgwLogXmlPart", pgwLogXmlPart);
		} catch (Exception e) {
			e.printStackTrace();
			debugLogger(e.getMessage());
			debugLogger(pgwLogLine);
		}
		return frontHalfTextAnalayseMap;
	}

	public Map<String, String> analysisPgwLogLineXml(String pgwLogLineBackHalf) {
		SAXReader reader = new SAXReader();
		StringReader in = new StringReader(pgwLogLineBackHalf);
		Map<String, String> pgwLogBackHalfAnalysisMap = new HashMap<>();
		try {
			Document doc = reader.read(in);
			OutputFormat formater = OutputFormat.createPrettyPrint();
			formater.setEncoding("UTF-8");
			StringWriter out = new StringWriter();
			XMLWriter writer = new XMLWriter(out, formater);
			writer.write(doc);
			writer.close();
			String formatedXml = out.toString();
			pgwLogBackHalfAnalysisMap.put("formatedXml", formatedXml);
			Element root = doc.getRootElement();
			String requestID = root.attribute("requestID").getText();
			pgwLogBackHalfAnalysisMap.put("requestID", requestID);
			String executionTime = root.attribute("executionTime").getText();
			pgwLogBackHalfAnalysisMap.put("executionTime", executionTime);
			String resultType = root.attribute("result").getText();
			pgwLogBackHalfAnalysisMap.put("resultType", resultType);
			String identifier = root.elementText("identifier") != null ? root.elementText("identifier")
					: getImsi(pgwLogLineBackHalf);
			if (identifier == null) {
				identifier = "0";
			}
			pgwLogBackHalfAnalysisMap.put("userNumber", identifier);
			String errorCode = root.attributeValue("errorCode") != null ? root.attributeValue("errorCode") : "";
			pgwLogBackHalfAnalysisMap.put("errorCode", errorCode);
			String errorMessage = root.elementText("errorMessage") != null ? root.elementText("errorMessage") : "";
			pgwLogBackHalfAnalysisMap.put("errorMessage", errorMessage);
			Element modificationNode = root.element("modification");
			String operation = null;
			if (modificationNode == null) {
				operation = "";
			} else {
				operation = modificationNode.attribute("operation").getText();
			}
			pgwLogBackHalfAnalysisMap.put("operation", operation);
		} catch (DocumentException e) {
			e.printStackTrace();
			LOGGER.info(pgwLogLineBackHalf);
			debugLogger(e.getMessage());
			debugLogger(pgwLogLineBackHalf);
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.info(pgwLogLineBackHalf);
			debugLogger(e.getMessage());
			debugLogger(pgwLogLineBackHalf);
		} catch (Exception e) {
			e.printStackTrace();
			debugLogger(e.getMessage());
			debugLogger(pgwLogLineBackHalf);
		}
		return pgwLogBackHalfAnalysisMap;
	}

	private String getImsi(String xml) {
		String imsi;
		try {
			imsi = xml.substring(xml.indexOf("<imsi>"), xml.indexOf("</imsi>"));
		} catch (IndexOutOfBoundsException e) {
			imsi = "0";
		}
		imsi = imsi.replace("<imsi>", "");
		return imsi;
	}

	@Override
	public void analysisTargetFile() {
		File uncompressedDir = new File(PrepareAction.getUncompressedFileDir());
		File[] uncompressedFiles = uncompressedDir.listFiles();
		for (File uncompressedFile : uncompressedFiles) {
			try {
				LOGGER.info("Analysis target:" + uncompressedFile.getAbsolutePath());
				analysisSingleFile(uncompressedFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void analysisSingleFile(File pgwLog) throws IOException {
		FileReader fr = new FileReader(pgwLog);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		List<Map<String, String>> dataList = new ArrayList<>();
		while ((line = br.readLine()) != null) {
			boolean isResponseLine = judgeResponse(line);
			if (isResponseLine == true) {
				try {
					Map<String, String> dataMap = new HashMap<>();
					dataMap = analysisPgwLogLineFront(line);
					String xmlPart = dataMap.get("pgwLogXmlPart").toString();
					dataMap.putAll(analysisPgwLogLineXml(xmlPart));
					dataList.add(dataMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		br.close();
		LOGGER.info("Data analysis completed.Data record number:" + dataList.size());
		writeLoaderFile(pgwLog.getName(), dataList);
	}

	private boolean judgeResponse(String line) {
		if (line.contains("<spml:modifyResponse") || line.contains("<spml:addResponse")
				|| line.contains("<spml:deleteResponse") || line.contains("<spml:extendedResponse")) {
			return true;
		} else {
			return false;
		}
	}

	private void writeLoaderFile(String pgwLogName, List<Map<String, String>> dataList) {
		LOGGER.info("Data analysis completed.Start to write loader file.");
		try {
			writeDetailDataFile(pgwLogName, dataList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writeXmlLogFile(pgwLogName, dataList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info("All loader files written.");
	}

	private void writeXmlLogFile(String pgwLogName, List<Map<String, String>> dataList) throws IOException {
		LOGGER.info("Writing a new xml log table loader file...");
		StringBuilder loader = new StringBuilder();
		for (Map<String, String> dataMap : dataList) {
			String responseTime = dataMap.get("responseTime");
			String requestID = dataMap.get("requestID");
			String xmlLog = dataMap.get("formatedXml");
			loader.append(responseTime + PGWAnalyseUtil.getFieldTerminator());
			loader.append(requestID + PGWAnalyseUtil.getFieldTerminator());
			loader.append(xmlLog + PGWAnalyseUtil.getLineTerminator());
		}
		File loaderFile = new File(PrepareAction.getLoaderXMLFileDir() + pgwLogName + ".xmlloader");
		LOGGER.info("Write data as:"+loaderFile.getAbsolutePath());
		loaderFile.createNewFile();
		FileWriter fw = new FileWriter(loaderFile);
		fw.write(loader.toString());
		fw.close();
		LOGGER.info("Writing completed!Path:" + loaderFile.getAbsolutePath() + ",size:"
				+ loaderFile.length() / 1024.0 / 1024.0 + "Mb");
	}

	private void writeDetailDataFile(String pgwLogName, List<Map<String, String>> dataList) throws IOException {
		LOGGER.info("Writing a new detail data table loader file...");
		StringBuilder loader = new StringBuilder();
		for (Map<String, String> dataMap : dataList) {
			String responseTime = dataMap.get("responseTime");
			String requestID = dataMap.get("requestID");
			String pgwName = dataMap.get("pgwName");
			String instanceName = dataMap.get("instanceName");
			String userName = dataMap.get("userName");
			String executionTime = dataMap.get("executionTime");
			String executionContent = dataMap.get("executionContent");
			String resultType = dataMap.get("resultType");
			String operation = dataMap.get("operation");
			String errorCode = dataMap.get("errorCode");
			String errorMessage = dataMap.get("errorMessage");
			String userNumber = dataMap.get("userNumber");
			loader.append(responseTime + PGWAnalyseUtil.getFieldTerminator());
			loader.append(requestID + PGWAnalyseUtil.getFieldTerminator());
			loader.append(pgwName + PGWAnalyseUtil.getFieldTerminator());
			loader.append(instanceName + PGWAnalyseUtil.getFieldTerminator());
			loader.append(userName + PGWAnalyseUtil.getFieldTerminator());
			loader.append(executionTime + PGWAnalyseUtil.getFieldTerminator());
			loader.append(executionContent + PGWAnalyseUtil.getFieldTerminator());
			loader.append(resultType + PGWAnalyseUtil.getFieldTerminator());
			loader.append(operation + PGWAnalyseUtil.getFieldTerminator());
			loader.append(userNumber + PGWAnalyseUtil.getFieldTerminator());
			loader.append(errorCode + PGWAnalyseUtil.getFieldTerminator());
			loader.append(errorMessage + PGWAnalyseUtil.getLineTerminator());
		}
		File loaderFile = new File(PrepareAction.getLoaderDetailFileDir() + pgwLogName + ".detail");
		LOGGER.info("Write data as:"+loaderFile.getAbsolutePath());
		loaderFile.createNewFile();
		FileWriter fw = new FileWriter(loaderFile);
		fw.write(loader.toString());
		fw.close();
		LOGGER.info("Writing completed!Path:" + loaderFile.getAbsolutePath() + ",size:"
				+ loaderFile.length() / 1024.0 / 1024.0 + "Mb");
	}

	@Override
	public void clearTempFile() {
		File uncompressedDir = new File(PrepareAction.getUncompressedFileDir());
		File xmlLoaderDir = new File(PrepareAction.getLoaderXMLFileDir());
		File detailLoaderDir = new File(PrepareAction.getLoaderDetailFileDir());
		File[] uncompressedFiles = uncompressedDir.listFiles();
		File[] xmlLoaderFiles = xmlLoaderDir.listFiles();
		File[] detailLoaderFiles = detailLoaderDir.listFiles();
		for (File file : uncompressedFiles) {
			LOGGER.info("Delete uncompressed file:" + file.getAbsolutePath());
			file.delete();
		}
		for (File file : xmlLoaderFiles) {
			LOGGER.info("Delete xmlLoader file:" + file.getAbsolutePath());
			file.delete();
		}
		for (File file : detailLoaderFiles) {
			LOGGER.info("Delete detailLoader file:" + file.getAbsolutePath());
			file.delete();
		}
	}

}
