package com.nokia.boss.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.util.StringUtils;

import com.nokia.boss.settings.CustomSettings;
import com.nokia.boss.task.LoadStaticData;

public class AnalyseUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalyseUtils.class);
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	private static String FIELD_TERMINATOR = LoadStaticData.getFieldTerminator();
	private static String LINE_TERMINATOR = LoadStaticData.getLineTerminator();
	/**
	 * 把GZ文件转换成普通的文本文件类，便于解析。
	 * @param gzPath GZ包的完整路径
	 * @param soapGwName SOAPGW名称
	 * @return
	 * @throws IOException
	 */

	public static File gzToFile(File gzFile,String soapGwName) throws IOException{
		String gzFileName = gzFile.getName();
		BufferedReader br = new BufferedReader(
				new InputStreamReader(new GZIPInputStream(new FileInputStream(gzFile.getAbsolutePath())), "utf-8"));
		String line = null;
		StringBuilder gzText = new StringBuilder();
		while ((line = br.readLine()) != null) {
			gzText.append(line+"\n");
		}
		br.close();
		String tempTransformFileAbsPath = CustomSettings.getCacheDataDir()+soapGwName+"_"+gzFileName.replace(".gz", ".trans");
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
		for (Map<String, Object> errorAnalysedMap : errorAnalysedDataList) {
			try {
				errorLoader.append(errorAnalysedMap.getOrDefault("task_id", "Boss program error!")+FIELD_TERMINATOR);
				errorLoader.append(errorAnalysedMap.getOrDefault("response_time", "Boss program error!")+FIELD_TERMINATOR);
				errorLoader.append(errorAnalysedMap.getOrDefault("user_name", "Boss program error!")+FIELD_TERMINATOR);
				errorLoader.append(errorAnalysedMap.getOrDefault("user_password", "Boss program error!")+FIELD_TERMINATOR);
				errorLoader.append(errorAnalysedMap.getOrDefault("imsi", null)+FIELD_TERMINATOR);
				errorLoader.append(errorAnalysedMap.getOrDefault("msisdn", null)+FIELD_TERMINATOR);
				errorLoader.append(errorAnalysedMap.getOrDefault("hlrsn", null)+FIELD_TERMINATOR);
				errorLoader.append(errorAnalysedMap.getOrDefault("business_type", "Boss program error!")+FIELD_TERMINATOR);
				errorLoader.append(errorAnalysedMap.getOrDefault("operation_name", "Boss program error!")+FIELD_TERMINATOR);
				errorLoader.append(errorAnalysedMap.getOrDefault("error_code", "Boss program error!")+FIELD_TERMINATOR);
				errorLoader.append(errorAnalysedMap.getOrDefault("error_message", "Boss program error!")+LINE_TERMINATOR);
			} catch (Exception e) {
				LOGGER.info("cannot concat this error log piece:");
				LOGGER.info(errorAnalysedMap.toString());
				e.printStackTrace();
			}
      
		}
		errorLoaderMap.put("error", errorLoader);
		for (Map<String, Object> errorAnalysedMap : errorAnalysedDataList) {
			try {
				errorJoinLoader.append(errorAnalysedMap.get("task_id")+FIELD_TERMINATOR+errorAnalysedMap.get("err_log")+FIELD_TERMINATOR+""+errorAnalysedMap.get("response_time")+LINE_TERMINATOR);
			} catch (Exception e) {
				LOGGER.info("cannot concat this error join log");
				LOGGER.info(errorAnalysedMap.toString());
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
			newFileDir = CustomSettings.getLoadFileDir().replace("#soap-gw-name#", soapGwName) + sdf.format(new Date()) + fileName + ".loadertemp";
		}
		LOGGER.info("Write loader:"+newFileDir);
		File loadFile = new File(newFileDir);
		loadFile.createNewFile();
		if (loadFile.exists()) {
			FileWriter fw = new FileWriter(loadFile);
			fw.write(loader.toString());
			fw.close();
			return loadFile;
		} else {
			LOGGER.info("cannot create new load file!");
			return null;
		}
	}

	/**
	 * 把SOAP日志解析为两种字符串，都是以LOAD SQL的格式写好。
	 * @param originalText
	 * @return
	 */

	public static Map<String, Object> originalDataTransformer(StringBuilder originalText,boolean hlrsnTransform) {
		
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
		LOGGER.info("Soap data size:"+successDataList.size());
		LOGGER.info("State data size:"+stateMap.size());
		for (String dataLine : successDataList) {
			String dataLine_ = new String(dataLine);
			try {
				/**
				 * Split 80M text file To increase the efficiency of the split.
				 * Choose to use this method instead of "string.split" local
				 * test: Use string.split : near 10s. Use indexOf : near 1s.
				 */
				int splitFlag = 0;
				String responseTime = null;
				String user = null;
				String taskId = null;
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
							responseTime = splitStr;
						case 1:
							user = splitStr;
						case 2:
							taskId = splitStr;
						case 3:
							restInfo = splitStr;
						}
						if(splitFlag==2){
							restInfo = dataLine;
							break;
						}
						splitFlag++;
				}
				String responseTimeWithNoMilliseconds = responseTime.substring(0, 19);;
				user = user.replace("User: ", "");
				taskId = taskId.replace("id:", "").trim();
				/**
				 * Analyze "JSON" in the rest info.
				 */
				String MSISDN = getJsonValue(restInfo, LoadStaticData.getMSISDN_PATTERN());
				String HLRSN = getJsonValue(restInfo, LoadStaticData.getHLRSN_PATTERN());
				if(hlrsnTransform==true){
					//浙江规则开始
					if(HLRSN.startsWith("5")){
						HLRSN = "35";
					}else if(HLRSN.startsWith("6")){
						HLRSN = "36";
					}else if(HLRSN.startsWith("7")){
						HLRSN = "37";
					}else if(HLRSN.startsWith("8")){
						HLRSN = "38";
					}
					//浙江规则结束
					
					//河南规则开始
					
					//河南规则结束
				}
				String IMSI = getJsonValue(restInfo, LoadStaticData.getIMSI_PATTERN());
				String IMPU = getJsonValue(restInfo, LoadStaticData.getIMPU_PATTERN());
				String operationName = getJsonValue(restInfo, LoadStaticData.getOPERATION_NAME_PATTERN());
				String businessType = LoadStaticData.getBusinessType(operationName);
				
				
				if (IMSI.equals("") && MSISDN.equals("")) {
					if (IMPU.contains("+86")) {
						MSISDN = IMPU.substring(5, 18);
					}
					if (IMPU.contains("46000")) {
						IMSI = IMPU.substring(4, 19);
					} 
				}
				long delay = -1;
				String endTime = stateMap.get(taskId);
				if(endTime!=null){
					String startTime = responseTime;
						delay = delayTimeCalculator(startTime, endTime);
				}else{
					delay = 0;
				}
				loadDataStringBuilder.append(
											user + FIELD_TERMINATOR + taskId + FIELD_TERMINATOR + responseTimeWithNoMilliseconds + FIELD_TERMINATOR 
											+ HLRSN + FIELD_TERMINATOR+ MSISDN + FIELD_TERMINATOR + IMSI + FIELD_TERMINATOR
											+ operationName + FIELD_TERMINATOR + businessType + FIELD_TERMINATOR + delay 
											+ LINE_TERMINATOR);
				
				loadJoinStringBuilder.append(taskId + FIELD_TERMINATOR + dataLine_+FIELD_TERMINATOR+responseTimeWithNoMilliseconds + LINE_TERMINATOR);
			} catch (Exception e) {
				LOGGER.info("ANALYZING ERROR(10001):");
				LOGGER.info("Original data:"+dataLine_);
				debugLogger("ANALYZING ERROR(10001):");
				debugLogger("Original data:"+dataLine_);
			}
		}
		Map<String, Object> transformerMap = new HashMap<String, Object>();
		transformerMap.put("boss_soap", loadDataStringBuilder);
		transformerMap.put("boss_join", loadJoinStringBuilder);
		return transformerMap;
	}
	public static Map<String, Object> originalDataTransformerUnicom(StringBuilder originalText) {
		StringBuilder loadDataStringBuilder = new StringBuilder();
		StringBuilder loadJoinStringBuilder = new StringBuilder();
		Map<String,String> taskIdAndHlrsnMap = new HashMap<>();
		String[] originDataLines = originalText.toString().split("\n");
		List<String> successDataList = new ArrayList<String>();
		Map<String,String> stateMap = new HashMap<>();
		for (String string : originDataLines) {
			if (string.contains("Callback")) {
				stateMap.put(string.split("\\|")[1].trim().replace("User: ", ""),string.substring(0,23));
			} else {
				successDataList.add(string);
			}
		}
		LOGGER.info("Soap data size:"+successDataList.size());
		LOGGER.info("State data size:"+stateMap.size());
		for (String dataLine : successDataList){
			String dataLine_ = new String(dataLine);
			try {
				int splitFlag = 0;
				String responseTime = null;
				String restInfo = null;
				while(true){
					String splitStr = null;
					int j = dataLine.indexOf("|");
					if (j < 0)
						break;
					splitStr = dataLine.substring(0, j);
					dataLine = dataLine.substring(j + 1);
					switch (splitFlag) {
					case 0:
						responseTime = splitStr;
					case 3:
						restInfo = splitStr;
					}
					if(splitFlag==2){
						restInfo = dataLine;
						break;
					}
					splitFlag++;
				}
				String responseTimeWithNoMilliseconds = responseTime.substring(0, 19);
				String MSISDN = getJsonValue(restInfo, LoadStaticData.getMSISDN_PATTERN_CUC());
				String HLRSN = getJsonValue(restInfo, LoadStaticData.getHLRSN_PATTERN());
				String IMSI = getJsonValue(restInfo, LoadStaticData.getIMSI_PATTERN());
				String IMPU = getJsonValue(restInfo, LoadStaticData.getIMPU_PATTERN());
				String operation = getJsonValue(restInfo, LoadStaticData.getOPERATION_PATTERN());//联通只有operation 没有operation name
				String businessType = LoadStaticData.getBusinessType(operation);
				String taskId = getJsonValue(restInfo,LoadStaticData.getTASK_ID_CUC());
				String userName = getJsonValue(restInfo,LoadStaticData.getUSER_CUC());
				taskIdAndHlrsnMap.put(taskId, HLRSN);
				if (IMSI.equals("") && MSISDN.equals("")) {
					if (IMPU.contains("+86")) {
						MSISDN = IMPU.substring(5, 18);
					}
					if (IMPU.contains("46000")) {
						IMSI = IMPU.substring(4, 19);
					} 
				}
				long delay = 0;
				String endTime = stateMap.get(taskId);
				if(endTime!=null){
					String startTime = responseTime;
					delay = delayTimeCalculator(startTime, endTime);
				}
				loadDataStringBuilder.append(
						userName + FIELD_TERMINATOR + taskId + FIELD_TERMINATOR + responseTimeWithNoMilliseconds + FIELD_TERMINATOR 
						+ HLRSN + FIELD_TERMINATOR+ MSISDN + FIELD_TERMINATOR + IMSI + FIELD_TERMINATOR
						+ operation + FIELD_TERMINATOR + businessType + FIELD_TERMINATOR + delay 
						+ LINE_TERMINATOR);

				loadJoinStringBuilder.append(taskId + FIELD_TERMINATOR + dataLine_ +FIELD_TERMINATOR+responseTimeWithNoMilliseconds+ LINE_TERMINATOR);
			} catch (Exception e) {
				LOGGER.info("ANALYZING ERROR(20001):");
				LOGGER.info("can not ananlysis this soap line:");
				LOGGER.info(dataLine_);
				debugLogger("can not ananlysis this soap line:");
				debugLogger(dataLine_);
				e.printStackTrace();
			}
		}
		Map<String, Object> transformerMap = new HashMap<String, Object>();
		transformerMap.put("boss_soap_cuc", loadDataStringBuilder);
		transformerMap.put("boss_join", loadJoinStringBuilder);
		transformerMap.put("taskIdAndHlrsnMap", taskIdAndHlrsnMap);
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
		Date startTime = SDF.parse(startReTime);
		Date endTime = SDF.parse(endReTime);
		delayTime = endTime.getTime()-startTime.getTime();
		return delayTime;
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
		int startFlag = restInfo.indexOf(KEY_PATTERN);
		if(startFlag!=-1){
			for (int i = (restInfo.indexOf(KEY_PATTERN) + KEY_PATTERN.length()); i < tempCharArray.length; i++) {
				if (tempCharArray[i] == '"') {
					endFlag = i;
					break;
				}
			}
			return restInfo.substring(restInfo.indexOf(KEY_PATTERN) + KEY_PATTERN.length(), endFlag);
		}else{
			return "";
		}
	}
	/**
	 * 解析ERROR XML标签的值，解析后添加到errorInfoMap
	 * @param node
	 * @param errorInfoMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> listNodes(Element node, Map<String, Object> errorInfoMap,boolean hlrsnTransform) {

		if (!(node.getTextTrim().equals(""))) {
			if (node.getName().equalsIgnoreCase("PassWord")) {
				errorInfoMap.put("user_password", node.getText());
			}
			if (node.getName().equalsIgnoreCase("UserName")) {
				errorInfoMap.put("user_name", node.getText());
			}
			if (node.getName().equalsIgnoreCase("HLRSN")) {
				String hlrsn = node.getText();
				if(hlrsnTransform==true){
					if(hlrsn.startsWith("5")){
						hlrsn = "35";
					}else if(hlrsn.startsWith("6")){
						hlrsn = "36";
					}else if(hlrsn.startsWith("7")){
						hlrsn = "37";
					}else if(hlrsn.startsWith("8")){
						hlrsn = "38";
					}
				}
				errorInfoMap.put("hlrsn", hlrsn);
			}
			if (node.getName().equalsIgnoreCase("IMSI")) {
				errorInfoMap.put("imsi", node.getText());
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
			listNodes(e, errorInfoMap,hlrsnTransform);
		}
		return errorInfoMap;
	}
	/**
	 * 解析ERROR日志
	 * @author Pei Nan
	 * @param errorLog. 完整的ERROR日志
	 * @return errorInfoMap.
	 * @throws DocumentException
	 * @throws IOException 
	 */
	public static Map<String, List<Map<String, Object>>> getErrorInfoMap(StringBuilder errorLog,boolean hlrsnTransform)
			throws DocumentException, IOException {
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
		LOGGER.info("Normal error:" + normalErrorListOriginal.size());
		LOGGER.info("HeartBeat error:" + heartBeatErrorListOriginal.size());
		LOGGER.info("All size:" + errorLogPieces.length);
		// 2 types of error log.
		List<Map<String, Object>> normalErrorAnalysedList = new ArrayList<>();
		for (String normalError : normalErrorListOriginal) {
			try {
				Map<String, Object> normalErrorMap = new HashMap<>();
				normalErrorMap.put("err_log", normalError.trim());
				String task_id = normalError.trim().split("={57}")[0].substring(36).trim();
				String response_time_withMilliseconds = normalError.trim().split("={57}")[0].split(" ")[0] + " "
						+ normalError.trim().split("={57}")[0].split(" ")[1];
				String response_time_withNoMilliseconds = response_time_withMilliseconds.split(",")[0];
				normalErrorMap.put("task_id", task_id);
				normalErrorMap.put("response_time", response_time_withNoMilliseconds);
				// request:
				String requestPhase = normalError.trim().split("={57}")[1].split("-{57}")[0].trim().replace("request:", "")
						.trim();
				Document documentRequest = DocumentHelper.parseText(requestPhase.toString());
				Element rootRequest = documentRequest.getRootElement();
				normalErrorMap.putAll(listNodes(rootRequest, normalErrorMap,hlrsnTransform));
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
						normalErrorMap.put("business_type", LoadStaticData.getBusinessType(operation_name));
					}
				}
				normalErrorMap.putAll(listNodes(rootResponse, normalErrorMap,hlrsnTransform));
				normalErrorAnalysedList.add(normalErrorMap);
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.info(e.getMessage());
				LOGGER.info("ANALYSE ERROR:");
				LOGGER.info(normalError);
				debugLogger("This log cannot be analysed:");
				debugLogger(normalError);
			}

		}
