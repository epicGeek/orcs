package com.nokia.ices.apps.fusion.score.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nokia.ices.apps.fusion.score.common.utils.DateUtils;
import com.nokia.ices.apps.fusion.score.common.utils.ExportColumns;
import com.nokia.ices.apps.fusion.score.common.utils.ExportCsv;
import com.nokia.ices.apps.fusion.score.common.utils.FileOperateUtil;
import com.nokia.ices.apps.fusion.score.common.utils.ZipCompressUtil;
import com.nokia.ices.apps.fusion.score.domain.IndexScoringRule;
import com.nokia.ices.apps.fusion.score.service.AreaScoreService;
import com.nokia.ices.apps.fusion.score.service.AreaService;
import com.nokia.ices.apps.fusion.score.service.IndexScoringRuleService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;

import hirondelle.date4j.DateTime;

@RestController
public class AreaScoreController {
	@Autowired
	AreaScoreService areaScoreService;
	private final static Logger logger = LoggerFactory.getLogger(AreaScoreController.class);
	private final static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddhhmmssSSS");

	@Autowired
	IndexScoringRuleService indexScoringRuleService;
	
	@Autowired
	AreaService areaService;	

	@RequestMapping(value = "/getAreaScore", method = RequestMethod.GET)
	public Map<String, Object> getAreaScore(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		String areaCode = null == parameterMap.get("areaCode") ? "" : parameterMap.get("areaCode")[0];
		String cityCode = null == parameterMap.get("cityCode") ? "" : parameterMap.get("cityCode")[0];
		String scoreType = null == parameterMap.get("scoreType") ? "" : parameterMap.get("scoreType")[0];
		String startDate = null == parameterMap.get("startDate") ? "" : parameterMap.get("startDate")[0];
		String endDate = null == parameterMap.get("endDate") ? "" : parameterMap.get("endDate")[0];
		String dataType = null == parameterMap.get("dataType") ? "" : parameterMap.get("dataType")[0];
		String page = null == parameterMap.get("page") ? "1" : parameterMap.get("page")[0];
		String pageSize = null == parameterMap.get("pageSize") ? "10" : parameterMap.get("pageSize")[0];
		Map<String, Object> params = new HashMap<String, Object>();
		//获取地区权限
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
		params.put("areaCode", areaCode);
		params.put("cityCode", cityCode);
		params.put("scoreType", scoreType);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("dataType", dataType);
		params.put("limit", pageSize);
		params.put("offset", (Integer.valueOf(page) - 1) * Integer.valueOf(pageSize));
		return areaScoreService.queryAllAreaScore(params);

	}

