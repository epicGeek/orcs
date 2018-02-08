package com.nokia.ices.apps.fusion.patrol.controller;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
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
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
//import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.command.repository.CommandCheckItemRepository;
import com.nokia.ices.apps.fusion.command.service.CommandService;
import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentNeType;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.jms.vo.Context;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckJob;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckResult;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckResultTmp;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckResultTmpExecFlag;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckScheduleResult;
import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckJobRepository;
import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckResultRepository;
import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckResultTmpRepository;
import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckScheduleResultRepository;
import com.nokia.ices.apps.fusion.patrol.service.SmartCheckJobService;
import com.nokia.ices.core.utils.Encodes;
import com.nokia.ices.core.utils.JsonMapper;

@RepositoryRestController
@RestController
public class SmartCheckController {

	@Autowired
	SmartCheckJobService smartCheckJobService;
	
	@Autowired
	SmartCheckScheduleResultRepository smartCheckScheduleResultRepository;
	
	@Autowired
	SmartCheckJobRepository  smartCheckJobRepository;
	
	@Autowired
	EquipmentUnitRepository equipmentUnitRepository;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	PagedResourcesAssembler pagedResourcesAssembler;
	
	@Autowired
	SmartCheckResultRepository smartCheckResultRepository;
	
	@Autowired
	CommandService commandService;
	
	@Autowired
    private JmsTemplate jmsTemplate;
	
	@Autowired
	CommandCheckItemRepository commandCheckItemRepository;
	
	@Autowired
	SmartCheckResultTmpRepository smartCheckResultTmpRepository;
	
	private static SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	private static SimpleDateFormat format1 = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	private Logger logger = LoggerFactory.getLogger(SmartCheckController.class);
//	private static SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	
//	@RequestMapping(value = "/smart-check-job/search/getUnitList", method = RequestMethod.GET)
//	public List<Map<String, Object>> getUnitList(){
//		return smartCheckJobService.findNeAll();
//	}
	
