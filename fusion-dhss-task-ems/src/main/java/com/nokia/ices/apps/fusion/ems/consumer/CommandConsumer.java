package com.nokia.ices.apps.fusion.ems.consumer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.ems.domain.EmsMonitor;
import com.nokia.ices.apps.fusion.ems.service.EmsCheckTaskJobService;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.core.utils.JsonMapper;


@Component
public class CommandConsumer {
	
	private Logger logger = LoggerFactory.getLogger(CommandConsumer.class);
	
	public static final String EMS_CMD_TASK = "EMS_CMD_TASK";
	
	@Autowired
	private EmsCheckTaskJobService emsCheckTaskJobService;
	
	@SuppressWarnings({ "unused", "unchecked" })
	@JmsListener(destination = EMS_CMD_TASK, containerFactory = "jmsContainerFactory")
	public void reseiveMessageForEmsCheckJobTask(Message message) {
		
		try {
			TextMessage messages = (TextMessage) message;
			String msgBody =  message.getStringProperty("msgBody");
			logger.info("消息返回信息  --- ：msgBody:{}", msgBody);
			Map<String, Object> json = (Map<String, Object>) new JsonMapper().fromJson(msgBody, Map.class);
			
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("unitName_EQ", json.get("ne"));
			List<EquipmentUnit> units = emsCheckTaskJobService.findUnits(map);
			map.clear();
			Map<String, Object> content = (Map<String, Object>)json.get("content");
			Map<String, String> paramsJson = new JsonMapper().fromJson(json.get("sessionid").toString(), Map.class)/*.split("@")[1].split("-")*/;
			logger.info("paramsJson  --- ：paramsJson:{}", paramsJson);
			map.put("id_EQ", paramsJson.get("itemId"));
			List<CommandCheckItem> items = emsCheckTaskJobService.findCommands(map);
			
			EquipmentUnit unit = units.size() == 0 ? null : units.get(0);
			CommandCheckItem item = items.size() == 0 ? null : items.get(0);
			
			if(json.get("flag") != null && Integer.parseInt(json.get("flag").toString()) == 0){
				logger.info("执行成功，发送serviceScript");
				emsCheckTaskJobService.sendMessageService(json,unit,item);
			}else{
				try {
					logger.info("执行异常：直接入库");
					Map<String,Object> emsmap = new HashMap<String,Object>();
					emsmap.put("monitoredUnitId_EQ", String.valueOf(unit.getId()));
					emsmap.put("monitoredCommandId_EQ", String.valueOf(item.getId()));
					
					List<EmsMonitor>  ess = emsCheckTaskJobService.findEmsMonitors(emsmap);
					EmsMonitor em = ess.size() == 0 ? new EmsMonitor() : ess.get(0); 
					
					String msg = json.get("msg") == null ? "" : json.get("msg").toString();
					
					if(!"3".equals(em.getResultLevel())){
						if(ProjectProperties.getSendErrorCode().contains(json.get("flag").toString().trim())){
							emsCheckTaskJobService.noticeGroup(paramsJson.get("roleId"), msg,String.valueOf(unit.getId()), String.valueOf(item.getId()), unit.getUnitName(), false, "");
						}
					}
					 
					em.setData(new Date(), item.getId(),item.getName(), unit.getId(),
							unit.getUnitName(), msg, "3", "",msg ,paramsJson.get("roleId"),paramsJson.get("cmdTime"),"",String.valueOf(System.currentTimeMillis()));
					em.setErrorCode(json.get("flag").toString().trim());
					emsCheckTaskJobService.saveEmsMonitor(em);
				} catch (Exception e) {
					logger.info(e.getMessage() + ":commandSendErrorMessage");
					logger.info("commandSendErrorMessage:{}",message);
					e.printStackTrace();
					emsCheckTaskJobService.taskErrorSendMessage("EMS入库异常:" + e.getMessage());
				}
			}
			
		} catch (JMSException e) {
			logger.info(e.getMessage() + ":commandSendErrorMessage");
			logger.info("commandSendErrorMessage:{}",message);
			e.printStackTrace();
			emsCheckTaskJobService.taskErrorSendMessage("EMS解析集中操作消息异常：" + e.getMessage());
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
