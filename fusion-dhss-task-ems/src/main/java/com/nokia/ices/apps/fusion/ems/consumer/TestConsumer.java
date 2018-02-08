/*package com.nokia.ices.apps.fusion.ems.consumer;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.core.utils.JsonMapper;

@Component
public class TestConsumer {
	
	private Logger logger = LoggerFactory.getLogger(TestConsumer.class);
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	@SuppressWarnings({ "unchecked", "unused" })
	@JmsListener(destination = "TEST", containerFactory = "jmsContainerFactory")
	public void test(Message message){
		logger.info("收到信息："+message);
		TextMessage messages = (TextMessage) message;
		String msgBody;
		try {
			msgBody = message.getStringProperty("msgBody");
			logger.info("消息返回信息CMD  --- ：msgBody:{}", msgBody);
			Map<String, String> json = (Map<String, String>) new JsonMapper().fromJson(msgBody, Map.class);
			Map<String, Object> mq_map = new HashMap<String, Object>();
			
			mq_map.put("flag", "0");
			mq_map.put("src","E:/lua/37.txt");
			mq_map.put("ne", json.get("ne"));
			mq_map.put("content", json.get("content"));
			mq_map.put("sessionid", json.get("sessionid"));
			mq_map.put("msg", "login timeout");
			jmsTemplate.setDefaultDestinationName("EMS_CMD_TASK");
 	        jmsTemplate.send(new MessageCreator(){
				public Message createMessage(Session session) throws JMSException {
					TextMessage txtMessage = session.createTextMessage("");
					try {
						
		                txtMessage.setStringProperty("msgBody", new JsonMapper().toJson(mq_map));
		                txtMessage.setIntProperty("msgCode", message.getIntProperty("msgCode"));
		                txtMessage.setJMSPriority(5);
		                logger.debug("TEST {},message.getDestQ() = {}", json.get("srcQ"), json.get("destQ"));
		                logger.debug(txtMessage.toString());
		                
					} catch (Exception e) {
						e.printStackTrace();
						logger.info(e.getMessage());
					}
					return txtMessage;
				}
			});
		} catch (JMSException e) {
			logger.info(e.getMessage());
		}
		
	}
}
*/