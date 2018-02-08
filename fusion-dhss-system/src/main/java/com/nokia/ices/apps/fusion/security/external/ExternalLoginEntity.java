package com.nokia.ices.apps.fusion.security.external;

import java.io.Serializable;

public class ExternalLoginEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5624299502587122492L;
	/**
	 * 
	 */
	private String sessionId;
	private int status = 0;
	private String token;
	private String username;
	private String role;
	private String message;
	
	private String displayName;
	private String moduleName;
	private Long expireTime;
	
	public static final Long expireInterval = 30 * 60 * 1000l;
	
	public ExternalLoginEntity() {
		super();
		this.expireTime = System.currentTimeMillis() + expireInterval;
	}

	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Long getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}
	
}
