package com.nokia.ices.apps.fusion.ems.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.persistence.criteria.Predicate.BooleanOperator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.command.repository.CommandCheckItemRepository;
import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.ems.consumer.CommandConsumer;
import com.nokia.ices.apps.fusion.ems.consumer.ServiceConsumer;
import com.nokia.ices.apps.fusion.ems.domain.EmsCheckJob;
import com.nokia.ices.apps.fusion.ems.domain.EmsMonitor;
import com.nokia.ices.apps.fusion.ems.domain.EmsMonitorHistory;
import com.nokia.ices.apps.fusion.ems.domain.EmsMutedItem;
import com.nokia.ices.apps.fusion.ems.message.MessageService;
import com.nokia.ices.apps.fusion.ems.message.MessageSms;
import com.nokia.ices.apps.fusion.ems.repository.EmsCheckJobRepository;
import com.nokia.ices.apps.fusion.ems.repository.EmsMonitorHistoryRepository;
import com.nokia.ices.apps.fusion.ems.repository.EmsMonitorRepoitory;
import com.nokia.ices.apps.fusion.ems.repository.EmsMutedItemRepository;
import com.nokia.ices.apps.fusion.ems.run.EmsTask;
import com.nokia.ices.apps.fusion.ems.service.EmsCheckTaskJobService;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.equipment.repository.EquipmentUnitRepository;
import com.nokia.ices.apps.fusion.jms.vo.Context;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.system.domain.SystemRole;
import com.nokia.ices.apps.fusion.system.domain.SystemUser;
import com.nokia.ices.apps.fusion.system.repository.SystemRoleRepository;
import com.nokia.ices.apps.fusion.system.repository.SystemUserRepository;
import com.nokia.ices.core.utils.Encodes;
import com.nokia.ices.core.utils.JsonMapper;

@Service("emsCheckTaskJobService")
public class EmsCheckTaskJobServiceImpl implements EmsCheckTaskJobService {
	
	private static Logger logger = LoggerFactory.getLogger(EmsCheckTaskJobServiceImpl.class);

	@Autowired
	private EmsCheckJobRepository emsCheckJobRepository;
	
	@Autowired
	private EquipmentUnitRepository equipmentUnitRepository;
	
	@Autowired
	private CommandCheckItemRepository commandCheckItemRepository;
	
	@Autowired
	private EmsMonitorRepoitory emsMonitorRepoitory;
	
	@Autowired
	private EmsMonitorHistoryRepository emsMonitorHistoryRepository;
	
	@Autowired
	private SystemRoleRepository systemRoleRepository;
	
	@Autowired
	private SystemUserRepository systemUserRepository;
	
	@Autowired
	private EmsMutedItemRepository emsMutedItemRepository;
	
	@Autowired
    private JmsTemplate jmsTemplate;
	
	
	
	@Override
	public void execEmsJob(EmsCheckJob emsCheckJob) {
		if(StringUtils.isNotBlank(emsCheckJob.getUnits()) && StringUtils.isNotBlank(emsCheckJob.getCommands())){
//			StringBuffer unitStr = new StringBuffer(emsCheckJob.getUnits());
//			StringBuffer commandStr = new StringBuffer(emsCheckJob.getCommands());
			logger.info("JOB NAME : " + emsCheckJob.getJobName()+", UNITS:" + emsCheckJob.getUnits() + " COMMANDS:" + emsCheckJob.getCommands());
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id_IN", arrayToList(emsCheckJob.getUnits().split(",")));
			List<EquipmentUnit> unitList = findUnits(map);
			map.put("id_IN", arrayToList(emsCheckJob.getCommands().split(",")));
			List<CommandCheckItem> commandList = findCommands(map);
			logger.info(" UNIT SIZE:" + unitList.size() + " , COMMAND SIZE:" + commandList.size());
			for (EquipmentUnit equipmentUnit : unitList) {
				for (CommandCheckItem commandCheckItem : commandList) {
					logger.info("在【"+equipmentUnit.getUnitName() + "】上执行【"+commandCheckItem.getName() + "：" + commandCheckItem.getCommand() + "】指令!");
					sendMessage(equipmentUnit, commandCheckItem,emsCheckJob.getRoles());
				}
			}
		}
	}
	
