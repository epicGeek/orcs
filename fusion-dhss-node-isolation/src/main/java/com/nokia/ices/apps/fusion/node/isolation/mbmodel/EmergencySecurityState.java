package com.nokia.ices.apps.fusion.node.isolation.mbmodel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 应急保障状态表
 * 
 * @author kingroc_zhang
 *
 */
@SuppressWarnings("serial")
public class EmergencySecurityState implements Serializable {

	private Long id;
	/**
	 * 操作状态 初始状态(未做过隔离或者恢复)：0、 1：节点隔离、2：节点恢复、4:网元隔离、5:网元恢复
	 */
	private int operateState;
	/**
	 * 1:对网元操作 2:对节点操作
	 */
	private int operate;
	/**
	 * 网元ID
	 */
	private int neId;
	/**
	 * 网元名称
	 */
	private String neName;
	/**
	 * 节点ID
	 */
	private int unitId;
	/**
	 * 节点名称
	 */
	private String unitName;
	/**
	 * 操作人
	 */
	private String operator;
	/**
	 * 操作时间
	 */
	private Date operateDate;
	/**
	 * 返回执行操作的信息
	 */
	// @Lob
	// private Object returnInfo;
	private String returnInfo;

	private List<StepExecute> stepExecuteList = new ArrayList<StepExecute>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getOperateState() {
		return operateState;
	}

	public void setOperateState(int operateState) {
		this.operateState = operateState;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
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

	public List<StepExecute> getStepExecuteList() {
		return stepExecuteList;
	}

	public void setStepExecuteList(List<StepExecute> stepExecuteList) {
		this.stepExecuteList = stepExecuteList;
	}

	public String getReturnInfo() {
		return returnInfo;
	}

	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}

	public int getNeId() {
		return neId;
	}

	public void setNeId(int neId) {
		this.neId = neId;
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

	public int getOperate() {
		return operate;
	}

	public void setOperate(int operate) {
		this.operate = operate;
	}

}
