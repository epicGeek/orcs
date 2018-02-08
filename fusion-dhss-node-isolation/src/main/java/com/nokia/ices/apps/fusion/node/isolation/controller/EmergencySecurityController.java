package com.nokia.ices.apps.fusion.node.isolation.controller;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.jms.Message;
import javax.persistence.criteria.Predicate.BooleanOperator;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.emergency.domian.EmergencySecurityState;
import com.nokia.ices.apps.fusion.emergency.domian.StepConfTable;
import com.nokia.ices.apps.fusion.emergency.domian.StepExecute;
import com.nokia.ices.apps.fusion.emergency.repository.EmergencySecurityStateRepository;
import com.nokia.ices.apps.fusion.emergency.repository.StepExecuteRepository;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentNeType;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentNeRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.jms.producer.DHSSMessageProducer;
import com.nokia.ices.apps.fusion.jms.vo.Context;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.node.isolation.service.EmergencySecurityService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.core.utils.FileOperateUtil;
import com.nokia.ices.core.utils.JsonMapper;

//import com.nsn.ices.apps.dhlr.common.utils.FileOperateUtil;
//import com.nsn.ices.apps.dhlr.exception.Exception;
//import com.nsn.ices.apps.dhlr.secure.ShiroDBRealm.ShiroUser;
//import com.nsn.ices.apps.dhlr.service.jms.ProducerServiceDhss;
//import com.nsn.ices.apps.dhlr.service.jms.impl.CheckResultCacheDhss;

/**
 * 应急保障
 * @author kingroc_zhang
 *
 */
@RepositoryRestController
@RestController
public class EmergencySecurityController {
	
	public static final Logger logger = LoggerFactory.getLogger(EmergencySecurityController.class);
	
	@Autowired
	EmergencySecurityService service;
	
    @Autowired
    DHSSMessageProducer messageProducer;
    
    @SuppressWarnings("rawtypes")
    @Autowired
    private PagedResourcesAssembler pagedResourcesAssembler;
    
    @Autowired
    private EmergencySecurityStateRepository emergencySecurityStateRepository;
    
    @Autowired
    private EquipmentNeRepository equipmentNeRepository;
    
    @Autowired
    private EquipmentUnitRepository equipmentUnitRepository;
    
