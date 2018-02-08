package com.nokia.ices.apps.fusion.node.isolation.mbmodel;


import java.io.Serializable;


/**
 * 执行步骤
 * 
 * @author kingroc_zhang
 *
 */
@SuppressWarnings("serial")
public class StepExecute implements Serializable {
	
	private Long id;

	private EmergencySecurityState emergencySecurityState;
	
	private StepConfTable stepConfTable;
	
	private int executeState;// 0:待执行；1：已执行

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EmergencySecurityState getEmergencySecurityState() {
		return emergencySecurityState;
	}

	public void setEmergencySecurityState(
			EmergencySecurityState emergencySecurityState) {
		this.emergencySecurityState = emergencySecurityState;
	}

	public StepConfTable getStepConfTable() {
		return stepConfTable;
	}

	public void setStepConfTable(StepConfTable stepConfTable) {
		this.stepConfTable = stepConfTable;
	}

	public int getExecuteState() {
		return executeState;
	}

	public void setExecuteState(int executeState) {
		this.executeState = executeState;
	}


}
