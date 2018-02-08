package com.nokia.ices.apps.fusion.equipment.domain;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentNeType;
import com.nokia.ices.apps.fusion.system.domain.AuditableEntity;
import com.nokia.ices.core.utils.Collections3;

@Entity
public class EquipmentNe extends AuditableEntity {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(unique=true,nullable=false)
    private String neName;
    
    private String dhssName;
    
    private String location;
    
    @Enumerated(EnumType.STRING)
	private EquipmentNeType neType;
    

    @Column(length=100)
    private String neCode;
    @Column(length=100)
    private String cnum;
    
    
    private String remarks;
    
    private String neIdsVersion;
    
    private String neSwVersion;


	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getNeIdsVersion() {
		return neIdsVersion;
	}

	public void setNeIdsVersion(String neIdsVersion) {
		this.neIdsVersion = neIdsVersion;
	}

	public String getNeSwVersion() {
		return neSwVersion;
	}

	public void setNeSwVersion(String neSwVersion) {
		this.neSwVersion = neSwVersion;
	}

	@ManyToMany
	@JsonIgnore
	private Set<EquipmentNumberSection> numberSection = new HashSet<EquipmentNumberSection>();
    

    public Set<EquipmentNumberSection> getNumberSection() {
		return numberSection;
	}

	public void setNumberSection(Set<EquipmentNumberSection> numberSection) {
		this.numberSection = numberSection;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

   

	public EquipmentNeType getNeType() {
		return neType;
	}

	public void setNeType(EquipmentNeType neType) {
		this.neType = neType;
	}

	public String getNeName() {
        return neName;
    }

    public void setNeName(String neName) {
        this.neName = neName;
    }

    public String getDhssName() {
        return dhssName;
    }

    public void setDhssName(String dhssName) {
        this.dhssName = dhssName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    public Long getNeId() {
        return getId();
    }
    
    public String getSearchField(){
    	
    	return getNeType()+"*"+getNeName()+"*"+getLocation();
    }

    
	public String getNeCode() {
		return neCode;
	}

	public void setNeCode(String neCode) {
		this.neCode = neCode;
	}

	public String getCnum() {
		return cnum;
	}

	public void setCnum(String cnum) {
		this.cnum = cnum;
	}
    
    public String getNumberSections(){
		if(Collections3.isEmpty(numberSection)){
			return "";
		}
		String result = "";
		for (EquipmentNumberSection EquipmentNumberSection : numberSection) {
			result += " NUM " + EquipmentNumberSection.getNumber();
		}
		return result;
	}
}
