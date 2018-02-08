package com.nokia.ices.apps.fusion.score.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.nokia.ices.apps.fusion.score.common.utils.ExportColumns;
import com.nokia.ices.apps.fusion.score.common.utils.ExportCsv;
import com.nokia.ices.apps.fusion.score.common.utils.FileOperateUtil;
import com.nokia.ices.apps.fusion.score.common.utils.ZipCompressUtil;
import com.nokia.ices.apps.fusion.score.service.AlarmQueryService;
import com.nokia.ices.apps.fusion.score.service.AreaService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;

/**
 * 告警查询 、退服查询 -->数据查询
 * */

@RestController
public class AlarmQueryController {

	public static final Logger logger = LoggerFactory.getLogger(AlarmQueryController.class);
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	@Autowired
	AreaService areaService;

	@Autowired
	AlarmQueryService alarmQueryService;

	/*
	 * 告警查询
	 */
	@RequestMapping(value = "alarmQuery/search")
	@ResponseBody
	public Map<String, Object> getInformationList(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, 
																					@RequestParam("neCode") String neCode,@RequestParam("cityCode") String cityCode,
																					@RequestParam("manufacturer") String manufacturer,@RequestParam("areaCode") String areaCode,
																					@RequestParam("startDate") String startDate,@RequestParam("endDate") String endDate,
																					@RequestParam("alarmTitle") String alarmTitle,
																					@RequestParam("alarmDelay") String alarmDelay,@RequestParam("tableName") String tableName) {
		
		Map<String, Object> searchParams = new HashMap<String, Object>();
		//获取地区权限
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
		searchParams.put("page", (page - 1) * pageSize);
		searchParams.put("pageSize", pageSize);
		searchParams.put("startDate", startDate);
		searchParams.put("endDate", endDate);
		searchParams.put("neCode", neCode);
		searchParams.put("cityCode", cityCode);
		searchParams.put("areaCode", areaCode);
		searchParams.put("alarmTitle", alarmTitle);
		searchParams.put("manufacturer", manufacturer);
		searchParams.put("alarmDelay", alarmDelay);
		searchParams.put("tableName", tableName);
		return alarmQueryService.findAlarmQueryAll(searchParams);

	}
	
	/*
	 * 退服查询
	 */
	@RequestMapping(value = "/outOfQuery/search")
	public Map<String, Object> getOutOfList(@RequestBody ModelMap paramMap) {
		
		try{
			return alarmQueryService.findOutOfQueryAll(paramMap);
		}catch(Exception e){
			logger.debug("outOfQuery/search si error ........"+e.getMessage());
		}
		return null;
		
	}

