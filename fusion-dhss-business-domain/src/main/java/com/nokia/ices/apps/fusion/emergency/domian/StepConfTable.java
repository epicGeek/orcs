package com.nokia.ices.apps.fusion.emergency.domian;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class StepConfTable {

	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * 命令类型（6：一键备份，1：节点隔离，2：节点恢复）
	 */
	private Integer confType;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getConfType() {
		return confType;
	}

	public void setConfType(Integer confType) {
		this.confType = confType;
	}

	public String getStepCommand() {
		return stepCommand;
	}

	public void setStepCommand(String stepCommand) {
		this.stepCommand = stepCommand;
	}

	public String getStepDescribe() {
		return stepDescribe;
	}

	public void setStepDescribe(String stepDescribe) {
		this.stepDescribe = stepDescribe;
	}

	public String getStepExplain() {
		return stepExplain;
	}

	public void setStepExplain(String stepExplain) {
		this.stepExplain = stepExplain;
	}

	public Integer getStepSeq() {
		return stepSeq;
	}

	public void setStepSeq(Integer stepSeq) {
		this.stepSeq = stepSeq;
	}

	public String getNeType() {
		return neType;
	}

	public void setNeType(String neType) {
		this.neType = neType;
	}

	/**
	 * 要下发的指令
	 */
	private String stepCommand;
	
	/**
	 * 指令描述
	 */
	private String stepDescribe; 
	
	/**
	 * 指令说明
	 */
	private String stepExplain;
	
	/**
	 * 步骤顺序
	 */
	private Integer stepSeq;
	
	/**
	 * 网元类型
	 */
	private String neType; 
}
