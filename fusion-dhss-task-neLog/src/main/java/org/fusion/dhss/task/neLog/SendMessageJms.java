package org.fusion.dhss.task.neLog;

import java.util.Map;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class SendMessageJms {
	
	private static Logger logger = Logger.getLogger(SendMessageJms.class);
	
	public static void sendMessage(final Map<String,Object> message){
		
		ActiveMQConnectionFactory connectionFactory = null;
		Connection connection = null;
		Session session = null;
		Destination destination = null;
		MessageProducer producer = null;
		String AmqUrl = SendNeLogTask.getPropertieValue("brokerURL");
	    try {
	    	connectionFactory = new ActiveMQConnectionFactory("activemqtest", "activemqtest", AmqUrl);
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, 1);
			destination = session.createQueue(message.get("destQ").toString());
			producer = session.createProducer(destination);
			producer.setDeliveryMode(1);
			TextMessage txtMessage = session.createTextMessage(message.get("destQ").toString());
			ObjectMapper mapper = new ObjectMapper();
			String JsonMgs = mapper.writeValueAsString(message);
			txtMessage.setStringProperty("msgBody", JsonMgs);
			txtMessage.setIntProperty("msgCode", Integer.parseInt(message.get("msgCode").toString()));
			txtMessage.setJMSPriority(9);
			//发送消息
			producer.send(txtMessage);
			logger.debug("message.getSrcQ() = {"+message.get("msgCode")+"},message.getDestQ() = {"+message.get("destQ")+"}");
			logger.debug(txtMessage.toString() +"----"+JsonMgs);
            
			
		} catch (JMSException e) {
			logger.debug(e.toString());
		} catch (JsonProcessingException e) {
			logger.debug(e.toString());
		}
		
	}

}
