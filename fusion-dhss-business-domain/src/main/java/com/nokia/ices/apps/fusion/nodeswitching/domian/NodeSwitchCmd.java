package com.nokia.ices.apps.fusion.nodeswitching.domian;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class NodeSwitchCmd {
	
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * 站点名称
	 */
	private String siteName;
	
	/**
	 * 网元类型
	 */
	private String neType;
	
	
	/**
	 * 执行机执行脚本名称
	 */
	private String script;
	
	/**
	 * 网元名称
	 */
	private String neName;
	
	
	/**
	 * 单元名称
	 */
	private String unitName;
	
	private Integer caseId;
	
	
	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	/**
	 * 场景ID
	 */
	@ManyToOne
	private NodeSwitchCase nodeCase;
	
	
	
	public NodeSwitchCase getNodeCase() {
		return nodeCase;
	}

	public void setNodeCase(NodeSwitchCase nodeCase) {
		this.nodeCase = nodeCase;
	}

	/**
	 * 配置类型
	 */
	private Integer confType;
	
	
	/**
	 * 执行顺序
	 */
	private Integer stepSeq;
	
	/**
	 * 执行描述
	 */
	private String stepDesc;
	
	/**
	 * 执行命令
	 */
	private String stepCmd;
	
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

	public String getNeType() {
		return neType;
	}

	public void setNeType(String neType) {
		this.neType = neType;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
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


	public Integer getConfType() {
		return confType;
	}

	public void setConfType(Integer confType) {
		this.confType = confType;
	}

	public Integer getStepSeq() {
		return stepSeq;
	}

	public void setStepSeq(Integer stepSeq) {
		this.stepSeq = stepSeq;
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

}
