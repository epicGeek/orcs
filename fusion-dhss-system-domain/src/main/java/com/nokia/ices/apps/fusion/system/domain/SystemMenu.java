package com.nokia.ices.apps.fusion.system.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "resource_id")
public class SystemMenu extends SystemResource {
    /**
     * 
     */
    private static final long serialVersionUID = -9214991057206220379L;
    @Column(unique = true, nullable = false)
    private String menuName;
    @Column(unique = true, nullable = false)
    private String menuCode;

    private String parentMenuName;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getParentMenuName() {
        return parentMenuName;
    }
    public void setParentMenuName(String parentMenuName) {
        this.parentMenuName = parentMenuName;
    }
    
    public Long getEntityId(){
    	return getId();
    }
    

}
