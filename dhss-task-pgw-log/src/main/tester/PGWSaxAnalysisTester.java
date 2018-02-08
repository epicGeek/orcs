/*package main.tester;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class PGWSaxAnalysisTester {
	static int modifyCounter = 0;
	static int addCounter = 0;
	static int deleteCounter = 0;
	static int extendedCounter = 0;
	public static void main(String[] args) throws IOException, DocumentException {
		File f = new File("E:/pgwdata/beginner.txt");
		StringBuilder sb = new StringBuilder();
		FileReader fr = new FileReader(f);
		BufferedReader br  = new BufferedReader(fr);
		String line = null;
		while ((line=br.readLine())!=null) {
			sb.append(line);
		}
		Document document = null;
		br.close();
        document = DocumentHelper.parseText(sb.toString());
        // 格式化输出格式
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        StringWriter writer = new StringWriter();
        // 格式化输出流
        XMLWriter xmlWriter = new XMLWriter(writer, format);
        // 将document写入到输出流
        xmlWriter.write(document);
        xmlWriter.close();
        System.out.println(writer.toString());
//		Long startTime = System.currentTimeMillis();
//		System.out.println("Start to analysis:");
//		File f = new File("E:/pgwdata/provgw-spml_command.log.2016_12_09-01_00_15_722");
//		//按行解析
//		FileReader fr = new FileReader(f);
//		BufferedReader br = new BufferedReader(fr);
//		String line = null;
//		int response = 0;
//		int request = 0;
//		int others = 0;
//		StringBuilder sb = new StringBuilder();
//		List<Map<String,String>> resultList =new ArrayList<>();
//		while ((line=br.readLine())!=null) {
//			if(line.endsWith("Response>")){
//				response++;
//				Map<String,String> dataMap = new HashMap<>();
//				dataMap = analysisLine(line);
//				resultList.add(dataMap);
//			}
//			else if(line.endsWith("Request>")){
//				//不用解析REQUEST
//				request++;
//			}
//			else{
//				others++;
//			}
//		}
//		br.close();
//		System.out.println("============overall req&res counts:================");
//		System.out.println("request count                      :"+request);
//		System.out.println("response count                     :"+response);
//		System.out.println("others count                       :"+others);
//		System.out.println("all count(request+response+others) :"+(request+response+others));
//		System.out.println("============response detail counts:================");
//		System.out.println("modifyCounter                      :"+modifyCounter);
//		System.out.println("addCounter                         :"+addCounter);
//		System.out.println("deleteCounter                      :"+deleteCounter);
//		System.out.println("extendedCounter                    :"+extendedCounter);
//		System.out.println("all response(mod+add+ext+del)      :"+(modifyCounter+addCounter+deleteCounter+extendedCounter));
//		System.out.println("===================================================");
//		Long endTime = System.currentTimeMillis();
//		System.out.println("Use time:"+(endTime-startTime)/1000.0+"s");
		
	}

	private static Map<String,String> analysisLine(String requestDataLine) {
		String headerInfo = requestDataLine.substring(0,requestDataLine.indexOf("<"));
		String xmlInfo = requestDataLine.substring(headerInfo.length());
		Map<String,String> dataMap = new HashMap<>();
		dataMap.putAll(analyseHeaderInfo(headerInfo));
		dataMap.putAll(analysexmlInfo(xmlInfo));
		return dataMap;
	}
	//spml:modifyResponse,addResponse,deleteResponse,extendedResponse
	private static Map<String, String> analysexmlInfo(String xmlInfo) {
		Map<String,String> xmlDataMap = new HashMap<>();
		Document doc = null;  
		try {
				if(xmlInfo.startsWith("<spml:modifyResponse")){
					xmlDataMap.put("response_type", "modifyResponse");
					doc = DocumentHelper.parseText(xmlInfo); // 将字符串转为XML  
			        Element rootElement = doc.getRootElement(); // 获取根节点  
		            xmlDataMap.put("request_id", rootElement.attributeValue("requestID"));
		            String executeResult = rootElement.attributeValue("result");
		            String executionTime = rootElement.attributeValue("executionTime");
		            xmlDataMap.put("execution_time", executionTime);
		            String errorCode = "";
		            String errorType = "";
		            String errorMessage = "";
		            xmlDataMap.put("execute_result", executeResult);
		            if(executeResult.equals("failure")){
		            	errorCode = rootElement.attributeValue("errorCode");
		            	errorType = rootElement.attributeValue("errorType");	
		            	errorMessage = rootElement.element("errorMessage").getStringValue();
		            }
		            xmlDataMap.put("error_code", errorCode);
		            xmlDataMap.put("error_type", errorType);
		            xmlDataMap.put("error_message", errorMessage);
		            String imsi = rootElement.element("identifier").getStringValue();
		            if(imsi==null){
		            	imsi = rootElement.element("imsi").getStringValue();
		            }
		            if(imsi==null){
		            	imsi = "0";
		            }
		            xmlDataMap.put("imsi", imsi);
		            String operationName = rootElement.element("modification").attributeValue("operation");
		            xmlDataMap.put("operation_name", operationName);
					modifyCounter++;
				}
				if(xmlInfo.startsWith("<spml:addResponse")){
					doc = DocumentHelper.parseText(xmlInfo); // 将字符串转为XML  
					Element rootElement = doc.getRootElement(); // 获取根节点  
					xmlDataMap.put("request_id", rootElement.attributeValue("requestID"));
					
					addCounter++;
				}
				if(xmlInfo.startsWith("<spml:deleteResponse")){
					deleteCounter++;
				}
				if(xmlInfo.startsWith("<spml:extendedResponse")){
					extendedCounter++;
				}
		} catch (Exception e) {
			System.out.println("cannot analyse this xml:");
			System.out.println(xmlInfo);
			e.printStackTrace();
		}
       
		return xmlDataMap;
	}

	private static Map<String, String> analyseHeaderInfo(String headerInfo) {
		Map<String,String> headerDataMap = new HashMap<>();
		String responseTime = headerInfo.substring(0,19);//no millisecond
		headerDataMap.put("response_time", responseTime);
		String afterResponseTime = headerInfo.substring(24).trim();
		String pgwName = afterResponseTime.substring(0, afterResponseTime.indexOf(" "));
		headerDataMap.put("pgw_name", pgwName);
		String afterPgwName = afterResponseTime.substring(pgwName.length()).trim();
		String instanceName = afterPgwName.substring(0, afterPgwName.indexOf(" "));
		headerDataMap.put("instance_name", instanceName);
		String afterInstanceName = afterPgwName.substring(instanceName.length()).trim();
		String userName = afterInstanceName.substring(0, afterInstanceName.indexOf(" "));
		headerDataMap.put("user_name", userName);
		//String defaultString = afterInstanceName.substring(userName.length()).trim(); 这个字段不知道有啥用
		return headerDataMap;
	}
	
}
*/