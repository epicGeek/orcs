package com.nokia.ices.apps.fusion.equipment.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"searchField","neStartTime", "neEndTime"})
@Entity
public class EquipmentNeOperationLog  implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	private String neType;
	private String neName;
	private String unitType;
	private String unitName;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale="zh", timezone="GMT+8")
	private Date giveTime;
	@Transient
	private Date neStartTime;
	@Transient
	private Date neEndTime;
    @Transient
	private String searchField;
	
	private String path;
	

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Date getNeEndTime() {
		return neEndTime;
	}
	public void setNeEndTime(Date neEndTime) {
		this.neEndTime = neEndTime;
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
	public String getNeName() {
		return neName;
	}
	public void setNeName(String neName) {
		this.neName = neName;
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
	public Date getGiveTime() {
		return giveTime;
	}
	public void setGiveTime(Date giveTime) {
		this.giveTime = giveTime;
	}
	public String getSearchField() {
		return searchField;
	}
	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}
	public Date getNeStartTime() {
        return neStartTime;
    }
    public void setNeStartTime(Date neStartTime) {
        this.neStartTime = neStartTime;
    }
	/*@Override
	public String toString() {
		return "NeOperationLog [id=" + id + ", neType=" + neType + ", neName="
				+ neName + ", unitType=" + unitType + ", unitName=" + unitName
				+ ", giveTime=" + giveTime + ", neStratTime=" + neStratTime
				+ ", neEndTime=" + neEndTime + ", searchField=" + searchField
				+ ", path=" + path + "]";
	}*/
	
	
	
}
