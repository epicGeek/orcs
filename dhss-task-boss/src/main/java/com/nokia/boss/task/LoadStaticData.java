package com.nokia.boss.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nokia.boss.settings.CustomSettings;


@Component
/**
 * 
 * @author Pei Nan
 * This class runs first when the scheduled task start.
 * When the scheduled task start,this class will load some static data and create some useful directory.
 * 
 * 在程序第一次启动的时候载入常量、创建必要的文件夹。
 *
 */
public class LoadStaticData implements CommandLineRunner {
	@Override
	public void run(String... args) throws Exception {
		loadStaticData();//载入常量
		makeDirs();//创建必要文件
	}
	private static final Logger logger = LoggerFactory.getLogger(LoadStaticData.class);
	private static Map<String, String> BUSINESS_TYPE_MAP = new HashMap<String, String>();
	private static List<Map<String,String>> soapAddressInfoList = new ArrayList<>();
	private static String currentPeriodKPICalculationSQL ;
	/**
	 * 
	 * Key : operationName 操作
	 * Value: business type 业务类型
	 */
	static {
		currentPeriodKPICalculationSQL = 
				"select total_view.hlrsn,total_view.operation_name,total_view.period_start_time,ifnull(fail_count,0) as fail_count,total_count from \n" +
						"(select count(1) as total_count,\n" +
						"hlrsn,operation_name,\n" +
						"DATE_ADD(date_format(re_time,\"%Y-%m-%d %H:00\"),interval (minute(re_time) div 15) *15 minute) as period_start_time\n" +
						"from boss_soap_temp \n" +
						"group by hlrsn,operation_name,\n" +
						"period_start_time) total_view\n" +
						"left join \n" +
						"(select count(1) as fail_count,\n" +
						"hlrsn,operation_name,\n" +
						"DATE_ADD(date_format(re_time,\"%Y-%m-%d %H:00\"),interval (minute(re_time) div 15) *15 minute) as period_start_time\n" +
						"from boss_err_case_temp \n" +
						"group by hlrsn,operation_name,\n" +
						"period_start_time) fail_view \n" +
						"using (hlrsn,operation_name,period_start_time)";
	}
	public static String getCurrentPeriodKPICalculationSQL(){
		return currentPeriodKPICalculationSQL;
	}
	static {
		BUSINESS_TYPE_MAP.put("BB","BB");
		BUSINESS_TYPE_MAP.put("ZMNE","LTE");
		BUSINESS_TYPE_MAP.put("ZMIM","CARD");
		BUSINESS_TYPE_MAP.put("ZMNB","STOPRESET");
		BUSINESS_TYPE_MAP.put("ZMNP","LTE");
		BUSINESS_TYPE_MAP.put("ZMNA","LTE");
		BUSINESS_TYPE_MAP.put("ZMGC","STOPRESET");
		BUSINESS_TYPE_MAP.put("ZMBC","VOICE");
		BUSINESS_TYPE_MAP.put("ZMNM","STOPRESET");
		BUSINESS_TYPE_MAP.put("ZMAE","OVERHEAD");
		BUSINESS_TYPE_MAP.put("ZMIR","OVERHEAD");
		BUSINESS_TYPE_MAP.put("ZMSD","VOICE");
		BUSINESS_TYPE_MAP.put("ZMBD","VOICE");
		BUSINESS_TYPE_MAP.put("ZMNC","GPRS");
		BUSINESS_TYPE_MAP.put("ZMIO","BQUERY");
		BUSINESS_TYPE_MAP.put("ZMSO","BQUERY");
		BUSINESS_TYPE_MAP.put("ZMNO","BQUERY");
		BUSINESS_TYPE_MAP.put("ZMQO","BQUERY");
		BUSINESS_TYPE_MAP.put("ZMAO","BQUERY");
		BUSINESS_TYPE_MAP.put("ZMNF","BQUERY");
		BUSINESS_TYPE_MAP.put("ZMSC","VOICE");
		BUSINESS_TYPE_MAP.put("ZMNI","BQUERY");
		BUSINESS_TYPE_MAP.put("ZMSS","VOICE");
		BUSINESS_TYPE_MAP.put("ZMGO","BQUERY");
		BUSINESS_TYPE_MAP.put("ZMND","GPRS");
		BUSINESS_TYPE_MAP.put("ZMQD","NETWORK");
		BUSINESS_TYPE_MAP.put("ZMQE","NETWORK");
		BUSINESS_TYPE_MAP.put("ZMID","OVERHEAD");
		BUSINESS_TYPE_MAP.put("ZMAD","OVERHEAD");
		BUSINESS_TYPE_MAP.put("ZMIO","BQUERY");
		BUSINESS_TYPE_MAP.put("ZMBO","BQUERY");
		BUSINESS_TYPE_MAP.put("ZMNR","LTE");
		BUSINESS_TYPE_MAP.put("ZMND","GPRS");
		BUSINESS_TYPE_MAP.put("ZVIR","voLTE");
		BUSINESS_TYPE_MAP.put("ZVID","voLTE");
		BUSINESS_TYPE_MAP.put("AA","AA");
		BUSINESS_TYPE_MAP.put("ADD_KI","OVERHEAD");
		BUSINESS_TYPE_MAP.put("RMV_KI","OVERHEAD");
		BUSINESS_TYPE_MAP.put("MOD_ARD","GPRS_LTE");
		BUSINESS_TYPE_MAP.put("LST_ARD","");
		BUSINESS_TYPE_MAP.put("MOD_BS","VOICE");
		BUSINESS_TYPE_MAP.put("LST_BS","");
		BUSINESS_TYPE_MAP.put("MOD_CFU","VOICE");
		BUSINESS_TYPE_MAP.put("MOD_CFNRC","VOICE");
		BUSINESS_TYPE_MAP.put("REG_CFNRC","VOICE");
		BUSINESS_TYPE_MAP.put("ERA_CFNRC","VOICE");
		BUSINESS_TYPE_MAP.put("MOD_CFD","VOICE");
		BUSINESS_TYPE_MAP.put("LST_CFALL","");
		BUSINESS_TYPE_MAP.put("REG_CFU","VOICE");
		BUSINESS_TYPE_MAP.put("ERA_CFU","VOICE");
		BUSINESS_TYPE_MAP.put("MOD_CFB","VOICE");
		BUSINESS_TYPE_MAP.put("REG_CFB","VOICE");
		BUSINESS_TYPE_MAP.put("ERA_CFB","VOICE");
		BUSINESS_TYPE_MAP.put("MOD_CFNRY","VOICE");
		BUSINESS_TYPE_MAP.put("REG_CFNRY","VOICE");
		BUSINESS_TYPE_MAP.put("ERA_CFNRY","VOICE");
		BUSINESS_TYPE_MAP.put("MOD_CB","VOICE");
		BUSINESS_TYPE_MAP.put("ACT_BICROM","VOICE");
		BUSINESS_TYPE_MAP.put("DEA_BICROM","VOICE");
		BUSINESS_TYPE_MAP.put("LST_CBAR","");
		BUSINESS_TYPE_MAP.put("MOD_BARPWD","VOICE");
		BUSINESS_TYPE_MAP.put("MOD_CBCOU","VOICE");
		BUSINESS_TYPE_MAP.put("ACT_BAOC","VOICE");
		BUSINESS_TYPE_MAP.put("DEA_BAOC","VOICE");
		BUSINESS_TYPE_MAP.put("ACT_BOIC","VOICE");
		BUSINESS_TYPE_MAP.put("DEA_BOIC","VOICE");
		BUSINESS_TYPE_MAP.put("ACT_BOICEXHC","VOICE");
		BUSINESS_TYPE_MAP.put("DEA_BOICEXHC","VOICE");
		BUSINESS_TYPE_MAP.put("ACT_BAIC","VOICE");
		BUSINESS_TYPE_MAP.put("DEA_BAIC","VOICE");
		BUSINESS_TYPE_MAP.put("MOD_CLIP","IDENTIFICATION");
		BUSINESS_TYPE_MAP.put("LST_CLIP","");
		BUSINESS_TYPE_MAP.put("MOD_CLIR","IDENTIFICATION");
		BUSINESS_TYPE_MAP.put("LST_CLIR","");
		BUSINESS_TYPE_MAP.put("MOD_COLP","IDENTIFICATION");
		BUSINESS_TYPE_MAP.put("LST_COLP","");
		BUSINESS_TYPE_MAP.put("MOD_COLR","IDENTIFICATION");
		BUSINESS_TYPE_MAP.put("LST_COLR","");
		BUSINESS_TYPE_MAP.put("MOD_PLMNSS","CUSTOM");
		BUSINESS_TYPE_MAP.put("MOD_OSS","VOICE");
		BUSINESS_TYPE_MAP.put("LST_OSS","");
		BUSINESS_TYPE_MAP.put("LST_SS","");
		BUSINESS_TYPE_MAP.put("MOD_LCS","LOCATION");
		BUSINESS_TYPE_MAP.put("LST_LCS","");
		BUSINESS_TYPE_MAP.put("MOD_CARP","VOICE");
		BUSINESS_TYPE_MAP.put("LST_CARP","");
		BUSINESS_TYPE_MAP.put("ADD_SUB","OVERHEAD");
		BUSINESS_TYPE_MAP.put("MOD_IMSI","OVERHEAD");
		BUSINESS_TYPE_MAP.put("MOD_ISDN","OVERHEAD");
		BUSINESS_TYPE_MAP.put("MOD_CATEGORY","USERTYPE");
		BUSINESS_TYPE_MAP.put("LST_CATEGORY","");
		BUSINESS_TYPE_MAP.put("MOD_NAM","GPRS_VOICE");
		BUSINESS_TYPE_MAP.put("LST_NAM","");
		BUSINESS_TYPE_MAP.put("MOD_CCGLOBAL","CHARGING");
		BUSINESS_TYPE_MAP.put("LST_CCGLOBAL","");
		BUSINESS_TYPE_MAP.put("ADD_TPLSUB","OVERHEAD");
		BUSINESS_TYPE_MAP.put("RMV_SUB","OVERHEAD");
		BUSINESS_TYPE_MAP.put("ADD_CSPSSUB","OVERHEAD");
		BUSINESS_TYPE_MAP.put("ADD_TPLCSPSSUB","OVERHEAD");
		BUSINESS_TYPE_MAP.put("RMV_CSPSSUB","OVERHEAD");
		BUSINESS_TYPE_MAP.put("ADD_EPSSUB","OVERHEAD");
		BUSINESS_TYPE_MAP.put("RMV_EPSSUB","OVERHEAD");
		BUSINESS_TYPE_MAP.put("LST_SUB","");
		BUSINESS_TYPE_MAP.put("MOD_CAMEL","NETWORK");
		BUSINESS_TYPE_MAP.put("LST_CAMEL","");
		BUSINESS_TYPE_MAP.put("SND_CANCELC","LOCATION");
		BUSINESS_TYPE_MAP.put("MOD_TS","VOICE");
		BUSINESS_TYPE_MAP.put("LST_TS","");
		BUSINESS_TYPE_MAP.put("MOD_TPLGPRS","GPRS");
		BUSINESS_TYPE_MAP.put("MOD_GPRS_CONTEXT","GPRS");
		BUSINESS_TYPE_MAP.put("LST_GPRS","");
		BUSINESS_TYPE_MAP.put("MOD_TPLEPS","LTE");
		BUSINESS_TYPE_MAP.put("MOD_EPSDATA","LTE");
		BUSINESS_TYPE_MAP.put("MOD_EPS_CONTEXT","LTE");
		BUSINESS_TYPE_MAP.put("LST_EPS","");
		BUSINESS_TYPE_MAP.put("MOD_DIAMRRS","LTE");
		BUSINESS_TYPE_MAP.put("LST_DIAMRRS","");
		BUSINESS_TYPE_MAP.put("ADD_TPLIMSSUB","OVERHEAD");
		BUSINESS_TYPE_MAP.put("MOD_TPLIMSSUB","VoLTE");
		BUSINESS_TYPE_MAP.put("ADD_IMSSUB","OVERHEAD");
		BUSINESS_TYPE_MAP.put("MOD_CAP","VoLTE");
		BUSINESS_TYPE_MAP.put("LST_STNSR","");
		BUSINESS_TYPE_MAP.put("ADD_SIFC","VoLTE");
		BUSINESS_TYPE_MAP.put("RMV_SIFC","VoLTE");
		BUSINESS_TYPE_MAP.put("LST_SIFC","");
		BUSINESS_TYPE_MAP.put("ADD_IFC","VoLTE");
		BUSINESS_TYPE_MAP.put("RMV_IFC","VoLTE");
		BUSINESS_TYPE_MAP.put("LST_IFC","");
		BUSINESS_TYPE_MAP.put("MOD_VOLTETAG","VoLTE");
		BUSINESS_TYPE_MAP.put("LST_VOLTETAG","");
		BUSINESS_TYPE_MAP.put("MOD_HBAR","VoLTE");
		BUSINESS_TYPE_MAP.put("LST_CAP","");
		BUSINESS_TYPE_MAP.put("LST_HBAR","");
		BUSINESS_TYPE_MAP.put("MOD_CHARGID","VoLTE");
		BUSINESS_TYPE_MAP.put("LST_CHARGID","");
		BUSINESS_TYPE_MAP.put("MOD_VNTPLID","VoLTE");
		BUSINESS_TYPE_MAP.put("LST_VNTPLID","");
		BUSINESS_TYPE_MAP.put("MOD_MEDIAID","VoLTE");
		BUSINESS_TYPE_MAP.put("LST_MEDIAID","");
		BUSINESS_TYPE_MAP.put("MOD_STNSR","VoLTE");
		BUSINESS_TYPE_MAP.put("LST_IMSSUB","");
		BUSINESS_TYPE_MAP.put("RMV_IMSSUB","OVERHEAD");
		BUSINESS_TYPE_MAP.put("MOD_LCK","STOPRESET");
		BUSINESS_TYPE_MAP.put("LST_LCK","");
		BUSINESS_TYPE_MAP.put("MOD_RR","ROAMING");
		BUSINESS_TYPE_MAP.put("LST_RR","");
		BUSINESS_TYPE_MAP.put("MOD_ODB","VOICE");
		BUSINESS_TYPE_MAP.put("LST_ODBDAT","");
		BUSINESS_TYPE_MAP.put("BAT_ADD_TPLSUB","OVERHEAD");
		BUSINESS_TYPE_MAP.put("BAT_RMV_SUB","OVERHEAD");
		BUSINESS_TYPE_MAP.put("BAT_ADD_TPLCSPSSUB","OVERHEAD");
		BUSINESS_TYPE_MAP.put("BAT_RMV_CSPSSUB","OVERHEAD");
		BUSINESS_TYPE_MAP.put("BAT_RMV_EPSDATA","OVERHEAD");
		BUSINESS_TYPE_MAP.put("BAT_ADD_KI","OVERHEAD");
		BUSINESS_TYPE_MAP.put("BAT_RMV_KI","OVERHEAD");
		BUSINESS_TYPE_MAP.put("BAT_MOD_LCK","STOPRESET");
		BUSINESS_TYPE_MAP.put("MOD_LCADDRESS","GPRS_VOICE");
	}
	//LOAD入库的SQL
	private static String LOAD_SOAP_DATA_SQL = 
			"LOAD DATA LOCAL INFILE 'loadFileDir' INTO TABLE boss_soap FIELDS TERMINATED BY '~' LINES TERMINATED BY ';;;'  (\n" +
					"	user_name,\n" +
					"	re_id,\n" +
					"	response_time,\n" +
					
