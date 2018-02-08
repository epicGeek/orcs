package com.nokia.ices.apps.fusion.patrol.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.persistence.criteria.Predicate.BooleanOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.nokia.ices.apps.fusion.jms.consumer.MessageConsumer;
import com.nokia.ices.apps.fusion.jpa.DynamicSpecifications;
import com.nokia.ices.apps.fusion.jpa.SearchFilter;
import com.nokia.ices.apps.fusion.patrol.config.SmartProjectProperties;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckJob;
import com.nokia.ices.apps.fusion.patrol.repository.SmartCheckJobRepository;
import com.nokia.ices.core.utils.JsonMapper;

@Component
public class TaskConsumer {
    
		@Autowired
		private SmartCheckJobRepository smartCheckJobRepository;
     
    
    	private final static Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    	
    	
    	@SuppressWarnings({ "unchecked", "unused" })
		@JmsListener(destination = SmartProjectProperties.smarTaskName, containerFactory = "jmsContainerFactory")
    	public void newreseiveMessage(Message message){
    		try {
    			TextMessage messages = (TextMessage) message;
    			String msgBody = message.getStringProperty("msgBody");
    			Integer msgCode = message.getIntProperty("msgCode");
    			logger.info("消息返回信息  --- ：msgCode:{},msgBody:{}", msgCode, msgBody);
    			if (null != msgBody) {
    				Map<String, String> json = (Map<String, String>) new JsonMapper().fromJson(msgBody, Map.class);
    				String taskName = json.get("taskName").toString();
    				int isSuccess = Integer.parseInt(json.get("isSuccess").toString());
        			String newMessage = json.get("message").toString();
        			Map<String,Object> map = new HashMap<String,Object>();
        			map.put("jobName_EQ", taskName);
        			SmartCheckJob job = findSmartCheckJob(map);
        			job.setIsDisable(0);
        			job.setMessage(isSuccess == 1 ? newMessage : "");
        			job.setIsSuccess(isSuccess);
        			smartCheckJobRepository.save(job);
    			}
    			
    		} catch (JMSException e) {
    			logger.info(e.getMessage());
    		}
    	}
    	
    	
    	public SmartCheckJob findSmartCheckJob(Map<String,Object> map){
    		Map<String,SearchFilter> filter = SearchFilter.parse(map);
            Specification<SmartCheckJob> spec = 
                    DynamicSpecifications.bySearchFilter(filter.values(), BooleanOperator.AND, SmartCheckJob.class);
            List<SmartCheckJob> list = smartCheckJobRepository.findAll(spec);
            return list.size() == 0 ? null : list.get(0);
    	}
    	
    
}

