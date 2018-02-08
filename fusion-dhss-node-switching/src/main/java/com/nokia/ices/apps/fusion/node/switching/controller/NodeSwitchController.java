package com.nokia.ices.apps.fusion.node.switching.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.jms.Message;
import javax.persistence.criteria.Predicate.BooleanOperator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.emergency.domian.EmergencySecurityState;
import com.nokia.ices.apps.fusion.emergency.repository.EmergencySecurityStateRepository;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentNeRepository;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.jms.producer.DHSSMessageProducer;
import com.nokia.ices.apps.fusion.jms.vo.Context;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.node.switching.exception.RestException;
import com.nokia.ices.apps.fusion.node.switching.service.NodeSwitchService;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitch;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchCase;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchCmd;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchSingleCmd;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchSingleHistory;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchSingleStepExecute;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchStepExecute;
import com.nokia.ices.apps.fusion.nodeswitching.repository.NodeSwitchCaseRepository;
import com.nokia.ices.apps.fusion.nodeswitching.repository.NodeSwitchRepository;
import com.nokia.ices.apps.fusion.nodeswitching.repository.NodeSwitchSingleHistoryRepository;
import com.nokia.ices.apps.fusion.nodeswitching.repository.NodeSwitchSingleStepExecuteRepository;
import com.nokia.ices.apps.fusion.nodeswitching.repository.NodeSwitchStepExecuteRepository;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.core.utils.Encodes;
import com.nokia.ices.core.utils.JsonMapper;

@RequestMapping
@RestController
public class NodeSwitchController {

	public static final Logger logger = LoggerFactory.getLogger(NodeSwitchController.class);
	@Autowired
	NodeSwitchService service;
	@Autowired
	DHSSMessageProducer messageProducer;

	@Autowired
	NodeSwitchSingleHistoryRepository nodeSwitchSingleHistoryRepository;

	@Autowired
	NodeSwitchSingleStepExecuteRepository nodeSwitchSingleStepExecuteRepository;

	@Autowired
	NodeSwitchRepository nodeSwitchRepository;

	@Autowired
	NodeSwitchStepExecuteRepository nodeSwitchStepExecuteRepository;

	@Autowired
	NodeSwitchCaseRepository nodeSwitchCaseRepository;

	@Autowired
	EquipmentUnitRepository equipmentUnitRepository;

	@Autowired
	EmergencySecurityStateRepository emergencySecurityStateRepository;

	@Autowired
	EquipmentNeRepository equipmentNeRepository;

	public String[] res = null;

	@RequestMapping(value = "nodeSwitch/updateSwitchingReturnInfo", method = RequestMethod.GET)
	public NodeSwitch updateSwitchingReturnInfo(@RequestParam(value = "id", required = true) String id) {
		NodeSwitch node = nodeSwitchRepository.findOne(Long.parseLong(id));
		node.setReturnInfo("");
		node = nodeSwitchRepository.save(node);

		return node;
	}

	@RequestMapping(value = "nodeSwitch/updateReturnInfo", method = RequestMethod.GET)
	public NodeSwitchSingleHistory updateReturnInfo(@RequestParam(value = "id", required = true) String id) {
		NodeSwitchSingleHistory node = nodeSwitchSingleHistoryRepository.findOne(Long.parseLong(id));
		node.setReturnInfo("");
		node = nodeSwitchSingleHistoryRepository.save(node);

		return node;
	}

