package com.nokia.ices.apps.fusion.boss_rev.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.boss_rev.service.BossRevolutionService;

@RestController
public class BossRevolutionController {
	private static Logger LOGGER = LoggerFactory.getLogger(BossRevolutionController.class);
	private static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");
	@Autowired
	BossRevolutionService bossRevolutionService;
	@Value("${spring.dhss.boss-rev.bossversion}")
	private String bossVersion ;
	@Value("${spring.dhss.boss-rev.excel-path}")
	private String bossExcelGenPath;
	
	@RequestMapping(value = "/boss-rev/getBossQueryDataAndCount")
	public Map<String, Object> getBossQueryDataAndCount(
			@RequestParam(value = "hlrsn") String hlrsn,
			@RequestParam(value = "operationName") String operationName,
			@RequestParam(value = "errorType") String errorType,
			@RequestParam(value = "startTime") String startTime,
			@RequestParam(value = "endTime") String endTime,
			@RequestParam(value = "numberSerie") String numberSerie,
			@RequestParam(value = "pageNumber") Integer pageNumber,
			@RequestParam(value = "pageSize") Integer pageSize,
			@RequestParam(value = "resultType") String resultType
			) {
		LOGGER.info("BOSS VERSION:"+bossVersion);
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("hlrsn", hlrsn);
		paraMap.put("operationName", operationName);
		paraMap.put("errorType", errorType);
		paraMap.put("startTime", startTime);
		paraMap.put("endTime", endTime);
		paraMap.put("numberSerie", numberSerie);
		paraMap.put("pageNumber", pageNumber);
		paraMap.put("pageSize", pageSize);
		if(resultType.equals("all")||resultType.isEmpty()||resultType.equals("undefined")){
			paraMap.put("resultType", "all");
		}else{
			paraMap.put("resultType", resultType);
		}
		LOGGER.info("QUERY CONDITIONS:");
		System.out.println(paraMap.toString());
		return bossRevolutionService.getBossQueryDataAndCount(paraMap,bossVersion);
	}
	@RequestMapping(value = "/boss-rev/getDhssNameAndHlrsnMap")
	public List<Map<String, Object>> getDhssNameAndHlrsnMap() {
		LOGGER.info("BOSS VERSION:"+bossVersion);
		return bossRevolutionService.getDhssNameAndHlrsnMap(bossVersion);
	}

	@RequestMapping(value = "/boss-rev/getDefaultTimePeriod")
	public Map<String, String> getDefaultTimePeriod() {
		LOGGER.info("BOSS VERSION:"+bossVersion);
		return bossRevolutionService.getDefaultSelectOptionsTimePeriod();
	}
	@RequestMapping(value = "/boss-rev/getCurrentHourTimePeriod")
	public Map<String, String> getCurrentHourTimePeriod() {
		LOGGER.info("BOSS VERSION:"+bossVersion);
		return bossRevolutionService.getCurrentHourTimePeriod();
	}
	@RequestMapping(value = "/boss-rev/getOperationName")
	public Map<String, Object> getOperationName() {
		LOGGER.info("BOSS VERSION:"+bossVersion);
		return bossRevolutionService.getOperationName(bossVersion);
	}
	@RequestMapping(value = "/boss-rev/getOperationName/update",method=RequestMethod.POST)
	public void updateOperationName(
			@RequestParam(value = "operationName",required=false) String operationName,
			@RequestParam(value = "businessType",required=false) String businessType,
			@RequestParam(value = "id",required=false) Integer id) {
		Map<String,Object> valueMap = new HashMap<>();
		valueMap.put("operationName", operationName);
		valueMap.put("businessType", businessType);
		valueMap.put("id", id);
		bossRevolutionService.updateOperationName(bossVersion, valueMap);
	}
	@RequestMapping(value = "/boss-rev/getOperationName/create",method=RequestMethod.POST)
	public void createOperationName(
			@RequestParam(value = "operationName",required=false) String operationName,
			@RequestParam(value = "businessType",required=false) String businessType) {
		Map<String,Object> valueMap = new HashMap<>();
		valueMap.put("operationName", operationName);
		valueMap.put("businessType", businessType);
		bossRevolutionService.createOperationName(bossVersion, valueMap);
	}
	@RequestMapping(value = "/boss-rev/getOperationName/destroy",method=RequestMethod.POST)
	public void destroyOperationName(@RequestParam(value = "id",required=false) Integer id) {
		bossRevolutionService.destroyOperationName(bossVersion, id);
	}

