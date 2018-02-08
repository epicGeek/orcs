package com.nokia.ices.apps.fusion.patrol.producer;


import java.util.Map;

import com.nokia.ices.apps.fusion.jms.vo.Message60003;


public interface TaskMessageProducer {
    public void sendMessage(Map<String,Object> msg);
    public void sendMessage60003(Message60003 msg60003); 
}
