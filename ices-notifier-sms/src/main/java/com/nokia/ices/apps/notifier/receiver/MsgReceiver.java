package com.nokia.ices.apps.notifier.receiver;

import java.io.IOException;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nokia.ices.apps.notifier.domain.NotificationMessageRecord;
import com.nokia.ices.apps.notifier.service.NotificationService;

import net.sf.json.JSONObject;



@Component
public class MsgReceiver {
	
	private static final Logger logger = LoggerFactory.getLogger(MsgReceiver.class);
	@Autowired
	private NotificationService notificationService;
	
	private static ObjectMapper mapper = new ObjectMapper();

	
	@JmsListener(destination="SMS",containerFactory = "jmsContainerFactory")
	public void receiveMessage(Message receivedText) throws JsonParseException, JsonMappingException, IOException {
		TextMessage receivedTextTrans = (TextMessage)receivedText;
		try {
			String msgBody = receivedTextTrans.getStringProperty("msgBody");
			logger.info("收到消息："+msgBody);
			JSONObject jo = JSONObject.fromObject(msgBody);
			Map<String,String> map = (Map<String,String>) jo;
			logger.info("手机号："+map.get("mobile")+","+"消息内容:"+map.get("smscontent"));
			NotificationMessageRecord message = mapper.readValue(msgBody, NotificationMessageRecord.class);
			notificationService.sendMSG(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}

    }
}
