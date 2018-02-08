package com.nokia.ices.apps.fusion.nodeswitching.domian;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class NodeSwitchStepExecute {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private NodeSwitch nodeSwitch;
	
	@ManyToOne
	private NodeSwitchCmd  nodeSwitchCmd;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NodeSwitch getNodeSwitch() {
		return nodeSwitch;
	}

	public void setNodeSwitch(NodeSwitch nodeSwitch) {
		this.nodeSwitch = nodeSwitch;
	}

	public NodeSwitchCmd getNodeSwitchCmd() {
		return nodeSwitchCmd;
	}

	public void setNodeSwitchCmd(NodeSwitchCmd nodeSwitchCmd) {
		this.nodeSwitchCmd = nodeSwitchCmd;
	}
	

}
