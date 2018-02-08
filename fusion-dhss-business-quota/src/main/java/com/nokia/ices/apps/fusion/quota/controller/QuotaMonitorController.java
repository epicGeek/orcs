package com.nokia.ices.apps.fusion.quota.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate.BooleanOperator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.kpi.domain.KpiItem;
import com.nokia.ices.apps.fusion.kpi.repository.KpiItemRepository;
import com.nokia.ices.apps.fusion.quota.domain.QuotaMonitor;
import com.nokia.ices.apps.fusion.quota.domain.QuotaMonitorHistory;
import com.nokia.ices.apps.fusion.quota.model.QuotaMonitorExport;
import com.nokia.ices.apps.fusion.quota.service.QuotaMonitorHistoryService;
import com.nokia.ices.apps.fusion.quota.service.QuotaMonitorService;
import com.nokia.ices.apps.fusion.quota.utils.ExportExcel;
@RepositoryRestController
@RestController
public class QuotaMonitorController {
	
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(QuotaMonitorController.class);
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	@Autowired
	QuotaMonitorService quotaMonitorService;
	@Autowired
	QuotaMonitorHistoryService quotaMonitorHistoryService;
	
	@Autowired
	KpiItemRepository kpiItemRepository;

	public QuotaMonitorService getQuotaMonitorService() {
		return quotaMonitorService;
	}

	public void setQuotaMonitorService(QuotaMonitorService quotaMonitorService) {
		this.quotaMonitorService = quotaMonitorService;
	}
	
