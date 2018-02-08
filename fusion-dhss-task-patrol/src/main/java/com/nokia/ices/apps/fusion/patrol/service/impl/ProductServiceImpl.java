package com.nokia.ices.apps.fusion.patrol.service.impl;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.nokia.ices.apps.fusion.patrol.mml.messages.Message60003;
import com.nokia.ices.apps.fusion.patrol.mml.messages.Message76001;
import com.nokia.ices.apps.fusion.patrol.service.ProductService;
import com.nokia.ices.core.utils.JsonMapper;


@Service("productService")
public class ProductServiceImpl implements ProductService {
//	@Autowired
//	SmartCheckJobService smartCheckJobService;
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	private JmsTemplate jmsTemplate;
	private ActiveMQQueue queueSource;
	private ActiveMQQueue queueDestination;

	@PostConstruct
	public void initRouteMap() {}

	@Override
	public void sendMessage60003(final String sessionId, final String ne, final String unitName, final String command,
			final String params) {
		getJmsTemplate().send(getQueueDestination(), new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				Message60003 message = new Message60003();
				message.setCommandName(command);
				message.setCommandParams(params);
				message.setDestQ(getQueueDestination().getQueueName());
				message.setExeTimeoutMinutes(30);
				message.setIdentification("");
				message.setNeName(unitName);
				message.setSession(sessionId);
				message.setSrcQ(getQueueSource().getQueueName());

				TextMessage txtMessage = session.createTextMessage("");
				txtMessage.setStringProperty("msgBody", new JsonMapper().toJson(message));
				txtMessage.setIntProperty("msgCode", 60003);
				txtMessage.setJMSPriority(9);
				logger.debug("message.getSrcQ() ={},message.getDestQ() = {},txtMessag={}",message.getSrcQ() ,message.getDestQ(),txtMessage.toString());
				return txtMessage;
			}
		});

	}

	@Override
	public void sendMessage76001(final String sessionId, final String type, final String logPath, final String luaZip) {
		getJmsTemplate().send(getQueueDestination(), new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				Message76001 message = new Message76001();
				message.setDestQ(getQueueDestination().getQueueName());
				message.setSession(sessionId);
				message.setSrcQ(getQueueSource().getQueueName());
				message.setType(type);
				message.setLog_path(logPath);

				TextMessage txtMessage = session.createTextMessage("");
				txtMessage.setStringProperty("msgBody", new JsonMapper().toJson(message));
				txtMessage.setIntProperty("msgCode", 76001);
				txtMessage.setJMSPriority(9);
				txtMessage.setText(luaZip);
				logger.debug("message.getSrcQ() ={},message.getDestQ() = {},txtMessag={}",message.getSrcQ() ,message.getDestQ(),txtMessage.toString());				 
				return txtMessage;
			}
		});

	}

	public void setQueueDestination(ActiveMQQueue queueDestination) {
		this.queueDestination = queueDestination;
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}


	public ActiveMQQueue getQueueDestination() {
		return queueDestination;
	}

	

	public ActiveMQQueue getQueueSource() {
		return queueSource;
	}

	public void setQueueSource(ActiveMQQueue queueSource) {
		this.queueSource = queueSource;
	}

	

}
