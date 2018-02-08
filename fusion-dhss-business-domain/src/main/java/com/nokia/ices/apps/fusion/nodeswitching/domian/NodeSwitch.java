package com.nokia.ices.apps.fusion.nodeswitching.domian;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class NodeSwitch {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String siteName;
	
	private String neName;
	
	private String confTypeName;
	
	private String caseName;
	
	private Date operateDate;
	
	private String operator; 
	
	@Column(length = 1024)
	private String returnInfo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getReturnInfo() {
		return returnInfo;
	}

	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	} 

}
