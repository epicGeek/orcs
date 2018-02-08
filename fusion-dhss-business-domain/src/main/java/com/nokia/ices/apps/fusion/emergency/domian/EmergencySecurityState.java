package com.nokia.ices.apps.fusion.emergency.domian;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.nokia.ices.apps.fusion.equipment.domain.EquipmentNe;
import com.nokia.ices.apps.fusion.equipment.domain.EquipmentUnit;
import com.nokia.ices.apps.fusion.nodeswitching.domian.NodeSwitchCase;

@Entity
public class EmergencySecurityState {
	
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * 操作时间
	 */
	private Date operateDate; 
	
	/**
	 * 操作状态（1：已经恢复，2：已经隔离）
	 */
	private Integer operateState; 
	
	/**
	 * 状态（1：隔离，2：恢复）
	 */
	private Integer operate;
	
	/**
	 * 执行人（当前登陆用户）
	 */
	private String operator;
	
	@ManyToOne
	private EquipmentNe ne;
	
	@ManyToOne
	private EquipmentUnit unit;
	
	@ManyToOne
	private NodeSwitchCase nodeCase;
	
	public NodeSwitchCase getNodeCase() {
		return nodeCase;
	}

	public void setNodeCase(NodeSwitchCase nodeCase) {
		this.nodeCase = nodeCase;
	}

	public EquipmentNe getNeEntity(){
		return ne;
	}
	
	public EquipmentUnit getUnitEntity(){
		return unit;
	}
	
	public NodeSwitchCase getNodeCaseEntity(){
		return nodeCase;
	}
	
	
	
	public EquipmentNe getNe() {
		return ne;
	}

	public void setNe(EquipmentNe ne) {
		this.ne = ne;
	}

	public EquipmentUnit getUnit() {
		return unit;
	}

	public void setUnit(EquipmentUnit unit) {
		this.unit = unit;
	}

	/**
	 * 返回信息
	 */
	@Column(length = 1024)
	private String returnInfo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public Integer getOperateState() {
		return operateState;
	}

	public void setOperateState(Integer operateState) {
		this.operateState = operateState;
	}

	public Integer getOperate() {
		return operate;
	}

	public void setOperate(Integer operate) {
		this.operate = operate;
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
