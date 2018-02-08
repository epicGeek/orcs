package com.nokia.ices.apps.fusion.jms.vo;

/**
 * @author Mars
 * @date 2014-02-24
 *
 */

public class Message60003 extends MessageSendCommandBase {

	// 组合指令参数
	private String commandParams;

	// vip
	private String identification;

	public String getCommandParams() {
		return commandParams;
	}

	public void setCommandParams(String commandParams) {
		this.commandParams = commandParams;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

}
