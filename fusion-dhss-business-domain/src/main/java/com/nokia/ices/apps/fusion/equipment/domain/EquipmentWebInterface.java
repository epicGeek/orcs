package com.nokia.ices.apps.fusion.equipment.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentWebInterfaceType;


@Entity
public class EquipmentWebInterface {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private EquipmentWebInterfaceType interfaceType;

    @Transient
	private boolean checked;
    
    @ManyToOne
    private EquipmentUnit unit;

	

	public EquipmentUnit getUnit() {
		return unit;
	}

	public void setUnit(EquipmentUnit unit) {
		this.unit = unit;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	private String url;

    private String userName;
    
    private String password;

    @Column(length = 4000)
    private String loginScript;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


	public EquipmentWebInterfaceType getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(EquipmentWebInterfaceType interfaceType) {
        this.interfaceType = interfaceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginScript() {
        return loginScript;
    }

    public void setLoginScript(String loginScript) {
        this.loginScript = loginScript;
    }
    public Long getEquipmentWebInterfaceId(){
    	return getId();
    }
}