	//告警查询 导出
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "alarmQuery/exportFile")
	public void saveSession(HttpServletRequest request, HttpServletResponse response) throws ParseException {

		response.setHeader("Pragma", "N o-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		Map<String, String[]> parameterMap = request.getParameterMap();

		String cityCode = null == parameterMap.get("cityCode") ? "" : parameterMap.get("cityCode")[0];
		String areaCode = null == parameterMap.get("areaCode") ? "" : parameterMap.get("areaCode")[0];
		String neCode = null == parameterMap.get("neCode") ? "" : parameterMap.get("neCode")[0];
		String startDate = null == parameterMap.get("startDate") ? "" : parameterMap.get("startDate")[0];
		String endDate = null == parameterMap.get("endDate") ? "" : parameterMap.get("endDate")[0];
		String alarmTitle = null == parameterMap.get("alarmTitle") ? "" : parameterMap.get("alarmTitle")[0];
		String manufacturer = null == parameterMap.get("manufacturer") ? "" : parameterMap.get("manufacturer")[0];
		String alarmDelay = null == parameterMap.get("alarmDelay") ? "" : parameterMap.get("alarmDelay")[0];
		String tableName = null == parameterMap.get("tableName") ? "" : parameterMap.get("tableName")[0];
		
		Map<String, Object> searchParams = new HashMap<String, Object>();
		
		if (StringUtils.isNotEmpty(tableName) && "null".equals(tableName)) {
			searchParams.put("tableName", tableName); 
		}
		if (StringUtils.isNotEmpty(areaCode) && "null".equals(areaCode)) {
			searchParams.put("areaCode", areaCode); // 区县
		}
		if (StringUtils.isNotEmpty(cityCode) && !"null".equals(cityCode)) {
			searchParams.put("cityCode", cityCode);// 地市名称
		}
		if (StringUtils.isNotEmpty(alarmTitle) && !"null".equals(alarmTitle)) {
			searchParams.put("alarmTitle", alarmTitle);
		}
		if (StringUtils.isNotEmpty(neCode) && !"null".equals(neCode)) {
			searchParams.put("neCode", neCode);// 基站ID
		}
		if (StringUtils.isNotEmpty(manufacturer) && !"null".equals(manufacturer)) {
			searchParams.put("manufacturer", manufacturer);
		}
		if (StringUtils.isNotEmpty(alarmDelay) && !"null".equals(alarmDelay)) {
			searchParams.put("alarmDelay", alarmDelay);
		}
		if (StringUtils.isNotEmpty(startDate) && !"null".equals(startDate)) {
			searchParams.put("startDate", startDate);// 开始时间
		}
		if (StringUtils.isNotEmpty(endDate) && !"null".equals(endDate)) {
			searchParams.put("endDate", endDate);// 结束时间
		}
		searchParams.put("startDate", startDate);
		searchParams.put("endDate", endDate);
		searchParams.put("neCode", neCode);
		searchParams.put("cityCode", cityCode);
		searchParams.put("areaCode", areaCode);
		searchParams.put("alarmTitle", alarmTitle);
		searchParams.put("manufacturer", manufacturer);
		searchParams.put("alarmDelay", alarmDelay);
		searchParams.put("tableName", tableName);
		
		String[] keyArray=null;
		String[] showColumnArray = null;
		String name="";
		if ("ices_alarm_monitor_delay".equalsIgnoreCase(tableName)) {
			
			keyArray = ExportColumns.ICES_ALARM_RECORD;
			showColumnArray = ExportColumns.ICES_ALARM_RECORD_HEADER;
			name="时长";
			
		} else if ("ices_alarm_monitor_frequency".equalsIgnoreCase(tableName)) {
			
			keyArray = ExportColumns.ICES_FRE_RECORD;
			showColumnArray = ExportColumns.ICES_FRE_RECORD_HEADER;
			name="频次";
		} 

		ExportCsv ec = new ExportCsv();
		File csvFile = null;
		BufferedWriter csvFileOutputStream = null;
		String alarmPath = FileOperateUtil.getPropertieValue("iceslte.kpi.file.path");
		String fileName = "告警-" +name+sdf.format(new Date());
		String path = alarmPath + fileName + ".csv";
		String zipPath = alarmPath + fileName + ".zip";
		File zipFile = null;
		String zipUrl = zipPath;
		try {
			File operationDir = new File(alarmPath);
			csvFile = new File(path);
			zipFile = new File(zipPath);
			if (!operationDir.exists() && !operationDir.isDirectory()) {
				operationDir.mkdir();
			}

			if (!zipFile.exists()) {
				
				Map<String, Object> map = alarmQueryService.findAlarmQueryAll(searchParams);
				List<Map<String, Object>> alarmQueryList = (List<Map<String, Object>>) map.get("content");
				Map<String, String> aa = Maps.newHashMap();
				aa.put("0", "诺基亚");
				aa.put("1", "华为");
				aa.put("2", "中兴");
				aa.put("3", "大唐");
				aa.put("4", "普天");
				aa.put("5", "全部厂家");	
				Map<String, Map<String, String>> mapList = Maps.newHashMap();
				mapList.put("manufacturer", aa);
				
				// 生成文件
				csvFile.createNewFile();
				csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 2048);
				ec.createCSVFile(alarmQueryList, showColumnArray, keyArray, csvFileOutputStream, mapList);
				// 压缩
				zipUrl = ZipCompressUtil.compress(new String[] { path }, alarmPath, fileName + ".zip");
				csvFileOutputStream.close();
				csvFile.delete();
			}

			// 下载
			FileOperateUtil.download(response, zipUrl, "" + zipFile.length(), fileName + ".zip");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("An error occurred while export method:exportBreakReason,error message:{}", e.getMessage());
		} finally {
			zipFile.delete();
			try {
				if (csvFileOutputStream != null) {
					csvFileOutputStream.close();
				}
			} catch (IOException e) {
				logger.error("An error occurred while invoke method:exportBreakReason,error message:{}", e.getMessage());
				e.printStackTrace();
			}
			csvFileOutputStream = null;

		}

	}
	
	//退服查询 导出
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "outOfQuery/outOfExportFile")
	public void saveSessionOutOf(HttpServletRequest request, HttpServletResponse response) throws ParseException {

		response.setHeader("Pragma", "N o-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		Map<String, String[]> parameterMap = request.getParameterMap();

		String cityCode = null == parameterMap.get("cityCode") ? "" : parameterMap.get("cityCode")[0];
		String areaCode = null == parameterMap.get("areaCode") ? "" : parameterMap.get("areaCode")[0];
		String neCode = null == parameterMap.get("neCode") ? "" : parameterMap.get("neCode")[0];
		String startDate = null == parameterMap.get("startDate") ? "" : parameterMap.get("startDate")[0];
		String endDate = null == parameterMap.get("endDate") ? "" : parameterMap.get("endDate")[0];
		String alarmTitle = null == parameterMap.get("alarmTitle") ? "" : parameterMap.get("alarmTitle")[0];
		String manufacturer = null == parameterMap.get("manufacturer") ? "" : parameterMap.get("manufacturer")[0];
		String alarmDelay = null == parameterMap.get("alarmDelay") ? "" : parameterMap.get("alarmDelay")[0];
		String tableName = null == parameterMap.get("tableName") ? "" : parameterMap.get("tableName")[0];
		
		Map<String, Object> searchParams = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(tableName) && "null".equals(tableName)) {
			searchParams.put("tableName", tableName); 
		}
		if (StringUtils.isNotEmpty(areaCode) && "null".equals(areaCode)) {
			searchParams.put("areaCode", areaCode); // 区县
		}
		if (StringUtils.isNotEmpty(cityCode) && !"null".equals(cityCode)) {
			searchParams.put("cityCode", cityCode);// 地市名称
		}
		if (StringUtils.isNotEmpty(alarmTitle) && !"null".equals(alarmTitle)) {
			searchParams.put("alarmTitle", alarmTitle);
		}
		if (StringUtils.isNotEmpty(neCode) && !"null".equals(neCode)) {
			searchParams.put("neCode", neCode);// 基站ID
		}
		if (StringUtils.isNotEmpty(manufacturer) && !"null".equals(manufacturer)) {
			searchParams.put("manufacturer", manufacturer);
		}
		if (StringUtils.isNotEmpty(alarmDelay) && !"null".equals(alarmDelay)) {
			searchParams.put("alarmDelay", alarmDelay);
		}
		if (StringUtils.isNotEmpty(startDate) && !"null".equals(startDate)) {
			searchParams.put("startDate", startDate);// 开始时间
		}
		if (StringUtils.isNotEmpty(endDate) && !"null".equals(endDate)) {
			searchParams.put("endDate", endDate);// 结束时间
		}
		searchParams.put("startDate", startDate);
		searchParams.put("endDate", endDate);
		searchParams.put("neCode", neCode);
		searchParams.put("cityCode", cityCode);
		searchParams.put("areaCode", areaCode);
		searchParams.put("alarmTitle", alarmTitle);
		searchParams.put("manufacturer", manufacturer);
		searchParams.put("alarmDelay", alarmDelay);
		searchParams.put("tableName", tableName);
		
		String[] keyArray=null;
		String[] showColumnArray = null;
		String name="";
		if ("ices_alarm_out_of_delay".equalsIgnoreCase(tableName)) {
			
			keyArray = ExportColumns.ICES_ALARM_RECORD;
			showColumnArray = ExportColumns.ICES_ALARM_RECORD_HEADER;
			name="时长";
			
		} else if ("ices_alarm_out_of_frequency".equalsIgnoreCase(tableName)) {
			
			keyArray = ExportColumns.ICES_FRE_RECORD;
			showColumnArray = ExportColumns.ICES_FRE_RECORD_HEADER;
			name="频次";
		} 

		ExportCsv ec = new ExportCsv();
		File csvFile = null;
		BufferedWriter csvFileOutputStream = null;
		String alarmPath = FileOperateUtil.getPropertieValue("iceslte.kpi.file.path");
		String fileName = "退服-" +name+sdf.format(new Date());
		String path = alarmPath + fileName + ".csv";
		String zipPath = alarmPath + fileName + ".zip";
		File zipFile = null;
		String zipUrl = zipPath;
		try {
			File operationDir = new File(alarmPath);
			csvFile = new File(path);
			zipFile = new File(zipPath);
			if (!operationDir.exists() && !operationDir.isDirectory()) {
				operationDir.mkdir();
			}

			if (!zipFile.exists()) {
				
				Map<String, Object> map = alarmQueryService.findOutOfQueryAll(searchParams);
				List<Map<String, Object>> outOfQueryList = (List<Map<String, Object>>) map.get("content");
				Map<String, String> aa = Maps.newHashMap();
				aa.put("0", "诺基亚");
				aa.put("1", "华为");
				aa.put("2", "中兴");
				aa.put("3", "大唐");
				aa.put("4", "普天");
				aa.put("5", "全部厂家");	
				Map<String, Map<String, String>> mapList = Maps.newHashMap();
				mapList.put("manufacturer", aa);
				
				// 生成文件
				csvFile.createNewFile();
				csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 2048);
				ec.createCSVFile(outOfQueryList, showColumnArray, keyArray, csvFileOutputStream, mapList);
				// 压缩
				zipUrl = ZipCompressUtil.compress(new String[] { path }, alarmPath, fileName + ".zip");
				csvFileOutputStream.close();
				csvFile.delete();
			}

			// 下载
			FileOperateUtil.download(response, zipUrl, "" + zipFile.length(), fileName + ".zip");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("An error occurred while export method:exportBreakReason,error message:{}", e.getMessage());
		} finally {
			zipFile.delete();
			try {
				if (csvFileOutputStream != null) {
					csvFileOutputStream.close();
				}
			} catch (IOException e) {
				logger.error("An error occurred while invoke method:exportBreakReason,error message:{}", e.getMessage());
				e.printStackTrace();
			}
			csvFileOutputStream = null;

		}

	}
		
}
