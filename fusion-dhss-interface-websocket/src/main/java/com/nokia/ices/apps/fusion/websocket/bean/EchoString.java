package com.nokia.ices.apps.fusion.websocket.bean;

import java.util.Date;

public class EchoString {
	private Long id;
	private String text;
	private String host;
	private Date executeTime;

	public EchoString(String text) {
		this.executeTime = new Date();
		this.text = text;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Date getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}

}
