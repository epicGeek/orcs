package com.nokia.ices.apps.fusion.jms.vo;
/**
 * @author Mars
 * @date 2014-03-18
 *
 */
public class Message60013 extends MessageBase{
	
	private String ip;
	private String port;
	private String username;
	private String password;
	//1,ssh;2,ftp;3,telnet
	private String protocal;
	private String action;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getProtocal() {
		return protocal;
	}
	public void setProtocal(String protocal) {
		this.protocal = protocal;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	
	
}
