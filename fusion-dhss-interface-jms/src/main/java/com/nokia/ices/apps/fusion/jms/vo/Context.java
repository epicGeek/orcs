package com.nokia.ices.apps.fusion.jms.vo;

public class Context {
	
	
	public Context(String cmd,Integer ct,Integer rt){
		this.cmd = cmd;
		this.ct = ct == null ? 1 : ct;
		this.rt = rt == null ? 1 : rt;
	}
	
	private String cmd;
	
	private Integer ct;
	
	private Integer rt;

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Integer getCt() {
		return ct;
	}

	public void setCt(Integer ct) {
		this.ct = ct;
	}

	public Integer getRt() {
		return rt;
	}

	public void setRt(Integer rt) {
		this.rt = rt;
	}

}
