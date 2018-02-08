package com.nokia.boss.task;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nokia.boss.settings.CustomSettings;


@Component
/**
 * 
 * @author Pei Nan
 * 
 * 
 * 这个类继承了CommandLineRunner，在程序运行时优先运行一次。用来加载一些常量和创建一些必要的路径。
 * This class implements CommandLineRunner.Once the program runs ,this class runs first.
 * It is used to load some static constants and create some necessary directory.
 *
 */
public class LoadStaticData implements CommandLineRunner {
	@Override
	/**
	 * This method runs first.
	 * 这个方法是优先运行的
	 */
	public void run(String... args) throws Exception {
		loadStaticData(); 
		makeDirs();
	}
	private static final Logger LOGGER = LoggerFactory.getLogger(LoadStaticData.class);
	private static Map<String, String> BUSINESS_TYPE_MAP = new HashMap<String, String>();// operation_name->business_type
	private static List<Map<String,String>> SOAP_ADDRESS_INFO_LIST = new ArrayList<>();
	private static String SOAP_TABLE_FIELD_ORDER;//SQL column order 
	private static String SOAP_TABLE_FIELD_ORDER_CUC;//SQL column order 
	private static String ERR_TABLE_FIELD_ORDER;
	private static String CUC_ERR_TABLE_FIELD_ORDER;
	private static String SOAP_JOIN_TABLE_FIELD_ORDER;
	private static String ERR_JOIN_TABLE_FIELD_ORDER;
	private static String FIELD_TERMINATOR;//field terminator signal
	private static String LINE_TERMINATOR;//line terminator signal
	private static String CURRENT_PERIOD_KPI_CALCULATION_SQL;
	private static String CURRENT_PERIOD_KPI_CALCULATION_SQL_CUC;
	private static String BOSS_HOUR_KPI_SQL ;
	private static String BOSS_DAY_KPI_SQL ;
	private static String BOSS_MONTH_KPI_SQL;
	private static String RULE_PATTERN_CM;//China mobile boss file name pattern
	private static String RULE_PATTERN_CUC;//unicom boss file name pattern
	public static String getFieldTerminator(){
		return FIELD_TERMINATOR;
	}
	public static String getLineTerminator(){
		return LINE_TERMINATOR;
	}
	private static Map<String,List<File>> BOSS_DIR = new HashMap<>();
	public static Map<String,List<File>> getBossDir() {
		return BOSS_DIR;
	}
	/**
	 * 中国移动BOSS日志的名字精确匹配
	 * ChinaMobile BOSS log file name match pattern.
	 */
	static{
		RULE_PATTERN_CM = 
					"+ */\n"+
					"+ backup/BOSS_ERR_CASE.log.yyyy-MM-dd.*\n"+
					"+ backup/BOSS_SOAP_Agent_BOSSA_main_yyyy-MM-dd.*\n"+
					"+ BOSS_ERR_CASE.log\n"+
					"+ BOSS_ERR_CASE.log.yyyy-MM-dd.*\n"+
					"+ BOSS_SOAP_Agent_BOSSA_main_yyyy-MM-dd.*\n"+
					"- *";
		RULE_PATTERN_CUC = 
					"+ */\n"+
					"+ backup/CUC_BOSS_ERR_CASE.log.yyyy-MM-dd.*\n"+
					"+ backup/CUC_Telnet_Agent_CUCA_main_yyyy-MM-dd.*\n"+
					"+ backup/CUC_Telnet_Agent_BOSSA_main_yyyy-MM-dd.*\n"+
					"+ CUC_BOSS_ERR_CASE.log\n"+
					"+ CUC_BOSS_ERR_CASE.log.yyyy-MM-dd.*\n"+
					"+ CUC_Telnet_Agent_CUCA_main_yyyy-MM-dd.*\n"+
					"+ CUC_Telnet_Agent_BOSSA_main_yyyy-MM-dd.*\n"+
					"- *";
	}
	
