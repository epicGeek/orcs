package com.nokia.ices.apps.fusion.patrol.service;

/**
 * JMS生产者
 * 
 * @author kongdy
 * 
 */
public interface ProductService {
	public void sendMessage60003(String sessionId, String ne, String unitName,String command,
			String params); 
	public void sendMessage76001(String sessionId,String type, String logPath, String luaZip); 
}