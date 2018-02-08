package com.nokia.ices.apps.fusion.node.switching.mbmodel;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class NodeSwitch implements Serializable {

	private Integer id;
	private String siteName;
	private String neName;
	private String confTypeName;
	private String caseName;
	private String operator;
	private Date operateDate;
	private String returnInfo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getNeName() {
		return neName;
	}

	public void setNeName(String neName) {
		this.neName = neName;
	}

	public String getConfTypeName() {
		return confTypeName;
	}

	public void setConfTypeName(String confTypeName) {
		this.confTypeName = confTypeName;
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getReturnInfo() {
		return returnInfo;
	}

	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}

}
