package com.nokia.ices.core.utils;

public class RestResult {
	private String title;
	private Object message;
	private Integer resultCode;
	public RestResult(String title, String message,Integer resultCode) {
		this.title = title;
		this.message = message;
		this.resultCode = resultCode;
	}
	public RestResult(String title, String message) {
		this.title = title;
		this.message = message;
		this.resultCode = 0;
	}
	public Object getMessage() {
		return message;
	}
	public void setMessage(Object message) {
		this.message = message;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getResultCode() {
		return resultCode;
	}
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}
	
}
