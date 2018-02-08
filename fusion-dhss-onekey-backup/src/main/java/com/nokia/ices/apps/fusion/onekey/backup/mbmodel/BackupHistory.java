package com.nokia.ices.apps.fusion.onekey.backup.mbmodel;

import java.io.Serializable;
import java.util.Date;

/**
 * 备份数据历史记录查询对应的结果
 * User: stev.zhang
 * Date: 2015/5/24
 * Time: 10:50
 */
public class BackupHistory implements Serializable {
    private String id;
    private String bkSiteId;
    private String bkSiteName;
    private String bkSiteTypeId;
    private String bkSiteTypeName;
    private String bkNodeId;
    private String bkNodeName;
    private Date bkDate; //备份时间
    private Date bkAddDate;
    private String bkAddWho;
    private String bkAddWhoName;
    private Date bkModifyDate;
    private String bkModifyWho;
    private String bkFileNameUuid;
    private String bkFileDisplayName; //下载文件输出名字
    private String bkFileDownFolder; //下载文件存储的目录



    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBkSiteId() {
        return bkSiteId;
    }

    public void setBkSiteId(String bkSiteId) {
        this.bkSiteId = bkSiteId;
    }

    public String getBkSiteName() {
        return bkSiteName;
    }

    public void setBkSiteName(String bkSiteName) {
        this.bkSiteName = bkSiteName;
    }

    public String getBkSiteTypeId() {
        return bkSiteTypeId;
    }

    public void setBkSiteTypeId(String bkSiteTypeId) {
        this.bkSiteTypeId = bkSiteTypeId;
    }

    public String getBkSiteTypeName() {
        return bkSiteTypeName;
    }

    public void setBkSiteTypeName(String bkSiteTypeName) {
        this.bkSiteTypeName = bkSiteTypeName;
    }

    public String getBkNodeId() {
        return bkNodeId;
    }

    public void setBkNodeId(String bkNodeId) {
        this.bkNodeId = bkNodeId;
    }

    public String getBkNodeName() {
        return bkNodeName;
    }

    public void setBkNodeName(String bkNodeName) {
        this.bkNodeName = bkNodeName;
    }

    public Date getBkDate() {
        return bkDate;
    }

    public void setBkDate(Date bkDate) {
        this.bkDate = bkDate;
    }

    public Date getBkAddDate() {
        return bkAddDate;
    }

    public void setBkAddDate(Date bkAddDate) {
        this.bkAddDate = bkAddDate;
    }

    public String getBkAddWho() {
        return bkAddWho;
    }

    public void setBkAddWho(String bkAddWho) {
        this.bkAddWho = bkAddWho;
    }

    public Date getBkModifyDate() {
        return bkModifyDate;
    }

    public void setBkModifyDate(Date bkModifyDate) {
        this.bkModifyDate = bkModifyDate;
    }

    public String getBkModifyWho() {
        return bkModifyWho;
    }

    public void setBkModifyWho(String bkModifyWho) {
        this.bkModifyWho = bkModifyWho;
    }

    public String getBkFileNameUuid() {
        return bkFileNameUuid;
    }

    public void setBkFileNameUuid(String bkFileNameUuid) {
        this.bkFileNameUuid = bkFileNameUuid;
    }

    public String getBkFileDisplayName() {
        return bkFileDisplayName;
    }

    public void setBkFileDisplayName(String bkFileDisplayName) {
        this.bkFileDisplayName = bkFileDisplayName;
    }

    public String getBkFileDownFolder() {
        return bkFileDownFolder;
    }

    public void setBkFileDownFolder(String bkFileDownFolder) {
        this.bkFileDownFolder = bkFileDownFolder;
    }

    public String getBkAddWhoName() {
        return bkAddWhoName;
    }

    public void setBkAddWhoName(String bkAddWhoName) {
        this.bkAddWhoName = bkAddWhoName;
    }
    
}
