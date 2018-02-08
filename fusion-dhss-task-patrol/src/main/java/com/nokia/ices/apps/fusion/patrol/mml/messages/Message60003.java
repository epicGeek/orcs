package com.nokia.ices.apps.fusion.patrol.mml.messages;

/**
 * @author Mars
 * @date 2014-02-24
 * 
 */

public class Message60003 extends MessageBase {

	// 组合指令参数
	private String commandParams;
	// 网元名称
	private String neName;
	// vip
	private String identification;
	// 指令超时时间，单位：分钟 有效范围：10----60
	private int exeTimeoutMinutes;
	// 指令名称
	private String commandName;

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

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

	public String getNeName() {
		return neName;
	}

	public void setNeName(String neName) {
		this.neName = neName;
	}

	public int getExeTimeoutMinutes() {
		return exeTimeoutMinutes;
	}

	public void setExeTimeoutMinutes(int exeTimeoutMinutes) {
		this.exeTimeoutMinutes = exeTimeoutMinutes;
	}
}
