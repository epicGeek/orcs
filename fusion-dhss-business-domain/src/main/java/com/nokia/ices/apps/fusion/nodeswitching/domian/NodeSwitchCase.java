package com.nokia.ices.apps.fusion.nodeswitching.domian;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class NodeSwitchCase {
	
	@Id
	@GeneratedValue
	private Long id;
	private String netype;
	private String casename;
	private String detailed;
	
	public Long getCaseId() {
		return getId();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNetype() {
		return netype;
	}
	public void setNetype(String netype) {
		this.netype = netype;
	}
	public String getCasename() {
		return casename;
	}
	public void setCasename(String casename) {
		this.casename = casename;
	}
	public String getDetailed() {
		return detailed;
	}
	public void setDetailed(String detailed) {
		this.detailed = detailed;
	}
	

}
