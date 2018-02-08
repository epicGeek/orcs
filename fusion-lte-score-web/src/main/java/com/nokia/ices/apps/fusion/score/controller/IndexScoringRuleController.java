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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nokia.ices.apps.fusion.score.common.utils.ExportExcel;
import com.nokia.ices.apps.fusion.score.common.utils.FileOperateUtil;
import com.nokia.ices.apps.fusion.score.common.utils.ObjectConvertMap;
import com.nokia.ices.apps.fusion.score.domain.IndexScoringRule;
import com.nokia.ices.apps.fusion.score.service.BtsScoreHourService;
import com.nokia.ices.apps.fusion.score.service.IndexScoringRuleService;
import com.nokia.ices.core.utils.JsonMapper;

/**
 * 
 * 指标评分规则
 */

@RequestMapping("/kpiIndex")
@RestController
public class IndexScoringRuleController {

	public static final Logger logger = LoggerFactory.getLogger(IndexScoringRuleController.class);
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	@Autowired
	IndexScoringRuleService indexScoringRuleService;

	@Autowired
	BtsScoreHourService btsScoreHourService;

	/**
	 * 根据属性值查询记录，=0不重复，>0重复
	 * 
	 * @return
	 */
	@RequestMapping(value = "/VerificationRepeat")
	public Integer VerificationRepeat(@RequestParam("searchFild") String searchFild,
			@RequestParam("valiDateFild") String valiDateFild) {
		// List<String> sortSet = new ArrayList<String>();
		Map<String, Object> searchParams = new HashMap<String, Object>();
		if ("1".equals(valiDateFild)) {
			searchParams.put("kpiId", searchFild);
			searchParams.put("cnName", "");
		} else if ("2".equals(valiDateFild)) {
			searchParams.put("kpiId", "");
			searchParams.put("cnName", searchFild);
		}
		// List<IndexScoringRule> indexList =
		// indexScoringRuleService.findIndexScoringRuleListByCreator(searchParams,
		// sortSet);
		List<IndexScoringRule> indexList = indexScoringRuleService.findIndexScoringRuleListByCreator(searchParams);
		Integer flag = indexList.size();
		return flag;

	}

	@RequestMapping(value = "/row")
	public Map<String, Object> getRowDataList(@RequestBody ModelMap params) {
		try {
			return btsScoreHourService.getKpiValueData(params);

		} catch (Exception e) {
			logger.error("get kpiDatas error .............. " + e.toString());
		}
		return null;
	}

	@RequestMapping(value = "/search")
	public Page<IndexScoringRule> getCheckItemList(@RequestParam("page") Integer iDisplayStart,
			@RequestParam("pageSize") Integer iDisplayLength, @RequestParam("kpiId") String kpiId) {

		List<String> sortSet = new ArrayList<String>();

		/*
		 * ShiroUser shiroUser = (ShiroUser)
		 * SecurityUtils.getSubject().getPrincipal(); SystemRole systemRole =
		 * shiroUser.getRole();
		 */
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("kpiId", kpiId);

		Page<IndexScoringRule> page = indexScoringRuleService.findIndexScoringRulePageBySearch(searchParams, null,
				iDisplayStart, iDisplayLength, sortSet);
		logger.debug(new JsonMapper().toJson(page));
		return page;
	}

