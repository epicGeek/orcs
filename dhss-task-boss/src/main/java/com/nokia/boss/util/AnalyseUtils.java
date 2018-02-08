package com.nokia.boss.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokia.boss.settings.CustomSettings;
import com.nokia.boss.task.LoadStaticData;

public class AnalyseUtils {

	private static final Logger logger = LoggerFactory.getLogger(AnalyseUtils.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	/**
	 * 把GZ文件转换成普通的文本文件类，便于解析。
	 * @param gzPath GZ包的完整路径
	 * @param soapGwName SOAPGW名称
	 * @return
	 * @throws IOException
	 */
	public static File gzToFile(String gzPath,String soapGwName) throws IOException{
		String gzFileName = gzPath.split("/")[gzPath.split("/").length-1];
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new GZIPInputStream(new FileInputStream(gzPath)), "utf-8"));
		String line = null;
		StringBuilder gzText = new StringBuilder();
		while ((line = br.readLine()) != null) {
			gzText.append(line+"\n");
		}
		br.close();
		String tempTransformFileAbsPath = CustomSettings.getTransformDir()+soapGwName+"_"+gzFileName.replace(".gz", ".trans");
		File f = new File(tempTransformFileAbsPath);
		FileWriter fw = new FileWriter(f);
		fw.write(gzText.toString());
		fw.close();
		return f;
	}
	/**
	 * 把ERROR日志的哈希表写成可以LOAD入库的字符串。
	 * @param errorAnalysedDataList
	 * @return
	 */
	public static Map<String,StringBuilder> errorAnalysedDataListToLoader(List<Map<String, Object>> errorAnalysedDataList){
		Map<String,StringBuilder> errorLoaderMap = new HashMap<>();
		StringBuilder errorLoader = new StringBuilder();
		StringBuilder errorJoinLoader = new StringBuilder();
		
		/**
		 * fail data load SQL column :
			"	re_id,\n" +
			"	re_time,\n" +
			"	response_time,\n" +
			"	user_name,\n" +
			"	user_password,\n" +
			"	imsi,\n" +
			"	msisdn,\n" +
			"	hlrsn,\n" +
			"	business_type,\n" +
			"	operation_name\n" +
		 */
		for (Map<String, Object> errorAnalysedMap : errorAnalysedDataList) {
			try {
				errorLoader.append(errorAnalysedMap.getOrDefault("re_id", "Boss program error!")+"~");
				errorLoader.append(errorAnalysedMap.getOrDefault("re_time", "Boss program error!")+"~");
				errorLoader.append(errorAnalysedMap.getOrDefault("response_time", "Boss program error!")+"~");
				errorLoader.append(errorAnalysedMap.getOrDefault("user_name", "Boss program error!")+"~");
				errorLoader.append(errorAnalysedMap.getOrDefault("user_password", "Boss program error!")+"~");
				errorLoader.append(errorAnalysedMap.getOrDefault("imsi", 0)+"~");
				errorLoader.append(errorAnalysedMap.getOrDefault("msisdn", 0)+"~");
				errorLoader.append(errorAnalysedMap.getOrDefault("hlrsn", 0)+"~");
				errorLoader.append(errorAnalysedMap.getOrDefault("business_type", "Boss program error!")+"~");
				errorLoader.append(errorAnalysedMap.getOrDefault("operation_name", "Boss program error!")+"~");
				errorLoader.append(errorAnalysedMap.getOrDefault("error_code", "Boss program error!")+"~");
				errorLoader.append(errorAnalysedMap.getOrDefault("error_message", "Boss program error!")+";;;");
			} catch (Exception e) {
				logger.info("cannot concat this error log piece:");
				logger.info(errorAnalysedMap.toString());
				e.printStackTrace();
			}
      
		}
		errorLoaderMap.put("error", errorLoader);
		/**
		 * 	"	re_id,\n" +
			"	soap_log,\n" +
			"	err_log \n" +
		 */
		for (Map<String, Object> errorAnalysedMap : errorAnalysedDataList) {
			try {
				errorJoinLoader.append(errorAnalysedMap.get("re_id").toString()+"~");
				errorJoinLoader.append("~");
				errorJoinLoader.append(errorAnalysedMap.get("err_log").toString()+";;;");
			} catch (Exception e) {
				logger.info("cannot concat this error join log");
				logger.info(errorAnalysedMap.toString());
			}

		}
		errorLoaderMap.put("join", errorJoinLoader);
		return errorLoaderMap;
	}
	/**
	 * 把解析完转化好的字符串写成文件，便于LOAD入库.
	 * @param loader
	 * @return
	 * @throws IOException
	 */
	public static File writeLoadFile(StringBuilder loader, String fileName,String soapGwName,String loadType) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
		String newFileDir = "";
		if (loadType.equals("data")) {
			newFileDir = CustomSettings.getLoadFileDir().replace("#soap-gw-name#", soapGwName) + sdf.format(new Date()) + fileName + ".loaddata";
		} else if(loadType.equals("join")) {
			newFileDir = CustomSettings.getLoadFileDir().replace("#soap-gw-name#", soapGwName) + sdf.format(new Date()) + fileName + ".loadjoin";
		} else if(loadType.equals("err")){
			newFileDir = CustomSettings.getLoadFileDir().replace("#soap-gw-name#", soapGwName) + sdf.format(new Date()) + fileName + ".loaderr";
		} else if(loadType.equals("errtemp")){
			newFileDir = CustomSettings.getLoadFileDir().replace("#soap-gw-name#", soapGwName) + sdf.format(new Date()) + fileName + ".loaderrtemp";
		}
		logger.info("Write loader:"+newFileDir);
		File loadFile = new File(newFileDir);
		loadFile.createNewFile();
		if (loadFile.exists()) {
			FileWriter fw = new FileWriter(loadFile);
			fw.write(loader.toString());
			fw.close();
			return loadFile;
		} else {
			logger.info("cannot create new load file!");
			return null;
		}
	}

	/**
	 * 把SOAP日志解析为两种字符串，都是以LOAD SQL的格式写好。
	 * @param originalText
	 * @return
	 */

	public static Map<String, StringBuilder> originalDataTransformer(StringBuilder originalText) {
		StringBuilder loadDataStringBuilder = new StringBuilder();
		StringBuilder loadJoinStringBuilder = new StringBuilder();
		String[] originDataLines = originalText.toString().split("\n");
		List<String> successDataList = new ArrayList<String>();
		Map<String,String> stateMap = new HashMap<>();
		for (String string : originDataLines) {
			if (string.contains("Callback")) {
				//id->time
				stateMap.put(string.split("\\|")[2].trim().replace("id:", ""),string.substring(0,23));
			} else {
				successDataList.add(string);
			}
		}
		logger.info("Soap data size:"+successDataList.size());
		logger.info("State data size:"+stateMap.size());
		for (String dataLine : successDataList) {
			String dataLine_ = dataLine;
			try {
				/**
				 * Split 80M text file To increase the efficiency of the split.
				 * Choose to use this method instead of "string.split" local
				 * test: Use string.split : near 10s. Use indexOf : near 1s.
				 */
				int splitFlag = 0;
				String reTime = null;
				String user = null;
				String reId = null;
				String restInfo = null;
				while (true) {
					String splitStr = null;
					int j = dataLine.indexOf("|");
					if (j < 0)
						break;
					splitStr = dataLine.substring(0, j);
					dataLine = dataLine.substring(j + 1);
					switch (splitFlag) {
					case 0:
						reTime = splitStr;
					case 1:
						user = splitStr;
					case 2:
						reId = splitStr;
					case 3:
						restInfo = splitStr;
					}
					splitFlag++;
				}
				String responseTime = reTime.substring(0, 19);
				user = user.replace("User: ", "");
				reId = reId.replace("id:", "").trim();

				/**
				 * Analyze "JSON" in the rest info.
				 */
				String MSISDN = getJsonValue(restInfo, LoadStaticData.getMSISDN_PATTERN());
				String HLRSN = getJsonValue(restInfo, LoadStaticData.getHLRSN_PATTERN());
				String IMSI = getJsonValue(restInfo, LoadStaticData.getIMSI_PATTERN());
				String IMPU = getJsonValue(restInfo, LoadStaticData.getIMPU_PATTERN());
				String operationName = getJsonValue(restInfo, LoadStaticData.getOPERATION_NAME_PATTERN());
				String businessType = LoadStaticData.getBUSINESS_TYPE_MAP().get(operationName);
				if (IMSI.equals("") && MSISDN.equals("")) {
					if (IMPU.contains("+86")) {
						MSISDN = IMPU.substring(5, 18);
					}
					if (IMPU.contains("46000")) {
						IMSI = IMPU.substring(4, 19);
					} 
				}
				long delay = 0;
				String endTime = stateMap.get(reId);
				if(endTime!=null){
					String startTime = reTime;
					delay = delayTimeCalculator(startTime, endTime);
				}
				
				
				loadDataStringBuilder.append(user +"~"+reId +"~"+responseTime + "~" 
											+ HLRSN + "~"+ MSISDN + "~"  
											+ IMSI + "~"+ reTime + "~"  
											+ operationName + "~"+ businessType + "~"+delay 
											+ ";;;");
				
				loadJoinStringBuilder.append(reId + "~" + dataLine_ + ";;;");
				
			} catch (Exception e) {
				logger.info("cannot ananlysis this line:");
				logger.info(dataLine_);
				e.printStackTrace();
			}

		}
		Map<String, StringBuilder> transformerMap = new HashMap<String, StringBuilder>();
		transformerMap.put("boss_soap", loadDataStringBuilder);
		transformerMap.put("boss_join", loadJoinStringBuilder);
		return transformerMap;
	}
	/**
	 * 计算下发指令的时间延迟
	 * @param startReTime
	 * @param endReTime
	 * @return
	 * @throws ParseException
	 */
	public static long delayTimeCalculator(String startReTime,String endReTime) throws ParseException{
		long delayTime = 0;
		Date startTime = sdf.parse(startReTime);
		Date endTime = sdf.parse(endReTime);
		delayTime = endTime.getTime()-startTime.getTime();
		return delayTime;
	}

	/**
	 * 根据给出的起始行，读取目标文件，从起始行读到文件最后一行，并返回最后一行的行数。
	 * @param source
	 * @param startLine
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> getAnalysisTarget(File source, int startLine) throws IOException {
		Map<String, Object> targetInfoMap = new HashMap<>();
		logger.info("analysis target file:" + source.getAbsolutePath());
		StringBuilder allText = new StringBuilder();

		FileReader fr = new FileReader(source);
		BufferedReader br = new BufferedReader(fr);
		String tempStr = null;
		while ((tempStr = br.readLine()) != null) {
			allText.append(tempStr + "\n");

		}
		br.close();
		StringBuilder targetText = new StringBuilder();
		String[] lines = allText.toString().split("\n");
		int endLine = startLine;
		for (int i = startLine; i < lines.length; i++) {
			targetText.append(lines[i] + "\n");
			endLine++;
		}
		targetInfoMap.put("targetText", targetText);
		targetInfoMap.put("endLine", endLine);
		return targetInfoMap;

	}
	/**
	 * 快速解析BOSS SOAP JSON串的方法。无须引包，增加JSON解析速度。
	 * @param restInfo BOSS_SOAP日志的JSON串部分。
	 * @param KEY_PATTERN JSON串的KEY值。
	 * @return
	 */
	public static String getJsonValue(String restInfo, String KEY_PATTERN) {
		char[] tempCharArray = restInfo.toCharArray();
		int endFlag = -1;
		for (int i = (restInfo.indexOf(KEY_PATTERN) + KEY_PATTERN.length()); i < tempCharArray.length; i++) {
			if (tempCharArray[i] == '"') {
				endFlag = i;
				break;
			}
		}
		return restInfo.substring(restInfo.indexOf(KEY_PATTERN) + KEY_PATTERN.length(), endFlag);

	}
	/**
	 * 解析ERROR XML标签的值，解析后添加到errorInfoMap
	 * @param node
	 * @param errorInfoMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> listNodes(Element node, Map<String, Object> errorInfoMap) {

		if (!(node.getTextTrim().equals(""))) {
			if (node.getName().equalsIgnoreCase("PassWord")) {
				errorInfoMap.put("user_password", node.getText());
			}
			if (node.getName().equalsIgnoreCase("UserName")) {
				errorInfoMap.put("user_name", node.getText());
			}
			if (node.getName().equalsIgnoreCase("HLRSN")) {
				errorInfoMap.put("hlrsn", node.getText());
			}
			if (node.getName().equalsIgnoreCase("IMSI")) {
				errorInfoMap.put("imsi", node.getText());
//				logger.info("!!!!!!!!!!!!!!!!!!");
//				logger.info("imsi:"+node.getText());
//				logger.info("!!!!!!!!!!!!!!!!!!");
			}
			if (node.getName().equalsIgnoreCase("ISDN")) {
				errorInfoMap.put("msisdn", node.getText());
			}
			if (node.getName().equalsIgnoreCase("IMPU")) {
				if (node.getText().contains("+86")) {
					errorInfoMap.put("msisdn", node.getText().substring(5, 18));
				} else {
					errorInfoMap.put("imsi", node.getText().substring(4, 18));
				}
			}
			if (node.getName().equalsIgnoreCase("ResultCode")) {
				errorInfoMap.put("error_code", node.getText());
			}
			if (node.getName().equalsIgnoreCase("ResultDesc")) {
				errorInfoMap.put("error_message", node.getText());
			}
		}

		Iterator<Element> iterator = node.elementIterator();
		while (iterator.hasNext()) {
			Element e = iterator.next();
			listNodes(e, errorInfoMap);
		}
		return errorInfoMap;
	}
	/**
	 * 解析ERROR日志
	 * @author Pei Nan
	 * @param errorLog. 完整的ERROR日志
	 * @return errorInfoMap.
	 * @throws DocumentException
	 */
	public static Map<String, List<Map<String, Object>>> getErrorInfoMap(StringBuilder errorLog)
			throws DocumentException {
		String[] errorLogPieces = errorLog.toString().trim().split("\\+{57}");
		Map<String, List<Map<String, Object>>> errorInfoMap = new HashMap<>();
		List<String> normalErrorListOriginal = new ArrayList<>();
		List<String> heartBeatErrorListOriginal = new ArrayList<>();
		for (String error : errorLogPieces) {
			if (error.contains("#HeartBeat#")) {
				heartBeatErrorListOriginal.add(error);
			} else {
				normalErrorListOriginal.add(error);
			}
		}
		logger.info("Normal error:" + normalErrorListOriginal.size());
		logger.info("HeartBeat error:" + heartBeatErrorListOriginal.size());
		logger.info("All size:" + errorLogPieces.length);
		// 2 types of error log.
		List<Map<String, Object>> normalErrorAnalysedList = new ArrayList<>();
		for (String normalError : normalErrorListOriginal) {
			try {
				Map<String, Object> normalErrorMap = new HashMap<>();
				normalErrorMap.put("err_log", normalError);
				String re_id = normalError.trim().split("={57}")[0].split(" ")[6];
				String re_time = normalError.trim().split("={57}")[0].split(" ")[0] + " "
						+ normalError.trim().split("={57}")[0].split(" ")[1];
				String response_time = re_time.replace("-", "").replace(":", "").replace(" ", "").replace(",", "");
				normalErrorMap.put("re_id", re_id);
				normalErrorMap.put("re_time", re_time);
				normalErrorMap.put("response_time", response_time);
				// request:
				String requestPhase = normalError.trim().split("={57}")[1].split("-{57}")[0].trim().replace("request:", "")
						.trim();
				Document documentRequest = DocumentHelper.parseText(requestPhase.toString());
				Element rootRequest = documentRequest.getRootElement();
				normalErrorMap.putAll(listNodes(rootRequest, normalErrorMap));
				// response:
				String responsePhase = normalError.trim().split("={57}")[1].split("-{57}")[1].trim()
						.replace("response:", "").trim();
				Document documentResponse = DocumentHelper.parseText(responsePhase.toString());
				Element rootResponse = documentResponse.getRootElement();
				for (String line : responsePhase.split("\n")) {
					if (line.contains("Response>")) {
						String operation_name = line.trim().replace(">", "").replace("<", "").replace("/", "")
								.replace("Response", "");
						normalErrorMap.put("operation_name", operation_name);
						normalErrorMap.put("business_type", LoadStaticData.getBUSINESS_TYPE_MAP().get(operation_name));
					}
				}
				normalErrorMap.putAll(listNodes(rootResponse, normalErrorMap));
				normalErrorAnalysedList.add(normalErrorMap);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("This log cannot be analysed:::");
				logger.info(normalError);
			}

		}

		List<Map<String, Object>> heartBeatErrorAnalysedList = new ArrayList<>();
		for (String heartBeatError : heartBeatErrorListOriginal) {
			try {
				Map<String, Object> heartBeatErrorMap = new HashMap<>();
				heartBeatErrorMap.put("operation_name", "HeartBeat");
				heartBeatErrorMap.put("err_log", heartBeatError);
				String re_id = heartBeatError.trim().split("={57}")[0].split(" ")[6];
				String re_time = heartBeatError.trim().split("={57}")[0].split(" ")[0] + " "
						+ heartBeatError.trim().split("={57}")[0].split(" ")[1];
				String response_time = re_time.replace("-", "").replace(":", "").replace(" ", "").replace(",", "");
				heartBeatErrorMap.put("re_id", re_id);
				heartBeatErrorMap.put("re_time", re_time);
				heartBeatErrorMap.put("response_time", response_time);
				// request:
				String requestPhase = heartBeatError.trim().split("={57}")[1].split("-{57}")[0].trim()
						.replace("request:", "").trim();
				Document documentRequest = DocumentHelper.parseText(requestPhase.toString());
				Element rootRequest = documentRequest.getRootElement();
				heartBeatErrorMap.putAll(listNodes(rootRequest, heartBeatErrorMap));
				// response:
				String responsePhase = heartBeatError.trim().split("={57}")[1].split("-{57}")[1].trim()
						.replace("response:", "").trim();
				Document documentResponse = DocumentHelper.parseText(responsePhase.toString());
				Element rootResponse = documentResponse.getRootElement();
				for (String line : responsePhase.split("\n")) {
					if (line.contains("Response>")) {
						String operation_name = line.trim().replace(">", "").replace("<", "").replace("/", "")
								.replace("Response", "");
						heartBeatErrorMap.put("operation_name", operation_name);
						heartBeatErrorMap.put("business_type", "HeartBeat");
					}
				}
				heartBeatErrorMap.putAll(listNodes(rootResponse, heartBeatErrorMap));
				heartBeatErrorAnalysedList.add(heartBeatErrorMap);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("This heartbeat log cannot be analysed:::");
				logger.info(heartBeatError);
			}

		}
		errorInfoMap.put("normalError", normalErrorAnalysedList);
		errorInfoMap.put("heartBeatError", heartBeatErrorAnalysedList);
		return errorInfoMap;
	}