//
//		List<Map<String, Object>> heartBeatErrorAnalysedList = new ArrayList<>();
//		for (String heartBeatError : heartBeatErrorListOriginal) {
//			try {
//				Map<String, Object> heartBeatErrorMap = new HashMap<>();
//				heartBeatErrorMap.put("operation_name", "HeartBeat");
//				heartBeatErrorMap.put("err_log", heartBeatError.trim());
//				String re_id = heartBeatError.trim().split("={57}")[0].split(" ")[6].trim();
//				String re_time = heartBeatError.trim().split("={57}")[0].split(" ")[0] + " "
//						+ heartBeatError.trim().split("={57}")[0].split(" ")[1];
//				String response_time = re_time.replace("-", "").replace(":", "").replace(" ", "").replace(",", "");
//				heartBeatErrorMap.put("re_id", re_id);
//				heartBeatErrorMap.put("re_time", re_time);
//				heartBeatErrorMap.put("response_time", response_time);
//				// request:
//				String requestPhase = heartBeatError.trim().split("={57}")[1].split("-{57}")[0].trim()
//						.replace("request:", "").trim();
//				Document documentRequest = DocumentHelper.parseText(requestPhase.toString());
//				Element rootRequest = documentRequest.getRootElement();
//				heartBeatErrorMap.putAll(listNodes(rootRequest, heartBeatErrorMap));
//				// response:
//				String responsePhase = heartBeatError.trim().split("={57}")[1].split("-{57}")[1].trim()
//						.replace("response:", "").trim();
//				Document documentResponse = DocumentHelper.parseText(responsePhase.toString());
//				Element rootResponse = documentResponse.getRootElement();
//				for (String line : responsePhase.split("\n")) {
//					if (line.contains("Response>")) {
//						String operation_name = line.trim().replace(">", "").replace("<", "").replace("/", "")
//								.replace("Response", "");
//						heartBeatErrorMap.put("operation_name", operation_name);
//						heartBeatErrorMap.put("business_type", "HeartBeat");
//					}
//				}
//				heartBeatErrorMap.putAll(listNodes(rootResponse, heartBeatErrorMap));
//				heartBeatErrorAnalysedList.add(heartBeatErrorMap);
//			} catch (Exception e) {
//				e.printStackTrace();
//				LOGGER.info("ANALYSE ERROR:");
//				LOGGER.info(heartBeatError);
//				debugLogger("This heartbeat log cannot be analysed:");
//				debugLogger(heartBeatError);
//			}
//
//		}
		errorInfoMap.put("normalError", normalErrorAnalysedList);
