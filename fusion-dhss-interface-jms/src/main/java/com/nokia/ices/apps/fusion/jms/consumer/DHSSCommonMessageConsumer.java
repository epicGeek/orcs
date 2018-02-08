package com.nokia.ices.apps.fusion.jms.consumer;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokia.ices.core.utils.JsonMapper;

public abstract class DHSSCommonMessageConsumer {

    private final static Logger logger = LoggerFactory.getLogger(DHSSCommonMessageConsumer.class);
    

    public static final String RESULT_CODE_SUCCESS = "0";
    
    
    public Map<String,String> convertMessageToMap(Message message){
        String msgBody = null;
        Integer msgCode = new Integer(0);
        try {
            msgCode = message.getIntProperty("msgCode");
            msgBody = message.getStringProperty("msgBody");
            logger.info("消息返回信息：msgCode:{},msgBody:{}", msgCode, msgBody);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        @SuppressWarnings("unchecked")
		Map<String, String> json = (Map<String, String>) new JsonMapper().fromJson(msgBody, Map.class);
        return json;
    }
    
}
