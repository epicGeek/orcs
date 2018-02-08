package com.nokia.ices.apps.fusion.onekey.backup.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.emergency.domian.StepConfTable;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentTypeRelNeUnit;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentNeType;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentNeRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentTypeRelNeUnitRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.jms.CCZXJResult;
import com.nokia.ices.apps.fusion.jms.producer.DHSSMessageProducer;
import com.nokia.ices.apps.fusion.jms.vo.Context;
import com.nokia.ices.apps.fusion.onekey.backup.mbmodel.BackupRSInfo;
import com.nokia.ices.apps.fusion.onekey.backup.service.BackupService;
import com.nokia.ices.apps.fusion.onekey.domian.OnekeyBackup;
import com.nokia.ices.apps.fusion.onekey.domian.OnekeyBackupMinitor;
import com.nokia.ices.apps.fusion.onekey.repository.OnekeyBackupMinitorRepository;
import com.nokia.ices.apps.fusion.onekey.repository.OnekeyBackupRepository;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.core.utils.Encodes;
import com.nokia.ices.core.utils.FileOperateUtil;
import com.nokia.ices.core.utils.JsonMapper;
import com.nokia.ices.core.utils.SSHCommandExecutor;

/**
 * 一键备份功能:
 */
@RepositoryRestController
@RequestMapping("/backup")
@RestController
public class BackupController {
	private int wlei_count = 0;
	private final static SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

	@Resource(name = "backupService")
	private BackupService backupService;

	@Autowired
	DHSSMessageProducer messageProducer;

	SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	public Message message = null;

	public String[] res = null;

	public Map<String, String> reMes = new HashMap<String, String>();

	public ShiroUser shiroUser = null;

	@Autowired
	EquipmentNeRepository equipmentNeRepository;

	@Autowired
	EquipmentUnitRepository equipmentUnitRepository;

	@Autowired
	EquipmentTypeRelNeUnitRepository equipmentTypeRelNeUnitRepository;

	@Autowired
	OnekeyBackupMinitorRepository onekeyBackupMinitorRepository;

	@Autowired
	OnekeyBackupRepository onekeyBackupRepository;

	@SuppressWarnings("rawtypes")
	@Autowired
	PagedResourcesAssembler pagedResourcesAssembler;

