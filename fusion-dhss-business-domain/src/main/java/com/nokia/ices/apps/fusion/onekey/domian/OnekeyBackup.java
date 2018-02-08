package com.nokia.ices.apps.fusion.onekey.domian;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class OnekeyBackup {
	
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * 原设计是做为站点名称，但后来集中操作入库为网元类型名称
	 */
	private String neType;
	
	/**
	 * 板卡类型名称
	 */
	private String unitType;
	
	/**
	 * 原先设计预留，但目前没有
	 */
	private String nodeId;
	
	/**
	 * 板卡名称
	 */
	private String unitName;
	
	/**
	 * 备份完成日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale="zh", timezone="GMT+8")
	private Date createDate;
	
	/**
	 * 原先设计预留，但目前没有
	 */
	private Date addDate;
	
	/**
	 * 登陆用户名
	 */
	private String userName;
	
	/**
	 * 原先设计预留，但目前没有
	 */
	private Date modifyDate;
	
	/**
	 * 原先设计预留，但目前没有
	 */
	private String modifyUser;
	
	/**
	 * 文件备份名
	 */
	private String fileDisplayName;
	/**
	 * 备份日志
	 */
	private String fileDownFolder;
	
	/**
	 * 文件备份名（与bk_file_display_name一模一样）
	 */
	private String fileUuidName;
	
	public Long getKey() {
		return getId();
	}
	
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

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getFileDisplayName() {
		return fileDisplayName;
	}

	public void setFileDisplayName(String fileDisplayName) {
		this.fileDisplayName = fileDisplayName;
	}

	public String getFileDownFolder() {
		return fileDownFolder;
	}

	public void setFileDownFolder(String fileDownFolder) {
		this.fileDownFolder = fileDownFolder;
	}

	public String getFileUuidName() {
		return fileUuidName;
	}

	public void setFileUuidName(String fileUuidName) {
		this.fileUuidName = fileUuidName;
	}

	
	
	

}
