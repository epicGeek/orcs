package com.nokia.ices.apps.fusion.boss.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@SuppressWarnings("serial")
@Entity
public class BusinessType implements Serializable{
	
	@Id
    @GeneratedValue
    private Long id;
	private String businessName;
	private String businessDes;
	private String businessNameEn;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getBusinessDes() {
		return businessDes;
	}
	public void setBusinessDes(String businessDes) {
		this.businessDes = businessDes;
	}
	public String getBusinessNameEn() {
		return businessNameEn;
	}
	public void setBusinessNameEn(String businessNameEn) {
		this.businessNameEn = businessNameEn;
	}
	
	

}
