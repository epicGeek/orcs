package com.nokia.ices.apps.fusion.volte.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class VolteMessage {
	
	
	
	
	@Id
	@GeneratedValue
	private Long id;
	private String imsi;
	private String msisdn;
	/**
	 * 状态 0成功 1失败
	 */
	private String actionStatus;
	//@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss SSS", locale="zh", timezone="GMT+8")
	private String startTime;
	/**
	 * 耗时
	 */
	private String costTime;
	/**
	 * 数据流向 hss2boss 或者 boss2hss
	 */
	private String flowDirection;
	private String neName;
	/**
	 * 1:yes 0:no
	 */
	private String autoProv;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getStatus() {
		return actionStatus;
	}
	public void setStatus(String status) {
		this.actionStatus = status;
	}
	
	public String getActionStatus() {
		return actionStatus;
	}
	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getCostTime() {
		return costTime;
	}
	public void setCostTime(String costTime) {
		this.costTime = costTime;
	}
	public String getFlowDirection() {
		return flowDirection;
	}
	public void setFlowDirection(String flowDirection) {
		this.flowDirection = flowDirection;
	}
	public String getNeName() {
		return neName;
	}
	public void setNeName(String neName) {
		this.neName = neName;
	}
	public String getAutoProv() {
		return autoProv;
	}
	public void setAutoProv(String autoProv) {
		this.autoProv = autoProv;
	}
	
}
