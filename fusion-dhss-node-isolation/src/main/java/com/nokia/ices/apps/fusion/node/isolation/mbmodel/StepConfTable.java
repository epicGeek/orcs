package com.nokia.ices.apps.fusion.node.isolation.mbmodel;


import java.io.Serializable;


//import com.nsn.ices.apps.dhlr.model.NeType;

/**
 * 指令执行步骤配置表
 * 
 * @author kingroc_zhang
 *
 */
@SuppressWarnings("serial")
public class StepConfTable implements Serializable {

	private Long id;

	private int stepSeq;// 步骤顺序
	private String stepExplain;// 步骤说明
	private String stepDescribe;// 步骤描述
	private String stepCommand;// 步骤命令
//	private NeType neType;// 网元类型
	private int confType;// 配置类型 1:节点隔离、2:节点恢复、3:板卡倒换、4：网元隔离、5：网元恢复、6：一键备份

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getStepSeq() {
		return stepSeq;
	}

	public void setStepSeq(int stepSeq) {
		this.stepSeq = stepSeq;
	}

	public String getStepExplain() {
		return stepExplain;
	}

	public void setStepExplain(String stepExplain) {
		this.stepExplain = stepExplain;
	}

	public String getStepDescribe() {
		return stepDescribe;
	}

	public void setStepDescribe(String stepDescribe) {
		this.stepDescribe = stepDescribe;
	}

	public String getStepCommand() {
		return stepCommand;
	}

	public void setStepCommand(String stepCommand) {
		this.stepCommand = stepCommand;
	}



	public int getConfType() {
		return confType;
	}

	public void setConfType(int confType) {
		this.confType = confType;
	}

}
