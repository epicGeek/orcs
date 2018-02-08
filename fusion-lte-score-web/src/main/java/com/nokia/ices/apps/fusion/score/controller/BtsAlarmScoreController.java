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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.nokia.ices.apps.fusion.score.common.utils.ExportColumns;
import com.nokia.ices.apps.fusion.score.common.utils.ExportCsv;
import com.nokia.ices.apps.fusion.score.common.utils.FileOperateUtil;
import com.nokia.ices.apps.fusion.score.common.utils.ZipCompressUtil;
import com.nokia.ices.apps.fusion.score.service.AreaService;
import com.nokia.ices.apps.fusion.score.service.BtsAlarmScoreService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;

/**
 * 健康度评分基站性能告警得分
 */
@RestController
public class BtsAlarmScoreController {

	public static final Logger logger = LoggerFactory.getLogger(BtsAlarmScoreController.class);
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");

	@Autowired
	AreaService areaService;
	@Autowired
	BtsAlarmScoreService btsAlarmScoreService;

	/**
	 * 健康度评分--基站性能告警得分
	 * 
	 * @param page
	 * @param pageSize
	 * @param neCode
	 * @param cycleHourStart
	 * @param cycleHourEnd
	 * @param cycleDate
	 * @return
	 */
	@RequestMapping(value = "btsAlarmScore/search")
	public Map<String, Object> getbtsAlarmScoreList(@RequestParam("page") Integer page,
			@RequestParam("pageSize") Integer pageSize, @RequestParam("neCode") String neCode,
			@RequestParam("cycleStart") String cycleStart, @RequestParam("cycleEnd") String cycleEnd,
			@RequestParam("cityCode") String cityCode, @RequestParam("areaCode") String areaCode,
			@RequestParam("score") String score) {

		Map<String, Object> searchParams = new HashMap<String, Object>();
		//获取地区权限
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
		searchParams.put("page", (page - 1) * pageSize);
		searchParams.put("pageSize", pageSize);
		searchParams.put("neCode", neCode);
		searchParams.put("cycleStart", cycleStart);
		searchParams.put("cycleEnd", cycleEnd);
		searchParams.put("cityCode", cityCode);
		searchParams.put("areaCode", areaCode);
		searchParams.put("score", score);

		return btsAlarmScoreService.findBtsPerformanceAlarmScore(searchParams);

	}

	/**
	 * 弹框显示 告警号
	 * 
	 * @param neCode
	 * @return
	 */
	@RequestMapping(value = "btsAlarmNo/search")
	public Map<String, Object> getbtsAlarmNoList(@RequestParam("page") Integer page,
			@RequestParam("pageSize") Integer pageSize, @RequestParam("neCode") String neCode,
			@RequestParam("cycle") String cycle) {

		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("page", (page - 1) * pageSize);
		searchParams.put("pageSize", pageSize);
		searchParams.put("neCode", neCode);
		searchParams.put("cycle", cycle);

		return btsAlarmScoreService.findAlarmNo(searchParams);

	}

	// 导出
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/btsAlarmScore/exportFile")
	public void saveSession(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		Map<String, String[]> parameterMap = request.getParameterMap();

		String cityCode = null == parameterMap.get("cityCode") ? "" : parameterMap.get("cityCode")[0];
		String areaCode = null == parameterMap.get("areaCode") ? "" : parameterMap.get("areaCode")[0];
		String neCode = null == parameterMap.get("neCode") ? "" : parameterMap.get("neCode")[0];
		String cycleStart = null == parameterMap.get("cycleStart") ? "" : parameterMap.get("cycleStart")[0];
		String cycleEnd = null == parameterMap.get("cycleEnd") ? "" : parameterMap.get("cycleEnd")[0];
		String score = null == parameterMap.get("score") ? "" : parameterMap.get("score")[0];

		String extName = "";
		String csvfileName = "";
		Map<String, Object> searchParams = new HashMap<String, Object>();

		//获取地区权限
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
		if (StringUtils.isNotEmpty(areaCode)) {
			searchParams.put("areaCode", areaCode); // 区县
			extName += areaCode;
		}
		if (StringUtils.isNotEmpty(cityCode) && !"null".equals(cityCode)) {
			searchParams.put("cityCode", cityCode);// 地市名称
			extName += cityCode;
		}
		if (StringUtils.isNotEmpty(neCode)) {
			searchParams.put("neCode", neCode); // 基站ID
			extName += neCode;
		}
		if (StringUtils.isNotEmpty(score)) {
			searchParams.put("score", score);
			extName += score;
		}
		if (StringUtils.isNotEmpty(cycleStart) && !"null".equals(cycleStart)) {
			searchParams.put("cycleStart", cycleStart);
			// extName += sDate;
		}
		if (StringUtils.isNotEmpty(cycleEnd) && !"null".equals(cycleEnd)) {
			searchParams.put("cycleEnd", cycleEnd);
			// extName += eDate;
		}
		if (!"".equals(extName)) {
			csvfileName = extName;
		}
		searchParams.put("cycleStart", cycleStart);
		searchParams.put("cycleEnd", cycleEnd);

		String[] keyArray = ExportColumns.BTS_ALARM_SCORE;
		String[] showColumnArray = ExportColumns.BTS_ALARM_SCORE_HEADER;

		ExportCsv ec = new ExportCsv();
		File csvFile = null;
		BufferedWriter csvFileOutputStream = null;
		String btsAlarmPath = FileOperateUtil.getPropertieValue("iceslte.kpi.file.path");
		String fileName = "btsAlarmScore-" + sdf.format(new Date());
		String path = btsAlarmPath + fileName + ".csv";
		String zipPath = btsAlarmPath + fileName + ".zip";
		File zipFile = null;
		String zipUrl = zipPath;
		try {
			File operationDir = new File(btsAlarmPath);
			csvFile = new File(path);
			zipFile = new File(zipPath);
			if (!operationDir.exists() && !operationDir.isDirectory()) {
				operationDir.mkdir();
			}

			if (!zipFile.exists()) {
				Map<String, Object> map = btsAlarmScoreService.findBtsPerformanceAlarmScore(searchParams);
				List<Map<String, Object>> alarmList = (List<Map<String, Object>>) map.get("content");
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
				csvFileOutputStream = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 2048);
				ec.createCSVFile(alarmList, showColumnArray, keyArray, csvFileOutputStream, mapList);
				// 压缩
				zipUrl = ZipCompressUtil.compress(new String[] { path }, btsAlarmPath, fileName + ".zip");
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
				logger.error("An error occurred while invoke method:exportBreakReason,error message:{}",
						e.getMessage());
				e.printStackTrace();
			}
			csvFileOutputStream = null;

		}
	}

}