	/****
	 * 删除网元监控数据
	 * 
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return List<BackupHistory>
	 ***/
	@RequestMapping(value = "/delBackUpMinitor", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> delBackUpMinitor(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		try {
			backupService.delBackupMinitor();
			resMap.put("result", "0");
		} catch (Exception e) {
			resMap.put("result", "1");
		}
		return resMap;
	}

	/****
	 * 得到所有网元树数据
	 * 
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return List<BackupHistory>
	 ***/
	@RequestMapping(value = "/getBackUpMinitor", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> getBackUpMinitor(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<OnekeyBackupMinitor> backUpMinitorList = new ArrayList<OnekeyBackupMinitor>();
		backUpMinitorList = backupService.getBacuMonitorLog();
		resMap.put("backUpMinitorList", backUpMinitorList);
		resMap.put("backUpingCount", backupService.getBackUpIngCount());

		return resMap;
	}

	/****
	 * 获取当前正在执行的网元信息
	 * 
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return List<BackupHistory>
	 ***/
	@RequestMapping(value = "/getExecutingMinitor", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> getExecutingMinitor(
			@RequestParam(value = "backUpUnits", required = true) String backUpUnits) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		List<OnekeyBackupMinitor> executingMinitorList = new ArrayList<OnekeyBackupMinitor>();
		String[] result_dirs = backUpUnits.split(",");
		List<String> array = new ArrayList<String>();
		for (String string : result_dirs) {
			array.add(string);
		}
		/*
		 * Map<String,Object> param=new HashMap<String,Object>();
		 * param.put("resultDir_IN", array);
		 */
		executingMinitorList = backupService.getExecutingMonitorLog(array);
		resMap.put("backUpMinitorList", executingMinitorList);

		return resMap;
	}

	private List<EquipmentNe> getByNeType(Iterable<EquipmentNe> all, EquipmentNeType neType) {
		List<EquipmentNe> nes = new ArrayList<EquipmentNe>();
		for (EquipmentNe equipmentNe : all) {
			if (equipmentNe.getNeType().name().equals(neType.name())) {
				nes.add(equipmentNe);
			}
		}
		return nes;
	}

	private List<EquipmentUnit> getByUnitTypeAndNeType(Iterable<EquipmentUnit> all, EquipmentNeType neType,
			EquipmentUnitType unitType, Long neId) {
		List<EquipmentUnit> units = new ArrayList<EquipmentUnit>();
		for (EquipmentUnit equipmentUnit : all) {
			if(equipmentUnit == null || equipmentUnit.getNeType() == null || neType == null || equipmentUnit.getUnitType() == null || unitType == null || equipmentUnit.getNeId() == null ){
				continue;
			}
			if (equipmentUnit.getNeType().name().equals(neType.name())
					&& equipmentUnit.getUnitType().name().equals(unitType.name()) && equipmentUnit.getNeId() == neId) {
				units.add(equipmentUnit);
			}
		}
		return units;
	}

	/****
	 * 得到所有网元树数据
	 * 
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return List<BackupHistory>
	 ***/
	@RequestMapping(value = "/getAllNeTree", method = { RequestMethod.POST, RequestMethod.GET })
	public List<Map<String, Object>> getAllNeTree(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		List<Map<String, Object>> neTypeList = new ArrayList<Map<String, Object>>();

		Map<String, List<String>> map = new HashMap<>();

		List<String> list = new ArrayList<String>();
		list.add("HSSFE");
		list.add("HSS_DRA");
		map.put("HSSFE", list);

		List<String> list1 = new ArrayList<String>();
		list1.add("PCC");
		list1.add("NTHLRFE");
		map.put("NTHLRFE", list1);

		List<String> list2 = new ArrayList<String>();
		list2.add("BE_DSA");
		map.put("ONE_NDS", list2);

		List<String> list3 = new ArrayList<String>();
		map.put("SGW", list3);
//		EquipmentNeType[] types = EquipmentNeType.values();

		Iterable<EquipmentNe> allNe = equipmentNeRepository.findAll();
		Iterable<EquipmentUnit> allUnit = equipmentUnitRepository.findAll();
		Map<String, Object> neTypeMap = null;
		for (String key : map.keySet()) {
			EquipmentNeType neType = EquipmentNeType.valueOf(key);
			neTypeMap = new HashMap<String, Object>();
			neTypeMap.put("id", neType.name());
			neTypeMap.put("text", neType.name());
			List<Map<String, Object>> neNameList = new ArrayList<Map<String, Object>>();
			List<EquipmentNe> neNames = getByNeType(allNe, neType);
			Map<String, Object> neNameMap = null;
			for (EquipmentNe nne : neNames) {
				neNameMap = new HashMap<String, Object>();
				neNameMap.put("id", nne.getId());
				neNameMap.put("text", nne.getNeName());
				List<Map<String, Object>> unitTypeList = new ArrayList<Map<String, Object>>();
				List<EquipmentTypeRelNeUnit> unitTypes = equipmentTypeRelNeUnitRepository.findByNeType(neType, null);
				Map<String, Object> unitTypeMap = null;
				for (EquipmentTypeRelNeUnit ute : unitTypes) {
					if(map.get(neType.name()) == null)
						continue;
					if (map.get(neType.name()).contains(ute.getUnitType().name())) {
						unitTypeMap = new HashMap<String, Object>();
						unitTypeMap.put("id", ute.getUnitType().name());
						unitTypeMap.put("text", ute.getUnitType().name());
						List<Map<String, Object>> unitNameList = new ArrayList<Map<String, Object>>();
						Map<String, Object> unitNameParm = new HashMap<String, Object>();
						unitNameParm.put("ne.id_EQ", String.valueOf(nne.getId()));
						unitNameParm.put("neType_EQ", neType.name());
						unitNameParm.put("unitType_EQ", ute.getUnitType().name());
						logger.info("allUnit:{}",allUnit);
						logger.info("neType:{}",neType);
						logger.info("ute.getUnitType():{}",ute.getUnitType());
						logger.info("nne.getId():{}",nne.getId());
						List<EquipmentUnit> units = getByUnitTypeAndNeType(allUnit, neType, ute.getUnitType(),
								nne.getId());
						Map<String, Object> unitMap = null;
						for (EquipmentUnit unit : units) {
							unitMap = new HashMap<String, Object>();
							unitMap.put("id", unit.getId());
							unitMap.put("text", unit.getUnitName());
							unitMap.put("allVal", neType.name() + "&" + ute.getUnitType().name() + "&" + unit
									.getUnitName());
							unitNameList.add(unitMap);
						}
						unitTypeMap.put("items", unitNameList);
						unitTypeList.add(unitTypeMap);
					}
				}
				if (neType.name().equals("SGW")) {
					neNameMap.put("allVal", nne.getNeType() + "&" + nne.getNeType() + "&" + nne.getNeType());
				}else{
					neNameMap.put("items", unitTypeList);
				}
				neNameList.add(neNameMap);
			}
			neTypeMap.put("items", neNameList);
			neTypeList.add(neTypeMap);
		}
		List<Map<String, Object>> sitejsonList = new ArrayList<Map<String, Object>>();
		Map<String, Object> sitejsonMap = new HashMap<String, Object>();
		sitejsonMap.put("id", "allsites");
		sitejsonMap.put("text", "所有网元");
		sitejsonMap.put("items", neTypeList);
		sitejsonList.add(sitejsonMap);
		return sitejsonList;
	}

	/****
	 * 查询备份的历史数据
	 * 
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return List<BackupHistory>
	 ***/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/searchOneKeyBackupHistoryList", method = { RequestMethod.POST, RequestMethod.GET })
	public PagedResources<OnekeyBackup> searchBackupHistory(
			@RequestParam(value = "nodeName", required = false) String nodeName,
			@RequestParam(value = "neType", required = false) String neType,
			@RequestParam(value = "unitType", required = false) String unitType, Pageable pageable,
			PersistentEntityResourceAssembler assembler) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(neType))
			paramMap.put("neType_EQ", neType);
		if (StringUtils.isNotEmpty(unitType))
			paramMap.put("unitType_EQ", unitType);
		if (StringUtils.isNotEmpty(nodeName))
			paramMap.put("unitName_LIKE", nodeName);
		Page<OnekeyBackup> page = backupService.searchBackupHistory(paramMap, pageable);
		return pagedResourcesAssembler.toResource(page, assembler);
		/*
		 * Map<String,Object> result=null; List<Map<String, Object>>
		 * backupHistoryList=null; String
		 * pSiteId=ServletRequestUtils.getStringParameter(httpServletRequest,
		 * "pSiteId",""); String
		 * pTypeId=ServletRequestUtils.getStringParameter(httpServletRequest,
		 * "pTypeId",""); String
		 * pNodeName=ServletRequestUtils.getStringParameter(httpServletRequest,
		 * "pNodeName",null);
		 * 
		 * int
		 * page=ServletRequestUtils.getIntParameter(httpServletRequest,"page",1)
		 * ;
		 * 
		 * int pageSize=ServletRequestUtils.getIntParameter(httpServletRequest,
		 * "pageSize",10);
		 * 
		 * Map<String,Object> searchParams=new HashMap<String,Object>();
		 * 
		 * //所有传递的参数的名称 Enumeration<String>
		 * paramNames=httpServletRequest.getParameterNames();
		 * System.out.println("page= "+page+" pageSize= "+pageSize); int
		 * start=(page-1)*pageSize; int limitCount=pageSize; System.out.println(
		 * "start= "+start+" limitCount= "+limitCount);
		 * searchParams.put("pSiteId",pSiteId);
		 * searchParams.put("pTypeId",pTypeId); searchParams.put("pNodeName",
		 * StringUtils.stripToEmpty(pNodeName));
		 * searchParams.put("start",start);
		 * searchParams.put("limit",limitCount);
		 * 
		 * backupHistoryList=backupService.searchBackupHistory(searchParams);
		 * int totalCount=backupService.searchBackupHistoryCount(searchParams);
		 * System.out.println("totalCount= "+totalCount);
		 * if(backupHistoryList!=null){ result=new HashMap<String,Object>();
		 * result.put("rows",backupHistoryList); result.put("total",totalCount);
		 * } // return new JsonMapper().toJson(backupHistoryList); return
		 * result;
		 */
	}

