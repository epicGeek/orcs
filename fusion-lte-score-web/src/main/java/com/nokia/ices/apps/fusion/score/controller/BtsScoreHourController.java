package com.nokia.ices.apps.fusion.score.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RestController;
import com.google.common.collect.Lists;
import com.nokia.ices.apps.fusion.score.common.utils.ExportColumns;
import com.nokia.ices.apps.fusion.score.common.utils.ExportCsv;
import com.nokia.ices.apps.fusion.score.common.utils.FileOperateUtil;
import com.nokia.ices.apps.fusion.score.common.utils.ZipCompressUtil;
import com.nokia.ices.apps.fusion.score.domain.IndexScoringRule;
import com.nokia.ices.apps.fusion.score.service.AreaService;
import com.nokia.ices.apps.fusion.score.service.BtsScoreHourService;
import com.nokia.ices.apps.fusion.score.service.IndexScoringRuleService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;

/**
 * 健康度评分 基站
 * */
@RestController
public class BtsScoreHourController {

	public static final Logger logger = LoggerFactory.getLogger(BtsScoreHourController.class);
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddhhmmssSSS");
	@Autowired
	AreaService areaService;
	
	@Autowired
	BtsScoreHourService btsScoreHourService;

	@Autowired
	IndexScoringRuleService indexScoringRuleService;

	// 基站评分
	@RequestMapping(value = "/btsScore/search")
	public Map<String, Object> getBtsScoreHourList(@RequestBody ModelMap paramMap) {
		String areaCode = paramMap.get("areaCode")==null?"":paramMap.get("areaCode").toString();
		try{
			//获取有权限的地区
			ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		    areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
		    paramMap.put("areaCode", areaCode);
			return btsScoreHourService.findBtsScoreHourAll(paramMap);
		}catch(Exception e){
			logger.debug("btsScore/search si error ........"+e.getMessage());
		}
		
		return null;
	}

	// kpi指标评分
	@RequestMapping(value = "/kpiScore/search")
	public Map<String, Object> getKpiHourList(@RequestParam("page") Integer page, 
			@RequestParam("pageSize") Integer pageSize, @RequestParam("areaCode") String areaCode,
			@RequestParam("cityCode") String cityCode, @RequestParam("neCode") String neCode, 
			@RequestParam("startCycle") String startCycle, @RequestParam("endCycle") String endCycle) {

		Map<String, Object> searchParams = new HashMap<String, Object>();
		//获取有权限的地区
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
	    areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
		searchParams.put("page", (page - 1) * pageSize);
		searchParams.put("pageSize", pageSize);
		searchParams.put("areaCode", areaCode);// 地市名称
		searchParams.put("cityCode", cityCode); // 区县
		searchParams.put("neCode", neCode); // 基站ID
		searchParams.put("startCycle", startCycle); // 开始时间
		searchParams.put("endCycle", endCycle); // 结束时间
	//	searchParams.put("tableName", tableName); 
		List<IndexScoringRule> indexList = indexScoringRuleService.findIndexScoringRuleAll();
		List<String> kpiColumns = new ArrayList<String>();
		for (IndexScoringRule isr : indexList) {
			kpiColumns.add(isr.getKpiId().toLowerCase() + "_score");
		}
		searchParams.put("list", kpiColumns); // 小时
		try{
			return btsScoreHourService.findKpiScoreHourAll(searchParams);
		}catch(Exception e){
			logger.debug("kpiScore/search si error ........"+e.getMessage());
		}
		return null;

	}

