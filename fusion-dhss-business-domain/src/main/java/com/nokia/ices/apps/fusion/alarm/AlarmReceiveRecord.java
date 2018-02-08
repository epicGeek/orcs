package com.nokia.ices.apps.fusion.alarm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
public class AlarmReceiveRecord {

	@Id
	@GeneratedValue
	private Long id;
	@Column(length=50)
	private String notifyId;
	@Column(length=4)
	private String alarmLevel;
	@Column(length=10)
	private String alarmNo;
	@Column(length=150)
	private String alarmCell;
	private String startTime;
	private String cancelTime;
	@Column(length=50)
	private String neName;
	@Column(length=150)
	private String neCode;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale="zh", timezone="GMT+8")
	private Date receiveStartTime;
	@Column(length=1024)
	private String userInfo;
	@Column(length=1024)
	private String alarmText;
	@Column(length=1024)
	private String supplInfo;
	@Column(length=1024)
	private String diagInfo;
	@Column(length=1024)
	private String objInfo;
	@Column(length=1024)
	private String alarmDesc;
	
	private String alarmType;
	
	private String dhssName;

	public String getDhssName() {
		return dhssName;
	}
	public void setDhssName(String dhssName) {
		this.dhssName = dhssName;
	}
	public String getAlarmDesc() {
		return alarmDesc;
	}
	public void setAlarmDesc(String alarmDesc) {
		this.alarmDesc = alarmDesc;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNotifyId() {
		return notifyId;
	}
	public void setNotifyId(String notifyId) {
		this.notifyId = notifyId;
	}
	public String getAlarmLevel() {
		return alarmLevel;
	}
	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}
	public String getAlarmNo() {
		return alarmNo;
	}
	public void setAlarmNo(String alarmNo) {
		this.alarmNo = alarmNo;
	}
	public String getAlarmCell() {
		return alarmCell;
	}
	public void setAlarmCell(String alarmCell) {
		this.alarmCell = alarmCell;
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
	public String getNeName() {
		return neName;
	}
	public void setNeName(String neName) {
		this.neName = neName;
	}
	public String getNeCode() {
		return neCode;
	}
	public void setNeCode(String neCode) {
		this.neCode = neCode;
	}
	public Date getReceiveStartTime() {
		return receiveStartTime;
	}
	public void setReceiveStartTime(Date receiveStartTime) {
		this.receiveStartTime = receiveStartTime;
	}
	public String getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}
	public String getAlarmText() {
		return alarmText;
	}
	public void setAlarmText(String alarmText) {
		this.alarmText = alarmText;
	}
	public String getSupplInfo() {
		return supplInfo;
	}
	public void setSupplInfo(String supplInfo) {
		this.supplInfo = supplInfo;
	}
	public String getDiagInfo() {
		return diagInfo;
	}
	public void setDiagInfo(String diagInfo) {
		this.diagInfo = diagInfo;
	}
	public String getObjInfo() {
		return objInfo;
	}
	public void setObjInfo(String objInfo) {
		this.objInfo = objInfo;
	}
	public String getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	
	
}