					"	hlrsn,\n" +
					"	msisdn,\n" +
					
					"	imsi,\n" +
					"	re_time,\n" +
					
					"	operation_name,\n" +
					"	business_type,\n" +
					"	delay_time\n" +
					")";
	
	private static String LOAD_ERR_DATA_SQL = 
			"LOAD DATA LOCAL INFILE 'loadFileDir' INTO TABLE boss_err_case FIELDS TERMINATED BY '~' LINES TERMINATED BY ';;;'  (\n" +
			"	re_id,\n" +
			"	re_time,\n" +
			"	response_time,\n" +
			"	user_name,\n" +
			"	user_password,\n" +
			"	imsi,\n" +
			"	msisdn,\n" +
			"	hlrsn,\n" +
			"	business_type,\n" +
			"	operation_name,\n" +
			"	error_code,\n" +
			"	error_message\n" +
			")";
	
	private static String LOAD_JOIN_SQL = 
			"LOAD DATA LOCAL INFILE 'loadFileDir' INTO TABLE boss_join FIELDS TERMINATED BY '~' LINES TERMINATED BY ';;;'  (\n" +
			"	re_id,\n" +
			"	soap_log,\n" +
			"	err_log \n" +
			")";
	//解析JSON串所用到的的KEY
	private static String HLRSN_PATTERN = "\"HLRSN\":\"";
	private static String HLRID_PATTERN = "\"HLRID\":\"";
	private static String MSISDN_PATTERN = "\"ISDN\":\"";
	private static String IMSI_PATTERN = "\"IMSI\":\"";
	private static String IMPU_PATTERN = "\"IMPU\":\"";
	private static String OPERATION_PATTERN = "\"OPERATION\":\"";
	private static String OPERATION_NAME_PATTERN = "\"operationName\":\"";
	
