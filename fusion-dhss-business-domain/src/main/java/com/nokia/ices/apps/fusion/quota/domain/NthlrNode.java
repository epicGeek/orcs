package com.nokia.ices.apps.fusion.quota.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class NthlrNode {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private Integer nthlrId;
	
	private String nthlrName;
	
	private Integer nthlrfeId;
	
	private String nthlrfeName;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNthlrId() {
		return nthlrId;
	}

	public void setNthlrId(Integer nthlrId) {
		this.nthlrId = nthlrId;
	}

	public String getNthlrName() {
		return nthlrName;
	}

	public void setNthlrName(String nthlrName) {
		this.nthlrName = nthlrName;
	}

	public Integer getNthlrfeId() {
		return nthlrfeId;
	}

	public void setNthlrfeId(Integer nthlrfeId) {
		this.nthlrfeId = nthlrfeId;
	}

	public String getNthlrfeName() {
		return nthlrfeName;
	}

	public void setNthlrfeName(String nthlrfeName) {
		this.nthlrfeName = nthlrfeName;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getNetype() {
		return netype;
	}

	public void setNetype(String netype) {
		this.netype = netype;
	}

	private String node;
	
	private String netype; 

}
