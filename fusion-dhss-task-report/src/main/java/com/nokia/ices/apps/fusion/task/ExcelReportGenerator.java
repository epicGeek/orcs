package com.nokia.ices.apps.fusion.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nokia.ices.apps.fusion.CustomSettings;
import com.nokia.ices.apps.fusion.monitor.domain.MonitorTable;
import com.nokia.ices.apps.fusion.monitor.repository.MonitorTableRepository;


@Component
@EnableScheduling
public class ExcelReportGenerator {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	@Qualifier("jdbcTemplateOss")
	private JdbcTemplate jdbcTemplateOss;
	@Autowired
	private MonitorTableRepository monitorTableRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(ExcelReportGenerator.class);
	private static final SimpleDateFormat formatWithSlash = new SimpleDateFormat("yyyy/MM/dd");
	private static final SimpleDateFormat formatWithClose = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
	private static final SimpleDateFormat formatWithDateSql = new SimpleDateFormat("yyyy-MM-dd");
	private static final Map<String, XSSFCellStyle> cellStyleMap = new HashMap<String, XSSFCellStyle>();
	private static final SimpleDateFormat minuteTimeFormat = new SimpleDateFormat("mm");
	
	/**
	 *  @see This method is the main entry for the whole program.
	 *  It's supposed to export one report every 15 minutes.
	 * @throws IOException
	 */
	//@Scheduled(initialDelay = 500, fixedDelay = 15 * 1000 * 60)
	//@Scheduled(cron = "0 5/15 * * * ?")
	//@Scheduled(cron = "0 0/1 * * * ?")
	/*public void judgeToStart(){
		List<Map<String, Object>> listoss = jdbcTemplateOss.queryForList("select sysdate from dual");
		Date ossTime = (Date)listoss.get(0).get("SYSDATE");
		String ossTimeMinStr = minuteTimeFormat.format(ossTime);
		logger.info("OSS time:" + ossTimeMinStr);
		if(ossTimeMinStr.equalsIgnoreCase("05")||ossTimeMinStr.equalsIgnoreCase("20")||ossTimeMinStr.equalsIgnoreCase("35")||ossTimeMinStr.equalsIgnoreCase("50")){
			logger.info("Required time on!:"+ossTimeMinStr);
			try {
				startTask();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			logger.info("NOT REQUIRED TIME.SAYONARA REPORT TASK~" + ossTimeMinStr);
		}
	}*/
	@Scheduled(cron = "${spring.report.generation-cron}")
	//@Scheduled(initialDelay = 50, fixedDelay = 1000000000)
	//@Scheduled(cron = "0 5/15 * * * ?")
	//@Scheduled(cron = "0 5 9,10,11,17,18,20,22 * * ?")
	//@Scheduled(cron = "0 5 21 * * ?")
	public void startTask() throws IOException {
		logger.info("Start to generate a new report......");

		
		List<Map<String, Object>> originalDataConfList = getOriginalDataConfList();
		/**
		 * Set wb as the report template.
		 */
		Map<String,Object> templateConf = readKpiReportTemplate().get(0);
		FileInputStream fis = new FileInputStream(templateConf.get("report_template_path").toString() + templateConf.get("report_name").toString());
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		/**
		 * Force the excel to execute the formula.
		 * For some reason,the formulas written manually is not executed automatically,double click the cell to execute the formula.
		 * So, used this method to force the formula to be executed.
		 */
		wb.setForceFormulaRecalculation(true);
		/**
		 * Set styles
		 */
		initStyleSheet(wb);
		/**
		 * Fill 'original data' 
		 */
		readyToFillOriginalData(originalDataConfList, wb);
		/**
		 * Fill the KPI data with the SQL given in the field.
		 */
	 	fillDynamicalSheetData(wb);
		//TO-DO: Daily report method written below.
		
		initDailyReportData(wb);
	
		// Daily report method written above.
		saveAndCloseOutPutFile(wb, generatedOutputDir()+formatWithClose.format(new Date())+".xlsx");
		wb.close();
		logger.info("Report Generated Successfully");

	}
	/**
	 * In this method ,It's supposed to query history KPI data from quota_monitor_history to get daily data.
	 * @param wb
	 */
	private List<Map<String,Object>> readKpiReportTemplate(){
		String getTemplateStr = "SELECT * FROM report_template WHERE 1=1";
		getTemplateStr += " AND id ="+CustomSettings.getId();
		List<Map<String, Object>> templateConf = jdbcTemplate.queryForList(getTemplateStr);
		return templateConf;
	}
	private void initDailyReportData(XSSFWorkbook wb){
		XSSFSheet sheet4DailyData = wb.getSheet("Today");
		/**
		 * Get query results
		 */
		fillDailyData(sheet4DailyData);
	}
	
