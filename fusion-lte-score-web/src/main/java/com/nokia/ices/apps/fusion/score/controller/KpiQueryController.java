package com.nokia.ices.apps.fusion.score.controller;

import hirondelle.date4j.DateTime;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nokia.ices.apps.fusion.score.common.callable.Exportcallable;
import com.nokia.ices.apps.fusion.score.common.utils.DateUtils;
import com.nokia.ices.apps.fusion.score.common.utils.ExportColumns;
import com.nokia.ices.apps.fusion.score.common.utils.ExportCsv;
import com.nokia.ices.apps.fusion.score.common.utils.FileOperateUtil;
import com.nokia.ices.apps.fusion.score.common.utils.LinuxCommand;
import com.nokia.ices.apps.fusion.score.common.utils.ZipCompressUtil;
import com.nokia.ices.apps.fusion.score.domain.IndexScoringRule;
import com.nokia.ices.apps.fusion.score.service.AreaService;
import com.nokia.ices.apps.fusion.score.service.BtsInformationCellService;
import com.nokia.ices.apps.fusion.score.service.BtsScoreHourService;
import com.nokia.ices.apps.fusion.score.service.IndexScoringRuleService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;

@RestController
public class KpiQueryController {

	public static final Logger logger = LoggerFactory.getLogger(KpiQueryController.class);
	private static Integer onceSize = 100000;// 一次导出10W条

	@Autowired
	BtsScoreHourService btsScoreHourService;

	@Autowired
	BtsInformationCellService btsInformationCellService; // 用于KPI图形指标