	private void sendMessage(EquipmentUnit equipmentUnit,CommandCheckItem commandCheckItem,String roleId){
		Map<String, Object> mq_map = new HashMap<String, Object>();
		mq_map.put("app", "EMS");
		mq_map.put("cacheTime", 5);
		mq_map.put("maxConnNum", ProjectProperties.getMaxNum()/*equipmentUnit.getUnitType().equals("SGW") || equipmentUnit.getUnitType().equals("SOAP_GW") ? 8  : 5*/);
		mq_map.put("type",equipmentUnit.getNeType());
		mq_map.put("srcQ", CommandConsumer.EMS_CMD_TASK);
        mq_map.put("destQ",ProjectProperties.getDesQName());
        Map<String, String> paramsJson = new HashMap<>();
		paramsJson.put("itemId", commandCheckItem.getId().toString());
		paramsJson.put("roleId", roleId);
		paramsJson.put("cmdTime", System.currentTimeMillis()+"");
		paramsJson.put("UUID", UUID.randomUUID().toString().replace("-", ""));
        mq_map.put("sessionid", new JsonMapper().toJson(paramsJson)/*UUID.randomUUID().toString().replace("-", "") + "@" + commandCheckItem.getId() + "-" + roleId+ "-"+System.currentTimeMillis()*/);
        mq_map.put("ne",  equipmentUnit.getUnitName());
        mq_map.put("neConnType",  "DHSS_"+equipmentUnit.getServerProtocol());
        mq_map.put("password",  Encodes.encodeHex(equipmentUnit.getLoginPassword() != null ? equipmentUnit.getLoginPassword().getBytes() : "".getBytes()));
        mq_map.put("port",  equipmentUnit.getServerPort());
        mq_map.put("priority",  5);
        mq_map.put("procotol",  equipmentUnit.getServerProtocol());
        mq_map.put("username",  equipmentUnit.getLoginName());
        mq_map.put("ip",  equipmentUnit.getServerIp());
        mq_map.put("content",new Context(equipmentUnit.getServerProtocol() + "_DHLR_COMMAND|"+commandCheckItem.getAccount()+","+
        					Encodes.encodeHex(equipmentUnit.getRootPassword() != null ? equipmentUnit.getRootPassword().getBytes() : 
        						"".getBytes())+":"+commandCheckItem.getCommand(),2,1));
        mq_map.put("hostname", equipmentUnit.getHostname());
        mq_map.put("netFlag", equipmentUnit.getNetFlag());
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
        mq_map.put("unitType", equipmentUnit.getUnitType());
        mq_map.put("msgCode", 71000);
        jmsTemplate.setDefaultDestinationName(ProjectProperties.getDesQName());
        jmsTemplate.send(new MessageCreator(){
			public Message createMessage(Session session) throws JMSException {
				 TextMessage txtMessage = session.createTextMessage("");
	                txtMessage.setStringProperty("msgBody", new JsonMapper().toJson(mq_map));
	                txtMessage.setIntProperty("msgCode", Integer.parseInt(mq_map.get("msgCode").toString()));
	                txtMessage.setJMSPriority(5);
	                logger.debug("message.getSrcQ() = {},message.getDestQ() = {}", mq_map.get("srcQ"), mq_map.get("destQ"));
	                logger.debug(txtMessage.toString());
	                return txtMessage;
			}
		});
	}
	
	
	