	/**
	 * 获得执行指令步骤
	 * 
	 * @param fromUnitName
	 * @param toUnitName
	 * @param ne_Id
	 * @param siteName
	 * @param neName
	 * @param unitTypeName
	 * @return
	 * @throws RestException
	 */
	@RequestMapping(value = "nodeSwitch/stepExecute", method = RequestMethod.GET)
	public Map<String, Object> stepExecute(@RequestParam(value = "neId", required = true) Integer neId,
			@RequestParam(value = "siteName", required = true) String siteName,
			@RequestParam(value = "neName", required = true) String neName,
			@RequestParam(value = "caseId", required = true) Integer caseId,
			@RequestParam(value = "actionVal", required = true) String actionVal) throws RestException {

		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		NodeSwitchCase ca = new NodeSwitchCase();
		if (caseId != null) {
			if (StringUtils.isNotEmpty(String.valueOf(caseId))) {
				ca = nodeSwitchCaseRepository.findOne(Long.valueOf(caseId));
			}
		}

		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ne.neName_EQ", neName);
		map1.put("operateState_NOTEQ", "1");
		map1.put("operateState_NOTEQ", "2");
		map1.put("nodeCase.id_EQ", caseId.toString());
		Map<String, SearchFilter> filter = SearchFilter.parse(map1);
		Specification<EmergencySecurityState> spec = DynamicSpecifications.bySearchFilter(filter.values(),
				BooleanOperator.AND, EmergencySecurityState.class);
		List<EmergencySecurityState> list1 = emergencySecurityStateRepository.findAll(spec);

		map1.clear();
		map1.put("neName_EQ", neName);
		filter = SearchFilter.parse(map1);
		Specification<EquipmentNe> spec1 = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				EquipmentNe.class);
		EquipmentNe ne = equipmentNeRepository.findAll(spec1).get(0);

		EmergencySecurityState ess = null;
		if (list1.size() != 0) {
			ess = list1.get(0);
		} else {
			ess = new EmergencySecurityState();
			ess.setOperate(0);
			ess.setNe(ne);
			ess.setUnit(null);
			ess.setNodeCase(ca);
		}
		ess.setOperateDate(new Date());
		ess.setOperateState(actionVal.equals("隔离") ? 5 : 4);
		ess.setOperator(shiroUser.getRealName());
		emergencySecurityStateRepository.save(ess);

		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("siteName_EQ", siteName);
		maps.put("caseName_EQ", ca.getCasename());
		maps.put("confTypeName_EQ", actionVal);
		maps.put("neName_EQ", neName);
		List<NodeSwitch> nodeList = service.getNodeSwitchStepList(maps);
		NodeSwitch ns = null;
		List<NodeSwitchCmd> stepList = null;

		if (nodeList.size() == 0) {
			ns = new NodeSwitch();
			ns.setSiteName(siteName);
			ns.setNeName(neName);
			ns.setConfTypeName(actionVal);
			ns.setCaseName(ca.getCasename());
			ns.setOperateDate(new Date());
			ns.setOperator(shiroUser.getUserName());
			ns.setReturnInfo("");
			ns = nodeSwitchRepository.save(ns);

			Map<String, Object> param3 = new HashMap<String, Object>();
			param3.put("confType_EQ", actionVal.equals("隔离") ? "1" : "2");
			param3.put("siteName_EQ", siteName);
			if (caseId != null) {
				if (StringUtils.isNotEmpty(String.valueOf(caseId))) {
					param3.put("caseId_EQ", String.valueOf(caseId));
				}
			}
			stepList = service.getStepList(param3);
			for (NodeSwitchCmd map : stepList) {
				NodeSwitchStepExecute nsse = new NodeSwitchStepExecute();
				nsse.setNodeSwitch(ns);
				nsse.setNodeSwitchCmd(map);
				nodeSwitchStepExecuteRepository.save(nsse);
			}
		} else {
			ns = nodeList.get(0);
			maps.clear();
			maps.put("nodeSwitch.id_EQ", String.valueOf(ns.getId()));
			List<NodeSwitchStepExecute> nsseList = service.getNodeSwitchStepExecuteList(maps);
			stepList = new ArrayList<NodeSwitchCmd>();
			for (NodeSwitchStepExecute nodeSwitchStepExecute : nsseList) {
				stepList.add(nodeSwitchStepExecute.getNodeSwitchCmd());
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("list", stepList);
		result.put("id", ns.getId());
		result.put("result", ns.getReturnInfo());

		return result;
	}

	/**
	 * 网元倒换_执行指令
	 * 
	 * @return
	 */
	@RequestMapping(value = "nodeSwitch/switching", method = RequestMethod.GET)
	public Map<String, Object> switching(@RequestParam(value = "unitName", required = true) String unitName,
			@RequestParam(value = "script", required = true) String script,
			@RequestParam(value = "command", required = true) String command,
			@RequestParam(value = "id", required = true) Long id) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("unitName_EQ", unitName);
		Map<String, SearchFilter> filter = SearchFilter.parse(params);
		Specification<EquipmentUnit> spec = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				EquipmentUnit.class);
		List<EquipmentUnit> list = equipmentUnitRepository.findAll(spec);
		EquipmentUnit unitEn = null;
		for (EquipmentUnit unita : list) {
			unitEn = unita;
		}
		if (unitEn == null) {
			return null;
		}

