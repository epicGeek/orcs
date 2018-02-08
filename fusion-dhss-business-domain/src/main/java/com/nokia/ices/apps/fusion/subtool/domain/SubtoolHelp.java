package com.nokia.ices.apps.fusion.subtool.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SubtoolHelp implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	
	private String code;
	
	private String helpContext;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getHelpContext() {
		return helpContext;
	}

	public void setHelpContext(String helpContext) {
		this.helpContext = helpContext;
	}
	
	
	
	
	
	
	
	
	
	

}
