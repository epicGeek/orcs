package com.nokia.ices.apps.fusion.ems.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by quyidong on 16/7/25.
 */
@Entity
public class EquipmentNodeGroup {
    @Id
    @GeneratedValue
    private Long id;
    private String groupName;
    private Date createDate;
    /*
     * 设备组对应的单元ID
     */
    private String unitIdCol;
	public String getUnitIdCol() {
		return unitIdCol;
	}
	public void setUnitIdCol(String unitIdCol) {
		this.unitIdCol = unitIdCol;
	}
	public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
}
