package com.nokia.ices.apps.fusion.maintain.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.criteria.Predicate.BooleanOperator;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.command.domain.types.CommandCategory;
import com.nokia.ices.apps.fusion.command.repository.CommandCheckItemRepository;
import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.jms.producer.DHSSMessageProducer;
import com.nokia.ices.apps.fusion.jms.vo.Context;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.jpa.SearchFilter.Operator;
import com.nokia.ices.apps.fusion.maintain.domain.MaintainOperation;
import com.nokia.ices.apps.fusion.maintain.domain.MaintainResult;
import com.nokia.ices.apps.fusion.maintain.repository.MaintainOperationRepository;
import com.nokia.ices.apps.fusion.maintain.repository.MaintainResultRepository;
import com.nokia.ices.apps.fusion.maintain.service.MaintainService;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.system.repository.types.SystemOperationLogOpType;
import com.nokia.ices.apps.fusion.system.service.SystemService;
import com.nokia.ices.core.utils.Encodes;
import com.nokia.ices.core.utils.JsonMapper;

@Service("maintainService")
public class MaintainServiceImpl implements MaintainService {

	private static final Logger logger = LoggerFactory.getLogger(MaintainServiceImpl.class);

	// private static final String srcSecurityQueueName = "DHSS_SECURITY";
	@Autowired
	private SystemService systemService;
	@Autowired
	private MaintainResultRepository maintainResultRepository;
	@Autowired
	private MaintainOperationRepository maintainOperationRepository;
	@Autowired
	private CommandCheckItemRepository commandCheckItemRepository;
	@Autowired
	private EquipmentUnitRepository equipmentUnitRepository;

	@Autowired
	private DHSSMessageProducer messageProducer;

