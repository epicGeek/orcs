package com.nokia.ices.apps.fusion.system.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;


@Entity
@PrimaryKeyJoinColumn(name = "resource_id")
public class SystemArea extends SystemResource {
    /**
     * 
     */
    private static final long serialVersionUID = -9214991057206220379L;
    @Column(unique=true,nullable=false)
    private String areaName;
    @Column(unique=true,nullable=false)
    private String areaCode;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    
    public Long getEntityId(){
    	return getId();
    }
}
