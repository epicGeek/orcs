package com.nokia.ices.apps.fusion.boss.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.nokia.ices.apps.fusion.boss.service.BossBusinessService;
import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.core.utils.DateUtil;
import com.nokia.ices.core.utils.ExportExcel;
import com.nokia.ices.core.utils.ModuleDownLoadNameDefinition;

@RestController
public class BossBusinessController {

	private final static Logger logger = LoggerFactory.getLogger(BossBusinessController.class);
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	@Autowired
	BossBusinessService bossBusinessService;

	@Autowired
	ProjectProperties projectProperties;
	@RequestMapping(value = "/boss/cmdAndBusinessTypeMap")
	public List<Map<String, Object>> cmdAndBusinessTypeMap() {
		return bossBusinessService.businessTypeAndCmdTypeMapList();
	}
	@RequestMapping(value = "/boss/message")
	public String findBossMessage(@RequestParam(value = "column") String column,
			@RequestParam(value = "reId") String reId, @RequestParam(value = "re_time") String re_time) {
		return bossBusinessService.getBossMessageService(column, reId, re_time);
	}

	/**
	 * BOSS业务实时监控成功率
	 * 
	 * @param imsi
	 * @param businessType
	 * @param startTime
	 * @param endTime
	 * @param period
	 * @return
	 */
	@RequestMapping(value = "/boss/succMonitor-item")
	@ResponseBody
	public Map<String, Object> getBossMonitorData(@RequestParam(value = "hlrid") String inputNeName,
			@RequestParam(value = "businessType") String businessType,
			@RequestParam(value = "startTime") String startTime, @RequestParam(value = "endTime") String endTime,
			@RequestParam(value = "period") String period) {

		Map<String, Object> rateSuccMap = new HashMap<String, Object>();
		List<Map<String, Object>> bossList = null;
		// String [] types = null;
		try {
			/*
			 * if(null!=businessType && !"".equals(businessType)){ types =
			 * businessType.split(","); }
			 */
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("inputNeName", inputNeName);
			params.put("inputBusiness", businessType);
			String[] dates = DateUtil.getFormatStartAndDate(startTime, endTime);
			String checkTime = DateUtil.checkDateTime(dates[0], dates[1], period);
			logger.debug("checkDate:" + checkTime);
			if ("ok".equalsIgnoreCase(checkTime)) {
				params.put("startdatetime", dates[0]);
				params.put("enddatetime", dates[1]);
				bossList = bossBusinessService.findBossMonitorData(params, period);
				rateSuccMap.put("succRate", bossList);
			} else {
				rateSuccMap.put("message", checkTime);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return rateSuccMap;
	}

	/**
	 * 异步获取条件赛选的总记录数
	 * 
	 * @param imsiMsisdn
	 * @param startTime
	 * @param endTime
	 * @param result
	 * @param businessType
	 * @param errorCode
	 * @param hlrsn
	 * @return
	 */
	@RequestMapping(value = "/boss/total")
	public Integer getBossTotal(@RequestParam(value = "imsiMsisdn") String imsiMsisdn,
			@RequestParam(value = "startTime") String startTime, @RequestParam(value = "endTime") String endTime,
			@RequestParam(value = "result") String result, @RequestParam(value = "businessType") String businessType,
			@RequestParam(value = "cmdType") String cmdType, @RequestParam(value = "errorCode") String errorCode,
			@RequestParam(value = "hlrsn") String hlrsn) {

		String[] dates = DateUtil.getStartAndTime(startTime, endTime);
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(imsiMsisdn)) {
			validataNumber(imsiMsisdn, params);
		}
		params.put("hlrsn", hlrsn);
		params.put("startTime", Long.parseLong(dates[0]));
		params.put("endTime", Long.parseLong(dates[1]));
		params.put("result", result);
		params.put("errorCode", errorCode);
		params.put("businessType", businessType);
		params.put("cmdType", cmdType);
		return bossBusinessService.getBossCount(params);
	}

	/**
	 * BOSS业务历史数据查询
	 * 
	 * @param imsi
	 * @param msisdn
	 * @param startTime
	 * @param endTime
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/boss/search-item")
	public Map<String, Object> getBossBusinessSearch(@RequestParam(value = "imsiMsisdn") String imsiMsisdn,
			@RequestParam(value = "startTime") String startTime, @RequestParam(value = "endTime") String endTime,
			@RequestParam(value = "result") String result, @RequestParam(value = "page") Integer page,
			@RequestParam(value = "pageSize") Integer pageSize,
			@RequestParam(value = "businessType") String businessType, @RequestParam(value = "cmdType") String cmdType,
			@RequestParam(value = "errorCode") String errorCode, @RequestParam(value = "hlrsn") String hlrsn) {

		String[] dates = DateUtil.getStartAndTime(startTime, endTime);
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(imsiMsisdn)) {
			validataNumber(imsiMsisdn, params);
		}
		
		params.put("hlrsn", hlrsn);
		params.put("startTime", Long.parseLong(dates[0]));
		params.put("endTime", Long.parseLong(dates[1]));
		params.put("result", result);
		params.put("errorCode", errorCode);
		params.put("businessType", businessType);
		params.put("cmdType", cmdType);
		params.put("page", (page - 1) * pageSize);
		params.put("pageSize", pageSize);
		Map<String, Object> itemMap = bossBusinessService.findUserBossBusiness(params);
		return itemMap;
	}

	@RequestMapping(value = "/getNePatentName")
	public List<String> getNePatentName() {

		return Arrays.asList(ProjectProperties.getParentNeName().split(","));
	}

	@RequestMapping(value = "/getErrorCode")
	public List<Map<String,Object>> getErrorCode() {
		return bossBusinessService.findErrorCode();
	}

	@RequestMapping(value = "/getCmdType")
	public List<String> getCmdType() {
		return bossBusinessService.findCmdTypeName();
	}

	/**
	 * 获取boss业务类型
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryMonitorType/all", method = RequestMethod.GET)
	public List<Map<String, Object>> queryMonitorType() {
		try {
			List<Map<String, Object>> buslist = bossBusinessService.findMonitorType();
			return buslist;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * 导出2007版本的excel
	 * 
	 * @param request
	 * @param response
	 * @param imsi
	 * @param msisdn
	 * @param startTime
	 * @param endTime
	 * @param result
	 * @param businessType
	 * @param errorCode
	 * @param hlrsn
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/userBoss/exportFile", method = RequestMethod.GET)
	public void monitorExportFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "imsiMsisdn") String imsiMsisdn, @RequestParam(value = "startTime") String startTime,
			@RequestParam(value = "endTime") String endTime, @RequestParam(value = "result") String result,
			@RequestParam(value = "businessType") String businessType, @RequestParam(value = "cmdType") String cmdType,
			@RequestParam(value = "errorCode") String errorCode, @RequestParam(value = "hlrsn") String hlrsn) {

		File fielDir = null;
		String[] dates = DateUtil.getStartAndTime(startTime, endTime);
		OutputStream out = null;

		try {
			Map<String, Object> params = new HashMap<String, Object>();
			if (StringUtils.isNotEmpty(imsiMsisdn)) {
				validataNumber(imsiMsisdn, params);
			}
			params.put("hlrsn", hlrsn);
			params.put("startTime", Long.parseLong(dates[0]));
			params.put("endTime", Long.parseLong(dates[1]));
			params.put("result", result);
			params.put("errorCode", errorCode);
			params.put("businessType", businessType);
			params.put("cmdType", cmdType);
			params.put("page", "");
			params.put("pageSize", "");
			Map<String, Object> itemMap = bossBusinessService.findUserBossBusiness(params);
			ExportExcel<Map<String, Object>> excel = new ExportExcel<Map<String, Object>>();
			String fileName = "";
			String bossPath = ProjectProperties.getLogBasePath() + File.separator
					+ ModuleDownLoadNameDefinition.DOWNLOAD_BOSSEXCEL + File.separator;
			// String bossPath =
			// FileOperateUtil.getPropertieValue("dhlr.boss.exportExcel.file.path");
			fileName = "boss用户数据统计-" + sdf.format(new Date()) + "-" + ".xlsx";
			// 保存到本地xls
			File operationDir = new File(bossPath);
			fielDir = new File(bossPath + fileName);
			if (!operationDir.exists() && !operationDir.isDirectory()) {
				operationDir.mkdir();
			}
			if (!fielDir.exists()) {
				fielDir.createNewFile();
			}

			out = new FileOutputStream(fielDir);
			List<Map<String, Object>> dataList = (List<Map<String, Object>>) itemMap.get("content");
			String[] headers_succ = { "请求ID:re_id", "请求时间:re_time", "imsi:imsi", "msisdn:isdn", "指令类型:operationname",
					"业务类型:b_class", "上级网元:hlrsn", "执行结果:callback_result", "错误代码:errorcode", "返回内容:message", "用户:user_2",
					"mml:mml" };

			excel.exportExcel("boss用户数据统计", headers_succ, dataList, out, "yyyy-MM-dd HH:mm:ss");

			// 导出excel
			download(request, response, bossPath + fileName, "application/octet-stream", fileName);
		} catch (Exception e) {
			logger.error(e.toString());
			throw new RuntimeException(e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fielDir.delete();
		}
	}

	/**
	 * 下载日志
	 * 
	 * @param request
	 * @param response
	 * @param downLoadPath
	 * @param contentType
	 * @param realName
	 * @throws Exception
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response, String downLoadPath,
			String contentType, String realName) throws Exception {
		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		File file = new File(downLoadPath);
		response.setContentType(contentType);
		response.setHeader("Content-disposition",
				"attachment; filename=" + new String(realName.getBytes("utf-8"), "ISO8859-1"));
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

	@RequestMapping(value = "/validataTime")
	public String validataTime(@RequestParam(value = "startTime") String startTime,
			@RequestParam(value = "endTime") String endTime) {
		String checkTime = DateUtil.checkDateTime(startTime, endTime, "H");
		return checkTime;
	}

	public void validataNumber(String imsiMsisdn, Map<String, Object> params) {

		String keyVa = "";
		String[] numbers = imsiMsisdn.split(",");
		int imsiNumber = 0;
		int msisdnNumber = 0;
		for (String number : numbers) {
			if (number.length() > 13) {
				keyVa = "imsi"+String.valueOf(imsiNumber);
				imsiNumber++;
			} else {
				keyVa = "msisdn"+String.valueOf(msisdnNumber);
				msisdnNumber++;
				// 验证是否以“86”开头
				if (!number.matches("^86.*") && StringUtils.isNotEmpty(number)) {
					number = "86" + number;
				}
			}
			params.put(keyVa, number);
		}
	}

}
