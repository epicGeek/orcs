package com.nokia.ices.apps.fusion.system.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class SystemRole extends AuditableEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1671465649939159756L;
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    private String roleName;

    private String roleDesc;
    private String path;
    private String notifiType;
    private String notifi;
    @ManyToMany
    private Set<SystemUser> systemUser;

    @ManyToMany
    private Set<SystemResource> systemResource;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<SystemUser> getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(Set<SystemUser> systemUser) {
        this.systemUser = systemUser;
    }

    public Set<SystemResource> getSystemResource() {
        return systemResource;
    }

    public void setSystemResource(Set<SystemResource> systemResource) {
        this.systemResource = systemResource;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

	public String getNotifiType() {
		return notifiType;
	}

	public void setNotifiType(String notifiType) {
		this.notifiType = notifiType;
	}

	public String getNotifi() {
		return notifi;
	}

	public void setNotifi(String notifi) {
		this.notifi = notifi;
	}
	
	public Long getRoleId(){
		return this.getId();
	}
    
}
