package com.nokia.ices.apps.fusion.ems.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class EmsMutedItem {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private Long mutedUnitId;
	
	private Long mutedCommandId;
	
	private Date resumeTime;
	
	private Date stopTime;
	
	private String opUser;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMutedUnitId() {
		return mutedUnitId;
	}

	public void setMutedUnitId(Long mutedUnitId) {
		this.mutedUnitId = mutedUnitId;
	}

	public Long getMutedCommandId() {
		return mutedCommandId;
	}

	public void setMutedCommandId(Long mutedCommandId) {
		this.mutedCommandId = mutedCommandId;
	}

	public Date getResumeTime() {
		return resumeTime;
	}

	public void setResumeTime(Date resumeTime) {
		this.resumeTime = resumeTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public String getOpUser() {
		return opUser;
	}

	public void setOpUser(String opUser) {
		this.opUser = opUser;
	}

}
