package com.nokia.ices.apps.fusion.subtool.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.equipment.service.NumberSectionService;
import com.nokia.ices.apps.fusion.jms.producer.DHSSMessageProducer;
import com.nokia.ices.apps.fusion.security.domain.ShiroUser;
import com.nokia.ices.apps.fusion.subtool.controller.SendSubToolThread;
import com.nokia.ices.apps.fusion.subtool.domain.SubtoolPgwLdapIp;
import com.nokia.ices.apps.fusion.subtool.message.SubtoolMessageConsumer;
import com.nokia.ices.apps.fusion.subtool.repository.SubtoolRepository;
import com.nokia.ices.apps.fusion.subtool.service.EditVrlOrSgsnService;
import com.nokia.ices.apps.fusion.subtool.service.SubtoolCmdService;
import com.nokia.ices.apps.fusion.subtool.service.SubtoolResultService;
import com.nokia.ices.core.utils.Encodes;

@Service
public class SubtoolCmdServiceImpl implements SubtoolCmdService {

	private final static Logger logger = LoggerFactory.getLogger(SubtoolCmdServiceImpl.class);

	/**
	 * 
	 * 用于用户数据管理命令下发匹配对应ip做号码缓存
	 */
	public static Map<String, List<String>> NUMBER_CACHE = new HashMap<String, List<String>>();

	@Autowired
	EditVrlOrSgsnService editVrlOrSgsnService;
	@Autowired
	DHSSMessageProducer messageProducer;
	@Autowired
	private EquipmentUnitRepository equipmentUnitRepository;
	@Autowired
	SubtoolResultService subtoolService;
	@Autowired
	NumberSectionService numberSectionService;
	@Autowired
	SubtoolRepository subtoolRepository;

	/**
	 * 发送subtool 命令
	 * isSucc:0成功
	 * isSucc：1 未找到合适的IP
	 * isSucc:3命令中未找到msisdn or imsi
	 */
	@Override
	public String sendCommandSubtool(String command, String checkName) {

		String isSucc =null;
		// 提取imsi或者msisdn号码
		String regStr = "[0-9]{13,}";
		String number = regNumber(regStr, command);

		logger.debug("command = " + command + "   number = " + number);
		if ("no".equalsIgnoreCase(number)) {
			isSucc = "no";
			// 命令中未找到imsi or msisdn
		} else {
			String cmdType = ProjectProperties.getCmdType();
			// 开始下发命令
			if ("0".equalsIgnoreCase(cmdType)) {
				isSucc = sendMmlCmd(command, checkName, number);
			} else {
				isSucc = sendSubtoolCmd(command, checkName, number);
			}
		}

		return isSucc;
	}

	/**
	 * 执行命令参数处理
	 * 
	 * @param command
	 * @param checkName
	 */
	private List<String> getCommandParams(ShiroUser shiroUser, String handleNumber, String type) {

		List<String> listIp = null;
		if (NUMBER_CACHE.containsKey(handleNumber)) {
			logger.debug("get cache number ......" + handleNumber);
			listIp = NUMBER_CACHE.get(handleNumber);
		} else {
			logger.debug("get dataBase number ......" + handleNumber);
			String isFlag = ProjectProperties.getIfDynamicGetIp();
			if ("true".equals(isFlag)) {
				List<EquipmentUnit> soapUrlList = numberSectionService.findAllUnitTypeByNumberSection(handleNumber,
						type, "PGW", shiroUser);
				listIp = filterPgwIpAndLdapIp(soapUrlList);
			} else {
				/**
				 * 获取配置ip
				 */
				listIp = new ArrayList<String>();
				String Ips = ProjectProperties.getSoap_ip();
				String[] addres = Ips.split(",");
				for (String ip : addres) {
					if (!ip.equals("")) {
						listIp.add(ip);
					}
				}
			}
			// 加入缓存
			if (null != listIp && listIp.size() > 0) {
				NUMBER_CACHE.put(handleNumber, listIp);
			}
		}
		return listIp;
	}

