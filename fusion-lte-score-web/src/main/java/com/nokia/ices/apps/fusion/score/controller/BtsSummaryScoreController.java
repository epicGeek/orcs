package com.nokia.ices.apps.fusion.score.controller;

import hirondelle.date4j.DateTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.score.common.utils.DateUtils;
import com.nokia.ices.apps.fusion.score.common.utils.ExportExcel;
import com.nokia.ices.apps.fusion.score.common.utils.FileOperateUtil;
import com.nokia.ices.apps.fusion.score.common.utils.ZipCompressUtil;
import com.nokia.ices.apps.fusion.score.service.AreaService;
import com.nokia.ices.apps.fusion.score.service.BtsSummaryScoreService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;

/**
 * 基站汇总评分查询
 * */
@RestController
public class BtsSummaryScoreController {

	public static final Logger logger = LoggerFactory.getLogger(BtsSummaryScoreController.class);

	@Autowired
	BtsSummaryScoreService btsScoreSumService;
	
	@Autowired
	AreaService areaService;
	
	@RequestMapping(value = "/btsSummaryScore/search")
	public Map<String, Object> getBtsScoreHourList(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize,
																					@RequestParam("areaCode") String areaCode,@RequestParam("cityCode") String cityCode,
																					@RequestParam("neCode") String neCode, @RequestParam("startCycleDate") String startCycleDate, 
																					@RequestParam("endCycleDate") String endCycleDate,@RequestParam("grade") String grade, 
																					@RequestParam("tableName") String tableName) throws ParseException {
	  //获取地区权限
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("page", (page - 1) * pageSize);
		searchParams.put("pageSize", pageSize);
		searchParams.put("cityCode", cityCode);// 地市名称
		searchParams.put("areaCode", areaCode); // 区县
		searchParams.put("neCode", neCode); // 基站ID
		searchParams.put("grade", grade);

		if ("bts_score_week".equals(tableName)) {// bts_score_week 3
			// 获取开始时间、结束时间所在的周
			String weekStart = DateUtils.getFirstDayOfWeek(startCycleDate).toString();
			if (startCycleDate.equals(endCycleDate)) {
				searchParams.put("curWeek", weekStart);
			} else {
				// 获取开始时间所在的周一，结束时间所在的周一
				String weekEnd = DateUtils.getFirstDayOfWeek(endCycleDate).toString();
				searchParams.put("weekStart", weekStart);
				searchParams.put("weekEnd", weekEnd);
			}

		} else if ("bts_score_month".equals(tableName)) { // 4

			// 获取开始时间所在的月、结束时间所在的月
			DateTime startDay = new DateTime(startCycleDate);
			String monthStart = startDay.getStartOfMonth().toString();
			System.out.println(monthStart);

			if (startCycleDate.equals(endCycleDate)) {
				searchParams.put("curMonth", monthStart);
			} else {
				DateTime endDay = new DateTime(endCycleDate);
				String monthEnd = endDay.getStartOfMonth().toString();
				searchParams.put("monthStart", monthStart);
				searchParams.put("monthEnd", monthEnd);
			}

		} else {

			if (startCycleDate.equals(endCycleDate)) {
				searchParams.put("curDate", startCycleDate);
			} else {
				searchParams.put("startDate", startCycleDate);
				searchParams.put("endDate", endCycleDate);
			}
		}
		searchParams.put("tableName", tableName);

		return btsScoreSumService.findBtsSumAll(searchParams);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/btsSummaryScore/exportFile")
	public void exportFileSummaryScore(@RequestParam("areaCode") String areaCode, @RequestParam("cityCode") String cityCode, @RequestParam("neCode") String neCode,
			@RequestParam("startCycleDate") String startCycleDate, @RequestParam("endCycleDate") String endCycleDate, @RequestParam("grade") String grade, @RequestParam("tableName") String tableName,
			HttpServletRequest request, HttpServletResponse response) {
		String extName2 = "";
		if (StringUtils.isNotEmpty(areaCode)) {
			extName2 += areaCode;
		}
		if (StringUtils.isNotEmpty(cityCode)) {
			extName2 += cityCode;
		}

		if (StringUtils.isNotEmpty(neCode)) {
			extName2 += neCode;
		}
		if (StringUtils.isNotEmpty(grade)) {
			extName2 += grade;
		}
		Map<String, Object> searchParams = new HashMap<String, Object>();
		// Date startDate = null;
		// Date endDate = null;
		OutputStream out = null;
		File fielDir = null;
		File zipFile=null;
		String[] headers_str = null;
		String sheetName = "";
		try {

			if (StringUtils.isNotEmpty(grade) && !"null".equals(grade)) {
				searchParams.put("grade", grade);
			}
			if (StringUtils.isNotEmpty(cityCode) && "null".equals(cityCode)) {
				searchParams.put("cityCode", cityCode);// 地市名称
			}
			// 获取有权限的地区
			ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
			searchParams.put("areaCode", areaCode); // 区县
			searchParams.put("neCode", neCode); // 基站ID
			searchParams.put("grade", grade);
			if ("bts_score_week".equals(tableName)) {// bts_score_week 3
				// 获取开始时间、结束时间所在的周
				String weekStart = DateUtils.getFirstDayOfWeek(startCycleDate).toString();
				if (startCycleDate.equals(endCycleDate)) {
					searchParams.put("curWeek", weekStart);
				} else {
					// 获取开始时间所在的周一，结束时间所在的周一
					String weekEnd = DateUtils.getFirstDayOfWeek(endCycleDate).toString();
					searchParams.put("weekStart", weekStart);
					searchParams.put("weekEnd", weekEnd);
				}

			} else if ("bts_score_month".equals(tableName)) { // 4

				// 获取开始时间所在的月、结束时间所在的月
				DateTime startDay = new DateTime(startCycleDate);
				String monthStart = startDay.getStartOfMonth().toString();

				if (startCycleDate.equals(endCycleDate)) {
					searchParams.put("curMonth", monthStart);
				} else {
					DateTime endDay = new DateTime(endCycleDate);
					String monthEnd = endDay.getStartOfMonth().toString();
					searchParams.put("monthStart", monthStart);
					searchParams.put("monthEnd", monthEnd);
				}

			} else {

				if (startCycleDate.equals(endCycleDate)) {
					searchParams.put("curDate", startCycleDate);
				} else {
					searchParams.put("startDate", startCycleDate);
					searchParams.put("endDate", endCycleDate);
				}
			}

			searchParams.put("tableName", tableName);

			// 导出开始

			if ("bts_score_day".equalsIgnoreCase(tableName)) {
				sheetName = "基站日评分";
				extName2 = sheetName + "-" + extName2;
				// 全部显示
				String[] headers = { "地市名称:area_name", "区县名称:city_name", "基站ID:ne_code", "基站名称:ne_name_cn", "月:cycle_month", "周:cycle_week", "日:cycle_date", "分数:total_score" };
				headers_str = headers;
			} else if ("bts_score_week".equalsIgnoreCase(tableName)) {
				sheetName = "基站周评分";
				extName2 = sheetName + "-" + extName2;
				// 不显示日期
				String[] headers = { "地市名称:area_name", "区县名称:city_name", "基站ID:ne_code", "基站名称:ne_name_cn", "月:cycle_month", "周:cycle_week", "分数:total_score" };
				headers_str = headers;
			} else if ("bts_score_month".equalsIgnoreCase(tableName)) {
				sheetName = "基站月评分";
				extName2 = sheetName + "-" + extName2;
				// 不显示日期、周字段
				String[] headers = { "地市名称:area_name", "区县名称:city_name", "基站ID:ne_code", "基站名称:ne_name_cn", "月:cycle_month", "分数:total_score" };
				headers_str = headers;
			}

			String btsSumPath = FileOperateUtil.getPropertieValue("iceslte.kpi.file.path");
			String fileName = extName2 + "-" + startCycleDate + "~" + endCycleDate + ".xlsx";
			String fileNamezip = extName2 + "-" + startCycleDate + "~" + endCycleDate;
			// 保存到本地xls
			String zipPath = btsSumPath + fileNamezip + ".zip";
			String zipUrl = zipPath;
			File operationDir = new File(btsSumPath);
			fielDir = new File(btsSumPath + fileName);
			zipFile = new File(zipPath);

			if (!operationDir.exists() && !operationDir.isDirectory()) {
				operationDir.mkdir();
			}

			if (!zipFile.exists()) {
				ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
				Map<String, Object> map = btsScoreSumService.findBtsSumAll(searchParams);
				List<Map<String, Object>> btsList = (List<Map<String, Object>>) map.get("content");

				// 生成文件
				fielDir.createNewFile();
				out = new FileOutputStream(fielDir);
				ex.exportExcel(sheetName, headers_str, btsList, out, "yyyy-MM-dd HH:mm:ss", "0", null);
				// 压缩
				zipUrl = ZipCompressUtil.compress(new String[] { btsSumPath + fileName }, btsSumPath, fileNamezip + ".zip");
				out.close();
				fielDir.delete();
			}

			// 导出excel
			FileOperateUtil.download(response, zipUrl, "" + zipFile.length(), fileNamezip + ".zip");
			// FileOperateUtil.download(request, response,
			// btsScorePath+fileName, "application/octet-stream",fileName);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("An error occurred while export method:exportBreakReason,error message:{}", e.getMessage());
		} finally {
			zipFile.delete();
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.error("An error occurred while invoke method:exportBreakReason,error message:{}", e.getMessage());
				e.printStackTrace();
			}
			out = null;

		}
	}

	/**
	 * 基站汇总图形
	 * */
	@RequestMapping(value = "/sumScoreChart/search")
	public List<Map<String, Object>> getSumChartList(@RequestParam("tableName") String tableName, @RequestParam("neCode") String neCode, @RequestParam("sumStartDate") String sumStartDate,
			@RequestParam("sumEndDate") String sumEndDate) {

		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("neCode", neCode);
		searchParams.put("sumStartDate", sumStartDate);
		searchParams.put("sumEndDate", sumEndDate);
		searchParams.put("tableName", tableName);

		return btsScoreSumService.findSumchartAll(searchParams);

	}

}