    @Autowired
    private StepExecuteRepository stepExecuteRepository;
    
    
    public String[] res=null;
    public ShiroUser shiroUser=null;
//	@Autowired
//	CheckResultCacheDhss checkResultCacheDhss;
    
    
    /**
	 * 获取unit的operateState
	 * @param unit
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOperateStateByUnit", method = RequestMethod.GET)
	public EmergencySecurityState getOperateStateByUnit(@RequestParam(value = "unit", required = true) String unit) {
		Map<String, Object> param = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(unit)){
    		param.put("unit.id_EQ", unit);
    	}
    	param.put("operateState_EQ", "2");
//		String  operateState = service.getOperateStateByUnit(param);
		Map<String,SearchFilter> filter = SearchFilter.parse(param);
		Specification<EmergencySecurityState> spec = 
                DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, EmergencySecurityState.class);
		List<EmergencySecurityState>  list = emergencySecurityStateRepository.findAll(spec);
		return list.size() == 0 ? null : list.get(0);
	}
	
    
    @RequestMapping(value = "emergencySecurity/querySecurityState", method = RequestMethod.GET)
	public PagedResources<EmergencySecurityState> querySecurityState(
			@RequestParam(value="startdatetime",required=false) String startTime,
    		@RequestParam(value="enddatetime",required=false) String endTime,
    		@RequestParam(value="operate_state",required=false) String operate_state,
    		@RequestParam(value="ne",required=false) String ne,
    		@RequestParam(value="unit",required=false) String unit,Pageable pageable,
            PersistentEntityResourceAssembler assembler) throws ParseException {
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
    	if(StringUtils.isNotEmpty(ne)){
    		params.put("ne.neName_EQ", ne);
    	}
    	if(StringUtils.isNotEmpty(operate_state)){
    		params.put("operateState_EQ", operate_state);
    	}
    	if(StringUtils.isNotEmpty(startTime)){
    		params.put("operateDate_GE",format.parse(startTime));
    	}
    	
    	if(StringUtils.isNotEmpty(endTime)){
    		params.put("operateDate_LE",format.parse(endTime));
    	}
    	
    	if(StringUtils.isNotEmpty(unit)){
    		params.put("unit.id_EQ",unit);
    	}
		
    	Page<EmergencySecurityState> result = service.querySecurityState(params, pageable);
		return pagedResourcesAssembler.toResource(result,assembler);
		
	}
	
    
    
    /**
	 * 添加隔离执行步骤到列表
	 * @param site
	 * @param ne_type
	 * @param unit
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "emergencySecurity/getStepByType", method = RequestMethod.GET)
	public List<StepExecute> getStepByType(
			@RequestParam(value = "neType", required = true) String neType,
			@RequestParam(value = "ne", required = true) String ne,
			@RequestParam(value = "unit", required = true) String unit		
			) throws Exception{
		EquipmentNe equipmentNe = equipmentNeRepository.findOne(Long.valueOf(ne));
		EquipmentUnit equipmentUnit = equipmentUnitRepository.findOne(Long.valueOf(unit));
		List<StepExecute> emergencySecurityList = new ArrayList<StepExecute>();
		if(unit==null || unit == ""){//网元隔离
			logger.debug("添加网元隔离步骤");

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("stepConfTable.confType_EQ", "4");//配置类型  1:节点隔离、2:节点恢复、3:板卡倒换、4：网元隔离、5:网元恢复
			param.put("stepConfTable.neType_EQ", neType);
			param.put("emergencySecurityState.ne.id_EQ", ne);
			emergencySecurityList = service.queryEmergencySecurity(param);
			
			logger.debug("emergencySecurityList.size()= "+emergencySecurityList.size());
			if(emergencySecurityList.size()==0){
				logger.debug("插入一条记录："+emergencySecurityList.size());
				
				EmergencySecurityState emergencySecurityState = new EmergencySecurityState();
				emergencySecurityState.setOperateDate(new Date());
				emergencySecurityState.setNe(equipmentNe);
				emergencySecurityState.setUnit(equipmentUnit);
				emergencySecurityState.setOperateState(0);
				emergencySecurityState.setOperate(1);
				emergencySecurityState.setOperator(this.getShiroUserName());
				emergencySecurityStateRepository.save(emergencySecurityState);
				/*Map<String, Object> param1 = new HashMap<String, Object>();
				
				param1.put("operate_date", new Date());
				param1.put("ne_id", 1);
				param1.put("ne_name", equipmentNe.getNeName());
				param1.put("operate_state", 0);
				param1.put("operate", 1);//网元操作
				param1.put("operator",this.getShiroUserName());
				service.saveEmergencySecurity(param1);*/
				
				Map<String, Object> param2 = new HashMap<String, Object>();
				param2.put("ne.id_EQ", "1");
				param2.put("operate_EQ", "1");
				EmergencySecurityState ess = service.getEmergencySecurityByUnit(param2);
				
				
				Map<String, Object> param3 = new HashMap<String, Object>();
				param3.put("neType_EQ", neType);
				param3.put("confType_EQ", "4");//网元隔离
				List<StepConfTable> stepList = service.getStepByType(param3);
				
				for (StepConfTable map : stepList) {
					
					StepExecute se = new StepExecute();
					se.setExecuteState(0);
					se.setEmergencySecurityState(ess);
					se.setStepConfTable(map);
					stepExecuteRepository.save(se);
					/*Map<String, Object> params = new HashMap<String, Object>();
					params.put("execute_state", 0);
					params.put("emergency_security_state", ess.getId());	
					
					for (Map.Entry<String, Object> entry : map.entrySet()) {  
						if(entry.getKey().equals("id")){
			            	params.put("step_conf_table", entry.getValue());
			            }   					
			        }		
					service.saveStepExecute(params);*/
				}
				