	@SuppressWarnings("rawtypes")
    @Autowired
    PagedResourcesAssembler pagedResourcesAssembler;
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value ="quota-monitor/search/findByFilter")
    public PagedResources<QuotaMonitor> getQuotaMonitorDataList(
    		@RequestParam(value="scene",required=false) String scene,
    		@RequestParam(value="neId",required=false) String neId,
    		//@RequestParam(value="threshold",required=false) String threshold,
    		@RequestParam(value="kpiName",required=false) String kpiName,Pageable pageable,
            PersistentEntityResourceAssembler assembler) {
		//        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        // SystemRole systemRole = shiroUser.getRole();
       /* SystemRole systemRole = new SystemRole();*/
    	Map<String, Object> paramMap = new HashMap<String, Object>();

        paramMap.put("scene_EQ", scene);
        paramMap.put("neName_EQ", neId);
        paramMap.put("kpiCode_EQ", kpiName);
        Page<QuotaMonitor> page = quotaMonitorService.findQuotaMonitorFilter(paramMap, pageable);
        return pagedResourcesAssembler.toResource(page, assembler);
    }

	

	@RequestMapping(value = "/findPageByCriteria",method = RequestMethod.GET)
	public Map<String,Object> findPageByCriteria(HttpServletRequest request)throws RuntimeException {
		Map<String, String[]> params = request.getParameterMap();
		String page = params.containsKey("page") ? params.get("page")[0] : "0";
		String pageSize = params.containsKey("pageSize") ? params.get("pageSize")[0] : "10";
		String nodeName = params.containsKey("nodeName") ? params.get("nodeName")[0] : "";
		String neType = params.containsKey("neType") ? params.get("neType")[0] : "";
		String scene = params.containsKey("scene") ? params.get("scene")[0] : "";
		String kpiName = params.containsKey("kpiName") ?  params.get("kpiName")[0] : "";
		String rows = params.containsKey("rows") ?  params.get("rows")[0] : "";
		
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("nodeName", nodeName);
		m.put("neType", neType);
		m.put("scene", scene);
		m.put("kpiName", kpiName);
		
		if ((rows != null) && (page != null)) {
			m.put("rows", Integer.parseInt(rows));
			m.put("page", (Integer.parseInt(page) - 1) * Integer.parseInt(rows));
		}
		List<QuotaMonitor> list = quotaMonitorService.findPageByCriteria(m);
		Long total = (Long) quotaMonitorService.queryQuotaMonitorDataCount(m);
		Map<String,Object> pAlarm = new HashMap<String,Object>();
		pAlarm.put("totalCount", total);
		pAlarm.put("pageSize",pageSize);
		pAlarm.put("currPage", page);
		pAlarm.put("Data", list);
		
		return pAlarm;
	}
	
	@RequestMapping(value = "/toAlarmMonitor")
	public ModelAndView toAlarmMonitor(
			@RequestParam(value = "neName", required = true) String neName,
			@RequestParam(value = "neType", required = true) String neType,
			@RequestParam(value = "alarmType", required = true) String alarmType,
			@RequestParam(value = "startTime", required = true) String startTime,
			@RequestParam(value = "endTime", required = true) String endTime,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize
			)
			throws RuntimeException {
		ModelAndView mo=new ModelAndView("alarmMonitor/search");
		mo.addObject("neNamev", neName);
		mo.addObject("neType", neType);
		mo.addObject("alarmType", alarmType);
		mo.addObject("startTime", startTime);
		mo.addObject("endTime", endTime);
		mo.addObject("page", page);
		mo.addObject("pageSize",pageSize);
		return mo;
	}
	
	@RequestMapping(value = "/queryQuotaMonitorData")
	public Map<String, Object> queryQuotaMonitorData(
			@RequestParam(value = "nodeName", required = true) String nodeName,
			@RequestParam(value = "neType", required = true) String neType,
			@RequestParam(value = "scene", required = true) String scene,
			@RequestParam(value = "kpiName", required = true) String kpiName,
			@RequestParam(value = "rows", required = false) Integer rows,
			@RequestParam(value = "page", required = false) Integer page
			)
			throws RuntimeException {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nodeName", nodeName);
		params.put("neType", neType);
		params.put("scene", scene);
		params.put("kpiName", kpiName);
//		System.out.println(nodeName+"=="+neType+"=="+scene+"==="+kpiName);
		
		if ((rows != null) && (page != null)) {
			params.put("rows", rows);
			params.put("page", (page - 1) * rows);
		}
		
		
		
		List<Map<String, Object>> list = quotaMonitorService.queryQuotaMonitorData(params);
		Long total = (Long) quotaMonitorService.queryQuotaMonitorDataCount(params);
//		System.out.println("list:"+list.size()+"-total:"+total);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", list);
		result.put("total", total);
		return result;
	}
	
	@RequestMapping(value = "/queryBscType")
	public Map<String, Object> queryBscType() {

		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, Object>> list = quotaMonitorService.queryAllBscType(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", list);
		result.put("total", list.size());
		return result;
	}
	
	@RequestMapping(value = "/queryQuotaWindow")
	public Map<String, Object> queryQuotaWindow(
			@RequestParam(value = "nodeName", required = true) String nodeName,
			@RequestParam(value = "bscName", required = true) String bscName,
			@RequestParam(value = "quotaName", required = true) String quotaName,
			@RequestParam(value = "startTime", required = true) String startTime,
			@RequestParam(value = "endTime", required = true) String endTime
			)
			throws RuntimeException {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bscName", Integer.parseInt(bscName));
		params.put("quotaName", quotaName);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("nodeName", nodeName);
//		System.out.println(bscName+"=="+quotaName+"=="+startTime+"==="+endTime);
		List<Map<String, Object>> list = quotaMonitorService.queryAllQuotaData(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", list);
		result.put("total", list.size());
		return result;
	}
	
	public String quotaNameToId(String name){
		if(name.equals("1")) return "鉴权成功率";
		else  if(name.equals("2")) return "语音呼叫查询成功率";
		else  if(name.equals("3")) return "短信被叫查询成功率";
		else  if(name.equals("4")) return "位置更新请求成功率";
		else  if(name.equals("5")) return "GPRS附着成功率";
		else  if(name.equals("6")) return "LTE位置更新成功率";
		else  if(name.equals("7")) return "LTE用户鉴权成功率";
		else  if(name.equals("8")) return "MAP-CancelLocation请求次数";
		else  if(name.equals("9")) return "S6A-CancelLocation请求次数";
		else  if(name.equals("10")) return "HLR-LADP";
		else return "";
	}
	
	
	/**
	 * excel文件导出
	 * @param file 文件
	 * @param request
	 * @return 
	 * @return
	 */
	@RequestMapping(value = "/exportFileQuotaMonitor")
	public void exportFileQuotaMonitor(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "neId", required = false) String neId,
			@RequestParam(value = "scene", required = false) String scene,
			@RequestParam(value = "kpiName", required = false) String kpiName) {
			
			//String nodeName = params.containsKey("nodeName") ? params.get("nodeName")[0]: "1";
			/*String neId = params.containsKey("neId") ? params.get("neId")[0] : "";
			String scene = params.containsKey("scene") ? params.get("scene")[0] : "";
			String kpiName = params.containsKey("kpiName") ?  params.get("kpiName")[0] : "";*/
			
			Map<String, Object> paramsa = new HashMap<String, Object>();
			if(neId!=null && !neId.equals("")){
				paramsa.put("neName_EQ", neId);
			}
			if(scene!=null && !scene.equals("") ){
				paramsa.put("scene_EQ", scene);
			}
			if(kpiName!=null && !kpiName.equals("") ){
				paramsa.put("kpiCode_EQ", kpiName);
			}
			
			List<QuotaMonitorExport> quotaList = quotaMonitorService.findPageByCriteriaExport(paramsa);
			ExportExcel<QuotaMonitorExport> ex = new ExportExcel<QuotaMonitorExport>();
			
		    String[] headers = { "网元ID", "网元名称","网元类型","节点名称","指标名称","指标值","单位", "指标周期"};
		    String bossPath = ProjectProperties.getBossDataPath();
		    String fileName ="QuotaMonitorExcel-"+ sdf.format(new Date()) +"-"+ ".xls";
		    OutputStream out =null;
		    //保存到本地excel 
		    try{
		    	File operationDir = new File(bossPath);
		    	File fileDir = new File(bossPath+fileName);
		    	if (!operationDir.exists() && !operationDir.isDirectory()) {
		    		operationDir.mkdir();
		    	}
		    	if (!fileDir.exists()) {
		    		fileDir.createNewFile();
		    	}
			    out = new FileOutputStream(fileDir);
			    ex.exportExcel("指标监控",headers,quotaList,out,"yyyy-MM-dd HH:mm:ss");
			    //导出excel
			    download(request, response, bossPath+fileName, "application/octet-stream",fileName);
			    
		    }catch(Exception e){
		    	logger.error(e.getMessage());
	        	throw new RuntimeException(e.getMessage());
		    }finally{
		    	if(null!=out){
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
    
    @RequestMapping(value = "/queryNodeName")
	public Map<String, Object> queryNodeName() {

		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, Object>> list = quotaMonitorService.queryAllBscName(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", list);
		result.put("total", list.size());
		return result;
	}
    @RequestMapping(value = "/calculateKpiValue")
	public Map<String,List<Map<String,String>>> calculateKpiValue(@RequestParam(value = "kpiName", required = true) String kpiName )
    {
    	
    	Map<String,List<Map<String,String>>> listMap = new HashMap<String,List<Map<String,String>>>();
    	DecimalFormat    df   = new DecimalFormat("######0.00"); //保留两位小数
    	Map<String,Object> kpiItemMap = new HashMap<String,Object>();
    	List<KpiItem> items = findKpiItems(kpiItemMap);
    	for (KpiItem kpiItem : items) {
    		kpiItemMap.put("kpiCode_EQ", kpiItem.getKpiCode());
    		List<QuotaMonitor> list = quotaMonitorService.findQuotaMonitor(kpiItemMap);
    		Map<String,Integer> requestCountMap = new HashMap<String,Integer>();
    		Map<String,Integer> successCountMap = new HashMap<String,Integer>();
    		String kpiNames = "";
			for (QuotaMonitor monitor : list) {
				Integer requestCount = requestCountMap.get(monitor.getDhssName());
				requestCountMap.put(monitor.getDhssName(), requestCount == null ? monitor.getKpiRequestCount() : requestCount+monitor.getKpiRequestCount());
				kpiNames = monitor.getKpiName();
				Integer successCount = successCountMap.get(monitor.getDhssName());
				successCountMap.put(monitor.getDhssName(), successCount == null ? monitor.getKpiSuccessCount() : successCount+monitor.getKpiSuccessCount());
			}
			List<Map<String,String>> lMap = new ArrayList<Map<String,String>>();
			for (String key : requestCountMap.keySet()) {
				Map<String,String> m = new HashMap<String,String>();
				switch (kpiItem.getOutPutField()) {
					case "success_rate":
						Integer success = successCountMap.get(key);
						Integer request = requestCountMap.get(key);
						if(request==null || request == 0){
							m.put("value","100%");
						}else{
							m.put("value", df.format(Double.parseDouble(success.toString()) / Double.parseDouble(request.toString()) * 100) +"%");
						}
						break;
					case "fail_count":
						m.put("value", (requestCountMap.get(key) - successCountMap.get(key))+"次");
						break;
					case "total_count":
						m.put("value", requestCountMap.get(key)+"次");
						break;
				}
				m.put("dhssName", key);
				m.put("KpiName", kpiNames);
				lMap.add(m);
			}
			listMap.put(kpiItem.getKpiCode(),lMap);
		}
    	return listMap;
	}
    
    public List<KpiItem> findKpiItems(Map<String, Object> paramMap){
    	Map<String, SearchFilter> filter = SearchFilter.parse(paramMap);
		Specification<KpiItem> spec = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				KpiItem.class);
		return kpiItemRepository.findAll(spec);
    }

}

