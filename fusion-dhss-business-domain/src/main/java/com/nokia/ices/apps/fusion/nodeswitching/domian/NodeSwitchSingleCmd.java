package com.nokia.ices.apps.fusion.nodeswitching.domian;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class NodeSwitchSingleCmd {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String neType;
	
	private String confType;
	
	private String script;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNeType() {
		return neType;
	}

	public void setNeType(String neType) {
		this.neType = neType;
	}

	public String getConfType() {
		return confType;
	}

	public void setConfType(String confType) {
		this.confType = confType;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public Integer getStepSep() {
		return stepSep;
	}

	public void setStepSep(Integer stepSep) {
		this.stepSep = stepSep;
	}

	public String getStepDesc() {
		return stepDesc;
	}

	public void setStepDesc(String stepDesc) {
		this.stepDesc = stepDesc;
	}

	public String getStepCmd() {
		return stepCmd;
	}

	public void setStepCmd(String stepCmd) {
		this.stepCmd = stepCmd;
	}

	private Integer stepSep;
	
	private String stepDesc;
	
	private String stepCmd;

}
