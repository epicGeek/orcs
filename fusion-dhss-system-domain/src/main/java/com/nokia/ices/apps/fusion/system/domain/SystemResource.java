package com.nokia.ices.apps.fusion.system.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class SystemResource  extends AuditableEntity implements Serializable {

    private static final long serialVersionUID = -5699923805520154005L;
    @Id
    @GeneratedValue
    private Long id;

    private Integer resourceOrder;

    private SystemResourceType resourceType;


    @JsonIgnore
    @ManyToMany(mappedBy = "systemResource")
    private Set<SystemRole> systemRole;



    public Integer getResourceOrder() {
        return resourceOrder;
    }

    public void setResourceOrder(Integer resourceOrder) {
        this.resourceOrder = resourceOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<SystemRole> getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(Set<SystemRole> systemRole) {
        this.systemRole = systemRole;
    }

    public SystemResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(SystemResourceType resourceType) {
        this.resourceType = resourceType;
    }

}
