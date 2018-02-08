package com.nokia.ices.apps.fusion.topology.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CsNeRela {
	
	
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String csCode;
	
	private String parent;
	
	private String location;
	
	
	private String ne;
	
	private String unit;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCsCode() {
		return csCode;
	}

	public void setCsCode(String csCode) {
		this.csCode = csCode;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNe() {
		return ne;
	}

	public void setNe(String ne) {
		this.ne = ne;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
