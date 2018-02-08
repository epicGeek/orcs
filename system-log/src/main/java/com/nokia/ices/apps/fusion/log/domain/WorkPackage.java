package com.nokia.ices.apps.fusion.log.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class WorkPackage {

	@Id
	@GeneratedValue
	private Long id;
	private String workPackage; //工作包
	@JsonIgnore
	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, optional = true)
	private WorkType workType;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWorkPackage() {
		return workPackage;
	}
	public void setWorkPackage(String workPackage) {
		this.workPackage = workPackage;
	}
	public WorkType getWorkType() {
		return workType;
	}
	public void setWorkType(WorkType workType) {
		this.workType = workType;
	}
	
	
}