		EmergencySecurityState ess = new EmergencySecurityState();
		emergencySecurityStateRepository.save(ess);

		Map<String, Object> result = new HashMap<String, Object>();
		String sessionId = UUID.randomUUID().toString();
		Map<String, Object> mq_map = new HashMap<String, Object>();
		// String commands="";

		// logger.warn("switching is commands"+commands);
		// mq_map.put("neName", unitName);
		// mq_map.put("commandName",script);
		// mq_map.put("commandParams",commands);
		// mq_map.put("exeTimeoutMinutes", 30);
		// mq_map.put("destQ", ProjectProperties.getDesQName());

		mq_map.put("msgCode", 71000);
		mq_map.put("app", "dhss");
		mq_map.put("cacheTime", 5);
		mq_map.put("maxConnNum", 5);
		mq_map.put("type", unitEn.getNeType());
		mq_map.put("srcQ", "DHLR_BACK_WLEI");
		mq_map.put("destQ", ProjectProperties.getDesQName());
		mq_map.put("sessionid", sessionId);
		mq_map.put("ne", unitEn.getUnitName());
		mq_map.put("neConnType", "DHSS_" + unitEn.getServerProtocol());
		mq_map.put("password", Encodes
				.encodeHex(unitEn.getLoginPassword() != null ? unitEn.getLoginPassword().getBytes() : "".getBytes()));
		mq_map.put("port", unitEn.getServerPort());
		mq_map.put("priority", 5);
		mq_map.put("procotol", unitEn.getServerProtocol());
		mq_map.put("username", unitEn.getLoginName());
		mq_map.put("ip", unitEn.getServerIp());

		
		if ("Hss_NetConvert".equals(script)) {
			mq_map.put("content",
					new Context(unitEn.getServerProtocol() + "_" +
							script + "|" + unitEn.getLoginName() + ","
									+ Encodes.encodeHex(unitEn.getRootPassword() != null
											? unitEn.getRootPassword().getBytes() : "".getBytes())
									+ ":" + command,
							2, 1));
			// commands = script+"|test,test:"+command;
		} else if ("HSS_SGW_NETOPERATE".equals(script)) {
			mq_map.put("content",
					new Context(unitEn.getServerProtocol() + "_" +
							script + "|" + unitEn.getLoginName() + ","
									+ Encodes.encodeHex(unitEn.getRootPassword() != null
											? unitEn.getRootPassword().getBytes() : "".getBytes())
									+ "@" + command,
							2, 1));
			// commands = script+"|test,test@"+command;
		} else {
			mq_map.put("content",
					new Context(unitEn.getServerProtocol() + "_" +
							/* script+"|"+ */unitEn.getLoginName() + ","
									+ Encodes.encodeHex(unitEn.getRootPassword() != null
											? unitEn.getRootPassword().getBytes() : "".getBytes())
									+ ":" + command,
							1, 1));
			// commands = "rtp99,yt_xk39b:"+command;
		}
		

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
		mq_map.put("taskNum", 71006);
		mq_map.put("unitType", unitEn.getUnitType());


		messageProducer.sendMessage(mq_map);
		// logger.warn("switching is commands"+commands);
		this.setRes(null);
		String resultCode = "";
		String msg = "";
		String ret = "无日志信息";
		int timeOut = 500;
		/***/
		while (true) {
			try {
				logger.warn("waiting...");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (timeOut-- <= 0) {
				logger.warn("Backup Timeout, Stop");
				result.put("error", -1);
				break;
			}

			if (this.getRes() != null) {
				resultCode = this.getRes()[0];
				msg = this.getRes()[1];
				if (!resultCode.equals("0")) {// 执行失败
					result.put("error", -2);
				} else {// 执行成功
					ret = msg/* FileOperateUtil.readFile(msg) */;
					NodeSwitch ns = nodeSwitchRepository.findOne(id);
					String retInfo = ns.getReturnInfo() + ret + ";";
					ns.setReturnInfo(retInfo);
					ns.setOperateDate(new Date());
					nodeSwitchRepository.save(ns);
					Map<String, Object> maps = new HashMap<String, Object>();
					maps.put("nodeSwitch.id_EQ", String.valueOf(ns.getId()));
					List<NodeSwitchStepExecute> nsseList = service.getNodeSwitchStepExecuteList(maps);
					List<NodeSwitchCmd> stepList = new ArrayList<NodeSwitchCmd>();
					for (NodeSwitchStepExecute nodeSwitchStepExecute : nsseList) {
						stepList.add(nodeSwitchStepExecute.getNodeSwitchCmd());
					}
					result.put("ret", retInfo);
					result.put("list", stepList);
					result.put("id", ns.getId());
					result.put("result", ns.getReturnInfo());
				}
				break;
			}
		}
		return result;
	}

