package com.nokia.ices.apps.fusion.patrol.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nokia.ices.apps.fusion.command.domain.CommandCheckItem;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.system.domain.AuditableEntity;

@Entity
public class SmartCheckJob extends AuditableEntity {

	@Id
	@GeneratedValue
	private Long id;

	// 方案名称
	private String jobName;

	// 方案描述
	@Column(length = 1024)
	private String jobDesc;

	// 创建时间
	private Date createTime;

	// 创建人ID
	private Long userId;

	// 开始时间(每月、每周几号)
	@JsonFormat(pattern="yyyy-MM-dd", locale="zh", timezone="GMT+8")
	private Date execDay;

	// 开始时间(小时:分钟)
	private String execTime;

	// 下次运行时间(每月、每周几号几点几分)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale="zh", timezone="GMT+8")
	private Date nextDay;

	// 执行粒度 1:每天执行 2:每周执行 3:每月执行
	private int jobType;

	// 执行状态 0：未启用 1:已启用 2:已停止
	private int execFlag;

	private String loopHour;
	
	private int isDisable;
	
	private int isSuccess;
	
	private String message;

	@ManyToMany
	private Set<CommandCheckItem> checkItem;
	
	
	@OneToOne
	private SmartCheckSchedule smartCheckSchedule;
	

    public SmartCheckSchedule getSmartCheckSchedule() {
		return smartCheckSchedule;
	}

	public void setSmartCheckSchedule(SmartCheckSchedule smartCheckSchedule) {
		this.smartCheckSchedule = smartCheckSchedule;
	}
	
	@OneToMany
	private Set<SmartCheckScheduleResult> SmartCheckScheduleResult;

	public Set<SmartCheckScheduleResult> getSmartCheckScheduleResult() {
		return SmartCheckScheduleResult;
	}

	public void setSmartCheckScheduleResult(
			Set<SmartCheckScheduleResult> smartCheckScheduleResult) {
		SmartCheckScheduleResult = smartCheckScheduleResult;
	}

	@ManyToMany
    private Set<EquipmentUnit> unit;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobDesc() {
		return jobDesc;
	}

	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
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

	public Date getExecDay() {
		return execDay;
	}

	public void setExecDay(Date execDay) {
		this.execDay = execDay;
	}

	public String getExecTime() {
		return execTime;
	}

	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}

	public Date getNextDay() {
		return nextDay;
	}

	public void setNextDay(Date nextDay) {
		this.nextDay = nextDay;
	}

	public int getJobType() {
		return jobType;
	}

	public void setJobType(int jobType) {
		this.jobType = jobType;
	}

	public int getExecFlag() {
		return execFlag;
	}

	public void setExecFlag(int execFlag) {
		this.execFlag = execFlag;
	}

	public String getLoopHour() {
		return loopHour;
	}

	public void setLoopHour(String loopHour) {
		this.loopHour = loopHour;
	}

    public Set<CommandCheckItem> getCheckItem() {
        return checkItem;
    }

    public void setCheckItem(Set<CommandCheckItem> checkItem) {
        this.checkItem = checkItem;
    }

    public Set<EquipmentUnit> getUnit() {
        return unit;
    }

    public void setUnit(Set<EquipmentUnit> unit) {
        this.unit = unit;
    }

	public int getIsDisable() {
		return isDisable;
	}

	public void setIsDisable(int isDisable) {
		this.isDisable = isDisable;
	}

	public int getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(int isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