	/**
	 * 根据基站neCode、ne_code\cycle_date\cycle_hour\total_score查询该基站的所有小区的评分折线图数据
	 * 基站指标明细
	 * 
	 * @param neCode
	 * @return
	 */
	@RequestMapping(value = "/kpiScoreNeCode/search")
	public List<Map<String, Object>> getKpiHourList(@RequestParam("neCode") String neCode, @RequestParam("totalScore") String totalScore, @RequestParam("cycleDate") String cycleDate,
			@RequestParam("cycleHour") String cycleHour) {

		Map<String, Object> searchParams = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(cycleDate) && !"null".equals(cycleDate)) {
			try {
				searchParams.put("neCode", neCode); // 基站ID
				searchParams.put("totalScore", totalScore); // 分数
				searchParams.put("cycleDate", sdf.parse(cycleDate));
				searchParams.put("cycleHour", cycleHour); // 小时
				List<IndexScoringRule> indexList = indexScoringRuleService.findIndexScoringRuleAll();
				List<String> kpiColumns = new ArrayList<String>();
				for (IndexScoringRule isr : indexList) {
					kpiColumns.add(isr.getKpiId().toLowerCase() + "_score");
				}
				searchParams.put("list", kpiColumns);

			} catch (ParseException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		return btsScoreHourService.findByBtsHourCodeAll(searchParams);

	}

	/**
	 * 基站健康度明细--> 图形指标
	 */
	@RequestMapping(value = "/kpiScoreList/search")
	public List<Map<String, Object>> getKpiHourGraphicList(@RequestBody ModelMap searchParams) {

		try{
			return btsScoreHourService.findByBtsScoreGraphicAll(searchParams);
		}catch(Exception e){
			logger.debug("kpiScoreList/search si error ........"+e.getMessage());
		}
		return null;
	}

	// 基站评分导出
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/btsScore/exportFile")
	public void saveSession(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		Map<String, String[]> parameterMap = request.getParameterMap();

		String cityCode = null == parameterMap.get("cityCode") ? "" : parameterMap.get("cityCode")[0];
		String areaCode = null == parameterMap.get("areaCode") ? "" : parameterMap.get("areaCode")[0];
		String neCode = null == parameterMap.get("neCode") ? "" : parameterMap.get("neCode")[0];
		String grade = null == parameterMap.get("grade") ? "" : parameterMap.get("grade")[0];
		String startDate = null == parameterMap.get("startDate") ? "" : parameterMap.get("startDate")[0];
		String endDate = null == parameterMap.get("endDate") ? "" : parameterMap.get("endDate")[0];
		String tableName = null == parameterMap.get("tableName") ? "" : parameterMap.get("tableName")[0];
		String kpiValue =null == parameterMap.get("kpiValue")?"":parameterMap.get("kpiValue")[0];
		String extName = "";
		Map<String, Object> searchParams = new HashMap<String, Object>();
		//获取有权限的地区
		//areaCode = areaService.getArea(areaCode);
		searchParams.put("cityCode", cityCode);// 地市名称
		searchParams.put("areaCode", areaCode); // 区县
		searchParams.put("neCode", neCode); // 基站ID
		searchParams.put("grade", grade); // 等级
		searchParams.put("startDate", startDate); 
		searchParams.put("endDate", endDate);
		searchParams.put("tableName", tableName);
		//searchParams.put("kpiValue", kpiValue); 
		
		if (StringUtils.isNotEmpty(kpiValue) && !"null".equals(kpiValue)) {
			searchParams.put("kpiValue", kpiValue.toLowerCase() + "_score"); 
		}
		
		List<IndexScoringRule> indexList = indexScoringRuleService.findIndexScoringRuleAll();
		
		IndexScoringRule alarm = new IndexScoringRule();
		alarm.setKpiId("alarm");
		alarm.setCnName("基站告警");
		indexList.add(alarm);	
		
	//	IndexScoringRule outof = new IndexScoringRule();
	//	outof.setKpiId("out_of_score");
	//	outof.setCnName("退服");
	//	indexList.add(outof);
		
		String[] keyArray=null;
		String[] showColumnArray=null;
		if ("bts_score_hour".equalsIgnoreCase(tableName)) {
			
			 keyArray = ExportColumns.BASE_SCORE_HOUR;
			 showColumnArray = ExportColumns.BASE_SCORE_HOUR_HEADER;
			 extName="基站小时评分";
			 
		}else if ("bts_score_day".equalsIgnoreCase(tableName)) {
			
			 keyArray = ExportColumns.BASE_SCORE_DAY;
			 showColumnArray = ExportColumns.BASE_SCORE_DAY_HEADER;
			 extName="基站日评分";
			 
		}else if("bts_score_week".equalsIgnoreCase(tableName)){
			
			 keyArray = ExportColumns.BASE_SCORE_WEEK;
			 showColumnArray = ExportColumns.BASE_SCORE_WEEK_HEADER;
			 extName="基站周评分";
			 
		}else if("bts_score_month".equalsIgnoreCase(tableName)){
			
			 keyArray = ExportColumns.BASE_SCORE_MONTH;
			 showColumnArray = ExportColumns.BASE_SCORE_MONTH_HEADER;
			 extName="基站月评分";
		}

		ExportCsv ec = new ExportCsv();
		File csvFile = null;
		BufferedWriter csvFileOutputStream = null;
		String bossPath = FileOperateUtil.getPropertieValue("iceslte.kpi.file.path");
		String fileName = "btsScore_"+extName+sdfDate.format(new Date());
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
			
				Map<String, Object> map = btsScoreHourService.exportAll(searchParams);	
				List<Map<String, Object>> btsList = (List<Map<String, Object>>) map.get("content");
				// 生成文件
				csvFile.createNewFile();
				csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 2048);
				ec.createScoreCSVFile(btsList, showColumnArray, keyArray, csvFileOutputStream, indexList);
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

	// 指标评分导出
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/kpiScore/exportFile")
	public void saveSession(@RequestParam("areaCode") String areaCode, @RequestParam("cityCode") String cityCode, 
			@RequestParam("neCode") String neCode, @RequestParam("startCycle") String startCycle,
			@RequestParam("endCycle") String endCycle, HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> searchParams = new HashMap<String, Object>();

		String extName = "";
		String extName2 = "";
		//获取有权限的地区
		if("null".equals(areaCode)) {
			areaCode = "";
		}
		if (StringUtils.isNotEmpty(areaCode) ) {			
			extName2 += areaCode;
		}
		if (StringUtils.isNotEmpty(cityCode) && !"null".equals(cityCode)) {
			searchParams.put("cityCode", cityCode);// 地市名称
			extName2 += cityCode;
		}
		if (StringUtils.isNotEmpty(neCode) && !"null".equals(neCode)) {
			searchParams.put("neCode", neCode);// 基站ID
			extName2 += neCode;
		}

		if (StringUtils.isNotEmpty(startCycle) && !"null".equals(startCycle)) {
			searchParams.put("startCycle", startCycle);// 开始时间
			String sDate = startCycle.replace(":", "-");
			extName2 += sDate;
		}
		if (StringUtils.isNotEmpty(endCycle) && !"null".equals(endCycle)) {
			searchParams.put("endCycle", endCycle);// 结束时间
			String eDate = endCycle.replace(":", "-");
			extName2 += "-" + eDate;
		}
		if (!"".equals(extName2)) {
			extName2 = "(" + extName2 + ")";
		}
		//获取地区权限
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
	    areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
		searchParams.put("areaCode", areaCode);// 地区名称
		List<IndexScoringRule> indexList = indexScoringRuleService.findIndexScoringRuleAll();
		List<String> kpiIdlist = Lists.newArrayList();
		List<String> kpiNamelist = Lists.newArrayList();
		for (IndexScoringRule isr : indexList) {
			kpiIdlist.add(isr.getKpiId().toLowerCase() + "_score");
		kpiNamelist.add(isr.getCnName());
		}
		searchParams.put("list", kpiIdlist);
		List<String> weekList = Lists.newArrayList(ExportColumns.KPI_HOUR_SCORE);
		weekList.addAll(kpiIdlist);
		List<String> showList = Lists.newArrayList(ExportColumns.KPI_HOUR_SCORE_HEADER);
		Object[] keyArray = weekList.toArray();
		showList.addAll(kpiNamelist);
		Object[] showColumnArray = showList.toArray();

		ExportCsv ec = new ExportCsv();
		File csvFile = null;
		BufferedWriter csvFileOutputStream = null;
		String bossPath = FileOperateUtil.getPropertieValue("iceslte.kpi.file.path");
		String fileName = "kpiScore-" + extName + extName2;
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
				Map<String, Object> map = btsScoreHourService.findKpiScoreHourAll(searchParams);
				List<Map<String, Object>> kpiScoreList = (List<Map<String, Object>>) map.get("content");
				// 生成文件
				csvFile.createNewFile();
				csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"), 2048);
				ec.createCSVFile(kpiScoreList, showColumnArray, keyArray, csvFileOutputStream, null);
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
}