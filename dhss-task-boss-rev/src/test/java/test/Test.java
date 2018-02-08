package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import com.nokia.boss.task.LoadStaticData;

public class Test {

	public static void main(String[] args) throws IOException, ScriptException {
		File f = new File("E:/ZMQE.txt");
		FileReader fr = new FileReader(f);
		String line = null;
		BufferedReader br = new BufferedReader(fr);
		StringBuilder sb = new StringBuilder();
		while(null!=(line=br.readLine())){
			sb.append(line); 
		}
		br.close();
		System.out.println(sb);
		originalDataTransformerUnicom(sb);
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
				//	delay = delayTimeCalculator(startTime, endTime);
				}
//				loadDataStringBuilder.append(
//						userName + FIELD_TERMINATOR + taskId + FIELD_TERMINATOR + responseTimeWithNoMilliseconds + FIELD_TERMINATOR 
//						+ HLRSN + FIELD_TERMINATOR+ MSISDN + FIELD_TERMINATOR + IMSI + FIELD_TERMINATOR
//						+ operation + FIELD_TERMINATOR + businessType + FIELD_TERMINATOR + delay 
//						+ LINE_TERMINATOR);

		//		loadJoinStringBuilder.append(taskId + FIELD_TERMINATOR + dataLine_ + LINE_TERMINATOR);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Map<String, Object> transformerMap = new HashMap<String, Object>();
		transformerMap.put("boss_soap_cuc", loadDataStringBuilder);
		transformerMap.put("boss_join", loadJoinStringBuilder);
		transformerMap.put("taskIdAndHlrsnMap", taskIdAndHlrsnMap);
		return transformerMap;
	}
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

}
