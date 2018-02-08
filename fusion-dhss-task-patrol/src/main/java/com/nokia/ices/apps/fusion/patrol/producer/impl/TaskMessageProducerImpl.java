package com.nokia.ices.apps.fusion.patrol.producer.impl;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.config.ProjectProperties;
import com.nokia.ices.apps.fusion.jms.vo.Message60003;
import com.nokia.ices.apps.fusion.patrol.producer.TaskMessageProducer;
import com.nokia.ices.core.utils.JsonMapper;


@Service("teskmessageProducer")
public class TaskMessageProducerImpl implements TaskMessageProducer {

    @Autowired
    private JmsTemplate jmsTemplate;
    private final static Logger logger = LoggerFactory.getLogger(TaskMessageProducerImpl.class);

//    private final static String srcQName = "DHLR";
    @Override
    public void sendMessage(final Map<String,Object> message) {
    	jmsTemplate.setDefaultDestinationName(ProjectProperties.getDesQName());
//    	jmsTemplate.setDefaultDestinationName("Q_LIBRA_01");
    	jmsTemplate.send(new MessageCreator(){
			public Message createMessage(Session session) throws JMSException {
				 TextMessage txtMessage = session.createTextMessage("");
	                txtMessage.setStringProperty("msgBody", new JsonMapper().toJson(message));
	                txtMessage.setIntProperty("msgCode", Integer.parseInt(message.get("msgCode").toString()));
	                txtMessage.setJMSPriority(5);
	                
	                logger.debug("message.getSrcQ() = {},message.getDestQ() = {}", message.get("msgCode"), message.get("destQ"));
	                logger.debug(txtMessage.toString());
	                return txtMessage;
			}});
    }
    
    

    
    public void sendMessage76001(final Map<String,Object> message,String uuid,String path,String script) {
    	sendMessage(message);
//    	jmsTemplate.setDefaultDestinationName(ProjectProperties.getDesQName());
//    	jmsTemplate.send(new MessageCreator(){
//			public Message createMessage(Session session) throws JMSException {
//				TextMessage txtMessage = session.createTextMessage("");
//				txtMessage.setStringProperty("msgBody", new JsonMapper().toJson(message));
//				txtMessage.setIntProperty("msgCode", 76001);
//				txtMessage.setJMSPriority(9);
//				txtMessage.setText(script);
//				logger.debug("message.getSrcQ() = {},message.getDestQ() = {}", message.get("msgCode"), message.get("destQ"));
//	            logger.debug(txtMessage.toString());				 
//				return txtMessage;
//			}
//		});
    }
    
    public void sendMessage60003(final Message60003 message) {
        getJmsTemplate().send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                // Message60003 message = new Message60003();
                // message.setCommandName(command);
                // message.setCommandParams(params);
                // message.setDestQ(desQName);
                // message.setSrcQ(srcQName);
                // message.setSession(sessionId);
                // message.setExeTimeoutMinutes(30);
                // message.setIdentification("");
                // message.setNeName(ne);
                TextMessage txtMessage = session.createTextMessage("");
                txtMessage.setStringProperty("msgBody", new JsonMapper().toJson(message));
                txtMessage.setIntProperty("msgCode",60003);
                txtMessage.setJMSPriority(9);
                logger.debug("message.getSrcQ() = {},message.getDestQ() = {}", message.getSrcQ(), message.getDestQ());
                logger.debug(txtMessage.toString());
                return txtMessage;
            }
        });
    }
    

    

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }



}
