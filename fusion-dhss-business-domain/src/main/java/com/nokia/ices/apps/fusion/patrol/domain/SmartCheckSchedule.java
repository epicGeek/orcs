package com.nokia.ices.apps.fusion.patrol.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "smart_check_schedule")
public class SmartCheckSchedule {

	@Id
	@GeneratedValue
	private Long id;

	// 方案ID
	private Long jobId;

	// 检查项ID
	private Long checkItemId;

	// 检查项名称
	private String checkItemName;

	// 适用网元类型
	private String neTypeId;

	// 创建时间
	private Date createTime;

	// 创建人ID
	private Long userId;
	
	@OneToOne
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

	public Long getCheckItemId() {
		return checkItemId;
	}

	public void setCheckItemId(Long checkItemId) {
		this.checkItemId = checkItemId;
	}

	public String getCheckItemName() {
		return checkItemName;
	}

	public void setCheckItemName(String checkItemName) {
		this.checkItemName = checkItemName;
	}

	public String getNeTypeId() {
		return neTypeId;
	}

	public void setNeTypeId(String neTypeId) {
		this.neTypeId = neTypeId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
