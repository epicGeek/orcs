package com.nokia.ices.apps.fusion.alarm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class IcesAlarmRule implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	private Integer alarmNo; // 告警编号

	private Integer faultId; // 故障ID

	private String alarmContent;
	
	private String alarmType;
	
	private String probableCause;

	private String alarmText;

	private String releaseVersion;
	
	@Lob
	private String alarmMeaning;

	@Lob
	private String alarmDesc;

	private String alarmName;

	private String alarmLevelVd;

	private String alarmLevel;

	private String onEquipment;

	private String onBusiness;

	private String alarmStd;

	private String handle;

	private Integer handleDelay;

	private String alarmRemark;

	private Boolean isActive;

	private String notifyImmediately;

	private Integer totalSumInterval;

	private Integer totalSumLimit;

	private Integer vipSumInterval;

	private Integer vipSumLimit;

	private Integer generalSumInterval;

	private Integer generalSumLimit;

	private String isImportant;

	private String fromRow;

	private String fromFile;
	

	public String getFromRow() {
		return fromRow;
	}

	public void setFromRow(String fromRow) {
		this.fromRow = fromRow;
	}

	public String getFromFile() {
		return fromFile;
	}

	public void setFromFile(String fromFile) {
		this.fromFile = fromFile;
	}

	public String getNotifyImmediately() {
		return notifyImmediately;
	}

	public void setNotifyImmediately(String notifyImmediately) {
		this.notifyImmediately = notifyImmediately;
	}

	public Integer getTotalSumInterval() {
		return totalSumInterval;
	}

	public void setTotalSumInterval(Integer totalSumInterval) {
		this.totalSumInterval = totalSumInterval;
	}

	public Integer getTotalSumLimit() {
		return totalSumLimit;
	}

	public void setTotalSumLimit(Integer totalSumLimit) {
		this.totalSumLimit = totalSumLimit;
	}

	public Integer getVipSumInterval() {
		return vipSumInterval;
	}

	public void setVipSumInterval(Integer vipSumInterval) {
		this.vipSumInterval = vipSumInterval;
	}

	public Integer getVipSumLimit() {
		return vipSumLimit;
	}

	public void setVipSumLimit(Integer vipSumLimit) {
		this.vipSumLimit = vipSumLimit;
	}

	public Integer getGeneralSumInterval() {
		return generalSumInterval;
	}

	public void setGeneralSumInterval(Integer generalSumInterval) {
		this.generalSumInterval = generalSumInterval;
	}

	public Integer getGeneralSumLimit() {
		return generalSumLimit;
	}

	public void setGeneralSumLimit(Integer generalSumLimit) {
		this.generalSumLimit = generalSumLimit;
	}

	public String getIsImportant() {
		return isImportant;
	}

	public void setIsImportant(String isImportant) {
		this.isImportant = isImportant;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAlarmNo() {
		return alarmNo;
	}

	public void setAlarmNo(Integer alarmNo) {
		this.alarmNo = alarmNo;
	}

	public Integer getFaultId() {
		return faultId;
	}

	public void setFaultId(Integer faultId) {
		this.faultId = faultId;
	}

	public String getAlarmContent() {
		return alarmContent;
	}

	public void setAlarmContent(String alarmContent) {
		this.alarmContent = alarmContent;
	}

	public String getAlarmDesc() {
		return alarmDesc;
	}

	public void setAlarmDesc(String alarmDesc) {
		this.alarmDesc = alarmDesc;
	}

	public String getAlarmName() {
		return alarmName;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public String getAlarmLevelVd() {
		return alarmLevelVd;
	}

	public void setAlarmLevelVd(String alarmLevelVd) {
		this.alarmLevelVd = alarmLevelVd;
	}

	public String getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public String getOnEquipment() {
		return onEquipment;
	}

	public void setOnEquipment(String onEquipment) {
		this.onEquipment = onEquipment;
	}

	public String getOnBusiness() {
		return onBusiness;
	}

	public void setOnBusiness(String onBusiness) {
		this.onBusiness = onBusiness;
	}

	public String getAlarmStd() {
		return alarmStd;
	}

	public void setAlarmStd(String alarmStd) {
		this.alarmStd = alarmStd;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public Integer getHandleDelay() {
		return handleDelay;
	}

	public void setHandleDelay(Integer handleDelay) {
		this.handleDelay = handleDelay;
	}

	public String getAlarmRemark() {
		return alarmRemark;
	}

	public void setAlarmRemark(String alarmRemark) {
		this.alarmRemark = alarmRemark;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getAlarmText() {
		return alarmText;
	}

	public void setAlarmText(String alarmText) {
		this.alarmText = alarmText;
	}

	public String getAlarmMeaning() {
		return alarmMeaning;
	}

	public void setAlarmMeaning(String alarmMeaning) {
		this.alarmMeaning = alarmMeaning;
	}

	public String getReleaseVersion() {
		return releaseVersion;
	}

	public void setReleaseVersion(String releaseVersion) {
		this.releaseVersion = releaseVersion;
	}

	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	public String getProbableCause() {
		return probableCause;
	}

	public void setProbableCause(String probableCause) {
		this.probableCause = probableCause;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
