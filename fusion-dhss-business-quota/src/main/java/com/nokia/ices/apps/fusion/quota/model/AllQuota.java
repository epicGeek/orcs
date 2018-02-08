package com.nokia.ices.apps.fusion.quota.model;

public class AllQuota{
//"网元ID", "网元名称", "指标名称","指标周期","成功率","单位",ttil,"请求次数"
	private String unitName;
	private String neName;
	private String kpiname;
	private String period;
	private String kpivalue;
	private String kipUnit;
	private String kpifailcount;
	private String kpirequestcount;
	
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getNeName() {
		return neName;
	}
	public void setNeName(String neName) {
		this.neName = neName;
	}
	public String getKpiname() {
		return kpiname;
	}
	public void setKpiname(String kpiname) {
		this.kpiname = kpiname;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getKpivalue() {
		return kpivalue;
	}
	public void setKpivalue(String kpivalue) {
		this.kpivalue = kpivalue;
	}
	public String getKpifailcount() {
		return kpifailcount;
	}
	public void setKpifailcount(String kpifailcount) {
		this.kpifailcount = kpifailcount;
	}
	public String getKipUnit() {
		return kipUnit;
	}
	public void setKipUnit(String kipUnit) {
		this.kipUnit = kipUnit;
	}
	public String getKpirequestcount() {
		return kpirequestcount;
	}
	public void setKpirequestcount(String kpirequestcount) {
		this.kpirequestcount = kpirequestcount;
	}
	
	
	
	
	
}
