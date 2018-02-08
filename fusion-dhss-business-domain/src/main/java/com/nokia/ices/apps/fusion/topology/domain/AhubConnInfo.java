package com.nokia.ices.apps.fusion.topology.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AhubConnInfo {
	@Id
	@GeneratedValue
	private Long id;
	
	private String selfPort;
	private String targetMode;
	private String targetLan;
	private String vlanId;
	private String ipAddress;
	private String targetEquipment;
	private String targetPort;
	private String upLinkIpAddress;
	private String ahubName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSelfPort() {
		return selfPort;
	}
	public void setSelfPort(String selfPort) {
		this.selfPort = selfPort;
	}
	public String getTargetMode() {
		return targetMode;
	}
	public void setTargetMode(String targetMode) {
		this.targetMode = targetMode;
	}
	public String getTargetLan() {
		return targetLan;
	}
	public void setTargetLan(String targetLan) {
		this.targetLan = targetLan;
	}
	public String getVlanId() {
		return vlanId;
	}
	public void setVlanId(String vlanId) {
		this.vlanId = vlanId;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getTargetEquipment() {
		return targetEquipment;
	}
	public void setTargetEquipment(String targetEquipment) {
		this.targetEquipment = targetEquipment;
	}
	public String getTargetPort() {
		return targetPort;
	}
	public void setTargetPort(String targetPort) {
		this.targetPort = targetPort;
	}
	public String getUpLinkIpAddress() {
		return upLinkIpAddress;
	}
	public void setUpLinkIpAddress(String upLinkIpAddress) {
		this.upLinkIpAddress = upLinkIpAddress;
	}
	public String getAhubName() {
		return ahubName;
	}
	public void setAhubName(String ahubName) {
		this.ahubName = ahubName;
	}
	
	
}