				emergencySecurityList = service.queryEmergencySecurity(param);
			}
		}else{//节点操作
			
			/*String neName = service.getNeNameById(ne_id);
			String unitName = service.getUnitNameById(unit);*/
			
			Map<String, Object> param4 = new HashMap<String, Object>();
			param4.put("stepConfTable.confType_EQ", "1");//配置类型  1:节点隔离、2:节点恢复、3:板卡倒换、4：网元隔离、5:网元恢复
			param4.put("stepConfTable.neType_EQ", neType);
			param4.put("emergencySecurityState.unit.id_EQ", unit);
			emergencySecurityList = service.queryEmergencySecurity(param4);
			
			logger.debug("emergencySecurityList.size()= "+emergencySecurityList.size());
			if(emergencySecurityList.size()==0){
				logger.debug("插入一条记录："+emergencySecurityList.size());
				EmergencySecurityState emergencySecurityState = new EmergencySecurityState();
				emergencySecurityState.setOperateDate(new Date());
				emergencySecurityState.setNe(equipmentNe);
				emergencySecurityState.setUnit(equipmentUnit);
				emergencySecurityState.setOperateState(0);
				emergencySecurityState.setOperate(2);
				emergencySecurityState.setOperator(this.getShiroUserName());
				emergencySecurityStateRepository.save(emergencySecurityState);
				/*Map<String, Object> param1 = new HashMap<String, Object>();
				
				param1.put("operate_date", new Date());
				param1.put("ne_id", ne);
				param1.put("ne_name", equipmentNe.getNeName());
				param1.put("unit_id", unit);
				param1.put("unit_name", equipmentUnit.getUnitName());
				param1.put("operate_state", 0);
				param1.put("operate", 2);//节点操作
				param1.put("operator",this.getShiroUserName());
				service.saveEmergencySecurity(param1);*/
				
				Map<String, Object> p2 = new HashMap<String, Object>();
				p2.put("unit.id_EQ", unit);
				p2.put("operate_EQ", "2");
				emergencySecurityState = service.getEmergencySecurityByUnit(p2);
				/*String essId = service.getEmergencySecurityByUnit(p2);*/
							
				Map<String, Object> param5 = new HashMap<String, Object>();
				param5.put("neType_EQ", neType);
				param5.put("confType_EQ", "1");//节点隔离
				List<StepConfTable> stepList = service.getStepByType(param5);
				
					for (StepConfTable map : stepList) {
						
						StepExecute se = new StepExecute();
						se.setExecuteState(0);
						se.setEmergencySecurityState(emergencySecurityState);
						se.setStepConfTable(map);
						stepExecuteRepository.save(se);
					}
				emergencySecurityList = service.queryEmergencySecurity(param4);
			}
		
		}
			
		return emergencySecurityList;
	}
    
    
	@RequestMapping(value = "/queryNeType", method = RequestMethod.GET)
	public EquipmentNeType[] queryNeType() {
		return EquipmentNeType.values();
	}
	
	@RequestMapping(value = "/queryUnitType", method = RequestMethod.GET)
	public EquipmentUnitType[] queryUnitType(){
		return EquipmentUnitType.values();
	}
	/*@RequestMapping(value = "/getAllNe", method = RequestMethod.GET)
	public Map<String, Object> getAllNe()throws Exception {

		List<Map<String, Object>> list = service.getAllNe();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", list);
		result.put("total", list.size());
		return result;
	}*/
	
	/*@RequestMapping(value = "/queryUnit", method = RequestMethod.GET)
	public List<Map<String, Object>> queryUnit(
			@RequestParam(value = "ne_type", required = true) Integer ne_type,
			@RequestParam(value = "location", required = true) String location
			) throws Exception{
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("location", location);
		params.put("ne_type", ne_type);
		
		List<Map<String, Object>> list = service.queryUnit(params);
		
		return list;
	}*/
	
	@RequestMapping(value = "/queryUnitByNe", method = RequestMethod.GET)
	public List<Map<String, Object>> queryUnitByNe(
			@RequestParam(value = "neId", required = true) Integer neId
			) throws Exception{
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("neId", neId);
		
		List<Map<String, Object>> list = service.queryUnitByNe(params);
		
		return list;
	}
	
	/**
	 * 获取ne的operateState
	 * @param unit
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOperateStateByNe", method = RequestMethod.GET)
	public String getOperateStateByNe(
			@RequestParam(value = "neId", required = true) Integer neId
			) throws Exception{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("neId", neId);
		String  operateState = service.getOperateStateByNe(param);
		
		return operateState;
	}
	
	
	
	
	
	/**
	 * 节点隔离
	 * @return
	 */
	@RequestMapping(value = "/isolation", method = RequestMethod.GET)
    public Map<String, Object> isolation(
    		@RequestParam(value = "neType", required = true) String neType,
    		@RequestParam(value = "unit", required = true) Integer unit,
    		@RequestParam(value = "unitName", required = true) String unitName,
    		@RequestParam(value = "stepId", required = true) Integer stepId,
    		@RequestParam(value = "command", required = true) String command,
    		@RequestParam(value = "id", required = true) Long id
    		) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(unit==0){//网元隔离
			if("HSSFE".equals(neType)){
				
			}
			if("NTHLRFE".equals(neType)){
						
			}
			if("ONE-NDS".equals(neType)){
				
			}
			if("SGW".equals(neType)){
				
			}
		}else{//节点隔离
			if("HSSFE".equals(neType)){
				executeUnitIsolation(unit, unitName, stepId, command, result,id);
			}
			if("NTHLRFE".equals(neType)){
				executeUnitIsolation(unit, unitName, stepId, command, result,id);		
			}
			if("ONE-NDS".equals(neType)){
				
			}
			if("SGW".equals(neType)){
				
			}
		}

		
		return result;
	}
    
	/**
	 * 添加节点恢复执行步骤到列表
	 * @param site
	 * @param ne_type
	 * @param unit
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "emergencySecurity/getStepRecovery", method = RequestMethod.GET)
	public List<StepExecute> getStepRecovery(
			@RequestParam(value = "site", required = true) String site,
			@RequestParam(value = "neType", required = true) String neType,
			@RequestParam(value = "unit", required = true) Integer unit
			) throws Exception{
		
		List<StepExecute> emergencySecurityList = new ArrayList<StepExecute>();
		if(unit==0){//网元恢复
			/*String neName = service.getNeNameById(ne_id);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("conf_type", 5);//配置类型  1:节点隔离、2:节点恢复、3:板卡倒换、4：网元隔离、5:网元恢复
			param.put("ne_type", neType);
//			param.put("unit_id", unit);
			param.put("ne_id", ne_id);
			emergencySecurityList = service.queryEmergencySecurity(param);
			
			if(emergencySecurityList.size()==0){
				
				Map<String, Object> param2 = new HashMap<String, Object>();
//				param2.put("unit_id", unit);
				param2.put("ne_id", ne_id);
				EmergencySecurityState ess = service.getEmergencySecurityByUnit(param2);
				
				
				Map<String, Object> param3 = new HashMap<String, Object>();
				param3.put("ne_type", neType);
				param3.put("conf_type", 5);//节点恢复
				List<StepConfTable> stepList = service.getStepByType(param3);
				
				for (StepConfTable map : stepList) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("execute_state", 0);
					params.put("emergency_security_state", ess.getId());	
					
					for (Map.Entry<String, Object> entry : map.entrySet()) {  
						if(entry.getKey().equals("id")){
			            	params.put("step_conf_table", entry.getValue());
			            }   
						
			        }		
					service.saveStepExecute(params);
				}
				
				emergencySecurityList = service.queryEmergencySecurity(param);
			}*/
		}else{
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("stepConfTable.confType_EQ", "2");//配置类型  1:节点隔离、2:节点恢复、3:板卡倒换、4：网元隔离、5:网元恢复
			param.put("stepConfTable.neType_EQ", neType);
			param.put("emergencySecurityState.unit.id_EQ", String.valueOf(unit));
			emergencySecurityList = service.queryEmergencySecurity(param);
			
			if(emergencySecurityList.size()==0){
				
				Map<String, Object> param2 = new HashMap<String, Object>();
				param2.put("unit.id_EQ", String.valueOf(unit));
				EmergencySecurityState ess = service.getEmergencySecurityByUnit(param2);
				
				
				Map<String, Object> param3 = new HashMap<String, Object>();
				param3.put("neType_EQ", neType);
				param3.put("confType_EQ", String.valueOf(2));//节点恢复
				List<StepConfTable> stepList = service.getStepByType(param3);
				
				for (StepConfTable map : stepList) {
					StepExecute se = new StepExecute();
					se.setExecuteState(0);
					se.setEmergencySecurityState(ess);
					se.setStepConfTable(map);
					stepExecuteRepository.save(se);
				}
				emergencySecurityList = service.queryEmergencySecurity(param);
			}
		}
		return emergencySecurityList;
	}
    
	/**
	 * 执行恢复
	 * @return
	 */
	@RequestMapping(value = "emergencySecurity/executeRecovery", method = RequestMethod.GET)
    public Map<String, Object> executeRecovery(
    		@RequestParam(value = "neTypeName", required = true) String neTypeName,
    		@RequestParam(value = "unit", required = true) Integer unit,
    		@RequestParam(value = "unitName", required = true) String unitName,
    		@RequestParam(value = "stepId", required = true) Integer stepId,
    		@RequestParam(value = "command", required = true) String command
    		) {	
		Map<String, Object> result = new HashMap<String, Object>();
		if(unit==0){//网元隔离
			if("HSSFE".equals(neTypeName)){
				if(stepId==1){	
				}
				if(stepId==2){	
				}
				if(stepId==3){	
				}
				if(stepId==4){	
				}
			}
			if("NTHLRFE".equals(neTypeName)){
						
			}
			if("ONE-NDS".equals(neTypeName)){
				
			}
			if("SGW".equals(neTypeName)){
				
			}
		}else{//节点隔离
			if("HSSFE".equals(neTypeName)){
				executeUnitRecovery(unit, unitName, stepId, command, result);
			}
			if("NTHLRFE".equals(neTypeName)){
				executeUnitRecovery(unit, unitName, stepId, command, result);	
			}
			if("ONE-NDS".equals(neTypeName)){
				
			}
			if("SGW".equals(neTypeName)){
				
			}
		}	
		
		return result;
	}
    
    
    
    public static byte[] readStream(InputStream inStream) throws Exception {  
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];  
        int len = -1;  
        while ((len = inStream.read(buffer)) != -1) {  
            outSteam.write(buffer, 0, len);  
        }  
        outSteam.close();  
        inStream.close();  
        return outSteam.toByteArray();  
    }
    private void executeUnitIsolation(Integer unit, String unitName, Integer stepId,
			String command, Map<String, Object> result,Long id) {
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("unitName_EQ", unitName);
		List<EquipmentUnit> list = service.queryAccount(params);
		String password = "";
		EquipmentUnit unitEn = null;
		for (EquipmentUnit unita : list) {
			password = Hex.encodeHexString(unita.getRootPassword().getBytes());
			unitEn = unita;
		}
		if(unitEn == null ){
			return ;
		}
		//
		/*String commands = "root,"+password+":"+command;*/
		String sessionId = UUID.randomUUID().toString();
		
		Map<String, Object> mq_map = new HashMap<String, Object>();
//		mq_map.put("srcQ", ProjectProperties.ccSrcQName);
		/*mq_map.put("srcQ", "DHLR_BACK_WLEI1");
		mq_map.put("destQ", ProjectProperties.getDesQName());
		mq_map.put("session", sessionId);
		mq_map.put("neName", unitName);
		mq_map.put("commandName","DHLR_COMMAND");//checkItem.getCommand());
		mq_map.put("commandParams",commands);
		mq_map.put("exeTimeoutMinutes", 30);
		mq_map.put("msgCode", 60003);*/
		
		
		mq_map.put("app", "dhss");
		mq_map.put("cacheTime", 5);
		mq_map.put("maxConnNum", 5);
		mq_map.put("type",unitEn.getNeType());
		mq_map.put("srcQ", "DHLR_BACK_WLEI1");
        mq_map.put("destQ", ProjectProperties.getDesQName());
        mq_map.put("sessionid", sessionId);
        mq_map.put("ne",  unitEn.getUnitName());
        mq_map.put("neConnType",  "DHSS_"+unitEn.getServerProtocol());
        mq_map.put("password",  password);
        mq_map.put("port",  unitEn.getServerPort());
        mq_map.put("priority",  5);
        mq_map.put("procotol",  unitEn.getServerProtocol());
        mq_map.put("username",  unitEn.getLoginName());
        mq_map.put("ip",  unitEn.getServerIp());
        mq_map.put("content",new Context(command,1,1));
        mq_map.put("hostname", unitEn.getHostname());
        mq_map.put("netFlag", unitEn.getNetFlag());
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
        mq_map.put("unitType", unitEn.getUnitType());
		
		
		
		this.setRes(null);
		messageProducer.sendMessage(mq_map);
		String[] res = null;
		String resultCode="";
		String msg="";
		String ret="无日志信息";
		int timeOut =20;
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
			    }

			if (this.getRes() != null) {
				resultCode = this.getRes()[0];
				msg =  this.getRes()[1];
				if (!resultCode.equals("0")) {// 执行失败
					result.put("error", -1);
				} else {// 执行成功aaaaaaaaa
					ret = FileOperateUtil.readFile(msg);
				}
				break;
			}	
		}
		//将返回的信息保存进数据库		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("unit.id_EQ", String.valueOf(unit));
		List<EmergencySecurityState> emergencySecurityList = service.querySecurityStateList(param);
		for (EmergencySecurityState emergencySecurityState : emergencySecurityList) {
			emergencySecurityState.setReturnInfo(ret);
		}
		emergencySecurityStateRepository.save(emergencySecurityList);
		
		//根据节点unit和步骤id 更新的执行步骤状态
		StepExecute se = stepExecuteRepository.findOne(id);
		se.setExecuteState(1);
		stepExecuteRepository.save(se);
		
					
		Map<String, Object> param1 = new HashMap<String, Object>();
		param1.put("stepConfTable.confType_EQ", "1");
		param1.put("unit.id_EQ", String.valueOf(unit));
		List<StepExecute> emergency = service.queryEmergencySecurity(param1);
		//判断执行步骤是否已全部完成
		int count =0;
		for (StepExecute map : emergency) {
				if(map.getExecuteState() == 1){
					count++;
				}
		}
		logger.debug("emergencySecurityList.size()= "+emergency.size()+" count="+count);
		//判断步骤是否执行完
		if(count==emergency.size()){//更新emergency_security_state表operate_state=1
			for (EmergencySecurityState emergencySecurityState : emergencySecurityList) {
				emergencySecurityState.setOperateState(2);
				emergencySecurityState.setOperateDate(new Date());
				emergencySecurityState.setOperator(this.getShiroUserName());
			}
			emergencySecurityStateRepository.save(emergencySecurityList);
			
			param1.put("stepConfTable.confType_EQ", "2");
			List<StepExecute> emergency1 = service.queryEmergencySecurity(param1);
			for (StepExecute stepExecute : emergency1) {
				stepExecute.setExecuteState(0);
			}
			stepExecuteRepository.save(emergency1);
			
		}		
		result.put("list", emergency);
		result.put("ret", ret);
	}
    //
    private void executeUnitRecovery(Integer unit, String unitName, Integer stepId,
			String command, Map<String, Object> result) {
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("unitName_EQ", unitName);
		List<EquipmentUnit> list = service.queryAccount(params);
		String password = "";
		EquipmentUnit unitEn = null;
		for (EquipmentUnit account : list) {
			password = Hex.encodeHexString(account.getRootPassword().getBytes());
			unitEn = account;
		}
		//
//		String commands = "root,"+password+":"+command;
		String sessionId = UUID.randomUUID().toString();
		
		Map<String, Object> mq_map = new HashMap<String, Object>();
//		mq_map.put("srcQ", ProjectProperties.ccSrcQName);
		/*mq_map.put("srcQ", "DHLR_BACK_WLEI1");
		mq_map.put("destQ", ProjectProperties.getDesQName());
		mq_map.put("session", sessionId);
		mq_map.put("neName", unitName);
		mq_map.put("commandName","DHLR_COMMAND");//checkItem.getCommand());
		mq_map.put("commandParams",commands);
		mq_map.put("exeTimeoutMinutes", 30);
		mq_map.put("msgCode", 60003);*/
		
		
		
		mq_map.put("app", "dhss");
		mq_map.put("cacheTime", 5);
		mq_map.put("maxConnNum", 5);
		mq_map.put("type",unitEn.getNeType());
		mq_map.put("srcQ", "DHLR_BACK_WLEI1");
        mq_map.put("destQ", ProjectProperties.getDesQName());
        mq_map.put("sessionid", sessionId);
        mq_map.put("ne",  unitEn.getUnitName());
        mq_map.put("neConnType",  "DHSS_"+unitEn.getServerProtocol());
        mq_map.put("password",  password);
        mq_map.put("port",  unitEn.getServerPort());
        mq_map.put("priority",  5);
        mq_map.put("procotol",  unitEn.getServerProtocol());
        mq_map.put("username",  unitEn.getLoginName());
        mq_map.put("ip",  unitEn.getServerIp());
        mq_map.put("content",new Context(command,1,1));
        mq_map.put("hostname", unitEn.getHostname());
        mq_map.put("netFlag", unitEn.getNetFlag());
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
        mq_map.put("unitType", unitEn.getUnitType());
		
		
		
		this.setRes(null);
		messageProducer.sendMessage(mq_map);
//		producerServiceDhss.sendMessage60003(sessionId, unitName,"DHLR_COMMAND_V2", commands);
		String[] res = null;
		String resultCode="";
		String msg="";
		String ret="无日志信息";
		int timeOut =20;
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
			    }

			if (this.getRes() != null) {
				resultCode = this.getRes()[0];
				msg =  this.getRes()[1];
				if (!resultCode.equals("0")) {// 执行失败
					result.put("error", -1);
				} else {// 执行成功
					ret = FileOperateUtil.readFile(msg);
				}
				break;
			}	
		}
		//将返回的信息保存进数据库
		Map<String, Object> returnInfo_params = new HashMap<String, Object>();
		returnInfo_params.put("unit.id_EQ", String.valueOf(unit));
		EmergencySecurityState s = service.getEmergencySecurityByUnit(returnInfo_params);
		s.setReturnInfo(ret);	
		emergencySecurityStateRepository.save(s);
		
				/*service.updateReturnInfo(returnInfo_params);*/
				//根据节点unit和步骤id 更新的执行步骤状态
				
				Map<String, Object> update_params = new HashMap<String, Object>();
				update_params.put("emergencySecurityState.unit.id", String.valueOf(unit));
				update_params.put("stepConfTable.stepSeq", String.valueOf(stepId));
