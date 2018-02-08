package com.nokia.ices.apps.fusion.jms.producer.impl;

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
import com.nokia.ices.apps.fusion.jms.producer.DHSSMessageProducer;
import com.nokia.ices.apps.fusion.jms.vo.Message60003;
import com.nokia.ices.core.utils.JsonMapper;


@Service("messageProducer")
public class MessageProducerImpl implements DHSSMessageProducer {

    @Autowired
    private JmsTemplate jmsTemplate;
    private final static Logger logger = LoggerFactory.getLogger(MessageProducerImpl.class);

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
    
    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }



}
