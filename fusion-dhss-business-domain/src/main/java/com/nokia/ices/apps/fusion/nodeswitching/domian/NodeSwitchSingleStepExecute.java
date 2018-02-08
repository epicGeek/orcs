package com.nokia.ices.apps.fusion.nodeswitching.domian;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class NodeSwitchSingleStepExecute {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private NodeSwitchSingleCmd nssc;
	
	@ManyToOne
	private NodeSwitchSingleHistory nssh;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NodeSwitchSingleCmd getNssc() {
		return nssc;
	}

	public void setNssc(NodeSwitchSingleCmd nssc) {
		this.nssc = nssc;
	}

	public NodeSwitchSingleHistory getNssh() {
		return nssh;
	}

	public void setNssh(NodeSwitchSingleHistory nssh) {
		this.nssh = nssh;
	}

}