	@RequestMapping(value = "/boss-rev/getErrorType")
	public Map<String, Object> getErrorType() {
		LOGGER.info("BOSS VERSION:"+bossVersion);
		return bossRevolutionService.getErrorType(bossVersion);
	}
	@RequestMapping(value = "/boss-rev/getErrorType/create",method=RequestMethod.POST)
	public void createErrorType(
			@RequestParam(value = "errorCode",required=false) String errorCode,
			@RequestParam(value = "errorCodeDesc",required=false) String errorCodeDesc
			) {
			Map<String,Object> valueMap = new HashMap<>();
			valueMap.put("errorCode", errorCode);
			valueMap.put("errorCodeDesc", errorCodeDesc);
			bossRevolutionService.createErrorType(bossVersion, valueMap);
		}
	@RequestMapping(value = "/boss-rev/getErrorType/update",method=RequestMethod.POST)
	public void updateErrorType(
			@RequestParam(value = "errorCode",required=false) String errorCode,
			@RequestParam(value = "errorCodeDesc",required=false) String errorCodeDesc,
			@RequestParam(value = "id",required=false) Integer id
			) {
		Map<String,Object> valueMap = new HashMap<>();
		valueMap.put("errorCode", errorCode);
		valueMap.put("errorCodeDesc", errorCodeDesc);
		valueMap.put("id", id);
		bossRevolutionService.updateErrorType(bossVersion, valueMap);
	}
	@RequestMapping(value = "/boss-rev/getErrorType/destroy",method=RequestMethod.POST)
	public void destroyErrorType(
			@RequestParam(value = "id",required=false) Integer id) {
		bossRevolutionService.destroyErrorType(bossVersion, id);
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/boss-rev/getErrorTypeOnly")
	public List<Map<String, Object>> getErrorTypeOnly() {
		LOGGER.info("BOSS VERSION:"+bossVersion);
		return (List<Map<String, Object>>)bossRevolutionService.getErrorType(bossVersion).get("data");
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/boss-rev/getOperationNameOnly")
	public List<Map<String, Object>> getOperationNameOnly() {
		LOGGER.info("BOSS VERSION:"+bossVersion);
		return (List<Map<String, Object>>)bossRevolutionService.getOperationName(bossVersion).get("data");
	}
	@RequestMapping(value = "/boss-rev/getBossDataDetail")
	public Map<String, Object> getBossDataDetail(@RequestParam(value = "taskId", required = true) String taskId) {
		return bossRevolutionService.getBossDataDetail(taskId,bossVersion);
	}
	@RequestMapping(value = "/boss-rev/exportDataToExcel")
	public void exportDataToExcel(
			@RequestParam(value = "hlrsn") String hlrsn,
			@RequestParam(value = "operationName") String operationName,
			@RequestParam(value = "errorType") String errorType,
			@RequestParam(value = "startTime") String startTime,
			@RequestParam(value = "endTime") String endTime,
			@RequestParam(value = "numberSerie") String numberSerie,
			@RequestParam(value = "resultType") String resultType,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
			Map<String, Object> paraMap = new HashMap<>();
			if(hlrsn.equals("undefined")){
				hlrsn="";
			}
			paraMap.put("hlrsn", hlrsn);
			paraMap.put("operationName", operationName);
			paraMap.put("errorType", errorType);
			paraMap.put("startTime", startTime);
			paraMap.put("endTime", endTime);
			paraMap.put("numberSerie", numberSerie);
			if(resultType.equals("all")||resultType.isEmpty()||resultType.equals("undefined")){
				paraMap.put("resultType", "all");
			}else{
				paraMap.put("resultType", resultType);
			}
			LOGGER.info("BOSS VERSION:"+bossVersion);
			List<Map<String,Object>> resultData = bossRevolutionService.getBossExportDataByCondition(paraMap,bossVersion);
			writeAndDownloadBossExcel(request, response, resultData);
	}

	public void writeAndDownloadBossExcel(HttpServletRequest request, HttpServletResponse response,
			List<Map<String, Object>> resultData) throws FileNotFoundException, IOException {
		XSSFWorkbook bossExcelReport = new XSSFWorkbook();
		XSSFSheet firstSheet = bossExcelReport.createSheet();
		String[] headers = {"请求ID","响应时间","HLRSN","MSISDN","IMSI","指令名称","执行结果"};
		String timeStr = SDF.format(new Date());
		XSSFRow header = firstSheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			XSSFCell c = header.createCell(i);
			c.setCellValue(headers[i]);
		}
		for(int startRow = 1;startRow <= resultData.size();startRow++){
			try{
				XSSFRow dataRow = firstSheet.createRow(startRow);
				Map<String,Object> dataMap = resultData.get(startRow-1);
				String taskId = String.valueOf(dataMap.get("task_id"));
				String responseTime = String.valueOf(dataMap.get("response_time"));
				String HLRSN = String.valueOf(dataMap.get("hlrsn"));
				String msisdn = String.valueOf(dataMap.get("msisdn"));
				String imsi = String.valueOf(dataMap.get("imsi"));
				String operationName = String.valueOf(dataMap.get("operation_name"));
				String callBackResult = String.valueOf(dataMap.get("callback_result"));
				XSSFCell taskIdCell = dataRow.createCell(0);
				taskIdCell.setCellValue(taskId);
				XSSFCell responseTimeCell = dataRow.createCell(1);
				responseTimeCell.setCellValue(responseTime);
				XSSFCell HLRSNCell = dataRow.createCell(2);
				HLRSNCell.setCellValue(HLRSN);
				XSSFCell msisdnCell = dataRow.createCell(3);
				msisdnCell.setCellValue(msisdn);
				XSSFCell imsiCell = dataRow.createCell(4);
				imsiCell.setCellValue(imsi);
				XSSFCell operationNameCell = dataRow.createCell(5);
				operationNameCell.setCellValue(operationName);
				XSSFCell callBackResultCell = dataRow.createCell(6);
				callBackResultCell.setCellValue(callBackResult 	);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
		for (int i = 0; i < headers.length; i++) {
			firstSheet.autoSizeColumn(i);
		}
		FileOutputStream out = new FileOutputStream(bossExcelGenPath+"BOSS_data_report_"+timeStr+".xlsx");
		bossExcelReport.write(out);
		bossExcelReport.close();
		try {
			download(request, response, bossExcelGenPath+"BOSS_data_report_"+timeStr+".xlsx", "application/octet-stream", "BOSS_data_report_"+timeStr+".xlsx");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     * 下载报表
     * @param request
     * @param response
     * @param downLoadPath
     * @param contentType
     * @param realName
     * @throws Exception
     */
    public static void download(HttpServletRequest request,HttpServletResponse response, String downLoadPath,
			String contentType, String realName) throws Exception {
		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		File file = new File(downLoadPath);
		if(!file.exists()){
			file.createNewFile();
		}
		response.setContentType(contentType);
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(realName.getBytes("utf-8"), "GBK"));
		response.setHeader("Content-Length", String.valueOf(file.length()));
		bis = new BufferedInputStream(new FileInputStream(file));
		bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		bis.close();
		bos.close();
	}
	@RequestMapping(value = "/boss-rev/getBossKpi")
	public List<Map<String, Object>> getBossKpi(
			@RequestParam(value = "hlrsn", required = true) String hlrsn,
			@RequestParam(value = "startTime", required = true) String startTime,
			@RequestParam(value = "endTime", required = true) String endTime,
			@RequestParam(value = "businessType", required = true) String businessType,
			@RequestParam(value = "timeFineness", required = true) String timeFineness
			) {
		LOGGER.info("BOSS VERSION:"+bossVersion);
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("hlrsn", hlrsn);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		paramMap.put("businessType", businessType);
		paramMap.put("timeFineness", timeFineness);
		return bossRevolutionService.getBossKpi(paramMap,bossVersion);
	}
	@RequestMapping(value = "/boss-rev/getBossKpiTimePeriodSelection")
	public Map<String, String> getBossKpiTimePeriodSelection(@RequestParam(value = "timeFitness", required = true) String timeFitness){
		Map<String, String> m = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		m.put("endTime", sdf.format(new Date()));
		Calendar calendar = Calendar.getInstance();
		if(timeFitness.equals("15min")){
			calendar.add(Calendar.HOUR, -1);
			Date fifteenMinAgo = calendar.getTime();
			m.put("startTime", sdf.format(fifteenMinAgo));
		}
		if(timeFitness.equals("1hour")){
			calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);
			Date oneHourAgo = calendar.getTime();
			m.put("startTime", sdf.format(oneHourAgo));
		}
		if(timeFitness.equals("1day")){
			calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -1);
			Date oneDayAgo = calendar.getTime();
			m.put("startTime", sdf.format(oneDayAgo));
		}
		if(timeFitness.equals("1month")){
			calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR, -1);
			Date oneMonthAgo = calendar.getTime();
			m.put("startTime", sdf.format(oneMonthAgo));
		}
		return m;
	}
}
