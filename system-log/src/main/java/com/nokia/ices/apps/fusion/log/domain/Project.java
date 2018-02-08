package com.nokia.ices.apps.fusion.log.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Project {
	
	@Id
	@GeneratedValue
	private Long id;
	private String projectName; //项目名称
	private String cbt;
	private Integer orderby;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getCbt() {
		return cbt;
	}
	public void setCbt(String cbt) {
		this.cbt = cbt;
	}
	public Integer getOrderby() {
		return orderby;
	}
	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}
	
}
