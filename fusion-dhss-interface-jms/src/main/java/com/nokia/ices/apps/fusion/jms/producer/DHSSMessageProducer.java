package com.nokia.ices.apps.fusion.jms.producer;


import java.util.Map;

import com.nokia.ices.apps.fusion.jms.vo.Message60003;


public interface DHSSMessageProducer {
    public void sendMessage(Map<String,Object> msg);
 	/*
    public void sendMessage(Map<String,Object> msg);
    /*
    public void sendMessage60003(Message60003 msg60003);	
>>>>>>> .r11123
	public void sendMessage60005(Message60005 msg60005);
	public void sendMessage60013(Message60013 msg60013);	
	public void sendMessage60009(Message60009 msg60013);*/
}