	@Autowired
	IndexScoringRuleService indexScoringRuleService;
	private static ThreadPoolExecutor exec = new ThreadPoolExecutor(10, 300, 300L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

	@Autowired
	AreaService areaService;
	
	/**
	 * KPI查询
	 * */
	@RequestMapping(value = "/kpiQuery/search")
	public Map<String, Object> getKpiHourList(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize,
			@RequestParam("areaCode") String areaCode,	@RequestParam("cityCode") String cityCode, 
			@RequestParam("cellId") String cellId, @RequestParam("startCycle") String startCycle, 
			@RequestParam("endCycle") String endCycle,@RequestParam("tableName") String tableName) {

		Map<String, Object> searchParams = new HashMap<String, Object>();
	  //获取地区权限
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
		searchParams.put("page", (page - 1) * pageSize);
		searchParams.put("pageSize", pageSize);
		searchParams.put("areaCode", areaCode);// 地市名称
		searchParams.put("cityCode", cityCode); // 区县
		searchParams.put("cellId", cellId); // 基站ID
		searchParams.put("startCycle", startCycle); // 开始时间
		searchParams.put("endCycle", endCycle); // 结束时间
		searchParams.put("tableName", tableName);

		return btsScoreHourService.findKpiQueryAll(searchParams);

	}

	// 导出
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/kpiQuery/exportFile")
	public void saveSession(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		String areaCode = null == parameterMap.get("areaCode") ? "" : parameterMap.get("areaCode")[0];
		String cityCode = null == parameterMap.get("cityCode") ? "" : parameterMap.get("cityCode")[0];
		String cellId = null == parameterMap.get("cellId") ? "" : parameterMap.get("cellId")[0];
		String startCycle = null == parameterMap.get("startCycle") ? "" : parameterMap.get("startCycle")[0];
		String endCycle = null == parameterMap.get("endCycle") ? "" : parameterMap.get("endCycle")[0];
		String tableName = null == parameterMap.get("tableName") ? "" : parameterMap.get("tableName")[0];
		String kpiName = null == parameterMap.get("kpiName") ? "" : parameterMap.get("kpiName")[0];
		
		Map<String, Object> searchParams = new HashMap<String, Object>();
		BufferedWriter csvFileOutputStream = null;
		File fielDir = null;
		File fielZipDir = null;
		String extName = "";
		if("null".equals(areaCode)) {
			areaCode = "";
		}
		if (StringUtils.isNotEmpty(areaCode) ) {			
			extName += areaCode;
		}
		if (StringUtils.isNotEmpty(cityCode) && !"null".equals(cityCode)) {
			searchParams.put("cityCode", cityCode);// 地市名称
			extName += cityCode;
		}

		if (StringUtils.isNotEmpty(cellId) && !"null".equals(cellId)) {
			searchParams.put("cellId", cellId);// 基站ID
			extName += cellId;
		}
	    searchParams.put("areaCode", areaCode);// 地市名称
		searchParams.put("startCycle", startCycle); // 开始时间
		searchParams.put("endCycle", endCycle); // 结束时间
		searchParams.put("tableName", tableName);
		// ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String,
		// Object>>();

		List<String> keyList = Lists.newArrayList(ExportColumns.BASE_KPI);
		List<String> showList = Lists.newArrayList(ExportColumns.BASE_KPI_HEADER);
		String kpiQueryPath = FileOperateUtil.getPropertieValue("iceslte.kpi.file.path");
		String fileName = kpiName + "-" + startCycle.replace(":", "-") + "~" + endCycle.replace(":", "-") + extName + ".csv";
		String fileZip = kpiName + "-" + startCycle.replace(":", "-") + "~" + endCycle.replace(":", "-") + extName + ".zip";
		String zipUrl = kpiQueryPath + fileZip;
		ExportCsv ec = new ExportCsv();
		// 保存到本地xls
		try {
			File operationDir = new File(kpiQueryPath);
			fielDir = new File(kpiQueryPath + fileName);
			fielZipDir = new File(zipUrl);
			if (!operationDir.exists() && !operationDir.isDirectory()) {
				operationDir.mkdir();
			}
			if (!fielZipDir.exists()) {
				Map<String, Object> map = btsScoreHourService.findKpiQueryAll(searchParams);
				List<Map<String, Object>> kpiScoreList = (List<Map<String, Object>>) map.get("content");
				fielDir.createNewFile();
				csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fielDir), "GB2312"), 2048);
				ec.createCSVFile(kpiScoreList, showList.toArray(), keyList.toArray(), csvFileOutputStream, null);
				/*
				 * out = new FileOutputStream(fielDir);
				 * ex.exportExcel("KPI查询数据导出", headers, kpiScoreList, out,
				 * "yyyy-MM-dd HH:mm:ss", "0", null);
				 */
				// 压缩
				zipUrl = ZipCompressUtil.compress(new String[] { kpiQueryPath + fileName }, kpiQueryPath, fileZip);
				csvFileOutputStream.close();
				fielDir.delete();// 删除文件
			}
			// 下载
			FileOperateUtil.download(response, zipUrl, "" + fielZipDir.length(), fileZip);
			// 导出excel
			//FileOperateUtil.download(request, response, kpiQueryPath + fileZip, "application/octet-stream", fileZip);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new RuntimeException(e);

		} finally {
			if (null != csvFileOutputStream) {
				try {
					csvFileOutputStream.close();
					fielDir.delete();// 删除文件
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
				csvFileOutputStream = null;
			}
		}
	}

	// 导出
	@RequestMapping(value = "/kpiQuery/exportCsvFile")
	public void export(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		String areaCode = null == parameterMap.get("areaCode") ? "" : parameterMap.get("areaCode")[0];
		String cityCode = null == parameterMap.get("cityCode") ? "" : parameterMap.get("cityCode")[0];
		String cellId = null == parameterMap.get("cellId") ? "" : parameterMap.get("cellId")[0];
		String startCycle = null == parameterMap.get("startCycle") ? "" : parameterMap.get("startCycle")[0];
		String endCycle = null == parameterMap.get("endCycle") ? "" : parameterMap.get("endCycle")[0];
		String tableName = null == parameterMap.get("tableName") ? "" : parameterMap.get("tableName")[0];
		String kpiName = null == parameterMap.get("kpiName") ? "" : parameterMap.get("kpiName")[0];
		// 每次导出重新生成
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("startCycle", startCycle); // 开始时间
		searchParams.put("endCycle", endCycle); // 结束时间
		searchParams.put("tableName", tableName);
		String extName = "";
		if("null".equals(areaCode)) {
			areaCode = "";
		}
		if (StringUtils.isNotEmpty(areaCode) ) {			
			extName += areaCode;
		}
		if (StringUtils.isNotEmpty(cityCode) && !"null".equals(cityCode)) {
			searchParams.put("cityCode", cityCode);// 地市名称
			extName += cityCode;
		}
		if (StringUtils.isNotEmpty(cellId) && !"null".equals(cellId)) {
			searchParams.put("cellId", cellId);// 基站ID
			extName += cellId;
		}
	  //获取地区权限
  		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
  		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
	    searchParams.put("areaCode", areaCode);// 地市名称
		String loadPath = FileOperateUtil.getPropertieValue("iceslte.kpi.file.path");
		List<String> filePathList = Lists.newArrayList();// 存放文件路径,用于合并该路径下所有文件
		List<String> fileNameList = Lists.newArrayList();// 存放文件名称,用于存放合并后需要打包的文件
		DateTime startTime = new DateTime(startCycle);
		DateTime endTime = new DateTime(endCycle);
		DateTime startTimeTmp = startTime;
		// 1.获取结束时间到开始时间的间隔天数
		int days = startTime.numDaysFrom(endTime);

		String[] showColumnArray = ExportColumns.BASE_KPI_HEADER;// 定义导出的表头
		for(String s : showColumnArray) {
			if(s.equals("KPI值")) {
				s = kpiName;
				break;
			}
		}
		String[] keyArray = ExportColumns.BASE_KPI;// 定义导出的字段
		// 定义导出ZIP文件名称
		String fileName = tableName + "(" + startTime.toString().replace(":", "-") + "-" + endTime.toString().replace(":", "-") + ")" + extName + ".zip";
		// String fileName = kpiName + "(" + startTime.toString().replace(":",
		// "-") + "-" + endTime.toString().replace(":", "-") + ")" + extName +
		// ".zip";
		File zipFile = null;
		String zipPath = loadPath + fileName;
		String zipUrl = zipPath;
		try {
			File operationDir = new File(loadPath);
			zipFile = new File(zipPath);
			if (!operationDir.exists() && !operationDir.isDirectory()) {
				operationDir.mkdir();
			}
			if (!zipFile.exists()) {
				int total = btsScoreHourService.getKpiTotal(searchParams);// 符合条件的总条数
				if (total > 0) {
					// 存放每天的总条数
					Map<Integer, Integer> pageMap = Maps.newHashMap();
					int k = 0;
					int totalPage = 0;// 总页数
					// 统计开始时间到结束时间-1天的23:59:59每天的页数
					for (int i = 0; i < days; i++) {
						k = i;
						String start = startTimeTmp.toString();
						DateTime end = startTimeTmp.getEndOfDay();
						startTimeTmp = startTimeTmp.plusDays(i + 1);
						searchParams.put("startCycle", start);
						searchParams.put("endCycle", end.toString());
						Integer dayTotal = btsScoreHourService.getKpiTotal(searchParams);
						if (dayTotal == 0) {
							pageMap.put(i, dayTotal);
						} else {
							int dayTotalSize = dayTotal % onceSize;
							if (dayTotalSize == 0) {
								dayTotalSize = dayTotal / onceSize;
							} else {
								dayTotalSize = dayTotal / onceSize + 1;
							}
							pageMap.put(i, dayTotalSize);
						}
						totalPage += pageMap.get(i);
					}

					// 统计结束时间00:00:00秒到选择的结束时间的页数
					k = k + 1;
					DateTime endDateStart = endTime.getStartOfDay();
					searchParams.put("startCycle", endDateStart.toString());
					searchParams.put("endCycle", endTime.toString());
					Integer dayTotal = btsScoreHourService.getKpiTotal(searchParams);
					if (dayTotal == 0) {
						pageMap.put(k, dayTotal);
					} else {
						int dayTotalSize = dayTotal % onceSize;
						if (dayTotalSize == 0) {
							dayTotalSize = dayTotal / onceSize;
						} else {
							dayTotalSize = dayTotal / onceSize + 1;
						}
						pageMap.put(k, dayTotalSize);
					}
					totalPage += pageMap.get(k);

					if (totalPage > 0) {
						//int limit = 0;
						int offset = 0;
						String path = "";
						File file = null;
						// 每次获取的页数数据导出作为一个新线程
						final CountDownLatch countDownLatch = new CountDownLatch(totalPage);
						// 处理结束时间那天的数据
						if (pageMap.get(k) > 0) {
							String endStr = endTime.format(DateUtils.DEFAULT_DATE_FORMAT);
							String startStr = endDateStart.format(DateUtils.DEFAULT_DATE_FORMAT);
							path = loadPath + File.separator + startStr + "-" + endStr + extName;
							file = new File(path);
							if (!file.exists()) {
								file.mkdir();
							} else {
								// LinuxCommand.truncateDataAndMkDir(path);
							}
							filePathList.add(path);
							fileNameList.add(path + ".csv");
							// 拆分条数进行导出
							Integer dayTotalSize = pageMap.get(k);
							for (int p = 0; p < dayTotalSize; p++) {
								//limit = (p + 1) * onceSize;
								offset = onceSize * p;
								// 读和写数据,存放到指定目录
								searchParams.put("limit", onceSize);
								searchParams.put("offset", offset);
								searchParams.put("cell_score_hour", tableName);
								List<Map<String, Object>> list = btsScoreHourService.getAllList(searchParams);
								// 导出的数据存放到path下
								Exportcallable callable = new Exportcallable(list, countDownLatch, p, path, showColumnArray, keyArray);
								exec.submit(callable);
							}
						}
						// 处理开始时间到结束时间前一天23:59:59的数据
						for (int i = 0; i < days; i++) {
							Integer dayTotalSize = pageMap.get(i);
							if (dayTotalSize > 0) {
								String start = startTime.toString();
								DateTime end = startTime.getEndOfDay();
								searchParams.put("startCycle", start);
								searchParams.put("endCycle", end.toString());
								String startStr = startTime.format(DateUtils.DEFAULT_DATE_FORMAT);
								String endStr = end.format(DateUtils.DEFAULT_DATE_FORMAT);
								path = loadPath + File.separator + startStr + "-" + endStr + extName;
								// 按天创建文件夹
								file = new File(path);
								if (!file.exists()) {
									file.mkdir();
								} else {
									// LinuxCommand.truncateDataAndMkDir(path);
								}
								filePathList.add(path);
								fileNameList.add(path + ".csv");
								for (int p = 0; p < dayTotalSize; p++) {
									//limit = (p + 1) * onceSize;
									offset = onceSize * p;
									// 读和写数据,存放到指定目录
									searchParams.put("limit", onceSize);
									searchParams.put("offset", offset);
									searchParams.put("cell_score_hour", tableName);
									List<Map<String, Object>> list = btsScoreHourService.getAllList(searchParams);
									// 导出的数据存放到path下
									Exportcallable callable = new Exportcallable(list, countDownLatch, p, path, showColumnArray, keyArray);
									exec.submit(callable);
								}
							}
							startTime = startTime.plusDays(i + 1);
						}

						countDownLatch.await();// 等待所有的导出都完成
						
						// // 合并文件
						for (String _filePath : filePathList) {
							LinuxCommand.merge(_filePath, _filePath + ".csv");
						}
						logger.info("Merge file finished.");
						// 打包
						zipUrl = ZipCompressUtil.compress(fileNameList, loadPath, fileName);
						// 删除CSV文件
						for (String _fileName : fileNameList) {
							File delFile = new File(_fileName);
							if(delFile.exists()) {
								delFile.delete();
							}
						}
						//删除文件夹下的内容
						for (String _filePath : filePathList) {
							File delFilePath = new File(_filePath);
							if(delFilePath.exists() && delFilePath.isDirectory()) {
								LinuxCommand.truncateDirData(_filePath);
							}
						}
						//删除文件夹
						for (String _filePath : filePathList) {
							File delFilePath = new File(_filePath);
							if(delFilePath.exists() && delFilePath.isDirectory()) {
								delFilePath.delete();
							}
						}
					}
				}
			}
			FileOperateUtil.download(response, zipUrl, "" + zipFile.length(), fileName);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * KPI图形指标
	 * */
	@RequestMapping(value = "/kpiChartQuery/search")
	public Map<String, Object> getKpiQueryChartList(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize, @RequestParam("areaCode") String areaCode,
			@RequestParam("cityCode") String cityCode, @RequestParam("cellId") String cellId, @RequestParam("neCode") String neCode) {

		Map<String, Object> searchParams = new HashMap<String, Object>();
		// 获取有权限的地区
  		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
  		areaCode = areaService.getCurrentAreaAuth(shiroUser.getUserName(),areaCode);
		searchParams.put("page", (page - 1) * pageSize);
		searchParams.put("pageSize", pageSize);
		searchParams.put("areaCode", areaCode);// 地市名称
		searchParams.put("cityCode", cityCode); // 区县
		searchParams.put("cellId", cellId); // 基站ID
		searchParams.put("neCode", neCode); // 基站ID

		return btsInformationCellService.findBtsAll(searchParams);

	}

	/**
	 * KPI图形指标-图
	 *根据 ne_code  
	 */
	@RequestMapping(value = "/kpiChartSearch/search")
	public  Map<String, Object> getKpiChartList(@RequestParam("cycleDate") String cycleDate, 
																						@RequestParam("cellId") String cellId,
																						@RequestParam("daysInput") String daysInput,
																						@RequestParam("kpiId") String kpiId) {

		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("cycleDate", cycleDate); // 开始时间
		searchParams.put("daysInput", daysInput); 
		searchParams.put("cellId", cellId); // 小区ID
		searchParams.put("kpiId", kpiId); 

	return btsScoreHourService.findKpiChartAll(searchParams);

	}

	/**
	 * KPI图形指标-明细
	 * */
	@RequestMapping(value = "/kpiChartList/search")
	public List<Map<String, Object>> getKpiGraphicDetail(@RequestParam("neCode") String neCode, 
																								  @RequestParam("cellid") String cellid,
																								  @RequestParam("cycleStart") String cycleStart,
																								  @RequestParam("sectorId") String sectorId, 
																								  @RequestParam("cycleEnd") String cycleEnd) {

		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("neCode", neCode); // 基站ID
		searchParams.put("cellid", cellid); // 小区ID
		searchParams.put("sectorId", sectorId); // 扇区ID
		searchParams.put("cycleStart", cycleStart); // 开始时间
		searchParams.put("cycleEnd", cycleEnd); // 结束时间

		List<IndexScoringRule> indexList = indexScoringRuleService.findIndexScoringRuleAll();
		List<String> kpiColumns = new ArrayList<String>();
		for (IndexScoringRule isr : indexList) {
			kpiColumns.add(isr.getKpiId().toLowerCase() + "_value");
		}
		searchParams.put("list", kpiColumns); // 小时

		return btsScoreHourService.findKpiGraphicDetailAll(searchParams);

	}

}
