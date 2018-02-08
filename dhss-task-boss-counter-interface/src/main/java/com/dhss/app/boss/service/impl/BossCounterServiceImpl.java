package com.dhss.app.boss.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.dhss.app.boss.config.BossCounterConfig;
import com.dhss.app.boss.domain.BossCounter;
import com.dhss.app.boss.domain.BossCounterMarker;
import com.dhss.app.boss.repo.BossCounterMarkerRepository;
import com.dhss.app.boss.repo.BossCounterRepository;
import com.dhss.app.boss.service.BossCounterService;
import com.dhss.app.boss.task.util.GzUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class BossCounterServiceImpl implements BossCounterService{
	
	private static Logger LOGGER = LoggerFactory.getLogger(BossCounterServiceImpl.class);
	private static SimpleDateFormat sdfFile = new SimpleDateFormat("yyyy/MM/dd/");
	private static SimpleDateFormat sdfFileName = new SimpleDateFormat("yyyyMMddHH");
	private static String spliter = "==================================================================================================================\n";
	private static String HLRSN_PATTERN = "\"HLRSN\":\"";
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	BossCounterRepository bossCounterRepository;
	@Autowired
	BossCounterMarkerRepository bossCounterMarkerRepository;
	@Autowired
	BossCounterConfig bossCounterConfig;
	@Override
	public void outPutJsonFile(Map<String, Integer> dataMap, DateTime startTime, DateTime endTime) throws IOException {
		String fileName = "BOSS_DHSS_counter_"+startTime.toString("yyyyMMddHHmm")+".txt";
		String outPutFileDir = bossCounterConfig.getJsonFileOutPutDir()+sdfFile.format(new Date());
		File outputFileDirF = new File(outPutFileDir);
		
		if(!outputFileDirF.exists()){
			outputFileDirF.mkdirs();
		}
		File outputFile = new File(outPutFileDir+fileName);
		if(!outputFile.exists()){
			outputFile.createNewFile();
		}
		Date starTimeDate = startTime.toDate();
		Date endTimeDate = endTime.toDate();
		LOGGER.info("Data write in file:"+outputFile.getAbsolutePath());
		LOGGER.info("New counter data:");
		Set<String> keySet = dataMap.keySet();
		for (String hlrsn : keySet) {
			BossCounter bc = new BossCounter();
			Integer count = dataMap.get(hlrsn);
			bc.setCounter(count);
			bc.setHlrsn(hlrsn);
			bc.setCounterStartTime(starTimeDate);
			bc.setCounterEndTime(endTimeDate);
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(bc)+"\n";
			FileUtils.write(outputFile,json,true);
			bossCounterRepository.save(bc);
		}
		FileUtils.write(outputFile,spliter,true);
	}
	@Override
	public Map<String, BossCounterMarker> checkMarker(Map<String,List<File>> soapOrderListMap) {
		Set<String> soapSet = soapOrderListMap.keySet();
		//获取同步顺序
		//先判断是否有对应的marker.如果没有，找出最新的文件，补marker.
		Map<String, BossCounterMarker> m = new HashMap<>();
		for (String soapGwName : soapSet) {
			BossCounterMarker bcm = bossCounterMarkerRepository.findBySoapGwName(soapGwName);
			if(bcm==null){
				//未找到这台SOAP日志解析记录，从最早的日志开始找,并补充好条目。
				LOGGER.info("****************************************");
				LOGGER.info("Mark for "+soapGwName+" is NOT found!!");
				maintainMarker(soapOrderListMap.get(soapGwName));
				BossCounterMarker bcmNew = bossCounterMarkerRepository.findBySoapGwName(soapGwName);
				m.put(soapGwName, bcmNew);
				LOGGER.info("Mark for "+soapGwName+" is created.");
				LOGGER.info("Start line is :"+bcmNew.getStartLine());
				LOGGER.info("File: "+bcmNew.getFileAbsPath());
				LOGGER.info("****************************************");
			}else{
				//找到上次解析的位置
				LOGGER.info("=========================================");
				LOGGER.info("Mark for "+soapGwName+" is found as:"+bcm.getStartLine());
				LOGGER.info("File: "+bcm.getFileAbsPath());
				LOGGER.info("=========================================");
				m.put(soapGwName, bcm);
			}
			
		}
		return m;
	}
	private void maintainMarker(List<File> soapFiles) {
		// TODO Auto-generated method stub
		
	}
	@Override
	/**
	 * 把rsync-data下的所有soap文件copy到workspace,再删掉多余文件
	 * @param rsyncDataDir
	 * @param counterWorkSpace
	 * @throws IOException 
	 */
	public void copyRsyncDataToWorkspace(String rsyncDataDir, String counterWorkSpace) throws IOException {
		LOGGER.info("Workspace dir is :");
		LOGGER.info(counterWorkSpace);
		File counterWorkSpaceDir = new File(counterWorkSpace);
		if(!counterWorkSpaceDir.exists()){
			counterWorkSpaceDir.mkdirs();
			LOGGER.info("workspace is not existed.mkdirs");
		}
		LOGGER.info("Start to copy files to workspace");
		Long start = System.currentTimeMillis();
		FileUtils.copyDirectoryToDirectory(new File(rsyncDataDir), counterWorkSpaceDir);
		Long end = System.currentTimeMillis();
		LOGGER.info("Copy completed.Use time:"+(end-start)+"ms");
		
	}
	@Override
	public Map<String,List<File>> getAnalysisOrder() {
		Map<String,List<File>> m = new HashMap<>();
		File workspaceDir = new File(bossCounterConfig.getCounterWorkSpaceDir()+"/"+"rsync-data");
		File[] listFiles = workspaceDir.listFiles();
		for (File soapDir : listFiles) {
			List<File> soapList = new ArrayList<>();
			if(soapDir.isDirectory()){
				LOGGER.info("soapName:"+soapDir.getName());
				Collection<File> soapFiles = FileUtils.listFiles(soapDir, null, true);
				Iterator<File> it = soapFiles.iterator();
				while(it.hasNext()){
					File f = it.next();
					if(f.getName().contains("BOSS_SOAP_Agent_BOSSA_main")){
						soapList.add(f);
					}
				}
				m.put(soapDir.getName(), soapList);
			}
		}
		
		Set<String> s = m.keySet();
		for (String string : s) {
			LOGGER.info("Order for "+string);
			List<File> l = m.get(string);
			for (File file : l) {
				LOGGER.info(file.getAbsolutePath());
			}
		}
		return m;
	}
	@Override
	public Map<String, File> generateAnalysisTarget(Map<String, List<File>> soapOrderListMap,DateTime startTime, DateTime endTime) throws IOException {

		Set<String> soapSet = soapOrderListMap.keySet();
		Map<String, File> m = new HashMap<>();
		LOGGER.info("~~~~~~~~~~~~~~~~~~~~~~~");
		LOGGER.info("Time scope is:");
		LOGGER.info(startTime.toString("yyyy-MM-dd HH:mm:ss")+" -> "+endTime.toString("yyyy-MM-dd HH:mm:ss"));
		LOGGER.info("~~~~~~~~~~~~~~~~~~~~~~~");
		for (String soapName : soapSet) {
			LOGGER.info("*****************");
			LOGGER.info("Generating target for "+soapName);
			List<File> fileList = soapOrderListMap.get(soapName);
			File latestFile = fileList.get(fileList.size()-1);
			File secondFile = fileList.get(fileList.size()-2);
			LOGGER.info("Latest is "+latestFile.getAbsolutePath());
			LOGGER.info("Second latest is "+secondFile.getAbsolutePath());
			Long start = System.currentTimeMillis();
			File target = combineSoapFiles(soapName,latestFile,secondFile);
			Long end = System.currentTimeMillis();
			LOGGER.info("Done.Use "+(end - start)+"ms");
			List<String> lines = FileUtils.readLines(target);
			LOGGER.info("################################");
			LOGGER.info("Picking lines for "+soapName);
			List<String> targetLines = new ArrayList<>();
			for (String line : lines) {
				if(line.contains("{\"HLRSN\"")){
					DateTime lineTime = lineStartTime(line,DATETIME_FORMAT);
					if(isBetweenTimeScope(startTime, endTime, lineTime)){
						targetLines.add(line);
					}	
				}
				
			}
			File realTarget = writeTarget(soapName,targetLines);
			m.put(soapName, realTarget);
			LOGGER.info("################################");
			LOGGER.info("*****************");
		}
		return m;
	}
	
	
	private File writeTarget(String soapName, List<String> targetLines) {
		File target = new File(bossCounterConfig.getCounterWorkSpaceDir()+"/cache/"+soapName+".rl");
		try {
			FileUtils.writeLines(target, targetLines);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return target;
	}
	private File combineSoapFiles(String soapName,File latestFile, File secondFile) throws IOException {
		if(latestFile.getName().contains(".gz")){
			latestFile = GzUtils.gzToFile(latestFile);
		}
		if(secondFile.getName().contains(".gz")){
			secondFile = GzUtils.gzToFile(secondFile);
		}
		String cacheDirPath = bossCounterConfig.getCounterWorkSpaceDir()+"/"+"cache";
		File cacheDir = new File(cacheDirPath);
		if(!cacheDir.exists()){
			cacheDir.mkdirs();
		}
		File target = new File(cacheDir.getAbsolutePath()+"/"+soapName);
		List<String> lines = FileUtils.readLines(latestFile);
		FileUtils.writeLines(target, lines, true);
		lines = FileUtils.readLines(secondFile);
		FileUtils.writeLines(target, lines, true);
		lines = null ;
		LOGGER.info("Target as :");
		LOGGER.info(target.getAbsolutePath());
		return target;
	}
	private DateTime lineStartTime(String line, DateTimeFormatter format){
		String[] ele = line.split(" ");
		try {
			String lineStartTime = ele[0]+" "+ele[1];
			return DateTime.parse(lineStartTime,format);
		} catch (Exception e) {
			LOGGER.info(line + "is not illegal");
			return new DateTime().minusYears(10);
		}
	}
	private boolean isBetweenTimeScope(DateTime startDateTime,DateTime endDateTime,DateTime targetTime){
		if(targetTime.isAfter(startDateTime) && endDateTime.isAfter(targetTime)){
			return true;
		}else{
			return false;
		}
	}
	@Override
	public Map<String, Integer> analysisData(Map<String, File> analysisTargetMap) {
		LOGGER.info("*******************************");
		Set<String> s = analysisTargetMap.keySet();
		Map<String, Integer> dataMap = new LinkedHashMap<>();
		for (String soapName : s) {
			LOGGER.info("analysis for "+soapName);
			LOGGER.info("File for "+soapName +":"+analysisTargetMap.get(soapName).getAbsolutePath());
			File f = analysisTargetMap.get(soapName);
			try {
				List<String> lines = FileUtils.readLines(f);
				for (String line : lines) {
					String hlrsn = getJsonValue(line.split("\\|")[3],HLRSN_PATTERN);
					if(!dataMap.containsKey(hlrsn)){
						dataMap.put(hlrsn, new Integer(0));
					}else{
						Integer number = dataMap.get(hlrsn);
						number++;
						dataMap.put(hlrsn, number);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LOGGER.info("Start to analysis data:");
		LOGGER.info("*******************************");
		System.out.println(dataMap);
		return dataMap;
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
			LOGGER.info("restInfo:"+restInfo);
			LOGGER.info("KEY_PATTERN:"+KEY_PATTERN);
			return "NUL-JV";
		}
	}
}
