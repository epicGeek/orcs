package com.nokia.pgw.task;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nokia.pgw.settings.CustomSetting;
import com.nokia.pgw.util.PGWAnalyseUtil;
@Component
public class PrepareAction implements CommandLineRunner{
	private static final Logger LOGGER = LoggerFactory.getLogger(PrepareAction.class);
	private static String uncompressedFileDir = "";
	private static String rsyncDataFileDir = "";
	private static String loaderFileDir = "";
	private static String loaderDetailFileDir = "";
	private static String loaderXMLFileDir = "";
	private static String detailDataLoadSQL ;
	private static String XMLDataLoadSQL ;

	@Autowired
	private CustomSetting customSetting;
	@Override
	public void run(String... args) throws Exception {
		LOGGER.info("Establish file directory...");
		String deployDir = customSetting.getPgwLogDeployDir();
		if(!deployDir.endsWith(File.separator)){
			deployDir+=File.separator;
			LOGGER.info("Program deploy location:"+deployDir);
		}
		String uncompressedDir = deployDir + "uncompressed"+File.separator;
		String rsyncDataDir = deployDir + "rsync-data"+File.separator;
		String loaderDir = deployDir + "loader"+File.separator;
		String loadDetailDataDir = loaderDir+"detail"+File.separator;
		String loadXmlDataDir = loaderDir + "xml"+File.separator;
		setUncompressedFileDir(uncompressedDir);
		setRsyncDataFileDir(rsyncDataDir);
		setLoaderFileDir(loaderDir);
		setLoaderDetailFileDir(loadDetailDataDir);
		setLoaderXMLFileDir(loadXmlDataDir);
		File uncompressedDirectory = new File(uncompressedFileDir);
		if(!uncompressedDirectory.exists()){
			uncompressedDirectory.mkdirs();
		}
		File rsyncDataDirectory = new File(rsyncDataFileDir);
		if(!rsyncDataDirectory.exists()){
			rsyncDataDirectory.mkdirs();
		}
		File loaderDirectory = new File(loaderFileDir);
		if(!loaderDirectory.exists()){
			loaderDirectory.mkdirs();
		}
		File loadDetailDataDirectory = new File(loadDetailDataDir);
		if(!loadDetailDataDirectory.exists()){
			loadDetailDataDirectory.mkdirs();
		}
		File loadXMLDataDirectory = new File(loadXmlDataDir);
		if(!loadXMLDataDirectory.exists()){
			loadXMLDataDirectory.mkdirs();
		}
		String fieldTerminator = PGWAnalyseUtil.getFieldTerminator();
		String lineTerminator = PGWAnalyseUtil.getLineTerminator();
		String detailSql = " LOAD DATA LOCAL INFILE '#fileAbsPath#' INTO TABLE pgw_detail_data FIELDS TERMINATED BY '#fieldTerminator#' LINES TERMINATED BY '#lineTerminator#'  "
				+ "(\n" +
				"	response_time,\n" +
				"	request_id,\n" +
				"	pgw_name,\n" +
				"	instance_name,\n" +
				"	user_name,\n" +
				"	execution_time,\n" +
				"	execution_content,\n" +
				"	result_type,\n" +
				"	operation,\n" +
				"	user_number,\n" +
				"	error_code,\n" +
				"	error_message\n" +
				")";
		String xmlSql = 
				" LOAD DATA LOCAL INFILE '#fileAbsPath#' INTO TABLE pgw_xml_log FIELDS TERMINATED BY '#fieldTerminator#' LINES TERMINATED BY '#lineTerminator#'  "
				+ "(\n" +
				"	response_time,\n" +
				"	request_id,\n" +
				"	response_log\n" +
				")";
		detailSql = detailSql.replace("#fieldTerminator#", fieldTerminator).replace("#lineTerminator#", lineTerminator);
		xmlSql = xmlSql.replace("#fieldTerminator#", fieldTerminator).replace("#lineTerminator#", lineTerminator);
		setDetailDataLoadSQL(detailSql);
		setXMLDataLoadSQL(xmlSql);
	}
	public static String getUncompressedFileDir() {
		return uncompressedFileDir;
	}
	public static void setUncompressedFileDir(String uncompressedFileDir) {
		PrepareAction.uncompressedFileDir = uncompressedFileDir;
	}
	public static String getRsyncDataFileDir() {
		return rsyncDataFileDir;
	}
	public static void setRsyncDataFileDir(String rsyncDataFileDir) {
		PrepareAction.rsyncDataFileDir = rsyncDataFileDir;
	}
	public static String getLoaderFileDir() {
		return loaderFileDir;
	}
	public static void setLoaderFileDir(String loaderFileDir) {
		PrepareAction.loaderFileDir = loaderFileDir;
	}
	public static String getLoaderDetailFileDir() {
		return loaderDetailFileDir;
	}
	public static void setLoaderDetailFileDir(String loaderDetailFileDir) {
		PrepareAction.loaderDetailFileDir = loaderDetailFileDir;
	}
	public static String getLoaderXMLFileDir() {
		return loaderXMLFileDir;
	}
	public static void setLoaderXMLFileDir(String loaderXMLFileDir) {
		PrepareAction.loaderXMLFileDir = loaderXMLFileDir;
	}
	public static String getDetailDataLoadSQL() {
		return detailDataLoadSQL;
	}
	public static void setDetailDataLoadSQL(String detailDataLoadSQL) {
		PrepareAction.detailDataLoadSQL = detailDataLoadSQL;
	}
	public static String getXMLDataLoadSQL() {
		return XMLDataLoadSQL;
	}
	public static void setXMLDataLoadSQL(String xMLDataLoadSQL) {
		XMLDataLoadSQL = xMLDataLoadSQL;
	}

}