//		errorInfoMap.put("heartBeatError", heartBeatErrorAnalysedList);
		return errorInfoMap;
	}
	/**
	 * 书写自定义DEBUG日志
	 * @param logInfo
	 * @throws IOException
	 */
	public static void debugLogger(String logInfo){
		try {
			File debugLog = new File(CustomSettings.getCacheDataDir().replace("cache/", "")+"debug.log");
			FileWriter fw = new FileWriter(debugLog,true);//持续写入
			fw.write(SDF.format(new Date())+":---------->"+logInfo+"\n");
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取解析目标文件。
	 * 如果不是压缩文件，直接取文件。
	 * 如果是GZ压缩文件，先转换为文本文件，放在./cache下缓存。
	 * @param targetLogFileName
	 * @param soapGwName
	 * @param targetFileDir ABS PATH
	 * @return
	 * @throws IOException
	 */
	public static File extractAnalysisTarget(File targetFile, String soapGwName)
			throws IOException {
		File targetDataFile;
		if(!targetFile.getName().contains(".gz")){
			LOGGER.info(targetFile.getAbsolutePath()+" is not a gz file.Return itself");
			targetDataFile = new File(targetFile.getAbsolutePath());
		}else{
			LOGGER.info(targetFile.getAbsolutePath()+" is a gz file.Need transform");
			targetDataFile = AnalyseUtils.gzToFile(targetFile, soapGwName);
			LOGGER.info(targetFile.getAbsolutePath()+"has been transformed as:"+targetDataFile.getAbsolutePath());
		}
		return targetDataFile;
	}
	public static Map<String, Object> getErrAnalysisTarget(File errFile, int startLine) throws IOException {
		int endLine = checkErrCompleteLines(errFile);//应该取到这行
		LOGGER.info("Analysis target:"+errFile.getAbsolutePath());
		LOGGER.info("Start line of the file is: "+startLine);
		LOGGER.info("Last complete line of the file is: "+endLine);
		Map<String, Object> m = new HashMap<>();
		if(endLine==1){//一块完整的都没有
			m.put("targetText", new StringBuilder());
			m.put("endLine", 0);
			return m;
		}
		FileReader fr = new FileReader(errFile);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		StringBuilder errLog = new StringBuilder();
		int i = 0;
		while ((line = br.readLine())!=null) {
			if(i>=startLine && i<endLine){
				errLog.append(line+"\n");
			}
			i++;
		}
		br.close();
		m.put("targetText", new StringBuilder(errLog.toString().trim()));
		m.put("endLine", endLine);
		return m;
	}
	/**
	 * 取SOAP日志时，startLine参数是上一个周期的endLine。
	 * endLine那行对应的行，应该是最后一行完整的数据。
	 * 如果startLine=0,说明MARK是被重置过的，取完整的文件即可（保证最后一行文本完整，最后一行完整文本的行数为endLine,作为startLine来更新MARK）
	 * @param soapLog 原始文件
	 * @param startLine 开始行。包括此行的数据。
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> getSoapLogAnalysisTarget(File soapLogFile, int startLine) throws IOException {
		FileReader fr = new FileReader(soapLogFile);
		BufferedReader br = new BufferedReader(fr);
		int allCompleteLines = checkSoapCompleteLines(soapLogFile);//应该取出最后一个完整行。
		LOGGER.info("Analysis target:"+soapLogFile.getAbsolutePath());
		LOGGER.info("Start line of the file is: "+startLine);
		LOGGER.info("Last complete line of the file is: "+allCompleteLines);
		StringBuilder soapLog = new StringBuilder();
		String lineText = null;
		int i = 0;
		while ((lineText = br.readLine())!=null) {
			i++;
			if(i > startLine && i <= allCompleteLines){
				soapLog.append(lineText+"\n");
			}
		}
		br.close();
		fr.close();
		Map<String, Object> m = new HashMap<>();
		m.put("targetText", new StringBuilder(soapLog.toString().trim()));
		m.put("endLine", allCompleteLines);
		return m;
	}
	public static int checkErrCompleteLines(File errFile) throws IOException{
		FileReader fr = new FileReader(errFile);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		//ERR LOG里，完整的一段是以57个加号结束的
		int allCompleteLines = checkSoapCompleteLines(errFile);//同理，认为每个换行符出现，就说明这行已经写完
		String[] allLines = new String[allCompleteLines];
		int i = 0;
		while ((line = br.readLine())!=null) {
			if(i<=allCompleteLines-1){
				allLines[i] = line+"\n";
				i++;
				
			}else{
				break;
			}
		}
		br.close();
		//从最后一行找五十七个加号
		int lineNumber = 0;
		for(int j = allLines.length-1;j>=0;j--){
			if(allLines[j].contains("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++")){
				lineNumber = j;
				break;
			}
		}
		return lineNumber+1;
	}
	public static int checkSoapCompleteLines(File soapFile) throws IOException {
		InputStream in = null;
		int lines = 0;
		try {
			in = new FileInputStream(soapFile);
			byte[] data = new byte[in.available()];
			in.read(data);
			String s = new String(data);
			lines = StringUtils.countOccurrencesOf(s, "\n");
			//每有一个换行符，就认为有完整的一行
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}
	public static Map<String, StringBuilder> errCaseCucLogAnalyser(StringBuilder errCaseUnicomTarget,Map<String,String> TASKID_AND_HLRSN_MAP) throws ParseException, IOException{
		int hlrsnMatched = 0;
		String[] singleBlocks = errCaseUnicomTarget.toString().split("\\+{57}");
		List<String> requestBlankErrCaseList = new ArrayList<>();
		List<String> normalErrList = new ArrayList<>();
		for (String singleBlock : singleBlocks) {
				singleBlock = singleBlock.replaceAll("\\+", "");
				String request = singleBlock.split("={57}")[1].split("-{57}")[0].trim().replace("request:", "").trim();
				if(request.equals("//;")||(!request.contains(":"))){
					requestBlankErrCaseList.add(singleBlock);
				}else if(singleBlock.contains("INCOMPLETE")){
					requestBlankErrCaseList.add(singleBlock);
				}
				else{
					normalErrList.add(singleBlock);
				}
		}
		LOGGER.info("Normal error size:"+normalErrList.size());
		LOGGER.info("Blank error size:"+requestBlankErrCaseList.size());
		StringBuilder errCaseCucAndTempLoader = new StringBuilder();
		StringBuilder cucErrJoinLoader = new StringBuilder();
		for (String normalErr : normalErrList) {
			try {
				String title = normalErr.split("={57}")[0].trim();
				String request = normalErr.split("={57}")[1].split("-{57}")[0].trim().replace("request:", "").trim();
				String response = normalErr.split("-{57}")[1].trim().replace("response:", "").trim();
				if(response.contains("INCOMPLETE")){
					debugLogger("INCOMPLETE COMMAND:");
					debugLogger(normalErr);
					continue;
				}
				String responseTimeWithNoMillisecond = title.split(",")[0];
				String taskId = title.substring(36);
				String errorCode = response.split("\n")[0].replaceAll("\\*", "").replaceAll("/", "").trim().replace("DX ERROR:", "").trim();
				String errorMessage = response.split("\n")[1].replaceAll("\\*", "").replaceAll("/", "").trim();
				String operation = request.split(":")[0];
				String mmlRestStr = request.split(":")[1];
				String MSISDN = "0";
				String IMSI = "0";
				if(mmlRestStr.contains("IMSI")){
					IMSI = mmlRestStr.replace("IMSI=", "").replace(";", "");
				}else if(mmlRestStr.contains("MSISDN")){
					MSISDN = mmlRestStr.replace("MSISDN=", "").replace(";", "");
				}else{
					debugLogger(mmlRestStr+": is neither MSISDN nor IMSI");
					debugLogger("Whole command:");
					debugLogger(normalErr);
				}
				String businessType = LoadStaticData.getBusinessType(operation);
				String hlrsn = TASKID_AND_HLRSN_MAP.get(taskId);
				if(hlrsn==null){
					hlrsn = "1";
				}else{
					hlrsnMatched++;
				}
				cucErrJoinLoader.append(taskId+FIELD_TERMINATOR+normalErr+FIELD_TERMINATOR+responseTimeWithNoMillisecond+LINE_TERMINATOR);
				errCaseCucAndTempLoader.append(taskId+FIELD_TERMINATOR+responseTimeWithNoMillisecond+FIELD_TERMINATOR+mmlRestStr+FIELD_TERMINATOR
												+IMSI+FIELD_TERMINATOR+MSISDN+FIELD_TERMINATOR+hlrsn+FIELD_TERMINATOR+businessType+FIELD_TERMINATOR
												+operation+FIELD_TERMINATOR+errorCode+FIELD_TERMINATOR+errorMessage+LINE_TERMINATOR);
			} catch (Exception e) {
				e.printStackTrace();
				debugLogger("Err case analyzing error:");
				debugLogger(normalErr);
			}
			
		}
		Map<String,StringBuilder> cucErrDataMap = new HashMap<>();
		cucErrDataMap.put("err_case_cuc", errCaseCucAndTempLoader);
		cucErrDataMap.put("err_join", cucErrJoinLoader);
		LOGGER.info("Normal error size:"+normalErrList.size());
		LOGGER.info("Normal error loader size:"+errCaseCucAndTempLoader.length());
		LOGGER.info("Join loader size:"+cucErrJoinLoader.length());
		LOGGER.info("HLRSN MATCHED IN THE ERROR LOG:"+hlrsnMatched);
		return cucErrDataMap;
	}

}