	/**
	 * 根据pgwIp匹配ldap ip
	 * 
	 * @param list
	 * @return
	 */
	private List<String> filterPgwIpAndLdapIp(List<EquipmentUnit> list) {

		logger.debug(" pgw ip size..." + list.size());
		List<String> listIp = new ArrayList<>();
		if (null != list && list.size() > 0) {
			for (EquipmentUnit unit : list) {
				List<SubtoolPgwLdapIp> pgwLdaps = subtoolService.fingSubtoolLadpIpList(unit.getServerIp());
				if (null != pgwLdaps && pgwLdaps.size() > 0) {
					for (SubtoolPgwLdapIp sub : pgwLdaps) {
						String pgwLdap = sub.getPgwIp() + ":" + sub.getLdapIp();
						if (!listIp.contains(pgwLdap)) {
							listIp.add(pgwLdap);
							logger.debug("unitType pgw ip and ldap ip ..." + pgwLdap);
						}
					}
				} else {
					// 未匹配使用全部
					if (!listIp.contains(unit.getServerIp())) {
						listIp.add(unit.getServerIp());
					}
				}
			}
		} else {
			logger.debug("The soap object url is  0 or null");
		}
		return listIp;

	}

	
	/**
	 * 针对移动客户群，发送subtool命令
	 * @param command
	 * @param checkName
	 * @param number
	 * @return
	 */
	private String sendSubtoolCmd(String command, String checkName, String number) {

		ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
		/**
		 * 命令中代码MSISDN，IMSI替换成小写，因subtool支持小写
		 */
		String type = null;
		if (number.length() > 13) {
			type = "2";
			if (command.contains("IMSI") || command.contains("imsi")) {
				command = command.replaceAll("IMSI", "imsi");
			}
		} else {
			type = "1";
			if (command.contains("MSISDN") || command.contains("msisdn")) {
				command = command.replaceAll("MSISDN", "msisdn");
			}
		}
		if (command.lastIndexOf(";") != -1) {
			command = command.substring(0, command.lastIndexOf(";"));
		}
		// 获取ip
		List<String> pgwIp = getCommandParams(shiroUser, number, type);

		if (null != pgwIp && pgwIp.size() > 0) {
			if (command.contains("ZMIMSGSN") || command.contains("ZMIMVLR")) {
				// 杭州针对vlr/sgsn 走soapGetw 下发命令
				editVrlOrSgsnService.editVlrOrSgsn(command, pgwIp, checkName);
			} else {
				// 其他统一走subtool环境工具下发命令
				new Thread(new SendSubToolThread(command, checkName, subtoolRepository, pgwIp, shiroUser.getUserName(),
						number)).start();
			}
		} else {
			logger.debug("The soap pgwIp is not Null or ListIp size = 0");
			return "zero";
		}

		return "ok";

	}

	/**
	 * 发送mml命令，针对联通客户群
	 * 
	 * @param command
	 * @param checkName
	 * @param number
	 * @return
	 */
	private String sendMmlCmd(String command, String checkName, String number) {

		command = command.replaceAll("\n", "");
		String uuid = UUID.randomUUID().toString();
		List<EquipmentUnit> listUnit = equipmentUnitRepository
				.findListByUnitTypeEquals(EquipmentUnitType.valueOf(EquipmentUnitType.class, "SOAP_GW"));
		if (null != listUnit && listUnit.size() > 0) {
			ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
			for (EquipmentUnit unit : listUnit) {
				if (ProjectProperties.getMmlIp().equalsIgnoreCase(unit.getServerIp())) {
					Map<String, Object> mq_map = new HashMap<String, Object>();
					mq_map.put("app", "dhss");
					mq_map.put("destQ", ProjectProperties.getDesQName());
					mq_map.put("srcQ", SubtoolResultServiceImpl.SRCQ_NAME_UNIT);
					// 命令对象 DHLR__USERMANAGER_COMMAND
					Map<String, Object> content = new HashMap<String, Object>();
					String dhlr_command = "DHLR_USERMANAGER_COMMAND|" + command;
					content.put("cmd", dhlr_command);
					content.put("ct", 2);
					content.put("rt", 1);
					mq_map.put("content", content);
					mq_map.put("msg", "");
					mq_map.put("procotol", ProjectProperties.getMmlProtocol());
					mq_map.put("flag", "");
					mq_map.put("jumpCount", 0);
					mq_map.put("sessionid", uuid);
					mq_map.put("callInterfaceName", "");
					mq_map.put("type", unit.getNe().getNeType());
					mq_map.put("unitType", unit.getNe().getNeType());
					mq_map.put("password", Encodes.encodeHex(ProjectProperties.getMmlUserPwd().getBytes()));
					mq_map.put("hostname", unit.getHostname());
					mq_map.put("vendor", "nokia");
					mq_map.put("maxConnNum", 8);
					mq_map.put("msgCode", 71000);
					mq_map.put("taskNum", 71000);
					mq_map.put("neConnType", "DHSS_" + ProjectProperties.getMmlProtocol());
					mq_map.put("netFlag", unit.getNetFlag());
					mq_map.put("src", "");
					mq_map.put("ip", ProjectProperties.getMmlIp());
					mq_map.put("priority", 8);
					mq_map.put("exculde", 0);
					mq_map.put("retryTimes", 2);
					mq_map.put("needJump", 0);
					mq_map.put("cacheTime", 5);
					mq_map.put("port", ProjectProperties.getMmlPort());
					mq_map.put("ne", unit.getUnitName());
					mq_map.put("retryInterval", 3);
					mq_map.put("username", ProjectProperties.getMmlUserName());

					SubtoolMessageConsumer.cacheCheckName.put(uuid,
							number + "," + checkName + "," + shiroUser.getUserName() + "," + unit.getUnitName());
					/**
					 * 发送mml命令
					 */
					messageProducer.sendMessage(mq_map);
					break;// 跳出循环
				}
			}
			return "ok";
		} else {
			logger.debug("The SOAP_GW is IP size 0.........");
			return "zero";
		}
	}

	private static String regNumber(String regStr, String command) {

		Pattern pattern = Pattern.compile(regStr);
		Matcher matcher = pattern.matcher(command);
		if (matcher.find()) {
			return matcher.group();
		} else {
			logger.debug("Number is not standard........");
			return "no";
		}
	}

}