	/**
	 * 单板卡倒换步骤列表
	 */
	@RequestMapping(value = "nodeSwitch/stepNodeExecute", method = RequestMethod.GET)
	public Map<String, Object> stepNodeExecute(@RequestParam(value = "siteName", required = true) String siteName,
			@RequestParam(value = "neName", required = true) String neName,
			@RequestParam(value = "unitName", required = true) String unitName,
			@RequestParam(value = "neType", required = true) String neType,
			@RequestParam(value = "actionVal", required = true) String actionVal) throws RestException {
		NodeSwitchSingleHistory nssh = null;
		List<NodeSwitchSingleCmd> singleStepList = null;
		String type = actionVal.equals("隔离") ? "1" : "2";
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("neName_EQ", neName);
		maps.put("unitName_EQ", unitName);
		maps.put("confTypeName_EQ", actionVal);
		List<NodeSwitchSingleHistory> lists = service.getNodeSwitchSingleHistory(maps);
		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("unit.unitName_EQ", unitName);
		// map.put("operateState_EQ", type.equals("1") ? "2" : "1");
		Map<String, SearchFilter> filter = SearchFilter.parse(map);
		Specification<EmergencySecurityState> spec = DynamicSpecifications.bySearchFilter(filter.values(),
				BooleanOperator.AND, EmergencySecurityState.class);
		List<EmergencySecurityState> list1 = emergencySecurityStateRepository.findAll(spec);

		map.clear();
		map.put("unitName_EQ", unitName);
		filter = SearchFilter.parse(map);
		Specification<EquipmentUnit> spec1 = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				EquipmentUnit.class);
		EquipmentUnit unit = equipmentUnitRepository.findAll(spec1).get(0);

		EmergencySecurityState ess = null;
		if (list1.size() != 0) {
			ess = list1.get(0);
		} else {
			ess = new EmergencySecurityState();
			ess.setOperate(0);
			ess.setNe(unit.getNe());
			ess.setUnit(unit);
		}
		ess.setOperateDate(new Date());
		ess.setOperateState(type.equals("1") ? 2 : 1);
		ess.setOperator(shiroUser.getRealName());
		emergencySecurityStateRepository.save(ess);

