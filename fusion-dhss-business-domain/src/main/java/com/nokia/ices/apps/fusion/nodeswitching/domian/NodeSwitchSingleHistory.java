package com.nokia.ices.apps.fusion.nodeswitching.domian;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class NodeSwitchSingleHistory {
	
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * 站点名称
	 */
	private String location;
	
	/**
	 * 网元名称
	 */
	private String neName; 
	
	/**
	 *板卡名称
	 */
	private String unitName;
	
	/**
	 * 配置类型
	 */
	private String confTypeName;
	
	
	/**
	 * 执行时间
	 */
	private Date operateDate; 
	
	/**
	 * 执行描述
	 */
	private String operatorDesc;
	
	/**
	 * 执行结果信息
	 */
	@Column(length = 1024)
	private String returnInfo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNeName() {
		return neName;
	}

	public void setNeName(String neName) {
		this.neName = neName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getConfTypeName() {
		return confTypeName;
	}

	public void setConfTypeName(String confTypeName) {
		this.confTypeName = confTypeName;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public String getOperatorDesc() {
		return operatorDesc;
	}

	public void setOperatorDesc(String operatorDesc) {
		this.operatorDesc = operatorDesc;
	}

	public String getReturnInfo() {
		return returnInfo;
	}

	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}
}
