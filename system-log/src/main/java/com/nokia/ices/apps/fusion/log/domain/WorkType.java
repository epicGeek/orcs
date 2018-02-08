package com.nokia.ices.apps.fusion.log.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class WorkType {

	@Id
	@GeneratedValue
	private Long id;

	private String workType;  //工作包类型
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "workType")
	private Set<WorkPackage> items = new HashSet<WorkPackage>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public Set<WorkPackage> getItems() {
		return items;
	}

	public void setItems(Set<WorkPackage> items) {
		this.items = items;
	}
	
	
	
}