		if (lists.size() == 0) {

			nssh = new NodeSwitchSingleHistory();
			nssh.setLocation(siteName);
			nssh.setNeName(neName);
			nssh.setUnitName(unitName);
			nssh.setConfTypeName(actionVal);
			nssh.setOperatorDesc(shiroUser.getUserName());
			nssh.setReturnInfo("");
			nssh.setOperateDate(new Date());
			nssh = nodeSwitchSingleHistoryRepository.save(nssh);

			Map<String, Object> step_param = new HashMap<String, Object>();
			step_param.put("confType_EQ", type);
			step_param.put("neType_EQ", neType);
			singleStepList = service.getSingleStepList(step_param);
			List<NodeSwitchSingleStepExecute> list = new ArrayList<NodeSwitchSingleStepExecute>();
			for (NodeSwitchSingleCmd nssc : singleStepList) {
				NodeSwitchSingleStepExecute execute = new NodeSwitchSingleStepExecute();
				execute.setNssh(nssh);
				execute.setNssc(nssc);
				list.add(execute);
			}
			nodeSwitchSingleStepExecuteRepository.save(list);
		} else {
			nssh = lists.get(0);
			singleStepList = new ArrayList<NodeSwitchSingleCmd>();
			maps.clear();
			maps.put("nssh.id_EQ", String.valueOf(nssh.getId()));
			List<NodeSwitchSingleStepExecute> ne = service.getNodeSwitchSingleStepExecute(maps);
			for (NodeSwitchSingleStepExecute nodeSwitchSingleStepExecute : ne) {
				singleStepList.add(nodeSwitchSingleStepExecute.getNssc());
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("list", singleStepList);
		result.put("id", nssh.getId());
		result.put("result", nssh.getReturnInfo());
		return result;
	}

	@RequestMapping(value = "nodeSwitch/updateSingleSwitching", method = RequestMethod.GET)
	public Map<String, Object> updateSingleSwitching(@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "desc", required = true) String desc) {
		NodeSwitchSingleHistory node = nodeSwitchSingleHistoryRepository.findOne(Long.parseLong(id));
		node.setReturnInfo(node.getReturnInfo() + desc + ",");
		node = nodeSwitchSingleHistoryRepository.save(node);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nssh.id_EQ", String.valueOf(node.getId()));
		List<NodeSwitchSingleStepExecute> ne = service.getNodeSwitchSingleStepExecute(map);
		List<NodeSwitchSingleCmd> singleStepList = new ArrayList<NodeSwitchSingleCmd>();
		for (NodeSwitchSingleStepExecute nodeSwitchSingleStepExecute : ne) {
			singleStepList.add(nodeSwitchSingleStepExecute.getNssc());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("list", singleStepList);
		result.put("id", node.getId());
		result.put("result", node.getReturnInfo());
		return result;
	}

	@RequestMapping(value = "nodeSwitch/updateSwitching", method = RequestMethod.GET)
	public Map<String, Object> updateSwitching(@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "desc", required = true) String desc) {
		NodeSwitch node = nodeSwitchRepository.findOne(Long.parseLong(id));
		node.setReturnInfo(node.getReturnInfo() + desc + ",");
		node = nodeSwitchRepository.save(node);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nodeSwitch.id_EQ", String.valueOf(node.getId()));
		List<NodeSwitchStepExecute> ne = service.getNodeSwitchStepExecuteList(map);
		List<NodeSwitchCmd> singleStepList = new ArrayList<NodeSwitchCmd>();
		for (NodeSwitchStepExecute nodeSwitchStepExecute : ne) {
			singleStepList.add(nodeSwitchStepExecute.getNodeSwitchCmd());
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("list", singleStepList);
		result.put("id", node.getId());
		result.put("result", node.getReturnInfo());
		return result;
	}

	/**
	 * 单板卡倒换_执行指令
	 * 
	 * @return
	 */
	@RequestMapping(value = "nodeSwitch/singleSwitching", method = RequestMethod.GET)
	public Map<String, Object> singleSwitching(@RequestParam(value = "unitName", required = true) String unitName,
			@RequestParam(value = "script", required = true) String script,
			@RequestParam(value = "command", required = true) String command,
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "desc", required = true) String desc) {

		NodeSwitchSingleHistory node = nodeSwitchSingleHistoryRepository.findOne(Long.parseLong(id));

		Map<String, Object> result = new HashMap<String, Object>();
		String sessionId = UUID.randomUUID().toString();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("unitName_EQ", unitName);
		Map<String, SearchFilter> filter = SearchFilter.parse(params);
		Specification<EquipmentUnit> spec = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				EquipmentUnit.class);
		List<EquipmentUnit> list = equipmentUnitRepository.findAll(spec);
		// String password = "";
		EquipmentUnit unitEn = null;
		for (EquipmentUnit unita : list) {
			// password =
			// Hex.encodeHexString(unita.getRootPassword().getBytes());
			unitEn = unita;
		}
		if (unitEn == null) {
			return null;
		}
		// String commands = "rtp99,"+password+":"+command;
		Map<String, Object> mq_map = new HashMap<String, Object>();
		/*
		 * mq_map.put("srcQ", "DHLR_BACK_WLEI"); mq_map.put("destQ",
		 * ProjectProperties.getDesQName()); mq_map.put("session", sessionId);
		 * mq_map.put("neName", unitName); mq_map.put("commandName",script);
		 * mq_map.put("commandParams",commands); mq_map.put("exeTimeoutMinutes",
		 * 30);
		 */

