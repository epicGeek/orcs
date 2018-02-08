package com.nokia.ices.apps.fusion.ems.consumer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.nokia.ices.apps.fusion.ems.domain.EmsMonitor;
import com.nokia.ices.apps.fusion.ems.domain.EmsMonitorHistory;
import com.nokia.ices.apps.fusion.ems.service.EmsCheckTaskJobService;
import com.nokia.ices.apps.fusion.ems.service.impl.EmsCheckTaskJobServiceImpl;
import com.nokia.ices.core.utils.JsonMapper;



@Component
public class ServiceConsumer {
	
	private Logger logger = LoggerFactory.getLogger(ServiceConsumer.class);
	
	@Autowired
	EmsCheckTaskJobService emsCheckTaskJobService;
	
	
	
	public static final String EMS_SERVICE_TASK = "EMS_SERVICE_TASK";
	
	
	
	@SuppressWarnings("unchecked")
	@JmsListener(destination = EMS_SERVICE_TASK, containerFactory = "jmsContainerFactory")
	public void reseiveMessageForSmartCheckJobTask(Message message) {
		
		
		try {
			TextMessage messages = (TextMessage) message;
			String msgBody = message.getStringProperty("msgBody");
			logger.info("msgBody  --- ：msgBody:{}", msgBody);
			String luaResult = messages.getText();
			Map<String, String> map = new HashMap<String,String>();
 			if(luaResult != null && !("").equals(luaResult)){
 				logger.info("luaResult  --- ：luaResult:{}", luaResult);
				luaResult = EmsCheckTaskJobServiceImpl.uncompress(luaResult);
				map = (Map<String, String>) new JsonMapper().fromJson(luaResult, Map.class);
			}
 			Map<String, String> json = (Map<String, String>) new JsonMapper().fromJson(msgBody, Map.class);
 			String [] sessionParam = (json.get("session") == null ? "" : json.get("session")).split("@");
 			String log_path = sessionParam[sessionParam.length - 1];
			logger.info("SERVICE消息返回信息  --- ：msgBody:{},log_path:{}", msgBody,log_path);
			logger.info("luaResult  --- ：luaResultMap:{}", map);
//			Map<String, String> paramsjson = (Map<String, String>) new JsonMapper().fromJson(msgBody, Map.class);
			String string  = json.get("invariant");
			String[] params = string.split("!");
			String [] jsonArray = params[0].split("@");
			String [] unitParam = jsonArray[0].split("#");
			String [] itemParam = jsonArray[1].split("#");
			
			String [] dates = params[1].split("@");
			String sendCmdDate = dates[0];
			String sendScriptDate = dates[1];
			String endDate = String.valueOf(System.currentTimeMillis());
			
			
			Map<String,Object> emsmap = new HashMap<String,Object>();
			emsmap.put("monitoredUnitId_EQ", unitParam[0]);
			emsmap.put("monitoredCommandId_EQ", itemParam[0]);
			List<EmsMonitor>  ess = emsCheckTaskJobService.findEmsMonitors(emsmap);
			
			
			String historyFlag = "";
			boolean isNoticeFlag = true;
			boolean flag = false;
			String msg = "";
			EmsMonitor em = ess.size() == 0 ? new EmsMonitor() : ess.get(0);
			if(em != null){
				if(em.getResultLevel() != null && !"0".equals(em.getResultLevel()) 
						&& map.get("level") != null && "0".equals(map.get("level")) ){
					em.setCancelTime(new Date());
					flag = true;
					msg = em.getNotificationContent();
				}
				if(em.getResultLevel() != null && !"".equals(map.get("level"))){
					int a = Integer.parseInt(em.getResultLevel());
					int b = Integer.parseInt(map.get("level"));
					historyFlag = a != 0 && b != 0 ? "（ALARM_REMIND）" : historyFlag;
					if(a == 3 && b == 0){
						isNoticeFlag = false;
					}else{
						if(a!=0 && b != 0 && a>=b){
							isNoticeFlag = false;
							historyFlag = "（ALARM_REMIND）";
						}
					} 
				}
				
			}
			em.setData(new Date(),Long.parseLong(itemParam[0]),itemParam[1],Long.parseLong(unitParam[0]),unitParam[1],
					map.get("content"),map.get("level"),/*json.get("log_path")*/log_path,historyFlag+map.get("value"),jsonArray[2],sendCmdDate,sendScriptDate,endDate);
			em.setErrorCode("0");
			emsCheckTaskJobService.saveEmsMonitor(em);
			
			
			EmsMonitorHistory emh = new EmsMonitorHistory();
			emh.setData(new Date(),Long.parseLong(itemParam[0]),itemParam[1],Long.parseLong(unitParam[0]),unitParam[1],
					map.get("content"),map.get("level"),/*json.get("log_path")*/log_path,historyFlag+map.get("value"),jsonArray[2],sendCmdDate,sendScriptDate,endDate);
			emsCheckTaskJobService.saveEmsMonitorHistory(emh);
			
			if(isNoticeFlag){
				if(flag || (map.get("level") != null && !map.get("level").equals("0"))){
					emsCheckTaskJobService.noticeGroup(jsonArray[2], map.get("content"),unitParam[0],itemParam[0],unitParam[1],flag,msg);
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage() + ":scriptSendErrorMessage");
			logger.info("scriptSendErrorMessage:{}",message);
			e.printStackTrace();
			emsCheckTaskJobService.taskErrorSendMessage("EMS入库异常" + e.getMessage());
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
