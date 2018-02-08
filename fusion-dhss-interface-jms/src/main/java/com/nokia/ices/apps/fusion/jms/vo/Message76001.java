package com.nokia.ices.apps.fusion.jms.vo;


/**
 * @author ls
 * @date 2015-07-28
 *
 */

public class Message76001 extends MessageBase {
 
	
	private String type;//类型
	

	private String log_path;//日志路径
	
	private String resultCode;//LUA执行结果 0：成功 1：失败	 

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLog_path() {
		return log_path;
	}

	public void setLog_path(String log_path) {
		this.log_path = log_path;
	}
	public String getResultCode() {
		return resultCode;
	}

	public void setResult_code(String resultCode) {
		this.resultCode = resultCode;
	}
 
}
