package com.nokia.ices.apps.fusion.quota.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class QuotaMonitorHistoryExportModel {

	/**
	 * 设备名称
	 */	
    private String dhssName;
    /**
     * 网元名称
     */
    private String neName;
    /**
     * 单元ID
     */
    private Long neId;
    /**
     * 单元名称
     */
    /**
     * 网元类型
     */
    private String neType;
    private String unitName;
    /**
    /**
     * 网管上的单元ID
     */
    private String unitId;
    
    private String unitNext;
	/**
	 * 次级单位ID
	 */
	private String unitNextId;
	
    /**
     * 局址
     */
    private String nodeName;
    
    
    /**
     * KPI名称
     */
    private String kpiName;
    /**
     * KPI值
     */
    private Double kpiValue;
    /**
     * 周期，当前取KPI时间
     */
    
	private String kpiUnit;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", locale="zh", timezone="GMT+8")
    private Date periodStartTime;

    public String getUnitNext() {
		return unitNext;
	}

	public void setUnitNext(String unitNext) {
		this.unitNext = unitNext;
	}

	public String getUnitNextId() {
		return unitNextId;
	}

	public void setUnitNextId(String unitNextId) {
		this.unitNextId = unitNextId;
	}

 

    public String getDhssName() {
        return dhssName;
    }

    public void setDhssName(String dhssName) {
        this.dhssName = dhssName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNeName() {
        return neName;
    }

    public void setNeName(String neName) {
        this.neName = neName;
    }

    public Long getNeId() {
        return neId;
    }

    public void setNeId(Long neId) {
        this.neId = neId;
    }
   
    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getNeType() {
        return neType;
    }

    public void setNeType(String neType) {
        this.neType = neType;
    }


    public String getKpiName() {
        return kpiName;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }


    public Double getKpiValue() {
        return kpiValue;
    }

    public void setKpiValue(Double kpiValue) {
        this.kpiValue = kpiValue;
    }

    public Date getPeriodStartTime() {
        return periodStartTime;
    }

    public void setPeriodStartTime(Date periodStartTime) {
        this.periodStartTime = periodStartTime;
    }

    public String getKpiUnit() {
        return kpiUnit;
    }

    public void setKpiUnit(String kpiUnit) {
        this.kpiUnit = kpiUnit;
    }


 
}
