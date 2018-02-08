package com.nokia.ices.apps.fusion.alarm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;


@Entity
public class AlarmMonitor {

	@Id
	@GeneratedValue
	private Long id;
	@Column(length=4096)
	private String alarmContent;
	private String alarmLevel;
	private String alarmScene;
	private String alarmTitle;
	private String alarmType;
	private String belongSite;
	private String neName;
	private String neType;
	private String startTime;
	private String filePath;
	private String alarmLimit;
	private String cancelTime;
	private String unitName;
	private String unitType;
	/**
	 * KPI 专用
	 * 
	 */
	private String kpiCode;
	private String kpiComparedMethod;
	
	@Transient
	private Date searchStartTime;
	
	@Transient
	private Date searchEndTime;
	
	
	public String getKpiComparedMethod() {
		return kpiComparedMethod;
	}

	public void setKpiComparedMethod(String kpiComparedMethod) {
		this.kpiComparedMethod = kpiComparedMethod;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAlarmContent() {
		return alarmContent;
	}

	public void setAlarmContent(String alarmContent) {
		this.alarmContent = alarmContent;
	}

	public String getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public String getAlarmScene() {
		return alarmScene;
	}

	public void setAlarmScene(String alarmScene) {
		this.alarmScene = alarmScene;
	}

	public String getAlarmTitle() {
		return alarmTitle;
	}

	public void setAlarmTitle(String alarmTitle) {
		this.alarmTitle = alarmTitle;
	}

	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	public String getBelongSite() {
		return belongSite;
	}

	public void setBelongSite(String belongSite) {
		this.belongSite = belongSite;
	}

	public String getNeName() {
		return neName;
	}

	public void setNeName(String neName) {
		this.neName = neName;
	}

	public String getNeType() {
		return neType;
	}

	public void setNeType(String neType) {
		this.neType = neType;
	}



	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getAlarmLimit() {
		return alarmLimit;
	}

	public void setAlarmLimit(String alarmLimit) {
		this.alarmLimit = alarmLimit;
	}



	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public Date getSearchStartTime() {
		return searchStartTime;
	}

	public void setSearchStartTime(Date searchStartTime) {
		this.searchStartTime = searchStartTime;
	}

	public Date getSearchEndTime() {
		return searchEndTime;
	}

	public void setSearchEndTime(Date searchEndTime) {
		this.searchEndTime = searchEndTime;
	}

	public String getKpiCode() {
		return kpiCode;
	}

	public void setKpiCode(String kpiCode) {
		this.kpiCode = kpiCode;
	}

	
}
