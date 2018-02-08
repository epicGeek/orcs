package com.nokia.ices.apps.fusion.quota.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentNeRepository;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.kpi.domain.KpiItem;
import com.nokia.ices.apps.fusion.kpi.repository.KpiItemRepository;
import com.nokia.ices.apps.fusion.quota.domain.QuotaMonitorHistory;
import com.nokia.ices.apps.fusion.quota.model.AllQuota;
import com.nokia.ices.apps.fusion.quota.service.AllQuotaService;
import com.nokia.ices.apps.fusion.quota.utils.ExportExcel;

@RestController
@RequestMapping("/allQuotaControll")
public class AllQuotaController {

	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(AllQuotaController.class);
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	ProjectProperties projectProperties;
	
	@Autowired
	AllQuotaService allQuotaService;
	
	@Autowired
	private EquipmentNeRepository equipmentNeRepository;
	
	@Autowired
	private KpiItemRepository kpiItemRepository;
	

	public AllQuotaService getAllQuotaService() {
		return allQuotaService;
	}

	public void setAllQuotaService(AllQuotaService allQuotaService) {
		this.allQuotaService = allQuotaService;
	}

	@RequestMapping(value = "/queryQuotaData")
	public Map<String, Object> queryQuotaData(@RequestParam(value = "bscName", required = true) String bscName,
			@RequestParam(value = "quotaName", required = true) String quotaName,
			@RequestParam(value = "startTime", required = true) String startTime,
			@RequestParam(value = "endTime", required = true) String endTime,
			@RequestParam(value = "dhss", required = true) String dhss,
			@RequestParam(value = "dhssLocation", required = true) String dhssLocation,Pageable pageable) {

		Map<String, Object> params = new HashMap<String, Object>();
		if(dhss!=null && dhss != "")
			params.put("dhssName_EQ", dhss);
		if(dhssLocation!=null && dhssLocation != "")
			params.put("nodeName_EQ", dhssLocation);
		if(bscName!=null && bscName != "")
			params.put("neName_EQ", bscName);
		if(quotaName!=null && quotaName != "")
			params.put("kpiCode_EQ", quotaName);
		else{
			List<KpiItem> ll = findKpiItems(new HashMap<String, Object>());
			quotaName = ll.size() == 0 ? "" : ll.get(0).getKpiCode();
			params.put("kpiCode_EQ", quotaName);
		}
		
			try {
				if(startTime!=null && startTime != "" ){
					params.put("periodStartTime_GE", simpleDateFormat.parse(startTime));
				}
				if( endTime != null && endTime != ""){
					params.put("periodStartTime_LE", simpleDateFormat.parse(endTime));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		
		Map<Date, QuotaMonitorHistory> mapItem = new HashMap<Date, QuotaMonitorHistory>();
		List<QuotaMonitorHistory> items = new ArrayList<>();
		List<QuotaMonitorHistory> list = allQuotaService.queryAllQuotaData(params, pageable);
		for (QuotaMonitorHistory quotaMonitorHistory : list) {
			if(mapItem.get(quotaMonitorHistory.getPeriodStartTime()) == null){
				mapItem.put(quotaMonitorHistory.getPeriodStartTime(), quotaMonitorHistory);
			}else{
				QuotaMonitorHistory qmh = mapItem.get(quotaMonitorHistory.getPeriodStartTime());
				quotaMonitorHistory.setKpiRequestCount(quotaMonitorHistory.getKpiRequestCount()+qmh.getKpiRequestCount());
				quotaMonitorHistory.setKpiSuccessCount(quotaMonitorHistory.getKpiSuccessCount()+qmh.getKpiSuccessCount());
				mapItem.put(qmh.getPeriodStartTime(), quotaMonitorHistory);
			}
		}
		for (Date date : mapItem.keySet()) {
			items.add(mapItem.get(date));
		}
		Collections.sort(items,new DateComparator());
		params.clear();
		params.put("kpiCode_EQ", quotaName);
		List<KpiItem> KpiItems = findKpiItems(params);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", items);
		result.put("total", list.size());
		result.put("item", KpiItems.size() == 0 ? null : KpiItems.get(0));
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	static class DateComparator implements Comparator {  
        public int compare(Object object1, Object object2) {// 实现接口中的方法  
        	QuotaMonitorHistory p1 = (QuotaMonitorHistory) object1; // 强制转换  
        	QuotaMonitorHistory p2 = (QuotaMonitorHistory) object2;  
            return p1.getPeriodStartTime().compareTo(p2.getPeriodStartTime());  
        }  
    }  
	
	public List<KpiItem> findKpiItems(Map<String, Object> paramMap){
    	Map<String, SearchFilter> filter = SearchFilter.parse(paramMap);
		Specification<KpiItem> spec = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				KpiItem.class);
		return kpiItemRepository.findAll(spec);
    }

	@RequestMapping(value = "/queryBscName")
	public Map<String, Object> queryBscName() {
		Iterable<EquipmentNe> nes = equipmentNeRepository.findAll();
		List<EquipmentNe> list = new ArrayList<EquipmentNe>();
		for (EquipmentNe equipmentNe : nes) {
			list.add(equipmentNe);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", list);
		result.put("total", list.size());
		return result;
	}

	public String quotaNameToId(String name) {
		if (name.equals("1"))
			return "鉴权成功率";
		else if (name.equals("2"))
			return "语音呼叫查询成功率";
		else if (name.equals("3"))
			return "短信被叫查询成功率";
		else if (name.equals("4"))
			return "位置更新请求成功率";
		else if (name.equals("5"))
			return "GPRS附着成功率";
		else if (name.equals("6"))
			return "LTE位置更新成功率";
		else if (name.equals("7"))
			return "LTE用户鉴权成功率";
		else if (name.equals("8"))
			return "MAP-CancelLocation请求次数";
		else if (name.equals("9"))
			return "S6A-CancelLocation请求次数";
		else if (name.equals("10"))
			return "HLR-LADP";
		else
			return "";
	}

	/**
	 * excel文件导出
	 * 
	 * @param file
	 *            文件
	 * @param request
	 * @return
	 * @return
	 */
	@RequestMapping(value = "/exportFileQuota")
	public void exportFileQuota(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String[]> params = request.getParameterMap();
		String bscName = params.containsKey("bscName") ? params.get("bscName")[0] : "";
		String quotaName = params.containsKey("quotaName") ? params.get("quotaName")[0] : "";
		String startTime = params.containsKey("startTime") ? params.get("startTime")[0] : "";
		String endTime = params.containsKey("endTime") ? params.get("endTime")[0] : "";


		// System.out.println(bscName+"=="+quotaName+"=="+startTime+"==="+endTime);
		
		Map<String, Object> paramsa = new HashMap<String, Object>();
		if(bscName!=null && bscName != ""){
//			EquipmentNe ne = equipmentNeRepository.findOne(Long.valueOf(bscName));
			paramsa.put("neName_EQ", bscName);
		}
		if(quotaName!=null && quotaName != "")
			paramsa.put("kpiCode_EQ", quotaName);
		if(startTime!=null && startTime != "" && endTime != null && endTime != ""){
			try {
				paramsa.put("periodStartTime_GE", simpleDateFormat.parse(startTime));
				paramsa.put("periodStartTime_LE", simpleDateFormat.parse(endTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		/*List<QuotaMonitorHistory> list = allQuotaService.queryAllQuotaData(paramsa);*/

		List<AllQuota> quotalist = allQuotaService.findPageByCriteriaAllQuotaExport(paramsa);

		ExportExcel<AllQuota> ex = new ExportExcel<AllQuota>();
		String ttil = "";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("kpiCode_EQ", quotaName);
		List<KpiItem> KpiItems = findKpiItems(param);
		if(KpiItems.size() == 0 )
			return;
		if (KpiItems.get(0).getOutPutField().equals("fail_count"))
			ttil = "失败次数";
		else if (KpiItems.get(0).getOutPutField().equals("success_rate"))
			ttil = "成功率";
		else
			ttil = "请求次数";
		String[] headers = { "单元名称", "网元名称", "指标名称", "指标周期", ttil , "单位", "失败次数", "请求次数" };
		String bossPath = ProjectProperties.getBossDataPath();
		// System.out.println(bossPath);
		String fileName = "AllQuotaExcel-" + sdf.format(new Date()) + "-" + ".xls";
		OutputStream out = null;
		// 保存到本地xls
		try {
			File operationDir = new File(bossPath);
			File fielDir = new File(bossPath + fileName);
			if (!operationDir.exists() && !operationDir.isDirectory()) {
				operationDir.mkdir();
			}
			if (!fielDir.exists()) {
				fielDir.createNewFile();
			}
			out = new FileOutputStream(fielDir);
			ex.exportExcel("指标监控", headers, quotalist, out, "yyyy-MM-dd HH:mm:ss");

			// 导出excel
			download(request, response, bossPath+fileName, "application/octet-stream",fileName);

		} catch (Exception e) {
			logger.error(e.getMessage());
            throw new RuntimeException(e);
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
				out = null;
			}
		}
	}
	/**
     * 下载日志
     * @param request
     * @param response
     * @param downLoadPath
     * @param contentType
     * @param realName
     * @throws Exception
     */
    public static void download(HttpServletRequest request,HttpServletResponse response, String downLoadPath,
			String contentType, String realName) throws Exception {
		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		File file = new File(downLoadPath);
		response.setContentType(contentType);
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(realName.getBytes("utf-8"), "ISO8859-1"));
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
}