	@Override
	public void sendMessageService(Map<String, Object> json,EquipmentUnit equipmentUnit,CommandCheckItem commandCheckItem){
		jmsTemplate.setDefaultDestinationName(ProjectProperties.getQueueDestination());
    	jmsTemplate.send(new MessageCreator(){
			@SuppressWarnings("unchecked")
			public Message createMessage(Session session) throws JMSException {
				Map<String,String> paramJson = new JsonMapper().fromJson(json.get("sessionid").toString(), Map.class);
				MessageService message = new MessageService();
				message.setDestQ(ProjectProperties.getQueueDestination());
				
				message.setSrcQ(ServiceConsumer.EMS_SERVICE_TASK);
				message.setType("1");
				message.setLog_path(ProjectProperties.getCOMP_BASE_PATH()+json.get("src").toString());
				message.setSession(UUID.randomUUID().toString().replace("-", "") + "@" + message.getLog_path() );
				Map<String, String> paramsJson = new HashMap<>();
				paramsJson.put("unitId", equipmentUnit.getId().toString());
				paramsJson.put("unitName", equipmentUnit.getUnitName());
				paramsJson.put("itemId", commandCheckItem.getId().toString());
				paramsJson.put("itemName", commandCheckItem.getName());
				paramsJson.put("roleId", paramJson.get("roleId"));
				paramsJson.put("cmdTime", paramJson.get("cmdTime"));
				paramsJson.put("scriptTime", System.currentTimeMillis()+"");
				message.setInvariant(/*new JsonMapper().toJson(paramsJson)*/equipmentUnit.getId()+"#"+equipmentUnit.getUnitName()+"@"+commandCheckItem.getId()+
												"#"+commandCheckItem.getName() + "@" + paramJson.get("roleId") + "!" + paramJson.get("cmdTime") + "@" + System.currentTimeMillis());
				message.setMsgCode(76005);
				TextMessage txtMessage = session.createTextMessage("");
				txtMessage.setStringProperty("msgBody", new JsonMapper().toJson(message));
				txtMessage.setIntProperty("msgCode", 76005);
				txtMessage.setJMSPriority(5);
				txtMessage.setText(compress(StringUtils.isEmpty(commandCheckItem.getScript()) ?  "" : commandCheckItem.getScript()));
				logger.debug("message.getSrcQ() ={},message.getDestQ() = {},txtMessag={}",message.getSrcQ() ,message.getDestQ(),txtMessage.toString());				 
				return txtMessage;
			}
		});
	}
	public static String compress(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		String result = "";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != gzip) {
				try {
					gzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			result = out.toString("ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public void sendMessageSms(String moblie,String smscontent){
		jmsTemplate.setDefaultDestinationName(ProjectProperties.getEmsName());
    	jmsTemplate.send(new MessageCreator(){
			public Message createMessage(Session session) throws JMSException {
				MessageSms message = new MessageSms();
				message.setMobile(moblie);
				message.setSmscontent(smscontent + " " + format.format(new Date()));
				
				TextMessage txtMessage = session.createTextMessage("");
				txtMessage.setStringProperty("msgBody", new JsonMapper().toJson(message));
				txtMessage.setJMSPriority(5);
				logger.debug("message.getSrcQ() ={},message.getDestQ() = {},txtMessag={}",txtMessage.toString());				 
				return txtMessage;
			}
		});
	}
	
	
	
	@Override
	public void taskErrorSendMessage(String message){
		String list = ProjectProperties.getEmsSendMoblieList();
		if(StringUtils.isNotBlank(list)){
			String [] moblies = list.split(",");
			for (String moblie : moblies) {
				sendMessageSms(moblie, message);
			}
		}
	}
	
	

	@Override
	public List<EmsCheckJob> findJobByMap(Map<String, Object> map) {
		Map<String, SearchFilter> filter = SearchFilter.parse(map);
		Specification<EmsCheckJob> spec = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				EmsCheckJob.class);

		return emsCheckJobRepository.findAll(spec);
	}

	@Override
	public void updateJobExecTime(EmsCheckJob emsCheckJob) throws ParseException {
		emsCheckJob.setExecDate(emsCheckJob.getNextDate());
		formatDate(emsCheckJob , emsCheckJob.getJobType());
		emsCheckJobRepository.save(emsCheckJob);
	}

	private void formatDate(EmsCheckJob emsCheckJob, int type) throws ParseException {
		int [] minutes = new int[]{5,10,15,20,30,60,0,1440};
		String format = "";
		Date thisDate = emsCheckJob.getNextDate();
		switch (type) {
			case 0:case 1:case 2:case 3:case 4:case 5:case 7:
				format = EmsTask.format.format(thisDate.getTime() + (1000 * 60 * minutes[type]));
				break;
			case 6:
				format = EmsTask.format.format(thisDate.getTime() + (1000 * 60 * (60 * emsCheckJob.getHour())));
				break;
		}
		emsCheckJob.setNextDate(EmsTask.format.parse(format));
		logger.info("修改【" + emsCheckJob.getJobName() + "】的下次执行时间为：" + emsCheckJob.getNextDate());
	}


	@Override
	public List<EquipmentUnit> findUnits(Map<String,Object> map) {
		Map<String, SearchFilter> filter = SearchFilter.parse(map);
		Specification<EquipmentUnit> spec = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				EquipmentUnit.class);
		return equipmentUnitRepository.findAll(spec);
	}
	
