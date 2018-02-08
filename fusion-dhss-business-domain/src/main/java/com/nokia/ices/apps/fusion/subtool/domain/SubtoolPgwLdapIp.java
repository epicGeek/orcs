package com.nokia.ices.apps.fusion.subtool.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class SubtoolPgwLdapIp implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	private String pgwIp;

	private String ldapIp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPgwIp() {
		return pgwIp;
	}

	public void setPgwIp(String pgwIp) {
		this.pgwIp = pgwIp;
	}

	public String getLdapIp() {
		return ldapIp;
	}

	public void setLdapIp(String ldapIp) {
		this.ldapIp = ldapIp;
	}
	
    
}