	@Override
	public Page<MaintainOperation> findMaintainOperationPageBySearch(String checkName,String createBy, String categoryName,
			Pageable pageable) {
		List<SearchFilter> searchFilter = new ArrayList<SearchFilter>();

		if (StringUtils.isNotEmpty(createBy) && !"admin".equals(createBy)) {
			searchFilter.add(new SearchFilter("createBy", Operator.EQ, createBy));
		}
		if (StringUtils.isNotEmpty(categoryName)) {
			CommandCategory category = (CommandCategory) Enum.valueOf(CommandCategory.class, categoryName);
			searchFilter.add(new SearchFilter("commandCategory", Operator.EQ, category));
		}
		if (StringUtils.isNotEmpty(checkName)) {
			searchFilter.add(new SearchFilter("checkName", Operator.LIKE, checkName));
		}
		Specification<MaintainOperation> specCommandCheckItem = DynamicSpecifications.bySearchFilter(searchFilter,
				BooleanOperator.AND, MaintainOperation.class);
		return maintainOperationRepository.findAll(specCommandCheckItem, pageable);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendCommandViaJcomp(ModelMap paramMap, String userName) {
		List<Map> list = (List<Map>) paramMap.get("data");
		String category_cn = paramMap.get("category_cn").toString();
		String category_code = paramMap.get("category_code").toString();


		MaintainOperation operation = new MaintainOperation();

		operation.setCreateBy(userName);

		operation.setCommandCategory(CommandCategory.valueOf(category_code));
		operation.setIsDone(false);
		operation.setRequestTime(new Date());

		Set<Long> unitList = new HashSet<Long>();
		boolean flag = false;
		for (Map map : list) {
			Long unitId = Long.parseLong(map.get("unitId").toString());
			unitList.add(unitId);
			Long itemId = Long.parseLong(map.get("itemId").toString());
			String account = map.get("account").toString();
			String command = "";
			String defaultParamValues = "";
			if (map.get("command") != null) {
				command = map.get("command").toString();
			}
			if (map.get("defaultParamValues") != null) {
				defaultParamValues = map.get("defaultParamValues").toString();
			}

			EquipmentUnit equipmentUnit = equipmentUnitRepository.findOne(unitId);
			CommandCheckItem commandCheckItem = commandCheckItemRepository.findOne(itemId);
			if (flag == false) {
				flag = true;
				operation.setCheckName(commandCheckItem.getName());
				operation = maintainOperationRepository.save(operation);
			}
			MaintainResult checkResult = new MaintainResult();
			String uuId = UUID.randomUUID().toString().replaceAll("-", "");
			checkResult.setUuId(uuId);
			checkResult.setNe(equipmentUnit.getNe());
			checkResult.setOperation(operation);
			checkResult.setUnit(equipmentUnit);
			checkResult.setCommandCheckItem(commandCheckItem);
			checkResult.setRequestTime(new Date());
			checkResult = maintainResultRepository.save(checkResult);

			String[] paramsArray = defaultParamValues.split(",");
			for (int i = 0; i < paramsArray.length; i++) {
				// 按照顺序替换动态值
				if (command.indexOf("$" + (i + 1)) != -1) {
					String value = paramsArray[i].toString();
					command = command.replace("$" + (i + 1), value);

				}
			}
			logger.debug("执行命令={ " + command + "} ");
			Map<String, Object> mq_map = new HashMap<String, Object>();
			/*
			 * mq_map.put("srcQ", MAINTAIN_QNAME); mq_map.put("destQ",
			 * ProjectProperties.getDesQName()); mq_map.put("session", uuId);
			 * mq_map.put("neName", equipmentUnit.getUnitName());
			 * mq_map.put("commandName", "DHLR_COMMAND");//
			 * checkItem.getCommand()); mq_map.put("exeTimeoutMinutes", 30);
			 * mq_map.put("commandParams", account + "," +
			 * Encodes.encodeHex(equipmentUnit.getRootPassword().getBytes()) +
			 * ":" + command); mq_map.put("msgCode", 60003);
			 */

			mq_map.put("msgCode", 71000);
			mq_map.put("app", "dhss");
			mq_map.put("cacheTime", 5);
			mq_map.put("maxConnNum", 5);
			mq_map.put("type", equipmentUnit.getNeType());
			mq_map.put("srcQ", MAINTAIN_QNAME);
			mq_map.put("destQ", ProjectProperties.getDesQName());
			mq_map.put("sessionid", uuId);
			mq_map.put("ne", equipmentUnit.getUnitName());
			mq_map.put("neConnType", "DHSS_" + equipmentUnit.getServerProtocol());
			mq_map.put("password", Encodes.encodeHex(equipmentUnit.getLoginPassword() != null
					? equipmentUnit.getLoginPassword().getBytes() : "".getBytes()));
			mq_map.put("port", equipmentUnit.getServerPort());
			mq_map.put("priority", 5);
			mq_map.put("procotol", equipmentUnit.getServerProtocol());
			mq_map.put("username", equipmentUnit.getLoginName());
			mq_map.put("ip", equipmentUnit.getServerIp());
			mq_map.put("content", new Context(
					equipmentUnit.getServerProtocol() + "_DHLR_COMMAND|" + account/*equipmentUnit.getLoginName()*/ + ","
							+ Encodes.encodeHex(equipmentUnit.getRootPassword() != null
									? equipmentUnit.getRootPassword().getBytes() : "".getBytes())
							+ ":" + command,
					2, 1));
			mq_map.put("hostname", equipmentUnit.getHostname());
			mq_map.put("netFlag", equipmentUnit.getNetFlag());
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
			mq_map.put("taskNum", 71002);
			mq_map.put("unitType", equipmentUnit.getUnitType());

			messageProducer.sendMessage(mq_map);
			logger.debug("Send command informcation to MQ:" + new JsonMapper().toJson(mq_map));
		}

		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		systemService.saveSystemOperationLog(shiroUser, category_cn, "执行 DHLR_COMMAND : 在 " + unitList.size() + " 个单元上",
				SystemOperationLogOpType.Other);
	}


        /*operation.setCreateBy(userName);
        
        operation.setCommandCategory(CommandCategory.valueOf(category_code));
        operation.setIsDone(false);
        operation.setRequestTime(new Date());
       
        Set<Long> unitList = new HashSet<Long>();
        boolean flag = false;
        for(Map map : list){
    	   Long unitId = Long.parseLong(map.get("unitId").toString());
    	   unitList.add(unitId);
    	   Long itemId = Long.parseLong(map.get("itemId").toString());
    	   String account = map.get("account").toString();
    	   String command = ""; 
    	   String defaultParamValues = "";
    	   if(map.get("command")!= null){
    		    command = map.get("command").toString();
    	   }
    	   if(map.get("defaultParamValues")!= null){
    		    defaultParamValues = map.get("defaultParamValues").toString();
    	   }
    	   
    	   EquipmentUnit  equipmentUnit = equipmentUnitRepository.findOne(unitId);
    	   CommandCheckItem commandCheckItem = commandCheckItemRepository.findOne(itemId);
    	   if(flag==false){
    		   flag = true;
    		   operation.setCheckName(commandCheckItem.getName());
    	       operation = maintainOperationRepository.save(operation);
    	   }
           MaintainResult checkResult = new MaintainResult();
           String uuId = UUID.randomUUID().toString().replaceAll("-", "");
           checkResult.setUuId(uuId);
           checkResult.setNe(equipmentUnit.getNe());
           checkResult.setOperation(operation);
           checkResult.setUnit(equipmentUnit);
           checkResult.setCommandCheckItem(commandCheckItem);
           checkResult.setRequestTime(new Date());
           checkResult = maintainResultRepository.save(checkResult);
                    
           String[] paramsArray =  defaultParamValues.split(",");
           for(int i=0;i<paramsArray.length;i++){
             //按照顺序替换动态值
             if(command.indexOf("$"+(i + 1))!=-1){
                 String value = paramsArray[i].toString();
                 command = command.replace("$" + (i + 1),value);
                 
             }
           }          
           logger.debug("执行命令={ "+command+"} ");
           Map<String, Object> mq_map = new HashMap<String, Object>();
           mq_map.put("srcQ",MAINTAIN_QNAME);
           mq_map.put("destQ", ProjectProperties.getDesQName());
           mq_map.put("session", uuId);
           mq_map.put("neName", equipmentUnit.getUnitName());
           String com = equipmentUnit.getUnitType().equals("AHUB") ? "AHUB" : "COMMAND";
           mq_map.put("commandName","DHLR_" + com );//checkItem.getCommand());
           mq_map.put("exeTimeoutMinutes", 30);
           mq_map.put("commandParams",account+","+Encodes.encodeHex(equipmentUnit.getRootPassword().getBytes())+":"+command);
           mq_map.put("msgCode", 60003);
           
           messageProducer.sendMessage(mq_map);
           logger.debug("Send command informcation to MQ:" + new JsonMapper().toJson(mq_map));
       }
       
       ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
       systemService.saveSystemOperationLog(shiroUser, category_cn, "执行 DHLR_COMMAND : 在 "+unitList.size()+" 个单元上", SystemOperationLogOpType.Other);
   }
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendChangePasswordCommandViaJcomp(ModelMap paramMap, String userName) {
		List<Map> unitMaplist = (List<Map>) paramMap.get("unitList");
		String category = paramMap.get("category").toString();
		String category_cn = paramMap.get("category_cn").toString();
		String account = paramMap.get("account").toString();
		String newPassword = paramMap.get("newPassword").toString();
		CommandCategory commandCategory = CommandCategory.valueOf(category);

		MaintainOperation operation = new MaintainOperation();

		operation.setCheckName("修改网元密码");
		operation.setIsDone(false);
		operation.setRequestTime(new Date());
		operation.setCommandCategory(commandCategory);
		operation.setCreateBy(userName);
		operation = maintainOperationRepository.save(operation);
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		systemService.saveSystemOperationLog(shiroUser, category_cn, "执行 修改密码 : 在 " + unitMaplist.size() + " 个单元上",
				SystemOperationLogOpType.Other);
		for (Map map : unitMaplist) {
			EquipmentUnit unit = equipmentUnitRepository.findOne(Long.parseLong(map.get("id").toString()));
			String session = UUID.randomUUID().toString().replaceAll("[-]", "");
			MaintainResult checkResult = new MaintainResult();
			checkResult.setUuId(session);
			checkResult.setNe(unit.getNe());
			checkResult.setOperation(operation);
			checkResult.setUnit(unit);
			checkResult.setRequestTime(new Date());
			checkResult = maintainResultRepository.save(checkResult);

			String scriptName = null;
			StringBuilder paramsBuilder = new StringBuilder();
			EquipmentUnitType unitType = unit.getUnitType();

			/**
			 * liuyh update 2015-08-05
			 */

			String rootPassword = unit.getRootPassword();
			String oldPassword = unit.getLoginPassword();

			// 向集中操作维护平台AMQ队列下发命令

			Map<String, Object> mq_map = new HashMap<String, Object>();

			if (EquipmentUnitType.AHUB.equals(unitType)) {
				// 传递的参数(按顺序),参数之间使用:分割 当前用户登陆网元时使用的Ip:当前用户名:加密后的新密码
				paramsBuilder.append(unit.getServerIp());
				paramsBuilder.append(",");
				paramsBuilder.append(unit.getLoginName());
				paramsBuilder.append(",");
				paramsBuilder.append(Encodes.encodeHex(newPassword.getBytes()));
				scriptName = "SSH_DHLR_CHANGE_PASSWORD_4_AHUB";
				mq_map.put("content", new Context(scriptName + "|" + paramsBuilder.toString(), 2, 1));
			} else if (EquipmentUnitType.SGW.equals(unitType)) {
				// 修改SGW的组合指令参数序列是：网元的IP,登录网元时使用的用户名,加密过的新密码,加密过的旧密码
				paramsBuilder.append(unit.getServerIp());
				paramsBuilder.append(",");
				paramsBuilder.append(account);
				paramsBuilder.append(",");
				paramsBuilder.append(Encodes.encodeHex(newPassword.getBytes()));
				paramsBuilder.append(",");
				paramsBuilder.append(Encodes.encodeHex(oldPassword.getBytes()));
				scriptName = "SSH_DHLR_CHANGE_PASSWORD_4_SGW";
				mq_map.put("content", new Context(scriptName + "|" + paramsBuilder.toString(), 2, 1));
			} else if (EquipmentUnitType.CE.equals(unitType)) {
				// 修改CE这种类型的网元的密码，传递的参数(按顺序),参数之间使用:分割
				// 当前用户登陆网元时使用的Ip,当前用户名,加密后的新密码,加密过的旧密码
				paramsBuilder.append(unit.getServerIp());
				paramsBuilder.append(",");
				paramsBuilder.append(account);
				paramsBuilder.append(",");
				paramsBuilder.append(Encodes.encodeHex(newPassword.getBytes()));
				paramsBuilder.append(",");
				paramsBuilder.append(Encodes.encodeHex(oldPassword.getBytes()));
				scriptName = "SSH_DHLR_CHANGE_PASSWORD_4_CE";
				mq_map.put("content", new Context(scriptName + "|" + paramsBuilder.toString(), 2, 1));
			} else {
				// 执行集中操作组合指令参数：待修改的账号名称:加密后的账号新密码:加密后root账号密码
				paramsBuilder.append(unit.getLoginName());
				paramsBuilder.append(":");
				paramsBuilder.append(Encodes.encodeHex(newPassword.getBytes()));
				paramsBuilder.append(":");
				paramsBuilder.append(Encodes.encodeHex(rootPassword.getBytes()));
				paramsBuilder.append(":");
				paramsBuilder.append(unit.getUnitName());
				scriptName = "SSH_DHLR_CHANGE_PASSWORD_COMMON";
				mq_map.put("content", new Context(scriptName + "|" + paramsBuilder.toString(), 2, 1));
			}

			mq_map.put("msgCode", 71000);
			mq_map.put("app", "dhss");
			mq_map.put("cacheTime", 5);
			mq_map.put("maxConnNum", 5);
			mq_map.put("type", unit.getNeType());
			mq_map.put("srcQ", SECURITY_QNAME);
			mq_map.put("destQ", ProjectProperties.getDesQName());
			mq_map.put("sessionid", session);
			mq_map.put("ne", unit.getUnitName());
			mq_map.put("neConnType", "DHSS_" + unit.getServerProtocol());
			mq_map.put("password", Encodes
					.encodeHex(unit.getLoginPassword() != null ? unit.getLoginPassword().getBytes() : "".getBytes()));
			mq_map.put("port", unit.getServerPort());
			mq_map.put("priority", 5);
			mq_map.put("procotol", unit.getServerProtocol());
			mq_map.put("username", unit.getLoginName());
			mq_map.put("ip", unit.getServerIp());
			mq_map.put("hostname", unit.getHostname());
			mq_map.put("netFlag", unit.getNetFlag());
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
			mq_map.put("taskNum", 71003);
			mq_map.put("unitType", unit.getUnitType());
			if (!newPassword.equals(oldPassword)) {
				messageProducer.sendMessage(mq_map);
				logger.debug("Send change " + unit.getUnitName() + " password command informcation to MQ:"
						+ new JsonMapper().toJson(mq_map));

				Map<String, Object> editmap = new HashMap<String, Object>();
				editmap.put("login_password", newPassword);
				editmap.put("root_password", unit.getRootPassword());
				editmap.put("username", account);
				editmap.put("resource_id", unit.getId());
				unit.setIsForbidden(true);
				unit.setTempJson(new JsonMapper().toJson(editmap));

				equipmentUnitRepository.save(unit);
			} 

		}
	}

	@Override
	public Page<MaintainResult> findMaintainResultPageBySearch(List<MaintainOperation> list, Pageable pageable) {
		List<SearchFilter> searchFilter = new ArrayList<SearchFilter>();
		if (!list.isEmpty()) {
			searchFilter.add(new SearchFilter("operation", Operator.ENTITYIN, list));
		} else {
			searchFilter.add(new SearchFilter("id", Operator.LT, 0));
		}
		Specification<MaintainResult> specCommandCheckItem = DynamicSpecifications.bySearchFilter(searchFilter,
				BooleanOperator.AND, MaintainResult.class);

		return maintainResultRepository.findAll(specCommandCheckItem, pageable);
	}

	@Override
	public MaintainResult getMaintainResultByUUID(String session) {
		List<SearchFilter> searchFilter = new ArrayList<SearchFilter>();
		searchFilter.add(new SearchFilter("uuId", Operator.EQ, session));
		Specification<MaintainResult> spec = DynamicSpecifications.bySearchFilter(searchFilter, BooleanOperator.AND,
				MaintainResult.class);

		return maintainResultRepository.findOne(spec);
	}

}
