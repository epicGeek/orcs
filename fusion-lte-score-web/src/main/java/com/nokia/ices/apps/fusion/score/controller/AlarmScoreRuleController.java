package com.nokia.ices.apps.fusion.score.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.score.common.utils.ExportExcel;
import com.nokia.ices.apps.fusion.score.common.utils.FileOperateUtil;
import com.nokia.ices.apps.fusion.score.common.utils.ObjectConvertMap;
import com.nokia.ices.apps.fusion.score.domain.AlarmScoreRule;
import com.nokia.ices.apps.fusion.score.service.AlarmScoreRuleService;
import com.nokia.ices.core.utils.JsonMapper;

/**
 * 告警评分规则
 * 
 * */
@RequestMapping("/alarmScore")
@RestController
public class AlarmScoreRuleController {

	public static final Logger logger = LoggerFactory
			.getLogger(AlarmScoreRuleController.class);
	private final static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	@Autowired
	AlarmScoreRuleService alarmScoreRuleService;

	@RequestMapping(value = "/search")
	public Page<AlarmScoreRule> getAlarmScoreRuleList(
			@RequestParam("alarmName") String alarmName,
			@RequestParam("alarmNo") String alarmNo,
			@RequestParam("page") Integer iDisplayStart, @RequestParam("pageSize") Integer iDisplayLength) {

		List<String> sortSet = new ArrayList<String>();
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("alarmName", alarmName);
		searchParams.put("alarmNo", alarmNo);
		Page<AlarmScoreRule> page = alarmScoreRuleService.findAlarmScoreRulePageBySearch(searchParams, null,iDisplayStart, iDisplayLength, sortSet);
		logger.debug(new JsonMapper().toJson(page));
		return page;
	}

	/**
	 * 根据属性值查询记录，=0不重复，>0重复
	 * 
	 * @return
	 */
	@RequestMapping(value = "/VerificationRepeat")
	public Integer VerificationRepeat(
			@RequestParam("searchFild") String searchFild,
			@RequestParam("valiDateFild") String valiDateFild,Sort sort) {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		if ("1".equals(valiDateFild)) {
			searchParams.put("alarmNo", searchFild);
			searchParams.put("alarmName", "");
		}
		List<AlarmScoreRule> alarmList = alarmScoreRuleService.findAlarmScoreRuleListByCreator(searchParams, sort);
		Integer flag = alarmList.size();
		return flag;

	}

	// 导出
	@RequestMapping(value = "/exportFile")
	public void saveSession(@RequestParam("alarmNo") String alarmNo,
				@RequestParam("alarmName") String alarmName,Sort sort,
				HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> searchParams = new HashMap<String, Object>();
		
		searchParams.put("alarmNo", alarmNo);
		searchParams.put("alarmName", alarmName);
		
		//ExportExcel<AlarmScoreRule> ex = new ExportExcel<AlarmScoreRule>();
		ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
		
		List<AlarmScoreRule> alarmList = alarmScoreRuleService.findAlarmScoreRuleListByCreator(searchParams, sort);
		
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		
		for(AlarmScoreRule rule:alarmList){
			Map<String, Object> data   = ObjectConvertMap.beanToMap(rule);
			listMap.add(data);
		}
		
		String[] headers = { "告警号:alarmNo", "告警名称:alarmNameCn","告警类别:alarmType",
				"时长:delayHour_1","时长扣除分数:delayScore_1","时长:delayHour_2","时长扣除分数:delayScore_2",
				"时长:delayHour_3","时长扣除分数:delayScore_3","频次:frequency_1","频次扣除分数:freScore_1",
				"频次:frequency_2","频次扣除分数:freScore_2","频次:frequency_3","频次扣除分数:freScore_3",
				"频次:frequency_4","频次扣除分数:freScore_4"};
		
		String alarmPath = FileOperateUtil.getPropertieValue("iceslte.kpi.file.path");
		String fileName = "告警规则-" + sdf.format(new Date()) + "-" + ".xls";
		OutputStream out = null;
		File fielDir = null;
		// 保存到本地xls
		try {
			File operationDir = new File(alarmPath);
			fielDir = new File(alarmPath + fileName);
			if (!operationDir.exists() && !operationDir.isDirectory()) {
				operationDir.mkdir();
			}
			if (!fielDir.exists()) {
				fielDir.createNewFile();
			}
			out = new FileOutputStream(fielDir);
			// 告警类别
			String[] alarmTypes = { "", "天馈", "光路", "传输", "电源", "时钟","软件", "硬件" };
		
			Map<String, String[]> maps = new HashMap<String, String[]>();
			maps.put("alarmType", alarmTypes);

			ex.exportExcel("告警规则数据导出", headers, listMap, out,"yyyy-MM-dd HH:mm:ss", "1", maps);

			// 导出excel
			FileOperateUtil.download(request, response, alarmPath + fileName,"application/octet-stream", fileName);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (null != out) {
				try {
					out.close();
					fielDir.delete();// 删除文件
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
				out = null;
			}
		}
	}

	/*// 上传
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public void uploadFile(@RequestParam("file") MultipartFile file,
			HttpServletRequest request) {

		try {
			if (!file.isEmpty()) {
				List<String[]> dataList = ExportExcel.readXls(
						file.getInputStream(), 9);
				for (String[] item : dataList) {
					AlarmScoreRule data = new AlarmScoreRule();
					data.setAlarmNo(Integer.parseInt(item[0]));
					data.setFaultId(Integer.parseInt(item[1]));
					data.setAlarmType(Integer.parseInt(item[2]));
					data.setAlarmLevel(Integer.parseInt(item[3]));
					data.setAlarmNameCn(item[4]);
					data.setAlarmDesc(item[5]);
					data.setAlarmMethod(item[6]);
					data.setAlarmHandle(Integer.parseInt(item[7]));
					data.setDeduction(Integer.parseInt(item[8]));
					alarmScoreRuleService.addAlarmScore(data);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
	        throw new RuntimeException(e);
		}
	}*/

	// 模板
	@RequestMapping(value = "/downFile")
	public void downloadLog(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			// fileLogPath =
			// FileOperateUtil.getPropertieValue("dhlr.userlog.file.path");
			String rootPath = request.getSession().getServletContext()
					.getRealPath("");
			String operationPath = rootPath + File.separator
					+ "file/template/alarmTemplate.xls";
			if (operationPath != null && !"".equals(operationPath)) {
				FileOperateUtil.download(request, response, operationPath,
						"application/octet-stream", "alarmTemplate.xls");
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
	        throw new RuntimeException(e);

		}

	}

}