	/**
	 * 计算每月BOSS KPI的SQL
	 * SQL for calculating every month's BOSS KPI
	 */
	static{
		BOSS_MONTH_KPI_SQL = 
				"SELECT\n" +
				"	sum(fail_count) AS fail_count,\n" +
				"	sum(total_count) AS total_count,\n" +
				"	DATE_FORMAT(period_start_day,\"%Y-%m-01 00:00:00\") as period_start_month,\n" +
				"	hlrsn,\n" +
				"	operation_name\n" +
				"FROM\n" +
				"	`boss_monitor_day`\n" +
				"where DATE_FORMAT(period_start_day,\"%Y-%m-01 00:00:00\") = '#month-start-time#'\n" +
				"GROUP BY \n" +
				"period_start_month,hlrsn,operation_name\n" +
				"ORDER BY\n" +
				"period_start_month,hlrsn,operation_name\n" ;
	}
	/**
	 * 计算最新周期的KPI SQL
	 * SQL for calculating latest period BOSS KPI
	 */
	static{
		CURRENT_PERIOD_KPI_CALCULATION_SQL = 
				"select total_view.hlrsn,total_view.operation_name,total_view.period_start_time,ifnull(fail_count,0) as fail_count,total_count from \n" +
				"(select count(1) as total_count,\n" +
				"hlrsn,operation_name,\n" +
				"DATE_ADD(date_format(response_time,\"%Y-%m-%d %H:00\"),interval (minute(response_time) div 15) *15 minute) as period_start_time\n" +
				"from boss_soap_temp \n" +
				"group by hlrsn,operation_name,\n" +
				"period_start_time) total_view\n" +
				"left join \n" +
				"(select count(1) as fail_count,\n" +
				"hlrsn,operation_name,\n" +
				"DATE_ADD(date_format(response_time,\"%Y-%m-%d %H:00\"),interval (minute(response_time) div 15) *15 minute) as period_start_time\n" +
				"from boss_err_case_temp \n" +
				"group by hlrsn,operation_name,\n" +
				"period_start_time) fail_view \n" +
				"using (hlrsn,operation_name,period_start_time)";
		
		CURRENT_PERIOD_KPI_CALCULATION_SQL_CUC = 
				"select total_view.hlrsn,total_view.operation_name,total_view.period_start_time,ifnull(fail_count,0) as fail_count,total_count from \n" +
				"(select count(1) as total_count,\n" +
				"hlrsn,operation_name,\n" +
				"DATE_ADD(date_format(response_time,\"%Y-%m-%d %H:00\"),interval (minute(response_time) div 15) *15 minute) as period_start_time\n" +
				"from boss_soap_cuc_temp \n" +
				"group by hlrsn,operation_name,\n" +
				"period_start_time) total_view\n" +
				"left join \n" +
				"(select count(1) as fail_count,\n" +
				"hlrsn,operation_name,\n" +
				"DATE_ADD(date_format(response_time,\"%Y-%m-%d %H:00\"),interval (minute(response_time) div 15) *15 minute) as period_start_time\n" +
				"from boss_err_case_cuc_temp \n" +
				"group by hlrsn,operation_name,\n" +
				"period_start_time) fail_view \n" +
				"using (hlrsn,operation_name,period_start_time)";
	}
	/**
	 * 计算上个小时的KPI SQL
	 * SQL for calculating last hour's BOSS KPI
	 */
	static{
		BOSS_HOUR_KPI_SQL = 
				"SELECT\n" +
				"	SUM(fail_count) as fail_count,\n" +
				"	SUM(total_count) as total_count,\n" +
				"	DATE_FORMAT(\n" +
				"		period_start_time,\n" +
				"		\"%Y-%m-%d %H:00:00\"\n" +
				"	) as period_start_hour,\n" +
				"	hlrsn,\n" +
				"	operation_name\n" +
				"FROM\n" +
				"	`boss_monitor_minute`\n" +
				"where \n" +
				"DATE_FORMAT(\n" +
				"		period_start_time,\n" +
				"		\"%Y-%m-%d %H:00:00\"\n" +
				"	)='#last_hour_oclock#'\n" +
				"GROUP BY\n" +
				"	operation_name,\n" +
				"	hlrsn,\n" +
				"	period_start_hour\n" +
				"ORDER BY\n" +
				"operation_name\n";
	}
	/**
	 * 计算昨天KPI SQL
	 * SQL for calculating yesterday's BOSS KPI
	 */
	static{
		BOSS_DAY_KPI_SQL = 
				"SELECT\n" +
				"	sum(fail_count) AS fail_count,\n" +
				"	sum(total_count) AS total_count,\n" +
				"	DATE_FORMAT(period_start_hour,\"%Y-%m-%d 00:00:00\") as period_start_day,\n" +
				"	hlrsn,\n" +
				"	operation_name\n" +
				"from boss_monitor_hour\n" +
				"where DATE_FORMAT(period_start_hour,\"%Y-%m-%d 00:00:00\") = '#yesterday-oclock#'\n" +
				"GROUP BY \n" +
				"period_start_day,hlrsn,operation_name\n" +
				"ORDER BY\n" +
				"period_start_day,hlrsn,operation_name";
	}
	/**
	 * 
	 * Key : operationName 操作
	 * Value: business type 业务类型
	 */
	static {
		BUSINESS_TYPE_MAP.put("ZVFS","UNKNOWN");
		BUSINESS_TYPE_MAP.put("ZMIS","UNKNOWN");
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
	/**
	 * LOAD SQL的字段顺序
	 * LOAD SQL COLUMN ORDER
	 */
	static{
		SOAP_TABLE_FIELD_ORDER = 					
						"	user_name,\n" +
						"	task_id,\n" +
						"	response_time,\n" +
						"	hlrsn,\n" +
						"	msisdn,\n" +
						"	imsi,\n" +
						"	operation_name,\n" +
						"	business_type,\n" +
						"	delay_time\n" ;
		SOAP_TABLE_FIELD_ORDER_CUC = 
						"	user_name,\n" +
						"	task_id,\n" +
						"	response_time,\n" +
						"	hlrsn,\n" +
						"	msisdn,\n" +
						"	imsi,\n" +
						"	operation_name,\n" +
						"	business_type,\n" +
						"	delay_time\n" ;
		ERR_TABLE_FIELD_ORDER = 
						"	task_id,\n" +
						"	response_time,\n" +
						"	user_name,\n" +
						"	user_password,\n" +
						"	imsi,\n" +
						"	msisdn,\n" +
						"	hlrsn,\n" +
						"	business_type,\n" +
						"	operation_name,\n" +
						"	error_code,\n" +
						"	error_message\n" ;
		CUC_ERR_TABLE_FIELD_ORDER = 
						"	task_id,\n" +
						"	response_time,\n" +
						"	mml,\n" +
						"	imsi,\n" +
						"	msisdn,\n" +
						"	hlrsn,\n" +
						"	business_type,\n" +
						"	operation_name,\n" +
						"	error_code,\n" +
						"	error_message\n" ;
		SOAP_JOIN_TABLE_FIELD_ORDER = 
						"	task_id,\n" +
						"	soap_log,\n" +
						"	response_time \n" ;
		ERR_JOIN_TABLE_FIELD_ORDER = 
						"	task_id,\n" +
						"	err_log ,\n" +
						"	response_time \n" ;
		FIELD_TERMINATOR = "~";
		LINE_TERMINATOR = ";;;";
						
		
	}
	//LOAD入库的SQL
	//LOAD SQL
	private static String LOAD_SOAP_DATA_SQL = 
			"LOAD DATA LOCAL INFILE 'loadFileDir' INTO TABLE boss_soap FIELDS TERMINATED BY '"+FIELD_TERMINATOR+"' LINES TERMINATED BY '"+LINE_TERMINATOR+"'  (\n" 
					+
					SOAP_TABLE_FIELD_ORDER
					+
					")";
	private static String LOAD_CUC_SOAP_DATA_SQL = 
			"LOAD DATA LOCAL INFILE 'loadFileDir' INTO TABLE boss_soap_cuc FIELDS TERMINATED BY '"+FIELD_TERMINATOR+"' LINES TERMINATED BY '"+LINE_TERMINATOR+"'  (\n" 
					+
					SOAP_TABLE_FIELD_ORDER_CUC
					+
					")";
	private static String LOAD_SOAP_DATA_SQL_TEMP = 
			"LOAD DATA LOCAL INFILE 'loadFileDir' INTO TABLE boss_soap_temp FIELDS TERMINATED BY '"+FIELD_TERMINATOR+"' LINES TERMINATED BY '"+LINE_TERMINATOR+"'  (\n" 
					+
					SOAP_TABLE_FIELD_ORDER
					+
					")";
	private static String LOAD_CUC_SOAP_DATA_SQL_TEMP = 
			"LOAD DATA LOCAL INFILE 'loadFileDir' INTO TABLE boss_soap_cuc_temp FIELDS TERMINATED BY '"+FIELD_TERMINATOR+"' LINES TERMINATED BY '"+LINE_TERMINATOR+"'  (\n" 
					+
					SOAP_TABLE_FIELD_ORDER_CUC
					+
					")";
	
	private static String LOAD_ERR_DATA_SQL = 
			"LOAD DATA LOCAL INFILE 'loadFileDir' INTO TABLE boss_err_case FIELDS TERMINATED BY '"+FIELD_TERMINATOR+"' LINES TERMINATED BY '"+LINE_TERMINATOR+"'  (\n" 
					+
					ERR_TABLE_FIELD_ORDER
					+
					")";
	private static String LOAD_ERR_DATA_SQL_TEMP = 
			"LOAD DATA LOCAL INFILE 'loadFileDir' INTO TABLE boss_err_case_temp FIELDS TERMINATED BY '"+FIELD_TERMINATOR+"' LINES TERMINATED BY '"+LINE_TERMINATOR+"'  (\n" 
					+
					ERR_TABLE_FIELD_ORDER
					+
					")";
	private static String LOAD_CUC_ERR_DATA_SQL = 
			"LOAD DATA LOCAL INFILE 'loadFileDir' INTO TABLE boss_err_case_cuc FIELDS TERMINATED BY '"+FIELD_TERMINATOR+"' LINES TERMINATED BY '"+LINE_TERMINATOR+"'  (\n" 
					+
					CUC_ERR_TABLE_FIELD_ORDER
					+
					")";
	private static String LOAD_CUC_ERR_TEMP_DATA_SQL = 
			"LOAD DATA LOCAL INFILE 'loadFileDir' INTO TABLE boss_err_case_cuc_temp FIELDS TERMINATED BY '"+FIELD_TERMINATOR+"' LINES TERMINATED BY '"+LINE_TERMINATOR+"'  (\n" 
					+
					CUC_ERR_TABLE_FIELD_ORDER
					+
					")";
	
	private static String LOAD_SOAP_JOIN_SQL = 
			"LOAD DATA LOCAL INFILE 'loadFileDir' INTO TABLE boss_join FIELDS TERMINATED BY '"+FIELD_TERMINATOR+"' LINES TERMINATED BY '"+LINE_TERMINATOR+"'  (\n" 
					+
					SOAP_JOIN_TABLE_FIELD_ORDER
					+
					")";
	private static String LOAD_ERR_JOIN_SQL = 
			"LOAD DATA LOCAL INFILE 'loadFileDir' INTO TABLE boss_join FIELDS TERMINATED BY '"+FIELD_TERMINATOR+"' LINES TERMINATED BY '"+LINE_TERMINATOR+"'  (\n" 
					+
					ERR_JOIN_TABLE_FIELD_ORDER
					+
					")";
	//解析JSON串所用到的的KEY
	//Key pattern for analysis JSON data.
	private static String HLRSN_PATTERN = "\"HLRSN\":\"";
	private static String HLRID_PATTERN = "\"HLRID\":\"";
	private static String MSISDN_PATTERN = "\"ISDN\":\"";
	private static String MSISDN_PATTERN_CUC = "\"MSISDN\":\"";
	private static String IMSI_PATTERN = "\"IMSI\":\"";
	private static String IMPU_PATTERN = "\"IMPU\":\"";
	private static String OPERATION_PATTERN = "\"OPERATION\":\"";
	private static String OPERATION_NAME_PATTERN = "\"operationName\":\"";
	private static String TASK_ID_CUC = "\"TASKID\":\"";
	private static String USER_CUC = "\"USER\":\"";
	private static String MML_CUC = "\"MML\":\"";
	
	
	
	public static String getLOAD_SOAP_DATA_SQL(){
		return LOAD_SOAP_DATA_SQL;
	}
	public static String getLOAD_ERR_DATA_SQL(){
		return LOAD_ERR_DATA_SQL;
	}
	public static String getLOAD_SOAP_DATA_SQL_TEMP(){
		return LOAD_SOAP_DATA_SQL_TEMP;
	}
	public static String getLOAD_ERR_DATA_SQL_TEMP(){
		return LOAD_ERR_DATA_SQL_TEMP;
	}
	@SuppressWarnings("unused")
	private static Map<String, String> getBUSINESS_TYPE_MAP() {
		return BUSINESS_TYPE_MAP;
	}
	public static String getBusinessType(String operationName){
		Map<String,String> m = BUSINESS_TYPE_MAP;
		if(m.containsKey(operationName)){
			return m.get(operationName);
		}else{
			//LOGGER.info("UNKNOWN OPERATION:"+operationName);
			return "UNKNOWN"; 
		}
	}
	public static List<Map<String, String>> getSoapAddressInfoList() {
		return SOAP_ADDRESS_INFO_LIST;
	}

	public static String getLOAD_SOAP_JOIN_SQL() {
		return LOAD_SOAP_JOIN_SQL;
	}
	public static String getLOAD_ERR_JOIN_SQL() {
		return LOAD_ERR_JOIN_SQL;
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
		List<File> cacheFileList = new ArrayList<>();
		String rsyncDir = CustomSettings.getRsyncDataDir();
		String loaderDir = CustomSettings.getLoadFileDir();
		String cacheDir = CustomSettings.getCacheDataDir();
		File cacheF = new File(cacheDir);
		cacheFileList.add(cacheF);
		BOSS_DIR.put("cache", cacheFileList);
		if(!cacheF.exists()){
			cacheF.mkdirs();
		}
		List<File> rsyncFileList = new ArrayList<>();
		List<File> loaderFileList = new ArrayList<>();
		for (Map<String, String> map : SOAP_ADDRESS_INFO_LIST) {
			String rsyncDirFileStr = rsyncDir.replace("#soap-gw-name#", map.get("soap-gw-name"));
			String rsyncBackupDirFileStr = rsyncDir.replace("#soap-gw-name#", map.get("soap-gw-name"))+"backup/";
			File rsyncDirFile = new File(rsyncDirFileStr);
			File rsyncBackupDirFile = new File(rsyncBackupDirFileStr);
			rsyncFileList.add(rsyncDirFile);
			rsyncFileList.add(rsyncBackupDirFile);
			File loaderDirFile = new File(loaderDir.replace("#soap-gw-name#", map.get("soap-gw-name")));
			loaderFileList.add(loaderDirFile);
			if(!rsyncDirFile.exists()){
				rsyncDirFile.mkdirs();
			}
			if(!rsyncBackupDirFile.exists()){
				rsyncDirFile.mkdirs();
			}
			if(!loaderDirFile.exists()){
				loaderDirFile.mkdirs();
			}
		}
		BOSS_DIR.put("rsync", rsyncFileList);
		BOSS_DIR.put("loader", loaderFileList);
	}
	
	/**
	 * 载入SOAPGW登录信息
	 * Load soap-GW login info
	 * 
	 */
	private void loadStaticData(){
		LOGGER.info("Start to load boss static data.");
		
		String[] soapInfoPieces = CustomSettings.getSoapGwInfo().split(",");
		for (String soapInfoPiece : soapInfoPieces) {
			Map<String,String> map = new HashMap<>();
			map.put("soap-gw-name", soapInfoPiece.split("-")[0]);
			map.put("soap-gw-ip", soapInfoPiece.split("-")[1]);
			SOAP_ADDRESS_INFO_LIST.add(map);
		}
	}

	public static String getCurrentPeriodKPICalculationSQL() {
		return CURRENT_PERIOD_KPI_CALCULATION_SQL;
	}
	public static String getCurrentPeriodKPICalculationSQL_CUC() {
		return CURRENT_PERIOD_KPI_CALCULATION_SQL_CUC;
	}
	public static String getBossHourKpiSQL() {
		return BOSS_HOUR_KPI_SQL;
	}
	public static String getBossDayKpiSQL() {
		return BOSS_DAY_KPI_SQL;
	}
	public static String getBossMonthKpiSQL() {
		return BOSS_MONTH_KPI_SQL;
	}
	public static String getRulePatternCM() {
		return RULE_PATTERN_CM;
	}
	public static String getTASK_ID_CUC() {
		return TASK_ID_CUC;
	}
	public static String getUSER_CUC() {
		return USER_CUC;
	}
	public static String getMML_CUC() {
		return MML_CUC;
	}
	public static String getMSISDN_PATTERN_CUC() {
		return MSISDN_PATTERN_CUC;
	}
	public static String getLOAD_CUC_SOAP_DATA_SQL() {
		return LOAD_CUC_SOAP_DATA_SQL;
	}
	public static String getLOAD_CUC_SOAP_DATA_SQL_TEMP() {
		return LOAD_CUC_SOAP_DATA_SQL_TEMP;
	}
	public static String getRULE_PATTERN_CUC() {
		return RULE_PATTERN_CUC;
	}
	public static String getLOAD_CUC_ERR_DATA_SQL() {
		return LOAD_CUC_ERR_DATA_SQL;
	}
	public static String getLOAD_CUC_ERR_TEMP_DATA_SQL() {
		return LOAD_CUC_ERR_TEMP_DATA_SQL;
	}
}
