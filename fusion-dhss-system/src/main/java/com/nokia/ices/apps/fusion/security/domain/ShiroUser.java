package com.nokia.ices.apps.fusion.security.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import com.google.common.base.Objects;

public class ShiroUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long userId;
	private String userName;
	private String realName;

	private Long roleId;

	private String roleName;
	private String roleDesc;
	private String selfLink;

	private String errorCode;
	
	private boolean delayPass;

	
	public boolean isDelayPass() {
		return delayPass;
	}

	public void setDelayPass(boolean delayPass) {
		this.delayPass = delayPass;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	private String roleCreatorPath;
	private String remoteAddress;
	private String token;

	// private Collection<SystemRole> role;
	private Collection<Long> resource;

	//
	// public Collection<SystemRole> getRole() {
	// return role;
	// }
	//
	// public void setRole(Collection<SystemRole> role) {
	// this.role = role;
	// }
	//
	// public Collection<Long> getResourceID() {
	// if(resource==null){
	// System.out.println("为空=");
	// resource = new ArrayList<Long>();
	// }
	// return resource;
	// }
	//
	// public void setResource(Collection<Long> resource) {
	// this.resource = resource;
	// }


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getRoleCreatorPath() {
		return roleCreatorPath;
	}

	public void setRoleCreatorPath(String roleCreatorPath) {
		this.roleCreatorPath = roleCreatorPath;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public String getSelfLink() {
		return selfLink;
	}

	public void setSelfLink(String selfLink) {
		this.selfLink = selfLink;
	}

	/**
	 * 本函数输出将作为默认的<shiro:principal/>输出.
	 */
	@Override
	public String toString() {
		return this.userName;
	}

	/**
	 * 重载hashCode,只计算loginName;
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(userName);
	}

	/**
	 * 重载equals,只计算loginName;
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getUserName() == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ShiroUser other = (ShiroUser) obj;

		if (other.getUserName() == null) {
			return false;
		}
		if (!getUserName().equals(other.getUserName())) {
			return false;
		}
		return true;
	}
}