	@RequestMapping("add/result")
	public SmartCheckResult test(){
		SmartCheckResult result = new SmartCheckResult();
		result.setCheckItemId(423L);
		result.setCheckItemName("Rtp99工作状态检查");
		result.setNeId(6L);
		result.setNeName("HSS02FE02HLR");
		EquipmentNeType netype = EquipmentNeType.valueOf("NTHLRFE");
		result.setNeType(netype);
		result.setNeTypeName(netype.name());
		result.setScheduleId(678L);
		try {
			result.setStartTime(format.parse("2016-09-05 16:00:00"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		result.setUnitId(134L);
		result.setUnitName("h02nt02f128");
		result.setUnitType(EquipmentUnitType.valueOf("NTHLRFE"));
		result.setErrorMessage("error message==================================================================Processname                                Node   Status   QueueAv==================================================================AdvCfgTool_257                            CE_01     boot    direct");
		result.setFilePath("/Adapter04/h02nt02f128/2016/9/5/15/SSH_DHLR_COMMAND_20160905155234169447.src");
		result.setResultCode(false);
		return smartCheckResultRepository.save(result);
	}
	
	/**
	 * <p>
	 * 查询记录列表，分页展示
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/smart-check-job/search/recordPageList", method = RequestMethod.GET)
	public PagedResources<PersistentEntityResource> recordPageList(
			@RequestParam(value="startTime",required=false) String startTime,
			@RequestParam(value="endTime",required=false) String endTime,
			@RequestParam(value="jobName",required=false) String jobName,
			Pageable pageable,PersistentEntityResourceAssembler assembler) {
		
//		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("jobName", jobName);
		Page<SmartCheckScheduleResult> results = smartCheckJobService.getSmartCheckJobResultPageList(map,pageable);
		
//		return  smartCheckService.getSmartCheckJobResultPageList(map);
		return  pagedResourcesAssembler.toResource(results,assembler);

	}

	@RequestMapping(value = "rest/smart-check-job/search/smartCheckPageList", method = RequestMethod.GET)
	public Map<String, Object> smartCheckPageList(@RequestParam(value="start",required=false) String start,
			@RequestParam(value="length",required=false) String length,
			@RequestParam(value="checkItemName",required=false) String checkItemName,
			@RequestParam(value="unitType",required=false) String unitType,
			@RequestParam(value="neType",required=false) String neType,
			@RequestParam(value="type",required=false) String type,
			@RequestParam(value="neName",required=false) String neName,
			@RequestParam(value="scheduleId",required=false) String scheduleId,Pageable pageable) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("checkItemName", checkItemName.trim());
		map.put("scheduleId", scheduleId);
		map.put("type", type);
		map.put("neName", neName);
		map.put("unitType", unitType);
		map.put("offset", start);
		map.put("neType", neType);
		map.put("limit", length);
		Map<String, Object> resultMaps = smartCheckJobService.getSmartCheckResultPageList(map);
		return resultMaps;
	}
	
	@RequestMapping("execJob")
	public boolean execJob(@RequestParam(value="id",required=false)Long id){
		try {
			SmartCheckJob job = smartCheckJobRepository.findOne(id);
			Set<EquipmentUnit> equipmentUnitSet = equipmentUnitRepository.findListBySmartCheckJob(job);
			List<CommandCheckItem> checkItemSet = commandCheckItemRepository.findListBySmartCheckJob(job);
			
			Integer size = equipmentUnitSet.size();
			SmartCheckScheduleResult result = new SmartCheckScheduleResult();
			result.setStartTime(new Date());
			result.setAmountJob(size);
			result.setAmountUnit(size);
			result.setErrorUnit(0);
			result.setExecFlag(Byte.parseByte(String.valueOf("2")));
			result.setJobDesc(job.getJobDesc());
			result.setJobId(job.getId());
			result.setSmartCheckJob(job);
			result.setJobName(job.getJobName());
			result = smartCheckScheduleResultRepository.save(result);
			
			
			
			for (EquipmentUnit unit : equipmentUnitSet) {
				EquipmentNe ne  = unit.getNe();
						for (CommandCheckItem checkItem : checkItemSet) {
							if(unit.getNeType().equals(ne.getNeType())){
								SmartCheckResultTmp resultTmp = new SmartCheckResultTmp();
								resultTmp.setUuId(UUID.randomUUID().toString().replace("-", ""));
								resultTmp.setCheckItemId(checkItem.getId());
								resultTmp.setCheckItemName(checkItem.getName());
								resultTmp.setScheduleId(result.getId());
								String command = checkItem.getCommand();
								if(StringUtils.isNotEmpty(checkItem.getDefaultParamValues())){
									String [] values = checkItem.getDefaultParamValues().split(",");
									for (int i = 0; i < values.length; i++) {
										command.replace("$"+i, values[i]);
									}
								}
								resultTmp.setHostname(unit.getHostname());
								resultTmp.setScript(checkItem.getScript());
								resultTmp.setNetFlag(unit.getNetFlag());
								resultTmp.setCommand(command);
								resultTmp.setLoginPwd(unit.getLoginPassword());
								resultTmp.setRootPwd(unit.getRootPassword());
								resultTmp.setPort(unit.getServerPort()+"");
								resultTmp.setProtocol(unit.getServerProtocol().name());
								resultTmp.setIp(unit.getServerIp());
								resultTmp.setUserName(unit.getLoginName() + "&&" + checkItem.getAccount());
								resultTmp.setUnitId(unit.getId());
								resultTmp.setUnitName(unit.getUnitName());
								resultTmp.setUnitType(unit.getUnitType());
								resultTmp.setNeId(ne.getId());
								resultTmp.setNeName(ne.getNeName());
								resultTmp.setNeType(ne.getNeType());
								resultTmp.setExecFlag(SmartCheckResultTmpExecFlag.EXECUTING);
								resultTmp.setStartTime(result.getStartTime());
								resultTmp.setResultCode(false);
								resultTmp = smartCheckResultTmpRepository.save(resultTmp);
								
								
								StringBuilder paramsBuilder = new StringBuilder();
								String pwd = Encodes.encodeHex(resultTmp.getRootPwd() != null ? resultTmp.getRootPwd().getBytes() : "".getBytes());
								String [] unames = resultTmp.getUserName().split("&&");
								paramsBuilder.append(unames[0] + "," + pwd + ":" + resultTmp.getCommand());
								Map<String, Object> mq_map = new HashMap<String, Object>();
								mq_map.put("app", "dhss");
								mq_map.put("cacheTime", 5);
								mq_map.put("maxConnNum", resultTmp.getUnitType().equals("SGW") || resultTmp.getUnitType().equals("SOAP_GW") ? 8  : ProjectProperties.getMaxNum());
								mq_map.put("type",resultTmp.getNeType());
								mq_map.put("srcQ", "DHLR-TASK-SMART");
						        mq_map.put("destQ", ProjectProperties.getDesQName());
						        mq_map.put("sessionid", resultTmp.getUuId());
						        mq_map.put("ne",  resultTmp.getUnitName());
						        mq_map.put("neConnType",  "DHSS_"+resultTmp.getProtocol());
						        mq_map.put("password",  Encodes.encodeHex(resultTmp.getLoginPwd() != null ? resultTmp.getLoginPwd().getBytes() : "".getBytes()));
						        mq_map.put("port",  resultTmp.getPort());
						        mq_map.put("priority",  5);
						        mq_map.put("procotol",  resultTmp.getProtocol());
						        mq_map.put("username",  unames[0]);
						        mq_map.put("ip",  resultTmp.getIp());
						        mq_map.put("content",new Context(resultTmp.getProtocol() + "_DHLR_COMMAND|"+unames[1]+","+
						        		Encodes.encodeHex(resultTmp.getRootPwd() != null ? resultTmp.getRootPwd().getBytes() : "".getBytes())+":"+resultTmp.getCommand(),2,1));
						        mq_map.put("hostname", resultTmp.getHostname());
						        mq_map.put("netFlag", resultTmp.getNetFlag());
						        mq_map.put("vendor", "nokia");
						        mq_map.put("flag", "");
						        mq_map.put("cacheTime", 5);
						        mq_map.put("retryInterval", 3);
						        mq_map.put("retryTimes", 2);
						        mq_map.put("needJump", 0);
						        mq_map.put("jumpCount", 0);
						        mq_map.put("callInterfaceName", "");
						        mq_map.put("msg", "");
						        mq_map.put("src", "");
						        mq_map.put("exculde", 0);
						        mq_map.put("taskNum", 71001);
						        mq_map.put("unitType", resultTmp.getUnitType());
						        mq_map.put("msgCode", 71000);
						        
						        
						        jmsTemplate.setDefaultDestinationName(ProjectProperties.getDesQName());
								jmsTemplate.send(new MessageCreator() {
									public Message createMessage(Session session) throws JMSException {
										TextMessage txtMessage = session.createTextMessage("");
										txtMessage.setStringProperty("msgBody", new JsonMapper().toJson(mq_map));
										txtMessage.setIntProperty("msgCode",
												Integer.parseInt(mq_map.get("msgCode").toString()));
										txtMessage.setJMSPriority(5);
		
										logger.debug("message.getSrcQ() = {},message.getDestQ() = {}", mq_map.get("msgCode"),
												mq_map.get("destQ"));
										logger.debug(txtMessage.toString());
										return txtMessage;
									}
								});
								
							}
							
						}
						
			}
			
			
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("resource")
	@RequestMapping(value = "/smart-check-job/donwload/smartCheckDetailPageList", method = RequestMethod.GET)
	public void donwloadDmartCheckDetailPageList(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String unitId = request.getParameter("unitId");
		String neId = request.getParameter("neId");
		String neTypeId = request.getParameter("neTypeId");
		String unitTypeId = request.getParameter("unitTypeId");
		String checkItemId = request.getParameter("checkItemId");
		String checkItemName = request.getParameter("checkItemName");
		String scheduleId = request.getParameter("scheduleId");
		String resultCode = request.getParameter("resultCode");
		String unitName = request.getParameter("unitName");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		List<SmartCheckResult> list = smartCheckDetailPageList(unitId, neId, unitTypeId, neTypeId, checkItemId, 
				checkItemName, unitName, scheduleId, "", resultCode, startTime, endTime);
		String [] headers = new String[]{"网元类型","单元类型","网元名称","单元名称","巡检任务名称","巡检结果","执行时间","异常原因"};
		
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		XSSFRow row = sheet.createRow(0);
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
		for (SmartCheckResult result : list) {
			row = sheet.createRow(index);
			
			row.createCell(0).setCellValue(new XSSFRichTextString(result.getNeType().name()));
			
			row.createCell(1).setCellValue(new XSSFRichTextString(result.getUnitType().name()));
			
			row.createCell(2).setCellValue(new XSSFRichTextString(result.getNeName()));
			
			row.createCell(3).setCellValue(new XSSFRichTextString(result.getUnitName()));
			
			row.createCell(4).setCellValue(new XSSFRichTextString(result.getCheckItemName()));
			
			row.createCell(5).setCellValue(new XSSFRichTextString(result.isResultCode() ? "成功" : "失败"));
			
			row.createCell(6).setCellValue(new XSSFRichTextString(String.valueOf(result.getStartTime())));
			
			row.createCell(7).setCellValue(new XSSFRichTextString(result.getErrorMessage()));
			
			index++;
		}
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String fileName = "异常单元_"+format1.format(new Date())+".xlsx";
		
		
		String rootPath = request.getSession().getServletContext().getRealPath("") + File.separator + "do.download";
		
		String operationLogPath = rootPath + File.separator + fileName;
		
		OutputStream out = new FileOutputStream(operationLogPath);
		workbook.write(out);
		out.close();
		
		download(request, response, operationLogPath, "application/octet-stream", fileName);
		
	}

	@RequestMapping(value = "/smart-check-job/search/smartCheckDetailPageList", method = RequestMethod.GET)
	public List<SmartCheckResult> smartCheckDetailPageList(@RequestParam(value="unitId",required=false)String unitId,
			@RequestParam(value="neId",required=false)String neId,
			@RequestParam(value="unitTypeId",required=false)String unitTypeId,
			@RequestParam(value="neTypeId",required=false)String neTypeId,
			@RequestParam(value="checkItemId",required=false)String checkItemId,
			@RequestParam(value="checkItemName",required=false)String checkItemName,
			@RequestParam(value="unitName",required=false)String unitName,
			@RequestParam(value="scheduleId",required=false)String scheduleId,
			@RequestParam(value="type",required=false)String type,
			@RequestParam(value="resultCode",required=false)String resultCode,
			@RequestParam(value="startTime",required=false)String startTime,
			@RequestParam(value="endTime",required=false)String endTime) throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(unitId)){
			map.put("unitId_EQ", unitId);
		}
		if(StringUtils.isNotEmpty(neId)){
			map.put("neId_EQ",neId );
		}
		if(StringUtils.isNotEmpty(neTypeId)){
			map.put("neType_EQ", neTypeId);
		}
		if(StringUtils.isNotEmpty(unitTypeId)){
			map.put("unitType_EQ", unitTypeId);
		}
		if(StringUtils.isNotEmpty(checkItemId)){
			map.put("checkItemId_EQ", checkItemId);
		}
		if(StringUtils.isNotEmpty(checkItemName)){
			map.put("checkItemName_EQ", checkItemName);
		}
		if(StringUtils.isNotEmpty(scheduleId)){
			map.put("scheduleId_EQ", scheduleId);
		}
		if(StringUtils.isNotEmpty(resultCode)){
			map.put("resultCode_EQ", resultCode.equals("1") ? true : false);
		}
		if(StringUtils.isNotEmpty(unitName)){
			map.put("unitName_EQ", unitName);
		}
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		if(StringUtils.isNotEmpty(startTime)){
			map.put("startTime_GE", format.parse(startTime));
		}
		if(StringUtils.isNotEmpty(endTime)){
			map.put("startTime_LT", format.parse(endTime));
		}
		
		return smartCheckJobService.findSmartCheckResultByFilter(map);
	}
	@RequestMapping(value = "/downloadDetailLog", method = RequestMethod.GET)
	public void downloadDetailLog(HttpServletRequest request,
			HttpServletResponse response) {

		Map<String, String[]> reqParams = request.getParameterMap();
		String filePath = reqParams.containsKey("filePath") ? reqParams .get("filePath")[0] : "";
		String sessionText = reqParams.containsKey("logText") ? reqParams .get("logText")[0] : "";
		String rootPath = ProjectProperties.getCOMP_BASE_PATH();
		String operationPath = rootPath + filePath ;
		String operationLogName =  sessionText + ".txt";
		
		try {
			download(request, response, operationPath, "application/octet-stream",operationLogName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/allStop", method = RequestMethod.GET)
	public boolean allStop() {
		Iterable<SmartCheckJob> jobs = smartCheckJobRepository.findAll();
		for (SmartCheckJob smartCheckJob : jobs) {
			smartCheckJob.setExecFlag(2);
//			smartCheckJob.setIsDisable(1);
//			smartCheckJobService.sendMessage(smartCheckJob, 1);
		}
		smartCheckJobRepository.save(jobs);
		return true;
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
		
		String operationLogName = new String( realName.getBytes("UTF-8"), "iso-8859-1");
		if (request.getHeader("User-Agent").indexOf("Trident") != -1) {
			operationLogName = java.net.URLEncoder.encode(realName, "UTF-8");
		} 
		response.setHeader("Content-disposition", "attachment; filename=" + operationLogName);
		
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
    
     
	@RequestMapping(value = "/downloadAllLog", method = RequestMethod.GET)
	public void downloadAllLog(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		SimpleDateFormat formats = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
		Map<String, String[]> reqParams = request.getParameterMap();
		String sessionText = "SMART_RESULT_LOG_" + formats.format(new Date());
		String scheduleId = reqParams.containsKey("scheduleId") ? reqParams.get("scheduleId")[0] : "";
		sessionText = sessionText.replaceAll(":", "-").replaceAll(" ", "_").replaceAll("-", "_");
		String rootPath = request.getSession().getServletContext().getRealPath("");
		String operationPath = rootPath + File.separator + "do.download";
		String operationLogName =  sessionText+ ".zip";
		
		Map<String, List<Map<String, Object>>> dhssMap = new HashMap<String, List<Map<String, Object>>>();
		List<Map<String, Object>> resultList = this.smartCheckJobService.getAllDownLoadPath(scheduleId);
		for (Map<String, Object> map : resultList) {
			List<Map<String, Object>> list = dhssMap.get(map.get("dhss_name")) == null ? 
												new ArrayList<Map<String, Object>>() : dhssMap.get(map.get("dhss_name"));
			list.add(map);
			dhssMap.put(String.valueOf(map.get("dhss_name")), list);
		}
		
		List<File> files = new ArrayList<File>();
		for (String key : dhssMap.keySet()) {
			File operationLogFile = new File(operationPath + File.separator + key + ".txt");

			if (operationLogFile.exists() && operationLogFile.isFile()) {
				operationLogFile.delete();
			}
			OutputStream writer =  new FileOutputStream(operationLogFile);
			for (Map<String, Object> listMap : dhssMap.get(key)) {
				
					StringBuilder string = new StringBuilder();
					string.append("网元：");
					string.append(listMap.get("NE_NAME"));
					string.append("  ");					 
					string.append("单元：");
					string.append(listMap.get("UNIT_NAME"));
					string.append("  ");
					string.append("检查项：");
					string.append(listMap.get("CHECK_ITEM_NAME"));
					string.append("  ");
					string.append("时间：");
					String startTime = "";
					if(!org.springframework.util.StringUtils.isEmpty(listMap.get("START_TIME"))){
						try {
							startTime= format.format(format1.parse((String)listMap.get("START_TIME")));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				 
					string.append(startTime);

					writer.write(string.toString().getBytes());
					writer.write("\r\n".getBytes());
					writer.write("\r\n".getBytes());
				// 若检查结果成功，则读取检查日志
				// 若检查结果失败，则直接读取错误信息
				String resultLog = ProjectProperties.getCOMP_BASE_PATH() + (String) listMap.get("FILE_PATH");
				// 0:false 1:true

				InputStream reader = null;
				try {
					reader = new FileInputStream(resultLog);

					byte[] b = new byte[1024];
					int len = 0;
					while ((len = reader.read(b)) != -1) {
						writer.write(b, 0, len);
					}
						writer.write("\r\n".getBytes());
						writer.write("----------------------------------------------------------------------------------"
								.getBytes());
						writer.write("\r\n".getBytes());
				} catch (Exception e) {
					writer.write(e.getMessage().getBytes());
						writer.write("\r\n".getBytes());
						writer.write("----------------------------------------------------------------------------------"
								.getBytes());
						writer.write("\r\n".getBytes());

				} finally {
					if (null != reader) {
						reader.close();
					}
				}

				writer.flush();
			}
			writer.close();
			files.add(operationLogFile);
		}
		
		
		File file = new File(operationPath + File.separator + operationLogName);
        if (!file.exists()){   
            file.createNewFile();   
        }
        response.reset();
        //response.getWriter()
        //创建文件输出流
        FileOutputStream fous = new FileOutputStream(file);   
        /**打包的方法我们会用到ZipOutputStream这样一个输出流,
         * 所以这里我们把输出流转换一下*/
        ZipOutputStream zipOut = new ZipOutputStream(fous);
        /**这个方法接受的就是一个所要打包文件的集合，
         * 还有一个ZipOutputStream*/
        zipFile(files, zipOut);
        zipOut.close();
        fous.close();
        downloadZip(file,response,request);
    }
	
	public static HttpServletResponse downloadZip(File file, HttpServletResponse response, HttpServletRequest request) {
		try {
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();

			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			String operationLogName = new String(file.getName().getBytes("UTF-8"), "iso-8859-1");
			if (request.getHeader("User-Agent").indexOf("Trident") != -1) {
				operationLogName = java.net.URLEncoder.encode(operationLogName, "UTF-8");
			}
			// 如果输出的是中文名的文件，在此处就要用URLEncoder.encode方法进行处理
			response.setHeader("Content-Disposition", "attachment;filename=" + operationLogName);
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				File f = new File(file.getPath());
				f.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return response;
	}
	
	public static void zipFile(List<File> files,ZipOutputStream outputStream) {
		int size = files.size();
		for(int i = 0; i < size; i++) {
		    File file = (File) files.get(i);
		    zipFile(file, outputStream);
		}
	}
	
	public static void zipFile(File inputFile,
            ZipOutputStream ouputStream) {
        try {
            if(inputFile.exists()) {
                /**如果是目录的话这里是不采取操作的，
                 * 至于目录的打包正在研究中*/
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
                    //org.apache.tools.zip.ZipEntry
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    ouputStream.putNextEntry(entry);
                    // 向压缩文件中输出数据   
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        ouputStream.write(buffer, 0, nNumber);
                    }
                    // 关闭创建的流对象   
                    bins.close();
                    IN.close();
                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            zipFile(files[i], ouputStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/downloadLog", method = RequestMethod.GET)
	public void downloadLog(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		Map<String, String[]> reqParams = request.getParameterMap();
		String neTypeId = reqParams.containsKey("neTypeId") ? reqParams
				.get("neTypeId")[0] : "";
		String neId = reqParams.containsKey("neId") ? reqParams
				.get("neId")[0] : "";
		String type = reqParams.containsKey("type") ? reqParams
				.get("type")[0] : "";
		String id = reqParams.containsKey("id") ? reqParams.get("id")[0] : "";
		String unitTypeId = reqParams.containsKey("unitTypeId") ? reqParams
				.get("unitTypeId")[0] : "";
		String checkItemId = reqParams.containsKey("checkItemId") ? reqParams
				.get("checkItemId")[0] : "";
		String sessionText = reqParams.containsKey("sessionText") ? reqParams
				.get("sessionText")[0] : "";
		String scheduleId = reqParams.containsKey("scheduleId") ? reqParams
				.get("scheduleId")[0] : "";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("scheduleId", scheduleId);
		map.put("neTypeId", neTypeId);
		map.put("neId", neId);
		map.put("id", id);
		map.put("type", type);
		map.put("unitTypeId", unitTypeId);
		map.put("checkItemId", checkItemId);
		sessionText = sessionText.replaceAll(":", "-").replaceAll(" ", "_").replaceAll("-", "_");
		map.put("sessionText", sessionText);
//		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject()
//				.getPrincipals().getPrimaryPrincipal();
		String rootPath = request.getSession().getServletContext()
				.getRealPath("");
		String operationPath = rootPath + File.separator + "do.download";
		String operationLogName =  sessionText
				+ ".txt";
		String operationLogPath = operationPath + File.separator
				+ operationLogName;
		File operationDir = new File(operationPath);
		if (!operationDir.exists() || !operationDir.isDirectory()) {
			operationDir.mkdirs();
		}
		File operationLogFile = new File(operationLogPath);
		// 若汇总日志文件 已存在，则不再进行汇总

		if (operationLogFile.exists() && operationLogFile.isFile()) {
			// do nothing
			operationLogFile.delete();
		}   
		{
//			operationLogFile.createNewFile();
			OutputStream writer = null;
			List<Map<String, Object>> resultList = this.smartCheckJobService
					.getDownLoadPath(map);

			try {
				writer = new FileOutputStream(operationLogFile);

				for (Map listMap : resultList) {
					if (id == null || "".equals(id)) {
						StringBuilder string = new StringBuilder();
						string.append("网元：");
						string.append(listMap.get("NE_NAME"));
						string.append("  ");					 
						string.append("单元：");
						string.append(listMap.get("UNIT_NAME"));
						string.append("  ");
						string.append("检查项：");
						string.append(listMap.get("CHECK_ITEM_NAME"));
						string.append("  ");
						string.append("时间：");
						String startTime = "";
						if(!org.springframework.util.StringUtils.isEmpty(listMap.get("START_TIME"))){
							try {
								startTime= format.format(format1.parse((String)listMap.get("START_TIME")));
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					 
						string.append(startTime);

						writer.write(string.toString().getBytes());
						writer.write("\r\n".getBytes());
						writer.write("\r\n".getBytes());
					}

					// 若检查结果成功，则读取检查日志
					// 若检查结果失败，则直接读取错误信息
					String resultLog = ProjectProperties.getCOMP_BASE_PATH() + (String) listMap.get("FILE_PATH");
					// 0:false 1:true

					InputStream reader = null;
					try {
						reader = new FileInputStream(resultLog);

						byte[] b = new byte[1024];
						int len = 0;
						while ((len = reader.read(b)) != -1) {
							writer.write(b, 0, len);
						}
						if (id == null || "".equals(id)) {
							writer.write("\r\n".getBytes());
							writer.write("----------------------------------------------------------------------------------"
									.getBytes());
							writer.write("\r\n".getBytes());
						}
					} catch (Exception e) {
						writer.write(e.getMessage().getBytes());
						if (id == null || "".equals(id)) {
							writer.write("\r\n".getBytes());
							writer.write("----------------------------------------------------------------------------------"
									.getBytes());
							writer.write("\r\n".getBytes());
						}

					} finally {
						if (null != reader) {
							reader.close();
						}
					}

					writer.flush();
				}
			} catch (IOException e) {

				e.printStackTrace();
			} finally {
				if (null != writer) {
					try {
						writer.close();
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
			}
		}

		// 下载日志
		InputStream is = null;
		OutputStream os = null;

		try {
			request.setCharacterEncoding("UTF-8");
			long fileLength = operationLogFile.length();

			response.setContentType("application/octet-stream");

			// 如果客户端为IE
			// System.out.println(request.getHeader("User-Agent"));
			if (request.getHeader("User-Agent").indexOf("Trident") != -1) {
				operationLogName = java.net.URLEncoder.encode(operationLogName,
						"UTF-8");
			} else {
				operationLogName = new String(
						operationLogName.getBytes("UTF-8"), "iso-8859-1");
			}

			response.setHeader("Content-disposition", "attachment; filename="
					+ operationLogName);
			response.setHeader("Content-Length", String.valueOf(fileLength));

			is = new FileInputStream(operationLogFile);
			os = response.getOutputStream();

			byte[] b = new byte[1024];
			int len = 0;
			while ((len = is.read(b)) != -1) {
				os.write(b, 0, len);
			}
//			os.flush(); 
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * <p>
	 * 查询方案记录列表，分页展示
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/smart-check-job/search/findByFilter", method = RequestMethod.GET)
	public PagedResources<PersistentEntityResource> getSmartCheckJob(@RequestParam(value="jobName",required=false) String jobName,
			Pageable pageable,PersistentEntityResourceAssembler assembler) {
//		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject()jobName
//				.getPrincipals().getPrimaryPrincipal();
		
		Page<SmartCheckJob> page = smartCheckJobService.getSmartCheckJob(jobName,null,pageable);
		return pagedResourcesAssembler.toResource(page, assembler);

	}

//	@RequestMapping(value = "/smart-check-job/search/getJobEntity", method = { RequestMethod.POST,RequestMethod.GET })
//	public SmartCheckJob getJobEntity(HttpServletRequest request) {
//		String jobDay = request.getParameter("execTime");
//		String jobType = request.getParameter("jobType");
//		String jobName = request.getParameter("jobName");
//		String jobDesc = request.getParameter("jobDesc");
//		String jobId = request.getParameter("id");
////		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject()
////				.getPrincipals().getPrimaryPrincipal();
//		String [] array = jobDay.split(" ");
//		SmartCheckJob smartCheckJob = new SmartCheckJob();
//		if(jobId != null && !jobId.equals("")){
//			smartCheckJob = smartCheckJobService.getCheckJobById(Long.parseLong(jobId));
//		}else{
//			smartCheckJob.setCreateTime(new Date());
//			smartCheckJob.setExecFlag(0);
//		}
//		smartCheckJob.setJobType(Integer.parseInt(jobType));
//		smartCheckJob.setJobName(jobName);
//		smartCheckJob.setJobDesc(jobDesc);
//		
//		String [] nextDate = getNextTime(array[0], array[1], Integer.parseInt(jobType)).split(" ");
//		try {
//			smartCheckJob.setExecDay(dayFormat.parse(array[0]));
//			smartCheckJob.setNextDay(dayFormat.parse(nextDate[0]));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		smartCheckJob.setExecTime(array[1].substring(0,5));
//		smartCheckJob.setLoopHour(array[1].substring(0,5));
////		smartCheckJob.setUserId(shiroUser.getUser().getId());
//		return smartCheckJob;
//	}
	
//	@RequestMapping(value = "/findById", method = { RequestMethod.POST,
//			RequestMethod.GET })
//	public SmartCheckJob findById(HttpServletRequest request) {
//		String id = request.getParameter("id");
//		return smartCheckJobService.getCheckJobById(Long.parseLong(id));
//	}
	
//	@RequestMapping(value = "/smart-check-job/search/getCountNeAndItem" , method = { RequestMethod.POST,RequestMethod.GET })
//	public int getCountNeAndItem(HttpServletRequest request){
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("_parameter", request.getParameter("id"));
//		List<Map<String, Object>> neItem = smartCheckJobService.getCountNeAndItem(map);
//		for (Map neMap : neItem) {
//			if (neMap.get("JOB_ID") == null || neMap.get("AMOUNT1") == null || neMap.get("AMOUNT2") == null) {
//				return 0;
//			}
//		}
//		return 1;
//	}

	
	
	
	
	
	
	
	
	
//
//	@RequestMapping(value = "deleteJob", method = RequestMethod.POST)
//	public Integer deleteJob(@RequestParam("jobId") String jobId) {
//		try {
//			smartCheckService.removeJobByID(jobId);
//			return 1;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 0;
//		}
//	}
//
//	@RequestMapping(value = "/getNeList", method = RequestMethod.GET)
//	public List<Map<String, Object>> getNeList(
//			@RequestParam("jobId") String jobId,
//			@RequestParam("neName") String neName) {
//
//		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject()
//				.getPrincipals().getPrimaryPrincipal();
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("jobId", jobId);
//		map.put("neName", neName);
////		map.put("userId", shiroUser.getUser().getId());
//		List<Map<String, Object>> result = smartCheckJobService.getNeList(map);
//		return result;
//	}
//
//	@RequestMapping(value = "/saveNeList", method = { RequestMethod.POST,
//			RequestMethod.GET })
//	public Integer saveNeList(@RequestParam("neId") String neId,
//			@RequestParam("neTypeId") String neTypeId,
//			@RequestParam("neTypeName") String neTypeName,
//			@RequestParam("neName") String neName,
//			@RequestParam("jobId") String jobId,
//			@RequestParam("unitTypeId") String unitTypeId,
//			@RequestParam("unitTypeName") String unitTypeName) {
//
//		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject()
//				.getPrincipals().getPrimaryPrincipal();
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("neId", neId);
//		map.put("neTypeId", neTypeId);
//		map.put("neTypeName", neTypeName);
//		map.put("neName", neName);
//		map.put("jobId", jobId);
//		map.put("unitTypeId", unitTypeId);
//		map.put("unitTypeName", unitTypeName);
//		map.put("userId", shiroUser.getUser().getId());
//		try {
//			this.smartCheckService.saveNeList(map);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 0;
//		}
//
//		return 1;
//	}
//
//	@RequestMapping(value = "/batchUpdate/{jobId:\\d+}/{addOrRemove}", method = {
//			RequestMethod.POST, RequestMethod.GET })
//	public Integer batchUpdate(@PathVariable String jobId,
//			@PathVariable String addOrRemove, @RequestBody List<Map> neList) {
//		try {
//			this.smartCheckService.batchUpdate(jobId, addOrRemove, neList);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 0;
//		}
//
//		return 1;
//	}
//
	@RequestMapping(value = "/smart-check-job/search/getCheckItemList", method = RequestMethod.GET)
	public List<CommandCheckItem> getCheckItemList(
			@RequestParam(value = "itemName" ,required = false) String itemName) {
		List<CommandCheckItem> result = commandService.findCommandCheckItemBySearch(itemName);
		return result;
	}
	
	@RequestMapping(value = "/smart-check-result/count", method = RequestMethod.GET)
	public List<Map<String, Object>> smartCheckDetailPageList() throws ParseException {
		return smartCheckJobService.getSmartCheckCountResult();
		
	}
//
//	@RequestMapping(value = "/saveCheckItemList", method = {
//			RequestMethod.POST, RequestMethod.GET })
//	public Integer saveCheckItemList(
//			@RequestParam("checkItemId") String checkItemId,
//			@RequestParam("checkItemName") String checkItemName,
//			@RequestParam("neTypeName") String neTypeName,
//			@RequestParam("jobId") String jobId) {
//
//		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject()
//				.getPrincipals().getPrimaryPrincipal();
//
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("checkItemId", checkItemId);
//		map.put("checkItemName", checkItemName);
//		map.put("neTypeName", neTypeName);
//		map.put("jobId", jobId);
//		map.put("userId", shiroUser.getUser().getId() + "");
//		try {
//			this.smartCheckService.saveCheckItemList(map);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 0;
//		}
//
//		return 1;
//	}
//
//	@RequestMapping(value = "/batchUpdateCheckItem/{jobId:\\d+}/{addOrRemove}", method = {
//			RequestMethod.POST, RequestMethod.GET })
//	public Integer batchUpdateCheckItem(@PathVariable String jobId,
//			@PathVariable String addOrRemove,
//			@RequestBody List<Map> checkItemList) {
//		try {
//			this.smartCheckService.batchUpdateCheckItem(jobId, addOrRemove,
//					checkItemList);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 0;
//		}
//
//		return 1;
//	}
//
//	@RequestMapping(value = "changeJobState", method = RequestMethod.POST)
//	public Integer changeJobState(@RequestParam("jobId") String jobId) {
//		Integer result = 0;
//		try {
//			result = smartCheckService.updateJobState(jobId);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 3;
//		}
//		return result;
//	}
}
