package com.nokia.ices.apps.fusion.patrol.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "smart_check_schedule_result")
public class SmartCheckScheduleResult {

	@Id
	@GeneratedValue
	private Long id;

	private Long jobId;

	private String jobName;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale="zh", timezone="GMT+8")
	private Date startTime;

	private byte execFlag;

	private Integer amountUnit;

	private Integer errorUnit;

	private String jobDesc;

	private Integer amountJob;
	
	@OneToMany
	private Set<SmartCheckResult> result;

	public Set<SmartCheckResult> getResult() {
		return result;
	}

	public void setResult(Set<SmartCheckResult> result) {
		this.result = result;
	}
	
	@ManyToOne
	private SmartCheckJob smartCheckJob;

	public SmartCheckJob getSmartCheckJob() {
		return smartCheckJob;
	}

	public void setSmartCheckJob(SmartCheckJob smartCheckJob) {
		this.smartCheckJob = smartCheckJob;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public byte getExecFlag() {
		return execFlag;
	}

	public void setExecFlag(byte execFlag) {
		this.execFlag = execFlag;
	}

	public Integer getAmountUnit() {
		return amountUnit;
	}

	public void setAmountUnit(Integer amountUnit) {
		this.amountUnit = amountUnit;
	}

	public Integer getErrorUnit() {
		return errorUnit;
	}

	public void setErrorUnit(Integer errorUnit) {
		this.errorUnit = errorUnit;
	}

	public String getJobDesc() {
		return jobDesc;
	}

	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}

	public Integer getAmountJob() {
		return amountJob;
	}

	public void setAmountJob(Integer amountJob) {
		this.amountJob = amountJob;
	}

}