	@Override
	public List<CommandCheckItem> findCommands(Map<String, Object> map) {
		Map<String, SearchFilter> filter = SearchFilter.parse(map);
		Specification<CommandCheckItem> spec = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				CommandCheckItem.class);
		return commandCheckItemRepository.findAll(spec);
	}
	
	@Override
	public List<EmsMonitor> findEmsMonitors(Map<String, Object> map) {
		Map<String, SearchFilter> filter = SearchFilter.parse(map);
		Specification<EmsMonitor> spec = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				EmsMonitor.class);
		return emsMonitorRepoitory.findAll(spec);
	}
	
	
	
	private List<Long> arrayToList(String [] array){
		List<Long> list = new ArrayList<Long>();
		for (String string : array) {
			if(StringUtils.isNotBlank(string)){
				try {
					list.add(Long.parseLong(string));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	@Override
	public void saveEmsResult(Map<String, Object> json,EquipmentUnit equipmentUnit,CommandCheckItem commandCheckItem,String groupId){
		Long unitId = equipmentUnit != null  ? equipmentUnit.getId() : 0;
		Long itemId = commandCheckItem != null ? commandCheckItem.getId() : 0;
		String message = json.get("msg") == null ? "" : json.get("msg").toString();
		Map<String,Object> emsmap = new HashMap<String,Object>();
		emsmap.put("monitoredUnitId_EQ", unitId+"");
		emsmap.put("monitoredCommandId_EQ", itemId+"");
		List<EmsMonitor>  ess = findEmsMonitors(emsmap);
		
		EmsMonitor em = ess.size() == 0 ? new EmsMonitor() : ess.get(0);
		em.setData(new Date(), itemId,commandCheckItem != null ? commandCheckItem.getName() : "", unitId,
						equipmentUnit != null ? equipmentUnit.getUnitName() : "", message, "3", "", message,groupId,"","","");
		saveEmsMonitor(em);
	}

	@Override
	public void saveEmsMonitor(EmsMonitor emsMonitor) {
		emsMonitorRepoitory.save(emsMonitor);
	}
	
	@Override
	public void saveEmsMonitorHistory(EmsMonitorHistory emsMonitorHistory) {
		emsMonitorHistoryRepository.save(emsMonitorHistory);
	}

	@Override
	public void noticeGroup(String groupId,String message,String unit, String item,String msg,boolean flag,String command) {
		String msgBody = msg + ":" + message;
		if(flag){
			msgBody = "清除： " + msg + " : " + command ;
		}
		if(isNotCancel(unit, item) == 0){
			if(groupId != null && !"".equals(groupId)){
				
				String [] array = groupId.split(",");
				for (String string : array) {
					if("".equals(string)){
						continue;
					}
					SystemRole role = systemRoleRepository.findOne(Long.parseLong(string));
					Collection<SystemRole> roles = new HashSet<SystemRole>();
					roles.add(role);
					List<SystemUser> userList = systemUserRepository.findUserBySystemRoleIn(roles);
					
					Map<String, String> userMap  = new HashMap<String, String>();
					for (SystemUser systemUser : userList) {
						if(systemUser.getMobile() != null && !systemUser.getMobile().equals("")){
							userMap.put(systemUser.getMobile(),msgBody);
						}
					}
					for (String key : userMap.keySet()) {
						sendMessageSms(key, userMap.get(key));
					}
				}
			}
			
		}else{
			logger.info(msg + ": 已经取消通知功能！");
		}
	}
	
	@Override
	public int isNotCancel(String unit, String item) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("mutedUnitId_EQ", unit);
		map.put("mutedCommandId_EQ", item);
		try {
			map.put("resumeTime_GT", EmsTask.format.parse(EmsTask.format.format(new Date())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Map<String, SearchFilter> filter = SearchFilter.parse(map);
		Specification<EmsMutedItem> spec = DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND,
				EmsMutedItem.class);
		return emsMutedItemRepository.findAll(spec).size();
		
	}
	
	public static String uncompress(String str) throws Exception {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(
					str.getBytes("ISO-8859-1"));
			GZIPInputStream gunzip = new GZIPInputStream(in);

			byte[] buffer = new byte[1024];
			int n;
			while ((n = gunzip.read(buffer)) != 0) {
				if (n >= 0) {
					out.write(buffer, 0, n);
				} else {
					break;
				}
			}
		} catch (IOException e) {
			logger.info(str + ": EXCEPTION");
			throw new Exception(e.getMessage());
		}
		return out.toString();
	}

	 
}
























