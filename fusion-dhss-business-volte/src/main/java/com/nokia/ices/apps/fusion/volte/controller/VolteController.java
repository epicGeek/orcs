package com.nokia.ices.apps.fusion.volte.controller;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipOutputStream;

import javax.persistence.criteria.Predicate.BooleanOperator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.alarm.AlarmMonitor;
import com.nokia.ices.apps.fusion.alarm.repository.AlarmMonitorRepository;
import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.monitor.domain.MonitorTable;
import com.nokia.ices.apps.fusion.monitor.repository.MonitorTableRepository;
import com.nokia.ices.apps.fusion.system.domain.SystemOperationLog;
import com.nokia.ices.apps.fusion.volte.domain.VolteMessage;
import com.nokia.ices.apps.fusion.volte.repository.VolteMessageRepository;

@RepositoryRestController
@RestController
public class VolteController {

	@Autowired
	private VolteMessageRepository volteMessageRepository;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	AlarmMonitorRepository alarmMonitorRepository;
	
	@Autowired
	MonitorTableRepository monitorTableRepository;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	PagedResourcesAssembler pagedResourcesAssembler;
	private static final Logger logger = LoggerFactory.getLogger(VolteController.class);
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@RequestMapping("volte-counter/batchDownload")
	public void batchDownloadFile(
			@RequestParam(value="endTime",required=true) String endTime,
			@RequestParam(value="startTime",required=true) String startTime,
			HttpServletResponse response
			) throws IOException{
		String sql = "select file_abs_dir from volte_counter where stop_time between ? and ?";
		List<Map<String,Object>> l = jdbcTemplate.queryForList(sql,startTime,endTime);
		List<File> downLoadFileList = new ArrayList<>();
		for (Map<String, Object> map : l) {
			try {
				String fileAbsPath = map.get("file_abs_dir").toString();
				File aFile = new File(fileAbsPath);
				downLoadFileList.add(aFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		SimpleDateFormat shortSdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String zipName = "volte_counter_xml_"+shortSdf.format(new Date())+".zip";
		response.setContentType("APPLICATION/OCTET-STREAM");  
        response.setHeader("Content-Disposition","attachment; filename="+zipName);
        ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
        try {
            for(File aFile:downLoadFileList){
                ZipUtils.doCompress(aFile.getAbsolutePath(), out);
                response.flushBuffer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            out.close();
        }
	}
	
	@RequestMapping("volte-counter/counter-time-scope")
	public Map<String,String> counterDefaultTime(){
		Map<String,String> m = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar now = Calendar.getInstance();
		Date endTime = now.getTime();
		String endTimeStr = sdf.format(endTime);
		now.add(Calendar.DATE, -1);
		Date startTime = now.getTime();
		String startTimeStr = sdf.format(startTime);
		m.put("startTime", startTimeStr);
		m.put("endTime", endTimeStr);
		return m;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("volte-alarm/search-page")
	public PagedResources<AlarmMonitor> searchAlarmPage(@RequestParam(value = "dhssName", required = false) String dhssName,
			@RequestParam(value="endTime",required=false) String endTime,
			@RequestParam(value="startTime",required=false) String startTime, Pageable pageable,
			PersistentEntityResourceAssembler assembler){
		Map modelMap = new HashMap();
		if(StringUtils.isNotEmpty(dhssName)){
			modelMap.put("belongSite_EQ", dhssName);
		}
		if(StringUtils.isNotEmpty(startTime)){
			modelMap.put("startTime_GE", startTime);
		}
		if(StringUtils.isNotEmpty(startTime)){
			modelMap.put("startTime_LE", endTime);
		}
    	modelMap.put("alarmType_EQ", "SOAPGW");
    	
        Map<String,SearchFilter> filter = SearchFilter.parse(modelMap);
	        Specification<AlarmMonitor> spec = 
	                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, AlarmMonitor.class);
	        
	    Page<AlarmMonitor> page = alarmMonitorRepository.findAll(spec, pageable);
        return pagedResourcesAssembler.toResource(page, assembler);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/volte/table/search/searchByFilter")
    public PagedResources<SystemOperationLog> getSystemOperationLogList(
    		@RequestParam(value="startTime",required=false) String startTime,
    		@RequestParam(value="endTime",required=false) String endTime,
    		@RequestParam(value="fileName",required=false) String fileName,Pageable pageable,
            PersistentEntityResourceAssembler assembler) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();

        paramMap.put("name_LIKE", fileName);
        try {
        	if(StringUtils.isNotBlank(startTime)){
    			paramMap.put("createDate_GE",sdf.parse(startTime));
        	}

        	if(StringUtils.isNotBlank(endTime)){
    			paramMap.put("createDate_LE",sdf.parse(endTime));
        	}
		} catch (ParseException e) {
			e.printStackTrace();
		}
        paramMap.put("fileType_EQ","VOLTE");
        Map<String,SearchFilter> filter = SearchFilter.parse(paramMap);
		Specification<MonitorTable> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, MonitorTable.class);
		Page<MonitorTable> page =monitorTableRepository.findAll(spec, pageable);
        return pagedResourcesAssembler.toResource(page, assembler);
    }
	
	@RequestMapping("volte/downloadMonitor")
    public void downloadMonitor(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
    	String name = request.getParameter("name");
    	String path = request.getParameter("path");
    	try {
			download(request, response, path, "application/octet-stream",name);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 下载报表
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
		if(!file.exists()){
			file.createNewFile();
		}
		response.setContentType(contentType);
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String(realName.getBytes("utf-8"), "GBK"));
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
	
	@SuppressWarnings({ "resource" })
	@RequestMapping("volte-table/export")
	public void searchtablePage(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String startTime = request.getParameter("startTime");
    	String endTime = request.getParameter("endTime");
    	String imsi = request.getParameter("imsi");
		Map<String, List<VolteMessage>> map = new HashMap<String,List<VolteMessage>>();
		List<VolteMessage> boss2hsslist = volteMessageRepository.findAll(findVolteList(startTime, endTime, "boss2hss", imsi),new Sort(Direction.DESC,"startTime"));
		List<VolteMessage> hss2bosslist = volteMessageRepository.findAll(findVolteList(startTime, endTime, "hss2boss", imsi),new Sort(Direction.DESC,"startTime"));
		
		String [] headers1 = new String[]{"IMSI","MSISDN","开通状态","时间","时差","网元"};
		String [] headers2 = new String[]{"IMSI","MSISDN","开通状态","时间","时差","网元","自动开通"};
		
		map.put("boss2hss", boss2hsslist);
		map.put("hss2boss", hss2bosslist);
		XSSFWorkbook workbook = new XSSFWorkbook();
		for (String key : map.keySet()) {
			XSSFSheet sheet = workbook.createSheet(key);

			XSSFRow row = sheet.createRow(0);
			String [] headers = headers1;
			if(key.equals("boss2hss")){
				headers = headers2;
			}
			
			XSSFCellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(new XSSFColor(new Color(0xffffff00)));

			style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

			style.setAlignment(XSSFCellStyle.ALIGN_CENTER);

			style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			
			for (int i = 0; i < headers.length; i++) {

				XSSFCell cell = row.createCell(i);
				XSSFRichTextString text = new XSSFRichTextString(headers[i]);
				cell.setCellStyle(style);
				
				cell.setCellValue(text);

			}
			
			
			
			
			int index = 1;
			for (VolteMessage volte : map.get(key)) {
				row = sheet.createRow(index);
				
				row.createCell(0).setCellValue(new XSSFRichTextString(volte.getImsi()));
				
				row.createCell(1).setCellValue(new XSSFRichTextString(volte.getMsisdn()));
				
				row.createCell(2).setCellValue(new XSSFRichTextString(actionStatus(volte.getActionStatus())));
				
				row.createCell(3).setCellValue(new XSSFRichTextString(volte.getStartTime()));
				
				row.createCell(4).setCellValue(new XSSFRichTextString(volte.getCostTime()));
				
				row.createCell(5).setCellValue(new XSSFRichTextString(volte.getNeName()));
				
				if(key.equals("boss2hss"))
					row.createCell(6).setCellValue(new XSSFRichTextString(AutoProv(volte.getAutoProv())));
				
				index++;
			}
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String date = format.format(new Date());
		File file = new File(ProjectProperties.getVolteTablePath()+File.separator+date);
		if(!file.exists()){
			file.mkdirs();
		}
		String fileName = "VOLTE_AUTO_LIST"+format1.format(new Date())+".xlsx";
		String filePath = file.getPath()+File.separator+fileName;
		OutputStream out = new FileOutputStream(filePath);
		workbook.write(out);
		out.close();
		
		MonitorTable table = new MonitorTable();
		table.setCreateDate(new Date());
		table.setFilePath(filePath);
		table.setFileType("VOLTE");
		table.setName(fileName);
		monitorTableRepository.save(table);
		
		
		download(request, response, filePath, "application/octet-stream", fileName);
	}
	
	
	
	private String actionStatus(String statu){
		return statu != null && statu.equals("0") ? "成功" : "失败";
	}
	
	private String AutoProv(String autoProv){
		return autoProv != null && autoProv.equals("0") ? "否" : "是";
	}
	
	
	
	
	
	@RequestMapping("volte-list/chart-line")
	public Map<String,Object> findChartLine(
			@RequestParam(value = "dhssName", required = false) String dhssName,
			@RequestParam(value = "colName", required = false) String colName,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime){
	//	GregorianCalendar todayCal = new GregorianCalendar();
//		String thisTime = sdf.format(todayCal.getTime());
//		String groupTime = sdf.format(todayCal.getTime().getTime()-1000*60*60*24);
		List<Object> p = new ArrayList<Object>();
		p.add(startTime);
		p.add(endTime);
		String sql ="select "+colName+",stop_time,dhss from volte_counter where stop_time between ? and ?";
		if(StringUtils.isNotEmpty(dhssName)){
			sql += " and dhss = ? ";
			p.add(dhssName);
		}
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,p.toArray());
		Set<String> dates = new TreeSet<String>();
		Map<String,List<Double>> resultListMap = new HashMap<String,List<Double>>();
		String[] dhssNames = getDhssName();
		for (String name : dhssNames) {
			resultListMap.put(name, new ArrayList<Double>());
		}
		for (Map<String, Object> map : list) {
			dates.add(map.get("stop_time").toString());
			double rul = Double.valueOf(map.get(colName).toString().replace("%", ""));
			String dhss = map.get("dhss").toString();
			resultListMap.get(dhss).add(rul);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		resultMap.put("dates", dates);
		resultMap.put("result", resultListMap);
		return resultMap;
	}
	
	
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/volte-list/searchPageList", method = RequestMethod.GET)
	public PagedResources<PersistentEntityResource> recordPageList(
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "imsi", required = false) String imsi,
			@RequestParam(value = "type", required = false) String type, Pageable pageable,
			PersistentEntityResourceAssembler assembler) throws ParseException {
		return pagedResourcesAssembler.toResource(
				volteMessageRepository.findAll(findVolteList(startTime, endTime, type, imsi), pageable),
				assembler);

	}
	
	public Specification<VolteMessage> findVolteList(String startTime,String endTime,String type,String imsi) throws ParseException{
		List<SearchFilter> searchFilterAND = new ArrayList<SearchFilter>();
		if (StringUtils.isNotEmpty(startTime))
			searchFilterAND.add(new SearchFilter("startTime", Operator.GE, sdf.parse(startTime)));
		if (StringUtils.isNotEmpty(endTime))
			searchFilterAND.add(new SearchFilter("startTime", Operator.LE, sdf.parse(endTime)));
		if (StringUtils.isNotEmpty(type))
			searchFilterAND.add(new SearchFilter("flowDirection", Operator.EQ, type));

		Specification<VolteMessage> specificationAND = DynamicSpecifications.bySearchFilter(searchFilterAND,
				BooleanOperator.AND, VolteMessage.class);

		List<SearchFilter> searchFilterOR = new ArrayList<SearchFilter>();
		if (StringUtils.isNotEmpty(imsi)) {
			searchFilterOR.add(new SearchFilter("imsi", Operator.LIKE, imsi));
			searchFilterOR.add(new SearchFilter("msisdn", Operator.LIKE, imsi));
		}
		Specification<VolteMessage> specificationOR = DynamicSpecifications.bySearchFilter(searchFilterOR,BooleanOperator.OR, VolteMessage.class);
		return Specifications.where(specificationAND).and(specificationOR);
	}
	
	
	
	
	@RequestMapping(value = "/volte-list/getDhssName", method = RequestMethod.GET)
	public String[] getDhssName(){
		String sql = "select DISTINCT dhss_name from equipment_ne";
		List<Map<String, Object>> dhssNameList = jdbcTemplate.queryForList(sql);
		String[] dhssNameArray = new String[dhssNameList.size()];
		int i = 0;
		for (Map<String,Object> name : dhssNameList) {
			dhssNameArray[i] = name.get("dhss_name").toString();
			i++;
		}
		return dhssNameArray;
	}
	@RequestMapping(value = "/volte-list/volteCounter", method = RequestMethod.GET)
	public List<Map<String, Object>> volteCounterList(
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		String [] units = new String[]{         "",
												"","%",
												"","%",
												"","%",
												"s"};
		
		String[] colNameArray = new String[] { "period",
											   "subs_hss_2_boss","rates_hss_2_boss", 
											   "subs_boss_2_hss_t","rates_boss_2_hss_t",
											   "subs_boss_2_hss_o",  "rates_boss_2_hss_o",  
											   "ads_boss_hss" };
		
		String[] colNameArrayCN = new String[] { "统计时间段", 
												"上行(HSS2BOSS)VoLTE业务自动开通请求数量", "上行(HSS2BOSS)VoLTE业务自动开通成功率",
												"下行(BOSS2HSS)VoLTE业务自动开通请求数量","下行(BOSS2HSS)VoLTE业务自动开通成功率",
												"VoLTE业务主动开通请求数量",  "VoLTE业务主动开通成功率", 
												"BOSS开通VoLTE与HSS提醒时间平均差" };
		String[] soapgwArray = getDhssName();
		for (String soapgwName : soapgwArray) {
			logger.info("dhss name:"+soapgwName);
		}
		String sql = "SELECT\n" +
				"	dhss AS dn,\n" +
				"	concat(\n" +
				"		min(start_time),\n" +
				"		' -> ',\n" +
				"		max(stop_time)\n" +
				"	) AS period,\n" +
				"	sum(subs_hss_2_boss) AS subs_hss_2_boss,\n" +
				"	sum(subs_boss_2_hss_t) AS subs_boss_2_hss_t,\n" +
				"	sum(subs_boss_2_hss_o) AS subs_boss_2_hss_o,\n" +
				"	CASE sum(subs_hss_2_boss)\n" +
				"WHEN 0 THEN\n" +
				"	'N/A'\n" +
				"ELSE\n" +
				"	concat(\n" +
				"		FORMAT(\n" +
				"			sum(\n" +
				"				subs_hss_2_boss * rates_hss_2_boss_num\n" +
				"			) / sum(subs_hss_2_boss),\n" +
				"			2\n" +
				"		),\n" +
				"		'%'\n" +
				"	)\n" +
				"END AS rates_hss_2_boss,\n" +
				" CASE sum(subs_boss_2_hss_t)\n" +
				"WHEN 0 THEN\n" +
				"	'N/A'\n" +
				"ELSE\n" +
				"	concat(\n" +
				"		FORMAT(\n" +
				"			sum(\n" +
				"				subs_boss_2_hss_t * rates_boss_2_hss_t_num\n" +
				"			) / sum(subs_boss_2_hss_t),\n" +
				"			2\n" +
				"		),\n" +
				"		'%'\n" +
				"	)\n" +
				"END AS rates_boss_2_hss_t,\n" +
				" CASE sum(subs_boss_2_hss_o)\n" +
				"WHEN 0 THEN\n" +
				"	'N/A'\n" +
				"ELSE\n" +
				"	concat(\n" +
				"		FORMAT(\n" +
				"			sum(\n" +
				"				subs_boss_2_hss_o * rates_boss_2_hss_o_num\n" +
				"			) / sum(subs_boss_2_hss_o),\n" +
				"			2\n" +
				"		),\n" +
				"		'%'\n" +
				"	)\n" +
				"END AS rates_boss_2_hss_o,\n" +
				"concat(	\n" +
				"	FORMAT(\n" +
				"		sum(\n" +
				"			(\n" +
				"				subs_boss_2_hss_t + subs_boss_2_hss_o\n" +
				"			) * ads_boss_hss\n" +
				"		) / sum(\n" +
				"			subs_boss_2_hss_t + subs_boss_2_hss_o\n" +
				"		),\n" +
				"		2,\n" +
				"		's'\n" +
				"	),'s')\n" +
				"AS ads_boss_hss,\n" +
				" sum(subs_hss_2_boss) AS subs_hss_2_boss\n" +
				"FROM\n" +
				"	volte_counter\n" +
				"WHERE\n" +
//				"	stop_time BETWEEN DATE_SUB(now(), INTERVAL ? MINUTE)\n" +
				"	stop_time BETWEEN ? AND ?\n" +
				"AND now()\n" +
				"GROUP BY\n" +
				"	dhss";
		System.out.println(sql);
		List<Map<String, Object>> unconvertedList = jdbcTemplate.queryForList(sql, startTime,endTime);
		Map<String,Object>  valueMap = new HashMap<>();
		for (Map<String, Object> unconvertedMap : unconvertedList) {
			Set<String> keySet = unconvertedMap.keySet();
			String dn = unconvertedMap.get("dn").toString();
			for (String key : keySet) {
				valueMap.put(dn+key, unconvertedMap.get(key));
			}
		}

		for (int i = 0; i < colNameArray.length; i++) {
			Map<String,Object> rowMap = new HashMap<>();
			rowMap.put("counterName", colNameArrayCN[i]);
			rowMap.put("counterType", colNameArray[i]);
			rowMap.put("counterUnit", units[i]);
			for (String soapgwName : soapgwArray) {
				String key = colNameArray[i];
				rowMap.put(soapgwName, valueMap.getOrDefault(soapgwName+key,""));
			}
			resultList.add(rowMap);
		}
		return resultList;
	}
	
}