	/**
	 * 故障评分统计
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getBreakDownReason", method = RequestMethod.GET)
	public Map<String, Object> getBreakDownReason(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();

		String scoreType = null == parameterMap.get("scoreType") ? "" : parameterMap.get("scoreType")[0];
		String startDate = null == parameterMap.get("startDate") ? "" : parameterMap.get("startDate")[0];
		String endDate = null == parameterMap.get("endDate") ? "" : parameterMap.get("endDate")[0];
		String cityCode = null == parameterMap.get("cityCode") ? "" : parameterMap.get("cityCode")[0];
		String areaCode = null == parameterMap.get("areaCode") ? "" : parameterMap.get("areaCode")[0];

		String page = null == parameterMap.get("page") ? "1" : parameterMap.get("page")[0];
		String pageSize = null == parameterMap.get("pageSize") ? "10" : parameterMap.get("pageSize")[0];
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("scoreType", scoreType);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		List<IndexScoringRule> indexList = indexScoringRuleService.findIndexScoringRuleAll();
		List<String> kpiColumns = new ArrayList<String>();
		for (IndexScoringRule isr : indexList) {
			kpiColumns.add(isr.getKpiId().toLowerCase());
		}
		params.put("list", kpiColumns); // 小时
		params.put("limit", pageSize);
		params.put("cityCode", cityCode);
		params.put("areaCode", areaCode);
		params.put("offset", (Integer.valueOf(page) - 1) * Integer.valueOf(pageSize));
		return areaScoreService.queryAllBreakReason(params);

	}

	/**
	 * 评分故障原因统计 导出
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportBreakReason")
	public void exportBreakReason(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		Map<String, String[]> parameterMap = request.getParameterMap();

		String scoreType = null == parameterMap.get("scoreType") ? "" : parameterMap.get("scoreType")[0];
		String startDate = null == parameterMap.get("startDate") ? "" : parameterMap.get("startDate")[0];
		String endDate = null == parameterMap.get("endDate") ? "" : parameterMap.get("endDate")[0];
		String cityCode = null == parameterMap.get("cityCode") ? "" : parameterMap.get("cityCode")[0];
		String areaCode = null == parameterMap.get("areaCode") ? "" : parameterMap.get("areaCode")[0];
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("scoreType", scoreType);
		params.put("cityCode", cityCode);
		params.put("areaCode", areaCode);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		
		Object[] showColumnArray;
		Object[] keyArray;
		String extName = "";
		List<String> kpiIdlist = Lists.newArrayList();
		List<String> kpiNamelist = Lists.newArrayList();
		List<IndexScoringRule> indexList = indexScoringRuleService.findIndexScoringRuleAll();
		for (IndexScoringRule isr : indexList) {
			kpiIdlist.add(isr.getKpiId().toLowerCase());
			kpiNamelist.add(isr.getCnName()+"占比");
		}
		params.put("list", kpiIdlist);
		if ("3".equals(scoreType)) {
			extName = "week";
			List<String> weekList = Lists.newArrayList(ExportColumns.FAULT_ANALYSIS_WEEK);
			weekList.addAll(kpiIdlist);
			List<String> showList = Lists.newArrayList(ExportColumns.FAULT_ANALYSIS_WEEK_HEADER);
			keyArray = weekList.toArray();
			showList.addAll(kpiNamelist);
			showColumnArray = showList.toArray();
			
			/*// 获取开始时间、结束时间所在的周
			String weekStart = DateUtils.getFirstDayOfWeek(startDate).toString();
			if (startDate.equals(endDate)) {
				params.put("curWeek", weekStart);
			} else {
				// 获取开始时间所在的周一，结束时间所在的周一
				String weekEnd = DateUtils.getFirstDayOfWeek(endDate).toString();
				params.put("weekStart", weekStart);
				params.put("weekEnd", weekEnd);
			}*/

		} else if ("4".equals(scoreType)) {
			extName = "month";
			List<String> monthList = Lists.newArrayList(ExportColumns.FAULT_ANALYSIS_MONTH);
			monthList.addAll(kpiIdlist);
			List<String> showList = Lists.newArrayList(ExportColumns.FAULT_ANALYSIS_MONTH_HEADER);
			keyArray = monthList.toArray();
			showList.addAll(kpiNamelist);
			showColumnArray = showList.toArray();

			/*keyArray = ExportColumns.FAULT_ANALYSIS_MONTH;
			showColumnArray = ExportColumns.FAULT_ANALYSIS_MONTH_HEADER;
			// 获取开始时间所在的月、结束时间所在的月
			DateTime startDay = new DateTime(startDate);
			String monthStart = startDay.getStartOfMonth().toString();

			if (startDate.equals(endDate)) {
				params.put("curMonth", monthStart);
			} else {
				DateTime endDay = new DateTime(endDate);
				String monthEnd = endDay.getStartOfMonth().toString();
				params.put("monthStart", monthStart);
				params.put("monthEnd", monthEnd);
			}*/
			
		} else if ("1".equals(scoreType)) {
				extName = "hour";
				List<String> hourList = Lists.newArrayList(ExportColumns.FAULT_ANALYSIS_HOUR);
				hourList.addAll(kpiIdlist);
				List<String> showList = Lists.newArrayList(ExportColumns.FAULT_ANALYSIS_HOUR_HEADER);
				keyArray = hourList.toArray();
				// keyArray = new String[];
				// System.arraycopy(obj, 0, keyArray, 0, obj.length);
				showList.addAll(kpiNamelist);
				showColumnArray = showList.toArray();

			} else {
				extName = "day";
				List<String> dayList = Lists.newArrayList(ExportColumns.FAULT_ANALYSIS_DAY);
				dayList.addAll(kpiIdlist);
				List<String> showList = Lists.newArrayList(ExportColumns.FAULT_ANALYSIS_DAY_HEADER);
				keyArray = dayList.toArray();
				showList.addAll(kpiNamelist);
				showColumnArray = showList.toArray();
					/*if (startDate.equals(endDate)) {
					params.put("curDate", startDate);
				} else {
					params.put("startDate", startDate);
					params.put("endDate", endDate);
				}*/
			}

		params.put("isExport", 1);
		
		ExportCsv ec = new ExportCsv();
		File csvFile = null;
		BufferedWriter csvFileOutputStream = null;
		String bossPath = FileOperateUtil.getPropertieValue("iceslte.kpi.file.path");
		/*String sDate = startDate.split(":")[0];
		String eDate = endDate.split(":")[0];*/
		//String fileName = "breakReason-" + sDate + "~" + eDate + "(" + extName + ")";
		String fileName = "breakReason-" + sdfDate.format(new Date())+ "(" + extName + ")";
		String path = bossPath + fileName + ".csv";
		String zipPath = bossPath + fileName + ".zip";
		File zipFile = null;
		String zipUrl = zipPath;
		try {
			File operationDir = new File(bossPath);
			csvFile = new File(path);
			zipFile = new File(zipPath);
			if (!operationDir.exists() && !operationDir.isDirectory()) {
				operationDir.mkdir();
			}

			if (!zipFile.exists()) {
				Map<String, Object> map = areaScoreService.queryAllBreakReason(params);
				List<Map<String, Object>> result = (List<Map<String, Object>>) map.get("rows");
			 
				Map<String, String> manufacturer = Maps.newHashMap();
				manufacturer.put("0", "诺基亚");
				manufacturer.put("1", "华为");
				manufacturer.put("2", "中兴");
				manufacturer.put("3", "大唐");
				manufacturer.put("4", "普天");
				manufacturer.put("5", "全部厂家");	
 
				Map<String, Map<String, String>> mapList = Maps.newHashMap();
				mapList.put("manufacturer", manufacturer);
				
				// 生成文件
				csvFile.createNewFile();
				csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 2048);
				ec.createCSVFile(result, showColumnArray, keyArray, csvFileOutputStream, mapList);
				// 压缩
				zipUrl = ZipCompressUtil.compress(new String[] { path }, bossPath, fileName + ".zip");
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

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportAreaScore")
	public void exportAreaScore(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		Map<String, String[]> parameterMap = request.getParameterMap();

		String areaCode = null == parameterMap.get("areaCode") ? "" : parameterMap.get("areaCode")[0];
		String cityCode = null == parameterMap.get("cityCode") ? "" : parameterMap.get("cityCode")[0];
		String scoreType = null == parameterMap.get("scoreType") ? "" : parameterMap.get("scoreType")[0];
		String startDate = null == parameterMap.get("startDate") ? "" : parameterMap.get("startDate")[0];
		String endDate = null == parameterMap.get("endDate") ? "" : parameterMap.get("endDate")[0];
		String dataType = null == parameterMap.get("dataType") ? "" : parameterMap.get("dataType")[0];

		Map<String, Object> params = new HashMap<String, Object>();
		String extName2 = "";
		if (!areaCode.isEmpty()) {
			extName2 += areaCode;
		}
		if (!cityCode.isEmpty()) {
			extName2 += cityCode;
		}
		//获取地区权限
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
		params.put("areaCode", areaCode);
		params.put("cityCode", cityCode);
		params.put("scoreType", scoreType);
		params.put("dataType", dataType);

		Map<String, String> areas = new HashMap<String, String>();
		areas.put("0", "全省");
		Map<String, String> citys = new HashMap<String, String>();
		citys.put("0", "全省");
		citys.put("1", "全市");
		Map<String, Map<String, String>> mapList = new HashMap<String, Map<String, String>>();
		mapList.put("area_name", areas);
		mapList.put("city_name", citys);
		String[] showColumnArray;
		String[] keyArray;
		String extName = "";
		if ("3".equals(scoreType)) {
			keyArray = ExportColumns.AREA_SCORE_WEEK;
			showColumnArray = ExportColumns.AREA_SCORE_WEEK_HEADER;
			// 获取开始时间、结束时间所在的周
			String weekStart = DateUtils.getFirstDayOfWeek(startDate).toString();
			String wDate = weekStart.split(":")[0];

			if (startDate.equals(endDate)) {
				params.put("curWeek", weekStart);
				extName = wDate + "week";
			} else {
				// 获取开始时间所在的周一，结束时间所在的周一
				String weekEnd = DateUtils.getFirstDayOfWeek(endDate).toString();
				String eDate = weekEnd.split(":")[0];
				params.put("weekStart", weekStart);
				params.put("weekEnd", weekEnd);
				extName = wDate + "~" + eDate + "-week";
			}

		} else if ("4".equals(scoreType)) {
			extName = "-month";
			keyArray = ExportColumns.AREA_SCORE_MONTH;
			showColumnArray = ExportColumns.AREA_SCORE_MONTH_HEADER;
			// 获取开始时间所在的月、结束时间所在的月
			DateTime startDay = new DateTime(startDate);
			Integer yearStart = startDay.getYear();
			String monthStart = startDay.getStartOfMonth().toString();
			// 获取开始时间所在的月、结束时间所在的月
			if (startDate.equals(endDate)) {
				params.put("curMonth", monthStart);
				extName = yearStart + "-" + startDay.getMonth() + "-month";
			} else {
				DateTime endDay = new DateTime(endDate);
				Integer yearEnd = endDay.getYear();
				String monthEnd = endDay.getStartOfMonth().toString();
				params.put("monthStart", monthStart);
				params.put("monthEnd", monthEnd);
				extName = yearStart + "-" + startDay.getMonth() + "~" + yearEnd + "-" + endDay.getMonth() + "-month";
			}
		} else {
			String sDate = startDate.split(":")[0];
			String eDate = endDate.split(":")[0];
			extName = sDate + "~" + eDate;
			if ("1".equals(scoreType)) {
				extName += "-hour";
				keyArray = ExportColumns.AREA_SCORE_HOUR;
				showColumnArray = ExportColumns.AREA_SCORE_HOUR_HEADER;
			} else {
				extName += "-day";
				keyArray = ExportColumns.AREA_SCORE_DAY;
				showColumnArray = ExportColumns.AREA_SCORE_DAY_HEADER;
			}
			if (startDate.equals(endDate)) {
				params.put("curDate", startDate);
			} else {
				params.put("startDate", startDate);
				params.put("endDate", endDate);
			}

		}
		params.put("isExport", 1);
	
		ExportCsv ec = new ExportCsv();
		File csvFile = null;
		BufferedWriter csvFileOutputStream = null;
		String bossPath = FileOperateUtil.getPropertieValue("iceslte.kpi.file.path");
		String fileName = "areaScore_"+ "(" + extName + ")"+extName2 ;
		String path = bossPath + fileName + ".csv";
		String zipPath = bossPath + fileName + ".zip";
		File zipFile = null;
		String zipUrl = zipPath;
		try {
			File operationDir = new File(bossPath);
			csvFile = new File(path);
			zipFile = new File(zipPath);
			if (!operationDir.exists() && !operationDir.isDirectory()) {
				operationDir.mkdir();
			}
			if (!zipFile.exists()) {
				Map<String, Object> map = areaScoreService.queryAllAreaScore(params);
				List<Map<String, Object>> result = (List<Map<String, Object>>) map.get("rows");
				// 生成文件
				csvFile.createNewFile();
				csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 2048);
				ec.createCSVFile(result, showColumnArray, keyArray, csvFileOutputStream, mapList);
				// 压缩
				zipUrl = ZipCompressUtil.compress(new String[] { path }, bossPath, fileName + ".zip");
				csvFileOutputStream.close();
				csvFile.delete();
			}
			// 下载
			FileOperateUtil.download(response, zipUrl, "" + zipFile.length(), fileName + ".zip");
		} catch (Exception e) {
			logger.error("An error occurred while export method:exportBreakReason,error message:{}", e.getMessage());
            throw new RuntimeException(e);

		} finally {
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
