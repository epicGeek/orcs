package com.nokia.ices.apps.fusion.quota.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class QuotaMonitorHistory {

	@Id
	@GeneratedValue
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private String flag;

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
	private String unitName;
	private String unitNext;
	/**
	 * 次级单位ID
	 */
	private String unitNextId;
	/**
	 * 单元次级的次级单位
	 */
	private String unitNextNext;
	private String unitNextNextId;
	/**
	 * /** 网管上的单元ID
	 */
	private String unitId;

	/**
	 * 网元类型
	 */
	private String neType;
	private String unitType;

	/**
	 * KPI名称
	 */
	private String kpiName;

	/**
	 * kpi001-kpi011
	 */
	private String kpiCode;
	/**
	 * KPI值
	 */
	private Double kpiValue;

	/**
	 * KPI成功次数
	 */
	private Integer kpiSuccessCount;

	/**
	 * KPI请求次数
	 */
	private Integer kpiRequestCount;

	/**
	 * 周期，当前取KPI时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Date periodStartTime;

	/**
	 * 场景（语单,短信，鉴权，位置）
	 */
	private String scene;

	/**
	 * '单位(个，%，次)
	 */
	private String kpiUnit;
	private String nodeName;
	private String dhssName;
	private Integer kpiFailCount;

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

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

	public String getUnitNextNext() {
		return unitNextNext;
	}

	public void setUnitNextNext(String unitNextNext) {
		this.unitNextNext = unitNextNext;
	}

	public String getUnitNextNextId() {
		return unitNextNextId;
	}

	public void setUnitNextNextId(String unitNextNextId) {
		this.unitNextNextId = unitNextNextId;
	}

	public Integer getKpiFailCount() {
		return kpiFailCount;
	}

	public void setKpiFailCount(Integer kpiFailCount) {
		this.kpiFailCount = kpiFailCount;
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

	public String getKpiCode() {
		return kpiCode;
	}

	public void setKpiCode(String kpiCode) {
		this.kpiCode = kpiCode;
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

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Integer getKpiSuccessCount() {
		return kpiSuccessCount;
	}

	public void setKpiSuccessCount(Integer kpiSuccessCount) {
		this.kpiSuccessCount = kpiSuccessCount;
	}

	public Integer getKpiRequestCount() {
		return kpiRequestCount;
	}

	public void setKpiRequestCount(Integer kpiRequestCount) {
		this.kpiRequestCount = kpiRequestCount;
	}
}
