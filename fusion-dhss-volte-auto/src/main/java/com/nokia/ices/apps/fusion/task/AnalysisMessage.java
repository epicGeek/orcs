package com.nokia.ices.apps.fusion.task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nokia.ices.apps.fusion.CustomSettings;
import com.nokia.ices.apps.fusion.alarm.repository.AlarmMonitorRepository;

@Component
@EnableScheduling
public class AnalysisMessage {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private AlarmMonitorRepository alarmMonitorRepository;
	private static final Logger logger = LoggerFactory.getLogger(AnalysisMessage.class);
	private static final SimpleDateFormat formatWithSlash = new SimpleDateFormat("yyyy/MM/dd/HH/mm");

	@Scheduled(cron = "0 0/1 * * * ?")
	public void analysisVolteMessage()  {
		logger.info("handling volte counter");
		logger.info("start to analysis volte data");
		// 1.在目标目录里面选择最新的TXT文本
		File dataDirFile = new File(CustomSettings.getDataFileDir());
		String[] allFileNames = dataDirFile.list();
		List<String> targetFilesTxt = new ArrayList<String>();
		List<String> targetFilesXml = new ArrayList<String>();
		for (String fileName : allFileNames) {
			// logger.info(fileName);
			// 2.解析最新的文件
			String[] fileNameElement = fileName.split("-");
			if (fileNameElement.length == 3 && fileName.endsWith(".txt")) {
				// 文件名格式： 数据流向-设备名-生成时间.txt hss2boss-soap50-20160909.txt(未解析)
				// 文件名格式： 数据流向-设备名-生成时间-finished.txt
				// hss2boss-soap50-20160909-finished.txt(已解析)
				logger.info(fileName + " is a new txt");
				targetFilesTxt.add(fileName);
			}
			if (fileName.endsWith(".xml")) {
				logger.info(fileName + " is a new xml");
				targetFilesXml.add(fileName);
			}
		}
		
		if (targetFilesTxt.size() == 0 && targetFilesXml.size() == 0) {
			logger.info("no data need to be analysed");
		}else{
			if (targetFilesTxt.size() != 0) {
				logger.info("txt number:" + targetFilesTxt.size());
					analysisTxt(targetFilesTxt);
			}
			if (targetFilesXml.size() != 0) {
				logger.info("xml number:" + targetFilesXml.size());
					analysisXml(targetFilesXml);
			}
		}
		try {
			moveAnalysedFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void analysisTxt(List<String> targetFiles) {
		String encoder = "UTF-8";
		StringBuilder fileData = new StringBuilder();
		for (String fileName : targetFiles) {
			try {
				logger.info(CustomSettings.getDataFileDir() + fileName + " need to be analysed");
				// 开始解析
				// 1.解析文件名
				String[] fileNameElements = fileName.split("-");
				String flowDirection = fileNameElements[0];
				String equipmentName = fileNameElements[1];
				// 文件名里面的生成时间，暂时没想到有啥用
				File targetFile = new File(CustomSettings.getDataFileDir() + fileName);
				InputStreamReader read = new InputStreamReader(new FileInputStream(targetFile), encoder);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				// hss2boss,boss2hss 格式不一样 分开解析
				if (flowDirection.equalsIgnoreCase("boss2hss")) {
					Long startTime = System.currentTimeMillis();
					while ((lineTxt = bufferedReader.readLine()) != null) {
						if (lineTxt.startsWith("460")) {
							// 原始报文：IMSI,MSISDN,Status(0:succ,1:failed),Starttime,Costtime(ms),autoProv
							// 460001851211232|null|0|2016-10-12 01:21:52 489|114|0
							// 后面拼上：数据流向，设备名
							// IMSI,MSISDN,Status(0:succ,1:failed),Starttime,Costtime(ms),autoProv,flowDirection,neName
							// 460001234567890|13812345678|0|2016-08-15 16:54:52
							// 620|128|1|hss2boss|soap50
							fileData.append(lineTxt);
							fileData.append('|');
							fileData.append(flowDirection);
							fileData.append('|');
							fileData.append(equipmentName);
							fileData.append('\n');
						}
					}
					bufferedReader.close();
					// 写文件
					FileWriter fw = new FileWriter(CustomSettings.getDataFileDir() + fileName + "load");
					BufferedWriter output = new BufferedWriter(fw);
					logger.info("start to write in " + CustomSettings.getDataFileDir() + fileName + "load");
					output.write(fileData.toString());
					fileData.delete(0, fileData.length());
					output.close();
					logger.info("write in finished");
					// 写好之后LOAD这个文件
					String tempDir = CustomSettings.getDataFileDir();
					String loadFileName = tempDir + fileName + "load";
					String sql = CustomSettings.getLoadSql();
					sql = sql.replaceAll("#loadFileName#", "\'" + loadFileName + "\'");
					logger.info("execute sql:" + sql);
					jdbcTemplate.execute(sql);
					// 解析前的文件要改名，表明已经LOAD完成
					targetFile.renameTo(
							new File(CustomSettings.getDataFileDir() + fileName.replaceAll(".txt", "-finished.txt")));
					Long endTime = System.currentTimeMillis();
					Double useTime = (Double.valueOf(endTime - startTime)) / 1000.0;
					logger.info("use time:" + useTime + "s");
				}
				if (flowDirection.equalsIgnoreCase("hss2boss")) {
					Long startTime = System.currentTimeMillis();
					while ((lineTxt = bufferedReader.readLine()) != null) {
						if (lineTxt.startsWith("460")) {
							// 原始报文：IMSI,MSISDN,Status(0:succ,1:failed),Starttime,Costtime(ms)
							// 460020000000001|86184000000002|0|2016-10-12 00:35:54
							// 911|754
							// 后面拼上：数据流向，设备名
							// IMSI,MSISDN,Status(0:succ,1:failed),Starttime,Costtime(ms),flowDirection,neName
							// 460020000000001|86184000000002|0|2016-10-12 00:35:54
							// 911|754
							// 620|128|1|hss2boss|soap50
							fileData.append(lineTxt);
							fileData.append('|');
							fileData.append(flowDirection);
							fileData.append('|');
							fileData.append(equipmentName);
							fileData.append('\n');
						}
					}
					bufferedReader.close();
					// 写文件
					FileWriter fw = new FileWriter(CustomSettings.getDataFileDir() + fileName + "load");
					BufferedWriter output = new BufferedWriter(fw);
					logger.info("start to write in " + CustomSettings.getDataFileDir() + fileName + "load");
					output.write(fileData.toString());
					fileData.delete(0, fileData.length());
					output.close();
					logger.info("write in finished");
					// 写好之后LOAD这个文件
					String tempDir = CustomSettings.getDataFileDir();
					String loadFileName = tempDir + fileName + "load";
					String sql = CustomSettings.getLoadSqlHss2Boss();
					sql = sql.replaceAll("#loadFileName#", "\'" + loadFileName + "\'");
					logger.info("execute sql:" + sql);
					jdbcTemplate.execute(sql);
					targetFile.renameTo(
							new File(CustomSettings.getDataFileDir() + fileName.replaceAll(".txt", "-finished.txt")));
					// 解析前的文件要改名，表明已经LOAD完成
					Long endTime = System.currentTimeMillis();
					Double useTime = (Double.valueOf(endTime - startTime)) / 1000.0;
					logger.info("use time:" + useTime + "s");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void analysisXml(List<String> targetFiles)
			 {
		List<Map<String,Object>> xmlDataList = new ArrayList<>();
		for (String fileName : targetFiles) {
			try {
				logger.info(CustomSettings.getDataFileDir() + fileName + " need to be analysed");
				File targetFile = new File(CustomSettings.getDataFileDir() + fileName);
				// step 1: 获得SAX解析器工厂实例
				SAXParserFactory factory = SAXParserFactory.newInstance();
				// step 2: 获得SAX解析器实例
				SAXParser parser = factory.newSAXParser();

				// step 3: 开始进行解析
				// 传入待解析的文档的处理器
				MySAXHandler mySAXHandler = new MySAXHandler(jdbcTemplate,alarmMonitorRepository);
				parser.parse(new File(CustomSettings.getDataFileDir() + fileName),mySAXHandler);
				Map<String,Object> xmlInsertData = mySAXHandler.getSingleXmlDataRecord();
				File xmlBak = new File(CustomSettings.getDataFileDir() + "/xmlBak" + File.separator
						+ formatWithSlash.format(new Date()) + File.separator);
				if(!xmlBak.exists()){
					xmlBak.mkdirs();
				}
				String backupXmlPath = xmlBak.getAbsolutePath()+File.separator+targetFile.getName();
				targetFile.renameTo(new File(backupXmlPath));
				xmlInsertData.put("file_abs_dir", backupXmlPath);
				xmlDataList.add(xmlInsertData);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		String sql = CustomSettings.getInsertCounterDataSql();
		for (Map<String, Object> xmlDataMap : xmlDataList) {
			System.out.println("INSERT TO DB xml:");
			System.out.println(xmlDataMap.toString());
			namedParameterJdbcTemplate.update(sql, xmlDataMap);
		}
	}

	public void moveAnalysedFiles() {
		String fileDir = CustomSettings.getDataFileDir();
		File allFiles = new File(fileDir);
		File txtLoad = new File(CustomSettings.getDataFileDir() + "/txtLoad" + File.separator
				+ formatWithSlash.format(new Date()) + File.separator);
		File txtFinished = new File(CustomSettings.getDataFileDir() + "/txtFinished" + File.separator
				+ formatWithSlash.format(new Date()) + File.separator);
		String[] allFileNames = allFiles.list();
		boolean hasTxtAndXml = false;
		for (String fileName : allFileNames) {
			if(fileName.contains(".txt") ||fileName.contains(".xml")){
				hasTxtAndXml = true;
				break;
			}
		}
		if (!hasTxtAndXml){
			return;
		}
		if (!txtLoad.exists()) {
			txtLoad.mkdirs();
		}
		if (!txtFinished.exists()) {
			txtFinished.mkdirs();
		}
		for (String fileName : allFileNames) {

			if (fileName.endsWith(".txtload")) {
				File thisFile = new File(fileDir + fileName);
				File toDirFile = new File(txtLoad.getPath() + "/"+fileName);
				thisFile.renameTo(toDirFile);
			}
			if (fileName.contains("finish")) {
				File thisFile = new File(fileDir + fileName);
				File toDirFile = new File(txtFinished.getPath() +"/"+ fileName);
				thisFile.renameTo(toDirFile);
			}
		}

	}

}
