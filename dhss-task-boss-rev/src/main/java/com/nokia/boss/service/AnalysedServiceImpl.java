package com.nokia.boss.service;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.nokia.boss.task.LoadStaticData;
import com.nokia.boss.util.AnalyseUtils;
/**
 * 
 * 
 * @author Pei Nan
 * 解析BOSS日志实现类
 */
@Component
public class AnalysedServiceImpl implements AnalysedSerivice{ 
	@Autowired
	JdbcTemplate jdbcTemplate;
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalysedServiceImpl.class);
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Map<String,String> TASKID_AND_HLRSN_MAP = new HashMap<>();
	@Value("${dhss.boss.boss-version}")
	private String bossVersion ;
	@Value("${dhss.boss.hlrsn-transform}")
	private boolean hlrsnTransform;
	@Override
	public void soapLogDataAnalysis(File soapLogFile,String soapGwName) throws IOException {//successLogFileName 变化的文件名，是相对路径
		File successDataFile = AnalyseUtils.extractAnalysisTarget(soapLogFile, soapGwName);//获取解析目标
		int startLine = getStartLine("success",soapGwName,soapLogFile.getName());//startLine-从日志文件的第几行开始解析
		Map<String, Object> analysisTarget = AnalyseUtils.getSoapLogAnalysisTarget(successDataFile, startLine);//根据起始行，获取需要解析解析日志文本
		int endLine = (int)analysisTarget.get("endLine");//获取结束行，作为下一个周期的起始行。
		if(startLine>endLine){
			LOGGER.info("Mark error.Reset this mark to 0");
			startLine = 0;
			String sql = "Update soap_mark set start_line = 0 where soap_name = ?";
			jdbcTemplate.update(sql,soapGwName);
			analysisTarget = AnalyseUtils.getSoapLogAnalysisTarget(successDataFile, startLine);
		}
		StringBuilder targetText = (StringBuilder)analysisTarget.get("targetText");
		if(targetText.length()==0){
			LOGGER.info("NOT ENOUGH SOAP LOG INFO,EXIT PROGRAM..");
			DecimalFormat df = new DecimalFormat("##.##");
			LOGGER.info("File name:"+soapLogFile.getAbsolutePath());
			LOGGER.info("File size:"+df.format(soapLogFile.length()/(1024.0*1024.0))+"Mb");
			return;
		}
		
		Map<String, Object> soapAnalysedInfo = AnalyseUtils.originalDataTransformer(targetText,hlrsnTransform);//把原始日志转换为LOAD数据
		loadSoapDataToDB(soapGwName, soapLogFile.getName(), soapAnalysedInfo,bossVersion);//数据入库
		afterLoad("success", soapLogFile.getName(), successDataFile, soapGwName, endLine);//入库后的处理动作
	}
	@Override
	public void errLogDataAnalysis(File errLogFile,String soapGwName)
			throws IOException, DocumentException {
		File failureDataFile = AnalyseUtils.extractAnalysisTarget(errLogFile, soapGwName);
		int startLine = getStartLine("failure",soapGwName,errLogFile.getName());
		Map<String, Object> analysisTarget = AnalyseUtils.getErrAnalysisTarget(failureDataFile, startLine);
		int endLine = (int)analysisTarget.get("endLine");
		if(startLine>endLine){
			LOGGER.info("Mark error.Reset this err mark to 0");
			startLine = 0;
			String sql = "Update err_mark set start_line = 0 where soap_name = ?";
			jdbcTemplate.update(sql,soapGwName);
			analysisTarget = AnalyseUtils.getErrAnalysisTarget(failureDataFile, startLine);
		}
		StringBuilder targetText = (StringBuilder)analysisTarget.get("targetText");
		if(targetText.length()==0){
			DecimalFormat df = new DecimalFormat("##.##");
			LOGGER.info("NOT ENOUGH ERR LOG INFO,EXIT PROGRAM..");
			LOGGER.info("File name:"+errLogFile.getAbsolutePath());
			LOGGER.info("File size:"+df.format(errLogFile.length()/(1024.0*1024.0))+"Mb");
			return;
		}
		Map<String, List<Map<String, Object>>> failAnalysedDataMap = new HashMap<>();
		failAnalysedDataMap = AnalyseUtils.getErrorInfoMap(targetText,hlrsnTransform);
		loadErrDataToDB(errLogFile.getName(), soapGwName, failAnalysedDataMap);
		afterLoad("failure", errLogFile.getName(), failureDataFile, soapGwName, endLine);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void soapLogDataAnalysisUnicom(File soapLogFile, String soapGwName) throws IOException {//successLogFileName 变化的文件名，是相对路径
		File successDataFile = AnalyseUtils.extractAnalysisTarget(soapLogFile, soapGwName);//获取解析目标
		int startLine = getStartLine("success",soapGwName,soapLogFile.getName());//startLine-从日志文件的第几行开始解析
		Map<String, Object> analysisTarget = AnalyseUtils.getSoapLogAnalysisTarget(successDataFile, startLine);//根据起始行，获取需要解析解析日志文本
		int endLine = (int)analysisTarget.get("endLine");//获取结束行，作为下一个周期的起始行。
		if(startLine>endLine){
			LOGGER.info("Mark error.Reset this mark to 0");
			startLine = 0;
			String sql = "Update soap_mark set start_line = 0 where soap_name = ?";
			jdbcTemplate.update(sql,soapGwName);
			analysisTarget = AnalyseUtils.getSoapLogAnalysisTarget(successDataFile, startLine);
		}
		StringBuilder targetText = (StringBuilder)analysisTarget.get("targetText");
		if(targetText.length()==0){
			LOGGER.info("NOT ENOUGH SOAP LOG INFO,EXIT PROGRAM..");
			DecimalFormat df = new DecimalFormat("##.##");
			LOGGER.info("File name:"+soapLogFile.getAbsolutePath());
			LOGGER.info("File size:"+df.format(soapLogFile.length()/(1024.0*1024.0))+"Mb");
			return;
		}
		
		Map<String, Object> soapAnalysedInfo = AnalyseUtils.originalDataTransformerUnicom(targetText);//把原始日志转换为LOAD数据
		TASKID_AND_HLRSN_MAP = (Map<String,String>)soapAnalysedInfo.get("taskIdAndHlrsnMap");
		loadSoapDataToDB(soapGwName, soapLogFile.getName(), soapAnalysedInfo,bossVersion);//数据入库
		afterLoad("success", soapLogFile.getName(), successDataFile, soapGwName, endLine);//入库后的处理动作
	}
	@Override
	public void errLogDataAnalysisUnicom(File errLogFile, String soapGwName) throws IOException, ParseException {
		File failureDataFile = AnalyseUtils.extractAnalysisTarget(errLogFile, soapGwName);
		int startLine = getStartLine("failure",soapGwName,errLogFile.getName());
		Map<String, Object> analysisTarget = AnalyseUtils.getErrAnalysisTarget(failureDataFile, startLine);
		int endLine = (int)analysisTarget.get("endLine");
		StringBuilder targetText = (StringBuilder)analysisTarget.get("targetText");
		if(startLine>endLine){
			LOGGER.info("Mark error.Reset this err mark to 0");
			startLine = 0;
			String sql = "Update err_mark set start_line = 0 where soap_name = ?";
			jdbcTemplate.update(sql,soapGwName);
			analysisTarget = AnalyseUtils.getErrAnalysisTarget(failureDataFile, startLine);
		}
		if(targetText.length()==0){
			DecimalFormat df = new DecimalFormat("##.##");
			LOGGER.info("NOT ENOUGH ERR LOG INFO,EXIT PROGRAM..");
			LOGGER.info("File name:"+errLogFile.getAbsolutePath());
			LOGGER.info("File size:"+df.format(errLogFile.length()/(1024.0*1024.0))+"Mb");
			return;
		}
		//HANDLE ERR TARGET
		LOGGER.info("TASK ID AND HLRSN MAP SIZE:"+TASKID_AND_HLRSN_MAP.size());
		Map<String, StringBuilder> m = AnalyseUtils.errCaseCucLogAnalyser(targetText,TASKID_AND_HLRSN_MAP);
		loadErrCucDataToDB(m,errLogFile,soapGwName);
		afterLoad("failure", errLogFile.getName(), failureDataFile, soapGwName, endLine);
	}

	private void loadErrCucDataToDB(Map<String, StringBuilder> m,File soapLogFile, String soapGwName) throws IOException {
		StringBuilder errCaseCucDataLoader = m.get("err_case_cuc");
		StringBuilder errJoinLoader = m.get("err_join");
		String loadToErrDataCucTableSQL = LoadStaticData.getLOAD_CUC_ERR_DATA_SQL();
		String loadToErrDataCucTempTableSQL = LoadStaticData.getLOAD_CUC_ERR_TEMP_DATA_SQL();
		String loadToJoinSQL = LoadStaticData.getLOAD_ERR_JOIN_SQL();
		File errCaseCucLoaderFile = AnalyseUtils.writeLoadFile(errCaseCucDataLoader, soapLogFile.getName(), soapGwName, "err");
		File errCaseCucLoaderTempFile = AnalyseUtils.writeLoadFile(errCaseCucDataLoader, soapLogFile.getName(), soapGwName, "errtemp");
		File errjoinLoaderFile = AnalyseUtils.writeLoadFile(errJoinLoader, soapLogFile.getName(), soapGwName, "join");
		loadToErrDataCucTableSQL = loadToErrDataCucTableSQL.replace("loadFileDir", errCaseCucLoaderFile.getAbsolutePath());
		loadToErrDataCucTempTableSQL = loadToErrDataCucTempTableSQL.replace("loadFileDir", errCaseCucLoaderTempFile.getAbsolutePath());
		loadToJoinSQL = loadToJoinSQL.replace("loadFileDir", errjoinLoaderFile.getAbsolutePath());
		LOGGER.info("Execute load to boss_err_case_cuc :");
		LOGGER.info(loadToErrDataCucTableSQL);
		jdbcTemplate.execute(loadToErrDataCucTableSQL);
		LOGGER.info("Complete");
		LOGGER.info("Execute load to boss_err_case_cuc :");
		LOGGER.info(loadToErrDataCucTempTableSQL);
		jdbcTemplate.execute(loadToErrDataCucTempTableSQL);
		LOGGER.info("Complete");
		LOGGER.info("Execute load to boss_join :");
		LOGGER.info(loadToJoinSQL);
		jdbcTemplate.execute(loadToJoinSQL);
		LOGGER.info("Complete");
		errCaseCucLoaderFile.delete();
		errCaseCucLoaderTempFile.delete();
		errjoinLoaderFile.delete();
	}

	/**
	 * ERR_CASE解析后入库
	 * @param failureLogFileName
	 * @param soapGwName
	 * @param failAnalysedDataMap
	 * @throws IOException
	 */
	private void loadErrDataToDB(String failureLogFileName, String soapGwName,
			Map<String, List<Map<String, Object>>> failAnalysedDataMap) throws IOException {
		List<Map<String, Object>> normalErrorAnalysedData = failAnalysedDataMap.get("normalError");
		StringBuilder normalErrorLoader = AnalyseUtils.errorAnalysedDataListToLoader(normalErrorAnalysedData).get("error");
		StringBuilder normalErrorJoinLoader =  AnalyseUtils.errorAnalysedDataListToLoader(normalErrorAnalysedData).get("join");
		File errorLoaderFile = AnalyseUtils.writeLoadFile(normalErrorLoader, failureLogFileName, soapGwName, "err");
		File errorJoinLoaderFile = AnalyseUtils.writeLoadFile(normalErrorJoinLoader, failureLogFileName, soapGwName, "join");
		File errorTempLoaderFile = AnalyseUtils.writeLoadFile(normalErrorLoader, failureLogFileName, soapGwName, "errtemp");
		String loadErrSQL = LoadStaticData.getLOAD_ERR_DATA_SQL().replace("loadFileDir", errorLoaderFile.getAbsolutePath());
		String loadErrJoinSQL = LoadStaticData.getLOAD_ERR_JOIN_SQL().replace("loadFileDir", errorJoinLoaderFile.getAbsolutePath());
		String loadNormalErrorToTempTableSQL = LoadStaticData.getLOAD_ERR_DATA_SQL().replace("loadFileDir", errorTempLoaderFile.getAbsolutePath()).replace("boss_err_case", "boss_err_case_temp");
		LOGGER.info("SQL for loading ERR_CASE data to boss_err_case:");
		LOGGER.info(loadErrSQL);
		jdbcTemplate.execute(loadErrSQL);
		LOGGER.info("SQL for loading ERR_CASE log to boss_join :");
		LOGGER.info(loadErrJoinSQL);
		jdbcTemplate.execute(loadErrJoinSQL);
		LOGGER.info("SQL for loading ERR_CASE data to temp table:");
		LOGGER.info(loadNormalErrorToTempTableSQL);
		jdbcTemplate.execute(loadNormalErrorToTempTableSQL);
		errorLoaderFile.delete();
		errorJoinLoaderFile.delete();
		errorTempLoaderFile.delete();
	}

	/**
	 * 
	 * @param logType
	 * @param filePureName
	 * @param analysedFile
	 * @param soapName
	 * @param startLine
	 */
	private void afterLoad(String logType,String filePureName ,File analysedFile,String soapName,int startLine){
		
		//文件变化有三种情况，根据不同的情况来处理MARK逻辑
		//1.如果解析的是GZ文件，应该重置MARK为0，并删除转换为文本后的文件（./cache/*.trans），并且将该文件名字加入IGNORE列表
		//2.如果解析的不是GZ文件，但是文件名字含有今日时间戳，应该是每天最后一个文件。因为没有写到一定的大小，所以不打包。
		//这种情况应该重置MARK，把文件添加到IGNORE列表，并且文件名+“.GZ”也添加到IGNORE列表
		//3.如果文件名没改，更新MARK即可。
		//参数STARTLINE实际上是解析后得到的ENDLINE，变为下一个周期的STARTLINE
		if(filePureName.contains(".gz")){ //ALL GZ
			afterGzAnalysed(logType, filePureName, soapName);
		}else{//NOT GZ
			if(logType.equalsIgnoreCase("failure")){
				if(filePureName.endsWith("BOSS_ERR_CASE.log")){
					String updateMark = "update err_mark set start_line = ?,file_name = ? where soap_name = ?";
					LOGGER.info(filePureName+" is still being wriring in.Update mark.");
					LOGGER.info("Update error mark as:err_mark = "+startLine+",soap name = "+soapName+", file name= "+filePureName);
					jdbcTemplate.update(updateMark, startLine,filePureName,soapName); 
				}else{
					LOGGER.info(filePureName+" is not a continuesly writing in file,reset mark.");
					lastFileNotCompressed(logType, filePureName, soapName);
				}
			}
			else if(logType.equalsIgnoreCase("success")){
				if(logType.equalsIgnoreCase("success")){
					String updateMark = "update soap_mark set start_line = ?,file_name = ? where soap_name = ?";
					LOGGER.info("Update soap mark as:soap_mark = "+startLine+",soap name = "+soapName+", file name= "+filePureName);
					jdbcTemplate.update(updateMark, startLine,filePureName,soapName); 
				}
			}
		}
	}
	/**
	 * 处理的文件没压缩，但是不写入数据了
	 * 加入两条数据到IGNORE列表 本名和本命+".gz"
	 * @param logType
	 * @param filePureName
	 * @param soapName
	 */
	public void lastFileNotCompressed(String logType, String filePureName, String soapName) {
		if(logType.equalsIgnoreCase("success")){
			//重置MARK的行数为0
			LOGGER.info(filePureName+" analysis completed.Reset mark to 0.Soap name:"+soapName);
			jdbcTemplate.update("update soap_mark set start_line = ? ,file_name = 'waiting next soap file' where soap_name = ?",0,soapName);
		}else if(logType.equalsIgnoreCase("failure")){
			LOGGER.info(filePureName+" analysis completed.Reset mark to 0.Soap name:"+soapName);
			jdbcTemplate.update("update err_mark set start_line = ? ,file_name = 'waiting next err file' where soap_name = ?",0,soapName);
		}
		//增加到IGNORE列表
		String updateIgnoreFile = "insert into ignore_file (file_name,analysed_time,soap_name)values(?,?,?)";
		LOGGER.info("Add a pair of new ignore record:");
		LOGGER.info("File name:");
		LOGGER.info(filePureName+" AND "+filePureName+".gz"+",soap name:"+soapName);
		jdbcTemplate.update(updateIgnoreFile, filePureName,SDF.format(new Date()),soapName);
		jdbcTemplate.update(updateIgnoreFile, filePureName+".gz",SDF.format(new Date()),soapName);
		
	}
	/**
	 * gz文件解析之后的动作
	 * @param logType
	 * @param filePureName
	 * @param analysedFile
	 * @param soapName
	 */
	public void afterGzAnalysed(String logType, String filePureName, String soapName) {
		if(logType.equalsIgnoreCase("success")){
			//重置MARK的行数为0
			jdbcTemplate.update("update soap_mark set start_line = ? ,file_name = 'waiting next soap file' where soap_name = ?",0,soapName);
			LOGGER.info("soap_mark for:"+soapName+" has been reset to 0.");
		}else if(logType.equalsIgnoreCase("failure")){
			jdbcTemplate.update("update err_mark set start_line = ? ,file_name = 'waiting next err file' where soap_name = ?",0,soapName);
			LOGGER.info("err_mark for:"+soapName+" has been reset to 0.");
		}
		//增加到IGNORE列表
		String updateIgnoreFile = "insert into ignore_file (file_name,analysed_time,soap_name)values(?,?,?)";
		jdbcTemplate.update(updateIgnoreFile, filePureName,SDF.format(new Date()),soapName);
		LOGGER.info("A new file has been added to ignore file list:");
		LOGGER.info("File name:"+filePureName+",SOAP GW name:"+soapName);
	}
	/**
	 * BOSS MARK机制：
	 * 首先要知道，每套日志的数据都是唯一的写入一个文件里，直到它写满或者到了第二天。
	 * 也就是说，不符合文件名匹配规范的变化文件，全都无视，不会解析。
	 * 根据SOAP的名字来确定MARK的唯一性，所以正常来讲，每个MARK表里，同一台SOAP上的日志记录，有且只有一个（程序强制规定）
	 * 每台SOAP上BOSS日志有两套，BOSS_SOAP(下发指令日志)，BOSS_ERR_CASE.LOG（业务错误日志）   //联通是另一套BOSS，机制差不多，文件名不一样
	 * SOAP_MARK存BOSS_SOAP日志的MARK，ERR_MARK存BOSS_ERR的MARK
	 * 如果监测到文件没有被压缩为GZ，只更新MARK字段即可。
	 * 如果文件被压缩为GZ，删除这条MARK记录
	 * 
	 * @param logType
	 * @return start line
	 */
	private int getStartLine(String logType,String soapGwName,String fileName){
		String sql = "select * from table where soap_name = ?";
		int startLine = 0;
		if(logType.equalsIgnoreCase("failure")){
			Map<String,Object> m = jdbcTemplate.queryForList(sql.replace("table", "err_mark"),soapGwName).get(0);
			startLine = (int)m.get("start_line");
			
		}else if(logType.equalsIgnoreCase("success")){
			Map<String,Object> m = jdbcTemplate.queryForList(sql.replace("table", "soap_mark"),soapGwName).get(0);
			startLine = (int)m.get("start_line");
		}
		return startLine;
	}
	/**
	 * SOAP日志解析后入库
	 * @param soapGwName
	 * @param pureName
	 * @param soapAnalysedInfo
	 * @throws IOException
	 */
	private void loadSoapDataToDB(String soapGwName, String pureName, Map<String, Object> soapAnalysedInfo,String bossVersion)
			throws IOException {
		File soapDataLoader = null;
		String loadSoapSQL = null;
		String loadSoapSQLTemp = null;
		if(bossVersion.equalsIgnoreCase("chinamobile")){
			soapDataLoader = AnalyseUtils.writeLoadFile((StringBuilder)soapAnalysedInfo.get("boss_soap"), pureName, soapGwName, "data");
			loadSoapSQL = LoadStaticData.getLOAD_SOAP_DATA_SQL().replace("loadFileDir", soapDataLoader.getAbsolutePath());
			loadSoapSQLTemp = LoadStaticData.getLOAD_SOAP_DATA_SQL_TEMP().replace("loadFileDir", soapDataLoader.getAbsolutePath());
			
		}else if(bossVersion.equalsIgnoreCase("unicom")){
			soapDataLoader = AnalyseUtils.writeLoadFile((StringBuilder)soapAnalysedInfo.get("boss_soap_cuc"), pureName, soapGwName, "data");
			loadSoapSQL = LoadStaticData.getLOAD_CUC_SOAP_DATA_SQL().replace("loadFileDir", soapDataLoader.getAbsolutePath());
			loadSoapSQLTemp = LoadStaticData.getLOAD_CUC_SOAP_DATA_SQL_TEMP().replace("loadFileDir", soapDataLoader.getAbsolutePath());
		}
		File soapJoinLoader = AnalyseUtils.writeLoadFile((StringBuilder)soapAnalysedInfo.get("boss_join"), pureName, soapGwName, "join");
		String loadJoinSQL =  LoadStaticData.getLOAD_SOAP_JOIN_SQL().replace("loadFileDir", soapJoinLoader.getAbsolutePath());
		LOGGER.info("EXECUTE LOADSQL-SOAP:");
		LOGGER.info(loadSoapSQL);
		jdbcTemplate.execute(loadSoapSQL);
		LOGGER.info("EXECUTE LOADSQL-TEMP:");
		LOGGER.info(loadSoapSQLTemp);
		jdbcTemplate.execute(loadSoapSQLTemp);
		LOGGER.info("EXECUTE LOADSQL-JOIN:");
		LOGGER.info(loadJoinSQL);
		jdbcTemplate.execute(loadJoinSQL);
		soapDataLoader.delete();
		soapJoinLoader.delete();
	}
}
