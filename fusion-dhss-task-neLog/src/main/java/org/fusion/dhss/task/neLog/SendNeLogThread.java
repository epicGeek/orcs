package org.fusion.dhss.task.neLog;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * 网元操作日志发送名称线程
 */
public class SendNeLogThread implements Runnable {

	

	private String unitName;
	
	private String commandParams;
	
	private String scriptName;
	
	

	public SendNeLogThread(String scriptName,String unitName,String commandParams) {
		this.scriptName =scriptName;
		this.unitName = unitName;
		this.commandParams = commandParams;
	}

	 
	@Override
	public void run() {
			// 向集中操作维护平台AMQ队列下发命令
			String sessionId = UUID.randomUUID().toString().replaceAll("[-]", "");
			 Map<String,Object> mq_map = new HashMap<String,Object>();
			 String srQName = SendNeLogTask.getPropertieValue("dhss.srQName");
             String desQName = SendNeLogTask.getPropertieValue("dhss.desQName");
			 mq_map.put("srcQ",  srQName);
			 mq_map.put("destQ", desQName);
			 mq_map.put("session", sessionId);
			 mq_map.put("commandName", scriptName);
			 mq_map.put("commandParams", commandParams);
			 mq_map.put("neName",unitName);
			 mq_map.put("exeTimeoutMinutes", 30);
			 mq_map.put("identification", "");
			 mq_map.put("eqType", unitName);
			 mq_map.put("msgCode", 60003);		 
			 SendMessageJms.sendMessage(mq_map);
			
	}
}