//	/**
//	 * This method is only used in every current period.Recovery BOSS Data KPI is not support.
//	 * @param soapLogSb
//	 * @param errorLogSb
//	 */
//	@Deprecated
//	public static List<Map<String, Object>> getCurrentPeriodBossKpi(StringBuilder soapLogSb, StringBuilder errorLogSb,int hlrsn) {
//		List<Map<String, Object>> bossMinuteKpiList = new ArrayList<>();
//		Set<String> oparationNameSet = LoadStaticData.getBUSINESS_TYPE_MAP().keySet();
//		for (String operationName : oparationNameSet) {
//			Map<String, Object> singleData = new HashMap<>();
//			singleData.put("operationName", operationName);
//			singleData.put("periodTime", DateUtils.getPeriodTimeString(new Date()));
//			singleData.put("hlrsn",hlrsn);
//			int totalCount = 0;
//			int failCount = 0;
//			
//			//SOAP LOG COUNTER
//			for (String soapData : soapLogSb.toString().split(";;;")) {
//				if (soapData.contains("\"" + operationName + "\"")) {
//					totalCount++;
//				}
//				singleData.put("totalCount", totalCount);
//			}
//			//ERROR LOG COUNTER
//			for (String errorData : errorLogSb.toString().split(";;;")) {
//				if (errorData.contains("\"" + operationName + "\"")) {
//					failCount++;
//				}
//				singleData.put("failCount", failCount);
//			}
//			bossMinuteKpiList.add(singleData);
//			try {
//				String successRate = (1.0-(double)failCount/(double)totalCount)*100.0+"%";
//				singleData.put("successRate", successRate);
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.info("Calculation ERROR!");
//			} finally{
//				String successRate = "ERROR";
//				singleData.put("successRate", successRate);
//			}
//		}
//		return bossMinuteKpiList;
//	}
}