	@RequestMapping(value = "/exportFile")
	public void exportFile(@RequestParam("kpiId") String kpiId, HttpServletRequest request,
			HttpServletResponse response) {

		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("kpiId", kpiId);

		ExportExcel<Map<String, Object>> ex = new ExportExcel<Map<String, Object>>();
		List<IndexScoringRule> indexList = indexScoringRuleService.findIndexScoringRuleListByCreator(searchParams);
		
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		
		for(IndexScoringRule rule:indexList){
			Map<String, Object> data   = ObjectConvertMap.beanToMap(rule);
			listMap.add(data);
		}

/*		String[] headers = { "KPIID:kpiId", "指标名称:cnName", "评分规则:scoreRule", "公式:formula","指标类型:kpiCategory",
				"门限判断分数:threshold", "门限判断符:relationThreshold", "kpi下限1:minValOne", "kpi上限1:maxValOne",
				"kpi下限2:minValTwo", "kpi上限2:maxValTwo", "kpi下限3:minValThree", "kpi上限3:maxValThree", "kpi下限4:minValFour",
				"kpi上限4:maxValFour", "kpi下限5:minValFive", "kpi上限5:maxValFive", "kpi下限6:minValSix", "kpi上限6:maxValSix",
				"下限判断符:operatorMin", "上限判断符:operatorMax", "创建时间:createTime" };*/
		String[] headers = { "KPIID:kpiId","指标类型:kpiCategory", "指标名称:cnName","评分规则:scoreRule"};
		
		String kpiPath = FileOperateUtil.getPropertieValue("iceslte.kpi.file.path");
		String fileName = "kpi评分规则-" + sdf.format(new Date())+ ".xls";
		OutputStream out = null;
		File fielDir = null;
		// 保存到本地xls
		try {
			File operationDir = new File(kpiPath);
			fielDir = new File(kpiPath + fileName);
			if (!operationDir.exists() && !operationDir.isDirectory()) {
				operationDir.mkdir();
			}
			if (!fielDir.exists()) {
				fielDir.createNewFile();
			}
			out = new FileOutputStream(fielDir);
			// 0:无 1:接入性 2:可用性 3：保持性 4：移动性 5：业务质量
			String[] kpiTypeValue = { "无", "接入性", "可用性", "保持性", "移动性", "业务质量" };
			Map<String, String[]> maps = new HashMap<String, String[]>();
			maps.put("kpiCategory", kpiTypeValue);
			
			ex.exportExcel("指标规则数据导出", headers, listMap, out, "yyyy-MM-dd HH:mm:ss", "1", maps);

			// 导出excel
			FileOperateUtil.download(request, response, kpiPath + fileName, "application/octet-stream", fileName);

		} catch (Exception e) {
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

	// 上传
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public void uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

		try {
			if (!file.isEmpty()) {
				List<String[]> dataList = ExportExcel.readXls(file.getInputStream(), 12);
				for (String[] item : dataList) {
					IndexScoringRule data = new IndexScoringRule();
					data.setKpiId(item[0]); // kpiID
					data.setCnName(item[1]); // 指标名称
					data.setScoreRule(item[2]); // 评分规则
					data.setCycle(Integer.parseInt(item[3])); // 周期
					data.setKpiCategory(Integer.parseInt(item[4])); // 指标类型

					data.setRelationThreshold(item[5]); // 门限判断符
					data.setThreshold(Double.parseDouble(item[6])); // 门限判断

					data.setMaxValOne(Integer.parseInt(item[7])); // 上限
					data.setMaxValTwo(Integer.parseInt(item[8])); // 上限
					data.setMaxValThree(Integer.parseInt(item[9])); // 上限
					data.setMaxValFour(Integer.parseInt(item[10])); // 上限
					data.setMaxValFive(Integer.parseInt(item[11])); // 上限
					data.setMaxValSix(Integer.parseInt(item[12])); // 上限

					data.setMinValOne(Integer.parseInt(item[13]));
					data.setMinValTwo(Integer.parseInt(item[14]));
					data.setMinValThree(Integer.parseInt(item[15]));
					data.setMinValFour(Integer.parseInt(item[16]));
					data.setMinValFive(Integer.parseInt(item[17]));
					data.setMinValSix(Integer.parseInt(item[18]));

					data.setOperatorMin(item[19]);
					data.setOperatorMax(item[20]);

					data.setScoreThreshold(Integer.parseInt(item[21])); // 门限扣除分数
					data.setScoreOne(Integer.parseInt(item[22])); // 扣除分数
					data.setScoreTwo(Integer.parseInt(item[23]));
					data.setScoreThree(Integer.parseInt(item[24]));
					data.setScoreFour(Integer.parseInt(item[25]));
					data.setScoreFive(Integer.parseInt(item[26]));
					data.setScoreSix(Integer.parseInt(item[27]));

					data.setHasThreshold(Integer.parseInt(item[28])); // 是否有阈值
					data.setCreateTime(new Date());
					indexScoringRuleService.addIndexScoringRule(data);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}

	}

	// 模板
	@RequestMapping(value = "/downFile")
	public void downloadLog(HttpServletRequest request, HttpServletResponse response) {
		try {
			// fileLogPath =
			// FileOperateUtil.getPropertieValue("dhlr.userlog.file.path");
			String rootPath = request.getSession().getServletContext().getRealPath("");
			String operationPath = rootPath + File.separator + "file/template/kpiTemplate.xls";
			if (operationPath != null && !"".equals(operationPath)) {
				FileOperateUtil.download(request, response, operationPath, "application/octet-stream",
						"kpiTemplate.xls");
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}

	}

	@RequestMapping(value = "/getKpiRule", method = RequestMethod.GET)
	public List<Map<String, String>> getKpiRule(HttpServletRequest request) {
		List<IndexScoringRule> indexList = indexScoringRuleService.findIndexScoringRuleAll();

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		if (indexList.size() > 0) {
			for (IndexScoringRule rule : indexList) {
				Map<String, String> rules = new HashMap<String, String>();
				rules.put("KPI_ID", rule.getKpiId());
				rules.put("KPI_NAME", rule.getCnName());
				rules.put("case_value", (int) rule.getThreshold() + "");
				result.add(rules);
			}
		}
		return result;
	}

}
