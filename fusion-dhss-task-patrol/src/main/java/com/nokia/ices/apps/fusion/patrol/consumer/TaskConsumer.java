package com.nokia.ices.apps.fusion.patrol.consumer;

import java.text.ParseException;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.nokia.ices.apps.fusion.command.repository.CommandCheckItemRepository;
import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.jms.consumer.MessageConsumer;
import com.nokia.ices.apps.fusion.patrol.common.JsonMapper;
import com.nokia.ices.apps.fusion.patrol.common.ZipUtil;
import com.nokia.ices.apps.fusion.patrol.mml.messages.Message76001;
import com.nokia.ices.apps.fusion.patrol.service.SmartCheckTaskService;

@Component
public class TaskConsumer {
    
    
    	
    	@Autowired
    	SmartCheckTaskService smartCheckTaskService;
    	
    	@Autowired
    	CommandCheckItemRepository commandCheckItemRepository;
    	
    	@Autowired
        private JmsTemplate jmsTemplate;
    	
    
    	private final static Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    	
    	
    	@SuppressWarnings({ "unchecked", "unused" })
		@JmsListener(destination = SmartCheckTaskService.TASKNAEM, containerFactory = "jmsContainerFactory")
    	public void newreseiveMessage(Message message){
    		try {
    			TextMessage messages = (TextMessage) message;
    			String msgBody = message.getStringProperty("msgBody");
    			Integer msgCode = message.getIntProperty("msgCode");
    			logger.info("消息返回信息  --- ：msgCode:{},msgBody:{}", msgCode, msgBody);
    			if (null != msgBody) {
    				Map<String, String> json = (Map<String, String>) new JsonMapper().fromJson(msgBody, Map.class);
    				String sessionid = String.valueOf(json.get("sessionid"));
    				String resultCode = String.valueOf(json.get("flag"));
    				String flag = resultCode.equals("0") ? "2" : "3";
    				String path = String.valueOf(json.get("src"));
        			String errorMessage = json.get("msg") ;
        			Map<String, Object> map = smartCheckTaskService.getSmartCheckResultTmpByUUID(sessionid);
        			
        			if(resultCode.equals("0") && map.get("script") != null && !"".equals(map.get("script"))){
        				String zipScript = ZipUtil.compress(map.get("script").toString());
						logger.info( "解析定时巡检任务，在网元:{}::uuId:{}:::执行的巡检任务：{}", map.get("ne_name") + ":::" + map.get("unit_name"),sessionid, map.get("check_item_name"));
						jmsTemplate.setDefaultDestinationName(ProjectProperties.getQueueDestination());
				    	jmsTemplate.send(new MessageCreator(){
							public Message createMessage(Session session) throws JMSException {
								smartCheckTaskService.updateSmartCheckResultTmp(flag, sessionid ,errorMessage,path);
								Message76001 message = new Message76001();
								message.setDestQ(ProjectProperties.getQueueDestination());
								message.setSession(sessionid);
								message.setSrcQ(SmartCheckTaskService.TASKSCRIPTNAEM);
								message.setType("1");
								message.setScript_type("1");
								message.setLog_path(ProjectProperties.getCOMP_BASE_PATH()+path);
								message.setMsgCode(76005);
								TextMessage txtMessage = session.createTextMessage("");
								
								txtMessage.setStringProperty("msgBody", new JsonMapper().toJson(message));
								txtMessage.setIntProperty("msgCode", 76005);
								txtMessage.setJMSPriority(5);
								txtMessage.setText(zipScript);
								logger.debug("message.getSrcQ() ={},message.getDestQ() = {},txtMessag={}",message.getSrcQ() ,message.getDestQ(),txtMessage.toString());				 
								return txtMessage;
							}
						});
				    	
        			}else{
        				try {
        					map.put("errorMessage", errorMessage);
        					map.put("file_path", path);
        					logger.info("errorMessage：{},file_path:{}",errorMessage,path);
        					if(!resultCode.equals("0")){
        						smartCheckTaskService.updateSmartCheckResultTmp(flag, sessionid ,errorMessage,path);
        						smartCheckTaskService.updateSmartCheckScheduleResultBySession(map.get("schedule_id").toString());
        						smartCheckTaskService.saveAlarm(map);
        					}
							smartCheckTaskService.addSmartCheckResult(flag,map);
							
						} catch (ParseException e) {
							e.printStackTrace();
							logger.info(e.getMessage());
						}
        			}
        			
    			}
    			
    		} catch (JMSException e) {
    			e.printStackTrace();
    			logger.info(e.getMessage());
    		}
    	}
    	
    	
    	@SuppressWarnings("unchecked")
		@JmsListener(destination = SmartCheckTaskService.TASKSCRIPTNAEM, containerFactory = "jmsContainerFactory")
    	public void newScript(Message message){
    		
			try {
				TextMessage messages = (TextMessage) message;
				String msgBody = message.getStringProperty("msgBody");
				Integer msgCode = message.getIntProperty("msgCode");
				String luaResult = messages.getText();
				logger.info("SCRIPT返回信息  --- ：msgCode:{},msgBody:{},luaResult:{}", msgCode, msgBody,luaResult);
				if (null != msgBody) {
	    			Map<String, String> json = (Map<String, String>) new JsonMapper().fromJson(msgBody, Map.class);
	    			String sessionid = String.valueOf(json.get("session"));
	    			Map<String, Object> map = smartCheckTaskService.getSmartCheckResultTmpByUUID(sessionid);
	    			String resultCode = String.valueOf(json.get("result_code"));
	    			String type = resultCode.equals("0") ? "2" : "3";
					if(StringUtils.isNotEmpty(luaResult)){
						type = "3";
						luaResult = ZipUtil.uncompress(luaResult);
						logger.info("解析LUA结果：{}",luaResult);
						
					}
					logger.info("TYPE:{}",type);
					map.put("errorMessage", luaResult);
					logger.info("MAP:{}",map);
					if(type.equals("3")){
						logger.info("修改tmp");
						smartCheckTaskService.updateSmartCheckResultTmp(type, sessionid ,luaResult);
						logger.info("统计错误单元数");
						smartCheckTaskService.updateSmartCheckScheduleResultBySession(map.get("schedule_id").toString());
						logger.info("插入告警");
						smartCheckTaskService.saveAlarm(map);
					}
					logger.info("插入巡检结果表");
					smartCheckTaskService.addSmartCheckResult(type,map);
					
				}
				
			} catch (JMSException e) {
				e.printStackTrace();
				logger.info(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				logger.info(e.getMessage());
			}
			
    	}
    	
//    	
//    	public static void main(String[] args) {
//			String str = "2017-09-25 14:02:10.711  INFO 58817 --- [enerContainer-1] c.n.i.a.f.jms.consumer.MessageConsumer   : 解析LUA结果：lua: ...vice/script/lua/9946eceb819443cfa2dfbf17aef5a5d1.lua:52: /home/shh_dhss/adapter_soap_pgw_log/adapter_log/adapter_6808/h51f13fe02/2017/9/25/13/SSH_DHLR_COMMAND_20170925135346337520.src: No such file or directorystack traceback:	[C]: in function 'assert'	...vice/script/lua/9946eceb819443cfa2dfbf17aef5a5d1.lua:52: in function 'read_file'	...vice/script/lua/9946eceb819443cfa2dfbf17aef5a5d1.lua:60: in function 'main'	...vice/script/lua/9946eceb819443cfa2dfbf17aef5a5d1.lua:71: in main chunk	[C]: ?";
//			System.out.println(str.length());
//    	}
    	
    	
    	/*@JmsListener(destination = SmartCheckTaskService.TASKNAEM, containerFactory = "jmsContainerFactory")
    	public void reseiveMessageForSmartCheckJobTask(Message message) {
    		String msgBody = null;
    		Integer msgCode = new Integer(0);
    		String luaResult = null;
    		TextMessage messages;
    		try {
    			messages = (TextMessage) message;
    			msgBody = message.getStringProperty("msgBody");
    			msgCode = message.getIntProperty("msgCode");
    			luaResult = messages.getText();
    			logger.info("消息返回信息  --- ：msgCode:{},msgBody:{}", msgCode, msgBody);
    		} catch (JMSException e) {
    			e.printStackTrace();
    		}

    		if (null != msgBody) {
    			@SuppressWarnings("unchecked")
    			Map<String, String> json = (Map<String, String>) new JsonMapper()
    					.fromJson(msgBody, Map.class);
    			String flag = "session";
    			if(msgCode == 76000){
    				flag = "sessionid";
    			}
    			String session = String.valueOf(json.get(flag));
    			
    			String resultCode = String.valueOf(json.get("flag"));
    			String type = resultCode.equals("0") ? "2" : "3";
    			// resultCode.equalsIgnoreCase("0") 表示正常返回结果，需要保存 Log 的路径
    			
    			String log = String.valueOf(json.get("src"));
    			String path = (resultCode.equalsIgnoreCase("0") ? log : "");
    			try {
    				Map<String, Object> map = smartCheckTaskService.getSmartCheckResultTmpByUUID(session);
        			map.put("message_log", log);
        			CommandCheckItem  checkItem = commandCheckItemRepository.findOne(Long.parseLong(map.get("check_item_id").toString()));
    				if (msgCode == 76000) {
    					String errorMessage = resultCode.equals("0") ? "" : log json.get("msg") ;
    					
    					smartCheckTaskService.persist(session, resultCode, path ,map ,checkItem);
    					
    					//修改SmartCheckResultTmp表的执行状态
    					smartCheckTaskService.updateSmartCheckResultTmp(type, session ,errorMessage,path);
    					map.put("file_path", path);
    					map.put("addFlag", "");
    					if(!StringUtils.isNotEmpty(checkItem.getScript()) || type.equals("3")){
    						//添加到SmartCheckResult表
    						map.put("message_log", errorMessage);
    						map.put("file_path", path);
	    					smartCheckTaskService.addSmartCheckResult(type, session ,path,map);
	    						//修改执行状态
	    					smartCheckTaskService.updateSmartCheckScheduleResultBySession(session,"2",map);
    					}
    					
    					logger.info("Receive 76000 information ::session={},resultCode={},location={}",session,resultCode,path);
    				} else if (msgCode == 76002) {
    					
    					resultCode = String.valueOf(json.get("resultCode"));
    	    			type = resultCode.equals("0") ? "2" : "3";
    	    			
    	    			log = json.get("message") == null ? "" :String.valueOf(json.get("message")) ;
    	    			path = (resultCode.equalsIgnoreCase("0") ? log : "");
    					
    					
    					if(StringUtils.isNotEmpty(luaResult)){
    						try {
    							luaResult = ZipUtil.uncompress(luaResult);
    							logger.info("解析LUA结果：{}",luaResult);
							} catch (Exception e) {
		    					logger.info("-----------------");
							}
    						type = "3";
    					}
    					//type 2表示执行LUA成功，3表示执行LUA失败
    					//修改SmartCheckResultTmp表的执行状态
    					smartCheckTaskService.updateSmartCheckResultTmp(type, session ,luaResult);
    					logger.debug("修改resultTmp");
    					
    					smartCheckTaskService.updateSmartCheckScheduleResultBySession(session,"2",map);
    					logger.debug("修改SmartCheckScheduleResult");
//    					if(type.equals("3") || type.equals(3)){
    						map.put("message_log", luaResult);
//    					}
    						map.put("addFlag", "ok");
    					if(type.equals("3")){
    						smartCheckTaskService.saveAlarm(map);
    					}
    					//添加到SmartCheckResult表
    					smartCheckTaskService.addSmartCheckResult(type, session ,log,map);
    					logger.debug("添加SmartCheckResult");
    					logger.debug("Receive 76002 information ::session={},resultCode={},luaResult={}",session,resultCode,luaResult);
    				}
    			} catch (Exception e) {
    				logger.debug("异常信息");
    				logger.debug(e.getMessage());
    				e.printStackTrace();
    			}
    		}
    	}*/
    	
    
    
}

