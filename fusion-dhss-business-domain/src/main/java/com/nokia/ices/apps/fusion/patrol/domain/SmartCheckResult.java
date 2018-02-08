package com.nokia.ices.apps.fusion.patrol.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.rest.core.config.Projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentNeType;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;

@Entity
@Table(name = "smart_check_result")
public class SmartCheckResult {

	@Id
	@GeneratedValue
	private Long id;

	// 网元ID
	private Long neId;

	// 网元类型ID
	private Long neTypeId;

	// 网元类型名称
	private String neTypeName;

	// 网元名称
	private String neName;

	// 单元ID
	private Long unitId;

	private EquipmentNeType neType;
	

    private EquipmentUnitType unitType;

	// 单元名称
	private String unitName;

	// 检查项ID
	private Long checkItemId;

	// 检查项名称
	private String checkItemName;

	// 检查任务ID
	private Long scheduleId;

	// 巡检结果 1：失败 0：成功
	private boolean resultCode;

	// 出错信息
	@Column(length=4096)
	private String errorMessage;

	// 报文路径
	private String filePath;

	// 巡检开始时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale="zh", timezone="GMT+8")
	private Date startTime;

	// 报文结果 1：失败 0：成功
	private boolean logState;

	@ManyToOne
	private SmartCheckScheduleResult scheduleResult;

	private String logContents;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNeId() {
		return neId;
	}

	public void setNeId(Long neId) {
		this.neId = neId;
	}

	public Long getNeTypeId() {
		return neTypeId;
	}

	public void setNeTypeId(Long neTypeId) {
		this.neTypeId = neTypeId;
	}

	public String getNeTypeName() {
		return neTypeName;
	}

	public void setNeTypeName(String neTypeName) {
		this.neTypeName = neTypeName;
	}

	public String getNeName() {
		return neName;
	}

	public void setNeName(String neName) {
		this.neName = neName;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
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

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public boolean isResultCode() {
		return resultCode;
	}

	public void setResultCode(boolean resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public boolean isLogState() {
		return logState;
	}

	public void setLogState(boolean logState) {
		this.logState = logState;
	}

	public SmartCheckScheduleResult getScheduleResult() {
		return scheduleResult;
	}

	public void setScheduleResult(SmartCheckScheduleResult scheduleResult) {
		this.scheduleResult = scheduleResult;
	}

	public String getLogContents() {
		return logContents;
	}

	public void setLogContents(String logContents) {
		this.logContents = logContents;
	}
	
	public EquipmentNeType getNeType() {
        return neType;
    }

    public void setNeType(EquipmentNeType neType) {
        this.neType = neType;
    }

    public EquipmentUnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(EquipmentUnitType unitType) {
        this.unitType = unitType;
    }



    @Projection(name = "SmartCheckResultWithAssociation", types = { SmartCheckResult.class })
	public interface SmartCheckResultWithAssociation {

		Long getId();

		int getNeId();

		byte getNeTypeId();

		String getNeTypeName();

		String getNeName();

		int getUnitId();

		byte getUnitTypeId();

		String getUnitTypeName();

		String getUnitName();

		Long getCheckItemId();

		String getcheckItemName();

		Long getScheduleId();

		boolean isResultCode();

		String getErrorMessage();

		SmartCheckScheduleResult getScheduleResult();

		String getFilePath();

		Date getStartTime();

		boolean isLogState();

		String getLogContents();
	}

}
