package com.nokia.ices.apps.fusion.onekey.domian;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class OnekeyBackupMinitor {
	
	@Id
	@GeneratedValue
	private Long id;
	//网元类型名称
	private String neType;
	
	//板卡类型
		private String unitType;
		//板卡名称
		private String unitName;
		//下发时间
		private String command; 
		//开始时间
		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale="zh", timezone="GMT+8")
		private Date startTime;
		//结束时间（暂时没有用到）
		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale="zh", timezone="GMT+8")
		private Date endTime;
		//目前为唯一标识用 格式为(网元类型名称_板卡类型名称_板卡名称)
		private String resultDir;
		//返回结果状态码
		private String resultCode;
		//是否完成（1:未完成，0：完成） 注：这个字段只代表是否完成指令下发并等待。不代表执行是否成功。未完成也可以看做为执行
		private String isFulish;
		//执行人，当前登录名称
		private String systemUser; 
		//执行命令步骤
		private String stepId;
		//返回结果信息
		private String errorMsg;
		//是否在执行中（0:执行中，1:不在执行中）
		private Integer executing; 
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNeType() {
		return neType;
	}
	public void setNeType(String neType) {
		this.neType = neType;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getResultDir() {
		return resultDir;
	}
	public void setResultDir(String resultDir) {
		this.resultDir = resultDir;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getIsFulish() {
		return isFulish;
	}
	public void setIsFulish(String isFulish) {
		this.isFulish = isFulish;
	}
	public String getSystemUser() {
		return systemUser;
	}
	public void setSystemUser(String systemUser) {
		this.systemUser = systemUser;
	}
	public String getStepId() {
		return stepId;
	}
	public void setStepId(String stepId) {
		this.stepId = stepId;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Integer getExecuting() {
		return executing;
	}
	public void setExecuting(Integer executing) {
		this.executing = executing;
	}
	

}