	public static String getLOAD_SOAP_DATA_SQL(){
		return LOAD_SOAP_DATA_SQL;
	}
	public static String getLOAD_ERR_DATA_SQL(){
		return LOAD_ERR_DATA_SQL;
	}
	public static Map<String, String> getBUSINESS_TYPE_MAP() {
		return BUSINESS_TYPE_MAP;
	}


	public static List<Map<String, String>> getSoapAddressInfoList() {
		return soapAddressInfoList;
	}




	public static String getLOAD_JOIN_SQL() {
		return LOAD_JOIN_SQL;
	}


	public static String getHLRSN_PATTERN() {
		return HLRSN_PATTERN;
	}


	public static String getHLRID_PATTERN() {
		return HLRID_PATTERN;
	}


	public static String getMSISDN_PATTERN() {
		return MSISDN_PATTERN;
	}


	public static String getIMSI_PATTERN() {
		return IMSI_PATTERN;
	}


	public static String getIMPU_PATTERN() {
		return IMPU_PATTERN;
	}


	public static String getOPERATION_PATTERN() {
		return OPERATION_PATTERN;
	}


	public static String getOPERATION_NAME_PATTERN() {
		return OPERATION_NAME_PATTERN;
	}
	//创建必要的文件 
	//Create necessary directory.
	private void makeDirs(){
		String loadDir = CustomSettings.getLoadFileDir();
		String markDir = CustomSettings.getMarkLogFile();
		String rsyncDir = CustomSettings.getRsyncDataDir();
		String dataRecoverDirPath = CustomSettings.getDataRecoverDir();
		String transfromDirPath = CustomSettings.getTransformDir();
		File dataRecoverDir = new File(dataRecoverDirPath);
		File transfromDir = new File(transfromDirPath);
		if(!dataRecoverDir.exists()){
			dataRecoverDir.mkdirs();
		}
		if(!transfromDir.exists()){
			transfromDir.mkdirs();
		}
		logger.info("rsync dir pattern:"+rsyncDir);
		for (Map<String,String> soapInfoMap : soapAddressInfoList) {
			String soapName = soapInfoMap.get("soap-gw-name");
			logger.info("soap name:"+soapName);
			//create "rsync" directory
			rsyncDir = rsyncDir.replace("#soap-gw-name#", soapName);
			File rsyncDir_ = new File(rsyncDir);
			if(!rsyncDir_.exists()){
				logger.info("This rsync dir is not existed:"+rsyncDir_.getAbsolutePath());
				rsyncDir_.mkdirs();
				logger.info(rsyncDir_.getAbsolutePath()+" created completed.");
			}
			//create loader dir
			loadDir = loadDir.replace("#soap-gw-name#", soapName);
			File loadDir_ = new File(loadDir);
			if(!loadDir_.exists()){
				logger.info("This loader dir is not existed:"+loadDir_.getAbsolutePath());
				loadDir_.mkdirs();
				logger.info(loadDir_.getAbsolutePath()+" created completed.");
			}
			//create mark dir
			markDir = markDir.replace("#soap-gw-name#", soapName);
			File markDir_ = new File(markDir);
			if(!markDir_.exists()){
				logger.info("This mark dir is not existed:"+markDir_.getAbsolutePath());
				markDir_.mkdirs();
				logger.info(markDir_.getAbsolutePath()+" created completed.");
			}
		}
	}
	
	/**
	 * 载入SOAPGW登录信息
	 * Load soap gw login info
	 * 
	 */
	private void loadStaticData(){
		logger.info("Start to load boss static data.");
		Map<String,String> map = new HashMap<>();
		String[] soapInfoPieces = CustomSettings.getSoapGwInfo().split(",");
		for (String soapInfoPiece : soapInfoPieces) {
			map.put("soap-gw-name", soapInfoPiece.split("-")[0]);
			map.put("soap-gw-ip", soapInfoPiece.split("-")[1]);
			soapAddressInfoList.add(map);
		}
	}
	

}