	/****
	 * 下载备份文件 根据系统配置的下载文件路径找到需要下载的文件，进行下载 如果没有找到则弹出提示未找到下载文件
	 *******/
	@RequestMapping(value = "/downloadBackupFile", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<byte[]> downloadBackFile(HttpServletRequest servletRequest,
			HttpServletResponse servletResponse) throws IOException {
		// 确认下载文件的参数是否正确 s
		String downloadFileName = ServletRequestUtils.getStringParameter(servletRequest, "downloadFileName", null);
		String downloadDisplayName = ServletRequestUtils.getStringParameter(servletRequest, "downloadFileDisplayName",
				null);
		// int
		// recordId=ServletRequestUtils.getIntParameter(servletRequest,"downloadRowId",-1);
		HttpHeaders headers = new HttpHeaders();
		String downloadRootFolder = null;
		byte[] result = null;
		if (StringUtils.stripToNull(downloadFileName) == null) {
			String msg = "未选择任何文件进行下载!";
			headers.setContentType(MediaType.TEXT_PLAIN);
			result = msg.getBytes();
		} else {
			// 获取下载文件的路径，从classpath中获取
			downloadRootFolder = ProjectProperties
					.getDownloadFolder()/*
										 * properties.getProperty(
										 * "backup.downloadFolder",null)
										 */;
			if (StringUtils.stripToNull(downloadRootFolder) == null) {
				String msg = "请配置下载文件的目录!";
				headers.setContentType(MediaType.TEXT_PLAIN);
				result = msg.getBytes();
			} else {
				File downloadFile = new File(downloadRootFolder, downloadFileName);
				if (!downloadFile.exists() || !downloadFile.isFile()) {
					String msg = "未找到下载文件,请联系管理员!";
					headers.setContentType(MediaType.TEXT_PLAIN);
					result = msg.getBytes();
				} else {
					servletResponse.setContentType("application/octet-stream"); // .exe
																				// file
					servletResponse.setHeader("Content-Disposition",
							"attachment; filename=\"" + (StringUtils.stripToNull(downloadDisplayName) == null
									? downloadFileName : downloadDisplayName) + "\"");
					result = FileUtils.readFileToString(downloadFile, "UTF-8").getBytes();
				}
			}
		}
		return new ResponseEntity<byte[]>(result, headers, HttpStatus.CREATED);
	}

	/****
	 * 暂时未用 一键备份功能 1.根据前台选择的站点，类型，以及节点名称获取服务器，端口，协议类型以及要执行的指令
	 * 2.根据协议类型登录到远程服务器，执行指令，进行备份 3.创建备份数据到数据库中
	 ***/
	@SuppressWarnings({ "unused", "static-access" })
	@RequestMapping("/doBackupAction")
	public @ResponseBody String doBackupAction(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, HttpSession session) {
		String resultCode = "-1";
		// 获取客户端传入的处理Id
		String processId = ServletRequestUtils.getStringParameter(httpServletRequest, "processId", null);
		session.setAttribute(processId, Double.valueOf(0));// 设置进度
		String siteId = ServletRequestUtils.getStringParameter(httpServletRequest, "siteId", null);
		String siteTypeId = ServletRequestUtils.getStringParameter(httpServletRequest, "siteTypeId", null);
		String nodeId = ServletRequestUtils.getStringParameter(httpServletRequest, "nodeId", null);

		String siteName = ServletRequestUtils.getStringParameter(httpServletRequest, "siteName", null);
		String siteTypeName = ServletRequestUtils.getStringParameter(httpServletRequest, "siteTypeName", null);
		String nodeName = ServletRequestUtils.getStringParameter(httpServletRequest, "nodeName", null);
		session.setAttribute(processId, Double.valueOf(0.3));// 设置进度
		// 1.查询待执行的命令信息，占比10%
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("siteId", siteId);
		params.put("siteTypeId", siteTypeId);
		params.put("nodeId", nodeId);

		params.put("siteName", siteName);
		params.put("siteTypeName", siteTypeName);
		params.put("nodeName", nodeName);

		BackupRSInfo backupRSInfo = backupService.searchBackupRSInfoParams(params);
		session.setAttribute(processId, Double.valueOf(1));// 设置进度
		if (backupRSInfo != null) {
			// 2 连接服务器，执行指令,备份到本地总共占比:70%
			// 是否需要传入HttpSession,一遍更新处理进度
			BackupRSInfo result = backupService.executeBackupCommands(backupRSInfo);
			session.setAttribute(processId, Double.valueOf(7)); // 设置进度
			if (result != null) {
				// 3.创建数据,并存储数据到数据库中占比20%
				// 获取用户的信息，需要放入项目中后放开
				Subject currentUser = SecurityUtils.getSubject();
				// ShiroDBRealm.ShiroUser shiroUser = (ShiroDBRealm.ShiroUser)
				// currentUser.getPrincipals().getPrimaryPrincipal();
				ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
				Long user_id = shiroUser.getUserId();
				// Long user_id = Long.valueOf(11);
				params.put("userId", user_id); // 设置操作者
				params.put("updateUserId", user_id); // 设置操作者
				session.setAttribute(processId, Double.valueOf(8)); // 设置进度
				int saveRecord = backupService.saveBackupDataAsHistory(params, result);
				session.setAttribute(processId, Double.valueOf(9)); // 设置进度
				if (saveRecord > 0) {
					resultCode = "0";
				}
				session.setAttribute(processId, Double.valueOf(10)); // 设置进度
			} else {
				resultCode = "-3";// 执行备份操作失败，详细可以查询日志
				session.setAttribute(processId, Double.valueOf(10)); // 设置进度
			}
		} else {
			try {
				Thread.currentThread().sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			resultCode = "-2";// 未查到任何远程服务器信息
			session.setAttribute(processId, Double.valueOf(10)); // 设置进度
		}
		return resultCode;
	}

	/****
	 * 一键备份功能检查进度
	 ***/
	@RequestMapping("/checkBackupProcessPercent")
	public @ResponseBody String checkBackupProcessPercent(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, HttpSession session) {
		// 创建一个处理的Id
		String processId = ServletRequestUtils.getStringParameter(httpServletRequest, "processId", null);
		String returnValue = "-1";
		if (StringUtils.stripToNull(processId) != null) {
			try {
				Double percent = Double.valueOf(session.getAttribute(processId).toString());
				returnValue = percent.toString();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return returnValue;
	}

	/****
	 * 加载Classpath的applicationContext.properties 用于获取系统配置的下载文件的目录
	 ******/
	public Properties loadSystemConfiguration() {
		ClassPathResource classPathResource = new ClassPathResource("application.properties");
		Properties properties = new Properties();
		try {
			properties.load(classPathResource.getInputStream());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return properties;
	}

	/**
	 * <p>
	 * 查询站点列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/siteList", method = RequestMethod.GET)
	public List<Map<String, String>> siteList(HttpServletRequest request) {
		logger.info("enter	IntelligentInspectionManualController	execute		siteList");
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		List<Map<String, Object>> sites = backupService.sites();

		Map<String, String> dataMap = null;
		for (Map<String, Object> site : sites) {
			dataMap = new HashMap<String, String>();
			dataMap.put("siteCode", site.get("site").toString());
			dataMap.put("siteName", site.get("site").toString());
			dataList.add(dataMap);
		}
		return dataList;
	}

	/**
	 * <p>
	 * 查询网元类型列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/allNeTypeList", method = RequestMethod.GET)
	public EquipmentNeType[] ALlNeTypeList(/* HttpServletRequest request */) {
		return EquipmentNeType.values();
		/*
		 * logger.info(
		 * "enter	IntelligentInspectionManualController	execute		siteList"
		 * ); List<Map<String, String>> dataList = new ArrayList<Map<String,
		 * String>>(); List<Map<String, Object>> sites =
		 * backupService.getNeType_t(null);
		 * 
		 * Map<String, String> dataMap = null; for (Map<String, Object> site :
		 * sites) { dataMap = new HashMap<String, String>();
		 * dataMap.put("siteCode", site.get("ne_type_id").toString());
		 * dataMap.put("siteName", site.get("ne_type_name").toString());
		 * dataList.add(dataMap); } return dataList;
		 */
	}

	/**
	 * <p>
	 * 查询网元类型列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getUnitTypeByBackUpHist", method = RequestMethod.GET)
	public List<EquipmentTypeRelNeUnit> getUnitTypeByBackUpHist(
			@RequestParam(value = "neType", required = true) String neType) {
		if (!StringUtils.isNotEmpty(neType)) {
			return new ArrayList<EquipmentTypeRelNeUnit>();
		} else {
			return equipmentTypeRelNeUnitRepository.findByNeType(EquipmentNeType.valueOf(neType), null);
		}
		/*
		 * logger.info(
		 * "enter	IntelligentInspectionManualController	execute		getUnitTypeByBackUpHist"
		 * ); Map<String, String[]> parameterMap = request.getParameterMap();
		 * List<Map<String, String>> dataList = new ArrayList<Map<String,
		 * String>>(); String location = null ==
		 * parameterMap.get("filter[filters][0][value]") ? "" : parameterMap
		 * .get("filter[filters][0][value]")[0]; Map<String, String> params =
		 * new HashMap<String, String>(); params.put("location", location);
		 * 
		 * List<Map<String, Object>> neTypes =
		 * backupService.getUnitTypeByBackUpHist(params);
		 * 
		 * Map<String, String> dataMap = null; for (Map<String, Object> neType :
		 * neTypes) { dataMap = new HashMap<String, String>(); //
		 * dataMap.put("neTypeCode", neType.get("parent").toString()); //
		 * dataMap.put("neTypeName", neType.get("netype").toString());
		 * dataMap.put("neTypeCode", neType.get("id").toString());
		 * dataMap.put("neTypeName", neType.get("name").toString());
		 * dataList.add(dataMap); } return dataList;
		 */
	}

	/**
	 * <p>
	 * 查询网元类型列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/neTypeList", method = RequestMethod.GET)
	public List<Map<String, String>> neTypeList(HttpServletRequest request) {
		logger.info("enter	IntelligentInspectionManualController	execute		neTypeList");
		Map<String, String[]> parameterMap = request.getParameterMap();
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		String location = null == parameterMap.get("filter[filters][0][value]") ? ""
				: parameterMap.get("filter[filters][0][value]")[0];
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("location", location);

		List<Map<String, Object>> neTypes = backupService.neTypes(params);

		Map<String, String> dataMap = null;
		for (Map<String, Object> neType : neTypes) {
			dataMap = new HashMap<String, String>();
			// dataMap.put("neTypeCode", neType.get("parent").toString());
			// dataMap.put("neTypeName", neType.get("netype").toString());
			dataMap.put("neTypeCode", neType.get("id").toString());
			dataMap.put("neTypeName", neType.get("name").toString());
			dataList.add(dataMap);
		}
		return dataList;
	}

	/**
	 * <p>
	 * 查询板卡列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/unitList", method = RequestMethod.GET)
	// public List<Map<String, String>> unitList(HttpServletRequest request) {
	public List<Map<String, String>> unitList(@RequestParam(value = "site", required = true) String site,
			@RequestParam(value = "unit_type", required = true) Integer unit_type) throws Exception {
		logger.info("enter	IntelligentInspectionManualController	execute		unitList");
		// Map<String, String[]> parameterMap = request.getParameterMap();
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		// String unit_type = null ==
		// parameterMap.get("filter[filters][0][value]") ? "" : parameterMap
		// .get("filter[filters][0][value]")[0];
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("site", site);
		params.put("unit_type", unit_type);
		List<Map<String, Object>> units = backupService.units(params);

		Map<String, String> dataMap = null;
		for (Map<String, Object> unit : units) {
			dataMap = new HashMap<String, String>();
			// dataMap.put("unitCode", unit.get("ne").toString());
			// dataMap.put("unitName", unit.get("unit").toString());
			dataMap.put("unitCode", unit.get("id").toString());
			dataMap.put("unitName", unit.get("unit").toString());
			dataList.add(dataMap);
		}
		return dataList;

	}

	/**
	 * 添加隔离执行步骤到列表
	 * 
	 * @param site
	 * @param ne_type
	 * @param unit
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getStepByType", method = RequestMethod.GET)
	public List<Map<String, String>> getStepByType(@RequestParam(value = "site", required = false) String site,
			@RequestParam(value = "unit_type", required = true) String unit_type,
			@RequestParam(value = "ne_type", required = true) String ne_type,
			@RequestParam(value = "unit", required = true) String unit) throws Exception {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("neType_EQ", ne_type);
		params.put("confType_EQ", "6");// 一键备份
		List<StepConfTable> stepList = backupService.getStepList(params);

		Map<String, String> dataMap = null;
		for (StepConfTable step : stepList) {
			dataMap = new HashMap<String, String>();
			dataMap.put("step_seq", String.valueOf(step.getStepSeq()));
			dataMap.put("step_command", step.getStepCommand());
			dataMap.put("step_describe", step.getStepDescribe());
			dataMap.put("step_explain", step.getStepExplain());
			dataList.add(dataMap);
		}
		return dataList;
	}

	@RequestMapping(value = "/oneKeyCmdBackUp", method = RequestMethod.POST)
	public Map<String, Object> oneKeyCmdBackUp(@RequestParam(value = "backUpUnits", required = true) String backUpUnits,
			@RequestParam(value = "stepId", required = false) Integer stepId,
			@RequestParam(value = "command", required = false) String command,
			@RequestParam(value = "isLocalhost", required = false) boolean isLocalhost) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		List<String> unitList = new ArrayList<String>();
		try {
			if (stepId == null && StringUtils.isEmpty(command))
				stepId = 0;
			backupService.delBackupMinitor();
			System.out.println(backUpUnits);
			CollectionUtils.addAll(unitList, backUpUnits.split(","));
			// List<String> units=new ArrayList<String>();
			// units.add(unitList.get(0));
			// unitList.remove(0);

			ShiroUser shiroUser = this.getShiroUser();
			for (String utpm : unitList) {
				OnekeyBackupMinitor obm = new OnekeyBackupMinitor();
				String[] um = utpm.split("&");
				obm.setNeType(um[0]);
				obm.setUnitType(um[1]);
				obm.setUnitName(um[2]);
				obm.setStepId(String.valueOf(stepId));
				obm.setCommand(command);
				obm.setStartTime(null);
				obm.setEndTime(null);
				obm.setResultDir(utpm);
				obm.setResultCode("");
				obm.setIsFulish("1");
				obm.setSystemUser(shiroUser.getUserName());
				obm.setErrorMsg("未执行");
				if (stepId == 0) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("neType_EQ", um[0]);
					params.put("confType_EQ", "6");// 一键备份
					List<StepConfTable> stepList = backupService.getStepList(params);
					for (StepConfTable step : stepList) {
						obm.setStepId(step.getStepSeq().toString());
						obm.setCommand(step.getStepCommand());
						if (onekeyBackupMinitorRepository.save(obm) == null) {
							backupService.delBackupMinitor();
							result.put("error", -2);
							return result;
						}
					}
				}
				if (onekeyBackupMinitorRepository.save(obm) == null) {
					backupService.delBackupMinitor();
					result.put("error", -2);
					return result;
				}

			}
			for (String utpm : unitList) {
				Map<String, Object> params = new HashMap<String, Object>();
				String[] um = utpm.split("&");
				params.put("neType_EQ", um[0]);
				params.put("confType_EQ", "6");// 一键备份
				List<StepConfTable> stepList = backupService.getStepList(params);
				Integer stepSize = stepList.size();
				for (StepConfTable step : stepList) {
					if (stepId == 0) {
						Map<String, Object> upParam = new HashMap<String, Object>();
						upParam.put("neType_EQ", um[0]);
						upParam.put("unitType_EQ", um[1]);
						upParam.put("unitName_EQ", um[2]);
						upParam.put("stepId_EQ", step.getStepSeq().toString());
						upParam.put("systemUser_EQ", shiroUser.getUserName());
						List<OnekeyBackupMinitor> lists = backupService.getMonitorByOther(upParam);
						for (OnekeyBackupMinitor onekeyBackupMinitor : lists) {
							onekeyBackupMinitor.setExecuting(0);
							onekeyBackupMinitor.setStartTime(new Date());
						}
						onekeyBackupMinitorRepository.save(lists);

						boolean flag = unitBackUp(um[0], um[1], um[2], step.getStepSeq(), step.getStepCommand(),
								isLocalhost,stepSize);
						String resultCode = flag ? "0" : "1";
						List<OnekeyBackupMinitor> data = backupService.getMonitorByOther(upParam);
						for (OnekeyBackupMinitor onekeyBackupMinitor : data) {
							onekeyBackupMinitor.setResultCode(resultCode);
							onekeyBackupMinitor.setExecuting(1);
							onekeyBackupMinitor.setErrorMsg(reMes.get("errormsg"));
						}
						onekeyBackupMinitorRepository.save(data);
					} else {
						if (stepId.toString().equals(step.getStepSeq().toString())
								&& command.equals(step.getStepCommand())) {
							Map<String, Object> upParam = new HashMap<String, Object>();
							upParam.put("neType_EQ", um[0]);
							upParam.put("unitType_EQ", um[1]);
							upParam.put("unitName_EQ", um[2]);
							upParam.put("stepId_EQ", stepId.toString());
							upParam.put("systemUser_EQ", shiroUser.getUserName());
							List<OnekeyBackupMinitor> lists = backupService.getMonitorByOther(upParam);
							for (OnekeyBackupMinitor onekeyBackupMinitor : lists) {
								onekeyBackupMinitor.setExecuting(0);
								onekeyBackupMinitor.setStartTime(new Date());
							}
							onekeyBackupMinitorRepository.save(lists);

							boolean flag = unitBackUp(um[0], um[1], um[2], stepId, command, isLocalhost,stepSize);
							String resultCode = flag ? "0" : "1";
							List<OnekeyBackupMinitor> data = backupService.getMonitorByOther(upParam);
							for (OnekeyBackupMinitor onekeyBackupMinitor : data) {
								onekeyBackupMinitor.setResultCode(resultCode);
								onekeyBackupMinitor.setExecuting(1);
								onekeyBackupMinitor.setErrorMsg(reMes.get("errormsg"));
							}
							onekeyBackupMinitorRepository.save(data);
						}
					}
				}
			}
			result.put("unitList", unitList);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
	}

	@RequestMapping(value = "/oneKeyBackUp", method = RequestMethod.GET)
	public Map<String, Object> oneKeyBackUp(@RequestParam(value = "backUpUnits", required = true) String backUpUnits)
			throws Exception {

		return null;
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "/executeBackup", method = RequestMethod.GET)
	public Map<String, Object> executeBackup(@RequestParam(value = "site", required = true) String site,
			@RequestParam(value = "unit_type", required = true) Integer unit_type,
			@RequestParam(value = "unit_name", required = true) String unit_name,
			@RequestParam(value = "stepId", required = true) Integer stepId,
			@RequestParam(value = "command", required = true) String command) throws Exception {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("unit_name", unit_name);
		List<Map<String, Object>> list = backupService.queryAccount(params);
		String username = "";
		String password = "";
		Map<String, Object> data = new HashMap<String,Object>();
		for (Map<String, Object> account : list) {
			username = account.get("username").toString();
			password = Hex.encodeHexString(account.get("password").toString().getBytes());
			data = account;
		}

		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> p1 = new HashMap<String, Object>();
		p1.put("id", unit_type);
		String unitTypeName = backupService.getUnitTypeNameById(p1);

		if ("PGW-DSA".equals(unitTypeName) || "BE-DSA".equals(unitTypeName) || "ROUTING-DSA".equals(unitTypeName)) {
			String commands = "root," + password + ":" + unit_name;

			String sessionId = UUID.randomUUID().toString();

			Map<String, Object> mq_map = new HashMap<String, Object>();
			// mq_map.put("srcQ", ProjectProperties.ccSrcQName);
			mq_map.put("srcQ", "DHLR_BACK_WLEI_debug_l");
			mq_map.put("destQ", ProjectProperties.getDesQName());
			mq_map.put("session", sessionId);
			mq_map.put("neName", unit_name);
			mq_map.put("commandName", "HSS_NETDATA_BACKUP2");// checkItem.getCommand());
			mq_map.put("commandParams", commands);
			mq_map.put("exeTimeoutMinutes", 30);
			mq_map.put("msgCode", 60003);
			
			
			
			
			
			
			
			

			new CCZXJResult().setRes(null);
			messageProducer.sendMessage(mq_map);

			String[] res = null;
			String resultCode = "";
			String msg = "";
			String ret = "无日志信息";
			int timeOut = 90;
			while (true) {
				try {
					System.out.println("waiting...");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (timeOut-- <= 0) {
					logger.warn("Backup Timeout, Stop");
					result.put("error", -1);
					break;
				}

				if ((res = new CCZXJResult().getRes()) != null) {
					new CCZXJResult().setRes(null);
					resultCode = res[0];
					msg = res[1];
					if (!resultCode.equals("0")) {// 执行失败
						result.put("error", -2);
					} else {// 执行成功
						ret = FileOperateUtil.readFile(msg);
					}
					break;
				}
			}
			result.put("ret", ret);
			System.out.println("-----------------------------------------------------------------------");
			Map<String, Object> bk_param = new HashMap<String, Object>();
			bk_param.put("bk_site_name", site);
			bk_param.put("bk_site_type_name", unit_type);
			bk_param.put("bk_node_name", unit_name);
			ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
			bk_param.put("bk_add_who", shiroUser.getUserName());
			bk_param.put("bk_date", new Date());

			bk_param.put("fileName", unit_name + "_af09_dailybkup." + sdf.format(new Date()));
			bk_param.put("fileDisplayName", unit_name + "_af09_dailybkup." + sdf.format(new Date()));
			bk_param.put("bk_file_log", ret);
			backupService.saveBackupDataAsHistory(bk_param);
		} else {

			String commands = "rtp99," + password + ":" + command;
			System.out.println("commands= " + commands);

			String sessionId = UUID.randomUUID().toString();

			Map<String, Object> mq_map = new HashMap<String, Object>();
			// mq_map.put("srcQ", ProjectProperties.ccSrcQName);
			mq_map.put("srcQ", "DHLR_BACK_WLEI_debug_l");
			mq_map.put("destQ", ProjectProperties.getDesQName());
			mq_map.put("session", sessionId);
			mq_map.put("neName", unit_name);
			mq_map.put("commandName", "DHLR_COMMAND");// checkItem.getCommand());
			mq_map.put("commandParams", commands);
			mq_map.put("exeTimeoutMinutes", 30);
			mq_map.put("msgCode", 60003);
			new CCZXJResult().setRes(null);
			messageProducer.sendMessage(mq_map);

			String[] res = null;
			String resultCode = "";
			String msg = "";
			String ret = "无日志信息";
			int timeOut = 90;
			while (true) {
				try {
					System.out.println("waiting...");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (timeOut-- <= 0) {
					logger.warn("Backup Timeout, Stop");
					result.put("error", -1);
					break;
				}

				if ((res = new CCZXJResult().getRes()) != null) {
					new CCZXJResult().setRes(null);
					resultCode = res[0];
					msg = res[1];
					if (!resultCode.equals("0")) {// 执行失败
						result.put("error", -1);
					} else {// 执行成功
						ret = FileOperateUtil.readFile(msg);
						result.put("ret", ret);
					}
					break;
				}
			}
			if (stepId == 4) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String date = sdf.format(new Date());
				String name = unit_name + "_" + date + ".backup";
				SSHCommandExecutor sshExecutor = new SSHCommandExecutor("10.221.30.102", "shh_dhss", "dhss");
				String cmmand = "cp " + msg + " /home/shh_dhss/oneKeyBackup/" + name;
				sshExecutor.execute(cmmand);

				Map<String, Object> bk_param = new HashMap<String, Object>();
				bk_param.put("bk_site_name", site);
				bk_param.put("bk_site_type_name", unit_type);
				bk_param.put("bk_node_name", unit_name);
				ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
				bk_param.put("bk_add_who", shiroUser.getUserName());
				bk_param.put("bk_date", new Date());

				bk_param.put("fileName", name);
				bk_param.put("fileDisplayName", name);
				bk_param.put("bk_file_log", ret);
				backupService.saveBackupDataAsHistory(bk_param);
			}

		}

		return result;
	}

	@RequestMapping(value = "/queryLog", method = RequestMethod.GET)
	public String queryLog(HttpServletRequest request) {
		logger.info("enter	BackupController	execute		queryLog");
		Map<String, String[]> parameterMap = request.getParameterMap();
		String id = parameterMap.get("id")[0];
		if (null == id)
			return "无日志记录";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);

		List<Map<String, Object>> result = backupService.queryLog(params);
		String log = "暂时没有产生日志";
		if (result.size() > 0) {
			log = result.get(0).get("log").toString();
			log = log.replaceAll("<", "&lt;");
			log = log.replaceAll(">", "&gt;");
			return log;
		}
		return log;
	}

	private static final Logger logger = LoggerFactory.getLogger(BackupController.class);

	public BackupService getBackupService() {
		return backupService;
	}

	public void setBackupService(BackupService backupService) {
		this.backupService = backupService;
	}

	@SuppressWarnings("finally")
	private boolean unitBackUp(String site, String unit_type, String unit_name, Integer stepId, String command,
			boolean isLoaclhost,Integer stepSize) {
		System.out.println(
				(++wlei_count) + "---------------" + site + "---" + unit_type + "--" + unit_name + "--" + stepId);
		boolean result = true;
		reMes.clear();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("unitName_EQ", unit_name);
			List<EquipmentUnit> list = backupService.getUnitName_t(params);
			String username = "";
			String password = "";
			EquipmentUnit unitEn = null;
			for (EquipmentUnit account : list) {
				username = account.getLoginName();
				password = Hex.encodeHexString(account.getRootPassword().getBytes());
				unitEn = account;
			}
			String unitTypeName = unit_type /*
											 * backupService.getUnitTypeNameById
											 * (p1)
											 */;

			if ("PGW_DSA".equals(unitTypeName) || "BE_DSA".equals(unitTypeName) || "ROUTING_DSA".equals(unitTypeName)) {
				String commands = username + "," + password + "&" + unit_name + "&";
				if (isLoaclhost)
					commands = commands + ProjectProperties.getLocalhost();
				else
					commands = commands + ProjectProperties.getOtherhost();

				String sessionId = UUID.randomUUID().toString();

				Map<String, Object> mq_map = new HashMap<String, Object>();
				// mq_map.put("srcQ", ProjectProperties.ccSrcQName);
				/*mq_map.put("srcQ", "DHLR_BACK_WLEI_debug_l");
				mq_map.put("destQ", ProjectProperties.getDesQName());
				mq_map.put("session", sessionId);
				mq_map.put("neName", unit_name);
				mq_map.put("commandName", "DHLR_NETDATA_BACKUP2");// checkItem.getCommand());
				mq_map.put("commandParams", commands);
				mq_map.put("exeTimeoutMinutes", 30);
				mq_map.put("msgCode", 60003);*/

				
				mq_map.put("msgCode", 71000);
				mq_map.put("app", "dhss");
				mq_map.put("cacheTime", 5);
				mq_map.put("maxConnNum", 5);
				mq_map.put("type", unitEn.getNeType());
				mq_map.put("srcQ", "DHLR_BACK_WLEI_debug_l");
				mq_map.put("destQ", ProjectProperties.getDesQName());
				mq_map.put("sessionid", sessionId);
				mq_map.put("ne", unitEn.getUnitName());
				mq_map.put("neConnType", "DHSS_" + unitEn.getServerProtocol());
				mq_map.put("password", Encodes.encodeHex(unitEn.getLoginPassword() != null
						? unitEn.getLoginPassword().getBytes() : "".getBytes()));
				mq_map.put("port", unitEn.getServerPort());
				mq_map.put("priority", 5);
				mq_map.put("procotol", unitEn.getServerProtocol());
				mq_map.put("username", unitEn.getLoginName());
				mq_map.put("ip", unitEn.getServerIp());
				
				mq_map.put("content", new Context(unitEn.getServerProtocol() +  "_DHLR_NETDATA_BACKUP2|" + commands, 2, 1));
				
				mq_map.put("hostname", unitEn.getHostname());
				mq_map.put("netFlag", unitEn.getNetFlag());
				mq_map.put("vendor", "nokia");
				mq_map.put("flag", "");
				mq_map.put("retryInterval", 3);
				mq_map.put("retryTimes", 2);
				mq_map.put("needJump", 0);
				mq_map.put("jumpCount", 0);
				mq_map.put("callInterfaceName", "");
				mq_map.put("msg", "");
				mq_map.put("src", "");
				mq_map.put("exculde", 0);
				mq_map.put("taskNum", 71004);
				mq_map.put("unitType", unitEn.getUnitType());
				
				this.setRes(null);
				messageProducer.sendMessage(mq_map);

				String resultCode = "";
				String msg = "";
				String ret = "无日志信息";
				int timeOut = 900;
				while (true) {
					try {
						System.out.println("waiting...");
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (timeOut-- <= 0) {
						logger.warn("Backup Timeout, Stop");
						reMes.put("errormsg", "备份超时");
						result = false;
						break;
					}

					if (this.getRes() != null) {
						resultCode = this.getRes()[0];
						msg = this.getRes()[1];
						System.out.println(resultCode + "-------------------" + msg);
						reMes.put("errormsg", msg);
						if (!resultCode.equals("0")) {// 执行失败
							result = false;
						} else {// 执行成功
							result = true;
							ret = FileOperateUtil.readFile(ProjectProperties.getCOMP_BASE_PATH() + msg);
							reMes.put("errormsg", "执行成功");
						}
						break;
					}
				}
				System.out.println("一键备份日志记录开始");
				OnekeyBackup obk = new OnekeyBackup();
				obk.setNeType(site);
				obk.setUnitName(unit_type);
				obk.setUnitName(unit_name);
				obk.setUserName(shiroUser.getUserName());
				obk.setCreateDate(new Date());
				obk.setFileUuidName(unit_name + "_af09_dailybkup." + sdf.format(new Date()));
				obk.setFileDisplayName(unit_name + "_af09_dailybkup." + sdf.format(new Date()));
				obk.setFileDownFolder(ret);
				onekeyBackupRepository.save(obk);
				System.out.println("一键备份日志记录开始结束");
			} else {

				String commands = username + "," + password + ":" + command;

				String sessionId = UUID.randomUUID().toString();

				Map<String, Object> mq_map = new HashMap<String, Object>();
				// mq_map.put("srcQ", ProjectProperties.ccSrcQName);
				/*mq_map.put("srcQ", "DHLR_BACK_WLEI_debug_l");
				mq_map.put("destQ", ProjectProperties.getDesQName());
				mq_map.put("session", sessionId);
				mq_map.put("neName", unit_name);
				mq_map.put("commandName", "DHLR_COMMAND");// checkItem.getCommand());
				mq_map.put("commandParams", commands);
				mq_map.put("exeTimeoutMinutes", 30);
				mq_map.put("msgCode", 60003);*/
				
				mq_map.put("msgCode", 71000);
				mq_map.put("app", "dhss");
				mq_map.put("cacheTime", 5);
				mq_map.put("maxConnNum", 5);
				mq_map.put("type", unitEn.getNeType());
				mq_map.put("srcQ", "DHLR_BACK_WLEI_debug_l");
				mq_map.put("destQ", ProjectProperties.getDesQName());
				mq_map.put("sessionid", sessionId);
				mq_map.put("ne", unitEn.getUnitName());
				mq_map.put("neConnType", "DHSS_" + unitEn.getServerProtocol());
				mq_map.put("password", Encodes.encodeHex(unitEn.getLoginPassword() != null
						? unitEn.getLoginPassword().getBytes() : "".getBytes()));
				mq_map.put("port", unitEn.getServerPort());
				mq_map.put("priority", 5);
				mq_map.put("procotol", unitEn.getServerProtocol());
				mq_map.put("username", unitEn.getLoginName());
				mq_map.put("ip", unitEn.getServerIp());
				
				mq_map.put("content", new Context(unitEn.getServerProtocol() +  "_DHLR_COMMAND|" + commands, 2, 1));
				
				mq_map.put("hostname", unitEn.getHostname());
				mq_map.put("netFlag", unitEn.getNetFlag());
				mq_map.put("vendor", "nokia");
				mq_map.put("flag", "");
				mq_map.put("retryInterval", 3);
				mq_map.put("retryTimes", 2);
				mq_map.put("needJump", 0);
				mq_map.put("jumpCount", 0);
				mq_map.put("callInterfaceName", "");
				mq_map.put("msg", "");
				mq_map.put("src", "");
				mq_map.put("exculde", 0);
				mq_map.put("taskNum", 71004);
				mq_map.put("unitType", unitEn.getUnitType());
				
				
				this.setRes(null);
				messageProducer.sendMessage(mq_map);
				String resultCode = "";
				String msg = "";
				String ret = "无日志信息";
				int timeOut = 900;
				while (true) {
					try {
						System.out.println("waiting...");
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (timeOut-- <= 0) {
						logger.warn("Backup Timeout, Stop");
						reMes.put("errormsg", "备份超时");
						result = false;
						break;
					}
					if (this.getRes() != null) {
						resultCode = this.getRes()[0];
						msg = this.getRes()[1];
						reMes.put("errormsg", msg);
						System.out.println(resultCode + "-------------------" + msg);
						if (!resultCode.equals("0")) {// 执行失败
							result = false;
						} else {// 执行成功
							reMes.put("errormsg", "执行成功");
							ret = FileOperateUtil.readFile(ProjectProperties.getCOMP_BASE_PATH() + msg);
							result = true;
						}
						break;
					}
				}
				System.out.println(stepId + "-----不是最后一步没有日志可以查看");
				if (stepId == stepSize) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					String date = sdf.format(new Date());
					String name = unit_name + "_" + date + ".backup";
					SSHCommandExecutor sshExecutor = new SSHCommandExecutor(ProjectProperties.getOneKeyLocation(),
							ProjectProperties.getOneKeyUser(), ProjectProperties.getOneKeyPwd());
					String cmmand = "cp " + msg + " " + ProjectProperties.getOneKeyCpLocation() + name;
					sshExecutor.execute(cmmand);
					OnekeyBackup obk = new OnekeyBackup();
					obk.setNeType(site);
					obk.setUnitName(unit_type);
					obk.setUnitName(unit_name);
					ShiroUser shiroUser = this.getShiroUser();
					obk.setUserName(shiroUser.getUserName());
					obk.setCreateDate(new Date());
					obk.setFileUuidName(name);
					obk.setFileDisplayName(name);
					obk.setFileDownFolder(ret);
					onekeyBackupRepository.save(obk);
				}
			}
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		} finally {
			return result;
		}
	}

	@JmsListener(destination = "DHLR_BACK_WLEI_debug_l", containerFactory = "jmsContainerFactory")
	public void reseiveMessageToBackUp(Message message) {
		try {
			this.setRes(null);
			System.out.println("----------------------------accpet-------------------------------------");
			System.out.println("-----------------------------------------------------------------");
			String msgBody = message.getStringProperty("msgBody");
			@SuppressWarnings("unchecked")
			Map<String, String> json = (Map<String, String>) new JsonMapper().fromJson(msgBody, Map.class);
			String resultCode = String.valueOf(json.get("flag"));
			String msg = resultCode.equals("0") ? String.valueOf(json.get("src")) : String.valueOf(json.get("msg")) ;// 网元操作日志返回msg
			String[] res = { resultCode.toString(), msg };
			System.out.println("          " + res[0] + "                          ");
			System.out.println("          " + res[1] + "                          ");
			System.out.println("-----------------------------------------------------------------");
			System.out.println("------------------------------end-----------------------------------");
			this.setRes(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("Received 2222<" + message + ">");
	}

	public ShiroUser getShiroUser() {
		if (this.shiroUser == null) {
			this.shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
			return this.shiroUser;
		} else {
			return this.shiroUser;
		}
	}

	public void setShiroUser(ShiroUser shiroUser) {
		this.shiroUser = shiroUser;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String[] getRes() {
		return res;
	}

	public void setRes(String[] res) {
		this.res = res;
	}

	public Map<String, String> getReMes() {
		return reMes;
	}

	public void setReMes(Map<String, String> reMes) {
		this.reMes = reMes;
	}

	// /****
	// * 得到所有网元树数据
	// *
	// * @param httpServletRequest
	// * @param httpServletResponse
	// * @return List<BackupHistory>
	// * ***/
	// @RequestMapping(value = "/getAllNeTree",method =
	// {RequestMethod.POST,RequestMethod.GET})
	// public List<Map<String, Object>> getAllNeTree(HttpServletRequest
	// httpServletRequest,HttpServletResponse httpServletResponse){
	// List<Map<String, Object>> siteList = new ArrayList<Map<String,
	// Object>>();
	// List<Map<String, Object>> sites = backupService
	// .sites();
	//
	// Map<String, Object> siteMap = null;
	// for (Map<String, Object> site : sites) {
	// siteMap = new HashMap<String, Object>();
	// siteMap.put("id", site.get("site").toString());
	// siteMap.put("text", site.get("site").toString());
	//// siteMap.put("expanded", true);
	// List<Map<String, Object>> neTypeList = new ArrayList<Map<String,
	// Object>>();
	// Map<String, Object> neTypeParm = new HashMap<String, Object>();
	// neTypeParm.put("location", site.get("site").toString());
	// List<Map<String, Object>> netypes = backupService.neTypes(neTypeParm);
	// Map<String, Object> neTypeMap = null;
	// for (Map<String, Object> neType : netypes) {
	// neTypeMap = new HashMap<String, Object>();
	// neTypeMap.put("id", neType.get("id").toString());
	// neTypeMap.put("text", neType.get("name").toString());
	//// neTypeMap.put("expanded", true);
	// List<Map<String, Object>> unitList = new ArrayList<Map<String,
	// Object>>();
	// Map<String, Object> unitParm = new HashMap<String, Object>();
	// unitParm.put("site", site.get("site").toString());
	// unitParm.put("unit_type", neType.get("id").toString());
	// List<Map<String, Object>> units = backupService.units(unitParm);
	// Map<String, Object> unitMap = null;
	// for (Map<String, Object> unit : units) {
	// unitMap = new HashMap<String, Object>();
	// unitMap.put("id", unit.get("id").toString());
	// unitMap.put("text", unit.get("unit").toString());
	// unitMap.put("allVal",
	// site.get("site").toString()+"_"+neType.get("id").toString()+"_"+unit.get("unit").toString()+"_"+unit.get("ne_type").toString());
	// unitList.add(unitMap);
	// }
	// neTypeMap.put("items", unitList);
	// neTypeList.add(neTypeMap);
	// }
	// siteMap.put("items", neTypeList);
	// siteList.add(siteMap);
	// }
	// List<Map<String, Object>> sitejsonList = new ArrayList<Map<String,
	// Object>>();
	// Map<String, Object> sitejsonMap = new HashMap<String, Object>();
	// sitejsonMap.put("id", "allsites");
	// sitejsonMap.put("text", "所有站点");
	// sitejsonMap.put("items",siteList);
	// sitejsonList.add(sitejsonMap);
	// return sitejsonList;
	// }
}