//				update_params.put("execute_state", 1);//更新状态为已执行
				update_params.put("stepConfTable.confType", String.valueOf(2));//节点恢复
//				service.updateStepExecute(update_params);
				List<StepExecute> ses = service.queryEmergencySecurity(returnInfo_params);
				for (StepExecute stepExecute : ses) {
					stepExecute.setExecuteState(1);
				}
				stepExecuteRepository.save(ses);
				
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("emergencySecurityState.unit.id_EQ", String.valueOf(unit));
				param.put("stepConfTable.confType_EQ", String.valueOf(2));
				List<StepExecute> emergencySecurityList = service.queryEmergencySecurity(param);
				//判断执行步骤是否已全部完成
				int count =0;
				for (StepExecute map : emergencySecurityList) {
					if(map.getExecuteState() == 1){
						count++;
					}
				}
				//判断步骤是否执行完
				if(count==emergencySecurityList.size()){//更新emergency_security_state表operate_state=2
					/*Map<String, Object> p1 = new HashMap<String, Object>();
					p1.put("unit_id", unit);
					p1.put("operate_state", 1);
					p1.put("operate_date", new Date());sss
					p1.put("operator", this.getShiroUserName());
					service.updateEmergencySecurity(p1);*/
					s.setOperateState(1);
					s.setOperateDate(new Date());
					s.setOperator(this.getShiroUserName());
					emergencySecurityStateRepository.save(s);
					
					
					param.put("stepConfTable.confType_EQ", "1");
					List<StepExecute> emergency1 = service.queryEmergencySecurity(param);
					for (StepExecute stepExecute : emergency1) {
						stepExecute.setExecuteState(0);
					}
					stepExecuteRepository.save(emergency1);
					
					//根据节点unit和步骤id 更新的执行步骤状态
					/*Map<String, Object> p2 = new HashMap<String, Object>();
					p2.put("unit", unit);
					p2.put("execute_state", 0);
					p2.put("conf_type", 1);aaa
					service.updateStepExecute(p2);*/
					
				}
		result.put("list", emergencySecurityList);
		result.put("ret", ret);
	}
	@JmsListener(destination = "DHLR_BACK_WLEI1", containerFactory = "jmsContainerFactory")
	public void reseiveMessageToBackUp(Message message){
	 try {
		this.setRes(null); 
		System.out.println("----------------------------accpet-------------------------------------");
		System.out.println("-----------------------------------------------------------------");
		String   msgBody = message.getStringProperty("msgBody");
		@SuppressWarnings("unchecked")
		Map<String, String> json = (Map<String, String>) new JsonMapper().fromJson(msgBody, Map.class);
		String resultCode = String.valueOf(json.get("falg"));
		
		String msg = resultCode.equals("0") ?  String.valueOf(json.get("src")) : String.valueOf(json.get("msg"));//网元操作日志返回msg
		String[] res={resultCode.toString(),msg};
		System.out.println("          "+res[0]+"                          ");
		System.out.println("          "+res[1]+"                          ");
		System.out.println("-----------------------------------------------------------------");
		System.out.println("------------------------------end-----------------------------------");
		this.setRes(res);
	} catch (Exception e) {
		e.printStackTrace();
	}
		logger.debug("Received 2222<" + message + ">"); 
	}
	
	public String getShiroUserName() {
		if(this.shiroUser==null){
			this.shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal(); 
			return this.shiroUser.getUserName();
		}else{
			return this.shiroUser.getUserName();
		}
	}
	
	

    /***
     * get  set
     * @return
     */
	public ShiroUser getShiroUser() {
		if(this.shiroUser==null){
			 this.shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal(); 
			 return this.shiroUser;
		}else{
			return this.shiroUser;
		}
	}

	public void setShiroUser(ShiroUser shiroUser) {
		this.shiroUser = shiroUser;
	}
	
	public DHSSMessageProducer getMessageProducer() {
		return messageProducer;
	}

	public void setMessageProducer(DHSSMessageProducer messageProducer) {
		this.messageProducer = messageProducer;
	}

	public String[] getRes() {
		return res;
	}

	public void setRes(String[] res) {
		this.res = res;
	}


    
    
}
