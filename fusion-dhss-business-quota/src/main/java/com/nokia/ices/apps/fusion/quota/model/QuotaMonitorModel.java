package com.nokia.ices.apps.fusion.quota.model;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class QuotaMonitorModel {
	
	private Long id;
	
	/**
	 * 网元ID
	 */
	private Long neId;
	
	/**
	 * 网元名称
	 */
	private String neName;
	
	/**
	 * 单元ID
	 */
	private Long unitId;
	
	/**
	 * 单元名称
	 */
	private String unitName;
	
	/**
	 * 网管上的单元ID
	 */
	private String unitNode;
	
	/**
	 * 网元类型
	 */
	private String neType;
	
	/**
	 * KPI名称
	 */
	private String kpiName;
	
	/**
	 * KPI值
	 */
	private Double kpiValue;
	
	/**
	 * KPI失败次数
	 */
	private Double kpiFailCount;
	
	/**
	 * KPI请求次数
	 */
	private Double kpiRequestCount;
	
	/**
	 * 周期，当前取KPI时间
	 */
	private Date period;
	
	/**
	 * 场景（语单,短信，鉴权，位置）
	 */
	private String scene;
	
	/**
	 * '单位(个，%，次)
	 */
	private String kpiUnit;
    
//	private String threshold;  门限值
	public Long getId() {
		return id;
	}

	/*public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}*/

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNeId() {
		return neId;
	}

	public void setNeId(long neId) {
		this.neId = neId;
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

	public String getUnitNode() {
		return unitNode;
	}

	public void setUnitNode(String unitNode) {
		this.unitNode = unitNode;
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

	public Double getKpiFailCount() {
		return kpiFailCount;
	}

	public void setKpiFailCount(Double kpiFailCount) {
		this.kpiFailCount = kpiFailCount;
	}

	public Double getKpiRequestCount() {
		return kpiRequestCount;
	}

	public void setKpiRequestCount(Double kpiRequestCount) {
		this.kpiRequestCount = kpiRequestCount;
	}

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public String getKpiUnit() {
		return kpiUnit;
	}

	public void setKpiUnit(String kpiUnit) {
		this.kpiUnit = kpiUnit;
	}


	

}

	