		mq_map.put("msgCode", 71000);
		mq_map.put("app", "dhss");
		mq_map.put("cacheTime", 5);
		mq_map.put("maxConnNum", 5);
		mq_map.put("type", unitEn.getNeType());
		mq_map.put("srcQ", "DHLR_BACK_WLEI");
		mq_map.put("destQ", ProjectProperties.getDesQName());
		mq_map.put("sessionid", sessionId);
		mq_map.put("ne", unitEn.getUnitName());
		mq_map.put("neConnType", "DHSS_" + unitEn.getServerProtocol());
		mq_map.put("password", Encodes
				.encodeHex(unitEn.getLoginPassword() != null ? unitEn.getLoginPassword().getBytes() : "".getBytes()));
		mq_map.put("port", unitEn.getServerPort());
		mq_map.put("priority", 5);
		mq_map.put("procotol", unitEn.getServerProtocol());
		mq_map.put("username", unitEn.getLoginName());
		mq_map.put("ip", unitEn.getServerIp());

		mq_map.put("content",
				new Context(unitEn.getServerProtocol() + "_" + script + "|" + unitEn.getLoginName() + ","
						+ Encodes.encodeHex(
								unitEn.getRootPassword() != null ? unitEn.getRootPassword().getBytes() : "".getBytes())
				+ ":" + command, 2, 1));

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
		mq_map.put("taskNum", 71005);
		mq_map.put("unitType", unitEn.getUnitType());

		this.setRes(null);
		messageProducer.sendMessage(mq_map);
		String resultCode = "";
		int timeOut = 500;

		while (true) {
			try {
				logger.warn("waiting...");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (timeOut-- <= 0) {
				logger.warn("SingleSwitching Timeout, Stop");
				result.put("error", -1);
				break;
			}

			if (this.getRes() != null) {
				resultCode = this.getRes()[0];
				if (!resultCode.equals("0")) {// 执行失败
					result.put("error", -2);
				} else {// 执行成功

					// 将返回的信息保存进数据库
					node.setOperateDate(new Date());
					node.setReturnInfo(node.getReturnInfo() + this.getRes()[1] + ";");
					result.put("ret", ProjectProperties.getCOMP_BASE_PATH() + this.getRes()[1]);// 回显到页面
					node = nodeSwitchSingleHistoryRepository.save(node);
					mq_map.clear();
					mq_map.put("nssh.id_EQ", String.valueOf(node.getId()));
					List<NodeSwitchSingleStepExecute> ne = service.getNodeSwitchSingleStepExecute(mq_map);
					List<NodeSwitchSingleCmd> singleStepList = new ArrayList<NodeSwitchSingleCmd>();
					for (NodeSwitchSingleStepExecute nodeSwitchSingleStepExecute : ne) {
						singleStepList.add(nodeSwitchSingleStepExecute.getNssc());
					}
					result.put("list", singleStepList);
					result.put("id", node.getId());
					result.put("result", node.getReturnInfo());
				}
				break;
			}
		}

		return result;
	}

	@RequestMapping(value = "nodeSwitch/downloadLog", method = RequestMethod.GET)
	public void downloadDetailLog(HttpServletRequest request, HttpServletResponse response) {
		String url = request.getParameter("url");
		String desc = request.getParameter("desc");
		try {
			download(request, response, ProjectProperties.getCOMP_BASE_PATH() + url, "application/octet-stream",
					desc + ".txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	@JmsListener(destination = "DHLR_BACK_WLEI", containerFactory = "jmsContainerFactory")
	public void reseiveMessageToBackUp(Message message) {
		try {
			this.setRes(null);
			System.out.println("----------------------------accpet-------------------------------------");
			System.out.println("-----------------------------------------------------------------");
			String msgBody = message.getStringProperty("msgBody");
			@SuppressWarnings("unchecked")
			Map<String, String> json = (Map<String, String>) new JsonMapper().fromJson(msgBody, Map.class);
			String resultCode = String.valueOf(json.get("flag"));
			String msg = resultCode.equals("0") ? String.valueOf(json.get("src")) : String.valueOf(json.get("msg"));// 网元操作日志返回msg
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

	public String[] getRes() {
		return res;
	}

	public void setRes(String[] res) {
		this.res = res;
	}

	public NodeSwitchService getService() {
		return service;
	}

	public void setService(NodeSwitchService service) {
		this.service = service;
	}

	public DHSSMessageProducer getMessageProducer() {
		return messageProducer;
	}

	public void setMessageProducer(DHSSMessageProducer messageProducer) {
		this.messageProducer = messageProducer;
	}

}