	/**
	 * Fill daily data to the excel.
	 * @param queryResult
	 */
	private void fillDailyData(XSSFSheet sheet4DailyData){
		String getDailyDataConf = "SELECT * FROM report_data_conf WHERE grid_type = 'dailylandscape' ";
		List<Map<String, Object>> dailyConf = jdbcTemplate.queryForList(getDailyDataConf);
		String todayStartStr = "'"+formatWithDateSql.format(new Date())+" 08:00:00'";
		String todayEndStr = "'"+formatWithDateSql.format(new Date())+" 19:59:59'";
		for (Map<String, Object> dailyConfMap : dailyConf) {
			logger.info("Get data :"+dailyConfMap.get("comment").toString());
			//String realSql = dailyConfMap.get("sql_for_original_data").toString().replaceAll("#today_start_time#", "'2016-05-09 08:00:00'").replaceAll("#today_end_time#", "'2016-05-09 19:59:59'");
			String realSql = dailyConfMap.get("sql_for_original_data").toString().replaceAll("#today_start_time#",todayStartStr).replaceAll("#today_end_time#", todayEndStr);
			logger.info("The real SQL is : "+realSql);
			CellAddress dailyDataStartPosition = new CellAddress(dailyConfMap.get("cell_start_address").toString());
			int startRow = dailyDataStartPosition.getRow();
			int startCol = dailyDataStartPosition.getColumn();
			int iRow = 0;
			int iCol = 0;
			String[] kpiType = dailyConfMap.get("header_name").toString().split(",");
			List<Map<String,Object>> queryResult = jdbcTemplate.queryForList(realSql);
			for (Map<String, Object> queryResultMap : queryResult) {
				logger.info("The queryResultMap is :"+queryResultMap.toString());
				iRow = 0;
				for (String kpiTypeStr : kpiType) {
					Cell cell = sheet4DailyData.getRow(startRow+iRow).getCell(startCol+iCol);
					if(queryResultMap.containsKey(kpiTypeStr)){
						fillCellWithValue(cell, queryResultMap.get(kpiTypeStr),cellStyleMap.get("default"));
					}else {
						fillCellWithValue(cell, 0, null);
					}
					iRow++;
				}
				iCol++;
			}
		}
	}	
	private void fillSingleKpiData(String equipmentName, XSSFSheet sheet, List<String> unitList) {
		String sql = "SELECT * FROM report_data_conf WHERE grid_type = 'landscape'";
		List<Map<String, Object>> kpiDataQueryList = jdbcTemplate.queryForList(sql);
		String neIdListSql = "SELECT ne_code FROM equipment_ne WHERE dhss_name= ? AND ne_code IS NOT NULL";
		List<Map<String, Object>> neIdList = jdbcTemplate.queryForList(neIdListSql, equipmentName);
		List<String> neList = new ArrayList<String>();
		neIdList.forEach(neIDMap -> neList.add(neIDMap.get("ne_code").toString()));
		Map<String, List<String>> queryParam = new HashMap<String, List<String>>();
		queryParam.put("neList", neList);
		for (Map<String, Object> map : kpiDataQueryList) {
			CellAddress ca = new CellAddress(map.get("cell_start_address").toString());
			int startColumn = ca.getColumn();
			String sqlToGetKpi = map.get("sql_for_original_data").toString();
			List<Map<String, Object>> getKpiDataList = new NamedParameterJdbcTemplate(jdbcTemplateOss.getDataSource())
					.queryForList(sqlToGetKpi, queryParam);
			Map<String, Map<String, Object>> resultMap = new HashMap<String, Map<String, Object>>();
			getKpiDataList.forEach(x -> resultMap.put(x.get("unit_id").toString(), x));
			String[] getKpiType = map.get("header_name").toString().split(",");
			for (String unitCode : unitList) {
				int startRow = ca.getRow();
				for (String colName : getKpiType) {
					Cell cell = sheet.getRow(startRow).createCell(startColumn);
					if (resultMap.containsKey(unitCode)) {
						String styleName = colName.endsWith("_rate") ? "percent" : null;
						fillCellWithValue(cell, resultMap.get(unitCode).get(colName), cellStyleMap.get(styleName));
					} else {
						fillCellWithValue(cell, 0, null);
					}
					startRow++;
				}
				startColumn++;
			}
		}
	}
	private void insertIntoOutputPath(String outPutPath){
		MonitorTable mt = new MonitorTable();
		mt.setCreateDate(new Date());
		mt.setFilePath(outPutPath);
		String reportName = "DHSS_KPI_Report_"+formatWithClose.format(new Date())+".xlsx";
		mt.setName(reportName);
		logger.info("Report named:"+" "+reportName);
		monitorTableRepository.save(mt);
	}
	private String generatedOutputDir() {
		String pathTemplate = File.separator + formatWithSlash.format(new Date()) + File.separator;
		File outPutBasePath = new File(CustomSettings.getOutPutFilePath()+pathTemplate);
		if (!outPutBasePath.exists())
			outPutBasePath.mkdirs();
		return CustomSettings.getOutPutFilePath() + pathTemplate;
	}
	/**
	 * Initiate the style of the sheet and cell.
	 * The styles are saved in sheet 'CustomStyleSheet'
	 * Generally,this sheet shall be hidden.
	 */
	private void initStyleSheet(XSSFWorkbook wb) {
		XSSFSheet sheet = wb.getSheet("CustomStyleSheet");
		XSSFCellStyle cellStyleDefault = sheet.getRow(0).getCell(0).getCellStyle();
		cellStyleMap.put("default", cellStyleDefault);
		XSSFCellStyle cellStyleDate = sheet.getRow(1).getCell(0).getCellStyle();
		cellStyleMap.put("date", cellStyleDate);
		XSSFCellStyle cellStylePrecent = sheet.getRow(2).getCell(0).getCellStyle();
		cellStyleMap.put("percent", cellStylePrecent);
		XSSFCellStyle cellStyleNumber = sheet.getRow(3).getCell(0).getCellStyle();
		cellStyleMap.put("number", cellStyleNumber);
	}
	/**
	 *  Get the configurations and template info to be ready for filling the original data.
	 *  1.According to sheet names to know what sheets are filled with original data.
	 *  2.Get the headers from the template by method 'readHeader'
	 * @param reportConfInfoMap
	 * @param wb
	 */
	private void readyToFillOriginalData(List<Map<String, Object>> originalDataConfList, XSSFWorkbook wb) {
		for (Map<String, Object> confMap : originalDataConfList) {
			logger.info("~~~~~~~~~~start to travel the map~~~~~~~~~~:SheetName:" + confMap.get("sheet_name"));
			XSSFSheet sheet = wb.getSheet(confMap.get("sheet_name").toString());
			/**
			 * Headers are saved in the List 'headerContext'
			 */
			List<String> headerContext = readHeader(sheet);
			/**
			 * The original data collection is saved in originalDataList.
			 */
			List<Map<String, Object>> originalDataList = getOriginalData(confMap.get("sql_for_original_data").toString());
			/**
			 * Fill the original data with this method.
			 */
			fillOriginalData(originalDataList, headerContext, sheet, confMap.get("cell_start_address").toString());
		}
	}
	/**
	 * In this method,KPI data will be filled in the cell.
	 * @param wb
	 */
	private void fillDynamicalSheetData(XSSFWorkbook wb) {
		/**
		 * 1.Get sheet distinct names with method 'getSheetName'
		 */
		List<String> hssName = getHssName();
		/**
		 * 2.Use lambda expression to process all the equipment names.
		 */
		hssName.forEach(x -> {
			/**
			 * 3.Fill titles:ne_name,fe_name,hss_id,hssfe_id
			 */
			List<String> unitList = fillEquipmentSheetData(x, wb.getSheet(x));
			fillSingleKpiData(x, wb.getSheet(x), unitList);
		});

	}
	/**
	 * In this method,title info which contains NE,unit..etc will be filled 
	 * @param equipmentName
	 * @param sheet
	 * @return
	 */
	private List<String> fillEquipmentSheetData(String equipmentName, XSSFSheet sheet) {
		List<Map<String, Object>> equipmentInfoList = getEquipmentHeader(equipmentName);
		CellAddress startLocation = new CellAddress("F2");
		List<String> unitCodeList = new ArrayList<String>();
		String[] keyList = new String[] { "ne_name", "unit_name", "ne_code", "unit_code" };
		int startColumn = startLocation.getColumn();
		for (Map<String, Object> map : equipmentInfoList) {
			int startRow = startLocation.getRow();
			for (String key : keyList) {
				Cell cell = sheet.getRow(startRow).createCell(startColumn);
				fillCellWithValue(cell, map.get(key), null);
				startRow++;
			}
			startColumn++;
			unitCodeList.add(map.get("unit_code").toString());
		}
		return unitCodeList;
	}
	/**
	 * Fill every single KPI data given in the report_data_conf table
	 * @param equipmentName
	 * @param sheet
	 * @param unitList
	 */
	private List<Map<String, Object>> getEquipmentHeader(String equipmentName) {
		String sql = "SELECT DISTINCT ne_name,unit_name,n.ne_code,u.ne_code as unit_code \n"
				+ " FROM equipment_unit u,equipment_ne n \n" 
				+ " WHERE \n"
				+ "u.ne_id = n.id AND  dhss_name = ? AND unit_type IN ('NTHLRFE','HSSFE') AND u.ne_code IS NOT NULL\n"
				+ "ORDER BY ne_name,unit_name";
		List<Map<String, Object>> equipmentInfoList = jdbcTemplate.queryForList(sql, equipmentName);
		return equipmentInfoList;
	}
	/**
	 * Use this method to get all HSS names.
	 * @return
	 */
	private List<String> getHssName() {
		String sql = "SELECT DISTINCT dhss_name FROM equipment_ne WHERE 1=1";
		List<Map<String, Object>> hssList = jdbcTemplate.queryForList(sql);
		List<String> hssNameList = new ArrayList<>();
		/**
		 * New tech:Lamba expression.
		 * 
		 */
		hssList.forEach(x -> hssNameList.add(x.get("dhss_name").toString()));
		return hssNameList;
	}
	/**
	 * Fill in the cells with values and set styles.
	 * @param cell
	 * @param cellValue
	 * @param cellStyle
	 */
	private void fillCellWithValue(Cell cell, Object cellValue, CellStyle cellStyle) {
		setCellStyle(cell, cellValue, cellStyle);
		try {
			if (cellValue == null)
				cell.setCellValue("");
			else if (cellValue instanceof Date)
				cell.setCellValue((Date) cellValue);
			else if (cellValue instanceof Double)
				cell.setCellValue((Double) cellValue);
			else if (cellValue instanceof Long)
				cell.setCellValue((Long) cellValue);
			else if (cellValue instanceof Integer)
				cell.setCellValue((Integer) cellValue);
			else if (cellValue instanceof String)
				cell.setCellValue((String) cellValue);
			else if (cellValue instanceof BigDecimal)
				cell.setCellValue(((BigDecimal) cellValue).doubleValue());
			else {
				cell.setCellValue("error class type:" + cellValue.getClass().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * According to cell type to set cell style.
	 * @param cell
	 * @param cellValue
	 * @param cellStyle
	 */
	private void setCellStyle(Cell cell, Object cellValue, CellStyle cellStyle) {
		if (cellStyle == null) {
			if (cellValue instanceof Double)
				cell.setCellStyle(cellStyleMap.get("number"));
			else if (cellValue instanceof Long)
				cell.setCellStyle(cellStyleMap.get("number"));
			else if (cellValue instanceof Integer)
				cell.setCellStyle(cellStyleMap.get("number"));
			else if (cellValue instanceof BigDecimal)
				cell.setCellStyle(cellStyleMap.get("number"));
			else if (cellValue instanceof Date)
				cell.setCellStyle(cellStyleMap.get("date"));
			else
				cell.setCellStyle(cellStyleMap.get("default"));
		} else {
			cell.setCellStyle(cellStyle);
		}
	}
	/**
	 * @see This method is used to get report configuration from table report_data_conf.Field grid_type shows 
	 * how the data is placed in the excel grids.
	 * @return
	 */
	private List<Map<String, Object>> getOriginalDataConfList() {
		logger.info("~~~~~~~~~~ Start to get report configuration ~~~~~~~~~~");
		String sql = "select * from report_data_conf where grid_type = 'portrait'";
		/**
		 * First of all,get 'portrait' data,like 'Authentication','MTC','LTE' ..etc
		 */
		List<Map<String, Object>> reportConf = jdbcTemplate.queryForList(sql);
		/*logger.info(reportConf.toString());*/
		logger.info("********** The report configuration info has been collected **********");
		return reportConf;
	}
	private void saveAndCloseOutPutFile(XSSFWorkbook wb, String outPutFileName) throws IOException {
		logger.info("~~~~~~~~~~Start to save and close file~~~~~~~~~~");
		FileOutputStream out = new FileOutputStream(outPutFileName);
		wb.write(out);
		insertIntoOutputPath(outPutFileName);
		out.close();
		logger.info("**********Saving and closing file ends**********");
	}
	/**
	 * Get the headers from the original data sheets.
	 * @param sheet
	 * @return
	 */
	private List<String> readHeader(XSSFSheet sheet) {
		List<String> headerContext = new ArrayList<String>();
		/**
		 * Generally the headers are written in the first row.
		 */
		XSSFRow firstRow = sheet.getRow(0);
		firstRow.forEach(a -> headerContext.add(a.getStringCellValue()));
		return headerContext;
	}
	/**
	 * Get the original data through the SQL given in the field.
	 * @param sql
	 * @return
	 */
	private List<Map<String, Object>> getOriginalData(String sql) {
		logger.info("~~~~~~~~~~ original data Start ~~~~~~~~~~");

		List<Map<String, Object>> infoList = new ArrayList<Map<String, Object>>();
		try {
			infoList = jdbcTemplateOss.queryForList(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("********** original info end **********");
		return infoList;
	}
	/**
	 * This method is used to fill original data.
	 * @param originalDataList
	 * @param headerContext
	 * @param sheet
	 * @param address
	 */
	private void fillOriginalData(List<Map<String, Object>> originalDataList, List<String> headerContext,
			XSSFSheet sheet, String cellAddress) {
		/**
		 * The original data's start addresses are given in the field cell_start_address
		 */
		CellAddress ca = new CellAddress(cellAddress);
		int rowStart = ca.getRow();
		int columnStart = ca.getColumn();
		int currentRow = 0;
		/**
		 * Start to fill original data.
		 */
		for (Map<String, Object> dataMap : originalDataList) {
			logger.info(dataMap.toString());
			Row row = sheet.createRow(rowStart + currentRow);
			int currentColunm = 0;
			for (String keyHeader : headerContext) {
				Cell cell = row.createCell(columnStart + currentColunm);
				fillCellWithValue(cell, dataMap.get(keyHeader), null);
				currentColunm++;
			}
			currentRow++;
		}

	}
}
