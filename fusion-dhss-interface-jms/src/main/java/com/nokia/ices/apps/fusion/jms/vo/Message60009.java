package com.nokia.ices.apps.fusion.jms.vo;

/**
 * @author Mars
 * @date 2014-02-24
 *
 */
public class Message60009 extends MessageSendCommandBase {

	//组合指令指令参数
	private String commandParams;
	
	//元任务名称
	private String metataskName;
	
	//元任务参数，格式: key1::value1;;key2::value2;;key3::value3
	private String metataskParams;
	
	//解析报文超时时间，单位：分钟 有效范围：10----30

	private int parseTimeoutMinutes;

	public String getCommandParams() {
		return commandParams;
	}

	public void setCommandParams(String commandParams) {
		this.commandParams = commandParams;
	}

	public String getMetataskName() {
		return metataskName;
	}

	public void setMetataskName(String metataskName) {
		this.metataskName = metataskName;
	}

	public String getMetataskParams() {
		return metataskParams;
	}

	public void setMetataskParams(String metataskParams) {
		this.metataskParams = metataskParams;
	}

	public int getParseTimeoutMinutes() {
		return parseTimeoutMinutes;
	}

	public void setParseTimeoutMinutes(int parseTimeoutMinutes) {
		this.parseTimeoutMinutes = parseTimeoutMinutes;
	}
}
