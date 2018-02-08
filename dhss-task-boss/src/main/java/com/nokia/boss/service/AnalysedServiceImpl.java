package com.nokia.boss.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.nokia.boss.settings.CustomSettings;
import com.nokia.boss.task.LoadStaticData;
import com.nokia.boss.util.AnalyseUtils;
/**
 * 
 * 
 * @author Pei Nan
 * 
 * successDataAnalysisForLog :
 */
@Component
public class AnalysedServiceImpl implements AnalysedSerivice{ 
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	private static final Logger logger = LoggerFactory.getLogger(AnalysedServiceImpl.class);
	/**
	 * 解析未被压缩的SOAP日志的方法
	 */
	@Override
	public void successDataAnalysisForLog(String successLogFileName, String soapGwName) throws IOException {
		//1.find mark file
		String markFilePath = CustomSettings.getMarkLogFile().replace("#soap-gw-name#", soapGwName)+successLogFileName+".mark";
		File markFile = new File(markFilePath);
		logger.info("soap name:"+soapGwName);
		logger.info("supposed mark file name:"+markFile.getName());
		logger.info("mark file abs path:"+markFile.getAbsolutePath());
		//2.mark file name:soapLogName.mark.For example: BOSS_SOAP_Agent_main......mark
		if(!markFile.exists()){
			logger.info("Mark file:"+markFile.getName()+" doesn't exist.Analysis all the file.");
			markFile.createNewFile();
			FileWriter fw = new FileWriter(markFile);
			fw.write("0");
			fw.close();
			int startLine = 0;
			logger.info("Analysis all log.");
			soapLogAnalysisStream(successLogFileName, soapGwName, markFile,startLine);
		}else{
			//if mark is found.
			logger.info("Mark file:"+markFile.getName()+" found");
			FileReader fr = new FileReader(markFile);
			BufferedReader br = new BufferedReader(fr);
			Integer numberLine = Integer.valueOf(br.readLine());
			br.close();
			logger.info("Analysis this log from the"+numberLine+" line.");
			soapLogAnalysisStream(successLogFileName, soapGwName, markFile,numberLine);
		}
	}
	/**
	 * 解析被压缩的SOAP日志的方法
	 */
	@Override
	public void successDataAnalysisForGz(String successGzFileName, String soapGwName) throws IOException {
		//1.find the mark
		logger.info("Gz file name:"+successGzFileName);//String successGzFileName "backup/successGzFileName"
		String gzPureName = successGzFileName.replace(".gz", "");
		String markFilePath = CustomSettings.getMarkLogFile().replace("#soap-gw-name#", soapGwName)+gzPureName+".mark";
		File markFile = new File(markFilePath.replace("/backup", ""));
		logger.info("soap name:"+soapGwName);
		logger.info("supposed mark file name:"+markFile.getName());
		logger.info("mark file abs path:"+markFile.getAbsolutePath());
		String gzPath = CustomSettings.getRsyncDataDir().replace("#soap-gw-name#", soapGwName)+successGzFileName;
		logger.info("This gz file abs path:"+gzPath);
		File analysableSoapGzFile = AnalyseUtils.gzToFile(gzPath, soapGwName);
		if(!markFile.exists()){
			logger.info("Mark file:"+markFile.getName()+" doesn't exist.Analysis all the file.");
			markFile.createNewFile();
			FileWriter fw = new FileWriter(markFile);
			fw.write("0");
			fw.close();
			int startLine = 0;
			soapLogAnalysisStream(analysableSoapGzFile.getName(), soapGwName, markFile, startLine);
		}else{
			logger.info("Mark file:"+markFile.getName()+" found");
			FileReader fr = new FileReader(markFile);
			BufferedReader br = new BufferedReader(fr);
			Integer numberLine = Integer.valueOf(br.readLine());
			br.close();
			logger.info("Analysis this log from the:"+numberLine+" line.");
			soapLogAnalysisStream(analysableSoapGzFile.getName(), soapGwName, markFile,numberLine);
		}
		
	}
	/**
	 * 解析未被压缩的ERROR日志的方法
	 */
	@Override
	public void failureDataAnalysisForLog(String failureLogFileName, String soapGwName) throws IOException, DocumentException {
		//1.find the mark file.
		String markFilePath = CustomSettings.getMarkLogFile().replace("#soap-gw-name#",soapGwName)+"fail.log";
		File failMarkFile = new File(markFilePath);
		String bossErrCaseLogPath = CustomSettings.getRsyncDataDir().replace("#soap-gw-name#", soapGwName)+"BOSS_ERR_CASE.log";
		File bossErrCaseLog = new File(bossErrCaseLogPath);
		if(!failMarkFile.exists()){
			logger.info("No fail mark file found.Analysis all error log.");
			failMarkFile.createNewFile();
			FileWriter fw = new FileWriter(failMarkFile);
			fw.write("0");
			fw.close();
			int startLine = 0 ;
			errCaseLogAnalysisStream(failureLogFileName, soapGwName,failMarkFile, bossErrCaseLog, startLine);
		}else{
			//if mark is found.
			logger.info("Mark file:"+failMarkFile.getName()+" found");
			FileReader fr = new FileReader(failMarkFile);
			BufferedReader br = new BufferedReader(fr);
			Integer numberLine = Integer.valueOf(br.readLine());
			br.close();
			logger.info("Analysis this log from the"+numberLine+" line.");
			errCaseLogAnalysisStream(failureLogFileName, soapGwName, failMarkFile, bossErrCaseLog, numberLine);
		}
	}
	/**
	 * 解析被压缩的ERROR日志方法
	 */
	@Override
	public void failureDataAnalysisForGz(String failureGzFileName, String soapGwName) throws IOException, DocumentException{
		String failureGzFileNamePure = failureGzFileName;//failureGzFileName may contains file path.
		//Sometimes fail GZ file may exist in '/var/log/NPM/' for a while.Later it may moved in the directory '/var/log/NPM/backup/'
		if(failureGzFileNamePure.contains("backup")){
			failureGzFileNamePure = failureGzFileNamePure.replace("backup", "").replace("/", "");
		}
		File failGzFileNameRecorder = new File(CustomSettings.getMarkLogFile().replace("#soap-gw-name#", soapGwName)+"gz_file_name.record");
		if(!failGzFileNameRecorder.exists()){
			failGzFileNameRecorder.createNewFile();
		}
		StringBuilder analysedFailGzFileNames = (StringBuilder)AnalyseUtils.getAnalysisTarget(failGzFileNameRecorder, 0).get("targetText");
		if(analysedFailGzFileNames.toString().contains(failureGzFileNamePure)){
			logger.info(failureGzFileNamePure+" has been analysed.It's not necessary to analysis it again.");
			
		}else{
			logger.info(failureGzFileNamePure+" is never analysed.Start to analysis");
			logger.info("Gz file name:"+failureGzFileNamePure);//String successGzFileName "backup/successGzFileName"
			logger.info("soap name:"+soapGwName);
			String gzPath = CustomSettings.getRsyncDataDir().replace("#soap-gw-name#", soapGwName)+failureGzFileName;
			File failMark = new File(CustomSettings.getMarkLogFile().replace("#soap-gw-name#", soapGwName)+"fail.log");
			//transform GZ file to normal file type.
			File bossErrCaseLogTrans = AnalyseUtils.gzToFile(gzPath, soapGwName);
			if(!failMark.exists()){
				//if mark file doesn't exists,analysis all GZ file.
				logger.info("Fail mark:"+failMark.getAbsolutePath()+" is not existed.Create one and analysis all gz.");
				int startLine = 0;
				errCaseLogAnalysisStream(failureGzFileNamePure, soapGwName,failMark, bossErrCaseLogTrans, startLine);
				FileWriter fw = new FileWriter(failGzFileNameRecorder,true);
				fw.write(failureGzFileNamePure+";");
				fw.close();
				bossErrCaseLogTrans.delete();
			}else{
				logger.info("Fail mark file:"+failMark.getName()+" found");
				FileReader fr = new FileReader(failMark);
				BufferedReader br = new BufferedReader(fr);
				Integer numberLine = Integer.valueOf(br.readLine());
				br.close();
				logger.info("Analysis this log from the:"+numberLine+" line.");
				errCaseLogAnalysisStream(failureGzFileNamePure, soapGwName, failMark, bossErrCaseLogTrans, numberLine);
				FileWriter fww = new FileWriter(failGzFileNameRecorder,true);
				fww.write(failureGzFileNamePure+"\n");
				fww.close();
				bossErrCaseLogTrans.delete();
			}
		}
	}
	/**
	 * SOAP日志解析逻辑流程
	 * @param successLogFileName
	 * @param soapGwName
	 * @param markFile
	 * @param startLine
	 * @throws IOException
	 */
	private void soapLogAnalysisStream(String successLogFileName,String soapGwName,File markFile,int startLine) throws IOException{
		String originalBossSoapLogDir = "";
		if(successLogFileName.contains(".trans")){
			originalBossSoapLogDir = CustomSettings.getTransformDir()+successLogFileName;
			logger.info("Soap log analysis stream target:"+originalBossSoapLogDir);
		}else{
			
			originalBossSoapLogDir = CustomSettings.getRsyncDataDir().replace("#soap-gw-name#", soapGwName)+successLogFileName;
			logger.info("Soap log analysis stream target:"+originalBossSoapLogDir);
		}
		File originalBossSoapLog = new File(originalBossSoapLogDir);
		logger.info("Start to analysis:"+originalBossSoapLog.getAbsolutePath());
		//start to analysis
		Map<String,Object> targetInfoMap = new HashMap<>();
		targetInfoMap = AnalyseUtils.getAnalysisTarget(originalBossSoapLog, startLine);
		StringBuilder originalText = (StringBuilder)targetInfoMap.get("targetText");
		int endLine = (int)targetInfoMap.get("endLine");
		//write load data
		StringBuilder loadBossSoapData = AnalyseUtils.originalDataTransformer(originalText).get("boss_soap");
		StringBuilder loadBossJoinData = AnalyseUtils.originalDataTransformer(originalText).get("boss_join");
		File bossSoapLoadFile = AnalyseUtils.writeLoadFile(loadBossSoapData, successLogFileName, soapGwName, "data");
		File bossJoinLoadFile = AnalyseUtils.writeLoadFile(loadBossJoinData, successLogFileName, soapGwName, "join");
		//load to data base
		String LoadSoapSQL = LoadStaticData.getLOAD_SOAP_DATA_SQL().replace("loadFileDir", bossSoapLoadFile.getAbsolutePath());
		String LoadSoapTempSQL = LoadSoapSQL.replace("boss_soap", "boss_soap_temp");
		logger.info("Load Soap data SQL:"+LoadSoapSQL);
		jdbcTemplate.execute(LoadSoapSQL);
		String LoadJoinSQL =  LoadStaticData.getLOAD_JOIN_SQL().replace("loadFileDir", bossJoinLoadFile.getAbsolutePath());
		logger.info("Load Soap JOIN data SQL:"+LoadJoinSQL);
		jdbcTemplate.execute(LoadJoinSQL);
		logger.info("Load Soap temp table:"+LoadSoapTempSQL);
		jdbcTemplate.execute(LoadSoapTempSQL);
		logger.info("Load soap data and its join data completed.");
		// update mark
		if(successLogFileName.contains("trans")){
			markFile.delete();
		}else{
			FileWriter markWriter = new FileWriter(markFile);
			markWriter.write(String.valueOf(endLine));
			markWriter.close();
		}
		//bossSoapLoadFile.delete();
		//bossJoinLoadFile.delete();
	}
	/**
	 * ERROR日志的解析逻辑流程
	 * @param failureLogFileName
	 * @param soapGwName
	 * @param markFile
	 * @param bossErrCaseLog
	 * @param startLine
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void errCaseLogAnalysisStream(String failureLogFileName, String soapGwName,File markFile, File bossErrCaseLog,
			int startLine) throws IOException, DocumentException {
		Map<String, Object> failInfoMap = new HashMap<>();
		failInfoMap = AnalyseUtils.getAnalysisTarget(bossErrCaseLog, startLine);
		Integer endLine = (Integer)AnalyseUtils.getAnalysisTarget(bossErrCaseLog, startLine).get("endLine");
		StringBuilder targetText = (StringBuilder)failInfoMap.get("targetText");
		Map<String, List<Map<String, Object>>> failAnalysedDataMap = new HashMap<>();
		failAnalysedDataMap = AnalyseUtils.getErrorInfoMap(targetText);
		List<Map<String, Object>> normalErrorAnalysedData = failAnalysedDataMap.get("normalError");
		List<Map<String, Object>> heartBeatErrorAnalysedData = failAnalysedDataMap.get("heartBeatError");
		String normalErrorLoader = AnalyseUtils.errorAnalysedDataListToLoader(normalErrorAnalysedData).get("error").toString();
		String heartBeatErrorLoader = AnalyseUtils.errorAnalysedDataListToLoader(heartBeatErrorAnalysedData).get("error").toString();
		String normalErrorJoinLoader =  AnalyseUtils.errorAnalysedDataListToLoader(normalErrorAnalysedData).get("join").toString();
		String heartBeatErrorJoinLoader =  AnalyseUtils.errorAnalysedDataListToLoader(heartBeatErrorAnalysedData).get("join").toString();
		String errorLoader = normalErrorLoader+heartBeatErrorLoader;
		String errorJoinLoader = normalErrorJoinLoader+heartBeatErrorJoinLoader;
		StringBuilder errorLoaderSb = new StringBuilder(errorLoader);
		StringBuilder errorJoinLoaderSb = new StringBuilder(errorJoinLoader);
		File errorLoaderFile = AnalyseUtils.writeLoadFile(errorLoaderSb, failureLogFileName, soapGwName, "err");
		File errorJoinLoaderFile = AnalyseUtils.writeLoadFile(errorJoinLoaderSb, failureLogFileName, soapGwName, "join");
		File errorTempLoaderFile = AnalyseUtils.writeLoadFile(new StringBuilder(normalErrorLoader), failureLogFileName, soapGwName, "errtemp");
		String loadErrSQL = LoadStaticData.getLOAD_ERR_DATA_SQL().replace("loadFileDir", errorLoaderFile.getAbsolutePath());
		String loadErrJoinSQL = LoadStaticData.getLOAD_JOIN_SQL().replace("loadFileDir", errorJoinLoaderFile.getAbsolutePath());
		String loadNormalErrorToTempTableSQL = LoadStaticData.getLOAD_ERR_DATA_SQL().replace("loadFileDir", errorTempLoaderFile.getAbsolutePath()).replace("boss_err_case", "boss_err_case_temp");
		logger.info("Load Error data SQL:"+loadErrSQL);
		jdbcTemplate.execute(loadErrSQL);
		logger.info("Load Error join SQL:"+loadErrJoinSQL);
		jdbcTemplate.execute(loadErrJoinSQL);
		logger.info("Load Error to temp table:"+loadNormalErrorToTempTableSQL);
		jdbcTemplate.execute(loadNormalErrorToTempTableSQL);
		if(bossErrCaseLog.getName().contains("trans")){
			markFile.delete();
		}else{
			FileWriter markWriter = new FileWriter(markFile);
			markWriter.write(String.valueOf(endLine));
			markWriter.close();
		}
	}
	@Override
	public void successDataAnalysisForLogUnicom(String successLogFileName, String soapGwName) throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void successDataAnalysisForGzUnicom(String successGzFileName, String soapGwName) throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void failureDataAnalysisForLogUnicom(String failureLogFileName, String soapGwName)
			throws IOException, DocumentException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void failureDataAnalysisForGzUnicom(String failureGzFileName, String soapGwName)
			throws IOException, DocumentException {
		// TODO Auto-generated method stub
		
	}
}
