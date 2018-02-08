package com.nokia.ices.apps.fusion.score.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.nokia.ices.apps.fusion.score.common.utils.ExportColumns;
import com.nokia.ices.apps.fusion.score.common.utils.ExportCsv;
import com.nokia.ices.apps.fusion.score.common.utils.FileOperateUtil;
import com.nokia.ices.apps.fusion.score.common.utils.ZipCompressUtil;
import com.nokia.ices.apps.fusion.score.service.AlarmDelayAndFrequencyService;
import com.nokia.ices.apps.fusion.score.service.AreaService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;

/**
 * 告警时长+频次统计 + 时长统计图形（总体呈现）
 * @author Administrator
 *
 */
@RequestMapping("/alarm")
@RestController
public class AlarmDelayAndFrequencyController {

	public static final Logger logger = LoggerFactory.getLogger(AlarmDelayAndFrequencyController.class);
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	@Autowired
	AreaService areaService;
	
	@Autowired
	AlarmDelayAndFrequencyService alarmService;
	
	
	//时长统计 图
    @RequestMapping(value = "/DelaySearchChart")
    public Map<String,Object> getAlarmDelayListChart( @RequestParam("startDate")String startDate,
																						    @RequestParam("endDate")String endDate,
																						    @RequestParam("areaCode")String areaCode,
																						    @RequestParam("tableName")String tableName) {
    	Map<String,Object> searchParams = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(areaCode)){
			searchParams.put("type", "city_code");
		}else{
			searchParams.put("type", "area_code");
		}
		//获取地区权限
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
        searchParams.put("areaCode", areaCode);
        searchParams.put("startDate", startDate);
        searchParams.put("endDate", endDate);
        searchParams.put("tableName", tableName);
       return  alarmService.findAlarmDelayAllData(searchParams);
        
    }
    
  //时长 列表（总时长）+ 频次  列表（总次数） 
    @RequestMapping(value = "/getTotalDelay")
    public Double getTotalDelay(@RequestParam("areaCode")String areaCode,
																			    @RequestParam("cityCode")String cityCode,
																			    @RequestParam("startDate")String startDate,
																			    @RequestParam("endDate")String endDate,
																			    @RequestParam("tableName")String tableName,
																			    @RequestParam("sumType")String sumType) {
    	String type = "0";
    	if(StringUtils.isNotEmpty(cityCode)){
    		type = "city_code";
    	}else  if(StringUtils.isNotEmpty(areaCode)){
    		type = "area_code";
    	}
 
        Map<String,Object> searchParams = new HashMap<String,Object>();
        searchParams.put("areaCode", areaCode);
        searchParams.put("cityCode", cityCode);
        searchParams.put("tableName", tableName);
        searchParams.put("startDate", startDate);
        searchParams.put("endDate", endDate);
        searchParams.put("areaType", type);
        searchParams.put("sumType", sumType);
       return  alarmService.findAlarmDelaySum(searchParams);
        
    }
    
	//时长 列表
    @RequestMapping(value = "/DelaySearch")
    public Map<String,Object> getAlarmDelayList(@RequestParam("page") Integer page, 
								    											@RequestParam("pageSize") Integer pageSize,
																			    @RequestParam("alarmDelay") String alarmDelay,
																			    @RequestParam("neCode") String neCode,
																			    @RequestParam("alarmNo") String alarmNo,
																			    @RequestParam("areaCode")String areaCode,
																			    @RequestParam("cityCode")String cityCode,
																			    @RequestParam("startDate")String startDate,
																			    @RequestParam("endDate")String endDate,
																			    @RequestParam("manufacturer")String manufacturer,
																			    @RequestParam("tableName")String tableName) {
    	String type = "0";
    	if(StringUtils.isNotEmpty(cityCode)){
    		type = "city_code";
    	}else  if(StringUtils.isNotEmpty(areaCode)){
    		type = "area_code";
    	}else{
    	}
    	//获取地区权限
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
        Map<String,Object> searchParams = new HashMap<String,Object>();
        searchParams.put("page", (page-1)*pageSize);
        searchParams.put("pageSize", pageSize);
        searchParams.put("alarmDelay", alarmDelay); //时长
        searchParams.put("areaCode", areaCode);
        searchParams.put("cityCode", cityCode);
        searchParams.put("tableName", tableName);
        searchParams.put("neCode", neCode);
        searchParams.put("alarmNo", alarmNo);
        searchParams.put("startDate", startDate);
        searchParams.put("endDate", endDate);
        searchParams.put("manufacturer", manufacturer);
        searchParams.put("areaType", type);
       return  alarmService.findAlarmDelayAll(searchParams);
        
    }
    
    //频次
    @RequestMapping(value = "/freqSearch")
    public Map<String,Object> getAlarmFreqList(@RequestParam("page") Integer page, 
								@RequestParam("pageSize") Integer pageSize,
							    @RequestParam("alarmFreq") String alarmFreq,
							    @RequestParam("areaCode")String areaCode,
							    @RequestParam("cityCode")String cityCode,
							    @RequestParam("neCode")String neCode,
							    @RequestParam("startDate")String startDate,
							    @RequestParam("endDate")String endDate,
							    @RequestParam("manufacturer")String manufacturer,
							    @RequestParam("tableName")String tableName){
    	
	  //获取地区权限
  		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
  		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
        Map<String,Object> searchParams = new HashMap<String,Object>();
        searchParams.put("page", (page-1)*pageSize);
        searchParams.put("pageSize", pageSize);
        searchParams.put("alarmFreq", alarmFreq); //时长
        searchParams.put("areaCode", areaCode);
        searchParams.put("cityCode", cityCode);
        searchParams.put("neCode", neCode);
        searchParams.put("startDate", startDate);
        searchParams.put("endDate", endDate);
        searchParams.put("manufacturer", manufacturer);
        searchParams.put("tableName", tableName);
    	
        return alarmService.findAlarmFrequencyAll(searchParams);
    }
      
    // 时长列表  导出
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportAlarmFile")
	public void exportFileAlarmDelay( @RequestParam("alarmDelay") String alarmDelay,@RequestParam("neCode") String neCode,@RequestParam("alarmNo") String alarmNo,
			@RequestParam("areaCode")String areaCode,@RequestParam("cityCode")String cityCode,@RequestParam("startDate")String startDate,
			@RequestParam("endDate")String endDate,@RequestParam("manufacturer")String manufacturer,
			@RequestParam("tableName")String tableName,HttpServletRequest request, HttpServletResponse response) {
    	
    	response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		//获取地区权限
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
		
		if (StringUtils.isNotEmpty(areaCode) && !"null".equals(areaCode)) {
			searchParams.put("areaCode", areaCode);// 地市名称
		}
		if (StringUtils.isNotEmpty(cityCode) && !"null".equals(cityCode)) {
			searchParams.put("cityCode", cityCode); // 区县
		}
		if (StringUtils.isNotEmpty(neCode) && !"null".equals(neCode)) {
			searchParams.put("neCode", neCode); // 基站ID
		}
		if (StringUtils.isNotEmpty(alarmNo) && !"null".equals(alarmNo)) {
			searchParams.put("alarmNo", alarmNo); // 基站ID
		}
		if (StringUtils.isNotEmpty(alarmDelay) && !"null".equals(alarmDelay)) {
			searchParams.put("alarmDelay", alarmDelay);
		}
		if (StringUtils.isNotEmpty(manufacturer) && !"null".equals(manufacturer)) {
			  searchParams.put("manufacturer", manufacturer);
		}
		searchParams.put("startDate", startDate);
		searchParams.put("endDate", endDate);
		searchParams.put("tableName", tableName);
		
		BufferedWriter csvFileOutputStream = null;
		String sheetName = "";
		
		String[] keyArray= ExportColumns.ALARM_DELAY_DAY;
		String[] showColumnArray= ExportColumns.ALARM_DELAY__DAY_HEADER;
		
		if ("bts_alarm_delay_day".equalsIgnoreCase(tableName)) {
			 sheetName = "alarmDelayDay";
			 keyArray = ExportColumns.ALARM_DELAY_DAY;
			showColumnArray = ExportColumns.ALARM_DELAY__DAY_HEADER;
			
		} else if ("bts_alarm_delay_week".equalsIgnoreCase(tableName)) {
			 sheetName = "alarmDelayWeek";
			 keyArray = ExportColumns.ALARM_DELAY_WEEK;
			 showColumnArray =ExportColumns.ALARM_DELAY__WEEK_HEADER;
			
		} else if ("bts_alarm_delay_month".equalsIgnoreCase(tableName)) {
			sheetName = "alarmDelayMonth";
			keyArray =ExportColumns.ALARM_DELAY_MONTH;
			showColumnArray = ExportColumns.ALARM_DELAY__MONTH_HEADER;
		}
		
		ExportCsv ec = new ExportCsv();
		File csvFile = null;
		File zipFile=null;
		
		String alarmDelayPath = FileOperateUtil.getPropertieValue("iceslte.kpi.file.path");
		//String fileName = sheetName + "-" + startCycle.replace(":", "-") + "~" + endCycle.replace(":", "-") + extName + ".csv";
		//String fileZip = kpiName + "-" + startCycle.replace(":", "-") + "~" + endCycle.replace(":", "-") + extName + ".zip";
		String fileName = sheetName+"_"+ sdf.format(new Date()); //文件名称
		String fileNameUrl= alarmDelayPath+fileName+".csv";
		String zipUrl = alarmDelayPath + fileName+".zip";
		try {
			// 保存到本地
			File operationDir = new File(alarmDelayPath);
			zipFile = new File(zipUrl);
			csvFile = new File(fileNameUrl);

			if (!operationDir.exists() && !operationDir.isDirectory()) {
				operationDir.mkdir();
			}
			if (!zipFile.exists()) {
				
				Map<String, Object> map = alarmService.findAlarmDelayAll(searchParams);
				List<Map<String, Object>> result = (List<Map<String, Object>>) map.get("content");
			    Map<String, String> mr = Maps.newHashMap();
				mr.put("0", "诺基亚");
				mr.put("1", "华为");
				mr.put("2", "中兴");
				mr.put("3", "大唐");
				mr.put("4", "普天");
				mr.put("5", "全部厂家");	
				Map<String, Map<String, String>> mapList = Maps.newHashMap();
				mapList.put("manufacturer", mr);

				// 生成文件
				csvFile.createNewFile();
				csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 2048);
				ec.createCSVFile(result, showColumnArray, keyArray, csvFileOutputStream, mapList);
				// 压缩
				zipUrl = ZipCompressUtil.compress(new String[] { fileNameUrl }, alarmDelayPath, fileName+".zip");
				csvFileOutputStream.close();
				csvFile.delete();
			}
			// 导出excel
			FileOperateUtil.download(response, zipUrl, "" + zipFile.length(),fileName+".zip");

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
    
    //告警频次列表 导出
    @SuppressWarnings("unchecked")
   	@RequestMapping(value = "/frequencyExportFile")
   	public void exportFileAlarmFire( @RequestParam("alarmFreq") String alarmFreq,
   														@RequestParam("neCode") String neCode,@RequestParam("areaCode")String areaCode,
   														@RequestParam("cityCode")String cityCode,@RequestParam("startDate")String startDate,
   														@RequestParam("endDate")String endDate,@RequestParam("manufacturer")String manufacturer,
   														@RequestParam("tableName")String tableName,HttpServletRequest request, HttpServletResponse response) {
       	response.setHeader("Pragma", "No-cache");
   		response.setHeader("Cache-Control", "no-cache");
   		response.setDateHeader("Expires", 0);
   		Map<String, Object> searchParams = new HashMap<String, Object>();
   	   //获取地区权限
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
   		searchParams.put("cityCode", cityCode); // 区县
   		searchParams.put("areaCode", areaCode);// 地市名称
   		searchParams.put("neCode", neCode); // 基站ID
   		searchParams.put("alarmFreq", alarmFreq);
   		searchParams.put("startDate", startDate);
   		searchParams.put("endDate", endDate);
   		searchParams.put("tableName", tableName);
   	    searchParams.put("manufacturer", manufacturer);
   		
   		String sheetName = "";
   		String[] keyArray = ExportColumns.ALARM_FREQUENCY_DAY;
   		String[] showColumnArray = ExportColumns.ALARM_FREQUENCY__DAY_HEADER;
   		
   		if ("bts_alarm_frequency_day".equalsIgnoreCase(tableName)) {
   			 sheetName = "alarmFrequencyDay";
   			keyArray = ExportColumns.ALARM_FREQUENCY_DAY;
   	   		showColumnArray = ExportColumns.ALARM_FREQUENCY__DAY_HEADER;
   			
   		} else if ("bts_alarm_frequency_week".equalsIgnoreCase(tableName)) {
   			 sheetName = "alarmFrequencyWeek";
   			 keyArray = ExportColumns.ALARM_FREQUENCY_WEEK;
   			 showColumnArray =ExportColumns.ALARM_FREQUENCY_WEEK_HEADER;
   			
   		} else if ("bts_alarm_frequency_month".equalsIgnoreCase(tableName)) {
   			sheetName = "alarmFrequencyMonth";
   			keyArray =ExportColumns.ALARM_FREQUENCY_MONTH;
   			showColumnArray = ExportColumns.ALARM_FREQUENCY_MONTH_HEADER;
   		}
   		
   		BufferedWriter csvFileOutputStream = null;
   		ExportCsv ec = new ExportCsv();
   		File csvFile = null;
   		File zipFile =null;
   		String alarmDelayPath = FileOperateUtil.getPropertieValue("iceslte.kpi.file.path");
   		String fileName = sheetName+"_"+ sdf.format(new Date()); //文件名称
   		String fileNameUrl= alarmDelayPath+fileName+".csv";
   		String zipUrl = alarmDelayPath + fileName+".zip";
   		try {
   			// 保存到本地
   			File operationDir = new File(alarmDelayPath);
   			 zipFile = new File(zipUrl);
   			csvFile = new File(fileNameUrl);

   			if (!operationDir.exists() && !operationDir.isDirectory()) {
   				operationDir.mkdir();
   			}
   			if (!zipFile.exists()) {
   				
   				Map<String, Object> map = alarmService.findAlarmFrequencyAll(searchParams);
   				List<Map<String, Object>> result = (List<Map<String, Object>>) map.get("content");
   			    Map<String, String> mr = Maps.newHashMap();
   				mr.put("0", "诺基亚");
   				mr.put("1", "华为");
   				mr.put("2", "中兴");
   				mr.put("3", "大唐");
   				mr.put("4", "普天");
   				mr.put("5", "全部厂家");	
   				Map<String, Map<String, String>> mapList = Maps.newHashMap();
   				mapList.put("manufacturer", mr);

   				// 生成文件
   				csvFile.createNewFile();
   				csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 2048);
   				ec.createCSVFile(result, showColumnArray, keyArray, csvFileOutputStream, mapList);
   				// 压缩
   				zipUrl = ZipCompressUtil.compress(new String[] { fileNameUrl }, alarmDelayPath, fileName+".zip");
   				csvFileOutputStream.close();
   				csvFile.delete();
   			}
   			// 导出excel
   			FileOperateUtil.download(response, zipUrl, "" + zipFile.length(),fileName+".zip");
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
